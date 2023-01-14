package com.github.jh3nd3rs0n.jargyle.server.internal.server.socks5;

import java.io.IOException;
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
import com.github.jh3nd3rs0n.jargyle.common.net.Host;
import com.github.jh3nd3rs0n.jargyle.common.net.Port;
import com.github.jh3nd3rs0n.jargyle.common.net.PortRange;
import com.github.jh3nd3rs0n.jargyle.common.net.PortRanges;
import com.github.jh3nd3rs0n.jargyle.common.net.SocketSetting;
import com.github.jh3nd3rs0n.jargyle.common.net.SocketSettings;
import com.github.jh3nd3rs0n.jargyle.common.number.PositiveInteger;
import com.github.jh3nd3rs0n.jargyle.internal.logging.ObjectLogMessageHelper;
import com.github.jh3nd3rs0n.jargyle.internal.throwable.ThrowableHelper;
import com.github.jh3nd3rs0n.jargyle.server.GeneralRuleResultSpecConstants;
import com.github.jh3nd3rs0n.jargyle.server.GeneralSettingSpecConstants;
import com.github.jh3nd3rs0n.jargyle.server.Rule;
import com.github.jh3nd3rs0n.jargyle.server.RuleContext;
import com.github.jh3nd3rs0n.jargyle.server.Settings;
import com.github.jh3nd3rs0n.jargyle.server.Socks5RuleResultSpecConstants;
import com.github.jh3nd3rs0n.jargyle.server.Socks5SettingSpecConstants;
import com.github.jh3nd3rs0n.jargyle.server.internal.net.BandwidthLimitedSocket;
import com.github.jh3nd3rs0n.jargyle.server.internal.server.RelayServer;
import com.github.jh3nd3rs0n.jargyle.server.internal.server.Rules;
import com.github.jh3nd3rs0n.jargyle.transport.socks5.Reply;
import com.github.jh3nd3rs0n.jargyle.transport.socks5.Socks5Reply;

final class ConnectCommandWorker extends CommandWorker {

	private static final Logger LOGGER = LoggerFactory.getLogger(
			ConnectCommandWorker.class);

	private Rule applicableRule;
	private final Socket clientSocket;
	private final CommandWorkerContext commandWorkerContext;
	private final String desiredDestinationAddress;
	private final int desiredDestinationPort;
	private final NetObjectFactory netObjectFactory;
	private final Rules rules;
	private final Settings settings;
	
	public ConnectCommandWorker(final CommandWorkerContext context) {
		super(context);
		Rule applicableRl = context.getApplicableRule();
		Socket clientSock = context.getClientSocket();
		String desiredDestinationAddr =	context.getDesiredDestinationAddress();
		int desiredDestinationPrt = context.getDesiredDestinationPort();
		NetObjectFactory netObjFactory = 
				context.getSelectedRoute().getNetObjectFactory();
		Rules rls = context.getRules();
		Settings sttngs = context.getSettings();
		this.applicableRule = applicableRl;
		this.clientSocket = clientSock;
		this.commandWorkerContext = context;
		this.desiredDestinationAddress = desiredDestinationAddr;
		this.desiredDestinationPort = desiredDestinationPrt;
		this.netObjectFactory = netObjFactory;
		this.rules = rls;
		this.settings = sttngs;	
	}
	
	private boolean canPrepareServerFacingSocket() {
		Boolean b = this.applicableRule.getLastRuleResultValue(
				Socks5RuleResultSpecConstants.SOCKS5_ON_CONNECT_PREPARE_SERVER_FACING_SOCKET);
		if (b != null) {
			return b.booleanValue();
		}
		b = this.settings.getLastValue(
				Socks5SettingSpecConstants.SOCKS5_ON_CONNECT_PREPARE_SERVER_FACING_SOCKET);
		return b.booleanValue();
	}

