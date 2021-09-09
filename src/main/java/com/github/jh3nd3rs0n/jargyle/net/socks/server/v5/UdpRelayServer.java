package com.github.jh3nd3rs0n.jargyle.net.socks.server.v5;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.jh3nd3rs0n.jargyle.internal.logging.LoggerHelper;
import com.github.jh3nd3rs0n.jargyle.net.HostResolver;
import com.github.jh3nd3rs0n.jargyle.net.Port;
import com.github.jh3nd3rs0n.jargyle.net.socks.transport.v5.UdpRequestHeader;
import com.github.jh3nd3rs0n.jargyle.util.Criteria;
import com.github.jh3nd3rs0n.jargyle.util.Criterion;

public final class UdpRelayServer {
	
	public static final class ClientDatagramSocketAddress {

		private final String address;
		private final int port;

		public ClientDatagramSocketAddress(final String addr, final int prt) {
			Objects.requireNonNull(addr);
			if (prt < 0 || prt > Port.MAX_INT_VALUE) {
				throw new IllegalArgumentException("port is out of range");
			}
			this.address = addr;
			this.port = prt; 	
		}
		
		public String getAddress() {
			return this.address;
		}
		
		public int getPort() {
			return this.port;
		}
		
	}
	
	public static final class DatagramSockets {
		
		private final DatagramSocket clientFacingDatagramSocket;
		private final DatagramSocket serverFacingDatagramSocket;
		
		public DatagramSockets(
				final DatagramSocket clientFacingDatagramSock,
				final DatagramSocket serverFacingDatagramSock) {
			Objects.requireNonNull(clientFacingDatagramSock);
			Objects.requireNonNull(serverFacingDatagramSock);
			this.clientFacingDatagramSocket = clientFacingDatagramSock;
			this.serverFacingDatagramSocket = serverFacingDatagramSock;
		}
		
		public DatagramSocket getClientFacingDatagramSocket() {
			return this.clientFacingDatagramSocket;
		}
		
		public DatagramSocket getServerFacingDatagramSocket() {
			return this.serverFacingDatagramSocket;
		}
		
	}
	
	public static final class InboundAddressCriteria {
		
		private final Criteria allowedInboundAddressCriteria;
		private final Criteria blockedInboundAddressCriteria;
		
		public InboundAddressCriteria(
				final Criteria allowedInboundAddrCriteria, 
				final Criteria blockedInboundAddrCriteria) {
			Objects.requireNonNull(allowedInboundAddrCriteria);
			Objects.requireNonNull(blockedInboundAddrCriteria);
			this.allowedInboundAddressCriteria = allowedInboundAddrCriteria;
			this.blockedInboundAddressCriteria = blockedInboundAddrCriteria;
		}
		
		public Criteria getAllowedInboundAddressCriteria() {
			return this.allowedInboundAddressCriteria;
		}
		
		public Criteria getBlockedInboundAddressCriteria() {
			return this.blockedInboundAddressCriteria;
		}
		
	}
	
	private static final class InboundPacketsWorker	extends PacketsWorker {
		
		private static final Logger LOGGER = LoggerFactory.getLogger(
				InboundPacketsWorker.class);
		
		public InboundPacketsWorker(final PacketsWorkerContext context) {
			super(context);
		}
		
		private boolean canAllowInboundAddress(final String inboundAddress) {
			Criterion criterion = 
					this.allowedInboundAddressCriteria.anyEvaluatesTrue(
							inboundAddress);
			if (criterion == null) {
				LOGGER.debug(LoggerHelper.objectMessage(this, String.format(
						"Inbound address %s not allowed",
						inboundAddress)));
				return false;
			}
			criterion =	this.blockedInboundAddressCriteria.anyEvaluatesTrue(
					inboundAddress);
			if (criterion != null) {
				LOGGER.debug(LoggerHelper.objectMessage(this, String.format(
						"Inbound address %s blocked based on the "
						+ "following criterion: %s",
						inboundAddress,
						criterion)));
				return false;
			}
			return true;
		}

		private DatagramPacket newDatagramPacket(
				final UdpRequestHeader header) {
			byte[] headerBytes = header.toByteArray();
			InetAddress inetAddress = null;
			try {
				inetAddress = InetAddress.getByName(this.clientAddress);
			} catch (IOException e) {
				LOGGER.warn( 
						LoggerHelper.objectMessage(
								this, 
								"Error in determining the IP address from the "
								+ "client"), 
						e);
				return null;
			}
			int inetPort = this.packetsWorkerContext.getClientPort();
			return new DatagramPacket(
					headerBytes, headerBytes.length, inetAddress, inetPort);
		}
		
