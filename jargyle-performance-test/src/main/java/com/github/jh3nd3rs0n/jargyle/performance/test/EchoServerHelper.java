package com.github.jh3nd3rs0n.jargyle.performance.test;

import com.github.jh3nd3rs0n.jargyle.integration.test.EchoServer;
import com.github.jh3nd3rs0n.jargyle.client.NetObjectFactory;

public class EchoServerHelper {
    public static final int BACKLOG = 100;

    public static EchoServer newEchoServer() {
        return new EchoServer(
                NetObjectFactory.getDefault(),
                0,
                BACKLOG,
                EchoServer.INET_ADDRESS,
                EchoServer.SOCKET_SETTINGS);
    }
}
