package com.github.jh3nd3rs0n.jargyle.server;

import java.io.IOException;
import java.net.BindException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.SocketException;
import java.util.Iterator;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.github.jh3nd3rs0n.jargyle.common.net.Host;
import com.github.jh3nd3rs0n.jargyle.common.net.Port;
import com.github.jh3nd3rs0n.jargyle.common.net.PortRange;
import com.github.jh3nd3rs0n.jargyle.common.net.PortRanges;
import com.github.jh3nd3rs0n.jargyle.common.net.SocketSettings;
import com.github.jh3nd3rs0n.jargyle.server.internal.server.Listener;

public final class SocksServer {
	
	public static enum State {
		
		STARTED,
		
		STOPPED
		
	}
	
	public static final int DEFAULT_PORT_INT_VALUE = 1080;
	public static final Port DEFAULT_PORT = Port.newInstanceOf(
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
		Settings settings = this.configuration.getSettings();
		Host host = settings.getLastValue(
				GeneralSettingSpecConstants.SOCKS_SERVER_BIND_HOST);
		if (host != null) {
			return host;
		}
		host = settings.getLastValue(
				GeneralSettingSpecConstants.INTERNAL_FACING_BIND_HOST);
		if (host != null) {
			return host;
		}
		host = settings.getLastValue(
				GeneralSettingSpecConstants.BIND_HOST);
		return host;
	}
	
	private PortRanges getBindPortRanges() {
		Settings settings = this.configuration.getSettings();
		Port port = settings.getLastValue(GeneralSettingSpecConstants.PORT);
		if (port != null) {
			return PortRanges.newInstance(PortRange.newInstance(port));
		}
		PortRanges portRanges = settings.getLastValue(
				GeneralSettingSpecConstants.SOCKS_SERVER_BIND_PORT_RANGES);
		if (portRanges.toList().size() > 0) {
			return portRanges;
		}
		portRanges = settings.getLastValue(
				GeneralSettingSpecConstants.INTERNAL_FACING_BIND_TCP_PORT_RANGES);
		if (portRanges.toList().size() > 0) {
			return portRanges;
		}
		portRanges = settings.getLastValue(
				GeneralSettingSpecConstants.BIND_TCP_PORT_RANGES);
		if (portRanges.equals(PortRanges.getDefault())) {
			return PortRanges.newInstance(PortRange.newInstance(DEFAULT_PORT));
		}
		return portRanges;
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
		Settings settings = this.configuration.getSettings();
		SocketSettings socketSettings =	settings.getLastValue(
				GeneralSettingSpecConstants.SOCKS_SERVER_SOCKET_SETTINGS);
		if (socketSettings.toMap().size() > 0) {
			return socketSettings;
		}
		socketSettings = settings.getLastValue(
				GeneralSettingSpecConstants.INTERNAL_FACING_SOCKET_SETTINGS);
		if (socketSettings.toMap().size() > 0) {
			return socketSettings;
		}
		socketSettings = settings.getLastValue(
				GeneralSettingSpecConstants.SOCKET_SETTINGS);
		return socketSettings;
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
		this.port = Port.newInstanceOf(this.serverSocket.getLocalPort());
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
