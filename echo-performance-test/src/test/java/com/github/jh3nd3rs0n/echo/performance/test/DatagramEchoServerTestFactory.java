package com.github.jh3nd3rs0n.echo.performance.test;

import java.net.InetAddress;

public abstract class DatagramEchoServerTestFactory {

    public abstract DatagramEchoServerTest newDatagramEchoServerTest(
            final InetAddress datagramEchServerInetAddress,
            final int datagramEchServerPort,
            final String scksServerHostAddress,
            final int scksServerPort);

}
