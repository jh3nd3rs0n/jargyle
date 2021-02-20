package jargyle.net;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

public final class DefaultSocketFactory extends SocketFactory {

	public DefaultSocketFactory() { }
	
	@Override
	public Socket newSocket() {
		return new Socket();
	}

	@Override
	public Socket newSocket(
			final InetAddress address, final int port) throws IOException {
		return new Socket(address, port);
	}

	@Override
	public Socket newSocket(
			final InetAddress address, 
			final int port, 
			final InetAddress localAddr, 
			final int localPort) throws IOException {
		return new Socket(address, port, localAddr, localPort);
	}

	@Override
	public Socket newSocket(
			final String host, 
			final int port) throws UnknownHostException, IOException {
		return new Socket(host, port);
	}

	@Override
	public Socket newSocket(
			final String host, 
			final int port, 
			final InetAddress localAddr, 
			final int localPort) throws IOException {
		return new Socket(host, port, localAddr, localPort);
	}

}
