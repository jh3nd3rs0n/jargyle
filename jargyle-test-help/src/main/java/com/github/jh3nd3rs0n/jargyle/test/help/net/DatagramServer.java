package com.github.jh3nd3rs0n.jargyle.test.help.net;

import com.github.jh3nd3rs0n.jargyle.test.help.concurrent.ExecutorsHelper;
import com.github.jh3nd3rs0n.jargyle.test.help.thread.ThreadHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.*;
import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.ReentrantLock;

/**
 * A UDP server used for clients to test against.
 */
public final class DatagramServer {

    /**
     * The default binding {@code InetAddress}.
     */
    public static final InetAddress INET_ADDRESS = InetAddress.getLoopbackAddress();

    /**
     * The default buffer size for receiving {@code DatagramPacket}s.
     */
    public static final int RECEIVE_BUFFER_SIZE = 65535;

    /**
     * {@code DtlsDatagramSocket$ConnectionClosedException} class name.
     */
    private static final String DTLS_DATAGRAM_SOCKET_CONNECTION_CLOSED_EXCEPTION_CLASS_NAME =
            "com.github.jh3nd3rs0n.jargyle.internal.net.ssl.DtlsDatagramSocket$ConnectionClosedException";

    /**
     * {@code DtlsDatagramSocket$ConnectionClosedException} class.
     */
    private static Class<?> dtlsDatagramSocketConnectionClosedExceptionClass;

    /**
     * The binding {@code InetAddress} of this {@code DatagramServer}.
     */
    private final InetAddress bindInetAddress;

    /**
     * The {@code ServerDatagramSocketFactory} used for creating a server
     * {@code DatagramSocket}.
     */
    private final ServerDatagramSocketFactory serverDatagramSocketFactory;

    /**
     * The specified binding port of this {@code DatagramServer}.
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
     * The actual binding port of this {@code DatagramServer}.
     */
    private int port;

    /**
     * The {@code DatagramSocket} that receives {@code DatagramPacket}s from
     * clients and sends {@code DatagramPacket}s to clients.
     */
    private DatagramSocket serverSocket;

    /**
     * The {@code State} of this {@code DatagramServer}.
     */
    private State state;

    /**
     * Constructs a {@code DatagramServer} with the provided
     * {@code ServerDatagramSocketFactory} used for creating a server
     * {@code DatagramSocket}, the provided specified binding port, and the
     * provided {@code WorkerFactory} used for creating {@code Worker}s.
     *
     * @param serverDatagramSockFactory the provided
     *                                  {@code ServerDatagramSocketFactory} used
     *                                  for creating a server
     *                                  {@code DatagramSocket}
     * @param prt                       the provided specified binding port
     * @param factory                   the provided {@code WorkerFactory}
     *                                  used for creating {@code Worker}s
     */
    public DatagramServer(
            final ServerDatagramSocketFactory serverDatagramSockFactory,
            final int prt,
            final WorkerFactory factory) {
        this(
                serverDatagramSockFactory,
                prt,
                INET_ADDRESS,
                factory);
    }

    /**
     * Constructs a {@code DatagramServer} with the provided
     * {@code ServerDatagramSocketFactory} used for creating a server
     * {@code DatagramSocket}, the provided specified binding port, the
     * provided binding {@code InetAddress}, and the provided
     * {@code WorkerFactory} used for creating {@code Worker}s.
     *
     * @param serverDatagramSockFactory the provided
     *                                  {@code ServerDatagramSocketFactory} used
     *                                  for creating a server
     *                                  {@code DatagramSocket}
     * @param prt                       the provided specified binding port
     * @param bindInetAddr              the provided binding
     *                                  {@code InetAddress}
     * @param factory                   the provided {@code WorkerFactory}
     *                                  used for creating {@code Worker}s
     */
    public DatagramServer(
            final ServerDatagramSocketFactory serverDatagramSockFactory,
            final int prt,
            final InetAddress bindInetAddr,
            final WorkerFactory factory) {
        this.bindInetAddress = bindInetAddr;
        this.serverDatagramSocketFactory = serverDatagramSockFactory;
        this.executor = null;
        this.port = -1;
        this.serverSocket = null;
        this.specifiedPort = prt;
        this.state = State.STOPPED;
        this.workerFactory = factory;
    }

