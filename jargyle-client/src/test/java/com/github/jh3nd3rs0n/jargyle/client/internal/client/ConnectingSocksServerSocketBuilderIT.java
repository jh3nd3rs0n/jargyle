package com.github.jh3nd3rs0n.jargyle.client.internal.client;

import com.github.jh3nd3rs0n.jargyle.client.*;
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
import java.util.Collections;

public class ConnectingSocksServerSocketBuilderIT {

    private static Server server;
    private static int serverPort;

    @BeforeClass
    public static void setUpBeforeClass() throws IOException {
        server = new Server(
                new Server.DefaultServerSocketFactory(),
                0,
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
    public void testGetConnectedSocksServerSocket01() throws IOException {
        SocksServerUri socksServerUri = SocksServerUriScheme.SOCKS5.newSocksServerUri(
                "localhost", serverPort);
        Properties properties = Properties.of();
        SocksClient socksClient = socksServerUri.newSocksClient(properties);
        SocksClientAgent socksClientAgent = new SocksClientAgent(socksClient);
        SocksServerSocketBuilder socksServerSocketBuilder =
                socksClientAgent.newSocksServerSocketBuilder();
        try (Socket socket = socksServerSocketBuilder.proceedToConfigure(
                        new Socket())
                .proceedToConnect()
                .getConnectedSocksServerSocket()) {
            Assert.assertEquals(
                    new InetSocketAddress(
                            socksServerUri.getHost().toInetAddress(),
                            socksServerUri.getPort().intValue()),
                    socket.getRemoteSocketAddress());
            Assert.assertEquals(Socket.class, socket.getClass());
        }
    }

    @Test
    public void testGetConnectedSocksServerSocket02() throws IOException {
        SocksServerUri socksServerUri = SocksServerUriScheme.SOCKS5.newSocksServerUri(
                "localhost", serverPort);
        Properties properties = Properties.of(
                GeneralPropertySpecConstants.SOCKS_SERVER_SOCKET_BIND_HOST.newPropertyWithParsedValue(
                        InetAddress.getLoopbackAddress().getHostAddress()));
        SocksClient socksClient = socksServerUri.newSocksClient(properties);
        SocksClientAgent socksClientAgent = new SocksClientAgent(socksClient);
        SocksServerSocketBuilder socksServerSocketBuilder =
                socksClientAgent.newSocksServerSocketBuilder();
        try (Socket socket = socksServerSocketBuilder.proceedToConfigure(
                        new Socket())
                .proceedToConnect()
                .setToBind(true)
                .getConnectedSocksServerSocket()) {
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
    public void testGetConnectedSocksServerSocket03() throws IOException {
        NetworkInterface networkInterface = NetworkInterface.getByInetAddress(
                InetAddress.getLoopbackAddress());
        NetInterface netInterface = NetInterface.newInstance(networkInterface);
        SocksServerUri socksServerUri = SocksServerUriScheme.SOCKS5.newSocksServerUri(
                "localhost", serverPort);
        Properties properties = Properties.of(
                GeneralPropertySpecConstants.SOCKS_SERVER_SOCKET_NET_INTERFACE.newProperty(netInterface),
                GeneralPropertySpecConstants.SOCKS_SERVER_SOCKET_BIND_HOST_ADDRESS_TYPES.newProperty(
                        HostAddressTypes.of(HostAddressType.HOST_IPV4_ADDRESS)));
        SocksClient socksClient = socksServerUri.newSocksClient(properties);
        SocksClientAgent socksClientAgent = new SocksClientAgent(socksClient);
        SocksServerSocketBuilder socksServerSocketBuilder =
                socksClientAgent.newSocksServerSocketBuilder();
        try (Socket socket = socksServerSocketBuilder.proceedToConfigure(
                        new Socket())
                .proceedToConnect()
                .setToBind(true)
                .getConnectedSocksServerSocket()) {
            Assert.assertTrue(
                    Collections.list(networkInterface.getInetAddresses()).contains(
                            socket.getInetAddress()));
            Assert.assertEquals(
                    new InetSocketAddress(
                            socksServerUri.getHost().toInetAddress(),
                            socksServerUri.getPort().intValue()),
                    socket.getRemoteSocketAddress());
            Assert.assertEquals(Socket.class, socket.getClass());
        }
    }

    @Test
    public void testGetConnectedSocksServerSocket04() throws IOException {
        SocksServerUri socksServerUri = SocksServerUriScheme.SOCKS5.newSocksServerUri(
                "localhost", serverPort);
        Properties properties = Properties.of(
                SslPropertySpecConstants.SSL_ENABLED.newProperty(true));
        SocksClient socksClient = socksServerUri.newSocksClient(properties);
        SocksClientAgent socksClientAgent = new SocksClientAgent(socksClient);
        SocksServerSocketBuilder socksServerSocketBuilder =
                socksClientAgent.newSocksServerSocketBuilder();
        try (Socket socket = socksServerSocketBuilder.proceedToConfigure(
                        new Socket())
                .proceedToConnect()
                .getConnectedSocksServerSocket()) {
            Assert.assertEquals(
                    new InetSocketAddress(
                            socksServerUri.getHost().toInetAddress(),
                            socksServerUri.getPort().intValue()),
                    socket.getRemoteSocketAddress());
            Assert.assertTrue(socket instanceof SSLSocket);
        }
    }

    @Test(expected = IOException.class)
    public void testGetConnectedSocksServerSocketForIOException01() throws IOException {
        SocksServerUri socksServerUri = SocksServerUriScheme.SOCKS5.newSocksServerUri(
                "localhost", 1);
        Properties properties = Properties.of();
        SocksClient socksClient = socksServerUri.newSocksClient(properties);
        SocksClientAgent socksClientAgent = new SocksClientAgent(socksClient);
        SocksServerSocketBuilder socksServerSocketBuilder =
                socksClientAgent.newSocksServerSocketBuilder();
        try (Socket socket = new Socket()) {
            socksServerSocketBuilder.proceedToConfigure(socket)
                    .proceedToConnect()
                    .setConnectTimeout(1)
                    .getConnectedSocksServerSocket();
        }
    }

    @Test(expected = IOException.class)
    public void testGetConnectedSocksServerSocketForIOException02() throws IOException {
        SocksServerUri socksServerUri = SocksServerUriScheme.SOCKS5.newSocksServerUri(
                "localhost", serverPort);
        Properties properties = Properties.of(
                GeneralPropertySpecConstants.SOCKS_SERVER_SOCKET_BIND_HOST.newPropertyWithParsedValue(
                        InetAddress.getLoopbackAddress().getHostAddress()),
                GeneralPropertySpecConstants.SOCKS_SERVER_SOCKET_BIND_PORT_RANGES.newProperty(
                        PortRanges.of(PortRange.of(Port.valueOf(serverPort)))));
        SocksClient socksClient = socksServerUri.newSocksClient(properties);
        SocksClientAgent socksClientAgent = new SocksClientAgent(socksClient);
        SocksServerSocketBuilder socksServerSocketBuilder =
                socksClientAgent.newSocksServerSocketBuilder();
        try (Socket socket = new Socket()) {
            socksServerSocketBuilder.proceedToConfigure(socket)
                    .proceedToConnect()
                    .setToBind(true)
                    .getConnectedSocksServerSocket();
        }
    }

    @Test(expected = IOException.class)
    public void testGetConnectedSocksServerSocketForIOException03() throws IOException {
        SocksServerUri socksServerUri = SocksServerUriScheme.SOCKS5.newSocksServerUri(
                "localhost", 1);
        Properties properties = Properties.of(
                GeneralPropertySpecConstants.SOCKS_SERVER_SOCKET_BIND_HOST.newPropertyWithParsedValue(
                        InetAddress.getLoopbackAddress().getHostAddress()));
        SocksClient socksClient = socksServerUri.newSocksClient(properties);
        SocksClientAgent socksClientAgent = new SocksClientAgent(socksClient);
        SocksServerSocketBuilder socksServerSocketBuilder =
                socksClientAgent.newSocksServerSocketBuilder();
        try (Socket socket = new Socket()) {
            socksServerSocketBuilder.proceedToConfigure(socket)
                    .proceedToConnect()
                    .setConnectTimeout(1)
                    .setToBind(true)
                    .getConnectedSocksServerSocket();
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetConnectTimeoutIntForIllegalArgumentException01() throws IOException {
        SocksServerUri socksServerUri = SocksServerUriScheme.SOCKS5.newSocksServerUri(
                "localhost", serverPort);
        Properties properties = Properties.of();
        SocksClient socksClient = socksServerUri.newSocksClient(properties);
        SocksClientAgent socksClientAgent = new SocksClientAgent(socksClient);
        SocksServerSocketBuilder socksServerSocketBuilder =
                socksClientAgent.newSocksServerSocketBuilder();
        try (Socket socket = new Socket()) {
            socksServerSocketBuilder.proceedToConfigure(socket)
                    .proceedToConnect()
                    .setConnectTimeout(-1);
        }
    }

}