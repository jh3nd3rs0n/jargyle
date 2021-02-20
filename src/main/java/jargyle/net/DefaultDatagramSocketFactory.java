package jargyle.net;

import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketAddress;
import java.net.SocketException;

public final class DefaultDatagramSocketFactory extends DatagramSocketFactory {

	@Override
	public DatagramSocket newDatagramSocket() throws SocketException {
		return new DatagramSocket();
	}

	@Override
	public DatagramSocket newDatagramSocket(
			final int port) throws SocketException {
		return new DatagramSocket(port);
	}

	@Override
	public DatagramSocket newDatagramSocket(
			final int port, final InetAddress laddr) throws SocketException {
		return new DatagramSocket(port, laddr);
	}

	@Override
	public DatagramSocket newDatagramSocket(
			final SocketAddress bindaddr) throws SocketException {
		return new DatagramSocket(bindaddr);
	}

}
