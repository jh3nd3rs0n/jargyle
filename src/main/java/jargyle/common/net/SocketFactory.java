package jargyle.common.net;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

public class SocketFactory {

	public static SocketFactory newInstance() {
		return new SocketFactory();
	}
	
	protected SocketFactory() { }
	
	public Socket newSocket() {
		return new Socket();
	}
	
	public Socket newSocket(
			final InetAddress address, 
			final int port) throws IOException {
		return new Socket(address, port);
	}
	
	public Socket newSocket(
			final InetAddress address, 
			final int port, 
			final InetAddress localAddr, 
			final int localPort) throws IOException {
		return new Socket(address, port, localAddr, localPort);
	}
	
	public Socket newSocket(
			final String host, 
			final int port) throws UnknownHostException, IOException {
		return new Socket(host, port);
	}
	
	public Socket newSocket(
			final String host, 
			final int port, 
			final InetAddress localAddr, 
			final int localPort) throws IOException {
		return new Socket(host, port, localAddr, localPort);
	}
	
}