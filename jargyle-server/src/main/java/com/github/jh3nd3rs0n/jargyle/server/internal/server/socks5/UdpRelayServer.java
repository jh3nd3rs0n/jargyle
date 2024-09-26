package com.github.jh3nd3rs0n.jargyle.server.internal.server.socks5;

import com.github.jh3nd3rs0n.jargyle.client.HostResolver;
import com.github.jh3nd3rs0n.jargyle.common.net.HostAddress;
import com.github.jh3nd3rs0n.jargyle.common.net.Port;
import com.github.jh3nd3rs0n.jargyle.common.number.UnsignedByte;
import com.github.jh3nd3rs0n.jargyle.internal.logging.ObjectLogMessageHelper;
import com.github.jh3nd3rs0n.jargyle.internal.throwable.ThrowableHelper;
import com.github.jh3nd3rs0n.jargyle.protocolbase.socks5.Address;
import com.github.jh3nd3rs0n.jargyle.protocolbase.socks5.UdpRequest;
import com.github.jh3nd3rs0n.jargyle.server.*;
import com.github.jh3nd3rs0n.jargyle.internal.concurrent.ExecutorsHelper;
import com.github.jh3nd3rs0n.jargyle.server.internal.server.Rules;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.*;
import java.util.Arrays;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;

final class UdpRelayServer {
	
	public static final class Builder {

		public static final int DEFAULT_BUFFER_SIZE = 32768;
		public static final HostResolver DEFAULT_HOST_RESOLVER = new HostResolver();
		public static final int DEFAULT_IDLE_TIMEOUT = 60000;
		public static final RuleContext DEFAULT_RULE_CONTEXT = new RuleContext();
		public static final Rules DEFAULT_RULES = Rules.of(Rule.getDefault());
		
		private int bufferSize;
		private final String clientAddress;
		private final DatagramSocket clientFacingDatagramSocket;
		private final int clientPort;
		private HostResolver hostResolver;
		private int idleTimeout;
		private final DatagramSocket peerFacingDatagramSocket;
		private RuleContext ruleContext;
		private Rules rules;
		
		public Builder(
				final String clientAddr,
				final int clientPrt,
				final DatagramSocket clientFacingDatagramSock,
				final DatagramSocket peerFacingDatagramSock) {
			Objects.requireNonNull(clientAddr);
			Objects.requireNonNull(clientFacingDatagramSock);
			Objects.requireNonNull(peerFacingDatagramSock);
			if (clientPrt < 0 || clientPrt > Port.MAX_INT_VALUE) {
				throw new IllegalArgumentException(
						"client port is out of range");
			}
			this.bufferSize = DEFAULT_BUFFER_SIZE;
			this.clientAddress = clientAddr;
			this.clientFacingDatagramSocket = clientFacingDatagramSock;
			this.clientPort = clientPrt;
			this.hostResolver = DEFAULT_HOST_RESOLVER;
			this.idleTimeout = DEFAULT_IDLE_TIMEOUT;
			this.peerFacingDatagramSocket = peerFacingDatagramSock;
			this.ruleContext = new RuleContext(DEFAULT_RULE_CONTEXT);
			this.rules = DEFAULT_RULES;
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
		
		public Builder ruleContext(final RuleContext context) {
			this.ruleContext = context;
			return this;
		}
		
		public Builder rules(final Rules rls) {
			this.rules = rls;
			return this;
		}
		
	}
	
	private static final class InboundPacketsWorker	extends PacketsWorker {
		
		private final Logger logger;
		
		public InboundPacketsWorker(final UdpRelayServer server) {
			super(server);
			this.logger = LoggerFactory.getLogger(InboundPacketsWorker.class);
		}
		
