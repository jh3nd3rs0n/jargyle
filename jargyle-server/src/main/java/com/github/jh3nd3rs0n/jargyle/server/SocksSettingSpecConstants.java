package com.github.jh3nd3rs0n.jargyle.server;

import com.github.jh3nd3rs0n.jargyle.common.net.*;
import com.github.jh3nd3rs0n.jargyle.common.number.PositiveInteger;
import com.github.jh3nd3rs0n.jargyle.common.string.CommaSeparatedValues;
import com.github.jh3nd3rs0n.jargyle.internal.annotation.NameValuePairValueSpecDoc;
import com.github.jh3nd3rs0n.jargyle.internal.annotation.NameValuePairValueSpecsDoc;
import com.github.jh3nd3rs0n.jargyle.server.internal.settingspec.impl.*;

import java.util.List;
import java.util.Map;

@NameValuePairValueSpecsDoc(
        description = "",
        name = "SOCKS Settings"
)
public final class SocksSettingSpecConstants {

    private static final SettingSpecs SETTING_SPECS = new SettingSpecs();

    @NameValuePairValueSpecDoc(
            defaultValue = "false",
            description = "The boolean value to indicate if the exchange of "
                    + "the GSS-API protection level negotiation must be "
                    + "unprotected according to the NEC reference "
                    + "implementation",
            name = "socks.gssapiauthmethod.necReferenceImpl",
            syntax = "socks.gssapiauthmethod.necReferenceImpl=true|false",
            valueType = Boolean.class
    )
    public static final SettingSpec<Boolean> SOCKS_GSSAPIAUTHMETHOD_NEC_REFERENCE_IMPL =
            SETTING_SPECS.addThenGet(new BooleanSettingSpec(
                    "socks.gssapiauthmethod.necReferenceImpl",
                    Boolean.FALSE));

    @NameValuePairValueSpecDoc(
            defaultValue = "REQUIRED_INTEG_AND_CONF,REQUIRED_INTEG,NONE",
            description = "The comma separated list of acceptable protection "
                    + "levels after GSS-API authentication (The first is "
                    + "preferred if the client does not provide a protection "
                    + "level that is acceptable.)",
            name = "socks.gssapiauthmethod.protectionLevels",
            syntax = "socks.gssapiauthmethod.protectionLevels=SOCKS_GSSAPIAUTHMETHOD_PROTECTION_LEVELS",
            valueType = CommaSeparatedValues.class
    )
    public static final SettingSpec<CommaSeparatedValues> SOCKS_GSSAPIAUTHMETHOD_PROTECTION_LEVELS =
            SETTING_SPECS.addThenGet(new CommaSeparatedValuesSettingSpec(
                    "socks.gssapiauthmethod.protectionLevels",
                    CommaSeparatedValues.of("REQUIRED_INTEG_AND_CONF","REQUIRED_INTEG","NONE")));

    @NameValuePairValueSpecDoc(
            defaultValue = "true",
            description = "The suggested privacy (i.e. confidentiality) state "
                    + "for GSS-API messages sent after GSS-API authentication "
                    + "(applicable if the negotiated protection level is "
                    + "SELECTIVE_INTEG_OR_CONF)",
            name = "socks.gssapiauthmethod.suggestedConf",
            syntax = "socks.gssapiauthmethod.suggestedConf=true|false",
            valueType = Boolean.class
    )
    public static final SettingSpec<Boolean> SOCKS_GSSAPIAUTHMETHOD_SUGGESTED_CONF =
            SETTING_SPECS.addThenGet(new BooleanSettingSpec(
                    "socks.gssapiauthmethod.suggestedConf",
                    Boolean.TRUE));

    @NameValuePairValueSpecDoc(
            defaultValue = "0",
            description = "The suggested quality-of-protection (i.e. "
                    + "integrity) value for GSS-API messages sent after "
                    + "GSS-API authentication (applicable if the negotiated "
                    + "protection level is SELECTIVE_INTEG_OR_CONF)",
            name = "socks.gssapiauthmethod.suggestedInteg",
            syntax = "socks.gssapiauthmethod.suggestedInteg=-2147483648-2147483647",
            valueType = Integer.class
    )
    public static final SettingSpec<Integer> SOCKS_GSSAPIAUTHMETHOD_SUGGESTED_INTEG =
            SETTING_SPECS.addThenGet(new IntegerSettingSpec(
                    "socks.gssapiauthmethod.suggestedInteg",
                    0));

    @NameValuePairValueSpecDoc(
            defaultValue = "NO_AUTHENTICATION_REQUIRED",
            description = "The comma separated list of acceptable "
                    + "authentication methods in order of preference",
            name = "socks.methods",
            syntax = "socks.methods=SOCKS_METHODS",
            valueType = CommaSeparatedValues.class
    )
    public static final SettingSpec<CommaSeparatedValues> SOCKS_METHODS =
            SETTING_SPECS.addThenGet(new CommaSeparatedValuesSettingSpec(
                    "socks.methods",
                    CommaSeparatedValues.of("NO_AUTHENTICATION_REQUIRED")));

