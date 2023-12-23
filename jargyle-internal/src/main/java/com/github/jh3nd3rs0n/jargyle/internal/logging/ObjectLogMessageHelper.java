package com.github.jh3nd3rs0n.jargyle.internal.logging;

import java.util.Objects;

/**
 * Helper class for creating log messages of {@code Object}s and their
 * associated messages.
 */
public final class ObjectLogMessageHelper {

    /**
     * Prevents the construction of unnecessary instances.
     */
    private ObjectLogMessageHelper() {
    }

    /**
     * Creates a log message of the provided {@code Object} and its associated
     * format message and its arguments. A {@code IllegalFormatException} is
     * thrown if the provided format message or the provided arguments are
     * incorrect.
     *
     * @param obj     the provided {@code Object}
     * @param message the provided {@code Object}'s associated format message
     * @param args    the provided arguments to the provided {@code Object}'s
     *                associated format message
     * @return a log message of the provided {@code Object} and its associated
     * message
     */
    public static String objectLogMessage(
            final Object obj, final String message, final Object... args) {
        Objects.requireNonNull(obj);
        return obj + ": " + String.format(message, args);
    }

}