	private boolean configureServerFacingSocket(
			final Socket serverFacingSocket) {
		SocketSettings socketSettings = this.getServerFacingSocketSettings();
		try {
			socketSettings.applyTo(serverFacingSocket);
		} catch (UnsupportedOperationException e) {
			LOGGER.error( 
					ObjectLogMessageHelper.objectLogMessage(
							this, "Error in setting the server-facing socket"), 
					e);
			Socks5Reply socks5Rep = Socks5Reply.newFailureInstance(
					Reply.GENERAL_SOCKS_SERVER_FAILURE);
			this.commandWorkerContext.sendSocks5Reply(this, socks5Rep, LOGGER);
			return false;			
		} catch (SocketException e) {
			LOGGER.error( 
					ObjectLogMessageHelper.objectLogMessage(
							this, "Error in setting the server-facing socket"), 
					e);
			Socks5Reply socks5Rep = Socks5Reply.newFailureInstance(
					Reply.GENERAL_SOCKS_SERVER_FAILURE);
			this.commandWorkerContext.sendSocks5Reply(this, socks5Rep, LOGGER);
			return false;
		}
		return true;
	}
	
	private int getRelayBufferSize() {
		PositiveInteger relayBufferSize =
				this.applicableRule.getLastRuleResultValue(
						Socks5RuleResultSpecConstants.SOCKS5_ON_CONNECT_RELAY_BUFFER_SIZE);
		if (relayBufferSize != null) {
			return relayBufferSize.intValue();
		}
		relayBufferSize = this.applicableRule.getLastRuleResultValue(
				Socks5RuleResultSpecConstants.SOCKS5_ON_COMMAND_RELAY_BUFFER_SIZE);
		if (relayBufferSize != null) {
			return relayBufferSize.intValue();
		}
		relayBufferSize = this.settings.getLastValue(
				Socks5SettingSpecConstants.SOCKS5_ON_CONNECT_RELAY_BUFFER_SIZE);
		if (relayBufferSize != null) {
			return relayBufferSize.intValue();
		}
		relayBufferSize = this.settings.getLastValue(
				Socks5SettingSpecConstants.SOCKS5_ON_COMMAND_RELAY_BUFFER_SIZE);
		return relayBufferSize.intValue();
	}
	
	private int getRelayIdleTimeout() {
		PositiveInteger relayIdleTimeout =
				this.applicableRule.getLastRuleResultValue(
						Socks5RuleResultSpecConstants.SOCKS5_ON_CONNECT_RELAY_IDLE_TIMEOUT);
		if (relayIdleTimeout != null) {
			return relayIdleTimeout.intValue();
		}
		relayIdleTimeout = this.applicableRule.getLastRuleResultValue(
				Socks5RuleResultSpecConstants.SOCKS5_ON_COMMAND_RELAY_IDLE_TIMEOUT);
		if (relayIdleTimeout != null) {
			return relayIdleTimeout.intValue();
		}
		relayIdleTimeout = this.settings.getLastValue(
				Socks5SettingSpecConstants.SOCKS5_ON_CONNECT_RELAY_IDLE_TIMEOUT);
		if (relayIdleTimeout != null) {
			return relayIdleTimeout.intValue();
		}
		relayIdleTimeout = this.settings.getLastValue(
				Socks5SettingSpecConstants.SOCKS5_ON_COMMAND_RELAY_IDLE_TIMEOUT);
		return relayIdleTimeout.intValue();
	}
	