    @NameValuePairValueSpecDoc(
            description = "The buffer size in bytes for relaying the data",
            name = "socks.onBindRequest.relayBufferSize",
            syntax = "socks.onBindRequest.relayBufferSize=POSITIVE_INTEGER",
            valueType = PositiveInteger.class
    )
    public static final SettingSpec<PositiveInteger> SOCKS_ON_BIND_REQUEST_RELAY_BUFFER_SIZE =
            SETTING_SPECS.addThenGet(new PositiveIntegerSettingSpec(
                    "socks.onBindRequest.relayBufferSize",
                    null));

    @NameValuePairValueSpecDoc(
            description = "The timeout in milliseconds on relaying no data",
            name = "socks.onBindRequest.relayIdleTimeout",
            syntax = "socks.onBindRequest.relayIdleTimeout=POSITIVE_INTEGER",
            valueType = PositiveInteger.class
    )
    public static final SettingSpec<PositiveInteger> SOCKS_ON_BIND_REQUEST_RELAY_IDLE_TIMEOUT =
            SETTING_SPECS.addThenGet(new PositiveIntegerSettingSpec(
                    "socks.onBindRequest.relayIdleTimeout",
                    null));

    @NameValuePairValueSpecDoc(
            description = "The upper limit on bandwidth in bytes per second of "
                    + "receiving inbound data to be relayed",
            name = "socks.onBindRequest.relayInboundBandwidthLimit",
            syntax = "socks.onBindRequest.relayInboundBandwidthLimit=POSITIVE_INTEGER",
            valueType = PositiveInteger.class
    )
    public static final SettingSpec<PositiveInteger> SOCKS_ON_BIND_REQUEST_RELAY_INBOUND_BANDWIDTH_LIMIT =
            SETTING_SPECS.addThenGet(new PositiveIntegerSettingSpec(
                    "socks.onBindRequest.relayInboundBandwidthLimit",
                    null));

    @NameValuePairValueSpecDoc(
            description = "The upper limit on bandwidth in bytes per second of "
                    + "receiving outbound data to be relayed",
            name = "socks.onBindRequest.relayOutboundBandwidthLimit",
            syntax = "socks.onBindRequest.relayOutboundBandwidthLimit=POSITIVE_INTEGER",
            valueType = PositiveInteger.class
    )
    public static final SettingSpec<PositiveInteger> SOCKS_ON_BIND_REQUEST_RELAY_OUTBOUND_BANDWIDTH_LIMIT =
            SETTING_SPECS.addThenGet(new PositiveIntegerSettingSpec(
                    "socks.onBindRequest.relayOutboundBandwidthLimit",
                    null));

    @NameValuePairValueSpecDoc(
            description = "The binding host name or address for the "
                    + "remote-facing TCP server socket if the provided host "
                    + "address is all zeros",
            name = "socks.onBindRequest.remoteFacingServerSocket.bindHost",
            syntax = "socks.onBindRequest.remoteFacingServerSocket.bindHost=HOST",
            valueType = Host.class
    )
    public static final SettingSpec<Host> SOCKS_ON_BIND_REQUEST_REMOTE_FACING_SERVER_SOCKET_BIND_HOST =
            SETTING_SPECS.addThenGet(new HostSettingSpec(
                    "socks.onBindRequest.remoteFacingServerSocket.bindHost",
                    null));

    @NameValuePairValueSpecDoc(
            description = "The comma separated list of acceptable binding " 
                    + "host address types for the remote-facing TCP server "
                    + "socket if the provided host address is all zeros",
            name = "socks.onBindRequest.remoteFacingServerSocket.bindHostAddressTypes",
            syntax = "socks.onBindRequest.remoteFacingServerSocket.bindHostAddressTypes=HOST_ADDRESS_TYPES",
            valueType = HostAddressTypes.class
    )
    public static final SettingSpec<HostAddressTypes> SOCKS_ON_BIND_REQUEST_REMOTE_FACING_SERVER_SOCKET_BIND_HOST_ADDRESS_TYPES =
            SETTING_SPECS.addThenGet(new HostAddressTypesSettingSpec(
                    "socks.onBindRequest.remoteFacingServerSocket.bindHostAddressTypes",
                    HostAddressTypes.of()));

    @NameValuePairValueSpecDoc(
            description = "The comma separated list of binding port ranges "
                    + "for the remote-facing TCP server socket if the "
                    + "provided port is zero",
            name = "socks.onBindRequest.remoteFacingServerSocket.bindPortRanges",
            syntax = "socks.onBindRequest.remoteFacingServerSocket.bindPortRanges=PORT_RANGES",
            valueType = PortRanges.class
    )
    public static final SettingSpec<PortRanges> SOCKS_ON_BIND_REQUEST_REMOTE_FACING_SERVER_SOCKET_BIND_PORT_RANGES =
            SETTING_SPECS.addThenGet(new PortRangesSettingSpec(
                    "socks.onBindRequest.remoteFacingServerSocket.bindPortRanges",
                    PortRanges.of()));

    @NameValuePairValueSpecDoc(
            description = "The network interface that provides a binding "
                    + "host address for the remote-facing TCP server socket "
                    + "if the provided host address is all zeros",
            name = "socks.onBindRequest.remoteFacingServerSocket.netInterface",
            syntax = "socks.onBindRequest.remoteFacingServerSocket.netInterface=NETWORK_INTERFACE",
            valueType = NetInterface.class
    )
    public static final SettingSpec<NetInterface> SOCKS_ON_BIND_REQUEST_REMOTE_FACING_SERVER_SOCKET_NET_INTERFACE =
            SETTING_SPECS.addThenGet(new NetInterfaceSettingSpec(
                    "socks.onBindRequest.remoteFacingServerSocket.netInterface",
                    null));

