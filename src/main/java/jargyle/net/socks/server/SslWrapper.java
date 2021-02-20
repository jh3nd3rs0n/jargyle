package jargyle.net.socks.server;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.DatagramSocket;
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
import jargyle.net.ssl.TrustManagerHelper;
import jargyle.security.EncryptedPassword;

public final class SslWrapper {
	
	private final Configuration configuration;
	private Configuration lastConfiguration;
	private SSLContext sslContext;	
	
	SslWrapper(final Configuration config) {
		this.configuration = config;
		this.lastConfiguration = null;
		this.sslContext = null;		
	}
	
	private SSLContext getSslContext() throws IOException {
		if (!Configuration.equals(this.lastConfiguration, this.configuration)) {
			this.sslContext = this.newSslContext();
			this.lastConfiguration = ImmutableConfiguration.newInstance(
					this.configuration);
		}
		return this.sslContext;
	}
	
	private SSLContext newSslContext() throws IOException {
		SSLContext context = null;
		Settings settings = this.configuration.getSettings();
		String protocol = settings.getLastValue(
				SettingSpec.SSL_PROTOCOL, String.class);
		try {
			context = SSLContext.getInstance(protocol);
		} catch (NoSuchAlgorithmException e) {
			throw new AssertionError(e);
		}
		KeyManager[] keyManagers = null;
		TrustManager[] trustManagers = null;
		File keyStoreFile = settings.getLastValue(
				SettingSpec.SSL_KEY_STORE_FILE, File.class);
		if (keyStoreFile != null) {
			EncryptedPassword keyStorePassword = 
					settings.getLastValue(
							SettingSpec.SSL_KEY_STORE_PASSWORD, 
							EncryptedPassword.class);
			String keyStoreType = settings.getLastValue(
					SettingSpec.SSL_KEY_STORE_TYPE, String.class);
			keyManagers = KeyManagerHelper.newKeyManagers(
					keyStoreFile, keyStorePassword, keyStoreType);
		}
		File trustStoreFile = settings.getLastValue(
				SettingSpec.SSL_TRUST_STORE_FILE, File.class);
		if (trustStoreFile != null) {
			EncryptedPassword trustStorePassword =
					settings.getLastValue(
							SettingSpec.SSL_TRUST_STORE_PASSWORD, 
							EncryptedPassword.class);
			String trustStoreType = settings.getLastValue(
					SettingSpec.SSL_TRUST_STORE_TYPE, String.class);			
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
	
	public DatagramSocket wrapIfSslEnabled(
			final DatagramSocket datagramSocket, 
			final String peerHost, 
			final int peerPort) 
			throws IOException {
		/*
		Settings settings = this.configuration.getSettings();
		if (!settings.getLastValue(
				SettingSpec.SSL_ENABLED, Boolean.class).booleanValue()) {
			return datagramSocket;
		}
		// TODO DtlsDatagramSocket
		*/
		return datagramSocket;			
	}
	
	public Socket wrapIfSslEnabled(
			final Socket socket, 
			final InputStream consumed, 
			final boolean autoClose) throws IOException {
		Settings settings = this.configuration.getSettings();
		if (!settings.getLastValue(
				SettingSpec.SSL_ENABLED, Boolean.class).booleanValue()) {
			return socket;
		}
		SSLContext sslContext = this.getSslContext();
		SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();
		SSLSocket sslSocket = (SSLSocket) sslSocketFactory.createSocket(
				socket,	consumed, autoClose); 
		CipherSuites enabledCipherSuites = settings.getLastValue(
				SettingSpec.SSL_ENABLED_CIPHER_SUITES, CipherSuites.class);
		String[] cipherSuites = enabledCipherSuites.toStringArray();
		if (cipherSuites.length > 0) {
			sslSocket.setEnabledCipherSuites(cipherSuites);
		}
		Protocols enabledProtocols = settings.getLastValue(
				SettingSpec.SSL_ENABLED_PROTOCOLS, Protocols.class);
		String[] protocols = enabledProtocols.toStringArray();
		if (protocols.length > 0) {
			sslSocket.setEnabledProtocols(protocols);
		}
		if (settings.getLastValue(SettingSpec.SSL_NEED_CLIENT_AUTH, 
				Boolean.class).booleanValue()) {
			sslSocket.setNeedClientAuth(true);
		}
		if (settings.getLastValue(SettingSpec.SSL_WANT_CLIENT_AUTH, 
				Boolean.class).booleanValue()) {
			sslSocket.setWantClientAuth(true);
		}
		return sslSocket;
	}
	
}
