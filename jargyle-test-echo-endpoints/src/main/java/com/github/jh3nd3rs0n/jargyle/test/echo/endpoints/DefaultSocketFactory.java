package com.github.jh3nd3rs0n.jargyle.test.echo.endpoints;

import java.net.Socket;

final class DefaultSocketFactory extends SocketFactory {

    private static final DefaultSocketFactory INSTANCE =
            new DefaultSocketFactory();

    private DefaultSocketFactory() { }

    public static DefaultSocketFactory getInstance() {
        return INSTANCE;
    }

    @Override
    public Socket newSocket() {
        return new Socket();
    }

}
