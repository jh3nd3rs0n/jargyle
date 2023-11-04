package com.github.jh3nd3rs0n.jargyle.protocolbase.socks5;

import java.io.IOException;
import java.net.DatagramSocket;
import java.net.Socket;

final class NullMethodEncapsulation extends MethodEncapsulation {
	
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

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append(this.getClass().getSimpleName())
			.append(" [socket=")
			.append(this.socket)
			.append("]");
		return builder.toString();
	}

}
