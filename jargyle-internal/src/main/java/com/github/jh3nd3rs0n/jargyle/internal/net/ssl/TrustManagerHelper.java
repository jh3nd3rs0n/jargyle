package com.github.jh3nd3rs0n.jargyle.internal.net.ssl;

import java.io.File;
import java.io.IOException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;

import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;

import com.github.jh3nd3rs0n.jargyle.internal.security.KeyStoreHelper;

public final class TrustManagerHelper {

	public static TrustManager[] getTrustManagers(
			final File trustStoreFile,
			final char[] trustStorePassword, 
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
