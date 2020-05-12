package jargyle.client.socks5;

import java.io.IOException;
import java.net.Socket;

enum DefaultAuthenticator implements Authenticator {

	INSTANCE;

	@Override
	public Socket authenticate(
			final Socket socket, 
			final Socks5Client socks5Client) throws IOException {
		return socket;
	}

	@Override
	public String toString() {
		return DefaultAuthenticator.class.getSimpleName();
	}
	
}
