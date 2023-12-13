package com.github.jh3nd3rs0n.jargyle.server;

import java.util.List;
import java.util.Map;

import com.github.jh3nd3rs0n.jargyle.common.net.Host;
import com.github.jh3nd3rs0n.jargyle.common.net.Port;
import com.github.jh3nd3rs0n.jargyle.common.net.PortRange;
import com.github.jh3nd3rs0n.jargyle.common.net.SocketSetting;
import com.github.jh3nd3rs0n.jargyle.common.number.PositiveInteger;
import com.github.jh3nd3rs0n.jargyle.internal.annotation.NameValuePairValueSpecDoc;
import com.github.jh3nd3rs0n.jargyle.internal.annotation.NameValuePairValueSpecsDoc;
import com.github.jh3nd3rs0n.jargyle.protocolbase.socks5.Address;
import com.github.jh3nd3rs0n.jargyle.server.internal.ruleresultspec.impl.AddressRuleResultSpec;
import com.github.jh3nd3rs0n.jargyle.server.internal.ruleresultspec.impl.BooleanRuleResultSpec;
import com.github.jh3nd3rs0n.jargyle.server.internal.ruleresultspec.impl.HostRuleResultSpec;
import com.github.jh3nd3rs0n.jargyle.server.internal.ruleresultspec.impl.LogActionRuleResultSpec;
import com.github.jh3nd3rs0n.jargyle.server.internal.ruleresultspec.impl.PortRangeRuleResultSpec;
import com.github.jh3nd3rs0n.jargyle.server.internal.ruleresultspec.impl.PortRuleResultSpec;
import com.github.jh3nd3rs0n.jargyle.server.internal.ruleresultspec.impl.PositiveIntegerRuleResultSpec;
import com.github.jh3nd3rs0n.jargyle.server.internal.ruleresultspec.impl.SocketSettingRuleResultSpec;

@NameValuePairValueSpecsDoc(
		description = "",
		name = "SOCKS5 Rule Results"
)
public final class Socks5RuleResultSpecConstants {

	private static final RuleResultSpecs RULE_RESULT_SPECS = new RuleResultSpecs();

	@NameValuePairValueSpecDoc(
			description = "Specifies the desired destination address redirect",
			name = "socks5.desiredDestinationAddressRedirect",
			syntax = "socks5.desiredDestinationAddressRedirect=SOCKS5_ADDRESS",
			valueType = Address.class
	)	
	public static final RuleResultSpec<Address> SOCKS5_DESIRED_DESTINATION_ADDRESS_REDIRECT = RULE_RESULT_SPECS.addThenGet(new AddressRuleResultSpec(
			"socks5.desiredDestinationAddressRedirect"));
	
	@NameValuePairValueSpecDoc(
			description = "Specifies the desired destination port redirect",
			name = "socks5.desiredDestinationPortRedirect",
			syntax = "socks5.desiredDestinationPortRedirect=PORT",
			valueType = Port.class
	)	
	public static final RuleResultSpec<Port> SOCKS5_DESIRED_DESTINATION_PORT_REDIRECT = RULE_RESULT_SPECS.addThenGet(new PortRuleResultSpec(
			"socks5.desiredDestinationPortRedirect"));
	
	@NameValuePairValueSpecDoc(
			description = "Specifies the logging action to take if the desired "
					+ "destination is redirected",
			name = "socks5.desiredDestinationRedirectLogAction",
			syntax = "socks5.desiredDestinationRedirectLogAction=LOG_ACTION",
			valueType = LogAction.class
	)	
	public static final RuleResultSpec<LogAction> SOCKS5_DESIRED_DESTINATION_REDIRECT_LOG_ACTION = RULE_RESULT_SPECS.addThenGet(new LogActionRuleResultSpec(
			"socks5.desiredDestinationRedirectLogAction"));
	
