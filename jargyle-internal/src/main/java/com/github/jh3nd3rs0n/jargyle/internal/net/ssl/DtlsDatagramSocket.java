package com.github.jh3nd3rs0n.jargyle.internal.net.ssl;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLEngine;
import javax.net.ssl.SSLEngineResult;
import javax.net.ssl.SSLException;
import javax.net.ssl.SSLParameters;
import javax.net.ssl.SSLSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.jh3nd3rs0n.jargyle.internal.logging.ObjectLogMessageHelper;
import com.github.jh3nd3rs0n.jargyle.internal.net.FilterDatagramSocket;

public final class DtlsDatagramSocket extends FilterDatagramSocket {
	
	private static final class Connection {
		
		private static final int MAX_APP_READ_LOOPS = 60;
		private static final int MAX_HANDSHAKE_LOOPS = 200;
		
		private final DatagramSocket datagramSocket;
		private final DtlsDatagramSocket dtlsDatagramSocket;
		private volatile boolean established;
		private final Logger logger;
		private final int maximumPacketSize;
		private final SocketAddress peerSocketAddress;
		private final SSLEngine sslEngine;
		
		public Connection(
				final DtlsDatagramSocket dtlsDatagramSock,
				final SocketAddress peerSocketAddr) {
			InetSocketAddress peerSockAddr = (InetSocketAddress) peerSocketAddr;
			SSLEngine engine = dtlsDatagramSock.dtlsContext.createSSLEngine(
					peerSockAddr.getHostString(),
					peerSockAddr.getPort());
			String[] enabledCipherSuites = 
					dtlsDatagramSock.getEnabledCipherSuites();
			if (enabledCipherSuites.length > 0) {
				engine.setEnabledCipherSuites(enabledCipherSuites);
			}
			String[] enabledProtocols = dtlsDatagramSock.getEnabledProtocols(); 
			if (enabledProtocols.length > 0) {
				engine.setEnabledProtocols(enabledProtocols);
			}
			engine.setNeedClientAuth(dtlsDatagramSock.getNeedClientAuth());
			engine.setUseClientMode(dtlsDatagramSock.getUseClientMode());
			engine.setWantClientAuth(dtlsDatagramSock.getWantClientAuth());
			int maxPacketSize = dtlsDatagramSock.getMaximumPacketSize();
			SSLParameters sslParameters = new SSLParameters();
			sslParameters.setMaximumPacketSize(maxPacketSize);
			engine.setSSLParameters(sslParameters);
			this.datagramSocket = dtlsDatagramSock.datagramSocket;
			this.dtlsDatagramSocket = dtlsDatagramSock;
			this.established = false;
			this.logger = LoggerFactory.getLogger(Connection.class);
			this.maximumPacketSize = maxPacketSize;
			this.peerSocketAddress = peerSocketAddr;
			this.sslEngine = engine;
		}
		
		private void handshake() throws IOException {
			this.handshake(new byte[] { }, null);
		}
		
