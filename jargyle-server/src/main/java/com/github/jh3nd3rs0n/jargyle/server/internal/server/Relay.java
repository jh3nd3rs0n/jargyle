package com.github.jh3nd3rs0n.jargyle.server.internal.server;

import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;

import com.github.jh3nd3rs0n.jargyle.internal.concurrent.ExecutorsHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.jh3nd3rs0n.jargyle.internal.logging.ObjectLogMessageHelper;
import com.github.jh3nd3rs0n.jargyle.internal.throwable.ThrowableHelper;

public final class Relay {
	
	public static final class Builder {
	
		public static final int DEFAULT_BUFFER_SIZE = 1024;
		public static final int DEFAULT_IDLE_TIMEOUT = 60000;
		
		private int bufferSize;
		private final Socket clientSocket;
		private final Socket externalSocket;		
		private int idleTimeout;
		
		public Builder(final Socket clientSock, final Socket externalSock) {
			Objects.requireNonNull(clientSock);
			Objects.requireNonNull(externalSock);
			this.bufferSize = DEFAULT_BUFFER_SIZE;
			this.clientSocket = clientSock;
			this.externalSocket = externalSock;			
			this.idleTimeout = DEFAULT_IDLE_TIMEOUT;
		}
		
		public Builder bufferSize(final int bffrSize) {
			if (bffrSize < 0) {
				throw new IllegalArgumentException(
						"buffer size must be greater than 0");
			}
			this.bufferSize = bffrSize;
			return this;
		}
		
		public Relay build() {
			return new Relay(this);
		}
		
		public Builder idleTimeout(final int idleTmt) {
			if (idleTmt < 0) {
				throw new IllegalArgumentException(
						"idle timeout must be greater than 0");
			}
			this.idleTimeout = idleTmt;
			return this;
		}
		
	}
	
	private static abstract class DataWorker implements Runnable {
		
		private final int bufferSize;
		private final int idleTimeout;
		private final Socket inputSocket;
		private final InputStream inputStream;
		private final Logger logger;
		private final Socket outputSocket;
		private final OutputStream outputStream;
		private final Relay relay;
				
		public DataWorker(
				final Relay rly,
				final Socket inputSock,
				final Socket outputSock) throws IOException {
			this.bufferSize = rly.bufferSize;
			this.idleTimeout = rly.idleTimeout;
			this.inputSocket = inputSock;
			this.inputStream = inputSock.getInputStream();
			this.logger = LoggerFactory.getLogger(this.getClass());
			this.outputSocket = outputSock;
			this.outputStream = outputSock.getOutputStream();
			this.relay = rly;
		}
		
