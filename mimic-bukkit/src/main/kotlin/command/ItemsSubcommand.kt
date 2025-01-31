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
import ru.endlesscode.mimic.impl.mimic.MimicItemsRegistry
import ru.endlesscode.mimic.items.BukkitItemsRegistry

@CommandAlias("%command")
@CommandPermission("%perm")
@Subcommand("items")
internal class ItemsSubcommand(private val itemsRegistry: BukkitItemsRegistry) : MimicCommand() {

    override fun afterRegister(manager: AbstractCommandManager) {
        manager.commandCompletions.registerAsyncCompletion("item") { itemsRegistry.knownIds }
    }

    @Subcommand("info")
    @Description("Show information about items service")
    fun info(sender: CommandSender) {
        val registries = (itemsRegistry as? MimicItemsRegistry)?.providers
            .orEmpty()
            .map { it.provider }
            .map { "  &f${it.id}: &7${it.knownIds.size}" }

        sender.send(
            "&3Items Service: &7${itemsRegistry.id}",
            "&3Known IDs amount: &7${itemsRegistry.knownIds.size}"
        )
        sender.send(registries)
    }

    @Subcommand("give")
    @Description("Give item to player")
    @CommandCompletion("@players @item @nothing")
    fun give(
        sender: CommandSender,
        @Flags("other") player: Player,
        item: String,
        @Default("1") amount: Int,
        @Optional payload: String?,
    ) {
        val itemStack = itemsRegistry.getItem(item, payload, amount)
        if (itemStack != null) {
            player.inventory.addItem(itemStack)
            sender.send("&6Gave ${itemStack.amount} [$item] to ${player.name}.")
        } else {
            sender.send("&cUnknown item '$item'.")
        }
    }

    @Subcommand("compare")
    @Description("Compare item in hand corresponds and given item")
    @CommandCompletion("@item")
    fun compare(
        @Flags("itemheld") player: Player,
        @Single item: String,
    ) {
        val isSame = itemsRegistry.isSameItem(player.inventory.itemInMainHand, item)
        player.send("&6Item in hand and '$item' are%s same.".format(if (isSame) "" else "n't"))
    }

    @Subcommand("id")
    @Description("Prints ID of item in hand")
    fun id(
        @Flags("itemheld") player: Player,
    ) {
        val id = itemsRegistry.getItemId(player.inventory.itemInMainHand)
        player.send("&6Id of item in hand is '$id'")
    }

    @Subcommand("find")
    @Description("Check that item with given ID exists")
    @CommandCompletion("@item")
    fun find(
        sender: CommandSender,
        @Single item: String,
    ) {
        val itemExists = itemsRegistry.isItemExists(item)
        sender.send("&6Item with id '$item'%s exists".format(if (itemExists) "" else " isn't"))
    }
}
