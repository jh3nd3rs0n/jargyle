package jargyle.client.socks5;

import java.io.IOException;
import java.net.Socket;

interface Authenticator {

	Socket authenticate(
			final Socket socket, 
			final Socks5Client socks5Client) throws IOException;
	
}
