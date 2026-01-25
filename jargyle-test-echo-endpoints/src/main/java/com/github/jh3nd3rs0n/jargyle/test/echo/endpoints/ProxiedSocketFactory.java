package com.github.jh3nd3rs0n.jargyle.test.echo.endpoints;

import java.net.Proxy;
import java.net.Socket;

public final class ProxiedSocketFactory extends SocketFactory {

    private final Proxy proxy;

    public ProxiedSocketFactory(final Proxy p) { this.proxy = p; }

    @Override
    public Socket newSocket() {
        return new Socket(this.proxy);
    }

}
