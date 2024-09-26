package com.github.jh3nd3rs0n.jargyle.performance.test;

import java.net.InetAddress;

public abstract class DatagramEchoServerTestRunner implements Runnable {

    protected final InetAddress datagramEchoServerInetAddress;
    protected final int datagramEchoServerPort;

    protected final String socksServerHostAddress;
    protected final int socksServerPort;

    public DatagramEchoServerTestRunner(
            final InetAddress datagramEchServerInetAddress,
            final int datagramEchServerPort,
            final String scksServerHostAddress,
            final int scksServerPort) {
        this.datagramEchoServerInetAddress = datagramEchServerInetAddress;
        this.datagramEchoServerPort = datagramEchServerPort;
        this.socksServerHostAddress = scksServerHostAddress;
        this.socksServerPort = scksServerPort;
    }

    @Override
    public abstract void run();

}
