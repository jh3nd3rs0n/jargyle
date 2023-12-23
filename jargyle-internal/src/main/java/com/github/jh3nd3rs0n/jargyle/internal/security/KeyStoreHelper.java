package com.github.jh3nd3rs0n.jargyle.internal.security;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.util.Objects;

/**
 * Helper class for {@code KeyStore}s.
 */
public final class KeyStoreHelper {

    /**
     * Prevents the construction of unnecessary instances.
     */
    private KeyStoreHelper() {
    }

    /**
     * Returns a {@code KeyStore} from the provided {@code File} of the key
     * store, the provided password for the key store, and the type of key
     * store.
     *
     * @param keyStoreFile     the provided {@code File} of the key store
     * @param keyStorePassword the provided password for the key store
     * @param keyStoreType     the type of key store (can be {@code null})
     * @return a {@code KeyStore} from the provided {@code File} of the key
     * store, the provided password for the key store, and the type of key
     * store
     * @throws IOException if an I/O error occurs when reading the provided
     *                     {@code File}
     */
    public static KeyStore getKeyStore(
            final File keyStoreFile,
            final char[] keyStorePassword,
            final String keyStoreType) throws IOException {
        Objects.requireNonNull(keyStoreFile);
        Objects.requireNonNull(keyStorePassword);
        String type = keyStoreType;
        if (type == null) {
            type = KeyStore.getDefaultType();
        }
        KeyStore keyStore;
        try {
            keyStore = KeyStore.getInstance(type);
        } catch (KeyStoreException e) {
            throw new IllegalArgumentException(e);
        }
        try (InputStream in = Files.newInputStream(keyStoreFile.toPath())) {
            keyStore.load(in, keyStorePassword);
        } catch (NoSuchAlgorithmException | CertificateException e) {
            throw new IOException(e);
        }
        return keyStore;
    }

}
