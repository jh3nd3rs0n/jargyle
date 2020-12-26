package jargyle.common.net.ssl;

import java.io.File;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;

import javax.net.ssl.KeyManager;
import javax.net.ssl.KeyManagerFactory;

import jargyle.common.security.EncryptedPassword;
import jargyle.common.security.KeyStoreFactory;

public final class KeyManagersFactory {

	public static KeyManager[] newKeyManagers(
			final File keyStoreFile, final EncryptedPassword keyStorePassword) {
		KeyStore keyStore = KeyStoreFactory.newKeyStore(
				keyStoreFile, keyStorePassword);
		KeyManagerFactory keyManagerFactory = null;
		try {
			keyManagerFactory = KeyManagerFactory.getInstance(
					KeyManagerFactory.getDefaultAlgorithm());
		} catch (NoSuchAlgorithmException e) {
			throw new AssertionError(e);
		}
		try {
			keyManagerFactory.init(
					keyStore, keyStorePassword.getPassword());
		} catch (UnrecoverableKeyException e) {
			throw new AssertionError(e);
		} catch (KeyStoreException e) {
			throw new AssertionError(e);
		} catch (NoSuchAlgorithmException e) {
			throw new AssertionError(e);
		}
		return keyManagerFactory.getKeyManagers();
	}
	
	private KeyManagersFactory() { }
	
}
