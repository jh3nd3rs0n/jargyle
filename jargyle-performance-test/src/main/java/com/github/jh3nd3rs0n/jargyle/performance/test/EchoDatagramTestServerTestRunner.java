package com.github.jh3nd3rs0n.jargyle.performance.test;

import java.net.InetAddress;

public abstract class EchoDatagramTestServerTestRunner implements Runnable {

    protected final InetAddress echoDatagramTestServerInetAddress;
    protected final int echoDatagramTestServerPort;

    protected final String socksServerHostAddress;
    protected final int socksServerPort;

    public EchoDatagramTestServerTestRunner(
            final InetAddress echDatagramTestServerInetAddress,
            final int echDatagramTestServerPort,
            final String scksServerHostAddress,
            final int scksServerPort) {
        this.echoDatagramTestServerInetAddress = echDatagramTestServerInetAddress;
        this.echoDatagramTestServerPort = echDatagramTestServerPort;
        this.socksServerHostAddress = scksServerHostAddress;
        this.socksServerPort = scksServerPort;
    }

    @Override
    public abstract void run();

}
