package com.github.jh3nd3rs0n.jargyle.common.security;

/**
 * A value for an {@code EncryptedPassword}.
 */
public abstract class EncryptedPasswordValue {

    @Override
    public abstract boolean equals(final Object obj);

    /**
     * Returns the password of this {@code EncryptedPasswordValue}.
     *
     * @return the password of this {@code EncryptedPasswordValue}
     */
    public abstract char[] getPassword();

    @Override
    public abstract int hashCode();

    /**
     * Returns the {@code String} representation of this
     * {@code EncryptedPasswordValue}.
     *
     * @return the {@code String} representation of this
     * {@code EncryptedPasswordValue}
     */
    @Override
    public abstract String toString();

}
