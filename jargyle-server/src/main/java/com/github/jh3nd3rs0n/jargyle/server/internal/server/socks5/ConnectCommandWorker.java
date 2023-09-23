package com.github.jh3nd3rs0n.jargyle.server.internal.server.socks5;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.net.BindException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.jh3nd3rs0n.jargyle.client.HostResolver;
import com.github.jh3nd3rs0n.jargyle.client.NetObjectFactory;
import com.github.jh3nd3rs0n.jargyle.common.lang.PositiveInteger;
import com.github.jh3nd3rs0n.jargyle.common.net.Host;
import com.github.jh3nd3rs0n.jargyle.common.net.Port;
import com.github.jh3nd3rs0n.jargyle.common.net.PortRange;
import com.github.jh3nd3rs0n.jargyle.common.net.PortRanges;
import com.github.jh3nd3rs0n.jargyle.common.net.SocketSetting;
import com.github.jh3nd3rs0n.jargyle.common.net.SocketSettings;
import com.github.jh3nd3rs0n.jargyle.internal.lang.ThrowableHelper;
import com.github.jh3nd3rs0n.jargyle.internal.logging.ObjectLogMessageHelper;
import com.github.jh3nd3rs0n.jargyle.server.GeneralRuleResultSpecConstants;
import com.github.jh3nd3rs0n.jargyle.server.GeneralSettingSpecConstants;
import com.github.jh3nd3rs0n.jargyle.server.Rule;
import com.github.jh3nd3rs0n.jargyle.server.RuleContext;
import com.github.jh3nd3rs0n.jargyle.server.Socks5RuleResultSpecConstants;
import com.github.jh3nd3rs0n.jargyle.server.Socks5SettingSpecConstants;
import com.github.jh3nd3rs0n.jargyle.server.internal.net.BandwidthLimitedSocket;
import com.github.jh3nd3rs0n.jargyle.server.internal.server.Relay;
import com.github.jh3nd3rs0n.jargyle.transport.socks5.Reply;
import com.github.jh3nd3rs0n.jargyle.transport.socks5.Socks5Reply;
import com.github.jh3nd3rs0n.jargyle.transport.socks5.Socks5Request;

final class ConnectCommandWorker extends TcpBasedCommandWorker {

	private final Logger logger;
	
	public ConnectCommandWorker(
			final Socks5Worker socks5Worker, 
			final MethodSubnegotiationResults methSubnegotiationResults, 
			final Socks5Request socks5Req) {
		super(socks5Worker, methSubnegotiationResults, socks5Req);
		this.logger = LoggerFactory.getLogger(ConnectCommandWorker.class);
	}
	
	private boolean canPrepareServerFacingSocket() {
		Boolean b = this.getApplicableRule().getLastRuleResultValue(
				Socks5RuleResultSpecConstants.SOCKS5_ON_CONNECT_PREPARE_SERVER_FACING_SOCKET);
		if (b != null) {
			return b.booleanValue();
		}
		b = this.getSettings().getLastValue(
				Socks5SettingSpecConstants.SOCKS5_ON_CONNECT_PREPARE_SERVER_FACING_SOCKET);
		return b.booleanValue();
	}

	private boolean configureServerFacingSocket(
			final Socket serverFacingSocket) {
		SocketSettings socketSettings = this.getServerFacingSocketSettings();
		try {
			socketSettings.applyTo(serverFacingSocket);
		} catch (UnsupportedOperationException e) {
			this.logger.error( 
					ObjectLogMessageHelper.objectLogMessage(
							this, "Error in setting the server-facing socket"), 
					e);
			Socks5Reply socks5Rep = Socks5Reply.newFailureInstance(
					Reply.GENERAL_SOCKS_SERVER_FAILURE);
			this.sendSocks5Reply(socks5Rep);
			return false;			
		} catch (SocketException e) {
			this.logger.error( 
					ObjectLogMessageHelper.objectLogMessage(
							this, "Error in setting the server-facing socket"), 
					e);
			Socks5Reply socks5Rep = Socks5Reply.newFailureInstance(
					Reply.GENERAL_SOCKS_SERVER_FAILURE);
			this.sendSocks5Reply(socks5Rep);
			return false;
		}
		return true;
	}
	
