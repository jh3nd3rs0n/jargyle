package com.github.jh3nd3rs0n.jargyle.net;

import java.io.IOException;
import java.io.InputStream;
import java.io.InterruptedIOException;
import java.io.OutputStream;
import java.net.Socket;
import java.net.SocketException;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.net.ssl.SSLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.jh3nd3rs0n.jargyle.internal.logging.LoggerHelper;

public final class RelayServer {

	private static final class DataWorker implements Runnable {
		
		private static final Logger LOGGER = LoggerFactory.getLogger(
				DataWorker.class);
		
		private final int bufferSize;
		private final DataWorkerContext dataWorkerContext;
		private final InputStream inputStream;
		private final OutputStream outputStream;
		private final int timeout;
				
		public DataWorker(final DataWorkerContext context) throws IOException {
			this.bufferSize = context.getBufferSize();
			this.dataWorkerContext = context;
			this.inputStream = context.getInputSocketInputStream();
			this.outputStream = context.getOutputSocketOutputStream();
			this.timeout = context.getTimeout();
		}
		
		@Override
		public void run() {
			this.dataWorkerContext.setLastReadTime(System.currentTimeMillis());
			while (true) {
				try {
					int bytesRead = 0;
					byte[] buffer = new byte[this.bufferSize];
					try {
						bytesRead = this.inputStream.read(buffer);
						this.dataWorkerContext.setLastReadTime(
								System.currentTimeMillis());
						LOGGER.trace(LoggerHelper.objectMessage(
								this, 
								String.format("Bytes read: %s", bytesRead)));
					} catch (SSLException e) {
						Throwable cause = e.getCause();
						if (cause != null && cause instanceof SocketException) {
							// socket closed
							break;
						}
						LOGGER.warn(
								LoggerHelper.objectMessage(
										this, 
										"Error occurred in the process of "
										+ "reading in data"), 
								e);
						break;						
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
						long lastReadTime = 
								this.dataWorkerContext.getLastReadTime();
						long timeSinceRead = 
								System.currentTimeMillis() - lastReadTime;
						if (timeSinceRead >= this.timeout) {
							LOGGER.trace(
									LoggerHelper.objectMessage(
											this, 
											"Relay timeout reached!"));
							break;
						}
						continue;
					}
					try {
						this.outputStream.write(buffer, 0, bytesRead);
					} catch (SSLException e) {
						Throwable cause = e.getCause();
						if (cause != null && cause instanceof SocketException) {
							// socket closed
							break;
						}
						LOGGER.warn(
								LoggerHelper.objectMessage(
										this, 
										"Error occurred in the process of "
										+ "writing out data"), 
								e);
						break;
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
					} catch (SSLException e) {
						Throwable cause = e.getCause();
						if (cause != null && cause instanceof SocketException) {
							// socket closed
							break;
						}
						LOGGER.warn(
								LoggerHelper.objectMessage(
										this, 
										"Error occurred in the process of "
										+ "flushing out any data"), 
								e);
						break;
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
			this.dataWorkerContext.stopRelayServerIfNotStopped();
		}

		@Override
		public String toString() {
			StringBuilder builder = new StringBuilder();
			builder.append(this.getClass().getSimpleName())
				.append(" [dataWorkerContext=")
				.append(this.dataWorkerContext)
				.append("]");
			return builder.toString();
		}
		
	}
	
	private static abstract class DataWorkerContext {
		
		private final Socket inputSocket;
		private final Socket outputSocket;
		private final RelayServer relayServer;
		
		public DataWorkerContext(
				final RelayServer server,
				final Socket inputSock,
				final Socket outputSock) {
			this.inputSocket = inputSock;
			this.outputSocket = outputSock;
			this.relayServer = server;
		}
		
		public final int getBufferSize() {
			return this.relayServer.bufferSize;
		}
		
		public final InputStream getInputSocketInputStream() throws IOException {
			return this.inputSocket.getInputStream();
		}
		
		public final long getLastReadTime() {
			return this.relayServer.getLastReadTime();
		}
		
		public final OutputStream getOutputSocketOutputStream() throws IOException {
			return this.outputSocket.getOutputStream();
		}
		
		public final int getTimeout() {
			return this.relayServer.timeout;
		}
		
		public final void setLastReadTime(final long time) {
			this.relayServer.setLastReadTime(time);
		}
		
		public final void stopRelayServerIfNotStopped() {
			this.relayServer.stopIfNotStopped();
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
	
	private static final class InboundDataWorkerContext
		extends DataWorkerContext {
		
		public InboundDataWorkerContext(final RelayServer server) {
			super(server, server.serverFacingSocket, server.clientFacingSocket);
		}
		
	}
	
	private static final class OutboundDataWorkerContext 
		extends DataWorkerContext {
		
		public OutboundDataWorkerContext(final RelayServer server) {
			super(server, server.clientFacingSocket, server.serverFacingSocket);
		}
		
	}
	
	private final Socket clientFacingSocket;
	private final int bufferSize;
	private ExecutorService executor;
	private long lastReadTime;
	private final Socket serverFacingSocket;
	private boolean started;
	private boolean stopped;
	private final int timeout;
	
	public RelayServer(
			final Socket clientFacingSock, 
			final Socket serverFacingSock, 
			final int bffrSize, 
			final int tmt) {
		Objects.requireNonNull(
				clientFacingSock, "client-facing socket must not be null");
		Objects.requireNonNull(
				serverFacingSock, "server-facing socket must not be null");
		if (bffrSize < 1) {
			throw new IllegalArgumentException(
					"buffer size must not be less than 1");
		}
		if (tmt < 1) {
			throw new IllegalArgumentException(
					"timeout must not be less than 1");
		}
		this.clientFacingSocket = clientFacingSock;
		this.bufferSize = bffrSize;
		this.executor = null;
		this.lastReadTime = 0L;
		this.serverFacingSocket = serverFacingSock;
		this.started = false;
		this.stopped = true;
		this.timeout = tmt;
	}
	
	private synchronized long getLastReadTime() {
		return this.lastReadTime;
	}
	
	public boolean isStarted() {
		return this.started;
	}
	
	public boolean isStopped() {
		return this.stopped;
	}
	
	private synchronized void setLastReadTime(final long time) {
		this.lastReadTime = time;
	}
	
	public void start() throws IOException {
		if (this.started) {
			throw new IllegalStateException("RelayServer already started");
		}
		this.lastReadTime = 0L;
		this.executor = Executors.newFixedThreadPool(2);
		this.executor.execute(new DataWorker(new InboundDataWorkerContext(
				this)));
		this.executor.execute(new DataWorker(new OutboundDataWorkerContext(
				this)));
		this.started = true;
		this.stopped = false;
	}
	
	public void stop() {
		if (this.stopped) {
			throw new IllegalStateException("RelayServer already stopped");
		}
		this.lastReadTime = 0L;
		this.executor.shutdownNow();
		this.executor = null;
		this.started = false;
		this.stopped = true;
	}
	
	private synchronized void stopIfNotStopped() {
		if (!this.stopped) {
			this.stop();
		}
	}
	
}
