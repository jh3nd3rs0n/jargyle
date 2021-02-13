package jargyle.net.socks.socks5.client;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

import jargyle.net.SocketInterface;
import jargyle.net.SocketInterfaceFactory;

public final class Socks5SocketInterfaceFactory extends SocketInterfaceFactory {

	private final Socks5Client socks5Client;
	
	public Socks5SocketInterfaceFactory(final Socks5Client client) {
		this.socks5Client = client;
	}
	
	@Override
	public SocketInterface newSocketInterface() {
		return new Socks5SocketInterface(this.socks5Client);
	}

	@Override
	public SocketInterface newSocketInterface(
			InetAddress address, int port) throws IOException {
		return new Socks5SocketInterface(this.socks5Client, address, port);
	}

	@Override
	public SocketInterface newSocketInterface(
			InetAddress address, 
			int port, 
			InetAddress localAddr, 
			int localPort) throws IOException {
		return new Socks5SocketInterface(
				this.socks5Client, address, port, localAddr, localPort);
	}

	@Override
	public SocketInterface newSocketInterface(
			String host, int port) throws UnknownHostException, IOException {
		return new Socks5SocketInterface(this.socks5Client, host, port);
	}

	@Override
	public SocketInterface newSocketInterface(
			String host, 
			int port, 
			InetAddress localAddr, 
			int localPort) throws IOException {
		return new Socks5SocketInterface(
				this.socks5Client, host, port, localAddr, localPort);
	}

}
