package com.github.jh3nd3rs0n.jargyle.internal.net.ssl;

import com.github.jh3nd3rs0n.jargyle.internal.net.FilterDatagramSocket;

import javax.net.ssl.*;
import java.io.*;
import java.net.*;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantLock;

/**
 * A {@code DatagramSocket} layered with Datagram Transport Layer Security
 * (DTLS).
 */
public final class DtlsDatagramSocket extends FilterDatagramSocket {

    /**
     * The default buffer size for receiving DTLS wrapped datagrams.
     */
    public static final int DEFAULT_WRAPPED_RECEIVE_BUFFER_SIZE = 65535;

    /**
     * A half second in milliseconds.
     */
    private static final int HALF_SECOND = 500;

    /**
     * The {@code SSLContext} as DTLS context.
     */
    private final SSLContext dtlsContext;

    /**
     * The {@code Map} of DTLS connections to the corresponding
     * {@code SocketAddress}.
     */
    private final Map<SocketAddress, Connection> connections;

    /**
     * The current count of established connections.
     */
    private final AtomicInteger establishedConnectionCount;

    /**
     * The {@code SSLEngine}.
     */
    private final SSLEngine sslEngine;

    /**
     * The {@code ReentrantLock} for controlling thread access to the
     * {@code SSLEngine}.
     */
    private final ReentrantLock sslEngineLock;

    /**
     * The buffer size for receiving DTLS wrapped datagrams.
     */
    private final AtomicInteger wrappedReceiveBufferSize;

    /**
     * Constructs a {@code DtlsDatagramSocket} with the existing
     * {@code DatagramSocket} and the provided {@code SSLContext} as DTLS
     * context.
     *
     * @param datagramSock the existing {@code DatagramSocket}
     * @param dtlsCntxt    the provided {@code SSLContext} as DTLS context
     * @throws SocketException if an error occurs in initializing this
     *                         {@code DatagramSocket}
     */
    DtlsDatagramSocket(
            final DatagramSocket datagramSock,
            final SSLContext dtlsCntxt) throws SocketException {
        super(datagramSock);
        Objects.requireNonNull(dtlsCntxt);
        SSLEngine engine = dtlsCntxt.createSSLEngine();
        this.dtlsContext = dtlsCntxt;
        this.connections = Collections.synchronizedMap(new HashMap<>());
        this.establishedConnectionCount = new AtomicInteger(0);
        this.sslEngine = engine;
        this.sslEngineLock = new ReentrantLock();
        this.wrappedReceiveBufferSize = new AtomicInteger(
                DEFAULT_WRAPPED_RECEIVE_BUFFER_SIZE);
    }

    /**
     * Closes this {@code DtlsDatagramSocket}. An
     * {@code UncheckedIOException} is thrown if any of the DTLS
     * connections is unable to close properly.
     */
    @Override
    public void close() {
        List<IOException> ioExceptions = new ArrayList<>();
        for (Connection connection : new ArrayList<>(
                this.connections.values())) {
            try {
                connection.close();
            } catch (IOException e) {
                ioExceptions.add(e);
            }
        }
        if (!ioExceptions.isEmpty()) {
            throw new UncheckedIOException(new ConnectionsCloseException(
                    ioExceptions));
        }
        super.close();
    }

    /**
     * Throws an {@code UnsupportedOperationException}.
     *
     * @return {@code null}
     */
    @Override
    public DatagramChannel getChannel() {
        throw new UnsupportedOperationException();
    }

    /**
     * Returns a {@code String} array of acceptable cipher suites enabled for
     * DTLS connections.
     * <p>
     * See {@link SSLEngine#getEnabledCipherSuites()} for further details.
     * </p>
     *
     * @return a {@code String} array of acceptable cipher suites enabled for
     * DTLS connections
     */
    public String[] getEnabledCipherSuites() {
        this.sslEngineLock.lock();
        try {
            return this.sslEngine.getEnabledCipherSuites();
        } finally {
            this.sslEngineLock.unlock();
        }
    }

    /**
     * Sets the {@code String} array of acceptable cipher suites enabled for
     * DTLS connections with a provided {@code String} array.
     * <p>
     * See {@link SSLEngine#setEnabledCipherSuites(String[])} for further
     * details.
     * </p>
     *
     * @param suites the provided {@code String} array of acceptable cipher
     *               suites enabled for DTLS connections
     */
    public void setEnabledCipherSuites(final String[] suites) {
        this.sslEngineLock.lock();
        try {
            this.sslEngine.setEnabledCipherSuites(suites);
        } finally {
            this.sslEngineLock.unlock();
        }
    }

    /**
     * Returns a {@code String} array of acceptable protocol versions enabled
     * for DTLS connections.
     * <p>
     * See {@link SSLEngine#getEnabledProtocols()} for further details.
     * </p>
     *
     * @return a {@code String} array of acceptable protocol versions enabled
     * for DTLS connections
     */
    public String[] getEnabledProtocols() {
        this.sslEngineLock.lock();
        try {
            return this.sslEngine.getEnabledProtocols();
        } finally {
            this.sslEngineLock.unlock();
        }
    }

