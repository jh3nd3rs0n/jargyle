package com.github.jh3nd3rs0n.jargyle.server.socks5.userpassauthmethod;

/**
 * A value for a {@code HashedPassword}.
 */
public abstract class HashedPasswordValue {

    @Override
    public abstract boolean equals(final Object obj);

    @Override
    public abstract int hashCode();

    /**
     * Returns the {@code boolean} value to indicate if this
     * {@code HashedPasswordValue}'s password is equal to the provided
     * password.
     *
     * @param password the provided secret
     * @return the {@code boolean} value to indicate if this
     * {@code HashedPasswordValue}'s password is equal to the provided
     * password
     */
    public abstract boolean passwordEquals(final char[] password);

    /**
     * Returns the {@code String} representation of this
     * {@code HashedPasswordValue}.
     *
     * @return the {@code String} representation of this
     * {@code HashedPasswordValue}
     */
    @Override
    public abstract String toString();

}
