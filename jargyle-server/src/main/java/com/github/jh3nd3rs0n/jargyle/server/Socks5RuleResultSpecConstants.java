package com.github.jh3nd3rs0n.jargyle.server;

import java.util.List;
import java.util.Map;

import com.github.jh3nd3rs0n.jargyle.common.lang.PositiveInteger;
import com.github.jh3nd3rs0n.jargyle.common.net.Host;
import com.github.jh3nd3rs0n.jargyle.common.net.Port;
import com.github.jh3nd3rs0n.jargyle.common.net.PortRange;
import com.github.jh3nd3rs0n.jargyle.common.net.SocketSetting;
import com.github.jh3nd3rs0n.jargyle.internal.annotation.HelpText;
import com.github.jh3nd3rs0n.jargyle.server.internal.ruleresultspec.impl.AddressRuleResultSpec;
import com.github.jh3nd3rs0n.jargyle.server.internal.ruleresultspec.impl.BooleanRuleResultSpec;
import com.github.jh3nd3rs0n.jargyle.server.internal.ruleresultspec.impl.HostRuleResultSpec;
import com.github.jh3nd3rs0n.jargyle.server.internal.ruleresultspec.impl.LogActionRuleResultSpec;
import com.github.jh3nd3rs0n.jargyle.server.internal.ruleresultspec.impl.PortRangeRuleResultSpec;
import com.github.jh3nd3rs0n.jargyle.server.internal.ruleresultspec.impl.PortRuleResultSpec;
import com.github.jh3nd3rs0n.jargyle.server.internal.ruleresultspec.impl.PositiveIntegerRuleResultSpec;
import com.github.jh3nd3rs0n.jargyle.server.internal.ruleresultspec.impl.SocketSettingRuleResultSpec;
import com.github.jh3nd3rs0n.jargyle.transport.socks5.Address;

public final class Socks5RuleResultSpecConstants {

	private static final RuleResultSpecs RULE_RESULT_SPECS = new RuleResultSpecs();

	@HelpText(
			doc = "Specifies the desired destination address redirect",
			usage = "socks5.desiredDestinationAddressRedirect=ADDRESS"
	)	
	public static final RuleResultSpec<Address> SOCKS5_DESIRED_DESTINATION_ADDRESS_REDIRECT = RULE_RESULT_SPECS.addThenGet(new AddressRuleResultSpec(
			"socks5.desiredDestinationAddressRedirect"));
	
	@HelpText(
			doc = "Specifies the desired destination port redirect",
			usage = "socks5.desiredDestinationPortRedirect=PORT"
	)	
	public static final RuleResultSpec<Port> SOCKS5_DESIRED_DESTINATION_PORT_REDIRECT = RULE_RESULT_SPECS.addThenGet(new PortRuleResultSpec(
			"socks5.desiredDestinationPortRedirect"));
	
	@HelpText(
			doc = "Specifies the logging action to take if the desired "
					+ "destination is redirected",
			usage = "socks5.desiredDestinationRedirectLogAction=LOG_ACTION"
	)	
	public static final RuleResultSpec<LogAction> SOCKS5_DESIRED_DESTINATION_REDIRECT_LOG_ACTION = RULE_RESULT_SPECS.addThenGet(new LogActionRuleResultSpec(
			"socks5.desiredDestinationRedirectLogAction"));
	
	@HelpText(
			doc = "Specifies a socket setting for the inbound socket (can be "
					+ "specified multiple times with each rule result "
					+ "specifying another socket setting)",
			usage = "socks5.onBind.inboundSocketSetting=SOCKET_SETTING"
	)	
	public static final RuleResultSpec<SocketSetting<Object>> SOCKS5_ON_BIND_INBOUND_SOCKET_SETTING = RULE_RESULT_SPECS.addThenGet(new SocketSettingRuleResultSpec(
			"socks5.onBind.inboundSocketSetting"));