		private boolean canAllowDatagramPacket(
				final Rule rule, final RuleContext ruleContext) {
			if (!this.hasInboundRuleCondition(rule)) {
				return true;
			}
			FirewallAction firewallAction = rule.getLastRuleResultValue(
					GeneralRuleResultSpecConstants.FIREWALL_ACTION);
			if (firewallAction == null) {
				return false;
			}
			LogAction firewallActionLogAction =	rule.getLastRuleResultValue(
					GeneralRuleResultSpecConstants.FIREWALL_ACTION_LOG_ACTION);
			if (firewallAction.equals(FirewallAction.ALLOW)
					&& firewallActionLogAction != null) {
				firewallActionLogAction.invoke(
						ObjectLogMessageHelper.objectLogMessage(
								this,
								"Inbound UDP packet allowed based on the "
								+ "following rule and context: rule: %s "
								+ "context: %s",
								rule,
								ruleContext));				
			} else if (firewallAction.equals(FirewallAction.DENY)
					&& firewallActionLogAction != null) {
				firewallActionLogAction.invoke(
						ObjectLogMessageHelper.objectLogMessage(
								this,
								"Inbound UDP packet denied based on the "
								+ "following rule and context: rule: %s "
								+ "context: %s",
								rule,
								ruleContext));				
			}
			return FirewallAction.ALLOW.equals(firewallAction);
		}
		
		private boolean canSendDatagramPacket() {
			return !HostAddress.isAllZerosHostAddress(
					this.udpRelayServer.getClientAddress())
					&& this.udpRelayServer.getClientPort() != 0;
		}

		private boolean hasInboundRuleCondition(final Rule rule) {
			if (rule.hasRuleCondition(
					Socks5RuleConditionSpecConstants.SOCKS5_UDP_INBOUND_DESIRED_DESTINATION_ADDRESS)) {
				return true;
			}
			if (rule.hasRuleCondition(
					Socks5RuleConditionSpecConstants.SOCKS5_UDP_INBOUND_DESIRED_DESTINATION_PORT)) {
				return true;
			}
			if (rule.hasRuleCondition(
					Socks5RuleConditionSpecConstants.SOCKS5_UDP_INBOUND_SOURCE_ADDRESS)) {
				return true;
			}
			if (rule.hasRuleCondition(
					Socks5RuleConditionSpecConstants.SOCKS5_UDP_INBOUND_SOURCE_PORT)) {
				return true;
			}
			return false;
		}
		
		private DatagramPacket newDatagramPacket(
				final UdpRequest header) {
			byte[] headerBytes = header.toByteArray();
			InetAddress inetAddress = null;
			try {
				inetAddress = InetAddress.getByName(
						this.udpRelayServer.getClientAddress());
			} catch (IOException e) {
				this.logger.error( 
						ObjectLogMessageHelper.objectLogMessage(
								this, 
								"Error in determining the IP address from the "
								+ "client"), 
						e);
				return null;
			}
			int inetPort = this.udpRelayServer.getClientPort();
			return new DatagramPacket(
					headerBytes, headerBytes.length, inetAddress, inetPort);
		}
		
		private RuleContext newInboundRuleContext(
				final RuleContext rlContext,
				final String peerAddr,
				final int peerPrt,
				final String clientAddr, 
				final int clientPrt) {
			RuleContext inboundRuleContext = new RuleContext(rlContext);
			inboundRuleContext.putRuleArgValue(
					Socks5RuleArgSpecConstants.SOCKS5_UDP_INBOUND_DESIRED_DESTINATION_ADDRESS, 
					clientAddr);
			inboundRuleContext.putRuleArgValue(
					Socks5RuleArgSpecConstants.SOCKS5_UDP_INBOUND_DESIRED_DESTINATION_PORT, 
					Port.valueOf(clientPrt));
			inboundRuleContext.putRuleArgValue(
					Socks5RuleArgSpecConstants.SOCKS5_UDP_INBOUND_SOURCE_ADDRESS, 
					peerAddr);
			inboundRuleContext.putRuleArgValue(
					Socks5RuleArgSpecConstants.SOCKS5_UDP_INBOUND_SOURCE_PORT, 
					Port.valueOf(peerPrt));
			return inboundRuleContext;
		}
		
		private UdpRequest newUdpRequest(
				final DatagramPacket packet) {
			String address = packet.getAddress().getHostAddress();
			int port = packet.getPort();
            return UdpRequest.newInstance(
					UnsignedByte.valueOf(0),
					Address.newInstanceFrom(address),
					Port.valueOf(port),
					Arrays.copyOfRange(
							packet.getData(),
							packet.getOffset(),
							packet.getLength()));
		}
		
