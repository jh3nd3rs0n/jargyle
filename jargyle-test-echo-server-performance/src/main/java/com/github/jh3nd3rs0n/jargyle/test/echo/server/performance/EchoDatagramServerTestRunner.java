package com.github.jh3nd3rs0n.jargyle.test.echo.server.performance;

import java.net.InetAddress;

public abstract class EchoDatagramServerTestRunner implements Runnable {

    protected final InetAddress echoDatagramServerInetAddress;
    protected final int echoDatagramServerPort;

    protected final InetAddress socksServerInetAddress;
    protected final int socksServerPort;

    public EchoDatagramServerTestRunner(
            final InetAddress echDatagramServerInetAddress,
            final int echDatagramServerPort,
            final InetAddress scksServerInetAddress,
            final int scksServerPort) {
        this.echoDatagramServerInetAddress = echDatagramServerInetAddress;
        this.echoDatagramServerPort = echDatagramServerPort;
        this.socksServerInetAddress = scksServerInetAddress;
        this.socksServerPort = scksServerPort;
    }

    @Override
    public abstract void run();

}
