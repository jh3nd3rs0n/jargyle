package jargyle.common.net;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;

public class ServerSocketFactory {

	public static ServerSocketFactory newInstance() {
		return new ServerSocketFactory();
	}
	
	protected ServerSocketFactory() { }
	
	public ServerSocket newServerSocket() throws IOException {
		return new ServerSocket();
	}
	
	public ServerSocket newServerSocket(final int port) throws IOException {
		return new ServerSocket(port);
	}
	
	public ServerSocket newServerSocket(
			final int port, 
			final int backlog) throws IOException {
		return new ServerSocket(port, backlog);
	}
	
	public ServerSocket newServerSocket(
			final int port, 
			final int backlog,
			final InetAddress bindAddr) throws IOException {
		return new ServerSocket(port, backlog, bindAddr);
	}
	
}