    /**
     * Sets the {@code String} array of acceptable protocol versions enabled
     * for DTLS connections with the provided {@code String} array.
     * <p>
     * See {@link SSLEngine#setEnabledProtocols(String[])} for further details.
     * </p>
     *
     * @param protocols the provided {@code String} array of acceptable
     *                  protocol versions enabled for DTLS connections
     */
    public void setEnabledProtocols(final String[] protocols) {
        this.sslEngineLock.lock();
        try {
            this.sslEngine.setEnabledProtocols(protocols);
        } finally {
            this.sslEngineLock.unlock();
        }
    }

    /**
     * Returns the {@code boolean} value to indicate that this
     * {@code DtlsDatagramSocket} will operate in client mode, meaning it will
     * initiate the communication with the peer.
     * <p>
     * See {@link SSLEngine#getUseClientMode()} for further details.
     * </p>
     *
     * @return the {@code boolean} value to indicate that this
     * {@code DtlsDatagramSocket} will operate in client mode
     */
    public boolean getUseClientMode() {
        this.sslEngineLock.lock();
        try {
            return this.sslEngine.getUseClientMode();
        } finally {
            this.sslEngineLock.unlock();
        }
    }

    /**
     * Sets the {@code boolean} value to indicate that this
     * {@code DtlsDatagramSocket} will operate in client mode.
     * <p>
     * See {@link SSLEngine#setUseClientMode(boolean)} for further details.
     * </p>
     *
     * @param mode the provided {@code boolean} value to indicate that this
     *             {@code DtlsDatagramSocket} will operate in client mode
     */
    public void setUseClientMode(final boolean mode) {
        this.sslEngineLock.lock();
        try {
            this.sslEngine.setUseClientMode(mode);
        } finally {
            this.sslEngineLock.unlock();
        }
    }

    /**
     * Returns the buffer size for receiving DTLS wrapped datagrams. The
     * default is {@link #DEFAULT_WRAPPED_RECEIVE_BUFFER_SIZE}.
     *
     * @return the buffer size for receiving DTLS wrapped datagrams
     */
    public int getWrappedReceiveBufferSize() {
        return this.wrappedReceiveBufferSize.get();
    }

    /**
     * Sets the buffer size for receiving DTLS wrapped datagrams. An
     * {@code IllegalArgumentException} is thrown if the provided buffer size
     * for receiving wrapped datagrams is less than one.
     *
     * @param wrapReceiveBufferSize the provided buffer size for receiving
     *                              DTLS wrapped datagrams
     */
    public void setWrappedReceiveBufferSize(
            final int wrapReceiveBufferSize) {
        if (wrapReceiveBufferSize < 1) {
            throw new IllegalArgumentException(
                    "wrapped receive buffer size must be at least 1");
        }
        this.wrappedReceiveBufferSize.set(wrapReceiveBufferSize);
    }

    @Override
    public synchronized void receive(
            final DatagramPacket p) throws IOException {
        if (this.getUseClientMode()) {
            this.waitForEstablishedConnections();
        }
        byte[] buffer = new byte[this.wrappedReceiveBufferSize.get()];
        DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
        this.datagramSocket.receive(packet);
        SocketAddress socketAddress = packet.getSocketAddress();
        Connection connection = this.connections.get(
                packet.getSocketAddress());
        if (connection == null) {
            connection = new Connection(this, socketAddress);
            this.connections.put(socketAddress, connection);
        }
        connection.receive(buffer, packet, p);
    }

    @Override
    public void send(final DatagramPacket p) throws IOException {
        SocketAddress socketAddress = p.getSocketAddress();
        if (this.isConnected()
                && !socketAddress.equals(this.getRemoteSocketAddress())) {
            throw new IllegalArgumentException(String.format(
                    "packet socket address %s does not match "
                            + "connected remote socket address %s",
                    socketAddress,
                    this.getRemoteSocketAddress()));
        }
        Connection connection = this.connections.get(p.getSocketAddress());
        if (connection == null) {
            connection = new Connection(this, socketAddress);
            this.connections.put(socketAddress, connection);
        }
        connection.send(p);
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName() +
                " [getLocalSocketAddress()=" +
                this.getLocalSocketAddress() +
                ", getUseClientMode()=" +
                this.getUseClientMode() +
                "]";
    }

