package jargyle.net.socks.client.v5;

import java.io.IOException;
import java.net.DatagramSocket;
import java.net.Socket;

abstract class MethodSubnegotiationResult {

	public abstract DatagramSocket getDatagramSocket(
			final DatagramSocket datagramSocket) throws IOException;
	
	public abstract Socket getSocket();
	
}
