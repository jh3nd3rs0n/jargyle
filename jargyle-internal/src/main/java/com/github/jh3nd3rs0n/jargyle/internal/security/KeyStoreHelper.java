package com.github.jh3nd3rs0n.jargyle.internal.security;

import java.io.IOException;
import java.io.InputStream;
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
     * Returns a {@code KeyStore} from the provided {@code InputStream} of
     * the key store, the provided password for the key store, and the
     * optionally provided type of key store. An
     * {@code IllegalArgumentException} is thrown if the provided type of key
     * store is not supported.
     *
     * @param keyStoreInputStream the provided {@code InputStream} of the key
     *                            store
     * @param keyStorePassword    the provided password for the key store
     * @param keyStoreType        the optionally provided type of key store
     *                            (can be {@code null})
     * @return a {@code KeyStore} from the provided {@code InputStream} of
     * the key store, the provided password for the key store, and the
     * optionally provided type of key store
     * @throws IOException if an I/O error occurs when reading the provided
     *                     {@code InputStream} of the key store
     */
    public static KeyStore getKeyStore(
            final InputStream keyStoreInputStream,
            final char[] keyStorePassword,
            final String keyStoreType) throws IOException {
        Objects.requireNonNull(keyStoreInputStream);
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
        try {
            keyStore.load(keyStoreInputStream, keyStorePassword);
        } catch (NoSuchAlgorithmException | CertificateException e) {
            throw new IOException(e);
        }
        return keyStore;
    }

}