	@NameValuePairValueSpecDoc(
			description = "Specifies a socket setting for the inbound socket "
					+ "(can be specified multiple times with each rule result "
					+ "specifying another socket setting)",
			name = "socks5.onBind.inboundSocketSetting",
			syntax = "socks5.onBind.inboundSocketSetting=SOCKET_SETTING",
			valueType = SocketSetting.class
	)	
	public static final RuleResultSpec<SocketSetting<Object>> SOCKS5_ON_BIND_INBOUND_SOCKET_SETTING = RULE_RESULT_SPECS.addThenGet(new SocketSettingRuleResultSpec(
			"socks5.onBind.inboundSocketSetting"));

	@NameValuePairValueSpecDoc(
			description = "Specifies the binding host name or address for the "
					+ "listen socket if the provided host address is all zeros",
			name = "socks5.onBind.listenBindHost",
			syntax = "socks5.onBind.listenBindHost=HOST",
			valueType = Host.class
	)
	public static final RuleResultSpec<Host> SOCKS5_ON_BIND_LISTEN_BIND_HOST = RULE_RESULT_SPECS.addThenGet(new HostRuleResultSpec(
			"socks5.onBind.listenBindHost"));
	
	@NameValuePairValueSpecDoc(
			description = "Specifies a binding port range for the listen socket "
					+ "if the provided port is zero (can be specified multiple "
					+ "times with each rule result specifying another port "
					+ "range)",
			name = "socks5.onBind.listenBindPortRange",
			syntax = "socks5.onBind.listenBindPortRange=PORT_RANGE",
			valueType = PortRange.class
	)
	public static final RuleResultSpec<PortRange> SOCKS5_ON_BIND_LISTEN_BIND_PORT_RANGE = RULE_RESULT_SPECS.addThenGet(new PortRangeRuleResultSpec(
			"socks5.onBind.listenBindPortRange"));
	
	@NameValuePairValueSpecDoc(
			description = "Specifies a socket setting for the listen socket "
					+ "(can be specified multiple times with each rule result "
					+ "specifying another socket setting)",
			name = "socks5.onBind.listenSocketSetting",
			syntax = "socks5.onBind.listenSocketSetting=SOCKET_SETTING",
			valueType = SocketSetting.class
	)	
	public static final RuleResultSpec<SocketSetting<Object>> SOCKS5_ON_BIND_LISTEN_SOCKET_SETTING = RULE_RESULT_SPECS.addThenGet(new SocketSettingRuleResultSpec(
			"socks5.onBind.listenSocketSetting"));
	
	@NameValuePairValueSpecDoc(
			description = "Specifies the buffer size in bytes for relaying the "
					+ "data",
			name = "socks5.onBind.relayBufferSize",
			syntax = "socks5.onBind.relayBufferSize=POSITIVE_INTEGER",
			valueType = PositiveInteger.class
	)	
	public static final RuleResultSpec<PositiveInteger> SOCKS5_ON_BIND_RELAY_BUFFER_SIZE = RULE_RESULT_SPECS.addThenGet(new PositiveIntegerRuleResultSpec(
			"socks5.onBind.relayBufferSize"));
	
	@NameValuePairValueSpecDoc(
			description = "Specifies the timeout in milliseconds on relaying "
					+ "no data",
			name = "socks5.onBind.relayIdleTimeout",
			syntax = "socks5.onBind.relayIdleTimeout=POSITIVE_INTEGER",
			valueType = PositiveInteger.class
	)	
	public static final RuleResultSpec<PositiveInteger> SOCKS5_ON_BIND_RELAY_IDLE_TIMEOUT = RULE_RESULT_SPECS.addThenGet(new PositiveIntegerRuleResultSpec(
			"socks5.onBind.relayIdleTimeout"));

	@NameValuePairValueSpecDoc(
			description = "Specifies the upper limit on bandwidth in bytes per "
					+ "second of receiving inbound data to be relayed",
			name = "socks5.onBind.relayInboundBandwidthLimit",
			syntax = "socks5.onBind.relayInboundBandwidthLimit=POSITIVE_INTEGER",
			valueType = PositiveInteger.class
	)
	public static final RuleResultSpec<PositiveInteger> SOCKS5_ON_BIND_RELAY_INBOUND_BANDWIDTH_LIMIT = RULE_RESULT_SPECS.addThenGet(new PositiveIntegerRuleResultSpec(
			"socks5.onBind.relayInboundBandwidthLimit"));

