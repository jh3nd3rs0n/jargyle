package jargyle.net;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

public abstract class SocketInterfaceFactory {

	public abstract SocketInterface newSocketInterface();
	
	public abstract SocketInterface newSocketInterface(
			InetAddress address, int port) throws IOException;
	
	public abstract SocketInterface newSocketInterface(
			InetAddress address, 
			int port, 
			InetAddress localAddr, 
			int localPort) throws IOException;
	
	public abstract SocketInterface newSocketInterface(
			String host, int port) throws UnknownHostException, IOException;
	
	public abstract SocketInterface newSocketInterface(
			String host, 
			int port, 
			InetAddress localAddr, 
			int localPort) throws IOException;
	
}
