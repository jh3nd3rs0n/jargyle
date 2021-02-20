package jargyle.net.socks.client;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import javax.net.ssl.KeyManager;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;

import jargyle.net.ssl.CipherSuites;
import jargyle.net.ssl.KeyManagerHelper;
import jargyle.net.ssl.Protocols;
import jargyle.net.ssl.SslSocketFactory;
import jargyle.net.ssl.TrustManagerHelper;
import jargyle.security.EncryptedPassword;

final class SslSocketFactoryImpl extends SslSocketFactory {
	
	private final Properties properties;
	private SSLContext sslContext;

	public SslSocketFactoryImpl(final Properties props) {
		this.properties = props;
		this.sslContext = null;
	}
	
	private SSLContext getSslContext() throws IOException {
		if (this.sslContext == null) {
			this.sslContext = this.newSslContext();
		}
		return this.sslContext;
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
		if (!this.properties.getValue(
				PropertySpec.SSL_ENABLED, Boolean.class).booleanValue()) {
			return socket;
		}
		SSLContext sslContext = this.getSslContext();
		SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();
		SSLSocket sslSocket = (SSLSocket) sslSocketFactory.createSocket(
				socket, 
				host, 
				port, 
				autoClose); 
		CipherSuites enabledCipherSuites = this.properties.getValue(
				PropertySpec.SSL_ENABLED_CIPHER_SUITES, CipherSuites.class);
		String[] cipherSuites = enabledCipherSuites.toStringArray();
		if (cipherSuites.length > 0) {
			sslSocket.setEnabledCipherSuites(cipherSuites);
		}
		Protocols enabledProtocols = this.properties.getValue(
				PropertySpec.SSL_ENABLED_PROTOCOLS, Protocols.class);
		String[] protocols = enabledProtocols.toStringArray();
		if (protocols.length > 0) {
			sslSocket.setEnabledProtocols(protocols);
		}
		return sslSocket;
	}

	private SSLContext newSslContext() throws IOException {
		SSLContext context = null;
		String protocol = this.properties.getValue(
				PropertySpec.SSL_PROTOCOL, String.class);
		try {
			context = SSLContext.getInstance(protocol);
		} catch (NoSuchAlgorithmException e) {
			throw new AssertionError(e);
		}
		KeyManager[] keyManagers = null;
		TrustManager[] trustManagers = null;
		File keyStoreFile = this.properties.getValue(
				PropertySpec.SSL_KEY_STORE_FILE, File.class);
		if (keyStoreFile != null) {
			EncryptedPassword keyStorePassword = 
					this.properties.getValue(
							PropertySpec.SSL_KEY_STORE_PASSWORD, 
							EncryptedPassword.class);
			String keyStoreType = this.properties.getValue(
					PropertySpec.SSL_KEY_STORE_TYPE, String.class);
			keyManagers = KeyManagerHelper.newKeyManagers(
					keyStoreFile, keyStorePassword, keyStoreType);
		}
		File trustStoreFile = this.properties.getValue(
				PropertySpec.SSL_TRUST_STORE_FILE, File.class);
		if (trustStoreFile != null) {
			EncryptedPassword trustStorePassword = 
					this.properties.getValue(
							PropertySpec.SSL_TRUST_STORE_PASSWORD, 
							EncryptedPassword.class);
			String trustStoreType = this.properties.getValue(
					PropertySpec.SSL_TRUST_STORE_TYPE, String.class);
			trustManagers = TrustManagerHelper.newTrustManagers(
					trustStoreFile, trustStorePassword, trustStoreType);
		}
		try {
			context.init(keyManagers, trustManagers, new SecureRandom());
		} catch (KeyManagementException e) {
			throw new IOException(e);
		}
		return context;
	}

}
