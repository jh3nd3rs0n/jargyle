package com.github.jh3nd3rs0n.jargyle.test.echo.server.performance;

import java.net.InetAddress;

public abstract class EchoDatagramServerTestRunnerFactory {

    public abstract EchoDatagramServerTestRunner newEchoDatagramServerTestRunner(
            final InetAddress echDatagramServerInetAddress,
            final int echDatagramServerPort,
            final InetAddress scksServerInetAddress,
            final int scksServerPort);

}
