package com.github.jh3nd3rs0n.jargyle.test.echo.endpoints;

import java.net.Socket;

public abstract class SocketFactory {

    public static SocketFactory getDefault() {
        return DefaultSocketFactory.getInstance();
    }

    public abstract Socket newSocket();

}
