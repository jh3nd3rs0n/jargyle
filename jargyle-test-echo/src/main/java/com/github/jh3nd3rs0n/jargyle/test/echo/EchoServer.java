package com.github.jh3nd3rs0n.jargyle.test.echo;

import com.github.jh3nd3rs0n.jargyle.client.NetObjectFactory;
import com.github.jh3nd3rs0n.jargyle.test.help.net.Server;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

public final class EchoServer {

    private final Server server;

    private EchoServer(final Server s) {
        this.server = s;
    }

    public static EchoServer newInstance(final int port) {
        return newInstance(
                port,
                Server.BACKLOG,
                Server.INET_ADDRESS);
    }

    public static EchoServer newInstance(
            final int port, final int backlog, final InetAddress inetAddress) {
        return newInstance(
                NetObjectFactory.getDefault(),
                port,
                backlog,
                inetAddress);
    }

    public static EchoServer newInstance(
            final NetObjectFactory netObjectFactory, final int port) {
        return newInstance(
                netObjectFactory,
                port,
                Server.BACKLOG,
                Server.INET_ADDRESS);
    }

    public static EchoServer newInstance(
            final NetObjectFactory netObjectFactory,
            final int port,
            final int backlog,
            final InetAddress inetAddress) {
        return new EchoServer(new Server(
                new ServerSocketFactory(netObjectFactory),
                port,
                backlog,
                inetAddress,
                new VirtualThreadPerTaskExecutorOrCachedThreadPoolFactory(),
                new EchoWorkerFactory()));
    }

    public InetAddress getInetAddress() {
        return this.server.getInetAddress();
    }

    public int getPort() {
        return this.server.getPort();
    }

    public State getState() {
        State state;
        switch (this.server.getState()) {
            case STARTED:
                state = State.STARTED;
                break;
            case STOPPED:
                state = State.STOPPED;
                break;
            default:
                throw new AssertionError(String.format(
                        "unhandled state: %s",
                        this.server.getState()));
        }
        return state;
    }

    public void start() throws IOException {
        this.server.start();
    }

    public String startThenEchoThenStop(
            final EchoClient echoClient,
            final String string) throws IOException {
        String returningString;
        this.start();
        try {
            returningString = echoClient.echo(
                    string, this.getInetAddress(), this.getPort());
        } finally {
            this.stop();
        }
        return returningString;
    }

    public void stop() throws IOException {
        this.server.stop();
    }

    private static final class EchoWorker extends Server.Worker {

        public EchoWorker(Socket clientSock) {
            super(clientSock);
        }

        @Override
        protected void doWork() throws IOException {
            Socket clientSocket = this.getClientSocket();
            InputStream in = clientSocket.getInputStream();
            OutputStream out = clientSocket.getOutputStream();
            byte[] bytes = MeasuredIoHelper.readFrom(in);
            MeasuredIoHelper.writeThenFlush(bytes, out);
        }

    }

    private static final class EchoWorkerFactory
            extends Server.WorkerFactory {

        public EchoWorkerFactory() {
        }

        @Override
        public Server.Worker newWorker(final Socket clientSocket) {
            return new EchoWorker(clientSocket);
        }

    }

    private static final class ServerSocketFactory
            extends Server.ServerSocketFactory {

        private final NetObjectFactory netObjectFactory;

        private ServerSocketFactory(final NetObjectFactory netObjFactory) {
            this.netObjectFactory = netObjFactory;
        }

        @Override
        public ServerSocket newServerSocket(
                final int port,
                final int backlog,
                final InetAddress bindInetAddress) throws IOException {
            return this.netObjectFactory.newServerSocket(
                    port, backlog, bindInetAddress);
        }

    }

    public enum State {
        STARTED,
        STOPPED
    }

}
