package com.github.jh3nd3rs0n.jargyle.test.echo.endpoints;

import com.github.jh3nd3rs0n.jargyle.client.SocksClient;

import java.net.Socket;

public final class SocksSocketFactory extends SocketFactory {

    private final SocksClient socksClient;

    public SocksSocketFactory(final SocksClient client) {
        this.socksClient = client;
    }

    @Override
    public Socket newSocket() {
        return this.socksClient.newSocksNetObjectFactory().newSocket();
    }

}
