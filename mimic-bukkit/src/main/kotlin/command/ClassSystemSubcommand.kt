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
import co.aikar.commands.MimicCommand
import co.aikar.commands.annotation.*
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import ru.endlesscode.mimic.Mimic

@CommandAlias("%command")
@CommandPermission("%perm")
@Subcommand("class")
internal class ClassSystemSubcommand(
    private val mimic: Mimic,
) : MimicCommand() {

    override fun afterRegister(manager: AbstractCommandManager) {
        manager.commandCompletions.registerEnumCompletion<Mode>()
    }

    @Subcommand("info")
    @Description("Show information about player's class system")
    @CommandCompletion("@players")
    fun info(sender: CommandSender, @Optional @Flags("other,defaultself") player: Player?) {
        if (player == null) return
        val provider = mimic.getClassSystemProvider()
        val system = provider.getSystem(player)
        sender.send(
            "&3System: &7${provider.id}",
            "&3Classes: &7${system.classes}",
            "&3Primary: &7${system.primaryClass}"
        )
    }

    @Subcommand("has")
    @Description("Check that player has given classes")
    @CommandCompletion("@nothing @mode @players")
    fun has(
        sender: CommandSender,
        @Split classes: Array<String>,
        @Default("all") mode: Mode,
        @Optional @Flags("other,defaultself") player: Player?,
    ) {
        if (player == null) return
        val system = mimic.getClassSystem(player)
        val has = if (mode == Mode.ALL) {
            system.hasAllClasses(classes.asList())
        } else {
            system.hasAnyOfClasses(classes.asList())
        }
        sender.send("&6Player '${player.name}' has%s given classes.".format(if (has) "" else " not"))
    }

    @Suppress("UNUSED")
    internal enum class Mode { ONE, ALL }
}
