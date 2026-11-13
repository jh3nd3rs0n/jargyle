package com.github.jh3nd3rs0n.jargyle.client;

import com.github.jh3nd3rs0n.jargyle.common.net.Host;
import com.github.jh3nd3rs0n.jargyle.common.net.Port;
import com.github.jh3nd3rs0n.jargyle.common.net.PortRange;
import com.github.jh3nd3rs0n.jargyle.common.net.PortRanges;
import com.github.jh3nd3rs0n.jargyle.common.number.NonNegativeInteger;
import org.junit.Assert;
import org.junit.Test;

public class SocksClientTest {

    @Test
    public void testNewInstance01() {
        System.setProperty("socksClient.socksServerUri", "socks5://127.0.0.1");
        try {
            Assert.assertNotNull(SocksClient.newInstance());
        } finally {
            System.clearProperty("socksClient.socksServerUri");
        }
    }

    @Test
    public void testNewInstance02() {
        System.setProperty("socksClient.socksServerUri", "socks5://127.0.0.1");
        System.setProperty("socksClient.socksServerSocket.bindHost", "127.0.0.1");
        System.setProperty("socksClient.socksServerSocket.bindPortRanges", "0-65535");
        System.setProperty("socksClient.socksServerSocket.connectTimeout", "60000");
        try {
            SocksClient socksClient = SocksClient.newInstance();
            Assert.assertNotNull(socksClient);
            Properties properties = socksClient.getProperties();
            Assert.assertEquals(
                    Host.newInstance("127.0.0.1"),
                    properties.getValue(GeneralPropertySpecConstants.SOCKS_SERVER_SOCKET_BIND_HOST));
            Assert.assertEquals(
                    PortRanges.of(PortRange.of(Port.valueOf(0), Port.valueOf(65535))),
                    properties.getValue(GeneralPropertySpecConstants.SOCKS_SERVER_SOCKET_BIND_PORT_RANGES));
            Assert.assertEquals(
                    NonNegativeInteger.valueOf(60000),
                    properties.getValue(GeneralPropertySpecConstants.SOCKS_SERVER_SOCKET_CONNECT_TIMEOUT));
        } finally {
            System.clearProperty("socksClient.socksServerUri");
            System.clearProperty("socksClient.socksServerSocket.bindHost");
            System.clearProperty("socksClient.socksServerSocket.bindPortRanges");
            System.clearProperty("socksClient.socksServerSocket.connectTimeout");
        }
    }

}