package com.github.jh3nd3rs0n.jargyle.server.internal.server.socks5;

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
import com.github.jh3nd3rs0n.jargyle.internal.logging.ObjectLogMessageHelper;
import com.github.jh3nd3rs0n.jargyle.internal.net.AddressAndPortHelper;
import com.github.jh3nd3rs0n.jargyle.internal.net.AddressHelper;
import com.github.jh3nd3rs0n.jargyle.server.FirewallAction;
import com.github.jh3nd3rs0n.jargyle.server.GeneralRuleResultSpecConstants;
import com.github.jh3nd3rs0n.jargyle.server.LogAction;
import com.github.jh3nd3rs0n.jargyle.server.Rule;
import com.github.jh3nd3rs0n.jargyle.server.RuleContext;
import com.github.jh3nd3rs0n.jargyle.server.Socks5RuleArgSpecConstants;
import com.github.jh3nd3rs0n.jargyle.server.Socks5RuleConditionSpecConstants;
import com.github.jh3nd3rs0n.jargyle.server.internal.server.Rules;
import com.github.jh3nd3rs0n.jargyle.server.internal.server.ThrowableHelper;
import com.github.jh3nd3rs0n.jargyle.transport.socks5.UdpRequestHeader;

final class UdpRelayServer {
	
	public static final class Builder {

		public static final int DEFAULT_BUFFER_SIZE = 32768;
		public static final HostResolver DEFAULT_HOST_RESOLVER = new HostResolver();
		public static final int DEFAULT_IDLE_TIMEOUT = 60000;
		public static final RuleContext DEFAULT_RULE_CONTEXT = new RuleContext();
		public static final Rules DEFAULT_RULES = Rules.newInstance(Rule.getDefault());
		
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
			if (AddressHelper.isAllZerosAddress(clientAddr)) {
				throw new IllegalArgumentException(
						"client address must not be all zeros");
			}
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
		
		private static final Logger LOGGER = LoggerFactory.getLogger(
				InboundPacketsWorker.class);
		
		public InboundPacketsWorker(final PacketsWorkerContext context) {
			super(context);
		}
		
