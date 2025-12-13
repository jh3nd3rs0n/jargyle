package com.github.jh3nd3rs0n.jargyle.server.socks5.userpassauthmethod.internal.hashedpassvalue.impl;

import com.github.jh3nd3rs0n.jargyle.server.socks5.userpassauthmethod.HashedPasswordValue;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.Base64;

/**
 * A {@code HashedPasswordValue} using the secret key factory algorithm
 * {@code PBKDF2WithHmacSHA256}.
 */
public final class Pbkdf2WithHmacSha256HashedPasswordValue
        extends HashedPasswordValue {

    /**
     * The iteration count.
     */
    private static final int ITERATION_COUNT = 65536;

    /**
     * The length of the secret key used to generate the hash.
     */
    private static final int KEY_LENGTH = 256;

    /**
     * The initial length of the salt.
     */
    private static final int SALT_LENGTH = 8;

    /**
     * The secret key factory algorithm to be used to create the secret key
     * used to generate the hash.
     */
    private static final String SECRET_KEY_FACTORY_ALGORITHM =
            "PBKDF2WithHmacSHA256";

    /**
     * The {@code SecureRandom} to be used to generate a new salt.
     */
    private static final SecureRandom SECURE_RANDOM = new SecureRandom();

    /**
     * The salt for this {@code HashedPasswordValue}.
     */
    private final byte[] salt;

    /**
     * The {@code String} representation of this
     * {@code Pbkdf2WithHmacSha256HashedPasswordValue}.
     */
    private final String string;

    /**
     * Constructs a {@code Pbkdf2WithHmacSha256HashedPasswordValue} of the
     * provided password.
     *
     * @param password the provided password
     */
    public Pbkdf2WithHmacSha256HashedPasswordValue(final char[] password) {
        this(password, newSalt());
    }

    /**
     * Constructs a {@code Pbkdf2WithHmacSha256HashedPasswordValue} of the
     * provided password and the provided salt.
     *
     * @param password the provided password
     * @param slt      the provided salt
     */
    Pbkdf2WithHmacSha256HashedPasswordValue(
            final char[] password, final byte[] slt) {
        KeySpec keySpec = new PBEKeySpec(
                password,
                slt,
                ITERATION_COUNT,
                KEY_LENGTH);
        SecretKeyFactory factory;
        try {
            factory = SecretKeyFactory.getInstance(
                    SECRET_KEY_FACTORY_ALGORITHM);
        } catch (NoSuchAlgorithmException e) {
            throw new AssertionError(e);
        }
        byte[] hash;
        try {
            hash = factory.generateSecret(keySpec).getEncoded();
        } catch (InvalidKeySpecException e) {
            throw new AssertionError(e);
        }
        this.salt = slt;
        Base64.Encoder encoder = Base64.getEncoder();
        this.string = String.format(
                "%s,%s",
                encoder.encodeToString(hash),
                encoder.encodeToString(slt));
    }

    /**
     * Constructs a {@code Pbkdf2WithHmacSha256HashedPasswordValue} from the
     * provided {@code String}. An {@code IllegalArgumentException} is thrown
     * if the provided {@code String} is invalid.
     *
     * @param s the provided {@code String}
     */
    public Pbkdf2WithHmacSha256HashedPasswordValue(final String s) {
        String message = String.format(
                "hashed password value must be in the following format: "
                        + "HASH_BASE_64_STRING,SALT_BASE_64_STRING "
                        + "actual hashed password value is %s",
                s);
        String[] sElements = s.split(",", -1);
        if (sElements.length != 2) {
            throw new IllegalArgumentException(message);
        }
        String hashBase64String = sElements[0];
        String saltBase64String = sElements[1];
        Base64.Decoder decoder = Base64.getDecoder();
        try {
            decoder.decode(hashBase64String);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException(message, e);
        }
        byte[] slt;
        try {
            slt = decoder.decode(saltBase64String);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException(message, e);
        }
        this.salt = slt;
        this.string = s;
    }

    /**
     * Returns a new salt.
     *
     * @return a new salt
     */
    private static byte[] newSalt() {
        byte[] salt = new byte[SALT_LENGTH];
        SECURE_RANDOM.nextBytes(salt);
        return salt;
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
        Pbkdf2WithHmacSha256HashedPasswordValue other =
                (Pbkdf2WithHmacSha256HashedPasswordValue) obj;
        return this.string.equals(other.string);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + this.string.hashCode();
        return result;
    }

    @Override
    public boolean passwordEquals(final char[] password) {
        Pbkdf2WithHmacSha256HashedPasswordValue other =
                new Pbkdf2WithHmacSha256HashedPasswordValue(
                        password, this.salt);
        return this.equals(other);
    }

    @Override
    public String toString() {
        return this.string;
    }

}
