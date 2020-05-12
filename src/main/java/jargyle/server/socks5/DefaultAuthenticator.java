package jargyle.server.socks5;

import java.io.IOException;
import java.net.Socket;

import jargyle.server.Configuration;

enum DefaultAuthenticator implements Authenticator {

	INSTANCE;
	
	@Override
	public Socket authenticate(
			final Socket socket, 
			final Configuration configuration) throws IOException {
		return socket;
	}

	@Override
	public String toString() {
		return DefaultAuthenticator.class.getSimpleName();
	}
}
