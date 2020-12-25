package jargyle.common.net.ssl;

import java.io.File;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;

import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;

import jargyle.common.security.EncryptedPassword;
import jargyle.common.security.KeyStoreFactory;

public final class TrustManagersFactory {

	public static TrustManager[] getTrustManagers(
			final File trustStoreFile, 
			final EncryptedPassword trustStorePassword) {
		KeyStore trustStore = KeyStoreFactory.getKeyStore(
				trustStoreFile, trustStorePassword);
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
			throw new AssertionError(e);
		}
		return trustManagerFactory.getTrustManagers();
	}
	
	private TrustManagersFactory() { }
	
}
