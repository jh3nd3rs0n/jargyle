package com.github.jh3nd3rs0n.jargyle.server;

import com.github.jh3nd3rs0n.jargyle.common.net.*;
import com.github.jh3nd3rs0n.jargyle.common.number.PositiveInteger;
import com.github.jh3nd3rs0n.jargyle.internal.annotation.NameValuePairValueSpecDoc;
import com.github.jh3nd3rs0n.jargyle.internal.annotation.NameValuePairValueSpecsDoc;
import com.github.jh3nd3rs0n.jargyle.server.internal.ruleactionspec.impl.*;

import java.util.List;
import java.util.Map;

@NameValuePairValueSpecsDoc(
        description = "",
        name = "SOCKS Rule Actions"
)
public final class SocksRuleActionSpecConstants {

    private static final RuleActionSpecs RULE_ACTION_SPECS = new RuleActionSpecs();

    @NameValuePairValueSpecDoc(
            description = "Specifies a socket setting for the inbound socket "
                    + "(can be specified multiple times with each rule action "
                    + "specifying another socket setting)",
            name = "socks.onBindRequest.inboundSocketSetting",
            syntax = "socks.onBindRequest.inboundSocketSetting=SOCKET_SETTING",
            valueType = SocketSetting.class
    )
    public static final RuleActionSpec<SocketSetting<Object>> SOCKS_ON_BIND_REQUEST_INBOUND_SOCKET_SETTING = RULE_ACTION_SPECS.addThenGet(new SocketSettingRuleActionSpec(
            "socks.onBindRequest.inboundSocketSetting"));

    @NameValuePairValueSpecDoc(
            description = "Specifies the binding host name or address for the "
                    + "listen socket if the provided host address is all zeros",
            name = "socks.onBindRequest.listenBindHost",
            syntax = "socks.onBindRequest.listenBindHost=HOST",
            valueType = Host.class
    )
    public static final RuleActionSpec<Host> SOCKS_ON_BIND_REQUEST_LISTEN_BIND_HOST = RULE_ACTION_SPECS.addThenGet(new HostRuleActionSpec(
            "socks.onBindRequest.listenBindHost"));

    @NameValuePairValueSpecDoc(
            description = "Specifies an acceptable binding host address type "
                    + "for the listen socket if the provided host address is "
                    + "all zeros (can be specified multiple times with each "
                    + "rule specifying another host address type)",
            name = "socks.onBindRequest.listenBindHostAddressType",
            syntax = "socks.onBindRequest.listenBindHostAddressType=HOST_ADDRESS_TYPE",
            valueType = HostAddressType.class
    )
    public static final RuleActionSpec<HostAddressType> SOCKS_ON_BIND_REQUEST_LISTEN_BIND_HOST_ADDRESS_TYPE = RULE_ACTION_SPECS.addThenGet(new HostAddressTypeRuleActionSpec(
            "socks.onBindRequest.listenBindHostAddressType"));

    @NameValuePairValueSpecDoc(
            description = "Specifies a binding port range for the listen socket "
                    + "if the provided port is zero (can be specified multiple "
                    + "times with each rule action specifying another port "
                    + "range)",
            name = "socks.onBindRequest.listenBindPortRange",
            syntax = "socks.onBindRequest.listenBindPortRange=PORT_RANGE",
            valueType = PortRange.class
    )
    public static final RuleActionSpec<PortRange> SOCKS_ON_BIND_REQUEST_LISTEN_BIND_PORT_RANGE = RULE_ACTION_SPECS.addThenGet(new PortRangeRuleActionSpec(
            "socks.onBindRequest.listenBindPortRange"));

    @NameValuePairValueSpecDoc(
            description = "Specifies the network interface that provides a "
                    + "binding host address for the listen socket if the "
                    + "provided host address is all zeros",
            name = "socks.onBindRequest.listenNetInterface",
            syntax = "socks.onBindRequest.listenNetInterface=NETWORK_INTERFACE",
            valueType = NetInterface.class
    )
    public static final RuleActionSpec<NetInterface> SOCKS_ON_BIND_REQUEST_LISTEN_NET_INTERFACE = RULE_ACTION_SPECS.addThenGet(new NetInterfaceRuleActionSpec(
            "socks.onBindRequest.listenNetInterface"));

    @NameValuePairValueSpecDoc(
            description = "Specifies a socket setting for the listen socket "
                    + "(can be specified multiple times with each rule action "
                    + "specifying another socket setting)",
            name = "socks.onBindRequest.listenSocketSetting",
            syntax = "socks.onBindRequest.listenSocketSetting=SOCKET_SETTING",
            valueType = SocketSetting.class
    )
    public static final RuleActionSpec<SocketSetting<Object>> SOCKS_ON_BIND_REQUEST_LISTEN_SOCKET_SETTING = RULE_ACTION_SPECS.addThenGet(new SocketSettingRuleActionSpec(
            "socks.onBindRequest.listenSocketSetting"));

