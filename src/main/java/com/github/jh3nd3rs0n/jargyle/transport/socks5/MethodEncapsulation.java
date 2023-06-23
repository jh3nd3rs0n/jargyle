package com.github.jh3nd3rs0n.jargyle.transport.socks5;

import java.io.IOException;
import java.net.DatagramSocket;
import java.net.Socket;

public abstract class MethodEncapsulation {

	public static MethodEncapsulation newNullInstance(final Socket sock) {
		return new NullMethodEncapsulation(sock);
	}
	
	public abstract DatagramSocket getDatagramSocket(
			final DatagramSocket datagramSocket) throws IOException;
	
	public abstract Socket getSocket();
	
	@Override
	public abstract String toString();
	
}
