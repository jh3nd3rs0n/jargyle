package com.github.jh3nd3rs0n.echo.performance.test;

import java.net.InetAddress;

public abstract class EchoServerTestFactory {

    public abstract EchoServerTest newEchoServerTest(
            final InetAddress echServerInetAddress,
            final int echServerPort,
            final String scksServerHostAddress,
            final int scksServerPort);

}
