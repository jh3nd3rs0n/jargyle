package jargyle.common.net;

import java.io.IOException;
import java.net.InetAddress;

public abstract class ServerSocketInterfaceFactory {

	public abstract ServerSocketInterface newServerSocketInterface() 
			throws IOException;
	
	public abstract ServerSocketInterface newServerSocketInterface(
			int port) throws IOException;
	
	public abstract ServerSocketInterface newServerSocketInterface(
			int port, int backlog) throws IOException;
	
	public abstract ServerSocketInterface newServerSocketInterface(
			int port, int backlog, InetAddress bindAddr) throws IOException;
	
}
