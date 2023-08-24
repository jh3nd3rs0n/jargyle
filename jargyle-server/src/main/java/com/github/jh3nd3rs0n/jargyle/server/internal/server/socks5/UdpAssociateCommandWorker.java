package com.github.jh3nd3rs0n.jargyle.server.internal.server.socks5;

import java.io.IOException;
import java.net.BindException;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketException;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.jh3nd3rs0n.jargyle.client.NetObjectFactory;
import com.github.jh3nd3rs0n.jargyle.common.lang.PositiveInteger;
import com.github.jh3nd3rs0n.jargyle.common.net.Host;
import com.github.jh3nd3rs0n.jargyle.common.net.Port;
import com.github.jh3nd3rs0n.jargyle.common.net.PortRange;
import com.github.jh3nd3rs0n.jargyle.common.net.PortRanges;
import com.github.jh3nd3rs0n.jargyle.common.net.SocketSetting;
import com.github.jh3nd3rs0n.jargyle.common.net.SocketSettings;
import com.github.jh3nd3rs0n.jargyle.internal.logging.ObjectLogMessageHelper;
import com.github.jh3nd3rs0n.jargyle.internal.net.ssl.DtlsDatagramSocketFactory;
import com.github.jh3nd3rs0n.jargyle.server.GeneralRuleResultSpecConstants;
import com.github.jh3nd3rs0n.jargyle.server.GeneralSettingSpecConstants;
import com.github.jh3nd3rs0n.jargyle.server.Rule;
import com.github.jh3nd3rs0n.jargyle.server.RuleContext;
import com.github.jh3nd3rs0n.jargyle.server.Settings;
import com.github.jh3nd3rs0n.jargyle.server.Socks5RuleResultSpecConstants;
import com.github.jh3nd3rs0n.jargyle.server.Socks5SettingSpecConstants;
import com.github.jh3nd3rs0n.jargyle.server.internal.net.BandwidthLimitedDatagramSocket;
import com.github.jh3nd3rs0n.jargyle.server.internal.server.Rules;
import com.github.jh3nd3rs0n.jargyle.transport.socks5.Reply;
import com.github.jh3nd3rs0n.jargyle.transport.socks5.Socks5Reply;

final class UdpAssociateCommandWorker extends CommandWorker {

	private static final int HALF_SECOND = 500;

	private static final Logger LOGGER = LoggerFactory.getLogger(
			UdpAssociateCommandWorker.class);
	
	private Rule applicableRule;
	private final DtlsDatagramSocketFactory clientFacingDtlsDatagramSocketFactory;
	private final Socket clientSocket;
	private final CommandWorkerContext commandWorkerContext;
	private final String desiredDestinationAddress;
	private final int desiredDestinationPort;
	private final MethodSubnegotiationResults methodSubnegotiationResults;	
	private final NetObjectFactory netObjectFactory;
	private final Rules rules;
	private final Settings settings;
	
	public UdpAssociateCommandWorker(final CommandWorkerContext context) {
		super(context);
		Rule applicableRl = context.getApplicableRule();
		DtlsDatagramSocketFactory clientFacingDtlsDatagramSockFactory =
				context.getClientFacingDtlsDatagramSocketFactory();
		Socket clientSock = context.getClientSocket();
		String desiredDestinationAddr =	context.getDesiredDestinationAddress();
		int desiredDestinationPrt = context.getDesiredDestinationPort();
		MethodSubnegotiationResults methSubnegotiationResults =
				context.getMethodSubnegotiationResults();		
		NetObjectFactory netObjFactory = 
				context.getSelectedRoute().getNetObjectFactory();
		Rules rls = context.getRules();
		Settings sttngs = context.getSettings();
		this.applicableRule = applicableRl;
		this.clientFacingDtlsDatagramSocketFactory = 
				clientFacingDtlsDatagramSockFactory;
		this.clientSocket = clientSock;
		this.commandWorkerContext = context;
		this.desiredDestinationAddress = desiredDestinationAddr;
		this.desiredDestinationPort = desiredDestinationPrt;
		this.methodSubnegotiationResults = methSubnegotiationResults;		
		this.netObjectFactory = netObjFactory;
		this.rules = rls;
		this.settings = sttngs;
	}
	
