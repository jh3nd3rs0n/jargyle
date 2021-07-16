package jargyle.internal.net.ssl;

import java.io.File;
import java.io.IOException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;

import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;

import jargyle.internal.security.KeyStoreHelper;
import jargyle.security.EncryptedPassword;

public final class TrustManagerHelper {

	public static TrustManager[] getTrustManagers(
			final File trustStoreFile,
			final EncryptedPassword trustStorePassword, 
			final String trustStoreType) throws IOException {
		TrustManagerFactory trustManagerFactory = null;
		try {
			trustManagerFactory = TrustManagerFactory.getInstance(
					TrustManagerFactory.getDefaultAlgorithm());
		} catch (NoSuchAlgorithmException e) {
			throw new AssertionError(e);
		}
		KeyStore trustStore = KeyStoreHelper.getKeyStore(
				trustStoreFile, trustStorePassword, trustStoreType);
		try {
			trustManagerFactory.init(trustStore);
		} catch (KeyStoreException e) {
			throw new IOException(e);
		}
		return trustManagerFactory.getTrustManagers();
	}
	
	private TrustManagerHelper() { }
	
}
