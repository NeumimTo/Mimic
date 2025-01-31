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

package ru.endlesscode.mimic.impl.heroes

import com.herocraftonline.heroes.Heroes
import com.herocraftonline.heroes.characters.Hero
import com.herocraftonline.heroes.characters.classes.HeroClass
import com.herocraftonline.heroes.util.Properties
import org.bukkit.entity.Player

internal class HeroesWrapper {

    val isEnabled: Boolean get() = Heroes.getInstance().isEnabled

    fun getClasses(): Set<HeroClass> = Heroes.getInstance().classManager.classes.orEmpty()
    fun getHero(player: Player): Hero = Heroes.getInstance().characterManager.getHero(player)
    fun getExp(level: Int): Int = Properties.getExp(level)
    fun getTotalExp(level: Int): Int = Properties.getTotalExp(level)
    fun getLevel(exp: Double): Int = Properties.getLevel(exp)
}
