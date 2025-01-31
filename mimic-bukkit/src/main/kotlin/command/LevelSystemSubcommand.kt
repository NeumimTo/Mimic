/*
 * This file is part of BukkitMimic.
 * Copyright (C) 2021 Osip Fatkullin
 * Copyright (C) 2021 EndlessCode Group and contributors
 *
 * BukkitMimic is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * BukkitMimic is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with BukkitMimic.  If not, see <http://www.gnu.org/licenses/>.
 */

package ru.endlesscode.mimic.command

import co.aikar.commands.AbstractCommandManager
import co.aikar.commands.InvalidCommandArgument
import co.aikar.commands.MessageKeys
import co.aikar.commands.MimicCommand
import co.aikar.commands.annotation.*
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import ru.endlesscode.mimic.Mimic
import ru.endlesscode.mimic.internal.Log
import ru.endlesscode.mimic.level.BukkitLevelSystem

@CommandAlias("%command")
@CommandPermission("%perm")
@Subcommand("experience|xp")
internal class LevelSystemSubcommand(
    private val mimic: Mimic
) : MimicCommand() {

    override fun afterRegister(manager: AbstractCommandManager) {
        manager.commandCompletions.registerEnumCompletion<ExtendedValueType>("extype")
        manager.commandCompletions.registerEnumCompletion<ValueType>("type")
    }

    @Subcommand("info")
    @Description("Show information about player's level system")
    @CommandCompletion("@players")
    fun info(sender: CommandSender, @Optional @Flags("other,defaultself") player: Player?) {
        if (player == null) return
        val provider = mimic.getLevelSystemProvider()
        val system = provider.getSystem(player)
        sender.send(
            "&3System: &7${provider.id}",
            "&3Level: &7%.2f".format(system.level + system.fractionalExp),
            "&3Exp: &7%.1f &8| &3To next level: &7%.1f".format(system.exp, system.expToNextLevel),
            "&3Total exp: &7%.1f".format(system.totalExp)
        )
    }

    @Subcommand("set")
    @Description("Set player's level or exp")
    @CommandCompletion("@nothing @extype @players")
    fun set(
        sender: CommandSender,
        amount: Double,
        @Default("lvl") type: ExtendedValueType,
        @Optional @Flags("other,defaultself") player: Player?,
    ) {
        if (player == null) return
        catchUnsupported {
            val system = mimic.getLevelSystem(player)
            @Suppress("DEPRECATION") // Allow using exp setter
            when (type) {
                ExtendedValueType.LVL -> system.level = amount.toInt()
                ExtendedValueType.TOTAL -> system.totalExp = amount
                ExtendedValueType.POINTS -> system.exp = amount
            }
            system.printNewStats(sender)
        }
    }

    @Subcommand("give")
    @Description("Give level or exp to player")
    @CommandCompletion("@nothing @type @players")
    fun give(
        sender: CommandSender,
        amount: Int,
        @Default("lvl") type: ValueType,
        @Optional @Flags("other,defaultself") player: Player?,
    ) {
        if (player == null) return
        catchUnsupported {
            val system = mimic.getLevelSystem(player)
            when (type) {
                ValueType.LVL -> system.giveLevels(amount)
                ValueType.POINTS -> system.giveExp(amount.toDouble())
            }
            system.printNewStats(sender)
        }
    }

    @Subcommand("take")
    @Description("Take level or exp from player")
    @CommandCompletion("@nothing @type @players")
    fun take(
        sender: CommandSender,
        amount: Int,
        @Default("lvl") type: ValueType,
        @Optional @Flags("other,defaultself") player: Player?,
    ) {
        if (player == null) return
        catchUnsupported {
            val system = mimic.getLevelSystem(player)
            when (type) {
                ValueType.LVL -> system.takeLevels(amount)
                ValueType.POINTS -> system.takeExp(amount.toDouble())
            }
            system.printNewStats(sender)
        }
    }

    private inline fun catchUnsupported(block: () -> Unit) {
        try {
            block()
        } catch (e: UnsupportedOperationException) {
            Log.d(e, quiet = true)
            throw InvalidCommandArgument(MessageKeys.ERROR_PREFIX, false, "{message}", e.message.toString())
        }
    }

    private fun BukkitLevelSystem.printNewStats(sender: CommandSender) {
        sender.send("&6New ${player.name}'s stats: $level LVL, %.1f XP".format(exp))
    }

    @Subcommand("has")
    @Description("Check that player did reach level or has exp")
    @CommandCompletion("@nothing @extype @players")
    fun has(
        sender: CommandSender,
        value: Int,
        @Default("lvl") type: ExtendedValueType,
        @Optional @Flags("other,defaultself") player: Player?,
    ) {
        if (player == null) return
        val system = mimic.getLevelSystem(player)
        val has = when (type) {
            ExtendedValueType.LVL -> system.didReachLevel(value)
            ExtendedValueType.POINTS -> system.hasExp(value.toDouble())
            ExtendedValueType.TOTAL -> system.hasExpTotal(value.toDouble())
        }
        sender.send("&6${player.name} has%s $value ${type.stringValue}."
            .format(if (has) "" else " not"))
    }

    internal enum class ValueType { LVL, POINTS }

    internal enum class ExtendedValueType(val stringValue: String) {
        LVL("level"),
        POINTS("points"),
        TOTAL("total experience")
    }
}