    @NameValuePairValueSpecDoc(
            description = "Specifies the buffer size in bytes for relaying the "
                    + "data",
            name = "socks.onBindRequest.relayBufferSize",
            syntax = "socks.onBindRequest.relayBufferSize=POSITIVE_INTEGER",
            valueType = PositiveInteger.class
    )
    public static final RuleActionSpec<PositiveInteger> SOCKS_ON_BIND_REQUEST_RELAY_BUFFER_SIZE = RULE_ACTION_SPECS.addThenGet(new PositiveIntegerRuleActionSpec(
            "socks.onBindRequest.relayBufferSize"));

    @NameValuePairValueSpecDoc(
            description = "Specifies the timeout in milliseconds on relaying "
                    + "no data",
            name = "socks.onBindRequest.relayIdleTimeout",
            syntax = "socks.onBindRequest.relayIdleTimeout=POSITIVE_INTEGER",
            valueType = PositiveInteger.class
    )
    public static final RuleActionSpec<PositiveInteger> SOCKS_ON_BIND_REQUEST_RELAY_IDLE_TIMEOUT = RULE_ACTION_SPECS.addThenGet(new PositiveIntegerRuleActionSpec(
            "socks.onBindRequest.relayIdleTimeout"));

    @NameValuePairValueSpecDoc(
            description = "Specifies the upper limit on bandwidth in bytes per "
                    + "second of receiving inbound data to be relayed",
            name = "socks.onBindRequest.relayInboundBandwidthLimit",
            syntax = "socks.onBindRequest.relayInboundBandwidthLimit=POSITIVE_INTEGER",
            valueType = PositiveInteger.class
    )
    public static final RuleActionSpec<PositiveInteger> SOCKS_ON_BIND_REQUEST_RELAY_INBOUND_BANDWIDTH_LIMIT = RULE_ACTION_SPECS.addThenGet(new PositiveIntegerRuleActionSpec(
            "socks.onBindRequest.relayInboundBandwidthLimit"));

    @NameValuePairValueSpecDoc(
            description = "Specifies the upper limit on bandwidth in bytes per "
                    + "second of receiving outbound data to be relayed",
            name = "socks.onBindRequest.relayOutboundBandwidthLimit",
            syntax = "socks.onBindRequest.relayOutboundBandwidthLimit=POSITIVE_INTEGER",
            valueType = PositiveInteger.class
    )
    public static final RuleActionSpec<PositiveInteger> SOCKS_ON_BIND_REQUEST_RELAY_OUTBOUND_BANDWIDTH_LIMIT = RULE_ACTION_SPECS.addThenGet(new PositiveIntegerRuleActionSpec(
            "socks.onBindRequest.relayOutboundBandwidthLimit"));

    @NameValuePairValueSpecDoc(
            description = "Specifies the boolean value to indicate if the "
                    + "target-facing socket is to be prepared before "
                    + "connecting (involves applying the specified socket "
                    + "settings, resolving the target host name, and setting "
                    + "the specified timeout on waiting to connect)",
            name = "socks.onConnectRequest.prepareTargetFacingSocket",
            syntax = "socks.onConnectRequest.prepareTargetFacingSocket=true|false",
            valueType = Boolean.class
    )
    public static final RuleActionSpec<Boolean> SOCKS_ON_CONNECT_REQUEST_PREPARE_TARGET_FACING_SOCKET = RULE_ACTION_SPECS.addThenGet(new BooleanRuleActionSpec(
            "socks.onConnectRequest.prepareTargetFacingSocket"));

    @NameValuePairValueSpecDoc(
            description = "Specifies the buffer size in bytes for relaying the "
                    + "data",
            name = "socks.onConnectRequest.relayBufferSize",
            syntax = "socks.onConnectRequest.relayBufferSize=POSITIVE_INTEGER",
            valueType = PositiveInteger.class
    )
    public static final RuleActionSpec<PositiveInteger> SOCKS_ON_CONNECT_REQUEST_RELAY_BUFFER_SIZE = RULE_ACTION_SPECS.addThenGet(new PositiveIntegerRuleActionSpec(
            "socks.onConnectRequest.relayBufferSize"));

    @NameValuePairValueSpecDoc(
            description = "Specifies the timeout in milliseconds on relaying "
                    + "no data",
            name = "socks.onConnectRequest.relayIdleTimeout",
            syntax = "socks.onConnectRequest.relayIdleTimeout=POSITIVE_INTEGER",
            valueType = PositiveInteger.class
    )
    public static final RuleActionSpec<PositiveInteger> SOCKS_ON_CONNECT_REQUEST_RELAY_IDLE_TIMEOUT = RULE_ACTION_SPECS.addThenGet(new PositiveIntegerRuleActionSpec(
            "socks.onConnectRequest.relayIdleTimeout"));

