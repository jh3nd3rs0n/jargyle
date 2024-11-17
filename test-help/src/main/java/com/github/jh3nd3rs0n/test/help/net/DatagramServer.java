package com.github.jh3nd3rs0n.test.help.net;

import com.github.jh3nd3rs0n.test.help.thread.ThreadHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.*;
import java.util.*;
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
     * The {@code ExecutorFactory} used for creating an {@code Executor} to
     * execute the {@code ReceivingPacketRelay} and the
     * {@code SendingPacketRelay}.
     */
    private final ExecutorFactory doubleThreadExecutorFactory;

    /**
     * The {@code ExecutorFactory} used for creating an {@code Executor} to
     * execute {@code Worker}s.
     */
    private final ExecutorFactory multipleThreadsExecutorFactory;

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
     * {@code DatagramSocket}, the provided specified binding port, the
     * provided {@code ExecutorFactory} used for creating an {@code Executor}
     * to execute two threads, the provided {@code ExecutorFactory} used for
     * creating an {@code Executor} to execute {@code Worker}s, and the
     * provided {@code WorkerFactory} used for creating {@code Worker}s.
     *
     * @param serverDatagramSockFactory   the provided
     *                                    {@code ServerDatagramSocketFactory} used
     *                                    for creating a server
     *                                    {@code DatagramSocket}
     * @param prt                         the provided specified binding port
     * @param twoThreadExecutorFactory    the provided {@code ExecutorFactory}
     *                                    used for creating an
     *                                    {@code Executor} to execute two
     *                                    threads
     * @param multiThreadsExecutorFactory the provided {@code ExecutorFactory}
     *                                    used for creating an
     *                                    {@code Executor} to execute
     *                                    {@code Worker}s
     * @param factory                     the provided {@code WorkerFactory}
     *                                    used for creating {@code Worker}s
     */
    public DatagramServer(
            final ServerDatagramSocketFactory serverDatagramSockFactory,
            final int prt,
            final ExecutorFactory twoThreadExecutorFactory,
            final ExecutorFactory multiThreadsExecutorFactory,
            final WorkerFactory factory) {
        this(
                serverDatagramSockFactory,
                prt,
                INET_ADDRESS,
                twoThreadExecutorFactory,
                multiThreadsExecutorFactory,
                factory);
    }

    /**
     * Constructs a {@code DatagramServer} with the provided
     * {@code ServerDatagramSocketFactory} used for creating a server
     * {@code DatagramSocket}, the provided specified binding port, the
     * provided binding {@code InetAddress}, the provided
     * {@code ExecutorFactory} used for creating an {@code Executor} to
     * execute two threads, the provided {@code ExecutorFactory} used for
     * creating an {@code Executor} to execute {@code Worker}s, and the
     * provided {@code WorkerFactory} used for creating {@code Worker}s.
     *
     * @param serverDatagramSockFactory   the provided
     *                                    {@code ServerDatagramSocketFactory} used
     *                                    for creating a server
     *                                    {@code DatagramSocket}
     * @param prt                         the provided specified binding port
     * @param bindInetAddr                the provided binding
     *                                    {@code InetAddress}
     * @param twoThreadExecutorFactory    the provided {@code ExecutorFactory}
     *                                    used for creating an
     *                                    {@code Executor} to execute two
     *                                    threads
     * @param multiThreadsExecutorFactory the provided {@code ExecutorFactory}
     *                                    used for creating an
     *                                    {@code Executor} to execute
     *                                    {@code Worker}s
     * @param factory                     the provided {@code WorkerFactory}
     *                                    used for creating {@code Worker}s
     */
    public DatagramServer(
            final ServerDatagramSocketFactory serverDatagramSockFactory,
            final int prt,
            final InetAddress bindInetAddr,
            final ExecutorFactory twoThreadExecutorFactory,
            final ExecutorFactory multiThreadsExecutorFactory,
            final WorkerFactory factory) {
        this.bindInetAddress = bindInetAddr;
        this.serverDatagramSocketFactory = serverDatagramSockFactory;
        this.doubleThreadExecutorFactory = twoThreadExecutorFactory;
        this.executor = null;
        this.multipleThreadsExecutorFactory = multiThreadsExecutorFactory;
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
                this.doubleThreadExecutorFactory,
                this.multipleThreadsExecutorFactory,
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
         * The {@code ExecutorFactory} used for creating an {@code Executor} to
         * execute the {@code ReceivingPacketRelay} and the
         * {@code SendingPacketRelay}.
         */
        private final ExecutorFactory doubleThreadExecutorFactory;

        /**
         * The {@code ExecutorFactory} used for creating an {@code Executor} to
         * execute {@code Worker}s.
         */
        private final ExecutorFactory multipleThreadsExecutorFactory;

        /**
         * The {@code Packets}.
         */
        private final Packets packets;

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
         * {@code DatagramSocket}, the provided {@code ExecutorFactory} used
         * for creating an {@code Executor} to execute the
         * {@code ReceivingPacketRelay} and the {@code SendingPacketRelay},
         * the provided {@code ExecutorFactory} used for creating an
         * {@code Executor} to execute {@code Worker}s, and the provided
         * {@code WorkerFactory} used for creating {@code Worker}s.
         *
         * @param serverSock                  the provided server
         *                                    {@code DatagramSocket}
         * @param twoThreadExecutorFactory    the provided
         *                                    {@code ExecutorFactory} used for
         *                                    creating an {@code Executor} to
         *                                    execute the
         *                                    {@code ReceivingPacketRelay} and
         *                                    the {@code SendingPacketRelay}
         * @param multiThreadsExecutorFactory the provided
         *                                    {@code ExecutorFactory} used for
         *                                    creating an {@code Executor} to
         *                                    execute {@code Worker}s
         * @param factory                     the provided
         *                                    {@code WorkerFactory} used for
         *                                    creating {@code Worker}s
         */
        public PacketRelay(
                final DatagramSocket serverSock,
                final ExecutorFactory twoThreadExecutorFactory,
                final ExecutorFactory multiThreadsExecutorFactory,
                final WorkerFactory factory) {
            this.doubleThreadExecutorFactory = twoThreadExecutorFactory;
            this.multipleThreadsExecutorFactory = multiThreadsExecutorFactory;
            this.packets = new Packets();
            this.serverSocket = serverSock;
            this.workerFactory = factory;
        }

        public void run() {
            ExecutorService executor =
                    this.doubleThreadExecutorFactory.newExecutor();
            try {
                executor.execute(new ReceivingPacketRelay(
                        this.serverSocket,
                        this.multipleThreadsExecutorFactory,
                        this.packets,
                        this.workerFactory));
                executor.execute(new SendingPacketRelay(
                        this.serverSocket, this.packets));
                do {
                    ThreadHelper.interruptibleSleepForThreeSeconds();
                } while (!this.serverSocket.isClosed());
            } finally {
                executor.shutdownNow();
            }
        }

    }

    /**
     * An {@code Object} of queues: queues of received {@code DatagramPacket}s
     * with each queue associated with a client's {@code SocketAddress} and a
     * queue of sendable {@code DatagramPacket} to the clients.
     */
    private static final class Packets {

        /**
         * The {@code ReentrantLock} for controlling thread access to the
         * {@code Map} of queues of received {@code DatagramPacket}s with each
         * queue associated with a client's {@code SocketAddress}.
         */
        private final ReentrantLock receivedPacketListMapLock;

        /**
         * The {@code Map} of queues of received {@code DatagramPacket}s with
         * each queue associated with a client's {@code SocketAddress}.
         */
        private final Map<SocketAddress, List<DatagramPacket>> receivedPacketListMap;

        /**
         * The queue of sendable {@code DatagramPacket}s to the clients.
         */
        private final List<DatagramPacket> sendablePacketList;

        /**
         * The {@code ReentrantLock} for controlling thread access to the
         * queue of sendable {@code DatagramPacket}s to the clients.
         */
        private final ReentrantLock sendablePacketListLock;

        /**
         * Constructs a {@code Packets}.
         */
        public Packets() {
            this.receivedPacketListMapLock = new ReentrantLock();
            this.receivedPacketListMap = new HashMap<>();
            this.sendablePacketList = new ArrayList<>();
            this.sendablePacketListLock = new ReentrantLock();
        }

        /**
         * Adds to the tail of the queue of received {@code DatagramPacket}s
         * from the client the provided received {@code DatagramPacket} from
         * the client. If the queue of received {@code DatagramPacket}s from
         * the client does not exist, one will be created with the provided
         * received {@code DatagramPacket} added.
         *
         * @param packet the provided received {@code DatagramPacket} from the
         *               client
         */
        public void addReceived(final DatagramPacket packet) {
            SocketAddress socketAddress = packet.getSocketAddress();
            this.receivedPacketListMapLock.lock();
            try {
                if (!this.hasReceivedFrom(socketAddress)) {
                    List<DatagramPacket> list = new ArrayList<>();
                    list.add(0, packet);
                    this.receivedPacketListMap.put(socketAddress, list);
                } else {
                    this.receivedPacketListMap.get(socketAddress).add(
                            0, packet);
                }
            } finally {
                this.receivedPacketListMapLock.unlock();
            }
        }

        /**
         * Adds to the tail of the queue of sendable {@code DatagramPacket}s
         * to the clients the provided sendable {@code DatagramPacket} to the
         * client.
         *
         * @param packet the provided sendable {@code DatagramPacket} to the
         *               client
         */
        public void addSendable(final DatagramPacket packet) {
            this.sendablePacketListLock.lock();
            try {
                this.sendablePacketList.add(0, packet);
            } finally {
                this.sendablePacketListLock.unlock();
            }
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
            this.receivedPacketListMapLock.lock();
            try {
                return this.receivedPacketListMap.containsKey(socketAddress)
                        && !this.receivedPacketListMap.get(socketAddress).isEmpty();
            } finally {
                this.receivedPacketListMapLock.unlock();
            }
        }

        /**
         * Returns a received {@code DatagramPacket} from the client based on
         * the provided client's {@code SocketAddress} and removes it from the
         * head of the queue of received {@code DatagramPacket}s from the
         * client. A {@code NoSuchElementException} is thrown if there are no
         * received {@code DatagramPacket}s from the client at this time.
         *
         * @return a received {@code DatagramPacket} from the client based on
         * the client's {@code SocketAddress}
         */
        public DatagramPacket removeReceived(
                final SocketAddress socketAddress) {
            this.receivedPacketListMapLock.lock();
            try {
                if (this.hasReceivedFrom(socketAddress)) {
                    List<DatagramPacket> list =
                            this.receivedPacketListMap.get(socketAddress);
                    return list.remove(list.size() - 1);
                }
            } finally {
                this.receivedPacketListMapLock.unlock();
            }
            throw new NoSuchElementException();
        }

        /**
         * Removes from this {@code Packets} the queue of received
         * {@code DatagramPacket}s from the client based on the provided
         * client's {@code SocketAddress}.
         *
         * @param socketAddress the provided client's {@code SocketAddress}
         */
        public void removeReceivedQueue(final SocketAddress socketAddress) {
            this.receivedPacketListMapLock.lock();
            try {
                if (this.receivedPacketListMap.containsKey(socketAddress)) {
                    this.receivedPacketListMap.get(socketAddress).clear();
                    this.receivedPacketListMap.remove(socketAddress);
                }
            } finally {
                this.receivedPacketListMapLock.unlock();
            }
        }

        /**
         * Returns a sendable {@code DatagramPacket} to the client and removes
         * it from the head of the queue of sendable {@code DatagramPacket}s
         * to the clients. A {@code NoSuchElementException} is thrown if there
         * are no sendable {@code DatagramPacket}s to the client at this time.
         *
         * @return a sendable {@code DatagramPacket} to the client
         */
        public DatagramPacket removeSendable() {
            this.sendablePacketListLock.lock();
            try {
                if (!this.sendablePacketList.isEmpty()) {
                    return this.sendablePacketList.remove(
                            this.sendablePacketList.size() - 1);
                }
            } finally {
                this.sendablePacketListLock.unlock();
            }
            throw new NoSuchElementException();
        }

    }

    /**
     * Receives a {@code DatagramPacket} from the client and sends them to a
     * {@code Worker}.
     */
    private static final class ReceivingPacketRelay implements Runnable {

        /**
         * The {@code Logger}.
         */
        private final Logger logger;

        /**
         * The {@code ExecutorFactory} used for creating an {@code Executor} to
         * execute {@code Worker}s.
         */
        private final ExecutorFactory multipleThreadsExecutorFactory;

        /**
         * The {@code Packets}.
         */
        private final Packets packets;

        /**
         * The server {@code DatagramSocket}.
         */
        private final DatagramSocket serverSocket;

        /**
         * The {@code WorkerFactory} used for creating {@code Worker}s.
         */
        private final WorkerFactory workerFactory;

        /**
         * Constructs a {@code ReceivingPacketRelay} with the provided server
         * {@code DatagramSocket}, the provided {@code ExecutorFactory} used
         * for creating an {@code Executor} to execute {@code Worker}s, the
         * provided {@code Packets}, and the provided
         * {@code WorkerFactory} used for creating {@code Worker}s.
         *
         * @param serverSock                  the provided server
         *                                    {@code DatagramSocket}
         * @param multiThreadsExecutorFactory the provided
         *                                    {@code ExecutorFactory} used for
         *                                    creating an {@code Executor} to
         *                                    execute {@code Worker}s
         * @param pckts                       the provided {@code Packets}
         * @param factory                     the provided
         *                                    {@code WorkerFactory} used for
         *                                    creating {@code Worker}s
         */
        public ReceivingPacketRelay(
                final DatagramSocket serverSock,
                final ExecutorFactory multiThreadsExecutorFactory,
                final Packets pckts,
                final WorkerFactory factory) {
            this.logger = LoggerFactory.getLogger(ReceivingPacketRelay.class);
            this.multipleThreadsExecutorFactory = multiThreadsExecutorFactory;
            this.packets = pckts;
            this.serverSocket = serverSock;
            this.workerFactory = factory;
        }

        @Override
        public void run() {
            ExecutorService executor =
                    this.multipleThreadsExecutorFactory.newExecutor();
            try {
                while (true) {
                    try {
                        byte[] buffer = new byte[RECEIVE_BUFFER_SIZE];
                        DatagramPacket receivedPacket = new DatagramPacket(
                                buffer, buffer.length);
                        this.serverSocket.receive(receivedPacket);
                        if (this.packets.hasReceivedFrom(
                                receivedPacket.getSocketAddress())) {
                            this.packets.addReceived(receivedPacket);
                        } else {
                            this.packets.addReceived(receivedPacket);
                            Worker worker = this.workerFactory.newWorker(
                                    new ClientPackets(
                                            receivedPacket.getSocketAddress(),
                                            this.packets));
                            executor.execute(worker);
                        }
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
            } finally {
                executor.shutdownNow();
            }
        }
    }

    /**
     * Sends {@code DatagramPacket}s from the {@code Worker}s to the clients.
     */
    private static final class SendingPacketRelay implements Runnable {

        /**
         * The {@code Logger}.
         */
        private final Logger logger;

        /**
         * The {@code Packets}.
         */
        private final Packets packets;

        /**
         * The server {@code DatagramSocket}.
         */
        private final DatagramSocket serverSocket;

        /**
         * Constructs a {@code SendingPacketRelay} with the provided server
         * {@code DatagramSocket} and the provided {@code Packets}.
         *
         * @param serverSock the provided server {@code DatagramSocket}
         * @param pckts      the provided {@code Packets}
         */
        public SendingPacketRelay(
                final DatagramSocket serverSock, final Packets pckts) {
            this.logger = LoggerFactory.getLogger(SendingPacketRelay.class);
            this.packets = pckts;
            this.serverSocket = serverSock;
        }

        @Override
        public void run() {
            while (true) {
                try {
                    DatagramPacket sentPacket;
                    try {
                        sentPacket = this.packets.removeSendable();
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
