package com.github.jh3nd3rs0n.jargyle.test.help.net;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * A UDP server used for clients to test against.
 */
public final class DatagramTestServer {

    /**
     * The default buffer size for receiving {@code DatagramPacket}s.
     */
    public static final int BUFFER_SIZE = 1024;

    /**
     * The default binding {@code InetAddress}.
     */
    public static final InetAddress INET_ADDRESS = InetAddress.getLoopbackAddress();

    /**
     * The binding {@code InetAddress} of this {@code DatagramTestServer}.
     */
    private final InetAddress bindInetAddress;

    /**
     * The {@code ExecutorFactory} used for creating an {@code Executor} to
     * execute {@code Worker}s.
     */
    private final ExecutorFactory executorFactory;

    /**
     * The specified binding port of this {@code DatagramTestServer}.
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
     * The actual binding port of this {@code DatagramTestServer}.
     */
    private int port;

    /**
     * The {@code DatagramSocket} that receives {@code DatagramPacket}s from
     * clients and sends {@code DatagramPacket}s to clients.
     */
    private DatagramSocket serverSocket;

    /**
     * The {@code State} of this {@code DatagramTestServer}.
     */
    private State state;

    /**
     * Constructs a {@code DatagramTestServer} with the provided specified
     * binding port, the provided {@code ExecutorFactory}, and the provided
     * {@code WorkerFactory}.
     *
     * @param prt      the provided specified binding port
     * @param factory1 the provided {@code ExecutorFactory}
     * @param factory2 the provided {@code WorkerFactory}
     */
    public DatagramTestServer(
            final int prt,
            final ExecutorFactory factory1,
            final WorkerFactory factory2) {
        this(prt, INET_ADDRESS, factory1, factory2);
    }

