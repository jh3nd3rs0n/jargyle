package com.github.jh3nd3rs0n.jargyle.client;

import java.io.IOException;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

public abstract class NetObjectFactory {
	
	private static final NetObjectFactory DEFAULT_INSTANCE = 
			new DefaultNetObjectFactory();
	
	public static NetObjectFactory getDefault() {
		return DEFAULT_INSTANCE;
	}
	
	public static NetObjectFactory getInstance() {
		NetObjectFactory netObjectFactory = SocksNetObjectFactory.newInstance();
		if (netObjectFactory != null) {
			return netObjectFactory;
		}
		return DEFAULT_INSTANCE;
	}
	
	public abstract DatagramSocket newDatagramSocket() throws SocketException;
	
	public abstract DatagramSocket newDatagramSocket(
			final int port) throws SocketException;
	
	public abstract DatagramSocket newDatagramSocket(
			final int port, final InetAddress laddr) throws SocketException;
	
	public abstract DatagramSocket newDatagramSocket(
			final SocketAddress bindaddr) throws SocketException;

	public abstract HostResolver newHostResolver();

	public abstract ServerSocket newServerSocket() throws IOException;
	
	public abstract ServerSocket newServerSocket(
			final int port) throws IOException;
	
	public abstract ServerSocket newServerSocket(
			final int port, final int backlog) throws IOException;
	
	public abstract ServerSocket newServerSocket(
			final int port, 
			final int backlog, 
			final InetAddress bindAddr) throws IOException;

	public abstract Socket newSocket();
	
	public abstract Socket newSocket(
			final InetAddress address, final int port) throws IOException;
	
	public abstract Socket newSocket(
			final InetAddress address, 
			final int port, 
			final InetAddress localAddr, 
			final int localPort) throws IOException;
	
	public abstract Socket newSocket(
			final String host, 
			final int port) throws UnknownHostException, IOException;
	
	public abstract Socket newSocket(
			final String host, 
			final int port, 
			final InetAddress localAddr, 
			final int localPort) throws IOException;

}
