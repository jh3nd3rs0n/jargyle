package com.github.jh3nd3rs0n.test.echo.server.performance;

import java.net.InetAddress;

public abstract class DatagramEchoServerTestRunnerFactory {

    public abstract DatagramEchoServerTestRunner newDatagramEchoServerTestRunner(
            final InetAddress datagramEchServerInetAddress,
            final int datagramEchServerPort,
            final String scksServerHostAddress,
            final int scksServerPort);

}
