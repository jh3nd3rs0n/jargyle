package jargyle.server;

import static org.junit.Assert.assertEquals;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.junit.Test;

import jargyle.TestStringConstants;
import jargyle.client.DatagramSocketFactory;
import jargyle.client.SocksClient;

public class DatagramSocketIT {

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
					DataInputStream dataInputStream = new DataInputStream(
							new ByteArrayInputStream(packet.getData()));
					String string = dataInputStream.readUTF();
					System.out.printf("Received from client: %s (%s bytes)%n", 
							string, string.getBytes().length);
					ByteArrayOutputStream byteArrayOutputStream = 
							new ByteArrayOutputStream();
					DataOutputStream dataOutputStream = 
							new DataOutputStream(byteArrayOutputStream);
					dataOutputStream.writeUTF(string);
					dataOutputStream.flush();
					byte[] stringBytes = byteArrayOutputStream.toByteArray();
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
	private static final int ECHO_SERVER_PORT = 1234;
	public static final InetAddress LOOPBACK_ADDRESS = 
			InetAddress.getLoopbackAddress();
	private static final int SLEEP_TIME = 500; // 1/2 second

	public static String echoThroughDatagramSocket(
			final String string, 
			final SocksClient socksClient, 
			final Configuration configuration) throws IOException {
		SocksServer socksServer = null;
		EchoServer echoServer = null;
		DatagramSocket echoDatagramSocket = null;
		String returningString = null;
		try {
			if (configuration != null) {
				socksServer = new SocksServer(
						configuration);
				socksServer.start();
			}
			echoServer = new EchoServer(ECHO_SERVER_PORT);
			echoServer.start();
			int port = echoServer.getPort();
			DatagramSocketFactory datagramSocketFactory =
					DatagramSocketFactory.newInstance(socksClient);
			echoDatagramSocket = datagramSocketFactory.newDatagramSocket(0);
			echoDatagramSocket.connect(LOOPBACK_ADDRESS, port);
			ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
			DataOutputStream dataOutputStream = 
					new DataOutputStream(byteArrayOutputStream);
			dataOutputStream.writeUTF(string);
			dataOutputStream.flush();
			byte[] buffer = byteArrayOutputStream.toByteArray();
			DatagramPacket packet = new DatagramPacket(
					buffer, buffer.length, LOOPBACK_ADDRESS, port);
			System.out.printf("Sending to server: %s (%s bytes)%n", 
					string, string.getBytes().length);
			echoDatagramSocket.send(packet);
			buffer = new byte[BUFFER_SIZE];
			packet = new DatagramPacket(buffer, buffer.length);
			echoDatagramSocket.receive(packet);
			DataInputStream dataInputStream = new DataInputStream(
					new ByteArrayInputStream(packet.getData()));
			returningString = dataInputStream.readUTF();
			System.out.printf("Received from server: %s (%s bytes)%n", 
					returningString, returningString.getBytes().length);
		} finally {
			if (echoDatagramSocket != null) {
				echoDatagramSocket.close();
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
	public void testThroughDatagramSocket01() throws IOException {
		System.out.println("Testing through DatagramSocket...");
		String string = TestStringConstants.STRING_01;
		String returningString = echoThroughDatagramSocket(string, null, null);
		assertEquals(string, returningString);
	}

	@Test
	public void testThroughDatagramSocket02() throws IOException {
		System.out.println("Testing through DatagramSocket...");
		String string = TestStringConstants.STRING_02;
		String returningString = echoThroughDatagramSocket(string, null, null);
		assertEquals(string, returningString);
	}

	@Test
	public void testThroughDatagramSocket03() throws IOException {
		System.out.println("Testing through DatagramSocket...");
		String string = TestStringConstants.STRING_03;
		String returningString = echoThroughDatagramSocket(string, null, null);
		assertEquals(string, returningString);
	}

}