	private Integer getRelayInboundBandwidthLimit() {
		PositiveInteger relayInboundBandwidthLimit =
				this.applicableRule.getLastRuleResultValue(
						Socks5RuleResultSpecConstants.SOCKS5_ON_CONNECT_RELAY_INBOUND_BANDWIDTH_LIMIT);
		if (relayInboundBandwidthLimit != null) {
			return Integer.valueOf(relayInboundBandwidthLimit.intValue());
		}
		relayInboundBandwidthLimit = this.applicableRule.getLastRuleResultValue(
				Socks5RuleResultSpecConstants.SOCKS5_ON_COMMAND_RELAY_INBOUND_BANDWIDTH_LIMIT);
		if (relayInboundBandwidthLimit != null) {
			return Integer.valueOf(relayInboundBandwidthLimit.intValue());
		}
		relayInboundBandwidthLimit = this.settings.getLastValue(
				Socks5SettingSpecConstants.SOCKS5_ON_CONNECT_RELAY_INBOUND_BANDWIDTH_LIMIT);
		if (relayInboundBandwidthLimit != null) {
			return Integer.valueOf(relayInboundBandwidthLimit.intValue());
		}
		relayInboundBandwidthLimit = this.settings.getLastValue(
				Socks5SettingSpecConstants.SOCKS5_ON_COMMAND_RELAY_INBOUND_BANDWIDTH_LIMIT);
		if (relayInboundBandwidthLimit != null) {
			return Integer.valueOf(relayInboundBandwidthLimit.intValue());
		}
		return null;
	}
	
	private Integer getRelayOutboundBandwidthLimit() {
		PositiveInteger relayOutboundBandwidthLimit =
				this.applicableRule.getLastRuleResultValue(
						Socks5RuleResultSpecConstants.SOCKS5_ON_CONNECT_RELAY_OUTBOUND_BANDWIDTH_LIMIT);
		if (relayOutboundBandwidthLimit != null) {
			return Integer.valueOf(relayOutboundBandwidthLimit.intValue());
		}
		relayOutboundBandwidthLimit =
				this.applicableRule.getLastRuleResultValue(
						Socks5RuleResultSpecConstants.SOCKS5_ON_COMMAND_RELAY_OUTBOUND_BANDWIDTH_LIMIT);
		if (relayOutboundBandwidthLimit != null) {
			return Integer.valueOf(relayOutboundBandwidthLimit.intValue());
		}		
		relayOutboundBandwidthLimit = this.settings.getLastValue(
				Socks5SettingSpecConstants.SOCKS5_ON_CONNECT_RELAY_OUTBOUND_BANDWIDTH_LIMIT);
		if (relayOutboundBandwidthLimit != null) {
			return Integer.valueOf(relayOutboundBandwidthLimit.intValue());
		}
		relayOutboundBandwidthLimit = this.settings.getLastValue(
				Socks5SettingSpecConstants.SOCKS5_ON_COMMAND_RELAY_OUTBOUND_BANDWIDTH_LIMIT);
		if (relayOutboundBandwidthLimit != null) {
			return Integer.valueOf(relayOutboundBandwidthLimit.intValue());
		}
		return null;
	}
	
	private Host getServerFacingBindHost() {
		Host host = this.applicableRule.getLastRuleResultValue(
				Socks5RuleResultSpecConstants.SOCKS5_ON_CONNECT_SERVER_FACING_BIND_HOST);
		if (host != null) {
			return host;
		}
		host = this.applicableRule.getLastRuleResultValue(
				Socks5RuleResultSpecConstants.SOCKS5_ON_COMMAND_EXTERNAL_FACING_BIND_HOST);
		if (host != null) {
			return host;
		}
		host = this.applicableRule.getLastRuleResultValue(
				Socks5RuleResultSpecConstants.SOCKS5_ON_COMMAND_BIND_HOST);
		if (host != null) {
			return host;
		}
		host = this.applicableRule.getLastRuleResultValue(
				GeneralRuleResultSpecConstants.EXTERNAL_FACING_BIND_HOST);
		if (host != null) {
			return host;
		}
		host = this.applicableRule.getLastRuleResultValue(
				GeneralRuleResultSpecConstants.BIND_HOST);
		if (host != null) {
			return host;
		}
		host = this.settings.getLastValue(
				Socks5SettingSpecConstants.SOCKS5_ON_CONNECT_SERVER_FACING_BIND_HOST);
		if (host != null) {
			return host;
		}
		host = this.settings.getLastValue(
				Socks5SettingSpecConstants.SOCKS5_ON_COMMAND_EXTERNAL_FACING_BIND_HOST);
		if (host != null) {
			return host;
		}
		host = this.settings.getLastValue(
				Socks5SettingSpecConstants.SOCKS5_ON_COMMAND_BIND_HOST);
		if (host != null) {
			return host;
		}
		host = this.settings.getLastValue(
				GeneralSettingSpecConstants.EXTERNAL_FACING_BIND_HOST);
		if (host != null) {
			return host;
		}
		host = this.settings.getLastValue(
				GeneralSettingSpecConstants.BIND_HOST);
		return host;
	}
	
