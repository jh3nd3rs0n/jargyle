package com.github.jh3nd3rs0n.jargyle.test.echo.endpoints;

import com.github.jh3nd3rs0n.jargyle.test.help.net.Server;

public abstract class ServerSocketFactory extends Server.ServerSocketFactory {

    public static ServerSocketFactory getDefault() {
        return DefaultServerSocketFactory.getInstance();
    }

}