    @NameValuePairValueSpecDoc(
            description = "The comma separated list of socket settings for "
                    + "the remote-facing TCP server socket",
            name = "socks.onBindRequest.remoteFacingServerSocket.socketSettings",
            syntax = "socks.onBindRequest.remoteFacingServerSocket.socketSettings=SOCKET_SETTINGS",
            valueType = SocketSettings.class
    )
    public static final SettingSpec<SocketSettings> SOCKS_ON_BIND_REQUEST_REMOTE_FACING_SERVER_SOCKET_SOCKET_SETTINGS =
            SETTING_SPECS.addThenGet(new SocketSettingsSettingSpec(
                    "socks.onBindRequest.remoteFacingServerSocket.socketSettings",
                    SocketSettings.of()));

    @NameValuePairValueSpecDoc(
            description = "The comma separated list of socket settings for the "
                    + "remote TCP socket",
            name = "socks.onBindRequest.remoteSocket.socketSettings",
            syntax = "socks.onBindRequest.remoteSocket.socketSettings=SOCKET_SETTINGS",
            valueType = SocketSettings.class
    )
    public static final SettingSpec<SocketSettings> SOCKS_ON_BIND_REQUEST_REMOTE_SOCKET_SOCKET_SETTINGS =
            SETTING_SPECS.addThenGet(new SocketSettingsSettingSpec(
                    "socks.onBindRequest.remoteSocket.socketSettings",
                    SocketSettings.of()));

    @NameValuePairValueSpecDoc(
            description = "The buffer size in bytes for relaying the data",
            name = "socks.onConnectRequest.relayBufferSize",
            syntax = "socks.onConnectRequest.relayBufferSize=POSITIVE_INTEGER",
            valueType = PositiveInteger.class
    )
    public static final SettingSpec<PositiveInteger> SOCKS_ON_CONNECT_REQUEST_RELAY_BUFFER_SIZE =
            SETTING_SPECS.addThenGet(new PositiveIntegerSettingSpec(
                    "socks.onConnectRequest.relayBufferSize",
                    null));

    @NameValuePairValueSpecDoc(
            description = "The timeout in milliseconds on relaying no data",
            name = "socks.onConnectRequest.relayIdleTimeout",
            syntax = "socks.onConnectRequest.relayIdleTimeout=POSITIVE_INTEGER",
            valueType = PositiveInteger.class
    )
    public static final SettingSpec<PositiveInteger> SOCKS_ON_CONNECT_REQUEST_RELAY_IDLE_TIMEOUT =
            SETTING_SPECS.addThenGet(new PositiveIntegerSettingSpec(
                    "socks.onConnectRequest.relayIdleTimeout",
                    null));

    @NameValuePairValueSpecDoc(
            description = "The upper limit on bandwidth in bytes per second of "
                    + "receiving inbound data to be relayed",
            name = "socks.onConnectRequest.relayInboundBandwidthLimit",
            syntax = "socks.onConnectRequest.relayInboundBandwidthLimit=POSITIVE_INTEGER",
            valueType = PositiveInteger.class
    )
    public static final SettingSpec<PositiveInteger> SOCKS_ON_CONNECT_REQUEST_RELAY_INBOUND_BANDWIDTH_LIMIT =
            SETTING_SPECS.addThenGet(new PositiveIntegerSettingSpec(
                    "socks.onConnectRequest.relayInboundBandwidthLimit",
                    null));

    @NameValuePairValueSpecDoc(
            description = "The upper limit on bandwidth in bytes per second of "
                    + "receiving outbound data to be relayed",
            name = "socks.onConnectRequest.relayOutboundBandwidthLimit",
            syntax = "socks.onConnectRequest.relayOutboundBandwidthLimit=POSITIVE_INTEGER",
            valueType = PositiveInteger.class
    )
    public static final SettingSpec<PositiveInteger> SOCKS_ON_CONNECT_REQUEST_RELAY_OUTBOUND_BANDWIDTH_LIMIT =
            SETTING_SPECS.addThenGet(new PositiveIntegerSettingSpec(
                    "socks.onConnectRequest.relayOutboundBandwidthLimit",
                    null));

    @NameValuePairValueSpecDoc(
            description = "The binding host name or address for the remote "
                    + "TCP socket",
            name = "socks.onConnectRequest.remoteSocket.bindHost",
            syntax = "socks.onConnectRequest.remoteSocket.bindHost=HOST",
            valueType = Host.class
    )
    public static final SettingSpec<Host> SOCKS_ON_CONNECT_REQUEST_REMOTE_SOCKET_BIND_HOST =
            SETTING_SPECS.addThenGet(new HostSettingSpec(
                    "socks.onConnectRequest.remoteSocket.bindHost",
                    null));