		private void handshake(
				final byte[] receivedDatagramBuffer,
				final DatagramPacket receivedDatagramPacket) 
				throws IOException {
			byte[] receivedDatagramBffr = receivedDatagramBuffer;
			DatagramPacket receivedDatagramPckt = receivedDatagramPacket;
			boolean endLoops = false;
			int loops = MAX_HANDSHAKE_LOOPS;
			this.sslEngine.beginHandshake();
			while (!endLoops) {
				if (--loops < 0) {
					throw new SSLException(
							"Too many loops to produce handshake packets");
				}
				SSLEngineResult.HandshakeStatus hs = 
						this.sslEngine.getHandshakeStatus();
				this.logger.debug(ObjectLogMessageHelper.objectLogMessage(
						this, 
						"Handshaking (iteration ID: %s, status: %s)",
						loops,
						hs));
				if (hs.equals(SSLEngineResult.HandshakeStatus.NEED_UNWRAP)
						|| hs.equals(SSLEngineResult.HandshakeStatus.NEED_UNWRAP_AGAIN)) {
					this.logger.debug(ObjectLogMessageHelper.objectLogMessage(
							this, 
							"Receive DTLS records, handshake status is %s",
							hs));
					ByteBuffer inNetData;
					ByteBuffer inAppData;
					if (hs.equals(
							SSLEngineResult.HandshakeStatus.NEED_UNWRAP)) {
						byte[] buffer = null;
						DatagramPacket packet = null;
						if (receivedDatagramBffr.length == 0 
								&& receivedDatagramPckt == null) {
							buffer = new byte[this.maximumPacketSize];
							packet = new DatagramPacket(buffer, buffer.length);
							try {
								this.datagramSocket.receive(packet);
							} catch (SocketTimeoutException ste) {
								this.logger.debug(
										ObjectLogMessageHelper.objectLogMessage(
												this,
												"Warning: %s",
												ste));
								List<DatagramPacket> packets = 
										new ArrayList<DatagramPacket>();
								boolean finished = this.reproduceHandshakePackets(
										packets, 
										this.peerSocketAddress);
								this.logger.debug(
										ObjectLogMessageHelper.objectLogMessage(
												this,
												"Reproduced %s packets",
												packets.size()));
								for (DatagramPacket p : packets) {
									this.datagramSocket.send(p);
								}
								if (finished) {
									this.logger.debug(
											ObjectLogMessageHelper.objectLogMessage(
													this,
													"Handshake status is FINISHED "
													+ "after calling "
													+ "reproduceHandshakePackets(), "
													+ "finish the loop"));
									endLoops = true;
								}
								this.logger.debug(
										ObjectLogMessageHelper.objectLogMessage(
												this,
												"New handshake status is %s",
												this.sslEngine.getHandshakeStatus()));
								continue;
							}
						} else {
							buffer = receivedDatagramBffr;
							packet = receivedDatagramPckt;
							receivedDatagramBffr = new byte[] { };
							receivedDatagramPckt = null;
						}
						inNetData = ByteBuffer.wrap(
								buffer, 0, packet.getLength());
						inAppData = ByteBuffer.allocate(this.maximumPacketSize);
					} else {
						inNetData = ByteBuffer.allocate(0);
						inAppData = ByteBuffer.allocate(this.maximumPacketSize);
					}
					SSLEngineResult r = this.sslEngine.unwrap(
							inNetData, inAppData);
					SSLEngineResult.Status rs = r.getStatus();
					hs = r.getHandshakeStatus();
					if (rs.equals(SSLEngineResult.Status.OK)) {
						// OK
					} else if (rs.equals(
							SSLEngineResult.Status.BUFFER_OVERFLOW)) {
						this.logger.debug(ObjectLogMessageHelper.objectLogMessage(
								this, 
								"BUFFER_OVERFLOW, handshake status is %s",
								hs));
						// the client maximum fragment size config does not work?
						throw new SSLException("Buffer overflow: incorrect "
								+ "client maximum fragment size");
					} else if (rs.equals(
							SSLEngineResult.Status.BUFFER_UNDERFLOW)) {
						this.logger.debug(ObjectLogMessageHelper.objectLogMessage(
								this, 
								"BUFFER_UNDERFLOW, handshake status is %s",
								hs));
						// bad packet, or the client maximum fragment size
						// config does not work?
						if (!hs.equals(
								SSLEngineResult.HandshakeStatus.NOT_HANDSHAKING)) {
							throw new SSLException("Buffer underflow: "
									+ "incorrect client maximum fragment size");
						} // otherwise, ignore this packet
					} else if (rs.equals(SSLEngineResult.Status.CLOSED)) {
						throw new SSLException(String.format(
								"SSL engine closed, handshake status is %s", 
								hs));
					} else {
						throw new SSLException(String.format(
								"Can't reach here, result is %s", 
								rs));
					}
					if (hs.equals(SSLEngineResult.HandshakeStatus.FINISHED)) {
						this.logger.debug(ObjectLogMessageHelper.objectLogMessage(
								this, 
								"Handshake status is FINISHED, finish the "
								+ "loop"));
						endLoops = true;
					}
				} else if (hs.equals(SSLEngineResult.HandshakeStatus.NEED_WRAP)) {
					List<DatagramPacket> packets = 
							new ArrayList<DatagramPacket>();
					boolean finished = this.produceHandshakePackets(
							packets, 
							this.peerSocketAddress);
					this.logger.debug(ObjectLogMessageHelper.objectLogMessage(
							this, 
							"Produced %s packets", 
							packets.size()));
					for (DatagramPacket p : packets) {
						this.datagramSocket.send(p);
					}
					if (finished) {
						this.logger.debug(ObjectLogMessageHelper.objectLogMessage(
								this, 
								"Handshake status is FINISHED after producing "
								+ "handshake packets, finish the loop"));
						endLoops = true;
					}
				} else if (hs.equals(
						SSLEngineResult.HandshakeStatus.NEED_TASK)) {
					this.runDelegatedTasks();
				} else if (hs.equals(
						SSLEngineResult.HandshakeStatus.NOT_HANDSHAKING)) {
					this.logger.debug(ObjectLogMessageHelper.objectLogMessage(
							this, 
							"Handshake status is NOT_HANDSHAKING, finish the "
							+ "loop"));
					endLoops = true;
				} else if (hs.equals(
						SSLEngineResult.HandshakeStatus.FINISHED)) {
					throw new SSLException(
							"Unexpected status, SSLEngine.getHandshakeStatus() "
							+ "shouldn't return FINISHED");
				} else {
					throw new SSLException(String.format(
							"Can't reach here, handshake status is %s", 
							hs));
				}
			}
			SSLEngineResult.HandshakeStatus hs = 
					this.sslEngine.getHandshakeStatus();
			this.logger.debug(ObjectLogMessageHelper.objectLogMessage(
					this, 
					"Handshake finished, status is %s", 
					hs));
			if (this.sslEngine.getHandshakeSession() != null) {
				throw new SSLException(
						"Handshake finished, but handshake session is not "
						+ "null");
			}
			SSLSession session = this.sslEngine.getSession();
			if (session == null) {
				throw new SSLException("Handshake finished, but session is "
						+ "null");
			}
			this.logger.debug(ObjectLogMessageHelper.objectLogMessage(
					this, 
					"Negotiated protocol is %s", 
					session.getProtocol()));
			this.logger.debug(ObjectLogMessageHelper.objectLogMessage(
					this, 
					"Negotiated cipher suite is %s", 
					session.getCipherSuite()));
			// handshake status should be NOT_HANDSHAKING
			//
			// According to the spec, SSLEngine.getHandshakeStatus() can't
			// return FINISHED.
			if (!hs.equals(SSLEngineResult.HandshakeStatus.NOT_HANDSHAKING)) {
				throw new SSLException(String.format(
						"Unexpected handshake status %s", hs));
			}
			this.established = true;
		}
		