    @NameValuePairValueSpecDoc(
            description = "Specifies the upper limit on bandwidth in bytes per "
                    + "second of receiving inbound data to be relayed",
            name = "socks.onConnectRequest.relayInboundBandwidthLimit",
            syntax = "socks.onConnectRequest.relayInboundBandwidthLimit=POSITIVE_INTEGER",
            valueType = PositiveInteger.class
    )
    public static final RuleActionSpec<PositiveInteger> SOCKS_ON_CONNECT_REQUEST_RELAY_INBOUND_BANDWIDTH_LIMIT = RULE_ACTION_SPECS.addThenGet(new PositiveIntegerRuleActionSpec(
            "socks.onConnectRequest.relayInboundBandwidthLimit"));

    @NameValuePairValueSpecDoc(
            description = "Specifies the upper limit on bandwidth in bytes per "
                    + "second of receiving outbound data to be relayed",
            name = "socks.onConnectRequest.relayOutboundBandwidthLimit",
            syntax = "socks.onConnectRequest.relayOutboundBandwidthLimit=POSITIVE_INTEGER",
            valueType = PositiveInteger.class
    )
    public static final RuleActionSpec<PositiveInteger> SOCKS_ON_CONNECT_REQUEST_RELAY_OUTBOUND_BANDWIDTH_LIMIT = RULE_ACTION_SPECS.addThenGet(new PositiveIntegerRuleActionSpec(
            "socks.onConnectRequest.relayOutboundBandwidthLimit"));

    @NameValuePairValueSpecDoc(
            description = "Specifies the binding host name or address for the "
                    + "target-facing socket",
            name = "socks.onConnectRequest.targetFacingBindHost",
            syntax = "socks.onConnectRequest.targetFacingBindHost=HOST",
            valueType = Host.class
    )
    public static final RuleActionSpec<Host> SOCKS_ON_CONNECT_REQUEST_TARGET_FACING_BIND_HOST = RULE_ACTION_SPECS.addThenGet(new HostRuleActionSpec(
            "socks.onConnectRequest.targetFacingBindHost"));

    @NameValuePairValueSpecDoc(
            description = "Specifies an acceptable binding host address type "
                    + "for the target-facing socket (can be specified "
                    + "multiple times with each rule action specifying "
                    + "another host address type)",
            name = "socks.onConnectRequest.targetFacingBindHostAddressType",
            syntax = "socks.onConnectRequest.targetFacingBindHostAddressType=HOST_ADDRESS_TYPE",
            valueType = HostAddressType.class
    )
    public static final RuleActionSpec<HostAddressType> SOCKS_ON_CONNECT_REQUEST_TARGET_FACING_BIND_HOST_ADDRESS_TYPE = RULE_ACTION_SPECS.addThenGet(new HostAddressTypeRuleActionSpec(
            "socks.onConnectRequest.targetFacingBindHostAddressType"));

    @NameValuePairValueSpecDoc(
            description = "Specifies a binding port range for the target-facing "
                    + "socket (can be specified multiple times with each rule "
                    + "action specifying another port range)",
            name = "socks.onConnectRequest.targetFacingBindPortRange",
            syntax = "socks.onConnectRequest.targetFacingBindPortRange=PORT_RANGE",
            valueType = PortRange.class
    )
    public static final RuleActionSpec<PortRange> SOCKS_ON_CONNECT_REQUEST_TARGET_FACING_BIND_PORT_RANGE = RULE_ACTION_SPECS.addThenGet(new PortRangeRuleActionSpec(
            "socks.onConnectRequest.targetFacingBindPortRange"));

    @NameValuePairValueSpecDoc(
            description = "Specifies the timeout in milliseconds on waiting for "
                    + "the target-facing socket to connect",
            name = "socks.onConnectRequest.targetFacingConnectTimeout",
            syntax = "socks.onConnectRequest.targetFacingConnectTimeout=POSITIVE_INTEGER",
            valueType = PositiveInteger.class
    )
    public static final RuleActionSpec<PositiveInteger> SOCKS_ON_CONNECT_REQUEST_TARGET_FACING_CONNECT_TIMEOUT = RULE_ACTION_SPECS.addThenGet(new PositiveIntegerRuleActionSpec(
            "socks.onConnectRequest.targetFacingConnectTimeout"));

