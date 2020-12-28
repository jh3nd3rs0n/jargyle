package jargyle.server;

import java.io.IOException;
import java.io.InputStream;
import java.io.InterruptedIOException;
import java.io.OutputStream;
import java.net.SocketException;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;

import jargyle.common.net.SocketInterface;

public final class TcpRelayServer {

	private static class DataWorker implements Runnable {
		
		private static final Logger LOGGER = Logger.getLogger(
				DataWorker.class.getName());
		
		private final TcpRelayServer tcpRelayServer;
		private final InputStream input;
		private final SocketInterface inputSocketInterface;
		private final OutputStream output;
		private final SocketInterface outputSocketInterface;
				
		public DataWorker(
				final TcpRelayServer server,
				final SocketInterface inSocketInterface,
				final SocketInterface outSocketInterface) throws IOException {
			this.tcpRelayServer = server;
			this.input = inSocketInterface.getInputStream();
			this.inputSocketInterface = inSocketInterface;
			this.output = outSocketInterface.getOutputStream();
			this.outputSocketInterface = outSocketInterface;
		}
		
		private String format(final String message) {
			return String.format("%s: %s", this, message);
		}

		@Override
		public void run() {
			this.tcpRelayServer.lastReadTime = System.currentTimeMillis();
			while (true) {
				try {
					int bytesRead = 0;
					byte[] buffer = new byte[this.tcpRelayServer.bufferSize];
					try {
						bytesRead = this.input.read(buffer);
						this.tcpRelayServer.lastReadTime = System.currentTimeMillis();
						LOGGER.log(
								Level.FINE, 
								this.format(String.format(
										"Bytes read: %s", 
										bytesRead)));
					} catch (SocketException e) {
						// socket closed
						break;
					} catch (InterruptedIOException e) {
						bytesRead = 0;
					} catch (IOException e) {
						LOGGER.log(
								Level.WARNING,
								this.format("Error occurred in the process of reading in data"), 
								e);
						break;
					}
					if (bytesRead == -1) {
						break;
					}
					if (bytesRead == 0) {
						long timeSinceRead = 
								System.currentTimeMillis() - this.tcpRelayServer.lastReadTime;
						if (timeSinceRead >= this.tcpRelayServer.timeout) {
							break;
						}
						continue;
					}
					try {
						this.output.write(buffer, 0, bytesRead);
					} catch (SocketException e) {
						// socket closed
						break;
					} catch (IOException e) {
						LOGGER.log(
								Level.WARNING,
								this.format("Error occurred in the process of writing out data"), 
								e);
						break;
					}
					try {
						this.output.flush();
					} catch (SocketException e) {
						// socket closed
						break;
					} catch (IOException e) {
						LOGGER.log(
								Level.WARNING,
								this.format("Error occurred in the process of flushing out any data"), 
								e);
						break;
					}					
				} catch (Throwable t) {
					LOGGER.log(
							Level.WARNING,
							this.format("Error occurred in the process of relaying the data"), 
							t);
					break;
				}
			}
			if (!this.tcpRelayServer.firstDataWorkerFinished) {
				this.tcpRelayServer.firstDataWorkerFinished = true;
			} else {
				if (!this.tcpRelayServer.stopped) {
					this.tcpRelayServer.stop();
				}
			}
		}

		@Override
		public String toString() {
			StringBuilder builder = new StringBuilder();
			builder.append(this.getClass().getSimpleName())
				.append(" [inputSocketInterface=")
				.append(this.inputSocketInterface)
				.append(", outputSocketInterface=")
				.append(this.outputSocketInterface)
				.append("]");
			return builder.toString();
		}
		
	}
	
	private static final class IncomingDataWorker extends DataWorker {

		public IncomingDataWorker(
				final TcpRelayServer server) throws IOException {
			super(
					server, 
					server.serverSocketInterface, 
					server.clientSocketInterface);
		}
		
	}
	
	private static final class OutgoingDataWorker extends DataWorker {

		public OutgoingDataWorker(
				final TcpRelayServer server) throws IOException {
			super(
					server, 
					server.clientSocketInterface, 
					server.serverSocketInterface);
		}
		
	}
	
	private final SocketInterface clientSocketInterface;
	private final int bufferSize;
	private ExecutorService executor;
	private boolean firstDataWorkerFinished;
	private long lastReadTime;
	private final SocketInterface serverSocketInterface;
	private boolean started;
	private boolean stopped;
	private final int timeout;
	
	public TcpRelayServer(
			final SocketInterface clientSockInterface, 
			final SocketInterface serverSockInterface, 
			final int bffrSize, 
			final int tmt) {
		Objects.requireNonNull(
				clientSockInterface, 
				"client socket interface must not be null");
		Objects.requireNonNull(
				serverSockInterface, 
				"server-facing socket interface must not be null");
		if (bffrSize < 1) {
			throw new IllegalArgumentException("buffer size must not be less than 1");
		}
		if (tmt < 1) {
			throw new IllegalArgumentException("timeout must not be less than 1");
		}
		this.clientSocketInterface = clientSockInterface;
		this.bufferSize = bffrSize;
		this.executor = null;
		this.firstDataWorkerFinished = false;
		this.lastReadTime = 0L;
		this.serverSocketInterface = serverSockInterface;
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