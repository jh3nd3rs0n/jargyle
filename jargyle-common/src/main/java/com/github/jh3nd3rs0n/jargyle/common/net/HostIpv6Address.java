package com.github.jh3nd3rs0n.jargyle.common.net;

import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.UnknownHostException;

public class HostIpv6Address extends HostAddress {

    public static final String ALL_ZEROS_IPV6_ADDRESS_IN_COMPRESSED_FORM = "::";

    public static final String ALL_ZEROS_IPV6_ADDRESS_IN_FULL_FORM = "0:0:0:0:0:0:0:0";

    private static final String IPV6_ADDRESS_IN_COMPRESSED_FORM_REGEX =
            "\\A([a-fA-F0-9]{0,4}:){1,7}(:[a-fA-F0-9]{0,4}){1,7}\\z";

    private static final String IPV6_ADDRESS_IN_FULL_FORM_REGEX =
            "\\A[a-fA-F0-9]{1,4}(:[a-fA-F0-9]{1,4}){7}+\\z";

    HostIpv6Address(String str, InetAddressFactory inetAddrFactory) {
        super(str, inetAddrFactory);
    }

    public static boolean isAllZerosIpv6Address(final String string) {
        return ALL_ZEROS_IPV6_ADDRESS_IN_COMPRESSED_FORM.equals(string)
                || ALL_ZEROS_IPV6_ADDRESS_IN_FULL_FORM.equals(string);
    }

    public static HostIpv6Address newHostIpv6Address(final String string) {
        String message = String.format(
                "invalid IPv6 address: %s",
                string);
        if (!(string.matches(IPV6_ADDRESS_IN_COMPRESSED_FORM_REGEX)
                || string.matches(IPV6_ADDRESS_IN_FULL_FORM_REGEX))) {
            throw new IllegalArgumentException(message);
        }
        InetAddress inetAddress;
        try {
            inetAddress = InetAddress.getByName(string);
        } catch (UnknownHostException e) {
            throw new IllegalArgumentException(message, e);
        }
        if (!(inetAddress instanceof Inet6Address)) {
            throw new IllegalArgumentException(message);
        }
        return new HostIpv6Address(
                string, new HostAddress.InetAddressFactoryImpl(inetAddress));
    }
}
