package com.github.jh3nd3rs0n.jargyle.test.socks.server;

import com.github.jh3nd3rs0n.jargyle.test.netty.example.socksproxy.SocksServer;

import java.io.IOException;
import java.net.InetAddress;

public final class NettySocksServer extends AbstractSocksServer {

    private final SocksServer socksServer;

    public NettySocksServer() {
        this.socksServer = new SocksServer();
    }

    @Override
    public InetAddress getInetAddress() {
        return this.socksServer.getInetAddress();
    }

    @Override
    public int getPort() {
        return this.socksServer.getPort();
    }

    @Override
    public State getState() {
        State state;
        switch (this.socksServer.getState()) {
            case STARTED:
                state = State.STARTED;
                break;
            case STOPPED:
                state = State.STOPPED;
                break;
            default:
                throw new AssertionError(String.format(
                        "unhandled state: %s",
                        this.socksServer.getState()));
        }
        return state;
    }

    @Override
    public void start() throws IOException {
        this.socksServer.start();
    }

    @Override
    public void stop() throws IOException {
        this.socksServer.stop();
    }

}
