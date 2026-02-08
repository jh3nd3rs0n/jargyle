package com.github.jh3nd3rs0n.jargyle.client;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * Default implementation of {@code SocketFactory}. This implementation
 * creates plain {@code Socket}s.
 */
final class DefaultSocketFactory extends SocketFactory {

    private static final DefaultSocketFactory INSTANCE = 
            new DefaultSocketFactory();

    /**
     * Prevents the construction of additional instances.
     */
    private DefaultSocketFactory() { }

    /**
     * Returns the instance of {@code DefaultSocketFactory}.
     *
     * @return the instance of {@code DefaultSocketFactory}
     */
    public static DefaultSocketFactory getInstance() {
        return INSTANCE;
    }

    @Override
    public Socket newSocket() {
        return new Socket();
    }

    @Override
    public Socket newSocket(
            final InetAddress address, final int port) throws IOException {
        return new Socket(address, port);
    }

    @Override
    public Socket newSocket(
            final InetAddress address,
            final int port,
            final InetAddress localAddr,
            final int localPort) throws IOException {
        return new Socket(address, port, localAddr, localPort);
    }

    @Override
    public Socket newSocket(
            final String host,
            final int port) throws UnknownHostException, IOException {
        return new Socket(host, port);
    }

    @Override
    public Socket newSocket(
            final String host,
            final int port,
            final InetAddress localAddr,
            final int localPort) throws IOException {
        return new Socket(host, port, localAddr, localPort);
    }

}
