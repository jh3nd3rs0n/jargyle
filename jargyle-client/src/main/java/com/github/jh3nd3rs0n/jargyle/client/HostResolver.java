package com.github.jh3nd3rs0n.jargyle.client;

import java.io.IOException;
import java.net.InetAddress;

/**
 * Resolves the provided host name or address by returning an
 * {@code InetAddress} of the provided host name or address.
 */
public class HostResolver {

    /**
     * Constructs a {@code HostResolver}.
     */
    public HostResolver() {
    }

    /**
     * Resolves the provided host name or address and returns an
     * {@code InetAddress} of the provided host or address.
     * <p>
     * This default implementation of this method calls
     * {@link InetAddress#getByName(String)}.
     * </p>
     *
     * @param host the provided host name or address (can be {@code null})
     * @return an {@code InetAddress} of the provided host
     * @throws IOException if this method is unable to resolve the provided
     *                     host
     */
    public InetAddress resolve(final String host) throws IOException {
        return InetAddress.getByName(host);
    }

}
