package jargyle.net.socks.socks5.client;

import java.io.IOException;
import java.net.InetAddress;

import jargyle.net.ServerSocketInterface;
import jargyle.net.ServerSocketInterfaceFactory;

public final class Socks5ServerSocketInterfaceFactory 
	extends ServerSocketInterfaceFactory {

	private final Socks5Client socks5Client;
	
	public Socks5ServerSocketInterfaceFactory(final Socks5Client client) {
		this.socks5Client = client;
	}
	
	@Override
	public ServerSocketInterface newServerSocketInterface() throws IOException {
		return new Socks5ServerSocketInterface(this.socks5Client);
	}

	@Override
	public ServerSocketInterface newServerSocketInterface(
			int port) throws IOException {
		return new Socks5ServerSocketInterface(this.socks5Client, port);
	}

	@Override
	public ServerSocketInterface newServerSocketInterface(
			int port, int backlog) throws IOException {
		return new Socks5ServerSocketInterface(this.socks5Client, port, backlog);
	}

	@Override
	public ServerSocketInterface newServerSocketInterface(
			int port, int backlog, InetAddress bindAddr) throws IOException {
		return new Socks5ServerSocketInterface(
				this.socks5Client, port, backlog, bindAddr);
	}

}