	@HelpText(
			doc = "Specifies the binding host name or address for the listen "
					+ "socket if the provided host address is all zeros",
			usage = "socks5.onBind.listenBindHost=HOST"
	)
	public static final RuleResultSpec<Host> SOCKS5_ON_BIND_LISTEN_BIND_HOST = RULE_RESULT_SPECS.addThenGet(new HostRuleResultSpec(
			"socks5.onBind.listenBindHost"));
	
	@HelpText(
			doc = "Specifies a binding port range for the listen socket if "
					+ "the provided port is zero (can be specified multiple "
					+ "times with each rule result specifying another port "
					+ "range)",
			usage = "socks5.onBind.listenBindPortRange=PORT|PORT1-PORT2"
	)
	public static final RuleResultSpec<PortRange> SOCKS5_ON_BIND_LISTEN_BIND_PORT_RANGE = RULE_RESULT_SPECS.addThenGet(new PortRangeRuleResultSpec(
			"socks5.onBind.listenBindPortRange"));
	
	@HelpText(
			doc = "Specifies a socket setting for the listen socket (can be "
					+ "specified multiple times with each rule result "
					+ "specifying another socket setting)",
			usage = "socks5.onBind.listenSocketSetting=SOCKET_SETTING"
	)	
	public static final RuleResultSpec<SocketSetting<Object>> SOCKS5_ON_BIND_LISTEN_SOCKET_SETTING = RULE_RESULT_SPECS.addThenGet(new SocketSettingRuleResultSpec(
			"socks5.onBind.listenSocketSetting"));
	
	@HelpText(
			doc = "Specifies the buffer size in bytes for relaying the data", 
			usage = "socks5.onBind.relayBufferSize=INTEGER_BETWEEN_1_AND_2147483647"
	)	
	public static final RuleResultSpec<PositiveInteger> SOCKS5_ON_BIND_RELAY_BUFFER_SIZE = RULE_RESULT_SPECS.addThenGet(new PositiveIntegerRuleResultSpec(
			"socks5.onBind.relayBufferSize"));
	
	@HelpText(
			doc = "Specifies the timeout in milliseconds on relaying no data", 
			usage = "socks5.onBind.relayIdleTimeout=INTEGER_BETWEEN_1_AND_2147483647"
	)	
	public static final RuleResultSpec<PositiveInteger> SOCKS5_ON_BIND_RELAY_IDLE_TIMEOUT = RULE_RESULT_SPECS.addThenGet(new PositiveIntegerRuleResultSpec(
			"socks5.onBind.relayIdleTimeout"));

	@HelpText(
			doc = "Specifies the upper limit on bandwidth in bytes per second "
					+ "of receiving inbound data to be relayed",
			usage = "socks5.onBind.relayInboundBandwidthLimit=INTEGER_BETWEEN_1_AND_2147483647"
	)
	public static final RuleResultSpec<PositiveInteger> SOCKS5_ON_BIND_RELAY_INBOUND_BANDWIDTH_LIMIT = RULE_RESULT_SPECS.addThenGet(new PositiveIntegerRuleResultSpec(
			"socks5.onBind.relayInboundBandwidthLimit"));

	@HelpText(
			doc = "Specifies the upper limit on bandwidth in bytes per second "
					+ "of receiving outbound data to be relayed",
			usage = "socks5.onBind.relayOutboundBandwidthLimit=INTEGER_BETWEEN_1_AND_2147483647"
	)
	public static final RuleResultSpec<PositiveInteger> SOCKS5_ON_BIND_RELAY_OUTBOUND_BANDWIDTH_LIMIT = RULE_RESULT_SPECS.addThenGet(new PositiveIntegerRuleResultSpec(
			"socks5.onBind.relayOutboundBandwidthLimit"));

