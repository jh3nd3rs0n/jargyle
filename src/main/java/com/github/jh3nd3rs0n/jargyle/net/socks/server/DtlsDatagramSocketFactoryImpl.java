package com.github.jh3nd3rs0n.jargyle.net.socks.server;

import java.io.File;
import java.io.IOException;
import java.net.DatagramSocket;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;

import javax.net.ssl.KeyManager;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;

import com.github.jh3nd3rs0n.jargyle.internal.net.ssl.KeyManagerHelper;
import com.github.jh3nd3rs0n.jargyle.internal.net.ssl.SslContextHelper;
import com.github.jh3nd3rs0n.jargyle.internal.net.ssl.TrustManagerHelper;
import com.github.jh3nd3rs0n.jargyle.net.ssl.DtlsDatagramSocket;
import com.github.jh3nd3rs0n.jargyle.net.ssl.DtlsDatagramSocketFactory;
import com.github.jh3nd3rs0n.jargyle.security.EncryptedPassword;
import com.github.jh3nd3rs0n.jargyle.util.PositiveInteger;
import com.github.jh3nd3rs0n.jargyle.util.Strings;

final class DtlsDatagramSocketFactoryImpl extends DtlsDatagramSocketFactory {

	private final Configuration configuration;
	private SSLContext dtlsContext;
	private Configuration lastConfiguration;
	
	public DtlsDatagramSocketFactoryImpl(final Configuration config) { 
		this.configuration = config;
		this.dtlsContext = null;
		this.lastConfiguration = null;
	}
	
	private SSLContext getDtlsContext() throws IOException {
		KeyManager[] keyManagers = null;
		TrustManager[] trustManagers = null;
		Settings settings = this.configuration.getSettings();
		File keyStoreFile = settings.getLastValue(
				SettingSpec.DTLS_KEY_STORE_FILE);
		if (keyStoreFile != null) {
			EncryptedPassword keyStorePassword = settings.getLastValue(
					SettingSpec.DTLS_KEY_STORE_PASSWORD);
			String keyStoreType = settings.getLastValue(
					SettingSpec.DTLS_KEY_STORE_TYPE);
			keyManagers = KeyManagerHelper.getKeyManagers(
					keyStoreFile, 
					keyStorePassword.getPassword(), 
					keyStoreType);
		}
		File trustStoreFile = settings.getLastValue(
				SettingSpec.DTLS_TRUST_STORE_FILE);
		if (trustStoreFile != null) {
			EncryptedPassword trustStorePassword = settings.getLastValue(
					SettingSpec.DTLS_TRUST_STORE_PASSWORD);
			String trustStoreType = settings.getLastValue(
					SettingSpec.DTLS_TRUST_STORE_TYPE);			
			trustManagers = TrustManagerHelper.getTrustManagers(
					trustStoreFile, 
					trustStorePassword.getPassword(), 
					trustStoreType);
		}
		SSLContext context = null;
		try {
			context = SslContextHelper.getSslContext(
					settings.getLastValue(SettingSpec.DTLS_PROTOCOL), 
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
	public DatagramSocket newDatagramSocket(
			final DatagramSocket datagramSocket, 
			final String peerHost, 
			final int peerPort)
			throws IOException {
		if (!ConfigurationsHelper.equals(
				this.lastConfiguration, this.configuration)) {
			this.dtlsContext = this.getDtlsContext();
			this.lastConfiguration = ImmutableConfiguration.newInstance(
					this.configuration);
		}
		DtlsDatagramSocketFactory factory = 
				DtlsDatagramSocketFactory.newInstance(this.dtlsContext);
		DtlsDatagramSocket dtlsDatagramSocket = 
				(DtlsDatagramSocket) factory.newDatagramSocket(
						datagramSocket, peerHost, peerPort);		
		dtlsDatagramSocket.setUseClientMode(false);
		Settings settings = this.configuration.getSettings();
		Strings enabledCipherSuites = settings.getLastValue(
				SettingSpec.DTLS_ENABLED_CIPHER_SUITES);
		String[] cipherSuites = enabledCipherSuites.toStringArray();
		if (cipherSuites.length > 0) {
			dtlsDatagramSocket.setEnabledCipherSuites(cipherSuites);
		}
		Strings enabledProtocols = settings.getLastValue(
				SettingSpec.DTLS_ENABLED_PROTOCOLS);
		String[] protocols = enabledProtocols.toStringArray();
		if (protocols.length > 0) {
			dtlsDatagramSocket.setEnabledProtocols(protocols);
		}
		PositiveInteger maxPacketSize = settings.getLastValue(
				SettingSpec.DTLS_MAX_PACKET_SIZE);
		dtlsDatagramSocket.setMaximumPacketSize(maxPacketSize.intValue());
		if (settings.getLastValue(
				SettingSpec.DTLS_NEED_CLIENT_AUTH).booleanValue()) {
			dtlsDatagramSocket.setNeedClientAuth(true);
		}
		if (settings.getLastValue(
				SettingSpec.DTLS_WANT_CLIENT_AUTH).booleanValue()) {
			dtlsDatagramSocket.setWantClientAuth(true);
		}		
		return dtlsDatagramSocket;
	}

}