	private int getRelayBufferSize() {
		Rule applicableRule = this.getApplicableRule();
		PositiveInteger relayBufferSize =
				applicableRule.getLastRuleResultValue(
						Socks5RuleResultSpecConstants.SOCKS5_ON_CONNECT_RELAY_BUFFER_SIZE);
		if (relayBufferSize != null) {
			return relayBufferSize.intValue();
		}
		relayBufferSize = applicableRule.getLastRuleResultValue(
				Socks5RuleResultSpecConstants.SOCKS5_ON_COMMAND_RELAY_BUFFER_SIZE);
		if (relayBufferSize != null) {
			return relayBufferSize.intValue();
		}
		relayBufferSize = this.getSettings().getLastValue(
				Socks5SettingSpecConstants.SOCKS5_ON_CONNECT_RELAY_BUFFER_SIZE);
		if (relayBufferSize != null) {
			return relayBufferSize.intValue();
		}
		relayBufferSize = this.getSettings().getLastValue(
				Socks5SettingSpecConstants.SOCKS5_ON_COMMAND_RELAY_BUFFER_SIZE);
		return relayBufferSize.intValue();
	}
	
	private int getRelayIdleTimeout() {
		Rule applicableRule = this.getApplicableRule();
		PositiveInteger relayIdleTimeout =
				applicableRule.getLastRuleResultValue(
						Socks5RuleResultSpecConstants.SOCKS5_ON_CONNECT_RELAY_IDLE_TIMEOUT);
		if (relayIdleTimeout != null) {
			return relayIdleTimeout.intValue();
		}
		relayIdleTimeout = applicableRule.getLastRuleResultValue(
				Socks5RuleResultSpecConstants.SOCKS5_ON_COMMAND_RELAY_IDLE_TIMEOUT);
		if (relayIdleTimeout != null) {
			return relayIdleTimeout.intValue();
		}
		relayIdleTimeout = this.getSettings().getLastValue(
				Socks5SettingSpecConstants.SOCKS5_ON_CONNECT_RELAY_IDLE_TIMEOUT);
		if (relayIdleTimeout != null) {
			return relayIdleTimeout.intValue();
		}
		relayIdleTimeout = this.getSettings().getLastValue(
				Socks5SettingSpecConstants.SOCKS5_ON_COMMAND_RELAY_IDLE_TIMEOUT);
		return relayIdleTimeout.intValue();
	}
	
	private Integer getRelayInboundBandwidthLimit() {
		Rule applicableRule = this.getApplicableRule();
		PositiveInteger relayInboundBandwidthLimit =
				applicableRule.getLastRuleResultValue(
						Socks5RuleResultSpecConstants.SOCKS5_ON_CONNECT_RELAY_INBOUND_BANDWIDTH_LIMIT);
		if (relayInboundBandwidthLimit != null) {
			return Integer.valueOf(relayInboundBandwidthLimit.intValue());
		}
		relayInboundBandwidthLimit = applicableRule.getLastRuleResultValue(
				Socks5RuleResultSpecConstants.SOCKS5_ON_COMMAND_RELAY_INBOUND_BANDWIDTH_LIMIT);
		if (relayInboundBandwidthLimit != null) {
			return Integer.valueOf(relayInboundBandwidthLimit.intValue());
		}
		relayInboundBandwidthLimit = this.getSettings().getLastValue(
				Socks5SettingSpecConstants.SOCKS5_ON_CONNECT_RELAY_INBOUND_BANDWIDTH_LIMIT);
		if (relayInboundBandwidthLimit != null) {
			return Integer.valueOf(relayInboundBandwidthLimit.intValue());
		}
		relayInboundBandwidthLimit = this.getSettings().getLastValue(
				Socks5SettingSpecConstants.SOCKS5_ON_COMMAND_RELAY_INBOUND_BANDWIDTH_LIMIT);
		if (relayInboundBandwidthLimit != null) {
			return Integer.valueOf(relayInboundBandwidthLimit.intValue());
		}
		return null;
	}
	
