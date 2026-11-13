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
			description = "Specifies the binding host name or address for the "
					+ "remote-facing TCP server socket if the provided host " 
					+ "address is all zeros",
			name = "socks5.onBindRequest.remoteFacingServerSocket.bindHost",
			syntax = "socks5.onBindRequest.remoteFacingServerSocket.bindHost=HOST",
			valueType = Host.class
	)
	public static final RuleActionSpec<Host> SOCKS5_ON_BIND_REQUEST_REMOTE_FACING_SERVER_SOCKET_BIND_HOST = RULE_ACTION_SPECS.addThenGet(new HostRuleActionSpec(
			"socks5.onBindRequest.remoteFacingServerSocket.bindHost"));

	@NameValuePairValueSpecDoc(
			description = "Specifies an acceptable binding host address type "
					+ "for the remote-facing TCP server socket if the " 
					+ "provided host address is all zeros (can be specified " 
					+ "multiple times with each rule specifying another host " 
					+ "address type)",
			name = "socks5.onBindRequest.remoteFacingServerSocket.bindHostAddressType",
			syntax = "socks5.onBindRequest.remoteFacingServerSocket.bindHostAddressType=HOST_ADDRESS_TYPE",
			valueType = HostAddressType.class
	)
	public static final RuleActionSpec<HostAddressType> SOCKS5_ON_BIND_REQUEST_REMOTE_FACING_SERVER_SOCKET_BIND_HOST_ADDRESS_TYPE = RULE_ACTION_SPECS.addThenGet(new HostAddressTypeRuleActionSpec(
			"socks5.onBindRequest.remoteFacingServerSocket.bindHostAddressType"));

	@NameValuePairValueSpecDoc(
			description = "Specifies a binding port range for the " 
					+ "remote-facing TCP server socket if the provided port " 
					+ "is zero (can be specified multiple times with each " 
					+ "rule action specifying another port range)",
			name = "socks5.onBindRequest.remoteFacingServerSocket.bindPortRange",
			syntax = "socks5.onBindRequest.remoteFacingServerSocket.bindPortRange=PORT_RANGE",
			valueType = PortRange.class
	)
	public static final RuleActionSpec<PortRange> SOCKS5_ON_BIND_REQUEST_REMOTE_FACING_SERVER_SOCKET_BIND_PORT_RANGE = RULE_ACTION_SPECS.addThenGet(new PortRangeRuleActionSpec(
			"socks5.onBindRequest.remoteFacingServerSocket.bindPortRange"));

	@NameValuePairValueSpecDoc(
			description = "Specifies the network interface that provides a "
					+ "binding host address for the remote-facing TCP server " 
					+ "socket if the provided host address is all zeros",
			name = "socks5.onBindRequest.remoteFacingServerSocket.netInterface",
			syntax = "socks5.onBindRequest.remoteFacingServerSocket.netInterface=NETWORK_INTERFACE",
			valueType = NetInterface.class
	)
	public static final RuleActionSpec<NetInterface> SOCKS5_ON_BIND_REQUEST_REMOTE_FACING_SERVER_SOCKET_NET_INTERFACE = RULE_ACTION_SPECS.addThenGet(new NetInterfaceRuleActionSpec(
			"socks5.onBindRequest.remoteFacingServerSocket.netInterface"));

	@NameValuePairValueSpecDoc(
			description = "Specifies a socket setting for the remote-facing " 
					+ "TCP server socket (can be specified multiple times " 
					+ "with each rule action specifying another socket " 
					+ "setting)",
			name = "socks5.onBindRequest.remoteFacingServerSocket.socketSetting",
			syntax = "socks5.onBindRequest.remoteFacingServerSocket.socketSetting=SOCKET_SETTING",
			valueType = SocketSetting.class
	)	
	public static final RuleActionSpec<SocketSetting<Object>> SOCKS5_ON_BIND_REQUEST_REMOTE_FACING_SERVER_SOCKET_SOCKET_SETTING = RULE_ACTION_SPECS.addThenGet(new SocketSettingRuleActionSpec(
			"socks5.onBindRequest.remoteFacingServerSocket.socketSetting"));

	@NameValuePairValueSpecDoc(
			description = "Specifies a socket setting for the remote TCP "
					+ "socket (can be specified multiple times with each "
					+ "rule action specifying another socket setting)",
			name = "socks5.onBindRequest.remoteSocket.socketSetting",
			syntax = "socks5.onBindRequest.remoteSocket.socketSetting=SOCKET_SETTING",
			valueType = SocketSetting.class
	)
	public static final RuleActionSpec<SocketSetting<Object>> SOCKS5_ON_BIND_REQUEST_REMOTE_SOCKET_SOCKET_SETTING = RULE_ACTION_SPECS.addThenGet(new SocketSettingRuleActionSpec(
			"socks5.onBindRequest.remoteSocket.socketSetting"));

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
					+ "remote TCP socket",
			name = "socks5.onConnectRequest.remoteSocket.bindHost",
			syntax = "socks5.onConnectRequest.remoteSocket.bindHost=HOST",
			valueType = Host.class
	)	
	public static final RuleActionSpec<Host> SOCKS5_ON_CONNECT_REQUEST_REMOTE_SOCKET_BIND_HOST = RULE_ACTION_SPECS.addThenGet(new HostRuleActionSpec(
			"socks5.onConnectRequest.remoteSocket.bindHost"));

	@NameValuePairValueSpecDoc(
			description = "Specifies an acceptable binding host address type "
					+ "for the remote TCP socket (can be specified multiple "
					+ "times with each rule action specifying another host "
					+ "address type)",
			name = "socks5.onConnectRequest.remoteSocket.bindHostAddressType",
			syntax = "socks5.onConnectRequest.remoteSocket.bindHostAddressType=HOST_ADDRESS_TYPE",
			valueType = HostAddressType.class
	)
	public static final RuleActionSpec<HostAddressType> SOCKS5_ON_CONNECT_REQUEST_REMOTE_SOCKET_BIND_HOST_ADDRESS_TYPE = RULE_ACTION_SPECS.addThenGet(new HostAddressTypeRuleActionSpec(
			"socks5.onConnectRequest.remoteSocket.bindHostAddressType"));

	@NameValuePairValueSpecDoc(
			description = "Specifies a binding port range for the remote TCP "
					+ "socket (can be specified multiple times with each "
					+ "rule action specifying another port range)",
			name = "socks5.onConnectRequest.remoteSocket.bindPortRange",
			syntax = "socks5.onConnectRequest.remoteSocket.bindPortRange=PORT_RANGE",
			valueType = PortRange.class
	)	
	public static final RuleActionSpec<PortRange> SOCKS5_ON_CONNECT_REQUEST_REMOTE_SOCKET_BIND_PORT_RANGE = RULE_ACTION_SPECS.addThenGet(new PortRangeRuleActionSpec(
			"socks5.onConnectRequest.remoteSocket.bindPortRange"));
	
	@NameValuePairValueSpecDoc(
			description = "Specifies the timeout in milliseconds on waiting " 
					+ "for the remote TCP socket to connect",
			name = "socks5.onConnectRequest.remoteSocket.connectTimeout",
			syntax = "socks5.onConnectRequest.remoteSocket.connectTimeout=POSITIVE_INTEGER",
			valueType = PositiveInteger.class
	)	
	public static final RuleActionSpec<PositiveInteger> SOCKS5_ON_CONNECT_REQUEST_REMOTE_SOCKET_CONNECT_TIMEOUT = RULE_ACTION_SPECS.addThenGet(new PositiveIntegerRuleActionSpec(
			"socks5.onConnectRequest.remoteSocket.connectTimeout"));

	@NameValuePairValueSpecDoc(
			description = "Specifies the network interface that provides a "
					+ "binding host address for the remote TCP socket",
			name = "socks5.onConnectRequest.remoteSocket.netInterface",
			syntax = "socks5.onConnectRequest.remoteSocket.netInterface=NETWORK_INTERFACE",
			valueType = NetInterface.class
	)
	public static final RuleActionSpec<NetInterface> SOCKS5_ON_CONNECT_REQUEST_REMOTE_SOCKET_NET_INTERFACE = RULE_ACTION_SPECS.addThenGet(new NetInterfaceRuleActionSpec(
			"socks5.onConnectRequest.remoteSocket.netInterface"));

	@NameValuePairValueSpecDoc(
			description = "Specifies the boolean value to indicate if the "
					+ "remote TCP socket is to be prepared before connecting "
					+ "(involves applying the specified socket settings, "
					+ "resolving the target host name, and setting the "
					+ "specified timeout on waiting to connect)",
			name = "socks5.onConnectRequest.remoteSocket.prepareSocket",
			syntax = "socks5.onConnectRequest.remoteSocket.prepareSocket=true|false",
			valueType = Boolean.class
	)
	public static final RuleActionSpec<Boolean> SOCKS5_ON_CONNECT_REQUEST_REMOTE_SOCKET_PREPARE_SOCKET = RULE_ACTION_SPECS.addThenGet(new BooleanRuleActionSpec(
			"socks5.onConnectRequest.remoteSocket.prepareSocket"));

	@NameValuePairValueSpecDoc(
			description = "Specifies a socket setting for the remote TCP "
					+ "socket (can be specified multiple times with each "
					+ "rule action specifying another socket setting)",
			name = "socks5.onConnectRequest.remoteSocket.socketSetting",
			syntax = "socks5.onConnectRequest.remoteSocket.socketSetting=SOCKET_SETTING",
			valueType = SocketSetting.class
	)	
	public static final RuleActionSpec<SocketSetting<Object>> SOCKS5_ON_CONNECT_REQUEST_REMOTE_SOCKET_SOCKET_SETTING = RULE_ACTION_SPECS.addThenGet(new SocketSettingRuleActionSpec(
			"socks5.onConnectRequest.remoteSocket.socketSetting"));

	@NameValuePairValueSpecDoc(
			description = "Specifies the binding host name or address for the "
					+ "client-facing UDP socket",
			name = "socks5.onUdpAssociateRequest.clientFacingDatagramSocket.bindHost",
			syntax = "socks5.onUdpAssociateRequest.clientFacingDatagramSocket.bindHost=HOST",
			valueType = Host.class
	)
	public static final RuleActionSpec<Host> SOCKS5_ON_UDP_ASSOCIATE_REQUEST_CLIENT_FACING_DATAGRAM_SOCKET_BIND_HOST = RULE_ACTION_SPECS.addThenGet(new HostRuleActionSpec(
			"socks5.onUdpAssociateRequest.clientFacingDatagramSocket.bindHost"));

	@NameValuePairValueSpecDoc(
			description = "Specifies an acceptable binding host address type "
					+ "for the client-facing UDP socket (can be specified "
					+ "multiple times with each rule action specifying "
					+ "another host address type)",
			name = "socks5.onUdpAssociateRequest.clientFacingDatagramSocket.bindHostAddressType",
			syntax = "socks5.onUdpAssociateRequest.clientFacingDatagramSocket.bindHostAddressType=HOST_ADDRESS_TYPE",
			valueType = HostAddressType.class
	)
	public static final RuleActionSpec<HostAddressType> SOCKS5_ON_UDP_ASSOCIATE_REQUEST_CLIENT_FACING_DATAGRAM_SOCKET_BIND_HOST_ADDRESS_TYPE = RULE_ACTION_SPECS.addThenGet(new HostAddressTypeRuleActionSpec(
			"socks5.onUdpAssociateRequest.clientFacingDatagramSocket.bindHostAddressType"));

	@NameValuePairValueSpecDoc(
			description = "Specifies a binding port range for the client-facing "
					+ "UDP socket (can be specified multiple times with each "
					+ "rule action specifying another port range)",
			name = "socks5.onUdpAssociateRequest.clientFacingDatagramSocket.bindPortRange",
			syntax = "socks5.onUdpAssociateRequest.clientFacingDatagramSocket.bindPortRange=PORT_RANGE",
			valueType = PortRange.class
	)	
	public static final RuleActionSpec<PortRange> SOCKS5_ON_UDP_ASSOCIATE_REQUEST_CLIENT_FACING_DATAGRAM_SOCKET_BIND_PORT_RANGE = RULE_ACTION_SPECS.addThenGet(new PortRangeRuleActionSpec(
			"socks5.onUdpAssociateRequest.clientFacingDatagramSocket.bindPortRange"));

	@NameValuePairValueSpecDoc(
			description = "Specifies the network interface that provides a "
					+ "binding host address for the client-facing UDP socket",
			name = "socks5.onUdpAssociateRequest.clientFacingDatagramSocket.netInterface",
			syntax = "socks5.onUdpAssociateRequest.clientFacingDatagramSocket.netInterface=NETWORK_INTERFACE",
			valueType = NetInterface.class
	)
	public static final RuleActionSpec<NetInterface> SOCKS5_ON_UDP_ASSOCIATE_REQUEST_CLIENT_FACING_DATAGRAM_SOCKET_NET_INTERFACE = RULE_ACTION_SPECS.addThenGet(new NetInterfaceRuleActionSpec(
			"socks5.onUdpAssociateRequest.clientFacingDatagramSocket.netInterface"));

	@NameValuePairValueSpecDoc(
			description = "Specifies a socket setting for the client-facing UDP "
					+ "socket (can be specified multiple times with each rule "
					+ "action specifying another socket setting)",
			name = "socks5.onUdpAssociateRequest.clientFacingDatagramSocket.socketSetting",
			syntax = "socks5.onUdpAssociateRequest.clientFacingDatagramSocket.socketSetting=SOCKET_SETTING",
			valueType = SocketSetting.class
	)	
	public static final RuleActionSpec<SocketSetting<Object>> SOCKS5_ON_UDP_ASSOCIATE_REQUEST_CLIENT_FACING_DATAGRAM_SOCKET_SOCKET_SETTING = RULE_ACTION_SPECS.addThenGet(new SocketSettingRuleActionSpec(
			"socks5.onUdpAssociateRequest.clientFacingDatagramSocket.socketSetting"));

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
			description = "Specifies the binding host name or address for the "
					+ "remote-facing UDP socket",
			name = "socks5.onUdpAssociateRequest.remoteFacingDatagramSocket.bindHost",
			syntax = "socks5.onUdpAssociateRequest.remoteFacingDatagramSocket.bindHost=HOST",
			valueType = Host.class
	)	
	public static final RuleActionSpec<Host> SOCKS5_ON_UDP_ASSOCIATE_REQUEST_REMOTE_FACING_DATAGRAM_SOCKET_BIND_HOST = RULE_ACTION_SPECS.addThenGet(new HostRuleActionSpec(
			"socks5.onUdpAssociateRequest.remoteFacingDatagramSocket.bindHost"));

	@NameValuePairValueSpecDoc(
			description = "Specifies an acceptable binding host address type "
					+ "for the remote-facing UDP socket (can be specified "
					+ "multiple times with each rule action specifying "
					+ "another host address type)",
			name = "socks5.onUdpAssociateRequest.remoteFacingDatagramSocket.bindHostAddressType",
			syntax = "socks5.onUdpAssociateRequest.remoteFacingDatagramSocket.bindHostAddressType=HOST_ADDRESS_TYPE",
			valueType = HostAddressType.class
	)
	public static final RuleActionSpec<HostAddressType> SOCKS5_ON_UDP_ASSOCIATE_REQUEST_REMOTE_FACING_DATAGRAM_SOCKET_BIND_HOST_ADDRESS_TYPE = RULE_ACTION_SPECS.addThenGet(new HostAddressTypeRuleActionSpec(
			"socks5.onUdpAssociateRequest.remoteFacingDatagramSocket.bindHostAddressType"));

	@NameValuePairValueSpecDoc(
			description = "Specifies a binding port range for the remote-facing "
					+ "UDP socket (can be specified multiple times with each "
					+ "rule action specifying another port range)",
			name = "socks5.onUdpAssociateRequest.remoteFacingDatagramSocket.bindPortRange",
			syntax = "socks5.onUdpAssociateRequest.remoteFacingDatagramSocket.bindPortRange=PORT_RANGE",
			valueType = PortRange.class
	)	
	public static final RuleActionSpec<PortRange> SOCKS5_ON_UDP_ASSOCIATE_REQUEST_REMOTE_FACING_DATAGRAM_SOCKET_BIND_PORT_RANGE = RULE_ACTION_SPECS.addThenGet(new PortRangeRuleActionSpec(
			"socks5.onUdpAssociateRequest.remoteFacingDatagramSocket.bindPortRange"));

	@NameValuePairValueSpecDoc(
			description = "Specifies the network interface that provides a "
					+ "binding host address for the remote-facing UDP socket",
			name = "socks5.onUdpAssociateRequest.remoteFacingDatagramSocket.netInterface",
			syntax = "socks5.onUdpAssociateRequest.remoteFacingDatagramSocket.netInterface=NETWORK_INTERFACE",
			valueType = NetInterface.class
	)
	public static final RuleActionSpec<NetInterface> SOCKS5_ON_UDP_ASSOCIATE_REQUEST_REMOTE_FACING_DATAGRAM_SOCKET_NET_INTERFACE = RULE_ACTION_SPECS.addThenGet(new NetInterfaceRuleActionSpec(
			"socks5.onUdpAssociateRequest.remoteFacingDatagramSocket.netInterface"));

	@NameValuePairValueSpecDoc(
			description = "Specifies a socket setting for the remote-facing UDP "
					+ "socket (can be specified multiple times with each rule "
					+ "action specifying another socket setting)",
			name = "socks5.onUdpAssociateRequest.remoteFacingDatagramSocket.socketSetting",
			syntax = "socks5.onUdpAssociateRequest.remoteFacingDatagramSocket.socketSetting=SOCKET_SETTING",
			valueType = SocketSetting.class
	)	
	public static final RuleActionSpec<SocketSetting<Object>> SOCKS5_ON_UDP_ASSOCIATE_REQUEST_REMOTE_FACING_DATAGRAM_SOCKET_SOCKET_SETTING = RULE_ACTION_SPECS.addThenGet(new SocketSettingRuleActionSpec(
			"socks5.onUdpAssociateRequest.remoteFacingDatagramSocket.socketSetting"));

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
