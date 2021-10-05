package com.github.jh3nd3rs0n.jargyle.client.socks5;

import java.io.File;
import java.io.IOException;
import java.net.DatagramSocket;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;

import javax.net.ssl.KeyManager;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;

import com.github.jh3nd3rs0n.jargyle.client.DtlsPropertySpecConstants;
import com.github.jh3nd3rs0n.jargyle.client.Properties;
import com.github.jh3nd3rs0n.jargyle.client.SocksClient;
import com.github.jh3nd3rs0n.jargyle.common.net.ssl.DtlsDatagramSocket;
import com.github.jh3nd3rs0n.jargyle.common.net.ssl.DtlsDatagramSocketFactory;
import com.github.jh3nd3rs0n.jargyle.common.number.impl.PositiveInteger;
import com.github.jh3nd3rs0n.jargyle.common.security.EncryptedPassword;
import com.github.jh3nd3rs0n.jargyle.common.text.Strings;
import com.github.jh3nd3rs0n.jargyle.internal.net.ssl.KeyManagerHelper;
import com.github.jh3nd3rs0n.jargyle.internal.net.ssl.SslContextHelper;
import com.github.jh3nd3rs0n.jargyle.internal.net.ssl.TrustManagerHelper;

final class DtlsDatagramSocketFactoryImpl extends DtlsDatagramSocketFactory {
	
	public static boolean isDtlsEnabled(final Properties props) {
		return props.getValue(
				DtlsPropertySpecConstants.DTLS_ENABLED).booleanValue();
	}
	
	private SSLContext dtlsContext;
	private final SocksClient socksClient;
		
	public DtlsDatagramSocketFactoryImpl(final SocksClient client) {
		this.dtlsContext = null;
		this.socksClient = client;
	}
	
	private SSLContext getDtlsContext() throws IOException {
		KeyManager[] keyManagers = null;
		TrustManager[] trustManagers = null;
		Properties properties = this.socksClient.getProperties();
		File keyStoreFile = properties.getValue(
				DtlsPropertySpecConstants.DTLS_KEY_STORE_FILE);
		if (keyStoreFile != null) {
			EncryptedPassword keyStorePassword = properties.getValue(
					DtlsPropertySpecConstants.DTLS_KEY_STORE_PASSWORD);
			String keyStoreType = properties.getValue(
					DtlsPropertySpecConstants.DTLS_KEY_STORE_TYPE);
			keyManagers = KeyManagerHelper.getKeyManagers(
					keyStoreFile, 
					keyStorePassword.getPassword(), 
					keyStoreType);
		}
		File trustStoreFile = properties.getValue(
				DtlsPropertySpecConstants.DTLS_TRUST_STORE_FILE);
		if (trustStoreFile != null) {
			EncryptedPassword trustStorePassword = properties.getValue(
					DtlsPropertySpecConstants.DTLS_TRUST_STORE_PASSWORD);
			String trustStoreType = properties.getValue(
					DtlsPropertySpecConstants.DTLS_TRUST_STORE_TYPE);
			trustManagers = TrustManagerHelper.getTrustManagers(
					trustStoreFile, 
					trustStorePassword.getPassword(), 
					trustStoreType);
		}
		SSLContext context = null;
		try {
			context = SslContextHelper.getSslContext(
					properties.getValue(
							DtlsPropertySpecConstants.DTLS_PROTOCOL), 
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
		synchronized (this) {
			if (this.dtlsContext == null) {
				this.dtlsContext = this.getDtlsContext();
			}
		}
		DtlsDatagramSocketFactory factory = 
				DtlsDatagramSocketFactory.newInstance(this.dtlsContext);
		DtlsDatagramSocket dtlsDatagramSocket = 
				(DtlsDatagramSocket) factory.newDatagramSocket(
						datagramSocket, peerHost, peerPort);
		dtlsDatagramSocket.setUseClientMode(true);
		Properties properties = this.socksClient.getProperties();
		Strings enabledCipherSuites = properties.getValue(
				DtlsPropertySpecConstants.DTLS_ENABLED_CIPHER_SUITES);
		String[] cipherSuites = enabledCipherSuites.toStringArray();
		if (cipherSuites.length > 0) {
			dtlsDatagramSocket.setEnabledCipherSuites(cipherSuites);
		}
		Strings enabledProtocols = properties.getValue(
				DtlsPropertySpecConstants.DTLS_ENABLED_PROTOCOLS);
		String[] protocols = enabledProtocols.toStringArray();
		if (protocols.length > 0) {
			dtlsDatagramSocket.setEnabledProtocols(protocols);
		}
		PositiveInteger maxPacketSize = properties.getValue(
				DtlsPropertySpecConstants.DTLS_MAX_PACKET_SIZE);
		dtlsDatagramSocket.setMaximumPacketSize(maxPacketSize.intValue());
		return dtlsDatagramSocket;
	}

}