		@Override
		public void run() {
			while (true) {
				try {
					byte[] buffer = new byte[this.bufferSize];
					DatagramPacket packet = new DatagramPacket(
							buffer, buffer.length);
					IOException ioe = null;
					try {
						this.peerFacingDatagramSocket.receive(packet);
						this.udpRelayServer.setIdleStartTime(
								System.currentTimeMillis());
					} catch (IOException e) {
						ioe = e;
					}
					if (ioe != null) {
						if (ThrowableHelper.isOrHasInstanceOf(
								ioe, SocketException.class)) {
							// socket closed
							break;
						} else if (ThrowableHelper.isOrHasInstanceOf(
								ioe, SocketTimeoutException.class)) {
							long idleStartTime = 
									this.udpRelayServer.getIdleStartTime();
							long timeSinceIdleStartTime = 
									System.currentTimeMillis() - idleStartTime;
							if (timeSinceIdleStartTime >= this.idleTimeout) {
								this.logger.trace(
										ObjectLogMessageHelper.objectLogMessage(
												this, 
												"Timeout reached for idle relay!"));							
								break;
							}
							continue;
						} else {
							this.logger.error( 
									ObjectLogMessageHelper.objectLogMessage(
											this, 
											"Error in receiving the packet from "
											+ "the peer"), 
									ioe);
							continue;
						}
					}
					this.logger.trace(ObjectLogMessageHelper.objectLogMessage(
							this, 
							"Packet data received: %s byte(s)",
							packet.getLength()));
					RuleContext inboundRuleContext = this.newInboundRuleContext(
							this.ruleContext,
							packet.getAddress().getHostAddress(),
							packet.getPort(),
							this.udpRelayServer.getClientAddress(), 
							this.udpRelayServer.getClientPort());
					Rule applicableRule = this.rules.firstAppliesTo(
							inboundRuleContext);
					if (applicableRule == null) {
						this.logger.error(
								ObjectLogMessageHelper.objectLogMessage(
										this,
										"No applicable rule found based on "
										+ "the following context: %s",
										inboundRuleContext));
						continue;
					}
					if (!this.canAllowDatagramPacket(
							applicableRule, inboundRuleContext)) {
						continue;
					}
					if (!this.canSendDatagramPacket()) {
						continue;
					}
					UdpRequest udpRequest = this.newUdpRequest(packet);
					this.logger.trace(ObjectLogMessageHelper.objectLogMessage(
							this, udpRequest.toString()));
					packet = this.newDatagramPacket(udpRequest);
					if (packet == null) {
						continue;
					}
					ioe = null;
					try {
						this.clientFacingDatagramSocket.send(packet);
					} catch (IOException e) {
						ioe = e;
					}
					if (ioe != null) {
						if (ThrowableHelper.isOrHasInstanceOf(
								ioe, SocketException.class)) {
							// socket closed
							break;
						} else {
							this.logger.error( 
									ObjectLogMessageHelper.objectLogMessage(
											this, 
											"Error in sending the packet to the "
											+ "client"), 
									ioe);
						}
					}
				} catch (Throwable t) {
					this.logger.error( 
							ObjectLogMessageHelper.objectLogMessage(
									this, 
									"Error occurred in the process of "
									+ "relaying of a packet from the peer to "
									+ "the client"), 
							t);
				}
			}
			this.udpRelayServer.stopIfNotStopped();				
		}
		
	}
	
	private static final class OutboundPacketsWorker extends PacketsWorker {
		
		private final Logger logger;
		
		public OutboundPacketsWorker(final UdpRelayServer server) {
			super(server);
			this.logger = LoggerFactory.getLogger(OutboundPacketsWorker.class);
		}
		
		private boolean canAcceptDatagramPacket(final DatagramPacket packet) {
			String address = packet.getAddress().getHostAddress();
			int port = packet.getPort();
			String clientAddr = this.udpRelayServer.getClientAddress();
			if (HostAddress.isAllZerosHostAddress(clientAddr)) {
				this.udpRelayServer.setClientAddress(address);
			} else {
				InetAddress clientInetAddr = null;
				try {
					clientInetAddr = InetAddress.getByName(clientAddr);
				} catch (IOException e) {
					this.logger.error( 
							ObjectLogMessageHelper.objectLogMessage(
									this, 
									"Error in determining the IP address from "
									+ "the client"), 
							e);
					return false;
				}
				InetAddress inetAddr = null;
				try {
					inetAddr = InetAddress.getByName(address);
				} catch (IOException e) {
					this.logger.error( 
							ObjectLogMessageHelper.objectLogMessage(
									this, 
									"Error in determining the IP address from "
									+ "the client"), 
							e);
					return false;
				}
				if ((!clientInetAddr.isLoopbackAddress() 
						|| !inetAddr.isLoopbackAddress())
						&& !clientInetAddr.equals(inetAddr)) {
					return false;
				}
			}
			int clientPrt = this.udpRelayServer.getClientPort();
			if (clientPrt == 0) {
				this.udpRelayServer.setClientPort(port);
			} else {
				if (clientPrt != port) {
					return false;
				}
			}
			return true;
		}
		
