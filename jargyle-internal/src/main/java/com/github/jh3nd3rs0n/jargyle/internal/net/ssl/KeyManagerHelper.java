package com.github.jh3nd3rs0n.jargyle.internal.net.ssl;

import java.io.File;
import java.io.IOException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;

import javax.net.ssl.KeyManager;
import javax.net.ssl.KeyManagerFactory;

import com.github.jh3nd3rs0n.jargyle.internal.security.KeyStoreHelper;

public final class KeyManagerHelper {

	public static KeyManager[] getKeyManagers(
			final File keyStoreFile, 
			final char[] keyStorePassword, 
			final String keyStoreType) throws IOException {
		KeyManagerFactory keyManagerFactory = null;
		try {
			keyManagerFactory = KeyManagerFactory.getInstance(
					KeyManagerFactory.getDefaultAlgorithm());
		} catch (NoSuchAlgorithmException e) {
			throw new AssertionError(e);
		}
		KeyStore keyStore = KeyStoreHelper.getKeyStore(
				keyStoreFile, keyStorePassword, keyStoreType);		
		try {
			keyManagerFactory.init(keyStore, keyStorePassword);
		} catch (UnrecoverableKeyException e) {
			throw new IOException(e);
		} catch (KeyStoreException e) {
			throw new IOException(e);
		} catch (NoSuchAlgorithmException e) {
			throw new IOException(e);
		}
		return keyManagerFactory.getKeyManagers();
	}
	
	private KeyManagerHelper() { }
	
}
