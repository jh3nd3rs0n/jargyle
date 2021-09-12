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
		private final int idleTimeout;		
		private final InputStream inputStream;
		private final OutputStream outputStream;
				
		public DataWorker(final DataWorkerContext context) throws IOException {
			this.bufferSize = context.getBufferSize();
			this.dataWorkerContext = context;
			this.idleTimeout = context.getIdleTimeout();			
			this.inputStream = context.getInputSocketInputStream();
			this.outputStream = context.getOutputSocketOutputStream();
		}
		
		@Override
		public void run() {
			while (true) {
				try {
					int bytesRead = 0;
					byte[] buffer = new byte[this.bufferSize];
					try {
						bytesRead = this.inputStream.read(buffer);
						this.dataWorkerContext.setIdleStartTime(
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
						long idleStartTime = 
								this.dataWorkerContext.getIdleStartTime();
						long timeSinceIdleStartTime = 
								System.currentTimeMillis() - idleStartTime;
						if (timeSinceIdleStartTime >= this.idleTimeout) {
							LOGGER.trace(
									LoggerHelper.objectMessage(
											this, 
											"Timeout reached for idle relay!"));
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
		
		public final long getIdleStartTime() {
			return this.relayServer.getIdleStartTime();
		}
		
		public final int getIdleTimeout() {
			return this.relayServer.idleTimeout;
		}
		
		public final InputStream getInputSocketInputStream() throws IOException {
			return this.inputSocket.getInputStream();
		}
		
		public final OutputStream getOutputSocketOutputStream() throws IOException {
			return this.outputSocket.getOutputStream();
		}
		
		public final void setIdleStartTime(final long time) {
			this.relayServer.setIdleStartTime(time);
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
	
	public static enum State {
		
		STARTED,
		
		STOPPED
		
	}
	
	private final Socket clientFacingSocket;
	private final int bufferSize;
	private ExecutorService executor;
	private long idleStartTime;
	private final int idleTimeout;
	private final Socket serverFacingSocket;
	private State state;
	
	public RelayServer(
			final Socket clientFacingSock, 
			final Socket serverFacingSock, 
			final int bffrSize, 
			final int idleTmt) {
		Objects.requireNonNull(
				clientFacingSock, "client-facing socket must not be null");
		Objects.requireNonNull(
				serverFacingSock, "server-facing socket must not be null");
		if (bffrSize < 1) {
			throw new IllegalArgumentException(
					"buffer size must not be less than 1");
		}
		if (idleTmt < 1) {
			throw new IllegalArgumentException(
					"idle idleTimeout must not be less than 1");
		}
		this.clientFacingSocket = clientFacingSock;
		this.bufferSize = bffrSize;
		this.executor = null;
		this.idleStartTime = 0L;
		this.idleTimeout = idleTmt;
		this.serverFacingSocket = serverFacingSock;
		this.state = State.STOPPED;
	}
	
	private synchronized long getIdleStartTime() {
		return this.idleStartTime;
	}
	
	public State getState() {
		return this.state;
	}
	
	private synchronized void setIdleStartTime(final long time) {
		this.idleStartTime = time;
	}
	
	public void start() throws IOException {
		if (this.state.equals(State.STARTED)) {
			throw new IllegalStateException("RelayServer already started");
		}
		this.idleStartTime = System.currentTimeMillis();
		this.executor = Executors.newFixedThreadPool(2);
		this.executor.execute(new DataWorker(new InboundDataWorkerContext(
				this)));
		this.executor.execute(new DataWorker(new OutboundDataWorkerContext(
				this)));
		this.state = State.STARTED;
	}
	
	public void stop() {
		if (this.state.equals(State.STOPPED)) {
			throw new IllegalStateException("RelayServer already stopped");
		}
		this.idleStartTime = 0L;
		this.executor.shutdownNow();
		this.executor = null;
		this.state = State.STOPPED;
	}
	
	private synchronized void stopIfNotStopped() {
		if (!this.state.equals(State.STOPPED)) {
			this.stop();
		}
	}
	
}
