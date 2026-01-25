package com.github.jh3nd3rs0n.jargyle.test.socks.server;

import java.io.IOException;
import java.net.InetAddress;
import java.util.List;

public abstract class AbstractSocksServer {

    public abstract InetAddress getInetAddress();

    public abstract int getPort();

    public abstract State getState();

    public abstract void start() throws IOException;

    public abstract void stop() throws IOException;

    public static void stop(
            final List<AbstractSocksServer> socksServers) throws IOException {
        for (AbstractSocksServer socksServer : socksServers) {
            if (!socksServer.getState().equals(State.STOPPED)) {
                socksServer.stop();
            }
        }
    }

    public enum State {
        STARTED,
        STOPPED
    }

}
