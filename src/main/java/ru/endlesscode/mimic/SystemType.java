/*
 * This file is part of MimicAPI.
 * Copyright (C) 2017 Osip Fatkullin
 *
 * MimicAPI is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * MimicAPI is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with MimicAPI.  If not, see <http://www.gnu.org/licenses/>.
 */

package ru.endlesscode.mimic;

import ru.endlesscode.mimic.classes.ClassSystem;
import ru.endlesscode.mimic.levels.LevelSystem;

/**
 * Available system types used in {@link Metadata ru.enlesscode.mimic.Metadata}
 *
 * @author Osip Fatkullin
 * @since 1.0
 */
public enum SystemType {
    /**
     * Level System type
     */
    LEVEL(LevelSystem.class),

    /**
     * Class System type
     */
    CLASS(ClassSystem.class);

    private final Class<? extends PlayerSystem> systemClass;

    SystemType(Class<? extends PlayerSystem> systemClass) {
        this.systemClass = systemClass;
    }

    /**
     * Returns system class
     *
     * @return system class
     */
    public <T extends PlayerSystem> Class<T> getSystemClass() {
        //noinspection unchecked
        return (Class<T>) systemClass;
    }
}