    @NameValuePairValueSpecDoc(
            description = "Specifies the network interface that provides a "
                    + "binding host address for the target-facing socket",
            name = "socks.onConnectRequest.targetFacingNetInterface",
            syntax = "socks.onConnectRequest.targetFacingNetInterface=NETWORK_INTERFACE",
            valueType = NetInterface.class
    )
    public static final RuleActionSpec<NetInterface> SOCKS_ON_CONNECT_REQUEST_TARGET_FACING_NET_INTERFACE = RULE_ACTION_SPECS.addThenGet(new NetInterfaceRuleActionSpec(
            "socks.onConnectRequest.targetFacingNetInterface"));

    @NameValuePairValueSpecDoc(
            description = "Specifies a socket setting for the target-facing "
                    + "socket (can be specified multiple times with each rule "
                    + "action specifying another socket setting)",
            name = "socks.onConnectRequest.targetFacingSocketSetting",
            syntax = "socks.onConnectRequest.targetFacingSocketSetting=SOCKET_SETTING",
            valueType = SocketSetting.class
    )
    public static final RuleActionSpec<SocketSetting<Object>> SOCKS_ON_CONNECT_REQUEST_TARGET_FACING_SOCKET_SETTING = RULE_ACTION_SPECS.addThenGet(new SocketSettingRuleActionSpec(
            "socks.onConnectRequest.targetFacingSocketSetting"));

    @NameValuePairValueSpecDoc(
            description = "Specifies the binding host name or address for all "
                    + "external-facing sockets",
            name = "socks.onRequest.externalFacingBindHost",
            syntax = "socks.onRequest.externalFacingBindHost=HOST",
            valueType = Host.class
    )
    public static final RuleActionSpec<Host> SOCKS_ON_REQUEST_EXTERNAL_FACING_BIND_HOST = RULE_ACTION_SPECS.addThenGet(new HostRuleActionSpec(
            "socks.onRequest.externalFacingBindHost"));

    @NameValuePairValueSpecDoc(
            description = "Specifies an acceptable binding host address type "
                    + "for all external-facing sockets (can be specified "
                    + "multiple times with each rule action specifying "
                    + "another host address type)",
            name = "socks.onRequest.externalFacingBindHostAddressType",
            syntax = "socks.onRequest.externalFacingBindHostAddressType=HOST_ADDRESS_TYPE",
            valueType = HostAddressType.class
    )
    public static final RuleActionSpec<HostAddressType> SOCKS_ON_REQUEST_EXTERNAL_FACING_BIND_HOST_ADDRESS_TYPE = RULE_ACTION_SPECS.addThenGet(new HostAddressTypeRuleActionSpec(
            "socks.onRequest.externalFacingBindHostAddressType"));

    @NameValuePairValueSpecDoc(
            description = "Specifies a binding port range for all "
                    + "external-facing TCP sockets (can be specified multiple "
                    + "times with each rule action specifying another port "
                    + "range)",
            name = "socks.onRequest.externalFacingBindTcpPortRange",
            syntax = "socks.onRequest.externalFacingBindTcpPortRange=PORT_RANGE",
            valueType = PortRange.class
    )
    public static final RuleActionSpec<PortRange> SOCKS_ON_REQUEST_EXTERNAL_FACING_BIND_TCP_PORT_RANGE = RULE_ACTION_SPECS.addThenGet(new PortRangeRuleActionSpec(
            "socks.onRequest.externalFacingBindTcpPortRange"));

    @NameValuePairValueSpecDoc(
            description = "Specifies a binding port range for all "
                    + "external-facing UDP sockets (can be specified multiple "
                    + "times with each rule action specifying another port "
                    + "range)",
            name = "socks.onRequest.externalFacingBindUdpPortRange",
            syntax = "socks.onRequest.externalFacingBindUdpPortRange=PORT_RANGE",
            valueType = PortRange.class
    )
    public static final RuleActionSpec<PortRange> SOCKS_ON_REQUEST_EXTERNAL_FACING_BIND_UDP_PORT_RANGE = RULE_ACTION_SPECS.addThenGet(new PortRangeRuleActionSpec(
            "socks.onRequest.externalFacingBindUdpPortRange"));

    @NameValuePairValueSpecDoc(
            description = "Specifies the network interface that provides a "
                    + "binding host address for all external-facing sockets",
            name = "socks.onRequest.externalFacingNetInterface",
            syntax = "socks.onRequest.externalFacingNetInterface=NETWORK_INTERFACE",
            valueType = NetInterface.class
    )
    public static final RuleActionSpec<NetInterface> SOCKS_ON_REQUEST_EXTERNAL_FACING_NET_INTERFACE = RULE_ACTION_SPECS.addThenGet(new NetInterfaceRuleActionSpec(
            "socks.onRequest.externalFacingNetInterface"));