	private boolean configureClientFacingDatagramSocket(
			final DatagramSocket clientFacingDatagramSock) {
		SocketSettings socketSettings = this.getClientFacingSocketSettings();
		try {
			socketSettings.applyTo(clientFacingDatagramSock);
		} catch (UnsupportedOperationException e) {
			LOGGER.error( 
					ObjectLogMessageHelper.objectLogMessage(
							this, 
							"Error in setting the client-facing UDP socket"), 
					e);
			Socks5Reply socks5Rep = Socks5Reply.newFailureInstance(
					Reply.GENERAL_SOCKS_SERVER_FAILURE);
			this.commandWorkerContext.sendSocks5Reply(this, socks5Rep, LOGGER);
			return false;			
		} catch (SocketException e) {
			LOGGER.error( 
					ObjectLogMessageHelper.objectLogMessage(
							this, 
							"Error in setting the client-facing UDP socket"), 
					e);
			Socks5Reply socks5Rep = Socks5Reply.newFailureInstance(
					Reply.GENERAL_SOCKS_SERVER_FAILURE);
			this.commandWorkerContext.sendSocks5Reply(this, socks5Rep, LOGGER);
			return false;
		}
		return true;
	}
	
	private boolean configurePeerFacingDatagramSocket(
			final DatagramSocket peerFacingDatagramSock) {
		SocketSettings socketSettings = this.getPeerFacingSocketSettings();
		try {
			socketSettings.applyTo(peerFacingDatagramSock);
		} catch (UnsupportedOperationException e) {
			LOGGER.error( 
					ObjectLogMessageHelper.objectLogMessage(
							this, 
							"Error in setting the peer-facing UDP socket"), 
					e);
			Socks5Reply socks5Rep = Socks5Reply.newFailureInstance(
					Reply.GENERAL_SOCKS_SERVER_FAILURE);
			this.commandWorkerContext.sendSocks5Reply(this, socks5Rep, LOGGER);
			return false;			
		} catch (SocketException e) {
			LOGGER.error( 
					ObjectLogMessageHelper.objectLogMessage(
							this, 
							"Error in setting the peer-facing UDP socket"), 
					e);
			Socks5Reply socks5Rep = Socks5Reply.newFailureInstance(
					Reply.GENERAL_SOCKS_SERVER_FAILURE);
			this.commandWorkerContext.sendSocks5Reply(this, socks5Rep, LOGGER);
			return false;
		}
		return true;
	}
	
	private Host getClientFacingBindHost() {
		Host host = this.applicableRule.getLastRuleResultValue(
				Socks5RuleResultSpecConstants.SOCKS5_ON_UDP_ASSOCIATE_CLIENT_FACING_BIND_HOST);
		if (host != null) {
			return host;
		}
		host = this.applicableRule.getLastRuleResultValue(
				Socks5RuleResultSpecConstants.SOCKS5_ON_COMMAND_INTERNAL_FACING_BIND_HOST);
		if (host != null) {
			return host;
		}
		host = this.applicableRule.getLastRuleResultValue(
				Socks5RuleResultSpecConstants.SOCKS5_ON_COMMAND_BIND_HOST);
		if (host != null) {
			return host;
		}
		host = this.applicableRule.getLastRuleResultValue(
				GeneralRuleResultSpecConstants.INTERNAL_FACING_BIND_HOST);
		if (host != null) {
			return host;
		}
		host = this.applicableRule.getLastRuleResultValue(
				GeneralRuleResultSpecConstants.BIND_HOST);
		if (host != null) {
			return host;
		}
		host = this.settings.getLastValue(
				Socks5SettingSpecConstants.SOCKS5_ON_UDP_ASSOCIATE_CLIENT_FACING_BIND_HOST);
		if (host != null) {
			return host;
		}
		host = this.settings.getLastValue(
				Socks5SettingSpecConstants.SOCKS5_ON_COMMAND_INTERNAL_FACING_BIND_HOST);
		if (host != null) {
			return host;
		}
		host = this.settings.getLastValue(
				Socks5SettingSpecConstants.SOCKS5_ON_COMMAND_BIND_HOST);
		if (host != null) {
			return host;
		}
		host = this.settings.getLastValue(
				GeneralSettingSpecConstants.INTERNAL_FACING_BIND_HOST);
		if (host != null) {
			return host;
		}
		host = this.settings.getLastValue(
				GeneralSettingSpecConstants.BIND_HOST);
		return host;
	}
	
