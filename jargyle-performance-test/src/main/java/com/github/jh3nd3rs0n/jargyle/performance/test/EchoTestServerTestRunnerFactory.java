package com.github.jh3nd3rs0n.jargyle.performance.test;

import java.net.InetAddress;

public abstract class EchoTestServerTestRunnerFactory {

    public abstract EchoTestServerTestRunner newEchoTestServerTestRunner(
            final InetAddress echTestServerInetAddress,
            final int echTestServerPort,
            final String scksServerHostAddress,
            final int scksServerPort);

}
