package jargyle.net;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

public final class DirectSocketInterfaceFactory extends SocketInterfaceFactory {
	
	public DirectSocketInterfaceFactory() { }
	
	@Override
	public SocketInterface newSocketInterface() {
		return new DirectSocketInterface(new Socket());
	}
	
	@Override
	public SocketInterface newSocketInterface(
			InetAddress address, int port) throws IOException {
		return new DirectSocketInterface(new Socket(address, port));
	}
	
	@Override
	public SocketInterface newSocketInterface(
			InetAddress address, 
			int port, 
			InetAddress localAddr, 
			int localPort) throws IOException {
		return new DirectSocketInterface(new Socket(
				address, port, localAddr, localPort));
	}
	
	@Override
	public SocketInterface newSocketInterface(
			String host, int port) throws UnknownHostException, IOException {
		return new DirectSocketInterface(new Socket(host, port));
	}
	
	@Override
	public SocketInterface newSocketInterface(
			String host,
			int port, 
			InetAddress localAddr, 
			int localPort) throws IOException {
		return new DirectSocketInterface(new Socket(
				host, port, localAddr, localPort));
	}
	
}
