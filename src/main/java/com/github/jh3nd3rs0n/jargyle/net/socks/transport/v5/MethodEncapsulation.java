package com.github.jh3nd3rs0n.jargyle.net.socks.transport.v5;

import java.io.IOException;
import java.net.DatagramSocket;
import java.net.Socket;

public abstract class MethodEncapsulation {

	public abstract DatagramSocket getDatagramSocket(
			final DatagramSocket datagramSocket) throws IOException;
	
	public abstract Socket getSocket();
	
}
