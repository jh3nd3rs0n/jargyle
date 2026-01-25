package com.github.jh3nd3rs0n.jargyle.test.echo.endpoints;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;

final class DefaultServerSocketFactory extends ServerSocketFactory {

    private static final DefaultServerSocketFactory INSTANCE =
            new DefaultServerSocketFactory();

    private DefaultServerSocketFactory() { }

    public static DefaultServerSocketFactory getInstance() {
        return INSTANCE;
    }

    @Override
    public ServerSocket newServerSocket(
            final int port,
            final int backlog,
            final InetAddress bindInetAddress) throws IOException {
        return new ServerSocket(port, backlog, bindInetAddress);
    }

}
