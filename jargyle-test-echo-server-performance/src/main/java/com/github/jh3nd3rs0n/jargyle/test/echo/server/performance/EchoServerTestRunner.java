package com.github.jh3nd3rs0n.jargyle.test.echo.server.performance;

import java.net.InetAddress;

public abstract class EchoServerTestRunner implements Runnable {

    protected final InetAddress echoServerInetAddress;
    protected final int echoServerPort;

    protected final InetAddress socksServerInetAddress;
    protected final int socksServerPort;

    public EchoServerTestRunner(
            final InetAddress echServerInetAddress,
            final int echServerPort,
            final InetAddress scksServerInetAddress,
            final int scksServerPort) {
        this.echoServerInetAddress = echServerInetAddress;
        this.echoServerPort = echServerPort;
        this.socksServerInetAddress = scksServerInetAddress;
        this.socksServerPort = scksServerPort;
    }

    @Override
    public abstract void run();

}
