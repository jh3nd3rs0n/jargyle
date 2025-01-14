package com.github.jh3nd3rs0n.jargyle.client;

import com.github.jh3nd3rs0n.jargyle.client.internal.propertyspec.impl.*;
import com.github.jh3nd3rs0n.jargyle.common.net.*;
import com.github.jh3nd3rs0n.jargyle.common.number.NonNegativeInteger;
import com.github.jh3nd3rs0n.jargyle.internal.annotation.NameValuePairValueSpecDoc;
import com.github.jh3nd3rs0n.jargyle.internal.annotation.NameValuePairValueSpecsDoc;

import java.util.List;
import java.util.Map;

/**
 * General {@code PropertySpec} constants.
 */
@NameValuePairValueSpecsDoc(
        description = "General properties",
        name = "General Properties"
)
public final class GeneralPropertySpecConstants {

    /**
     * The {@code PropertySpecs} of the {@code PropertySpec} constants of
     * this class.
     */
    private static final PropertySpecs PROPERTY_SPECS = new PropertySpecs();

    /**
     * {@code PropertySpec} constant for {@code socksClient.clientBindHost}:
     * the {@code Host} for the binding host name or address for the client
     * socket that is used to connect to the SOCKS server (default is
     * {@code 0.0.0.0}).
     */
    @NameValuePairValueSpecDoc(
            description = "The binding host name or address for the client "
                    + "socket that is used to connect to the SOCKS server "
                    + "(default is 0.0.0.0)",
            name = "socksClient.clientBindHost",
            syntax = "socksClient.clientBindHost=HOST",
            valueType = Host.class
    )
    public static final PropertySpec<Host> CLIENT_BIND_HOST =
            PROPERTY_SPECS.addThenGet(new HostPropertySpec(
                    "socksClient.clientBindHost",
                    null));

    /**
     * {@code PropertySpec} constant for
     * {@code socksClient.clientBindHostAddressTypes}: the
     * {@code HostAddressTypes} for the acceptable binding host address types
     * for the client socket that is used to connect to the SOCKS server
     * (default is {@code IPv4,IPv6}).
     */
    @NameValuePairValueSpecDoc(
            description = "The comma separated list of acceptable binding "
                    + "host address types for the client socket that is used "
                    + "to connect to the SOCKS server (default is IPv4,IPv6)",
            name = "socksClient.clientBindHostAddressTypes",
            syntax = "socksClient.clientBindHostAddressTypes=HOST_ADDRESS_TYPES",
            valueType = HostAddressTypes.class
    )
    public static final PropertySpec<HostAddressTypes> CLIENT_BIND_HOST_ADDRESS_TYPES =
            PROPERTY_SPECS.addThenGet(new HostAddressTypesPropertySpec(
                    "socksClient.clientBindHostAddressTypes",
                    HostAddressTypes.getDefault()));

    /**
     * {@code PropertySpec} constant for
     * {@code socksClient.clientBindPortRanges}: the {@code PortRanges} for
     * the binding port ranges for the client socket that is used to connect
     * to the SOCKS server (default is {@code 0}).
     */
    @NameValuePairValueSpecDoc(
            description = "The comma separated list of binding port ranges for "
                    + "the client socket that is used to connect to the SOCKS "
                    + "server (default is 0)",
            name = "socksClient.clientBindPortRanges",
            syntax = "socksClient.clientBindPortRanges=PORT_RANGES",
            valueType = PortRanges.class
    )
    public static final PropertySpec<PortRanges> CLIENT_BIND_PORT_RANGES =
            PROPERTY_SPECS.addThenGet(new PortRangesPropertySpec(
                    "socksClient.clientBindPortRanges",
                    PortRanges.getDefault()));

