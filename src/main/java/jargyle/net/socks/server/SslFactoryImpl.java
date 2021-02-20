package jargyle.net.socks.server;

import java.io.File;
import java.io.IOException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import javax.net.ssl.KeyManager;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;

import jargyle.net.ssl.DtlsDatagramSocketFactory;
import jargyle.net.ssl.KeyManagerHelper;
import jargyle.net.ssl.SslFactory;
import jargyle.net.ssl.SslSocketFactory;
import jargyle.net.ssl.TrustManagerHelper;
import jargyle.security.EncryptedPassword;

final class SslFactoryImpl extends SslFactory {
	
	private final Configuration configuration;
	private Configuration lastConfiguration;
	private SSLContext sslContext;	
	
	public SslFactoryImpl(final Configuration config) {
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

	@Override
	public DtlsDatagramSocketFactory newDtlsDatagramSocketFactory() 
			throws IOException {
		return new DtlsDatagramSocketFactoryImpl();
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

	@Override
	public SslSocketFactory newSslSocketFactory() throws IOException {
		return new SslSocketFactoryImpl(this.configuration, this.getSslContext());
	}

}