    @NameValuePairValueSpecDoc(
            description = "The comma separated list of acceptable binding "
                    + "host address types for the remote TCP socket",
            name = "socks.onConnectRequest.remoteSocket.bindHostAddressTypes",
            syntax = "socks.onConnectRequest.remoteSocket.bindHostAddressTypes=HOST_ADDRESS_TYPES",
            valueType = HostAddressTypes.class
    )
    public static final SettingSpec<HostAddressTypes> SOCKS_ON_CONNECT_REQUEST_REMOTE_SOCKET_BIND_HOST_ADDRESS_TYPES =
            SETTING_SPECS.addThenGet(new HostAddressTypesSettingSpec(
                    "socks.onConnectRequest.remoteSocket.bindHostAddressTypes",
                    HostAddressTypes.of()));

    @NameValuePairValueSpecDoc(
            description = "The comma separated list of binding port ranges "
                    + "for the remote TCP socket",
            name = "socks.onConnectRequest.remoteSocket.bindPortRanges",
            syntax = "socks.onConnectRequest.remoteSocket.bindPortRanges=PORT_RANGES",
            valueType = PortRanges.class
    )
    public static final SettingSpec<PortRanges> SOCKS_ON_CONNECT_REQUEST_REMOTE_SOCKET_BIND_PORT_RANGES =
            SETTING_SPECS.addThenGet(new PortRangesSettingSpec(
                    "socks.onConnectRequest.remoteSocket.bindPortRanges",
                    PortRanges.of()));

    @NameValuePairValueSpecDoc(
            defaultValue = "60000",
            description = "The timeout in milliseconds on waiting for the "
                    + "remote TCP socket to connect",
            name = "socks.onConnectRequest.remoteSocket.connectTimeout",
            syntax = "socks.onConnectRequest.remoteSocket.connectTimeout=POSITIVE_INTEGER",
            valueType = PositiveInteger.class
    )
    public static final SettingSpec<PositiveInteger> SOCKS_ON_CONNECT_REQUEST_REMOTE_SOCKET_CONNECT_TIMEOUT =
            SETTING_SPECS.addThenGet(new PositiveIntegerSettingSpec(
                    "socks.onConnectRequest.remoteSocket.connectTimeout",
                    PositiveInteger.valueOf(60000)));

    @NameValuePairValueSpecDoc(
            description = "The network interface that provides a binding "
                    + "host address for the remote TCP socket",
            name = "socks.onConnectRequest.remoteSocket.netInterface",
            syntax = "socks.onConnectRequest.remoteSocket.netInterface=NETWORK_INTERFACE",
            valueType = NetInterface.class
    )
    public static final SettingSpec<NetInterface> SOCKS_ON_CONNECT_REQUEST_REMOTE_SOCKET_NET_INTERFACE =
            SETTING_SPECS.addThenGet(new NetInterfaceSettingSpec(
                    "socks.onConnectRequest.remoteSocket.netInterface",
                    null));

    @NameValuePairValueSpecDoc(
            defaultValue = "false",
            description = "The boolean value to indicate if the remote TCP "
                    + "socket is to be prepared before connecting (involves "
                    + "applying the specified socket settings, resolving the "
                    + "target host name, and setting the specified timeout "
                    + "on waiting to connect)",
            name = "socks.onConnectRequest.remoteSocket.prepareSocket",
            syntax = "socks.onConnectRequest.remoteSocket.prepareSocket=true|false",
            valueType = Boolean.class
    )
    public static final SettingSpec<Boolean> SOCKS_ON_CONNECT_REQUEST_REMOTE_SOCKET_PREPARE_SOCKET =
            SETTING_SPECS.addThenGet(new BooleanSettingSpec(
                    "socks.onConnectRequest.remoteSocket.prepareSocket",
                    Boolean.FALSE));

    @NameValuePairValueSpecDoc(
            description = "The comma separated list of socket settings for "
                    + "the remote TCP socket",
            name = "socks.onConnectRequest.remoteSocket.socketSettings",
            syntax = "socks.onConnectRequest.remoteSocket.socketSettings=SOCKET_SETTINGS",
            valueType = SocketSettings.class
    )
    public static final SettingSpec<SocketSettings> SOCKS_ON_CONNECT_REQUEST_REMOTE_SOCKET_SOCKET_SETTINGS =
            SETTING_SPECS.addThenGet(new SocketSettingsSettingSpec(
                    "socks.onConnectRequest.remoteSocket.socketSettings",
                    SocketSettings.of()));

    @NameValuePairValueSpecDoc(
            description = "The binding host name or address for all "
                    + "client endpoints",
            name = "socks.onRequest.clientEndpoints.bindHost",
            syntax = "socks.onRequest.clientEndpoints.bindHost=HOST",
            valueType = Host.class
    )
    public static final SettingSpec<Host> SOCKS_ON_REQUEST_CLIENT_ENDPOINTS_BIND_HOST =
            SETTING_SPECS.addThenGet(new HostSettingSpec(
                    "socks.onRequest.clientEndpoints.bindHost",
                    null));

    @NameValuePairValueSpecDoc(
            description = "The comma separated list of acceptable binding "
                    + "host address types for all client endpoints",
            name = "socks.onRequest.clientEndpoints.bindHostAddressTypes",
            syntax = "socks.onRequest.clientEndpoints.bindHostAddressTypes=HOST_ADDRESS_TYPES",
            valueType = HostAddressTypes.class
    )
    public static final SettingSpec<HostAddressTypes> SOCKS_ON_REQUEST_CLIENT_ENDPOINTS_BIND_HOST_ADDRESS_TYPES =
            SETTING_SPECS.addThenGet(new HostAddressTypesSettingSpec(
                    "socks.onRequest.clientEndpoints.bindHostAddressTypes",
                    HostAddressTypes.of()));

