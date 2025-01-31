package ru.endlesscode.mimic

import org.bukkit.entity.Player
import org.bukkit.plugin.Plugin
import org.bukkit.plugin.ServicePriority
import org.bukkit.plugin.ServicesManager
import ru.endlesscode.mimic.bukkit.loadAll
import ru.endlesscode.mimic.bukkit.register
import ru.endlesscode.mimic.classes.BukkitClassSystem
import ru.endlesscode.mimic.classes.WrappedClassSystemProvider
import ru.endlesscode.mimic.config.MimicConfig
import ru.endlesscode.mimic.internal.Log
import ru.endlesscode.mimic.items.BukkitItemsRegistry
import ru.endlesscode.mimic.items.WrappedItemsRegistry
import ru.endlesscode.mimic.level.BukkitLevelSystem
import ru.endlesscode.mimic.level.WrappedLevelSystemProvider
import kotlin.reflect.KClass

internal class MimicImpl(
    private val servicesManager: ServicesManager,
    private val config: MimicConfig,
) : Mimic {

    override fun registerClassSystem(
        provider: BukkitClassSystem.Provider,
        apiLevel: Int,
        plugin: Plugin,
        priority: ServicePriority,
    ): BukkitClassSystem.Provider? = tryRegisterService<BukkitClassSystem.Provider>(apiLevel, plugin, priority) {
        WrappedClassSystemProvider(provider, plugin)
    }

    override fun getClassSystem(player: Player): BukkitClassSystem = getClassSystemProvider().getSystem(player)
    override fun getClassSystemProvider(): BukkitClassSystem.Provider = loadService(config.classSystem)
    override fun getAllClassSystemProviders(): Map<String, BukkitClassSystem.Provider> = loadAllServices()

    override fun registerItemsRegistry(
        registry: BukkitItemsRegistry,
        apiLevel: Int,
        plugin: Plugin,
        priority: ServicePriority,
    ): BukkitItemsRegistry? = tryRegisterService<BukkitItemsRegistry>(apiLevel, plugin, priority) {
        WrappedItemsRegistry(registry, config, plugin)
    }

    override fun getItemsRegistry(): BukkitItemsRegistry = loadService()
    override fun getAllItemsRegistries(): Map<String, BukkitItemsRegistry> = loadAllServices()

    override fun registerLevelSystem(
        provider: BukkitLevelSystem.Provider,
        apiLevel: Int,
        plugin: Plugin,
        priority: ServicePriority,
    ): BukkitLevelSystem.Provider? = tryRegisterService<BukkitLevelSystem.Provider>(apiLevel, plugin, priority) {
        WrappedLevelSystemProvider(provider, plugin)
    }

    override fun getLevelSystem(player: Player): BukkitLevelSystem = getLevelSystemProvider().getSystem(player)
    override fun getLevelSystemProvider(): BukkitLevelSystem.Provider = loadService(config.levelSystem)
    override fun getAllLevelSystemProviders(): Map<String, BukkitLevelSystem.Provider> = loadAllServices()

    private inline fun <reified Service : MimicService> tryRegisterService(
        apiLevel: Int,
        plugin: Plugin,
        priority: ServicePriority,
        crossinline createService: () -> Service,
    ): Service? = runCatching {
        // Check if service can be registered
        val apiName = Service::class.getApiName()
        checkApiLevel(apiLevel, apiName, plugin)

        // Register service
        val service = createService()
        servicesManager.register(service, plugin, priority)
        Log.i("Successfully registered $apiName '${service.id}' by $plugin")

        // Return the registered service or null if service is not registered
        service
    }.onFailure(Log::w).getOrNull()

    private fun checkApiLevel(apiLevel: Int, apiName: String, plugin: Plugin) {
        if (!MimicApiLevel.checkApiLevel(apiLevel)) {
            error(
                """
                Can not register $apiName implemented by $plugin,
                because Mimic version installed on the server is lower than minimal required version.
                You can get the latest Mimic from GitHub: https://github.com/EndlessCodeGroup/Mimic/releases
                """.trimIndent()
            )
        }
    }

    private inline fun <reified T : MimicService> loadService(preferred: String = ""): T {
        val services = loadAllServices<T>().filterValues { it.isEnabled }
        val defaultService = services.values.firstOrNull()
        checkNotNull(defaultService) {
            """
            ${T::class.getApiName()} should always have default implementation.
            Please file an issue on GitHub: https://github.com/EndlessCodeGroup/Mimic/issues
            """.trimIndent()
        }

        return when {
            preferred.isEmpty() -> defaultService
            preferred in services -> services.getValue(preferred)

            else -> {
                Log.w("${T::class.getApiName()} with id '$preferred' not found, will be used '${defaultService.id}' instead.")
                Log.w("Please specify any of known implementations: ${services.keys}.")
                defaultService
            }
        }
    }

    private inline fun <reified T : MimicService> loadAllServices(): Map<String, T> {
        return servicesManager.loadAll<T>().associateBy { it.id }
    }

    private fun KClass<out MimicService>.getApiName(): String = when (this) {
        BukkitClassSystem.Provider::class -> "ClassSystem"
        BukkitLevelSystem.Provider::class -> "LevelSystem"
        BukkitItemsRegistry::class -> "ItemsRegistry"
        else -> error("Unknown service: ${this.java.name}")
    }
}
