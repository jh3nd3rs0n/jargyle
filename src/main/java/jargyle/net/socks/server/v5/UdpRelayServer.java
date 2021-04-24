package jargyle.net.socks.server.v5;

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

import jargyle.logging.LoggerHelper;
import jargyle.net.HostResolver;
import jargyle.net.Port;
import jargyle.net.socks.transport.v5.UdpRequestHeader;
import jargyle.util.Criteria;
import jargyle.util.Criterion;

final class UdpRelayServer {
	
	public static final class ClientSocketAddress {

		private final String address;
		private final int port;

		public ClientSocketAddress(final String addr, final int prt) {
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
	
	public static final class ExternalInboundAddressCriteria {
		
		private final Criteria allowedCriteria;
		private final Criteria blockedCriteria;
		
		public ExternalInboundAddressCriteria(
				final Criteria allowCriteria,
				final Criteria blockCriteria) {
			Objects.requireNonNull(allowCriteria);
			Objects.requireNonNull(blockCriteria);
			this.allowedCriteria = allowCriteria;
			this.blockedCriteria = blockCriteria;
		}
		
		public Criteria getAllowedCriteria() {
			return this.allowedCriteria;
		}
		
		public Criteria getBlockedCriteria() {
			return this.blockedCriteria;
		}
		
	}
	
	private static final class ExternalInboundPacketsWorker 
		extends PacketsWorker {
		
		private static final Logger LOGGER = LoggerFactory.getLogger(
				ExternalInboundPacketsWorker.class);
		
		public ExternalInboundPacketsWorker(
				final PacketsWorkerContext context) {
			super(context);
		}
		
		private boolean canAllowExternalInboundAddress(
				final String externalInboundAddress) {
			Criterion criterion = 
					this.allowedExternalInboundAddressCriteria.anyEvaluatesTrue(
							externalInboundAddress);
			if (criterion == null) {
				LOGGER.debug(LoggerHelper.objectMessage(this, String.format(
						"External inbound address %s not allowed",
						externalInboundAddress)));
				return false;
			}
			criterion = 
					this.blockedExternalInboundAddressCriteria.anyEvaluatesTrue(
							externalInboundAddress);
			if (criterion != null) {
				LOGGER.debug(LoggerHelper.objectMessage(this, String.format(
						"External inbound address %s blocked based on the "
						+ "following criterion: %s",
						externalInboundAddress,
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
					if (!this.canAllowExternalInboundAddress(
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
			if (!this.packetsWorkerContext.isFirstPacketsWorkerFinished()) {
				this.packetsWorkerContext.setFirstPacketsWorkerFinished(true);
			} else {
				if (!this.packetsWorkerContext.isUdpRelayServerStopped()) {
					this.packetsWorkerContext.stopUdpRelayServer();
				}				
			}
		}
		
	}
	
	public static final class InternalOutboundAddressCriteria {
		
		private final Criteria allowedCriteria;
		private final Criteria blockedCriteria;
		
		public InternalOutboundAddressCriteria(
				final Criteria allowCriteria,
				final Criteria blockCriteria) {
			Objects.requireNonNull(allowCriteria);
			Objects.requireNonNull(blockCriteria);
			this.allowedCriteria = allowCriteria;
			this.blockedCriteria = blockCriteria;
		}
		
		public Criteria getAllowedCriteria() {
			return this.allowedCriteria;
		}
		
		public Criteria getBlockedCriteria() {
			return this.blockedCriteria;
		}
		
	}
	
	private static final class InternalOutboundPacketsWorker 
		extends PacketsWorker {
		
		private static final Logger LOGGER = LoggerFactory.getLogger(
				InternalOutboundPacketsWorker.class);
		
		public InternalOutboundPacketsWorker(
				final PacketsWorkerContext context) {
			super(context);
		}
		
		private boolean canAllowInternalOutboundAddress(
				final String externalOutboundAddress) {
			Criterion criterion = 
					this.allowedInternalOutboundAddressCriteria.anyEvaluatesTrue(
							externalOutboundAddress);
			if (criterion == null) {
				LOGGER.debug(LoggerHelper.objectMessage(this, String.format(
						"Internal outbound address %s not allowed",
						externalOutboundAddress)));
				return false;
			}
			criterion = 
					this.blockedInternalOutboundAddressCriteria.anyEvaluatesTrue(
							externalOutboundAddress);
			if (criterion != null) {
				LOGGER.debug(LoggerHelper.objectMessage(this, String.format(
						"Internal outbound address %s blocked based on the "
						+ "following criterion: %s",
						externalOutboundAddress,
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
					if (!this.canAllowInternalOutboundAddress(
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
			if (!this.packetsWorkerContext.isFirstPacketsWorkerFinished()) {
				this.packetsWorkerContext.setFirstPacketsWorkerFinished(true);
			} else {
				if (!this.packetsWorkerContext.isUdpRelayServerStopped()) {
					this.packetsWorkerContext.stopUdpRelayServer();
				}				
			}
		}

	}
	
	private static abstract class PacketsWorker implements Runnable {
		
		protected final Criteria allowedExternalInboundAddressCriteria;
		protected final Criteria allowedInternalOutboundAddressCriteria;
		protected final Criteria blockedExternalInboundAddressCriteria;
		protected final Criteria blockedInternalOutboundAddressCriteria;
		protected final int bufferSize;
		protected final String clientAddress;
		protected final DatagramSocket clientFacingDatagramSocket;
		protected final HostResolver hostResolver;
		protected final PacketsWorkerContext packetsWorkerContext;
		protected final DatagramSocket serverFacingDatagramSocket;
		protected final int timeout;

		public PacketsWorker(final PacketsWorkerContext context) {
			this.allowedExternalInboundAddressCriteria =
					context.getAllowedExternalInboundAddressCriteria();
			this.allowedInternalOutboundAddressCriteria =
					context.getAllowedInternalOutboundAddressCriteria();
			this.blockedExternalInboundAddressCriteria =
					context.getBlockedExternalInboundAddressCriteria();
			this.blockedInternalOutboundAddressCriteria =
					context.getBlockedInternalOutboundAddressCriteria();
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
		
		public final Criteria getAllowedExternalInboundAddressCriteria() {
			return this.udpRelayServer.allowedExternalInboundAddressCriteria;
		}
		
		public final Criteria getAllowedInternalOutboundAddressCriteria() {
			return this.udpRelayServer.allowedInternalOutboundAddressCriteria;
		}
		
		public final Criteria getBlockedExternalInboundAddressCriteria() {
			return this.udpRelayServer.blockedExternalInboundAddressCriteria;
		}
		
		public final Criteria getBlockedInternalOutboundAddressCriteria() {
			return this.udpRelayServer.blockedInternalOutboundAddressCriteria;
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
			return this.udpRelayServer.clientPort;
		}
		
		public final HostResolver getHostResolver() {
			return this.udpRelayServer.hostResolver;
		}
		
		public final long getLastReceiveTime() {
			return this.udpRelayServer.lastReceiveTime;
		}
		
		public final DatagramSocket getServerFacingDatagramSocket() {
			return this.serverFacingDatagramSocket;
		}
		
		public final int getTimeout() {
			return this.udpRelayServer.timeout;
		}
		
		public final boolean isFirstPacketsWorkerFinished() {
			return this.udpRelayServer.firstPacketsWorkerFinished;
		}
		
		public final boolean isUdpRelayServerStopped() {
			return this.udpRelayServer.stopped;
		}
		
		public final void setClientPort(final int port) {
			this.udpRelayServer.clientPort = port;
		}
		
		public final void setFirstPacketsWorkerFinished(final boolean b) {
			this.udpRelayServer.firstPacketsWorkerFinished = b;
		}
		
		public final void setLastReceiveTime(final long time) {
			this.udpRelayServer.lastReceiveTime = time;
		}
		
		public final void stopUdpRelayServer() {
			this.udpRelayServer.stop();
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
	
	private final Criteria allowedExternalInboundAddressCriteria;
	private final Criteria allowedInternalOutboundAddressCriteria;
	private final Criteria blockedExternalInboundAddressCriteria;
	private final Criteria blockedInternalOutboundAddressCriteria;
	private final int bufferSize;	
	private String clientAddress;
	private final DatagramSocket clientFacingDatagramSocket;	
	private int clientPort;
	private ExecutorService executor;
	private boolean firstPacketsWorkerFinished;
	private HostResolver hostResolver;
	private long lastReceiveTime;
	private final DatagramSocket serverFacingDatagramSocket;
	private boolean started;
	private boolean stopped;
	private final int timeout;
	
	public UdpRelayServer(		
			final ClientSocketAddress clientSockAddr,
			final DatagramSockets datagramSocks,
			final HostResolver resolver, 
			final ExternalInboundAddressCriteria externalInboundAddrCriteria, 
			final InternalOutboundAddressCriteria externalOutboundAddrCriteria, 
			final RelaySettings settings) {
		Objects.requireNonNull(clientSockAddr);
		Objects.requireNonNull(datagramSocks);
		Objects.requireNonNull(resolver);		
		Objects.requireNonNull(externalInboundAddrCriteria);
		Objects.requireNonNull(externalOutboundAddrCriteria);
		Objects.requireNonNull(settings);
		this.allowedExternalInboundAddressCriteria = 
				externalInboundAddrCriteria.getAllowedCriteria();
		this.allowedInternalOutboundAddressCriteria =
				externalOutboundAddrCriteria.getAllowedCriteria();
		this.blockedExternalInboundAddressCriteria = 
				externalInboundAddrCriteria.getBlockedCriteria();
		this.blockedInternalOutboundAddressCriteria =
				externalOutboundAddrCriteria.getBlockedCriteria();
		this.bufferSize = settings.getBufferSize();
		this.clientAddress = clientSockAddr.getAddress();
		this.clientFacingDatagramSocket = 
				datagramSocks.getClientFacingDatagramSocket();
		this.clientPort = clientSockAddr.getPort();
		this.executor = null;
		this.firstPacketsWorkerFinished = false;
		this.hostResolver = resolver;
		this.lastReceiveTime = 0L;
		this.serverFacingDatagramSocket = 
				datagramSocks.getServerFacingDatagramSocket();
		this.started = false;
		this.stopped = true;
		this.timeout = settings.getTimeout();
	}
	
	public boolean isStarted() {
		return this.started;
	}
	
	public boolean isStopped() {
		return this.stopped;
	}
	
	public void start() throws IOException {
		if (this.started) {
			throw new IllegalStateException("UdpRelayServer already started");
		}
		this.lastReceiveTime = 0L;
		this.firstPacketsWorkerFinished = false;
		this.executor = Executors.newFixedThreadPool(2);
		this.executor.execute(new ExternalInboundPacketsWorker(
				new PacketsWorkerContext(this)));
		this.executor.execute(new InternalOutboundPacketsWorker(
				new PacketsWorkerContext(this)));
		this.started = true;
		this.stopped = false;
	}
	
	public void stop() {
		if (this.stopped) {
			throw new IllegalStateException("UdpRelayServer already stopped");
		}
		this.lastReceiveTime = 0L;
		this.firstPacketsWorkerFinished = true;
		this.executor.shutdownNow();
		this.executor = null;
		this.started = false;
		this.stopped = true;
	}
	
}