    @NameValuePairValueSpecDoc(
            description = "Specifies a socket setting for all external-facing "
                    + "sockets (can be specified multiple times with each rule "
                    + "action specifying another socket setting)",
            name = "socks.onRequest.externalFacingSocketSetting",
            syntax = "socks.onRequest.externalFacingSocketSetting=SOCKET_SETTING",
            valueType = SocketSetting.class
    )
    public static final RuleActionSpec<SocketSetting<Object>> SOCKS_ON_REQUEST_EXTERNAL_FACING_SOCKET_SETTING = RULE_ACTION_SPECS.addThenGet(new SocketSettingRuleActionSpec(
            "socks.onRequest.externalFacingSocketSetting"));

    @NameValuePairValueSpecDoc(
            description = "Specifies the binding host name or address for all "
                    + "internal-facing sockets",
            name = "socks.onRequest.internalFacingBindHost",
            syntax = "socks.onRequest.internalFacingBindHost=HOST",
            valueType = Host.class
    )
    public static final RuleActionSpec<Host> SOCKS_ON_REQUEST_INTERNAL_FACING_BIND_HOST = RULE_ACTION_SPECS.addThenGet(new HostRuleActionSpec(
            "socks.onRequest.internalFacingBindHost"));

    @NameValuePairValueSpecDoc(
            description = "Specifies an acceptable binding host address type "
                    + "for all internal-facing sockets (can be specified "
                    + "multiple times with each rule action specifying "
                    + "another host address type)",
            name = "socks.onRequest.internalFacingBindHostAddressType",
            syntax = "socks.onRequest.internalFacingBindHostAddressType=HOST_ADDRESS_TYPE",
            valueType = HostAddressType.class
    )
    public static final RuleActionSpec<HostAddressType> SOCKS_ON_REQUEST_INTERNAL_FACING_BIND_HOST_ADDRESS_TYPE = RULE_ACTION_SPECS.addThenGet(new HostAddressTypeRuleActionSpec(
            "socks.onRequest.internalFacingBindHostAddressType"));

    @NameValuePairValueSpecDoc(
            description = "Specifies a binding port range for all "
                    + "internal-facing UDP sockets (can be specified multiple "
                    + "times with each rule action specifying another port "
                    + "range)",
            name = "socks.onRequest.internalFacingBindUdpPortRange",
            syntax = "socks.onRequest.internalFacingBindUdpPortRange=PORT_RANGE",
            valueType = PortRange.class
    )
    public static final RuleActionSpec<PortRange> SOCKS_ON_REQUEST_INTERNAL_FACING_BIND_UDP_PORT_RANGE = RULE_ACTION_SPECS.addThenGet(new PortRangeRuleActionSpec(
            "socks.onRequest.internalFacingBindUdpPortRange"));

    @NameValuePairValueSpecDoc(
            description = "Specifies the network interface that provides a "
                    + "binding host address for all internal-facing sockets",
            name = "socks.onRequest.internalFacingNetInterface",
            syntax = "socks.onRequest.internalFacingNetInterface=NETWORK_INTERFACE",
            valueType = NetInterface.class
    )
    public static final RuleActionSpec<NetInterface> SOCKS_ON_REQUEST_INTERNAL_FACING_NET_INTERFACE = RULE_ACTION_SPECS.addThenGet(new NetInterfaceRuleActionSpec(
            "socks.onRequest.internalFacingNetInterface"));

    @NameValuePairValueSpecDoc(
            description = "Specifies a socket setting for all internal-facing "
                    + "sockets (can be specified multiple times with each rule "
                    + "action specifying another socket setting)",
            name = "socks.onRequest.internalFacingSocketSetting",
            syntax = "socks.onRequest.internalFacingSocketSetting=SOCKET_SETTING",
            valueType = SocketSetting.class
    )
    public static final RuleActionSpec<SocketSetting<Object>> SOCKS_ON_REQUEST_INTERNAL_FACING_SOCKET_SETTING = RULE_ACTION_SPECS.addThenGet(new SocketSettingRuleActionSpec(
            "socks.onRequest.internalFacingSocketSetting"));

    @NameValuePairValueSpecDoc(
            description = "Specifies the buffer size in bytes for relaying the "
                    + "data",
            name = "socks.onRequest.relayBufferSize",
            syntax = "socks.onRequest.relayBufferSize=POSITIVE_INTEGER",
            valueType = PositiveInteger.class
    )
    public static final RuleActionSpec<PositiveInteger> SOCKS_ON_REQUEST_RELAY_BUFFER_SIZE = RULE_ACTION_SPECS.addThenGet(new PositiveIntegerRuleActionSpec(
            "socks.onRequest.relayBufferSize"));

