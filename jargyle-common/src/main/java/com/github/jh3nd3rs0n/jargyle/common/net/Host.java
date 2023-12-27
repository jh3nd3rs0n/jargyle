package com.github.jh3nd3rs0n.jargyle.common.net;

import com.github.jh3nd3rs0n.jargyle.internal.annotation.SingleValueTypeDoc;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Objects;

/**
 * A name or address of a node of a network.
 */
@SingleValueTypeDoc(
        description = "A name or address of a node of a network",
        name = "Host",
        syntax = "HOST_NAME|HOST_ADDRESS",
        syntaxName = "HOST"
)
public abstract class Host {

    /**
     * The {@code String} representation of this {@code Host}.
     */
    final String string;

    /**
     * Constructs a {@code Host} with the provided name or address.
     *
     * @param str the provided name or address
     */
    Host(final String str) {
        this.string = Objects.requireNonNull(str);
    }

    /**
     * Returns a new {@code Host} with the provided name or address. A
     * {@code IllegalArgumentException} is thrown if the provided name or
     * address is invalid.
     *
     * @param s the provided name or address
     * @return a new {@code Host} with the provided name or address
     */
    public static Host newInstance(final String s) {
        try {
            return HostAddress.newHostAddress(s);
        } catch (IllegalArgumentException ignored) {
        }
        try {
            return HostName.newHostName(s);
        } catch (IllegalArgumentException ignored) {
        }
        throw new IllegalArgumentException(String.format(
                "invalid host name or address: %s",
                s));
    }

    @Override
    public abstract boolean equals(final Object obj);

    @Override
    public abstract int hashCode();

    /**
     * Returns an {@code InetAddress} of this {@code Host}.
     *
     * @return an {@code InetAddress} of this {@code Host}
     * @throws UnknownHostException if the IP address cannot be determined
     *                              from the {@code String} representation of
	 *                              this {@code Host}
     */
    public abstract InetAddress toInetAddress() throws UnknownHostException;

    @Override
    public final String toString() {
        return this.string;
    }

}
