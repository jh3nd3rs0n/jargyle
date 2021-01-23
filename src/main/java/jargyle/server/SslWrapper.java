package jargyle.server;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Objects;

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
	private EncryptedPassword lastKeyStorePassword;
	private String lastKeyStoreType;
	private String lastProtocol;
	private File lastTrustStoreFile;
	private long lastTrustStoreFileModified;
	private EncryptedPassword lastTrustStorePassword;
	private String lastTrustStoreType;
	private SSLContext sslContext;	
	
	SslWrapper(final Configuration config) {
		this.configuration = config;
		this.lastKeyStoreFile = null;
		this.lastKeyStoreFileModified = 0L;
		this.lastKeyStorePassword = null;
		this.lastKeyStoreType = null;
		this.lastProtocol = null;
		this.lastTrustStoreFile = null;
		this.lastTrustStoreFileModified = 0L;
		this.lastTrustStorePassword = null;
		this.lastTrustStoreType = null;
		this.sslContext = null;		
	}
	
	private SSLContext getSslContext() throws IOException {
		if (this.isNewSslContextNeeded()) {
			this.sslContext = this.newSslContext();
		}
		return this.sslContext;
	}
	
	private boolean isNewSslContextNeeded() {
		if (this.sslContext == null) {
			return true;
		}
		Settings settings = this.configuration.getSettings();
		File keyStoreFile = settings.getLastValue(
				SettingSpec.SSL_KEY_STORE_FILE, File.class);
		if (!Objects.equals(this.lastKeyStoreFile, keyStoreFile)) {
			return true;
		} else {
			if (keyStoreFile != null 
					&& this.lastKeyStoreFileModified < keyStoreFile.lastModified()) {
				return true;
			}
		}
		EncryptedPassword keyStorePassword = null; 
		if (settings.containsNondefaultValue(SettingSpec.SSL_KEY_STORE_PASSWORD)) {
			keyStorePassword = settings.getLastValue(
					SettingSpec.SSL_KEY_STORE_PASSWORD, 
					EncryptedPassword.class);
		}
		if (!Objects.equals(this.lastKeyStorePassword, keyStorePassword)) {
			return true;
		}		
		String keyStoreType = settings.getLastValue(
				SettingSpec.SSL_KEY_STORE_TYPE, String.class);
		if (!Objects.equals(this.lastKeyStoreType, keyStoreType)) {
			return true;
		}
		String protocol = settings.getLastValue(
				SettingSpec.SSL_PROTOCOL, String.class);
		if (!Objects.equals(this.lastProtocol, protocol)) {
			return true;
		}
		File trustStoreFile = settings.getLastValue(
				SettingSpec.SSL_TRUST_STORE_FILE, File.class);
		if (!Objects.equals(this.lastTrustStoreFile, trustStoreFile)) {
			return true;
		} else {
			if (trustStoreFile != null 
					&& this.lastTrustStoreFileModified < trustStoreFile.lastModified()) {
				return true;
			}			
		}
		EncryptedPassword trustStorePassword = null;
		if (settings.containsNondefaultValue(SettingSpec.SSL_TRUST_STORE_PASSWORD)) {
			trustStorePassword = settings.getLastValue(
					SettingSpec.SSL_TRUST_STORE_PASSWORD,
					EncryptedPassword.class);
		}
		if (!Objects.equals(this.lastTrustStorePassword, trustStorePassword)) {
			return true;
		}
		String trustStoreType = settings.getLastValue(
				SettingSpec.SSL_TRUST_STORE_TYPE, String.class);
		if (!Objects.equals(this.lastTrustStoreType, trustStoreType)) {
			return true;
		}
		return false;		
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
		EncryptedPassword keyStorePassword = 
				settings.getLastValue(
						SettingSpec.SSL_KEY_STORE_PASSWORD, 
						EncryptedPassword.class);
		String keyStoreType = settings.getLastValue(
				SettingSpec.SSL_KEY_STORE_TYPE, String.class);
		if (keyStoreFile != null) {
			keyManagers = KeyManagersFactory.newKeyManagers(
					keyStoreFile, keyStorePassword, keyStoreType);
		}
		File trustStoreFile = settings.getLastValue(
				SettingSpec.SSL_TRUST_STORE_FILE, File.class);
		EncryptedPassword trustStorePassword =
				settings.getLastValue(
						SettingSpec.SSL_TRUST_STORE_PASSWORD, 
						EncryptedPassword.class);
		String trustStoreType = settings.getLastValue(
				SettingSpec.SSL_TRUST_STORE_TYPE, String.class);
		if (trustStoreFile != null) {
			trustManagers = TrustManagersFactory.newTrustManagers(
					trustStoreFile, trustStorePassword, trustStoreType);
		}
		try {
			context.init(keyManagers, trustManagers, new SecureRandom());
		} catch (KeyManagementException e) {
			throw new IOException(e);
		}
		this.lastKeyStoreFile = keyStoreFile;
		if (keyStoreFile != null) {
			this.lastKeyStoreFileModified = keyStoreFile.lastModified();
		}
		this.lastKeyStorePassword = settings.containsNondefaultValue(
				SettingSpec.SSL_KEY_STORE_PASSWORD) ? keyStorePassword : null;
		this.lastKeyStoreType = keyStoreType;
		this.lastProtocol = protocol;
		this.lastTrustStoreFile = trustStoreFile;
		if (trustStoreFile != null) {
			this.lastTrustStoreFileModified = trustStoreFile.lastModified();
		}
		this.lastTrustStorePassword = settings.containsNondefaultValue(
				SettingSpec.SSL_TRUST_STORE_PASSWORD) ? trustStorePassword : null;
		this.lastTrustStoreType = trustStoreType;
		return context;
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
