package com.github.jh3nd3rs0n.jargyle.test.echo.server.performance;

import java.io.IOException;

public abstract class SocksServerInterface {

    public abstract String getHostAddress();

    public abstract int getPort();

    public abstract State getState();

    public abstract void start() throws IOException;

    public abstract void stop() throws IOException;

    public enum State {
        STARTED,
        STOPPED
    }

}
