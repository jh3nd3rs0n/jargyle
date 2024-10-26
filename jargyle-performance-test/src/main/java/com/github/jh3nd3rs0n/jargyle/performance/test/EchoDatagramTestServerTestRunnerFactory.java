package com.github.jh3nd3rs0n.jargyle.performance.test;

import java.net.InetAddress;

public abstract class EchoDatagramTestServerTestRunnerFactory {

    public abstract EchoDatagramTestServerTestRunner newEchoDatagramTestServerTestRunner(
            final InetAddress echDatagramTestServerInetAddress,
            final int echDatagramTestServerPort,
            final String scksServerHostAddress,
            final int scksServerPort);

}
