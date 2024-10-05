package com.github.jh3nd3rs0n.jargyle.server.internal.server;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.DatagramSocket;
import java.nio.file.Files;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.concurrent.locks.ReentrantLock;

import javax.net.ssl.KeyManager;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;

import com.github.jh3nd3rs0n.jargyle.common.number.PositiveInteger;
import com.github.jh3nd3rs0n.jargyle.common.string.CommaSeparatedValues;
import com.github.jh3nd3rs0n.jargyle.internal.net.ssl.DtlsDatagramSocket;
import com.github.jh3nd3rs0n.jargyle.internal.net.ssl.DtlsDatagramSocketFactory;
import com.github.jh3nd3rs0n.jargyle.internal.net.ssl.KeyManagerHelper;
import com.github.jh3nd3rs0n.jargyle.internal.net.ssl.SslContextHelper;
import com.github.jh3nd3rs0n.jargyle.internal.net.ssl.TrustManagerHelper;
import com.github.jh3nd3rs0n.jargyle.server.Configuration;
import com.github.jh3nd3rs0n.jargyle.server.DtlsSettingSpecConstants;
import com.github.jh3nd3rs0n.jargyle.server.Settings;

final class DtlsDatagramSocketFactoryImpl extends DtlsDatagramSocketFactory {

	public static boolean isDtlsEnabled(final Configuration configuration) {
		Settings settings = configuration.getSettings();
		return settings.getLastValue(
				DtlsSettingSpecConstants.DTLS_ENABLED).booleanValue();
	}
	
	private final Configuration configuration;
	private SSLContext dtlsContext;
	private final ReentrantLock lock;
	
	public DtlsDatagramSocketFactoryImpl(final Configuration config) { 
		this.configuration = config;
		this.dtlsContext = null;
		this.lock = new ReentrantLock();
	}
	
	private SSLContext getDtlsContext() throws IOException {
		KeyManager[] keyManagers = null;
		Settings settings = this.configuration.getSettings();
		InputStream keyStoreInputStream = settings.getLastValue(
				DtlsSettingSpecConstants.DTLS_KEY_STORE_INPUT_STREAM);
		File keyStoreFile = settings.getLastValue(
				DtlsSettingSpecConstants.DTLS_KEY_STORE_FILE);
		if (keyStoreFile != null) {
			keyStoreInputStream = Files.newInputStream(keyStoreFile.toPath());
		}
		if (keyStoreInputStream != null) {
			char[] keyStorePassword = settings.getLastValue(
					DtlsSettingSpecConstants.DTLS_KEY_STORE_PASSWORD).getPassword();
			String keyStoreType = settings.getLastValue(
					DtlsSettingSpecConstants.DTLS_KEY_STORE_TYPE);
			keyManagers = KeyManagerHelper.getKeyManagers(
					keyStoreInputStream, keyStorePassword, keyStoreType);
			Arrays.fill(keyStorePassword, '\0');
		}
		SSLContext context = null;
		try {
			context = SslContextHelper.getSslContext(
					settings.getLastValue(
							DtlsSettingSpecConstants.DTLS_PROTOCOL), 
					keyManagers, 
					null);
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
		dtlsDatagramSocket.setUseClientMode(false);
		Settings settings = this.configuration.getSettings();
		CommaSeparatedValues enabledCipherSuites = settings.getLastValue(
				DtlsSettingSpecConstants.DTLS_ENABLED_CIPHER_SUITES);
		if (enabledCipherSuites != null) {
			dtlsDatagramSocket.setEnabledCipherSuites(
					enabledCipherSuites.toArray());
		}
		CommaSeparatedValues enabledProtocols = settings.getLastValue(
				DtlsSettingSpecConstants.DTLS_ENABLED_PROTOCOLS);
		if (enabledProtocols != null) {
			dtlsDatagramSocket.setEnabledProtocols(enabledProtocols.toArray());
		}
		PositiveInteger maxPacketSize = settings.getLastValue(
				DtlsSettingSpecConstants.DTLS_MAX_PACKET_SIZE);
		dtlsDatagramSocket.setMaximumPacketSize(maxPacketSize.intValue());
		return dtlsDatagramSocket;
	}

}
