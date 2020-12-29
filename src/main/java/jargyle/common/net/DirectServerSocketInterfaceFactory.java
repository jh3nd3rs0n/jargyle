package jargyle.common.net;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;

public final class DirectServerSocketInterfaceFactory 
	extends ServerSocketInterfaceFactory {
	
	public DirectServerSocketInterfaceFactory() { }
	
	@Override
	public ServerSocketInterface newServerSocketInterface() throws IOException {
		return new DirectServerSocketInterface(new ServerSocket());
	}

	@Override
	public ServerSocketInterface newServerSocketInterface(
			int port) throws IOException {
		return new DirectServerSocketInterface(new ServerSocket(port));
	}

	@Override
	public ServerSocketInterface newServerSocketInterface(
			int port, int backlog) throws IOException {
		return new DirectServerSocketInterface(new ServerSocket(port, backlog));
	}

	@Override
	public ServerSocketInterface newServerSocketInterface(
			int port, int backlog, InetAddress bindAddr) throws IOException {
		return new DirectServerSocketInterface(new ServerSocket(
				port, backlog, bindAddr));
	}

}