	private PortRanges getClientFacingBindPortRanges() {
		List<PortRange> portRanges = this.applicableRule.getRuleResultValues(
				Socks5RuleResultSpecConstants.SOCKS5_ON_UDP_ASSOCIATE_CLIENT_FACING_BIND_PORT_RANGE);
		if (portRanges.size() > 0) {
			return PortRanges.newInstance(portRanges);
		}
		portRanges = this.applicableRule.getRuleResultValues(
				Socks5RuleResultSpecConstants.SOCKS5_ON_COMMAND_INTERNAL_FACING_BIND_UDP_PORT_RANGE);
		if (portRanges.size() > 0) {
			return PortRanges.newInstance(portRanges);
		}
		portRanges = this.applicableRule.getRuleResultValues(
				Socks5RuleResultSpecConstants.SOCKS5_ON_COMMAND_BIND_UDP_PORT_RANGE);
		if (portRanges.size() > 0) {
			return PortRanges.newInstance(portRanges);
		}
		portRanges = this.applicableRule.getRuleResultValues(
				GeneralRuleResultSpecConstants.INTERNAL_FACING_BIND_UDP_PORT_RANGE);
		if (portRanges.size() > 0) {
			return PortRanges.newInstance(portRanges);
		}
		portRanges = this.applicableRule.getRuleResultValues(
				GeneralRuleResultSpecConstants.BIND_UDP_PORT_RANGE);
		if (portRanges.size() > 0) {
			return PortRanges.newInstance(portRanges);
		}
		PortRanges prtRanges = this.settings.getLastValue(
				Socks5SettingSpecConstants.SOCKS5_ON_UDP_ASSOCIATE_CLIENT_FACING_BIND_PORT_RANGES);
		if (prtRanges.toList().size() > 0) {
			return prtRanges;
		}
		prtRanges = this.settings.getLastValue(
				Socks5SettingSpecConstants.SOCKS5_ON_COMMAND_INTERNAL_FACING_BIND_UDP_PORT_RANGES);
		if (prtRanges.toList().size() > 0) {
			return prtRanges;
		}
		prtRanges = this.settings.getLastValue(
				Socks5SettingSpecConstants.SOCKS5_ON_COMMAND_BIND_UDP_PORT_RANGES);
		if (prtRanges.toList().size() > 0) {
			return prtRanges;
		}
		prtRanges = this.settings.getLastValue(
				GeneralSettingSpecConstants.INTERNAL_FACING_BIND_UDP_PORT_RANGES);
		if (prtRanges.toList().size() > 0) {
			return prtRanges;
		}
		prtRanges = this.settings.getLastValue(
				GeneralSettingSpecConstants.BIND_UDP_PORT_RANGES);
		return prtRanges;
	}
	