	private PortRanges getServerFacingBindPortRanges() {
		List<PortRange> portRanges = this.applicableRule.getRuleResultValues(
				Socks5RuleResultSpecConstants.SOCKS5_ON_CONNECT_SERVER_FACING_BIND_PORT_RANGE);
		if (portRanges.size() > 0) {
			return PortRanges.newInstance(portRanges);
		}
		portRanges = this.applicableRule.getRuleResultValues(
				Socks5RuleResultSpecConstants.SOCKS5_ON_COMMAND_EXTERNAL_FACING_BIND_TCP_PORT_RANGE);
		if (portRanges.size() > 0) {
			return PortRanges.newInstance(portRanges);
		}
		portRanges = this.applicableRule.getRuleResultValues(
				Socks5RuleResultSpecConstants.SOCKS5_ON_COMMAND_BIND_TCP_PORT_RANGE);
		if (portRanges.size() > 0) {
			return PortRanges.newInstance(portRanges);
		}
		portRanges = this.applicableRule.getRuleResultValues(
				GeneralRuleResultSpecConstants.EXTERNAL_FACING_BIND_TCP_PORT_RANGE);
		if (portRanges.size() > 0) {
			return PortRanges.newInstance(portRanges);
		}
		portRanges = this.applicableRule.getRuleResultValues(
				GeneralRuleResultSpecConstants.BIND_TCP_PORT_RANGE);
		if (portRanges.size() > 0) {
			return PortRanges.newInstance(portRanges);
		}
		PortRanges prtRanges = this.settings.getLastValue(
				Socks5SettingSpecConstants.SOCKS5_ON_CONNECT_SERVER_FACING_BIND_PORT_RANGES);
		if (prtRanges.toList().size() > 0) {
			return prtRanges;
		}
		prtRanges = this.settings.getLastValue(
				Socks5SettingSpecConstants.SOCKS5_ON_COMMAND_EXTERNAL_FACING_BIND_TCP_PORT_RANGES);
		if (prtRanges.toList().size() > 0) {
			return prtRanges;
		}
		prtRanges = this.settings.getLastValue(
				Socks5SettingSpecConstants.SOCKS5_ON_COMMAND_BIND_TCP_PORT_RANGES);
		if (prtRanges.toList().size() > 0) {
			return prtRanges;
		}
		prtRanges = this.settings.getLastValue(
				GeneralSettingSpecConstants.EXTERNAL_FACING_BIND_TCP_PORT_RANGES);
		if (prtRanges.toList().size() > 0) {
			return prtRanges;
		}
		prtRanges = this.settings.getLastValue(
				GeneralSettingSpecConstants.BIND_TCP_PORT_RANGES);
		return prtRanges;
	}
	
	private int getServerFacingConnectTimeout() {
		PositiveInteger connectTimeout =
				this.applicableRule.getLastRuleResultValue(
						Socks5RuleResultSpecConstants.SOCKS5_ON_CONNECT_SERVER_FACING_CONNECT_TIMEOUT);
		if (connectTimeout != null) {
			return connectTimeout.intValue();
		}
		connectTimeout = this.settings.getLastValue(
				Socks5SettingSpecConstants.SOCKS5_ON_CONNECT_SERVER_FACING_CONNECT_TIMEOUT);
		return connectTimeout.intValue();
	}
	
