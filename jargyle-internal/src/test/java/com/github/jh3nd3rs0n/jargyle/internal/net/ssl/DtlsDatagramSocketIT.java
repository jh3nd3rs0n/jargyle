package com.github.jh3nd3rs0n.jargyle.internal.net.ssl;

import com.github.jh3nd3rs0n.jargyle.internal.VirtualThreadPerTaskExecutorOrCachedThreadPoolFactory;
import com.github.jh3nd3rs0n.jargyle.internal.VirtualThreadPerTaskExecutorOrTripleThreadPoolFactory;
import com.github.jh3nd3rs0n.jargyle.test.help.net.DatagramServer;
import com.github.jh3nd3rs0n.jargyle.test.help.security.KeyStoreResourceConstants;
import com.github.jh3nd3rs0n.jargyle.test.help.string.StringConstants;
import org.junit.*;
import org.junit.rules.Timeout;

import javax.net.ssl.SSLContext;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.nio.charset.StandardCharsets;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;

public class DtlsDatagramSocketIT {

    private static final int RECEIVE_BUFFER_SIZE =
            DatagramServer.RECEIVE_BUFFER_SIZE;

    private static SSLContext clientDtlsContextUsingDtlsv1point2;

    private static DatagramServer echoDtlsDatagramServerUsingDtlsv1Point2;
    private static int echoDtlsDatagramServerPortUsingDtlsv1point2;

    @Rule
    public Timeout globalTimeout = Timeout.builder()
            .withTimeout(60, TimeUnit.SECONDS)
            .withLookingForStuckThread(true)
            .build();

    private static String echo(
            final ClientDtlsDatagramSocketFactory clientDtlsDatagramSocketFactory,
            final String string,
            final int echoDtlsEchoDatagramServerPort) throws IOException {
        String returningString;
        try (DatagramSocket dtlsDatagramSocket =
                     clientDtlsDatagramSocketFactory.newClientDatagramSocket()) {
            byte[] stringBytes = string.getBytes(StandardCharsets.UTF_8);
            DatagramPacket packet = new DatagramPacket(
                    stringBytes,
                    stringBytes.length,
                    DatagramServer.INET_ADDRESS,
                    echoDtlsEchoDatagramServerPort);
            dtlsDatagramSocket.send(packet);
            byte[] buffer = new byte[RECEIVE_BUFFER_SIZE];
            packet = new DatagramPacket(buffer, buffer.length);
            dtlsDatagramSocket.receive(packet);
            returningString = new String(Arrays.copyOfRange(
                    packet.getData(),
                    packet.getOffset(),
                    packet.getOffset() + packet.getLength()));
        }
        return returningString;
    }

    private static DatagramServer newEchoDtlsDatagramServer(
            final ServerDtlsDatagramSocketFactory serverDtlsDatagramSocketFactory) {
        return new DatagramServer(
                serverDtlsDatagramSocketFactory,
                0,
                new VirtualThreadPerTaskExecutorOrTripleThreadPoolFactory(),
                new VirtualThreadPerTaskExecutorOrCachedThreadPoolFactory(),
                new EchoWorkerFactory());
    }

    @BeforeClass
    public static void setUpBeforeClass() throws IOException, NoSuchAlgorithmException, KeyManagementException {

        // System.setProperty("javax.net.debug", "ssl,handshake");

        clientDtlsContextUsingDtlsv1point2 = SslContextHelper.getSslContext(
                "DTLSv1.2",
                null,
                TrustManagerHelper.getTrustManagers(
                        new ByteArrayInputStream(KeyStoreResourceConstants.JARGYLE_TEST_HELP_SECURITY_KEY_STORE_FILE_1.getContentAsBytes()),
                        KeyStoreResourceConstants.JARGYLE_TEST_HELP_SECURITY_KEY_STORE_PASSWORD_FILE_1.getContentAsString().toCharArray(),
                        null));

        SSLContext serverDtlsContextUsingDtlsv1point2 = SslContextHelper.getSslContext(
                "DTLSv1.2",
                KeyManagerHelper.getKeyManagers(
                        new ByteArrayInputStream(KeyStoreResourceConstants.JARGYLE_TEST_HELP_SECURITY_KEY_STORE_FILE_1.getContentAsBytes()),
                        KeyStoreResourceConstants.JARGYLE_TEST_HELP_SECURITY_KEY_STORE_PASSWORD_FILE_1.getContentAsString().toCharArray(),
                        null),
                null);

        echoDtlsDatagramServerUsingDtlsv1Point2 = newEchoDtlsDatagramServer(
                new ServerDtlsDatagramSocketFactory(serverDtlsContextUsingDtlsv1point2));
        echoDtlsDatagramServerUsingDtlsv1Point2.start();
        echoDtlsDatagramServerPortUsingDtlsv1point2 =
                echoDtlsDatagramServerUsingDtlsv1Point2.getPort();

    }

    @AfterClass
    public static void tearDownAfterClass() throws IOException {
        // System.clearProperty("javax.net.debug");
        if (echoDtlsDatagramServerUsingDtlsv1Point2 != null
                && !echoDtlsDatagramServerUsingDtlsv1Point2.getState().equals(DatagramServer.State.STOPPED)) {
            echoDtlsDatagramServerUsingDtlsv1Point2.stop();
        }
    }

    @Test
    public void testEchoUsingDtlsv1point201() throws IOException {
        String string = StringConstants.STRING_01;
        String returningString = echo(
                new ClientDtlsDatagramSocketFactory(clientDtlsContextUsingDtlsv1point2),
                string,
                echoDtlsDatagramServerPortUsingDtlsv1point2);
        Assert.assertEquals(string, returningString);
    }

