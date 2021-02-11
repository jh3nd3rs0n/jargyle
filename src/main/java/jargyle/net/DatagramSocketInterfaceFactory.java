package jargyle.net;

import java.net.InetAddress;
import java.net.SocketAddress;
import java.net.SocketException;

public abstract class DatagramSocketInterfaceFactory {

	public abstract DatagramSocketInterface newDatagramSocketInterface() 
			throws SocketException;
	
	public abstract DatagramSocketInterface newDatagramSocketInterface(
			int port) throws SocketException;
	
	public abstract DatagramSocketInterface newDatagramSocketInterface(
			int port, InetAddress laddr) throws SocketException;
	
	public abstract DatagramSocketInterface newDatagramSocketInterface(
			SocketAddress bindaddr) throws SocketException;
	
}
