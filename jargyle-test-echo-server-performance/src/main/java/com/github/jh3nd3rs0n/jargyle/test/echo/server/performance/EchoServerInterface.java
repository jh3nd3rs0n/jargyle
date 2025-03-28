package com.github.jh3nd3rs0n.jargyle.test.echo.server.performance;

import java.io.IOException;
import java.net.InetAddress;

public abstract class EchoServerInterface {

    public abstract InetAddress getInetAddress();

    public abstract int getPort();

    public abstract State getState();

    public abstract void start() throws IOException;

    public abstract void stop() throws IOException;

    public enum State {
        STARTED,
        STOPPED
    }

}