	private SocketSettings getServerFacingSocketSettings() {
		List<SocketSetting<Object>> socketSettings =
				this.applicableRule.getRuleResultValues(
						Socks5RuleResultSpecConstants.SOCKS5_ON_CONNECT_SERVER_FACING_SOCKET_SETTING);
		if (socketSettings.size() > 0) {
			return SocketSettings.newInstance(
					socketSettings.stream().collect(Collectors.toList()));
		}
		socketSettings = this.applicableRule.getRuleResultValues(
				Socks5RuleResultSpecConstants.SOCKS5_ON_COMMAND_EXTERNAL_FACING_SOCKET_SETTING);
		if (socketSettings.size() > 0) {
			return SocketSettings.newInstance(
					socketSettings.stream().collect(Collectors.toList()));
		}
		socketSettings = this.applicableRule.getRuleResultValues(
				Socks5RuleResultSpecConstants.SOCKS5_ON_COMMAND_SOCKET_SETTING);
		if (socketSettings.size() > 0) {
			return SocketSettings.newInstance(
					socketSettings.stream().collect(Collectors.toList()));
		}
		socketSettings = this.applicableRule.getRuleResultValues(
				GeneralRuleResultSpecConstants.EXTERNAL_FACING_SOCKET_SETTING);
		if (socketSettings.size() > 0) {
			return SocketSettings.newInstance(
					socketSettings.stream().collect(Collectors.toList()));
		}
		socketSettings = this.applicableRule.getRuleResultValues(
				GeneralRuleResultSpecConstants.SOCKET_SETTING);
		if (socketSettings.size() > 0) {
			return SocketSettings.newInstance(
					socketSettings.stream().collect(Collectors.toList()));
		}
		SocketSettings socketSttngs = this.settings.getLastValue(
				Socks5SettingSpecConstants.SOCKS5_ON_CONNECT_SERVER_FACING_SOCKET_SETTINGS);
		if (socketSttngs.toMap().size() > 0) {
			return socketSttngs;
		}
		socketSttngs = this.settings.getLastValue(
				Socks5SettingSpecConstants.SOCKS5_ON_COMMAND_EXTERNAL_FACING_SOCKET_SETTINGS);
		if (socketSttngs.toMap().size() > 0) {
			return socketSttngs;
		}
		socketSttngs = this.settings.getLastValue(
				Socks5SettingSpecConstants.SOCKS5_ON_COMMAND_SOCKET_SETTINGS);
		if (socketSttngs.toMap().size() > 0) {
			return socketSttngs;
		}
		socketSttngs = this.settings.getLastValue(
				GeneralSettingSpecConstants.EXTERNAL_FACING_SOCKET_SETTINGS);
		if (socketSttngs.toMap().size() > 0) {
			return socketSttngs;
		}
		return this.settings.getLastValue(
				GeneralSettingSpecConstants.SOCKET_SETTINGS);
	}

