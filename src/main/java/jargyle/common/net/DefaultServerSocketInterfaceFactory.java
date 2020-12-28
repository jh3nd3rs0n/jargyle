package jargyle.common.net;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;

public final class DefaultServerSocketInterfaceFactory 
	extends ServerSocketInterfaceFactory {
	
	public DefaultServerSocketInterfaceFactory() { }
	
	@Override
	public ServerSocketInterface newServerSocketInterface() throws IOException {
		return new DefaultServerSocketInterface(new ServerSocket());
	}

	@Override
	public ServerSocketInterface newServerSocketInterface(
			int port) throws IOException {
		return new DefaultServerSocketInterface(new ServerSocket(port));
	}

	@Override
	public ServerSocketInterface newServerSocketInterface(
			int port, int backlog) throws IOException {
		return new DefaultServerSocketInterface(new ServerSocket(port, backlog));
	}

	@Override
	public ServerSocketInterface newServerSocketInterface(
			int port, int backlog, InetAddress bindAddr) throws IOException {
		return new DefaultServerSocketInterface(new ServerSocket(
				port, backlog, bindAddr));
	}

}
