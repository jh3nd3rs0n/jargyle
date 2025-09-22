package com.github.jh3nd3rs0n.jargyle.test.echo;

import com.github.jh3nd3rs0n.jargyle.client.HostResolver;
import com.github.jh3nd3rs0n.jargyle.client.NetObjectFactory;

import java.io.IOException;
import java.net.*;

public class ProxiedNetObjectFactory extends NetObjectFactory {

    private final Proxy proxy;

    public ProxiedNetObjectFactory(final Proxy p) {
        this.proxy = p;
    }

    @Override
    public DatagramSocket newDatagramSocket() throws SocketException {
        throw new UnsupportedOperationException();
    }

    @Override
    public DatagramSocket newDatagramSocket(int port) throws SocketException {
        throw new UnsupportedOperationException();
    }

    @Override
    public DatagramSocket newDatagramSocket(int port, InetAddress laddr) throws SocketException {
        throw new UnsupportedOperationException();
    }

    @Override
    public DatagramSocket newDatagramSocket(SocketAddress bindaddr) throws SocketException {
        throw new UnsupportedOperationException();
    }

    @Override
    public HostResolver newHostResolver() {
        throw new UnsupportedOperationException();
    }

    @Override
    public ServerSocket newServerSocket() throws IOException {
        throw new UnsupportedOperationException();
    }

    @Override
    public ServerSocket newServerSocket(int port) throws IOException {
        throw new UnsupportedOperationException();
    }

    @Override
    public ServerSocket newServerSocket(int port, int backlog) throws IOException {
        throw new UnsupportedOperationException();
    }

    @Override
    public ServerSocket newServerSocket(int port, int backlog, InetAddress bindAddr) throws IOException {
        throw new UnsupportedOperationException();
    }

    @Override
    public Socket newSocket() {
        return new Socket(this.proxy);
    }

    @Override
    public Socket newSocket(InetAddress address, int port) throws IOException {
        throw new UnsupportedOperationException();
    }

    @Override
    public Socket newSocket(InetAddress address, int port, InetAddress localAddr, int localPort) throws IOException {
        throw new UnsupportedOperationException();
    }

    @Override
    public Socket newSocket(String host, int port) throws UnknownHostException, IOException {
        throw new UnsupportedOperationException();
    }

    @Override
    public Socket newSocket(String host, int port, InetAddress localAddr, int localPort) throws IOException {
        throw new UnsupportedOperationException();
    }
}
