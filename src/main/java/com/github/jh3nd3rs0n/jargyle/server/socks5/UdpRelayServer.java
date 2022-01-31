package com.github.jh3nd3rs0n.jargyle.server.socks5;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;
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
import com.github.jh3nd3rs0n.jargyle.server.Rule;
import com.github.jh3nd3rs0n.jargyle.server.rules.impl.FirewallRuleActionDenyException;
import com.github.jh3nd3rs0n.jargyle.server.rules.impl.FirewallRuleNotFoundException;
import com.github.jh3nd3rs0n.jargyle.server.rules.impl.Socks5UdpFirewallRule;
import com.github.jh3nd3rs0n.jargyle.server.rules.impl.Socks5UdpFirewallRules;
import com.github.jh3nd3rs0n.jargyle.transport.socks5.Method;
import com.github.jh3nd3rs0n.jargyle.transport.socks5.NullMethodEncapsulation;
import com.github.jh3nd3rs0n.jargyle.transport.socks5.UdpRequestHeader;

public final class UdpRelayServer {
	
	public static final class Builder {
	
		public static final int DEFAULT_BUFFER_SIZE = 32768;
		public static final HostResolver DEFAULT_HOST_RESOLVER = 
				new HostResolver();
		public static final int DEFAULT_IDLE_TIMEOUT = 60000;
		public static final Socks5UdpFirewallRules DEFAULT_INBOUND_SOCKS5_UDP_FIREWALL_RULES =
				Socks5UdpFirewallRules.getDefault();
		public static final MethodSubnegotiationResults DEFAULT_METHOD_SUBNEGOTIATION_RESULTS =
				new MethodSubnegotiationResults(
						Method.NO_AUTHENTICATION_REQUIRED, 
						new NullMethodEncapsulation(new Socket()),
						null);
		public static final Socks5UdpFirewallRules DEFAULT_OUTBOUND_SOCKS5_UDP_FIREWALL_RULES =
				Socks5UdpFirewallRules.getDefault();
		
		private int bufferSize;
		private final String clientAddress;
		private final DatagramSocket clientFacingDatagramSocket;
		private final int clientPort;
		private HostResolver hostResolver;
		private int idleTimeout;
		private Socks5UdpFirewallRules inboundSocks5UdpFirewallRules;
		private MethodSubnegotiationResults methodSubnegotiationResults;		
		private Socks5UdpFirewallRules outboundSocks5UdpFirewallRules;
		private final DatagramSocket peerFacingDatagramSocket;
		
		public Builder(
				final String clientAddr,
				final int clientPrt,
				final DatagramSocket clientFacingDatagramSock,
				final DatagramSocket peerFacingDatagramSock) {
			Objects.requireNonNull(clientAddr);
			Objects.requireNonNull(clientFacingDatagramSock);
			Objects.requireNonNull(peerFacingDatagramSock);
			if (clientPrt < 0 || clientPrt > Port.MAX_INT_VALUE) {
				throw new IllegalArgumentException("client port is out of range");
			}
			this.bufferSize = DEFAULT_BUFFER_SIZE;
			this.clientAddress = clientAddr;
			this.clientFacingDatagramSocket = clientFacingDatagramSock;
			this.clientPort = clientPrt;
			this.hostResolver = DEFAULT_HOST_RESOLVER;
			this.idleTimeout = DEFAULT_IDLE_TIMEOUT;
			this.inboundSocks5UdpFirewallRules = DEFAULT_INBOUND_SOCKS5_UDP_FIREWALL_RULES;
			this.methodSubnegotiationResults = DEFAULT_METHOD_SUBNEGOTIATION_RESULTS;			
			this.outboundSocks5UdpFirewallRules = DEFAULT_OUTBOUND_SOCKS5_UDP_FIREWALL_RULES;
			this.peerFacingDatagramSocket = peerFacingDatagramSock;
		}
		