		private boolean canAllowDatagramPacket(
				final Rule rule, final RuleContext ruleContext) {
			if (!this.hasOutboundRuleCondition(rule)) {
				return true;
			}
			FirewallAction firewallAction =	rule.getLastRuleResultValue(
					GeneralRuleResultSpecConstants.FIREWALL_ACTION);
			if (firewallAction == null) {
				return false;
			}
			LogAction firewallActionLogAction =	rule.getLastRuleResultValue(
					GeneralRuleResultSpecConstants.FIREWALL_ACTION_LOG_ACTION);
			if (firewallAction.equals(FirewallAction.ALLOW)
					&& firewallActionLogAction != null) {
				firewallActionLogAction.invoke(
						ObjectLogMessageHelper.objectLogMessage(
								this,
								"Outbound UDP packet allowed based on the "
								+ "following rule and context: rule: %s "
								+ "context: %s",
								rule,
								ruleContext));				
			} else if (firewallAction.equals(FirewallAction.DENY)
					&& firewallActionLogAction != null) {
				firewallActionLogAction.invoke(
						ObjectLogMessageHelper.objectLogMessage(
								this,
								"Outbound UDP packet denied based on the "
								+ "following rule and context: rule: %s "
								+ "context: %s",
								rule,
								ruleContext));				
			}
			return FirewallAction.ALLOW.equals(firewallAction);
		}
		
		private boolean hasOutboundRuleCondition(final Rule rule) {
			if (rule.hasRuleCondition(
					Socks5RuleConditionSpecConstants.SOCKS5_UDP_OUTBOUND_DESIRED_DESTINATION_ADDRESS)) {
				return true;
			}
			if (rule.hasRuleCondition(
					Socks5RuleConditionSpecConstants.SOCKS5_UDP_OUTBOUND_DESIRED_DESTINATION_PORT)) {
				return true;
			}
			if (rule.hasRuleCondition(
					Socks5RuleConditionSpecConstants.SOCKS5_UDP_OUTBOUND_SOURCE_ADDRESS)) {
				return true;
			}
			if (rule.hasRuleCondition(
					Socks5RuleConditionSpecConstants.SOCKS5_UDP_OUTBOUND_SOURCE_PORT)) {
				return true;
			}
			return false;
		}
		
		private DatagramPacket newDatagramPacket(
				final UdpRequest udpRequest) {
			byte[] userData = udpRequest.getUserData();
			InetAddress inetAddress = null;
			try {
				inetAddress = this.hostResolver.resolve(
						udpRequest.getDesiredDestinationAddress().toString());
			} catch (IOException e) {
				this.logger.error( 
						ObjectLogMessageHelper.objectLogMessage(
								this, 
								"Error in determining the IP address from the "
								+ "peer"), 
						e);
				return null;
			}
			int inetPort = udpRequest.getDesiredDestinationPort().intValue();
			return new DatagramPacket(
					userData, userData.length, inetAddress, inetPort);
		}
		
		private RuleContext newOutboundRuleContext(
				final RuleContext rlContext,
				final String clientAddr,
				final int clientPrt,
				final String peerAddr, 
				final int peerPrt) {
			RuleContext outboundRuleContext = new RuleContext(rlContext);
			outboundRuleContext.putRuleArgValue(
					Socks5RuleArgSpecConstants.SOCKS5_UDP_OUTBOUND_DESIRED_DESTINATION_ADDRESS, 
					peerAddr);
			outboundRuleContext.putRuleArgValue(
					Socks5RuleArgSpecConstants.SOCKS5_UDP_OUTBOUND_DESIRED_DESTINATION_PORT, 
					Port.valueOf(peerPrt));
			outboundRuleContext.putRuleArgValue(
					Socks5RuleArgSpecConstants.SOCKS5_UDP_OUTBOUND_SOURCE_ADDRESS, 
					clientAddr);
			outboundRuleContext.putRuleArgValue(
					Socks5RuleArgSpecConstants.SOCKS5_UDP_OUTBOUND_SOURCE_PORT, 
					Port.valueOf(clientPrt));
			return outboundRuleContext;
		}
		