    /**
     * Returns the {@code boolean} value indicating if the provided
     * {@code Throwable} is an instance of
     * {@code DtlsDatagramSocket$ConnectionClosedException} or has an instance
     * of {@code DtlsDatagramSocket$ConnectionClosedException}.
     *
     * @param t the provided {@code Throwable}
     * @return the {@code boolean} value indicating if the provided
     * {@code Throwable} is an instance of
     * {@code DtlsDatagramSocket$ConnectionClosedException} or has an instance
     * of {@code DtlsDatagramSocket$ConnectionClosedException}
     */
    private static boolean isOrHasInstanceOfDtlsDatagramSocketConnectionClosedException(
            final Throwable t) {
        if (dtlsDatagramSocketConnectionClosedExceptionClass == null) {
            try {
                dtlsDatagramSocketConnectionClosedExceptionClass = Class.forName(
                        DTLS_DATAGRAM_SOCKET_CONNECTION_CLOSED_EXCEPTION_CLASS_NAME);
            } catch (ClassNotFoundException ignored) {
            }
        }
        if (dtlsDatagramSocketConnectionClosedExceptionClass != null
                && dtlsDatagramSocketConnectionClosedExceptionClass.isInstance(t)) {
            return true;
        }
        Throwable cause = t.getCause();
        return cause != null && isOrHasInstanceOfDtlsDatagramSocketConnectionClosedException(
                cause);
    }

    /**
     * Returns the binding {@code InetAddress} of this
     * {@code DatagramServer}.
     *
     * @return the binding {@code InetAddress} of this
     * {@code DatagramServer}
     */
    public InetAddress getInetAddress() {
        return this.bindInetAddress;
    }

    /**
     * Returns the actual binding port of this {@code DatagramServer}.
     *
     * @return the actual binding port of this {@code DatagramServer}
     */
    public int getPort() {
        return this.port;
    }

    /**
     * Returns the {@code State} of this {@code DatagramServer}.
     *
     * @return the {@code State} of this {@code DatagramServer}
     */
    public State getState() {
        return this.state;
    }

    /**
     * Starts this {@code DatagramServer}.
     *
     * @throws IOException if there is an error in starting this
     *                     {@code DatagramServer}
     */
    public void start() throws IOException {
        this.serverSocket =
                this.serverDatagramSocketFactory.newServerDatagramSocket(
                        this.specifiedPort, this.bindInetAddress);
        this.port = this.serverSocket.getLocalPort();
        this.executor = Executors.newSingleThreadExecutor();
        this.executor.execute(new PacketRelay(
                this.serverSocket,
                this.workerFactory));
        this.state = State.STARTED;
    }

    /**
     * Stops this {@code DatagramServer}.
     *
     * @throws IOException if there is an error in stopping this
     *                     {@code DatagramServer}
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
     * The state of the {@code DatagramServer}.
     */
    public enum State {

        /**
         * The {@code DatagramServer} has been started.
         */
        STARTED,

        /**
         * The {@code DatagramServer} has been stopped.
         */
        STOPPED;

    }

    /**
     * An {@code Object} of two queues: a queue of received
     * {@code DatagramPacket}s from the client and a queue of sendable
     * {@code DatagramPacket} to the client.
     */
    public static final class ClientPackets {

        /**
         * The client {@code SocketAddress}
         */
        private final SocketAddress clientSocketAddress;

        /**
         * The {@code Packets}.
         */
        private final Packets packets;

        /**
         * The {@code boolean} value to indicate if this {@code ClientPackets}
         * is closed for receiving and sending {@code DatagramPacket}s from
         * and to the client.
         */
        private boolean closed;

