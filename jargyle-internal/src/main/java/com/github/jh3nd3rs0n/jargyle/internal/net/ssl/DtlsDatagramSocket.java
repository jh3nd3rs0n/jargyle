package com.github.jh3nd3rs0n.jargyle.internal.net.ssl;

import com.github.jh3nd3rs0n.jargyle.internal.logging.ObjectLogMessageHelper;
import com.github.jh3nd3rs0n.jargyle.internal.net.FilterDatagramSocket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.*;
import java.io.IOException;
import java.net.*;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReferenceArray;

/**
 * A {@code DatagramSocket} layered with Datagram Transport Layer Security
 * (DTLS).
 */
public final class DtlsDatagramSocket extends FilterDatagramSocket {

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
     * The maximum size of a datagram packet.
     */
    private final AtomicInteger maximumPacketSize;

    /**
     * The {@code String} array of acceptable cipher suites enabled for DTLS
     * connections.
     */
    private AtomicReferenceArray<String> enabledCipherSuites;

    /**
     * The {@code String} array of acceptable protocol versions enabled for
     * DTLS connections.
     */
    private AtomicReferenceArray<String> enabledProtocols;

    /**
     * The {@code boolean} value to indicate that this
     * {@code DtlsDatagramSocket} is in use (sending and/or receiving packets).
     */
    private volatile boolean inUse;

    /**
     * The {@code boolean} value to indicate that client authentication is
     * required.
     */
    private volatile boolean needClientAuth;

    /**
     * The {@code boolean} value to indicate that this
     * {@code DtlsDatagramSocket} will operate in client mode, meaning it will
     * initiate the communication with the peer.
     */
    private volatile boolean useClientMode;

    /**
     * The {@code boolean} value to indicate that client authentication is
     * requested.
     */
    private volatile boolean wantClientAuth;

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
        this.dtlsContext = Objects.requireNonNull(dtlsCntxt);
        this.connections = new HashMap<>();
        this.enabledCipherSuites = new AtomicReferenceArray<>(0);
        this.enabledProtocols = new AtomicReferenceArray<>(0);
        this.establishedConnectionCount = new AtomicInteger(0);
        this.inUse = false;
        this.maximumPacketSize = new AtomicInteger(0);
        this.needClientAuth = false;
        this.useClientMode = false;
        this.wantClientAuth = false;
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
     *
     * @return a {@code String} array of acceptable cipher suites enabled for
     * DTLS connections
     */
    public String[] getEnabledCipherSuites() {
        int length = this.enabledCipherSuites.length();
        String[] array = new String[length];
        for (int i = 0; i < length; i++) {
            array[i] = this.enabledCipherSuites.get(i);
        }
        return array;
    }

    /**
     * Sets the {@code String} array of acceptable cipher suites enabled for
     * DTLS connections with a provided {@code String} array. An
     * {@code IllegalStateException} is thrown if this
     * {@code DtlsDatagramSocket} is in use (sending and/or receiving packets).
     *
     * @param suites the provided {@code String} array of acceptable cipher
     *               suites enabled for DTLS connections
     */
    public void setEnabledCipherSuites(final String[] suites) {
        if (this.inUse) {
            throw new IllegalStateException("DtlsDatagramSocket in use");
        }
        this.enabledCipherSuites = new AtomicReferenceArray<>(suites);
    }

    /**
     * Returns a {@code String} array of acceptable protocol versions enabled
     * for DTLS connections.
     *
     * @return a {@code String} array of acceptable protocol versions enabled
     * for DTLS connections
     */
    public String[] getEnabledProtocols() {
        int length = this.enabledProtocols.length();
        String[] array = new String[length];
        for (int i = 0; i < length; i++) {
            array[i] = this.enabledProtocols.get(i);
        }
        return array;
    }

    /**
     * Sets the {@code String} array of acceptable protocol versions enabled
     * for DTLS connections with the provided {@code String} array. An
     * {@code IllegalStateException} is thrown if this
     * {@code DtlsDatagramSocket} is in use (sending and/or receiving packets).
     *
     * @param protocols the provided {@code String} array of acceptable
     *                  protocol versions enabled for DTLS connections
     */
    public void setEnabledProtocols(final String[] protocols) {
        if (this.inUse) {
            throw new IllegalStateException("DtlsDatagramSocket in use");
        }
        this.enabledProtocols = new AtomicReferenceArray<>(protocols);
    }

