package com.github.jh3nd3rs0n.jargyle.server.socks5;

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

import com.github.jh3nd3rs0n.jargyle.client.HostResolver;
import com.github.jh3nd3rs0n.jargyle.common.net.Port;
import com.github.jh3nd3rs0n.jargyle.internal.logging.LoggerHelper;
import com.github.jh3nd3rs0n.jargyle.server.Action;
import com.github.jh3nd3rs0n.jargyle.server.Rule;
import com.github.jh3nd3rs0n.jargyle.server.Rules;
import com.github.jh3nd3rs0n.jargyle.transport.socks5.UdpRequestHeader;

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
	
	private static final class InboundPacketsWorker	extends PacketsWorker {
		
		private static final Logger LOGGER = LoggerFactory.getLogger(
				InboundPacketsWorker.class);
		
		public InboundPacketsWorker(final PacketsWorkerContext context) {
			super(context);
		}
		
		private boolean canAllowInboundAddress(final String inboundAddress) {
			Rule inboundAddressRule = this.inboundAddressRules.anyAppliesTo(
					inboundAddress);
			if (inboundAddressRule == null) {
				LOGGER.debug(LoggerHelper.objectMessage(this, String.format(
						"Inbound address %s not allowed",
						inboundAddress)));
				return false;
			}
			if (inboundAddressRule.getAction().equals(Action.BLOCK)) {
				LOGGER.debug(LoggerHelper.objectMessage(this, String.format(
						"Inbound address %s blocked based on the "
						+ "following rule: %s",
						inboundAddress,
						inboundAddressRule)));
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
				LOGGER.error( 
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
			while (true) {
				try {
					byte[] buffer = new byte[this.bufferSize];
					DatagramPacket packet = new DatagramPacket(
							buffer, buffer.length);
					try {
						this.serverFacingDatagramSocket.receive(packet);
						this.packetsWorkerContext.setIdleStartTime(
								System.currentTimeMillis());
					} catch (SocketException e) {
						// socket closed
						break;
					} catch (SocketTimeoutException e) {
						long idleStartTime = 
								this.packetsWorkerContext.getIdleStartTime();
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
					} catch (IOException e) {
						LOGGER.error( 
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
						LOGGER.error( 
								LoggerHelper.objectMessage(
										this, 
										"Error in sending the packet to the "
										+ "client"), 
								e);
					}
				} catch (Throwable t) {
					LOGGER.error( 
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
	
	private static final class OutboundPacketsWorker extends PacketsWorker {
		
		private static final Logger LOGGER = LoggerFactory.getLogger(
				OutboundPacketsWorker.class);
		
		public OutboundPacketsWorker(final PacketsWorkerContext context) {
			super(context);
		}
		
		private boolean canAllowOutboundAddress(final String outboundAddress) {
			Rule outboundAddressRule = this.outboundAddressRules.anyAppliesTo(
					outboundAddress);
			if (outboundAddressRule == null) {
				LOGGER.debug(LoggerHelper.objectMessage(this, String.format(
						"Outbound address %s not allowed",
						outboundAddress)));
				return false;
			}
			if (outboundAddressRule.getAction().equals(Action.BLOCK)) {
				LOGGER.debug(LoggerHelper.objectMessage(this, String.format(
						"Outbound address %s blocked based on the "
						+ "following rule: %s",
						outboundAddress,
						outboundAddressRule)));
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
				LOGGER.error( 
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
				LOGGER.error( 
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
				LOGGER.error( 
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
				LOGGER.error( 
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
			while (true) {
				try {
					byte[] buffer = new byte[this.bufferSize];
					DatagramPacket packet = new DatagramPacket(
							buffer, buffer.length);
					try {
						this.clientFacingDatagramSocket.receive(packet);
						this.packetsWorkerContext.setIdleStartTime(
								System.currentTimeMillis());
					} catch (SocketException e) {
						// socket closed
						break;
					} catch (SocketTimeoutException e) {
						long idleStartTime = 
								this.packetsWorkerContext.getIdleStartTime();
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
					} catch (IOException e) {
						LOGGER.error( 
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
						LOGGER.error( 
								LoggerHelper.objectMessage(
										this, 
										"Error in sending the packet to the "
										+ "server"), 
								e);
					}
				} catch (Throwable t) {
					LOGGER.error( 
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
		
		protected final int bufferSize;
		protected final String clientAddress;
		protected final DatagramSocket clientFacingDatagramSocket;
		protected final HostResolver hostResolver;
		protected final int idleTimeout;
		protected final Rules inboundAddressRules;
		protected final Rules outboundAddressRules;
		protected final PacketsWorkerContext packetsWorkerContext;
		protected final DatagramSocket serverFacingDatagramSocket;

		public PacketsWorker(final PacketsWorkerContext context) {
			this.bufferSize = context.getBufferSize();
			this.clientAddress = context.getClientAddress();
			this.clientFacingDatagramSocket = 
					context.getClientFacingDatagramSocket();
			this.hostResolver = context.getHostResolver();
			this.idleTimeout = context.getIdleTimeout();
			this.inboundAddressRules = context.getInboundAddressRules();
			this.outboundAddressRules = context.getOutboundAddressRules();
			this.packetsWorkerContext = context;
			this.serverFacingDatagramSocket = 
					context.getServerFacingDatagramSocket();
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
		
		public final long getIdleStartTime() {
			return this.udpRelayServer.getIdleStartTime();
		}
		
		public final int getIdleTimeout() {
			return this.udpRelayServer.idleTimeout;
		}
		
		public final Rules getInboundAddressRules() {
			return this.udpRelayServer.inboundAddressRules;
		}
		
		public final Rules getOutboundAddressRules() {
			return this.udpRelayServer.outboundAddressRules;
		}
		
		public final DatagramSocket getServerFacingDatagramSocket() {
			return this.serverFacingDatagramSocket;
		}
		
		public final void setClientPort(final int port) {
			this.udpRelayServer.setClientPort(port);
		}
		
		public final void setIdleStartTime(final long time) {
			this.udpRelayServer.setIdleStartTime(time);
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
		private final int idleTimeout;
		
		public RelaySettings(final int bffrSize, final int idleTmt) {
			if (bffrSize < 1) {
				throw new IllegalArgumentException(
						"buffer size must not be less than 1");
			}
			if (idleTmt < 1) {
				throw new IllegalArgumentException(
						"idle timeout must not be less than 1");
			}
			this.bufferSize = bffrSize;
			this.idleTimeout = idleTmt;
		}
		
		public int getBufferSize() {
			return this.bufferSize;
		}
		
		public int getIdleTimeout() {
			return this.idleTimeout;
		}
		
	}
	
	public static enum State {
		
		STARTED,
		
		STOPPED
		
	}
	
	private final int bufferSize;	
	private final String clientAddress;
	private final DatagramSocket clientFacingDatagramSocket;	
	private int clientPort;
	private ExecutorService executor;
	private final HostResolver hostResolver;
	private long idleStartTime;
	private final int idleTimeout;
	private final Rules inboundAddressRules;
	private final Rules outboundAddressRules;
	private final DatagramSocket serverFacingDatagramSocket;
	private State state;
	
	
	public UdpRelayServer(		
			final ClientDatagramSocketAddress clientDatagramSockAddr,
			final DatagramSockets datagramSocks,
			final HostResolver resolver, 
			final Rules inboundAddrRules, 
			final Rules outboundAddrRules, 
			final RelaySettings settings) {
		Objects.requireNonNull(clientDatagramSockAddr);
		Objects.requireNonNull(datagramSocks);
		Objects.requireNonNull(resolver);		
		Objects.requireNonNull(inboundAddrRules);
		Objects.requireNonNull(outboundAddrRules);
		Objects.requireNonNull(settings);
		this.bufferSize = settings.getBufferSize();
		this.clientAddress = clientDatagramSockAddr.getAddress();
		this.clientFacingDatagramSocket = 
				datagramSocks.getClientFacingDatagramSocket();
		this.clientPort = clientDatagramSockAddr.getPort();
		this.executor = null;
		this.hostResolver = resolver;
		this.idleStartTime = 0L;
		this.idleTimeout = settings.getIdleTimeout();
		this.inboundAddressRules = inboundAddrRules;
		this.outboundAddressRules = outboundAddrRules;
		this.serverFacingDatagramSocket = 
				datagramSocks.getServerFacingDatagramSocket();
		this.state = State.STOPPED;
	}

	private synchronized int getClientPort() {
		return this.clientPort;
	}
	
	private synchronized long getIdleStartTime() {
		return this.idleStartTime;
	}
	
	public State getState() {
		return this.state;
	}
	
	private synchronized void setClientPort(final int port) {
		this.clientPort = port;
	}
	
	private synchronized void setIdleStartTime(final long time) {
		this.idleStartTime = time;
	}
	
	public void start() throws IOException {
		if (this.state.equals(State.STARTED)) {
			throw new IllegalStateException("UdpRelayServer already started");
		}
		this.idleStartTime = System.currentTimeMillis();
		this.executor = Executors.newFixedThreadPool(2);
		this.executor.execute(new InboundPacketsWorker(
				new PacketsWorkerContext(this)));
		this.executor.execute(new OutboundPacketsWorker(
				new PacketsWorkerContext(this)));
		this.state = State.STARTED;
	}
	
	public void stop() {
		if (this.state.equals(State.STOPPED)) {
			throw new IllegalStateException("UdpRelayServer already stopped");
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