	private Integer getRelayOutboundBandwidthLimit() {
		Rule applicableRule = this.getApplicableRule();
		PositiveInteger relayOutboundBandwidthLimit =
				applicableRule.getLastRuleResultValue(
						Socks5RuleResultSpecConstants.SOCKS5_ON_CONNECT_RELAY_OUTBOUND_BANDWIDTH_LIMIT);
		if (relayOutboundBandwidthLimit != null) {
			return Integer.valueOf(relayOutboundBandwidthLimit.intValue());
		}
		relayOutboundBandwidthLimit =
				applicableRule.getLastRuleResultValue(
						Socks5RuleResultSpecConstants.SOCKS5_ON_COMMAND_RELAY_OUTBOUND_BANDWIDTH_LIMIT);
		if (relayOutboundBandwidthLimit != null) {
			return Integer.valueOf(relayOutboundBandwidthLimit.intValue());
		}		
		relayOutboundBandwidthLimit = this.getSettings().getLastValue(
				Socks5SettingSpecConstants.SOCKS5_ON_CONNECT_RELAY_OUTBOUND_BANDWIDTH_LIMIT);
		if (relayOutboundBandwidthLimit != null) {
			return Integer.valueOf(relayOutboundBandwidthLimit.intValue());
		}
		relayOutboundBandwidthLimit = this.getSettings().getLastValue(
				Socks5SettingSpecConstants.SOCKS5_ON_COMMAND_RELAY_OUTBOUND_BANDWIDTH_LIMIT);
		if (relayOutboundBandwidthLimit != null) {
			return Integer.valueOf(relayOutboundBandwidthLimit.intValue());
		}
		return null;
	}
	
	private Host getServerFacingBindHost() {
		Rule applicableRule = this.getApplicableRule();
		Host host = applicableRule.getLastRuleResultValue(
				Socks5RuleResultSpecConstants.SOCKS5_ON_CONNECT_SERVER_FACING_BIND_HOST);
		if (host != null) {
			return host;
		}
		host = applicableRule.getLastRuleResultValue(
				Socks5RuleResultSpecConstants.SOCKS5_ON_COMMAND_EXTERNAL_FACING_BIND_HOST);
		if (host != null) {
			return host;
		}
		host = applicableRule.getLastRuleResultValue(
				Socks5RuleResultSpecConstants.SOCKS5_ON_COMMAND_BIND_HOST);
		if (host != null) {
			return host;
		}
		host = applicableRule.getLastRuleResultValue(
				GeneralRuleResultSpecConstants.EXTERNAL_FACING_BIND_HOST);
		if (host != null) {
			return host;
		}
		host = applicableRule.getLastRuleResultValue(
				GeneralRuleResultSpecConstants.BIND_HOST);
		if (host != null) {
			return host;
		}
		host = this.getSettings().getLastValue(
				Socks5SettingSpecConstants.SOCKS5_ON_CONNECT_SERVER_FACING_BIND_HOST);
		if (host != null) {
			return host;
		}
		host = this.getSettings().getLastValue(
				Socks5SettingSpecConstants.SOCKS5_ON_COMMAND_EXTERNAL_FACING_BIND_HOST);
		if (host != null) {
			return host;
		}
		host = this.getSettings().getLastValue(
				Socks5SettingSpecConstants.SOCKS5_ON_COMMAND_BIND_HOST);
		if (host != null) {
			return host;
		}
		host = this.getSettings().getLastValue(
				GeneralSettingSpecConstants.EXTERNAL_FACING_BIND_HOST);
		if (host != null) {
			return host;
		}
		host = this.getSettings().getLastValue(
				GeneralSettingSpecConstants.BIND_HOST);
		return host;
	}
	