    @NameValuePairValueSpecDoc(
            description = "The comma separated list of binding port ranges for "
                    + "all client UDP endpoints",
            name = "socks.onRequest.clientEndpoints.bindUdpPortRanges",
            syntax = "socks.onRequest.clientEndpoints.bindUdpPortRanges=PORT_RANGES",
            valueType = PortRanges.class
    )
    public static final SettingSpec<PortRanges> SOCKS_ON_REQUEST_CLIENT_ENDPOINTS_BIND_UDP_PORT_RANGES =
            SETTING_SPECS.addThenGet(new PortRangesSettingSpec(
                    "socks.onRequest.clientEndpoints.bindUdpPortRanges",
                    PortRanges.of()));

    @NameValuePairValueSpecDoc(
            description = "The network interface that provides a binding host "
                    + "address for all client endpoints",
            name = "socks.onRequest.clientEndpoints.netInterface",
            syntax = "socks.onRequest.clientEndpoints.netInterface=NETWORK_INTERFACE",
            valueType = NetInterface.class
    )
    public static final SettingSpec<NetInterface> SOCKS_ON_REQUEST_CLIENT_ENDPOINTS_NET_INTERFACE =
            SETTING_SPECS.addThenGet(new NetInterfaceSettingSpec(
                    "socks.onRequest.clientEndpoints.netInterface",
                    null));

    @NameValuePairValueSpecDoc(
            description = "The comma separated list of socket settings for all "
                    + "client endpoints",
            name = "socks.onRequest.clientEndpoints.socketSettings",
            syntax = "socks.onRequest.clientEndpoints.socketSettings=SOCKET_SETTINGS",
            valueType = SocketSettings.class
    )
    public static final SettingSpec<SocketSettings> SOCKS_ON_REQUEST_CLIENT_ENDPOINTS_SOCKET_SETTINGS =
            SETTING_SPECS.addThenGet(new SocketSettingsSettingSpec(
                    "socks.onRequest.clientEndpoints.socketSettings",
                    SocketSettings.of()));

    @NameValuePairValueSpecDoc(
            defaultValue = "1024",
            description = "The buffer size in bytes for relaying the data",
            name = "socks.onRequest.relayBufferSize",
            syntax = "socks.onRequest.relayBufferSize=POSITIVE_INTEGER",
            valueType = PositiveInteger.class
    )
    public static final SettingSpec<PositiveInteger> SOCKS_ON_REQUEST_RELAY_BUFFER_SIZE =
            SETTING_SPECS.addThenGet(new PositiveIntegerSettingSpec(
                    "socks.onRequest.relayBufferSize",
                    PositiveInteger.valueOf(1024)));

    @NameValuePairValueSpecDoc(
            defaultValue = "60000",
            description = "The timeout in milliseconds on relaying no data",
            name = "socks.onRequest.relayIdleTimeout",
            syntax = "socks.onRequest.relayIdleTimeout=POSITIVE_INTEGER",
            valueType = PositiveInteger.class
    )
    public static final SettingSpec<PositiveInteger> SOCKS_ON_REQUEST_RELAY_IDLE_TIMEOUT =
            SETTING_SPECS.addThenGet(new PositiveIntegerSettingSpec(
                    "socks.onRequest.relayIdleTimeout",
                    PositiveInteger.valueOf(60000)));

    @NameValuePairValueSpecDoc(
            description = "The upper limit on bandwidth in bytes per second of "
                    + "receiving inbound data to be relayed",
            name = "socks.onRequest.relayInboundBandwidthLimit",
            syntax = "socks.onRequest.relayInboundBandwidthLimit=POSITIVE_INTEGER",
            valueType = PositiveInteger.class
    )
    public static final SettingSpec<PositiveInteger> SOCKS_ON_REQUEST_RELAY_INBOUND_BANDWIDTH_LIMIT =
            SETTING_SPECS.addThenGet(new PositiveIntegerSettingSpec(
                    "socks.onRequest.relayInboundBandwidthLimit",
                    null));

    @NameValuePairValueSpecDoc(
            description = "The upper limit on bandwidth in bytes per second of "
                    + "receiving outbound data to be relayed",
            name = "socks.onRequest.relayOutboundBandwidthLimit",
            syntax = "socks.onRequest.relayOutboundBandwidthLimit=POSITIVE_INTEGER",
            valueType = PositiveInteger.class
    )
    public static final SettingSpec<PositiveInteger> SOCKS_ON_REQUEST_RELAY_OUTBOUND_BANDWIDTH_LIMIT =
            SETTING_SPECS.addThenGet(new PositiveIntegerSettingSpec(
                    "socks.onRequest.relayOutboundBandwidthLimit",
                    null));

    @NameValuePairValueSpecDoc(
            description = "The binding host name or address for all "
                    + "remote endpoints",
            name = "socks.onRequest.remoteEndpoints.bindHost",
            syntax = "socks.onRequest.remoteEndpoints.bindHost=HOST",
            valueType = Host.class
    )
    public static final SettingSpec<Host> SOCKS_ON_REQUEST_REMOTE_ENDPOINTS_BIND_HOST =
            SETTING_SPECS.addThenGet(new HostSettingSpec(
                    "socks.onRequest.remoteEndpoints.bindHost",
                    null));

