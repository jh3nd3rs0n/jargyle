package com.github.jh3nd3rs0n.test.echo.server.performance;

import java.net.InetAddress;

public abstract class EchoDatagramServerTestRunnerFactory {

    public abstract EchoDatagramServerTestRunner newEchoDatagramServerTestRunner(
            final InetAddress echDatagramServerInetAddress,
            final int echDatagramServerPort,
            final String scksServerHostAddress,
            final int scksServerPort);

}
