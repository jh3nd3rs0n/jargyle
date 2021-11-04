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
import com.github.jh3nd3rs0n.jargyle.server.RuleActionDenyException;
import com.github.jh3nd3rs0n.jargyle.server.Settings;
import com.github.jh3nd3rs0n.jargyle.server.Socks5SettingSpecConstants;
import com.github.jh3nd3rs0n.jargyle.transport.socks5.UdpRequestHeader;

public final class UdpRelayServer {
	
	private static final class InboundPacketsWorker	extends PacketsWorker {
		
		private static final Logger LOGGER = LoggerFactory.getLogger(
				InboundPacketsWorker.class);
		
		public InboundPacketsWorker(final PacketsWorkerContext context) {
			super(context);
		}
		
		private boolean canAllow(
				final String clientAddr,
				final MethodSubnegotiationResults methSubnegotiationResults,
				final String peerAddr) {
			String user = methSubnegotiationResults.getUser();
			String possibleUser = (user != null) ? 
					String.format(" (%s)", user) : "";
			Socks5UdpRule inboundSocks5UdpRule = null; 
			try {
				inboundSocks5UdpRule = 
						this.inboundSocks5UdpRules.anyAppliesTo(
								clientAddr, 
								methSubnegotiationResults,
								peerAddr);
			} catch (IllegalArgumentException e) {
				LOGGER.error(
						LoggerHelper.objectMessage(this, String.format(
								"Error regarding the client %s%s and the peer "
								+ "%s",
								clientAddr,
								possibleUser,
								peerAddr)),
						e);
				return false;
			}
			try {
				inboundSocks5UdpRule.applyTo(
						clientAddr, methSubnegotiationResults, peerAddr);
			} catch (RuleActionDenyException e) {
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
					if (!this.canAllow(
							this.clientAddress, 
							this.methodSubnegotiationResults, 
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
		
		private boolean canAllow(
				final String clientAddr,
				final MethodSubnegotiationResults methSubnegotiationResults,
				final String peerAddr) {
			String user = methSubnegotiationResults.getUser();
			String possibleUser = (user != null) ? 
					String.format(" (%s)", user) : "";
			Socks5UdpRule outboundSocks5UdpRule = null; 
			try {
				outboundSocks5UdpRule = 
						this.outboundSocks5UdpRules.anyAppliesTo(
								clientAddr, 
								methSubnegotiationResults,
								peerAddr);
			} catch (IllegalArgumentException e) {
				LOGGER.error(
						LoggerHelper.objectMessage(this, String.format(
								"Error regarding the client %s%s and the peer "
								+ "%s",
								clientAddr,
								possibleUser,
								peerAddr)),
						e);
				return false;
			}
			try {
				outboundSocks5UdpRule.applyTo(
						clientAddr,	methSubnegotiationResults, peerAddr);
			} catch (RuleActionDenyException e) {
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
					if (!this.canAllow(
							this.clientAddress,
							this.methodSubnegotiationResults,
							header.getDesiredDestinationAddress())) {
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
		protected final Socks5UdpRules inboundSocks5UdpRules;
		protected final MethodSubnegotiationResults methodSubnegotiationResults;
		protected final Socks5UdpRules outboundSocks5UdpRules;
		protected final PacketsWorkerContext packetsWorkerContext;
		protected final DatagramSocket peerFacingDatagramSocket;

		public PacketsWorker(final PacketsWorkerContext context) {
			this.bufferSize = context.getBufferSize();
			this.clientAddress = context.getClientAddress();
			this.clientFacingDatagramSocket = 
					context.getClientFacingDatagramSocket();
			this.hostResolver = context.getNetObjectFactory().newHostResolver();
			this.idleTimeout = context.getIdleTimeout();
			this.inboundSocks5UdpRules = context.getSettings().getLastValue(
					Socks5SettingSpecConstants.SOCKS5_ON_UDP_ASSOCIATE_INBOUND_SOCKS5_UDP_RULES); 
			this.methodSubnegotiationResults = 
					context.getMethodSubnegotiationResults();
			this.outboundSocks5UdpRules = context.getSettings().getLastValue(
					Socks5SettingSpecConstants.SOCKS5_ON_UDP_ASSOCIATE_OUTBOUND_SOCKS5_UDP_RULES);
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
	
	private static final class PacketsWorkerContext 
		extends CommandWorkerContext {
		
		private final DatagramSocket clientFacingDatagramSocket;
		private final DatagramSocket peerFacingDatagramSocket;
		private final UdpRelayServer udpRelayServer;
		
		public PacketsWorkerContext(
				final CommandWorkerContext context,
				final UdpRelayServer server) {
			super(context);
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
		
		public final long getIdleStartTime() {
			return this.udpRelayServer.getIdleStartTime();
		}
		
		public final int getIdleTimeout() {
			return this.udpRelayServer.idleTimeout;
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
	private final CommandWorkerContext commandWorkerContext;
	private ExecutorService executor;
	private long idleStartTime;
	private final int idleTimeout;
	private final DatagramSocket peerFacingDatagramSocket;
	private State state;
	
	public UdpRelayServer(		
			final String clientAddr,
			final int clientPrt,
			final DatagramSocket clientFacingDatagramSock,
			final DatagramSocket peerFacingDatagramSock,
			final CommandWorkerContext context) {
		Objects.requireNonNull(clientAddr);
		Objects.requireNonNull(clientFacingDatagramSock);
		Objects.requireNonNull(peerFacingDatagramSock);
		Objects.requireNonNull(context);
		if (clientPrt < 0 || clientPrt > Port.MAX_INT_VALUE) {
			throw new IllegalArgumentException("client port is out of range");
		}
		Settings settings = context.getSettings();
		this.bufferSize = settings.getLastValue(
				Socks5SettingSpecConstants.SOCKS5_ON_UDP_ASSOCIATE_RELAY_BUFFER_SIZE).intValue();
		this.clientAddress = clientAddr;
		this.clientFacingDatagramSocket = clientFacingDatagramSock;
		this.clientPort = clientPrt;
		this.commandWorkerContext = context;
		this.executor = null;
		this.idleStartTime = 0L;
		this.idleTimeout = settings.getLastValue(
				Socks5SettingSpecConstants.SOCKS5_ON_UDP_ASSOCIATE_RELAY_IDLE_TIMEOUT).intValue();
		this.peerFacingDatagramSocket = peerFacingDatagramSock;
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
				new PacketsWorkerContext(this.commandWorkerContext, this)));
		this.executor.execute(new OutboundPacketsWorker(
				new PacketsWorkerContext(this.commandWorkerContext, this)));
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