    @NameValuePairValueSpecDoc(
            description = "Specifies the timeout in milliseconds on relaying "
                    + "no data",
            name = "socks.onRequest.relayIdleTimeout",
            syntax = "socks.onRequest.relayIdleTimeout=POSITIVE_INTEGER",
            valueType = PositiveInteger.class
    )
    public static final RuleActionSpec<PositiveInteger> SOCKS_ON_REQUEST_RELAY_IDLE_TIMEOUT = RULE_ACTION_SPECS.addThenGet(new PositiveIntegerRuleActionSpec(
            "socks.onRequest.relayIdleTimeout"));

    @NameValuePairValueSpecDoc(
            description = "Specifies the upper limit on bandwidth in bytes per "
                    + "second of receiving inbound data to be relayed",
            name = "socks.onRequest.relayInboundBandwidthLimit",
            syntax = "socks.onRequest.relayInboundBandwidthLimit=POSITIVE_INTEGER",
            valueType = PositiveInteger.class
    )
    public static final RuleActionSpec<PositiveInteger> SOCKS_ON_REQUEST_RELAY_INBOUND_BANDWIDTH_LIMIT = RULE_ACTION_SPECS.addThenGet(new PositiveIntegerRuleActionSpec(
            "socks.onRequest.relayInboundBandwidthLimit"));

    @NameValuePairValueSpecDoc(
            description = "Specifies the upper limit on bandwidth in bytes per "
                    + "second of receiving outbound data to be relayed",
            name = "socks.onRequest.relayOutboundBandwidthLimit",
            syntax = "socks.onRequest.relayOutboundBandwidthLimit=POSITIVE_INTEGER",
            valueType = PositiveInteger.class
    )
    public static final RuleActionSpec<PositiveInteger> SOCKS_ON_REQUEST_RELAY_OUTBOUND_BANDWIDTH_LIMIT = RULE_ACTION_SPECS.addThenGet(new PositiveIntegerRuleActionSpec(
            "socks.onRequest.relayOutboundBandwidthLimit"));

    @NameValuePairValueSpecDoc(
            description = "Specifies the binding host name or address for the "
                    + "client-facing UDP socket",
            name = "socks.onUdpAssociateRequest.clientFacingBindHost",
            syntax = "socks.onUdpAssociateRequest.clientFacingBindHost=HOST",
            valueType = Host.class
    )
    public static final RuleActionSpec<Host> SOCKS_ON_UDP_ASSOCIATE_REQUEST_CLIENT_FACING_BIND_HOST = RULE_ACTION_SPECS.addThenGet(new HostRuleActionSpec(
            "socks.onUdpAssociateRequest.clientFacingBindHost"));

    @NameValuePairValueSpecDoc(
            description = "Specifies an acceptable binding host address type "
                    + "for the client-facing UDP socket (can be specified "
                    + "multiple times with each rule action specifying "
                    + "another host address type)",
            name = "socks.onUdpAssociateRequest.clientFacingBindHostAddressType",
            syntax = "socks.onUdpAssociateRequest.clientFacingBindHostAddressType=HOST_ADDRESS_TYPE",
            valueType = HostAddressType.class
    )
    public static final RuleActionSpec<HostAddressType> SOCKS_ON_UDP_ASSOCIATE_REQUEST_CLIENT_FACING_BIND_HOST_ADDRESS_TYPE = RULE_ACTION_SPECS.addThenGet(new HostAddressTypeRuleActionSpec(
            "socks.onUdpAssociateRequest.clientFacingBindHostAddressType"));

    @NameValuePairValueSpecDoc(
            description = "Specifies a binding port range for the client-facing "
                    + "UDP socket (can be specified multiple times with each "
                    + "rule action specifying another port range)",
            name = "socks.onUdpAssociateRequest.clientFacingBindPortRange",
            syntax = "socks.onUdpAssociateRequest.clientFacingBindPortRange=PORT_RANGE",
            valueType = PortRange.class
    )
    public static final RuleActionSpec<PortRange> SOCKS_ON_UDP_ASSOCIATE_REQUEST_CLIENT_FACING_BIND_PORT_RANGE = RULE_ACTION_SPECS.addThenGet(new PortRangeRuleActionSpec(
            "socks.onUdpAssociateRequest.clientFacingBindPortRange"));

    @NameValuePairValueSpecDoc(
            description = "Specifies the network interface that provides a "
                    + "binding host address for the client-facing UDP socket",
            name = "socks.onUdpAssociateRequest.clientFacingNetInterface",
            syntax = "socks.onUdpAssociateRequest.clientFacingNetInterface=NETWORK_INTERFACE",
            valueType = NetInterface.class
    )
    public static final RuleActionSpec<NetInterface> SOCKS_ON_UDP_ASSOCIATE_REQUEST_CLIENT_FACING_NET_INTERFACE = RULE_ACTION_SPECS.addThenGet(new NetInterfaceRuleActionSpec(
            "socks.onUdpAssociateRequest.clientFacingNetInterface"));