		private UdpRequestHeader newUdpRequestHeader(
				final DatagramPacket packet) {
			String address = packet.getAddress().getHostAddress();
			int port = packet.getPort();
			UdpRequestHeader header = UdpRequestHeader.newInstance(
					0,
					address,
					port,
					packet.getData());
			return header;
		}
		
		@Override
		public void run() {
			this.packetsWorkerContext.setLastReceiveTime(
					System.currentTimeMillis());
			while (true) {
				try {
					byte[] buffer = new byte[this.bufferSize];
					DatagramPacket packet = new DatagramPacket(
							buffer, buffer.length);
					try {
						this.serverFacingDatagramSocket.receive(packet);
						this.packetsWorkerContext.setLastReceiveTime(
								System.currentTimeMillis());
					} catch (SocketException e) {
						// socket closed
						break;
					} catch (SocketTimeoutException e) {
						long lastReceiveTime = 
								this.packetsWorkerContext.getLastReceiveTime();
						long timeSinceReceive = 
								System.currentTimeMillis() - lastReceiveTime;
						if (timeSinceReceive >= this.timeout) {
							LOGGER.trace(
									LoggerHelper.objectMessage(
											this, 
											"Timeout reached!"));							
							break;
						}
						continue;
					} catch (IOException e) {
						LOGGER.warn( 
								LoggerHelper.objectMessage(
										this, 
										"Error in receiving the packet from "
										+ "the server"), 
								e);
						continue;
					}
					LOGGER.trace(LoggerHelper.objectMessage(this, String.format(
							"Packet data received: %s byte(s)",
							packet.getLength())));
					if (!this.canAllowInboundAddress(
							packet.getAddress().getHostAddress())) {
						continue;
					}
					UdpRequestHeader header = this.newUdpRequestHeader(packet);
					LOGGER.trace(LoggerHelper.objectMessage(
							this, header.toString()));
					packet = this.newDatagramPacket(header);
					if (packet == null) {
						continue;
					}
					try {
						this.clientFacingDatagramSocket.send(packet);
					} catch (SocketException e) {
						// socket closed
						break;
					} catch (IOException e) {
						LOGGER.warn( 
								LoggerHelper.objectMessage(
										this, 
										"Error in sending the packet to the "
										+ "client"), 
								e);
					}
				} catch (Throwable t) {
					LOGGER.warn( 
							LoggerHelper.objectMessage(
									this, 
									"Error occurred in the process of "
									+ "relaying of a packet from the server to "
									+ "the client"), 
							t);
				}
			}
			this.packetsWorkerContext.stopUdpRelayServerIfNotStopped();				
		}
		
	}
	
	public static final class OutboundAddressCriteria {
		
		private final Criteria allowedOutboundAddressCriteria;
		private final Criteria blockedOutboundAddressCriteria;
		
		public OutboundAddressCriteria(
				final Criteria allowedOutboundAddrCriteria, 
				final Criteria blockedOutboundAddrCriteria) {
			Objects.requireNonNull(allowedOutboundAddrCriteria);
			Objects.requireNonNull(blockedOutboundAddrCriteria);
			this.allowedOutboundAddressCriteria = allowedOutboundAddrCriteria;
			this.blockedOutboundAddressCriteria = blockedOutboundAddrCriteria;
		}
		
		public Criteria getAllowedOutboundAddressCriteria() {
			return this.allowedOutboundAddressCriteria;
		}
		
		public Criteria getBlockedOutboundAddressCriteria() {
			return this.blockedOutboundAddressCriteria;
		}
		
	}
	
	private static final class OutboundPacketsWorker extends PacketsWorker {
		
		private static final Logger LOGGER = LoggerFactory.getLogger(
				OutboundPacketsWorker.class);
		
		public OutboundPacketsWorker(final PacketsWorkerContext context) {
			super(context);
		}
		
		private boolean canAllowOutboundAddress(final String outboundAddress) {
			Criterion criterion = 
					this.allowedOutboundAddressCriteria.anyEvaluatesTrue(
							outboundAddress);
			if (criterion == null) {
				LOGGER.debug(LoggerHelper.objectMessage(this, String.format(
						"Outbound address %s not allowed",
						outboundAddress)));
				return false;
			}
			criterion = this.blockedOutboundAddressCriteria.anyEvaluatesTrue(
					outboundAddress);
			if (criterion != null) {
				LOGGER.debug(LoggerHelper.objectMessage(this, String.format(
						"Outbound address %s blocked based on the "
						+ "following criterion: %s",
						outboundAddress,
						criterion)));
				return false;
			}
			return true;
		}
		
