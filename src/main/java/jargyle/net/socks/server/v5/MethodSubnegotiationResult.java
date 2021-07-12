package jargyle.net.socks.server.v5;

import java.io.IOException;
import java.net.DatagramSocket;
import java.net.Socket;

public abstract class MethodSubnegotiationResult {

	public abstract DatagramSocket getDatagramSocket(
			final DatagramSocket datagramSocket) throws IOException;
	
	public abstract Socket getSocket();
	
}
