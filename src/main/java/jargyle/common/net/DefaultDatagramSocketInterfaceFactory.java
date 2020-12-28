package jargyle.common.net;

import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketAddress;
import java.net.SocketException;

public final class DefaultDatagramSocketInterfaceFactory 
	extends DatagramSocketInterfaceFactory {
	
	public DefaultDatagramSocketInterfaceFactory() { }
	
	@Override
	public DatagramSocketInterface newDatagramSocketInterface() throws SocketException {
		return new DefaultDatagramSocketInterface(new DatagramSocket());
	}
	
	public DatagramSocketInterface newDatagramSocketInterface(
			int port) throws SocketException {
		return new DefaultDatagramSocketInterface(new DatagramSocket(port));
	}
	
	public DatagramSocketInterface newDatagramSocketInterface(
			int port, InetAddress laddr) throws SocketException {
		return new DefaultDatagramSocketInterface(new DatagramSocket(port, laddr));
	}
	
	public DatagramSocketInterface newDatagramSocketInterface(
			SocketAddress bindaddr) throws SocketException {
		return new DefaultDatagramSocketInterface(new DatagramSocket(bindaddr));
	}
	
}
