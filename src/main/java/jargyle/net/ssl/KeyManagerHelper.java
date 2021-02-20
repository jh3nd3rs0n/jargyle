package jargyle.net.ssl;

import java.io.File;
import java.io.IOException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;

import javax.net.ssl.KeyManager;
import javax.net.ssl.KeyManagerFactory;

import jargyle.security.EncryptedPassword;
import jargyle.security.KeyStoreHelper;

public final class KeyManagerHelper {

	public static KeyManager[] newKeyManagers(
			final File keyStoreFile, 
			final EncryptedPassword keyStorePassword, 
			final String keyStoreType) throws IOException {
		KeyStore keyStore = KeyStoreHelper.newKeyStore(
				keyStoreFile, keyStorePassword, keyStoreType);
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