	private PortRanges getServerFacingBindPortRanges() {
		Rule applicableRule = this.getApplicableRule();
		List<PortRange> portRanges = applicableRule.getRuleResultValues(
				Socks5RuleResultSpecConstants.SOCKS5_ON_CONNECT_SERVER_FACING_BIND_PORT_RANGE);
		if (portRanges.size() > 0) {
			return PortRanges.newInstance(portRanges);
		}
		portRanges = applicableRule.getRuleResultValues(
				Socks5RuleResultSpecConstants.SOCKS5_ON_COMMAND_EXTERNAL_FACING_BIND_TCP_PORT_RANGE);
		if (portRanges.size() > 0) {
			return PortRanges.newInstance(portRanges);
		}
		portRanges = applicableRule.getRuleResultValues(
				Socks5RuleResultSpecConstants.SOCKS5_ON_COMMAND_BIND_TCP_PORT_RANGE);
		if (portRanges.size() > 0) {
			return PortRanges.newInstance(portRanges);
		}
		portRanges = applicableRule.getRuleResultValues(
				GeneralRuleResultSpecConstants.EXTERNAL_FACING_BIND_TCP_PORT_RANGE);
		if (portRanges.size() > 0) {
			return PortRanges.newInstance(portRanges);
		}
		portRanges = applicableRule.getRuleResultValues(
				GeneralRuleResultSpecConstants.BIND_TCP_PORT_RANGE);
		if (portRanges.size() > 0) {
			return PortRanges.newInstance(portRanges);
		}
		PortRanges prtRanges = this.getSettings().getLastValue(
				Socks5SettingSpecConstants.SOCKS5_ON_CONNECT_SERVER_FACING_BIND_PORT_RANGES);
		if (prtRanges.toList().size() > 0) {
			return prtRanges;
		}
		prtRanges = this.getSettings().getLastValue(
				Socks5SettingSpecConstants.SOCKS5_ON_COMMAND_EXTERNAL_FACING_BIND_TCP_PORT_RANGES);
		if (prtRanges.toList().size() > 0) {
			return prtRanges;
		}
		prtRanges = this.getSettings().getLastValue(
				Socks5SettingSpecConstants.SOCKS5_ON_COMMAND_BIND_TCP_PORT_RANGES);
		if (prtRanges.toList().size() > 0) {
			return prtRanges;
		}
		prtRanges = this.getSettings().getLastValue(
				GeneralSettingSpecConstants.EXTERNAL_FACING_BIND_TCP_PORT_RANGES);
		if (prtRanges.toList().size() > 0) {
			return prtRanges;
		}
		prtRanges = this.getSettings().getLastValue(
				GeneralSettingSpecConstants.BIND_TCP_PORT_RANGES);
		return prtRanges;
	}
	
	private int getServerFacingConnectTimeout() {
		Rule applicableRule = this.getApplicableRule();
		PositiveInteger connectTimeout =
				applicableRule.getLastRuleResultValue(
						Socks5RuleResultSpecConstants.SOCKS5_ON_CONNECT_SERVER_FACING_CONNECT_TIMEOUT);
		if (connectTimeout != null) {
			return connectTimeout.intValue();
		}
		connectTimeout = this.getSettings().getLastValue(
				Socks5SettingSpecConstants.SOCKS5_ON_CONNECT_SERVER_FACING_CONNECT_TIMEOUT);
		return connectTimeout.intValue();
	}
	
	private SocketSettings getServerFacingSocketSettings() {
		Rule applicableRule = this.getApplicableRule();
		List<SocketSetting<Object>> socketSettings =
				applicableRule.getRuleResultValues(
						Socks5RuleResultSpecConstants.SOCKS5_ON_CONNECT_SERVER_FACING_SOCKET_SETTING);
		if (socketSettings.size() > 0) {
			return SocketSettings.newInstance(
					socketSettings.stream().collect(Collectors.toList()));
		}
		socketSettings = applicableRule.getRuleResultValues(
				Socks5RuleResultSpecConstants.SOCKS5_ON_COMMAND_EXTERNAL_FACING_SOCKET_SETTING);
		if (socketSettings.size() > 0) {
			return SocketSettings.newInstance(
					socketSettings.stream().collect(Collectors.toList()));
		}
		socketSettings = applicableRule.getRuleResultValues(
				Socks5RuleResultSpecConstants.SOCKS5_ON_COMMAND_SOCKET_SETTING);
		if (socketSettings.size() > 0) {
			return SocketSettings.newInstance(
					socketSettings.stream().collect(Collectors.toList()));
		}
		socketSettings = applicableRule.getRuleResultValues(
				GeneralRuleResultSpecConstants.EXTERNAL_FACING_SOCKET_SETTING);
		if (socketSettings.size() > 0) {
			return SocketSettings.newInstance(
					socketSettings.stream().collect(Collectors.toList()));
		}
		socketSettings = applicableRule.getRuleResultValues(
				GeneralRuleResultSpecConstants.SOCKET_SETTING);
		if (socketSettings.size() > 0) {
			return SocketSettings.newInstance(
					socketSettings.stream().collect(Collectors.toList()));
		}
		SocketSettings socketSttngs = this.getSettings().getLastValue(
				Socks5SettingSpecConstants.SOCKS5_ON_CONNECT_SERVER_FACING_SOCKET_SETTINGS);
		if (socketSttngs.toMap().size() > 0) {
			return socketSttngs;
		}
		socketSttngs = this.getSettings().getLastValue(
				Socks5SettingSpecConstants.SOCKS5_ON_COMMAND_EXTERNAL_FACING_SOCKET_SETTINGS);
		if (socketSttngs.toMap().size() > 0) {
			return socketSttngs;
		}
		socketSttngs = this.getSettings().getLastValue(
				Socks5SettingSpecConstants.SOCKS5_ON_COMMAND_SOCKET_SETTINGS);
		if (socketSttngs.toMap().size() > 0) {
			return socketSttngs;
		}
		socketSttngs = this.getSettings().getLastValue(
				GeneralSettingSpecConstants.EXTERNAL_FACING_SOCKET_SETTINGS);
		if (socketSttngs.toMap().size() > 0) {
			return socketSttngs;
		}
		return this.getSettings().getLastValue(
				GeneralSettingSpecConstants.SOCKET_SETTINGS);
	}

