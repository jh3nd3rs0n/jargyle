package com.github.jh3nd3rs0n.jargyle.server;

import com.github.jh3nd3rs0n.jargyle.common.net.*;
import com.github.jh3nd3rs0n.jargyle.common.number.PositiveInteger;
import com.github.jh3nd3rs0n.jargyle.internal.annotation.NameValuePairValueSpecDoc;
import com.github.jh3nd3rs0n.jargyle.internal.annotation.NameValuePairValueSpecsDoc;
import com.github.jh3nd3rs0n.jargyle.protocolbase.socks5.Address;
import com.github.jh3nd3rs0n.jargyle.server.internal.ruleactionspec.impl.*;

import java.util.List;
import java.util.Map;

@NameValuePairValueSpecsDoc(
		description = "",
		name = "SOCKS5 Rule Actions"
)
public final class Socks5RuleActionSpecConstants {

	private static final RuleActionSpecs RULE_ACTION_SPECS = new RuleActionSpecs();

	@NameValuePairValueSpecDoc(
			description = "Specifies a socket setting for the inbound socket "
					+ "(can be specified multiple times with each rule action "
					+ "specifying another socket setting)",
			name = "socks5.onBindRequest.inboundSocketSetting",
			syntax = "socks5.onBindRequest.inboundSocketSetting=SOCKET_SETTING",
			valueType = SocketSetting.class
	)	
	public static final RuleActionSpec<SocketSetting<Object>> SOCKS5_ON_BIND_REQUEST_INBOUND_SOCKET_SETTING = RULE_ACTION_SPECS.addThenGet(new SocketSettingRuleActionSpec(
			"socks5.onBindRequest.inboundSocketSetting"));

	@NameValuePairValueSpecDoc(
			description = "Specifies the binding host name or address for the "
					+ "listen socket if the provided host address is all zeros",
			name = "socks5.onBindRequest.listenBindHost",
			syntax = "socks5.onBindRequest.listenBindHost=HOST",
			valueType = Host.class
	)
	public static final RuleActionSpec<Host> SOCKS5_ON_BIND_REQUEST_LISTEN_BIND_HOST = RULE_ACTION_SPECS.addThenGet(new HostRuleActionSpec(
			"socks5.onBindRequest.listenBindHost"));

	@NameValuePairValueSpecDoc(
			description = "Specifies an acceptable binding host address type "
					+ "for the listen socket if the provided host address is "
					+ "all zeros (can be specified multiple times with each "
					+ "rule specifying another host address type)",
			name = "socks5.onBindRequest.listenBindHostAddressType",
			syntax = "socks5.onBindRequest.listenBindHostAddressType=HOST_ADDRESS_TYPE",
			valueType = HostAddressType.class
	)
	public static final RuleActionSpec<HostAddressType> SOCKS5_ON_BIND_REQUEST_LISTEN_BIND_HOST_ADDRESS_TYPE = RULE_ACTION_SPECS.addThenGet(new HostAddressTypeRuleActionSpec(
			"socks5.onBindRequest.listenBindHostAddressType"));

	@NameValuePairValueSpecDoc(
			description = "Specifies a binding port range for the listen socket "
					+ "if the provided port is zero (can be specified multiple "
					+ "times with each rule action specifying another port "
					+ "range)",
			name = "socks5.onBindRequest.listenBindPortRange",
			syntax = "socks5.onBindRequest.listenBindPortRange=PORT_RANGE",
			valueType = PortRange.class
	)
	public static final RuleActionSpec<PortRange> SOCKS5_ON_BIND_REQUEST_LISTEN_BIND_PORT_RANGE = RULE_ACTION_SPECS.addThenGet(new PortRangeRuleActionSpec(
			"socks5.onBindRequest.listenBindPortRange"));

