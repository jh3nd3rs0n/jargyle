package jargyle.net.ssl;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.net.ssl.SSLEngine;
import javax.net.ssl.SSLEngineResult;
import javax.net.ssl.SSLParameters;
import javax.net.ssl.SSLSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jargyle.net.FilterDatagramSocket;

public final class DtlsDatagramSocket extends FilterDatagramSocket {

	private static final Logger LOGGER = LoggerFactory.getLogger(
			DtlsDatagramSocket.class);

	private static final int MAX_APP_READ_LOOPS = 60;
	private static final int MAX_HANDSHAKE_LOOPS = 200;
	
	private final int bufferSize;
	private final boolean clientMode;
	private boolean handshakeCompleted;
	private final InetAddress peerAddress;
	private final int peerPort;
	private final SocketAddress peerSocketAddress;
	private final SSLEngine sslEngine;

	public DtlsDatagramSocket(
			final DatagramSocket datagramSock, 
			final SSLEngine engine) throws SocketException {
		super(datagramSock);
		SSLParameters params = engine.getSSLParameters();
		int maxPacketSize = params.getMaximumPacketSize();
		SocketAddress peerSocketAddr = new InetSocketAddress(
				engine.getPeerHost(), engine.getPeerPort());
		InetAddress peerAddr = 
				((InetSocketAddress) peerSocketAddr).getAddress();
		int peerPrt = ((InetSocketAddress) peerSocketAddr).getPort();
		boolean useClientMode = engine.getUseClientMode();
		this.bufferSize = maxPacketSize;
		this.clientMode = useClientMode;
		this.handshakeCompleted = false;
		this.peerAddress = peerAddr;
		this.peerPort = peerPrt;
		this.peerSocketAddress = peerSocketAddr;
		this.sslEngine = engine;
	}

	private String format(final String message) {
		return String.format("%s: %s", this, message);
	}

	public InetAddress getPeerAddress() {
		return this.peerAddress;
	}
	
	public int getPeerPort() {
		return this.peerPort;
	}
	
	public SocketAddress getPeerSocketAddress() {
		return this.peerSocketAddress;
	}
	
