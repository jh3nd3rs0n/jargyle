package com.github.jh3nd3rs0n.jargyle.net.socks.transport.v5.gssapiauth;

import java.io.IOException;
import java.net.DatagramSocket;
import java.net.Socket;

import com.github.jh3nd3rs0n.jargyle.net.socks.transport.v5.MethodEncapsulation;

public final class GssapiMethodEncapsulation extends MethodEncapsulation {
	
	private final GssSocket socket;
	
	public GssapiMethodEncapsulation(final GssSocket sock) {
		this.socket = sock;
	}
	
	@Override
	public DatagramSocket getDatagramSocket(
			final DatagramSocket datagramSocket) throws IOException {
		return new GssDatagramSocket(
				datagramSocket, 
				this.socket.getGSSContext(), 
				this.socket.getMessageProp());
	}

	@Override
	public Socket getSocket() {
		return this.socket;
	}

}
