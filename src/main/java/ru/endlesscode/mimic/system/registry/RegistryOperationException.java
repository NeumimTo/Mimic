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

package ru.endlesscode.mimic.system.registry;

/**
 * Common superclass of exceptions thrown by registry operations in Mimic.
 *
 * @author Osip Fatkullin
 * @since 1.0
 */
public class RegistryOperationException extends Exception {
    /**
     * Constructs extension with no parameters
     */
    public RegistryOperationException() {
        super();
    }

    /**
     * Constructs extension with specified cause
     *
     * @param cause         The cause (which is saved for later retrieval by the
     *                      {@link #getCause()} method). (A {@code null} value is
     *                      permitted, and indicates that the cause is nonexistent or
     *                      unknown)
     */
    public RegistryOperationException(Throwable cause) {
        super(cause);
    }

    /**
     * Constructs exception with detail message
     *
     * @param message       The detail message
     */
    public RegistryOperationException(String message) {
        super(message);
    }

    /**
     * Constructs exception with specified detail message and cause
     *
     * @param message       The detail message
     * @param cause         The cause (which is saved for later retrieval by the
     *                      {@link #getCause()} method). (A {@code null} value is
     *                      permitted, and indicates that the cause is nonexistent or
     *                      unknown)
     */
    public RegistryOperationException(String message, Throwable cause) {
        super(message, cause);
    }
}