	private Socket limitClientSocket(final Socket clientSocket) {
		Integer outboundBandwidthLimit = this.getRelayOutboundBandwidthLimit();
		if (outboundBandwidthLimit != null) {
			return new BandwidthLimitedSocket(
					clientSocket, outboundBandwidthLimit.intValue());
		}
		return clientSocket;
	}
	
	private Socket limitServerFacingSocket(final Socket serverFacingSocket) {
		Integer inboundBandwidthLimit = this.getRelayInboundBandwidthLimit();
		if (inboundBandwidthLimit != null) {
			return new BandwidthLimitedSocket(
					serverFacingSocket, inboundBandwidthLimit.intValue());
		}
		return serverFacingSocket;
	}
	
	private Socket newExtemporaneousServerFacingSocket(
			final InetAddress bindInetAddress, 
			final PortRanges bindPortRanges) {
		Socks5Reply socks5Rep = null;
		NetObjectFactory netObjectFactory = 
				this.getSelectedRoute().getNetObjectFactory();
		Socket serverFacingSocket = null;
		boolean serverFacingSocketBound = false;
		for (Iterator<PortRange> iterator = bindPortRanges.toList().iterator();
				!serverFacingSocketBound && iterator.hasNext();) {
			PortRange bindPortRange = iterator.next();
			for (Iterator<Port> iter = bindPortRange.iterator();
					!serverFacingSocketBound && iter.hasNext();) {
				Port bindPort = iter.next();
				try {
					serverFacingSocket = netObjectFactory.newSocket(
							this.getDesiredDestinationAddress(), 
							this.getDesiredDestinationPort(), 
							bindInetAddress, 
							bindPort.intValue());
				} catch (UnknownHostException e) {
					this.logger.error( 
							ObjectLogMessageHelper.objectLogMessage(
									this, 
									"Error in creating the server-facing "
									+ "socket"), 
							e);
					socks5Rep = Socks5Reply.newFailureInstance(
							Reply.HOST_UNREACHABLE);
					this.sendSocks5Reply(socks5Rep);
					return null;
				} catch (IOException e) {
					if (ThrowableHelper.isOrHasInstanceOf(
							e, BindException.class)) {
						continue;
					}
					if (ThrowableHelper.isOrHasInstanceOf(
							e, SocketException.class)) {
						this.logger.error( 
								ObjectLogMessageHelper.objectLogMessage(
										this, 
										"Error in connecting the server-facing "
										+ "socket"), 
								e);
						socks5Rep = Socks5Reply.newFailureInstance(
								Reply.NETWORK_UNREACHABLE);
						this.sendSocks5Reply(socks5Rep);
						return null;						
					}
					this.logger.error( 
							ObjectLogMessageHelper.objectLogMessage(
									this, 
									"Error in connecting the server-facing "
									+ "socket"), 
							e);
					socks5Rep = Socks5Reply.newFailureInstance(
							Reply.GENERAL_SOCKS_SERVER_FAILURE);
					this.sendSocks5Reply(socks5Rep);
					return null;
				}
				serverFacingSocketBound = true;
			}
		}
		if (!serverFacingSocketBound) {
			this.logger.error( 
					ObjectLogMessageHelper.objectLogMessage(
							this, 
							"Unable to bind the server-facing socket to the "
							+ "following address and port (range(s)): %s %s",
							bindInetAddress,
							bindPortRanges));
			socks5Rep = Socks5Reply.newFailureInstance(
					Reply.GENERAL_SOCKS_SERVER_FAILURE);
			this.sendSocks5Reply(socks5Rep);
			return null;			
		}
		return serverFacingSocket;
	}
	
