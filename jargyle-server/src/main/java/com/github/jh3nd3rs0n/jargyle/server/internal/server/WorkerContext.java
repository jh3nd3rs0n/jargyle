package com.github.jh3nd3rs0n.jargyle.server.internal.server;

import java.net.Socket;
import java.util.concurrent.atomic.AtomicInteger;

final class WorkerContext {

    private Socket clientSocket;
    private final ConfiguredObjectsProvider configuredObjectsProvider;
    private final AtomicInteger currentWorkerCount;

    public WorkerContext(
            final Socket clientSock,
            final AtomicInteger workerCount,
            final ConfiguredObjectsProvider configuredObjsProvider) {
        this.clientSocket = clientSock;
        this.configuredObjectsProvider = configuredObjsProvider;
        this.currentWorkerCount = workerCount;
    }

    public int decrementAndGetCurrentWorkerCount() {
        return this.currentWorkerCount.decrementAndGet();
    }

    public Socket getClientSocket() {
        return this.clientSocket;
    }

    public ConfiguredObjects getConfiguredObjects() {
        return this.configuredObjectsProvider.getConfiguredObjects();
    }

    public int incrementAndGetCurrentWorkerCount() {
        return this.currentWorkerCount.incrementAndGet();
    }

    public void setClientSocket(final Socket clientSock) {
        this.clientSocket = clientSock;
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName() +
                " [getClientSocket()=" +
                this.getClientSocket() +
                "]";
    }

}
