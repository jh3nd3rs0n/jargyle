package com.github.jh3nd3rs0n.jargyle.server;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.Arrays;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.github.jh3nd3rs0n.jargyle.server.internal.concurrent.ExecutorHelper;

public final class DatagramEchoServer {

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

	public static enum State {
		
		STARTED,
		
		STOPPED;
		
	}
	
	private static final class Worker implements Runnable {
		
		private final DatagramPacket packet;
		private final DatagramSocket serverSocket;
	
		public Worker(
				final DatagramSocket serverSock, 
				final DatagramPacket pckt) {
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
	
	public static final int BUFFER_SIZE = 1024;
	public static final int PORT = 1081;
	
	private ExecutorService executor;
	private DatagramSocket serverSocket;
	private State state;

	public DatagramEchoServer() {
		this.executor = null;
		this.serverSocket = null;
		this.state = State.STOPPED;
	}
	
	public State getState() {
		return this.state;
	}

	public void start() throws IOException {
		this.serverSocket = new DatagramSocket(PORT);
		this.executor = Executors.newSingleThreadExecutor();
		this.executor.execute(new Listener(this.serverSocket));
		this.state = State.STARTED;
	}

	public void stop() throws IOException {
		this.serverSocket.close();
		this.serverSocket = null;
		this.executor.shutdownNow();
		this.executor = null;
		this.state = State.STOPPED;
	}
}