	@NameValuePairValueSpecDoc(
			description = "Specifies the network interface that provides a "
					+ "binding host address for the listen socket if the "
					+ "provided host address is all zeros",
			name = "socks5.onBindRequest.listenNetInterface",
			syntax = "socks5.onBindRequest.listenNetInterface=NETWORK_INTERFACE",
			valueType = NetInterface.class
	)
	public static final RuleActionSpec<NetInterface> SOCKS5_ON_BIND_REQUEST_LISTEN_NET_INTERFACE = RULE_ACTION_SPECS.addThenGet(new NetInterfaceRuleActionSpec(
			"socks5.onBindRequest.listenNetInterface"));

	@NameValuePairValueSpecDoc(
			description = "Specifies a socket setting for the listen socket "
					+ "(can be specified multiple times with each rule action "
					+ "specifying another socket setting)",
			name = "socks5.onBindRequest.listenSocketSetting",
			syntax = "socks5.onBindRequest.listenSocketSetting=SOCKET_SETTING",
			valueType = SocketSetting.class
	)	
	public static final RuleActionSpec<SocketSetting<Object>> SOCKS5_ON_BIND_REQUEST_LISTEN_SOCKET_SETTING = RULE_ACTION_SPECS.addThenGet(new SocketSettingRuleActionSpec(
			"socks5.onBindRequest.listenSocketSetting"));
	
	@NameValuePairValueSpecDoc(
			description = "Specifies the buffer size in bytes for relaying the "
					+ "data",
			name = "socks5.onBindRequest.relayBufferSize",
			syntax = "socks5.onBindRequest.relayBufferSize=POSITIVE_INTEGER",
			valueType = PositiveInteger.class
	)	
	public static final RuleActionSpec<PositiveInteger> SOCKS5_ON_BIND_REQUEST_RELAY_BUFFER_SIZE = RULE_ACTION_SPECS.addThenGet(new PositiveIntegerRuleActionSpec(
			"socks5.onBindRequest.relayBufferSize"));
	
	@NameValuePairValueSpecDoc(
			description = "Specifies the timeout in milliseconds on relaying "
					+ "no data",
			name = "socks5.onBindRequest.relayIdleTimeout",
			syntax = "socks5.onBindRequest.relayIdleTimeout=POSITIVE_INTEGER",
			valueType = PositiveInteger.class
	)	
	public static final RuleActionSpec<PositiveInteger> SOCKS5_ON_BIND_REQUEST_RELAY_IDLE_TIMEOUT = RULE_ACTION_SPECS.addThenGet(new PositiveIntegerRuleActionSpec(
			"socks5.onBindRequest.relayIdleTimeout"));

	@NameValuePairValueSpecDoc(
			description = "Specifies the upper limit on bandwidth in bytes per "
					+ "second of receiving inbound data to be relayed",
			name = "socks5.onBindRequest.relayInboundBandwidthLimit",
			syntax = "socks5.onBindRequest.relayInboundBandwidthLimit=POSITIVE_INTEGER",
			valueType = PositiveInteger.class
	)
	public static final RuleActionSpec<PositiveInteger> SOCKS5_ON_BIND_REQUEST_RELAY_INBOUND_BANDWIDTH_LIMIT = RULE_ACTION_SPECS.addThenGet(new PositiveIntegerRuleActionSpec(
			"socks5.onBindRequest.relayInboundBandwidthLimit"));

	@NameValuePairValueSpecDoc(
			description = "Specifies the upper limit on bandwidth in bytes per "
					+ "second of receiving outbound data to be relayed",
			name = "socks5.onBindRequest.relayOutboundBandwidthLimit",
			syntax = "socks5.onBindRequest.relayOutboundBandwidthLimit=POSITIVE_INTEGER",
			valueType = PositiveInteger.class
	)
	public static final RuleActionSpec<PositiveInteger> SOCKS5_ON_BIND_REQUEST_RELAY_OUTBOUND_BANDWIDTH_LIMIT = RULE_ACTION_SPECS.addThenGet(new PositiveIntegerRuleActionSpec(
			"socks5.onBindRequest.relayOutboundBandwidthLimit"));

