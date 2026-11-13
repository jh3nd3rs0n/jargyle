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
     * {@code PropertySpec} constant for
     * {@code socksClient.socksServerSocket.bindHost}: the {@code Host} for
     * the binding host name or address for the SOCKS server {@code Socket}
     * (default value is {@code 0.0.0.0}).
     */
    @NameValuePairValueSpecDoc(
            defaultValue = "0.0.0.0",
            description = "The binding host name or address for the SOCKS "
                    + "server TCP socket",
            name = "socksClient.socksServerSocket.bindHost",
            syntax = "socksClient.socksServerSocket.bindHost=HOST",
            valueType = Host.class
    )
    public static final PropertySpec<Host> SOCKS_SERVER_SOCKET_BIND_HOST =
            PROPERTY_SPECS.addThenGet(new HostPropertySpec(
                    "socksClient.socksServerSocket.bindHost",
                    null));

    /**
     * {@code PropertySpec} constant for
     * {@code socksClient.socksServerSocket.bindHostAddressTypes}: the
     * {@code HostAddressTypes} for the acceptable binding host address types
     * for the SOCKS server {@code Socket} (default value is
     * {@code HOST_IPV4_ADDRESS,HOST_IPV6_ADDRESS}).
     */
    @NameValuePairValueSpecDoc(
            defaultValue = "HOST_IPV4_ADDRESS,HOST_IPV6_ADDRESS",
            description = "The comma separated list of acceptable binding "
                    + "host address types for the SOCKS server TCP socket",
            name = "socksClient.socksServerSocket.bindHostAddressTypes",
            syntax = "socksClient.socksServerSocket.bindHostAddressTypes=HOST_ADDRESS_TYPES",
            valueType = HostAddressTypes.class
    )
    public static final PropertySpec<HostAddressTypes> SOCKS_SERVER_SOCKET_BIND_HOST_ADDRESS_TYPES =
            PROPERTY_SPECS.addThenGet(new HostAddressTypesPropertySpec(
                    "socksClient.socksServerSocket.bindHostAddressTypes",
                    HostAddressTypes.getDefault()));

    /**
     * {@code PropertySpec} constant for
     * {@code socksClient.socksServerSocket.bindPortRanges}: the
     * {@code PortRanges} for the binding port ranges for the SOCKS server
     * {@code Socket} (default value is {@code 0}).
     */
    @NameValuePairValueSpecDoc(
            defaultValue = "0",
            description = "The comma separated list of binding port ranges for "
                    + "the SOCKS server TCP socket",
            name = "socksClient.socksServerSocket.bindPortRanges",
            syntax = "socksClient.socksServerSocket.bindPortRanges=PORT_RANGES",
            valueType = PortRanges.class
    )
    public static final PropertySpec<PortRanges> SOCKS_SERVER_SOCKET_BIND_PORT_RANGES =
            PROPERTY_SPECS.addThenGet(new PortRangesPropertySpec(
                    "socksClient.socksServerSocket.bindPortRanges",
                    PortRanges.getDefault()));

    /**
     * {@code PropertySpec} constant for
     * {@code socksClient.socksServerSocket.connectTimeout}: the
     * {@code NonNegativeInteger} for the timeout in milliseconds on waiting
     * for the SOCKS server {@code Socket} to connect (a timeout of {@code 0}
     * is interpreted as an infinite timeout) (default value is {@code 60000}).
     */
    @NameValuePairValueSpecDoc(
            defaultValue = "60000",
            description = "The timeout in milliseconds on waiting for the "
                    + "SOCKS server TCP socket to connect (a timeout of 0 is "
                    + "interpreted as an infinite timeout)",
            name = "socksClient.socksServerSocket.connectTimeout",
            syntax = "socksClient.socksServerSocket.connectTimeout=NON_NEGATIVE_INTEGER",
            valueType = NonNegativeInteger.class
    )
    public static final PropertySpec<NonNegativeInteger> SOCKS_SERVER_SOCKET_CONNECT_TIMEOUT =
            PROPERTY_SPECS.addThenGet(new NonNegativeIntegerPropertySpec(
                    "socksClient.socksServerSocket.connectTimeout",
                    NonNegativeInteger.valueOf(60000))); // 1 minute

    /**
     * {@code PropertySpec} constant for
     * {@code socksClient.socksServerSocket.netInterface}: the
     * {@code NetInterface} for the network interface that provides a binding
     * host address for the SOCKS server {@code Socket}.
     */
    @NameValuePairValueSpecDoc(
            description = "The network interface that provides a binding "
                    + "host address for the SOCKS server TCP socket",
            name = "socksClient.socksServerSocket.netInterface",
            syntax = "socksClient.socksServerSocket.netInterface=NETWORK_INTERFACE",
            valueType = NetInterface.class
    )
    public static final PropertySpec<NetInterface> SOCKS_SERVER_SOCKET_NET_INTERFACE =
            PROPERTY_SPECS.addThenGet(new NetInterfacePropertySpec(
                    "socksClient.socksServerSocket.netInterface",
                    null));

    /**
     * {@code PropertySpec} constant for
     * {@code socksClient.socksServerSocket.prepareSocket}: the
     * {@code Boolean} value to indicate if the SOCKS server {@code Socket} is
     * to be prepared before connecting (involves apply the specified socket
     * settings, resolving the SOCKS server host name, and setting the
     * specified timeout on waiting to connect) (default value is
     * {@code false}).
     */
    @NameValuePairValueSpecDoc(
            defaultValue = "false",
            description = "The boolean value to indicate if the SOCKS server "
                    + "TCP socket is to be prepared before connecting "
                    + "(involves applying the specified socket settings, "
                    + "resolving the SOCKS server host name, and setting the "
                    + "specified timeout on waiting to connect)",
            name = "socksClient.socksServerSocket.prepareSocket",
            syntax = "socksClient.socksServerSocket.prepareSocket=true|false",
            valueType = Boolean.class
    )
    public static final PropertySpec<Boolean> SOCKS_SERVER_SOCKET_PREPARE_SOCKET =
            PROPERTY_SPECS.addThenGet(new BooleanPropertySpec(
                    "socksClient.socksServerSocket.prepareSocket",
                    Boolean.FALSE));

    /**
     * {@code PropertySpec} constant for
     * {@code socksClient.socksServerSocket.socketSettings}: the
     * {@code SocketSettings} for the SOCKS server {@code Socket}.
     */
    @NameValuePairValueSpecDoc(
            description = "The comma separated list of socket settings for the "
                    + "SOCKS server TCP socket",
            name = "socksClient.socksServerSocket.socketSettings",
            syntax = "socksClient.socksServerSocket.socketSettings=SOCKET_SETTINGS",
            valueType = SocketSettings.class
    )
    public static final PropertySpec<SocketSettings> SOCKS_SERVER_SOCKET_SOCKET_SETTINGS =
            PROPERTY_SPECS.addThenGet(new SocketSettingsPropertySpec(
                    "socksClient.socksServerSocket.socketSettings",
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
