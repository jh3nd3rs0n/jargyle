package com.github.jh3nd3rs0n.jargyle.protocolbase.socks5;

import com.github.jh3nd3rs0n.jargyle.common.net.Host;
import com.github.jh3nd3rs0n.jargyle.common.net.HostIpv4Address;
import com.github.jh3nd3rs0n.jargyle.common.net.HostIpv6Address;
import com.github.jh3nd3rs0n.jargyle.common.net.HostName;
import com.github.jh3nd3rs0n.jargyle.common.number.UnsignedByte;
import com.github.jh3nd3rs0n.jargyle.internal.annotation.SingleValueTypeDoc;
import com.github.jh3nd3rs0n.jargyle.protocolbase.internal.UnsignedByteIoHelper;
import com.github.jh3nd3rs0n.jargyle.protocolbase.socks5.address.impl.DomainName;
import com.github.jh3nd3rs0n.jargyle.protocolbase.socks5.address.impl.Ipv4Address;
import com.github.jh3nd3rs0n.jargyle.protocolbase.socks5.address.impl.Ipv6Address;

import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;

/**
 * An identifier or a virtual location of a node of a network.
 */
@SingleValueTypeDoc(
        description = "An identifier or a virtual location of a node of a " +
                "network",
        name = "SOCKS5 Address",
        syntax = "DOMAIN_NAME|IPV4_ADDRESS|IPV6_ADDRESS",
        syntaxName = "SOCKS5_ADDRESS"
)
public abstract class Address {

    /**
     * The {@code UnsignedByte} associated with this type of {@code Address}.
     */
    private final UnsignedByte addressType;

    /**
     * The {@code String} representation of this {@code Address}.
     */
    private final String string;

    /**
     * Constructs an {@code Address} with the provided {@code UnsignedByte}
     * associated with this type of {@code Address} and the provided
     * name or address.
     *
     * @param addrType the provided {@code UnsignedByte} associated with this
     *                 type of {@code Address}
     * @param str      the provided name or address
     */
    public Address(final UnsignedByte addrType, final String str) {
        this.addressType = Objects.requireNonNull(addrType);
        this.string = Objects.requireNonNull(str);
    }

    /**
     * Returns a new {@code Address} from the provided {@code InputStream}. An
     * {@code EOFException} is thrown if the end of the provided
     * {@code InputStream} is reached.
     *
     * @param in the provided {@code InputStream}
     * @return a new {@code Address}
     * @throws IOException if the end of the provided {@code InputStream}
     *                     is reached ({@code EOFException}) or if an I/O
     *                     error occurs
     */
    public static Address newInstanceFrom(
            final InputStream in) throws IOException {
        UnsignedByte b = UnsignedByteIoHelper.readUnsignedByteFrom(in);
        switch (b.byteValue()) {
            case Ipv4Address.ADDRESS_TYPE_BYTE_VALUE:
                return Ipv4Address.newIpv4AddressFrom(in);
            case DomainName.ADDRESS_TYPE_BYTE_VALUE:
                return DomainName.newDomainNameFrom(in);
            case Ipv6Address.ADDRESS_TYPE_BYTE_VALUE:
                return Ipv6Address.newIpv6AddressFrom(in);
            default:
                throw new AddressTypeNotSupportedException(b);
        }
    }

    /**
     * Returns a new {@code Address} from the provided {@code String}. An
     * {@code IllegalArgumentException} is thrown if the provided
     * {@code String} is invalid.
     *
     * @param string the provided {@code String}
     * @return a new {@code Address}
     */
    public static Address newInstanceFrom(final String string) {
        Host host = Host.newInstance(string);
        if (host instanceof HostIpv4Address) {
            return Ipv4Address.newIpv4AddressFrom((HostIpv4Address) host);
        }
        if (host instanceof HostName) {
            return DomainName.newDomainNameFrom((HostName) host);
        }
        if (host instanceof HostIpv6Address) {
            return Ipv6Address.newIpv6AddressFrom((HostIpv6Address) host);
        }
        throw new IllegalArgumentException(String.format(
                "unable to determine address type for the specified address: %s",
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
        Address other = (Address) obj;
        return this.string.equals(other.string);
    }

    /**
     * Returns the {@code UnsignedByte} associated with this type of
     * {@code Address}.
     *
     * @return the {@code UnsignedByte} associated with this type of
     * {@code Address}
     */
    public final UnsignedByte getAddressType() {
        return this.addressType;
    }

    /**
     * Returns the {@code byte}s of this {@code Address}.
     *
     * @return the {@code byte}s of this {@code Address}
     */
    public abstract byte[] getBytes();

    @Override
    public final int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + this.addressType.hashCode();
        result = prime * result + this.string.hashCode();
        return result;
    }

    /**
     * Returns the {@code byte} array of the {@code byte} value associated
     * with this type of {@code Address} followed by the {@code byte}s of this
     * {@code Address}.
     *
     * @return the {@code byte} array of the {@code byte} value associated
     * with this type of {@code Address} followed by the {@code byte}s of this
     * {@code Address}
     */
    public final byte[] toByteArray() {
        byte[] bytes = this.getBytes();
        byte[] arr = new byte[1 + bytes.length];
        arr[0] = this.addressType.byteValue();
        System.arraycopy(bytes, 0, arr, 1, bytes.length);
        return arr;
    }

    /**
     * Returns the {@code String} representation of this {@code Address}.
     * If this {@code Address} is an IPv4 address or an IPv6 address, the
     * {@code String} representation will consist of the full form of this
     * {@code Address}.
     *
     * @return the {@code String} representation of this {@code Address}
     */
    @Override
    public final String toString() {
        return this.string;
    }

}