	@NameValuePairValueSpecDoc(
			description = "Specifies the boolean value to indicate if the "
					+ "target-facing socket is to be prepared before "
					+ "connecting (involves applying the specified socket "
					+ "settings, resolving the target host name, and setting "
					+ "the specified timeout on waiting to connect)",
			name = "socks5.onConnectRequest.prepareTargetFacingSocket",
			syntax = "socks5.onConnectRequest.prepareTargetFacingSocket=true|false",
			valueType = Boolean.class
	)	
	public static final RuleActionSpec<Boolean> SOCKS5_ON_CONNECT_REQUEST_PREPARE_TARGET_FACING_SOCKET = RULE_ACTION_SPECS.addThenGet(new BooleanRuleActionSpec(
			"socks5.onConnectRequest.prepareTargetFacingSocket"));

	@NameValuePairValueSpecDoc(
			description = "Specifies the buffer size in bytes for relaying the "
					+ "data",
			name = "socks5.onConnectRequest.relayBufferSize",
			syntax = "socks5.onConnectRequest.relayBufferSize=POSITIVE_INTEGER",
			valueType = PositiveInteger.class
	)	
	public static final RuleActionSpec<PositiveInteger> SOCKS5_ON_CONNECT_REQUEST_RELAY_BUFFER_SIZE = RULE_ACTION_SPECS.addThenGet(new PositiveIntegerRuleActionSpec(
			"socks5.onConnectRequest.relayBufferSize"));
	
	@NameValuePairValueSpecDoc(
			description = "Specifies the timeout in milliseconds on relaying "
					+ "no data",
			name = "socks5.onConnectRequest.relayIdleTimeout",
			syntax = "socks5.onConnectRequest.relayIdleTimeout=POSITIVE_INTEGER",
			valueType = PositiveInteger.class
	)	
	public static final RuleActionSpec<PositiveInteger> SOCKS5_ON_CONNECT_REQUEST_RELAY_IDLE_TIMEOUT = RULE_ACTION_SPECS.addThenGet(new PositiveIntegerRuleActionSpec(
			"socks5.onConnectRequest.relayIdleTimeout"));

	@NameValuePairValueSpecDoc(
			description = "Specifies the upper limit on bandwidth in bytes per "
					+ "second of receiving inbound data to be relayed",
			name = "socks5.onConnectRequest.relayInboundBandwidthLimit",
			syntax = "socks5.onConnectRequest.relayInboundBandwidthLimit=POSITIVE_INTEGER",
			valueType = PositiveInteger.class
	)
	public static final RuleActionSpec<PositiveInteger> SOCKS5_ON_CONNECT_REQUEST_RELAY_INBOUND_BANDWIDTH_LIMIT = RULE_ACTION_SPECS.addThenGet(new PositiveIntegerRuleActionSpec(
			"socks5.onConnectRequest.relayInboundBandwidthLimit"));

	@NameValuePairValueSpecDoc(
			description = "Specifies the upper limit on bandwidth in bytes per "
					+ "second of receiving outbound data to be relayed",
			name = "socks5.onConnectRequest.relayOutboundBandwidthLimit",
			syntax = "socks5.onConnectRequest.relayOutboundBandwidthLimit=POSITIVE_INTEGER",
			valueType = PositiveInteger.class
	)
	public static final RuleActionSpec<PositiveInteger> SOCKS5_ON_CONNECT_REQUEST_RELAY_OUTBOUND_BANDWIDTH_LIMIT = RULE_ACTION_SPECS.addThenGet(new PositiveIntegerRuleActionSpec(
			"socks5.onConnectRequest.relayOutboundBandwidthLimit"));
	
	@NameValuePairValueSpecDoc(
			description = "Specifies the binding host name or address for the "
					+ "target-facing socket",
			name = "socks5.onConnectRequest.targetFacingBindHost",
			syntax = "socks5.onConnectRequest.targetFacingBindHost=HOST",
			valueType = Host.class
	)	
	public static final RuleActionSpec<Host> SOCKS5_ON_CONNECT_REQUEST_TARGET_FACING_BIND_HOST = RULE_ACTION_SPECS.addThenGet(new HostRuleActionSpec(
			"socks5.onConnectRequest.targetFacingBindHost"));

