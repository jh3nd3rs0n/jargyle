package com.github.jh3nd3rs0n.jargyle.server;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.github.jh3nd3rs0n.jargyle.common.net.Host;
import com.github.jh3nd3rs0n.jargyle.common.net.Port;
import com.github.jh3nd3rs0n.jargyle.common.net.SocketSettings;
import com.github.jh3nd3rs0n.jargyle.server.internal.server.Listener;

public final class SocksServer {
	
	public static enum State {
		
		STARTED,
		
		STOPPED
		
	}
	
	private int backlog;
	private final Configuration configuration;
	private ExecutorService executor;
	private Host host;
	private Port port;
	private ServerSocket serverSocket;
	private SocketSettings socketSettings;
	private State state;
	
	public SocksServer(final Configuration config) {
		this.backlog = config.getSettings().getLastValue(
				GeneralSettingSpecConstants.BACKLOG).intValue();
		this.configuration = config;
		this.executor = null;
		this.host = config.getSettings().getLastValue(
				GeneralSettingSpecConstants.HOST);
		this.port = config.getSettings().getLastValue(
				GeneralSettingSpecConstants.PORT);
		this.serverSocket = null;
		this.socketSettings = config.getSettings().getLastValue(
				GeneralSettingSpecConstants.SOCKET_SETTINGS);
		this.state = State.STOPPED;
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
	
	public State getState() {
		return this.state;
	}
	
	public void start() throws IOException {
		if (this.state.equals(State.STARTED)) {
			throw new IllegalStateException("SocksServer already started");
		}
		this.serverSocket = new ServerSocket();
		this.socketSettings.applyTo(this.serverSocket);
		this.serverSocket.bind(new InetSocketAddress(
				this.host.toInetAddress(), this.port.intValue()), this.backlog);
		this.port = Port.newInstance(this.serverSocket.getLocalPort());
		this.executor = Executors.newSingleThreadExecutor();
		this.executor.execute(new Listener(
				this.serverSocket, this.configuration));
		this.state = State.STARTED;
	}
	
	public void stop() throws IOException {
		if (this.state.equals(State.STOPPED)) {
			throw new IllegalStateException("SocksServer already stopped");
		}
		this.backlog = this.configuration.getSettings().getLastValue(
				GeneralSettingSpecConstants.BACKLOG).intValue();
		this.host = this.configuration.getSettings().getLastValue(
				GeneralSettingSpecConstants.HOST);
		this.port = this.configuration.getSettings().getLastValue(
				GeneralSettingSpecConstants.PORT);
		this.socketSettings = this.configuration.getSettings().getLastValue(
				GeneralSettingSpecConstants.SOCKET_SETTINGS);
		this.serverSocket.close();
		this.serverSocket = null;
		this.executor.shutdownNow();
		this.executor = null;
		this.state = State.STOPPED;
	}

}
