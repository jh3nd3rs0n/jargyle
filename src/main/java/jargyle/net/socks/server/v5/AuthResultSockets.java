package jargyle.net.socks.server.v5;

import java.net.DatagramSocket;
import java.net.Socket;
import java.net.SocketException;

public abstract class AuthResultSockets {

	public abstract DatagramSocket getDatagramSocket(
			final DatagramSocket datagramSocket) throws SocketException;
	
	public abstract Socket getSocket();
	
}