    @NameValuePairValueSpecDoc(
            description = "The comma separated list of acceptable binding "
                    + "host address types for all remote endpoints",
            name = "socks.onRequest.remoteEndpoints.bindHostAddressTypes",
            syntax = "socks.onRequest.remoteEndpoints.bindHostAddressTypes=HOST_ADDRESS_TYPES",
            valueType = HostAddressTypes.class
    )
    public static final SettingSpec<HostAddressTypes> SOCKS_ON_REQUEST_REMOTE_ENDPOINTS_BIND_HOST_ADDRESS_TYPES =
            SETTING_SPECS.addThenGet(new HostAddressTypesSettingSpec(
                    "socks.onRequest.remoteEndpoints.bindHostAddressTypes",
                    HostAddressTypes.of()));

    @NameValuePairValueSpecDoc(
            description = "The comma separated list of binding port ranges for "
                    + "all remote TCP endpoints",
            name = "socks.onRequest.remoteEndpoints.bindTcpPortRanges",
            syntax = "socks.onRequest.remoteEndpoints.bindTcpPortRanges=PORT_RANGES",
            valueType = PortRanges.class
    )
    public static final SettingSpec<PortRanges> SOCKS_ON_REQUEST_REMOTE_ENDPOINTS_BIND_TCP_PORT_RANGES =
            SETTING_SPECS.addThenGet(new PortRangesSettingSpec(
                    "socks.onRequest.remoteEndpoints.bindTcpPortRanges",
                    PortRanges.of()));

    @NameValuePairValueSpecDoc(
            description = "The comma separated list of binding port ranges for "
                    + "all remote UDP endpoints",
            name = "socks.onRequest.remoteEndpoints.bindUdpPortRanges",
            syntax = "socks.onRequest.remoteEndpoints.bindUdpPortRanges=PORT_RANGES",
            valueType = PortRanges.class
    )
    public static final SettingSpec<PortRanges> SOCKS_ON_REQUEST_REMOTE_ENDPOINTS_BIND_UDP_PORT_RANGES =
            SETTING_SPECS.addThenGet(new PortRangesSettingSpec(
                    "socks.onRequest.remoteEndpoints.bindUdpPortRanges",
                    PortRanges.of()));

    @NameValuePairValueSpecDoc(
            description = "The network interface that provides a binding host "
                    + "address for all remote endpoints",
            name = "socks.onRequest.remoteEndpoints.netInterface",
            syntax = "socks.onRequest.remoteEndpoints.netInterface=NETWORK_INTERFACE",
            valueType = NetInterface.class
    )
    public static final SettingSpec<NetInterface> SOCKS_ON_REQUEST_REMOTE_ENDPOINTS_NET_INTERFACE =
            SETTING_SPECS.addThenGet(new NetInterfaceSettingSpec(
                    "socks.onRequest.remoteEndpoints.netInterface",
                    null));

    @NameValuePairValueSpecDoc(
            description = "The comma separated list of socket settings for all "
                    + "remote endpoints",
            name = "socks.onRequest.remoteEndpoints.socketSettings",
            syntax = "socks.onRequest.remoteEndpoints.socketSettings=SOCKET_SETTINGS",
            valueType = SocketSettings.class
    )
    public static final SettingSpec<SocketSettings> SOCKS_ON_REQUEST_REMOTE_ENDPOINTS_SOCKET_SETTINGS =
            SETTING_SPECS.addThenGet(new SocketSettingsSettingSpec(
                    "socks.onRequest.remoteEndpoints.socketSettings",
                    SocketSettings.of()));

    @NameValuePairValueSpecDoc(
            description = "The binding host name or address for the "
                    + "client-facing UDP socket",
            name = "socks.onUdpAssociateRequest.clientFacingDatagramSocket.bindHost",
            syntax = "socks.onUdpAssociateRequest.clientFacingDatagramSocket.bindHost=HOST",
            valueType = Host.class
    )
    public static final SettingSpec<Host> SOCKS_ON_UDP_ASSOCIATE_REQUEST_CLIENT_FACING_DATAGRAM_SOCKET_BIND_HOST =
            SETTING_SPECS.addThenGet(new HostSettingSpec(
                    "socks.onUdpAssociateRequest.clientFacingDatagramSocket.bindHost",
                    null));

    @NameValuePairValueSpecDoc(
            description = "The comma separated list of acceptable binding "
                    + "host address types for the client-facing UDP socket",
            name = "socks.onUdpAssociateRequest.clientFacingDatagramSocket.bindHostAddressTypes",
            syntax = "socks.onUdpAssociateRequest.clientFacingDatagramSocket.bindHostAddressTypes=HOST_ADDRESS_TYPES",
            valueType = HostAddressTypes.class
    )
    public static final SettingSpec<HostAddressTypes> SOCKS_ON_UDP_ASSOCIATE_REQUEST_CLIENT_FACING_DATAGRAM_SOCKET_BIND_HOST_ADDRESS_TYPES =
            SETTING_SPECS.addThenGet(new HostAddressTypesSettingSpec(
                    "socks.onUdpAssociateRequest.clientFacingDatagramSocket.bindHostAddressTypes",
                    HostAddressTypes.of()));

