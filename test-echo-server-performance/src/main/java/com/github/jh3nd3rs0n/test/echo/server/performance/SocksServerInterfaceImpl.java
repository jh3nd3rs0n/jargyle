package com.github.jh3nd3rs0n.test.echo.server.performance;

import com.github.jh3nd3rs0n.jargyle.server.SocksServer;

import java.io.IOException;

public final class SocksServerInterfaceImpl extends SocksServerInterface {

    private final SocksServer socksServer;

    public SocksServerInterfaceImpl(final SocksServer scksServer) {
        this.socksServer = scksServer;
    }

    @Override
    public String getHostAddress() {
        return this.socksServer.getHost().toString();
    }

    @Override
    public int getPort() {
        return this.socksServer.getPort().intValue();
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
