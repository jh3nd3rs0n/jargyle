package com.github.jh3nd3rs0n.jargyle.common.security;

import java.util.Objects;

/**
 * The specification of an {@code EncryptedPassword}.
 */
public abstract class EncryptedPasswordSpec {

    /**
     * The name of the type of {@code EncryptedPassword}.
     */
    private final String typeName;

    /**
     * Constructs an {@code EncryptedPasswordSpec} with the provided name of
     * the type of {@code EncryptedPassword}.
     *
     * @param typName the provided name of the type of
     *                {@code EncryptedPassword}
     */
    EncryptedPasswordSpec(final String typName) {
        Objects.requireNonNull(typName);
        this.typeName = typName;
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (this.getClass() != obj.getClass()) {
            return false;
        }
        EncryptedPasswordSpec other = (EncryptedPasswordSpec) obj;
        return this.typeName.equals(other.typeName);
    }

    /**
     * Returns the name of the type of {@code EncryptedPassword}.
     *
     * @return the name of the type of {@code EncryptedPassword}
     */
    public final String getTypeName() {
        return this.typeName;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + this.typeName.hashCode();
        return result;
    }

    /**
     * Returns a new {@code EncryptedPassword} of the provided password.
     *
     * @param password the provided password
     * @return a new {@code EncryptedPassword} of the provided password
     */
    public abstract EncryptedPassword newEncryptedPassword(
            final char[] password);

    /**
     * Returns a new {@code EncryptedPassword} with the parsed
     * {@code EncryptedPasswordValue} from the provided {@code String} value.
     * An {@code IllegalArgumentException} is thrown if the provided
     * {@code String} value to be parsed as an {@code EncryptedPasswordValue}
     * is invalid.
     *
     * @param encryptedPasswordValue the provided {@code String} value to be
     *                               parsed as an
     *                               {@code EncryptedPasswordValue}
     * @return a new {@code EncryptedPassword} with the parsed
     * {@code EncryptedPasswordValue} from the provided {@code String} value
     */
    public abstract EncryptedPassword newEncryptedPassword(
            final String encryptedPasswordValue);

    /**
     * Returns the {@code String} representation of this
     * {@code EncryptedPasswordSpec}.
     *
     * @return the {@code String} representation of this
     * {@code EncryptedPasswordSpec}
     */
    @Override
    public String toString() {
        return this.getClass().getSimpleName() +
                " [typeName=" +
                this.typeName +
                "]";
    }

}
