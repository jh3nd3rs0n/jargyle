package com.github.jh3nd3rs0n.jargyle.client.socks5;

import com.github.jh3nd3rs0n.jargyle.client.SocksSocketFactory;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

public final class Socks5SocketFactory extends SocksSocketFactory {

    private final Socks5Client socks5Client;
    private final Socks5ClientAgent socks5ClientAgent;

    Socks5SocketFactory(final Socks5Client client) {
        super(client);
        this.socks5Client = client;
        this.socks5ClientAgent = new Socks5ClientAgent(client);
    }

    public Socks5Client getSocks5Client() {
        return this.socks5Client;
    }

    @Override
    public Socket newSocket() {
        return new Socks5Socket(this.socks5ClientAgent);
    }

    @Override
    public Socket newSocket(
            final InetAddress address, final int port) throws IOException {
        return new Socks5Socket(this.socks5ClientAgent, address, port);
    }

    @Override
    public Socket newSocket(
            final InetAddress address,
            final int port,
            final InetAddress localAddr,
            final int localPort) throws IOException {
        return new Socks5Socket(
                this.socks5ClientAgent, address, port, localAddr, localPort);
    }

    @Override
    public Socket newSocket(
            final String host,
            final int port) throws UnknownHostException, IOException {
        return new Socks5Socket(this.socks5ClientAgent, host, port);
    }

    @Override
    public Socket newSocket(
            final String host,
            final int port,
            final InetAddress localAddr,
            final int localPort) throws IOException {
        return new Socks5Socket(
                this.socks5ClientAgent, host, port, localAddr, localPort);
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName() +
                " [getSocks5Client()=" +
                this.getSocks5Client() +
                "]";
    }

}
