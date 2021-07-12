package jargyle.net.socks.client.v5;

import java.net.DatagramSocket;
import java.net.Socket;
import java.net.SocketException;

final class NullEncapsulator extends Encapsulator {
	
	public NullEncapsulator() { }
	
	@Override
	public DatagramSocket encapsulate(
			final DatagramSocket datagramSocket) throws SocketException {
		return datagramSocket;
	}

	@Override
	public Socket encapsulate(final Socket socket) {
		return socket;
	}

}
