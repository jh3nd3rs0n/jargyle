package com.github.jh3nd3rs0n.jargyle.server;

import com.github.jh3nd3rs0n.jargyle.common.net.*;
import com.github.jh3nd3rs0n.jargyle.server.internal.server.GeneralValueDerivationHelper;
import com.github.jh3nd3rs0n.jargyle.server.internal.server.Listener;

import java.io.IOException;
import java.net.*;
import java.util.Iterator;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public final class SocksServer {
	
	public static enum State {
		
		STARTED,
		
		STOPPED
		
	}
	
	public static final int DEFAULT_PORT_INT_VALUE = 1080;
	public static final Port DEFAULT_PORT = Port.valueOf(
			DEFAULT_PORT_INT_VALUE);
	
	private final Configuration configuration;
	private ExecutorService executor;
	private Host host;
	private Port port;
	private ServerSocket serverSocket;
	private State state;
	
	public SocksServer(final Configuration config) {
		this.configuration = config;
		this.executor = null;
		this.host = null;
		this.port = null;
		this.serverSocket = null;
		this.state = State.STOPPED;
	}
	
	private int getBacklog() {
		return this.configuration.getSettings().getLastValue(
				GeneralSettingSpecConstants.BACKLOG).intValue();
	}
	
	private Host getBindHost() {
		return GeneralValueDerivationHelper.getSocksServerBindHostFrom(
				this.configuration.getSettings());
	}
	
	private PortRanges getBindPortRanges() {
		return GeneralValueDerivationHelper.getSocksServerBindPortRangesFrom(
				this.configuration.getSettings());
	}
	
	public Configuration getConfiguration() {
		return this.configuration;
	}
	
	public Host getHost() {
		return this.host;
	}
	
	public Port getPort() {
		return this.port;
	}
	
	private SocketSettings getSocketSettings() {
		return GeneralValueDerivationHelper.getSocksServerSocketSettingsFrom(
				this.configuration.getSettings());
	}
	
	public State getState() {
		return this.state;
	}
	
	private ServerSocket newServerSocket(
			final InetAddress bindInetAddress,
			final PortRanges bindPortRanges,
			final SocketSettings socketSettings,
			final int backlog) throws IOException {
		ServerSocket serverSock = null;
		boolean serverSockBound = false;
		for (Iterator<PortRange> iterator = bindPortRanges.toList().iterator();
				!serverSockBound && iterator.hasNext();) {
			PortRange bindPortRange = iterator.next();
			for (Iterator<Port> iter = bindPortRange.iterator();
					!serverSockBound && iter.hasNext();) {
				Port bindPort = iter.next();
				serverSock = new ServerSocket();
				try {
					socketSettings.applyTo(serverSock);
				} catch (UnsupportedOperationException e) {
					serverSock.close();
					throw e;
				} catch (SocketException e) {
					serverSock.close();
					throw e;
				}
				try {
					serverSock.bind(
							new InetSocketAddress(
									bindInetAddress, 
									bindPort.intValue()), 
							backlog);
				} catch (BindException e) {
					serverSock.close();
					continue;
				} catch (IOException e) {
					serverSock.close();
					throw e;
				}
				serverSockBound = true;
			}
		}
		if (!serverSockBound) {
			throw new BindException(String.format(
					"unable to bind following address and port (range(s)): "
					+ "%s %s", 
					bindInetAddress,
					bindPortRanges));			
		}
		return serverSock;
	}
	
	public void start() throws IOException {
		if (this.state.equals(State.STARTED)) {
			throw new IllegalStateException("SocksServer already started");
		}
		Host bindHost = this.getBindHost();
		PortRanges bindPortRanges = this.getBindPortRanges();
		SocketSettings socketSettings = this.getSocketSettings();
		int backlog = this.getBacklog();
		this.serverSocket = newServerSocket(
				bindHost.toInetAddress(),
				bindPortRanges,
				socketSettings,
				backlog);
		this.host = bindHost;
		this.port = Port.valueOf(this.serverSocket.getLocalPort());
		this.executor = Executors.newSingleThreadExecutor();
		this.executor.execute(new Listener(
				this.serverSocket, this.configuration));
		this.state = State.STARTED;
	}
	
	public void stop() throws IOException {
		if (this.state.equals(State.STOPPED)) {
			throw new IllegalStateException("SocksServer already stopped");
		}
		this.host = null;
		this.port = null;
		this.serverSocket.close();
		this.serverSocket = null;
		this.executor.shutdownNow();
		this.executor = null;
		this.state = State.STOPPED;
	}

}
