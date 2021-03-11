package jargyle.net.ssl;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;

public abstract class SslSocketFactory {

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