    /**
     * Returns the maximum size of a datagram packet.
     *
     * @return the maximum size of a datagram packet
     */
    public int getMaximumPacketSize() {
        return this.maximumPacketSize.get();
    }

    /**
     * Sets the maximum size of a datagram packet with the provided maximum
     * size. An {@code IllegalStateException} is thrown if this
     * {@code DtlsDatagramSocket} is in use (sending and/or receiving packets).
     * An {@code IllegalArgumentException} is thrown if the provided maximum
     * size is less than zero.
     *
     * @param maxPacketSize the provided maximum size
     */
    public void setMaximumPacketSize(final int maxPacketSize) {
        if (this.inUse) {
            throw new IllegalStateException("DtlsDatagramSocket in use");
        }
        if (maxPacketSize < 0) {
            throw new IllegalArgumentException(
                    "maximum packet size must be at least 0");
        }
        this.maximumPacketSize.set(maxPacketSize);
    }

    /**
     * Returns the {@code boolean} value to indicate that client authentication
     * is required.
     *
     * @return the {@code boolean} value to indicate that client authentication
     * is required
     */
    public boolean getNeedClientAuth() {
        return this.needClientAuth;
    }

    /**
     * Sets the {@code boolean} value to indicate that client authentication
     * is required with the provided {@code boolean} value. An
     * {@code IllegalStateException} is thrown if this
     * {@code DtlsDatagramSocket} is in use (sending and/or receiving packets).
     *
     * @param need the provided {@code boolean} value to indicate that client
     *             authentication is required
     */
    public void setNeedClientAuth(final boolean need) {
        if (this.inUse) {
            throw new IllegalStateException("DtlsDatagramSocket in use");
        }
        this.needClientAuth = need;
    }

    /**
     * Returns the {@code boolean} value to indicate that this
     * {@code DtlsDatagramSocket} will operate in client mode, meaning it will
     * initiate the communication with the peer.
     *
     * @return the {@code boolean} value to indicate that this
     * {@code DtlsDatagramSocket} will operate in client mode
     */
    public boolean getUseClientMode() {
        return this.useClientMode;
    }

    /**
     * Sets the {@code boolean} value to indicate that this
     * {@code DtlsDatagramSocket} will operate in client mode with the provided
     * {@code boolean} value. An {@code IllegalStateException} is thrown if
     * this {@code DtlsDatagramSocket} is in use (sending and/or receiving
     * packets).
     *
     * @param mode the provided {@code boolean} value to indicate that this
     *             {@code DtlsDatagramSocket} will operate in client mode with
     *             the provided {@code boolean} value
     */
    public void setUseClientMode(final boolean mode) {
        if (this.inUse) {
            throw new IllegalStateException("DtlsDatagramSocket in use");
        }
        this.useClientMode = mode;
    }

    /**
     * Returns the {@code boolean} value to indicate that client authentication
     * is requested.
     *
     * @return the {@code boolean} value to indicate that client authentication
     * is requested
     */
    public boolean getWantClientAuth() {
        return this.wantClientAuth;
    }

    /**
     * Sets the {@code boolean} value to indicate that client authentication is
     * requested with the provided {@code boolean} value. An
     * {@code IllegalStateException} is thrown if this
     * {@code DtlsDatagramSocket} is in use (sending and/or receiving packets).
     *
     * @param want the provided {@code boolean} value to indicate that client
     *             authentication is requested
     */
    public void setWantClientAuth(final boolean want) {
        if (this.inUse) {
            throw new IllegalStateException("DtlsDatagramSocket in use");
        }
        this.wantClientAuth = want;
    }

    @Override
    public synchronized void receive(
            final DatagramPacket p) throws IOException {
        this.inUse = true;
        if (this.useClientMode) {
            this.waitForEstablishedConnections();
        }
        byte[] buffer = new byte[this.maximumPacketSize.get()];
        DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
        this.datagramSocket.receive(packet);
        SocketAddress socketAddress = packet.getSocketAddress();
        Connection connection = this.connections.get(
                packet.getSocketAddress());
        boolean connectionPresent = connection != null;
        if (!connectionPresent) {
            connection = new Connection(this, socketAddress);
            this.connections.put(socketAddress, connection);
        }
        try {
            connection.receive(buffer, packet, p);
        } catch (Throwable t) {
            this.connections.remove(socketAddress);
            if (connectionPresent && connection.isEstablished()) {
                this.establishedConnectionCount.decrementAndGet();
            }
            throw t;
        }
        if (!connectionPresent && connection.isEstablished()) {
            this.establishedConnectionCount.incrementAndGet();
        }
    }