	private void handshake() throws IOException {
		boolean endLoops = false;
		int loops = MAX_HANDSHAKE_LOOPS;
		this.sslEngine.beginHandshake();
		while (!endLoops) {
			if (--loops < 0) {
				throw new IOException(
						"Too many loops to produce handshake packets");
			}
			SSLEngineResult.HandshakeStatus hs = this.sslEngine.getHandshakeStatus();
			LOGGER.debug(this.format(String.format(
					"=======handshake(%s, %s)=======", 
					loops, 
					hs)));
			if (hs.equals(SSLEngineResult.HandshakeStatus.NEED_UNWRAP)
					|| hs.equals(SSLEngineResult.HandshakeStatus.NEED_UNWRAP_AGAIN)) {
				LOGGER.debug(this.format(String.format(
						"Receive DTLS records, handshake status is %s", 
						hs)));
				ByteBuffer inNetData;
				ByteBuffer inAppData;
				if (hs.equals(SSLEngineResult.HandshakeStatus.NEED_UNWRAP)) {
					byte[] buffer = new byte[this.bufferSize];
					DatagramPacket packet = new DatagramPacket(
							buffer, buffer.length);
					try {
						this.datagramSocket.receive(packet);
					} catch (SocketTimeoutException ste) {
						LOGGER.debug(this.format(String.format(
								"Warning: %s", 
								ste)));
						List<DatagramPacket> packets = 
								new ArrayList<DatagramPacket>();
						boolean finished = this.reproduceHandshakePackets(
								packets, this.peerSocketAddress);
						LOGGER.debug(this.format(String.format(
								"Reproduced %s packets", 
								packets.size())));
						for (DatagramPacket p : packets) {
							LOGGER.debug(this.format(String.format(
									"Reproduced packet %s",
									Arrays.toString(Arrays.copyOfRange(
											p.getData(), 
											p.getOffset(), 
											p.getLength())))));
							this.datagramSocket.send(p);
						}
						if (finished) {
							LOGGER.debug(this.format(
									"Handshake status is FINISHED after "
									+ "calling reproduceHandshakePackets(), "
									+ "finish the loop"));
							endLoops = true;
						}
						LOGGER.debug(this.format(String.format(
								"New handshake status is %s", 
								this.sslEngine.getHandshakeStatus())));
						continue;
					}
					inNetData = ByteBuffer.wrap(buffer, 0, packet.getLength());
					inAppData = ByteBuffer.allocate(this.bufferSize);
				} else {
					inNetData = ByteBuffer.allocate(0);
					inAppData = ByteBuffer.allocate(this.bufferSize);
				}
				SSLEngineResult r = this.sslEngine.unwrap(inNetData, inAppData);
				SSLEngineResult.Status rs = r.getStatus();
				hs = r.getHandshakeStatus();
				if (rs.equals(SSLEngineResult.Status.OK)) {
					// OK
				} else if (rs.equals(SSLEngineResult.Status.BUFFER_OVERFLOW)) {
					LOGGER.debug(this.format(String.format(
							"BUFFER_OVERFLOW, handshake status is %s", 
							hs)));
					// the client maximum fragment size config does not work?
					throw new IOException("Buffer overflow: incorrect client "
							+ "maximum fragment size");
				} else if (rs.equals(SSLEngineResult.Status.BUFFER_UNDERFLOW)) {
					LOGGER.debug(this.format(String.format(
							"BUFFER_UNDERFLOW, handshake status is %s", 
							hs)));
					// bad packet, or the client maximum fragment size
					// config does not work?
					if (!hs.equals(SSLEngineResult.HandshakeStatus.NOT_HANDSHAKING)) {
						throw new IOException("Buffer underflow: incorrect "
								+ "client maximum fragment size");
					} // otherwise, ignore this packet
				} else if (rs.equals(SSLEngineResult.Status.CLOSED)) {
					throw new IOException(String.format(
							"SSL engine closed, handshake status is %s", 
							hs));
				} else {
					throw new IOException(String.format(
							"Can't reach here, result is %s", 
							rs));
				}
				if (hs.equals(SSLEngineResult.HandshakeStatus.FINISHED)) {
					LOGGER.debug(this.format(
							"Handshake status is FINISHED, finish the loop"));
					endLoops = true;
				}
			} else if (hs.equals(SSLEngineResult.HandshakeStatus.NEED_WRAP)) {
				List<DatagramPacket> packets = new ArrayList<DatagramPacket>();
				boolean finished = this.produceHandshakePackets(
						packets, this.peerSocketAddress);
				LOGGER.debug(this.format(String.format(
						"Produced %s packets", 
						packets.size())));
				for (DatagramPacket p : packets) {
					this.datagramSocket.send(p);
				}
				if (finished) {
					LOGGER.debug(this.format("Handshake status is FINISHED "
							+ "after producing handshake packets, finish the "
							+ "loop"));
					endLoops = true;
				}
			} else if (hs.equals(SSLEngineResult.HandshakeStatus.NEED_TASK)) {
				this.runDelegatedTasks();
			} else if (hs.equals(SSLEngineResult.HandshakeStatus.NOT_HANDSHAKING)) {
				LOGGER.debug(this.format(
						"Handshake status is NOT_HANDSHAKING, finish the loop"));
				endLoops = true;
			} else if (hs.equals(SSLEngineResult.HandshakeStatus.FINISHED)) {
				throw new IOException(
						"Unexpected status, SSLEngine.getHandshakeStatus() "
						+ "shouldn't return FINISHED");
			} else {
				throw new IOException(String.format(
						"Can't reach here, handshake status is %s", 
						hs));
			}
		}
		SSLEngineResult.HandshakeStatus hs = this.sslEngine.getHandshakeStatus();
		LOGGER.debug(this.format(String.format(
				"Handshake finished, status is %s", 
				hs)));
		if (this.sslEngine.getHandshakeSession() != null) {
			throw new IOException(
					"Handshake finished, but handshake session is not null");
		}
		SSLSession session = this.sslEngine.getSession();
		if (session == null) {
			throw new IOException("Handshake finished, but session is null");
		}
		LOGGER.debug(this.format(String.format(
				"Negotiated protocol is %s", 
				session.getProtocol())));
		LOGGER.debug(this.format(String.format(
				"Negotiated cipher suite is %s", 
				session.getCipherSuite())));
		// handshake status should be NOT_HANDSHAKING
		//
		// According to the spec, SSLEngine.getHandshakeStatus() can't
		// return FINISHED.
		if (!hs.equals(SSLEngineResult.HandshakeStatus.NOT_HANDSHAKING)) {
			throw new IOException(String.format(
					"Unexpected handshake status %s", hs));
		}
		this.handshakeCompleted = true;
	}

	public boolean isClientMode() {
		return this.clientMode;
	}
	
