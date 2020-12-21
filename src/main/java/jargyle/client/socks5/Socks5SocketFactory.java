package jargyle.client.socks5;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

import jargyle.common.net.SocketFactory;

public final class Socks5SocketFactory extends SocketFactory {

	private final Socks5Client socks5Client;
	
	public Socks5SocketFactory(final Socks5Client client) {
		this.socks5Client = client;
	}
	
	@Override
	public Socket newSocket() {
		return new Socks5Socket(this.socks5Client);
	}
	
	@Override
	public Socket newSocket(
			final InetAddress address, 
			final int port) throws IOException {
		return new Socks5Socket(this.socks5Client, address, port);
	}
	
	@Override
	public Socket newSocket(
			final InetAddress address, 
			final int port, 
			final InetAddress localAddr, 
			final int localPort) throws IOException {
		return new Socks5Socket(this.socks5Client, address, port, localAddr, localPort);
	}
	
	@Override
	public Socket newSocket(
			final String host, 
			final int port) throws UnknownHostException, IOException {
		return new Socks5Socket(this.socks5Client, host, port);
	}
	
	@Override
	public Socket newSocket(
			final String host, 
			final int port, 
			final InetAddress localAddr, 
			final int localPort) throws IOException {
		return new Socks5Socket(this.socks5Client, host, port, localAddr, localPort);
	}
	
}
