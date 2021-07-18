package jargyle.net.socks.client.v5;

import java.io.IOException;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

import jargyle.net.HostResolver;
import jargyle.net.socks.client.SocksNetObjectFactory;

final class Socks5NetObjectFactory extends SocksNetObjectFactory {
	
	private final Socks5Client socks5Client;

	public Socks5NetObjectFactory(final Socks5Client client) {
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
			final int port, final InetAddress laddr) throws SocketException {
		return new Socks5DatagramSocket(this.socks5Client, port, laddr);
	}

	@Override
	public DatagramSocket newDatagramSocket(
			final SocketAddress bindaddr) throws SocketException {
		return new Socks5DatagramSocket(this.socks5Client, bindaddr);
	}
	
	@Override
	public HostResolver newHostResolver() {
		return new Socks5HostResolver(this.socks5Client);
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
	
	@Override
	public Socket newSocket() {
		return new Socks5Socket(this.socks5Client);
	}

	@Override
	public Socket newSocket(
			final InetAddress address, final int port) throws IOException {
		return new Socks5Socket(this.socks5Client, address, port);
	}

	@Override
	public Socket newSocket(
			final InetAddress address, 
			final int port, 
			final InetAddress localAddr, 
			final int localPort) throws IOException {
		return new Socks5Socket(
				this.socks5Client, address, port, localAddr, localPort);
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
		return new Socks5Socket(
				this.socks5Client, host, port, localAddr, localPort);
	}
	
}
