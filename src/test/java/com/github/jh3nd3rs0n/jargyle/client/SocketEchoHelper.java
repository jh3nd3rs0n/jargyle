package com.github.jh3nd3rs0n.jargyle.client;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.net.ServerSocketFactory;

import com.github.jh3nd3rs0n.jargyle.IoHelper;
import com.github.jh3nd3rs0n.jargyle.server.internal.concurrent.ExecutorHelper;

public final class SocketEchoHelper {
	
	private static final class EchoServer {

		private static final int BACKLOG = 65535;
		
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
			this.serverSocket = factory.createServerSocket(this.port, BACKLOG);
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
			ExecutorService executor = ExecutorHelper.newExecutor();
			while (true) {
				try {
					Socket clientSocket = this.serverSocket.accept();
					executor.execute(new Worker(clientSocket));
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
		
		private final Socket clientSocket;
	
		public Worker(final Socket clientSock) {
			this.clientSocket = clientSock;
		}
	
		public void run() {
			try {
				InputStream in = this.clientSocket.getInputStream();
				OutputStream out = this.clientSocket.getOutputStream();
				byte[] bytes = IoHelper.readFrom(in);
				String string = new String(bytes);
				IoHelper.writeThenFlush(string.getBytes(), out);
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				try {
					this.clientSocket.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	private static final int ECHO_CLIENT_TIMEOUT = 60000;
	private static final int ECHO_SERVER_PORT = 1084;
	private static final EchoServer ECHO_SERVER = new EchoServer(
			ECHO_SERVER_PORT);
	
	public static String echoThroughSocket(
			final String string, 
			final NetObjectFactory netObjectFactory) throws IOException {
		Socket echoClient = null;
		String returningString = null;
		try {
			NetObjectFactory netObjFactory = netObjectFactory;
			if (netObjFactory == null) {
				netObjFactory = new DefaultNetObjectFactory();
			}
			echoClient = netObjFactory.newSocket();
			echoClient.setSoTimeout(ECHO_CLIENT_TIMEOUT);
			echoClient.connect(new InetSocketAddress(
					InetAddress.getLoopbackAddress(), ECHO_SERVER.getPort()));
			InputStream in = echoClient.getInputStream();
			OutputStream out = echoClient.getOutputStream();
			IoHelper.writeThenFlush(string.getBytes(), out);
			byte[] bytes = IoHelper.readFrom(in);
			returningString = new String(bytes);
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
	
	private SocketEchoHelper() { }
	
}
