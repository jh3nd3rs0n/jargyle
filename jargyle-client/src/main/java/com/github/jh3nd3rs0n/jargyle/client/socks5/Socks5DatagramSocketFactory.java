package com.github.jh3nd3rs0n.jargyle.client.socks5;

import com.github.jh3nd3rs0n.jargyle.client.SocksDatagramSocketFactory;

import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketAddress;
import java.net.SocketException;

public final class Socks5DatagramSocketFactory extends SocksDatagramSocketFactory {

    private final Socks5Client socks5Client;
    private final Socks5ClientAgent socks5ClientAgent;

    Socks5DatagramSocketFactory(final Socks5Client client) {
        super(client);
        this.socks5Client = client;
        this.socks5ClientAgent = new Socks5ClientAgent(client);
    }

    public Socks5Client getSocks5Client() {
        return this.socks5Client;
    }

    @Override
    public DatagramSocket newDatagramSocket() throws SocketException {
        return new Socks5DatagramSocket(this.socks5ClientAgent);
    }

    @Override
    public DatagramSocket newDatagramSocket(
            final int port) throws SocketException {
        return new Socks5DatagramSocket(this.socks5ClientAgent, port);
    }

    @Override
    public DatagramSocket newDatagramSocket(
            final int port, final InetAddress laddr) throws SocketException {
        return new Socks5DatagramSocket(this.socks5ClientAgent, port, laddr);
    }

    @Override
    public DatagramSocket newDatagramSocket(
            final SocketAddress bindaddr) throws SocketException {
        return new Socks5DatagramSocket(this.socks5ClientAgent, bindaddr);
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName() +
                " [getSocks5Client()=" +
                this.getSocks5Client() +
                "]";
    }

}
