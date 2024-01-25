package com.github.jh3nd3rs0n.jargyle.common.net;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Objects;

/**
 * An address of a node of a network.
 */
public abstract class HostAddress extends Host {

    /**
     * The {@code InetAddress} of this {@code Host}.
     */
    private final InetAddress inetAddress;

    /**
     * Constructs a {@code HostAddress} of the provided address and
     * the provided {@code InetAddress} of the provided address.
     *
     * @param str      the provided address
     * @param inetAddr the provided {@code InetAddress} of the provided
     *                 address
     */
    HostAddress(final String str, final InetAddress inetAddr) {
        super(str);
        this.inetAddress = Objects.requireNonNull(inetAddr);
    }

    /**
     * Returns a {@code boolean} value to indicate if the provided
     * address is an IP address of all zeros.
     *
     * @param string the provided address
     * @return a {@code boolean} value to indicate if the provided
     * {@code String} is an IP address of all zeros
     */
    public static boolean isAllZerosHostAddress(final String string) {
        return HostIpv4Address.isAllZerosIpv4Address(string)
                || HostIpv6Address.isAllZerosIpv6Address(string);
    }

    /**
     * Returns a new {@code HostAddress} of the provided address. An
     * {@code IllegalArgumentException} is thrown if the provided
     * address is invalid.
     *
     * @param string the provided address
     * @return a new {@code HostAddress} of the provided address
     */
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

    @Override
    public final boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (this.getClass() != obj.getClass()) {
            return false;
        }
        HostAddress other = (HostAddress) obj;
        return this.inetAddress.equals(other.inetAddress);
    }

    public final int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + this.inetAddress.hashCode();
        return result;
    }

    @Override
    public final InetAddress toInetAddress() throws UnknownHostException {
        return this.inetAddress;
    }

}
