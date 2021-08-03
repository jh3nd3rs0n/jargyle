package com.github.jh3nd3rs0n.jargyle.internal.net.ssl;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;

final class DefaultSslSocketFactory extends SslSocketFactory {

	private final SSLContext sslContext;
	
	public DefaultSslSocketFactory(final SSLContext sslCntxt) {
		this.sslContext = sslCntxt;
	}
	
	@Override
	public Socket newSocket(
			final Socket socket, 
			final InputStream consumed, 
			final boolean autoClose) throws IOException {
		SSLSocketFactory factory = this.sslContext.getSocketFactory();
		return factory.createSocket(socket, consumed, autoClose);
	}

	@Override
	public Socket newSocket(
			final Socket socket, 
			final String host, 
			final int port, 
			final boolean autoClose) throws IOException {
		SSLSocketFactory factory = this.sslContext.getSocketFactory();
		return factory.createSocket(socket, host, port, autoClose);
	}

}