		public boolean isEstablished() {
			return this.established;
		}
		
		private List<DatagramPacket> produceApplicationPackets(
				final ByteBuffer outAppData, 
				final SocketAddress peerSocketAddr) throws IOException {
			List<DatagramPacket> packets = new ArrayList<DatagramPacket>();
			ByteBuffer outNetData = ByteBuffer.allocate(this.maximumPacketSize);
			SSLEngineResult r = this.sslEngine.wrap(outAppData, outNetData);
			outNetData.flip();
			SSLEngineResult.Status rs = r.getStatus();
			if (rs.equals(SSLEngineResult.Status.BUFFER_OVERFLOW)) {
				throw new SSLException("Buffer overflow during wrapping");
			} else if (rs.equals(SSLEngineResult.Status.BUFFER_UNDERFLOW)) {
				throw new SSLException("Buffer underflow during wrapping");
			} else if (rs.equals(SSLEngineResult.Status.CLOSED)) {
				throw new SSLException("SSLEngine has closed");
			} else if (rs.equals(SSLEngineResult.Status.OK)) {
				// OK
			} else {
				throw new SSLException(String.format(
						"Can't reach here, result is %s", 
						rs));
			}
			// SSLEngineResult.Status.OK:
			if (outNetData.hasRemaining()) {
				byte[] bytes = new byte[outNetData.remaining()];
				outNetData.get(bytes);
				DatagramPacket packet = new DatagramPacket(
						bytes, bytes.length, peerSocketAddr);
				packets.add(packet);
			}
			return packets;
		}
		