    /**
     * {@code PropertySpec} constant for
     * {@code socksClient.clientConnectTimeout}: the {@code NonNegativeInteger}
     * for the timeout in milliseconds on waiting for the client socket to
     * connect to the SOCKS server (a timeout of {@code 0} is interpreted as
     * an infinite timeout) (default is {@code 60000}).
     */
    @NameValuePairValueSpecDoc(
            description = "The timeout in milliseconds on waiting for the "
                    + "client socket to connect to the SOCKS server (a "
                    + "timeout of 0 is interpreted as an infinite timeout) "
                    + "(default is 60000)",
            name = "socksClient.clientConnectTimeout",
            syntax = "socksClient.clientConnectTimeout=NON_NEGATIVE_INTEGER",
            valueType = NonNegativeInteger.class
    )
    public static final PropertySpec<NonNegativeInteger> CLIENT_CONNECT_TIMEOUT =
            PROPERTY_SPECS.addThenGet(new NonNegativeIntegerPropertySpec(
                    "socksClient.clientConnectTimeout",
                    NonNegativeInteger.valueOf(60000))); // 1 minute

    /**
     * {@code PropertySpec} constant for
     * {@code socksClient.clientNetInterface}: the {@code NetInterface} for
     * the network interface that provides a binding host address for the
     * client socket that is used to connect to the SOCKS server.
     */
    @NameValuePairValueSpecDoc(
            description = "The network interface that provides a binding "
                    + "host address for the client socket that is used to "
                    + "connect to the SOCKS server",
            name = "socksClient.clientNetInterface",
            syntax = "socksClient.clientNetInterface=NETWORK_INTERFACE",
            valueType = NetInterface.class
    )
    public static final PropertySpec<NetInterface> CLIENT_NET_INTERFACE =
            PROPERTY_SPECS.addThenGet(new NetInterfacePropertySpec(
                    "socksClient.clientNetInterface",
                    null));

    /**
     * {@code PropertySpec} constant for
     * {@code socksClient.clientSocketSettings}: the {@code SocketSettings}
     * for the client socket that is used to connect to the SOCKS server.
     */
    @NameValuePairValueSpecDoc(
            description = "The comma separated list of socket settings for the "
                    + "client socket that is used to connect to the SOCKS "
                    + "server",
            name = "socksClient.clientSocketSettings",
            syntax = "socksClient.clientSocketSettings=SOCKET_SETTINGS",
            valueType = SocketSettings.class
    )
    public static final PropertySpec<SocketSettings> CLIENT_SOCKET_SETTINGS =
            PROPERTY_SPECS.addThenGet(new SocketSettingsPropertySpec(
                    "socksClient.clientSocketSettings",
                    SocketSettings.of()));

    /**
     * {@code PropertySpec} constant for {@code socksClient.socksServerUri}:
     * the {@code SocksServerUri} for the URI of the SOCKS server.
     */
    @NameValuePairValueSpecDoc(
            description = "The URI of the SOCKS server",
            name = "socksClient.socksServerUri",
            syntax = "socksClient.socksServerUri=SOCKS_SERVER_URI",
            valueType = SocksServerUri.class
    )
    public static final PropertySpec<SocksServerUri> SOCKS_SERVER_URI =
            PROPERTY_SPECS.addThenGet(new SocksServerUriPropertySpec(
                    "socksClient.socksServerUri",
                    null));

    /**
     * Prevents the construction of unnecessary instances.
     */
    private GeneralPropertySpecConstants() {
    }

    /**
     * Returns an unmodifiable {@code List} of the {@code PropertySpec}
     * constants.
     *
     * @return an unmodifiable {@code List} of the {@code PropertySpec}
     * constants
     */
    public static List<PropertySpec<Object>> values() {
        return PROPERTY_SPECS.toList();
    }

    /**
     * Returns an unmodifiable {@code Map} of the {@code PropertySpec}
     * constants each associated by the name they specify for their
     * {@code Property}.
     *
     * @return an unmodifiable {@code Map} of the {@code PropertySpec}
     * constants each associated by the name they specify for their
     * {@code Property}
     */
    public static Map<String, PropertySpec<Object>> valuesMap() {
        return PROPERTY_SPECS.toMap();
    }

}
