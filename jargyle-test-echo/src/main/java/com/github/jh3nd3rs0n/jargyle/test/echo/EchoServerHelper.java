package com.github.jh3nd3rs0n.jargyle.test.echo;

import com.github.jh3nd3rs0n.jargyle.client.NetObjectFactory;
import com.github.jh3nd3rs0n.jargyle.test.help.net.Server;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

public final class EchoServerHelper {

    private EchoServerHelper() {
    }

    public static Server newEchoServer(final int port) {
        return newEchoServer(
                port,
                Server.BACKLOG,
                Server.INET_ADDRESS);
    }

    public static Server newEchoServer(
            final int port, final int backlog, final InetAddress inetAddress) {
        return newEchoServer(
                NetObjectFactory.getDefault(),
                port,
                backlog,
                inetAddress);
    }

    public static Server newEchoServer(
            final NetObjectFactory netObjectFactory, final int port) {
        return newEchoServer(
                netObjectFactory,
                port,
                Server.BACKLOG,
                Server.INET_ADDRESS);
    }

    public static Server newEchoServer(
            final NetObjectFactory netObjectFactory,
            final int port,
            final int backlog,
            final InetAddress inetAddress) {
        return new Server(
                new ServerSocketFactory(netObjectFactory),
                port,
                backlog,
                inetAddress,
                new VirtualThreadPerTaskExecutorOrCachedThreadPoolFactory(),
                new EchoWorkerFactory());
    }

    public static String startThenEchoThenStop(
            final Server echoServer,
            final EchoClient echoClient,
            final String string) throws IOException {
        String returningString;
        echoServer.start();
        try {
            returningString = echoClient.echo(
                    string, echoServer.getInetAddress(), echoServer.getPort());
        } finally {
            echoServer.stop();
        }
        return returningString;
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

}
