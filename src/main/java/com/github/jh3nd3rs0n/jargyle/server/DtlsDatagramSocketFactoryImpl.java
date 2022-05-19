package com.github.jh3nd3rs0n.jargyle.server;

import java.io.File;
import java.io.IOException;
import java.net.DatagramSocket;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

import javax.net.ssl.KeyManager;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;

import com.github.jh3nd3rs0n.jargyle.common.net.ssl.DtlsDatagramSocket;
import com.github.jh3nd3rs0n.jargyle.common.net.ssl.DtlsDatagramSocketFactory;
import com.github.jh3nd3rs0n.jargyle.common.number.PositiveInteger;
import com.github.jh3nd3rs0n.jargyle.common.text.Words;
import com.github.jh3nd3rs0n.jargyle.internal.net.ssl.KeyManagerHelper;
import com.github.jh3nd3rs0n.jargyle.internal.net.ssl.SslContextHelper;
import com.github.jh3nd3rs0n.jargyle.internal.net.ssl.TrustManagerHelper;

final class DtlsDatagramSocketFactoryImpl extends DtlsDatagramSocketFactory {

	public static boolean isDtlsEnabled(final Configuration configuration) {
		Settings settings = configuration.getSettings();
		return settings.getLastValue(
				DtlsSettingSpecConstants.DTLS_ENABLED).booleanValue();
	}
	
	private final Configuration configuration;
	private SSLContext dtlsContext;
	
	public DtlsDatagramSocketFactoryImpl(final Configuration config) { 
		this.configuration = config;
		this.dtlsContext = null;
	}
	
	private SSLContext getDtlsContext() throws IOException {
		KeyManager[] keyManagers = null;
		TrustManager[] trustManagers = null;
		Settings settings = this.configuration.getSettings();
		File keyStoreFile = settings.getLastValue(
				DtlsSettingSpecConstants.DTLS_KEY_STORE_FILE);
		if (keyStoreFile != null) {
			char[] keyStorePassword = settings.getLastValue(
					DtlsSettingSpecConstants.DTLS_KEY_STORE_PASSWORD).getPassword();
			String keyStoreType = settings.getLastValue(
					DtlsSettingSpecConstants.DTLS_KEY_STORE_TYPE);
			keyManagers = KeyManagerHelper.getKeyManagers(
					keyStoreFile, keyStorePassword,	keyStoreType);
			Arrays.fill(keyStorePassword, '\0');
		}
		File trustStoreFile = settings.getLastValue(
				DtlsSettingSpecConstants.DTLS_TRUST_STORE_FILE);
		if (trustStoreFile != null) {
			char[] trustStorePassword = settings.getLastValue(
					DtlsSettingSpecConstants.DTLS_TRUST_STORE_PASSWORD).getPassword();
			String trustStoreType = settings.getLastValue(
					DtlsSettingSpecConstants.DTLS_TRUST_STORE_TYPE);			
			trustManagers = TrustManagerHelper.getTrustManagers(
					trustStoreFile,	trustStorePassword,	trustStoreType);
			Arrays.fill(trustStorePassword, '\0');
		}
		SSLContext context = null;
		try {
			context = SslContextHelper.getSslContext(
					settings.getLastValue(
							DtlsSettingSpecConstants.DTLS_PROTOCOL), 
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
			final DatagramSocket datagramSocket) throws IOException {
		synchronized (this) {
			if (this.dtlsContext == null) {
				this.dtlsContext = this.getDtlsContext();
			}
		}
		DtlsDatagramSocketFactory factory = 
				DtlsDatagramSocketFactory.newInstance(this.dtlsContext);
		DtlsDatagramSocket dtlsDatagramSocket = 
				(DtlsDatagramSocket) factory.newDatagramSocket(datagramSocket);		
		dtlsDatagramSocket.setUseClientMode(false);
		Settings settings = this.configuration.getSettings();
		Words enabledCipherSuites = settings.getLastValue(
				DtlsSettingSpecConstants.DTLS_ENABLED_CIPHER_SUITES);
		String[] cipherSuites = enabledCipherSuites.toStringArray();
		if (cipherSuites.length > 0) {
			dtlsDatagramSocket.setEnabledCipherSuites(cipherSuites);
		}
		Words enabledProtocols = settings.getLastValue(
				DtlsSettingSpecConstants.DTLS_ENABLED_PROTOCOLS);
		String[] protocols = enabledProtocols.toStringArray();
		if (protocols.length > 0) {
			dtlsDatagramSocket.setEnabledProtocols(protocols);
		}
		PositiveInteger maxPacketSize = settings.getLastValue(
				DtlsSettingSpecConstants.DTLS_MAX_PACKET_SIZE);
		dtlsDatagramSocket.setMaximumPacketSize(maxPacketSize.intValue());
		if (settings.getLastValue(
				DtlsSettingSpecConstants.DTLS_NEED_CLIENT_AUTH).booleanValue()) {
			dtlsDatagramSocket.setNeedClientAuth(true);
		}
		if (settings.getLastValue(
				DtlsSettingSpecConstants.DTLS_WANT_CLIENT_AUTH).booleanValue()) {
			dtlsDatagramSocket.setWantClientAuth(true);
		}		
		return dtlsDatagramSocket;
	}

}
