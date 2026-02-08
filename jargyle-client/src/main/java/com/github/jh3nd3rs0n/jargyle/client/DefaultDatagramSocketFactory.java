package com.github.jh3nd3rs0n.jargyle.client;

import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketAddress;
import java.net.SocketException;

/**
 * Default implementation of {@code DatagramSocketFactory}. This implementation
 * creates plain {@code DatagramSocket}s.
 */
final class DefaultDatagramSocketFactory extends DatagramSocketFactory {

    private static final DefaultDatagramSocketFactory INSTANCE =
            new DefaultDatagramSocketFactory();

    /**
     * Prevents the construction of additional instances.
     */
    private DefaultDatagramSocketFactory() { }

    /**
     * Returns the instance of {@code DefaultDatagramSocketFactory}.
     *
     * @return the instance of {@code DefaultDatagramSocketFactory}
     */
    public static DefaultDatagramSocketFactory getInstance() {
        return INSTANCE;
    }

    @Override
    public DatagramSocket newDatagramSocket() throws SocketException {
        return new DatagramSocket();
    }

    @Override
    public DatagramSocket newDatagramSocket(
            final int port) throws SocketException {
        return new DatagramSocket(port);
    }

    @Override
    public DatagramSocket newDatagramSocket(
            final int port, final InetAddress laddr) throws SocketException {
        return new DatagramSocket(port, laddr);
    }

    @Override
    public DatagramSocket newDatagramSocket(
            final SocketAddress bindaddr) throws SocketException {
        return new DatagramSocket(bindaddr);
    }

}
