package com.github.jh3nd3rs0n.jargyle.test.echo.endpoints;

import java.net.DatagramSocket;
import java.net.SocketAddress;
import java.net.SocketException;

final class DefaultDatagramSocketFactory extends DatagramSocketFactory {

    private static final DefaultDatagramSocketFactory INSTANCE =
            new DefaultDatagramSocketFactory();

    private DefaultDatagramSocketFactory() { }

    public static DefaultDatagramSocketFactory getInstance() {
        return INSTANCE;
    }

    @Override
    public DatagramSocket newDatagramSocket(
            final SocketAddress bindaddr) throws SocketException {
        return new DatagramSocket(bindaddr);
    }

}
