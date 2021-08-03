package com.github.jh3nd3rs0n.jargyle.internal.net.ssl;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import javax.net.ssl.KeyManager;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;

public final class SslContextHelper {

	public static SSLContext getSslContext(
			final String protocol,
			final KeyManager[] keyManagers,
			final TrustManager[] trustManagers) 
			throws NoSuchAlgorithmException, KeyManagementException {
		SSLContext context = SSLContext.getInstance(protocol);
		context.init(keyManagers, trustManagers, new SecureRandom());
		return context;
	}
	
	private SslContextHelper() { }
	
}