    @NameValuePairValueSpecDoc(
            description = "The comma separated list of binding port ranges for "
                    + "the client-facing UDP socket",
            name = "socks.onUdpAssociateRequest.clientFacingDatagramSocket.bindPortRanges",
            syntax = "socks.onUdpAssociateRequest.clientFacingDatagramSocket.bindPortRanges=PORT_RANGES",
            valueType = PortRanges.class
    )
    public static final SettingSpec<PortRanges> SOCKS_ON_UDP_ASSOCIATE_REQUEST_CLIENT_FACING_DATAGRAM_SOCKET_BIND_PORT_RANGES =
            SETTING_SPECS.addThenGet(new PortRangesSettingSpec(
                    "socks.onUdpAssociateRequest.clientFacingDatagramSocket.bindPortRanges",
                    PortRanges.of()));

    @NameValuePairValueSpecDoc(
            description = "The network interface that provides a binding host "
                    + "address for the client-facing UDP socket",
            name = "socks.onUdpAssociateRequest.clientFacingDatagramSocket.netInterface",
            syntax = "socks.onUdpAssociateRequest.clientFacingDatagramSocket.netInterface=NETWORK_INTERFACE",
            valueType = NetInterface.class
    )
    public static final SettingSpec<NetInterface> SOCKS_ON_UDP_ASSOCIATE_REQUEST_CLIENT_FACING_DATAGRAM_SOCKET_NET_INTERFACE =
            SETTING_SPECS.addThenGet(new NetInterfaceSettingSpec(
                    "socks.onUdpAssociateRequest.clientFacingDatagramSocket.netInterface",
                    null));

    @NameValuePairValueSpecDoc(
            description = "The comma separated list of socket settings for the "
                    + "client-facing UDP socket",
            name = "socks.onUdpAssociateRequest.clientFacingDatagramSocket.socketSettings",
            syntax = "socks.onUdpAssociateRequest.clientFacingDatagramSocket.socketSettings=SOCKET_SETTINGS",
            valueType = SocketSettings.class
    )
    public static final SettingSpec<SocketSettings> SOCKS_ON_UDP_ASSOCIATE_REQUEST_CLIENT_FACING_DATAGRAM_SOCKET_SOCKET_SETTINGS =
            SETTING_SPECS.addThenGet(new SocketSettingsSettingSpec(
                    "socks.onUdpAssociateRequest.clientFacingDatagramSocket.socketSettings",
                    SocketSettings.of()));

    @NameValuePairValueSpecDoc(
            description = "The buffer size in bytes for relaying the data",
            name = "socks.onUdpAssociateRequest.relayBufferSize",
            syntax = "socks.onUdpAssociateRequest.relayBufferSize=POSITIVE_INTEGER",
            valueType = PositiveInteger.class
    )
    public static final SettingSpec<PositiveInteger> SOCKS_ON_UDP_ASSOCIATE_REQUEST_RELAY_BUFFER_SIZE =
            SETTING_SPECS.addThenGet(new PositiveIntegerSettingSpec(
                    "socks.onUdpAssociateRequest.relayBufferSize",
                    null));

    @NameValuePairValueSpecDoc(
            description = "The timeout in milliseconds on relaying no data",
            name = "socks.onUdpAssociateRequest.relayIdleTimeout",
            syntax = "socks.onUdpAssociateRequest.relayIdleTimeout=POSITIVE_INTEGER",
            valueType = PositiveInteger.class
    )
    public static final SettingSpec<PositiveInteger> SOCKS_ON_UDP_ASSOCIATE_REQUEST_RELAY_IDLE_TIMEOUT =
            SETTING_SPECS.addThenGet(new PositiveIntegerSettingSpec(
                    "socks.onUdpAssociateRequest.relayIdleTimeout",
                    null));

    @NameValuePairValueSpecDoc(
            description = "The upper limit on bandwidth in bytes per second of "
                    + "receiving inbound data to be relayed",
            name = "socks.onUdpAssociateRequest.relayInboundBandwidthLimit",
            syntax = "socks.onUdpAssociateRequest.relayInboundBandwidthLimit=POSITIVE_INTEGER",
            valueType = PositiveInteger.class
    )
    public static final SettingSpec<PositiveInteger> SOCKS_ON_UDP_ASSOCIATE_REQUEST_RELAY_INBOUND_BANDWIDTH_LIMIT =
            SETTING_SPECS.addThenGet(new PositiveIntegerSettingSpec(
                    "socks.onUdpAssociateRequest.relayInboundBandwidthLimit",
                    null));

    @NameValuePairValueSpecDoc(
            description = "The upper limit on bandwidth in bytes per second of "
                    + "receiving outbound data to be relayed",
            name = "socks.onUdpAssociateRequest.relayOutboundBandwidthLimit",
            syntax = "socks.onUdpAssociateRequest.relayOutboundBandwidthLimit=POSITIVE_INTEGER",
            valueType = PositiveInteger.class
    )
    public static final SettingSpec<PositiveInteger> SOCKS_ON_UDP_ASSOCIATE_REQUEST_RELAY_OUTBOUND_BANDWIDTH_LIMIT =
            SETTING_SPECS.addThenGet(new PositiveIntegerSettingSpec(
                    "socks.onUdpAssociateRequest.relayOutboundBandwidthLimit",
                    null));