	@NameValuePairValueSpecDoc(
			description = "Specifies the upper limit on bandwidth in bytes per "
					+ "second of receiving outbound data to be relayed",
			name = "socks5.onBind.relayOutboundBandwidthLimit",
			syntax = "socks5.onBind.relayOutboundBandwidthLimit=POSITIVE_INTEGER",
			valueType = PositiveInteger.class
	)
	public static final RuleResultSpec<PositiveInteger> SOCKS5_ON_BIND_RELAY_OUTBOUND_BANDWIDTH_LIMIT = RULE_RESULT_SPECS.addThenGet(new PositiveIntegerRuleResultSpec(
			"socks5.onBind.relayOutboundBandwidthLimit"));

	@NameValuePairValueSpecDoc(
			description = "Specifies the binding host name or address for all "
					+ "sockets",
			name = "socks5.onCommand.bindHost",
			syntax = "socks5.onCommand.bindHost=HOST",
			valueType = Host.class
	)
	public static final RuleResultSpec<Host> SOCKS5_ON_COMMAND_BIND_HOST = RULE_RESULT_SPECS.addThenGet(new HostRuleResultSpec(
			"socks5.onCommand.bindHost"));
	
	@NameValuePairValueSpecDoc(
			description = "Specifies a binding port range for all TCP sockets "
					+ "(can be specified multiple times with each rule result "
					+ "specifying another port range)",
			name = "socks5.onCommand.bindTcpPortRange",
			syntax = "socks5.onCommand.bindTcpPortRange=PORT_RANGE",
			valueType = PortRange.class
	)
	public static final RuleResultSpec<PortRange> SOCKS5_ON_COMMAND_BIND_TCP_PORT_RANGE = RULE_RESULT_SPECS.addThenGet(new PortRangeRuleResultSpec(
			"socks5.onCommand.bindTcpPortRange"));
	
	@NameValuePairValueSpecDoc(
			description = "Specifies a binding port range for all UDP sockets "
					+ "(can be specified multiple times with each rule result "
					+ "specifying another port range)",
			name = "socks5.onCommand.bindUdpPortRange",
			syntax = "socks5.onCommand.bindUdpPortRange=PORT_RANGE",
			valueType = PortRange.class
	)
	public static final RuleResultSpec<PortRange> SOCKS5_ON_COMMAND_BIND_UDP_PORT_RANGE = RULE_RESULT_SPECS.addThenGet(new PortRangeRuleResultSpec(
			"socks5.onCommand.bindUdpPortRange"));

	@NameValuePairValueSpecDoc(
			description = "Specifies the binding host name or address for all "
					+ "external-facing sockets",
			name = "socks5.onCommand.externalFacingBindHost",
			syntax = "socks5.onCommand.externalFacingBindHost=HOST",
			valueType = Host.class
	)
	public static final RuleResultSpec<Host> SOCKS5_ON_COMMAND_EXTERNAL_FACING_BIND_HOST = RULE_RESULT_SPECS.addThenGet(new HostRuleResultSpec(
			"socks5.onCommand.externalFacingBindHost"));
	
	@NameValuePairValueSpecDoc(
			description = "Specifies a binding port range for all "
					+ "external-facing TCP sockets (can be specified multiple "
					+ "times with each rule result specifying another port "
					+ "range)",
			name = "socks5.onCommand.externalFacingBindTcpPortRange",
			syntax = "socks5.onCommand.externalFacingBindTcpPortRange=PORT_RANGE",
			valueType = PortRange.class
	)
	public static final RuleResultSpec<PortRange> SOCKS5_ON_COMMAND_EXTERNAL_FACING_BIND_TCP_PORT_RANGE = RULE_RESULT_SPECS.addThenGet(new PortRangeRuleResultSpec(
			"socks5.onCommand.externalFacingBindTcpPortRange"));
	