        /**
         * Constructs a {@code ClientPackets} with the provided client
         * {@code SocketAddress} and the provided {@code Packets}.
         *
         * @param clientSockAddress the provided client {@code SocketAddress}
         * @param pckts             the provided {@code Packets}
         */
        private ClientPackets(
                final SocketAddress clientSockAddress, final Packets pckts) {
            this.clientSocketAddress = clientSockAddress;
            this.packets = pckts;
            this.closed = false;
        }

        /**
         * Adds to the tail of the queue of sendable {@code DatagramPacket}s
         * the provided sendable {@code DatagramPacket} to the client. An
         * {@code IllegalStateException} is thrown if this
         * {@code ClientPackets} is closed.
         *
         * @param packet the provided sendable {@code DatagramPacket} to the
         *               client
         */
        public void addSendable(final DatagramPacket packet) {
            if (this.closed) {
                throw new IllegalStateException("ClientPackets closed");
            }
            this.packets.addSendable(packet);
        }

        /**
         * Closes this {@code ClientPackets} for receiving and sending
         * {@code DatagramPacket}s from and to the client. An
         * {@code IllegalStateException} is thrown if this
         * {@code ClientPackets} is already closed.
         */
        public void close() {
            if (this.closed) {
                throw new IllegalStateException(
                        "ClientPackets is already closed");
            }
            this.packets.removeReceivedQueue(this.clientSocketAddress);
            this.closed = true;
        }

        /**
         * Returns the {@code boolean} value to indicate if this
         * {@code ClientPackets} is closed for receiving and sending
         * {@code DatagramPacket}s from and to the client.
         *
         * @return the {@code boolean} value to indicate if this
         * {@code ClientPackets} is closed for receiving and sending
         * {@code DatagramPacket}s from and to the client
         */
        public boolean isClosed() {
            return this.closed;
        }

        /**
         * Returns a received {@code DatagramPacket} from the client and
         * removes it from the head of the queue of received
         * {@code DatagramPacket}s from the client. A
         * {@code NoSuchElementException} is thrown if there are no received
         * {@code DatagramPacket}s from the client at this time. An
         * {@code IllegalStateException} is thrown if this
         * {@code ClientPackets} is closed.
         *
         * @return a received {@code DatagramPacket} from the client
         */
        public DatagramPacket removeReceived() {
            if (this.closed) {
                throw new IllegalStateException("ClientPackets closed");
            }
            return this.packets.removeReceived(this.clientSocketAddress);
        }

    }

    /**
     * A default implementation of {@code ServerDatagramSocketFactory}. This
     * implementation creates a plain server {@code DatagramSocket}.
     */
    public static final class DefaultServerDatagramSocketFactory
            extends ServerDatagramSocketFactory {

        /**
         * Constructs a {@code DefaultServerDatagramSocketFactory}.
         */
        public DefaultServerDatagramSocketFactory() {
        }

        @Override
        public DatagramSocket newServerDatagramSocket(
                final int port,
                final InetAddress bindInetAddress) throws SocketException {
            return new DatagramSocket(port, bindInetAddress);
        }

    }

    /**
     * Initiates the {@code ReceivingPacketRelay} and the
     * {@code SendingPacketRelay}.
     */
    private static final class PacketRelay implements Runnable {

        /**
         * The {@code Queue} of received {@code DatagramPacket}s.
         */
        private final Queue<DatagramPacket> receivedPackets;

        /**
         * The {@code Queue} of sendable {@code DatagramPacket}s.
         */
        private final Queue<DatagramPacket> sendablePackets;

        /**
         * The server {@code DatagramSocket}.
         */
        private final DatagramSocket serverSocket;

        /**
         * The {@code WorkerFactory} used for creating {@code Worker}s.
         */
        private final WorkerFactory workerFactory;