	@NameValuePairValueSpecDoc(
			description = "Specifies an acceptable binding host address type "
					+ "for the target-facing socket (can be specified "
					+ "multiple times with each rule action specifying "
					+ "another host address type)",
			name = "socks5.onConnectRequest.targetFacingBindHostAddressType",
			syntax = "socks5.onConnectRequest.targetFacingBindHostAddressType=HOST_ADDRESS_TYPE",
			valueType = HostAddressType.class
	)
	public static final RuleActionSpec<HostAddressType> SOCKS5_ON_CONNECT_REQUEST_TARGET_FACING_BIND_HOST_ADDRESS_TYPE = RULE_ACTION_SPECS.addThenGet(new HostAddressTypeRuleActionSpec(
			"socks5.onConnectRequest.targetFacingBindHostAddressType"));

	@NameValuePairValueSpecDoc(
			description = "Specifies a binding port range for the target-facing "
					+ "socket (can be specified multiple times with each rule "
					+ "action specifying another port range)",
			name = "socks5.onConnectRequest.targetFacingBindPortRange",
			syntax = "socks5.onConnectRequest.targetFacingBindPortRange=PORT_RANGE",
			valueType = PortRange.class
	)	
	public static final RuleActionSpec<PortRange> SOCKS5_ON_CONNECT_REQUEST_TARGET_FACING_BIND_PORT_RANGE = RULE_ACTION_SPECS.addThenGet(new PortRangeRuleActionSpec(
			"socks5.onConnectRequest.targetFacingBindPortRange"));
	
	@NameValuePairValueSpecDoc(
			description = "Specifies the timeout in milliseconds on waiting for "
					+ "the target-facing socket to connect",
			name = "socks5.onConnectRequest.targetFacingConnectTimeout",
			syntax = "socks5.onConnectRequest.targetFacingConnectTimeout=POSITIVE_INTEGER",
			valueType = PositiveInteger.class
	)	
	public static final RuleActionSpec<PositiveInteger> SOCKS5_ON_CONNECT_REQUEST_TARGET_FACING_CONNECT_TIMEOUT = RULE_ACTION_SPECS.addThenGet(new PositiveIntegerRuleActionSpec(
			"socks5.onConnectRequest.targetFacingConnectTimeout"));

	@NameValuePairValueSpecDoc(
			description = "Specifies the network interface that provides a "
					+ "binding host address for the target-facing socket",
			name = "socks5.onConnectRequest.targetFacingNetInterface",
			syntax = "socks5.onConnectRequest.targetFacingNetInterface=NETWORK_INTERFACE",
			valueType = NetInterface.class
	)
	public static final RuleActionSpec<NetInterface> SOCKS5_ON_CONNECT_REQUEST_TARGET_FACING_NET_INTERFACE = RULE_ACTION_SPECS.addThenGet(new NetInterfaceRuleActionSpec(
			"socks5.onConnectRequest.targetFacingNetInterface"));

	@NameValuePairValueSpecDoc(
			description = "Specifies a socket setting for the target-facing "
					+ "socket (can be specified multiple times with each rule "
					+ "action specifying another socket setting)",
			name = "socks5.onConnectRequest.targetFacingSocketSetting",
			syntax = "socks5.onConnectRequest.targetFacingSocketSetting=SOCKET_SETTING",
			valueType = SocketSetting.class
	)	
	public static final RuleActionSpec<SocketSetting<Object>> SOCKS5_ON_CONNECT_REQUEST_TARGET_FACING_SOCKET_SETTING = RULE_ACTION_SPECS.addThenGet(new SocketSettingRuleActionSpec(
			"socks5.onConnectRequest.targetFacingSocketSetting"));

	@NameValuePairValueSpecDoc(
			description = "Specifies the binding host name or address for the "
					+ "client-facing UDP socket",
			name = "socks5.onUdpAssociateRequest.clientFacingBindHost",
			syntax = "socks5.onUdpAssociateRequest.clientFacingBindHost=HOST",
			valueType = Host.class
	)
	public static final RuleActionSpec<Host> SOCKS5_ON_UDP_ASSOCIATE_REQUEST_CLIENT_FACING_BIND_HOST = RULE_ACTION_SPECS.addThenGet(new HostRuleActionSpec(
			"socks5.onUdpAssociateRequest.clientFacingBindHost"));