    @NameValuePairValueSpecDoc(
            description = "Specifies a socket setting for the client-facing UDP "
                    + "socket (can be specified multiple times with each rule "
                    + "action specifying another socket setting)",
            name = "socks.onUdpAssociateRequest.clientFacingSocketSetting",
            syntax = "socks.onUdpAssociateRequest.clientFacingSocketSetting=SOCKET_SETTING",
            valueType = SocketSetting.class
    )
    public static final RuleActionSpec<SocketSetting<Object>> SOCKS_ON_UDP_ASSOCIATE_REQUEST_CLIENT_FACING_SOCKET_SETTING = RULE_ACTION_SPECS.addThenGet(new SocketSettingRuleActionSpec(
            "socks.onUdpAssociateRequest.clientFacingSocketSetting"));

    @NameValuePairValueSpecDoc(
            description = "Specifies the binding host name or address for the "
                    + "peer-facing UDP socket",
            name = "socks.onUdpAssociateRequest.peerFacingBindHost",
            syntax = "socks.onUdpAssociateRequest.peerFacingBindHost=HOST",
            valueType = Host.class
    )
    public static final RuleActionSpec<Host> SOCKS_ON_UDP_ASSOCIATE_REQUEST_PEER_FACING_BIND_HOST = RULE_ACTION_SPECS.addThenGet(new HostRuleActionSpec(
            "socks.onUdpAssociateRequest.peerFacingBindHost"));

    @NameValuePairValueSpecDoc(
            description = "Specifies an acceptable binding host address type "
                    + "for the peer-facing UDP socket (can be specified "
                    + "multiple times with each rule action specifying "
                    + "another host address type)",
            name = "socks.onUdpAssociateRequest.peerFacingBindHostAddressType",
            syntax = "socks.onUdpAssociateRequest.peerFacingBindHostAddressType=HOST_ADDRESS_TYPE",
            valueType = HostAddressType.class
    )
    public static final RuleActionSpec<HostAddressType> SOCKS_ON_UDP_ASSOCIATE_REQUEST_PEER_FACING_BIND_HOST_ADDRESS_TYPE = RULE_ACTION_SPECS.addThenGet(new HostAddressTypeRuleActionSpec(
            "socks.onUdpAssociateRequest.peerFacingBindHostAddressType"));

    @NameValuePairValueSpecDoc(
            description = "Specifies a binding port range for the peer-facing "
                    + "UDP socket (can be specified multiple times with each "
                    + "rule action specifying another port range)",
            name = "socks.onUdpAssociateRequest.peerFacingBindPortRange",
            syntax = "socks.onUdpAssociateRequest.peerFacingBindPortRange=PORT_RANGE",
            valueType = PortRange.class
    )
    public static final RuleActionSpec<PortRange> SOCKS_ON_UDP_ASSOCIATE_REQUEST_PEER_FACING_BIND_PORT_RANGE = RULE_ACTION_SPECS.addThenGet(new PortRangeRuleActionSpec(
            "socks.onUdpAssociateRequest.peerFacingBindPortRange"));

    @NameValuePairValueSpecDoc(
            description = "Specifies the network interface that provides a "
                    + "binding host address for the peer-facing UDP socket",
            name = "socks.onUdpAssociateRequest.peerFacingNetInterface",
            syntax = "socks.onUdpAssociateRequest.peerFacingNetInterface=NETWORK_INTERFACE",
            valueType = NetInterface.class
    )
    public static final RuleActionSpec<NetInterface> SOCKS_ON_UDP_ASSOCIATE_REQUEST_PEER_FACING_NET_INTERFACE = RULE_ACTION_SPECS.addThenGet(new NetInterfaceRuleActionSpec(
            "socks.onUdpAssociateRequest.peerFacingNetInterface"));

    @NameValuePairValueSpecDoc(
            description = "Specifies a socket setting for the peer-facing UDP "
                    + "socket (can be specified multiple times with each rule "
                    + "action specifying another socket setting)",
            name = "socks.onUdpAssociateRequest.peerFacingSocketSetting",
            syntax = "socks.onUdpAssociateRequest.peerFacingSocketSetting=SOCKET_SETTING",
            valueType = SocketSetting.class
    )
    public static final RuleActionSpec<SocketSetting<Object>> SOCKS_ON_UDP_ASSOCIATE_REQUEST_PEER_FACING_SOCKET_SETTING = RULE_ACTION_SPECS.addThenGet(new SocketSettingRuleActionSpec(
            "socks.onUdpAssociateRequest.peerFacingSocketSetting"));

