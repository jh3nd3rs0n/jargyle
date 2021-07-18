package jargyle.internal.net.socks.transport.v5;

import java.io.IOException;
import java.net.DatagramSocket;
import java.net.Socket;

import jargyle.net.socks.transport.v5.MethodEncapsulation;

public final class NullMethodEncapsulation 
	extends MethodEncapsulation {
	
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
