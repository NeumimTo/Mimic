/*
 * This file is part of Mimic.
 * Copyright (C) 2021 Osip Fatkullin
 * Copyright (C) 2021 EndlessCode Group and contributors
 *
 * Mimic is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Mimic is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Mimic.  If not, see <http://www.gnu.org/licenses/>.
 */

package ru.endlesscode.mimic.classes

/**
 * System that provides methods to work with player's class.
 *
 * Before implementing run an eye over all default method implementations
 * and override all methods that works not properly for your case.
 */
public interface ClassSystem {

    /**
     * Primary class of the player.
     *
     * It is called "primary" because some class systems can support many classes
     * for one player. Primary class can't be null if player has at least one class.
     *
     * @return Primary class name or `null` if player has no any classes.
     */
    public val primaryClass: String?
        get() = classes.firstOrNull()

    /**
     * List of player classes.
     *
     * It is actual for systems which support many classes for one player.
     * If system doesn't support - it just returns [List] with one element.
     *
     * Never returns `null`, but can return empty [List].
     *
     * @return List of player's classes
     * @throws IllegalStateException If player-related object not exists.
     */
    public val classes: List<String>

    /** Returns `true` if player has any class. */
    public fun hasAnyClass(): Boolean = primaryClass != null

    /**
     * Returns `true` if player has any of required classes.
     * @since 0.6.2
     */
    public fun hasAnyOfClasses(firstClass: String, vararg restClasses: String): Boolean {
        return hasAnyOfClasses(listOf(firstClass, *restClasses))
    }

    /** Returns `true` if player has any of [requiredClasses]. */
    public fun hasAnyOfClasses(requiredClasses: List<String>): Boolean {
        return requiredClasses.any { hasClass(it) }
    }

    /**
     * Returns `true` if player has all required classes.
     * @since 0.6.2
     */
    public fun hasAllClasses(firstClass: String, vararg restClasses: String): Boolean {
        return hasAllClasses(listOf(firstClass, *restClasses))
    }

    /** Returns `true` if player has all [requiredClasses]. */
    public fun hasAllClasses(requiredClasses: List<String>): Boolean {
        return requiredClasses.all { hasClass(it) }
    }

    /** Returns `true` if player has [requiredClass]. */
    public fun hasClass(requiredClass: String): Boolean {
        return classes.any {
            it.equals(requiredClass, ignoreCase = true)
        }
    }
}
