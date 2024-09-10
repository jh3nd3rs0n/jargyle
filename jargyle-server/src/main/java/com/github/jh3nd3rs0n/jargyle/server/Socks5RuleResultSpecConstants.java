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
			description = "Specifies a socket setting for the inbound socket "
					+ "(can be specified multiple times with each rule result "
					+ "specifying another socket setting)",
			name = "socks5.onBindRequest.inboundSocketSetting",
			syntax = "socks5.onBindRequest.inboundSocketSetting=SOCKET_SETTING",
			valueType = SocketSetting.class
	)	
	public static final RuleResultSpec<SocketSetting<Object>> SOCKS5_ON_BIND_REQUEST_INBOUND_SOCKET_SETTING = RULE_RESULT_SPECS.addThenGet(new SocketSettingRuleResultSpec(
			"socks5.onBindRequest.inboundSocketSetting"));

	@NameValuePairValueSpecDoc(
			description = "Specifies the binding host name or address for the "
					+ "listen socket if the provided host address is all zeros",
			name = "socks5.onBindRequest.listenBindHost",
			syntax = "socks5.onBindRequest.listenBindHost=HOST",
			valueType = Host.class
	)
	public static final RuleResultSpec<Host> SOCKS5_ON_BIND_REQUEST_LISTEN_BIND_HOST = RULE_RESULT_SPECS.addThenGet(new HostRuleResultSpec(
			"socks5.onBindRequest.listenBindHost"));
	
	@NameValuePairValueSpecDoc(
			description = "Specifies a binding port range for the listen socket "
					+ "if the provided port is zero (can be specified multiple "
					+ "times with each rule result specifying another port "
					+ "range)",
			name = "socks5.onBindRequest.listenBindPortRange",
			syntax = "socks5.onBindRequest.listenBindPortRange=PORT_RANGE",
			valueType = PortRange.class
	)
	public static final RuleResultSpec<PortRange> SOCKS5_ON_BIND_REQUEST_LISTEN_BIND_PORT_RANGE = RULE_RESULT_SPECS.addThenGet(new PortRangeRuleResultSpec(
			"socks5.onBindRequest.listenBindPortRange"));
	
	@NameValuePairValueSpecDoc(
			description = "Specifies a socket setting for the listen socket "
					+ "(can be specified multiple times with each rule result "
					+ "specifying another socket setting)",
			name = "socks5.onBindRequest.listenSocketSetting",
			syntax = "socks5.onBindRequest.listenSocketSetting=SOCKET_SETTING",
			valueType = SocketSetting.class
	)	
	public static final RuleResultSpec<SocketSetting<Object>> SOCKS5_ON_BIND_REQUEST_LISTEN_SOCKET_SETTING = RULE_RESULT_SPECS.addThenGet(new SocketSettingRuleResultSpec(
			"socks5.onBindRequest.listenSocketSetting"));
	
	@NameValuePairValueSpecDoc(
			description = "Specifies the buffer size in bytes for relaying the "
					+ "data",
			name = "socks5.onBindRequest.relayBufferSize",
			syntax = "socks5.onBindRequest.relayBufferSize=POSITIVE_INTEGER",
			valueType = PositiveInteger.class
	)	
	public static final RuleResultSpec<PositiveInteger> SOCKS5_ON_BIND_REQUEST_RELAY_BUFFER_SIZE = RULE_RESULT_SPECS.addThenGet(new PositiveIntegerRuleResultSpec(
			"socks5.onBindRequest.relayBufferSize"));
	
	@NameValuePairValueSpecDoc(
			description = "Specifies the timeout in milliseconds on relaying "
					+ "no data",
			name = "socks5.onBindRequest.relayIdleTimeout",
			syntax = "socks5.onBindRequest.relayIdleTimeout=POSITIVE_INTEGER",
			valueType = PositiveInteger.class
	)	
	public static final RuleResultSpec<PositiveInteger> SOCKS5_ON_BIND_REQUEST_RELAY_IDLE_TIMEOUT = RULE_RESULT_SPECS.addThenGet(new PositiveIntegerRuleResultSpec(
			"socks5.onBindRequest.relayIdleTimeout"));

	@NameValuePairValueSpecDoc(
			description = "Specifies the upper limit on bandwidth in bytes per "
					+ "second of receiving inbound data to be relayed",
			name = "socks5.onBindRequest.relayInboundBandwidthLimit",
			syntax = "socks5.onBindRequest.relayInboundBandwidthLimit=POSITIVE_INTEGER",
			valueType = PositiveInteger.class
	)
	public static final RuleResultSpec<PositiveInteger> SOCKS5_ON_BIND_REQUEST_RELAY_INBOUND_BANDWIDTH_LIMIT = RULE_RESULT_SPECS.addThenGet(new PositiveIntegerRuleResultSpec(
			"socks5.onBindRequest.relayInboundBandwidthLimit"));

	@NameValuePairValueSpecDoc(
			description = "Specifies the upper limit on bandwidth in bytes per "
					+ "second of receiving outbound data to be relayed",
			name = "socks5.onBindRequest.relayOutboundBandwidthLimit",
			syntax = "socks5.onBindRequest.relayOutboundBandwidthLimit=POSITIVE_INTEGER",
			valueType = PositiveInteger.class
	)
	public static final RuleResultSpec<PositiveInteger> SOCKS5_ON_BIND_REQUEST_RELAY_OUTBOUND_BANDWIDTH_LIMIT = RULE_RESULT_SPECS.addThenGet(new PositiveIntegerRuleResultSpec(
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
	public static final RuleResultSpec<Boolean> SOCKS5_ON_CONNECT_REQUEST_PREPARE_TARGET_FACING_SOCKET = RULE_RESULT_SPECS.addThenGet(new BooleanRuleResultSpec(
			"socks5.onConnectRequest.prepareTargetFacingSocket"));

	@NameValuePairValueSpecDoc(
			description = "Specifies the buffer size in bytes for relaying the "
					+ "data",
			name = "socks5.onConnectRequest.relayBufferSize",
			syntax = "socks5.onConnectRequest.relayBufferSize=POSITIVE_INTEGER",
			valueType = PositiveInteger.class
	)	
	public static final RuleResultSpec<PositiveInteger> SOCKS5_ON_CONNECT_REQUEST_RELAY_BUFFER_SIZE = RULE_RESULT_SPECS.addThenGet(new PositiveIntegerRuleResultSpec(
			"socks5.onConnectRequest.relayBufferSize"));
	
	@NameValuePairValueSpecDoc(
			description = "Specifies the timeout in milliseconds on relaying "
					+ "no data",
			name = "socks5.onConnectRequest.relayIdleTimeout",
			syntax = "socks5.onConnectRequest.relayIdleTimeout=POSITIVE_INTEGER",
			valueType = PositiveInteger.class
	)	
	public static final RuleResultSpec<PositiveInteger> SOCKS5_ON_CONNECT_REQUEST_RELAY_IDLE_TIMEOUT = RULE_RESULT_SPECS.addThenGet(new PositiveIntegerRuleResultSpec(
			"socks5.onConnectRequest.relayIdleTimeout"));

	@NameValuePairValueSpecDoc(
			description = "Specifies the upper limit on bandwidth in bytes per "
					+ "second of receiving inbound data to be relayed",
			name = "socks5.onConnectRequest.relayInboundBandwidthLimit",
			syntax = "socks5.onConnectRequest.relayInboundBandwidthLimit=POSITIVE_INTEGER",
			valueType = PositiveInteger.class
	)
	public static final RuleResultSpec<PositiveInteger> SOCKS5_ON_CONNECT_REQUEST_RELAY_INBOUND_BANDWIDTH_LIMIT = RULE_RESULT_SPECS.addThenGet(new PositiveIntegerRuleResultSpec(
			"socks5.onConnectRequest.relayInboundBandwidthLimit"));

	@NameValuePairValueSpecDoc(
			description = "Specifies the upper limit on bandwidth in bytes per "
					+ "second of receiving outbound data to be relayed",
			name = "socks5.onConnectRequest.relayOutboundBandwidthLimit",
			syntax = "socks5.onConnectRequest.relayOutboundBandwidthLimit=POSITIVE_INTEGER",
			valueType = PositiveInteger.class
	)
	public static final RuleResultSpec<PositiveInteger> SOCKS5_ON_CONNECT_REQUEST_RELAY_OUTBOUND_BANDWIDTH_LIMIT = RULE_RESULT_SPECS.addThenGet(new PositiveIntegerRuleResultSpec(
			"socks5.onConnectRequest.relayOutboundBandwidthLimit"));
	
	@NameValuePairValueSpecDoc(
			description = "Specifies the binding host name or address for the "
					+ "target-facing socket",
			name = "socks5.onConnectRequest.targetFacingBindHost",
			syntax = "socks5.onConnectRequest.targetFacingBindHost=HOST",
			valueType = Host.class
	)	
	public static final RuleResultSpec<Host> SOCKS5_ON_CONNECT_REQUEST_TARGET_FACING_BIND_HOST = RULE_RESULT_SPECS.addThenGet(new HostRuleResultSpec(
			"socks5.onConnectRequest.targetFacingBindHost"));
	
	@NameValuePairValueSpecDoc(
			description = "Specifies a binding port range for the target-facing "
					+ "socket (can be specified multiple times with each rule "
					+ "result specifying another port range)",
			name = "socks5.onConnectRequest.targetFacingBindPortRange",
			syntax = "socks5.onConnectRequest.targetFacingBindPortRange=PORT_RANGE",
			valueType = PortRange.class
	)	
	public static final RuleResultSpec<PortRange> SOCKS5_ON_CONNECT_REQUEST_TARGET_FACING_BIND_PORT_RANGE = RULE_RESULT_SPECS.addThenGet(new PortRangeRuleResultSpec(
			"socks5.onConnectRequest.targetFacingBindPortRange"));
	
	@NameValuePairValueSpecDoc(
			description = "Specifies the timeout in milliseconds on waiting for "
					+ "the target-facing socket to connect",
			name = "socks5.onConnectRequest.targetFacingConnectTimeout",
			syntax = "socks5.onConnectRequest.targetFacingConnectTimeout=POSITIVE_INTEGER",
			valueType = PositiveInteger.class
	)	
	public static final RuleResultSpec<PositiveInteger> SOCKS5_ON_CONNECT_REQUEST_TARGET_FACING_CONNECT_TIMEOUT = RULE_RESULT_SPECS.addThenGet(new PositiveIntegerRuleResultSpec(
			"socks5.onConnectRequest.targetFacingConnectTimeout"));
	
	@NameValuePairValueSpecDoc(
			description = "Specifies a socket setting for the target-facing "
					+ "socket (can be specified multiple times with each rule "
					+ "result specifying another socket setting)",
			name = "socks5.onConnectRequest.targetFacingSocketSetting",
			syntax = "socks5.onConnectRequest.targetFacingSocketSetting=SOCKET_SETTING",
			valueType = SocketSetting.class
	)	
	public static final RuleResultSpec<SocketSetting<Object>> SOCKS5_ON_CONNECT_REQUEST_TARGET_FACING_SOCKET_SETTING = RULE_RESULT_SPECS.addThenGet(new SocketSettingRuleResultSpec(
			"socks5.onConnectRequest.targetFacingSocketSetting"));

	@NameValuePairValueSpecDoc(
			description = "Specifies the binding host name or address for all "
					+ "sockets",
			name = "socks5.onRequest.bindHost",
			syntax = "socks5.onRequest.bindHost=HOST",
			valueType = Host.class
	)
	public static final RuleResultSpec<Host> SOCKS5_ON_REQUEST_BIND_HOST = RULE_RESULT_SPECS.addThenGet(new HostRuleResultSpec(
			"socks5.onRequest.bindHost"));

	@NameValuePairValueSpecDoc(
			description = "Specifies a binding port range for all TCP sockets "
					+ "(can be specified multiple times with each rule result "
					+ "specifying another port range)",
			name = "socks5.onRequest.bindTcpPortRange",
			syntax = "socks5.onRequest.bindTcpPortRange=PORT_RANGE",
			valueType = PortRange.class
	)
	public static final RuleResultSpec<PortRange> SOCKS5_ON_REQUEST_BIND_TCP_PORT_RANGE = RULE_RESULT_SPECS.addThenGet(new PortRangeRuleResultSpec(
			"socks5.onRequest.bindTcpPortRange"));

	@NameValuePairValueSpecDoc(
			description = "Specifies a binding port range for all UDP sockets "
					+ "(can be specified multiple times with each rule result "
					+ "specifying another port range)",
			name = "socks5.onRequest.bindUdpPortRange",
			syntax = "socks5.onRequest.bindUdpPortRange=PORT_RANGE",
			valueType = PortRange.class
	)
	public static final RuleResultSpec<PortRange> SOCKS5_ON_REQUEST_BIND_UDP_PORT_RANGE = RULE_RESULT_SPECS.addThenGet(new PortRangeRuleResultSpec(
			"socks5.onRequest.bindUdpPortRange"));

	@NameValuePairValueSpecDoc(
			description = "Specifies the binding host name or address for all "
					+ "external-facing sockets",
			name = "socks5.onRequest.externalFacingBindHost",
			syntax = "socks5.onRequest.externalFacingBindHost=HOST",
			valueType = Host.class
	)
	public static final RuleResultSpec<Host> SOCKS5_ON_REQUEST_EXTERNAL_FACING_BIND_HOST = RULE_RESULT_SPECS.addThenGet(new HostRuleResultSpec(
			"socks5.onRequest.externalFacingBindHost"));

	@NameValuePairValueSpecDoc(
			description = "Specifies a binding port range for all "
					+ "external-facing TCP sockets (can be specified multiple "
					+ "times with each rule result specifying another port "
					+ "range)",
			name = "socks5.onRequest.externalFacingBindTcpPortRange",
			syntax = "socks5.onRequest.externalFacingBindTcpPortRange=PORT_RANGE",
			valueType = PortRange.class
	)
	public static final RuleResultSpec<PortRange> SOCKS5_ON_REQUEST_EXTERNAL_FACING_BIND_TCP_PORT_RANGE = RULE_RESULT_SPECS.addThenGet(new PortRangeRuleResultSpec(
			"socks5.onRequest.externalFacingBindTcpPortRange"));

	@NameValuePairValueSpecDoc(
			description = "Specifies a binding port range for all "
					+ "external-facing UDP sockets (can be specified multiple "
					+ "times with each rule result specifying another port "
					+ "range)",
			name = "socks5.onRequest.externalFacingBindUdpPortRange",
			syntax = "socks5.onRequest.externalFacingBindUdpPortRange=PORT_RANGE",
			valueType = PortRange.class
	)
	public static final RuleResultSpec<PortRange> SOCKS5_ON_REQUEST_EXTERNAL_FACING_BIND_UDP_PORT_RANGE = RULE_RESULT_SPECS.addThenGet(new PortRangeRuleResultSpec(
			"socks5.onRequest.externalFacingBindUdpPortRange"));

	@NameValuePairValueSpecDoc(
			description = "Specifies a socket setting for all external-facing "
					+ "sockets (can be specified multiple times with each rule "
					+ "result specifying another socket setting)",
			name = "socks5.onRequest.externalFacingSocketSetting",
			syntax = "socks5.onRequest.externalFacingSocketSetting=SOCKET_SETTING",
			valueType = SocketSetting.class
	)
	public static final RuleResultSpec<SocketSetting<Object>> SOCKS5_ON_REQUEST_EXTERNAL_FACING_SOCKET_SETTING = RULE_RESULT_SPECS.addThenGet(new SocketSettingRuleResultSpec(
			"socks5.onRequest.externalFacingSocketSetting"));

	@NameValuePairValueSpecDoc(
			description = "Specifies the binding host name or address for all "
					+ "internal-facing sockets",
			name = "socks5.onRequest.internalFacingBindHost",
			syntax = "socks5.onRequest.internalFacingBindHost=HOST",
			valueType = Host.class
	)
	public static final RuleResultSpec<Host> SOCKS5_ON_REQUEST_INTERNAL_FACING_BIND_HOST = RULE_RESULT_SPECS.addThenGet(new HostRuleResultSpec(
			"socks5.onRequest.internalFacingBindHost"));

	@NameValuePairValueSpecDoc(
			description = "Specifies a binding port range for all "
					+ "internal-facing UDP sockets (can be specified multiple "
					+ "times with each rule result specifying another port "
					+ "range)",
			name = "socks5.onRequest.internalFacingBindUdpPortRange",
			syntax = "socks5.onRequest.internalFacingBindUdpPortRange=PORT_RANGE",
			valueType = PortRange.class
	)
	public static final RuleResultSpec<PortRange> SOCKS5_ON_REQUEST_INTERNAL_FACING_BIND_UDP_PORT_RANGE = RULE_RESULT_SPECS.addThenGet(new PortRangeRuleResultSpec(
			"socks5.onRequest.internalFacingBindUdpPortRange"));

	@NameValuePairValueSpecDoc(
			description = "Specifies a socket setting for all internal-facing "
					+ "sockets (can be specified multiple times with each rule "
					+ "result specifying another socket setting)",
			name = "socks5.onRequest.internalFacingSocketSetting",
			syntax = "socks5.onRequest.internalFacingSocketSetting=SOCKET_SETTING",
			valueType = SocketSetting.class
	)
	public static final RuleResultSpec<SocketSetting<Object>> SOCKS5_ON_REQUEST_INTERNAL_FACING_SOCKET_SETTING = RULE_RESULT_SPECS.addThenGet(new SocketSettingRuleResultSpec(
			"socks5.onRequest.internalFacingSocketSetting"));

	@NameValuePairValueSpecDoc(
			description = "Specifies the buffer size in bytes for relaying the "
					+ "data",
			name = "socks5.onRequest.relayBufferSize",
			syntax = "socks5.onRequest.relayBufferSize=POSITIVE_INTEGER",
			valueType = PositiveInteger.class
	)
	public static final RuleResultSpec<PositiveInteger> SOCKS5_ON_REQUEST_RELAY_BUFFER_SIZE = RULE_RESULT_SPECS.addThenGet(new PositiveIntegerRuleResultSpec(
			"socks5.onRequest.relayBufferSize"));

	@NameValuePairValueSpecDoc(
			description = "Specifies the timeout in milliseconds on relaying "
					+ "no data",
			name = "socks5.onRequest.relayIdleTimeout",
			syntax = "socks5.onRequest.relayIdleTimeout=POSITIVE_INTEGER",
			valueType = PositiveInteger.class
	)
	public static final RuleResultSpec<PositiveInteger> SOCKS5_ON_REQUEST_RELAY_IDLE_TIMEOUT = RULE_RESULT_SPECS.addThenGet(new PositiveIntegerRuleResultSpec(
			"socks5.onRequest.relayIdleTimeout"));

	@NameValuePairValueSpecDoc(
			description = "Specifies the upper limit on bandwidth in bytes per "
					+ "second of receiving inbound data to be relayed",
			name = "socks5.onRequest.relayInboundBandwidthLimit",
			syntax = "socks5.onRequest.relayInboundBandwidthLimit=POSITIVE_INTEGER",
			valueType = PositiveInteger.class
	)
	public static final RuleResultSpec<PositiveInteger> SOCKS5_ON_REQUEST_RELAY_INBOUND_BANDWIDTH_LIMIT = RULE_RESULT_SPECS.addThenGet(new PositiveIntegerRuleResultSpec(
			"socks5.onRequest.relayInboundBandwidthLimit"));

	@NameValuePairValueSpecDoc(
			description = "Specifies the upper limit on bandwidth in bytes per "
					+ "second of receiving outbound data to be relayed",
			name = "socks5.onRequest.relayOutboundBandwidthLimit",
			syntax = "socks5.onRequest.relayOutboundBandwidthLimit=POSITIVE_INTEGER",
			valueType = PositiveInteger.class
	)
	public static final RuleResultSpec<PositiveInteger> SOCKS5_ON_REQUEST_RELAY_OUTBOUND_BANDWIDTH_LIMIT = RULE_RESULT_SPECS.addThenGet(new PositiveIntegerRuleResultSpec(
			"socks5.onRequest.relayOutboundBandwidthLimit"));

	@NameValuePairValueSpecDoc(
			description = "Specifies a socket setting for all sockets (can be "
					+ "specified multiple times with each rule result "
					+ "specifying another socket setting)",
			name = "socks5.onRequest.socketSetting",
			syntax = "socks5.onRequest.socketSetting=SOCKET_SETTING",
			valueType = SocketSetting.class
	)
	public static final RuleResultSpec<SocketSetting<Object>> SOCKS5_ON_REQUEST_SOCKET_SETTING = RULE_RESULT_SPECS.addThenGet(new SocketSettingRuleResultSpec(
			"socks5.onRequest.socketSetting"));

	@NameValuePairValueSpecDoc(
			description = "Specifies the binding host name or address for the "
					+ "client-facing UDP socket",
			name = "socks5.onUdpAssociateRequest.clientFacingBindHost",
			syntax = "socks5.onUdpAssociateRequest.clientFacingBindHost=HOST",
			valueType = Host.class
	)
	public static final RuleResultSpec<Host> SOCKS5_ON_UDP_ASSOCIATE_REQUEST_CLIENT_FACING_BIND_HOST = RULE_RESULT_SPECS.addThenGet(new HostRuleResultSpec(
			"socks5.onUdpAssociateRequest.clientFacingBindHost"));
	
	@NameValuePairValueSpecDoc(
			description = "Specifies a binding port range for the client-facing "
					+ "UDP socket (can be specified multiple times with each "
					+ "rule result specifying another port range)",
			name = "socks5.onUdpAssociateRequest.clientFacingBindPortRange",
			syntax = "socks5.onUdpAssociateRequest.clientFacingBindPortRange=PORT_RANGE",
			valueType = PortRange.class
	)	
	public static final RuleResultSpec<PortRange> SOCKS5_ON_UDP_ASSOCIATE_REQUEST_CLIENT_FACING_BIND_PORT_RANGE = RULE_RESULT_SPECS.addThenGet(new PortRangeRuleResultSpec(
			"socks5.onUdpAssociateRequest.clientFacingBindPortRange"));
	
	@NameValuePairValueSpecDoc(
			description = "Specifies a socket setting for the client-facing UDP "
					+ "socket (can be specified multiple times with each rule "
					+ "result specifying another socket setting)",
			name = "socks5.onUdpAssociateRequest.clientFacingSocketSetting",
			syntax = "socks5.onUdpAssociateRequest.clientFacingSocketSetting=SOCKET_SETTING",
			valueType = SocketSetting.class
	)	
	public static final RuleResultSpec<SocketSetting<Object>> SOCKS5_ON_UDP_ASSOCIATE_REQUEST_CLIENT_FACING_SOCKET_SETTING = RULE_RESULT_SPECS.addThenGet(new SocketSettingRuleResultSpec(
			"socks5.onUdpAssociateRequest.clientFacingSocketSetting"));
	
	@NameValuePairValueSpecDoc(
			description = "Specifies the binding host name or address for the "
					+ "peer-facing UDP socket",
			name = "socks5.onUdpAssociateRequest.peerFacingBindHost",
			syntax = "socks5.onUdpAssociateRequest.peerFacingBindHost=HOST",
			valueType = Host.class
	)	
	public static final RuleResultSpec<Host> SOCKS5_ON_UDP_ASSOCIATE_REQUEST_PEER_FACING_BIND_HOST = RULE_RESULT_SPECS.addThenGet(new HostRuleResultSpec(
			"socks5.onUdpAssociateRequest.peerFacingBindHost"));

	@NameValuePairValueSpecDoc(
			description = "Specifies a binding port range for the peer-facing "
					+ "UDP socket (can be specified multiple times with each "
					+ "rule result specifying another port range)",
			name = "socks5.onUdpAssociateRequest.peerFacingBindPortRange",
			syntax = "socks5.onUdpAssociateRequest.peerFacingBindPortRange=PORT_RANGE",
			valueType = PortRange.class
	)	
	public static final RuleResultSpec<PortRange> SOCKS5_ON_UDP_ASSOCIATE_REQUEST_PEER_FACING_BIND_PORT_RANGE = RULE_RESULT_SPECS.addThenGet(new PortRangeRuleResultSpec(
			"socks5.onUdpAssociateRequest.peerFacingBindPortRange"));
	
	@NameValuePairValueSpecDoc(
			description = "Specifies a socket setting for the peer-facing UDP "
					+ "socket (can be specified multiple times with each rule "
					+ "result specifying another socket setting)",
			name = "socks5.onUdpAssociateRequest.peerFacingSocketSetting",
			syntax = "socks5.onUdpAssociateRequest.peerFacingSocketSetting=SOCKET_SETTING",
			valueType = SocketSetting.class
	)	
	public static final RuleResultSpec<SocketSetting<Object>> SOCKS5_ON_UDP_ASSOCIATE_REQUEST_PEER_FACING_SOCKET_SETTING = RULE_RESULT_SPECS.addThenGet(new SocketSettingRuleResultSpec(
			"socks5.onUdpAssociateRequest.peerFacingSocketSetting"));
	
	@NameValuePairValueSpecDoc(
			description = "Specifies the buffer size in bytes for relaying the "
					+ "data",
			name = "socks5.onUdpAssociateRequest.relayBufferSize",
			syntax = "socks5.onUdpAssociateRequest.relayBufferSize=POSITIVE_INTEGER",
			valueType = PositiveInteger.class
	)	
	public static final RuleResultSpec<PositiveInteger> SOCKS5_ON_UDP_ASSOCIATE_REQUEST_RELAY_BUFFER_SIZE = RULE_RESULT_SPECS.addThenGet(new PositiveIntegerRuleResultSpec(
			"socks5.onUdpAssociateRequest.relayBufferSize"));
	
	@NameValuePairValueSpecDoc(
			description = "Specifies the timeout in milliseconds on relaying no "
					+ "data",
			name = "socks5.onUdpAssociateRequest.relayIdleTimeout",
			syntax = "socks5.onUdpAssociateRequest.relayIdleTimeout=POSITIVE_INTEGER",
			valueType = PositiveInteger.class
	)	
	public static final RuleResultSpec<PositiveInteger> SOCKS5_ON_UDP_ASSOCIATE_REQUEST_RELAY_IDLE_TIMEOUT = RULE_RESULT_SPECS.addThenGet(new PositiveIntegerRuleResultSpec(
			"socks5.onUdpAssociateRequest.relayIdleTimeout"));

	@NameValuePairValueSpecDoc(
			description = "Specifies the upper limit on bandwidth in bytes per "
					+ "second of receiving inbound data to be relayed",
			name = "socks5.onUdpAssociateRequest.relayInboundBandwidthLimit",
			syntax = "socks5.onUdpAssociateRequest.relayInboundBandwidthLimit=POSITIVE_INTEGER",
			valueType = PositiveInteger.class
	)
	public static final RuleResultSpec<PositiveInteger> SOCKS5_ON_UDP_ASSOCIATE_REQUEST_RELAY_INBOUND_BANDWIDTH_LIMIT = RULE_RESULT_SPECS.addThenGet(new PositiveIntegerRuleResultSpec(
			"socks5.onUdpAssociateRequest.relayInboundBandwidthLimit"));

	@NameValuePairValueSpecDoc(
			description = "Specifies the upper limit on bandwidth in bytes per "
					+ "second of receiving outbound data to be relayed",
			name = "socks5.onUdpAssociateRequest.relayOutboundBandwidthLimit",
			syntax = "socks5.onUdpAssociateRequest.relayOutboundBandwidthLimit=POSITIVE_INTEGER",
			valueType = PositiveInteger.class
	)
	public static final RuleResultSpec<PositiveInteger> SOCKS5_ON_UDP_ASSOCIATE_REQUEST_RELAY_OUTBOUND_BANDWIDTH_LIMIT = RULE_RESULT_SPECS.addThenGet(new PositiveIntegerRuleResultSpec(
			"socks5.onUdpAssociateRequest.relayOutboundBandwidthLimit"));

	@NameValuePairValueSpecDoc(
			description = "Specifies the desired destination address "
					+ "redirect for the request",
			name = "socks5.request.desiredDestinationAddressRedirect",
			syntax = "socks5.request.desiredDestinationAddressRedirect=SOCKS5_ADDRESS",
			valueType = Address.class
	)
	public static final RuleResultSpec<Address> SOCKS5_REQUEST_DESIRED_DESTINATION_ADDRESS_REDIRECT = RULE_RESULT_SPECS.addThenGet(new AddressRuleResultSpec(
			"socks5.request.desiredDestinationAddressRedirect"));

	@NameValuePairValueSpecDoc(
			description = "Specifies the desired destination port redirect "
					+ "for the request",
			name = "socks5.request.desiredDestinationPortRedirect",
			syntax = "socks5.request.desiredDestinationPortRedirect=PORT",
			valueType = Port.class
	)
	public static final RuleResultSpec<Port> SOCKS5_REQUEST_DESIRED_DESTINATION_PORT_REDIRECT = RULE_RESULT_SPECS.addThenGet(new PortRuleResultSpec(
			"socks5.request.desiredDestinationPortRedirect"));

	@NameValuePairValueSpecDoc(
			description = "Specifies the logging action to take if the desired "
					+ "destination of the request is redirected",
			name = "socks5.request.desiredDestinationRedirectLogAction",
			syntax = "socks5.request.desiredDestinationRedirectLogAction=LOG_ACTION",
			valueType = LogAction.class
	)
	public static final RuleResultSpec<LogAction> SOCKS5_REQUEST_DESIRED_DESTINATION_REDIRECT_LOG_ACTION = RULE_RESULT_SPECS.addThenGet(new LogActionRuleResultSpec(
			"socks5.request.desiredDestinationRedirectLogAction"));

	public static List<RuleResultSpec<Object>> values() {
		return RULE_RESULT_SPECS.toList();
	}
	
	public static Map<String, RuleResultSpec<Object>> valuesMap() {
		return RULE_RESULT_SPECS.toMap();
	}
	
	private Socks5RuleResultSpecConstants() { }

}