	@NameValuePairValueSpecDoc(
			description = "Specifies a binding port range for all "
					+ "external-facing UDP sockets (can be specified multiple "
					+ "times with each rule result specifying another port "
					+ "range)",
			name = "socks5.onCommand.externalFacingBindUdpPortRange",
			syntax = "socks5.onCommand.externalFacingBindUdpPortRange=PORT_RANGE",
			valueType = PortRange.class
	)
	public static final RuleResultSpec<PortRange> SOCKS5_ON_COMMAND_EXTERNAL_FACING_BIND_UDP_PORT_RANGE = RULE_RESULT_SPECS.addThenGet(new PortRangeRuleResultSpec(
			"socks5.onCommand.externalFacingBindUdpPortRange"));
	
	@NameValuePairValueSpecDoc(
			description = "Specifies a socket setting for all external-facing "
					+ "sockets (can be specified multiple times with each rule "
					+ "result specifying another socket setting)",
			name = "socks5.onCommand.externalFacingSocketSetting",
			syntax = "socks5.onCommand.externalFacingSocketSetting=SOCKET_SETTING",
			valueType = SocketSetting.class
	)
	public static final RuleResultSpec<SocketSetting<Object>> SOCKS5_ON_COMMAND_EXTERNAL_FACING_SOCKET_SETTING = RULE_RESULT_SPECS.addThenGet(new SocketSettingRuleResultSpec(
			"socks5.onCommand.externalFacingSocketSetting"));
	
	@NameValuePairValueSpecDoc(
			description = "Specifies the binding host name or address for all "
					+ "internal-facing sockets",
			name = "socks5.onCommand.internalFacingBindHost",
			syntax = "socks5.onCommand.internalFacingBindHost=HOST",
			valueType = Host.class
	)
	public static final RuleResultSpec<Host> SOCKS5_ON_COMMAND_INTERNAL_FACING_BIND_HOST = RULE_RESULT_SPECS.addThenGet(new HostRuleResultSpec(
			"socks5.onCommand.internalFacingBindHost"));

	@NameValuePairValueSpecDoc(
			description = "Specifies a binding port range for all "
					+ "internal-facing UDP sockets (can be specified multiple "
					+ "times with each rule result specifying another port "
					+ "range)",
			name = "socks5.onCommand.internalFacingBindUdpPortRange",
			syntax = "socks5.onCommand.internalFacingBindUdpPortRange=PORT_RANGE",
			valueType = PortRange.class
	)
	public static final RuleResultSpec<PortRange> SOCKS5_ON_COMMAND_INTERNAL_FACING_BIND_UDP_PORT_RANGE = RULE_RESULT_SPECS.addThenGet(new PortRangeRuleResultSpec(
			"socks5.onCommand.internalFacingBindUdpPortRange"));
	
	@NameValuePairValueSpecDoc(
			description = "Specifies a socket setting for all internal-facing "
					+ "sockets (can be specified multiple times with each rule "
					+ "result specifying another socket setting)",
			name = "socks5.onCommand.internalFacingSocketSetting",
			syntax = "socks5.onCommand.internalFacingSocketSetting=SOCKET_SETTING",
			valueType = SocketSetting.class
	)
	public static final RuleResultSpec<SocketSetting<Object>> SOCKS5_ON_COMMAND_INTERNAL_FACING_SOCKET_SETTING = RULE_RESULT_SPECS.addThenGet(new SocketSettingRuleResultSpec(
			"socks5.onCommand.internalFacingSocketSetting"));
	
	@NameValuePairValueSpecDoc(
			description = "Specifies the buffer size in bytes for relaying the "
					+ "data",
			name = "socks5.onCommand.relayBufferSize",
			syntax = "socks5.onCommand.relayBufferSize=POSITIVE_INTEGER",
			valueType = PositiveInteger.class
	)	
	public static final RuleResultSpec<PositiveInteger> SOCKS5_ON_COMMAND_RELAY_BUFFER_SIZE = RULE_RESULT_SPECS.addThenGet(new PositiveIntegerRuleResultSpec(
			"socks5.onCommand.relayBufferSize"));
	