    /**
     * Constructs a {@code DatagramTestServer} with the provided specified
     * binding port, the provided binding {@code InetAddress}, the provided
     * {@code ExecutorFactory}, and the provided {@code WorkerFactory}.
     *
     * @param prt          the provided specified binding port
     * @param bindInetAddr the provided binding {@code InetAddress}
     * @param factory1     the provided {@code ExecutorFactory}
     * @param factory2     the provided {@code WorkerFactory}
     */
    public DatagramTestServer(
            final int prt,
            final InetAddress bindInetAddr,
            final ExecutorFactory factory1,
            final WorkerFactory factory2) {
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
     * Returns the binding {@code InetAddress} of this
     * {@code DatagramTestServer}.
     *
     * @return the binding {@code InetAddress} of this
     * {@code DatagramTestServer}
     */
    public InetAddress getInetAddress() {
        return this.bindInetAddress;
    }

    /**
     * Returns the actual binding port of this {@code DatagramTestServer}.
     *
     * @return the actual binding port of this {@code DatagramTestServer}
     */
    public int getPort() {
        return this.port;
    }

    /**
     * Returns the {@code State} of this {@code DatagramTestServer}.
     *
     * @return the {@code State} of this {@code DatagramTestServer}
     */
    public State getState() {
        return this.state;
    }

    /**
     * Starts this {@code DatagramTestServer}.
     *
     * @throws IOException if there is an error in starting this
     *                     {@code DatagramTestServer}
     */
    public void start() throws IOException {
        this.serverSocket = new DatagramSocket(
                this.specifiedPort, this.bindInetAddress);
        this.port = this.serverSocket.getLocalPort();
        this.executor = Executors.newSingleThreadExecutor();
        this.executor.execute(new Listener(
                this.serverSocket, this.executorFactory, this.workerFactory));
        this.state = State.STARTED;
    }

    /**
     * Stops this {@code DatagramTestServer}.
     *
     * @throws IOException if there is an error in stopping this
     *                     {@code DatagramTestServer}
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
     * The state of the {@code DatagramTestServer}.
     */
    public enum State {

        /**
         * The {@code DatagramTestServer} has been started.
         */
        STARTED,

        /**
         * The {@code DatagramTestServer} has been stopped.
         */
        STOPPED;

    }

    /**
     * A default implementation of {@code Worker}. This implementation does
     * nothing.
     */
    private static final class DefaultWorker extends Worker {

        /**
         * Constructs a {@code DefaultWorker} with the provided
         * {@code DatagramSocket} for sending {@code DatagramPacket}s to the
         * client and the provided {@code DatagramPacket} from the client.
         *
         * @param serverSock the provided {@code DatagramSocket} for sending
         *                   {@code DatagramPacket}s to the client
         * @param pckt       the provided {@code DatagramPacket} from the client.
         */
        public DefaultWorker(
                final DatagramSocket serverSock, final DatagramPacket pckt) {
            super(serverSock, pckt);
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
        public Worker newWorker(
                final DatagramSocket serverSocket, final DatagramPacket packet) {
            return new DefaultWorker(serverSocket, packet);
        }

    }

    /**
     * Continues to listen for a {@code DatagramPacket} from a client, receive
     * a {@code DatagramPacket}, and then execute a new {@code Worker} to
     * handle the received {@code DatagramPacket}.
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
         * The {@code DatagramSocket} that receives {@code DatagramPacket}s from
         * clients and sends {@code DatagramPacket}s to clients.
         */
        private final DatagramSocket serverSocket;

        /**
         * The {@code WorkerFactory} used to create {@code Worker}s.
         */
        private final WorkerFactory workerFactory;

        /**
         * Constructs a {@code Listener} with the provided
         * {@code DatagramSocket} that receives {@code DatagramPacket}s from
         * clients and sends {@code DatagramPacket}s to clients, the provided
         * {@code ExecutorFactory}, and the provided {@code WorkerFactory}.
         *
         * @param serverSock the provided {@code DatagramSocket} that receives
         *                   {@code DatagramPacket}s from clients and sends
         *                   {@code DatagramPacket}s to clients
         * @param factory1   the provided {@code ExecutorFactory}
         * @param factory2   the provided {@code WorkerFactory}
         */
        public Listener(
                final DatagramSocket serverSock,
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
                        byte[] buffer = new byte[BUFFER_SIZE];
                        DatagramPacket packet = new DatagramPacket(
                                buffer, buffer.length);
                        this.serverSocket.receive(packet);
                        executor.execute(
                                this.workerFactory.newWorker(
                                        this.serverSocket, packet));
                    } catch (SocketException e) {
                        break;
                    } catch (IOException e) {
                        this.logger.warn(String.format(
                                        "%s: An error occurred in waiting for a UDP packet",
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
     * Performs work on the {@code DatagramPacket} from the client.
     */
    public static abstract class Worker implements Runnable {

        /**
         * The {@code Logger}.
         */
        private final Logger logger;

        /**
         * The {@code DatagramPacket} from the client.
         */
        private final DatagramPacket packet;

        /**
         * The {@code DatagramSocket} that receives {@code DatagramPacket}s from
         * clients and sends {@code DatagramPacket}s to clients.
         */
        private final DatagramSocket serverSocket;

        /**
         * Constructs a {@code Worker} with the provided
         * {@code DatagramSocket} that receives {@code DatagramPacket}s from
         * clients and sends {@code DatagramPacket}s to clients and the
         * provided {@code DatagramPacket} from the client.
         *
         * @param serverSock the provided {@code DatagramSocket} that receives
         *                   {@code DatagramPacket}s from clients and sends
         *                   {@code DatagramPacket}s to clients
         * @param pckt       the provided {@code DatagramPacket} from the client
         */
        public Worker(
                final DatagramSocket serverSock,
                final DatagramPacket pckt) {
            this.logger = LoggerFactory.getLogger(Worker.class);
            this.packet = pckt;
            this.serverSocket = serverSock;
        }

        /**
         * Performs the work on the {@code DatagramPacket} from the client.
         *
         * @throws IOException if there is an error in handing the
         *                     {@code DatagramPacket} from the client
         */
        protected abstract void doWork() throws IOException;

        /**
         * Returns the {@code DatagramPacket} from the client.
         *
         * @return the {@code DatagramPacket} from the client
         */
        protected final DatagramPacket getPacket() {
            return this.packet;
        }

        /**
         * Returns the {@code DatagramSocket} that receives
         * {@code DatagramPacket}s from clients and sends
         * {@code DatagramPacket}s to clients.
         *
         * @return the {@code DatagramSocket} that receives
         * {@code DatagramPacket}s from clients and sends
         * {@code DatagramPacket}s to clients
         */
        protected final DatagramSocket getServerSocket() {
            return this.serverSocket;
        }

        @Override
        public final void run() {
            try {
                this.doWork();
            } catch (SocketException ignored) {
            } catch (IOException e) {
                this.logger.warn(String.format(
                                "%s: %s",
                                this.getClass().getSimpleName(),
                                e),
                        e);
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
         * Returns a new {@code Worker} with the provided
         * {@code DatagramSocket} that receives {@code DatagramPacket}s from
         * clients and sends {@code DatagramPacket}s to clients and the
         * provided {@code DatagramPacket} from the client.
         *
         * @param serverSocket the provided {@code DatagramSocket} that receives
         *                     {@code DatagramPacket}s from clients and sends
         *                     {@code DatagramPacket}s to clients
         * @param packet       the provided {@code DatagramPacket} from the client
         * @return a new {@code Worker} with the provided
         * {@code DatagramSocket} that receives {@code DatagramPacket}s from
         * clients and sends {@code DatagramPacket}s to clients and the
         * provided {@code DatagramPacket} from the client
         */
        public abstract Worker newWorker(
                final DatagramSocket serverSocket, final DatagramPacket packet);

    }

}