		private boolean canForwardDatagramPacket(final DatagramPacket packet) {
			String address = packet.getAddress().getHostAddress();
			int port = packet.getPort();
			InetAddress clientInetAddr = null;
			try {
				clientInetAddr = InetAddress.getByName(this.clientAddress);
			} catch (IOException e) {
				LOGGER.warn( 
						LoggerHelper.objectMessage(
								this, 
								"Error in determining the IP address from the "
								+ "client"), 
						e);
				return false;
			}
			InetAddress inetAddr = null;
			try {
				inetAddr = InetAddress.getByName(address);
			} catch (IOException e) {
				LOGGER.warn( 
						LoggerHelper.objectMessage(
								this, 
								"Error in determining the IP address from the "
								+ "client"), 
						e);
				return false;
			}
			if ((!clientInetAddr.isLoopbackAddress() 
					|| !inetAddr.isLoopbackAddress())
					&& !clientInetAddr.equals(inetAddr)) {
				return false;
			}
			int clientPrt = this.packetsWorkerContext.getClientPort();
			if (clientPrt == 0) {
				this.packetsWorkerContext.setClientPort(port);
			} else {
				if (clientPrt != port) {
					return false;
				}
			}
			return true;
		}
		
		private DatagramPacket newDatagramPacket(
				final UdpRequestHeader header) {
			byte[] userData = header.getUserData();
			InetAddress inetAddress = null;
			try {
				inetAddress = this.hostResolver.resolve(
						header.getDesiredDestinationAddress());
			} catch (IOException e) {
				LOGGER.warn( 
						LoggerHelper.objectMessage(
								this, 
								"Error in determining the IP address from the "
								+ "server"), 
						e);
				return null;
			}
			int inetPort = header.getDesiredDestinationPort();
			return new DatagramPacket(
					userData, userData.length, inetAddress, inetPort);
		}
		
		private UdpRequestHeader newUdpRequestHeader(
				final DatagramPacket packet) {
			UdpRequestHeader header = null; 
			try {
				header = UdpRequestHeader.newInstance(packet.getData());
			} catch (IllegalArgumentException e) {
				LOGGER.warn( 
						LoggerHelper.objectMessage(
								this, 
								"Error in parsing the UDP header request from "
								+ "the client"), 
						e);
				return null;
			}
			return header;
		}
		
		@Override
		public void run() {
			this.packetsWorkerContext.setLastReceiveTime(
					System.currentTimeMillis());
			while (true) {
				try {
					byte[] buffer = new byte[this.bufferSize];
					DatagramPacket packet = new DatagramPacket(
							buffer, buffer.length);
					try {
						this.clientFacingDatagramSocket.receive(packet);
						this.packetsWorkerContext.setLastReceiveTime(
								System.currentTimeMillis());
					} catch (SocketException e) {
						// socket closed
						break;
					} catch (SocketTimeoutException e) {
						long lastReceiveTime = 
								this.packetsWorkerContext.getLastReceiveTime();
						long timeSinceReceive = 
								System.currentTimeMillis() - lastReceiveTime;
						if (timeSinceReceive >= this.timeout) {
							LOGGER.trace(
									LoggerHelper.objectMessage(
											this, 
											"Timeout reached!"));
							break;
						}
						continue;
					} catch (IOException e) {
						LOGGER.warn( 
								LoggerHelper.objectMessage(
										this, 
										"Error in receiving packet from the "
										+ "client"), 
								e);
						continue;
					}
					LOGGER.trace(LoggerHelper.objectMessage(this, String.format(
							"Packet data received: %s byte(s)",
							packet.getLength())));					
					if (!this.canForwardDatagramPacket(packet)) {
						continue;
					}
					UdpRequestHeader header = this.newUdpRequestHeader(packet);
					if (header == null) {
						continue;
					}
					LOGGER.trace(LoggerHelper.objectMessage(
							this, header.toString()));
					if (header.getCurrentFragmentNumber() != 0) {
						continue;
					}
					if (!this.canAllowOutboundAddress(
							header.getDesiredDestinationAddress())) {
						continue;
					}
					packet = this.newDatagramPacket(header);
					if (packet == null) {
						continue;
					}
					try {
						this.serverFacingDatagramSocket.send(packet);
					} catch (SocketException e) {
						// socket closed
						break;
					} catch (IOException e) {
						LOGGER.warn( 
								LoggerHelper.objectMessage(
										this, 
										"Error in sending the packet to the "
										+ "server"), 
								e);
					}
				} catch (Throwable t) {
					LOGGER.warn( 
							LoggerHelper.objectMessage(
									this, 
									"Error occurred in the process of "
									+ "relaying of a packet from the client to "
									+ "the server"), 
							t);
				}
			}
			this.packetsWorkerContext.stopUdpRelayServerIfNotStopped();				
		}

	}
	
