package com.github.jh3nd3rs0n.jargyle.client;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.Arrays;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.github.jh3nd3rs0n.jargyle.server.internal.concurrent.ExecutorHelper;

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
		
		private final DatagramSocket serverSocket;
		
		public Listener(final DatagramSocket serverSock) {
			this.serverSocket = serverSock;
		}
		
		public void run() {
			ExecutorService executor = ExecutorHelper.newExecutor();
			while (true) {
				try {
					byte[] buffer = new byte[BUFFER_SIZE];
					DatagramPacket packet = new DatagramPacket(
							buffer, buffer.length);
					this.serverSocket.receive(packet);
					executor.execute(new Worker(this.serverSocket, packet));
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
		
		private final DatagramPacket packet;
		private final DatagramSocket serverSocket;
	
		public Worker(
				final DatagramSocket serverSock, final DatagramPacket pckt) {
			this.packet = pckt;
			this.serverSocket = serverSock;
		}
	
		public void run() {
			InetAddress address = this.packet.getAddress();
			int port = this.packet.getPort();
			String string = new String(Arrays.copyOfRange(
					this.packet.getData(), 
					this.packet.getOffset(), 
					this.packet.getLength()));
			byte[] stringBytes = string.getBytes();
			DatagramPacket newPacket = new DatagramPacket(
					stringBytes, stringBytes.length, address, port);
			try {
				this.serverSocket.send(newPacket);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	
	}

	private static final int BUFFER_SIZE = 1024;
	private static final int ECHO_CLIENT_TIMEOUT = 60000;
	private static final int ECHO_SERVER_PORT = 1081;
	private static final EchoServer ECHO_SERVER = new EchoServer(
			ECHO_SERVER_PORT);
	
	public static String echoThroughDatagramSocket(
			final String string, 
			final NetObjectFactory netObjectFactory) throws IOException {
		DatagramSocket echoClient = null;
		String returningString = null;
		try {
			int port = ECHO_SERVER.getPort();
			NetObjectFactory netObjFactory = netObjectFactory; 
			if (netObjFactory == null) {
				netObjFactory = new DefaultNetObjectFactory();
			}
			echoClient = netObjFactory.newDatagramSocket(null);
			echoClient.setSoTimeout(ECHO_CLIENT_TIMEOUT);
			echoClient.bind(null);
			echoClient.connect(InetAddress.getLoopbackAddress(), port);
			byte[] buffer = string.getBytes();
			DatagramPacket packet = new DatagramPacket(
					buffer, buffer.length, InetAddress.getLoopbackAddress(), port);
			echoClient.send(packet);
			buffer = new byte[BUFFER_SIZE];
			packet = new DatagramPacket(buffer, buffer.length);
			echoClient.receive(packet);
			returningString = new String(Arrays.copyOfRange(
					packet.getData(), packet.getOffset(), packet.getLength()));
		} finally {
			if (echoClient != null) {
				echoClient.close();
			}
		}
		return returningString;
	}

	public static void startEchoServer() throws IOException {
		ECHO_SERVER.start();
	}
	
	public static void stopEchoServer() throws IOException {
		if (ECHO_SERVER.isStarted()) {
			ECHO_SERVER.stop();
		}
	}

	private DatagramSocketEchoHelper() { }
	
}
