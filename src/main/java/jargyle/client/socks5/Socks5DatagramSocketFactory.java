package jargyle.client.socks5;

import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketAddress;
import java.net.SocketException;

import jargyle.client.DatagramSocketFactory;

public final class Socks5DatagramSocketFactory extends DatagramSocketFactory {

	private final Socks5Client socks5Client;
	
	public Socks5DatagramSocketFactory(final Socks5Client client) {
		this.socks5Client = client;
	}
	
	@Override
	public DatagramSocket newDatagramSocket() throws SocketException {
		return new Socks5DatagramSocket(this.socks5Client);
	}
	
	@Override
	public DatagramSocket newDatagramSocket(
			final int port) throws SocketException {
		return new Socks5DatagramSocket(this.socks5Client, port);
	}
	
	@Override
	public DatagramSocket newDatagramSocket(
			final int port, 
			final InetAddress laddr) throws SocketException {
		return new Socks5DatagramSocket(this.socks5Client, port, laddr);
	}
	
	@Override
	public DatagramSocket newDatagramSocket(
			final SocketAddress bindaddr) throws SocketException {
		return new Socks5DatagramSocket(this.socks5Client, bindaddr);
	}
	
}
