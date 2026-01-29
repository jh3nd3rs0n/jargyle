package com.github.jh3nd3rs0n.jargyle.client.socks5;

import java.io.IOException;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

import com.github.jh3nd3rs0n.jargyle.client.HostResolver;
import com.github.jh3nd3rs0n.jargyle.client.SocksNetObjectFactory;

public final class Socks5NetObjectFactory extends SocksNetObjectFactory {

	private final Socks5Client socks5Client;
	private final Socks5ClientAgent socks5ClientAgent;

	Socks5NetObjectFactory(final Socks5Client client) {
		super(client);
		this.socks5Client = client;
		this.socks5ClientAgent = new Socks5ClientAgent(client);
	}

	public Socks5Client getSocks5Client() {
		return this.socks5Client;
	}

	@Override
	public DatagramSocket newDatagramSocket() throws SocketException {
		return new Socks5DatagramSocket(this.socks5ClientAgent);
	}

	@Override
	public DatagramSocket newDatagramSocket(
			final int port) throws SocketException {
		return new Socks5DatagramSocket(this.socks5ClientAgent, port);
	}

	@Override
	public DatagramSocket newDatagramSocket(
			final int port, final InetAddress laddr) throws SocketException {
		return new Socks5DatagramSocket(this.socks5ClientAgent, port, laddr);
	}
	
	@Override
	public DatagramSocket newDatagramSocket(
			final SocketAddress bindaddr) throws SocketException {
		return new Socks5DatagramSocket(this.socks5ClientAgent, bindaddr);
	}
	
	@Override
	public HostResolver newHostResolver() {
		return new HostResolver();
	}

	@Override
	public ServerSocket newServerSocket() throws IOException {
		return new Socks5ServerSocket(this.socks5ClientAgent);
	}

	@Override
	public ServerSocket newServerSocket(final int port) throws IOException {
		return new Socks5ServerSocket(this.socks5ClientAgent, port);
	}

	@Override
	public ServerSocket newServerSocket(
			final int port, final int backlog) throws IOException {
		return new Socks5ServerSocket(this.socks5ClientAgent, port);
	}
	
	@Override
	public ServerSocket newServerSocket(
			final int port, 
			final int backlog, 
			final InetAddress bindAddr) throws IOException {
		return new Socks5ServerSocket(this.socks5ClientAgent, port, bindAddr);
	}

	@Override
	public Socket newSocket() {
		return new Socks5Socket(this.socks5ClientAgent);
	}

	@Override
	public Socket newSocket(
			final InetAddress address, final int port) throws IOException {
		return new Socks5Socket(this.socks5ClientAgent, address, port);
	}

	@Override
	public Socket newSocket(
			final InetAddress address, 
			final int port, 
			final InetAddress localAddr, 
			final int localPort) throws IOException {
		return new Socks5Socket(
				this.socks5ClientAgent, address, port, localAddr, localPort);
	}

	@Override
	public Socket newSocket(
			final String host, 
			final int port) throws UnknownHostException, IOException {
		return new Socks5Socket(this.socks5ClientAgent, host, port);
	}

	@Override
	public Socket newSocket(
			final String host, 
			final int port, 
			final InetAddress localAddr, 
			final int localPort) throws IOException {
		return new Socks5Socket(
				this.socks5ClientAgent, host, port, localAddr, localPort);
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append(this.getClass().getSimpleName())
			.append(" [getSocks5Client()=")
			.append(this.getSocks5Client())
			.append("]");
		return builder.toString();
	}
	
}
