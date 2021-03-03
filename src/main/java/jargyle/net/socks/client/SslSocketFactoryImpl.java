package jargyle.net.socks.client;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;

import jargyle.net.ssl.SslSocketFactory;
import jargyle.util.Strings;

final class SslSocketFactoryImpl extends SslSocketFactory {

	private final SocksClient socksClient;
	private SSLContext sslContext;

	public SslSocketFactoryImpl(final SocksClient client) {
		this.socksClient = client;
		this.sslContext = null;
	}

	@Override
	public Socket newSocket(
			final Socket socket, 
			final InputStream consumed, 
			final boolean autoClose) throws IOException {
		throw new UnsupportedOperationException();
	}
	
	@Override
	public Socket newSocket(
			final Socket socket, 
			final String host, 
			final int port, 
			final boolean autoClose) throws IOException {
		Properties properties = this.socksClient.getProperties();
		if (this.sslContext == null) {
			this.sslContext = this.socksClient.getSslContext(
					properties.getValue(
							PropertySpec.SSL_PROTOCOL, String.class));
		}
		SSLSocketFactory sslSocketFactory = this.sslContext.getSocketFactory();
		SSLSocket sslSocket = (SSLSocket) sslSocketFactory.createSocket(
				socket, 
				host, 
				port, 
				autoClose);
		Strings enabledCipherSuites = properties.getValue(
				PropertySpec.SSL_ENABLED_CIPHER_SUITES, Strings.class);
		String[] cipherSuites = enabledCipherSuites.toStringArray();
		if (cipherSuites.length > 0) {
			sslSocket.setEnabledCipherSuites(cipherSuites);
		}
		Strings enabledProtocols = properties.getValue(
				PropertySpec.SSL_ENABLED_PROTOCOLS, Strings.class);
		String[] protocols = enabledProtocols.toStringArray();
		if (protocols.length > 0) {
			sslSocket.setEnabledProtocols(protocols);
		}
		return sslSocket;
	}

}
