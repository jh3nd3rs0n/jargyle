package com.github.jh3nd3rs0n.jargyle.common.security.internal.encryptedpassvalue.impl;

import com.github.jh3nd3rs0n.jargyle.common.security.SystemPropertyNameConstants;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * Helper class for obtaining the password to be used for
 * encryption/decryption.
 */
final class EncryptionPasswordHelper {

    /**
     * Prevents the construction of unnecessary instances.
     */
    private EncryptionPasswordHelper() {
    }

    /**
     * Returns the partial password from the provided file as a {@code String}.
     * If the provided file as a {@code String} is invalid or is not found, or
     * when an I/O error occurs upon reading the provided file, an empty
     * {@code char} array is returned.
     *
     * @param s the provided file as a {@code String}
     * @return the partial password from the provided file as a {@code String}
     * or an empty {@code char} array if the provided file as a {@code String}
     * is invalid or is not found, or when an I/O error occurs upon reading
     * the provided file
     */
    private static char[] getPartialPasswordFromFile(final String s) {
        if (s.isEmpty()) {
            return new char[]{};
        }
        List<Character> characters = new ArrayList<>();
        try (Reader reader = new InputStreamReader(
                Files.newInputStream(
                        new File(s).toPath()), StandardCharsets.UTF_8)) {
            int ch;
            while ((ch = reader.read()) != -1) {
                characters.add((char) ch);
            }
        } catch (InvalidPathException | IOException e) {
            return new char[]{};
        }
        int charactersCount = characters.size();
        char[] password = new char[charactersCount];
        for (int i = 0; i < charactersCount; i++) {
            password[i] = characters.get(i);
        }
        return password;
    }

    /**
     * Returns from the system properties the password to be used for
     * encryption/decryption.
     *
     * @return the password to be used for encryption/decryption
     */
    public static char[] getEncryptionPassword() {
        Properties properties = System.getProperties();
        int passwordLength = 0;
        char[] partialPasswordFromFile = getPartialPasswordFromFile(
                properties.getProperty(
                        SystemPropertyNameConstants.PARTIAL_ENCRYPTION_PASSWORD_FILE_SYSTEM_PROPERTY_NAME,
                        ""));
        passwordLength += partialPasswordFromFile.length;
        char[] partialPassword = properties.getProperty(
                SystemPropertyNameConstants.PARTIAL_ENCRYPTION_PASSWORD_SYSTEM_PROPERTY_NAME,
                "").toCharArray();
        passwordLength += partialPassword.length;
        char[] password = new char[passwordLength];
        int destPos = 0;
        System.arraycopy(
                partialPasswordFromFile,
                0,
                password,
                destPos,
                partialPasswordFromFile.length);
        destPos += partialPasswordFromFile.length;
        System.arraycopy(
                partialPassword,
                0,
                password,
                destPos,
                partialPassword.length);
        return password;
    }

}
