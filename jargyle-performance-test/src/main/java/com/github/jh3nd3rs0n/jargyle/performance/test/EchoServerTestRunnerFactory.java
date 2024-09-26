package com.github.jh3nd3rs0n.jargyle.performance.test;

import java.net.InetAddress;

public abstract class EchoServerTestRunnerFactory {

    public abstract EchoServerTestRunner newEchoServerTestRunner(
            final InetAddress echServerInetAddress,
            final int echServerPort,
            final String scksServerHostAddress,
            final int scksServerPort);

}
