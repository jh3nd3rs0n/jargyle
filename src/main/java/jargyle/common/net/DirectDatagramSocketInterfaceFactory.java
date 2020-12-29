package jargyle.common.net;

import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketAddress;
import java.net.SocketException;

public final class DirectDatagramSocketInterfaceFactory 
	extends DatagramSocketInterfaceFactory {
	
	public DirectDatagramSocketInterfaceFactory() { }
	
	@Override
	public DatagramSocketInterface newDatagramSocketInterface() throws SocketException {
		return new DirectDatagramSocketInterface(new DatagramSocket());
	}
	
	public DatagramSocketInterface newDatagramSocketInterface(
			int port) throws SocketException {
		return new DirectDatagramSocketInterface(new DatagramSocket(port));
	}
	
	public DatagramSocketInterface newDatagramSocketInterface(
			int port, InetAddress laddr) throws SocketException {
		return new DirectDatagramSocketInterface(new DatagramSocket(port, laddr));
	}
	
	public DatagramSocketInterface newDatagramSocketInterface(
			SocketAddress bindaddr) throws SocketException {
		return new DirectDatagramSocketInterface(new DatagramSocket(bindaddr));
	}
	
}
