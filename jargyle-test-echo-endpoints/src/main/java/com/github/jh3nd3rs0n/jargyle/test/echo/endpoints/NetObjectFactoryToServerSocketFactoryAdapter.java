package com.github.jh3nd3rs0n.jargyle.test.echo.endpoints;

import com.github.jh3nd3rs0n.jargyle.client.NetObjectFactory;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;

public final class NetObjectFactoryToServerSocketFactoryAdapter
        extends ServerSocketFactory {

    private final NetObjectFactory netObjectFactory;

    public NetObjectFactoryToServerSocketFactoryAdapter(
            final NetObjectFactory netObjFactory) {
        this.netObjectFactory = netObjFactory;
    }

    @Override
    public ServerSocket newServerSocket(
            final int port,
            final int backlog,
            final InetAddress bindInetAddress) throws IOException {
        return this.netObjectFactory.newServerSocket(
                port, backlog, bindInetAddress);
    }

}