	@NameValuePairValueSpecDoc(
			description = "Specifies the timeout in milliseconds on relaying "
					+ "no data",
			name = "socks5.onCommand.relayIdleTimeout",
			syntax = "socks5.onCommand.relayIdleTimeout=POSITIVE_INTEGER",
			valueType = PositiveInteger.class
	)	
	public static final RuleResultSpec<PositiveInteger> SOCKS5_ON_COMMAND_RELAY_IDLE_TIMEOUT = RULE_RESULT_SPECS.addThenGet(new PositiveIntegerRuleResultSpec(
			"socks5.onCommand.relayIdleTimeout"));

	@NameValuePairValueSpecDoc(
			description = "Specifies the upper limit on bandwidth in bytes per "
					+ "second of receiving inbound data to be relayed",
			name = "socks5.onCommand.relayInboundBandwidthLimit",
			syntax = "socks5.onCommand.relayInboundBandwidthLimit=POSITIVE_INTEGER",
			valueType = PositiveInteger.class
	)
	public static final RuleResultSpec<PositiveInteger> SOCKS5_ON_COMMAND_RELAY_INBOUND_BANDWIDTH_LIMIT = RULE_RESULT_SPECS.addThenGet(new PositiveIntegerRuleResultSpec(
			"socks5.onCommand.relayInboundBandwidthLimit"));

	@NameValuePairValueSpecDoc(
			description = "Specifies the upper limit on bandwidth in bytes per "
					+ "second of receiving outbound data to be relayed",
			name = "socks5.onCommand.relayOutboundBandwidthLimit",
			syntax = "socks5.onCommand.relayOutboundBandwidthLimit=POSITIVE_INTEGER",
			valueType = PositiveInteger.class
	)
	public static final RuleResultSpec<PositiveInteger> SOCKS5_ON_COMMAND_RELAY_OUTBOUND_BANDWIDTH_LIMIT = RULE_RESULT_SPECS.addThenGet(new PositiveIntegerRuleResultSpec(
			"socks5.onCommand.relayOutboundBandwidthLimit"));
	
	@NameValuePairValueSpecDoc(
			description = "Specifies a socket setting for all sockets (can be "
					+ "specified multiple times with each rule result "
					+ "specifying another socket setting)",
			name = "socks5.onCommand.socketSetting",
			syntax = "socks5.onCommand.socketSetting=SOCKET_SETTING",
			valueType = SocketSetting.class
	)
	public static final RuleResultSpec<SocketSetting<Object>> SOCKS5_ON_COMMAND_SOCKET_SETTING = RULE_RESULT_SPECS.addThenGet(new SocketSettingRuleResultSpec(
			"socks5.onCommand.socketSetting"));
	
	@NameValuePairValueSpecDoc(
			description = "Specifies the boolean value to indicate if the "
					+ "server-facing socket is to be prepared before "
					+ "connecting (involves applying the specified socket "
					+ "settings, resolving the target host name, and setting "
					+ "the specified timeout on waiting to connect)",
			name = "socks5.onConnect.prepareServerFacingSocket",
			syntax = "socks5.onConnect.prepareServerFacingSocket=true|false",
			valueType = Boolean.class
	)	
	public static final RuleResultSpec<Boolean> SOCKS5_ON_CONNECT_PREPARE_SERVER_FACING_SOCKET = RULE_RESULT_SPECS.addThenGet(new BooleanRuleResultSpec(
			"socks5.onConnect.prepareServerFacingSocket"));

	@NameValuePairValueSpecDoc(
			description = "Specifies the buffer size in bytes for relaying the "
					+ "data",
			name = "socks5.onConnect.relayBufferSize",
			syntax = "socks5.onConnect.relayBufferSize=POSITIVE_INTEGER",
			valueType = PositiveInteger.class
	)	
	public static final RuleResultSpec<PositiveInteger> SOCKS5_ON_CONNECT_RELAY_BUFFER_SIZE = RULE_RESULT_SPECS.addThenGet(new PositiveIntegerRuleResultSpec(
			"socks5.onConnect.relayBufferSize"));
	
