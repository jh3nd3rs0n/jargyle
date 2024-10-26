package com.github.jh3nd3rs0n.jargyle.integration.test;

import com.github.jh3nd3rs0n.jargyle.client.NetObjectFactory;
import com.github.jh3nd3rs0n.jargyle.test.help.net.TestServer;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

public final class EchoTestServerHelper {

    private EchoTestServerHelper() {
    }

    public static TestServer newEchoTestServer(final int port) {
        return newEchoTestServer(
                port,
                TestServer.BACKLOG,
                TestServer.INET_ADDRESS);
    }

    public static TestServer newEchoTestServer(
            final int port, final int backlog, final InetAddress inetAddress) {
        return newEchoTestServer(
                NetObjectFactory.getDefault(),
                port,
                backlog,
                inetAddress);
    }

    public static TestServer newEchoTestServer(
            final NetObjectFactory netObjectFactory, final int port) {
        return newEchoTestServer(
                netObjectFactory,
                port,
                TestServer.BACKLOG,
                TestServer.INET_ADDRESS);
    }

    public static TestServer newEchoTestServer(
            final NetObjectFactory netObjectFactory,
            final int port,
            final int backlog,
            final InetAddress inetAddress) {
        return new TestServer(
                new ServerSocketFactory(netObjectFactory),
                port,
                backlog,
                inetAddress,
                new VirtualThreadPerTaskExecutorOrCachedThreadPoolFactory(),
                new EchoWorkerFactory());
    }

    public static String startThenEchoThenStop(
            final TestServer echoTestServer,
            final EchoTestClient echoTestClient,
            final String string) throws IOException {
        String returningString;
        echoTestServer.start();
        try {
            returningString = echoTestClient.echo(
                    string, echoTestServer.getInetAddress(), echoTestServer.getPort());
        } finally {
            if (!echoTestServer.getState().equals(TestServer.State.STOPPED)) {
                echoTestServer.stop();
            }
        }
        return returningString;
    }

    private static final class EchoWorker extends TestServer.Worker {

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
            extends TestServer.WorkerFactory {

        public EchoWorkerFactory() {
        }

        @Override
        public TestServer.Worker newWorker(final Socket clientSocket) {
            return new EchoWorker(clientSocket);
        }

    }

    private static final class ServerSocketFactory
            extends TestServer.ServerSocketFactory {

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
