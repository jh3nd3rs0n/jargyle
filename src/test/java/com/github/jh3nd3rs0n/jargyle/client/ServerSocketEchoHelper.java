package com.github.jh3nd3rs0n.jargyle.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.net.ServerSocketFactory;
import javax.net.SocketFactory;

import com.github.jh3nd3rs0n.jargyle.IoHelper;
import com.github.jh3nd3rs0n.jargyle.server.internal.concurrent.ExecutorHelper;

public final class ServerSocketEchoHelper {
	
	private static final class EchoServer {
		
		private ExecutorService executor;
		private final int port;
		private ServerSocket serverSocket;
		private boolean started;
		private final String string;
		private String returningString;
		
		public EchoServer(final int prt, final String str) {
			this.executor = null;
			this.port = prt;
			this.serverSocket = null;
			this.started = false;
			this.string = str;
			this.returningString = null;
		}
		
		public int getPort() {
			return this.port;
		}
		
		public synchronized String getReturningString() {
			return this.returningString;
		}
		
		public String getString() {
			return this.string;
		}
		
		public boolean isStarted() {
			return this.started;
		}
		
		public synchronized void setReturningString(final String returningStr) {
			this.returningString = returningStr;
		}
		
		public void start() throws IOException {
			if (this.started) {
				throw new IllegalStateException();
			}
			ServerSocketFactory factory = ServerSocketFactory.getDefault();
			this.serverSocket = factory.createServerSocket(this.port);
			this.executor = Executors.newSingleThreadExecutor();
			this.executor.execute(new Listener(this, this.serverSocket));
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
		
		private final EchoServer echoServer;
		private final ServerSocket serverSocket;
		
		public Listener(final EchoServer server, final ServerSocket serverSock) {
			this.echoServer = server;
			this.serverSocket = serverSock;
		}
		
		public void run() {
			ExecutorService executor = ExecutorHelper.newExecutor();
			while (true) {
				try {
					Socket clientSocket = this.serverSocket.accept();
					executor.execute(new Worker(this.echoServer, clientSocket));
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
		
		private final EchoServer echoServer;
		private final Socket clientSocket;
		
		public Worker(final EchoServer server, final Socket clientSock) {
			this.echoServer = server;
			this.clientSocket = clientSock;
		}
		
		public void run() {
			Socket socket = null;
			try {
				BufferedReader reader = new BufferedReader(
						new InputStreamReader(this.clientSocket.getInputStream()));
				String inputLine = reader.readLine();
				int index = inputLine.lastIndexOf(':');
				String host = inputLine.substring(0, index);
				int port = Integer.parseInt(inputLine.substring(index + 1));
				SocketFactory factory = SocketFactory.getDefault();
				socket = factory.createSocket(host, port);
				InputStream socketIn = socket.getInputStream();
				OutputStream socketOut = socket.getOutputStream();
				String string = this.echoServer.getString();
				byte[] stringBytes = string.getBytes();
				IoHelper.writeThenFlush(stringBytes, socketOut);
				byte[] bytes = IoHelper.readFrom(socketIn);
				String returningString = new String(bytes);
				this.echoServer.setReturningString(returningString);
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				if (socket != null) {
					try {
						socket.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				try {
					this.clientSocket.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	private static final int ECHO_CLIENT_TIMEOUT = 60000;
	private static final int ECHO_SERVER_PORT = 1082;
	private static final int SERVER_PORT = 1083;
	private static final int SLEEP_TIME = 500; // 1/2 second

	public static String echoThroughServerSocket(
			final String string, 
			final NetObjectFactory netObjectFactory) throws IOException {
		EchoServer echoServer = null;
		Socket echoClient = null;
		Socket socket = null;
		String returningString = null;		
		try {
			echoServer = new EchoServer(ECHO_SERVER_PORT, string);
			echoServer.start();
			NetObjectFactory netObjFactory = netObjectFactory;
			if (netObjFactory == null) {
				netObjFactory = new DefaultNetObjectFactory();
			}
			echoClient = netObjFactory.newSocket();
			echoClient.setSoTimeout(ECHO_CLIENT_TIMEOUT);
			echoClient.connect(new InetSocketAddress(
					InetAddress.getLoopbackAddress(), echoServer.getPort()));
			OutputStream out = echoClient.getOutputStream();
			PrintWriter writer = new PrintWriter(out, true);
			ServerSocket serverSocket = netObjFactory.newServerSocket();
			serverSocket.setSoTimeout(ECHO_CLIENT_TIMEOUT);
			serverSocket.bind(new InetSocketAddress(
					(InetAddress) null, SERVER_PORT));
			writer.println(String.format(
					"%s:%s", 
					serverSocket.getInetAddress().getHostAddress(), 
					serverSocket.getLocalPort()));
			try {
				socket = serverSocket.accept();
			} finally {
				serverSocket.close();
			}
			InputStream socketIn = socket.getInputStream();
			OutputStream socketOut = socket.getOutputStream();
			byte[] bytes = IoHelper.readFrom(socketIn);
			String str = new String(bytes);
			IoHelper.writeThenFlush(str.getBytes(), socketOut);
			do {
				try {
					Thread.sleep(SLEEP_TIME);
				} catch (InterruptedException e) {
					Thread.currentThread().interrupt();
				}				
			} while ((returningString = echoServer.getReturningString()) == null);
		} finally {
			if (socket != null) {
				socket.close();
			}
			if (echoClient != null) {
				echoClient.close();
			}
			if (echoServer != null && echoServer.isStarted()) {
				echoServer.stop();
			}
			try {
				Thread.sleep(SLEEP_TIME);
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
			}
		}
		return returningString;
	}

	private ServerSocketEchoHelper() { }
	
}
