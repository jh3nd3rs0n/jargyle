package com.github.jh3nd3rs0n.jargyle.client;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.github.jh3nd3rs0n.jargyle.server.Configuration;
import com.github.jh3nd3rs0n.jargyle.server.SocksServer;

public final class DatagramSocketEchoHelper {
	
	private static final class EchoServer {
		
		private ExecutorService executor;
		private final int port;
		private DatagramSocket serverSocket;
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
			this.serverSocket = new DatagramSocket(this.port);
			this.executor = Executors.newSingleThreadExecutor();
			this.executor.execute(new Worker(this.serverSocket));
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

	private static final class Worker implements Runnable {
		
		private final DatagramSocket serverSocket;
	
		public Worker(final DatagramSocket serverSock) {
			this.serverSocket = serverSock;
		}
	
		public void run() {
			while (true) {
				try {
					byte[] buffer = new byte[BUFFER_SIZE];
					DatagramPacket packet = new DatagramPacket(
							buffer, buffer.length);
					this.serverSocket.receive(packet);
					InetAddress address = packet.getAddress();
					int port = packet.getPort();
					String string = new String(Arrays.copyOfRange(
							packet.getData(), 
							packet.getOffset(), 
							packet.getLength()));
					byte[] stringBytes = string.getBytes();
					packet = new DatagramPacket(
							stringBytes, stringBytes.length, address, port);
					this.serverSocket.send(packet);
				} catch (IOException e) {
					if (this.serverSocket.isClosed()) {
						break;
					}
					e.printStackTrace();
				}
			}
		}
	
	}

	// big enough for any message size under GSS-API authentication
	private static final int BUFFER_SIZE = 65535;
	private static final int ECHO_SERVER_PORT = 1081;
	private static final int SLEEP_TIME = 500; // 1/2 second

	public static String echoThroughDatagramSocket(
			final String string, 
			final SocksClient socksClient, 
			final Configuration... configurations) throws IOException {
		return echoThroughDatagramSocket(
				string, socksClient, Arrays.asList(configurations));
	}
	
	public static String echoThroughDatagramSocket(
			final String string, 
			final SocksClient socksClient, 
			final List<Configuration> configurations) throws IOException {
		int configurationsSize = configurations.size();
		List<SocksServer> socksServers = new ArrayList<SocksServer>();
		EchoServer echoServer = null;
		DatagramSocket echoDatagramSocket = null;
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
			int port = echoServer.getPort();
			NetObjectFactory netObjectFactory = new DefaultNetObjectFactory();
			if (socksClient != null) {
				netObjectFactory = socksClient.newSocksNetObjectFactory();
			}
			echoDatagramSocket = netObjectFactory.newDatagramSocket(0);
			echoDatagramSocket.connect(InetAddress.getLoopbackAddress(), port);
			byte[] buffer = string.getBytes();
			DatagramPacket packet = new DatagramPacket(
					buffer, buffer.length, InetAddress.getLoopbackAddress(), port);
			echoDatagramSocket.send(packet);
			buffer = new byte[BUFFER_SIZE];
			packet = new DatagramPacket(buffer, buffer.length);
			echoDatagramSocket.receive(packet);
			returningString = new String(Arrays.copyOfRange(
					packet.getData(), packet.getOffset(), packet.getLength()));
		} finally {
			if (echoDatagramSocket != null) {
				echoDatagramSocket.close();
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

	private DatagramSocketEchoHelper() { }
	
}
