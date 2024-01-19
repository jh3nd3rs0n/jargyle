package com.github.jh3nd3rs0n.jargyle.common.security;

import java.util.Objects;

/**
 * An encrypted password.
 */
public final class EncryptedPassword {

    /**
     * The {@code EncryptedPasswordSpec} for this {@code EncryptedPassword}.
     */
    private final EncryptedPasswordSpec encryptedPasswordSpec;

    /**
     * The {@code EncryptedPasswordValue} for this {@code EncryptedPassword}.
     */
    private final EncryptedPasswordValue encryptedPasswordValue;

    /**
     * The name of the type of this {@code EncryptedPassword}.
     */
    private final String typeName;

    /**
     * Constructs an {@code EncryptedPassword} with the provided
     * {@code EncryptedPasswordSpec} and the provided
     * {@code EncryptedPasswordValue}.
     *
     * @param spec         the provided {@code EncryptedPasswordSpec}
     * @param encPassValue the provided {@code EncryptedPasswordValue}
     */
    EncryptedPassword(
            final EncryptedPasswordSpec spec,
            final EncryptedPasswordValue encPassValue) {
        this.encryptedPasswordSpec = Objects.requireNonNull(spec);
        this.encryptedPasswordValue = Objects.requireNonNull(encPassValue);
        this.typeName = spec.getTypeName();
    }

    /**
     * Returns a new {@code EncryptedPassword} of the provided password.
     *
     * @param password the provided password
     * @return a new {@code EncryptedPassword} of the provided password
     */
    public static EncryptedPassword newInstance(final char[] password) {
        return EncryptedPasswordSpecConstants.AES_CFB_PKCS5PADDING_ENCRYPTED_PASSWORD.newEncryptedPassword(
                password);
    }

    /**
     * Returns a new {@code EncryptedPassword} from the provided
     * {@code String}. An {@code IllegalArgumentException} is thrown if the
     * provided {@code String} is invalid.
     *
     * @param s the provided {@code String}
     * @return a new {@code EncryptedPassword} from the provided
     * {@code String}
     */
    public static EncryptedPassword newInstanceFrom(final String s) {
        String[] sElements = s.split(":", 2);
        if (sElements.length != 2) {
            throw new IllegalArgumentException(
                    "encrypted password must be in the following format: "
                            + "TYPE_NAME:ENCRYPTED_PASSWORD_VALUE");
        }
        String typeName = sElements[0];
        String encryptedPasswordValue = sElements[1];
        return newInstance(typeName, encryptedPasswordValue);
    }

    /**
     * Returns a new {@code EncryptedPassword} with the provided name of the
     * type of {@code EncryptedPassword} and the provided {@code String}
     * representation of an {@code EncryptedPasswordValue}. An
     * {@code IllegalArgumentException} is thrown if the provided name of the
     * type of {@code EncryptedPassword} does not exist or the provided
     * {@code String} representation of an {@code EncryptedPasswordValue} is
     * invalid.
     *
     * @param typeName               the provided name of the type of
     *                               {@code EncryptedPassword}
     * @param encryptedPasswordValue the provided {@code String}
     *                               representation of an
     *                               {@code EncryptedPasswordValue}
     * @return a new {@code EncryptedPassword} with the provided name of the
     * type of {@code EncryptedPassword} and the provided {@code String}
     * representation of an {@code EncryptedPasswordValue}
     */
    public static EncryptedPassword newInstance(
            final String typeName, final String encryptedPasswordValue) {
        EncryptedPasswordSpec encryptedPasswordSpec =
                EncryptedPasswordSpecConstants.valueOfTypeName(typeName);
        return encryptedPasswordSpec.newEncryptedPassword(
                encryptedPasswordValue);
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
        EncryptedPassword other = (EncryptedPassword) obj;
        if (!this.typeName.equals(other.typeName)) {
            return false;
        }
        return this.encryptedPasswordValue.equals(
                other.encryptedPasswordValue);
    }

    /**
     * Returns the {@code EncryptedPasswordSpec} of this
     * {@code EncryptedPassword}.
     *
     * @return the {@code EncryptedPasswordSpec} of this
     * {@code EncryptedPassword}
     */
    public EncryptedPasswordSpec getEncryptedPasswordSpec() {
        return this.encryptedPasswordSpec;
    }

    /**
     * Returns the {@code EncryptedPasswordValue} of this
     * {@code EncryptedPassword}.
     *
     * @return the {@code EncryptedPasswordValue} of this
     * {@code EncryptedPassword}
     */
    public EncryptedPasswordValue getEncryptedPasswordValue() {
        return this.encryptedPasswordValue;
    }

    /**
     * Returns the decrypted password of this {@code EncryptedPassword}.
     *
     * @return the decrypted password of this {@code EncryptedPassword}
     */
    public char[] getPassword() {
        return this.encryptedPasswordValue.getPassword();
    }

    /**
     * Returns the name of the type of this {@code EncryptedPassword}.
     *
     * @return the name of the type of this {@code EncryptedPassword}
     */
    public String getTypeName() {
        return this.typeName;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + this.typeName.hashCode();
        result = prime * result + this.encryptedPasswordValue.hashCode();
        return result;
    }

    /**
     * Retyrns the {@code String} representation of this
     * {@code EncryptedPassword}. The {@code String} representation is the
     * name of the type of this {@code EncryptedPassword} followed by a colon
     * (:) followed by the {@code String} representation of the
     * {@code EncryptedPasswordValue} of this {@code EncryptedPassword}.
     *
     * @return the {@code String} representation of this
     * {@code EncryptedPassword}
     */
    @Override
    public String toString() {
        return String.format(
                "%s:%s",
                this.typeName,
                this.encryptedPasswordValue);
    }

}
