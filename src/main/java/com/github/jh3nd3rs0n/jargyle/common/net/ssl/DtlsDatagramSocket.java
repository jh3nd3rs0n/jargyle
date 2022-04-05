package com.github.jh3nd3rs0n.jargyle.common.net.ssl;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketAddress;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.net.ssl.SSLEngine;
import javax.net.ssl.SSLEngineResult;
import javax.net.ssl.SSLException;
import javax.net.ssl.SSLParameters;
import javax.net.ssl.SSLSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.jh3nd3rs0n.jargyle.common.net.FilterDatagramSocket;
import com.github.jh3nd3rs0n.jargyle.internal.logging.ObjectLogMessageHelper;

public final class DtlsDatagramSocket extends FilterDatagramSocket {
	
	private static enum HandshakeStatus {
		UNINITIATED,
		INITIATED,
		COMPLETED
	}
	
	
	private static final class HandshakeStatusHelper {
		
		private static final Map<UUID, HandshakeStatus> MAP =
				Collections.synchronizedMap(new HashMap<UUID, HandshakeStatus>());
		
		public static boolean has(
				final UUID uuid, final HandshakeStatus associationStatus) {
			System.out.printf("Getting handshake status%n");
			if (!MAP.containsKey(uuid)) {
				return false;
			}
			HandshakeStatus value = MAP.get(uuid);
			System.out.printf("Handshake status is %s%n", value);
			return value.equals(associationStatus);
		}
		
		public static void put(
				final UUID uuid, final HandshakeStatus associationStatus) {
			MAP.put(uuid, associationStatus);
		}
		
		public static void remove(final UUID uuid) {
			MAP.remove(uuid);
		}
		
		private HandshakeStatusHelper() { }
		
	}
	
	private static final Logger LOGGER = LoggerFactory.getLogger(
			DtlsDatagramSocket.class);

	private static final int HALF_SECOND = 500;
	private static final int MAX_APP_READ_LOOPS = 60;
	private static final int MAX_HANDSHAKE_LOOPS = 200;
	
	// private UUID handshakeStatusUuid;
	private boolean handshakeCompleted;
	private final SSLEngine sslEngine;

	DtlsDatagramSocket(
			final DatagramSocket datagramSock, 
			final SSLEngine engine) throws SocketException {
		super(datagramSock);
		/*
		UUID statusUuid = UUID.randomUUID();
		HandshakeStatusHelper.put(statusUuid, HandshakeStatus.UNINITIATED);
		this.handshakeStatusUuid = statusUuid;
		*/
		this.handshakeCompleted = false;
		this.sslEngine = engine;
	}

	@Override
	public void close() {
		// HandshakeStatusHelper.remove(this.handshakeStatusUuid);
		this.handshakeCompleted = false;
		super.close();
	}
	
	@Override
	public DatagramChannel getChannel() {
		throw new UnsupportedOperationException();
	}
	
	public String[] getEnabledCipherSuites() {
		return this.sslEngine.getEnabledCipherSuites();
	}
	
	public String[] getEnabledProtocols() {
		return this.sslEngine.getEnabledProtocols();
	}
	
	public int getMaximumPacketSize() {
		SSLParameters sslParameters = this.sslEngine.getSSLParameters();
		return sslParameters.getMaximumPacketSize();
	}
	
	public boolean getNeedClientAuth() {
		return this.sslEngine.getNeedClientAuth();
	}
	
	public boolean getUseClientMode() {
		return this.sslEngine.getUseClientMode();
	}
	
	public boolean getWantClientAuth() {
		return this.sslEngine.getWantClientAuth();
	}
	
