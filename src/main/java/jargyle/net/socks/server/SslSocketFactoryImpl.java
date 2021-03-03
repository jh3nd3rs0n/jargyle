package jargyle.net.socks.server;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;

import jargyle.net.ssl.SslSocketFactory;
import jargyle.util.Strings;

final class SslSocketFactoryImpl extends SslSocketFactory {

	private final Configuration configuration;
	private Configuration lastConfiguration;
	private SSLContext sslContext;
	
	public SslSocketFactoryImpl(final Configuration config) {
		this.configuration = config;
		this.lastConfiguration = null;
		this.sslContext = null;
	}
	
	@Override
	public Socket newSocket(
			final Socket socket, 
			final InputStream consumed, 
			final boolean autoClose) throws IOException {
		if (!Configuration.equals(this.lastConfiguration, this.configuration)) {
			this.sslContext = SslContextHelper.getSslContext(
					this.configuration.getSettings().getLastValue(
							SettingSpec.SSL_PROTOCOL, String.class), 
					this.configuration);
			this.lastConfiguration = ImmutableConfiguration.newInstance(
					this.configuration);
		}
		SSLSocketFactory sslSocketFactory = this.sslContext.getSocketFactory();
		SSLSocket sslSocket = (SSLSocket) sslSocketFactory.createSocket(
				socket,	consumed, autoClose);
		Settings settings = this.configuration.getSettings();
		Strings enabledCipherSuites = settings.getLastValue(
				SettingSpec.SSL_ENABLED_CIPHER_SUITES, Strings.class);
		String[] cipherSuites = enabledCipherSuites.toStringArray();
		if (cipherSuites.length > 0) {
			sslSocket.setEnabledCipherSuites(cipherSuites);
		}
		Strings enabledProtocols = settings.getLastValue(
				SettingSpec.SSL_ENABLED_PROTOCOLS, Strings.class);
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
		return sslSocket;
	}
	
	@Override
	public Socket newSocket(
			final Socket socket, 
			final String host, 
			final int port, 
			final boolean autoClose) throws IOException {
		throw new UnsupportedOperationException();
	}

}