	@HelpText(
			doc = "Specifies the binding host name or address for all sockets",
			usage = "socks5.onCommand.bindHost=HOST"
	)
	public static final RuleResultSpec<Host> SOCKS5_ON_COMMAND_BIND_HOST = RULE_RESULT_SPECS.addThenGet(new HostRuleResultSpec(
			"socks5.onCommand.bindHost"));
	
	@HelpText(
			doc = "Specifies a binding port range for all TCP sockets (can be "
					+ "specified multiple times with each rule result "
					+ "specifying another port range)",
			usage = "socks5.onCommand.bindTcpPortRange=PORT|PORT1-PORT2"
	)
	public static final RuleResultSpec<PortRange> SOCKS5_ON_COMMAND_BIND_TCP_PORT_RANGE = RULE_RESULT_SPECS.addThenGet(new PortRangeRuleResultSpec(
			"socks5.onCommand.bindTcpPortRange"));
	
	@HelpText(
			doc = "Specifies a binding port range for all UDP sockets (can be "
					+ "specified multiple times with each rule result "
					+ "specifying another port range)",
			usage = "socks5.onCommand.bindUdpPortRange=PORT|PORT1-PORT2"
	)
	public static final RuleResultSpec<PortRange> SOCKS5_ON_COMMAND_BIND_UDP_PORT_RANGE = RULE_RESULT_SPECS.addThenGet(new PortRangeRuleResultSpec(
			"socks5.onCommand.bindUdpPortRange"));

	@HelpText(
			doc = "Specifies the binding host name or address for all "
					+ "external-facing sockets",
			usage = "socks5.onCommand.externalFacingBindHost=HOST"
	)
	public static final RuleResultSpec<Host> SOCKS5_ON_COMMAND_EXTERNAL_FACING_BIND_HOST = RULE_RESULT_SPECS.addThenGet(new HostRuleResultSpec(
			"socks5.onCommand.externalFacingBindHost"));
	
	@HelpText(
			doc = "Specifies a binding port range for all external-facing TCP "
					+ "sockets (can be specified multiple times with each rule "
					+ "result specifying another port range)",
			usage = "socks5.onCommand.externalFacingBindTcpPortRange=PORT|PORT1-PORT2"
	)
	public static final RuleResultSpec<PortRange> SOCKS5_ON_COMMAND_EXTERNAL_FACING_BIND_TCP_PORT_RANGE = RULE_RESULT_SPECS.addThenGet(new PortRangeRuleResultSpec(
			"socks5.onCommand.externalFacingBindTcpPortRange"));
	
	@HelpText(
			doc = "Specifies a binding port range for all external-facing UDP "
					+ "sockets (can be specified multiple times with each rule "
					+ "result specifying another port range)",
			usage = "socks5.onCommand.externalFacingBindUdpPortRange=PORT|PORT1-PORT2"
	)
	public static final RuleResultSpec<PortRange> SOCKS5_ON_COMMAND_EXTERNAL_FACING_BIND_UDP_PORT_RANGE = RULE_RESULT_SPECS.addThenGet(new PortRangeRuleResultSpec(
			"socks5.onCommand.externalFacingBindUdpPortRange"));
	
	@HelpText(
			doc = "Specifies a socket setting for all external-facing sockets "
					+ "(can be specified multiple times with each rule result "
					+ "specifying another socket setting)",
			usage = "socks5.onCommand.externalFacingSocketSetting=SOCKET_SETTING"
	)
	public static final RuleResultSpec<SocketSetting<Object>> SOCKS5_ON_COMMAND_EXTERNAL_FACING_SOCKET_SETTING = RULE_RESULT_SPECS.addThenGet(new SocketSettingRuleResultSpec(
			"socks5.onCommand.externalFacingSocketSetting"));
	