        /**
         * Constructs a {@code PacketRelay} with the provided server
         * {@code DatagramSocket} and the provided {@code WorkerFactory}
         * used for creating {@code Worker}s.
         *
         * @param serverSock the provided server
         *                   {@code DatagramSocket}
         * @param factory    the provided
         *                   {@code WorkerFactory} used for
         *                   creating {@code Worker}s
         */
        public PacketRelay(
                final DatagramSocket serverSock,
                final WorkerFactory factory) {
            this.receivedPackets = new ConcurrentLinkedQueue<>();
            this.sendablePackets = new ConcurrentLinkedQueue<>();
            this.serverSocket = serverSock;
            this.workerFactory = factory;
        }

        public void run() {
            ExecutorService executor =
                    ExecutorsHelper.newVirtualThreadPerTaskExecutorOrElse(
                            ExecutorsHelper.newFixedThreadPoolBuilder(3));
            try {
                executor.execute(new ReceivingPacketRelay(
                        this.serverSocket, this.receivedPackets));
                executor.execute(new ForwardingPacketRelay(
                        this.receivedPackets,
                        this.sendablePackets,
                        this.workerFactory));
                executor.execute(new SendingPacketRelay(
                        this.serverSocket, this.sendablePackets));
                do {
                    ThreadHelper.interruptibleSleepForThreeSeconds();
                } while (!this.serverSocket.isClosed());
            } finally {
                executor.shutdownNow();
            }
        }

    }

    /**
     * An {@code Object} of {@code Queue}s: {@code Queue}s of received
     * {@code DatagramPacket}s with each {@code Queue} associated with a
     * client's {@code SocketAddress} and a {@code Queue} of sendable
     * {@code DatagramPacket} to the clients.
     */
    private static final class Packets {

        /**
         * The {@code ReentrantLock} for controlling thread access to the
         * {@code Map} of {@code Queue}s of received {@code DatagramPacket}s
         * with each {@code Queue} associated with a client's
         * {@code SocketAddress}.
         */
        private final ReentrantLock receivedPacketQueueMapLock;

        /**
         * The {@code Map} of {@code Queue}s of received
         * {@code DatagramPacket}s with each {@code Queue} associated with a
         * client's {@code SocketAddress}.
         */
        private final Map<SocketAddress, Queue<DatagramPacket>> receivedPacketQueueMap;

        /**
         * The {@code Queue} of sendable {@code DatagramPacket}s to the
         * clients.
         */
        private final Queue<DatagramPacket> sendablePacketQueue;

        /**
         * Constructs a {@code Packets}.
         */
        public Packets() {
            this.receivedPacketQueueMapLock = new ReentrantLock();
            this.receivedPacketQueueMap = new HashMap<>();
            this.sendablePacketQueue = new ConcurrentLinkedQueue<>();
        }

        /**
         * Adds to the tail of the {@code Queue} of received
         * {@code DatagramPacket}s from the client the provided received
         * {@code DatagramPacket} from the client. If the {@code Queue} of
         * received {@code DatagramPacket}s from the client does not exist,
         * one will be created with the provided received
         * {@code DatagramPacket} added.
         *
         * @param packet the provided received {@code DatagramPacket} from the
         *               client
         */
        public void addReceived(final DatagramPacket packet) {
            SocketAddress socketAddress = packet.getSocketAddress();
            this.receivedPacketQueueMapLock.lock();
            try {
                if (!this.hasReceivedFrom(socketAddress)) {
                    Queue<DatagramPacket> queue =
                            new ConcurrentLinkedQueue<>();
                    queue.add(packet);
                    this.receivedPacketQueueMap.put(socketAddress, queue);
                } else {
                    this.receivedPacketQueueMap.get(socketAddress).add(
                            packet);
                }
            } finally {
                this.receivedPacketQueueMapLock.unlock();
            }
        }

