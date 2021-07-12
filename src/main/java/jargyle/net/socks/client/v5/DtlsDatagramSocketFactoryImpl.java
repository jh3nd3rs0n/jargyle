package jargyle.net.socks.client.v5;

import java.io.File;
import java.io.IOException;
import java.net.DatagramSocket;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;

import javax.net.ssl.KeyManager;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;

import jargyle.net.socks.client.Properties;
import jargyle.net.socks.client.PropertySpec;
import jargyle.net.socks.client.SocksClient;
import jargyle.net.ssl.DtlsDatagramSocket;
import jargyle.net.ssl.DtlsDatagramSocketFactory;
import jargyle.net.ssl.KeyManagerHelper;
import jargyle.net.ssl.SslContextHelper;
import jargyle.net.ssl.TrustManagerHelper;
import jargyle.security.EncryptedPassword;
import jargyle.util.PositiveInteger;
import jargyle.util.Strings;

final class DtlsDatagramSocketFactoryImpl extends DtlsDatagramSocketFactory {
	
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
				PropertySpec.DTLS_KEY_STORE_FILE);
		if (keyStoreFile != null) {
			EncryptedPassword keyStorePassword = properties.getValue(
					PropertySpec.DTLS_KEY_STORE_PASSWORD);
			String keyStoreType = properties.getValue(
					PropertySpec.DTLS_KEY_STORE_TYPE);
			keyManagers = KeyManagerHelper.getKeyManagers(
					keyStoreFile, keyStorePassword, keyStoreType);
		}
		File trustStoreFile = properties.getValue(
				PropertySpec.DTLS_TRUST_STORE_FILE);
		if (trustStoreFile != null) {
			EncryptedPassword trustStorePassword = properties.getValue(
					PropertySpec.DTLS_TRUST_STORE_PASSWORD);
			String trustStoreType = properties.getValue(
					PropertySpec.DTLS_TRUST_STORE_TYPE);
			trustManagers = TrustManagerHelper.getTrustManagers(
					trustStoreFile, trustStorePassword, trustStoreType);
		}
		SSLContext context = null;
		try {
			context = SslContextHelper.getSslContext(
					properties.getValue(PropertySpec.DTLS_PROTOCOL), 
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
		if (this.dtlsContext == null) {
			this.dtlsContext = this.getDtlsContext();
		}
		DtlsDatagramSocketFactory factory = 
				DtlsDatagramSocketFactory.newInstance(this.dtlsContext);
		DtlsDatagramSocket dtlsDatagramSocket = 
				(DtlsDatagramSocket) factory.newDatagramSocket(
						datagramSocket, peerHost, peerPort);
		dtlsDatagramSocket.setUseClientMode(true);
		Properties properties = this.socksClient.getProperties();
		Strings enabledCipherSuites = properties.getValue(
				PropertySpec.DTLS_ENABLED_CIPHER_SUITES);
		String[] cipherSuites = enabledCipherSuites.toStringArray();
		if (cipherSuites.length > 0) {
			dtlsDatagramSocket.setEnabledCipherSuites(cipherSuites);
		}
		Strings enabledProtocols = properties.getValue(
				PropertySpec.DTLS_ENABLED_PROTOCOLS);
		String[] protocols = enabledProtocols.toStringArray();
		if (protocols.length > 0) {
			dtlsDatagramSocket.setEnabledProtocols(protocols);
		}
		PositiveInteger maxPacketSize = properties.getValue(
				PropertySpec.DTLS_MAX_PACKET_SIZE);
		dtlsDatagramSocket.setMaximumPacketSize(maxPacketSize.intValue());
		return dtlsDatagramSocket;
	}

}
