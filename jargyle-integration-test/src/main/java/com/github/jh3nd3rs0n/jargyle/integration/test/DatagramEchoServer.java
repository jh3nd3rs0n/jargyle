package com.github.jh3nd3rs0n.jargyle.integration.test;

import com.github.jh3nd3rs0n.jargyle.internal.concurrent.ExecutorsHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public final class DatagramEchoServer {

	private static final class Listener implements Runnable {

		private final Logger logger;
		private final DatagramSocket serverSocket;
		
		public Listener(final DatagramSocket serverSock) {
			this.logger = LoggerFactory.getLogger(Listener.class);
			this.serverSocket = serverSock;
		}
		
		public void run() {
			ExecutorService executor =
					ExecutorsHelper.newVirtualThreadPerTaskExecutorOrDefault(
							ExecutorsHelper.newCachedThreadPoolBuilder());
			try {
				while (true) {
					try {
						byte[] buffer = new byte[BUFFER_SIZE];
						DatagramPacket packet = new DatagramPacket(
								buffer, buffer.length);
						this.serverSocket.receive(packet);
						executor.execute(new Worker(this.serverSocket, packet));
					} catch (SocketException e) {
						// closed by DatagramEchoServer.stop()
						break;
					} catch (IOException e) {
						this.logger.warn(String.format(
								"%s: An error occurred in waiting for a UDP packet",
								this.getClass().getSimpleName()),
								e);
						break;
					}
				}
			} finally {
				executor.shutdownNow();
			}
		}
		
	}

	public enum State {
		
		STARTED,
		
		STOPPED;
		
	}
	
	private static final class Worker implements Runnable {

		private final Logger logger;
		private final DatagramPacket packet;
		private final DatagramSocket serverSocket;
	
		public Worker(
				final DatagramSocket serverSock, 
				final DatagramPacket pckt) {
			this.logger = LoggerFactory.getLogger(Worker.class);
			this.packet = pckt;
			this.serverSocket = serverSock;
		}
	
		public void run() {
			try {
				this.serverSocket.send(new DatagramPacket(
						this.packet.getData(),
						this.packet.getOffset(),
						this.packet.getLength(),
						this.packet.getAddress(),
						this.packet.getPort()));
			} catch (SocketException ignored) {
				// closed by DatagramEchoServer.stop()
			} catch (IOException e) {
				this.logger.warn(String.format(
						"%s: An error occurred in sending the UDP packet",
						this.getClass().getSimpleName()),
						e);
			}
		}
	
	}
	
	public static final int BUFFER_SIZE = 1024;
	public static final InetAddress INET_ADDRESS = InetAddress.getLoopbackAddress();

	private final InetAddress bindInetAddress;
	private ExecutorService executor;
	private int port;
	private DatagramSocket serverSocket;
	private final int specifiedPort;
	private State state;

	public DatagramEchoServer(final int prt) {
		this(prt, INET_ADDRESS);
	}

	public DatagramEchoServer(final int prt, final InetAddress bindInetAddr) {
		this.bindInetAddress = bindInetAddr;
		this.executor = null;
		this.port = -1;
		this.serverSocket = null;
		this.specifiedPort = prt;
		this.state = State.STOPPED;
	}

	public InetAddress getInetAddress() {
		return this.bindInetAddress;
	}

	public int getPort() {
		return this.port;
	}

	public State getState() {
		return this.state;
	}

	public void start() throws IOException {
		this.serverSocket = new DatagramSocket(
				this.specifiedPort, this.bindInetAddress);
		this.port = this.serverSocket.getLocalPort();
		this.executor = Executors.newSingleThreadExecutor();
		this.executor.execute(new Listener(this.serverSocket));
		this.state = State.STARTED;
	}

	public void stop() throws IOException {
		this.serverSocket.close();
		this.serverSocket = null;
		this.port = -1;
		this.executor.shutdownNow();
		this.executor = null;
		this.state = State.STOPPED;
	}
}