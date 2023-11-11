package com.github.jh3nd3rs0n.jargyle.server.internal.server;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.concurrent.locks.ReentrantLock;

import javax.net.ssl.KeyManager;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.TrustManager;

import com.github.jh3nd3rs0n.jargyle.common.string.CommaSeparatedValues;
import com.github.jh3nd3rs0n.jargyle.internal.net.ssl.KeyManagerHelper;
import com.github.jh3nd3rs0n.jargyle.internal.net.ssl.SslContextHelper;
import com.github.jh3nd3rs0n.jargyle.internal.net.ssl.SslSocketFactory;
import com.github.jh3nd3rs0n.jargyle.internal.net.ssl.TrustManagerHelper;
import com.github.jh3nd3rs0n.jargyle.server.Configuration;
import com.github.jh3nd3rs0n.jargyle.server.Settings;
import com.github.jh3nd3rs0n.jargyle.server.SslSettingSpecConstants;

final class SslSocketFactoryImpl extends SslSocketFactory {

	public static boolean isSslEnabled(final Configuration configuration) {
		Settings settings = configuration.getSettings();
		return settings.getLastValue(
				SslSettingSpecConstants.SSL_ENABLED).booleanValue();
	}
	
	private final Configuration configuration;
	private final ReentrantLock lock;
	private SSLContext sslContext;
	
	public SslSocketFactoryImpl(final Configuration config) {
		this.configuration = config;
		this.lock = new ReentrantLock();
		this.sslContext = null;
	}
	
	private SSLContext getSslContext() throws IOException {
		KeyManager[] keyManagers = null;
		TrustManager[] trustManagers = null;
		Settings settings = this.configuration.getSettings();
		File keyStoreFile = settings.getLastValue(
				SslSettingSpecConstants.SSL_KEY_STORE_FILE);
		if (keyStoreFile != null) {
			char[] keyStorePassword = settings.getLastValue(
					SslSettingSpecConstants.SSL_KEY_STORE_PASSWORD).getPassword();
			String keyStoreType = settings.getLastValue(
					SslSettingSpecConstants.SSL_KEY_STORE_TYPE);
			keyManagers = KeyManagerHelper.getKeyManagers(
					keyStoreFile, keyStorePassword,	keyStoreType);
			Arrays.fill(keyStorePassword, '\0');
		}
		File trustStoreFile = settings.getLastValue(
				SslSettingSpecConstants.SSL_TRUST_STORE_FILE);
		if (trustStoreFile != null) {
			char[] trustStorePassword = settings.getLastValue(
					SslSettingSpecConstants.SSL_TRUST_STORE_PASSWORD).getPassword();
			String trustStoreType = settings.getLastValue(
					SslSettingSpecConstants.SSL_TRUST_STORE_TYPE);			
			trustManagers = TrustManagerHelper.getTrustManagers(
					trustStoreFile, trustStorePassword,	trustStoreType);
			Arrays.fill(trustStorePassword, '\0');
		}
		SSLContext context = null;
		try {
			context = SslContextHelper.getSslContext(
					settings.getLastValue(SslSettingSpecConstants.SSL_PROTOCOL), 
					keyManagers, 
					trustManagers);
		} catch (KeyManagementException e) {
			throw new IOException(e);
		} catch (NoSuchAlgorithmException e) {
			throw new IllegalArgumentException(e);
		}
		return context;		
	}
	
	@Override
	public Socket newSocket(
			final Socket socket, 
			final InputStream consumed, 
			final boolean autoClose) throws IOException {
		this.lock.lock();
		try {
			if (this.sslContext == null) {
				this.sslContext = this.getSslContext();
			}
		} finally {
			this.lock.unlock();
		}
		SslSocketFactory factory = SslSocketFactory.newInstance(
				this.sslContext);
		SSLSocket sslSocket = (SSLSocket) factory.newSocket(
				socket,	consumed, autoClose);
		Settings settings = this.configuration.getSettings();
		CommaSeparatedValues enabledCipherSuites = settings.getLastValue(
				SslSettingSpecConstants.SSL_ENABLED_CIPHER_SUITES);
		String[] cipherSuites = enabledCipherSuites.toArray();
		if (cipherSuites.length > 0) {
			sslSocket.setEnabledCipherSuites(cipherSuites);
		}
		CommaSeparatedValues enabledProtocols = settings.getLastValue(
				SslSettingSpecConstants.SSL_ENABLED_PROTOCOLS);
		String[] protocols = enabledProtocols.toArray();
		if (protocols.length > 0) {
			sslSocket.setEnabledProtocols(protocols);
		}
		if (settings.getLastValue(
				SslSettingSpecConstants.SSL_NEED_CLIENT_AUTH).booleanValue()) {
			sslSocket.setNeedClientAuth(true);
		}
		if (settings.getLastValue(
				SslSettingSpecConstants.SSL_WANT_CLIENT_AUTH).booleanValue()) {
			sslSocket.setWantClientAuth(true);
		}
		return sslSocket;
	}
	
	@Override
	public Socket newSocket(
			final Socket socket, 
			final String host, 
			final int port, 
			final boolean autoClose) throws IOException {
		throw new UnsupportedOperationException();
	}

}