        /**
         * Adds to the tail of the {@code Queue} of sendable
         * {@code DatagramPacket}s to the clients the provided sendable
         * {@code DatagramPacket} to the client.
         *
         * @param packet the provided sendable {@code DatagramPacket} to the
         *               client
         */
        public void addSendable(final DatagramPacket packet) {
            this.sendablePacketQueue.add(packet);
        }

        /**
         * Returns the {@code boolean} value to indicate if this
         * {@code Packets} has a received {@code DatagramPacket} from the
         * client based on the provided client's {@code SocketAddress}.
         *
         * @param socketAddress the provided client's {@code SocketAddress}
         * @return the {@code boolean} value to indicate if this
         * {@code Packets} has a received {@code DatagramPacket} from the
         * client based on the provided client's {@code SocketAddress}
         */
        public boolean hasReceivedFrom(final SocketAddress socketAddress) {
            this.receivedPacketQueueMapLock.lock();
            try {
                return this.receivedPacketQueueMap.containsKey(socketAddress)
                        && !this.receivedPacketQueueMap.get(socketAddress).isEmpty();
            } finally {
                this.receivedPacketQueueMapLock.unlock();
            }
        }

        /**
         * Returns a received {@code DatagramPacket} from the client based on
         * the provided client's {@code SocketAddress} and removes it from the
         * head of the {@code Queue} of received {@code DatagramPacket}s from
         * the client. A {@code NoSuchElementException} is thrown if there are
         * no received {@code DatagramPacket}s from the client at this time.
         *
         * @return a received {@code DatagramPacket} from the client based on
         * the client's {@code SocketAddress}
         */
        public DatagramPacket removeReceived(
                final SocketAddress socketAddress) {
            this.receivedPacketQueueMapLock.lock();
            try {
                if (this.hasReceivedFrom(socketAddress)) {
                    return this.receivedPacketQueueMap.get(
                            socketAddress).remove();
                }
            } finally {
                this.receivedPacketQueueMapLock.unlock();
            }
            throw new NoSuchElementException();
        }

        /**
         * Removes from this {@code Packets} the {@code Queue} of received
         * {@code DatagramPacket}s from the client based on the provided
         * client's {@code SocketAddress}.
         *
         * @param socketAddress the provided client's {@code SocketAddress}
         */
        public void removeReceivedQueue(final SocketAddress socketAddress) {
            this.receivedPacketQueueMapLock.lock();
            try {
                if (this.receivedPacketQueueMap.containsKey(socketAddress)) {
                    this.receivedPacketQueueMap.get(socketAddress).clear();
                    this.receivedPacketQueueMap.remove(socketAddress);
                }
            } finally {
                this.receivedPacketQueueMapLock.unlock();
            }
        }

        /**
         * Returns a sendable {@code DatagramPacket} to the client and removes
         * it from the head of the {@code Queue} of sendable
         * {@code DatagramPacket}s to the clients. A
         * {@code NoSuchElementException} is thrown if there are no sendable
         * {@code DatagramPacket}s to the client at this time.
         *
         * @return a sendable {@code DatagramPacket} to the client
         */
        public DatagramPacket removeSendable() {
            return this.sendablePacketQueue.remove();
        }

    }

    /**
     * Receives {@code DatagramPacket}s from the clients.
     */
    private static final class ReceivingPacketRelay implements Runnable {

        /**
         * The {@code Logger}.
         */
        private final Logger logger;

        /**
         * The {@code Queue} of received {@code DatagramPacket}s.
         */
        private final Queue<DatagramPacket> receivedPackets;

        /**
         * The server {@code DatagramSocket}.
         */
        private final DatagramSocket serverSocket;

        /**
         * Constructs a {@code ReceivingPacketRelay} with the provided server
         * {@code DatagramSocket} and the provided {@code Queue} of received
         * {@code DatagramPacket}s.
         *
         * @param serverSock    the provided server {@code DatagramSocket}
         * @param receivedPckts the provided {@code Queue} of received
         *                      {@code DatagramPacket}s
         */
        public ReceivingPacketRelay(
                final DatagramSocket serverSock,
                final Queue<DatagramPacket> receivedPckts) {
            this.logger = LoggerFactory.getLogger(ReceivingPacketRelay.class);
            this.receivedPackets = receivedPckts;
            this.serverSocket = serverSock;
        }

