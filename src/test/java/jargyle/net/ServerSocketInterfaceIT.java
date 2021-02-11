package jargyle.net;

import static org.junit.Assert.assertEquals;

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

import org.junit.Test;

import jargyle.IoHelper;
import jargyle.TestStringConstants;
import jargyle.net.socks.Configuration;
import jargyle.net.socks.SocksClient;
import jargyle.net.socks.SocksServer;

public class ServerSocketInterfaceIT {

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
		
		public String getReturningString() {
			return this.returningString;
		}
		
		public String getString() {
			return this.string;
		}
		
		public boolean isStarted() {
			return this.started;
		}
		
		public void setReturningString(final String returningStr) {
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
			ExecutorService executor = Executors.newCachedThreadPool();
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
				long startTime = System.currentTimeMillis();
				String inputLine = reader.readLine();
				long endTime = System.currentTimeMillis();
				if (inputLine == null) {
					return;
				}
				int index = inputLine.lastIndexOf(':');
				String host = inputLine.substring(0, index);
				int port = Integer.parseInt(inputLine.substring(index + 1));
				SocketFactory factory = SocketFactory.getDefault();
				socket = factory.createSocket(host, port);
				InputStream socketIn = socket.getInputStream();
				OutputStream socketOut = socket.getOutputStream();
				String string = this.echoServer.getString();
				try {
					Thread.sleep(endTime - startTime);
				} catch (InterruptedException e) {
					Thread.currentThread().interrupt();
				}
				IoHelper.writeAsDataWithIndicatedLengthsThenFlush(
						string.getBytes(), socketOut);
				byte[] b = IoHelper.readDataWithIndicatedLengthFrom(socketIn);
				String returningString = new String(b);
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

	private static final int ECHO_SERVER_PORT = 1024;
	public static final InetAddress LOOPBACK_ADDRESS = 
			InetAddress.getLoopbackAddress();
	private static final int SERVER_PORT = 5678;
	private static final int SLEEP_TIME = 500; // 1/2 second

	public static String echoThroughServerSocketInterface(
			final String string, 
			final SocksClient socksClient, 
			final Configuration configuration) throws IOException {
		String returningString = null;
		SocksServer socksServer = null;
		EchoServer echoServer = null;
		SocketInterface echoSocketInterface = null;
		SocketInterface socketInterface = null;
		try {
			if (configuration != null) {
				socksServer = new SocksServer(
						configuration);
				socksServer.start();
			}
			echoServer = new EchoServer(ECHO_SERVER_PORT, string);
			echoServer.start();
			SocketInterfaceFactory socketInterfaceFactory =
					new DirectSocketInterfaceFactory();
			if (socksClient != null) {
				socketInterfaceFactory = socksClient.newSocketInterfaceFactory();
			}
			echoSocketInterface = socketInterfaceFactory.newSocketInterface();
			echoSocketInterface.connect(new InetSocketAddress(
					LOOPBACK_ADDRESS, echoServer.getPort()));
			OutputStream out = echoSocketInterface.getOutputStream();
			PrintWriter writer = new PrintWriter(out, true);
			ServerSocketInterfaceFactory serverSocketInterfaceFactory =
					 new DirectServerSocketInterfaceFactory();
			if (socksClient != null) {
				serverSocketInterfaceFactory = 
						socksClient.newServerSocketInterfaceFactory();
			}
			ServerSocketInterface serverSocketInterface = 
					serverSocketInterfaceFactory.newServerSocketInterface();
			serverSocketInterface.bind(new InetSocketAddress(
					(InetAddress) null, SERVER_PORT));
			writer.println(String.format(
					"%s:%s", 
					serverSocketInterface.getInetAddress().getHostAddress(), 
					serverSocketInterface.getLocalPort()));
			try {
				socketInterface = serverSocketInterface.accept();
			} finally {
				serverSocketInterface.close();
			}
			InputStream socketIn = socketInterface.getInputStream();
			OutputStream socketOut = socketInterface.getOutputStream();
			byte[] b = IoHelper.readDataWithIndicatedLengthFrom(socketIn);
			String str = new String(b);
			IoHelper.writeAsDataWithIndicatedLengthsThenFlush(
					str.getBytes(), socketOut);
		} finally {
			if (socketInterface != null) {
				socketInterface.close();
			}
			if (echoSocketInterface != null) {
				echoSocketInterface.close();
			}
			if (echoServer != null && echoServer.isStarted()) {
				echoServer.stop();
			}
			if (socksServer != null && socksServer.isStarted()) {
				socksServer.stop();
			}
			try {
				Thread.sleep(SLEEP_TIME);
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
			}
		}
		if (echoServer != null) {
			returningString = echoServer.getReturningString();
		}
		return returningString;
	}

	@Test
	public void testThroughServerSocketInterface01() throws IOException {
		String string = TestStringConstants.STRING_01;
		String returningString = echoThroughServerSocketInterface(string, null, null);
		assertEquals(string, returningString);
	}

	@Test
	public void testThroughServerSocketInterface02() throws IOException {
		String string = TestStringConstants.STRING_02;
		String returningString = echoThroughServerSocketInterface(string, null, null);
		assertEquals(string, returningString);
	}

	@Test
	public void testThroughServerSocketInterface03() throws IOException {
		String string = TestStringConstants.STRING_03;
		String returningString = echoThroughServerSocketInterface(string, null, null);
		assertEquals(string, returningString);
	}

}