	@HelpText(
			doc = "Specifies the binding host name or address for all "
					+ "internal-facing sockets",
			usage = "socks5.onCommand.internalFacingBindHost=HOST"
	)
	public static final RuleResultSpec<Host> SOCKS5_ON_COMMAND_INTERNAL_FACING_BIND_HOST = RULE_RESULT_SPECS.addThenGet(new HostRuleResultSpec(
			"socks5.onCommand.internalFacingBindHost"));
	
	@HelpText(
			doc = "Specifies a binding port range for all internal-facing TCP "
					+ "sockets (can be specified multiple times with each rule "
					+ "result specifying another port range)",
			usage = "socks5.onCommand.internalFacingBindTcpPortRange=PORT|PORT1-PORT2"
	)
	public static final RuleResultSpec<PortRange> SOCKS5_ON_COMMAND_INTERNAL_FACING_BIND_TCP_PORT_RANGE = RULE_RESULT_SPECS.addThenGet(new PortRangeRuleResultSpec(
			"socks5.onCommand.internalFacingBindTcpPortRange"));
	
	@HelpText(
			doc = "Specifies a binding port range for all internal-facing UDP "
					+ "sockets (can be specified multiple times with each rule "
					+ "result specifying another port range)",
			usage = "socks5.onCommand.internalFacingBindUdpPortRange=PORT|PORT1-PORT2"
	)
	public static final RuleResultSpec<PortRange> SOCKS5_ON_COMMAND_INTERNAL_FACING_BIND_UDP_PORT_RANGE = RULE_RESULT_SPECS.addThenGet(new PortRangeRuleResultSpec(
			"socks5.onCommand.internalFacingBindUdpPortRange"));
	
	@HelpText(
			doc = "Specifies a socket setting for all internal-facing sockets "
					+ "(can be specified multiple times with each rule result "
					+ "specifying another socket setting)",
			usage = "socks5.onCommand.internalFacingSocketSetting=SOCKET_SETTING"
	)
	public static final RuleResultSpec<SocketSetting<Object>> SOCKS5_ON_COMMAND_INTERNAL_FACING_SOCKET_SETTING = RULE_RESULT_SPECS.addThenGet(new SocketSettingRuleResultSpec(
			"socks5.onCommand.internalFacingSocketSetting"));
	
	@HelpText(
			doc = "Specifies the buffer size in bytes for relaying the data", 
			usage = "socks5.onCommand.relayBufferSize=INTEGER_BETWEEN_1_AND_2147483647"
	)	
	public static final RuleResultSpec<PositiveInteger> SOCKS5_ON_COMMAND_RELAY_BUFFER_SIZE = RULE_RESULT_SPECS.addThenGet(new PositiveIntegerRuleResultSpec(
			"socks5.onCommand.relayBufferSize"));
	
	@HelpText(
			doc = "Specifies the timeout in milliseconds on relaying no data", 
			usage = "socks5.onCommand.relayIdleTimeout=INTEGER_BETWEEN_1_AND_2147483647"
	)	
	public static final RuleResultSpec<PositiveInteger> SOCKS5_ON_COMMAND_RELAY_IDLE_TIMEOUT = RULE_RESULT_SPECS.addThenGet(new PositiveIntegerRuleResultSpec(
			"socks5.onCommand.relayIdleTimeout"));

	@HelpText(
			doc = "Specifies the upper limit on bandwidth in bytes per second "
					+ "of receiving inbound data to be relayed",
			usage = "socks5.onCommand.relayInboundBandwidthLimit=INTEGER_BETWEEN_1_AND_2147483647"
	)
	public static final RuleResultSpec<PositiveInteger> SOCKS5_ON_COMMAND_RELAY_INBOUND_BANDWIDTH_LIMIT = RULE_RESULT_SPECS.addThenGet(new PositiveIntegerRuleResultSpec(
			"socks5.onCommand.relayInboundBandwidthLimit"));