	@NameValuePairValueSpecDoc(
			description = "Specifies the timeout in milliseconds on relaying "
					+ "no data",
			name = "socks5.onConnect.relayIdleTimeout",
			syntax = "socks5.onConnect.relayIdleTimeout=POSITIVE_INTEGER",
			valueType = PositiveInteger.class
	)	
	public static final RuleResultSpec<PositiveInteger> SOCKS5_ON_CONNECT_RELAY_IDLE_TIMEOUT = RULE_RESULT_SPECS.addThenGet(new PositiveIntegerRuleResultSpec(
			"socks5.onConnect.relayIdleTimeout"));

	@NameValuePairValueSpecDoc(
			description = "Specifies the upper limit on bandwidth in bytes per "
					+ "second of receiving inbound data to be relayed",
			name = "socks5.onConnect.relayInboundBandwidthLimit",
			syntax = "socks5.onConnect.relayInboundBandwidthLimit=POSITIVE_INTEGER",
			valueType = PositiveInteger.class
	)
	public static final RuleResultSpec<PositiveInteger> SOCKS5_ON_CONNECT_RELAY_INBOUND_BANDWIDTH_LIMIT = RULE_RESULT_SPECS.addThenGet(new PositiveIntegerRuleResultSpec(
			"socks5.onConnect.relayInboundBandwidthLimit"));

	@NameValuePairValueSpecDoc(
			description = "Specifies the upper limit on bandwidth in bytes per "
					+ "second of receiving outbound data to be relayed",
			name = "socks5.onConnect.relayOutboundBandwidthLimit",
			syntax = "socks5.onConnect.relayOutboundBandwidthLimit=POSITIVE_INTEGER",
			valueType = PositiveInteger.class
	)
	public static final RuleResultSpec<PositiveInteger> SOCKS5_ON_CONNECT_RELAY_OUTBOUND_BANDWIDTH_LIMIT = RULE_RESULT_SPECS.addThenGet(new PositiveIntegerRuleResultSpec(
			"socks5.onConnect.relayOutboundBandwidthLimit"));
	
	@NameValuePairValueSpecDoc(
			description = "Specifies the binding host name or address for the "
					+ "server-facing socket",
			name = "socks5.onConnect.serverFacingBindHost",
			syntax = "socks5.onConnect.serverFacingBindHost=HOST",
			valueType = Host.class
	)	
	public static final RuleResultSpec<Host> SOCKS5_ON_CONNECT_SERVER_FACING_BIND_HOST = RULE_RESULT_SPECS.addThenGet(new HostRuleResultSpec(
			"socks5.onConnect.serverFacingBindHost"));
	
	@NameValuePairValueSpecDoc(
			description = "Specifies a binding port range for the server-facing "
					+ "socket (can be specified multiple times with each rule "
					+ "result specifying another port range)",
			name = "socks5.onConnect.serverFacingBindPortRange",
			syntax = "socks5.onConnect.serverFacingBindPortRange=PORT_RANGE",
			valueType = PortRange.class
	)	
	public static final RuleResultSpec<PortRange> SOCKS5_ON_CONNECT_SERVER_FACING_BIND_PORT_RANGE = RULE_RESULT_SPECS.addThenGet(new PortRangeRuleResultSpec(
			"socks5.onConnect.serverFacingBindPortRange"));
	
	@NameValuePairValueSpecDoc(
			description = "Specifies the timeout in milliseconds on waiting for "
					+ "the server-facing socket to connect",
			name = "socks5.onConnect.serverFacingConnectTimeout",
			syntax = "socks5.onConnect.serverFacingConnectTimeout=POSITIVE_INTEGER",
			valueType = PositiveInteger.class
	)	
	public static final RuleResultSpec<PositiveInteger> SOCKS5_ON_CONNECT_SERVER_FACING_CONNECT_TIMEOUT = RULE_RESULT_SPECS.addThenGet(new PositiveIntegerRuleResultSpec(
			"socks5.onConnect.serverFacingConnectTimeout"));
	