	@NameValuePairValueSpecDoc(
			description = "Specifies an acceptable binding host address type "
					+ "for the client-facing UDP socket (can be specified "
					+ "multiple times with each rule action specifying "
					+ "another host address type)",
			name = "socks5.onUdpAssociateRequest.clientFacingBindHostAddressType",
			syntax = "socks5.onUdpAssociateRequest.clientFacingBindHostAddressType=HOST_ADDRESS_TYPE",
			valueType = HostAddressType.class
	)
	public static final RuleActionSpec<HostAddressType> SOCKS5_ON_UDP_ASSOCIATE_REQUEST_CLIENT_FACING_BIND_HOST_ADDRESS_TYPE = RULE_ACTION_SPECS.addThenGet(new HostAddressTypeRuleActionSpec(
			"socks5.onUdpAssociateRequest.clientFacingBindHostAddressType"));

	@NameValuePairValueSpecDoc(
			description = "Specifies a binding port range for the client-facing "
					+ "UDP socket (can be specified multiple times with each "
					+ "rule action specifying another port range)",
			name = "socks5.onUdpAssociateRequest.clientFacingBindPortRange",
			syntax = "socks5.onUdpAssociateRequest.clientFacingBindPortRange=PORT_RANGE",
			valueType = PortRange.class
	)	
	public static final RuleActionSpec<PortRange> SOCKS5_ON_UDP_ASSOCIATE_REQUEST_CLIENT_FACING_BIND_PORT_RANGE = RULE_ACTION_SPECS.addThenGet(new PortRangeRuleActionSpec(
			"socks5.onUdpAssociateRequest.clientFacingBindPortRange"));

	@NameValuePairValueSpecDoc(
			description = "Specifies the network interface that provides a "
					+ "binding host address for the client-facing UDP socket",
			name = "socks5.onUdpAssociateRequest.clientFacingNetInterface",
			syntax = "socks5.onUdpAssociateRequest.clientFacingNetInterface=NETWORK_INTERFACE",
			valueType = NetInterface.class
	)
	public static final RuleActionSpec<NetInterface> SOCKS5_ON_UDP_ASSOCIATE_REQUEST_CLIENT_FACING_NET_INTERFACE = RULE_ACTION_SPECS.addThenGet(new NetInterfaceRuleActionSpec(
			"socks5.onUdpAssociateRequest.clientFacingNetInterface"));

	@NameValuePairValueSpecDoc(
			description = "Specifies a socket setting for the client-facing UDP "
					+ "socket (can be specified multiple times with each rule "
					+ "action specifying another socket setting)",
			name = "socks5.onUdpAssociateRequest.clientFacingSocketSetting",
			syntax = "socks5.onUdpAssociateRequest.clientFacingSocketSetting=SOCKET_SETTING",
			valueType = SocketSetting.class
	)	
	public static final RuleActionSpec<SocketSetting<Object>> SOCKS5_ON_UDP_ASSOCIATE_REQUEST_CLIENT_FACING_SOCKET_SETTING = RULE_ACTION_SPECS.addThenGet(new SocketSettingRuleActionSpec(
			"socks5.onUdpAssociateRequest.clientFacingSocketSetting"));
	
	@NameValuePairValueSpecDoc(
			description = "Specifies the binding host name or address for the "
					+ "peer-facing UDP socket",
			name = "socks5.onUdpAssociateRequest.peerFacingBindHost",
			syntax = "socks5.onUdpAssociateRequest.peerFacingBindHost=HOST",
			valueType = Host.class
	)	
	public static final RuleActionSpec<Host> SOCKS5_ON_UDP_ASSOCIATE_REQUEST_PEER_FACING_BIND_HOST = RULE_ACTION_SPECS.addThenGet(new HostRuleActionSpec(
			"socks5.onUdpAssociateRequest.peerFacingBindHost"));