	private Socket newExtemporaneousServerFacingSocket(
			final InetAddress bindInetAddress, 
			final PortRanges bindPortRanges) {
		Socks5Reply socks5Rep = null;
		Socket serverFacingSocket = null;
		boolean serverFacingSocketBound = false;
		for (Iterator<PortRange> iterator = bindPortRanges.toList().iterator();
				!serverFacingSocketBound && iterator.hasNext();) {
			PortRange bindPortRange = iterator.next();
			for (Iterator<Port> iter = bindPortRange.iterator();
					!serverFacingSocketBound && iter.hasNext();) {
				Port bindPort = iter.next();
				try {
					serverFacingSocket = this.netObjectFactory.newSocket(
							this.desiredDestinationAddress, 
							this.desiredDestinationPort, 
							bindInetAddress, 
							bindPort.intValue());
				} catch (UnknownHostException e) {
					LOGGER.error( 
							ObjectLogMessageHelper.objectLogMessage(
									this, 
									"Error in creating the server-facing "
									+ "socket"), 
							e);
					socks5Rep = Socks5Reply.newFailureInstance(
							Reply.HOST_UNREACHABLE);
					this.commandWorkerContext.sendSocks5Reply(
							this, socks5Rep, LOGGER);
					return null;
				} catch (IOException e) {
					if (e instanceof BindException 
							|| ThrowableHelper.getRecentCause(
									e, BindException.class) != null) {
						continue;
					}
					if (e instanceof SocketException
							|| ThrowableHelper.getRecentCause(
									e, SocketException.class) != null) {
						LOGGER.error( 
								ObjectLogMessageHelper.objectLogMessage(
										this, 
										"Error in connecting the server-facing "
										+ "socket"), 
								e);
						socks5Rep = Socks5Reply.newFailureInstance(
								Reply.NETWORK_UNREACHABLE);
						this.commandWorkerContext.sendSocks5Reply(
								this, socks5Rep, LOGGER);
						return null;						
					}
					LOGGER.error( 
							ObjectLogMessageHelper.objectLogMessage(
									this, 
									"Error in connecting the server-facing "
									+ "socket"), 
							e);
					socks5Rep = Socks5Reply.newFailureInstance(
							Reply.GENERAL_SOCKS_SERVER_FAILURE);
					this.commandWorkerContext.sendSocks5Reply(
							this, socks5Rep, LOGGER);
					return null;
				}
				serverFacingSocketBound = true;
			}
		}
		if (!serverFacingSocketBound) {
			LOGGER.error( 
					ObjectLogMessageHelper.objectLogMessage(
							this, 
							"Unable to bind the server-facing socket to the "
							+ "following address and port (range(s)): %s %s",
							bindInetAddress,
							bindPortRanges));
			socks5Rep = Socks5Reply.newFailureInstance(
					Reply.GENERAL_SOCKS_SERVER_FAILURE);
			this.commandWorkerContext.sendSocks5Reply(this, socks5Rep, LOGGER);
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
						this.desiredDestinationAddress);
		if (desiredDestinationInetAddress == null) {
			return null;
		}
		int connectTimeout = this.getServerFacingConnectTimeout();
		Socket serverFacingSocket = null;
		boolean serverFacingSocketBound = false;
		for (Iterator<PortRange> iterator = bindPortRanges.toList().iterator();
				!serverFacingSocketBound && iterator.hasNext();) {
			PortRange bindPortRange = iterator.next();
			for (Iterator<Port> iter = bindPortRange.iterator();
					!serverFacingSocketBound && iter.hasNext();) {
				Port bindPort = iter.next();
				serverFacingSocket = this.netObjectFactory.newSocket();
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
					LOGGER.error( 
							ObjectLogMessageHelper.objectLogMessage(
									this, 
									"Error in binding the server-facing "
									+ "socket"), 
							e);
					socks5Rep = Socks5Reply.newFailureInstance(
							Reply.GENERAL_SOCKS_SERVER_FAILURE);
					this.commandWorkerContext.sendSocks5Reply(
							this, socks5Rep, LOGGER);
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
							this.desiredDestinationPort),
							connectTimeout);
				} catch (IOException e) {
					if (e instanceof BindException 
							|| ThrowableHelper.getRecentCause(
									e, BindException.class) != null) {
						try {
							serverFacingSocket.close();
						} catch (IOException ex) {
							throw new AssertionError(ex);
						}
						continue;
					}
					if (e instanceof SocketException
							|| ThrowableHelper.getRecentCause(
									e, SocketException.class) != null) {
						LOGGER.error( 
								ObjectLogMessageHelper.objectLogMessage(
										this, 
										"Error in connecting the server-facing "
										+ "socket"), 
								e);
						socks5Rep = Socks5Reply.newFailureInstance(
								Reply.NETWORK_UNREACHABLE);
						this.commandWorkerContext.sendSocks5Reply(
								this, socks5Rep, LOGGER);
						try {
							serverFacingSocket.close();
						} catch (IOException ex) {
							throw new AssertionError(ex);
						}						
						return null;						
					}
					LOGGER.error( 
							ObjectLogMessageHelper.objectLogMessage(
									this, 
									"Error in connecting the server-facing "
									+ "socket"), 
							e);
					socks5Rep = Socks5Reply.newFailureInstance(
							Reply.GENERAL_SOCKS_SERVER_FAILURE);
					this.commandWorkerContext.sendSocks5Reply(
							this, socks5Rep, LOGGER);
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
			LOGGER.error( 
					ObjectLogMessageHelper.objectLogMessage(
							this, 
							"Unable to bind the server-facing socket to the "
							+ "following address and port (range(s)): %s %s",
							bindInetAddress,
							bindPortRanges));
			socks5Rep = Socks5Reply.newFailureInstance(
					Reply.GENERAL_SOCKS_SERVER_FAILURE);
			this.commandWorkerContext.sendSocks5Reply(this, socks5Rep, LOGGER);
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
		HostResolver hostResolver =	this.netObjectFactory.newHostResolver();
		InetAddress desiredDestinationInetAddress = null;
		try {
			desiredDestinationInetAddress = hostResolver.resolve(
					desiredDestinationAddress);
		} catch (UnknownHostException e) {
			LOGGER.error( 
					ObjectLogMessageHelper.objectLogMessage(
							this, 
							"Unable to resolve the desired destination "
							+ "address for the server-facing socket: %s",
							desiredDestinationAddress), 
					e);
			socks5Rep = Socks5Reply.newFailureInstance(
					Reply.HOST_UNREACHABLE);
			this.commandWorkerContext.sendSocks5Reply(
					this, socks5Rep, LOGGER);
			return null;
		} catch (IOException e) {
			LOGGER.error( 
					ObjectLogMessageHelper.objectLogMessage(
							this, 
							"Error in resolving the desired destination "
							+ "address for the server-facing socket: %s",
							desiredDestinationAddress), 
					e);
			socks5Rep = Socks5Reply.newFailureInstance(
					Reply.HOST_UNREACHABLE);
			this.commandWorkerContext.sendSocks5Reply(
					this, socks5Rep, LOGGER);
			return null;
		}
		return desiredDestinationInetAddress;
	}
	
