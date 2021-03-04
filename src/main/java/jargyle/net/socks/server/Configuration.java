package jargyle.net.socks.server;

import java.io.File;
import java.io.IOException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.Objects;

import javax.net.ssl.KeyManager;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;

import jargyle.net.ssl.KeyManagerHelper;
import jargyle.net.ssl.SslContextHelper;
import jargyle.net.ssl.TrustManagerHelper;
import jargyle.security.EncryptedPassword;

public abstract class Configuration {

	public static boolean equals(
			final Configuration config1, final Configuration config2) {
		if (config1 == null) {
			if (config2 != null) {
				return false;
			}
		} else {
			if (config2 == null) {
				return false;
			} else {
				if (!Objects.equals(config1.getSettings(), config2.getSettings())) {
					return false;
				}
			}
		}
		return true;
	}
	
	public Configuration() { }
	
	public abstract Settings getSettings();
	
	public final SSLContext getSslContext(
			final String protocol) throws IOException {
		Settings settings = this.getSettings();
		KeyManager[] keyManagers = null;
		TrustManager[] trustManagers = null;
		File keyStoreFile = settings.getLastValue(
				SettingSpec.SSL_KEY_STORE_FILE, File.class);
		if (keyStoreFile != null) {
			EncryptedPassword keyStorePassword = settings.getLastValue(
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
			EncryptedPassword trustStorePassword = settings.getLastValue(
					SettingSpec.SSL_TRUST_STORE_PASSWORD,
					EncryptedPassword.class);
			String trustStoreType = settings.getLastValue(
					SettingSpec.SSL_TRUST_STORE_TYPE, String.class);			
			trustManagers = TrustManagerHelper.newTrustManagers(
					trustStoreFile, trustStorePassword, trustStoreType);
		}
		SSLContext context = null;
		try {
			context = SslContextHelper.getSslContext(
					protocol, keyManagers, trustManagers);
		} catch (KeyManagementException e) {
			throw new IOException(e);
		} catch (NoSuchAlgorithmException e) {
			throw new IllegalArgumentException(e);
		}
		return context;		
	}

}