	private Socket newPreparedServerFacingSocket(
			final InetAddress bindInetAddress, 
			final PortRanges bindPortRanges) {
		Socks5Reply socks5Rep = null;
		InetAddress desiredDestinationInetAddress = 
				this.resolveDesiredDestinationAddress(
						this.getDesiredDestinationAddress());
		if (desiredDestinationInetAddress == null) {
			return null;
		}
		NetObjectFactory netObjectFactory = 
				this.getSelectedRoute().getNetObjectFactory();
		int connectTimeout = this.getServerFacingConnectTimeout();
		Socket serverFacingSocket = null;
		boolean serverFacingSocketBound = false;
		for (Iterator<PortRange> iterator = bindPortRanges.toList().iterator();
				!serverFacingSocketBound && iterator.hasNext();) {
			PortRange bindPortRange = iterator.next();
			for (Iterator<Port> iter = bindPortRange.iterator();
					!serverFacingSocketBound && iter.hasNext();) {
				Port bindPort = iter.next();
				serverFacingSocket = netObjectFactory.newSocket();
				if (!this.configureServerFacingSocket(serverFacingSocket)) {
					try {
						serverFacingSocket.close();
					} catch (IOException e) {
						throw new AssertionError(e);
					}
					return null;
				}
				try {
					serverFacingSocket.bind(new InetSocketAddress(
							bindInetAddress, bindPort.intValue()));
				} catch (SocketException e) {
					try {
						serverFacingSocket.close();
					} catch (IOException ex) {
						throw new AssertionError(ex);
					}
					continue;
				} catch (IOException e) {
					this.logger.error( 
							ObjectLogMessageHelper.objectLogMessage(
									this, 
									"Error in binding the server-facing "
									+ "socket"), 
							e);
					socks5Rep = Socks5Reply.newFailureInstance(
							Reply.GENERAL_SOCKS_SERVER_FAILURE);
					this.sendSocks5Reply(socks5Rep);
					try {
						serverFacingSocket.close();
					} catch (IOException ex) {
						throw new AssertionError(ex);
					}					
					return null;
				}				
				try {
					serverFacingSocket.connect(new InetSocketAddress(
							desiredDestinationInetAddress,
							this.getDesiredDestinationPort()),
							connectTimeout);
				} catch (IOException e) {
					if (ThrowableHelper.isOrHasInstanceOf(
							e, BindException.class)) {
						try {
							serverFacingSocket.close();
						} catch (IOException ex) {
							throw new AssertionError(ex);
						}
						continue;
					}
					if (ThrowableHelper.isOrHasInstanceOf(
							e, SocketException.class)) {
						this.logger.error( 
								ObjectLogMessageHelper.objectLogMessage(
										this, 
										"Error in connecting the server-facing "
										+ "socket"), 
								e);
						socks5Rep = Socks5Reply.newFailureInstance(
								Reply.NETWORK_UNREACHABLE);
						this.sendSocks5Reply(socks5Rep);
						try {
							serverFacingSocket.close();
						} catch (IOException ex) {
							throw new AssertionError(ex);
						}						
						return null;						
					}
					this.logger.error( 
							ObjectLogMessageHelper.objectLogMessage(
									this, 
									"Error in connecting the server-facing "
									+ "socket"), 
							e);
					socks5Rep = Socks5Reply.newFailureInstance(
							Reply.GENERAL_SOCKS_SERVER_FAILURE);
					this.sendSocks5Reply(socks5Rep);
					try {
						serverFacingSocket.close();
					} catch (IOException ex) {
						throw new AssertionError(ex);
					}
					return null;
				}				
				serverFacingSocketBound = true;
			}
		}
		if (!serverFacingSocketBound) {
			this.logger.error( 
					ObjectLogMessageHelper.objectLogMessage(
							this, 
							"Unable to bind the server-facing socket to the "
							+ "following address and port (range(s)): %s %s",
							bindInetAddress,
							bindPortRanges));
			socks5Rep = Socks5Reply.newFailureInstance(
					Reply.GENERAL_SOCKS_SERVER_FAILURE);
			this.sendSocks5Reply(socks5Rep);
			return null;
		}
		return serverFacingSocket;
	}
	
