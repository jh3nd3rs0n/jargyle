package jargyle.net;

import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketAddress;
import java.net.SocketException;

public abstract class DatagramSocketFactory {

	public abstract DatagramSocket newDatagramSocket() throws SocketException;
	
	public abstract DatagramSocket newDatagramSocket(
			final int port) throws SocketException;
	
	public abstract DatagramSocket newDatagramSocket(
			final int port, final InetAddress laddr) throws SocketException;
	
	public abstract DatagramSocket newDatagramSocket(
			final SocketAddress bindaddr) throws SocketException;

}
