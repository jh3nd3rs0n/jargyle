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
    private final ExecutorFactory multipleThreadsExecutorFactory;

    /**
     * The {@code ServerSocketFactory} used for creating a
     * {@code ServerSocket}.
     */
    private final ServerSocketFactory serverSocketFactory;

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
     * Constructs a {@code TestServer} with the provided
     * {@code ServerSocketFactory} used for creating a {@code ServerSocket},
     * the provided specified port, the provided {@code ExecutorFactory} used
     * for creating an {@code Executor} to execute {@code Worker}s, and the
     * provided {@code WorkerFactory} used for creating {@code Worker}s.
     *
     * @param serverSockFactory           the provided
     *                                    {@code ServerSocketFactory} used for
     *                                    creating a {@code ServerSocket}
     * @param prt                         the provided specified port
     * @param multiThreadsExecutorFactory the provided {@code ExecutorFactory}
     *                                    used for creating an
     *                                    {@code Executor} to execute
     *                                    {@code Worker}s
     * @param factory                     the provided {@code WorkerFactory}
     *                                    used for creating {@code Worker}s
     */
    public TestServer(
            final ServerSocketFactory serverSockFactory,
            final int prt,
            final ExecutorFactory multiThreadsExecutorFactory,
            final WorkerFactory factory) {
        this(
                serverSockFactory,
                prt,
                BACKLOG,
                INET_ADDRESS,
                multiThreadsExecutorFactory,
                factory);
    }

    /**
     * Constructs a {@code TestServer} with the provided
     * {@code ServerSocketFactory} used for creating a {@code ServerSocket},
     * the provided specified port, the provided maximum length of the queue
     * of incoming connections, the provided binding {@code InetAddress}, the
     * provided {@code ExecutorFactory} used for creating an {@code Executor}
     * to execute {@code Worker}s, and the provided {@code WorkerFactory} used
     * for creating {@code Worker}s.
     *
     * @param serverSockFactory           the provided
     *                                    {@code ServerSocketFactory} used for
     *                                    creating a {@code ServerSocket}
     * @param prt                         the provided specified port
     * @param bklog                       the provided maximum length of the
     *                                    queue of incoming connections
     * @param bindInetAddr                the provided binding
     *                                    {@code InetAddress}
     * @param multiThreadsExecutorFactory the provided {@code ExecutorFactory}
     *                                    used for creating an
     *                                    {@code Executor} to execute
     *                                    {@code Worker}s
     * @param factory                     the provided {@code WorkerFactory}
     *                                    used for creating {@code Worker}s
     */
    public TestServer(
            final ServerSocketFactory serverSockFactory,
            final int prt,
            final int bklog,
            final InetAddress bindInetAddr,
            final ExecutorFactory multiThreadsExecutorFactory,
            final WorkerFactory factory) {
        this.backlog = bklog;
        this.bindInetAddress = bindInetAddr;
        this.executor = null;
        this.multipleThreadsExecutorFactory = multiThreadsExecutorFactory;
        this.port = -1;
        this.serverSocket = null;
        this.serverSocketFactory = serverSockFactory;
        this.specifiedPort = prt;
        this.state = State.STOPPED;
        this.workerFactory = factory;
    }

    /**
     * Returns the {@code boolean} value indicating if the provided
     * {@code Throwable} is an instance of {@code SocketException} or has
     * an instance of {@code SocketException}.
     *
     * @param t the provided {@code Throwable}
     * @return the {@code boolean} value indicating if the provided
     * {@code Throwable} is an instance of {@code SocketException} or has
     * an instance of {@code SocketException}
     */
    private static boolean isOrHasInstanceOfSocketException(
            final Throwable t) {
        if (t instanceof SocketException) {
            return true;
        }
        Throwable cause = t.getCause();
        return cause != null && isOrHasInstanceOfSocketException(cause);
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
        this.serverSocket = this.serverSocketFactory.newServerSocket(
                this.specifiedPort, this.backlog, this.bindInetAddress);
        this.port = this.serverSocket.getLocalPort();
        this.executor = Executors.newSingleThreadExecutor();
        this.executor.execute(new Listener(
                this.serverSocket,
                this.multipleThreadsExecutorFactory,
                this.workerFactory));
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
     * A default implementation of {@code ServerSocketFactory}. This
     * implementation creates a plain {@code ServerSocket}.
     */
    public static final class DefaultServerSocketFactory
            extends ServerSocketFactory {

        /**
         * Constructs a {@code DefaultServerSocketFactory}.
         */
        public DefaultServerSocketFactory() {
        }

        @Override
        public ServerSocket newServerSocket(
                final int port,
                final int backlog,
                final InetAddress bindInetAddress) throws IOException {
            return new ServerSocket(port, backlog, bindInetAddress);
        }

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
         * The {@code Logger}.
         */
        private final Logger logger;

        /**
         * The {@code ExecutorFactory} used for creating an {@code Executor} to
         * execute {@code Worker}s.
         */
        private final ExecutorFactory multiThreadsExecutorFactory;

        /**
         * The {@code ServerSocket}.
         */
        private final ServerSocket serverSocket;

        /**
         * The {@code WorkerFactory} used for creating {@code Worker}s.
         */
        private final WorkerFactory workerFactory;

        /**
         * Constructs a {@code Listener} with the provided
         * {@code ServerSocket}, the provided {@code ExecutorFactory} used for
         * creating an {@code Executor} to execute {@code Worker}s, and the
         * provided {@code WorkerFactory} used for creating {@code Worker}s.
         *
         * @param serverSock                  the provided {@code ServerSocket}
         * @param multiThreadsExecutorFactory the provided
         *                                    {@code ExecutorFactory} used for
         *                                    creating an {@code Executor} to
         *                                    execute {@code Worker}s
         * @param factory                     the provided
         *                                    {@code WorkerFactory} used for
         *                                    creating {@code Worker}s
         */
        public Listener(
                final ServerSocket serverSock,
                final ExecutorFactory multiThreadsExecutorFactory,
                final WorkerFactory factory) {
            this.multiThreadsExecutorFactory = multiThreadsExecutorFactory;
            this.logger = LoggerFactory.getLogger(Listener.class);
            this.serverSocket = serverSock;
            this.workerFactory = factory;
        }

        @Override
        public void run() {
            ExecutorService executor =
                    this.multiThreadsExecutorFactory.newExecutor();
            try {
                while (true) {
                    try {
                        Socket clientSocket = this.serverSocket.accept();
                        executor.execute(this.workerFactory.newWorker(
                                clientSocket));
                    } catch (IOException e) {
                        if (isOrHasInstanceOfSocketException(e)) {
                            // closed by TestServer.stop()
                            break;
                        }
                        this.logger.warn(String.format(
                                        "%s: An exception occurred in "
                                                + "waiting for a client "
                                                + "socket",
                                        this.getClass().getSimpleName()),
                                e);
                    }
                }
            } finally {
                executor.shutdownNow();
            }
        }
    }

    /**
     * A factory that creates {@code ServerSocket}s.
     */
    public static abstract class ServerSocketFactory {

        /**
         * Allows the construction of subclass instances.
         */
        public ServerSocketFactory() {
        }

        /**
         * Returns a new {@code ServerSocket} with the provided specified
         * port, the provided maximum length of the queue of incoming
         * connections, the provided binding {@code InetAddress}.
         *
         * @param port            the provided specified port
         * @param backlog         the provided maximum length of the queue of
         *                        incoming connections
         * @param bindInetAddress the provided binding {@code InetAddress}
         * @return a new {@code ServerSocket} with the provided specified
         * port, the provided maximum length of the queue of incoming
         * connections, the provided binding {@code InetAddress}
         * @throws IOException if there is an error in creating the
         *                     {@code ServerSocket}
         */
        public abstract ServerSocket newServerSocket(
                final int port,
                final int backlog,
                final InetAddress bindInetAddress) throws IOException;

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
        private Socket clientSocket;

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

        /**
         * Sets the client {@code Socket} with the provided {@code Socket}.
         * This method is for setting the {@code Socket} with a wrapped
         * {@code Socket} of the other.
         *
         * @param clientSock the provided {@code Socket}
         */
        protected final void setClientSocket(final Socket clientSock) {
            this.clientSocket = clientSock;
        }

        @Override
        public final void run() {
            try {
                this.doWork();
            } catch (IOException e) {
                if (!isOrHasInstanceOfSocketException(e)) {
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
                                    "%s: An exception occurred in closing "
                                            + "the client socket",
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
