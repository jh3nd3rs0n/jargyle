package com.github.jh3nd3rs0n.jargyle.client;

import com.github.jh3nd3rs0n.jargyle.client.internal.propertyspec.impl.HostPropertySpec;
import com.github.jh3nd3rs0n.jargyle.client.internal.propertyspec.impl.PortPropertySpec;
import com.github.jh3nd3rs0n.jargyle.client.internal.propertyspec.impl.SchemePropertySpec;
import com.github.jh3nd3rs0n.jargyle.common.net.Host;
import com.github.jh3nd3rs0n.jargyle.common.net.Port;
import com.github.jh3nd3rs0n.jargyle.internal.annotation.NameValuePairValueSpecDoc;
import com.github.jh3nd3rs0n.jargyle.internal.annotation.NameValuePairValueSpecsDoc;

import java.util.List;
import java.util.Map;

/**
 * SOCKS server URI specific {@code PropertySpec} constants.
 */
@NameValuePairValueSpecsDoc(
        description = "SOCKS server URI specific properties",
        name = "SOCKS Server URI Properties"
)
public final class SocksServerUriPropertySpecConstants {

    /**
     * The {@code PropertySpecs} of the {@code PropertySpec} constants of
     * this class.
     */
    private static final PropertySpecs PROPERTY_SPECS = new PropertySpecs();

    /**
     * {@code PropertySpec} constant for {@code socksServerUri.port}: the
     * {@code Port} for the port of the SOCKS server URI.
     */
    @NameValuePairValueSpecDoc(
            description = "The port of the SOCKS server URI",
            name = "socksServerUri.port",
            syntax = "socksServerUri.port=PORT",
            valueType = Port.class
    )
    public static final PropertySpec<Port> PORT =
            PROPERTY_SPECS.addThenGet(new PortPropertySpec(
                    "socksServerUri.port",
                    null));

    /**
     * {@code PropertySpec} constant for {@code socksServerUri.host}: the
     * {@code Host} for the host name or address of the SOCKS server URI.
     */
    @NameValuePairValueSpecDoc(
            description = "The host name or address of the SOCKS server URI",
            name = "socksServerUri.host",
            syntax = "socksServerUri.host=HOST",
            valueType = Host.class
    )
    public static final PropertySpec<Host> HOST =
            PROPERTY_SPECS.addThenGet(new HostPropertySpec(
                    "socksServerUri.host",
                    null));

    /**
     * {@code PropertySpec} constant for {@code socksServerUri.scheme}: the
     * {@code Scheme} for the scheme of the SOCKS server URI.
     */
    @NameValuePairValueSpecDoc(
            description = "The scheme of the SOCKS server URI",
            name = "socksServerUri.scheme",
            syntax = "socksServerUri.scheme=SCHEME",
            valueType = Scheme.class
    )
    public static final PropertySpec<Scheme> SCHEME =
            PROPERTY_SPECS.addThenGet(new SchemePropertySpec(
                    "socksServerUri.scheme",
                    null));

    /**
     * Prevents the construction of unnecessary instances.
     */
    private SocksServerUriPropertySpecConstants() {
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