	@NameValuePairValueSpecDoc(
			description = "Specifies a socket setting for the server-facing "
					+ "socket (can be specified multiple times with each rule "
					+ "result specifying another socket setting)",
			name = "socks5.onConnect.serverFacingSocketSetting",
			syntax = "socks5.onConnect.serverFacingSocketSetting=SOCKET_SETTING",
			valueType = SocketSetting.class
	)	
	public static final RuleResultSpec<SocketSetting<Object>> SOCKS5_ON_CONNECT_SERVER_FACING_SOCKET_SETTING = RULE_RESULT_SPECS.addThenGet(new SocketSettingRuleResultSpec(
			"socks5.onConnect.serverFacingSocketSetting"));

	@NameValuePairValueSpecDoc(
			description = "Specifies the binding host name or address for the "
					+ "client-facing UDP socket",
			name = "socks5.onUdpAssociate.clientFacingBindHost",
			syntax = "socks5.onUdpAssociate.clientFacingBindHost=HOST",
			valueType = Host.class
	)
	public static final RuleResultSpec<Host> SOCKS5_ON_UDP_ASSOCIATE_CLIENT_FACING_BIND_HOST = RULE_RESULT_SPECS.addThenGet(new HostRuleResultSpec(
			"socks5.onUdpAssociate.clientFacingBindHost"));
	
	@NameValuePairValueSpecDoc(
			description = "Specifies a binding port range for the client-facing "
					+ "UDP socket (can be specified multiple times with each "
					+ "rule result specifying another port range)",
			name = "socks5.onUdpAssociate.clientFacingBindPortRange",
			syntax = "socks5.onUdpAssociate.clientFacingBindPortRange=PORT_RANGE",
			valueType = PortRange.class
	)	
	public static final RuleResultSpec<PortRange> SOCKS5_ON_UDP_ASSOCIATE_CLIENT_FACING_BIND_PORT_RANGE = RULE_RESULT_SPECS.addThenGet(new PortRangeRuleResultSpec(
			"socks5.onUdpAssociate.clientFacingBindPortRange"));
	
	@NameValuePairValueSpecDoc(
			description = "Specifies a socket setting for the client-facing UDP "
					+ "socket (can be specified multiple times with each rule "
					+ "result specifying another socket setting)",
			name = "socks5.onUdpAssociate.clientFacingSocketSetting",
			syntax = "socks5.onUdpAssociate.clientFacingSocketSetting=SOCKET_SETTING",
			valueType = SocketSetting.class
	)	
	public static final RuleResultSpec<SocketSetting<Object>> SOCKS5_ON_UDP_ASSOCIATE_CLIENT_FACING_SOCKET_SETTING = RULE_RESULT_SPECS.addThenGet(new SocketSettingRuleResultSpec(
			"socks5.onUdpAssociate.clientFacingSocketSetting"));
	
	@NameValuePairValueSpecDoc(
			description = "Specifies the binding host name or address for the "
					+ "peer-facing UDP socket",
			name = "socks5.onUdpAssociate.peerFacingBindHost",
			syntax = "socks5.onUdpAssociate.peerFacingBindHost=HOST",
			valueType = Host.class
	)	
	public static final RuleResultSpec<Host> SOCKS5_ON_UDP_ASSOCIATE_PEER_FACING_BIND_HOST = RULE_RESULT_SPECS.addThenGet(new HostRuleResultSpec(
			"socks5.onUdpAssociate.peerFacingBindHost"));

	@NameValuePairValueSpecDoc(
			description = "Specifies a binding port range for the peer-facing "
					+ "UDP socket (can be specified multiple times with each "
					+ "rule result specifying another port range)",
			name = "socks5.onUdpAssociate.peerFacingBindPortRange",
			syntax = "socks5.onUdpAssociate.peerFacingBindPortRange=PORT_RANGE",
			valueType = PortRange.class
	)	
	public static final RuleResultSpec<PortRange> SOCKS5_ON_UDP_ASSOCIATE_PEER_FACING_BIND_PORT_RANGE = RULE_RESULT_SPECS.addThenGet(new PortRangeRuleResultSpec(
			"socks5.onUdpAssociate.peerFacingBindPortRange"));
	