	private void handshake() throws IOException {
		if (!super.isConnected()) { 
			throw new IllegalStateException(
					"DtlsDatagramSocket must be connected before handshake "
					+ "can be performed");
		}
		/*
		HandshakeStatusHelper.put(
				this.handshakeStatusUuid, HandshakeStatus.INITIATED);
		*/
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
			LOGGER.debug(ObjectLogMessageHelper.objectLogMessage(
					this, 
					"Handshaking (iteration ID: %s, status: %s)",
					loops,
					hs));
			if (hs.equals(SSLEngineResult.HandshakeStatus.NEED_UNWRAP)
					|| hs.equals(SSLEngineResult.HandshakeStatus.NEED_UNWRAP_AGAIN)) {
				LOGGER.debug(ObjectLogMessageHelper.objectLogMessage(
						this, 
						"Receive DTLS records, handshake status is %s",
						hs));
				ByteBuffer inNetData;
				ByteBuffer inAppData;
				if (hs.equals(SSLEngineResult.HandshakeStatus.NEED_UNWRAP)) {
					byte[] buffer = new byte[this.getMaximumPacketSize()];
					DatagramPacket packet = new DatagramPacket(
							buffer, buffer.length);
					try {
						this.datagramSocket.receive(packet);
					} catch (SocketTimeoutException ste) {
						LOGGER.debug(ObjectLogMessageHelper.objectLogMessage(
								this, 
								"Warning: %s", 
								ste));
						List<DatagramPacket> packets = 
								new ArrayList<DatagramPacket>();
						boolean finished = this.reproduceHandshakePackets(
								packets, 
								this.datagramSocket.getRemoteSocketAddress());
						LOGGER.debug(ObjectLogMessageHelper.objectLogMessage(
								this, 
								"Reproduced %s packets",
								packets.size()));
						for (DatagramPacket p : packets) {
							LOGGER.debug(
									ObjectLogMessageHelper.objectLogMessage(
											this, 
											"Reproduced packet %s",
											Arrays.toString(
													Arrays.copyOfRange(
															p.getData(),
															p.getOffset(),
															p.getLength()))));
							this.datagramSocket.send(p);
						}
						if (finished) {
							LOGGER.debug(ObjectLogMessageHelper.objectLogMessage(
									this, 
									"Handshake status is FINISHED after "
									+ "calling reproduceHandshakePackets(), "
									+ "finish the loop"));
							endLoops = true;
						}
						LOGGER.debug(ObjectLogMessageHelper.objectLogMessage(
								this, 
								"New handshake status is %s",
								this.sslEngine.getHandshakeStatus()));
						continue;
					}
					inNetData = ByteBuffer.wrap(buffer, 0, packet.getLength());
					inAppData = ByteBuffer.allocate(this.getMaximumPacketSize());
				} else {
					inNetData = ByteBuffer.allocate(0);
					inAppData = ByteBuffer.allocate(this.getMaximumPacketSize());
				}
				SSLEngineResult r = this.sslEngine.unwrap(inNetData, inAppData);
				SSLEngineResult.Status rs = r.getStatus();
				hs = r.getHandshakeStatus();
				if (rs.equals(SSLEngineResult.Status.OK)) {
					// OK
				} else if (rs.equals(SSLEngineResult.Status.BUFFER_OVERFLOW)) {
					LOGGER.debug(ObjectLogMessageHelper.objectLogMessage(
							this, 
							"BUFFER_OVERFLOW, handshake status is %s",
							hs));
					// the client maximum fragment size config does not work?
					throw new SSLException("Buffer overflow: incorrect client "
							+ "maximum fragment size");
				} else if (rs.equals(SSLEngineResult.Status.BUFFER_UNDERFLOW)) {
					LOGGER.debug(ObjectLogMessageHelper.objectLogMessage(
							this, 
							"BUFFER_UNDERFLOW, handshake status is %s",
							hs));
					// bad packet, or the client maximum fragment size
					// config does not work?
					if (!hs.equals(SSLEngineResult.HandshakeStatus.NOT_HANDSHAKING)) {
						throw new SSLException("Buffer underflow: incorrect "
								+ "client maximum fragment size");
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
					LOGGER.debug(ObjectLogMessageHelper.objectLogMessage(
							this, 
							"Handshake status is FINISHED, finish the loop"));
					endLoops = true;
				}
			} else if (hs.equals(SSLEngineResult.HandshakeStatus.NEED_WRAP)) {
				List<DatagramPacket> packets = new ArrayList<DatagramPacket>();
				boolean finished = this.produceHandshakePackets(
						packets, 
						this.datagramSocket.getRemoteSocketAddress());
				LOGGER.debug(ObjectLogMessageHelper.objectLogMessage(
						this, 
						"Produced %s packets", 
						packets.size()));
				for (DatagramPacket p : packets) {
					this.datagramSocket.send(p);
				}
				if (finished) {
					LOGGER.debug(ObjectLogMessageHelper.objectLogMessage(
							this, 
							"Handshake status is FINISHED after producing "
							+ "handshake packets, finish the loop"));
					endLoops = true;
				}
			} else if (hs.equals(SSLEngineResult.HandshakeStatus.NEED_TASK)) {
				this.runDelegatedTasks();
			} else if (hs.equals(SSLEngineResult.HandshakeStatus.NOT_HANDSHAKING)) {
				LOGGER.debug(ObjectLogMessageHelper.objectLogMessage(
						this, 
						"Handshake status is NOT_HANDSHAKING, finish the loop"));
				endLoops = true;
			} else if (hs.equals(SSLEngineResult.HandshakeStatus.FINISHED)) {
				throw new SSLException(
						"Unexpected status, SSLEngine.getHandshakeStatus() "
						+ "shouldn't return FINISHED");
			} else {
				throw new SSLException(String.format(
						"Can't reach here, handshake status is %s", 
						hs));
			}
		}
		SSLEngineResult.HandshakeStatus hs = this.sslEngine.getHandshakeStatus();
		LOGGER.debug(ObjectLogMessageHelper.objectLogMessage(
				this, 
				"Handshake finished, status is %s", 
				hs));
		if (this.sslEngine.getHandshakeSession() != null) {
			throw new SSLException(
					"Handshake finished, but handshake session is not null");
		}
		SSLSession session = this.sslEngine.getSession();
		if (session == null) {
			throw new SSLException("Handshake finished, but session is null");
		}
		LOGGER.debug(ObjectLogMessageHelper.objectLogMessage(
				this, 
				"Negotiated protocol is %s", 
				session.getProtocol()));
		LOGGER.debug(ObjectLogMessageHelper.objectLogMessage(
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
		/*
		HandshakeStatusHelper.put(
				this.handshakeStatusUuid, HandshakeStatus.COMPLETED);
		*/
		this.handshakeCompleted = true;
	}
	
	private void handshakeIfNotCompleted() throws IOException {
		/*
		if (HandshakeStatusHelper.has(
				this.handshakeStatusUuid, HandshakeStatus.INITIATED)) {
			this.waitForCompletedHandshake();
		} else if (HandshakeStatusHelper.has(
				this.handshakeStatusUuid, HandshakeStatus.UNINITIATED)) {
			this.handshake();
		}
		*/
		if (!this.handshakeCompleted) {
			this.handshake();
		}
	}
	
	private List<DatagramPacket> produceApplicationPackets(
			final ByteBuffer outAppData, 
			final SocketAddress peerSocketAddr) throws IOException {
		List<DatagramPacket> packets = new ArrayList<DatagramPacket>();
		ByteBuffer outNetData = ByteBuffer.allocate(this.getMaximumPacketSize());
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
					this.getMaximumPacketSize());
			SSLEngineResult r = this.sslEngine.wrap(outAppData, outNetData);
			outNetData.flip();
			SSLEngineResult.Status rs = r.getStatus();
			SSLEngineResult.HandshakeStatus hs = r.getHandshakeStatus();
			LOGGER.debug(ObjectLogMessageHelper.objectLogMessage(
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
			} else if (rs.equals(SSLEngineResult.Status.BUFFER_UNDERFLOW)) {
				LOGGER.debug(ObjectLogMessageHelper.objectLogMessage(
						this, 
						"Produce handshake packets: BUFFER_UNDERFLOW occured"));
				LOGGER.debug(ObjectLogMessageHelper.objectLogMessage(
						this, 
						"Produce handshake packets: Handshake status: %s",
						hs));
				// bad packet, or the client maximum fragment size
				// config does not work?
				if (!hs.equals(SSLEngineResult.HandshakeStatus.NOT_HANDSHAKING)) {
					throw new SSLException("Buffer underflow: incorrect server "
							+ "maximum fragment size");
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
				LOGGER.debug(ObjectLogMessageHelper.objectLogMessage(
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
				} else if (nhs.equals(SSLEngineResult.HandshakeStatus.NEED_WRAP)) {
					endInnerLoop = true;
				} else if (nhs.equals(SSLEngineResult.HandshakeStatus.FINISHED)) {
					throw new SSLException("Unexpected status, "
							+ "SSLEngine.getHandshakeStatus() shouldn't return "
							+ "FINISHED");
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
	
	@Override
	public synchronized void receive(final DatagramPacket p) throws IOException {
		// this.handshakeIfNotCompleted();
		if (this.getUseClientMode()) {
			LOGGER.info("{}: Waiting for handshaked to be completed", this.getLocalSocketAddress());
			this.waitForCompletedHandshake();
			LOGGER.info("{}: Finished waiting for handshaked to be completed", this.getLocalSocketAddress());
		} else {
			/*
			if (HandshakeStatusHelper.has(
					this.handshakeStatusUuid, HandshakeStatus.INITIATED)) {
				LOGGER.info("{}: Waiting for handshaked to be completed", this.getLocalSocketAddress());
				this.waitForCompletedHandshake();
				LOGGER.info("{}: Finished waiting for handshaked to be completed", this.getLocalSocketAddress());
			} else if (HandshakeStatusHelper.has(
					this.handshakeStatusUuid, HandshakeStatus.UNINITIATED)) {
				LOGGER.info("{}: Initiating handshake", this.getLocalSocketAddress());
				this.handshake();
				LOGGER.info("{}: Handshake completed", this.getLocalSocketAddress());
			}
			*/
			/*
			if (!HandshakeStatusHelper.has(
					this.handshakeStatusUuid, HandshakeStatus.COMPLETED)) {
				LOGGER.info("{}: Initiating handshake", this.getLocalSocketAddress());
				this.handshake();
				LOGGER.info("{}: Handshake completed", this.getLocalSocketAddress());
			}
			*/
			if (!this.handshakeCompleted) {
				this.handshake();
			}
		}
		int loops = MAX_APP_READ_LOOPS;
		while (true) {
			if (--loops < 0) {
				throw new SSLException(
						"Too many loops to receive application data");
			}
			byte[] buffer = new byte[this.getMaximumPacketSize()];
			DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
			this.datagramSocket.receive(packet);
			ByteBuffer inNetData = ByteBuffer.wrap(buffer, 0, packet.getLength());
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
		SSLEngineResult.HandshakeStatus hs = this.sslEngine.getHandshakeStatus();
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
		SSLEngineResult.HandshakeStatus hs = this.sslEngine.getHandshakeStatus();
		if (hs.equals(SSLEngineResult.HandshakeStatus.NEED_TASK)) {
			throw new SSLException("Handshake shouldn't need additional tasks");
		}
	}

	@Override
	public void send(final DatagramPacket p) throws IOException {
		// this.handshakeIfNotCompleted();
		if (this.getUseClientMode()) {
			/*
			if (HandshakeStatusHelper.has(
					this.handshakeStatusUuid, HandshakeStatus.INITIATED)) {
				LOGGER.info("{}: Waiting for handshaked to be completed", this.getLocalSocketAddress());
				this.waitForCompletedHandshake();
				LOGGER.info("{}: Finished waiting for handshaked to be completed", this.getLocalSocketAddress());
			} else if (HandshakeStatusHelper.has(
					this.handshakeStatusUuid, HandshakeStatus.UNINITIATED)) {
				LOGGER.info("{}: Initiating handshake", this.getLocalSocketAddress());
				this.handshake();
				LOGGER.info("{}: Handshake completed", this.getLocalSocketAddress());
			}
			*/
			/*
			if (!HandshakeStatusHelper.has(
					this.handshakeStatusUuid, HandshakeStatus.COMPLETED)) {
				LOGGER.info("{}: Initiating handshake", this.getLocalSocketAddress());
				this.handshake();
				LOGGER.info("{}: Handshake completed", this.getLocalSocketAddress());
			}
			*/
			if (!this.handshakeCompleted) {
				this.handshake();
			}
		} else {
			/*
			LOGGER.info("{}: Waiting for handshaked to be completed", this.getLocalSocketAddress());
			this.waitForCompletedHandshake();
			LOGGER.info("{}: Finished waiting for handshaked to be completed", this.getLocalSocketAddress());
			*/
			if (!this.handshakeCompleted) {
				throw new IllegalStateException(
						"Handshake must be initiated and completed by receive() "
						+ "before invoking send()");
			}
		}
		ByteBuffer outAppData = ByteBuffer.wrap(p.getData());
		// Note: have not considered the packet losses
		List<DatagramPacket> packets = this.produceApplicationPackets(
				outAppData, p.getSocketAddress());
		outAppData.flip();
		for (DatagramPacket packet : packets) {
			this.datagramSocket.send(packet);
		}
	}
	
	public void setEnabledCipherSuites(final String[] suites) {
		this.sslEngine.setEnabledCipherSuites(suites);
	}

	public void setEnabledProtocols(final String[] protocols) {
		this.sslEngine.setEnabledProtocols(protocols);
	}

	public void setMaximumPacketSize(final int maximumPacketSize) {
		SSLParameters sslParameters = this.sslEngine.getSSLParameters();
		sslParameters.setMaximumPacketSize(maximumPacketSize);
		this.sslEngine.setSSLParameters(sslParameters);
	}
	
	public void setNeedClientAuth(final boolean need) {
		this.sslEngine.setNeedClientAuth(need);
	}

	public void setUseClientMode(final boolean mode) {
		this.sslEngine.setUseClientMode(mode);
	}

	public void setWantClientAuth(final boolean want) {
		this.sslEngine.setWantClientAuth(want);
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
	
	private void waitForCompletedHandshake() throws IOException {
		int soTimeout = super.getSoTimeout();
		long waitStartTime = System.currentTimeMillis();
		/*
		while (!HandshakeStatusHelper.has(
				this.handshakeStatusUuid, HandshakeStatus.COMPLETED)) {
			LOGGER.info("Waiting for completed handshake...");
			try {
				Thread.sleep(HALF_SECOND);
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
			}
			if (soTimeout == 0) { continue; }
			long timeSinceWaitStartTime = 
					System.currentTimeMillis() - waitStartTime;
			if (timeSinceWaitStartTime >= soTimeout) {
				throw new SSLException(
						"Timeout for waiting for completed handshake has been "
						+ "reached");
			}
		}
		*/
		while (!this.handshakeCompleted) {
			LOGGER.info("Waiting for completed handshake...");
			try {
				Thread.sleep(HALF_SECOND);
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
			}
			if (soTimeout == 0) { continue; }
			long timeSinceWaitStartTime = 
					System.currentTimeMillis() - waitStartTime;
			if (timeSinceWaitStartTime >= soTimeout) {
				throw new SSLException(
						"Timeout for waiting for completed handshake has been "
						+ "reached");
			}
		}		
	}

}
