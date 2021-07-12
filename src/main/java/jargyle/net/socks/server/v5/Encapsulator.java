package jargyle.net.socks.server.v5;

import java.net.DatagramSocket;
import java.net.Socket;
import java.net.SocketException;

public abstract class Encapsulator {

	public abstract DatagramSocket encapsulate(
			final DatagramSocket datagramSocket) throws SocketException;
	
	public abstract Socket encapsulate(final Socket socket);
	
}