    @Test
    public void testEchoUsingDtlsv1point202() throws IOException {
        String string = StringConstants.STRING_02;
        String returningString = echo(
                new ClientDtlsDatagramSocketFactory(clientDtlsContextUsingDtlsv1point2),
                string,
                echoDtlsDatagramServerPortUsingDtlsv1point2);
        Assert.assertEquals(string, returningString);
    }

    @Test
    public void testEchoUsingDtlsv1point203() throws IOException {
        String string = StringConstants.STRING_03;
        String returningString = echo(
                new ClientDtlsDatagramSocketFactory(clientDtlsContextUsingDtlsv1point2),
                string,
                echoDtlsDatagramServerPortUsingDtlsv1point2);
        Assert.assertEquals(string, returningString);
    }

    @Test
    public void testEchoUsingDtlsv1point204() throws IOException {
        String string = StringConstants.STRING_04;
        String returningString = echo(
                new ClientDtlsDatagramSocketFactory(clientDtlsContextUsingDtlsv1point2),
                string,
                echoDtlsDatagramServerPortUsingDtlsv1point2);
        Assert.assertEquals(string, returningString);
    }

    @Test
    public void testEchoUsingDtlsv1point205() throws IOException {
        String string = StringConstants.STRING_05;
        String returningString = echo(
                new ClientDtlsDatagramSocketFactory(clientDtlsContextUsingDtlsv1point2),
                string,
                echoDtlsDatagramServerPortUsingDtlsv1point2);
        Assert.assertEquals(string, returningString);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetChannelForUnsupportedOperationException() throws SocketException {
        try (DtlsDatagramSocket dtlsDatagramSocket = new DtlsDatagramSocket(
                new DatagramSocket(0, InetAddress.getLoopbackAddress()),
                clientDtlsContextUsingDtlsv1point2)) {
            dtlsDatagramSocket.getChannel();
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSendDatagramPacketForIllegalArgumentException() throws IOException {
        try (DtlsDatagramSocket dtlsDatagramSocket = new DtlsDatagramSocket(
                new DatagramSocket(0, InetAddress.getLoopbackAddress()),
                clientDtlsContextUsingDtlsv1point2)) {
            dtlsDatagramSocket.connect(InetAddress.getLoopbackAddress(), 2323);
            byte[] bytes = StringConstants.STRING_01.getBytes(
                    StandardCharsets.UTF_8);
            dtlsDatagramSocket.send(new DatagramPacket(
                    bytes,
                    bytes.length,
                    InetAddress.getLoopbackAddress(),
                    3232));
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetWrappedReceiveBufferSizeIntForIllegalArgumentException() throws SocketException {
        try (DtlsDatagramSocket dtlsDatagramSocket = new DtlsDatagramSocket(
                new DatagramSocket(0, InetAddress.getLoopbackAddress()),
                clientDtlsContextUsingDtlsv1point2)) {
            dtlsDatagramSocket.setWrappedReceiveBufferSize(-1);
        }
    }

    private static final class ClientDtlsDatagramSocketFactory {

        private final SSLContext clientDtlsContext;

        public ClientDtlsDatagramSocketFactory(
                final SSLContext clientDtlsCntxt) {
            this.clientDtlsContext = clientDtlsCntxt;
        }

        public DatagramSocket newClientDatagramSocket() throws SocketException {
            DtlsDatagramSocket dtlsDatagramSocket = new DtlsDatagramSocket(
                    new DatagramSocket(0, InetAddress.getLoopbackAddress()),
                    this.clientDtlsContext);
            dtlsDatagramSocket.setUseClientMode(true);
            return dtlsDatagramSocket;
        }

    }

    private static final class EchoWorker extends DatagramServer.Worker {

        public EchoWorker(final DatagramServer.ClientPackets clientPckts) {
            super(clientPckts);
        }

        @Override
        protected void doWork() {
            DatagramPacket packet = this.getClientPackets().removeReceived();
            byte[] bytes = Arrays.copyOfRange(
                    packet.getData(),
                    packet.getOffset(),
                    packet.getLength());
            packet = new DatagramPacket(
                    bytes,
                    bytes.length,
                    packet.getSocketAddress());
            this.getClientPackets().addSendable(packet);
        }

    }

    private static final class EchoWorkerFactory
            extends DatagramServer.WorkerFactory {

        public EchoWorkerFactory() {
        }

        @Override
        public DatagramServer.Worker newWorker(
                final DatagramServer.ClientPackets clientPckts) {
            return new EchoWorker(clientPckts);
        }

    }

    private static final class ServerDtlsDatagramSocketFactory
            extends DatagramServer.ServerDatagramSocketFactory {

        private final SSLContext serverDtlsContext;

        public ServerDtlsDatagramSocketFactory(
                final SSLContext serverDtlsCntxt) {
            this.serverDtlsContext = serverDtlsCntxt;
        }

        @Override
        public DatagramSocket newServerDatagramSocket(
                final int port,
                final InetAddress bindInetAddress) throws SocketException {
            DtlsDatagramSocket dtlsDatagramSocket = new DtlsDatagramSocket(
                    new DatagramSocket(port, bindInetAddress),
                    this.serverDtlsContext);
            dtlsDatagramSocket.setUseClientMode(false);
            return dtlsDatagramSocket;
        }

    }

}