	@Override
	public void run() throws IOException {
		Socket serverFacingSocket = null;
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
			RuleContext socks5ReplyRuleContext = 
					this.commandWorkerContext.newSocks5ReplyRuleContext(
							socks5Rep);
			this.applicableRule = this.rules.firstAppliesTo(
					socks5ReplyRuleContext);
			if (!this.commandWorkerContext.canAllowSocks5Reply(
					this, 
					this.applicableRule, 
					socks5ReplyRuleContext, 
					LOGGER)) {
				return;
			}
			if (!this.commandWorkerContext.sendSocks5Reply(
					this, socks5Rep, LOGGER)) {
				return;
			}
			Integer inboundBandwidthLimit = this.getRelayInboundBandwidthLimit();
			Integer outboundBandwidthLimit = this.getRelayOutboundBandwidthLimit();
			Socket clientSock = this.clientSocket;
			Socket serverFacingSock = serverFacingSocket;
			if (outboundBandwidthLimit != null) {
				clientSock = new BandwidthLimitedSocket(
						clientSock, outboundBandwidthLimit.intValue());
			}
			if (inboundBandwidthLimit != null) {
				serverFacingSock = new BandwidthLimitedSocket(
						serverFacingSock, inboundBandwidthLimit.intValue());
			}			
			RelayServer.Builder builder = new RelayServer.Builder(
					clientSock, serverFacingSock);
			builder.bufferSize(this.getRelayBufferSize());
			builder.idleTimeout(this.getRelayIdleTimeout());
			try {
				TcpBasedCommandWorkerHelper.passData(builder);				
			} catch (IOException e) {
				LOGGER.error( 
						ObjectLogMessageHelper.objectLogMessage(
								this, "Error in starting to pass data"), 
						e);
			}
		} finally {
			if (serverFacingSocket != null && !serverFacingSocket.isClosed()) {
				serverFacingSocket.close();
			}
		}
	}

}