		@Override
		public final void run() {
			while (true) {
				try {
					int bytesRead = 0;
					byte[] buffer = new byte[this.bufferSize];
					IOException ioe = null;
					try {
						bytesRead = this.inputStream.read(buffer);
						this.relay.setIdleStartTime(
								System.currentTimeMillis());
						this.logger.trace(ObjectLogMessageHelper.objectLogMessage(
								this, 
								"Bytes read: %s", 
								bytesRead));
					} catch (IOException e) {
						ioe = e;
					}
					if (ioe != null) {
						if (ThrowableHelper.isOrHasInstanceOf(
								ioe, EOFException.class)) {
							// end of input stream reached
							break;
						} else if (ThrowableHelper.isOrHasInstanceOf(
								ioe, SocketException.class)) {
							// socket closed
							break;
						} else if (ThrowableHelper.isOrHasInstanceOf(
								ioe, SocketTimeoutException.class)) {
							bytesRead = 0;
						} else {
							this.logger.warn(
									ObjectLogMessageHelper.objectLogMessage(
											this, 
											"An exception occurred in the "
													+ "process of reading in "
													+ "data"),
									ioe);
							continue;
						}
					}
					if (bytesRead == -1) {
						break;
					}
					if (bytesRead == 0) {
						long idleStartTime = this.relay.getIdleStartTime();
						long timeSinceIdleStartTime = 
								System.currentTimeMillis() - idleStartTime;
						if (timeSinceIdleStartTime >= this.idleTimeout) {
							this.logger.trace(
									ObjectLogMessageHelper.objectLogMessage(
											this, 
											"Timeout reached for idle relay!"));
							break;
						}
						continue;
					}
					ioe = null;
					try {
						this.outputStream.write(buffer, 0, bytesRead);
					} catch (IOException e) {
						ioe = e;
					}
					if (ioe != null) {
						if (ThrowableHelper.isOrHasInstanceOf(
								ioe, SocketException.class)) {
							// socket closed
							break;
						} else {
							this.logger.warn(
									ObjectLogMessageHelper.objectLogMessage(
											this, 
											"An exception occurred in the "
													+ "process of writing "
													+ "out data"),
									ioe);
							continue;
						}						
					}
					ioe = null;
					try {
						this.outputStream.flush();
					} catch (IOException e) {
						ioe = e;
					}
					if (ioe != null) {
						if (ThrowableHelper.isOrHasInstanceOf(
								ioe, SocketException.class)) {
							// socket closed
							break;
						} else {
							this.logger.warn(
									ObjectLogMessageHelper.objectLogMessage(
											this, 
											"An exception occurred in the "
													+ "process of flushing "
													+ "out any data"),
									ioe);
						}
					}					
				} catch (Throwable t) {
					this.logger.warn(
							ObjectLogMessageHelper.objectLogMessage(
									this, 
									"An exception occurred in the process of "
									+ "relaying the data"), 
							t);
				}
			}
			this.relay.stopIfNotStopped();
		}

		@Override
		public final String toString() {
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
	
	private static final class InboundDataWorker extends DataWorker {

		public InboundDataWorker(final Relay rly) throws IOException {
			super(rly, rly.externalSocket, rly.clientSocket);
		}
		
	}
	
	private static final class OutboundDataWorker extends DataWorker {

		public OutboundDataWorker(final Relay rly) throws IOException {
			super(rly, rly.clientSocket, rly.externalSocket);
		}
		
	}
	
	public static enum State {
		
		STARTED,
		
		STOPPED
		
	}
	
	private final Socket clientSocket;
	private final int bufferSize;
	private ExecutorService executor;
	private final Socket externalSocket;	
	private final AtomicLong idleStartTime;
	private final int idleTimeout;
	private final AtomicReference<State> state;
	
	private Relay(final Builder builder) {
		this.clientSocket = builder.clientSocket;
		this.bufferSize = builder.bufferSize;
		this.executor = null;
		this.externalSocket = builder.externalSocket;		
		this.idleStartTime = new AtomicLong(0L);
		this.idleTimeout = builder.idleTimeout;
		this.state = new AtomicReference<State>(State.STOPPED);
	}
	
	private long getIdleStartTime() {
		return this.idleStartTime.get();
	}
	
	public State getState() {
		return this.state.get();
	}
	
	private void setIdleStartTime(final long time) {
		this.idleStartTime.set(time);
	}
	
	public void start() throws IOException {
		if (!this.state.compareAndSet(State.STOPPED, State.STARTED)) {
			throw new IllegalStateException("Relay already started");
		}
		this.idleStartTime.set(System.currentTimeMillis());
		this.executor =
				ExecutorsHelper.newVirtualThreadPerTaskExecutorOrElse(
						ExecutorsHelper.newFixedThreadPoolBuilder(2));
		this.executor.execute(new InboundDataWorker(this));
		this.executor.execute(new OutboundDataWorker(this));
	}
	
	public void stop() {
		if (!this.state.compareAndSet(State.STARTED, State.STOPPED)) {
			throw new IllegalStateException("Relay already stopped");
		}
		this.idleStartTime.set(0L);
		this.executor.shutdownNow();
		this.executor = null;
	}
	
	private void stopIfNotStopped() {
		if (!this.state.get().equals(State.STOPPED)) {
			try {
				this.stop();
			} catch (IllegalStateException e) {
				// the other thread stopped the relay
			}
		}
	}
	
}
