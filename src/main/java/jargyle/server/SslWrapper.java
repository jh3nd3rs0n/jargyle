package jargyle.server;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import javax.net.ssl.KeyManager;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;

import jargyle.common.net.DatagramSocketInterface;
import jargyle.common.net.DirectSocketInterface;
import jargyle.common.net.SocketInterface;
import jargyle.common.net.SocketInterfaceSocketAdapter;
import jargyle.common.net.ssl.CipherSuites;
import jargyle.common.net.ssl.KeyManagersFactory;
import jargyle.common.net.ssl.Protocols;
import jargyle.common.net.ssl.TrustManagersFactory;
import jargyle.common.security.EncryptedPassword;

public final class SslWrapper {

	private final Configuration configuration;
	private File lastKeyStoreFile;
	private long lastKeyStoreFileModified;
	private SSLContext lastSslContext;	
	private File lastTrustStoreFile;
	private long lastTrustStoreFileModified;
	
	SslWrapper(final Configuration config) {
		this.configuration = config;
		this.lastKeyStoreFile = null;
		this.lastKeyStoreFileModified = 0L;
		this.lastSslContext = null;
		this.lastTrustStoreFile = null;
		this.lastTrustStoreFileModified = 0L;		
	}
	
	private SSLContext getSslContext() {
		Settings settings = this.configuration.getSettings();
		File keyStoreFile = settings.getLastValue(
				SettingSpec.SSL_KEY_STORE_FILE, File.class);
		if (this.lastKeyStoreFile == null) {
			if (keyStoreFile != null) {
				return this.newSslContext();
			}
		} else {
			if (keyStoreFile == null) {
				return this.newSslContext();
			} else {
				if (!this.lastKeyStoreFile.equals(keyStoreFile)) {
					return this.newSslContext();
				} else {
					if (this.lastKeyStoreFileModified < keyStoreFile.lastModified()) {
						return this.newSslContext();
					}
				}
			}
		}
		File trustStoreFile = settings.getLastValue(
				SettingSpec.SSL_TRUST_STORE_FILE, File.class);
		if (this.lastTrustStoreFile == null) {
			if (trustStoreFile != null) {
				return this.newSslContext();
			}
		} else {
			if (trustStoreFile == null) {
				return this.newSslContext();
			} else {
				if (!this.lastTrustStoreFile.equals(trustStoreFile)) {
					return this.newSslContext();
				} else {
					if (this.lastTrustStoreFileModified < trustStoreFile.lastModified()) {
						return this.newSslContext();
					}
				}
			}
		}
		if (this.lastSslContext == null) {
			return this.newSslContext();
		}
		return this.lastSslContext;
	}
	
	private SSLContext newSslContext() {
		SSLContext sslContext = null;
		Settings settings = this.configuration.getSettings();
		String protocol = settings.getLastValue(
				SettingSpec.SSL_PROTOCOL, String.class);
		try {
			sslContext = SSLContext.getInstance(protocol);
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
			keyManagers = KeyManagersFactory.newKeyManagers(
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
			trustManagers = TrustManagersFactory.newTrustManagers(
					trustStoreFile, trustStorePassword, trustStoreType);
		}
		try {
			sslContext.init(keyManagers, trustManagers, new SecureRandom());
		} catch (KeyManagementException e) {
			throw new AssertionError(e);
		}
		this.lastKeyStoreFile = keyStoreFile;
		if (keyStoreFile != null) {
			this.lastKeyStoreFileModified = keyStoreFile.lastModified();
		}
		this.lastSslContext = sslContext;
		this.lastTrustStoreFile = trustStoreFile;
		if (trustStoreFile != null) {
			this.lastTrustStoreFileModified = trustStoreFile.lastModified();
		}
		return this.lastSslContext;
	}
	
	public DatagramSocketInterface wrapIfSslEnabled(
			final DatagramSocketInterface datagramSocketInterface) 
			throws IOException {
		/*
		if (!this.configuration.getSettings().getLastValue(
				SettingSpec.SSL_ENABLED, Boolean.class).booleanValue()) {
			return datagramSocketInterface;
		}
		// TODO DtlsDatagramSocketInterface
		*/
		return datagramSocketInterface;			
	}
	
	public SocketInterface wrapIfSslEnabled(
			final SocketInterface socketInterface, 
			final InputStream consumed, 
			final boolean autoClose) throws IOException {
		Settings settings = this.configuration.getSettings();
		if (!settings.getLastValue(
				SettingSpec.SSL_ENABLED, Boolean.class).booleanValue()) {
			return socketInterface;
		}
		SSLContext sslContext = this.getSslContext();
		SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();
		SSLSocket sslSocket = (SSLSocket) sslSocketFactory.createSocket(
				new SocketInterfaceSocketAdapter(socketInterface), 
				consumed, 
				autoClose); 
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
		return new DirectSocketInterface(sslSocket);
	}
	
}
