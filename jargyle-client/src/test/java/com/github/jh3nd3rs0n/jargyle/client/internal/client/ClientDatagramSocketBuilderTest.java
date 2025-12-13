package com.github.jh3nd3rs0n.jargyle.client.internal.client;

import com.github.jh3nd3rs0n.jargyle.client.*;
import com.github.jh3nd3rs0n.jargyle.internal.net.ssl.DtlsDatagramSocket;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.net.*;

public class ClientDatagramSocketBuilderTest {

    @Test
    public void getConnectedClientDatagramSocketDatagramSocketInetAddressInt01() throws IOException {
        SocksServerUri socksServerUri = SocksServerUriScheme.SOCKS5.newSocksServerUri(
                "localhost");
        Properties properties = Properties.of();
        SocksClient socksClient = socksServerUri.newSocksClient(properties);
        SocksClientAgent socksClientAgent = new SocksClientAgent(socksClient);
        ClientDatagramSocketBuilder clientDatagramSocketBuilder =
                socksClientAgent.newClientDatagramSocketBuilder();
        InetAddress inetAddress = InetAddress.getLoopbackAddress();
        int port = 2080;
        SocketAddress socketAddress = new InetSocketAddress(inetAddress, port);
        try (DatagramSocket datagramSocket1 = new DatagramSocket(null);
             DatagramSocket datagramSocket2 =
                     clientDatagramSocketBuilder.getConnectedClientDatagramSocket(
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
    public void getConnectedClientDatagramSocketDatagramSocketInetAddressInt02() throws IOException {
        SocksServerUri socksServerUri = SocksServerUriScheme.SOCKS5.newSocksServerUri(
                "localhost");
        Properties properties = Properties.of(
                DtlsPropertySpecConstants.DTLS_ENABLED.newProperty(true));
        SocksClient socksClient = socksServerUri.newSocksClient(properties);
        SocksClientAgent socksClientAgent = new SocksClientAgent(socksClient);
        ClientDatagramSocketBuilder clientDatagramSocketBuilder =
                socksClientAgent.newClientDatagramSocketBuilder();
        InetAddress inetAddress = InetAddress.getLoopbackAddress();
        int port = 3080;
        SocketAddress socketAddress = new InetSocketAddress(inetAddress, port);
        try (DatagramSocket datagramSocket1 = new DatagramSocket(null);
             DatagramSocket datagramSocket2 =
                     clientDatagramSocketBuilder.getConnectedClientDatagramSocket(
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