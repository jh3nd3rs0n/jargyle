package com.github.jh3nd3rs0n.jargyle.common.net;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * An IPv4 address of a node of a network.
 */
public final class HostIpv4Address extends HostAddress {

    /**
     * The all zeros IPv4 address (as 4 parts).
     */
    public static final String ALL_ZEROS_IPV4_ADDRESS = "0.0.0.0";

    /**
     * The regular expression for an all zeros IPv4 address as 1 part.
     */
    private static final String ALL_ZEROS_IPV4_ADDRESS_AS_1_PART_REGEX =
            "\\A0{1,10}\\z";

    /**
     * The regular expression for an all zeros IPv4 address as 2 parts.
     */
    private static final String ALL_ZEROS_IPV4_ADDRESS_AS_2_PARTS_REGEX =
            "\\A0{1,3}\\.0{1,8}\\z";

    /**
     * The regular expression for an all zeros IPv4 address as 3 parts.
     */
    private static final String ALL_ZEROS_IPV4_ADDRESS_AS_3_PARTS_REGEX =
            "\\A0{1,3}\\.0{1,3}\\.0{1,5}\\z";

    /**
     * The regular expression for an all zeros IPv4 address as 4 parts.
     */
    private static final String ALL_ZEROS_IPV4_ADDRESS_AS_4_PARTS_REGEX =
            "\\A0{1,3}(\\.0{1,3}){3}+\\z";

    /**
     * The regular expression for an IPv4 address as 1 part.
     */
    private static final String IPV4_ADDRESS_AS_1_PART_REGEX = "\\A\\d{1,10}\\z";

    /**
     * The regular expression for an IPv4 address as 2 parts.
     */
    private static final String IPV4_ADDRESS_AS_2_PARTS_REGEX =
            "\\A\\d{1,3}\\.\\d{1,8}\\z";

    /**
     * The regular expression for an IPv4 address as 3 parts.
     */
    private static final String IPV4_ADDRESS_AS_3_PARTS_REGEX =
            "\\A\\d{1,3}\\.\\d{1,3}\\.\\d{1,5}\\z";

    /**
     * The regular expression for an IPv4 address as 4 parts.
     */
    private static final String IPV4_ADDRESS_AS_4_PARTS_REGEX =
            "\\A\\d{1,3}(\\.\\d{1,3}){3}+\\z";

    /**
     * The {@code InetAddress} of an all zeros IPv4 address.
     */
    private static InetAddress allZerosInetAddress;

    /**
     * The {@code HostIpv4Address} of an all zeros IPv4 address.
     */
    private static HostIpv4Address allZerosInstance;

    /**
     * Constructs a {@code HostIpv4Address} of the provided IPv4 address
     * and the provided {@code InetAddress} of the provided IPv4 address.
     *
     * @param str      the provided IPv4 address
     * @param inetAddr the provided {@code InetAddress} of the provided
     *                 IPv4 address
     */
    HostIpv4Address(final String str, final InetAddress inetAddr) {
        super(str, inetAddr);
    }

    /**
     * Returns a {@code HostIpv4Address} of an all zeros IPv4 address.
     *
     * @return a {@code HostIpv4Address} of an all zeros IPv4 address
     */
    public static HostIpv4Address getAllZerosInstance() {
        if (allZerosInstance == null) {
            allZerosInstance = new HostIpv4Address(
                    ALL_ZEROS_IPV4_ADDRESS, getAllZerosInetAddress());
        }
        return allZerosInstance;
    }

    /**
     * Returns an {@code InetAddress} of an all zeros IPv4 address.
     *
     * @return an {@code InetAddress} of an all zeros IPv4 address
     */
    public static InetAddress getAllZerosInetAddress() {
        if (allZerosInetAddress == null) {
            try {
                allZerosInetAddress = InetAddress.getByName(
                        ALL_ZEROS_IPV4_ADDRESS);
            } catch (UnknownHostException e) {
                throw new AssertionError(e);
            }
        }
        return allZerosInetAddress;
    }

    /**
     * Returns a {@code boolean} value to indicate if the provided
     * IPv4 address is an IPv4 address of all zeros.
     *
     * @param string the provided IPv4 address
     * @return a {@code boolean} value to indicate if the provided
     * IPv4 address is an IPv4 address of all zeros
     */
    public static boolean isAllZerosIpv4Address(final String string) {
        return string.matches(ALL_ZEROS_IPV4_ADDRESS_AS_4_PARTS_REGEX)
                || string.matches(ALL_ZEROS_IPV4_ADDRESS_AS_3_PARTS_REGEX)
                || string.matches(ALL_ZEROS_IPV4_ADDRESS_AS_2_PARTS_REGEX)
                || string.matches(ALL_ZEROS_IPV4_ADDRESS_AS_1_PART_REGEX);
    }

    /**
     * Returns a new {@code HostIpv4Address} of the provided IPv4 address.
     * An {@code IllegalArgumentException} is thrown if the provided
     * IPv4 address is invalid.
     *
     * @param string the provided IPv4 address
     * @return a new {@code HostIpv4Address} of the provided IPv4 address
     */
    public static HostIpv4Address newHostIpv4AddressOf(final String string) {
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
        return new HostIpv4Address(string, inetAddress);
    }

}