	// produce application packets
	private List<DatagramPacket> produceApplicationPackets(
			final ByteBuffer outAppData, 
			final SocketAddress peerSocketAddr) throws IOException {
		List<DatagramPacket> packets = new ArrayList<DatagramPacket>();
		ByteBuffer outNetData = ByteBuffer.allocate(this.bufferSize);
		SSLEngineResult r = this.sslEngine.wrap(outAppData, outNetData);
		outNetData.flip();
		SSLEngineResult.Status rs = r.getStatus();
		if (rs.equals(SSLEngineResult.Status.BUFFER_OVERFLOW)) {
			throw new IOException("Buffer overflow during wrapping");
		} else if (rs.equals(SSLEngineResult.Status.BUFFER_UNDERFLOW)) {
			throw new IOException("Buffer underflow during wrapping");
		} else if (rs.equals(SSLEngineResult.Status.CLOSED)) {
			throw new IOException("SSLEngine has closed");
		} else if (rs.equals(SSLEngineResult.Status.OK)) {
			// OK
		} else {
			throw new IOException(String.format(
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

	// produce handshake packets
	private boolean produceHandshakePackets(
			final List<DatagramPacket> packets,
			final SocketAddress peerSocketAddr) throws IOException {
		boolean endLoops = false;
		int loops = MAX_HANDSHAKE_LOOPS / 2;
		while (!endLoops) {
			if (--loops < 0) {
				throw new IOException(
						"Too many loops to produce handshake packets");
			}
			ByteBuffer outAppData = ByteBuffer.allocate(0);
			ByteBuffer outNetData = ByteBuffer.allocate(this.bufferSize);
			SSLEngineResult r = this.sslEngine.wrap(outAppData, outNetData);
			outNetData.flip();
			SSLEngineResult.Status rs = r.getStatus();
			SSLEngineResult.HandshakeStatus hs = r.getHandshakeStatus();
			LOGGER.debug(this.format(String.format(
					"----produce handshake packet(%s, %s, %s)----", 
					loops, 
					rs, 
					hs)));
			if (rs.equals(SSLEngineResult.Status.BUFFER_OVERFLOW)) {
				// the client maximum fragment size config does not work?
				throw new IOException("Buffer overflow: incorrect server "
						+ "maximum fragment size");
			} else if (rs.equals(SSLEngineResult.Status.BUFFER_UNDERFLOW)) {
				LOGGER.debug(this.format(
						"Produce handshake packets: BUFFER_UNDERFLOW occured"));
				LOGGER.debug(this.format(String.format(
						"Produce handshake packets: Handshake status: %s", 
						hs)));
				// bad packet, or the client maximum fragment size
				// config does not work?
				if (!hs.equals(SSLEngineResult.HandshakeStatus.NOT_HANDSHAKING)) {
					throw new IOException("Buffer underflow: incorrect server "
							+ "maximum fragment size");
				} // otherwise, ignore this packet
			} else if (rs.equals(SSLEngineResult.Status.CLOSED)) {
				throw new IOException("SSLEngine has closed");
			} else if (rs.equals(SSLEngineResult.Status.OK)) {
				// OK
			} else {
				throw new IOException(String.format(
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
				LOGGER.debug(this.format(
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
					throw new IOException("Unexpected status, "
							+ "SSLEngine.getHandshakeStatus() shouldn't return "
							+ "FINISHED");
				} else {
					throw new IOException(String.format(
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
		if (!this.handshakeCompleted) { this.handshake(); }
		int loops = MAX_APP_READ_LOOPS;
		while (true) {
			if (--loops < 0) {
				throw new IOException(
						"Too many loops to receive application data");
			}
			byte[] buffer = new byte[this.bufferSize];
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

	// reproduce handshake packets if timeout
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

	// run delegated tasks
	private void runDelegatedTasks() throws IOException {
		Runnable runnable;
		while ((runnable = this.sslEngine.getDelegatedTask()) != null) {
			runnable.run();
		}
		SSLEngineResult.HandshakeStatus hs = this.sslEngine.getHandshakeStatus();
		if (hs.equals(SSLEngineResult.HandshakeStatus.NEED_TASK)) {
			throw new IOException("Handshake shouldn't need additional tasks");
		}
	}

	@Override
	public void send(final DatagramPacket p) throws IOException {
		SocketAddress socketAddress = p.getSocketAddress();
		if (!this.peerSocketAddress.equals(socketAddress)) {
			throw new IllegalArgumentException(
					"packet address and peer socket address must be the same");
		}
		if (!this.handshakeCompleted) { this.handshake(); }
		ByteBuffer outAppData = ByteBuffer.wrap(p.getData());
		// Note: have not considered the packet losses
		List<DatagramPacket> packets = this.produceApplicationPackets(
				outAppData, socketAddress);
		outAppData.flip();
		for (DatagramPacket packet : packets) {
			this.datagramSocket.send(packet);
		}
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append(this.getClass().getSimpleName())
			.append(" [getLocalSocketAddress()=")
			.append(this.getLocalSocketAddress())
			.append(", mode=")
			.append(this.clientMode ? "clientMode" : "serverMode")
			.append(", peerSocketAddress=")
			.append(this.peerSocketAddress)			
			.append("]");
		return builder.toString();
	}

}
