package com.github.jh3nd3rs0n.jargyle.test.help.net;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * A server used for clients to test against.
 */
public final class TestServer {

    /**
     * The default maximum length of the queue of incoming connections.
     */
    public static final int BACKLOG = 50;

    /**
     * The default binding {@code InetAddress}.
     */
    public static final InetAddress INET_ADDRESS = InetAddress.getLoopbackAddress();

    /**
     * The maximum length of the queue of incoming connections.
     */
    private final int backlog;

    /**
     * The binding {@code InetAddress} of this {@code TestServer}.
     */
    private final InetAddress bindInetAddress;

    /**
     * The {@code ExecutorFactory} used for creating an {@code Executor} to
     * execute {@code Worker}s.
     */
    private final ExecutorFactory executorFactory;

    /**
     * The specified binding port of this {@code TestServer}.
     */
    private final int specifiedPort;

    /**
     * The {@code WorkerFactory} used for creating {@code Worker}s.
     */
    private final WorkerFactory workerFactory;

    /**
     * The {@code Executor}.
     */
    private ExecutorService executor;

    /**
     * The actual binding port of this {@code TestServer}.
     */
    private int port;

    /**
     * The {@code ServerSocket}.
     */
    private ServerSocket serverSocket;

    /**
     * The {@code State} of this {@code TestServer}.
     */
    private State state;

    /**
     * Constructs a {@code TestServer} with the provided specified port, the
     * provided {@code ExecutorFactory}, and the provided {@code WorkerFactory}.
     *
     * @param prt      the provided specified port
     * @param factory1 the provided {@code ExecutorFactory}
     * @param factory2 the provided {@code WorkerFactory}
     */
    public TestServer(
            final int prt,
            final ExecutorFactory factory1,
            final WorkerFactory factory2) {
        this(prt, BACKLOG, INET_ADDRESS, factory1, factory2);
    }

    /**
     * Constructs a {@code TestServer} with the provided specified port, the
     * provided maximum length of the queue of incoming connections, the
     * provided binding {@code InetAddress}, the provided
     * {@code ExecutorFactory}, and the provided {@code WorkerFactory}.
     *
     * @param prt          the provided specified port
     * @param bklog        the provided maximum length of the queue of incoming
     *                     connections
     * @param bindInetAddr the provided binding {@code InetAddress}
     * @param factory1     the provided {@code ExecutorFactory}
     * @param factory2     the provided {@code WorkerFactory}
     */
    public TestServer(
            final int prt,
            final int bklog,
            final InetAddress bindInetAddr,
            final ExecutorFactory factory1,
            final WorkerFactory factory2) {
        this.backlog = bklog;
        this.bindInetAddress = bindInetAddr;
        this.executor = null;
        this.executorFactory = factory1;
        this.port = -1;
        this.serverSocket = null;
        this.specifiedPort = prt;
        this.state = State.STOPPED;
        this.workerFactory = factory2;
    }

    /**
     * Returns the binding {@code InetAddress} of this {@code TestServer}.
     *
     * @return the binding {@code InetAddress} of this {@code TestServer}
     */
    public InetAddress getInetAddress() {
        return this.bindInetAddress;
    }

    /**
     * Returns the actual binding port of this {@code TestServer}.
     *
     * @return the actual binding port of this {@code TestServer}
     */
    public int getPort() {
        return this.port;
    }

    /**
     * Returns the {@code State} of this {@code TestServer}.
     *
     * @return the {@code State} of this {@code TestServer}
     */
    public State getState() {
        return this.state;
    }

    /**
     * Starts this {@code TestServer}.
     *
     * @throws IOException if there is an error in starting the
     *                     {@code TestServer}
     */
    public void start() throws IOException {
        this.serverSocket = new ServerSocket(
                this.specifiedPort, this.backlog, this.bindInetAddress);
        this.port = this.serverSocket.getLocalPort();
        this.executor = Executors.newSingleThreadExecutor();
        this.executor.execute(new Listener(
                this.serverSocket, this.executorFactory, this.workerFactory));
        this.state = State.STARTED;
    }

    /**
     * Stops this {@code TestServer}.
     *
     * @throws IOException if there is an error in stopping the
     *                     {@code TestServer}
     */
    public void stop() throws IOException {
        this.serverSocket.close();
        this.serverSocket = null;
        this.port = -1;
        this.executor.shutdownNow();
        this.executor = null;
        this.state = State.STOPPED;
    }

    /**
     * The state of the {@code TestServer}.
     */
    public enum State {

        /**
         * The {@code TestServer} has been started.
         */
        STARTED,

        /**
         * The {@code TestServer} has been stopped.
         */
        STOPPED;

    }

    /**
     * A default implementation of {@code Worker}. This implementation does
     * nothing.
     */
    private static final class DefaultWorker extends Worker {

