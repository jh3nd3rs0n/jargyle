package jargyle.net.socks.client.v5;

import java.net.DatagramSocket;
import java.net.Socket;
import java.net.SocketException;

import jargyle.net.socks.transport.v5.Method;

final class DefaultAuthResult extends AuthResult {

	private final Method method;
	private final Socket socket;
	
	public DefaultAuthResult(final Method meth, final Socket sock) {
		this.method = meth;
		this.socket = sock;
	}
	
	@Override
	public DatagramSocket getDatagramSocket(
			final DatagramSocket datagramSocket) throws SocketException {
		return datagramSocket;
	}

	@Override
	public Method getMethod() {
		return this.method;
	}

	@Override
	public Socket getSocket() {
		return this.socket;
	}

}
