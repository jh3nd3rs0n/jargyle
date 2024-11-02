package com.github.jh3nd3rs0n.test.echo.server.performance;

import java.net.InetAddress;

public abstract class EchoDatagramServerTestRunner implements Runnable {

    protected final InetAddress echoDatagramServerInetAddress;
    protected final int echoDatagramServerPort;

    protected final String socksServerHostAddress;
    protected final int socksServerPort;

    public EchoDatagramServerTestRunner(
            final InetAddress echDatagramServerInetAddress,
            final int echDatagramServerPort,
            final String scksServerHostAddress,
            final int scksServerPort) {
        this.echoDatagramServerInetAddress = echDatagramServerInetAddress;
        this.echoDatagramServerPort = echDatagramServerPort;
        this.socksServerHostAddress = scksServerHostAddress;
        this.socksServerPort = scksServerPort;
    }

    @Override
    public abstract void run();

}