	private static abstract class PacketsWorker implements Runnable {
		
		protected final Criteria allowedInboundAddressCriteria;
		protected final Criteria allowedOutboundAddressCriteria;
		protected final Criteria blockedInboundAddressCriteria;
		protected final Criteria blockedOutboundAddressCriteria;
		protected final int bufferSize;
		protected final String clientAddress;
		protected final DatagramSocket clientFacingDatagramSocket;
		protected final HostResolver hostResolver;
		protected final PacketsWorkerContext packetsWorkerContext;
		protected final DatagramSocket serverFacingDatagramSocket;
		protected final int timeout;

		public PacketsWorker(final PacketsWorkerContext context) {
			this.allowedInboundAddressCriteria =
					context.getAllowedInboundAddressCriteria();
			this.allowedOutboundAddressCriteria =
					context.getAllowedOutboundAddressCriteria();
			this.blockedInboundAddressCriteria =
					context.getBlockedInboundAddressCriteria();
			this.blockedOutboundAddressCriteria =
					context.getBlockedOutboundAddressCriteria();
			this.bufferSize = context.getBufferSize();
			this.clientAddress = context.getClientAddress();
			this.clientFacingDatagramSocket = 
					context.getClientFacingDatagramSocket();
			this.hostResolver = context.getHostResolver();
			this.packetsWorkerContext = context;
			this.serverFacingDatagramSocket = 
					context.getServerFacingDatagramSocket();
			this.timeout = context.getTimeout();
		}

		@Override
		public final String toString() {
			StringBuilder builder = new StringBuilder();
			builder.append(this.getClass().getSimpleName())
				.append(" [packetsWorkerContext=")
				.append(this.packetsWorkerContext)
				.append("]");
			return builder.toString();
		}
		
	}
	
	private static final class PacketsWorkerContext {
		
		private final DatagramSocket clientFacingDatagramSocket;
		private final DatagramSocket serverFacingDatagramSocket;
		private final UdpRelayServer udpRelayServer;
		
		public PacketsWorkerContext(final UdpRelayServer server) {
			this.clientFacingDatagramSocket = server.clientFacingDatagramSocket;
			this.serverFacingDatagramSocket = server.serverFacingDatagramSocket;
			this.udpRelayServer = server;
		}
		
		public final Criteria getAllowedInboundAddressCriteria() {
			return this.udpRelayServer.allowedInboundAddressCriteria;
		}
		
		public final Criteria getAllowedOutboundAddressCriteria() {
			return this.udpRelayServer.allowedOutboundAddressCriteria;
		}
		
		public final Criteria getBlockedInboundAddressCriteria() {
			return this.udpRelayServer.blockedInboundAddressCriteria;
		}
		
		public final Criteria getBlockedOutboundAddressCriteria() {
			return this.udpRelayServer.blockedOutboundAddressCriteria;
		}
		
		public final int getBufferSize() {
			return this.udpRelayServer.bufferSize;
		}
		
		public final String getClientAddress() {
			return this.udpRelayServer.clientAddress;
		}
		
		public final DatagramSocket getClientFacingDatagramSocket() {
			return this.clientFacingDatagramSocket;
		}
		
		public final int getClientPort() {
			return this.udpRelayServer.getClientPort();
		}
		
		public final HostResolver getHostResolver() {
			return this.udpRelayServer.hostResolver;
		}
		
		public final long getLastReceiveTime() {
			return this.udpRelayServer.getLastReceiveTime();
		}
		
		public final DatagramSocket getServerFacingDatagramSocket() {
			return this.serverFacingDatagramSocket;
		}
		
		public final int getTimeout() {
			return this.udpRelayServer.timeout;
		}
		
		public final void setClientPort(final int port) {
			this.udpRelayServer.setClientPort(port);
		}
		
		public final void setLastReceiveTime(final long time) {
			this.udpRelayServer.setLastReceiveTime(time);
		}
		
		public final void stopUdpRelayServerIfNotStopped() {
			this.udpRelayServer.stopIfNotStopped();
		}