		private boolean produceHandshakePackets(
				final List<DatagramPacket> packets,
				final SocketAddress peerSocketAddr) throws IOException {
			boolean endLoops = false;
			int loops = MAX_HANDSHAKE_LOOPS / 2;
			while (!endLoops) {
				if (--loops < 0) {
					throw new SSLException(
							"Too many loops to produce handshake packets");
				}
				ByteBuffer outAppData = ByteBuffer.allocate(0);
				ByteBuffer outNetData = ByteBuffer.allocate(
						this.maximumPacketSize);
				SSLEngineResult r = this.sslEngine.wrap(outAppData, outNetData);
				outNetData.flip();
				SSLEngineResult.Status rs = r.getStatus();
				SSLEngineResult.HandshakeStatus hs = r.getHandshakeStatus();
				this.logger.debug(ObjectLogMessageHelper.objectLogMessage(
						this, 
						"Producing handshake packet (iteration ID: %s, result "
						+ "status: %s, handshake status: %s)",
						loops,
						rs,
						hs));
				if (rs.equals(SSLEngineResult.Status.BUFFER_OVERFLOW)) {
					// the client maximum fragment size config does not work?
					throw new SSLException("Buffer overflow: incorrect server "
							+ "maximum fragment size");
				} else if (rs.equals(
						SSLEngineResult.Status.BUFFER_UNDERFLOW)) {
					this.logger.debug(ObjectLogMessageHelper.objectLogMessage(
							this, 
							"Produce handshake packets: BUFFER_UNDERFLOW "
							+ "occured"));
					this.logger.debug(ObjectLogMessageHelper.objectLogMessage(
							this, 
							"Produce handshake packets: Handshake status: %s",
							hs));
					// bad packet, or the client maximum fragment size
					// config does not work?
					if (!hs.equals(
							SSLEngineResult.HandshakeStatus.NOT_HANDSHAKING)) {
						throw new SSLException("Buffer underflow: incorrect "
								+ "server maximum fragment size");
					} // otherwise, ignore this packet
				} else if (rs.equals(SSLEngineResult.Status.CLOSED)) {
					throw new SSLException("SSLEngine has closed");
				} else if (rs.equals(SSLEngineResult.Status.OK)) {
					// OK
				} else {
					throw new SSLException(String.format(
							"Can't reach here, result is %s", 
							rs));
				}
				// SSLEngineResult.Status.OK:
				if (outNetData.hasRemaining()) {
					byte[] bytes = new byte[outNetData.remaining()];
					outNetData.get(bytes);
					DatagramPacket packet = new DatagramPacket(
							bytes, bytes.length, peerSocketAddr);
					packets.add(packet);
				}
				if (hs.equals(SSLEngineResult.HandshakeStatus.FINISHED)) {
					this.logger.debug(ObjectLogMessageHelper.objectLogMessage(
							this, 
							"Produce handshake packets: "
							+ "Handshake status is FINISHED, finish the loop"));
					return true;
				}
				boolean endInnerLoop = false;
				SSLEngineResult.HandshakeStatus nhs = hs;
				while (!endInnerLoop) {
					if (nhs.equals(SSLEngineResult.HandshakeStatus.NEED_TASK)) {
						this.runDelegatedTasks();
					} else if (nhs.equals(SSLEngineResult.HandshakeStatus.NEED_UNWRAP)
							|| nhs.equals(SSLEngineResult.HandshakeStatus.NEED_UNWRAP_AGAIN)
							|| nhs.equals(SSLEngineResult.HandshakeStatus.NOT_HANDSHAKING)) {
						endInnerLoop = true;
						endLoops = true;
					} else if (nhs.equals(
							SSLEngineResult.HandshakeStatus.NEED_WRAP)) {
						endInnerLoop = true;
					} else if (nhs.equals(
							SSLEngineResult.HandshakeStatus.FINISHED)) {
						throw new SSLException("Unexpected status, "
								+ "SSLEngine.getHandshakeStatus() shouldn't "
								+ "return FINISHED");
					} else {
						throw new SSLException(String.format(
								"Can't reach here, handshake status is %s", 
								nhs));
					}
					nhs = this.sslEngine.getHandshakeStatus();
				}
			}
			return false;
		}
		
