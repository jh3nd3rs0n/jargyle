package com.github.jh3nd3rs0n.jargyle.test.echo.endpoints;

import java.net.DatagramSocket;
import java.net.SocketAddress;
import java.net.SocketException;

public abstract class DatagramSocketFactory {

    public static DatagramSocketFactory getDefault() {
        return DefaultDatagramSocketFactory.getInstance();
    }

    public abstract DatagramSocket newDatagramSocket(
            final SocketAddress bindaddr) throws SocketException;

}