	@HelpText(
			doc = "Specifies the upper limit on bandwidth in bytes per second "
					+ "of receiving outbound data to be relayed",
			usage = "socks5.onCommand.relayOutboundBandwidthLimit=INTEGER_BETWEEN_1_AND_2147483647"
	)
	public static final RuleResultSpec<PositiveInteger> SOCKS5_ON_COMMAND_RELAY_OUTBOUND_BANDWIDTH_LIMIT = RULE_RESULT_SPECS.addThenGet(new PositiveIntegerRuleResultSpec(
			"socks5.onCommand.relayOutboundBandwidthLimit"));
	
	@HelpText(
			doc = "Specifies a socket setting for all sockets (can be "
					+ "specified multiple times with each rule result "
					+ "specifying another socket setting)",
			usage = "socks5.onCommand.socketSetting=SOCKET_SETTING"
	)
	public static final RuleResultSpec<SocketSetting<Object>> SOCKS5_ON_COMMAND_SOCKET_SETTING = RULE_RESULT_SPECS.addThenGet(new SocketSettingRuleResultSpec(
			"socks5.onCommand.socketSetting"));
	
	@HelpText(
			doc = "Specifies the boolean value to indicate if the "
					+ "server-facing socket is to be prepared before "
					+ "connecting (involves applying the specified socket "
					+ "settings, resolving the target host name, and setting "
					+ "the specified timeout on waiting to connect)", 
			usage = "socks5.onConnect.prepareServerFacingSocket=true|false"
	)	
	public static final RuleResultSpec<Boolean> SOCKS5_ON_CONNECT_PREPARE_SERVER_FACING_SOCKET = RULE_RESULT_SPECS.addThenGet(new BooleanRuleResultSpec(
			"socks5.onConnect.prepareServerFacingSocket"));

	@HelpText(
			doc = "Specifies the buffer size in bytes for relaying the data", 
			usage = "socks5.onConnect.relayBufferSize=INTEGER_BETWEEN_1_AND_2147483647"
	)	
	public static final RuleResultSpec<PositiveInteger> SOCKS5_ON_CONNECT_RELAY_BUFFER_SIZE = RULE_RESULT_SPECS.addThenGet(new PositiveIntegerRuleResultSpec(
			"socks5.onConnect.relayBufferSize"));
	
	@HelpText(
			doc = "Specifies the timeout in milliseconds on relaying no data", 
			usage = "socks5.onConnect.relayIdleTimeout=INTEGER_BETWEEN_1_AND_2147483647"
	)	
	public static final RuleResultSpec<PositiveInteger> SOCKS5_ON_CONNECT_RELAY_IDLE_TIMEOUT = RULE_RESULT_SPECS.addThenGet(new PositiveIntegerRuleResultSpec(
			"socks5.onConnect.relayIdleTimeout"));

	@HelpText(
			doc = "Specifies the upper limit on bandwidth in bytes per second "
					+ "of receiving inbound data to be relayed",
			usage = "socks5.onConnect.relayInboundBandwidthLimit=INTEGER_BETWEEN_1_AND_2147483647"
	)
	public static final RuleResultSpec<PositiveInteger> SOCKS5_ON_CONNECT_RELAY_INBOUND_BANDWIDTH_LIMIT = RULE_RESULT_SPECS.addThenGet(new PositiveIntegerRuleResultSpec(
			"socks5.onConnect.relayInboundBandwidthLimit"));

	@HelpText(
			doc = "Specifies the upper limit on bandwidth in bytes per second "
					+ "of receiving outbound data to be relayed",
			usage = "socks5.onConnect.relayOutboundBandwidthLimit=INTEGER_BETWEEN_1_AND_2147483647"
	)
	public static final RuleResultSpec<PositiveInteger> SOCKS5_ON_CONNECT_RELAY_OUTBOUND_BANDWIDTH_LIMIT = RULE_RESULT_SPECS.addThenGet(new PositiveIntegerRuleResultSpec(
			"socks5.onConnect.relayOutboundBandwidthLimit"));
	
