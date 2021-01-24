package jargyle.common.security;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;

public final class KeyStoreHelper {
	
	public static KeyStore newKeyStore(
			final File keyStoreFile, 
			final EncryptedPassword keyStorePassword, 
			final String keyStoreType) throws IOException {
		String type = keyStoreType;
		if (type == null) {
			type = KeyStore.getDefaultType();
		}
		KeyStore keyStore = null;
		try {
			keyStore = KeyStore.getInstance(type);
		} catch (KeyStoreException e) {
			throw new IllegalArgumentException(e);
		}
		try (InputStream in = new FileInputStream(keyStoreFile)) {
			keyStore.load(in, keyStorePassword.getPassword());
		} catch (NoSuchAlgorithmException e) {
			throw new IOException(e);
		} catch (CertificateException e) {
			throw new IOException(e);
		}
		return keyStore;
	}

	private KeyStoreHelper() { }
	
}
