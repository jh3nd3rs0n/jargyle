package com.github.jh3nd3rs0n.jargyle.client.socks5;

import com.github.jh3nd3rs0n.jargyle.client.HostResolver;
import com.github.jh3nd3rs0n.jargyle.client.SocksHostResolverFactory;

public final class Socks5HostResolverFactory extends SocksHostResolverFactory {

    private final Socks5Client socks5Client;
    private final Socks5ClientAgent socks5ClientAgent;

    Socks5HostResolverFactory(final Socks5Client client) {
        super(client);
        this.socks5Client = client;
        this.socks5ClientAgent = new Socks5ClientAgent(client);
    }

    public Socks5Client getSocks5Client() {
        return this.socks5Client;
    }

    @Override
    public HostResolver newHostResolver() {
        return new Socks5HostResolver(this.socks5ClientAgent);
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName() +
                " [getSocks5Client()=" +
                this.getSocks5Client() +
                "]";
    }

}