		private UdpRequest newUdpRequest(
				final DatagramPacket packet) {
			UdpRequest udpRequest = null;
			try {
				udpRequest = UdpRequest.newInstanceFrom(
						Arrays.copyOfRange(
								packet.getData(),
								packet.getOffset(),
								packet.getLength()));
			} catch (IllegalArgumentException e) {
				this.logger.error( 
						ObjectLogMessageHelper.objectLogMessage(
								this, 
								"Error in parsing the UDP request from "
								+ "the client"), 
						e);
				return null;
			}
			return udpRequest;
		}
		
		@Override
		public void run() {
			while (true) {
				try {
					byte[] buffer = new byte[this.bufferSize];
					DatagramPacket packet = new DatagramPacket(
							buffer, buffer.length);
					IOException ioe = null;
					try {
						this.clientFacingDatagramSocket.receive(packet);
						this.udpRelayServer.setIdleStartTime(
								System.currentTimeMillis());
					} catch (IOException e) {
						ioe = e;
					}
					if (ioe != null) {
						if (ThrowableHelper.isOrHasInstanceOf(
								ioe, SocketException.class)) {
							// socket closed
							break;							
						} else if (ThrowableHelper.isOrHasInstanceOf(
								ioe, SocketTimeoutException.class)) {
							long idleStartTime = 
									this.udpRelayServer.getIdleStartTime();
							long timeSinceIdleStartTime = 
									System.currentTimeMillis() - idleStartTime;
							if (timeSinceIdleStartTime >= this.idleTimeout) {
								this.logger.trace(
										ObjectLogMessageHelper.objectLogMessage(
												this, 
												"Timeout reached for idle relay!"));
								break;
							}
							continue;							
						} else {
							this.logger.error( 
									ObjectLogMessageHelper.objectLogMessage(
											this, 
											"Error in receiving packet from the "
											+ "client"), 
									ioe);
							continue;							
						}
					}
					this.logger.trace(ObjectLogMessageHelper.objectLogMessage(
							this, 
							"Packet data received: %s byte(s)",
							packet.getLength()));					
					if (!this.canAcceptDatagramPacket(packet)) {
						continue;
					}
					UdpRequest udpRequest = this.newUdpRequest(packet);
					if (udpRequest == null) {
						continue;
					}
					this.logger.trace(ObjectLogMessageHelper.objectLogMessage(
							this, udpRequest.toString(), new Object[]{}));
					if (udpRequest.getCurrentFragmentNumber().intValue() != 0) {
						continue;
					}
					RuleContext outboundRuleContext =
							this.newOutboundRuleContext(
									this.ruleContext,
									this.udpRelayServer.getClientAddress(),
									this.udpRelayServer.getClientPort(),
									udpRequest.getDesiredDestinationAddress().toString(),
									udpRequest.getDesiredDestinationPort().intValue());
					Rule applicableRule = this.rules.firstAppliesTo(
							outboundRuleContext);
					if (applicableRule == null) {
						this.logger.error(
								ObjectLogMessageHelper.objectLogMessage(
										this,
										"No applicable rule found based on "
										+ "the following context: %s",
										outboundRuleContext));
						continue;
					}					
					if (!this.canAllowDatagramPacket(
							applicableRule,	outboundRuleContext)) {
						continue;
					}
					packet = this.newDatagramPacket(udpRequest);
					if (packet == null) {
						continue;
					}
					ioe = null;
					try {
						this.peerFacingDatagramSocket.send(packet);
					} catch (IOException e) {
						ioe = e;
					}
					if (ioe != null) {
						if (ThrowableHelper.isOrHasInstanceOf(
								ioe, SocketException.class)) {
							// socket closed
							break;							
						} else {
							this.logger.error( 
									ObjectLogMessageHelper.objectLogMessage(
											this, 
											"Error in sending the packet to the "
											+ "peer"), 
									ioe);							
						}
					}
				} catch (Throwable t) {
					this.logger.error( 
							ObjectLogMessageHelper.objectLogMessage(
									this, 
									"Error occurred in the process of "
									+ "relaying of a packet from the client to "
									+ "the peer"), 
							t);
				}
			}
			this.udpRelayServer.stopIfNotStopped();				
		}

	}
	
