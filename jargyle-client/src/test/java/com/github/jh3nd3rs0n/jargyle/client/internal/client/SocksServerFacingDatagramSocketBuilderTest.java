package com.github.jh3nd3rs0n.jargyle.client.internal.client;

import com.github.jh3nd3rs0n.jargyle.client.*;
import com.github.jh3nd3rs0n.jargyle.internal.net.ssl.DtlsDatagramSocket;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketAddress;

public class SocksServerFacingDatagramSocketBuilderTest {

    @Test
    public void getConnectedSocksServerFacingDatagramSocketDatagramSocketInetAddressInt01() throws IOException {
        SocksServerUri socksServerUri = SocksServerUriScheme.SOCKS5.newSocksServerUri(
                "localhost");
        Properties properties = Properties.of();
        SocksClient socksClient = socksServerUri.newSocksClient(properties);
        SocksClientAgent socksClientAgent = new SocksClientAgent(socksClient);
        SocksServerFacingDatagramSocketBuilder socksServerFacingDatagramSocketBuilder =
                socksClientAgent.newSocksServerFacingDatagramSocketBuilder();
        InetAddress inetAddress = InetAddress.getLoopbackAddress();
        int port = 2080;
        SocketAddress socketAddress = new InetSocketAddress(inetAddress, port);
        try (DatagramSocket datagramSocket1 = new DatagramSocket(null);
             DatagramSocket datagramSocket2 =
                     socksServerFacingDatagramSocketBuilder.getConnectedSocksServerFacingDatagramSocket(
                             datagramSocket1,
                             inetAddress,
                             port)) {
            Assert.assertEquals(
                    socketAddress,
                    datagramSocket2.getRemoteSocketAddress());
            Assert.assertEquals(
                    datagramSocket1,
                    datagramSocket2);
        }
    }

    @Test
    public void getConnectedSocksServerFacingDatagramSocketDatagramSocketInetAddressInt02() throws IOException {
        SocksServerUri socksServerUri = SocksServerUriScheme.SOCKS5.newSocksServerUri(
                "localhost");
        Properties properties = Properties.of(
                DtlsPropertySpecConstants.DTLS_ENABLED.newProperty(true));
        SocksClient socksClient = socksServerUri.newSocksClient(properties);
        SocksClientAgent socksClientAgent = new SocksClientAgent(socksClient);
        SocksServerFacingDatagramSocketBuilder socksServerFacingDatagramSocketBuilder =
                socksClientAgent.newSocksServerFacingDatagramSocketBuilder();
        InetAddress inetAddress = InetAddress.getLoopbackAddress();
        int port = 3080;
        SocketAddress socketAddress = new InetSocketAddress(inetAddress, port);
        try (DatagramSocket datagramSocket1 = new DatagramSocket(null);
             DatagramSocket datagramSocket2 =
                     socksServerFacingDatagramSocketBuilder.getConnectedSocksServerFacingDatagramSocket(
                             datagramSocket1,
                             inetAddress,
                             port)) {
            Assert.assertEquals(
                    socketAddress,
                    datagramSocket2.getRemoteSocketAddress());
            Assert.assertEquals(
                    DtlsDatagramSocket.class,
                    datagramSocket2.getClass());
        }
    }

}