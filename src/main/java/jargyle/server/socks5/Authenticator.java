package jargyle.server.socks5;

import java.io.IOException;
import java.net.Socket;

import jargyle.server.Configuration;

public interface Authenticator {

	Socket authenticate(
			final Socket socket, 
			final Configuration configuration) throws IOException;
	
}
