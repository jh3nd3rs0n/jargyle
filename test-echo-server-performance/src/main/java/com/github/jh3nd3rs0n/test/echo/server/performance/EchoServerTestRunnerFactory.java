package com.github.jh3nd3rs0n.test.echo.server.performance;

import java.net.InetAddress;

public abstract class EchoServerTestRunnerFactory {

    public abstract EchoServerTestRunner newEchoServerTestRunner(
            final InetAddress echServerInetAddress,
            final int echServerPort,
            final String scksServerHostAddress,
            final int scksServerPort);

}
