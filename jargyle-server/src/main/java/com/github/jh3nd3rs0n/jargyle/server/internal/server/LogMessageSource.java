package com.github.jh3nd3rs0n.jargyle.server.internal.server;

/**
 * Marker interface for objects whose {@code String} representation will be
 * used as the source of log messages.
 */
public interface LogMessageSource {

    /**
     * Returns the {@code String} representation of this
     * {@code LogMessageSource}.
     *
     * @return the {@code String} representation of this
     * {@code LogMessageSource}
     */
    @Override
    String toString();

}
