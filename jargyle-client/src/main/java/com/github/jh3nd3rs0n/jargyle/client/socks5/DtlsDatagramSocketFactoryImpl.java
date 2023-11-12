package com.github.jh3nd3rs0n.jargyle.client.socks5;

import java.io.File;
import java.io.IOException;
import java.net.DatagramSocket;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.concurrent.locks.ReentrantLock;

import javax.net.ssl.KeyManager;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;

import com.github.jh3nd3rs0n.jargyle.client.DtlsPropertySpecConstants;
import com.github.jh3nd3rs0n.jargyle.client.Properties;
import com.github.jh3nd3rs0n.jargyle.client.SocksClient;
import com.github.jh3nd3rs0n.jargyle.common.number.PositiveInteger;
import com.github.jh3nd3rs0n.jargyle.common.string.CommaSeparatedValues;
import com.github.jh3nd3rs0n.jargyle.internal.net.ssl.DtlsDatagramSocket;
import com.github.jh3nd3rs0n.jargyle.internal.net.ssl.DtlsDatagramSocketFactory;
import com.github.jh3nd3rs0n.jargyle.internal.net.ssl.KeyManagerHelper;
import com.github.jh3nd3rs0n.jargyle.internal.net.ssl.SslContextHelper;
import com.github.jh3nd3rs0n.jargyle.internal.net.ssl.TrustManagerHelper;

final class DtlsDatagramSocketFactoryImpl extends DtlsDatagramSocketFactory {
	
	public static boolean isDtlsEnabled(final Properties props) {
		return props.getValue(
				DtlsPropertySpecConstants.DTLS_ENABLED).booleanValue();
	}
	
	private SSLContext dtlsContext;
	private final ReentrantLock lock;
	private final SocksClient socksClient;
		
	public DtlsDatagramSocketFactoryImpl(final SocksClient client) {
		this.dtlsContext = null;
		this.lock = new ReentrantLock();
		this.socksClient = client;
	}
	
	private SSLContext getDtlsContext() throws IOException {
		KeyManager[] keyManagers = null;
		TrustManager[] trustManagers = null;
		Properties properties = this.socksClient.getProperties();
		File keyStoreFile = properties.getValue(
				DtlsPropertySpecConstants.DTLS_KEY_STORE_FILE);
		if (keyStoreFile != null) {
			char[] keyStorePassword = properties.getValue(
					DtlsPropertySpecConstants.DTLS_KEY_STORE_PASSWORD).getPassword();
			String keyStoreType = properties.getValue(
					DtlsPropertySpecConstants.DTLS_KEY_STORE_TYPE);
			keyManagers = KeyManagerHelper.getKeyManagers(
					keyStoreFile, keyStorePassword,	keyStoreType);
			Arrays.fill(keyStorePassword, '\0');
		}
		File trustStoreFile = properties.getValue(
				DtlsPropertySpecConstants.DTLS_TRUST_STORE_FILE);
		if (trustStoreFile != null) {
			char[] trustStorePassword = properties.getValue(
					DtlsPropertySpecConstants.DTLS_TRUST_STORE_PASSWORD).getPassword();
			String trustStoreType = properties.getValue(
					DtlsPropertySpecConstants.DTLS_TRUST_STORE_TYPE);
			trustManagers = TrustManagerHelper.getTrustManagers(
					trustStoreFile, trustStorePassword,	trustStoreType);
			Arrays.fill(trustStorePassword, '\0');
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
			final DatagramSocket datagramSocket) throws IOException {
		this.lock.lock();
		try {
			if (this.dtlsContext == null) {
				this.dtlsContext = this.getDtlsContext();
			}
		} finally {
			this.lock.unlock();
		}
		DtlsDatagramSocketFactory factory = 
				DtlsDatagramSocketFactory.newInstance(this.dtlsContext);
		DtlsDatagramSocket dtlsDatagramSocket = 
				(DtlsDatagramSocket) factory.newDatagramSocket(datagramSocket);
		dtlsDatagramSocket.setUseClientMode(true);
		Properties properties = this.socksClient.getProperties();
		CommaSeparatedValues enabledCipherSuites = properties.getValue(
				DtlsPropertySpecConstants.DTLS_ENABLED_CIPHER_SUITES);
		String[] cipherSuites = enabledCipherSuites.toArray();
		if (cipherSuites.length > 0) {
			dtlsDatagramSocket.setEnabledCipherSuites(cipherSuites);
		}
		CommaSeparatedValues enabledProtocols = properties.getValue(
				DtlsPropertySpecConstants.DTLS_ENABLED_PROTOCOLS);
		String[] protocols = enabledProtocols.toArray();
		if (protocols.length > 0) {
			dtlsDatagramSocket.setEnabledProtocols(protocols);
		}
		PositiveInteger maxPacketSize = properties.getValue(
				DtlsPropertySpecConstants.DTLS_MAX_PACKET_SIZE);
		dtlsDatagramSocket.setMaximumPacketSize(maxPacketSize.intValue());
		return dtlsDatagramSocket;
	}

}
