package jargyle.net.socks.client;

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
	
	private final Properties properties;
	private SSLContext sslContext;
	
	public SslFactoryImpl(final Properties props) {
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
	public DtlsDatagramSocketFactory newDtlsDatagramSocketFactory() 
			throws IOException {
		return new DtlsDatagramSocketFactoryImpl();
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

	@Override
	public SslSocketFactory newSslSocketFactory() throws IOException {
		return new SslSocketFactoryImpl(this.properties, this.getSslContext());
	}

}
