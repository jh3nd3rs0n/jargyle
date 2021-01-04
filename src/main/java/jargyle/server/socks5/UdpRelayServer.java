package jargyle.server.socks5;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;

import jargyle.common.net.DatagramSocketInterface;
import jargyle.common.net.socks5.AddressType;
import jargyle.common.net.socks5.UdpRequestHeader;
import jargyle.server.Criteria;
import jargyle.server.Criterion;

final class UdpRelayServer {
	
	private static final class IncomingPacketsWorker extends PacketsWorker {
		
		private static final Logger LOGGER = Logger.getLogger(
				IncomingPacketsWorker.class.getName());
		
		public IncomingPacketsWorker(final UdpRelayServer server) {
			super(server);
		}
		
		private boolean canAcceptExternalIncomingAddress(
				final String externalIncomingAddress) {
			Criteria allowedExternalIncomingAddrCriteria =
					this.getUdpRelayServer().allowedExternalIncomingAddressCriteria;
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
					this.getUdpRelayServer().blockedExternalIncomingAddressCriteria;
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
		
		private boolean canForwardDatagramPacket(final DatagramPacket packet) {
			String address = packet.getAddress().getHostAddress();
			int port = packet.getPort();
			String desiredDestinationAddr = 
					this.getUdpRelayServer().desiredDestinationAddress;
			int desiredDestinationPrt =
					this.getUdpRelayServer().desiredDestinationPort;
			if (desiredDestinationAddr != null 
					&& desiredDestinationPrt > -1) {
				InetAddress desiredDestinationInetAddr = null;
				try {
					desiredDestinationInetAddr = InetAddress.getByName(
							desiredDestinationAddr);
				} catch (UnknownHostException e) {
					LOGGER.log(
							Level.WARNING, 
							this.format("Error in determining the IP address from the server"), 
							e);
					return false;
				}
				InetAddress inetAddr = null;
				try {
					inetAddr = InetAddress.getByName(address);
				} catch (UnknownHostException e) {
					LOGGER.log(
							Level.WARNING, 
							this.format("Error in determining the IP address from the server"), 
							e);
					return false;
				}
				if (!desiredDestinationInetAddr.isLoopbackAddress()
						|| !inetAddr.isLoopbackAddress()) {
					if (!desiredDestinationInetAddr.equals(inetAddr)) {
						return false;
					}
				}
				if (desiredDestinationPrt != port) {
					return false;
				}
			}
			return true;
		}

		private DatagramPacket newDatagramPacket(final UdpRequestHeader header) {
			byte[] headerBytes = header.toByteArray();
			InetAddress inetAddress = null;
			try {
				inetAddress = InetAddress.getByName(
						this.getUdpRelayServer().sourceAddress);
			} catch (UnknownHostException e) {
				LOGGER.log(
						Level.WARNING, 
						this.format("Error in determining the IP address from the client"), 
						e);
				return null;
			}
			int inetPort = this.getUdpRelayServer().sourcePort;
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
			this.getUdpRelayServer().lastReceiveTime = System.currentTimeMillis();
			while (true) {
				try {
					byte[] buffer = new byte[this.getUdpRelayServer().bufferSize];
					DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
					try {
						this.getServerDatagramSocketInterface().receive(packet);
						this.getUdpRelayServer().lastReceiveTime = System.currentTimeMillis();
					} catch (SocketException e) {
						// socket closed
						break;
					} catch (SocketTimeoutException e) {
						long timeSinceReceive = 
								System.currentTimeMillis() - this.getUdpRelayServer().lastReceiveTime;
						if (timeSinceReceive >= this.getUdpRelayServer().timeout) {
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
					if (!this.canForwardDatagramPacket(packet)) {
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
			if (!this.getUdpRelayServer().firstPacketsWorkerFinished) {
				this.getUdpRelayServer().firstPacketsWorkerFinished = true;
			} else {
				if (!this.getUdpRelayServer().stopped) {
					this.getUdpRelayServer().stop();
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
		
		private boolean canForwardDatagramPacket(final DatagramPacket packet) {
			String address = packet.getAddress().getHostAddress();
			int port = packet.getPort();
			InetAddress sourceInetAddr = null;
			try {
				sourceInetAddr = InetAddress.getByName(
						this.getUdpRelayServer().sourceAddress);
			} catch (UnknownHostException e) {
				LOGGER.log(
						Level.WARNING, 
						this.format("Error in determining the IP address from the client"), 
						e);
				return false;
			}
			InetAddress inetAddr = null;
			try {
				inetAddr = InetAddress.getByName(address);
			} catch (UnknownHostException e) {
				LOGGER.log(
						Level.WARNING, 
						this.format("Error in determining the IP address from the client"), 
						e);
				return false;
			}
			if (!sourceInetAddr.isLoopbackAddress() 
					|| !inetAddr.isLoopbackAddress()) {
				if (!sourceInetAddr.equals(inetAddr)) {
					return false;
				}
			}
			if (this.getUdpRelayServer().sourcePort == -1) {
				this.getUdpRelayServer().sourcePort = port;
			}
			return true;
		}
		
		private boolean canForwardUdpRequestHeader(
				final UdpRequestHeader header) {
			if (header.getCurrentFragmentNumber() != 0) {
				return false;
			}
			String desiredDestinationAddr = 
					this.getUdpRelayServer().desiredDestinationAddress;
			int desiredDestinationPrt =
					this.getUdpRelayServer().desiredDestinationPort;
			String desiredDestAddr = 
					header.getDesiredDestinationAddress();
			int desiredDestPrt =
					header.getDesiredDestinationPort();
			if (desiredDestinationAddr != null 
					&& desiredDestinationPrt > -1) {
				InetAddress desiredDestinationInetAddr = null;
				try {
					desiredDestinationInetAddr = InetAddress.getByName(
							desiredDestinationAddr);
				} catch (UnknownHostException e) {
					LOGGER.log(
							Level.WARNING, 
							this.format("Error in determining the IP address from the server"), 
							e);
					return false;
				}
				InetAddress desiredDestInetAddr = null;
				try {
					desiredDestInetAddr = InetAddress.getByName(
							desiredDestAddr);
				} catch (UnknownHostException e) {
					LOGGER.log(
							Level.WARNING, 
							this.format("Error in determining the IP address from the server"), 
							e);
					return false;
				}
				if (!desiredDestinationInetAddr.isLoopbackAddress()
						|| !desiredDestInetAddr.isLoopbackAddress()) {
					if (!desiredDestinationInetAddr.equals(
							desiredDestInetAddr)) {
						return false;
					}
				}
				if (desiredDestinationPrt != desiredDestPrt) {
					return false;
				}
			}
			return true;
		}
		
		private DatagramPacket newDatagramPacket(final UdpRequestHeader header) {
			byte[] userData = header.getUserData();
			InetAddress inetAddress = null;
			try {
				inetAddress = InetAddress.getByName(
						header.getDesiredDestinationAddress());
			} catch (UnknownHostException e) {
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
			this.getUdpRelayServer().lastReceiveTime = System.currentTimeMillis();
			while (true) {
				try {
					byte[] buffer = new byte[this.getUdpRelayServer().bufferSize];
					DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
					try {
						this.getClientDatagramSocketInterface().receive(packet);
						this.getUdpRelayServer().lastReceiveTime = System.currentTimeMillis();
					} catch (SocketException e) {
						// socket closed
						break;
					} catch (SocketTimeoutException e) {
						long timeSinceReceive = 
								System.currentTimeMillis() - this.getUdpRelayServer().lastReceiveTime;
						if (timeSinceReceive >= this.getUdpRelayServer().timeout) {
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
					if (!this.canForwardUdpRequestHeader(header)) {
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
			if (!this.getUdpRelayServer().firstPacketsWorkerFinished) {
				this.getUdpRelayServer().firstPacketsWorkerFinished = true;
			} else {
				if (!this.getUdpRelayServer().stopped) {
					this.getUdpRelayServer().stop();
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
			this.clientDatagramSocketInterface = server.clientDatagramSocketInterface;
			this.serverDatagramSocketInterface = server.serverDatagramSocketInterface;
		}
		
		protected String format(final String message) {
			return String.format("%s: %s", this, message);
		}
		
		protected final DatagramSocketInterface getClientDatagramSocketInterface() {
			return this.clientDatagramSocketInterface;
		}
		
		protected final DatagramSocketInterface getServerDatagramSocketInterface() {
			return this.serverDatagramSocketInterface;
		}
		
		protected final UdpRelayServer getUdpRelayServer() {
			return this.udpRelayServer;
		}
		
		@Override
		public abstract void run();

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
	
	private final Criteria allowedExternalIncomingAddressCriteria;
	private final Criteria blockedExternalIncomingAddressCriteria;
	private final DatagramSocketInterface clientDatagramSocketInterface;
	private final int bufferSize;
	private String desiredDestinationAddress;
	private int desiredDestinationPort;
	private ExecutorService executor;
	private boolean firstPacketsWorkerFinished;
	private long lastReceiveTime;
	private final DatagramSocketInterface serverDatagramSocketInterface;
	private String sourceAddress;
	private int sourcePort;
	private boolean started;
	private boolean stopped;
	private final int timeout;
	
	public UdpRelayServer(		
			final DatagramSocketInterface clientDatagramSockInterface,
			final DatagramSocketInterface serverDatagramSockInterface,
			final String sourceAddr,
			final String desiredDestinationAddr,
			final int desiredDestinationPrt, 
			final Criteria allowedExternalIncomingAddrCriteria, 
			final Criteria blockedExternalIncomingAddrCriteria, 
			final int bffrSize, 
			final int tmt) {
		Objects.requireNonNull(clientDatagramSockInterface);
		Objects.requireNonNull(serverDatagramSockInterface);
		Objects.requireNonNull(desiredDestinationAddr);
		Objects.requireNonNull(allowedExternalIncomingAddrCriteria);
		Objects.requireNonNull(blockedExternalIncomingAddrCriteria);
		UdpRequestHeader.validateDesiredDestinationAddress(
				desiredDestinationAddr);
		UdpRequestHeader.validateDesiredDestinationPort(
				desiredDestinationPrt);
		if (bffrSize < 1) {
			throw new IllegalArgumentException(
					"buffer size must not be less than 1");
		}
		if (tmt < 1) {
			throw new IllegalArgumentException(
					"timeout must not be less than 1");
		}
		String desiredDestAddr = desiredDestinationAddr;
		int desiredDestPrt = desiredDestinationPrt;
		if (!desiredDestAddr.matches("[a-zA-Z1-9]") && desiredDestPrt == 0) {
			desiredDestAddr = null;
			desiredDestPrt = -1;
		}
		this.allowedExternalIncomingAddressCriteria = 
				allowedExternalIncomingAddrCriteria;
		this.blockedExternalIncomingAddressCriteria = 
				blockedExternalIncomingAddrCriteria;
		this.clientDatagramSocketInterface = clientDatagramSockInterface;
		this.bufferSize = bffrSize;
		this.desiredDestinationAddress = desiredDestAddr;
		this.desiredDestinationPort = desiredDestPrt;
		this.executor = null;
		this.firstPacketsWorkerFinished = false;
		this.lastReceiveTime = 0L;
		this.serverDatagramSocketInterface = serverDatagramSockInterface;
		this.sourceAddress = sourceAddr;
		this.sourcePort = -1;
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