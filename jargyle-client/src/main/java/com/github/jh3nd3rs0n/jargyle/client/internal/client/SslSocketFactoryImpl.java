package com.github.jh3nd3rs0n.jargyle.client.internal.client;

import com.github.jh3nd3rs0n.jargyle.client.Properties;
import com.github.jh3nd3rs0n.jargyle.client.SslPropertySpecConstants;
import com.github.jh3nd3rs0n.jargyle.common.string.CommaSeparatedValues;
import com.github.jh3nd3rs0n.jargyle.internal.net.ssl.KeyManagerHelper;
import com.github.jh3nd3rs0n.jargyle.internal.net.ssl.SslContextHelper;
import com.github.jh3nd3rs0n.jargyle.internal.net.ssl.SslSocketFactory;
import com.github.jh3nd3rs0n.jargyle.internal.net.ssl.TrustManagerHelper;

import javax.net.ssl.KeyManager;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.TrustManager;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.concurrent.locks.ReentrantLock;

final class SslSocketFactoryImpl extends SslSocketFactory {

	public static boolean isSslEnabled(final Properties props) {
		return props.getValue(
				SslPropertySpecConstants.SSL_ENABLED).booleanValue();
	}
	
	private final ReentrantLock lock;
	private final Properties properties;
	private SSLContext sslContext;

	public SslSocketFactoryImpl(final Properties props) {
		this.lock = new ReentrantLock();
		this.properties = props;
		this.sslContext = null;
	}

	private SSLContext getSslContext() throws IOException {
		KeyManager[] keyManagers = null;
		TrustManager[] trustManagers = null;
		File keyStoreFile = this.properties.getValue(
				SslPropertySpecConstants.SSL_KEY_STORE_FILE);
		if (keyStoreFile != null) {
			char[] keyStorePassword = this.properties.getValue(
					SslPropertySpecConstants.SSL_KEY_STORE_PASSWORD).getPassword();
			String keyStoreType = this.properties.getValue(
					SslPropertySpecConstants.SSL_KEY_STORE_TYPE);
			keyManagers = KeyManagerHelper.getKeyManagers(
					keyStoreFile, keyStorePassword,	keyStoreType);
			Arrays.fill(keyStorePassword, '\0');
		}
		File trustStoreFile = this.properties.getValue(
				SslPropertySpecConstants.SSL_TRUST_STORE_FILE);
		if (trustStoreFile != null) {
			char[] trustStorePassword = this.properties.getValue(
					SslPropertySpecConstants.SSL_TRUST_STORE_PASSWORD).getPassword();
			String trustStoreType = this.properties.getValue(
					SslPropertySpecConstants.SSL_TRUST_STORE_TYPE);
			trustManagers = TrustManagerHelper.getTrustManagers(
					trustStoreFile, trustStorePassword,	trustStoreType);
			Arrays.fill(trustStorePassword, '\0');
		}
		SSLContext context = null;
		try {
			context = SslContextHelper.getSslContext(
					this.properties.getValue(
							SslPropertySpecConstants.SSL_PROTOCOL),
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
		throw new UnsupportedOperationException();
	}
	
	@Override
	public Socket newSocket(
			final Socket socket, 
			final String host, 
			final int port, 
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
				socket, host, port,	autoClose);
		CommaSeparatedValues enabledCipherSuites = this.properties.getValue(
				SslPropertySpecConstants.SSL_ENABLED_CIPHER_SUITES);
		String[] cipherSuites = enabledCipherSuites.toArray();
		if (cipherSuites.length > 0) {
			sslSocket.setEnabledCipherSuites(cipherSuites);
		}
		CommaSeparatedValues enabledProtocols = this.properties.getValue(
				SslPropertySpecConstants.SSL_ENABLED_PROTOCOLS);
		String[] protocols = enabledProtocols.toArray();
		if (protocols.length > 0) {
			sslSocket.setEnabledProtocols(protocols);
		}
		return sslSocket;
	}

}
