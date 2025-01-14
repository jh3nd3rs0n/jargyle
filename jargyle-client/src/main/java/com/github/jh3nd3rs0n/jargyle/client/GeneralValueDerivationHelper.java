package com.github.jh3nd3rs0n.jargyle.client;

import com.github.jh3nd3rs0n.jargyle.common.net.Host;
import com.github.jh3nd3rs0n.jargyle.common.net.HostAddressTypes;
import com.github.jh3nd3rs0n.jargyle.common.net.HostIpv4Address;
import com.github.jh3nd3rs0n.jargyle.common.net.NetInterface;

/**
 * Helper class for deriving general specific values from {@code Properties}.
 */
final class GeneralValueDerivationHelper {

    /**
     * Prevents the construction of unnecessary instances.
     */
    private GeneralValueDerivationHelper() {
    }

    public static Host getClientBindHostFrom(final Properties properties) {
        Host host = properties.getValue(
                GeneralPropertySpecConstants.CLIENT_BIND_HOST);
        if (host != null) {
            return host;
        }
        NetInterface netInterface = getClientNetInterfaceFrom(properties);
        if (netInterface != null) {
            return netInterface.getHostAddresses(
                    getClientBindHostAddressTypesFrom(properties))
                    .stream()
                    .findFirst()
                    .orElse(HostIpv4Address.getAllZerosInstance());
        }
        return HostIpv4Address.getAllZerosInstance();
    }

    private static HostAddressTypes getClientBindHostAddressTypesFrom(
            final Properties properties) {
        return properties.getValue(
                GeneralPropertySpecConstants.CLIENT_BIND_HOST_ADDRESS_TYPES);
    }

    private static NetInterface getClientNetInterfaceFrom(
            final Properties properties) {
        return properties.getValue(
                GeneralPropertySpecConstants.CLIENT_NET_INTERFACE);
    }

}