	@HelpText(
			doc = "Specifies the binding host name or address for the "
					+ "server-facing socket", 
			usage = "socks5.onConnect.serverFacingBindHost=HOST"
	)	
	public static final RuleResultSpec<Host> SOCKS5_ON_CONNECT_SERVER_FACING_BIND_HOST = RULE_RESULT_SPECS.addThenGet(new HostRuleResultSpec(
			"socks5.onConnect.serverFacingBindHost"));
	
	@HelpText(
			doc = "Specifies a binding port range for the server-facing socket "
					+ "(can be specified multiple times with each rule result "
					+ "specifying another port range)", 
			usage = "socks5.onConnect.serverFacingBindPortRange=PORT|PORT1-PORT2"
	)	
	public static final RuleResultSpec<PortRange> SOCKS5_ON_CONNECT_SERVER_FACING_BIND_PORT_RANGE = RULE_RESULT_SPECS.addThenGet(new PortRangeRuleResultSpec(
			"socks5.onConnect.serverFacingBindPortRange"));
	
	@HelpText(
			doc = "Specifies the timeout in milliseconds on waiting for the "
					+ "server-facing socket to connect", 
			usage = "socks5.onConnect.serverFacingConnectTimeout=INTEGER_BETWEEN_1_AND_2147483647"
	)	
	public static final RuleResultSpec<PositiveInteger> SOCKS5_ON_CONNECT_SERVER_FACING_CONNECT_TIMEOUT = RULE_RESULT_SPECS.addThenGet(new PositiveIntegerRuleResultSpec(
			"socks5.onConnect.serverFacingConnectTimeout"));
	
	@HelpText(
			doc = "Specifies a socket setting for the server-facing socket "
					+ "(can be specified multiple times with each rule result "
					+ "specifying another socket setting)",
			usage = "socks5.onConnect.serverFacingSocketSetting=SOCKET_SETTING"
	)	
	public static final RuleResultSpec<SocketSetting<Object>> SOCKS5_ON_CONNECT_SERVER_FACING_SOCKET_SETTING = RULE_RESULT_SPECS.addThenGet(new SocketSettingRuleResultSpec(
			"socks5.onConnect.serverFacingSocketSetting"));

	@HelpText(
			doc = "Specifies the binding host name or address for the "
					+ "client-facing UDP socket", 
			usage = "socks5.onUdpAssociate.clientFacingBindHost=HOST"
	)
	public static final RuleResultSpec<Host> SOCKS5_ON_UDP_ASSOCIATE_CLIENT_FACING_BIND_HOST = RULE_RESULT_SPECS.addThenGet(new HostRuleResultSpec(
			"socks5.onUdpAssociate.clientFacingBindHost"));
	
	@HelpText(
			doc = "Specifies a binding port range for the client-facing UDP "
					+ "socket (can be specified multiple times with each rule "
					+ "result specifying another port range)", 
			usage = "socks5.onUdpAssociate.clientFacingBindPortRange=PORT|PORT1-PORT2"
	)	
	public static final RuleResultSpec<PortRange> SOCKS5_ON_UDP_ASSOCIATE_CLIENT_FACING_BIND_PORT_RANGE = RULE_RESULT_SPECS.addThenGet(new PortRangeRuleResultSpec(
			"socks5.onUdpAssociate.clientFacingBindPortRange"));
	
	@HelpText(
			doc = "Specifies a socket setting for the client-facing UDP socket "
					+ "(can be specified multiple times with each rule result "
					+ "specifying another socket setting)",
			usage = "socks5.onUdpAssociate.clientFacingSocketSetting=SOCKET_SETTING"
	)	
	public static final RuleResultSpec<SocketSetting<Object>> SOCKS5_ON_UDP_ASSOCIATE_CLIENT_FACING_SOCKET_SETTING = RULE_RESULT_SPECS.addThenGet(new SocketSettingRuleResultSpec(
			"socks5.onUdpAssociate.clientFacingSocketSetting"));
	