	@NameValuePairValueSpecDoc(
			description = "Specifies a socket setting for the peer-facing UDP "
					+ "socket (can be specified multiple times with each rule "
					+ "result specifying another socket setting)",
			name = "socks5.onUdpAssociate.peerFacingSocketSetting",
			syntax = "socks5.onUdpAssociate.peerFacingSocketSetting=SOCKET_SETTING",
			valueType = SocketSetting.class
	)	
	public static final RuleResultSpec<SocketSetting<Object>> SOCKS5_ON_UDP_ASSOCIATE_PEER_FACING_SOCKET_SETTING = RULE_RESULT_SPECS.addThenGet(new SocketSettingRuleResultSpec(
			"socks5.onUdpAssociate.peerFacingSocketSetting"));
	
	@NameValuePairValueSpecDoc(
			description = "Specifies the buffer size in bytes for relaying the "
					+ "data",
			name = "socks5.onUdpAssociate.relayBufferSize",
			syntax = "socks5.onUdpAssociate.relayBufferSize=POSITIVE_INTEGER",
			valueType = PositiveInteger.class
	)	
	public static final RuleResultSpec<PositiveInteger> SOCKS5_ON_UDP_ASSOCIATE_RELAY_BUFFER_SIZE = RULE_RESULT_SPECS.addThenGet(new PositiveIntegerRuleResultSpec(
			"socks5.onUdpAssociate.relayBufferSize"));
	
	@NameValuePairValueSpecDoc(
			description = "Specifies the timeout in milliseconds on relaying no "
					+ "data",
			name = "socks5.onUdpAssociate.relayIdleTimeout",
			syntax = "socks5.onUdpAssociate.relayIdleTimeout=POSITIVE_INTEGER",
			valueType = PositiveInteger.class
	)	
	public static final RuleResultSpec<PositiveInteger> SOCKS5_ON_UDP_ASSOCIATE_RELAY_IDLE_TIMEOUT = RULE_RESULT_SPECS.addThenGet(new PositiveIntegerRuleResultSpec(
			"socks5.onUdpAssociate.relayIdleTimeout"));

	@NameValuePairValueSpecDoc(
			description = "Specifies the upper limit on bandwidth in bytes per "
					+ "second of receiving inbound data to be relayed",
			name = "socks5.onUdpAssociate.relayInboundBandwidthLimit",
			syntax = "socks5.onUdpAssociate.relayInboundBandwidthLimit=POSITIVE_INTEGER",
			valueType = PositiveInteger.class
	)
	public static final RuleResultSpec<PositiveInteger> SOCKS5_ON_UDP_ASSOCIATE_RELAY_INBOUND_BANDWIDTH_LIMIT = RULE_RESULT_SPECS.addThenGet(new PositiveIntegerRuleResultSpec(
			"socks5.onUdpAssociate.relayInboundBandwidthLimit"));

	@NameValuePairValueSpecDoc(
			description = "Specifies the upper limit on bandwidth in bytes per "
					+ "second of receiving outbound data to be relayed",
			name = "socks5.onUdpAssociate.relayOutboundBandwidthLimit",
			syntax = "socks5.onUdpAssociate.relayOutboundBandwidthLimit=POSITIVE_INTEGER",
			valueType = PositiveInteger.class
	)
	public static final RuleResultSpec<PositiveInteger> SOCKS5_ON_UDP_ASSOCIATE_RELAY_OUTBOUND_BANDWIDTH_LIMIT = RULE_RESULT_SPECS.addThenGet(new PositiveIntegerRuleResultSpec(
			"socks5.onUdpAssociate.relayOutboundBandwidthLimit"));
	
	public static List<RuleResultSpec<Object>> values() {
		return RULE_RESULT_SPECS.toList();
	}
	
	public static Map<String, RuleResultSpec<Object>> valuesMap() {
		return RULE_RESULT_SPECS.toMap();
	}
	
	private Socks5RuleResultSpecConstants() { }

}
