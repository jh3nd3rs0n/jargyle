package com.github.jh3nd3rs0n.jargyle.protocolbase.socks5.address.impl;

import com.github.jh3nd3rs0n.jargyle.common.net.HostIpv6Address;
import com.github.jh3nd3rs0n.jargyle.common.number.UnsignedByte;
import com.github.jh3nd3rs0n.jargyle.internal.io.InputStreamHelper;
import com.github.jh3nd3rs0n.jargyle.protocolbase.socks5.Address;
import com.github.jh3nd3rs0n.jargyle.protocolbase.socks5.Socks5Exception;

import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * An IPv6 address.
 */
public final class Ipv6Address extends Address {

    /**
     * The {@code byte} value associated with this type of {@code Address}.
     */
    public static final byte ADDRESS_TYPE_BYTE_VALUE = (byte) 0x04;

    /**
     * The {@code UnsignedByte} associated with this type of {@code Address}.
     */
    public static final UnsignedByte ADDRESS_TYPE = UnsignedByte.valueOf(
            ADDRESS_TYPE_BYTE_VALUE);

    /**
     * The length of an IPv6 address in bytes.
     */
    private static final int ADDRESS_LENGTH = 16;

    /**
     * Constructs an {@code Ipv6Address} with the provided address.
     *
     * @param str the provided address
     */
    private Ipv6Address(final String str) {
        super(ADDRESS_TYPE, str);
    }

    /**
     * Returns a new {@code Ipv6Address} from the provided
     * {@code HostIpv6Address}.
     *
     * @param host the provided {@code HostIpv6Address}
     * @return a new {@code Ipv6Address} from the provided
     * {@code HostIpv6Address}
     */
    public static Ipv6Address newIpv6AddressFrom(final HostIpv6Address host) {
        InetAddress inetAddress;
        try {
            inetAddress = host.toInetAddress();
        } catch (UnknownHostException e) {
            throw new AssertionError(e);
        }
        return new Ipv6Address(inetAddress.getHostAddress());
    }

    /**
     * Returns a new {@code Ipv6Address} from the provided {@code InputStream}.
     * An {@code EOFException} is thrown if the end of the provided
     * {@code InputStream} is reached.
     *
     * @param in the provided {@code InputStream}
     * @return a new {@code Ipv6Address}
     * @throws IOException if the end of the provided {@code InputStream}
     *                     is reached ({@code EOFException}) or if an I/O
     *                     error occurs
     */
    public static Ipv6Address newIpv6AddressFrom(
            final InputStream in) throws IOException {
        byte[] bytes = new byte[ADDRESS_LENGTH];
        int bytesRead = InputStreamHelper.continuouslyReadFrom(in, bytes);
        if (bytesRead != ADDRESS_LENGTH) {
            throw new Socks5Exception(String.format(
                    "expected address length is %s. "
                            + "actual address length is %s",
                    ADDRESS_LENGTH,
                    (bytesRead == -1) ? 0 : bytesRead));
        }
        InetAddress inetAddress;
        try {
            inetAddress = InetAddress.getByAddress(bytes);
        } catch (UnknownHostException e) {
            throw new AssertionError(e);
        }
        return new Ipv6Address(inetAddress.getHostAddress());
    }

    @Override
    public byte[] getBytes() {
        InetAddress inetAddress;
        try {
            inetAddress = InetAddress.getByName(this.toString());
        } catch (UnknownHostException e) {
            throw new AssertionError(e);
        }
        return inetAddress.getAddress();
    }

}