	@HelpText(
			doc = "Specifies the binding host name or address for the "
					+ "peer-facing UDP socket", 
			usage = "socks5.onUdpAssociate.peerFacingBindHost=HOST"
	)	
	public static final RuleResultSpec<Host> SOCKS5_ON_UDP_ASSOCIATE_PEER_FACING_BIND_HOST = RULE_RESULT_SPECS.addThenGet(new HostRuleResultSpec(
			"socks5.onUdpAssociate.peerFacingBindHost"));

	@HelpText(
			doc = "Specifies a binding port range for the peer-facing UDP "
					+ "socket (can be specified multiple times with each rule "
					+ "result specifying another port range)", 
			usage = "socks5.onUdpAssociate.peerFacingBindPortRange=PORT|PORT1-PORT2"
	)	
	public static final RuleResultSpec<PortRange> SOCKS5_ON_UDP_ASSOCIATE_PEER_FACING_BIND_PORT_RANGE = RULE_RESULT_SPECS.addThenGet(new PortRangeRuleResultSpec(
			"socks5.onUdpAssociate.peerFacingBindPortRange"));
	
	@HelpText(
			doc = "Specifies a socket setting for the peer-facing UDP socket "
					+ "(can be specified multiple times with each rule result "
					+ "specifying another socket setting)",
			usage = "socks5.onUdpAssociate.peerFacingSocketSetting=SOCKET_SETTING"
	)	
	public static final RuleResultSpec<SocketSetting<Object>> SOCKS5_ON_UDP_ASSOCIATE_PEER_FACING_SOCKET_SETTING = RULE_RESULT_SPECS.addThenGet(new SocketSettingRuleResultSpec(
			"socks5.onUdpAssociate.peerFacingSocketSetting"));
	
	@HelpText(
			doc = "Specifies the buffer size in bytes for relaying the data", 
			usage = "socks5.onUdpAssociate.relayBufferSize=INTEGER_BETWEEN_1_AND_2147483647"
	)	
	public static final RuleResultSpec<PositiveInteger> SOCKS5_ON_UDP_ASSOCIATE_RELAY_BUFFER_SIZE = RULE_RESULT_SPECS.addThenGet(new PositiveIntegerRuleResultSpec(
			"socks5.onUdpAssociate.relayBufferSize"));
	
	@HelpText(
			doc = "Specifies the timeout in milliseconds on relaying no data", 
			usage = "socks5.onUdpAssociate.relayIdleTimeout=INTEGER_BETWEEN_1_AND_2147483647"
	)	
	public static final RuleResultSpec<PositiveInteger> SOCKS5_ON_UDP_ASSOCIATE_RELAY_IDLE_TIMEOUT = RULE_RESULT_SPECS.addThenGet(new PositiveIntegerRuleResultSpec(
			"socks5.onUdpAssociate.relayIdleTimeout"));

	@HelpText(
			doc = "Specifies the upper limit on bandwidth in bytes per second "
					+ "of receiving inbound data to be relayed",
			usage = "socks5.onUdpAssociate.relayInboundBandwidthLimit=INTEGER_BETWEEN_1_AND_2147483647"
	)
	public static final RuleResultSpec<PositiveInteger> SOCKS5_ON_UDP_ASSOCIATE_RELAY_INBOUND_BANDWIDTH_LIMIT = RULE_RESULT_SPECS.addThenGet(new PositiveIntegerRuleResultSpec(
			"socks5.onUdpAssociate.relayInboundBandwidthLimit"));

	@HelpText(
			doc = "Specifies the upper limit on bandwidth in bytes per second "
					+ "of receiving outbound data to be relayed",
			usage = "socks5.onUdpAssociate.relayOutboundBandwidthLimit=INTEGER_BETWEEN_1_AND_2147483647"
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