package jargyle.net.socks.server.v5;

import java.io.IOException;
import java.net.DatagramSocket;
import java.net.Socket;

import jargyle.internal.net.socks.common.v5.gssapiauth.GssDatagramSocket;
import jargyle.internal.net.socks.common.v5.gssapiauth.GssSocket;

final class GssapiMethodSubnegotiationResult 
	extends MethodSubnegotiationResult {
	
	private final GssSocket socket;
	
	public GssapiMethodSubnegotiationResult(final GssSocket sock) {
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