    @NameValuePairValueSpecDoc(
            description = "Specifies the buffer size in bytes for relaying the "
                    + "data",
            name = "socks.onUdpAssociateRequest.relayBufferSize",
            syntax = "socks.onUdpAssociateRequest.relayBufferSize=POSITIVE_INTEGER",
            valueType = PositiveInteger.class
    )
    public static final RuleActionSpec<PositiveInteger> SOCKS_ON_UDP_ASSOCIATE_REQUEST_RELAY_BUFFER_SIZE = RULE_ACTION_SPECS.addThenGet(new PositiveIntegerRuleActionSpec(
            "socks.onUdpAssociateRequest.relayBufferSize"));

    @NameValuePairValueSpecDoc(
            description = "Specifies the timeout in milliseconds on relaying no "
                    + "data",
            name = "socks.onUdpAssociateRequest.relayIdleTimeout",
            syntax = "socks.onUdpAssociateRequest.relayIdleTimeout=POSITIVE_INTEGER",
            valueType = PositiveInteger.class
    )
    public static final RuleActionSpec<PositiveInteger> SOCKS_ON_UDP_ASSOCIATE_REQUEST_RELAY_IDLE_TIMEOUT = RULE_ACTION_SPECS.addThenGet(new PositiveIntegerRuleActionSpec(
            "socks.onUdpAssociateRequest.relayIdleTimeout"));

    @NameValuePairValueSpecDoc(
            description = "Specifies the upper limit on bandwidth in bytes per "
                    + "second of receiving inbound data to be relayed",
            name = "socks.onUdpAssociateRequest.relayInboundBandwidthLimit",
            syntax = "socks.onUdpAssociateRequest.relayInboundBandwidthLimit=POSITIVE_INTEGER",
            valueType = PositiveInteger.class
    )
    public static final RuleActionSpec<PositiveInteger> SOCKS_ON_UDP_ASSOCIATE_REQUEST_RELAY_INBOUND_BANDWIDTH_LIMIT = RULE_ACTION_SPECS.addThenGet(new PositiveIntegerRuleActionSpec(
            "socks.onUdpAssociateRequest.relayInboundBandwidthLimit"));

    @NameValuePairValueSpecDoc(
            description = "Specifies the upper limit on bandwidth in bytes per "
                    + "second of receiving outbound data to be relayed",
            name = "socks.onUdpAssociateRequest.relayOutboundBandwidthLimit",
            syntax = "socks.onUdpAssociateRequest.relayOutboundBandwidthLimit=POSITIVE_INTEGER",
            valueType = PositiveInteger.class
    )
    public static final RuleActionSpec<PositiveInteger> SOCKS_ON_UDP_ASSOCIATE_REQUEST_RELAY_OUTBOUND_BANDWIDTH_LIMIT = RULE_ACTION_SPECS.addThenGet(new PositiveIntegerRuleActionSpec(
            "socks.onUdpAssociateRequest.relayOutboundBandwidthLimit"));

    @NameValuePairValueSpecDoc(
            description = "Specifies the desired destination address "
                    + "redirect for the request.",
            name = "socks.request.desiredDestinationAddressRedirect",
            syntax = "socks.request.desiredDestinationAddressRedirect=ADDRESS",
            valueType = String.class
    )
    public static final RuleActionSpec<String> SOCKS_REQUEST_DESIRED_DESTINATION_ADDRESS_REDIRECT = RULE_ACTION_SPECS.addThenGet(new StringRuleActionSpec(
            "socks.request.desiredDestinationAddressRedirect"));

    @NameValuePairValueSpecDoc(
            description = "Specifies the desired destination port redirect "
                    + "for the request",
            name = "socks.request.desiredDestinationPortRedirect",
            syntax = "socks.request.desiredDestinationPortRedirect=PORT",
            valueType = Port.class
    )
    public static final RuleActionSpec<Port> SOCKS_REQUEST_DESIRED_DESTINATION_PORT_REDIRECT = RULE_ACTION_SPECS.addThenGet(new PortRuleActionSpec(
            "socks.request.desiredDestinationPortRedirect"));

    @NameValuePairValueSpecDoc(
            description = "Specifies the logging action to take if the desired "
                    + "destination of the request is redirected",
            name = "socks.request.desiredDestinationRedirectLogAction",
            syntax = "socks.request.desiredDestinationRedirectLogAction=LOG_ACTION",
            valueType = LogAction.class
    )
    public static final RuleActionSpec<LogAction> SOCKS_REQUEST_DESIRED_DESTINATION_REDIRECT_LOG_ACTION = RULE_ACTION_SPECS.addThenGet(new LogActionRuleActionSpec(
            "socks.request.desiredDestinationRedirectLogAction"));

    public static List<RuleActionSpec<Object>> values() {
        return RULE_ACTION_SPECS.toList();
    }

    public static Map<String, RuleActionSpec<Object>> valuesMap() {
        return RULE_ACTION_SPECS.toMap();
    }
    
    private SocksRuleActionSpecConstants() { }
    
}