        /**
         * Constructs a {@code DefaultWorker} with the provided client
         * {@code Socket}.
         *
         * @param clientSock the provided client {@code Socket}
         */
        public DefaultWorker(final Socket clientSock) {
            super(clientSock);
        }

        @Override
        protected void doWork() {
            // does nothing
        }

    }

    /**
     * A default implementation of {@code WorkerFactory} that creates
     * {@code Worker}s that do nothing.
     */
    public static final class DefaultWorkerFactory extends WorkerFactory {

        /**
         * Constructs a {@code DefaultWorkerFactory}.
         */
        public DefaultWorkerFactory() {
        }

        @Override
        public Worker newWorker(final Socket clientSocket) {
            return new DefaultWorker(clientSocket);
        }

    }

    /**
     * Continues to listen for a client {@code Socket}, accept the client
     * {@code Socket}, and then execute a new {@code Worker} to handle the
     * client {@code Socket}.
     */
    private static final class Listener implements Runnable {

        /**
         * The {@code ExecutorFactory} used for creating an {@code Executor} to
         * execute {@code Worker}s.
         */
        private final ExecutorFactory executorFactory;

        /**
         * The {@code Logger}.
         */
        private final Logger logger;

        /**
         * The {@code ServerSocket}.
         */
        private final ServerSocket serverSocket;

        /**
         * The {@code WorkerFactory} used to create {@code Worker}s.
         */
        private final WorkerFactory workerFactory;

        /**
         * Constructs a {@code Listener} with the provided
         * {@code ServerSocket}, the provided {@code ExecutorFactory}, and the
         * provided {@code WorkerFactory}.
         *
         * @param serverSock the provided {@code ServerSocket}
         * @param factory1   the provided {@code ExecutorFactory}
         * @param factory2   the provided {@code WorkerFactory}
         */
        public Listener(
                final ServerSocket serverSock,
                final ExecutorFactory factory1,
                final WorkerFactory factory2) {
            this.executorFactory = factory1;
            this.logger = LoggerFactory.getLogger(Listener.class);
            this.serverSocket = serverSock;
            this.workerFactory = factory2;
        }

        @Override
        public void run() {
            ExecutorService executor = this.executorFactory.newExecutor();
            try {
                while (true) {
                    try {
                        Socket clientSocket = this.serverSocket.accept();
                        executor.execute(this.workerFactory.newWorker(
                                clientSocket));
                    } catch (IOException e) {
                        if (ThrowableHelper.isOrHasInstanceOf(
                                e, SocketException.class)) {
                            break;
                        }
                        this.logger.warn(String.format(
                                        "%s: An error occurred in waiting "
                                                + "for a client socket",
                                        this.getClass().getSimpleName()),
                                e);
                        break;
                    }
                }
            } finally {
                executor.shutdownNow();
            }
        }
    }

    /**
     * Performs work on the client {@code Socket}.
     */
    public static abstract class Worker implements Runnable {

        /**
         * The {@code Logger}.
         */
        private final Logger logger;

        /**
         * The client {@code Socket}.
         */
        private final Socket clientSocket;

        /**
         * Constructs a {@code Worker} with the provided client {@code Socket}.
         *
         * @param clientSock the provided client {@code Socket}
         */
        public Worker(final Socket clientSock) {
            this.logger = LoggerFactory.getLogger(Worker.class);
            this.clientSocket = clientSock;
        }

        /**
         * Performs the work on the client {@code Socket}.
         *
         * @throws IOException if there is an error in handling the client
         *                     {@code Socket}
         */
        protected abstract void doWork() throws IOException;

        /**
         * Returns the client {@code Socket}.
         *
         * @return the client {@code Socket}
         */
        protected final Socket getClientSocket() {
            return this.clientSocket;
        }

        @Override
        public final void run() {
            try {
                this.doWork();
            } catch (IOException e) {
                if (!ThrowableHelper.isOrHasInstanceOf(
                        e, SocketException.class)) {
                    this.logger.warn(String.format(
                                    "%s: %s",
                                    this.getClass().getSimpleName(),
                                    e),
                            e);
                }
            } finally {
                try {
                    this.clientSocket.close();
                } catch (IOException e) {
                    this.logger.warn(String.format(
                                    "%s: An error occurred in closing the client socket",
                                    this.getClass().getSimpleName()),
                            e);
                }
            }
        }

    }

    /**
     * A factory that creates {@code Worker}s.
     */
    public static abstract class WorkerFactory {

        /**
         * Allows the construction of subclass instances.
         */
        public WorkerFactory() {
        }

        /**
         * Returns a new {@code Worker} with the provided client
         * {@code Socket}.
         *
         * @param clientSocket the provided client {@code Socket}
         * @return a new {@code Worker} with the provided client
         * {@code Socket}
         */
        public abstract Worker newWorker(final Socket clientSocket);

    }

}
