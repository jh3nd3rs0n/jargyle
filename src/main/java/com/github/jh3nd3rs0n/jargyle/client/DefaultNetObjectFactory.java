package com.github.jh3nd3rs0n.jargyle.client;

import java.io.IOException;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

final class DefaultNetObjectFactory extends NetObjectFactory {

	@Override
	public DatagramSocket newDatagramSocket() throws SocketException {
		return new DatagramSocket();
	}

	@Override
	public DatagramSocket newDatagramSocket(
			final int port) throws SocketException {
		return new DatagramSocket(port);
	}

	@Override
	public DatagramSocket newDatagramSocket(
			final int port, final InetAddress laddr) throws SocketException {
		return new DatagramSocket(port, laddr);
	}

	@Override
	public DatagramSocket newDatagramSocket(
			final SocketAddress bindaddr) throws SocketException {
		return new DatagramSocket(bindaddr);
	}
	
	@Override
	public HostResolver newHostResolver() {
		return new HostResolver();
	}
	
	@Override
	public ServerSocket newServerSocket() throws IOException {
		return new ServerSocket();
	}

	@Override
	public ServerSocket newServerSocket(final int port) throws IOException {
		return new ServerSocket(port);
	}

	@Override
	public ServerSocket newServerSocket(
			final int port, final int backlog) throws IOException {
		return new ServerSocket(port, backlog);
	}

	@Override
	public ServerSocket newServerSocket(
			final int port, 
			final int backlog, 
			final InetAddress bindAddr) throws IOException {
		return new ServerSocket(port, backlog, bindAddr);
	}
	
	@Override
	public Socket newSocket() {
		return new Socket();
	}

	@Override
	public Socket newSocket(
			final InetAddress address, final int port) throws IOException {
		return new Socket(address, port);
	}

	@Override
	public Socket newSocket(
			final InetAddress address, 
			final int port, 
			final InetAddress localAddr, 
			final int localPort) throws IOException {
		return new Socket(address, port, localAddr, localPort);
	}

	@Override
	public Socket newSocket(
			final String host, 
			final int port) throws UnknownHostException, IOException {
		return new Socket(host, port);
	}

	@Override
	public Socket newSocket(
			final String host, 
			final int port, 
			final InetAddress localAddr, 
			final int localPort) throws IOException {
		return new Socket(host, port, localAddr, localPort);
	}

}
