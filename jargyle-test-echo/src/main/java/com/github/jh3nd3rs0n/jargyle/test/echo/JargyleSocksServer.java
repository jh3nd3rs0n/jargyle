package com.github.jh3nd3rs0n.jargyle.test.echo;

import com.github.jh3nd3rs0n.jargyle.server.Configuration;
import com.github.jh3nd3rs0n.jargyle.server.SocksServer;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

public final class JargyleSocksServer extends AbstractSocksServer {

    private final SocksServer socksServer;

    public JargyleSocksServer(final Configuration configuration) {
        this.socksServer = new SocksServer(configuration);
    }

    @Override
    public InetAddress getInetAddress() {
        try {
            return this.socksServer.getHost().toInetAddress();
        } catch (UnknownHostException e) {
            throw new UncheckedIOException(e);
        }
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