        @Override
        public void run() {
            while (true) {
                try {
                    byte[] buffer = new byte[RECEIVE_BUFFER_SIZE];
                    DatagramPacket receivedPacket = new DatagramPacket(
                            buffer, buffer.length);
                    this.serverSocket.receive(receivedPacket);
                    this.receivedPackets.add(receivedPacket);
                } catch (SocketException e) {
                    // closed by DatagramServer.stop()
                    break;
                } catch (IOException e) {
                    if (!isOrHasInstanceOfDtlsDatagramSocketConnectionClosedException(e)) {
                        this.logger.warn(String.format(
                                        "%s: An exception occurred in " +
                                                "waiting for a UDP packet",
                                        this.getClass().getSimpleName()),
                                e);
                    }
                }
            }
        }
    }

    /**
     * Forwards the {@code DatagramPacket} from the
     * {@code ReceivingPacketRelay} to a {@code Worker} and forwards the
     * {@code DatagramPacket} from a {@code Worker} to the
     * {@code SendingPacketRelay}.
     */
    private static final class ForwardingPacketRelay implements Runnable {

        /**
         * The {@code Queue} of received {@code DatagramPacket}s.
         */
        private final Queue<DatagramPacket> receivedPackets;

        /**
         * The {@code Queue} of sendable {@code DatagramPacket}s.
         */
        private final Queue<DatagramPacket> sendablePackets;

        /**
         * The {@code WorkerFactory} used for creating {@code Worker}s.
         */
        private final WorkerFactory workerFactory;

        /**
         * Constructs a {@code ForwardingPacketRelay} with the provided
         * {@code Queue} of received {@code DatagramPacket}s, the provided
         * {@code Queue} of sendable {@code DatagramPacket}s, and the provided
         * {@code WorkerFactory} used for creating {@code Worker}s.
         *
         * @param receivedPckts the provided {@code Queue} of
         *                      received {@code DatagramPacket}s
         * @param sendablePckts the provided {@code Queue} of
         *                      sendable {@code DatagramPacket}s
         * @param factory       the provided
         *                      {@code WorkerFactory} used for
         *                      creating {@code Worker}s
         */
        public ForwardingPacketRelay(
                final Queue<DatagramPacket> receivedPckts,
                final Queue<DatagramPacket> sendablePckts,
                final WorkerFactory factory) {
            this.receivedPackets = receivedPckts;
            this.sendablePackets = sendablePckts;
            this.workerFactory = factory;
        }

        @Override
        public void run() {
            Packets packets = new Packets();
            ExecutorService executor =
                    ExecutorsHelper.newVirtualThreadPerTaskExecutorOrElse(
                            ExecutorsHelper.newCachedThreadPoolBuilder());
            try {
                while (true) {
                    try {
                        DatagramPacket receivedPacket =
                                this.receivedPackets.remove();
                        if (packets.hasReceivedFrom(
                                receivedPacket.getSocketAddress())) {
                            packets.addReceived(receivedPacket);
                        } else {
                            packets.addReceived(receivedPacket);
                            Worker worker = this.workerFactory.newWorker(
                                    new ClientPackets(
                                            receivedPacket.getSocketAddress(),
                                            packets));
                            executor.execute(worker);
                        }
                    } catch (NoSuchElementException ignored) {
                    }
                    try {
                        DatagramPacket sendablePacket =
                                packets.removeSendable();
                        this.sendablePackets.add(sendablePacket);
                    } catch (NoSuchElementException ignored) {
                    }
                }
            } finally {
                executor.shutdownNow();
            }
        }

    }

    /**
     * Sends {@code DatagramPacket}s to the clients.
     */
    private static final class SendingPacketRelay implements Runnable {

