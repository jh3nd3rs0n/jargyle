package com.github.jh3nd3rs0n.jargyle.protocolbase.socks5.address.impl;

import com.github.jh3nd3rs0n.jargyle.common.net.HostName;
import com.github.jh3nd3rs0n.jargyle.common.number.UnsignedByte;
import com.github.jh3nd3rs0n.jargyle.internal.io.InputStreamHelper;
import com.github.jh3nd3rs0n.jargyle.protocolbase.internal.UnsignedByteIoHelper;
import com.github.jh3nd3rs0n.jargyle.protocolbase.socks5.Address;
import com.github.jh3nd3rs0n.jargyle.protocolbase.socks5.Socks5Exception;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

/**
 * A fully-qualified domain name.
 */
public final class DomainName extends Address {

    /**
     * The {@code byte} value associated with this type of {@code Address}.
     */
    public static final byte ADDRESS_TYPE_BYTE_VALUE = (byte) 0x03;

    /**
     * The {@code UnsignedByte} associated with this type of {@code Address}.
     */
    public static final UnsignedByte ADDRESS_TYPE = UnsignedByte.valueOf(
            ADDRESS_TYPE_BYTE_VALUE);

    /**
     * The {@code Charset} used to encode/decode the domain name as a
     * {@code String} in {@code byte}s.
     */
    private static final Charset CHARSET = StandardCharsets.US_ASCII;

    /**
     * Constructs a {@code DomainName} with the provided name.
     *
     * @param str the provided name
     */
    private DomainName(final String str) {
        super(ADDRESS_TYPE, str);
    }

    /**
     * Returns a new {@code DomainName} from the provided {@code HostName}. An
     * {@code IllegalArgumentException} is thrown if the length in bytes of
     * the {@code String} representation of the provided {@code HostName} is
     * greater than 255.
     *
     * @param host the provided {@code HostName}
     * @return a new {@code DomainName} from the provided {@code HostName}
     */
    public static DomainName newDomainNameFrom(final HostName host) {
        String string = host.toString();
        byte[] stringBytes = string.getBytes(CHARSET);
        if (stringBytes.length > UnsignedByte.MAX_INT_VALUE) {
            throw new IllegalArgumentException(String.format(
                    "expected address length in bytes must be no more than " +
                            "%s. actual address length in bytes is %s",
                    UnsignedByte.MAX_INT_VALUE,
                    stringBytes.length));
        }
        return new DomainName(string);
    }

    /**
     * Returns a new {@code DomainName} from the provided {@code InputStream}.
     * An {@code EOFException} is thrown if the end of the provided
     * {@code InputStream} is reached.
     *
     * @param in the provided {@code InputStream}
     * @return a new {@code DomainName}
     * @throws IOException if the end of the provided {@code InputStream}
     *                     is reached ({@code EOFException}) or if an I/O
     *                     error occurs
     */
    public static DomainName newDomainNameFrom(
            final InputStream in) throws IOException {
        UnsignedByte octetCount =
                UnsignedByteIoHelper.readUnsignedByteFrom(in);
        byte[] stringBytes = new byte[octetCount.intValue()];
        if (octetCount.intValue() > 0) {
            int bytesRead = InputStreamHelper.continuouslyReadFrom(in, stringBytes);
            if (octetCount.intValue() != bytesRead) {
                throw new Socks5Exception(String.format(
                        "expected address length is %s. "
                                + "actual address length is %s",
                        octetCount.intValue(),
                        (bytesRead == -1) ? 0 : bytesRead));
            }
        }
        String string = new String(stringBytes, CHARSET);
        try {
            HostName.newHostName(string);
        } catch (IllegalArgumentException e) {
            throw new Socks5Exception(String.format(
                    "invalid domain name: '%s'",
                    string));
        }
        return new DomainName(string);
    }

    @Override
    public byte[] getBytes() {
        byte[] stringBytes = this.toString().getBytes(CHARSET);
        UnsignedByte octetCount = UnsignedByte.valueOf(stringBytes.length);
        byte[] bytes = new byte[1 + octetCount.intValue()];
        bytes[0] = octetCount.byteValue();
        System.arraycopy(stringBytes, 0, bytes, 1, octetCount.intValue());
        return bytes;
    }

}