    @Override
    public void send(final DatagramPacket p) throws IOException {
        this.inUse = true;
        SocketAddress socketAddress = p.getSocketAddress();
        Connection connection = this.connections.get(p.getSocketAddress());
        boolean connectionPresent = connection != null;
        if (!connectionPresent) {
            connection = new Connection(this, socketAddress);
            this.connections.put(socketAddress, connection);
        }
        try {
            connection.send(p);
        } catch (Throwable t) {
            this.connections.remove(socketAddress);
            if (connectionPresent && connection.isEstablished()) {
                this.establishedConnectionCount.decrementAndGet();
            }
            throw t;
        }
        if (!connectionPresent && connection.isEstablished()) {
            this.establishedConnectionCount.incrementAndGet();
        }
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
     * when this {@code DtlsDatagramSocket} is in client mode and is about to
     * wait for receiving any datagram packets.
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
         * The {@code Logger}.
         */
        private final Logger logger;

        /**
         * The maximum size of a datagram packet.
         */
        private final int maximumPacketSize;

        /**
         * The {@code SocketAddress} of the peer.
         */
        private final SocketAddress peerSocketAddress;

        /**
         * The {@code SSLEngine} for DTLS operations.
         */
        private final SSLEngine sslEngine;

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
            String[] enabledCipherSuites =
                    dtlsDatagramSock.getEnabledCipherSuites();
            if (enabledCipherSuites.length > 0) {
                engine.setEnabledCipherSuites(enabledCipherSuites);
            }
            String[] enabledProtocols = dtlsDatagramSock.getEnabledProtocols();
            if (enabledProtocols.length > 0) {
                engine.setEnabledProtocols(enabledProtocols);
            }
            engine.setNeedClientAuth(dtlsDatagramSock.getNeedClientAuth());
            engine.setUseClientMode(dtlsDatagramSock.getUseClientMode());
            engine.setWantClientAuth(dtlsDatagramSock.getWantClientAuth());
            int maxPacketSize = dtlsDatagramSock.getMaximumPacketSize();
            SSLParameters sslParameters = new SSLParameters();
            sslParameters.setMaximumPacketSize(maxPacketSize);
            engine.setSSLParameters(sslParameters);
            this.datagramSocket = dtlsDatagramSock.datagramSocket;
            this.dtlsDatagramSocket = dtlsDatagramSock;
            this.established = false;
            this.logger = LoggerFactory.getLogger(Connection.class);
            this.maximumPacketSize = maxPacketSize;
            this.peerSocketAddress = peerSocketAddr;
            this.sslEngine = engine;
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
                case OK:
                    // OK
                    break;
                case BUFFER_OVERFLOW:
                    this.logger.debug(ObjectLogMessageHelper.objectLogMessage(
                            this,
                            "BUFFER_OVERFLOW, handshake status is %s",
                            hs));
                    // the client maximum fragment size config does not work?
                    throw new SSLException("Buffer overflow: incorrect "
                            + "client maximum fragment size");
                case BUFFER_UNDERFLOW:
                    this.logger.debug(ObjectLogMessageHelper.objectLogMessage(
                            this,
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
                    throw new SSLException(String.format(
                            "SSL engine closed, handshake status is %s",
                            hs));
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
            this.logger.debug(ObjectLogMessageHelper.objectLogMessage(
                    this,
                    "Produced %s packets",
                    packets.size()));
            for (DatagramPacket p : packets) {
                this.datagramSocket.send(p);
            }
            if (finished) {
                this.logger.debug(ObjectLogMessageHelper.objectLogMessage(
                        this,
                        "Handshake status is FINISHED after producing "
                                + "handshake packets, finish the loop"));
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
            this.logger.debug(
                    ObjectLogMessageHelper.objectLogMessage(
                            this,
                            "Reproduced %s packets",
                            packets.size()));
            for (DatagramPacket p : packets) {
                this.datagramSocket.send(p);
            }
            if (finished) {
                this.logger.debug(
                        ObjectLogMessageHelper.objectLogMessage(
                                this,
                                "Handshake status is FINISHED "
                                        + "after calling "
                                        + "reproduceHandshakePackets(), "
                                        + "finish the loop"));
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
                this.logger.debug(ObjectLogMessageHelper.objectLogMessage(
                        this,
                        "Handshaking (iteration ID: %s, status: %s)",
                        loops,
                        hs));
                switch (hs) {
                    case NEED_UNWRAP:
                    case NEED_UNWRAP_AGAIN:
                        this.logger.debug(ObjectLogMessageHelper.objectLogMessage(
                                this,
                                "Receive DTLS records, handshake status is %s",
                                hs));
                        ByteBuffer inNetData;
                        ByteBuffer inAppData;
                        if (hs.equals(
                                SSLEngineResult.HandshakeStatus.NEED_UNWRAP)) {
                            byte[] buffer;
                            DatagramPacket packet;
                            if (receivedDatagramBffr.length == 0
                                    && receivedDatagramPckt == null) {
                                buffer = new byte[this.maximumPacketSize];
                                packet = new DatagramPacket(buffer, buffer.length);
                                try {
                                    this.datagramSocket.receive(packet);
                                } catch (SocketTimeoutException ste) {
                                    this.logger.debug(
                                            ObjectLogMessageHelper.objectLogMessage(
                                                    this,
                                                    "Warning: %s",
                                                    ste));
                                    endLoops = this.sendReproducedHandshakePackets();
                                    this.logger.debug(
                                            ObjectLogMessageHelper.objectLogMessage(
                                                    this,
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
                            inAppData = ByteBuffer.allocate(this.maximumPacketSize);
                        } else {
                            inNetData = ByteBuffer.allocate(0);
                            inAppData = ByteBuffer.allocate(this.maximumPacketSize);
                        }
                        SSLEngineResult r = this.sslEngine.unwrap(
                                inNetData, inAppData);
                        SSLEngineResult.Status rs = r.getStatus();
                        hs = r.getHandshakeStatus();
                        this.checkAfterReceivingHandshakePacket(rs, hs);
                        if (hs.equals(SSLEngineResult.HandshakeStatus.FINISHED)) {
                            this.logger.debug(ObjectLogMessageHelper.objectLogMessage(
                                    this,
                                    "Handshake status is FINISHED, finish the "
                                            + "loop"));
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
                        this.logger.debug(ObjectLogMessageHelper.objectLogMessage(
                                this,
                                "Handshake status is NOT_HANDSHAKING, finish the "
                                        + "loop"));
                        endLoops = true;
                        break;
                    case FINISHED:
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
            this.logger.debug(ObjectLogMessageHelper.objectLogMessage(
                    this,
                    "Handshake finished, status is %s",
                    hs));
            if (this.sslEngine.getHandshakeSession() != null) {
                throw new SSLException(
                        "Handshake finished, but handshake session is not "
                                + "null");
            }
            SSLSession session = this.sslEngine.getSession();
            if (session == null) {
                throw new SSLException("Handshake finished, but session is "
                        + "null");
            }
            this.logger.debug(ObjectLogMessageHelper.objectLogMessage(
                    this,
                    "Negotiated protocol is %s",
                    session.getProtocol()));
            this.logger.debug(ObjectLogMessageHelper.objectLogMessage(
                    this,
                    "Negotiated cipher suite is %s",
                    session.getCipherSuite()));
            // handshake status should be NOT_HANDSHAKING
            //
            // According to the spec, SSLEngine.getHandshakeStatus() can't
            // return FINISHED.
            if (!hs.equals(SSLEngineResult.HandshakeStatus.NOT_HANDSHAKING)) {
                throw new SSLException(String.format(
                        "Unexpected handshake status %s", hs));
            }
            this.established = true;
        }

        /**
         * Returns the {@code boolean} value to indicate that this
         * {@code Connection} is established.
         *
         * @return the {@code boolean} value to indicate that this
         * {@code Connection} is established
         */
        public boolean isEstablished() {
            return this.established;
        }

        /**
         * Checks the provided {@code Status} before producing application
         * packets.
         *
         * @param rs the provided {@code Status}
         * @throws IOException if an I/O error occurs
         */
        private void checkBeforeProducingApplicationPackets(
                final SSLEngineResult.Status rs)
                throws IOException {
            switch (rs) {
                case BUFFER_OVERFLOW:
                    throw new SSLException("Buffer overflow during wrapping");
                case BUFFER_UNDERFLOW:
                    throw new SSLException("Buffer underflow during wrapping");
                case CLOSED:
                    throw new SSLException("SSLEngine has closed");
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
         * Produces a {@code List} of application {@code DatagramPacket}s with
         * the provided {@code ByteBuffer} of application data to be wrapped
         * and the provided {@code SocketAddress} of the peer the application
         * data will be sent to.
         *
         * @param outAppData     the provided {@code ByteBuffer} of application
         *                       data to be wrapped
         * @param peerSocketAddr the provided {@code SocketAddress} of the peer
         *                       the application data will be sent to
         * @return a {@code List} of application {@code DatagramPacket}s
         * @throws IOException if an I/O error occurs
         */
        private List<DatagramPacket> produceApplicationPackets(
                final ByteBuffer outAppData,
                final SocketAddress peerSocketAddr) throws IOException {
            List<DatagramPacket> packets = new ArrayList<>();
            ByteBuffer outNetData = ByteBuffer.allocate(this.maximumPacketSize);
            SSLEngineResult r = this.sslEngine.wrap(outAppData, outNetData);
            outNetData.flip();
            SSLEngineResult.Status rs = r.getStatus();
            this.checkBeforeProducingApplicationPackets(rs);
            if (outNetData.hasRemaining()) {
                byte[] bytes = new byte[outNetData.remaining()];
                outNetData.get(bytes);
                DatagramPacket packet = new DatagramPacket(
                        bytes, bytes.length, peerSocketAddr);
                packets.add(packet);
            }
            return packets;
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
                    this.logger.debug(ObjectLogMessageHelper.objectLogMessage(
                            this,
                            "Produce handshake packets: BUFFER_UNDERFLOW "
                                    + "occurred"));
                    this.logger.debug(ObjectLogMessageHelper.objectLogMessage(
                            this,
                            "Produce handshake packets: Handshake status: %s",
                            hs));
                    // bad packet, or the client maximum fragment size
                    // config does not work?
                    if (!hs.equals(
                            SSLEngineResult.HandshakeStatus.NOT_HANDSHAKING)) {
                        throw new SSLException("Buffer underflow: incorrect "
                                + "server maximum fragment size");
                    } // otherwise, ignore this packet
                case CLOSED:
                    throw new SSLException("SSLEngine has closed");
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
                        this.maximumPacketSize);
                SSLEngineResult r = this.sslEngine.wrap(outAppData, outNetData);
                outNetData.flip();
                SSLEngineResult.Status rs = r.getStatus();
                SSLEngineResult.HandshakeStatus hs = r.getHandshakeStatus();
                this.logger.debug(ObjectLogMessageHelper.objectLogMessage(
                        this,
                        "Producing handshake packet (iteration ID: %s, result "
                                + "status: %s, handshake status: %s)",
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
                    this.logger.debug(ObjectLogMessageHelper.objectLogMessage(
                            this,
                            "Produce handshake packets: "
                                    + "Handshake status is FINISHED, finish the loop"));
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
                    buffer = new byte[this.maximumPacketSize];
                    packet = new DatagramPacket(buffer, buffer.length);
                    this.datagramSocket.receive(packet);
                } else {
                    buffer = receivedDatagramBffr;
                    packet = receivedDatagramPckt;
                    receivedDatagramBffr = new byte[]{};
                    receivedDatagramPckt = null;
                }
                ByteBuffer inNetData = ByteBuffer.wrap(
                        buffer, 0, packet.getLength());
                ByteBuffer inAppData = ByteBuffer.allocate(p.getLength());
                this.sslEngine.unwrap(inNetData, inAppData);
                inAppData.flip();
                if (inAppData.hasRemaining()) {
                    byte[] bytes = new byte[inAppData.remaining()];
                    inAppData.get(bytes);
                    p.setSocketAddress(packet.getSocketAddress());
                    p.setData(bytes, 0, bytes.length);
                    p.setLength(bytes.length);
                    return;
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
                throw new SSLException(
                        "Handshake shouldn't need additional tasks");
            }
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
            ByteBuffer outAppData = ByteBuffer.wrap(Arrays.copyOfRange(
                    p.getData(), p.getOffset(), p.getLength()));
            // Note: have not considered the packet losses
            List<DatagramPacket> packets = this.produceApplicationPackets(
                    outAppData, p.getSocketAddress());
            outAppData.flip();
            for (DatagramPacket packet : packets) {
                this.datagramSocket.send(packet);
            }
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

}