	@NameValuePairValueSpecDoc(
			description = "Specifies an acceptable binding host address type "
					+ "for the peer-facing UDP socket (can be specified "
					+ "multiple times with each rule action specifying "
					+ "another host address type)",
			name = "socks5.onUdpAssociateRequest.peerFacingBindHostAddressType",
			syntax = "socks5.onUdpAssociateRequest.peerFacingBindHostAddressType=HOST_ADDRESS_TYPE",
			valueType = HostAddressType.class
	)
	public static final RuleActionSpec<HostAddressType> SOCKS5_ON_UDP_ASSOCIATE_REQUEST_PEER_FACING_BIND_HOST_ADDRESS_TYPE = RULE_ACTION_SPECS.addThenGet(new HostAddressTypeRuleActionSpec(
			"socks5.onUdpAssociateRequest.peerFacingBindHostAddressType"));

	@NameValuePairValueSpecDoc(
			description = "Specifies a binding port range for the peer-facing "
					+ "UDP socket (can be specified multiple times with each "
					+ "rule action specifying another port range)",
			name = "socks5.onUdpAssociateRequest.peerFacingBindPortRange",
			syntax = "socks5.onUdpAssociateRequest.peerFacingBindPortRange=PORT_RANGE",
			valueType = PortRange.class
	)	
	public static final RuleActionSpec<PortRange> SOCKS5_ON_UDP_ASSOCIATE_REQUEST_PEER_FACING_BIND_PORT_RANGE = RULE_ACTION_SPECS.addThenGet(new PortRangeRuleActionSpec(
			"socks5.onUdpAssociateRequest.peerFacingBindPortRange"));

	@NameValuePairValueSpecDoc(
			description = "Specifies the network interface that provides a "
					+ "binding host address for the peer-facing UDP socket",
			name = "socks5.onUdpAssociateRequest.peerFacingNetInterface",
			syntax = "socks5.onUdpAssociateRequest.peerFacingNetInterface=NETWORK_INTERFACE",
			valueType = NetInterface.class
	)
	public static final RuleActionSpec<NetInterface> SOCKS5_ON_UDP_ASSOCIATE_REQUEST_PEER_FACING_NET_INTERFACE = RULE_ACTION_SPECS.addThenGet(new NetInterfaceRuleActionSpec(
			"socks5.onUdpAssociateRequest.peerFacingNetInterface"));

	@NameValuePairValueSpecDoc(
			description = "Specifies a socket setting for the peer-facing UDP "
					+ "socket (can be specified multiple times with each rule "
					+ "action specifying another socket setting)",
			name = "socks5.onUdpAssociateRequest.peerFacingSocketSetting",
			syntax = "socks5.onUdpAssociateRequest.peerFacingSocketSetting=SOCKET_SETTING",
			valueType = SocketSetting.class
	)	
	public static final RuleActionSpec<SocketSetting<Object>> SOCKS5_ON_UDP_ASSOCIATE_REQUEST_PEER_FACING_SOCKET_SETTING = RULE_ACTION_SPECS.addThenGet(new SocketSettingRuleActionSpec(
			"socks5.onUdpAssociateRequest.peerFacingSocketSetting"));
	
	@NameValuePairValueSpecDoc(
			description = "Specifies the buffer size in bytes for relaying the "
					+ "data",
			name = "socks5.onUdpAssociateRequest.relayBufferSize",
			syntax = "socks5.onUdpAssociateRequest.relayBufferSize=POSITIVE_INTEGER",
			valueType = PositiveInteger.class
	)	
	public static final RuleActionSpec<PositiveInteger> SOCKS5_ON_UDP_ASSOCIATE_REQUEST_RELAY_BUFFER_SIZE = RULE_ACTION_SPECS.addThenGet(new PositiveIntegerRuleActionSpec(
			"socks5.onUdpAssociateRequest.relayBufferSize"));
	
	@NameValuePairValueSpecDoc(
			description = "Specifies the timeout in milliseconds on relaying no "
					+ "data",
			name = "socks5.onUdpAssociateRequest.relayIdleTimeout",
			syntax = "socks5.onUdpAssociateRequest.relayIdleTimeout=POSITIVE_INTEGER",
			valueType = PositiveInteger.class
	)	
	public static final RuleActionSpec<PositiveInteger> SOCKS5_ON_UDP_ASSOCIATE_REQUEST_RELAY_IDLE_TIMEOUT = RULE_ACTION_SPECS.addThenGet(new PositiveIntegerRuleActionSpec(
			"socks5.onUdpAssociateRequest.relayIdleTimeout"));

