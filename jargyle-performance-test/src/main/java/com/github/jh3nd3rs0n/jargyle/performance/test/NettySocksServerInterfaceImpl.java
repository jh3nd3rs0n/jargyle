package com.github.jh3nd3rs0n.jargyle.performance.test;

import com.github.jh3nd3rs0n.jargyle.integration.test.NettySocksServer;

import java.io.IOException;

public final class NettySocksServerInterfaceImpl extends SocksServerInterface {

    private final NettySocksServer nettySocksServer;

    public NettySocksServerInterfaceImpl() {
        this.nettySocksServer = new NettySocksServer();
    }

    @Override
    public String getHostAddress() {
        return this.nettySocksServer.getHostAddress();
    }

    @Override
    public int getPort() {
        return this.nettySocksServer.getPort();
    }

    @Override
    public State getState() {
        State state;
        switch (this.nettySocksServer.getState()) {
            case STARTED:
                state = State.STARTED;
                break;
            case STOPPED:
                state = State.STOPPED;
                break;
            default:
                throw new AssertionError(String.format(
                        "unhandled state: %s",
                        this.nettySocksServer.getState()));
        }
        return state;
    }

    @Override
    public void start() throws IOException {
        this.nettySocksServer.start();
    }

    @Override
    public void stop() throws IOException {
        this.nettySocksServer.stop();
    }

}