		public Builder bufferSize(final int bffrSize) {
			if (bffrSize < 0) {
				throw new IllegalArgumentException(
						"buffer size must be greater than 0");
			}
			this.bufferSize = bffrSize;
			return this;
		}
		
		public UdpRelayServer build() {
			return new UdpRelayServer(this);
		}
		
		public Builder hostResolver(final HostResolver resolver) {
			this.hostResolver = Objects.requireNonNull(resolver);
			return this;
		}
		
		public Builder idleTimeout(final int idleTmt) {
			if (idleTmt < 0) {
				throw new IllegalArgumentException(
						"idle timeout must be greater than 0");
			}
			this.idleTimeout = idleTmt;
			return this;
		}
		
		public Builder inboundSocks5UdpFirewallRules(
				final Socks5UdpFirewallRules socks5UdpFirewallRules) {
			this.inboundSocks5UdpFirewallRules = Objects.requireNonNull(socks5UdpFirewallRules);
			return this;
		}
		
		public Builder methodSubnegotiationResults(
				final MethodSubnegotiationResults methSubnegotiationResults) {
			this.methodSubnegotiationResults = methSubnegotiationResults;
			return this;
		}
		
		public Builder outboundSocks5UdpFirewallRules(
				final Socks5UdpFirewallRules socks5UdpFirewallRules) {
			this.outboundSocks5UdpFirewallRules = Objects.requireNonNull(socks5UdpFirewallRules);
			return this;
		}
		
	}
	
	private static final class InboundPacketsWorker	extends PacketsWorker {
		
		private static final Logger LOGGER = LoggerFactory.getLogger(
				InboundPacketsWorker.class);
		
		public InboundPacketsWorker(final PacketsWorkerContext context) {
			super(context);
		}
		