    /**
     * Waits for existing connections to be established. This method is called
     * when {@link #receive(DatagramPacket)} is called and this
     * {@code DtlsDatagramSocket} is in client mode.
     * {@link #receive(DatagramPacket)} will wait for existing connections to
     * be established by {@link #send(DatagramPacket)} before receiving any
     * datagram packets. This method is useful if this
     * {@code DtlsDatagramPacket} performs as the outbound part of a UDP relay.
     *
     * @throws IOException if the socket timeout is greater than zero and the
     *                     same socket timeout used for waiting for existing
     *                     connections to be established has been reached
     */
    private void waitForEstablishedConnections() throws IOException {
        int soTimeout = this.datagramSocket.getSoTimeout();
        long waitStartTime = System.currentTimeMillis();
        while (this.establishedConnectionCount.get() == 0
                || this.establishedConnectionCount.get() < this.connections.size()) {
            try {
                Thread.sleep(HALF_SECOND);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            if (soTimeout == 0) {
                continue;
            }
            long timeSinceWaitStartTime =
                    System.currentTimeMillis() - waitStartTime;
            if (timeSinceWaitStartTime >= soTimeout) {
                throw new SocketTimeoutException(
                        "Timeout for waiting for established connections has "
                                + "been reached");
            }
        }
    }

    /**
     * A DTLS connection to a peer.
     */
    private static final class Connection {

        /**
         * The maximum number of loops in attempts to receive application data.
         */
        private static final int MAX_APP_READ_LOOPS = 60;

        /**
         * The maximum number of loops in attempts to perform handshaking.
         */
        private static final int MAX_HANDSHAKE_LOOPS = 200;

        /**
         * The underlying {@code DatagramSocket}.
         */
        private final DatagramSocket datagramSocket;

        /**
         * The {@code DtlsDatagramSocket} of this {@code Connection}.
         */
        private final DtlsDatagramSocket dtlsDatagramSocket;

        /**
         * The {@code SocketAddress} of the peer.
         */
        private final SocketAddress peerSocketAddress;

        /**
         * The {@code SSLEngine} for DTLS operations.
         */
        private final SSLEngine sslEngine;

        /**
         * The {@code SslLogger}.
         */
        private final SslLogger sslLogger;

        /**
         * The buffer size for receiving DTLS wrapped datagrams.
         */
        private final int wrappedReceiveBufferSize;

        /**
         * The {@code boolean} value to indicate that this {@code Connection}
         * is established.
         */
        private volatile boolean established;

        /**
         * Constructs a {@code Connection} with the provided
         * {@code DtlsDatagramSocket} and the provided {@code SocketAddress} of
         * the peer.
         *
         * @param dtlsDatagramSock the provided {@code DtlsDatagramSocket}
         * @param peerSocketAddr   the provided {@code SocketAddress} of the
         *                         peer
         */
        public Connection(
                final DtlsDatagramSocket dtlsDatagramSock,
                final SocketAddress peerSocketAddr) {
            Objects.requireNonNull(dtlsDatagramSock);
            Objects.requireNonNull(peerSocketAddr);
            InetSocketAddress peerSockAddr = (InetSocketAddress) peerSocketAddr;
            SSLEngine engine = dtlsDatagramSock.dtlsContext.createSSLEngine(
                    peerSockAddr.getHostString(),
                    peerSockAddr.getPort());
            engine.setEnabledCipherSuites(
                    dtlsDatagramSock.getEnabledCipherSuites());
            engine.setEnabledProtocols(dtlsDatagramSock.getEnabledProtocols());
            engine.setUseClientMode(dtlsDatagramSock.getUseClientMode());
            int wrapReceiveBufferSize =
                    dtlsDatagramSock.getWrappedReceiveBufferSize();
            this.datagramSocket = dtlsDatagramSock.datagramSocket;
            this.dtlsDatagramSocket = dtlsDatagramSock;
            this.established = false;
            this.peerSocketAddress = peerSocketAddr;
            this.sslEngine = engine;
            this.sslLogger = new SslLogger();            
            this.wrappedReceiveBufferSize = wrapReceiveBufferSize;
        }

        /**
         * Closes this {@code Connection}.
         *
         * @throws IOException if an I/O error occurs
         */
        public void close() throws IOException {
            this.dtlsDatagramSocket.connections.remove(this.peerSocketAddress);
            if (this.established) {
                this.dtlsDatagramSocket.establishedConnectionCount.decrementAndGet();
            }
            this.established = false;
            this.sslEngine.closeOutbound();
            if (!this.sslEngine.isOutboundDone()) {
                ByteBuffer empty = ByteBuffer.wrap(new byte[]{});
                ByteBuffer outNetData = ByteBuffer.allocate(
                        this.sslEngine.getSSLParameters().getMaximumPacketSize());
                SSLEngineResult r = this.sslEngine.wrap(empty, outNetData);
                outNetData.flip();
                SSLEngineResult.Status rs = r.getStatus();
                this.checkAfterWrappingCloseMessage(rs);
                if (outNetData.hasRemaining()) {
                    byte[] bytes = new byte[outNetData.remaining()];
                    outNetData.get(bytes);
                    DatagramPacket packet = new DatagramPacket(
                            bytes, bytes.length, this.peerSocketAddress);
                    this.datagramSocket.send(packet);
                }
            }
        }

        /**
         * Checks the provided {@code Status} after wrapping the closing
         * message.
         *
         * @param rs the provided {@code Status}
         * @throws IOException if an I/O error occurs
         */
        private void checkAfterWrappingCloseMessage(
                final SSLEngineResult.Status rs) throws IOException {
            switch (rs) {
                case BUFFER_OVERFLOW:
                    throw new SSLException(
                            "Buffer overflow during wrapping");
                case BUFFER_UNDERFLOW:
                    throw new SSLException(
                            "Buffer underflow during wrapping");
                case CLOSED:
                    break;
                default:
                    throw new SSLException(String.format(
                            "Can't reach here, result is %s",
                            rs));
            }
            // SSLEngineResult.Status.CLOSED
        }

        /**
         * Starts the handshaking process.
         *
         * @throws IOException if an I/O error occurs
         */
        private void startHandshake() throws IOException {
            this.startHandshake(new byte[]{}, null);
        }

        /**
         * Checks the provided {@code Status} and the provided
         * {@code HandshakeStatus} after receiving a handshake packet.
         *
         * @param rs the provided {@code Status}
         * @param hs the provided {@code HandshakeStatus}
         * @throws IOException if an I/O error occurs
         */
        private void checkAfterReceivingHandshakePacket(
                final SSLEngineResult.Status rs,
                final SSLEngineResult.HandshakeStatus hs)
                throws IOException {
            switch (rs) {
                case BUFFER_OVERFLOW:
                    this.sslLogger.debug(
                            "dtls",
                            String.format(
                                    "BUFFER_OVERFLOW, handshake status is %s",
                                    hs));
                    // the client maximum fragment size config does not work?
                    throw new SSLException("Buffer overflow: incorrect "
                            + "client maximum fragment size");
                case BUFFER_UNDERFLOW:
                    this.sslLogger.debug(
                            "dtls",
                            String.format(
                                    "BUFFER_UNDERFLOW, handshake status is %s",
                                    hs));
                    // bad packet, or the client maximum fragment size
                    // config does not work?
                    if (!hs.equals(
                            SSLEngineResult.HandshakeStatus.NOT_HANDSHAKING)) {
                        throw new SSLException("Buffer underflow: "
                                + "incorrect client maximum fragment size");
                    } // otherwise, ignore this packet
                    break;
                case CLOSED:
                    this.close();
                    throw new ConnectionClosedException(String.format(
                            "Connection closed, handshake status is %s",
                            hs));
                case OK:
                    // OK
                    break;
                default:
                    throw new SSLException(String.format(
                            "Can't reach here, result is %s",
                            rs));
            }
        }

        /**
         * Produces and sends the handshake packets to the peer.
         *
         * @return a {@code boolean} value to indicate that the handshaking
         * process is finished
         * @throws IOException if an I/O error occurs
         */
        private boolean sendHandshakePackets() throws IOException {
            List<DatagramPacket> packets = new ArrayList<>();
            boolean finished = this.produceHandshakePackets(
                    packets
            );
            this.sslLogger.debug(
                    "dtls",
                    String.format("Produced %s packets", packets.size()));
            for (DatagramPacket p : packets) {
                this.datagramSocket.send(p);
            }
            if (finished) {
                this.sslLogger.debug(
                        "dtls",
                        "Handshake status is FINISHED after producing "
                                + "handshake packets, finish the loop");
                return true;
            }
            return false;
        }

        /**
         * Reproduces and sends the handshake packets to the peer.
         *
         * @return a {@code boolean} value to indicate that the handshaking
         * process is finished
         * @throws IOException if an I/O error occurs
         */
        private boolean sendReproducedHandshakePackets() throws IOException {
            List<DatagramPacket> packets = new ArrayList<>();
            boolean finished = this.reproduceHandshakePackets(
                    packets
            );
            this.sslLogger.debug(
                    "dtls",
                    String.format("Reproduced %s packets", packets.size()));
            for (DatagramPacket p : packets) {
                this.datagramSocket.send(p);
            }
            if (finished) {
                this.sslLogger.debug(
                        "dtls",
                        "Handshake status is FINISHED after calling "
                                + "reproduceHandshakePackets(), finish the "
                                + "loop");
                return true;
            }
            return false;
        }

        /**
         * Performs the handshaking process with the optionally empty received
         * datagram buffer and the optional received {@code DatagramPacket}.
         *
         * @param receivedDatagramBuffer the received datagram buffer (can be
         *                               empty)
         * @param receivedDatagramPacket the received {@code DatagramPacket}
         *                               (can be {@code null})
         * @throws IOException if an I/O error occurs
         */
        private void doHandshaking(
                final byte[] receivedDatagramBuffer,
                final DatagramPacket receivedDatagramPacket)
                throws IOException {
            boolean endLoops = false;
            int loops = MAX_HANDSHAKE_LOOPS;
            byte[] receivedDatagramBffr = receivedDatagramBuffer;
            DatagramPacket receivedDatagramPckt = receivedDatagramPacket;
            while (!endLoops) {
                if (--loops < 0) {
                    throw new SSLException(
                            "Too many loops to produce handshake packets");
                }
                SSLEngineResult.HandshakeStatus hs =
                        this.sslEngine.getHandshakeStatus();
                this.sslLogger.debug(
                        "dtls",
                        String.format(
                                "Handshaking (iteration ID: %s, status: %s)",
                                loops,
                                hs));
                switch (hs) {
                    case NEED_UNWRAP:
                    case NEED_UNWRAP_AGAIN:
                        this.sslLogger.debug(
                                "dtls",
                                String.format(
                                        "Receive DTLS records, "
                                                + "handshake status is %s",
                                        hs));
                        ByteBuffer inNetData;
                        ByteBuffer inAppData;
                        if (hs.equals(
                                SSLEngineResult.HandshakeStatus.NEED_UNWRAP)) {
                            byte[] buffer;
                            DatagramPacket packet;
                            if (receivedDatagramBffr.length == 0
                                    && receivedDatagramPckt == null) {
                                buffer = new byte[this.wrappedReceiveBufferSize];
                                packet = new DatagramPacket(buffer, buffer.length);
                                try {
                                    this.datagramSocket.receive(packet);
                                } catch (SocketTimeoutException ste) {
                                    this.sslLogger.debug(
                                            "dtls",
                                            String.format("Warning: %s", ste),
                                            ste);
                                    endLoops = this.sendReproducedHandshakePackets();
                                    this.sslLogger.debug(
                                            "dtls",
                                            String.format(
                                                    "New handshake status is %s",
                                                    this.sslEngine.getHandshakeStatus()));
                                    continue;
                                }
                            } else {
                                buffer = receivedDatagramBffr;
                                packet = receivedDatagramPckt;
                                receivedDatagramBffr = new byte[]{};
                                receivedDatagramPckt = null;
                            }
                            inNetData = ByteBuffer.wrap(
                                    buffer, 0, packet.getLength());
                            inAppData = ByteBuffer.allocate(
                                    this.wrappedReceiveBufferSize);
                        } else {
                            inNetData = ByteBuffer.allocate(0);
                            inAppData = ByteBuffer.allocate(
                                    this.wrappedReceiveBufferSize);
                        }
                        SSLEngineResult r = this.sslEngine.unwrap(
                                inNetData, inAppData);
                        SSLEngineResult.Status rs = r.getStatus();
                        hs = r.getHandshakeStatus();
                        this.checkAfterReceivingHandshakePacket(rs, hs);
                        if (hs.equals(SSLEngineResult.HandshakeStatus.FINISHED)) {
                            this.sslLogger.debug(
                                    "dtls",
                                    "Handshake status is FINISHED, finish the "
                                            + "loop");
                            endLoops = true;
                        }
                        break;
                    case NEED_WRAP:
                        endLoops = this.sendHandshakePackets();
                        break;
                    case NEED_TASK:
                        this.runDelegatedTasks();
                        break;
                    case NOT_HANDSHAKING:
                        this.sslLogger.debug(
                                "dtls",
                                "Handshake status is NOT_HANDSHAKING, "
                                        + "finish the loop");
                        endLoops = true;
                        break;
                    case FINISHED:
                        this.close();
                        throw new SSLException(
                                "Unexpected status, SSLEngine.getHandshakeStatus() "
                                        + "shouldn't return FINISHED");
                    default:
                        throw new SSLException(String.format(
                                "Can't reach here, handshake status is %s",
                                hs));
                }
            }
        }

        /**
         * Initiates the handshake process with the optionally empty received
         * datagram buffer and the optional received {@code DatagramPacket}.
         *
         * @param receivedDatagramBuffer the received datagram buffer (can be
         *                               empty)
         * @param receivedDatagramPacket the received {@code DatagramPacket}
         *                               (can be {@code null})
         * @throws IOException if an I/O error occurs
         */
        private void startHandshake(
                final byte[] receivedDatagramBuffer,
                final DatagramPacket receivedDatagramPacket)
                throws IOException {
            this.sslEngine.beginHandshake();
            this.doHandshaking(receivedDatagramBuffer, receivedDatagramPacket);
            SSLEngineResult.HandshakeStatus hs =
                    this.sslEngine.getHandshakeStatus();
            this.sslLogger.debug(
                    "dtls",
                    String.format("Handshake finished, status is %s", hs));
            if (this.sslEngine.getHandshakeSession() != null) {
                this.close();
                throw new SSLException(
                        "Handshake finished, but handshake session is not "
                                + "null");
            }
            SSLSession session = this.sslEngine.getSession();
            if (session == null) {
                this.close();
                throw new SSLException("Handshake finished, but session is "
                        + "null");
            }
            this.sslLogger.debug(
                    "dtls",
                    String.format(
                            "Negotiated protocol is %s",
                            session.getProtocol()));
            this.sslLogger.debug(
                    "dtls",
                    String.format(
                            "Negotiated cipher suite is %s",
                            session.getCipherSuite()));
            // handshake status should be NOT_HANDSHAKING
            //
            // According to the spec, SSLEngine.getHandshakeStatus() can't
            // return FINISHED.
            if (!hs.equals(SSLEngineResult.HandshakeStatus.NOT_HANDSHAKING)) {
                this.close();
                throw new SSLException(String.format(
                        "Unexpected handshake status %s", hs));
            }
            this.established = true;
            this.dtlsDatagramSocket.establishedConnectionCount.incrementAndGet();
        }

        /**
         * Checks the provided {@code Status} and the provided
         * {@code HandshakeStatus} before producing handshake packets.
         *
         * @param rs the provided {@code Status}
         * @param hs the provided {@code HandshakeStatus}
         * @throws IOException if an I/O error occurs
         */
        private void checkBeforeProducingHandshakePackets(
                final SSLEngineResult.Status rs,
                final SSLEngineResult.HandshakeStatus hs)
                throws IOException {
            switch (rs) {
                case BUFFER_OVERFLOW:
                    // the client maximum fragment size config does not work?
                    throw new SSLException("Buffer overflow: incorrect server "
                            + "maximum fragment size");
                case BUFFER_UNDERFLOW:
                    this.sslLogger.debug(
                            "dtls",
                            "Produce handshake packets: BUFFER_UNDERFLOW "
                                    + "occurred");
                    this.sslLogger.debug(
                            "dtls",
                            String.format(
                                    "Produce handshake packets: "
                                            + "Handshake status: %s",
                                    hs));
                    // bad packet, or the client maximum fragment size
                    // config does not work?
                    if (!hs.equals(
                            SSLEngineResult.HandshakeStatus.NOT_HANDSHAKING)) {
                        throw new SSLException("Buffer underflow: incorrect "
                                + "server maximum fragment size");
                    } // otherwise, ignore this packet
                case CLOSED:
                    this.close();
                    throw new ConnectionClosedException(
                            "Connection has closed");
                case OK:
                    // OK
                    break;
                default:
                    throw new SSLException(String.format(
                            "Can't reach here, result is %s",
                            rs));
            }
        }

        /**
         * Produces handshake packets and puts them in the provided
         * empty {@code List} of {@code DatagramPacket}s.
         *
         * @param packets the provided empty {@code List} of
         *                {@code DatagramPacket}s to receive the produced
         *                handshake packets
         * @return a {@code boolean} value to indicate that the handshake
         * process is finished
         * @throws IOException if an I/O error occurs
         */
        private boolean produceHandshakePackets(
                final List<DatagramPacket> packets) throws IOException {
            boolean endLoops = false;
            int loops = MAX_HANDSHAKE_LOOPS / 2;
            while (!endLoops) {
                if (--loops < 0) {
                    throw new SSLException(
                            "Too many loops to produce handshake packets");
                }
                ByteBuffer outAppData = ByteBuffer.allocate(0);
                ByteBuffer outNetData = ByteBuffer.allocate(
                        this.wrappedReceiveBufferSize);
                SSLEngineResult r = this.sslEngine.wrap(outAppData, outNetData);
                outNetData.flip();
                SSLEngineResult.Status rs = r.getStatus();
                SSLEngineResult.HandshakeStatus hs = r.getHandshakeStatus();
                this.sslLogger.debug(
                        "dtls",
                        String.format(
                                "Producing handshake packet ("
                                        + "iteration ID: %s, "
                                        + "result status: %s, "
                                        + "handshake status: %s)",
                                loops,
                                rs,
                                hs));
                this.checkBeforeProducingHandshakePackets(rs, hs);
                // SSLEngineResult.Status.OK:
                if (outNetData.hasRemaining()) {
                    byte[] bytes = new byte[outNetData.remaining()];
                    outNetData.get(bytes);
                    DatagramPacket packet = new DatagramPacket(
                            bytes, bytes.length, this.peerSocketAddress);
                    packets.add(packet);
                }
                if (hs.equals(SSLEngineResult.HandshakeStatus.FINISHED)) {
                    this.sslLogger.debug(
                            "dtls",
                            "Produce handshake packets: "
                                    + "Handshake status is FINISHED, "
                                    + "finish the loop");
                    return true;
                }
                boolean endInnerLoop = false;
                SSLEngineResult.HandshakeStatus nhs = hs;
                while (!endInnerLoop) {
                    switch (nhs) {
                        case NEED_TASK:
                            this.runDelegatedTasks();
                            break;
                        case NEED_UNWRAP:
                        case NEED_UNWRAP_AGAIN:
                        case NOT_HANDSHAKING:
                            endInnerLoop = true;
                            endLoops = true;
                            break;
                        case NEED_WRAP:
                            endInnerLoop = true;
                            break;
                        case FINISHED:
                            this.close();
                            throw new SSLException("Unexpected status, "
                                    + "SSLEngine.getHandshakeStatus() shouldn't "
                                    + "return FINISHED");
                        default:
                            throw new SSLException(String.format(
                                    "Can't reach here, handshake status is %s",
                                    nhs));
                    }
                    nhs = this.sslEngine.getHandshakeStatus();
                }
            }
            return false;
        }

        /**
         * Checks the provided {@code Status} after unwrapping the network
         * data.
         *
         * @param rs the provided {@code Status}
         * @throws IOException if an I/O error occurs
         */
        private void checkAfterUnwrappingNetworkData(
                final SSLEngineResult.Status rs) throws IOException {
            switch (rs) {
                case BUFFER_OVERFLOW:
                    throw new SSLException(
                            "Buffer overflow during unwrapping");
                case BUFFER_UNDERFLOW:
                    throw new SSLException(
                            "Buffer underflow during unwrapping");
                case CLOSED:
                    this.close();
                    throw new ConnectionClosedException(
                            "Connection has closed");
                case OK:
                    // OK
                    break;
                default:
                    throw new SSLException(String.format(
                            "Can't reach here, result is %s",
                            rs));
            }
            // SSLEngineResult.Status.OK:
        }

        /**
         * Receives the {@code DatagramPacket} from this {@code Connection}
         * after receiving the optionally empty datagram buffer and the
         * optional {@code DatagramPacket}.
         *
         * @param receivedDatagramBuffer the received datagram buffer (can be
         *                               empty)
         * @param receivedDatagramPacket the received {@code DatagramPacket}
         *                               (can be {@code null})
         * @param p                      the {@code DatagramPacket} into which
         *                               to place the incoming application data
         * @throws IOException if an I/O error occurs
         */
        public void receive(
                final byte[] receivedDatagramBuffer,
                final DatagramPacket receivedDatagramPacket,
                final DatagramPacket p) throws IOException {
            byte[] receivedDatagramBffr = receivedDatagramBuffer;
            DatagramPacket receivedDatagramPckt = receivedDatagramPacket;
            if (!this.sslEngine.getUseClientMode() && !this.established) {
                this.startHandshake(receivedDatagramBffr, receivedDatagramPckt);
                receivedDatagramBffr = new byte[]{};
                receivedDatagramPckt = null;
            }
            int loops = MAX_APP_READ_LOOPS;
            while (true) {
                if (--loops < 0) {
                    throw new SSLException(
                            "Too many loops to receive application data");
                }
                byte[] buffer;
                DatagramPacket packet;
                if (receivedDatagramBffr.length == 0
                        && receivedDatagramPckt == null) {
                    buffer = new byte[this.wrappedReceiveBufferSize];
                    packet = new DatagramPacket(buffer, buffer.length);
                    this.datagramSocket.receive(packet);
                } else {
                    buffer = receivedDatagramBffr;
                    packet = receivedDatagramPckt;
                    receivedDatagramBffr = new byte[]{};
                    receivedDatagramPckt = null;
                }
                buffer = Arrays.copyOf(buffer, packet.getLength());
                int pLength = p.getLength();
                ByteArrayOutputStream byteArrayOutputStream =
                        new ByteArrayOutputStream();
                while (buffer.length > 0) {
                    ByteBuffer inNetData = ByteBuffer.wrap(buffer);
                    ByteBuffer inAppData = ByteBuffer.allocate(pLength);
                    SSLEngineResult r = this.sslEngine.unwrap(
                            inNetData, inAppData);
                    inAppData.flip();
                    SSLEngineResult.Status rs = r.getStatus();
                    this.checkAfterUnwrappingNetworkData(rs);
                    if (inAppData.hasRemaining()) {
                        byte[] bytes = new byte[inAppData.remaining()];
                        inAppData.get(bytes);
                        byteArrayOutputStream.write(bytes);
                        byteArrayOutputStream.flush();
                        if (!inNetData.hasRemaining()) {
                            byte[] totalBytes =
                                    byteArrayOutputStream.toByteArray();
                            int length = Math.min(totalBytes.length, pLength);
                            p.setSocketAddress(packet.getSocketAddress());
                            p.setData(totalBytes, 0, length);
                            p.setLength(length);
                            return;
                        }
                    }
                    buffer = new byte[]{};
                    if (inNetData.hasRemaining()) {
                        buffer = new byte[inNetData.remaining()];
                        inNetData.get(buffer);
                    }
                }
            }
        }

        /**
         * Reproduces handshake packets and puts them in the provided empty
         * {@code List} of {@code DatagramPacket}s.
         *
         * @param packets the provided empty {@code List} of
         *                {@code DatagramPacket}s to receive the reproduced
         *                handshake packets
         * @return a {@code boolean} value to indicate the handshaking process
         * is finished
         * @throws IOException if an I/O error occurs
         */
        private boolean reproduceHandshakePackets(
                final List<DatagramPacket> packets) throws IOException {
            SSLEngineResult.HandshakeStatus hs =
                    this.sslEngine.getHandshakeStatus();
            if (hs.equals(SSLEngineResult.HandshakeStatus.NOT_HANDSHAKING)) {
                return false;
            } else {
                // reproduce handshake packets
                return this.produceHandshakePackets(packets);
            }
        }

        /**
         * Runs the delegated tasks assigned to the {@code SSLEngine}.
         *
         * @throws IOException if an I/O error occurs
         */
        private void runDelegatedTasks() throws IOException {
            Runnable runnable;
            while ((runnable = this.sslEngine.getDelegatedTask()) != null) {
                runnable.run();
            }
            SSLEngineResult.HandshakeStatus hs =
                    this.sslEngine.getHandshakeStatus();
            if (hs.equals(SSLEngineResult.HandshakeStatus.NEED_TASK)) {
                this.close();
                throw new SSLException(
                        "Handshake shouldn't need additional tasks");
            }
        }

        /**
         * Checks the provided {@code Status} after wrapping the application
         * data.
         *
         * @param rs the provided {@code Status}
         * @throws IOException if an I/O error occurs
         */
        private void checkAfterWrappingApplicationData(
                final SSLEngineResult.Status rs) throws IOException {
            switch (rs) {
                case BUFFER_OVERFLOW:
                    throw new SSLException(
                            "Buffer overflow during wrapping");
                case BUFFER_UNDERFLOW:
                    throw new SSLException(
                            "Buffer underflow during wrapping");
                case CLOSED:
                    this.close();
                    throw new ConnectionClosedException(
                            "Connection has closed");
                case OK:
                    // OK
                    break;
                default:
                    throw new SSLException(String.format(
                            "Can't reach here, result is %s",
                            rs));
            }
            // SSLEngineResult.Status.OK:
        }

        /**
         * Sends the provided {@code DatagramPacket} to the peer.
         *
         * @param p the provided {@code DatagramPacket}
         * @throws IOException if an I/O error occurs
         */
        public void send(final DatagramPacket p) throws IOException {
            if (this.sslEngine.getUseClientMode() && !this.established) {
                this.startHandshake();
            }
            ByteArrayOutputStream byteArrayOutputStream =
                    new ByteArrayOutputStream();
            byte[] buffer = Arrays.copyOfRange(
                    p.getData(), p.getOffset(), p.getOffset() + p.getLength());
            while (buffer.length > 0) {
                ByteBuffer outAppData = ByteBuffer.wrap(buffer);
                ByteBuffer outNetData = ByteBuffer.allocate(
                        this.wrappedReceiveBufferSize);
                SSLEngineResult r = this.sslEngine.wrap(
                        outAppData, outNetData);
                outNetData.flip();
                SSLEngineResult.Status rs = r.getStatus();
                this.checkAfterWrappingApplicationData(rs);
                if (outNetData.hasRemaining()) {
                    byte[] bytes = new byte[outNetData.remaining()];
                    outNetData.get(bytes);
                    byteArrayOutputStream.write(bytes);
                    byteArrayOutputStream.flush();
                }
                buffer = new byte[]{};
                if (outAppData.hasRemaining()) {
                    buffer = new byte[outAppData.remaining()];
                    outAppData.get(buffer);
                }
            }
            byte[] totalBytes = byteArrayOutputStream.toByteArray();
            DatagramPacket packet = new DatagramPacket(
                    totalBytes, totalBytes.length, p.getSocketAddress());
            this.datagramSocket.send(packet);
        }

        @Override
        public String toString() {
            return this.getClass().getSimpleName() +
                    " [dtlsDatagramSocket=" +
                    this.dtlsDatagramSocket +
                    ", peerSocketAddress=" +
                    this.peerSocketAddress +
                    "]";
        }

    }

    /**
     * Thrown when a DTLS connection is closed.
     */
    public static final class ConnectionClosedException extends SSLException {

        /**
         * The default serial version UID.
         */
        private static final long serialVersionUID = 1L;

        /**
         * Constructs a {@code ConnectionClosedException} with the provided
         * reason.
         *
         * @param reason the provided reason
         */
        public ConnectionClosedException(final String reason) {
            super(reason);
        }

    }

    /**
     * Thrown when {@code IOException}s occur upon closing DTLS connections.
     */
    public static final class ConnectionsCloseException extends IOException {

        /**
         * The default serial version UID.
         */
        private static final long serialVersionUID = 1L;

        /**
         * The {@code IOException}s that occur upon closing DTLS connections.
         */
        private final List<IOException> ioExceptions;

        /**
         * Constructs a {@code ConnectionsCloseException} with the provided
         * {@code List} of {@code IOException}s that occur upon closing DTLS
         * connections.
         *
         * @param exceptions the provided {@code List} of {@code IOException}s
         *                   that occur upon closing DTLS connections
         */
        public ConnectionsCloseException(final List<IOException> exceptions) {
            this.ioExceptions = new ArrayList<>(exceptions);
        }

        /**
         * Returns an unmodifiable {@code List} of {@code IOException}s that
         * occur upon closing DTLS connections.
         *
         * @return an unmodifiable {@code List} of {@code IOException}s that
         * occur upon closing DTLS connections
         */
        public List<IOException> getIOExceptions() {
            return Collections.unmodifiableList(this.ioExceptions);
        }

        @Override
        public void printStackTrace() {
            this.printStackTrace(new PrintStream(System.err));
        }

        @Override
        public void printStackTrace(PrintStream s) {
            this.printStackTrace(new PrintWriter(s));
        }

        @Override
        public void printStackTrace(PrintWriter w) {
            super.printStackTrace(w);
            for (IOException ioException : this.ioExceptions) {
                ioException.printStackTrace(w);
            }
        }

        @Override
        public String toString() {
            StringBuilder stringBuilder = new StringBuilder(
                    this.getClass().getName());
            Iterator<IOException> iterator = ioExceptions.iterator();
            if (iterator.hasNext()) {
                stringBuilder.append(": ");
            }
            while (iterator.hasNext()) {
                IOException ioException = iterator.next();
                stringBuilder.append(ioException);
                if (iterator.hasNext()) {
                    stringBuilder.append(", ");
                }
            }
            return stringBuilder.toString();
        }

    }

}
