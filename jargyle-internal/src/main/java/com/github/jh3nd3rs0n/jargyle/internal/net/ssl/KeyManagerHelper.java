package com.github.jh3nd3rs0n.jargyle.internal.net.ssl;

import com.github.jh3nd3rs0n.jargyle.internal.security.KeyStoreHelper;

import javax.net.ssl.KeyManager;
import javax.net.ssl.KeyManagerFactory;
import java.io.IOException;
import java.io.InputStream;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;

/**
 * Helper class for {@code KeyManager}s.
 */
public final class KeyManagerHelper {

    /**
     * Prevents the construction of unnecessary instances.
     */
    private KeyManagerHelper() {
    }

    /**
     * Returns an array of {@code KeyManager}s from the provided
     * {@code InputStream} of the key store, the provided password of the key
     * store, and the optionally provided type of key store. An
     * {@code IllegalArgumentException} is thrown if the provided type of key
     * store is not supported.
     *
     * @param keyStoreInputStream the provided {@code InputStream} of the key
     *                            store
     * @param keyStorePassword    the provided password of the key store
     * @param keyStoreType        the optionally provided type of key store
     *                            (can be {@code null})
     * @return an array of {@code KeyManager}s from the provided
     * {@code InputStream} of the key store, the provided password of the key
     * store, and the optionally provided type of key store
     * @throws IOException if an I/O error occurs when reading the provided
     *                     {@code InputStream} of the key store
     */
    public static KeyManager[] getKeyManagers(
            final InputStream keyStoreInputStream,
            final char[] keyStorePassword,
            final String keyStoreType) throws IOException {
        KeyManagerFactory keyManagerFactory;
        try {
            keyManagerFactory = KeyManagerFactory.getInstance(
                    KeyManagerFactory.getDefaultAlgorithm());
        } catch (NoSuchAlgorithmException e) {
            throw new AssertionError(e);
        }
        KeyStore keyStore = KeyStoreHelper.getKeyStore(
                keyStoreInputStream, keyStorePassword, keyStoreType);
        try {
            keyManagerFactory.init(keyStore, keyStorePassword);
        } catch (UnrecoverableKeyException | KeyStoreException | NoSuchAlgorithmException e) {
            throw new IOException(e);
        }
        return keyManagerFactory.getKeyManagers();
    }

}
