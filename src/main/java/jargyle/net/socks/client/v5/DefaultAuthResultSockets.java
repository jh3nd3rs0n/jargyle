package jargyle.net.socks.client.v5;

import java.net.DatagramSocket;
import java.net.Socket;
import java.net.SocketException;

final class DefaultAuthResultSockets extends AuthResultSockets {

	private final Socket socket;
	
	public DefaultAuthResultSockets(final Socket sock) {
		this.socket = sock;
	}
	
	@Override
	public DatagramSocket getDatagramSocket(
			final DatagramSocket datagramSocket) throws SocketException {
		return datagramSocket;
	}

	@Override
	public Socket getSocket() {
		return this.socket;
	}

}