		@Override
		public final String toString() {
			StringBuilder builder = new StringBuilder();
			builder.append(this.getClass().getSimpleName())
				.append(" [clientFacingDatagramSocket=")
				.append(this.clientFacingDatagramSocket)
				.append(", serverFacingDatagramSocket=")
				.append(this.serverFacingDatagramSocket)
				.append("]");
			return builder.toString();
		}
		
	}
	
	public static final class RelaySettings {
		
		private final int bufferSize;
		private final int timeout;
		
		public RelaySettings(final int bffrSize, final int tmt) {
			if (bffrSize < 1) {
				throw new IllegalArgumentException(
						"buffer size must not be less than 1");
			}
			if (tmt < 1) {
				throw new IllegalArgumentException(
						"timeout must not be less than 1");
			}
			this.bufferSize = bffrSize;
			this.timeout = tmt;
		}
		
		public int getBufferSize() {
			return this.bufferSize;
		}
		
		public int getTimeout() {
			return this.timeout;
		}
		
	}
	
	private final Criteria allowedInboundAddressCriteria;
	private final Criteria allowedOutboundAddressCriteria;
	private final Criteria blockedInboundAddressCriteria;
	private final Criteria blockedOutboundAddressCriteria;
	private final int bufferSize;	
	private final String clientAddress;
	private final DatagramSocket clientFacingDatagramSocket;	
	private int clientPort;
	private ExecutorService executor;
	private HostResolver hostResolver;
	private long lastReceiveTime;
	private final DatagramSocket serverFacingDatagramSocket;
	private boolean started;
	private boolean stopped;
	private final int timeout;
	
	public UdpRelayServer(		
			final ClientDatagramSocketAddress clientDatagramSockAddr,
			final DatagramSockets datagramSocks,
			final HostResolver resolver, 
			final InboundAddressCriteria inboundAddrCriteria, 
			final OutboundAddressCriteria outboundAddrCriteria, 
			final RelaySettings settings) {
		Objects.requireNonNull(clientDatagramSockAddr);
		Objects.requireNonNull(datagramSocks);
		Objects.requireNonNull(resolver);		
		Objects.requireNonNull(inboundAddrCriteria);
		Objects.requireNonNull(outboundAddrCriteria);
		Objects.requireNonNull(settings);
		this.allowedInboundAddressCriteria = 
				inboundAddrCriteria.getAllowedInboundAddressCriteria();
		this.allowedOutboundAddressCriteria =
				outboundAddrCriteria.getAllowedOutboundAddressCriteria();
		this.blockedInboundAddressCriteria = 
				inboundAddrCriteria.getBlockedInboundAddressCriteria();
		this.blockedOutboundAddressCriteria =
				outboundAddrCriteria.getBlockedOutboundAddressCriteria();
		this.bufferSize = settings.getBufferSize();
		this.clientAddress = clientDatagramSockAddr.getAddress();
		this.clientFacingDatagramSocket = 
				datagramSocks.getClientFacingDatagramSocket();
		this.clientPort = clientDatagramSockAddr.getPort();
		this.executor = null;
		this.hostResolver = resolver;
		this.lastReceiveTime = 0L;
		this.serverFacingDatagramSocket = 
				datagramSocks.getServerFacingDatagramSocket();
		this.started = false;
		this.stopped = true;
		this.timeout = settings.getTimeout();
	}

	private synchronized int getClientPort() {
		return this.clientPort;
	}
	
	private synchronized long getLastReceiveTime() {
		return this.lastReceiveTime;
	}
	
	public boolean isStarted() {
		return this.started;
	}
	
	public boolean isStopped() {
		return this.stopped;
	}
	
	private synchronized void setClientPort(final int port) {
		this.clientPort = port;
	}
	
	private synchronized void setLastReceiveTime(final long time) {
		this.lastReceiveTime = time;
	}
	
	public void start() throws IOException {
		if (this.started) {
			throw new IllegalStateException("UdpRelayServer already started");
		}
		this.lastReceiveTime = 0L;
		this.executor = Executors.newFixedThreadPool(2);
		this.executor.execute(new InboundPacketsWorker(
				new PacketsWorkerContext(this)));
		this.executor.execute(new OutboundPacketsWorker(
				new PacketsWorkerContext(this)));
		this.started = true;
		this.stopped = false;
	}
	
	public void stop() {
		if (this.stopped) {
			throw new IllegalStateException("UdpRelayServer already stopped");
		}
		this.lastReceiveTime = 0L;
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
