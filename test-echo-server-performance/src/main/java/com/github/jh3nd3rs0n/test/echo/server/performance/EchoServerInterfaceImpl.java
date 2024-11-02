package com.github.jh3nd3rs0n.test.echo.server.performance;

import com.github.jh3nd3rs0n.test.help.net.Server;

import java.io.IOException;
import java.net.InetAddress;

public final class EchoServerInterfaceImpl
        extends EchoServerInterface {

    private final Server echoServer;

    public EchoServerInterfaceImpl(final Server echServer) {
        this.echoServer = echServer;
    }

    @Override
    public InetAddress getInetAddress() {
        return this.echoServer.getInetAddress();
    }

    @Override
    public int getPort() {
        return this.echoServer.getPort();
    }

    @Override
    public State getState() {
        State state;
        switch (this.echoServer.getState()) {
            case STARTED:
                state = State.STARTED;
                break;
            case STOPPED:
                state = State.STOPPED;
                break;
            default:
                throw new AssertionError(String.format(
                        "unhandled state: %s",
                        this.echoServer.getState()));
        }
        return state;
    }

    @Override
    public void start() throws IOException {
        this.echoServer.start();
    }

    @Override
    public void stop() throws IOException {
        this.echoServer.stop();
    }

}