    @NameValuePairValueSpecDoc(
            description = "The binding host name or address for the remote-facing "
                    + "UDP socket",
            name = "socks.onUdpAssociateRequest.remoteFacingDatagramSocket.bindHost",
            syntax = "socks.onUdpAssociateRequest.remoteFacingDatagramSocket.bindHost=HOST",
            valueType = Host.class
    )
    public static final SettingSpec<Host> SOCKS_ON_UDP_ASSOCIATE_REQUEST_REMOTE_FACING_DATAGRAM_SOCKET_BIND_HOST =
            SETTING_SPECS.addThenGet(new HostSettingSpec(
                    "socks.onUdpAssociateRequest.remoteFacingDatagramSocket.bindHost",
                    null));

    @NameValuePairValueSpecDoc(
            description = "The comma separated list of acceptable binding "
                    + "host address types for the remote-facing UDP socket",
            name = "socks.onUdpAssociateRequest.remoteFacingDatagramSocket.bindHostAddressTypes",
            syntax = "socks.onUdpAssociateRequest.remoteFacingDatagramSocket.bindHostAddressTypes=HOST_ADDRESS_TYPES",
            valueType = HostAddressTypes.class
    )
    public static final SettingSpec<HostAddressTypes> SOCKS_ON_UDP_ASSOCIATE_REQUEST_REMOTE_FACING_DATAGRAM_SOCKET_BIND_HOST_ADDRESS_TYPES =
            SETTING_SPECS.addThenGet(new HostAddressTypesSettingSpec(
                    "socks.onUdpAssociateRequest.remoteFacingDatagramSocket.bindHostAddressTypes",
                    HostAddressTypes.of()));

    @NameValuePairValueSpecDoc(
            description = "The comma separated list of binding port ranges for "
                    + "the remote-facing UDP socket",
            name = "socks.onUdpAssociateRequest.remoteFacingDatagramSocket.bindPortRanges",
            syntax = "socks.onUdpAssociateRequest.remoteFacingDatagramSocket.bindPortRanges=PORT_RANGES",
            valueType = PortRanges.class
    )
    public static final SettingSpec<PortRanges> SOCKS_ON_UDP_ASSOCIATE_REQUEST_REMOTE_FACING_DATAGRAM_SOCKET_BIND_PORT_RANGES =
            SETTING_SPECS.addThenGet(new PortRangesSettingSpec(
                    "socks.onUdpAssociateRequest.remoteFacingDatagramSocket.bindPortRanges",
                    PortRanges.of()));

    @NameValuePairValueSpecDoc(
            description = "The network interface that provides a binding host "
                    + "address for the remote-facing UDP socket",
            name = "socks.onUdpAssociateRequest.remoteFacingDatagramSocket.netInterface",
            syntax = "socks.onUdpAssociateRequest.remoteFacingDatagramSocket.netInterface=NETWORK_INTERFACE",
            valueType = NetInterface.class
    )
    public static final SettingSpec<NetInterface> SOCKS_ON_UDP_ASSOCIATE_REQUEST_REMOTE_FACING_DATAGRAM_SOCKET_NET_INTERFACE =
            SETTING_SPECS.addThenGet(new NetInterfaceSettingSpec(
                    "socks.onUdpAssociateRequest.remoteFacingDatagramSocket.netInterface",
                    null));

    @NameValuePairValueSpecDoc(
            description = "The comma separated list of socket settings for the "
                    + "remote-facing UDP socket",
            name = "socks.onUdpAssociateRequest.remoteFacingDatagramSocket.socketSettings",
            syntax = "socks.onUdpAssociateRequest.remoteFacingDatagramSocket.socketSettings=SOCKET_SETTINGS",
            valueType = SocketSettings.class
    )
    public static final SettingSpec<SocketSettings> SOCKS_ON_UDP_ASSOCIATE_REQUEST_REMOTE_FACING_DATAGRAM_SOCKET_SOCKET_SETTINGS =
            SETTING_SPECS.addThenGet(new SocketSettingsSettingSpec(
                    "socks.onUdpAssociateRequest.remoteFacingDatagramSocket.socketSettings",
                    SocketSettings.of()));

    @NameValuePairValueSpecDoc(
            defaultValue = "StringSourceUserRepository:",
            description = "The user repository used for username password "
                    + "authentication",
            name = "socks.userpassauthmethod.userRepository",
            syntax = "socks.userpassauthmethod.userRepository=SOCKS_USERPASSAUTHMETHOD_USER_REPOSITORY",
            valueType = String.class
    )
    public static final SettingSpec<String> SOCKS_USERPASSAUTHMETHOD_USER_REPOSITORY =
            SETTING_SPECS.addThenGet(new StringSettingSpec(
                    "socks.userpassauthmethod.userRepository",
                    "StringSourceUserRepository:"));

    public static List<SettingSpec<Object>> values() {
        return SETTING_SPECS.toList();
    }

    public static Map<String, SettingSpec<Object>> valuesMap() {
        return SETTING_SPECS.toMap();
    }
    
    private SocksSettingSpecConstants() { }
    
}
