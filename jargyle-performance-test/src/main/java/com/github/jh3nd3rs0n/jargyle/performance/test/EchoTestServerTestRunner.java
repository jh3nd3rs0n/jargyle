package com.github.jh3nd3rs0n.jargyle.performance.test;

import java.net.InetAddress;

public abstract class EchoTestServerTestRunner implements Runnable {

    protected final InetAddress echoTestServerInetAddress;
    protected final int echoTestServerPort;

    protected final String socksServerHostAddress;
    protected final int socksServerPort;

    public EchoTestServerTestRunner(
            final InetAddress echTestServerInetAddress,
            final int echTestServerPort,
            final String scksServerHostAddress,
            final int scksServerPort) {
        this.echoTestServerInetAddress = echTestServerInetAddress;
        this.echoTestServerPort = echTestServerPort;
        this.socksServerHostAddress = scksServerHostAddress;
        this.socksServerPort = scksServerPort;
    }

    @Override
    public abstract void run();

}
