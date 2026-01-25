package com.github.jh3nd3rs0n.jargyle.test.echo.endpoints;

import com.github.jh3nd3rs0n.jargyle.client.NetObjectFactory;

import java.net.DatagramSocket;
import java.net.SocketAddress;
import java.net.SocketException;

public final class NetObjectFactoryToDatagramSocketFactoryAdapter
        extends DatagramSocketFactory {

    private final NetObjectFactory netObjectFactory;

    public NetObjectFactoryToDatagramSocketFactoryAdapter(
            final NetObjectFactory netObjFactory) {
        this.netObjectFactory = netObjFactory;
    }

    @Override
    public DatagramSocket newDatagramSocket(
            final SocketAddress bindaddr) throws SocketException {
        return this.netObjectFactory.newDatagramSocket(bindaddr);
    }

}
