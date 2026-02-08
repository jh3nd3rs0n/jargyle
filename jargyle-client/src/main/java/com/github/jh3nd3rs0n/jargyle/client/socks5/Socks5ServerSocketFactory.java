package com.github.jh3nd3rs0n.jargyle.client.socks5;

import com.github.jh3nd3rs0n.jargyle.client.SocksServerSocketFactory;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;

public final class Socks5ServerSocketFactory extends SocksServerSocketFactory {

    private final Socks5Client socks5Client;
    private final Socks5ClientAgent socks5ClientAgent;

    Socks5ServerSocketFactory(final Socks5Client client) {
        super(client);
        this.socks5Client = client;
        this.socks5ClientAgent = new Socks5ClientAgent(client);
    }

    public Socks5Client getSocks5Client() {
        return this.socks5Client;
    }

    @Override
    public ServerSocket newServerSocket() throws IOException {
        return new Socks5ServerSocket(this.socks5ClientAgent);
    }

    @Override
    public ServerSocket newServerSocket(final int port) throws IOException {
        return new Socks5ServerSocket(this.socks5ClientAgent, port);
    }

    @Override
    public ServerSocket newServerSocket(
            final int port, final int backlog) throws IOException {
        return new Socks5ServerSocket(this.socks5ClientAgent, port);
    }

    @Override
    public ServerSocket newServerSocket(
            final int port,
            final int backlog,
            final InetAddress bindAddr) throws IOException {
        return new Socks5ServerSocket(this.socks5ClientAgent, port, bindAddr);
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName() +
                " [getSocks5Client()=" +
                this.getSocks5Client() +
                "]";
    }

}
