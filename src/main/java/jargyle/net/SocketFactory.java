package jargyle.net;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

public abstract class SocketFactory {

	public abstract Socket newSocket();
	
	public abstract Socket newSocket(
			final InetAddress address, final int port) throws IOException;
	
	public abstract Socket newSocket(
			final InetAddress address, 
			final int port, 
			final InetAddress localAddr, 
			final int localPort) throws IOException;
	
	public abstract Socket newSocket(
			final String host, 
			final int port) throws UnknownHostException, IOException;
	
	public abstract Socket newSocket(
			final String host, 
			final int port, 
			final InetAddress localAddr, 
			final int localPort) throws IOException;

}
