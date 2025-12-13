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
            description = "The comma separated list of socket settings for the "
                    + "inbound socket",
            name = "socks.onBindRequest.inboundSocketSettings",
            syntax = "socks.onBindRequest.inboundSocketSettings=SOCKET_SETTINGS",
            valueType = SocketSettings.class
    )
    public static final SettingSpec<SocketSettings> SOCKS_ON_BIND_REQUEST_INBOUND_SOCKET_SETTINGS =
            SETTING_SPECS.addThenGet(new SocketSettingsSettingSpec(
                    "socks.onBindRequest.inboundSocketSettings",
                    SocketSettings.of()));

    @NameValuePairValueSpecDoc(
            description = "The binding host name or address for the listen "
                    + "socket if the provided host address is all zeros",
            name = "socks.onBindRequest.listenBindHost",
            syntax = "socks.onBindRequest.listenBindHost=HOST",
            valueType = Host.class
    )
    public static final SettingSpec<Host> SOCKS_ON_BIND_REQUEST_LISTEN_BIND_HOST =
            SETTING_SPECS.addThenGet(new HostSettingSpec(
                    "socks.onBindRequest.listenBindHost",
                    null));

    @NameValuePairValueSpecDoc(
            description = "The comma separated list of acceptable binding " +
                    "host address types for the listen socket if the "
                    + "provided host address is all zeros",
            name = "socks.onBindRequest.listenBindHostAddressTypes",
            syntax = "socks.onBindRequest.listenBindHostAddressTypes=HOST_ADDRESS_TYPES",
            valueType = HostAddressTypes.class
    )
    public static final SettingSpec<HostAddressTypes> SOCKS_ON_BIND_REQUEST_LISTEN_BIND_HOST_ADDRESS_TYPES =
            SETTING_SPECS.addThenGet(new HostAddressTypesSettingSpec(
                    "socks.onBindRequest.listenBindHostAddressTypes",
                    HostAddressTypes.of()));

    @NameValuePairValueSpecDoc(
            description = "The comma separated list of binding port ranges for "
                    + "the listen socket if the provided port is zero",
            name = "socks.onBindRequest.listenBindPortRanges",
            syntax = "socks.onBindRequest.listenBindPortRanges=PORT_RANGES",
            valueType = PortRanges.class
    )
    public static final SettingSpec<PortRanges> SOCKS_ON_BIND_REQUEST_LISTEN_BIND_PORT_RANGES =
            SETTING_SPECS.addThenGet(new PortRangesSettingSpec(
                    "socks.onBindRequest.listenBindPortRanges",
                    PortRanges.of()));

    @NameValuePairValueSpecDoc(
            description = "The network interface that provides a binding "
                    + "host address for the listen socket if the provided "
                    + "host address is all zeros",
            name = "socks.onBindRequest.listenNetInterface",
            syntax = "socks.onBindRequest.listenNetInterface=NETWORK_INTERFACE",
            valueType = NetInterface.class
    )
    public static final SettingSpec<NetInterface> SOCKS_ON_BIND_REQUEST_LISTEN_NET_INTERFACE =
            SETTING_SPECS.addThenGet(new NetInterfaceSettingSpec(
                    "socks.onBindRequest.listenNetInterface",
                    null));

    @NameValuePairValueSpecDoc(
            description = "The comma separated list of socket settings for the "
                    + "listen socket",
            name = "socks.onBindRequest.listenSocketSettings",
            syntax = "socks.onBindRequest.listenSocketSettings=SOCKET_SETTINGS",
            valueType = SocketSettings.class
    )
    public static final SettingSpec<SocketSettings> SOCKS_ON_BIND_REQUEST_LISTEN_SOCKET_SETTINGS =
            SETTING_SPECS.addThenGet(new SocketSettingsSettingSpec(
                    "socks.onBindRequest.listenSocketSettings",
                    SocketSettings.of()));

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
            defaultValue = "false",
            description = "The boolean value to indicate if the target-facing "
                    + "socket is to be prepared before connecting (involves "
                    + "applying the specified socket settings, resolving the "
                    + "target host name, and setting the specified timeout on "
                    + "waiting to connect)",
            name = "socks.onConnectRequest.prepareTargetFacingSocket",
            syntax = "socks.onConnectRequest.prepareTargetFacingSocket=true|false",
            valueType = Boolean.class
    )
    public static final SettingSpec<Boolean> SOCKS_ON_CONNECT_REQUEST_PREPARE_TARGET_FACING_SOCKET =
            SETTING_SPECS.addThenGet(new BooleanSettingSpec(
                    "socks.onConnectRequest.prepareTargetFacingSocket",
                    Boolean.FALSE));

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
            description = "The binding host name or address for the "
                    + "target-facing socket",
            name = "socks.onConnectRequest.targetFacingBindHost",
            syntax = "socks.onConnectRequest.targetFacingBindHost=HOST",
            valueType = Host.class
    )
    public static final SettingSpec<Host> SOCKS_ON_CONNECT_REQUEST_TARGET_FACING_BIND_HOST =
            SETTING_SPECS.addThenGet(new HostSettingSpec(
                    "socks.onConnectRequest.targetFacingBindHost",
                    null));

    @NameValuePairValueSpecDoc(
            description = "The comma separated list of acceptable binding "
                    + "host address types for the target-facing socket",
            name = "socks.onConnectRequest.targetFacingBindHostAddressTypes",
            syntax = "socks.onConnectRequest.targetFacingBindHostAddressTypes=HOST_ADDRESS_TYPES",
            valueType = HostAddressTypes.class
    )
    public static final SettingSpec<HostAddressTypes> SOCKS_ON_CONNECT_REQUEST_TARGET_FACING_BIND_HOST_ADDRESS_TYPES =
            SETTING_SPECS.addThenGet(new HostAddressTypesSettingSpec(
                    "socks.onConnectRequest.targetFacingBindHostAddressTypes",
                    HostAddressTypes.of()));

    @NameValuePairValueSpecDoc(
            description = "The comma separated list of binding port ranges for "
                    + "the target-facing socket",
            name = "socks.onConnectRequest.targetFacingBindPortRanges",
            syntax = "socks.onConnectRequest.targetFacingBindPortRanges=PORT_RANGES",
            valueType = PortRanges.class
    )
    public static final SettingSpec<PortRanges> SOCKS_ON_CONNECT_REQUEST_TARGET_FACING_BIND_PORT_RANGES =
            SETTING_SPECS.addThenGet(new PortRangesSettingSpec(
                    "socks.onConnectRequest.targetFacingBindPortRanges",
                    PortRanges.of()));

    @NameValuePairValueSpecDoc(
            defaultValue = "60000",
            description = "The timeout in milliseconds on waiting for the "
                    + "target-facing socket to connect",
            name = "socks.onConnectRequest.targetFacingConnectTimeout",
            syntax = "socks.onConnectRequest.targetFacingConnectTimeout=POSITIVE_INTEGER",
            valueType = PositiveInteger.class
    )
    public static final SettingSpec<PositiveInteger> SOCKS_ON_CONNECT_REQUEST_TARGET_FACING_CONNECT_TIMEOUT =
            SETTING_SPECS.addThenGet(new PositiveIntegerSettingSpec(
                    "socks.onConnectRequest.targetFacingConnectTimeout",
                    PositiveInteger.valueOf(60000)));

    @NameValuePairValueSpecDoc(
            description = "The network interface that provides a binding "
                    + "host address for the target-facing socket",
            name = "socks.onConnectRequest.targetFacingNetInterface",
            syntax = "socks.onConnectRequest.targetFacingNetInterface=NETWORK_INTERFACE",
            valueType = NetInterface.class
    )
    public static final SettingSpec<NetInterface> SOCKS_ON_CONNECT_REQUEST_TARGET_FACING_NET_INTERFACE =
            SETTING_SPECS.addThenGet(new NetInterfaceSettingSpec(
                    "socks.onConnectRequest.targetFacingNetInterface",
                    null));

    @NameValuePairValueSpecDoc(
            description = "The comma separated list of socket settings for the "
                    + "target-facing socket",
            name = "socks.onConnectRequest.targetFacingSocketSettings",
            syntax = "socks.onConnectRequest.targetFacingSocketSettings=SOCKET_SETTINGS",
            valueType = SocketSettings.class
    )
    public static final SettingSpec<SocketSettings> SOCKS_ON_CONNECT_REQUEST_TARGET_FACING_SOCKET_SETTINGS =
            SETTING_SPECS.addThenGet(new SocketSettingsSettingSpec(
                    "socks.onConnectRequest.targetFacingSocketSettings",
                    SocketSettings.of()));

    @NameValuePairValueSpecDoc(
            description = "The binding host name or address for all "
                    + "external-facing sockets",
            name = "socks.onRequest.externalFacingBindHost",
            syntax = "socks.onRequest.externalFacingBindHost=HOST",
            valueType = Host.class
    )
    public static final SettingSpec<Host> SOCKS_ON_REQUEST_EXTERNAL_FACING_BIND_HOST =
            SETTING_SPECS.addThenGet(new HostSettingSpec(
                    "socks.onRequest.externalFacingBindHost",
                    null));

    @NameValuePairValueSpecDoc(
            description = "The comma separated list of acceptable binding "
                    + "host address types for all external-facing sockets",
            name = "socks.onRequest.externalFacingBindHostAddressTypes",
            syntax = "socks.onRequest.externalFacingBindHostAddressTypes=HOST_ADDRESS_TYPES",
            valueType = HostAddressTypes.class
    )
    public static final SettingSpec<HostAddressTypes> SOCKS_ON_REQUEST_EXTERNAL_FACING_BIND_HOST_ADDRESS_TYPES =
            SETTING_SPECS.addThenGet(new HostAddressTypesSettingSpec(
                    "socks.onRequest.externalFacingBindHostAddressTypes",
                    HostAddressTypes.of()));

    @NameValuePairValueSpecDoc(
            description = "The comma separated list of binding port ranges for "
                    + "all external-facing TCP sockets",
            name = "socks.onRequest.externalFacingBindTcpPortRanges",
            syntax = "socks.onRequest.externalFacingBindTcpPortRanges=PORT_RANGES",
            valueType = PortRanges.class
    )
    public static final SettingSpec<PortRanges> SOCKS_ON_REQUEST_EXTERNAL_FACING_BIND_TCP_PORT_RANGES =
            SETTING_SPECS.addThenGet(new PortRangesSettingSpec(
                    "socks.onRequest.externalFacingBindTcpPortRanges",
                    PortRanges.of()));

    @NameValuePairValueSpecDoc(
            description = "The comma separated list of binding port ranges for "
                    + "all external-facing UDP sockets",
            name = "socks.onRequest.externalFacingBindUdpPortRanges",
            syntax = "socks.onRequest.externalFacingBindUdpPortRanges=PORT_RANGES",
            valueType = PortRanges.class
    )
    public static final SettingSpec<PortRanges> SOCKS_ON_REQUEST_EXTERNAL_FACING_BIND_UDP_PORT_RANGES =
            SETTING_SPECS.addThenGet(new PortRangesSettingSpec(
                    "socks.onRequest.externalFacingBindUdpPortRanges",
                    PortRanges.of()));

    @NameValuePairValueSpecDoc(
            description = "The network interface that provides a binding host "
                    + "address for all external-facing sockets",
            name = "socks.onRequest.externalFacingNetInterface",
            syntax = "socks.onRequest.externalFacingNetInterface=NETWORK_INTERFACE",
            valueType = NetInterface.class
    )
    public static final SettingSpec<NetInterface> SOCKS_ON_REQUEST_EXTERNAL_FACING_NET_INTERFACE =
            SETTING_SPECS.addThenGet(new NetInterfaceSettingSpec(
                    "socks.onRequest.externalFacingNetInterface",
                    null));

    @NameValuePairValueSpecDoc(
            description = "The comma separated list of socket settings for all "
                    + "external-facing sockets",
            name = "socks.onRequest.externalFacingSocketSettings",
            syntax = "socks.onRequest.externalFacingSocketSettings=SOCKET_SETTINGS",
            valueType = SocketSettings.class
    )
    public static final SettingSpec<SocketSettings> SOCKS_ON_REQUEST_EXTERNAL_FACING_SOCKET_SETTINGS =
            SETTING_SPECS.addThenGet(new SocketSettingsSettingSpec(
                    "socks.onRequest.externalFacingSocketSettings",
                    SocketSettings.of()));

    @NameValuePairValueSpecDoc(
            description = "The binding host name or address for all "
                    + "internal-facing sockets",
            name = "socks.onRequest.internalFacingBindHost",
            syntax = "socks.onRequest.internalFacingBindHost=HOST",
            valueType = Host.class
    )
    public static final SettingSpec<Host> SOCKS_ON_REQUEST_INTERNAL_FACING_BIND_HOST =
            SETTING_SPECS.addThenGet(new HostSettingSpec(
                    "socks.onRequest.internalFacingBindHost",
                    null));

    @NameValuePairValueSpecDoc(
            description = "The comma separated list of acceptable binding "
                    + "host address types for all internal-facing sockets",
            name = "socks.onRequest.internalFacingBindHostAddressTypes",
            syntax = "socks.onRequest.internalFacingBindHostAddressTypes=HOST_ADDRESS_TYPES",
            valueType = HostAddressTypes.class
    )
    public static final SettingSpec<HostAddressTypes> SOCKS_ON_REQUEST_INTERNAL_FACING_BIND_HOST_ADDRESS_TYPES =
            SETTING_SPECS.addThenGet(new HostAddressTypesSettingSpec(
                    "socks.onRequest.internalFacingBindHostAddressTypes",
                    HostAddressTypes.of()));

    @NameValuePairValueSpecDoc(
            description = "The comma separated list of binding port ranges for "
                    + "all internal-facing UDP sockets",
            name = "socks.onRequest.internalFacingBindUdpPortRanges",
            syntax = "socks.onRequest.internalFacingBindUdpPortRanges=PORT_RANGES",
            valueType = PortRanges.class
    )
    public static final SettingSpec<PortRanges> SOCKS_ON_REQUEST_INTERNAL_FACING_BIND_UDP_PORT_RANGES =
            SETTING_SPECS.addThenGet(new PortRangesSettingSpec(
                    "socks.onRequest.internalFacingBindUdpPortRanges",
                    PortRanges.of()));

    @NameValuePairValueSpecDoc(
            description = "The network interface that provides a binding host "
                    + "address for all internal-facing sockets",
            name = "socks.onRequest.internalFacingNetInterface",
            syntax = "socks.onRequest.internalFacingNetInterface=NETWORK_INTERFACE",
            valueType = NetInterface.class
    )
    public static final SettingSpec<NetInterface> SOCKS_ON_REQUEST_INTERNAL_FACING_NET_INTERFACE =
            SETTING_SPECS.addThenGet(new NetInterfaceSettingSpec(
                    "socks.onRequest.internalFacingNetInterface",
                    null));

    @NameValuePairValueSpecDoc(
            description = "The comma separated list of socket settings for all "
                    + "internal-facing sockets",
            name = "socks.onRequest.internalFacingSocketSettings",
            syntax = "socks.onRequest.internalFacingSocketSettings=SOCKET_SETTINGS",
            valueType = SocketSettings.class
    )
    public static final SettingSpec<SocketSettings> SOCKS_ON_REQUEST_INTERNAL_FACING_SOCKET_SETTINGS =
            SETTING_SPECS.addThenGet(new SocketSettingsSettingSpec(
                    "socks.onRequest.internalFacingSocketSettings",
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
            description = "The binding host name or address for the "
                    + "client-facing UDP socket",
            name = "socks.onUdpAssociateRequest.clientFacingBindHost",
            syntax = "socks.onUdpAssociateRequest.clientFacingBindHost=HOST",
            valueType = Host.class
    )
    public static final SettingSpec<Host> SOCKS_ON_UDP_ASSOCIATE_REQUEST_CLIENT_FACING_BIND_HOST =
            SETTING_SPECS.addThenGet(new HostSettingSpec(
                    "socks.onUdpAssociateRequest.clientFacingBindHost",
                    null));

    @NameValuePairValueSpecDoc(
            description = "The comma separated list of acceptable binding "
                    + "host address types for the client-facing UDP socket",
            name = "socks.onUdpAssociateRequest.clientFacingBindHostAddressTypes",
            syntax = "socks.onUdpAssociateRequest.clientFacingBindHostAddressTypes=HOST_ADDRESS_TYPES",
            valueType = HostAddressTypes.class
    )
    public static final SettingSpec<HostAddressTypes> SOCKS_ON_UDP_ASSOCIATE_REQUEST_CLIENT_FACING_BIND_HOST_ADDRESS_TYPES =
            SETTING_SPECS.addThenGet(new HostAddressTypesSettingSpec(
                    "socks.onUdpAssociateRequest.clientFacingBindHostAddressTypes",
                    HostAddressTypes.of()));

    @NameValuePairValueSpecDoc(
            description = "The comma separated list of binding port ranges for "
                    + "the client-facing UDP socket",
            name = "socks.onUdpAssociateRequest.clientFacingBindPortRanges",
            syntax = "socks.onUdpAssociateRequest.clientFacingBindPortRanges=PORT_RANGES",
            valueType = PortRanges.class
    )
    public static final SettingSpec<PortRanges> SOCKS_ON_UDP_ASSOCIATE_REQUEST_CLIENT_FACING_BIND_PORT_RANGES =
            SETTING_SPECS.addThenGet(new PortRangesSettingSpec(
                    "socks.onUdpAssociateRequest.clientFacingBindPortRanges",
                    PortRanges.of()));

    @NameValuePairValueSpecDoc(
            description = "The network interface that provides a binding host "
                    + "address for the client-facing UDP socket",
            name = "socks.onUdpAssociateRequest.clientFacingNetInterface",
            syntax = "socks.onUdpAssociateRequest.clientFacingNetInterface=NETWORK_INTERFACE",
            valueType = NetInterface.class
    )
    public static final SettingSpec<NetInterface> SOCKS_ON_UDP_ASSOCIATE_REQUEST_CLIENT_FACING_NET_INTERFACE =
            SETTING_SPECS.addThenGet(new NetInterfaceSettingSpec(
                    "socks.onUdpAssociateRequest.clientFacingNetInterface",
                    null));

    @NameValuePairValueSpecDoc(
            description = "The comma separated list of socket settings for the "
                    + "client-facing UDP socket",
            name = "socks.onUdpAssociateRequest.clientFacingSocketSettings",
            syntax = "socks.onUdpAssociateRequest.clientFacingSocketSettings=SOCKET_SETTINGS",
            valueType = SocketSettings.class
    )
    public static final SettingSpec<SocketSettings> SOCKS_ON_UDP_ASSOCIATE_REQUEST_CLIENT_FACING_SOCKET_SETTINGS =
            SETTING_SPECS.addThenGet(new SocketSettingsSettingSpec(
                    "socks.onUdpAssociateRequest.clientFacingSocketSettings",
                    SocketSettings.of()));

    @NameValuePairValueSpecDoc(
            description = "The binding host name or address for the peer-facing "
                    + "UDP socket",
            name = "socks.onUdpAssociateRequest.peerFacingBindHost",
            syntax = "socks.onUdpAssociateRequest.peerFacingBindHost=HOST",
            valueType = Host.class
    )
    public static final SettingSpec<Host> SOCKS_ON_UDP_ASSOCIATE_REQUEST_PEER_FACING_BIND_HOST =
            SETTING_SPECS.addThenGet(new HostSettingSpec(
                    "socks.onUdpAssociateRequest.peerFacingBindHost",
                    null));

    @NameValuePairValueSpecDoc(
            description = "The comma separated list of acceptable binding "
                    + "host address types for the peer-facing UDP socket",
            name = "socks.onUdpAssociateRequest.peerFacingBindHostAddressTypes",
            syntax = "socks.onUdpAssociateRequest.peerFacingBindHostAddressTypes=HOST_ADDRESS_TYPES",
            valueType = HostAddressTypes.class
    )
    public static final SettingSpec<HostAddressTypes> SOCKS_ON_UDP_ASSOCIATE_REQUEST_PEER_FACING_BIND_HOST_ADDRESS_TYPES =
            SETTING_SPECS.addThenGet(new HostAddressTypesSettingSpec(
                    "socks.onUdpAssociateRequest.peerFacingBindHostAddressTypes",
                    HostAddressTypes.of()));

    @NameValuePairValueSpecDoc(
            description = "The comma separated list of binding port ranges for "
                    + "the peer-facing UDP socket",
            name = "socks.onUdpAssociateRequest.peerFacingBindPortRanges",
            syntax = "socks.onUdpAssociateRequest.peerFacingBindPortRanges=PORT_RANGES",
            valueType = PortRanges.class
    )
    public static final SettingSpec<PortRanges> SOCKS_ON_UDP_ASSOCIATE_REQUEST_PEER_FACING_BIND_PORT_RANGES =
            SETTING_SPECS.addThenGet(new PortRangesSettingSpec(
                    "socks.onUdpAssociateRequest.peerFacingBindPortRanges",
                    PortRanges.of()));

    @NameValuePairValueSpecDoc(
            description = "The network interface that provides a binding host "
                    + "address for the peer-facing UDP socket",
            name = "socks.onUdpAssociateRequest.peerFacingNetInterface",
            syntax = "socks.onUdpAssociateRequest.peerFacingNetInterface=NETWORK_INTERFACE",
            valueType = NetInterface.class
    )
    public static final SettingSpec<NetInterface> SOCKS_ON_UDP_ASSOCIATE_REQUEST_PEER_FACING_NET_INTERFACE =
            SETTING_SPECS.addThenGet(new NetInterfaceSettingSpec(
                    "socks.onUdpAssociateRequest.peerFacingNetInterface",
                    null));

    @NameValuePairValueSpecDoc(
            description = "The comma separated list of socket settings for the "
                    + "peer-facing UDP socket",
            name = "socks.onUdpAssociateRequest.peerFacingSocketSettings",
            syntax = "socks.onUdpAssociateRequest.peerFacingSocketSettings=SOCKET_SETTINGS",
            valueType = SocketSettings.class
    )
    public static final SettingSpec<SocketSettings> SOCKS_ON_UDP_ASSOCIATE_REQUEST_PEER_FACING_SOCKET_SETTINGS =
            SETTING_SPECS.addThenGet(new SocketSettingsSettingSpec(
                    "socks.onUdpAssociateRequest.peerFacingSocketSettings",
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
