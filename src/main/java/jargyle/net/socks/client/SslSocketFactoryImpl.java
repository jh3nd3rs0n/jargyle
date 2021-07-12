package jargyle.net.socks.client;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;

import javax.net.ssl.KeyManager;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.TrustManager;

import jargyle.net.ssl.KeyManagerHelper;
import jargyle.net.ssl.SslContextHelper;
import jargyle.net.ssl.SslSocketFactory;
import jargyle.net.ssl.TrustManagerHelper;
import jargyle.security.EncryptedPassword;
import jargyle.util.Strings;

final class SslSocketFactoryImpl extends SslSocketFactory {

	private final SocksClient socksClient;
	private SSLContext sslContext;

	public SslSocketFactoryImpl(final SocksClient client) {
		this.socksClient = client;
		this.sslContext = null;
	}

	private SSLContext getSslContext() throws IOException {
		KeyManager[] keyManagers = null;
		TrustManager[] trustManagers = null;
		Properties properties = this.socksClient.getProperties();
		File keyStoreFile = properties.getValue(
				PropertySpec.SSL_KEY_STORE_FILE);
		if (keyStoreFile != null) {
			EncryptedPassword keyStorePassword = properties.getValue(
					PropertySpec.SSL_KEY_STORE_PASSWORD);
			String keyStoreType = properties.getValue(
					PropertySpec.SSL_KEY_STORE_TYPE);
			keyManagers = KeyManagerHelper.getKeyManagers(
					keyStoreFile, keyStorePassword, keyStoreType);
		}
		File trustStoreFile = properties.getValue(
				PropertySpec.SSL_TRUST_STORE_FILE);
		if (trustStoreFile != null) {
			EncryptedPassword trustStorePassword = properties.getValue(
					PropertySpec.SSL_TRUST_STORE_PASSWORD);
			String trustStoreType = properties.getValue(
					PropertySpec.SSL_TRUST_STORE_TYPE);
			trustManagers = TrustManagerHelper.getTrustManagers(
					trustStoreFile, trustStorePassword, trustStoreType);
		}
		SSLContext context = null;
		try {
			context = SslContextHelper.getSslContext(
					properties.getValue(PropertySpec.SSL_PROTOCOL), 
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
		if (this.sslContext == null) {
			this.sslContext = this.getSslContext();
		}
		SslSocketFactory factory = SslSocketFactory.newInstance(
				this.sslContext);
		SSLSocket sslSocket = (SSLSocket) factory.newSocket(
				socket, host, port,	autoClose);
		Properties properties = this.socksClient.getProperties();
		Strings enabledCipherSuites = properties.getValue(
				PropertySpec.SSL_ENABLED_CIPHER_SUITES);
		String[] cipherSuites = enabledCipherSuites.toStringArray();
		if (cipherSuites.length > 0) {
			sslSocket.setEnabledCipherSuites(cipherSuites);
		}
		Strings enabledProtocols = properties.getValue(
				PropertySpec.SSL_ENABLED_PROTOCOLS);
		String[] protocols = enabledProtocols.toStringArray();
		if (protocols.length > 0) {
			sslSocket.setEnabledProtocols(protocols);
		}
		return sslSocket;
	}

}