	private SocketSettings getClientFacingSocketSettings() {
		List<SocketSetting<Object>> socketSettings =
				this.applicableRule.getRuleResultValues(
						Socks5RuleResultSpecConstants.SOCKS5_ON_UDP_ASSOCIATE_CLIENT_FACING_SOCKET_SETTING);
		if (socketSettings.size() > 0) {
			return SocketSettings.newInstance(
					socketSettings.stream().collect(Collectors.toList()));
		}
		socketSettings = this.applicableRule.getRuleResultValues(
				Socks5RuleResultSpecConstants.SOCKS5_ON_COMMAND_INTERNAL_FACING_SOCKET_SETTING);
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
				GeneralRuleResultSpecConstants.INTERNAL_FACING_SOCKET_SETTING);
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
				Socks5SettingSpecConstants.SOCKS5_ON_UDP_ASSOCIATE_CLIENT_FACING_SOCKET_SETTINGS);
		if (socketSttngs.toMap().size() > 0) {
			return socketSttngs;
		}
		socketSttngs = this.settings.getLastValue(
				Socks5SettingSpecConstants.SOCKS5_ON_COMMAND_INTERNAL_FACING_SOCKET_SETTINGS);
		if (socketSttngs.toMap().size() > 0) {
			return socketSttngs;
		}
		socketSttngs = this.settings.getLastValue(
				Socks5SettingSpecConstants.SOCKS5_ON_COMMAND_SOCKET_SETTINGS);
		if (socketSttngs.toMap().size() > 0) {
			return socketSttngs;
		}
		socketSttngs = this.settings.getLastValue(
				GeneralSettingSpecConstants.INTERNAL_FACING_SOCKET_SETTINGS);
		if (socketSttngs.toMap().size() > 0) {
			return socketSttngs;
		}
		socketSttngs = this.settings.getLastValue(
				GeneralSettingSpecConstants.SOCKET_SETTINGS);
		return socketSttngs;
	}
	
	private Host getPeerFacingBindHost() {
		Host host = this.applicableRule.getLastRuleResultValue(
				Socks5RuleResultSpecConstants.SOCKS5_ON_UDP_ASSOCIATE_PEER_FACING_BIND_HOST);
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
				Socks5SettingSpecConstants.SOCKS5_ON_UDP_ASSOCIATE_PEER_FACING_BIND_HOST);
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
	
	private PortRanges getPeerFacingBindPortRanges() {
		List<PortRange> portRanges = this.applicableRule.getRuleResultValues(
				Socks5RuleResultSpecConstants.SOCKS5_ON_UDP_ASSOCIATE_PEER_FACING_BIND_PORT_RANGE);
		if (portRanges.size() > 0) {
			return PortRanges.newInstance(portRanges);
		}
		portRanges = this.applicableRule.getRuleResultValues(
				Socks5RuleResultSpecConstants.SOCKS5_ON_COMMAND_EXTERNAL_FACING_BIND_UDP_PORT_RANGE);
		if (portRanges.size() > 0) {
			return PortRanges.newInstance(portRanges);
		}
		portRanges = this.applicableRule.getRuleResultValues(
				Socks5RuleResultSpecConstants.SOCKS5_ON_COMMAND_BIND_UDP_PORT_RANGE);
		if (portRanges.size() > 0) {
			return PortRanges.newInstance(portRanges);
		}
		portRanges = this.applicableRule.getRuleResultValues(
				GeneralRuleResultSpecConstants.EXTERNAL_FACING_BIND_UDP_PORT_RANGE);
		if (portRanges.size() > 0) {
			return PortRanges.newInstance(portRanges);
		}
		portRanges = this.applicableRule.getRuleResultValues(
				GeneralRuleResultSpecConstants.BIND_UDP_PORT_RANGE);
		if (portRanges.size() > 0) {
			return PortRanges.newInstance(portRanges);
		}
		PortRanges prtRanges = this.settings.getLastValue(
				Socks5SettingSpecConstants.SOCKS5_ON_UDP_ASSOCIATE_PEER_FACING_BIND_PORT_RANGES);
		if (prtRanges.toList().size() > 0) {
			return prtRanges;
		}
		prtRanges = this.settings.getLastValue(
				Socks5SettingSpecConstants.SOCKS5_ON_COMMAND_EXTERNAL_FACING_BIND_UDP_PORT_RANGES);
		if (prtRanges.toList().size() > 0) {
			return prtRanges;
		}
		prtRanges = this.settings.getLastValue(
				Socks5SettingSpecConstants.SOCKS5_ON_COMMAND_BIND_UDP_PORT_RANGES);
		if (prtRanges.toList().size() > 0) {
			return prtRanges;
		}
		prtRanges = this.settings.getLastValue(
				GeneralSettingSpecConstants.EXTERNAL_FACING_BIND_UDP_PORT_RANGES);
		if (prtRanges.toList().size() > 0) {
			return prtRanges;
		}
		prtRanges = this.settings.getLastValue(
				GeneralSettingSpecConstants.BIND_UDP_PORT_RANGES);
		return prtRanges;
	}
	
	private SocketSettings getPeerFacingSocketSettings() {
		List<SocketSetting<Object>> socketSettings =
				this.applicableRule.getRuleResultValues(
						Socks5RuleResultSpecConstants.SOCKS5_ON_UDP_ASSOCIATE_PEER_FACING_SOCKET_SETTING);
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
				Socks5SettingSpecConstants.SOCKS5_ON_UDP_ASSOCIATE_PEER_FACING_SOCKET_SETTINGS);
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
		socketSttngs = this.settings.getLastValue(
				GeneralSettingSpecConstants.SOCKET_SETTINGS);
		return socketSttngs;
	}
	
	private int getRelayBufferSize() {
		PositiveInteger relayBufferSize =
				this.applicableRule.getLastRuleResultValue(
						Socks5RuleResultSpecConstants.SOCKS5_ON_UDP_ASSOCIATE_RELAY_BUFFER_SIZE);
		if (relayBufferSize != null) {
			return relayBufferSize.intValue();
		}
		relayBufferSize = this.applicableRule.getLastRuleResultValue(
				Socks5RuleResultSpecConstants.SOCKS5_ON_COMMAND_RELAY_BUFFER_SIZE);
		if (relayBufferSize != null) {
			return relayBufferSize.intValue();
		}
		relayBufferSize = this.settings.getLastValue(
				Socks5SettingSpecConstants.SOCKS5_ON_UDP_ASSOCIATE_RELAY_BUFFER_SIZE);
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
						Socks5RuleResultSpecConstants.SOCKS5_ON_UDP_ASSOCIATE_RELAY_IDLE_TIMEOUT);
		if (relayIdleTimeout != null) {
			return relayIdleTimeout.intValue();
		}
		relayIdleTimeout = this.applicableRule.getLastRuleResultValue(
				Socks5RuleResultSpecConstants.SOCKS5_ON_COMMAND_RELAY_IDLE_TIMEOUT);
		if (relayIdleTimeout != null) {
			return relayIdleTimeout.intValue();
		}
		relayIdleTimeout = this.settings.getLastValue(
				Socks5SettingSpecConstants.SOCKS5_ON_UDP_ASSOCIATE_RELAY_IDLE_TIMEOUT);
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
						Socks5RuleResultSpecConstants.SOCKS5_ON_UDP_ASSOCIATE_RELAY_INBOUND_BANDWIDTH_LIMIT);
		if (relayInboundBandwidthLimit != null) {
			return Integer.valueOf(relayInboundBandwidthLimit.intValue());
		}
		relayInboundBandwidthLimit = this.applicableRule.getLastRuleResultValue(
				Socks5RuleResultSpecConstants.SOCKS5_ON_COMMAND_RELAY_INBOUND_BANDWIDTH_LIMIT);
		if (relayInboundBandwidthLimit != null) {
			return Integer.valueOf(relayInboundBandwidthLimit.intValue());
		}
		relayInboundBandwidthLimit = this.settings.getLastValue(
				Socks5SettingSpecConstants.SOCKS5_ON_UDP_ASSOCIATE_RELAY_INBOUND_BANDWIDTH_LIMIT);
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
						Socks5RuleResultSpecConstants.SOCKS5_ON_UDP_ASSOCIATE_RELAY_OUTBOUND_BANDWIDTH_LIMIT);
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
				Socks5SettingSpecConstants.SOCKS5_ON_UDP_ASSOCIATE_RELAY_OUTBOUND_BANDWIDTH_LIMIT);
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
	
	private DatagramSocket newClientFacingDatagramSocket() {
		Host bindHost = this.getClientFacingBindHost();
		InetAddress bindInetAddress = bindHost.toInetAddress();
		PortRanges bindPortRanges = this.getClientFacingBindPortRanges();
		DatagramSocket clientFacingDatagramSock = null;
		boolean clientFacingDatagramSockBound = false;
		for (Iterator<PortRange> iterator = bindPortRanges.toList().iterator();
				!clientFacingDatagramSockBound && iterator.hasNext();) {
			PortRange bindPortRange = iterator.next();
			for (Iterator<Port> iter = bindPortRange.iterator();
					!clientFacingDatagramSockBound && iter.hasNext();) {
				Port bindPort = iter.next();
				try {
					clientFacingDatagramSock = new DatagramSocket(null);
				} catch (SocketException e) {
					LOGGER.error( 
							ObjectLogMessageHelper.objectLogMessage(
									this, 
									"Error in creating the client-facing UDP "
									+ "socket"), 
							e);
					Socks5Reply socks5Rep = Socks5Reply.newFailureInstance(
							Reply.GENERAL_SOCKS_SERVER_FAILURE);
					this.commandWorkerContext.sendSocks5Reply(
							this, socks5Rep, LOGGER);
					if (clientFacingDatagramSock != null 
							&& !clientFacingDatagramSock.isClosed()) {
						clientFacingDatagramSock.close();
					}
					return null;
				}
				if (!this.configureClientFacingDatagramSocket(
						clientFacingDatagramSock)) {
					clientFacingDatagramSock.close();
					return null;
				}
				try {
					clientFacingDatagramSock.bind(new InetSocketAddress(
							bindInetAddress, bindPort.intValue()));
				} catch (BindException e) {
					clientFacingDatagramSock.close();
					continue;
				} catch (SocketException e) {
					LOGGER.error( 
							ObjectLogMessageHelper.objectLogMessage(
									this, 
									"Error in binding the client-facing UDP "
									+ "socket"), 
							e);
					Socks5Reply socks5Rep = Socks5Reply.newFailureInstance(
							Reply.GENERAL_SOCKS_SERVER_FAILURE);
					this.commandWorkerContext.sendSocks5Reply(
							this, socks5Rep, LOGGER);
					clientFacingDatagramSock.close();
					return null;
				}
				clientFacingDatagramSockBound = true;
			}
		}
		if (!clientFacingDatagramSockBound) {
			LOGGER.error( 
					ObjectLogMessageHelper.objectLogMessage(
							this, 
							"Unable to bind the client-facing UDP socket to "
							+ "the following address and port (range(s)): "
							+ "%s %s",
							bindInetAddress,
							bindPortRanges));
			Socks5Reply socks5Rep = Socks5Reply.newFailureInstance(
					Reply.GENERAL_SOCKS_SERVER_FAILURE);
			this.commandWorkerContext.sendSocks5Reply(
					this, socks5Rep, LOGGER);
			return null;			
		}
		return clientFacingDatagramSock;
	}
	
	private DatagramSocket newPeerFacingDatagramSocket() {
		Host bindHost = this.getPeerFacingBindHost();
		InetAddress bindInetAddress = bindHost.toInetAddress();
		PortRanges bindPortRanges = this.getPeerFacingBindPortRanges();
		DatagramSocket peerFacingDatagramSock = null;
		boolean peerFacingDatagramSockBound = false;
		for (Iterator<PortRange> iterator = bindPortRanges.toList().iterator();
				!peerFacingDatagramSockBound && iterator.hasNext();) {
			PortRange bindPortRange = iterator.next();
			for (Iterator<Port> iter = bindPortRange.iterator();
					!peerFacingDatagramSockBound && iter.hasNext();) {
				Port bindPort = iter.next();
				try {
					peerFacingDatagramSock = 
							this.netObjectFactory.newDatagramSocket(null);
				} catch (SocketException e) {
					LOGGER.error( 
							ObjectLogMessageHelper.objectLogMessage(
									this, 
									"Error in creating the peer-facing UDP "
									+ "socket"), 
							e);
					Socks5Reply socks5Rep = Socks5Reply.newFailureInstance(
							Reply.GENERAL_SOCKS_SERVER_FAILURE);
					this.commandWorkerContext.sendSocks5Reply(
							this, socks5Rep, LOGGER);
					if (peerFacingDatagramSock != null 
							&& !peerFacingDatagramSock.isClosed()) {
						peerFacingDatagramSock.close();
					}
					return null;
				}
				if (!this.configurePeerFacingDatagramSocket(
						peerFacingDatagramSock)) {
					peerFacingDatagramSock.close();
					return null;
				}
				try {
					peerFacingDatagramSock.bind(new InetSocketAddress(
							bindInetAddress, bindPort.intValue()));
				} catch (BindException e) {
					peerFacingDatagramSock.close();
					continue;
				} catch (SocketException e) {
					LOGGER.error( 
							ObjectLogMessageHelper.objectLogMessage(
									this, 
									"Error in binding the peer-facing UDP "
									+ "socket"), 
							e);
					Socks5Reply socks5Rep = Socks5Reply.newFailureInstance(
							Reply.GENERAL_SOCKS_SERVER_FAILURE);
					this.commandWorkerContext.sendSocks5Reply(
							this, socks5Rep, LOGGER);
					peerFacingDatagramSock.close();
					return null;
				}
				peerFacingDatagramSockBound = true;
			}
		}
		if (!peerFacingDatagramSockBound) {
			LOGGER.error( 
					ObjectLogMessageHelper.objectLogMessage(
							this, 
							"Unable to bind the peer-facing UDP socket to the "
							+ "following address and port (range(s)): %s %s",
							bindInetAddress,
							bindPortRanges));
			Socks5Reply socks5Rep = Socks5Reply.newFailureInstance(
					Reply.GENERAL_SOCKS_SERVER_FAILURE);
			this.commandWorkerContext.sendSocks5Reply(this, socks5Rep, LOGGER);
			return null;			
		}
		return peerFacingDatagramSock;
	}

	private void passPackets(
			final UdpRelayServer.Builder builder) throws IOException {
		UdpRelayServer udpRelayServer = builder.build();
		try {
			udpRelayServer.start();
			try {
				while (this.clientSocket.getInputStream().read() != -1) {
					try {
						Thread.sleep(HALF_SECOND);
					} catch (InterruptedException e) {
						Thread.currentThread().interrupt();
					}
				}
			} catch (IOException e) {
			}
		} finally {
			if (!udpRelayServer.getState().equals(
					UdpRelayServer.State.STOPPED)) {
				udpRelayServer.stop();
			}
		}
	}
	
	@Override
	public void run() throws IOException {
		DatagramSocket peerFacingDatagramSock = null;
		DatagramSocket clientFacingDatagramSock = null;		
		Socks5Reply socks5Rep = null;
		try {
			peerFacingDatagramSock = this.newPeerFacingDatagramSocket();
			if (peerFacingDatagramSock == null) {
				return;
			}
			clientFacingDatagramSock = this.newClientFacingDatagramSocket();
			if (clientFacingDatagramSock == null) {
				return;
			}
			DatagramSocket clientFacingDatagramSck = 
					this.wrapClientFacingDatagramSocket(
							clientFacingDatagramSock); 
			if (clientFacingDatagramSck == null) {
				return;
			}
			clientFacingDatagramSock = clientFacingDatagramSck;
			InetAddress inetAddress =
					clientFacingDatagramSock.getLocalAddress();
			String serverBoundAddress = inetAddress.getHostAddress();
			int serverBoundPort = clientFacingDatagramSock.getLocalPort();
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
			if (outboundBandwidthLimit != null) {
				clientFacingDatagramSock = new BandwidthLimitedDatagramSocket(
						clientFacingDatagramSock, 
						outboundBandwidthLimit.intValue());
			}
			if (inboundBandwidthLimit != null) {
				peerFacingDatagramSock = new BandwidthLimitedDatagramSocket(
						peerFacingDatagramSock,
						inboundBandwidthLimit.intValue());
			}
			UdpRelayServer.Builder builder = new UdpRelayServer.Builder(
					this.desiredDestinationAddress, 
					this.desiredDestinationPort,
					clientFacingDatagramSock, 
					peerFacingDatagramSock);
			builder.bufferSize(this.getRelayBufferSize());
			builder.hostResolver(this.netObjectFactory.newHostResolver());
			builder.idleTimeout(this.getRelayIdleTimeout());
			builder.ruleContext(socks5ReplyRuleContext);
			builder.rules(this.rules);
			try {
				this.passPackets(builder);
			} catch (IOException e) {
				LOGGER.error( 
						ObjectLogMessageHelper.objectLogMessage(
								this, "Error in starting the UDP association"), 
						e);
			}
		} finally {
			if (clientFacingDatagramSock != null 
					&& !clientFacingDatagramSock.isClosed()) {
				clientFacingDatagramSock.close();
			}
			if (peerFacingDatagramSock != null 
					&& !peerFacingDatagramSock.isClosed()) {
				peerFacingDatagramSock.close();
			}
		}
	}
	
	private DatagramSocket wrapClientFacingDatagramSocket(
			final DatagramSocket clientFacingDatagramSock) {
		DatagramSocket clientFacingDatagramSck = clientFacingDatagramSock;
		if (this.clientFacingDtlsDatagramSocketFactory != null) {
			try {
				clientFacingDatagramSck = 
						this.clientFacingDtlsDatagramSocketFactory.newDatagramSocket(
								clientFacingDatagramSck);
			} catch (IOException e) {
				LOGGER.error( 
						ObjectLogMessageHelper.objectLogMessage(
								this, 
								"Error in wrapping the client-facing UDP socket"), 
						e);
				Socks5Reply socks5Rep = Socks5Reply.newFailureInstance(
						Reply.GENERAL_SOCKS_SERVER_FAILURE);
				this.commandWorkerContext.sendSocks5Reply(
						this, socks5Rep, LOGGER);
				return null;
			}
		}
		try {
			clientFacingDatagramSck = 
					this.methodSubnegotiationResults.getDatagramSocket(
							clientFacingDatagramSck);
		} catch (IOException e) {
			LOGGER.error( 
					ObjectLogMessageHelper.objectLogMessage(
							this, 
							"Error in wrapping the client-facing UDP socket"), 
					e);
			Socks5Reply socks5Rep = Socks5Reply.newFailureInstance(
					Reply.GENERAL_SOCKS_SERVER_FAILURE);
			this.commandWorkerContext.sendSocks5Reply(this, socks5Rep, LOGGER);
			return null;
		}
		return clientFacingDatagramSck;
	}

}
