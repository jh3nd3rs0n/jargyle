package com.github.jh3nd3rs0n.jargyle.server.internal.server;

import com.github.jh3nd3rs0n.jargyle.client.DatagramSocketFactory;
import com.github.jh3nd3rs0n.jargyle.client.HostResolverFactory;
import com.github.jh3nd3rs0n.jargyle.client.ServerSocketFactory;
import com.github.jh3nd3rs0n.jargyle.client.SocketFactory;

public final class DirectRoute extends Route {

    public DirectRoute(final String id) {
        super(id);
    }

    @Override
    public DatagramSocketFactory getDatagramSocketFactory() {
        return DatagramSocketFactory.getDefault();
    }

    @Override
    public HostResolverFactory getHostResolverFactory() {
        return HostResolverFactory.getDefault();
    }

    @Override
    public ServerSocketFactory getServerSocketFactory() {
        return ServerSocketFactory.getDefault();
    }

    @Override
    public SocketFactory getSocketFactory() {
        return SocketFactory.getDefault();
    }

}
