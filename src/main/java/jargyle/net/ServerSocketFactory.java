package jargyle.net;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;

public abstract class ServerSocketFactory {

	public abstract ServerSocket newServerSocket() throws IOException;
	
	public abstract ServerSocket newServerSocket(
			final int port) throws IOException;
	
	public abstract ServerSocket newServerSocket(
			final int port, final int backlog) throws IOException;
	
	public abstract ServerSocket newServerSocket(
			final int port, 
			final int backlog, 
			final InetAddress bindAddr) throws IOException;

}
