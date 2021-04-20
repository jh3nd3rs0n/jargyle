package jargyle.net.socks.server;

import java.io.IOException;
import java.io.InputStream;
import java.io.InterruptedIOException;
import java.io.OutputStream;
import java.net.Socket;
import java.net.SocketException;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jargyle.logging.LoggerHelper;

public final class TcpRelayServer {

	private static class DataWorker implements Runnable {
		
		private static final Logger LOGGER = LoggerFactory.getLogger(
				DataWorker.class);
		
		private final TcpRelayServer tcpRelayServer;
		private final InputStream inputStream;
		private final Socket inputSocket;
		private final OutputStream outputStream;
		private final Socket outputSocket;
				
		public DataWorker(
				final TcpRelayServer server,
				final Socket inSocket,
				final Socket outSocket) throws IOException {
			this.tcpRelayServer = server;
			this.inputStream = inSocket.getInputStream();
			this.inputSocket = inSocket;
			this.outputStream = outSocket.getOutputStream();
			this.outputSocket = outSocket;
		}

		private int getBufferSize() {
			return this.tcpRelayServer.bufferSize;
		}
		
		private long getLastReadTime() {
			return this.tcpRelayServer.lastReadTime;
		}
		
		private int getTimeout() {
			return this.tcpRelayServer.timeout;
		}
		
		private boolean isFirstDataWorkerFinished() {
			return this.tcpRelayServer.firstDataWorkerFinished;
		}
		
		private boolean isTcpRelayServerStopped() {
			return this.tcpRelayServer.stopped;
		}
		
		@Override
		public void run() {
			this.setLastReadTime(System.currentTimeMillis());
			while (true) {
				try {
					int bytesRead = 0;
					byte[] buffer = new byte[this.getBufferSize()];
					try {
						bytesRead = this.inputStream.read(buffer);
						this.setLastReadTime(System.currentTimeMillis());
						LOGGER.trace(LoggerHelper.objectMessage(this, String.format(
								"Bytes read: %s",
								bytesRead)));
					} catch (SocketException e) {
						// socket closed
						break;
					} catch (InterruptedIOException e) {
						bytesRead = 0;
					} catch (IOException e) {
						LOGGER.warn(
								LoggerHelper.objectMessage(
										this, 
										"Error occurred in the process of "
										+ "reading in data"), 
								e);
						break;
					}
					if (bytesRead == -1) {
						break;
					}
					if (bytesRead == 0) {
						long timeSinceRead = 
								System.currentTimeMillis() - this.getLastReadTime();
						if (timeSinceRead >= this.getTimeout()) {
							break;
						}
						continue;
					}
					try {
						this.outputStream.write(buffer, 0, bytesRead);
					} catch (SocketException e) {
						// socket closed
						break;
					} catch (IOException e) {
						LOGGER.warn(
								LoggerHelper.objectMessage(
										this, 
										"Error occurred in the process of "
										+ "writing out data"), 
								e);
						break;
					}
					try {
						this.outputStream.flush();
					} catch (SocketException e) {
						// socket closed
						break;
					} catch (IOException e) {
						LOGGER.warn(
								LoggerHelper.objectMessage(
										this, 
										"Error occurred in the process of "
										+ "flushing out any data"), 
								e);
						break;
					}					
				} catch (Throwable t) {
					LOGGER.warn(
							LoggerHelper.objectMessage(
									this, 
									"Error occurred in the process of "
									+ "relaying the data"), 
							t);
					break;
				}
			}
			if (!this.isFirstDataWorkerFinished()) {
				this.setFirstDataWorkerFinished(true);
			} else {
				if (!this.isTcpRelayServerStopped()) {
					this.stopTcpRelayServer();
				}
			}
		}
		
		private void setFirstDataWorkerFinished(final boolean b) {
			this.tcpRelayServer.firstDataWorkerFinished = b;
		}
		
		private void setLastReadTime(final long time) {
			this.tcpRelayServer.lastReadTime = time;
		}
		
		private void stopTcpRelayServer() {
			this.tcpRelayServer.stop();
		}

		@Override
		public String toString() {
			StringBuilder builder = new StringBuilder();
			builder.append(this.getClass().getSimpleName())
				.append(" [inputSocket=")
				.append(this.inputSocket)
				.append(", outputSocket=")
				.append(this.outputSocket)
				.append("]");
			return builder.toString();
		}
		
	}
	
	private static final class IncomingDataWorker extends DataWorker {

		public IncomingDataWorker(
				final TcpRelayServer server) throws IOException {
			super(server, server.serverSocket, server.clientSocket);
		}
		
	}
	
	private static final class OutgoingDataWorker extends DataWorker {

		public OutgoingDataWorker(
				final TcpRelayServer server) throws IOException {
			super(server, server.clientSocket, server.serverSocket);
		}
		
	}
	
	private final Socket clientSocket;
	private final int bufferSize;
	private ExecutorService executor;
	private boolean firstDataWorkerFinished;
	private long lastReadTime;
	private final Socket serverSocket;
	private boolean started;
	private boolean stopped;
	private final int timeout;
	
	public TcpRelayServer(
			final Socket clientSock, 
			final Socket serverSock, 
			final int bffrSize, 
			final int tmt) {
		Objects.requireNonNull(clientSock, "client socket must not be null");
		Objects.requireNonNull(
				serverSock, "server-facing socket must not be null");
		if (bffrSize < 1) {
			throw new IllegalArgumentException("buffer size must not be less than 1");
		}
		if (tmt < 1) {
			throw new IllegalArgumentException("timeout must not be less than 1");
		}
		this.clientSocket = clientSock;
		this.bufferSize = bffrSize;
		this.executor = null;
		this.firstDataWorkerFinished = false;
		this.lastReadTime = 0L;
		this.serverSocket = serverSock;
		this.started = false;
		this.stopped = true;
		this.timeout = tmt;
	}
	
	public boolean isStarted() {
		return this.started;
	}
	
	public boolean isStopped() {
		return this.stopped;
	}
	
	public void start() throws IOException {
		if (this.started) {
			throw new IllegalStateException("TcpRelayServer already started");
		}
		this.lastReadTime = 0L;
		this.firstDataWorkerFinished = false;
		this.executor = Executors.newFixedThreadPool(2);
		this.executor.execute(new IncomingDataWorker(this));
		this.executor.execute(new OutgoingDataWorker(this));
		this.started = true;
		this.stopped = false;
	}
	
	public void stop() {
		if (this.stopped) {
			throw new IllegalStateException("TcpRelayServer already stopped");
		}
		this.lastReadTime = 0L;
		this.firstDataWorkerFinished = true;
		this.executor.shutdownNow();
		this.executor = null;
		this.started = false;
		this.stopped = true;
	}
	
}