		private boolean canAllowPacket(final Rule.Context context) {
			Socks5UdpFirewallRule inboundSocks5UdpFirewallRule = null; 
			try {
				inboundSocks5UdpFirewallRule = 
						this.inboundSocks5UdpFirewallRules.anyAppliesBasedOn(
								context);
			} catch (FirewallRuleNotFoundException e) {
				LOGGER.error(
						LoggerHelper.objectMessage(this, String.format(
								"Firewall rule not found for the following "
								+ "context: %s",
								context)),
						e);
				return false;
			}
			try {
				inboundSocks5UdpFirewallRule.applyBasedOn(context);
			} catch (FirewallRuleActionDenyException e) {
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
						this.peerFacingDatagramSocket.receive(packet);
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
										+ "the peer"), 
								e);
						continue;
					}
					LOGGER.trace(LoggerHelper.objectMessage(this, String.format(
							"Packet data received: %s byte(s)",
							packet.getLength())));
					if (!this.canAllowPacket(new Socks5UdpFirewallRule.Context(
							this.clientAddress, 
							this.methodSubnegotiationResults, 
							packet.getAddress().getHostAddress()))) {
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
									+ "relaying of a packet from the peer to "
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
		
		private boolean canAllowPacket(final Rule.Context context) {
			Socks5UdpFirewallRule outboundSocks5UdpFirewallRule = null; 
			try {
				outboundSocks5UdpFirewallRule = 
						this.outboundSocks5UdpFirewallRules.anyAppliesBasedOn(
								context);
			} catch (FirewallRuleNotFoundException e) {
				LOGGER.error(
						LoggerHelper.objectMessage(this, String.format(
								"Firewall rule not found for the following "
								+ "context: %s",
								context)),
						e);
				return false;
			}
			try {
				outboundSocks5UdpFirewallRule.applyBasedOn(context);
			} catch (FirewallRuleActionDenyException e) {
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
								+ "peer"), 
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
					if (!this.canAllowPacket(new Socks5UdpFirewallRule.Context(
							this.clientAddress,
							this.methodSubnegotiationResults,
							header.getDesiredDestinationAddress()))) {
						continue;
					}
					packet = this.newDatagramPacket(header);
					if (packet == null) {
						continue;
					}
					try {
						this.peerFacingDatagramSocket.send(packet);
					} catch (SocketException e) {
						// socket closed
						break;
					} catch (IOException e) {
						LOGGER.error( 
								LoggerHelper.objectMessage(
										this, 
										"Error in sending the packet to the "
										+ "peer"), 
								e);
					}
				} catch (Throwable t) {
					LOGGER.error( 
							LoggerHelper.objectMessage(
									this, 
									"Error occurred in the process of "
									+ "relaying of a packet from the client to "
									+ "the peer"), 
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
		protected final Socks5UdpFirewallRules inboundSocks5UdpFirewallRules;
		protected final MethodSubnegotiationResults methodSubnegotiationResults;
		protected final Socks5UdpFirewallRules outboundSocks5UdpFirewallRules;
		protected final PacketsWorkerContext packetsWorkerContext;
		protected final DatagramSocket peerFacingDatagramSocket;

		public PacketsWorker(final PacketsWorkerContext context) {
			this.bufferSize = context.getBufferSize();
			this.clientAddress = context.getClientAddress();
			this.clientFacingDatagramSocket = 
					context.getClientFacingDatagramSocket();
			this.hostResolver = context.getHostResolver();
			this.idleTimeout = context.getIdleTimeout();
			this.inboundSocks5UdpFirewallRules = 
					context.getInboundSocks5UdpFirewallRules(); 
			this.methodSubnegotiationResults = 
					context.getMethodSubnegotiationResults();
			this.outboundSocks5UdpFirewallRules = 
					context.getOutboundSocks5UdpFirewallRules();
			this.packetsWorkerContext = context;
			this.peerFacingDatagramSocket = 
					context.getPeerFacingDatagramSocket();
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
		private final DatagramSocket peerFacingDatagramSocket;
		private final UdpRelayServer udpRelayServer;
		
		public PacketsWorkerContext(final UdpRelayServer server) {
			this.clientFacingDatagramSocket = server.clientFacingDatagramSocket;
			this.peerFacingDatagramSocket = server.peerFacingDatagramSocket;
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
		
		public final Socks5UdpFirewallRules getInboundSocks5UdpFirewallRules() {
			return this.udpRelayServer.inboundSocks5UdpFirewallRules;
		}
		
		public final MethodSubnegotiationResults getMethodSubnegotiationResults() {
			return this.udpRelayServer.methodSubnegotiationResults;
		}
		
		public final Socks5UdpFirewallRules getOutboundSocks5UdpFirewallRules() {
			return this.udpRelayServer.outboundSocks5UdpFirewallRules;
		}
		
		public final DatagramSocket getPeerFacingDatagramSocket() {
			return this.peerFacingDatagramSocket;
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
				.append(", peerFacingDatagramSocket=")
				.append(this.peerFacingDatagramSocket)
				.append("]");
			return builder.toString();
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
	private final Socks5UdpFirewallRules inboundSocks5UdpFirewallRules;
	private final MethodSubnegotiationResults methodSubnegotiationResults;	
	private final Socks5UdpFirewallRules outboundSocks5UdpFirewallRules;
	private final DatagramSocket peerFacingDatagramSocket;
	private State state;
	
	private UdpRelayServer(final Builder builder) {
		this.bufferSize = builder.bufferSize;
		this.clientAddress = builder.clientAddress;
		this.clientFacingDatagramSocket = builder.clientFacingDatagramSocket;
		this.clientPort = builder.clientPort;
		this.executor = null;
		this.hostResolver = builder.hostResolver;
		this.idleStartTime = 0L;
		this.idleTimeout = builder.idleTimeout;
		this.inboundSocks5UdpFirewallRules = 
				builder.inboundSocks5UdpFirewallRules;
		this.methodSubnegotiationResults = builder.methodSubnegotiationResults;		
		this.outboundSocks5UdpFirewallRules = 
				builder.outboundSocks5UdpFirewallRules;
		this.peerFacingDatagramSocket = builder.peerFacingDatagramSocket;
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
