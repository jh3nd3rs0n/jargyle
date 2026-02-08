package com.github.jh3nd3rs0n.jargyle.test.echo.endpoints;

import com.github.jh3nd3rs0n.jargyle.client.SocksClient;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;

public final class SocksClientToServerSocketFactoryAdapter
        extends ServerSocketFactory {

    private final SocksClient socksClient;

    public SocksClientToServerSocketFactoryAdapter(final SocksClient client) {
        this.socksClient = client;
    }

    @Override
    public ServerSocket newServerSocket(
            final int port,
            final int backlog,
            final InetAddress bindInetAddress) throws IOException {
        return this.socksClient.getSocksServerSocketFactory().newServerSocket(
                port, backlog, bindInetAddress);
    }

}