		private boolean canForwardDatagramPacket(
				final Rule applicableRule,
				final RuleContext inboundRuleContext) {
			if (applicableRule == null) {
				return false;
			}
			boolean hasDesiredDestinationAddressRuleCondition =
					applicableRule.hasRuleCondition(
							Socks5RuleConditionSpecConstants.SOCKS5_UDP_INBOUND_DESIRED_DESTINATION_ADDRESS);
			boolean hasDesiredDestinationPortRuleCondition =
					applicableRule.hasRuleCondition(
							Socks5RuleConditionSpecConstants.SOCKS5_UDP_INBOUND_DESIRED_DESTINATION_PORT);
			boolean hasSourceAddressRuleCondition =
					applicableRule.hasRuleCondition(
							Socks5RuleConditionSpecConstants.SOCKS5_UDP_INBOUND_SOURCE_ADDRESS);
			boolean hasSourcePortRuleCondition =
					applicableRule.hasRuleCondition(
							Socks5RuleConditionSpecConstants.SOCKS5_UDP_INBOUND_SOURCE_PORT);
			if (!hasDesiredDestinationAddressRuleCondition
					&& !hasDesiredDestinationPortRuleCondition
					&& !hasSourceAddressRuleCondition
					&& !hasSourcePortRuleCondition) {
				return true;
			}
			FirewallAction firewallAction = 
					applicableRule.getLastRuleResultValue(
							GeneralRuleResultSpecConstants.FIREWALL_ACTION);
			if (firewallAction == null) {
				return false;
			}
			LogAction firewallActionLogAction = 
					applicableRule.getLastRuleResultValue(
							GeneralRuleResultSpecConstants.FIREWALL_ACTION_LOG_ACTION);
			String desiredDestinationAddress = 
					inboundRuleContext.getRuleArgValue(
							Socks5RuleArgSpecConstants.SOCKS5_UDP_INBOUND_DESIRED_DESTINATION_ADDRESS);
			Port desiredDestinationPort =
					inboundRuleContext.getRuleArgValue(
							Socks5RuleArgSpecConstants.SOCKS5_UDP_INBOUND_DESIRED_DESTINATION_PORT);
			String sourceAddress =
					inboundRuleContext.getRuleArgValue(
							Socks5RuleArgSpecConstants.SOCKS5_UDP_INBOUND_SOURCE_ADDRESS);
			Port sourcePort =
					inboundRuleContext.getRuleArgValue(
							Socks5RuleArgSpecConstants.SOCKS5_UDP_INBOUND_SOURCE_PORT);
			if (firewallAction.equals(FirewallAction.ALLOW)
					&& firewallActionLogAction != null) {
				firewallActionLogAction.invoke(
						LOGGER, 
						ObjectLogMessageHelper.objectLogMessage(
								this,
								"UDP inbound source address and port (%s) to "
								+ "UDP inbound desired destination address and "
								+ "port (%s) allowed based on the following "
								+ "rule and context: rule: %s context: %s",
								AddressAndPortHelper.toString(
										sourceAddress, 
										sourcePort.intValue()),
								AddressAndPortHelper.toString(
										desiredDestinationAddress,
										desiredDestinationPort.intValue()),
								applicableRule,
								inboundRuleContext));				
			} else if (firewallAction.equals(FirewallAction.DENY)
					&& firewallActionLogAction != null) {
				firewallActionLogAction.invoke(
						LOGGER, 
						ObjectLogMessageHelper.objectLogMessage(
								this,
								"UDP inbound source address and port (%s) to "
								+ "UDP inbound desired destination address and "
								+ "port (%s) denied based on the following "
								+ "rule and context: rule: %s context: %s",
								AddressAndPortHelper.toString(
										sourceAddress, 
										sourcePort.intValue()),
								AddressAndPortHelper.toString(
										desiredDestinationAddress,
										desiredDestinationPort.intValue()),
								applicableRule,
								inboundRuleContext));				
			}
			return FirewallAction.ALLOW.equals(firewallAction);
		}