        /**
         * The {@code Logger}.
         */
        private final Logger logger;

        /**
         * The {@code Queue} of sendable {@code DatagramPacket}s.
         */
        private final Queue<DatagramPacket> sendablePackets;

        /**
         * The server {@code DatagramSocket}.
         */
        private final DatagramSocket serverSocket;

        /**
         * Constructs a {@code SendingPacketRelay} with the provided server
         * {@code DatagramSocket} and the provided {@code Queue} of sendable
         * {@code DatagramPacket}s.
         *
         * @param serverSock    the provided server {@code DatagramSocket}
         * @param sendablePckts the provided {@code Queue} of sendable
         *                      {@code DatagramPacket}s
         */
        public SendingPacketRelay(
                final DatagramSocket serverSock,
                final Queue<DatagramPacket> sendablePckts) {
            this.logger = LoggerFactory.getLogger(SendingPacketRelay.class);
            this.sendablePackets = sendablePckts;
            this.serverSocket = serverSock;
        }

        @Override
        public void run() {
            while (true) {
                try {
                    DatagramPacket sentPacket;
                    try {
                        sentPacket = this.sendablePackets.remove();
                    } catch (NoSuchElementException e) {
                        continue;
                    }
                    this.serverSocket.send(sentPacket);
                } catch (SocketException e) {
                    // closed by DatagramServer.stop()
                    break;
                } catch (IOException e) {
                    if (!isOrHasInstanceOfDtlsDatagramSocketConnectionClosedException(e)) {
                        this.logger.warn(String.format(
                                        "%s: An exception occurred in "
                                                + "sending a UDP packet",
                                        this.getClass().getSimpleName()),
                                e);
                    }
                }
            }
        }
    }

    /**
     * A factory that creates server {@code DatagramSocket}s.
     */
    public static abstract class ServerDatagramSocketFactory {

        /**
         * Allows the construction of subclass instances.
         */
        public ServerDatagramSocketFactory() {
        }

        /**
         * Returns a new server {@code DatagramSocket} with the provided
         * specified port and the provided binding {@code InetAddress}.
         *
         * @param port            the provided specified port
         * @param bindInetAddress the provided binding {@code InetAddress}
         * @return a new server {@code DatagramSocket} with the provided
         * specified port and the provided binding {@code InetAddress}
         * @throws SocketException if there is an error in creating the
         *                         server {@code DatagramSocket}
         */
        public abstract DatagramSocket newServerDatagramSocket(
                final int port,
                final InetAddress bindInetAddress) throws SocketException;

    }

    /**
     * Performs work on the received {@code DatagramPacket}s from the client
     * and provides if any to the client sendable {@code DatagramPacket}s.
     */
    public static abstract class Worker implements Runnable {

        /**
         * The {@code ClientPackets}.
         */
        private final ClientPackets clientPackets;

        /**
         * Constructs a {@code Worker} with the provided {@code ClientPackets}.
         *
         * @param clientPckts the provided {@code ClientPackets}
         */
        public Worker(final ClientPackets clientPckts) {
            this.clientPackets = clientPckts;
        }

        /**
         * Performs work on the received {@code DatagramPacket}s from the
         * client.
         */
        protected abstract void doWork();

        /**
         * Returns the {@code ClientPackets}.
         *
         * @return the {@code ClientPackets}
         */
        protected final ClientPackets getClientPackets() {
            return this.clientPackets;
        }

        @Override
        public final void run() {
            try {
                this.doWork();
            } finally {
                if (!this.clientPackets.isClosed()) {
                    this.clientPackets.close();
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
         * Returns a new {@code Worker} with the provided
         * {@code ClientPackets}.
         *
         * @param clientPckts the provided {@code ClientPackets}
         * @return a new {@code Worker} with the provided {@code ClientPackets}
         */
        public abstract Worker newWorker(final ClientPackets clientPckts);

    }

}