	@NameValuePairValueSpecDoc(
			description = "Specifies the upper limit on bandwidth in bytes per "
					+ "second of receiving inbound data to be relayed",
			name = "socks5.onUdpAssociateRequest.relayInboundBandwidthLimit",
			syntax = "socks5.onUdpAssociateRequest.relayInboundBandwidthLimit=POSITIVE_INTEGER",
			valueType = PositiveInteger.class
	)
	public static final RuleActionSpec<PositiveInteger> SOCKS5_ON_UDP_ASSOCIATE_REQUEST_RELAY_INBOUND_BANDWIDTH_LIMIT = RULE_ACTION_SPECS.addThenGet(new PositiveIntegerRuleActionSpec(
			"socks5.onUdpAssociateRequest.relayInboundBandwidthLimit"));

	@NameValuePairValueSpecDoc(
			description = "Specifies the upper limit on bandwidth in bytes per "
					+ "second of receiving outbound data to be relayed",
			name = "socks5.onUdpAssociateRequest.relayOutboundBandwidthLimit",
			syntax = "socks5.onUdpAssociateRequest.relayOutboundBandwidthLimit=POSITIVE_INTEGER",
			valueType = PositiveInteger.class
	)
	public static final RuleActionSpec<PositiveInteger> SOCKS5_ON_UDP_ASSOCIATE_REQUEST_RELAY_OUTBOUND_BANDWIDTH_LIMIT = RULE_ACTION_SPECS.addThenGet(new PositiveIntegerRuleActionSpec(
			"socks5.onUdpAssociateRequest.relayOutboundBandwidthLimit"));

	@NameValuePairValueSpecDoc(
			description = "Specifies the desired destination address "
					+ "redirect for the request",
			name = "socks5.request.desiredDestinationAddressRedirect",
			syntax = "socks5.request.desiredDestinationAddressRedirect=SOCKS5_ADDRESS",
			valueType = Address.class
	)
	public static final RuleActionSpec<Address> SOCKS5_REQUEST_DESIRED_DESTINATION_ADDRESS_REDIRECT = RULE_ACTION_SPECS.addThenGet(new Socks5AddressRuleActionSpec(
			"socks5.request.desiredDestinationAddressRedirect"));

	@NameValuePairValueSpecDoc(
			description = "Specifies the desired destination port redirect "
					+ "for the request",
			name = "socks5.request.desiredDestinationPortRedirect",
			syntax = "socks5.request.desiredDestinationPortRedirect=PORT",
			valueType = Port.class
	)
	public static final RuleActionSpec<Port> SOCKS5_REQUEST_DESIRED_DESTINATION_PORT_REDIRECT = RULE_ACTION_SPECS.addThenGet(new PortRuleActionSpec(
			"socks5.request.desiredDestinationPortRedirect"));

	@NameValuePairValueSpecDoc(
			description = "Specifies the logging action to take if the desired "
					+ "destination of the request is redirected",
			name = "socks5.request.desiredDestinationRedirectLogAction",
			syntax = "socks5.request.desiredDestinationRedirectLogAction=LOG_ACTION",
			valueType = LogAction.class
	)
	public static final RuleActionSpec<LogAction> SOCKS5_REQUEST_DESIRED_DESTINATION_REDIRECT_LOG_ACTION = RULE_ACTION_SPECS.addThenGet(new LogActionRuleActionSpec(
			"socks5.request.desiredDestinationRedirectLogAction"));

	public static List<RuleActionSpec<Object>> values() {
		return RULE_ACTION_SPECS.toList();
	}
	
	public static Map<String, RuleActionSpec<Object>> valuesMap() {
		return RULE_ACTION_SPECS.toMap();
	}
	
	private Socks5RuleActionSpecConstants() { }

}
