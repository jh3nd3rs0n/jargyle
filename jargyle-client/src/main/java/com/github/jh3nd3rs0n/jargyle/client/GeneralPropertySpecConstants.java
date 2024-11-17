package com.github.jh3nd3rs0n.jargyle.client;

import com.github.jh3nd3rs0n.jargyle.client.internal.propertyspec.impl.HostPropertySpec;
import com.github.jh3nd3rs0n.jargyle.client.internal.propertyspec.impl.PortRangesPropertySpec;
import com.github.jh3nd3rs0n.jargyle.client.internal.propertyspec.impl.PositiveIntegerPropertySpec;
import com.github.jh3nd3rs0n.jargyle.client.internal.propertyspec.impl.SocketSettingsPropertySpec;
import com.github.jh3nd3rs0n.jargyle.client.internal.propertyspec.impl.SocksServerUriPropertySpec;
import com.github.jh3nd3rs0n.jargyle.common.net.Host;
import com.github.jh3nd3rs0n.jargyle.common.net.HostIpv4Address;
import com.github.jh3nd3rs0n.jargyle.common.net.PortRanges;
import com.github.jh3nd3rs0n.jargyle.common.net.SocketSettings;
import com.github.jh3nd3rs0n.jargyle.common.number.PositiveInteger;
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
                    HostIpv4Address.getAllZerosInstance()));

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
     * {@code socksClient.clientConnectTimeout}: the {@code PositiveInteger}
     * for the timeout in milliseconds on waiting for the client socket to
     * connect to the SOCKS server (default is {@code 60000}).
     */
    @NameValuePairValueSpecDoc(
            description = "The timeout in milliseconds on waiting for the "
                    + "client socket to connect to the SOCKS server "
                    + "(default is 60000)",
            name = "socksClient.clientConnectTimeout",
            syntax = "socksClient.clientConnectTimeout=POSITIVE_INTEGER",
            valueType = PositiveInteger.class
    )
    public static final PropertySpec<PositiveInteger> CLIENT_CONNECT_TIMEOUT =
            PROPERTY_SPECS.addThenGet(new PositiveIntegerPropertySpec(
                    "socksClient.clientConnectTimeout",
                    PositiveInteger.valueOf(60000))); // 1 minute

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
