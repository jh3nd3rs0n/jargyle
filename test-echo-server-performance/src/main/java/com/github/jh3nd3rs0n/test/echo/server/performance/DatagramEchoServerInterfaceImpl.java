package com.github.jh3nd3rs0n.test.echo.server.performance;

import com.github.jh3nd3rs0n.test.help.net.DatagramServer;

import java.io.IOException;
import java.net.InetAddress;

public final class DatagramEchoServerInterfaceImpl
        extends DatagramEchoServerInterface {

    private final DatagramServer datagramEchoServer;

    public DatagramEchoServerInterfaceImpl(
            final DatagramServer datagramEchServer) {
        this.datagramEchoServer = datagramEchServer;
    }

    @Override
    public InetAddress getInetAddress() {
        return this.datagramEchoServer.getInetAddress();
    }

    @Override
    public int getPort() {
        return this.datagramEchoServer.getPort();
    }

    @Override
    public State getState() {
        State state;
        switch (this.datagramEchoServer.getState()) {
            case STARTED:
                state = State.STARTED;
                break;
            case STOPPED:
                state = State.STOPPED;
                break;
            default:
                throw new AssertionError(String.format(
                        "unhandled state: %s",
                        this.datagramEchoServer.getState()));
        }
        return state;
    }

    @Override
    public void start() throws IOException {
        this.datagramEchoServer.start();
    }

    @Override
    public void stop() throws IOException {
        this.datagramEchoServer.stop();
    }

}
