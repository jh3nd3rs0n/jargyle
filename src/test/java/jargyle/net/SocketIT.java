package jargyle.net;

import static org.junit.Assert.assertEquals;

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

import org.junit.Test;

import jargyle.IoHelper;
import jargyle.TestStringConstants;
import jargyle.net.socks.client.SocksClient;
import jargyle.net.socks.server.Configuration;
import jargyle.net.socks.server.SocksServer;

public class SocketIT {

	public static final class EchoServer {
		
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

	public static final class Listener implements Runnable {
		
		private final ServerSocket serverSocket;

		public Listener(final ServerSocket serverSock) {
			this.serverSocket = serverSock;
		}

		public void run() {
			ExecutorService executor = Executors.newCachedThreadPool();
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

	public static final class Worker implements Runnable {
		
		private final Socket clientSocket;

		public Worker(final Socket clientSock) {
			this.clientSocket = clientSock;
		}

		public void run() {
			try {
				InputStream in = this.clientSocket.getInputStream();
				OutputStream out = this.clientSocket.getOutputStream();
				byte[] b = IoHelper.readDataWithIndicatedLengthFrom(in);
				String string = new String(b);
				IoHelper.writeAsDataWithIndicatedLengthsThenFlush(
						string.getBytes(), out);
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

	private static final int ECHO_SERVER_PORT = 1234;
	public static final InetAddress LOOPBACK_ADDRESS = 
			InetAddress.getLoopbackAddress();
	private static final int SLEEP_TIME = 500; // 1/2 second

	public static String echoThroughSocket(
			final String string, 
			final SocksClient socksClient, 
			final Configuration configuration) throws IOException {
		String returningString = null;
		SocksServer socksServer = null;
		EchoServer echoServer = null;
		Socket echoSocket = null;
		try {
			if (configuration != null) {
				socksServer = new SocksServer(
						configuration);
				socksServer.start();
			}
			echoServer = new EchoServer(ECHO_SERVER_PORT);
			echoServer.start();
			jargyle.net.SocketFactory socketFactory = 
					new DefaultSocketFactory();
			if (socksClient != null) {
				socketFactory = 
						socksClient.newNetFactory().newSocketFactory();
			}
			echoSocket = socketFactory.newSocket();
			echoSocket.connect(new InetSocketAddress(
					LOOPBACK_ADDRESS, echoServer.getPort()));
			InputStream in = echoSocket.getInputStream();
			OutputStream out = echoSocket.getOutputStream();
			IoHelper.writeAsDataWithIndicatedLengthsThenFlush(
					string.getBytes(), out);
			byte[] b = IoHelper.readDataWithIndicatedLengthFrom(in);
			returningString = new String(b);
		} finally {
			if (echoSocket != null) {
				echoSocket.close();
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
		return returningString;
	}

	@Test
	public void testThroughSocket01() throws IOException {
		String string = TestStringConstants.STRING_01;
		String returningString = echoThroughSocket(string, null, null);
		assertEquals(string, returningString);
	}

	@Test
	public void testThroughSocket02() throws IOException {
		String string = TestStringConstants.STRING_02;
		String returningString = echoThroughSocket(string, null, null);
		assertEquals(string, returningString);
	}

	@Test
	public void testThroughSocket03() throws IOException {
		String string = TestStringConstants.STRING_03;
		String returningString = echoThroughSocket(string, null, null);
		assertEquals(string, returningString);
	}

}