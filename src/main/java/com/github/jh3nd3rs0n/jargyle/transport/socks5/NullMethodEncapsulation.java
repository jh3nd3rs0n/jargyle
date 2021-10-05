package com.github.jh3nd3rs0n.jargyle.transport.socks5;

import java.io.IOException;
import java.net.DatagramSocket;
import java.net.Socket;

public final class NullMethodEncapsulation extends MethodEncapsulation {
	
	private final Socket socket;
	
	public NullMethodEncapsulation(final Socket sock) { 
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
