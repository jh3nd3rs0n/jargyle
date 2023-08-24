package com.github.jh3nd3rs0n.jargyle.internal.net.ssl;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;

import javax.net.ssl.SSLContext;

public abstract class SslSocketFactory {

	public static SslSocketFactory newInstance(final SSLContext sslContext) {
		return new DefaultSslSocketFactory(sslContext);
	}
	
	public abstract Socket newSocket(
			final Socket socket, 
			final InputStream consumed, 
			final boolean autoClose) throws IOException;
	
	public abstract Socket newSocket(
			final Socket socket, 
			final String host, 
			final int port, 
			final boolean autoClose) throws IOException;
	
}
