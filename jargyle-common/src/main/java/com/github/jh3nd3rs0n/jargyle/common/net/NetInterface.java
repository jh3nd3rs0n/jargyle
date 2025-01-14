package com.github.jh3nd3rs0n.jargyle.common.net;

import com.github.jh3nd3rs0n.jargyle.internal.annotation.SingleValueTypeDoc;

import java.io.UncheckedIOException;
import java.net.Inet4Address;
import java.net.Inet6Address;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * An interface between the local system and a network. It is a wrapper of a
 * {@link java.net.NetworkInterface} and its {@link java.net.InetAddress}es
 * assigned to it.
 */
@SingleValueTypeDoc(
        description = "A name of an interface between the local system "
                + "and a network (for example, \"le0\")",
        name = "Network Interface",
        syntax = "NETWORK_INTERFACE_NAME",
        syntaxName = "NETWORK_INTERFACE"
)
public final class NetInterface {

    /**
     * The {@code List} of {@code HostAddress}es of this {@code NetInterface}.
     */
    private final List<HostAddress> hostAddresses;

    /**
     * The underlying {@code NetworkInterface} of this {@code NetInterface}.
     */
    private final NetworkInterface networkInterface;

    /**
     * Constructs a {@code NetInterface} with the provided underlying
     * {@code NetworkInterface}.
     *
     * @param netInterface the provided underlying {@code NetworkInterface}
     */
    private NetInterface(final NetworkInterface netInterface) {
        this.hostAddresses = netInterface.inetAddresses()
                .map(i -> {
                    if (i instanceof Inet6Address) {
                        return new HostIpv6Address(
                                i.getHostAddress(),
                                (Inet6Address) i);
                    }
                    return new HostIpv4Address(
                            i.getHostAddress(),
                            (Inet4Address) i);
                })
                .collect(Collectors.toList());
        this.networkInterface = netInterface;
    }

    /**
     * Returns a new {@code NetInterface} with the provided underlying
     * {@code NetworkInterface}.
     *
     * @param netInterface the provided underlying {@code NetworkInterface}
     * @return a new {@code NetInterface} with the provided underlying
     * {@code NetworkInterface}
     */
    public static NetInterface newInstance(
            final NetworkInterface netInterface) {
        return new NetInterface(Objects.requireNonNull(netInterface));
    }

    /**
     * Returns a new {@code NetInterface} from the provided {@code String} of
     * the name of the network interface. An {@code IllegalArgumentException}
     * is thrown if the provided {@code String} of the name of the network
     * interface is unknown. An {@code UncheckedIOException} is thrown if an
     * I/O error occurs in searching for the network interface.
     *
     * @param s the provided {@code String} of the name of the network
     *          interface
     * @return a new {@code NetInterface} from the provided {@code String} of
     * the name of the network interface
     */
    public static NetInterface newInstanceFrom(final String s) {
        NetworkInterface networkInterface;
        try {
            networkInterface = NetworkInterface.getByName(s);
        } catch (SocketException e) {
            throw new UncheckedIOException(e);
        }
        if (networkInterface == null) {
            throw new IllegalArgumentException(String.format(
                    "unknown network interface: '%s'", s));
        }
        return newInstance(networkInterface);
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (this.getClass() != obj.getClass()) {
            return false;
        }
        NetInterface other = (NetInterface) obj;
        return this.networkInterface.equals(other.networkInterface);
    }

    /**
     * Returns an unmodifiable {@code List} of {@code HostAddress}es of this
     * {@code NetInterface}.
     *
     * @return an unmodifiable {@code List} of {@code HostAddress}es of this
     * {@code NetInterface}
     */
    public List<HostAddress> getHostAddresses() {
        return Collections.unmodifiableList(this.hostAddresses);
    }

    /**
     * Returns an unmodifiable {@code List} of {@code HostAddress}es of this
     * {@code NetInterface} of the provided specified {@code HostAddressTypes}.
     *
     * @param hostAddressTypes the provided specified {@code HostAddressTypes}
     * @return an unmodifiable {@code List} of {@code HostAddress}es of this
     * {@code NetInterface} of the provided specified {@code HostAddressTypes}
     */
    public List<HostAddress> getHostAddresses(
            final HostAddressTypes hostAddressTypes) {
        return Collections.unmodifiableList(this.hostAddresses.stream()
                .filter(h -> hostAddressTypes.firstDescribes(h) != null)
                .collect(Collectors.toList()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + this.networkInterface.hashCode();
        return result;
    }

    /**
     * Returns the underlying {@code NetworkInterface} of this
     * {@code NetInterface}.
     *
     * @return the underlying {@code NetworkInterface} of this
     * {@code NetInterface}
     */
    public NetworkInterface toNetworkInterface() {
        return this.networkInterface;
    }

    /**
     * Returns the {@code String} representation of this {@code NetInterface}.
     * The {@code String} representation is the name of the underlying
     * {@code NetworkInterface} of this {@code NetInterface}.
     *
     * @return the {@code String} representation of this {@code NetInterface}
     */
    @Override
    public String toString() {
        return this.networkInterface.getName();
    }

}
