package jargyle.net.socks.socks5.server;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;

import jargyle.net.DatagramSocketInterface;
import jargyle.net.HostnameResolver;
import jargyle.net.socks.socks5.common.AddressType;
import jargyle.net.socks.socks5.common.UdpRequestHeader;
import jargyle.util.Criteria;
import jargyle.util.Criterion;

final class UdpRelayServer {
	
	public static final class ClientSocketAddress {

		private final String address;
		private final int port;

		public ClientSocketAddress(final String addr, final int prt) {
			Objects.requireNonNull(addr);
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
	
	public static final class DatagramSocketInterfaces {
		
		private final DatagramSocketInterface clientDatagramSocketInterface;
		private final DatagramSocketInterface serverDatagramSocketInterface;
		
		public DatagramSocketInterfaces(
				final DatagramSocketInterface clientDatagramSockInterface,
				final DatagramSocketInterface serverDatagramSockInterface) {
			Objects.requireNonNull(clientDatagramSockInterface);
			Objects.requireNonNull(serverDatagramSockInterface);
			this.clientDatagramSocketInterface = clientDatagramSockInterface;
			this.serverDatagramSocketInterface = serverDatagramSockInterface;
		}
		
		public DatagramSocketInterface getClientDatagramSocketInterface() {
			return this.clientDatagramSocketInterface;
		}
		
		public DatagramSocketInterface getServerDatagramSocketInterface() {
			return this.serverDatagramSocketInterface;
		}
		
	}
	
	public static final class ExternalIncomingAddressCriteria {
		
		private final Criteria allowedCriteria;
		private final Criteria blockedCriteria;
		
		public ExternalIncomingAddressCriteria(
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
	
	public static final class ExternalOutgoingAddressCriteria {
		
		private final Criteria allowedCriteria;
		private final Criteria blockedCriteria;
		
		public ExternalOutgoingAddressCriteria(
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
	
	private static final class IncomingPacketsWorker extends PacketsWorker {
		
		private static final Logger LOGGER = Logger.getLogger(
				IncomingPacketsWorker.class.getName());
		
		public IncomingPacketsWorker(final UdpRelayServer server) {
			super(server);
		}
		
		private boolean canAcceptExternalIncomingAddress(
				final String externalIncomingAddress) {
			Criteria allowedExternalIncomingAddrCriteria =
					this.getAllowedExternalIncomingAddressCriteria();
			Criterion criterion = 
					allowedExternalIncomingAddrCriteria.anyEvaluatesTrue(
							externalIncomingAddress);
			if (criterion == null) {
				LOGGER.log(
						Level.FINE, 
						this.format(String.format(
								"External incoming address %s not allowed", 
								externalIncomingAddress)));
				return false;
			}
			Criteria blockedExternalIncomingAddrCriteria =
					this.getBlockedExternalIncomingAddressCriteria();
			criterion = blockedExternalIncomingAddrCriteria.anyEvaluatesTrue(
					externalIncomingAddress);
			if (criterion != null) {
				LOGGER.log(
						Level.FINE, 
						this.format(String.format(
								"External incoming address %s blocked based on the "
								+ "following criterion: %s", 
								externalIncomingAddress,
								criterion)));
				return false;
			}
			return true;
		}

		private DatagramPacket newDatagramPacket(final UdpRequestHeader header) {
			byte[] headerBytes = header.toByteArray();
			InetAddress inetAddress = null;
			try {
				inetAddress = InetAddress.getByName(this.getClientAddress());
			} catch (IOException e) {
				LOGGER.log(
						Level.WARNING, 
						this.format("Error in determining the IP address from the client"), 
						e);
				return null;
			}
			int inetPort = this.getClientPort();
			return new DatagramPacket(
					headerBytes, headerBytes.length, inetAddress, inetPort);
		}
		
		private UdpRequestHeader newUdpRequestHeader(
				final DatagramPacket packet) {
			String address = packet.getAddress().getHostAddress();
			int port = packet.getPort();
			AddressType addressType = AddressType.get(address);
			UdpRequestHeader header = UdpRequestHeader.newInstance(
					0,
					addressType,
					address,
					port,
					packet.getData());
			return header;
		}
		
		@Override
		public void run() {
			this.setLastReceiveTime(System.currentTimeMillis());
			while (true) {
				try {
					byte[] buffer = new byte[this.getBufferSize()];
					DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
					try {
						this.getServerDatagramSocketInterface().receive(packet);
						this.setLastReceiveTime(System.currentTimeMillis());
					} catch (SocketException e) {
						// socket closed
						break;
					} catch (SocketTimeoutException e) {
						long timeSinceReceive = 
								System.currentTimeMillis() - this.getLastReceiveTime();
						if (timeSinceReceive >= this.getTimeout()) {
							break;
						}
						continue;
					} catch (IOException e) {
						LOGGER.log(
								Level.WARNING, 
								this.format("Error in receiving the packet from the server"), 
								e);
						continue;
					}
					LOGGER.log(
							Level.FINE, 
							this.format(String.format(
									"Packet data received: %s byte(s)", 
									packet.getLength())));
					if (!this.canAcceptExternalIncomingAddress(
							packet.getAddress().getHostAddress())) {
						continue;
					}
					UdpRequestHeader header = this.newUdpRequestHeader(packet);
					LOGGER.log(Level.FINE, this.format(header.toString()));
					packet = this.newDatagramPacket(header);
					if (packet == null) {
						continue;
					}
					try {
						this.getClientDatagramSocketInterface().send(packet);
					} catch (SocketException e) {
						// socket closed
						break;
					} catch (IOException e) {
						LOGGER.log(
								Level.WARNING, 
								this.format("Error in sending the packet to the client"), 
								e);
					}
				} catch (Throwable t) {
					LOGGER.log(
							Level.WARNING, 
							this.format("Error occurred in the process of relaying of "
									+ "a packet from the server to the client"), 
							t);
				}
			}
			if (!this.isFirstPacketsWorkerFinished()) {
				this.setFirstPacketsWorkerFinished(true);
			} else {
				if (!this.isUdpRelayServerStopped()) {
					this.stopUdpRelayServer();
				}				
			}
		}
		
	}
	
	private static final class OutgoingPacketsWorker extends PacketsWorker {
		
		private static final Logger LOGGER = Logger.getLogger(
				OutgoingPacketsWorker.class.getName());
		
		public OutgoingPacketsWorker(final UdpRelayServer server) {
			super(server);
		}
		
		private boolean canAcceptExternalOutgoingAddress(
				final String externalOutgoingAddress) {
			Criteria allowedExternalOutgoingAddrCriteria =
					this.getAllowedExternalOutgoingAddressCriteria();
			Criterion criterion = 
					allowedExternalOutgoingAddrCriteria.anyEvaluatesTrue(
							externalOutgoingAddress);
			if (criterion == null) {
				LOGGER.log(
						Level.FINE, 
						this.format(String.format(
								"External outgoing address %s not allowed", 
								externalOutgoingAddress)));
				return false;
			}
			Criteria blockedExternalOutgoingAddrCriteria =
					this.getBlockedExternalOutgoingAddressCriteria();
			criterion = blockedExternalOutgoingAddrCriteria.anyEvaluatesTrue(
					externalOutgoingAddress);
			if (criterion != null) {
				LOGGER.log(
						Level.FINE, 
						this.format(String.format(
								"External outgoing address %s blocked based on the "
								+ "following criterion: %s", 
								externalOutgoingAddress,
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
				clientInetAddr = InetAddress.getByName(this.getClientAddress());
			} catch (IOException e) {
				LOGGER.log(
						Level.WARNING, 
						this.format("Error in determining the IP address from the client"), 
						e);
				return false;
			}
			InetAddress inetAddr = null;
			try {
				inetAddr = InetAddress.getByName(address);
			} catch (IOException e) {
				LOGGER.log(
						Level.WARNING, 
						this.format("Error in determining the IP address from the client"), 
						e);
				return false;
			}
			if ((!clientInetAddr.isLoopbackAddress() 
					|| !inetAddr.isLoopbackAddress())
					&& !clientInetAddr.equals(inetAddr)) {
				return false;
			}
			int clientPrt = this.getClientPort();
			if (clientPrt == 0) {
				this.setClientPort(port);
			} else {
				if (clientPrt != port) {
					return false;
				}
			}
			return true;
		}
		
		private DatagramPacket newDatagramPacket(final UdpRequestHeader header) {
			byte[] userData = header.getUserData();
			InetAddress inetAddress = null;
			try {
				inetAddress = this.getHostnameResolver().resolve(
						header.getDesiredDestinationAddress());
			} catch (IOException e) {
				LOGGER.log(
						Level.WARNING, 
						this.format("Error in determining the IP address from the server"), 
						e);
				return null;
			}
			int inetPort = header.getDesiredDestinationPort();
			return new DatagramPacket(
					userData, userData.length, inetAddress, inetPort);
		}
		
		private UdpRequestHeader newUdpRequestHeader(final DatagramPacket packet) {
			UdpRequestHeader header = null; 
			try {
				header = UdpRequestHeader.newInstance(packet.getData());
			} catch (IllegalArgumentException e) {
				LOGGER.log(
						Level.WARNING, 
						this.format("Error in parsing the UDP header request from the client"), 
						e);
				return null;
			}
			return header;
		}
		
		@Override
		public void run() {
			this.setLastReceiveTime(System.currentTimeMillis());
			while (true) {
				try {
					byte[] buffer = new byte[this.getBufferSize()];
					DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
					try {
						this.getClientDatagramSocketInterface().receive(packet);
						this.setLastReceiveTime(System.currentTimeMillis());
					} catch (SocketException e) {
						// socket closed
						break;
					} catch (SocketTimeoutException e) {
						long timeSinceReceive = 
								System.currentTimeMillis() - this.getLastReceiveTime();
						if (timeSinceReceive >= this.getTimeout()) {
							break;
						}
						continue;
					} catch (IOException e) {
						LOGGER.log(
								Level.WARNING, 
								this.format("Error in receiving packet from the client"), 
								e);
						continue;
					}
					LOGGER.log(
							Level.FINE, 
							this.format(String.format(
									"Packet data received: %s byte(s)", 
									packet.getLength())));					
					if (!this.canForwardDatagramPacket(packet)) {
						continue;
					}
					UdpRequestHeader header = this.newUdpRequestHeader(packet);
					if (header == null) {
						continue;
					}
					LOGGER.log(Level.FINE, this.format(header.toString()));
					if (header.getCurrentFragmentNumber() != 0) {
						continue;
					}
					if (!this.canAcceptExternalOutgoingAddress(
							header.getDesiredDestinationAddress())) {
						continue;
					}
					packet = this.newDatagramPacket(header);
					if (packet == null) {
						continue;
					}
					try {
						this.getServerDatagramSocketInterface().send(packet);
					} catch (SocketException e) {
						// socket closed
						break;
					} catch (IOException e) {
						LOGGER.log(
								Level.WARNING, 
								this.format("Error in sending the packet to the server"), 
								e);
					}
				} catch (Throwable t) {
					LOGGER.log(
							Level.WARNING, 
							this.format("Error occurred in the process of relaying of "
									+ "a packet from the client to the server"), 
							t);
				}
			}
			if (!this.isFirstPacketsWorkerFinished()) {
				this.setFirstPacketsWorkerFinished(true);
			} else {
				if (!this.isUdpRelayServerStopped()) {
					this.stopUdpRelayServer();
				}				
			}
		}

	}
	
	private static abstract class PacketsWorker implements Runnable {
		
		private final UdpRelayServer udpRelayServer;
		private final DatagramSocketInterface clientDatagramSocketInterface;
		private final DatagramSocketInterface serverDatagramSocketInterface;

		public PacketsWorker(final UdpRelayServer server) {
			this.udpRelayServer = server;
			this.clientDatagramSocketInterface = 
					server.clientDatagramSocketInterface;
			this.serverDatagramSocketInterface = 
					server.serverDatagramSocketInterface;
		}
		
		protected final String format(final String message) {
			return String.format("%s: %s", this, message);
		}
		
		protected final Criteria getAllowedExternalIncomingAddressCriteria() {
			return this.udpRelayServer.allowedExternalIncomingAddressCriteria;
		}
		
		protected final Criteria getAllowedExternalOutgoingAddressCriteria() {
			return this.udpRelayServer.allowedExternalOutgoingAddressCriteria;
		}
		
		protected final Criteria getBlockedExternalIncomingAddressCriteria() {
			return this.udpRelayServer.blockedExternalIncomingAddressCriteria;
		}
		
		protected final Criteria getBlockedExternalOutgoingAddressCriteria() {
			return this.udpRelayServer.blockedExternalOutgoingAddressCriteria;
		}
		
		protected final int getBufferSize() {
			return this.udpRelayServer.bufferSize;
		}
		
		protected final String getClientAddress() {
			return this.udpRelayServer.clientAddress;
		}
		
		protected final DatagramSocketInterface getClientDatagramSocketInterface() {
			return this.clientDatagramSocketInterface;
		}
		
		protected final int getClientPort() {
			return this.udpRelayServer.clientPort;
		}
		
		protected final HostnameResolver getHostnameResolver() {
			return this.udpRelayServer.hostnameResolver;
		}
		
		protected final long getLastReceiveTime() {
			return this.udpRelayServer.lastReceiveTime;
		}
		
		protected final DatagramSocketInterface getServerDatagramSocketInterface() {
			return this.serverDatagramSocketInterface;
		}
		
		protected final int getTimeout() {
			return this.udpRelayServer.timeout;
		}
		
		protected final boolean isFirstPacketsWorkerFinished() {
			return this.udpRelayServer.firstPacketsWorkerFinished;
		}
		
		protected final boolean isUdpRelayServerStopped() {
			return this.udpRelayServer.stopped;
		}
		
		@Override
		public abstract void run();
		
		protected final void setClientPort(final int port) {
			this.udpRelayServer.clientPort = port;
		}
		
		protected final void setFirstPacketsWorkerFinished(final boolean b) {
			this.udpRelayServer.firstPacketsWorkerFinished = b;
		}
		
		protected final void setLastReceiveTime(final long time) {
			this.udpRelayServer.lastReceiveTime = time;
		}
		
		protected final void stopUdpRelayServer() {
			this.udpRelayServer.stop();
		}

		@Override
		public final String toString() {
			StringBuilder builder = new StringBuilder();
			builder.append(this.getClass().getSimpleName())
				.append(" [clientDatagramSocketInterface=")
				.append(this.clientDatagramSocketInterface)
				.append(", serverDatagramSocketInterface=")
				.append(this.serverDatagramSocketInterface)
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
	
	private final Criteria allowedExternalIncomingAddressCriteria;
	private final Criteria allowedExternalOutgoingAddressCriteria;
	private final Criteria blockedExternalIncomingAddressCriteria;
	private final Criteria blockedExternalOutgoingAddressCriteria;
	private final int bufferSize;	
	private final DatagramSocketInterface clientDatagramSocketInterface;
	private String clientAddress;
	private int clientPort;
	private ExecutorService executor;
	private boolean firstPacketsWorkerFinished;
	private HostnameResolver hostnameResolver;
	private long lastReceiveTime;
	private final DatagramSocketInterface serverDatagramSocketInterface;
	private boolean started;
	private boolean stopped;
	private final int timeout;
	
	public UdpRelayServer(		
			final ClientSocketAddress clientSockAddr,
			final DatagramSocketInterfaces datagramSockInterfaces,
			final HostnameResolver resolver, 
			final ExternalIncomingAddressCriteria externalIncomingAddrCriteria, 
			final ExternalOutgoingAddressCriteria externalOutgoingAddrCriteria, 
			final RelaySettings settings) {
		Objects.requireNonNull(clientSockAddr);
		Objects.requireNonNull(datagramSockInterfaces);
		Objects.requireNonNull(resolver);		
		Objects.requireNonNull(externalIncomingAddrCriteria);
		Objects.requireNonNull(externalOutgoingAddrCriteria);
		Objects.requireNonNull(settings);
		this.allowedExternalIncomingAddressCriteria = 
				externalIncomingAddrCriteria.getAllowedCriteria();
		this.allowedExternalOutgoingAddressCriteria =
				externalOutgoingAddrCriteria.getAllowedCriteria();
		this.blockedExternalIncomingAddressCriteria = 
				externalIncomingAddrCriteria.getBlockedCriteria();
		this.blockedExternalOutgoingAddressCriteria =
				externalOutgoingAddrCriteria.getBlockedCriteria();
		this.clientDatagramSocketInterface = 
				datagramSockInterfaces.getClientDatagramSocketInterface();
		this.bufferSize = settings.getBufferSize();
		this.clientAddress = clientSockAddr.getAddress();
		this.clientPort = clientSockAddr.getPort();
		this.executor = null;
		this.firstPacketsWorkerFinished = false;
		this.hostnameResolver = resolver;
		this.lastReceiveTime = 0L;
		this.serverDatagramSocketInterface = 
				datagramSockInterfaces.getServerDatagramSocketInterface();
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
		this.executor.execute(new IncomingPacketsWorker(this));
		this.executor.execute(new OutgoingPacketsWorker(this));
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