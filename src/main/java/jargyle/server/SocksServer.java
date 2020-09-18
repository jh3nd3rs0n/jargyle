package jargyle.server;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import argmatey.ArgMatey.CLI;
import jargyle.common.net.SocketSettings;
import jargyle.common.util.NonnegativeInteger;

public final class SocksServer {
	
	public static void main(final String[] args) {
		CLI socksServerCLI = new SocksServerCLI(args, false);
		int status = socksServerCLI.handleArgs();
		if (status != 0) { System.exit(status);	}
	}
	
	private int backlog;
	private final Configuration configuration;
	private ExecutorService executor;
	private Host host;
	private Port port;
	private ServerSocket serverSocket;
	private SocketSettings socketSettings;
	private boolean started;
	private boolean stopped;
	
	public SocksServer(final Configuration config) {
		this.backlog = config.getSettings().getLastValue(
				SettingSpec.BACKLOG, NonnegativeInteger.class).intValue();
		this.configuration = config;
		this.executor = null;
		this.host = config.getSettings().getLastValue(
				SettingSpec.HOST, Host.class);
		this.port = config.getSettings().getLastValue(
				SettingSpec.PORT, Port.class);
		this.serverSocket = null;
		this.socketSettings = config.getSettings().getLastValue(
				SettingSpec.SOCKET_SETTINGS, SocketSettings.class);
		this.started = false;
		this.stopped = true;
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
	
	public boolean isStarted() {
		return this.started;
	}
	
	public boolean isStopped() {
		return this.stopped;
	}
	
	public void start() throws IOException {
		if (this.started) {
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
		this.started = true;
		this.stopped = false;
	}
	
	public void stop() throws IOException {
		if (this.stopped) {
			throw new IllegalStateException("SocksServer already stopped");
		}
		this.backlog = this.configuration.getSettings().getLastValue(
				SettingSpec.BACKLOG, NonnegativeInteger.class).intValue();
		this.host = this.configuration.getSettings().getLastValue(
				SettingSpec.HOST, Host.class);
		this.port = this.configuration.getSettings().getLastValue(
				SettingSpec.PORT, Port.class);
		this.socketSettings = this.configuration.getSettings().getLastValue(
				SettingSpec.SOCKET_SETTINGS, SocketSettings.class);
		this.serverSocket.close();
		this.serverSocket = null;
		this.executor.shutdownNow();
		this.executor = null;
		this.started = false;
		this.stopped = true;
	}

}
