package com.github.jh3nd3rs0n.jargyle.client;

import com.github.jh3nd3rs0n.jargyle.common.net.*;
import com.github.jh3nd3rs0n.jargyle.test.help.net.Server;
import com.github.jh3nd3rs0n.jargyle.test.help.thread.ThreadHelper;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import javax.net.ssl.SSLSocket;
import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.NetworkInterface;
import java.net.Socket;

public class ConnectingClientSocketBuilderIT {

    private static Server server;
    private static int serverPort;

    @BeforeClass
    public static void setUpBeforeClass() throws IOException {
        server = new Server(
                new Server.DefaultServerSocketFactory(),
                0,
                new VirtualThreadPerTaskExecutorOrCachedThreadPoolFactory(),
                new Server.DefaultWorkerFactory());
        server.start();
        serverPort = server.getPort();
    }

    @AfterClass
    public static void tearDownAfterClass() throws IOException {
        if (server != null
                && !server.getState().equals(Server.State.STOPPED)) {
            server.stop();
        }
        ThreadHelper.interruptibleSleepForThreeSeconds();
    }

    @Test
    public void testGetConnectedClientSocket01() throws IOException {
        SocksServerUri socksServerUri = Scheme.SOCKS5.newSocksServerUri(
                "localhost", serverPort);
        Properties properties = Properties.of();
        ClientSocketBuilder clientSocketBuilder =
                socksServerUri.newSocksClient(properties)
                        .newClientSocketBuilder();
        try (Socket socket = clientSocketBuilder.proceedToConfigure(
                new Socket())
                .proceedToConnect()
                .getConnectedClientSocket()) {
            Assert.assertEquals(
                    new InetSocketAddress(
                            socksServerUri.getHost().toInetAddress(),
                            socksServerUri.getPort().intValue()),
                    socket.getRemoteSocketAddress());
            Assert.assertEquals(Socket.class, socket.getClass());
        }
    }

    @Test
    public void testGetConnectedClientSocket02() throws IOException {
        SocksServerUri socksServerUri = Scheme.SOCKS5.newSocksServerUri(
                "localhost", serverPort);
        Properties properties = Properties.of(
                GeneralPropertySpecConstants.CLIENT_BIND_HOST.newPropertyWithParsedValue(
                        InetAddress.getLoopbackAddress().getHostAddress()));
        ClientSocketBuilder clientSocketBuilder =
                socksServerUri.newSocksClient(properties)
                        .newClientSocketBuilder();
        try (Socket socket = clientSocketBuilder.proceedToConfigure(
                        new Socket())
                .proceedToConnect()
                .setToBind(true)
                .getConnectedClientSocket()) {
            Assert.assertEquals(
                    InetAddress.getLoopbackAddress(), socket.getInetAddress());
            Assert.assertEquals(
                    new InetSocketAddress(
                            socksServerUri.getHost().toInetAddress(),
                            socksServerUri.getPort().intValue()),
                    socket.getRemoteSocketAddress());
            Assert.assertEquals(Socket.class, socket.getClass());
        }
    }

    @Test
    public void testGetConnectedClientSocket03() throws IOException {
        NetworkInterface networkInterface = NetworkInterface.getByInetAddress(
                InetAddress.getLoopbackAddress());
        NetInterface netInterface = NetInterface.newInstance(networkInterface);
        HostAddressTypes hostAddressTypes = HostAddressTypes.of(HostAddressType.IPV4);
        HostAddress hostAddress = netInterface.getHostAddresses(hostAddressTypes).get(0);
        SocksServerUri socksServerUri = Scheme.SOCKS5.newSocksServerUri(
                "localhost", serverPort);
        Properties properties = Properties.of(
                GeneralPropertySpecConstants.CLIENT_NET_INTERFACE.newProperty(
                        netInterface),
                GeneralPropertySpecConstants.CLIENT_BIND_HOST_ADDRESS_TYPES.newProperty(
                        hostAddressTypes));
        ClientSocketBuilder clientSocketBuilder =
                socksServerUri.newSocksClient(properties)
                        .newClientSocketBuilder();
        try (Socket socket = clientSocketBuilder.proceedToConfigure(
                        new Socket())
                .proceedToConnect()
                .setToBind(true)
                .getConnectedClientSocket()) {
            Assert.assertEquals(
                    hostAddress.toString(), socket.getInetAddress().getHostAddress());
            Assert.assertEquals(
                    new InetSocketAddress(
                            socksServerUri.getHost().toInetAddress(),
                            socksServerUri.getPort().intValue()),
                    socket.getRemoteSocketAddress());
            Assert.assertEquals(Socket.class, socket.getClass());
        }
    }

