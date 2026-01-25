package com.github.jh3nd3rs0n.jargyle.test.echo.endpoints;

import com.github.jh3nd3rs0n.jargyle.client.NetObjectFactory;

import java.net.Socket;

public final class NetObjectFactoryToSocketFactoryAdapter
        extends SocketFactory {

    private final NetObjectFactory netObjectFactory;

    public NetObjectFactoryToSocketFactoryAdapter(
            final NetObjectFactory netObjFactory) {
        this.netObjectFactory = netObjFactory;
    }

    @Override
    public Socket newSocket() {
        return this.netObjectFactory.newSocket();
    }

}
