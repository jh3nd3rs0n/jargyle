package com.github.jh3nd3rs0n.test.echo.server.performance;

import com.github.jh3nd3rs0n.test.help.net.DatagramServer;

import java.io.IOException;
import java.net.InetAddress;

public final class EchoDatagramServerInterfaceImpl
        extends EchoDatagramServerInterface {

    private final DatagramServer echoDatagramServer;

    public EchoDatagramServerInterfaceImpl(
            final DatagramServer echDatagramServer) {
        this.echoDatagramServer = echDatagramServer;
    }

    @Override
    public InetAddress getInetAddress() {
        return this.echoDatagramServer.getInetAddress();
    }

    @Override
    public int getPort() {
        return this.echoDatagramServer.getPort();
    }

    @Override
    public State getState() {
        State state;
        switch (this.echoDatagramServer.getState()) {
            case STARTED:
                state = State.STARTED;
                break;
            case STOPPED:
                state = State.STOPPED;
                break;
            default:
                throw new AssertionError(String.format(
                        "unhandled state: %s",
                        this.echoDatagramServer.getState()));
        }
        return state;
    }

    @Override
    public void start() throws IOException {
        this.echoDatagramServer.start();
    }

    @Override
    public void stop() throws IOException {
        this.echoDatagramServer.stop();
    }

}
