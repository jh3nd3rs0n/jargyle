package jargyle.net.socks.server;

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

import jargyle.internal.net.ssl.KeyManagerHelper;
import jargyle.internal.net.ssl.SslContextHelper;
import jargyle.internal.net.ssl.SslSocketFactory;
import jargyle.internal.net.ssl.TrustManagerHelper;
import jargyle.security.EncryptedPassword;
import jargyle.util.Strings;

final class SslSocketFactoryImpl extends SslSocketFactory {

	private final Configuration configuration;
	private Configuration lastConfiguration;
	private SSLContext sslContext;
	
	public SslSocketFactoryImpl(final Configuration config) {
		this.configuration = config;
		this.lastConfiguration = null;
		this.sslContext = null;
	}
	
	private SSLContext getSslContext() throws IOException {
		KeyManager[] keyManagers = null;
		TrustManager[] trustManagers = null;
		Settings settings = this.configuration.getSettings();
		File keyStoreFile = settings.getLastValue(
				SettingSpec.SSL_KEY_STORE_FILE);
		if (keyStoreFile != null) {
			EncryptedPassword keyStorePassword = settings.getLastValue(
					SettingSpec.SSL_KEY_STORE_PASSWORD);
			String keyStoreType = settings.getLastValue(
					SettingSpec.SSL_KEY_STORE_TYPE);
			keyManagers = KeyManagerHelper.getKeyManagers(
					keyStoreFile, keyStorePassword, keyStoreType);
		}
		File trustStoreFile = settings.getLastValue(
				SettingSpec.SSL_TRUST_STORE_FILE);
		if (trustStoreFile != null) {
			EncryptedPassword trustStorePassword = settings.getLastValue(
					SettingSpec.SSL_TRUST_STORE_PASSWORD);
			String trustStoreType = settings.getLastValue(
					SettingSpec.SSL_TRUST_STORE_TYPE);			
			trustManagers = TrustManagerHelper.getTrustManagers(
					trustStoreFile, trustStorePassword, trustStoreType);
		}
		SSLContext context = null;
		try {
			context = SslContextHelper.getSslContext(
					settings.getLastValue(SettingSpec.SSL_PROTOCOL), 
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
		if (!ConfigurationsHelper.equals(
				this.lastConfiguration, this.configuration)) {
			this.sslContext = this.getSslContext();
			this.lastConfiguration = ImmutableConfiguration.newInstance(
					this.configuration);
		}
		SslSocketFactory factory = SslSocketFactory.newInstance(
				this.sslContext);
		SSLSocket sslSocket = (SSLSocket) factory.newSocket(
				socket,	consumed, autoClose);
		Settings settings = this.configuration.getSettings();
		Strings enabledCipherSuites = settings.getLastValue(
				SettingSpec.SSL_ENABLED_CIPHER_SUITES);
		String[] cipherSuites = enabledCipherSuites.toStringArray();
		if (cipherSuites.length > 0) {
			sslSocket.setEnabledCipherSuites(cipherSuites);
		}
		Strings enabledProtocols = settings.getLastValue(
				SettingSpec.SSL_ENABLED_PROTOCOLS);
		String[] protocols = enabledProtocols.toStringArray();
		if (protocols.length > 0) {
			sslSocket.setEnabledProtocols(protocols);
		}
		if (settings.getLastValue(
				SettingSpec.SSL_NEED_CLIENT_AUTH).booleanValue()) {
			sslSocket.setNeedClientAuth(true);
		}
		if (settings.getLastValue(
				SettingSpec.SSL_WANT_CLIENT_AUTH).booleanValue()) {
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
