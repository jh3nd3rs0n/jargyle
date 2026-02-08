package com.github.jh3nd3rs0n.jargyle.server.internal.server;

import com.github.jh3nd3rs0n.jargyle.client.*;

public final class ChainRoute extends Route {

    private final SocksClient socksClient;

    public ChainRoute(final String id, final SocksClient client) {
        super(id);
        this.socksClient = client;
    }

    @Override
    public DatagramSocketFactory getDatagramSocketFactory() {
        return this.socksClient.getSocksDatagramSocketFactory();
    }

    @Override
    public HostResolverFactory getHostResolverFactory() {
        return this.socksClient.getSocksHostResolverFactory();
    }

    @Override
    public ServerSocketFactory getServerSocketFactory() {
        return this.socksClient.getSocksServerSocketFactory();
    }

    @Override
    public SocketFactory getSocketFactory() {
        return this.socksClient.getSocksSocketFactory();
    }

}
