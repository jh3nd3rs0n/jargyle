package com.github.jh3nd3rs0n.jargyle.internal.net.ssl;

import com.github.jh3nd3rs0n.jargyle.internal.VirtualThreadPerTaskExecutorOrCachedThreadPoolFactory;
import com.github.jh3nd3rs0n.jargyle.internal.VirtualThreadPerTaskExecutorOrDoubleThreadPoolFactory;
import com.github.jh3nd3rs0n.jargyle.test.help.net.DatagramTestServer;
import com.github.jh3nd3rs0n.jargyle.test.help.security.TestKeyStoreResourceConstants;
import com.github.jh3nd3rs0n.jargyle.test.help.string.TestStringConstants;
import org.junit.*;
import org.junit.rules.Timeout;

import javax.net.ssl.SSLContext;
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

public class DtlsDatagramSocketTest {

    private static final int RECEIVE_BUFFER_SIZE =
            DatagramTestServer.RECEIVE_BUFFER_SIZE;

    private static SSLContext clientDtlsContextUsingDtlsv1point2;

    private static DatagramTestServer echoDtlsDatagramTestServerUsingDtlsv1point2;
    private static int echoDtlsDatagramTestServerPortUsingDtlsv1point2;

    @Rule
    public Timeout globalTimeout = Timeout.builder()
            .withTimeout(60, TimeUnit.SECONDS)
            .withLookingForStuckThread(true)
            .build();

    private static String echo(
            final ClientDtlsDatagramSocketFactory clientDtlsDatagramSocketFactory,
            final String string,
            final int echoDtlsDatagramEchoServerPort) throws IOException {
        String returningString;
        try (DatagramSocket dtlsDatagramSocket =
                     clientDtlsDatagramSocketFactory.newClientDatagramSocket()) {
            byte[] stringBytes = string.getBytes(StandardCharsets.UTF_8);
            DatagramPacket packet = new DatagramPacket(
                    stringBytes,
                    stringBytes.length,
                    DatagramTestServer.INET_ADDRESS,
                    echoDtlsDatagramEchoServerPort);
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

    private static DatagramTestServer newEchoDtlsDatagramTestServer(
            final ServerDtlsDatagramSocketFactory serverDtlsDatagramSocketFactory) {
        return new DatagramTestServer(
                serverDtlsDatagramSocketFactory,
                0,
                new VirtualThreadPerTaskExecutorOrDoubleThreadPoolFactory(),
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
                        TestKeyStoreResourceConstants.JARGYLE_TEST_HELP_SECURITY_KEY_STORE_FILE_1.getInputStream(),
                        TestKeyStoreResourceConstants.JARGYLE_TEST_HELP_SECURITY_KEY_STORE_PASSWORD_FILE_1.getContentAsString().toCharArray(),
                        null));

        SSLContext serverDtlsContextUsingDtlsv1point2 = SslContextHelper.getSslContext(
                "DTLSv1.2",
                KeyManagerHelper.getKeyManagers(
                        TestKeyStoreResourceConstants.JARGYLE_TEST_HELP_SECURITY_KEY_STORE_FILE_1.getInputStream(),
                        TestKeyStoreResourceConstants.JARGYLE_TEST_HELP_SECURITY_KEY_STORE_PASSWORD_FILE_1.getContentAsString().toCharArray(),
                        null),
                null);

        echoDtlsDatagramTestServerUsingDtlsv1point2 = newEchoDtlsDatagramTestServer(
                new ServerDtlsDatagramSocketFactory(serverDtlsContextUsingDtlsv1point2));
        echoDtlsDatagramTestServerUsingDtlsv1point2.start();
        echoDtlsDatagramTestServerPortUsingDtlsv1point2 =
                echoDtlsDatagramTestServerUsingDtlsv1point2.getPort();

    }

    @AfterClass
    public static void tearDownAfterClass() throws IOException {
        // System.clearProperty("javax.net.debug");
        if (echoDtlsDatagramTestServerUsingDtlsv1point2 != null) {
            if (!echoDtlsDatagramTestServerUsingDtlsv1point2.getState().equals(DatagramTestServer.State.STOPPED)) {
                echoDtlsDatagramTestServerUsingDtlsv1point2.stop();
            }
        }
    }

    @Test
    public void testEchoUsingDtlsv1point201() throws IOException {
        String string = TestStringConstants.STRING_01;
        String returningString = echo(
                new ClientDtlsDatagramSocketFactory(clientDtlsContextUsingDtlsv1point2),
                string,
                echoDtlsDatagramTestServerPortUsingDtlsv1point2);
        Assert.assertEquals(string, returningString);
    }

    @Test
    public void testEchoUsingDtlsv1point202() throws IOException {
        String string = TestStringConstants.STRING_02;
        String returningString = echo(
                new ClientDtlsDatagramSocketFactory(clientDtlsContextUsingDtlsv1point2),
                string,
                echoDtlsDatagramTestServerPortUsingDtlsv1point2);
        Assert.assertEquals(string, returningString);
    }

    @Test
    public void testEchoUsingDtlsv1point203() throws IOException {
        String string = TestStringConstants.STRING_03;
        String returningString = echo(
                new ClientDtlsDatagramSocketFactory(clientDtlsContextUsingDtlsv1point2),
                string,
                echoDtlsDatagramTestServerPortUsingDtlsv1point2);
        Assert.assertEquals(string, returningString);
    }

    @Test
    public void testEchoUsingDtlsv1point204() throws IOException {
        String string = TestStringConstants.STRING_04;
        String returningString = echo(
                new ClientDtlsDatagramSocketFactory(clientDtlsContextUsingDtlsv1point2),
                string,
                echoDtlsDatagramTestServerPortUsingDtlsv1point2);
        Assert.assertEquals(string, returningString);
    }

    @Test
    public void testEchoUsingDtlsv1point205() throws IOException {
        String string = TestStringConstants.STRING_05;
        String returningString = echo(
                new ClientDtlsDatagramSocketFactory(clientDtlsContextUsingDtlsv1point2),
                string,
                echoDtlsDatagramTestServerPortUsingDtlsv1point2);
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
            byte[] bytes = TestStringConstants.STRING_01.getBytes(
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

    private static final class EchoWorker extends DatagramTestServer.Worker {

        public EchoWorker(final DatagramTestServer.ClientPackets clientPckts) {
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
            extends DatagramTestServer.WorkerFactory {

        public EchoWorkerFactory() {
        }

        @Override
        public DatagramTestServer.Worker newWorker(
                final DatagramTestServer.ClientPackets clientPckts) {
            return new EchoWorker(clientPckts);
        }

    }

    private static final class ServerDtlsDatagramSocketFactory
            extends DatagramTestServer.ServerDatagramSocketFactory {

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