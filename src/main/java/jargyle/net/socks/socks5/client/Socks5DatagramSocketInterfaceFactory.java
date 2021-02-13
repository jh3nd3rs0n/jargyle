package jargyle.net.socks.socks5.client;

import java.net.InetAddress;
import java.net.SocketAddress;
import java.net.SocketException;

import jargyle.net.DatagramSocketInterface;
import jargyle.net.DatagramSocketInterfaceFactory;

public final class Socks5DatagramSocketInterfaceFactory 
	extends DatagramSocketInterfaceFactory {
	
	private final Socks5Client socks5Client;
	
	public Socks5DatagramSocketInterfaceFactory(final Socks5Client client) {
		this.socks5Client = client;
	}

	@Override
	public DatagramSocketInterface newDatagramSocketInterface() throws SocketException {
		return new Socks5DatagramSocketInterface(this.socks5Client);
	}

	@Override
	public DatagramSocketInterface newDatagramSocketInterface(
			int port) throws SocketException {
		return new Socks5DatagramSocketInterface(this.socks5Client, port);
	}

	@Override
	public DatagramSocketInterface newDatagramSocketInterface(
			int port, InetAddress laddr) throws SocketException {
		return new Socks5DatagramSocketInterface(this.socks5Client, port, laddr);
	}

	@Override
	public DatagramSocketInterface newDatagramSocketInterface(
			SocketAddress bindaddr) throws SocketException {
		return new Socks5DatagramSocketInterface(this.socks5Client, bindaddr);
	}

}
