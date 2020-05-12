package jargyle.client;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;

public class ServerSocketFactory {

	public static ServerSocketFactory newInstance() {
		return newInstance(null);
	}
	
	public static ServerSocketFactory newInstance(
			final SocksClient socksClient) {
		SocksClient client = socksClient;
		if (client == null) {
			client = SocksClient.newInstance();
		}
		if (client != null) {
			return client.newServerSocketFactory();
		}
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