		public void receive(
				final byte[] receivedDatagramBuffer,
				final DatagramPacket receivedDatagramPacket, 
				final DatagramPacket p) throws IOException {
			byte[] receivedDatagramBffr = receivedDatagramBuffer;
			DatagramPacket receivedDatagramPckt = receivedDatagramPacket;
			if (!this.sslEngine.getUseClientMode() && !this.established) {
				this.handshake(receivedDatagramBffr, receivedDatagramPckt);
				receivedDatagramBffr = new byte[] { };
				receivedDatagramPckt = null;
			}
			int loops = MAX_APP_READ_LOOPS;
			while (true) {
				if (--loops < 0) {
					throw new SSLException(
							"Too many loops to receive application data");
				}
				byte[] buffer = null;
				DatagramPacket packet = null;
				if (receivedDatagramBffr.length == 0 
						&& receivedDatagramPckt == null) {
					buffer = new byte[this.maximumPacketSize];
					packet = new DatagramPacket(buffer, buffer.length);
					this.datagramSocket.receive(packet);
				} else {
					buffer = receivedDatagramBffr;
					packet = receivedDatagramPckt;
					receivedDatagramBffr = new byte[] { };
					receivedDatagramPckt = null;
				}
				ByteBuffer inNetData = ByteBuffer.wrap(
						buffer, 0, packet.getLength());
				ByteBuffer inAppData = ByteBuffer.allocate(p.getLength());
				this.sslEngine.unwrap(inNetData, inAppData);
				inAppData.flip();
				if (inAppData.hasRemaining()) {
					byte[] bytes = new byte[inAppData.remaining()];
					inAppData.get(bytes);
					p.setSocketAddress(packet.getSocketAddress());
					p.setData(bytes, 0, bytes.length);
					p.setLength(bytes.length);
					return;
				}
			}
		}
		
		private boolean reproduceHandshakePackets(
				final List<DatagramPacket> packets, 
				final SocketAddress peerSocketAddr) throws IOException {
			SSLEngineResult.HandshakeStatus hs = 
					this.sslEngine.getHandshakeStatus();
			if (hs.equals(SSLEngineResult.HandshakeStatus.NOT_HANDSHAKING)) {
				return false;
			} else {
				// reproduce handshake packets
				return this.produceHandshakePackets(packets, peerSocketAddr);
			}
		}
		
		private void runDelegatedTasks() throws IOException {
			Runnable runnable;
			while ((runnable = this.sslEngine.getDelegatedTask()) != null) {
				runnable.run();
			}
			SSLEngineResult.HandshakeStatus hs = 
					this.sslEngine.getHandshakeStatus();
			if (hs.equals(SSLEngineResult.HandshakeStatus.NEED_TASK)) {
				throw new SSLException(
						"Handshake shouldn't need additional tasks");
			}
		}

		public void send(final DatagramPacket p) throws IOException {
			if (this.sslEngine.getUseClientMode() && !this.established) {
				this.handshake();
			}
			ByteBuffer outAppData = ByteBuffer.wrap(Arrays.copyOfRange(
					p.getData(), p.getOffset(), p.getLength()));
			// Note: have not considered the packet losses
			List<DatagramPacket> packets = this.produceApplicationPackets(
					outAppData, p.getSocketAddress());
			outAppData.flip();
			for (DatagramPacket packet : packets) {
				this.datagramSocket.send(packet);
			}
		}
		
		@Override
		public String toString() {
			StringBuilder builder = new StringBuilder();
			builder.append(this.getClass().getSimpleName())
				.append(" [dtlsDatagramSocket=")
				.append(this.dtlsDatagramSocket)
				.append(", peerSocketAddress=")
				.append(this.peerSocketAddress)
				.append("]");
			return builder.toString();
		}
		
	}
	
	private static final int HALF_SECOND = 500;
	
	private final SSLContext dtlsContext;
	private final Map<SocketAddress, Connection> connections;
	private volatile String[] enabledCipherSuites;
	private volatile String[] enabledProtocols;
	private volatile int establishedConnectionCount;
	private volatile boolean inUse;
	private volatile int maximumPacketSize;
	private volatile boolean needClientAuth;
	private volatile boolean useClientMode;
	private volatile boolean wantClientAuth;

	DtlsDatagramSocket(
			final DatagramSocket datagramSock, 
			final SSLContext dtlsCntxt) throws SocketException {
		super(datagramSock);
		this.dtlsContext = dtlsCntxt;
		this.connections = new HashMap<SocketAddress, Connection>();
		this.enabledCipherSuites = new String[] { };
		this.enabledProtocols = new String[] { };
		this.establishedConnectionCount = 0;
		this.inUse = false;
		this.maximumPacketSize = 0;
		this.needClientAuth = false;
		this.useClientMode = false;
		this.wantClientAuth = false;
	}
	
	@Override
	public DatagramChannel getChannel() {
		throw new UnsupportedOperationException();
	}
	
	public String[] getEnabledCipherSuites() {
		return Arrays.copyOf(
				this.enabledCipherSuites, this.enabledCipherSuites.length);
	}
	
	public String[] getEnabledProtocols() {
		return Arrays.copyOf(
				this.enabledProtocols, this.enabledProtocols.length);
	}
	
	public int getMaximumPacketSize() {
		return this.maximumPacketSize;
	}
	
