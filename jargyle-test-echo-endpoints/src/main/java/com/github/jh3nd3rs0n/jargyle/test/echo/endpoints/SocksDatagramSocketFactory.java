package com.github.jh3nd3rs0n.jargyle.test.echo.endpoints;

import com.github.jh3nd3rs0n.jargyle.client.SocksClient;

import java.net.DatagramSocket;
import java.net.SocketAddress;
import java.net.SocketException;

public final class SocksDatagramSocketFactory extends DatagramSocketFactory {

    private final SocksClient socksClient;

    public SocksDatagramSocketFactory(final SocksClient client) {
        this.socksClient = client;
    }

    @Override
    public DatagramSocket newDatagramSocket(
            final SocketAddress bindaddr) throws SocketException {
        return this.socksClient.newSocksNetObjectFactory().newDatagramSocket(
                bindaddr);
    }

}
