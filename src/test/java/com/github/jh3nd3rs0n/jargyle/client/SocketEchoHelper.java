package com.github.jh3nd3rs0n.jargyle.client;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.net.ServerSocketFactory;

import com.github.jh3nd3rs0n.jargyle.IoHelper;
import com.github.jh3nd3rs0n.jargyle.server.Configuration;
import com.github.jh3nd3rs0n.jargyle.server.SocksServer;

public final class SocketEchoHelper {
	
	private static final class EchoServer {
		
		private ExecutorService executor;
		private final int port;
		private ServerSocket serverSocket;
		private boolean started;
	
		public EchoServer(final int prt) {
			this.executor = null;
			this.port = prt;
			this.serverSocket = null;
			this.started = false;
		}
	
		public int getPort() {
			return this.port;
		}
	
		public boolean isStarted() {
			return this.started;
		}
	
		public void start() throws IOException {
			if (this.started) {
				throw new IllegalStateException();
			}
			ServerSocketFactory factory = ServerSocketFactory.getDefault();
			this.serverSocket = factory.createServerSocket(this.port);
			this.executor = Executors.newSingleThreadExecutor();
			this.executor.execute(new Listener(this.serverSocket));
			this.started = true;
		}
	
		public void stop() throws IOException {
			if (!this.started) {
				throw new IllegalStateException();
			}
			this.serverSocket.close();
			this.serverSocket = null;
			this.executor.shutdownNow();
			this.executor = null;
			this.started = false;
		}
	}

	private static final class Listener implements Runnable {
		
		private final ServerSocket serverSocket;
	
		public Listener(final ServerSocket serverSock) {
			this.serverSocket = serverSock;
		}
	
		public void run() {
			ExecutorService executor = Executors.newCachedThreadPool();
			while (true) {
				try {
					Socket clientFacingSocket = this.serverSocket.accept();
					executor.execute(new Worker(clientFacingSocket));
				} catch (SocketException e) {
					break;
				} catch (IOException e) {
					e.printStackTrace();
					break;
				}
			}
			executor.shutdownNow();
		}
	}

	private static final class Worker implements Runnable {
		
		private final Socket clientFacingSocket;
	
		public Worker(final Socket clientFacingSock) {
			this.clientFacingSocket = clientFacingSock;
		}
	
		public void run() {
			try {
				InputStream in = this.clientFacingSocket.getInputStream();
				OutputStream out = this.clientFacingSocket.getOutputStream();
				byte[] bytes = IoHelper.readFrom(in);
				String string = new String(bytes);
				IoHelper.writeThenFlush(string.getBytes(), out);
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				try {
					this.clientFacingSocket.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	private static final int ECHO_SERVER_PORT = 1081;
	private static final int SLEEP_TIME = 500; // 1/2 second

	public static String echoThroughSocket(
			final String string, 
			final SocksClient socksClient, 
			final Configuration... configurations) throws IOException {
		return echoThroughSocket(
				string, socksClient, Arrays.asList(configurations));
	}
	
	public static String echoThroughSocket(
			final String string, 
			final SocksClient socksClient, 
			final List<Configuration> configurations) throws IOException {
		int configurationsSize = configurations.size();
		List<SocksServer> socksServers = new ArrayList<SocksServer>();
		EchoServer echoServer = null;
		Socket echoSocket = null;
		String returningString = null;
		try {
			if (configurationsSize > 0) {
				for (int i = configurationsSize - 1; i > -1; i--) {
					Configuration configuration = configurations.get(i);
					SocksServer socksServer = new SocksServer(configuration);
					socksServers.add(0, socksServer);
					socksServer.start();
				}
			}
			echoServer = new EchoServer(ECHO_SERVER_PORT);
			echoServer.start();
			NetObjectFactory netObjectFactory = new DefaultNetObjectFactory();
			if (socksClient != null) {
				netObjectFactory = socksClient.newSocksNetObjectFactory();
			}
			echoSocket = netObjectFactory.newSocket();
			echoSocket.connect(new InetSocketAddress(
					InetAddress.getLoopbackAddress(), echoServer.getPort()));
			InputStream in = echoSocket.getInputStream();
			OutputStream out = echoSocket.getOutputStream();
			IoHelper.writeThenFlush(string.getBytes(), out);
			byte[] bytes = IoHelper.readFrom(in);
			returningString = new String(bytes);
		} finally {
			if (echoSocket != null) {
				echoSocket.close();
			}
			if (echoServer != null && echoServer.isStarted()) {
				echoServer.stop();
			}
			if (socksServers.size() > 0) {
				for (SocksServer socksServer : socksServers) {
					if (!socksServer.getState().equals(
							SocksServer.State.STOPPED)) {
						socksServer.stop();
					}
				}
			}
			try {
				Thread.sleep(SLEEP_TIME);
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
			}
		}
		return returningString;
	}

	private SocketEchoHelper() { }
	
}
