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
            description = "Specifies the binding host name or address for the "
                    + "remote-facing TCP server socket if the provided host " 
                    + "address is all zeros",
            name = "socks.onBindRequest.remoteFacingServerSocket.bindHost",
            syntax = "socks.onBindRequest.remoteFacingServerSocket.bindHost=HOST",
            valueType = Host.class
    )
    public static final RuleActionSpec<Host> SOCKS_ON_BIND_REQUEST_REMOTE_FACING_SERVER_SOCKET_BIND_HOST = RULE_ACTION_SPECS.addThenGet(new HostRuleActionSpec(
            "socks.onBindRequest.remoteFacingServerSocket.bindHost"));

    @NameValuePairValueSpecDoc(
            description = "Specifies an acceptable binding host address type "
                    + "for the remote-facing TCP server socket if the "
                    + "provided host address is all zeros (can be specified "
                    + "multiple times with each rule specifying another host "
                    + "address type)",
            name = "socks.onBindRequest.remoteFacingServerSocket.bindHostAddressType",
            syntax = "socks.onBindRequest.remoteFacingServerSocket.bindHostAddressType=HOST_ADDRESS_TYPE",
            valueType = HostAddressType.class
    )
    public static final RuleActionSpec<HostAddressType> SOCKS_ON_BIND_REQUEST_REMOTE_FACING_SERVER_SOCKET_BIND_HOST_ADDRESS_TYPE = RULE_ACTION_SPECS.addThenGet(new HostAddressTypeRuleActionSpec(
            "socks.onBindRequest.remoteFacingServerSocket.bindHostAddressType"));

    @NameValuePairValueSpecDoc(
            description = "Specifies a binding port range for the "
                    + "remote-facing TCP server socket if the provided port "
                    + "is zero (can be specified multiple times with each "
                    + "rule action specifying another port range)",
            name = "socks.onBindRequest.remoteFacingServerSocket.bindPortRange",
            syntax = "socks.onBindRequest.remoteFacingServerSocket.bindPortRange=PORT_RANGE",
            valueType = PortRange.class
    )
    public static final RuleActionSpec<PortRange> SOCKS_ON_BIND_REQUEST_REMOTE_FACING_SERVER_SOCKET_BIND_PORT_RANGE = RULE_ACTION_SPECS.addThenGet(new PortRangeRuleActionSpec(
            "socks.onBindRequest.remoteFacingServerSocket.bindPortRange"));

    @NameValuePairValueSpecDoc(
            description = "Specifies the network interface that provides a "
                    + "binding host address for the remote-facing TCP server "
                    + "socket if the provided host address is all zeros",
            name = "socks.onBindRequest.remoteFacingServerSocket.netInterface",
            syntax = "socks.onBindRequest.remoteFacingServerSocket.netInterface=NETWORK_INTERFACE",
            valueType = NetInterface.class
    )
    public static final RuleActionSpec<NetInterface> SOCKS_ON_BIND_REQUEST_REMOTE_FACING_SERVER_SOCKET_NET_INTERFACE = RULE_ACTION_SPECS.addThenGet(new NetInterfaceRuleActionSpec(
            "socks.onBindRequest.remoteFacingServerSocket.netInterface"));

    @NameValuePairValueSpecDoc(
            description = "Specifies a socket setting for the remote-facing " 
                    + "TCP server socket (can be specified multiple times "
                    + "with each rule action specifying another socket "
                    + "setting)",
            name = "socks.onBindRequest.remoteFacingServerSocket.socketSetting",
            syntax = "socks.onBindRequest.remoteFacingServerSocket.socketSetting=SOCKET_SETTING",
            valueType = SocketSetting.class
    )
    public static final RuleActionSpec<SocketSetting<Object>> SOCKS_ON_BIND_REQUEST_REMOTE_FACING_SERVER_SOCKET_SOCKET_SETTING = RULE_ACTION_SPECS.addThenGet(new SocketSettingRuleActionSpec(
            "socks.onBindRequest.remoteFacingServerSocket.socketSetting"));

    @NameValuePairValueSpecDoc(
            description = "Specifies a socket setting for the remote TCP "
                    + "socket (can be specified multiple times with each "
                    + "rule action specifying another socket setting)",
            name = "socks.onBindRequest.remoteSocket.socketSetting",
            syntax = "socks.onBindRequest.remoteSocket.socketSetting=SOCKET_SETTING",
            valueType = SocketSetting.class
    )
    public static final RuleActionSpec<SocketSetting<Object>> SOCKS_ON_BIND_REQUEST_REMOTE_SOCKET_SOCKET_SETTING = RULE_ACTION_SPECS.addThenGet(new SocketSettingRuleActionSpec(
            "socks.onBindRequest.remoteSocket.socketSetting"));

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
                    + "remote TCP socket",
            name = "socks.onConnectRequest.remoteSocket.bindHost",
            syntax = "socks.onConnectRequest.remoteSocket.bindHost=HOST",
            valueType = Host.class
    )
    public static final RuleActionSpec<Host> SOCKS_ON_CONNECT_REQUEST_REMOTE_SOCKET_BIND_HOST = RULE_ACTION_SPECS.addThenGet(new HostRuleActionSpec(
            "socks.onConnectRequest.remoteSocket.bindHost"));

    @NameValuePairValueSpecDoc(
            description = "Specifies an acceptable binding host address type "
                    + "for the remote TCP socket (can be specified multiple "
                    + "times with each rule action specifying another host "
                    + "address type)",
            name = "socks.onConnectRequest.remoteSocket.bindHostAddressType",
            syntax = "socks.onConnectRequest.remoteSocket.bindHostAddressType=HOST_ADDRESS_TYPE",
            valueType = HostAddressType.class
    )
    public static final RuleActionSpec<HostAddressType> SOCKS_ON_CONNECT_REQUEST_REMOTE_SOCKET_BIND_HOST_ADDRESS_TYPE = RULE_ACTION_SPECS.addThenGet(new HostAddressTypeRuleActionSpec(
            "socks.onConnectRequest.remoteSocket.bindHostAddressType"));

    @NameValuePairValueSpecDoc(
            description = "Specifies a binding port range for the remote TCP "
                    + "socket (can be specified multiple times with each "
                    + "rule action specifying another port range)",
            name = "socks.onConnectRequest.remoteSocket.bindPortRange",
            syntax = "socks.onConnectRequest.remoteSocket.bindPortRange=PORT_RANGE",
            valueType = PortRange.class
    )
    public static final RuleActionSpec<PortRange> SOCKS_ON_CONNECT_REQUEST_REMOTE_SOCKET_BIND_PORT_RANGE = RULE_ACTION_SPECS.addThenGet(new PortRangeRuleActionSpec(
            "socks.onConnectRequest.remoteSocket.bindPortRange"));

    @NameValuePairValueSpecDoc(
            description = "Specifies the timeout in milliseconds on waiting "
                    + "for the remote TCP socket to connect",
            name = "socks.onConnectRequest.remoteSocket.connectTimeout",
            syntax = "socks.onConnectRequest.remoteSocket.connectTimeout=POSITIVE_INTEGER",
            valueType = PositiveInteger.class
    )
    public static final RuleActionSpec<PositiveInteger> SOCKS_ON_CONNECT_REQUEST_REMOTE_SOCKET_CONNECT_TIMEOUT = RULE_ACTION_SPECS.addThenGet(new PositiveIntegerRuleActionSpec(
            "socks.onConnectRequest.remoteSocket.connectTimeout"));

    @NameValuePairValueSpecDoc(
            description = "Specifies the network interface that provides a "
                    + "binding host address for the remote TCP socket",
            name = "socks.onConnectRequest.remoteSocket.netInterface",
            syntax = "socks.onConnectRequest.remoteSocket.netInterface=NETWORK_INTERFACE",
            valueType = NetInterface.class
    )
    public static final RuleActionSpec<NetInterface> SOCKS_ON_CONNECT_REQUEST_REMOTE_SOCKET_NET_INTERFACE = RULE_ACTION_SPECS.addThenGet(new NetInterfaceRuleActionSpec(
            "socks.onConnectRequest.remoteSocket.netInterface"));

    @NameValuePairValueSpecDoc(
            description = "Specifies the boolean value to indicate if the "
                    + "remote TCP socket is to be prepared before connecting "
                    + "(involves applying the specified socket settings, "
                    + "resolving the target host name, and setting the "
                    + "specified timeout on waiting to connect)",
            name = "socks.onConnectRequest.remoteSocket.prepareSocket",
            syntax = "socks.onConnectRequest.remoteSocket.prepareSocket=true|false",
            valueType = Boolean.class
    )
    public static final RuleActionSpec<Boolean> SOCKS_ON_CONNECT_REQUEST_REMOTE_SOCKET_PREPARE_SOCKET = RULE_ACTION_SPECS.addThenGet(new BooleanRuleActionSpec(
            "socks.onConnectRequest.remoteSocket.prepareSocket"));

    @NameValuePairValueSpecDoc(
            description = "Specifies a socket setting for the remote TCP "
                    + "socket (can be specified multiple times with each "
                    + "rule action specifying another socket setting)",
            name = "socks.onConnectRequest.remoteSocket.socketSetting",
            syntax = "socks.onConnectRequest.remoteSocket.socketSetting=SOCKET_SETTING",
            valueType = SocketSetting.class
    )
    public static final RuleActionSpec<SocketSetting<Object>> SOCKS_ON_CONNECT_REQUEST_REMOTE_SOCKET_SOCKET_SETTING = RULE_ACTION_SPECS.addThenGet(new SocketSettingRuleActionSpec(
            "socks.onConnectRequest.remoteSocket.socketSetting"));

    @NameValuePairValueSpecDoc(
            description = "Specifies the binding host name or address for all "
                    + "client endpoints",
            name = "socks.onRequest.clientEndpoints.bindHost",
            syntax = "socks.onRequest.clientEndpoints.bindHost=HOST",
            valueType = Host.class
    )
    public static final RuleActionSpec<Host> SOCKS_ON_REQUEST_CLIENT_ENDPOINTS_BIND_HOST = RULE_ACTION_SPECS.addThenGet(new HostRuleActionSpec(
            "socks.onRequest.clientEndpoints.bindHost"));

    @NameValuePairValueSpecDoc(
            description = "Specifies an acceptable binding host address type "
                    + "for all client endpoints (can be specified "
                    + "multiple times with each rule action specifying "
                    + "another host address type)",
            name = "socks.onRequest.clientEndpoints.bindHostAddressType",
            syntax = "socks.onRequest.clientEndpoints.bindHostAddressType=HOST_ADDRESS_TYPE",
            valueType = HostAddressType.class
    )
    public static final RuleActionSpec<HostAddressType> SOCKS_ON_REQUEST_CLIENT_ENDPOINTS_BIND_HOST_ADDRESS_TYPE = RULE_ACTION_SPECS.addThenGet(new HostAddressTypeRuleActionSpec(
            "socks.onRequest.clientEndpoints.bindHostAddressType"));

    @NameValuePairValueSpecDoc(
            description = "Specifies a binding port range for all "
                    + "client UDP endpoints (can be specified multiple "
                    + "times with each rule action specifying another port "
                    + "range)",
            name = "socks.onRequest.clientEndpoints.bindUdpPortRange",
            syntax = "socks.onRequest.clientEndpoints.bindUdpPortRange=PORT_RANGE",
            valueType = PortRange.class
    )
    public static final RuleActionSpec<PortRange> SOCKS_ON_REQUEST_CLIENT_ENDPOINTS_BIND_UDP_PORT_RANGE = RULE_ACTION_SPECS.addThenGet(new PortRangeRuleActionSpec(
            "socks.onRequest.clientEndpoints.bindUdpPortRange"));

    @NameValuePairValueSpecDoc(
            description = "Specifies the network interface that provides a "
                    + "binding host address for all client endpoints",
            name = "socks.onRequest.clientEndpoints.netInterface",
            syntax = "socks.onRequest.clientEndpoints.netInterface=NETWORK_INTERFACE",
            valueType = NetInterface.class
    )
    public static final RuleActionSpec<NetInterface> SOCKS_ON_REQUEST_CLIENT_ENDPOINTS_NET_INTERFACE = RULE_ACTION_SPECS.addThenGet(new NetInterfaceRuleActionSpec(
            "socks.onRequest.clientEndpoints.netInterface"));

    @NameValuePairValueSpecDoc(
            description = "Specifies a socket setting for all client "
                    + "endpoints (can be specified multiple times with each "
                    + "rule action specifying another socket setting)",
            name = "socks.onRequest.clientEndpoints.socketSetting",
            syntax = "socks.onRequest.clientEndpoints.socketSetting=SOCKET_SETTING",
            valueType = SocketSetting.class
    )
    public static final RuleActionSpec<SocketSetting<Object>> SOCKS_ON_REQUEST_CLIENT_ENDPOINTS_SOCKET_SETTING = RULE_ACTION_SPECS.addThenGet(new SocketSettingRuleActionSpec(
            "socks.onRequest.clientEndpoints.socketSetting"));

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
            description = "Specifies the binding host name or address for all "
                    + "remote endpoints",
            name = "socks.onRequest.remoteEndpoints.bindHost",
            syntax = "socks.onRequest.remoteEndpoints.bindHost=HOST",
            valueType = Host.class
    )
    public static final RuleActionSpec<Host> SOCKS_ON_REQUEST_REMOTE_ENDPOINTS_BIND_HOST = RULE_ACTION_SPECS.addThenGet(new HostRuleActionSpec(
            "socks.onRequest.remoteEndpoints.bindHost"));

    @NameValuePairValueSpecDoc(
            description = "Specifies an acceptable binding host address type "
                    + "for all remote endpoints (can be specified "
                    + "multiple times with each rule action specifying "
                    + "another host address type)",
            name = "socks.onRequest.remoteEndpoints.bindHostAddressType",
            syntax = "socks.onRequest.remoteEndpoints.bindHostAddressType=HOST_ADDRESS_TYPE",
            valueType = HostAddressType.class
    )
    public static final RuleActionSpec<HostAddressType> SOCKS_ON_REQUEST_REMOTE_ENDPOINTS_BIND_HOST_ADDRESS_TYPE = RULE_ACTION_SPECS.addThenGet(new HostAddressTypeRuleActionSpec(
            "socks.onRequest.remoteEndpoints.bindHostAddressType"));

    @NameValuePairValueSpecDoc(
            description = "Specifies a binding port range for all "
                    + "remote TCP endpoints (can be specified multiple "
                    + "times with each rule action specifying another port "
                    + "range)",
            name = "socks.onRequest.remoteEndpoints.bindTcpPortRange",
            syntax = "socks.onRequest.remoteEndpoints.bindTcpPortRange=PORT_RANGE",
            valueType = PortRange.class
    )
    public static final RuleActionSpec<PortRange> SOCKS_ON_REQUEST_REMOTE_ENDPOINTS_BIND_TCP_PORT_RANGE = RULE_ACTION_SPECS.addThenGet(new PortRangeRuleActionSpec(
            "socks.onRequest.remoteEndpoints.bindTcpPortRange"));

    @NameValuePairValueSpecDoc(
            description = "Specifies a binding port range for all "
                    + "remote UDP endpoints (can be specified multiple "
                    + "times with each rule action specifying another port "
                    + "range)",
            name = "socks.onRequest.remoteEndpoints.bindUdpPortRange",
            syntax = "socks.onRequest.remoteEndpoints.bindUdpPortRange=PORT_RANGE",
            valueType = PortRange.class
    )
    public static final RuleActionSpec<PortRange> SOCKS_ON_REQUEST_REMOTE_ENDPOINTS_BIND_UDP_PORT_RANGE = RULE_ACTION_SPECS.addThenGet(new PortRangeRuleActionSpec(
            "socks.onRequest.remoteEndpoints.bindUdpPortRange"));

    @NameValuePairValueSpecDoc(
            description = "Specifies the network interface that provides a "
                    + "binding host address for all remote endpoints",
            name = "socks.onRequest.remoteEndpoints.netInterface",
            syntax = "socks.onRequest.remoteEndpoints.netInterface=NETWORK_INTERFACE",
            valueType = NetInterface.class
    )
    public static final RuleActionSpec<NetInterface> SOCKS_ON_REQUEST_REMOTE_ENDPOINTS_NET_INTERFACE = RULE_ACTION_SPECS.addThenGet(new NetInterfaceRuleActionSpec(
            "socks.onRequest.remoteEndpoints.netInterface"));

    @NameValuePairValueSpecDoc(
            description = "Specifies a socket setting for all remote "
                    + "endpoints (can be specified multiple times with each "
                    + "rule action specifying another socket setting)",
            name = "socks.onRequest.remoteEndpoints.socketSetting",
            syntax = "socks.onRequest.remoteEndpoints.socketSetting=SOCKET_SETTING",
            valueType = SocketSetting.class
    )
    public static final RuleActionSpec<SocketSetting<Object>> SOCKS_ON_REQUEST_REMOTE_ENDPOINTS_SOCKET_SETTING = RULE_ACTION_SPECS.addThenGet(new SocketSettingRuleActionSpec(
            "socks.onRequest.remoteEndpoints.socketSetting"));

    @NameValuePairValueSpecDoc(
            description = "Specifies the binding host name or address for the "
                    + "client-facing UDP socket",
            name = "socks.onUdpAssociateRequest.clientFacingDatagramSocket.bindHost",
            syntax = "socks.onUdpAssociateRequest.clientFacingDatagramSocket.bindHost=HOST",
            valueType = Host.class
    )
    public static final RuleActionSpec<Host> SOCKS_ON_UDP_ASSOCIATE_REQUEST_CLIENT_FACING_DATAGRAM_SOCKET_BIND_HOST = RULE_ACTION_SPECS.addThenGet(new HostRuleActionSpec(
            "socks.onUdpAssociateRequest.clientFacingDatagramSocket.bindHost"));

    @NameValuePairValueSpecDoc(
            description = "Specifies an acceptable binding host address type "
                    + "for the client-facing UDP socket (can be specified "
                    + "multiple times with each rule action specifying "
                    + "another host address type)",
            name = "socks.onUdpAssociateRequest.clientFacingDatagramSocket.bindHostAddressType",
            syntax = "socks.onUdpAssociateRequest.clientFacingDatagramSocket.bindHostAddressType=HOST_ADDRESS_TYPE",
            valueType = HostAddressType.class
    )
    public static final RuleActionSpec<HostAddressType> SOCKS_ON_UDP_ASSOCIATE_REQUEST_CLIENT_FACING_DATAGRAM_SOCKET_BIND_HOST_ADDRESS_TYPE = RULE_ACTION_SPECS.addThenGet(new HostAddressTypeRuleActionSpec(
            "socks.onUdpAssociateRequest.clientFacingDatagramSocket.bindHostAddressType"));

    @NameValuePairValueSpecDoc(
            description = "Specifies a binding port range for the client-facing "
                    + "UDP socket (can be specified multiple times with each "
                    + "rule action specifying another port range)",
            name = "socks.onUdpAssociateRequest.clientFacingDatagramSocket.bindPortRange",
            syntax = "socks.onUdpAssociateRequest.clientFacingDatagramSocket.bindPortRange=PORT_RANGE",
            valueType = PortRange.class
    )
    public static final RuleActionSpec<PortRange> SOCKS_ON_UDP_ASSOCIATE_REQUEST_CLIENT_FACING_DATAGRAM_SOCKET_BIND_PORT_RANGE = RULE_ACTION_SPECS.addThenGet(new PortRangeRuleActionSpec(
            "socks.onUdpAssociateRequest.clientFacingDatagramSocket.bindPortRange"));

    @NameValuePairValueSpecDoc(
            description = "Specifies the network interface that provides a "
                    + "binding host address for the client-facing UDP socket",
            name = "socks.onUdpAssociateRequest.clientFacingDatagramSocket.netInterface",
            syntax = "socks.onUdpAssociateRequest.clientFacingDatagramSocket.netInterface=NETWORK_INTERFACE",
            valueType = NetInterface.class
    )
    public static final RuleActionSpec<NetInterface> SOCKS_ON_UDP_ASSOCIATE_REQUEST_CLIENT_FACING_DATAGRAM_SOCKET_NET_INTERFACE = RULE_ACTION_SPECS.addThenGet(new NetInterfaceRuleActionSpec(
            "socks.onUdpAssociateRequest.clientFacingDatagramSocket.netInterface"));

    @NameValuePairValueSpecDoc(
            description = "Specifies a socket setting for the client-facing UDP "
                    + "socket (can be specified multiple times with each rule "
                    + "action specifying another socket setting)",
            name = "socks.onUdpAssociateRequest.clientFacingDatagramSocket.socketSetting",
            syntax = "socks.onUdpAssociateRequest.clientFacingDatagramSocket.socketSetting=SOCKET_SETTING",
            valueType = SocketSetting.class
    )
    public static final RuleActionSpec<SocketSetting<Object>> SOCKS_ON_UDP_ASSOCIATE_REQUEST_CLIENT_FACING_DATAGRAM_SOCKET_SOCKET_SETTING = RULE_ACTION_SPECS.addThenGet(new SocketSettingRuleActionSpec(
            "socks.onUdpAssociateRequest.clientFacingDatagramSocket.socketSetting"));

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
            description = "Specifies the binding host name or address for the "
                    + "remote-facing UDP socket",
            name = "socks.onUdpAssociateRequest.remoteFacingDatagramSocket.bindHost",
            syntax = "socks.onUdpAssociateRequest.remoteFacingDatagramSocket.bindHost=HOST",
            valueType = Host.class
    )
    public static final RuleActionSpec<Host> SOCKS_ON_UDP_ASSOCIATE_REQUEST_REMOTE_FACING_DATAGRAM_SOCKET_BIND_HOST = RULE_ACTION_SPECS.addThenGet(new HostRuleActionSpec(
            "socks.onUdpAssociateRequest.remoteFacingDatagramSocket.bindHost"));

    @NameValuePairValueSpecDoc(
            description = "Specifies an acceptable binding host address type "
                    + "for the remote-facing UDP socket (can be specified "
                    + "multiple times with each rule action specifying "
                    + "another host address type)",
            name = "socks.onUdpAssociateRequest.remoteFacingDatagramSocket.bindHostAddressType",
            syntax = "socks.onUdpAssociateRequest.remoteFacingDatagramSocket.bindHostAddressType=HOST_ADDRESS_TYPE",
            valueType = HostAddressType.class
    )
    public static final RuleActionSpec<HostAddressType> SOCKS_ON_UDP_ASSOCIATE_REQUEST_REMOTE_FACING_DATAGRAM_SOCKET_BIND_HOST_ADDRESS_TYPE = RULE_ACTION_SPECS.addThenGet(new HostAddressTypeRuleActionSpec(
            "socks.onUdpAssociateRequest.remoteFacingDatagramSocket.bindHostAddressType"));

    @NameValuePairValueSpecDoc(
            description = "Specifies a binding port range for the remote-facing "
                    + "UDP socket (can be specified multiple times with each "
                    + "rule action specifying another port range)",
            name = "socks.onUdpAssociateRequest.remoteFacingDatagramSocket.bindPortRange",
            syntax = "socks.onUdpAssociateRequest.remoteFacingDatagramSocket.bindPortRange=PORT_RANGE",
            valueType = PortRange.class
    )
    public static final RuleActionSpec<PortRange> SOCKS_ON_UDP_ASSOCIATE_REQUEST_REMOTE_FACING_DATAGRAM_SOCKET_BIND_PORT_RANGE = RULE_ACTION_SPECS.addThenGet(new PortRangeRuleActionSpec(
            "socks.onUdpAssociateRequest.remoteFacingDatagramSocket.bindPortRange"));

    @NameValuePairValueSpecDoc(
            description = "Specifies the network interface that provides a "
                    + "binding host address for the remote-facing UDP socket",
            name = "socks.onUdpAssociateRequest.remoteFacingDatagramSocket.netInterface",
            syntax = "socks.onUdpAssociateRequest.remoteFacingDatagramSocket.netInterface=NETWORK_INTERFACE",
            valueType = NetInterface.class
    )
    public static final RuleActionSpec<NetInterface> SOCKS_ON_UDP_ASSOCIATE_REQUEST_REMOTE_FACING_DATAGRAM_SOCKET_NET_INTERFACE = RULE_ACTION_SPECS.addThenGet(new NetInterfaceRuleActionSpec(
            "socks.onUdpAssociateRequest.remoteFacingDatagramSocket.netInterface"));

    @NameValuePairValueSpecDoc(
            description = "Specifies a socket setting for the remote-facing UDP "
                    + "socket (can be specified multiple times with each rule "
                    + "action specifying another socket setting)",
            name = "socks.onUdpAssociateRequest.remoteFacingDatagramSocket.socketSetting",
            syntax = "socks.onUdpAssociateRequest.remoteFacingDatagramSocket.socketSetting=SOCKET_SETTING",
            valueType = SocketSetting.class
    )
    public static final RuleActionSpec<SocketSetting<Object>> SOCKS_ON_UDP_ASSOCIATE_REQUEST_REMOTE_FACING_DATAGRAM_SOCKET_SOCKET_SETTING = RULE_ACTION_SPECS.addThenGet(new SocketSettingRuleActionSpec(
            "socks.onUdpAssociateRequest.remoteFacingDatagramSocket.socketSetting"));

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
