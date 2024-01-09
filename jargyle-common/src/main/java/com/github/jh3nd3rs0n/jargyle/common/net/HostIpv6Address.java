package com.github.jh3nd3rs0n.jargyle.common.net;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * An IPv6 address of a node of a network.
 */
public final class HostIpv6Address extends HostAddress {

    /**
     * The regular expression for an all zeros IPv6 address in compressed form.
     */
    private static final String ALL_ZEROS_IPV6_ADDRESS_IN_COMPRESSED_FORM_REGEX =
            "\\A(0{0,4}:){1,7}(:0{0,4}){1,7}\\z";

    /**
     * The regular expression for an all zeros IPv6 address in full form.
     */
    private static final String ALL_ZEROS_IPV6_ADDRESS_IN_FULL_FORM_REGEX =
            "\\A0{1,4}(:0{1,4}){7}+\\z";

    /**
     * The regular expression for an IPv6 address in compressed form.
     */
    private static final String IPV6_ADDRESS_IN_COMPRESSED_FORM_REGEX =
            "\\A([a-fA-F0-9]{0,4}:){1,7}(:[a-fA-F0-9]{0,4}){1,7}\\z";

    /**
     * The regular expression for an IPv6 address in full form.
     */
    private static final String IPV6_ADDRESS_IN_FULL_FORM_REGEX =
            "\\A[a-fA-F0-9]{1,4}(:[a-fA-F0-9]{1,4}){7}+\\z";

    /**
     * Constructs a {@code HostIpv6Address} of the provided IPv6 address
     * and the provided {@code InetAddress} of the provided IPv6 address.
     *
     * @param str      the provided IPv6 address
     * @param inetAddr the provided {@code InetAddress} of the provided
     *                 IPv6 address
     */
    HostIpv6Address(final String str, final InetAddress inetAddr) {
        super(str, inetAddr);
    }

    /**
     * Returns a {@code boolean} value to indicate if the provided
     * IPv6 address is an IPv6 address of all zeros.
     *
     * @param string the provided IPv6 address
     * @return a {@code boolean} value to indicate if the provided
     * IPv6 address is an IPv6 address of all zeros
     */
    public static boolean isAllZerosIpv6Address(final String string) {
        return string.matches(ALL_ZEROS_IPV6_ADDRESS_IN_FULL_FORM_REGEX)
                || (string.matches(ALL_ZEROS_IPV6_ADDRESS_IN_COMPRESSED_FORM_REGEX)
                && string.split("::").length <= 2
                && string.split(":").length <= 8);
    }

    /**
     * Returns a new {@code HostIpv6Address} of the provided IPv6 address.
     * An {@code IllegalArgumentException} is thrown if the provided
     * IPv6 address is invalid.
     *
     * @param string the provided IPv6 address
     * @return a new {@code HostIpv6Address} of the provided IPv6 address
     */
    public static HostIpv6Address newHostIpv6AddressOf(final String string) {
        String message = String.format(
                "invalid IPv6 address: %s",
                string);
        if (!(string.matches(IPV6_ADDRESS_IN_FULL_FORM_REGEX)
                || (string.matches(IPV6_ADDRESS_IN_COMPRESSED_FORM_REGEX)
                && string.split("::").length <= 2
                && string.split(":").length <= 8))) {
            throw new IllegalArgumentException(message);
        }
        InetAddress inetAddress;
        try {
            inetAddress = InetAddress.getByName(string);
        } catch (UnknownHostException e) {
            throw new IllegalArgumentException(message, e);
        }
        return new HostIpv6Address(string, inetAddress);
    }

}
