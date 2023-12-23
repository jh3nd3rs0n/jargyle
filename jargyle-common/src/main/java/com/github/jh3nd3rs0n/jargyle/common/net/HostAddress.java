package com.github.jh3nd3rs0n.jargyle.common.net;

import java.net.InetAddress;
import java.net.UnknownHostException;

public abstract class HostAddress extends Host {

    static final class InetAddressFactoryImpl implements InetAddressFactory {

        private final InetAddress inetAddress;

        public InetAddressFactoryImpl(final InetAddress inetAddr) {
            this.inetAddress = inetAddr;
        }

        @Override
        public InetAddress getInetAddress() throws UnknownHostException {
            return this.inetAddress;
        }

    }

    HostAddress(final String str, final InetAddressFactory inetAddrFactory) {
        super(str, inetAddrFactory);
    }

    public static boolean isAllZerosHostAddress(final String string) {
        return HostIpv4Address.isAllZerosIpv4Address(string)
                || HostIpv6Address.isAllZerosIpv6Address(string);
    }

    public static HostAddress newHostAddress(final String string) {
        try {
            return HostIpv4Address.newHostIpv4Address(string);
        } catch (IllegalArgumentException ignored) {
        }
        try {
            return HostIpv6Address.newHostIpv6Address(string);
        } catch (IllegalArgumentException ignored) {
        }
        throw new IllegalArgumentException(String.format(
                "invalid host address: %s",
                string));
    }

}
