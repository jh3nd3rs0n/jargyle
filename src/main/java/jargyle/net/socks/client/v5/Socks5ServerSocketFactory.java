package jargyle.net.socks.client.v5;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;

import jargyle.net.ServerSocketFactory;

public final class Socks5ServerSocketFactory extends ServerSocketFactory {

	private final Socks5Client socks5Client;
	
	public Socks5ServerSocketFactory(final Socks5Client client) {
		this.socks5Client = client;
	}
	
	@Override
	public ServerSocket newServerSocket() throws IOException {
		return new Socks5ServerSocket(this.socks5Client);
	}

	@Override
	public ServerSocket newServerSocket(final int port) throws IOException {
		return new Socks5ServerSocket(this.socks5Client, port);
	}

	@Override
	public ServerSocket newServerSocket(
			final int port, final int backlog) throws IOException {
		return new Socks5ServerSocket(this.socks5Client, port, backlog);
	}

	@Override
	public ServerSocket newServerSocket(
			final int port, 
			final int backlog, 
			final InetAddress bindAddr) throws IOException {
		return new Socks5ServerSocket(this.socks5Client, port, backlog, bindAddr);
	}

}
