package jargyle.common.net.ssl;

import java.io.File;
import java.io.IOException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;

import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;

import jargyle.common.security.EncryptedPassword;
import jargyle.common.security.KeyStoreHelper;

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
