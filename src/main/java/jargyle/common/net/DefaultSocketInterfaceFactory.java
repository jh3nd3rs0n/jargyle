package jargyle.common.net;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

public final class DefaultSocketInterfaceFactory extends SocketInterfaceFactory {
	
	public DefaultSocketInterfaceFactory() { }
	
	@Override
	public SocketInterface newSocketInterface() {
		return new DefaultSocketInterface(new Socket());
	}
	
	@Override
	public SocketInterface newSocketInterface(
			InetAddress address, int port) throws IOException {
		return new DefaultSocketInterface(new Socket(address, port));
	}
	
	@Override
	public SocketInterface newSocketInterface(
			InetAddress address, 
			int port, 
			InetAddress localAddr, 
			int localPort) throws IOException {
		return new DefaultSocketInterface(new Socket(
				address, port, localAddr, localPort));
	}
	
	@Override
	public SocketInterface newSocketInterface(
			String host, int port) throws UnknownHostException, IOException {
		return new DefaultSocketInterface(new Socket(host, port));
	}
	
	@Override
	public SocketInterface newSocketInterface(
			String host,
			int port, 
			InetAddress localAddr, 
			int localPort) throws IOException {
		return new DefaultSocketInterface(new Socket(
				host, port, localAddr, localPort));
	}
	
}
