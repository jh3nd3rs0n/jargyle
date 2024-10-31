package com.github.jh3nd3rs0n.jargyle.integration.test;

import io.netty.example.socksproxy.SocksServer;

import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicReference;

public final class NettySocksServer {

    private ExecutorService executor;
    private State state;

    public NettySocksServer() {
        this.executor = null;
        this.state = State.STOPPED;
    }

    public String getHostAddress() {
        return SocksServer.INET_ADDRESS.getHostAddress();
    }

    public int getPort() {
        return SocksServer.PORT;
    }

    public State getState() {
        return this.state;
    }

    public void start() throws IOException {
        AtomicReference<Exception> exceptionAtomicReference =
                new AtomicReference<>();
        this.executor = Executors.newSingleThreadExecutor();
        this.executor.execute(() -> {
            try {
                SocksServer.main(new String[]{});
            } catch (InterruptedException e) {
                // caused by this.executor.shutdownNow();
            } catch (Exception e) {
                exceptionAtomicReference.set(e);
            }
        });
        boolean socksServerActive = false;
        Exception e = null;
        do {
            try (Socket socket = new Socket(
                    SocksServer.INET_ADDRESS, SocksServer.PORT)) {
                socksServerActive = socket.isConnected();
            } catch (IOException ignored) {
            }
        } while (!socksServerActive
                && (e = exceptionAtomicReference.get()) == null);
        if (e != null) {
            throw new IOException(e);
        }
        this.state = State.STARTED;
    }

    public void stop() {
        this.executor.shutdownNow();
        this.executor = null;
        this.state = State.STOPPED;
    }

    public enum State {

        STARTED,

        STOPPED

    }

}
