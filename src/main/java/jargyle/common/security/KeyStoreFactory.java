package jargyle.common.security;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;

public final class KeyStoreFactory {
	
	public static KeyStore newKeyStore(
			final File keyStoreFile, 
			final EncryptedPassword keyStorePassword, 
			final String keyStoreType) {
		String type = keyStoreType;
		if (type == null) {
			type = KeyStore.getDefaultType();
		}
		KeyStore keyStore = null;
		try {
			keyStore = KeyStore.getInstance(type);
		} catch (KeyStoreException e) {
			throw new AssertionError(e);
		}
		try {
			keyStore.load(
					new FileInputStream(keyStoreFile), 
					keyStorePassword.getPassword());
		} catch (NoSuchAlgorithmException e) {
			throw new AssertionError(e);
		} catch (CertificateException e) {
			throw new AssertionError(e);
		} catch (FileNotFoundException e) {
			throw new AssertionError(e);
		} catch (IOException e) {
			throw new AssertionError(e);
		}
		return keyStore;
	}

	private KeyStoreFactory() { }
	
}
