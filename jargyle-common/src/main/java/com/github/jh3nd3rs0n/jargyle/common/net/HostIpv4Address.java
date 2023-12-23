package com.github.jh3nd3rs0n.jargyle.common.net;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.UnknownHostException;

public class HostIpv4Address extends HostAddress {

    public static final String ALL_ZEROS_IPV4_ADDRESS_AS_1_PART = "0";

    public static final String ALL_ZEROS_IPV4_ADDRESS_AS_2_PARTS = "0.0";

    public static final String ALL_ZEROS_IPV4_ADDRESS_AS_3_PARTS = "0.0.0";

    public static final String ALL_ZEROS_IPV4_ADDRESS_AS_4_PARTS = "0.0.0.0";

    public static final String ALL_ZEROS_IPV4_ADDRESS = ALL_ZEROS_IPV4_ADDRESS_AS_4_PARTS;

    private static final String IPV4_ADDRESS_AS_1_PART_REGEX = "\\A\\d{1,10}\\z";

    private static final String IPV4_ADDRESS_AS_2_PARTS_REGEX =
            "\\A\\d{1,3}\\.\\d{1,8}\\z";

    private static final String IPV4_ADDRESS_AS_3_PARTS_REGEX =
            "\\A\\d{1,3}\\.\\d{1,3}\\.\\d{1,5}\\z";

    private static final String IPV4_ADDRESS_AS_4_PARTS_REGEX =
            "\\A\\d{1,3}(\\.\\d{1,3}){3}+\\z";

    private static InetAddress allZerosInetAddress;

    private static HostIpv4Address allZerosInstance;

    HostIpv4Address(String str, InetAddressFactory inetAddrFactory) {
        super(str, inetAddrFactory);
    }

    public static HostIpv4Address getAllZerosInstance() {
        if (allZerosInstance == null) {
            allZerosInstance = new HostIpv4Address(
                    ALL_ZEROS_IPV4_ADDRESS,
                    new HostAddress.InetAddressFactoryImpl(
                            getAllZerosInetAddress()));
        }
        return allZerosInstance;
    }

    public static InetAddress getAllZerosInetAddress() {
        if (allZerosInetAddress == null) {
            try {
                allZerosInetAddress = InetAddress.getByName(ALL_ZEROS_IPV4_ADDRESS);
            } catch (UnknownHostException e) {
                throw new AssertionError(e);
            }
        }
        return allZerosInetAddress;
    }

    public static boolean isAllZerosIpv4Address(final String string) {
        return ALL_ZEROS_IPV4_ADDRESS_AS_4_PARTS.equals(string)
                || ALL_ZEROS_IPV4_ADDRESS_AS_3_PARTS.equals(string)
                || ALL_ZEROS_IPV4_ADDRESS_AS_2_PARTS.equals(string)
                || ALL_ZEROS_IPV4_ADDRESS_AS_1_PART.equals(string);
    }

    public static HostIpv4Address newHostIpv4Address(final String string) {
        String message = String.format(
                "invalid IPv4 address: %s",
                string);
        if (!(string.matches(IPV4_ADDRESS_AS_4_PARTS_REGEX)
                || string.matches(IPV4_ADDRESS_AS_3_PARTS_REGEX)
                || string.matches(IPV4_ADDRESS_AS_2_PARTS_REGEX)
                || string.matches(IPV4_ADDRESS_AS_1_PART_REGEX))) {
            throw new IllegalArgumentException(message);
        }
        InetAddress inetAddress;
        try {
            inetAddress = InetAddress.getByName(string);
        } catch (UnknownHostException e) {
            throw new IllegalArgumentException(message, e);
        }
        if (!(inetAddress instanceof Inet4Address)) {
            throw new IllegalArgumentException(message);
        }
        return new HostIpv4Address(
                string, new HostAddress.InetAddressFactoryImpl(inetAddress));
    }
}