	private Socket newServerFacingSocket() {
		Host bindHost = this.getServerFacingBindHost();
		InetAddress bindInetAddress = bindHost.toInetAddress();
		PortRanges bindPortRanges = this.getServerFacingBindPortRanges();
		return this.canPrepareServerFacingSocket() ? 
				this.newPreparedServerFacingSocket(
						bindInetAddress, bindPortRanges)	
				: this.newExtemporaneousServerFacingSocket(
						bindInetAddress, bindPortRanges);
	}
	
	private InetAddress resolveDesiredDestinationAddress(
			final String desiredDestinationAddress) {
		Socks5Reply socks5Rep = null;
		NetObjectFactory netObjectFactory =
				this.getSelectedRoute().getNetObjectFactory();
		HostResolver hostResolver =	netObjectFactory.newHostResolver();
		InetAddress desiredDestinationInetAddress = null;
		try {
			desiredDestinationInetAddress = hostResolver.resolve(
					desiredDestinationAddress);
		} catch (UnknownHostException e) {
			this.logger.error( 
					ObjectLogMessageHelper.objectLogMessage(
							this, 
							"Unable to resolve the desired destination "
							+ "address for the server-facing socket: %s",
							desiredDestinationAddress), 
					e);
			socks5Rep = Socks5Reply.newFailureInstance(
					Reply.HOST_UNREACHABLE);
			this.sendSocks5Reply(socks5Rep);
			return null;
		} catch (IOException e) {
			this.logger.error( 
					ObjectLogMessageHelper.objectLogMessage(
							this, 
							"Error in resolving the desired destination "
							+ "address for the server-facing socket: %s",
							desiredDestinationAddress), 
					e);
			socks5Rep = Socks5Reply.newFailureInstance(
					Reply.HOST_UNREACHABLE);
			this.sendSocks5Reply(socks5Rep);
			return null;
		}
		return desiredDestinationInetAddress;
	}
	
	@Override
	public void run() {
		Socket serverFacingSocket = null;
		Socket clientSocket = this.getClientSocket();		
		Socks5Reply socks5Rep = null;
		try {
			serverFacingSocket = this.newServerFacingSocket();
			if (serverFacingSocket == null) {
				return;
			}
			String serverBoundAddress = 
					serverFacingSocket.getInetAddress().getHostAddress();
			int serverBoundPort = serverFacingSocket.getPort();
			socks5Rep = Socks5Reply.newInstance(
					Reply.SUCCEEDED, 
					serverBoundAddress, 
					serverBoundPort);
			RuleContext ruleContext = this.newSocks5ReplyRuleContext(
					socks5Rep);
			this.setRuleContext(ruleContext);
			Rule applicableRule = this.getRules().firstAppliesTo(
					this.getRuleContext());
			if (applicableRule == null) {
				this.logger.error(ObjectLogMessageHelper.objectLogMessage(
						this, 
						"No applicable rule found based on the following "
						+ "context: %s",
						this.getRuleContext()));				
				socks5Rep = Socks5Reply.newFailureInstance(
						Reply.CONNECTION_NOT_ALLOWED_BY_RULESET);
				this.sendSocks5Reply(socks5Rep);
				return;
			}			
			this.setApplicableRule(applicableRule);
			if (!this.canAllowSocks5Reply()) {
				return;
			}
			if (!this.sendSocks5Reply(socks5Rep)) {
				return;
			}
			/*
			 * Create a temporary variable to avoid the resource-never-closed warning
			 * (The resource will get closed)
			 */
			Socket serverFacingSock = this.limitServerFacingSocket(
					serverFacingSocket);
			serverFacingSocket = serverFacingSock;
			clientSocket = this.limitClientSocket(clientSocket);
			Relay.Builder builder = new Relay.Builder(
					clientSocket, serverFacingSocket);
			builder.bufferSize(this.getRelayBufferSize());
			builder.idleTimeout(this.getRelayIdleTimeout());
			Relay relay = builder.build();
			try {
				this.passData(relay);				
			} catch (IOException e) {
				this.logger.error( 
						ObjectLogMessageHelper.objectLogMessage(
								this, "Error in starting to pass data"), 
						e);
			}
		} finally {
			if (serverFacingSocket != null && !serverFacingSocket.isClosed()) {
				try {
					serverFacingSocket.close();
				} catch (IOException e) {
					throw new UncheckedIOException(e);
				}
			}
		}
	}

}
