package jargyle.client;

import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketAddress;
import java.net.SocketException;

public class DatagramSocketFactory {

	public static DatagramSocketFactory newInstance() {
		return newInstance(null);
	}
	
	public static DatagramSocketFactory newInstance(
			final SocksClient socksClient) {
		SocksClient client = socksClient;
		if (client == null) {
			client = SocksClient.newInstance();
		}
		if (client != null) {
			return client.newDatagramSocketFactory();
		}
		return new DatagramSocketFactory();
	}
	
	protected DatagramSocketFactory() { }
	
	public DatagramSocket newDatagramSocket() throws SocketException {
		return new DatagramSocket();
	}
	
	public DatagramSocket newDatagramSocket(
			final int port) throws SocketException {
		return new DatagramSocket(port);
	}
	
	public DatagramSocket newDatagramSocket(
			final int port, 
			final InetAddress laddr) throws SocketException {
		return new DatagramSocket(port, laddr);
	}
	
	public DatagramSocket newDatagramSocket(
			final SocketAddress bindaddr) throws SocketException {
		return new DatagramSocket(bindaddr);
	}

}
