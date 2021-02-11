package jargyle.net.ssl;

import java.io.File;
import java.io.IOException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;

import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;

import jargyle.security.EncryptedPassword;
import jargyle.security.KeyStoreHelper;

public final class TrustManagerHelper {

	public static TrustManager[] newTrustManagers(
			final File trustStoreFile,
			final EncryptedPassword trustStorePassword, 
			final String trustStoreType) throws IOException {
		KeyStore trustStore = KeyStoreHelper.newKeyStore(
				trustStoreFile, trustStorePassword, trustStoreType);
		TrustManagerFactory trustManagerFactory = null;
		try {
			trustManagerFactory = TrustManagerFactory.getInstance(
					TrustManagerFactory.getDefaultAlgorithm());
		} catch (NoSuchAlgorithmException e) {
			throw new AssertionError(e);
		}
		try {
			trustManagerFactory.init(trustStore);
		} catch (KeyStoreException e) {
			throw new IOException(e);
		}
		return trustManagerFactory.getTrustManagers();
	}
	
	private TrustManagerHelper() { }
	
}