    @Test
    public void testGetConnectedClientSocket04() throws IOException {
        SocksServerUri socksServerUri = Scheme.SOCKS5.newSocksServerUri(
                "localhost", serverPort);
        Properties properties = Properties.of(
                SslPropertySpecConstants.SSL_ENABLED.newProperty(true));
        ClientSocketBuilder clientSocketBuilder =
                socksServerUri.newSocksClient(properties)
                        .newClientSocketBuilder();
        try (Socket socket = clientSocketBuilder.proceedToConfigure(
                        new Socket())
                .proceedToConnect()
                .getConnectedClientSocket()) {
            Assert.assertEquals(
                    new InetSocketAddress(
                            socksServerUri.getHost().toInetAddress(),
                            socksServerUri.getPort().intValue()),
                    socket.getRemoteSocketAddress());
            Assert.assertTrue(socket instanceof SSLSocket);
        }
    }

    @Test(expected = IOException.class)
    public void testGetConnectedClientSocketForIOException01() throws IOException {
        SocksServerUri socksServerUri = Scheme.SOCKS5.newSocksServerUri(
                "localhost", 1);
        Properties properties = Properties.of();
        ClientSocketBuilder clientSocketBuilder =
                socksServerUri.newSocksClient(properties)
                        .newClientSocketBuilder();
        try (Socket socket = new Socket()) {
            clientSocketBuilder.proceedToConfigure(socket)
                    .proceedToConnect()
                    .setConnectTimeout(1)
                    .getConnectedClientSocket();
        }
    }

    @Test(expected = IOException.class)
    public void testGetConnectedClientSocketForIOException02() throws IOException {
        SocksServerUri socksServerUri = Scheme.SOCKS5.newSocksServerUri(
                "localhost", serverPort);
        Properties properties = Properties.of(
                GeneralPropertySpecConstants.CLIENT_BIND_HOST.newPropertyWithParsedValue(
                        InetAddress.getLoopbackAddress().getHostAddress()),
                GeneralPropertySpecConstants.CLIENT_BIND_PORT_RANGES.newProperty(
                        PortRanges.of(PortRange.of(Port.valueOf(serverPort)))));
        ClientSocketBuilder clientSocketBuilder =
                socksServerUri.newSocksClient(properties)
                        .newClientSocketBuilder();
        try (Socket socket = new Socket()) {
            clientSocketBuilder.proceedToConfigure(socket)
                    .proceedToConnect()
                    .setToBind(true)
                    .getConnectedClientSocket();
        }
    }

    @Test(expected = IOException.class)
    public void testGetConnectedClientSocketForIOException03() throws IOException {
        SocksServerUri socksServerUri = Scheme.SOCKS5.newSocksServerUri(
                "localhost", 1);
        Properties properties = Properties.of(
                GeneralPropertySpecConstants.CLIENT_BIND_HOST.newPropertyWithParsedValue(
                        InetAddress.getLoopbackAddress().getHostAddress()));
        ClientSocketBuilder clientSocketBuilder =
                socksServerUri.newSocksClient(properties)
                        .newClientSocketBuilder();
        try (Socket socket = new Socket()) {
            clientSocketBuilder.proceedToConfigure(socket)
                    .proceedToConnect()
                    .setConnectTimeout(1)
                    .setToBind(true)
                    .getConnectedClientSocket();
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetConnectTimeoutIntForIllegalArgumentException01() throws IOException {
        SocksServerUri socksServerUri = Scheme.SOCKS5.newSocksServerUri(
                "localhost", serverPort);
        Properties properties = Properties.of();
        ClientSocketBuilder clientSocketBuilder =
                socksServerUri.newSocksClient(properties)
                        .newClientSocketBuilder();
        try (Socket socket = new Socket()) {
            clientSocketBuilder.proceedToConfigure(socket)
                    .proceedToConnect()
                    .setConnectTimeout(-1);
        }
    }

}