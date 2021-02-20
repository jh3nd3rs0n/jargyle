package jargyle.net;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;

public final class DefaultServerSocketFactory extends ServerSocketFactory {

	public DefaultServerSocketFactory() { }
	
	@Override
	public ServerSocket newServerSocket() throws IOException {
		return new ServerSocket();
	}

	@Override
	public ServerSocket newServerSocket(final int port) throws IOException {
		return new ServerSocket(port);
	}

	@Override
	public ServerSocket newServerSocket(
			final int port, final int backlog) throws IOException {
		return new ServerSocket(port, backlog);
	}

	@Override
	public ServerSocket newServerSocket(
			final int port, 
			final int backlog, 
			final InetAddress bindAddr) throws IOException {
		return new ServerSocket(port, backlog, bindAddr);
	}

}