	public boolean getNeedClientAuth() {
		return this.needClientAuth;
	}
	
	public boolean getUseClientMode() {
		return this.useClientMode;
	}
	
	public boolean getWantClientAuth() {
		return this.wantClientAuth;
	}
	
	@Override
	public synchronized void receive(
			final DatagramPacket p) throws IOException {
		this.inUse = true;
		if (this.useClientMode) {
			this.waitForEstablishedConnections();
		}
		byte[] buffer = new byte[this.maximumPacketSize];
		DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
		this.datagramSocket.receive(packet);
		SocketAddress socketAddress = packet.getSocketAddress();
		Connection connection = this.connections.get(
				packet.getSocketAddress());
		boolean connectionPresent = connection != null; 
		if (!connectionPresent) {
			connection = new Connection(this, socketAddress);
			this.connections.put(socketAddress, connection);
		}
		try {
			connection.receive(buffer, packet, p);
		} catch (Throwable t) {
			this.connections.remove(socketAddress);
			if (connectionPresent && connection.isEstablished()) {
				this.establishedConnectionCount--;
			}
			throw t;
		}
		if (!connectionPresent && connection.isEstablished()) {
			this.establishedConnectionCount++;
		}
	}

	@Override
	public void send(final DatagramPacket p) throws IOException {
		this.inUse = true;
		SocketAddress socketAddress = p.getSocketAddress();
		Connection connection = this.connections.get(p.getSocketAddress());
		boolean connectionPresent = connection != null; 
		if (!connectionPresent) {
			connection = new Connection(this, socketAddress);
			this.connections.put(socketAddress, connection);
		}
		try {
			connection.send(p);
		} catch (Throwable t) {
			this.connections.remove(socketAddress);
			if (connectionPresent && connection.isEstablished()) {
				this.establishedConnectionCount--;
			}
			throw t;
		}
		if (!connectionPresent && connection.isEstablished()) {
			this.establishedConnectionCount++;
		}
	}
	
	public void setEnabledCipherSuites(final String[] suites) {
		if (this.inUse) {
			throw new IllegalStateException("DtlsDatagramSocket in use");
		}
		this.enabledCipherSuites = Arrays.copyOf(suites, suites.length);
	}

	public void setEnabledProtocols(final String[] protocols) {
		if (this.inUse) {
			throw new IllegalStateException("DtlsDatagramSocket in use");
		}		
		this.enabledProtocols = Arrays.copyOf(protocols, protocols.length);
	}

	public void setMaximumPacketSize(final int maxPacketSize) {
		if (this.inUse) {
			throw new IllegalStateException("DtlsDatagramSocket in use");
		}		
		if (maxPacketSize < 0) {
			throw new IllegalArgumentException(
					"maximum packet size must be at least 0");
		}
		this.maximumPacketSize = maxPacketSize;
	}
	
	public void setNeedClientAuth(final boolean need) {
		if (this.inUse) {
			throw new IllegalStateException("DtlsDatagramSocket in use");
		}		
		this.needClientAuth = need;
	}

	public void setUseClientMode(final boolean mode) {
		if (this.inUse) {
			throw new IllegalStateException("DtlsDatagramSocket in use");
		}		
		this.useClientMode = mode;
	}

	public void setWantClientAuth(final boolean want) {
		if (this.inUse) {
			throw new IllegalStateException("DtlsDatagramSocket in use");
		}		
		this.wantClientAuth = want;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append(this.getClass().getSimpleName())
			.append(" [getLocalSocketAddress()=")
			.append(this.getLocalSocketAddress())
			.append(", getUseClientMode()=")
			.append(this.getUseClientMode())
			.append("]");
		return builder.toString();
	}

	private void waitForEstablishedConnections() throws IOException {
		int soTimeout = this.datagramSocket.getSoTimeout();
		long waitStartTime = System.currentTimeMillis();
		while (this.establishedConnectionCount == 0 
				|| this.establishedConnectionCount < this.connections.size()) {
			try {
				Thread.sleep(HALF_SECOND);
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
			}
			if (soTimeout == 0) { continue; }
			long timeSinceWaitStartTime = 
					System.currentTimeMillis() - waitStartTime;
			if (timeSinceWaitStartTime >= soTimeout) {
				throw new SocketTimeoutException(
						"Timeout for waiting for established connections has "
						+ "been reached");
			}			
		}
	}

}
