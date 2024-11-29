package com.github.jh3nd3rs0n.jargyle.common.security.internal.encryptedpassvalue.impl;

import com.github.jh3nd3rs0n.jargyle.common.security.EncryptedPasswordValue;

import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.InvalidParameterSpecException;
import java.security.spec.KeySpec;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

/**
 * An {@code EncryptedPasswordValue} using the cipher algorithm
 * {@code AES/CFB/PKCS5Padding}.
 */
public final class AesCfbPkcs5PaddingEncryptedPasswordValue
        extends EncryptedPasswordValue {

    /**
     * The cipher algorithm to be used.
     */
    private static final String CIPHER_ALGORITHM = "AES/CFB/PKCS5Padding";

    /**
     * The iteration count.
     */
    private static final int ITERATION_COUNT = 65536;

    /**
     * The length of the encoded key.
     */
    private static final int KEY_LENGTH = 256;

    /**
     * The initial length of the salt.
     */
    private static final int SALT_LENGTH = 8;

    /**
     * The secret key factory algorithm to be used to create the secret key
     * used for encryption/decryption.
     */
    private static final String SECRET_KEY_FACTORY_ALGORITHM =
            "PBKDF2WithHmacSHA256";

    /**
     * The algorithm for the specification of the encoded key.
     */
    private static final String SECRET_KEY_SPEC_ALGORITHM = "AES";

    /**
     * The {@code SecureRandom} to be used to generate a new salt.
     */
    private static final SecureRandom SECURE_RANDOM = new SecureRandom();

    /**
     * The encrypted password.
     */
    private final byte[] encrypted;

    /**
     * The initialization vector.
     */
    private final byte[] initializationVector;

    /**
     * The salt.
     */
    private final byte[] salt;

    /**
     * The {@code String} representation of this
     * {@code AesCfbPkcs5PaddingEncryptedPasswordValue}.
     */
    private final String string;

    /**
     * Constructs an {@code AesCfbPkcs5PaddingEncryptedPasswordValue} of the
     * provided password.
     *
     * @param password the provided password
     */
    public AesCfbPkcs5PaddingEncryptedPasswordValue(final char[] password) {
        byte[] slt = newSalt();
        SecretKey secretKey = newSecretKey(slt);
        Cipher cipher;
        try {
            cipher = Cipher.getInstance(CIPHER_ALGORITHM);
        } catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
            throw new AssertionError(e);
        }
        try {
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
        } catch (InvalidKeyException e) {
            throw new AssertionError(e);
        }
        AlgorithmParameters params = cipher.getParameters();
        IvParameterSpec ivParameterSpec;
        try {
            ivParameterSpec = params.getParameterSpec(
                    IvParameterSpec.class);
        } catch (InvalidParameterSpecException e) {
            throw new AssertionError(e);
        }
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try (Writer writer = new OutputStreamWriter(out)) {
            writer.write(password);
            writer.flush();
        } catch (IOException e) {
            throw new AssertionError(e);
        }
        byte[] enc;
        try {
            enc = cipher.doFinal(out.toByteArray());
        } catch (IllegalBlockSizeException | BadPaddingException e) {
            throw new AssertionError(e);
        }
        this.encrypted = enc;
        this.initializationVector = ivParameterSpec.getIV();
        this.salt = slt;
        Base64.Encoder encoder = Base64.getEncoder();
        this.string = String.format(
                "%s,%s,%s",
                encoder.encodeToString(this.encrypted),
                encoder.encodeToString(this.initializationVector),
                encoder.encodeToString(this.salt));
    }

    /**
     * Constructs an {@code AesCfbPkcs5PaddingEncryptedPasswordValue} from the
     * provided {@code String}. An {@code IllegalArgumentException} is thrown
     * if the provided {@code String} is invalid.
     *
     * @param s the provided {@code String}
     */
    public AesCfbPkcs5PaddingEncryptedPasswordValue(final String s) {
        String message = String.format(
                "encrypted password value must be in the following format: "
                        + "ENCRYPTED_BASE_64_STRING,"
                        + "INITIALIZATION_VECTOR_BASE_64_STRING,"
                        + "SALT_BASE_64_STRING "
                        + "actual encrypted password value is %s",
                s);
        String[] sElements = s.split(",", -1);
        if (sElements.length != 3) {
            throw new IllegalArgumentException(message);
        }
        String encryptedBase64String = sElements[0];
        String initializationVectorBase64String = sElements[1];
        String saltBase64String = sElements[2];
        Base64.Decoder decoder = Base64.getDecoder();
        byte[] enc;
        try {
            enc = decoder.decode(encryptedBase64String);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException(message, e);
        }
        byte[] initVector;
        try {
            initVector = decoder.decode(
                    initializationVectorBase64String);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException(message, e);
        }
        byte[] slt;
        try {
            slt = decoder.decode(saltBase64String);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException(message, e);
        }
        checkArguments(enc, initVector, slt);
        this.encrypted = enc;
        this.initializationVector = initVector;
        this.salt = slt;
        this.string = s;
    }

    /**
     * Checks if the provided arguments are valid. An
     * {@code IllegalArgumentException} is thrown if one of the arguments is
     * invalid.
     *
     * @param encrypted            the encrypted password
     * @param initializationVector the initialization vector
     * @param salt                 the salt
     */
    private static void checkArguments(
            final byte[] encrypted,
            final byte[] initializationVector,
            final byte[] salt) {
        decrypt(encrypted, initializationVector, salt);
    }

    /**
     * Returns the decrypted secret. An {@code IllegalArgumentException} is
     * thrown if one of the arguments is invalid.
     *
     * @param encrypted            the encrypted password
     * @param initializationVector the initialization vector
     * @param salt                 the salt
     * @return the decrypted secret
     */
    private static byte[] decrypt(
            final byte[] encrypted,
            final byte[] initializationVector,
            final byte[] salt) {
        Cipher cipher;
        try {
            cipher = Cipher.getInstance(CIPHER_ALGORITHM);
        } catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
            throw new AssertionError(e);
        }
        try {
            cipher.init(
                    Cipher.DECRYPT_MODE,
                    newSecretKey(salt),
                    new IvParameterSpec(initializationVector));
        } catch (InvalidKeyException | InvalidAlgorithmParameterException e) {
            throw new IllegalArgumentException(e);
        }
        byte[] decrypted;
        try {
            decrypted = cipher.doFinal(encrypted);
        } catch (IllegalBlockSizeException | BadPaddingException e) {
            throw new IllegalArgumentException(e);
        }
        return decrypted;
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

    /**
     * Returns a new {@code SecretKey} using the provided salt.
     *
     * @param salt the provided salt
     * @return a new {@code SecretKey} using the provided salt
     */
    private static SecretKey newSecretKey(final byte[] salt) {
        SecretKeyFactory factory;
        try {
            factory = SecretKeyFactory.getInstance(
                    SECRET_KEY_FACTORY_ALGORITHM);
        } catch (NoSuchAlgorithmException e) {
            throw new AssertionError(e);
        }
        KeySpec keySpec = new PBEKeySpec(
                EncryptionPasswordHelper.getEncryptionPassword(),
                salt,
                ITERATION_COUNT,
                KEY_LENGTH);
        SecretKey secretKey;
        try {
            secretKey = factory.generateSecret(keySpec);
        } catch (InvalidKeySpecException e) {
            throw new AssertionError(e);
        }
        return new SecretKeySpec(
                secretKey.getEncoded(), SECRET_KEY_SPEC_ALGORITHM);
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
        AesCfbPkcs5PaddingEncryptedPasswordValue other =
                (AesCfbPkcs5PaddingEncryptedPasswordValue) obj;
        return this.string.equals(other.string);
    }

    @Override
    public char[] getPassword() {
        byte[] decrypted = decrypt(
                this.encrypted,
                this.initializationVector,
                this.salt);
        List<Character> characters = new ArrayList<>();
        try (Reader reader = new InputStreamReader(new ByteArrayInputStream(
                decrypted))) {
            int ch;
            while ((ch = reader.read()) != -1) {
                characters.add((char) ch);
            }
        } catch (IOException e) {
            throw new AssertionError(e);
        }
        int charactersCount = characters.size();
        char[] password = new char[charactersCount];
        for (int i = 0; i < charactersCount; i++) {
            password[i] = characters.get(i);
        }
        return password;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + this.string.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return this.string;
    }

}
