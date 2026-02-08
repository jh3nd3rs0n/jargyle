package com.github.jh3nd3rs0n.jargyle.client;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;

/**
 * Default implementation of {@code ServerSocketFactory}. This implementation
 * creates plain {@code ServerSocket}s.
 */
final class DefaultServerSocketFactory extends ServerSocketFactory {

    private static final DefaultServerSocketFactory INSTANCE = 
            new DefaultServerSocketFactory();

    /**
     * Prevents the construction of additional instances.
     */    
    private DefaultServerSocketFactory() { }

    /**
     * Returns the instance of {@code DefaultServerSocketFactory}.
     *
     * @return the instance of {@code DefaultServerSocketFactory}
     */    
    public static DefaultServerSocketFactory getInstance() {
        return INSTANCE;
    }

    @Override
    public ServerSocket newServerSocket() throws IOException {
        return new ServerSocket();
    }

    @Override
    public ServerSocket newServerSocket(final int port) throws IOException {
        return new ServerSocket(port);
    }

    @Override
    public ServerSocket newServerSocket(
            final int port, final int backlog) throws IOException {
        return new ServerSocket(port, backlog);
    }

    @Override
    public ServerSocket newServerSocket(
            final int port,
            final int backlog,
            final InetAddress bindAddr) throws IOException {
        return new ServerSocket(port, backlog, bindAddr);
    }

}
