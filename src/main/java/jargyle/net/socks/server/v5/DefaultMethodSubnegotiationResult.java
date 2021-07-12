package jargyle.net.socks.server.v5;

import java.io.IOException;
import java.net.DatagramSocket;
import java.net.Socket;

final class DefaultMethodSubnegotiationResult 
	extends MethodSubnegotiationResult {
	
	private final Socket socket;
	
	public DefaultMethodSubnegotiationResult(final Socket sock) { 
		this.socket = sock;
	}
	
	@Override
	public DatagramSocket getDatagramSocket(
			final DatagramSocket datagramSocket) throws IOException {
		return datagramSocket;
	}

	@Override
	public Socket getSocket() {
		return this.socket;
	}

}