		private DatagramPacket newDatagramPacket(
				final UdpRequestHeader header) {
			byte[] headerBytes = header.toByteArray();
			InetAddress inetAddress = null;
			try {
				inetAddress = InetAddress.getByName(this.clientAddress);
			} catch (IOException e) {
				LOGGER.error( 
						ObjectLogMessageHelper.objectLogMessage(
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
		
		private RuleContext newInboundRuleContext(
				final String peerAddr,
				final int peerPrt,
				final String clientAddr,
				final int clientPrt) {
			RuleContext inboundRuleContext = new RuleContext(this.ruleContext);
			inboundRuleContext.putRuleArgValue(
					Socks5RuleArgSpecConstants.SOCKS5_UDP_INBOUND_DESIRED_DESTINATION_ADDRESS, 
					clientAddr);
			inboundRuleContext.putRuleArgValue(
					Socks5RuleArgSpecConstants.SOCKS5_UDP_INBOUND_DESIRED_DESTINATION_PORT, 
					Port.newInstance(clientPrt));
			inboundRuleContext.putRuleArgValue(
					Socks5RuleArgSpecConstants.SOCKS5_UDP_INBOUND_SOURCE_ADDRESS, 
					peerAddr);
			inboundRuleContext.putRuleArgValue(
					Socks5RuleArgSpecConstants.SOCKS5_UDP_INBOUND_SOURCE_PORT, 
					Port.newInstance(peerPrt));
			return inboundRuleContext;
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
					IOException ioe = null;
					try {
						this.peerFacingDatagramSocket.receive(packet);
						this.packetsWorkerContext.setIdleStartTime(
								System.currentTimeMillis());
					} catch (IOException e) {
						ioe = e;
					}
					if (ioe != null) {
						if (ioe instanceof SocketException
								|| ThrowableHelper.getRecentCause(
										ioe, SocketException.class) != null) {
							// socket closed
							break;
						} else if (ioe instanceof SocketTimeoutException 
								|| ThrowableHelper.getRecentCause(
										ioe, SocketTimeoutException.class) != null) {
							long idleStartTime = 
									this.packetsWorkerContext.getIdleStartTime();
							long timeSinceIdleStartTime = 
									System.currentTimeMillis() - idleStartTime;
							if (timeSinceIdleStartTime >= this.idleTimeout) {
								LOGGER.trace(
										ObjectLogMessageHelper.objectLogMessage(
												this, 
												"Timeout reached for idle relay!"));							
								break;
							}
							continue;
						} else {
							LOGGER.error( 
									ObjectLogMessageHelper.objectLogMessage(
											this, 
											"Error in receiving the packet from "
											+ "the peer"), 
									ioe);
							continue;
						}
					}
					LOGGER.trace(ObjectLogMessageHelper.objectLogMessage(
							this, 
							"Packet data received: %s byte(s)",
							packet.getLength()));
					RuleContext inboundRuleContext = this.newInboundRuleContext(
							packet.getAddress().getHostAddress(),
							packet.getPort(),
							this.clientAddress,
							this.packetsWorkerContext.getClientPort());
					Rule applicableRule = this.rules.firstAppliesTo(
							inboundRuleContext);
					if (!this.canForwardDatagramPacket(
							applicableRule, inboundRuleContext)) {
						continue;
					}
					UdpRequestHeader header = this.newUdpRequestHeader(packet);
					LOGGER.trace(ObjectLogMessageHelper.objectLogMessage(
							this, header.toString()));
					packet = this.newDatagramPacket(header);
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
						if (ioe instanceof SocketException
								|| ThrowableHelper.getRecentCause(
										ioe, SocketException.class) != null) {
							// socket closed
							break;
						} else {
							LOGGER.error( 
									ObjectLogMessageHelper.objectLogMessage(
											this, 
											"Error in sending the packet to the "
											+ "client"), 
									ioe);
						}
					}
				} catch (Throwable t) {
					LOGGER.error( 
							ObjectLogMessageHelper.objectLogMessage(
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
		
		private boolean canAllowDatagramPacket(final DatagramPacket packet) {
			String address = packet.getAddress().getHostAddress();
			int port = packet.getPort();
			String clientAddr = this.clientAddress;
			InetAddress clientInetAddr = null;
			try {
				clientInetAddr = InetAddress.getByName(clientAddr);
			} catch (IOException e) {
				LOGGER.error( 
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
				LOGGER.error( 
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
		
		private boolean canForwardDatagramPacket(
				final Rule applicableRule,
				final RuleContext outboundRuleContext) {
			if (applicableRule == null) {
				return false;
			}
			boolean hasDesiredDestinationAddressRuleCondition =
					applicableRule.hasRuleCondition(
							Socks5RuleConditionSpecConstants.SOCKS5_UDP_OUTBOUND_DESIRED_DESTINATION_ADDRESS);
			boolean hasDesiredDestinationPortRuleCondition =
					applicableRule.hasRuleCondition(
							Socks5RuleConditionSpecConstants.SOCKS5_UDP_OUTBOUND_DESIRED_DESTINATION_PORT);
			boolean hasSourceAddressRuleCondition =
					applicableRule.hasRuleCondition(
							Socks5RuleConditionSpecConstants.SOCKS5_UDP_OUTBOUND_SOURCE_ADDRESS);
			boolean hasSourcePortRuleCondition =
					applicableRule.hasRuleCondition(
							Socks5RuleConditionSpecConstants.SOCKS5_UDP_OUTBOUND_SOURCE_PORT);
			if (!hasDesiredDestinationAddressRuleCondition
					&& !hasDesiredDestinationPortRuleCondition
					&& !hasSourceAddressRuleCondition
					&& !hasSourcePortRuleCondition) {
				return true;
			}
			FirewallAction firewallAction = 
					applicableRule.getLastRuleResultValue(
							GeneralRuleResultSpecConstants.FIREWALL_ACTION);
			if (firewallAction == null) {
				return false;
			}
			LogAction firewallActionLogAction = 
					applicableRule.getLastRuleResultValue(
							GeneralRuleResultSpecConstants.FIREWALL_ACTION_LOG_ACTION);
			String desiredDestinationAddress = 
					outboundRuleContext.getRuleArgValue(
							Socks5RuleArgSpecConstants.SOCKS5_UDP_OUTBOUND_DESIRED_DESTINATION_ADDRESS);
			Port desiredDestinationPort =
					outboundRuleContext.getRuleArgValue(
							Socks5RuleArgSpecConstants.SOCKS5_UDP_OUTBOUND_DESIRED_DESTINATION_PORT);
			String sourceAddress =
					outboundRuleContext.getRuleArgValue(
							Socks5RuleArgSpecConstants.SOCKS5_UDP_OUTBOUND_SOURCE_ADDRESS);
			Port sourcePort =
					outboundRuleContext.getRuleArgValue(
							Socks5RuleArgSpecConstants.SOCKS5_UDP_OUTBOUND_SOURCE_PORT);
			if (firewallAction.equals(FirewallAction.ALLOW)
					&& firewallActionLogAction != null) {
				firewallActionLogAction.invoke(
						LOGGER, 
						ObjectLogMessageHelper.objectLogMessage(
								this,
								"UDP outbound source address and port (%s) to "
								+ "UDP outbound desired destination address "
								+ "and port (%s) allowed based on the "
								+ "following rule and context: rule: %s "
								+ "context: %s",
								AddressAndPortHelper.toString(
										sourceAddress, 
										sourcePort.intValue()),
								AddressAndPortHelper.toString(
										desiredDestinationAddress, 
										desiredDestinationPort.intValue()),
								applicableRule,
								outboundRuleContext));				
			} else if (firewallAction.equals(FirewallAction.DENY)
					&& firewallActionLogAction != null) {
				firewallActionLogAction.invoke(
						LOGGER, 
						ObjectLogMessageHelper.objectLogMessage(
								this,
								"UDP outbound source address and port (%s) to "
								+ "UDP outbound desired destination address "
								+ "and port (%s) denied based on the "
								+ "following rule and context: rule: %s "
								+ "context: %s",
								AddressAndPortHelper.toString(
										sourceAddress, 
										sourcePort.intValue()),
								AddressAndPortHelper.toString(
										desiredDestinationAddress, 
										desiredDestinationPort.intValue()),
								applicableRule,
								outboundRuleContext));				
			}
			return FirewallAction.ALLOW.equals(firewallAction);
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
						ObjectLogMessageHelper.objectLogMessage(
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
		
		private RuleContext newOutboundRuleContext(
				final String clientAddr,
				final int clientPrt,
				final String peerAddr,
				final int peerPrt) {
			RuleContext outboundRuleContext = new RuleContext(this.ruleContext);
			outboundRuleContext.putRuleArgValue(
					Socks5RuleArgSpecConstants.SOCKS5_UDP_OUTBOUND_DESIRED_DESTINATION_ADDRESS, 
					peerAddr);
			outboundRuleContext.putRuleArgValue(
					Socks5RuleArgSpecConstants.SOCKS5_UDP_OUTBOUND_DESIRED_DESTINATION_PORT, 
					Port.newInstance(peerPrt));
			outboundRuleContext.putRuleArgValue(
					Socks5RuleArgSpecConstants.SOCKS5_UDP_OUTBOUND_SOURCE_ADDRESS, 
					clientAddr);
			outboundRuleContext.putRuleArgValue(
					Socks5RuleArgSpecConstants.SOCKS5_UDP_OUTBOUND_SOURCE_PORT, 
					Port.newInstance(clientPrt));
			return outboundRuleContext;
		}
		
		private UdpRequestHeader newUdpRequestHeader(
				final DatagramPacket packet) {
			UdpRequestHeader header = null; 
			try {
				header = UdpRequestHeader.newInstance(packet.getData());
			} catch (IllegalArgumentException e) {
				LOGGER.error( 
						ObjectLogMessageHelper.objectLogMessage(
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
					IOException ioe = null;
					try {
						this.clientFacingDatagramSocket.receive(packet);
						this.packetsWorkerContext.setIdleStartTime(
								System.currentTimeMillis());
					} catch (IOException e) {
						ioe = e;
					}
					if (ioe != null) {
						if (ioe instanceof SocketException
								|| ThrowableHelper.getRecentCause(
										ioe, SocketException.class) != null) {
							// socket closed
							break;							
						} else if (ioe instanceof SocketTimeoutException
								|| ThrowableHelper.getRecentCause(
										ioe, SocketTimeoutException.class) != null) {
							long idleStartTime = 
									this.packetsWorkerContext.getIdleStartTime();
							long timeSinceIdleStartTime = 
									System.currentTimeMillis() - idleStartTime;
							if (timeSinceIdleStartTime >= this.idleTimeout) {
								LOGGER.trace(
										ObjectLogMessageHelper.objectLogMessage(
												this, 
												"Timeout reached for idle relay!"));
								break;
							}
							continue;							
						} else {
							LOGGER.error( 
									ObjectLogMessageHelper.objectLogMessage(
											this, 
											"Error in receiving packet from the "
											+ "client"), 
									ioe);
							continue;							
						}
					}
					LOGGER.trace(ObjectLogMessageHelper.objectLogMessage(
							this, 
							"Packet data received: %s byte(s)",
							packet.getLength()));					
					if (!this.canAllowDatagramPacket(packet)) {
						continue;
					}
					UdpRequestHeader header = this.newUdpRequestHeader(packet);
					if (header == null) {
						continue;
					}
					LOGGER.trace(ObjectLogMessageHelper.objectLogMessage(
							this, header.toString(), new Object[]{}));
					if (header.getCurrentFragmentNumber() != 0) {
						continue;
					}
					RuleContext outboundRuleContext =
							this.newOutboundRuleContext(
									this.clientAddress,
									this.packetsWorkerContext.getClientPort(),
									header.getDesiredDestinationAddress(),
									header.getDesiredDestinationPort());
					Rule applicableRule = this.rules.firstAppliesTo(
							outboundRuleContext);
					if (!this.canForwardDatagramPacket(
							applicableRule,	outboundRuleContext)) {
						continue;
					}
					packet = this.newDatagramPacket(header);
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
						if (ioe instanceof SocketException
								|| ThrowableHelper.getRecentCause(
										ioe, SocketException.class) != null) {
							// socket closed
							break;							
						} else {
							LOGGER.error( 
									ObjectLogMessageHelper.objectLogMessage(
											this, 
											"Error in sending the packet to the "
											+ "peer"), 
									ioe);							
						}
					}
				} catch (Throwable t) {
					LOGGER.error( 
							ObjectLogMessageHelper.objectLogMessage(
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
		protected final PacketsWorkerContext packetsWorkerContext;
		protected final DatagramSocket peerFacingDatagramSocket;
		protected final RuleContext ruleContext;
		protected final Rules rules;

		public PacketsWorker(final PacketsWorkerContext context) {
			this.bufferSize = context.getBufferSize();
			this.clientAddress = context.getClientAddress();
			this.clientFacingDatagramSocket = 
					context.getClientFacingDatagramSocket();
			this.hostResolver = context.getHostResolver();
			this.idleTimeout = context.getIdleTimeout();
			this.packetsWorkerContext = context;
			this.peerFacingDatagramSocket = 
					context.getPeerFacingDatagramSocket();
			this.ruleContext = context.getRuleContext();
			this.rules = context.getRules();
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
		
		public final DatagramSocket getPeerFacingDatagramSocket() {
			return this.peerFacingDatagramSocket;
		}
		
		public final RuleContext getRuleContext() {
			return this.udpRelayServer.ruleContext;
		}
		
		public final Rules getRules() {
			return this.udpRelayServer.rules;
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
	private final DatagramSocket peerFacingDatagramSocket;
	private final RuleContext ruleContext;
	private final Rules rules;
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
		this.peerFacingDatagramSocket = builder.peerFacingDatagramSocket;
		this.ruleContext = builder.ruleContext;
		this.rules = builder.rules;
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