	private static abstract class PacketsWorker implements Runnable {
		
		protected final int bufferSize;
		protected final DatagramSocket clientFacingDatagramSocket;
		protected final HostResolver hostResolver;
		protected final int idleTimeout;
		protected final DatagramSocket peerFacingDatagramSocket;
		protected final RuleContext ruleContext;
		protected final Rules rules;
		protected final UdpRelayServer udpRelayServer;

		public PacketsWorker(final UdpRelayServer server) {
			this.bufferSize = server.bufferSize;
			this.clientFacingDatagramSocket = server.clientFacingDatagramSocket;
			this.hostResolver = server.hostResolver;
			this.idleTimeout = server.idleTimeout;
			this.peerFacingDatagramSocket = server.peerFacingDatagramSocket;			
			this.ruleContext = server.ruleContext;
			this.rules = server.rules;
			this.udpRelayServer = server;
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
	private final AtomicReference<String> clientAddress;
	private final DatagramSocket clientFacingDatagramSocket;	
	private final AtomicInteger clientPort;
	private ExecutorService executor;
	private final HostResolver hostResolver;
	private final AtomicLong idleStartTime;
	private final int idleTimeout;
	private final DatagramSocket peerFacingDatagramSocket;
	private final RuleContext ruleContext;
	private final Rules rules;
	private final AtomicReference<State> state;
	
	private UdpRelayServer(final Builder builder) {
		this.bufferSize = builder.bufferSize;
		this.clientAddress = new AtomicReference<String>(builder.clientAddress);
		this.clientFacingDatagramSocket = builder.clientFacingDatagramSocket;
		this.clientPort = new AtomicInteger(builder.clientPort);
		this.executor = null;
		this.hostResolver = builder.hostResolver;
		this.idleStartTime = new AtomicLong(0L);
		this.idleTimeout = builder.idleTimeout;
		this.peerFacingDatagramSocket = builder.peerFacingDatagramSocket;
		this.ruleContext = builder.ruleContext;
		this.rules = builder.rules;
		this.state = new AtomicReference<State>(State.STOPPED);
	}
	
	private String getClientAddress() {
		return this.clientAddress.get();
	}
	
	private int getClientPort() {
		return this.clientPort.get();
	}
	
	private long getIdleStartTime() {
		return this.idleStartTime.get();
	}
	
	public State getState() {
		return this.state.get();
	}
	
	private void setClientAddress(final String address) {
		this.clientAddress.set(address);
	}
	
	private void setClientPort(final int port) {
		this.clientPort.set(port);
	}
	
	private void setIdleStartTime(final long time) {
		this.idleStartTime.set(time);
	}
	
	public void start() throws IOException {
		if (!this.state.compareAndSet(State.STOPPED, State.STARTED)) {
			throw new IllegalStateException("UdpRelayServer already started");
		}
		this.idleStartTime.set(System.currentTimeMillis());
		this.executor =
				ExecutorsHelper.newVirtualThreadPerTaskExecutorOrDefault(
						ExecutorsHelper.newFixedThreadPoolBuilder(2));
		this.executor.execute(new InboundPacketsWorker(this));
		this.executor.execute(new OutboundPacketsWorker(this));
	}
	
	public void stop() {
		if (!this.state.compareAndSet(State.STARTED, State.STOPPED)) {
			throw new IllegalStateException("UdpRelayServer already stopped");
		}
		this.idleStartTime.set(0L);
		this.executor.shutdownNow();
		this.executor = null;
	}
	
	private void stopIfNotStopped() {
		if (!this.state.get().equals(State.STOPPED)) {
			try {
				this.stop();
			} catch (IllegalStateException e) {
				// the other thread stopped the UDP relay server
			}
		}
	}
	
}
