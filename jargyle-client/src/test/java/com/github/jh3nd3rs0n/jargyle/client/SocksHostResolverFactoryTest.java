package com.github.jh3nd3rs0n.jargyle.client;

import org.junit.Assert;
import org.junit.Test;

public class SocksHostResolverFactoryTest {

    @Test
    public void testNewInstance01() {
        System.setProperty("socksClient.socksServerUri", "socks5://127.0.0.1");
        try {
            Assert.assertNotNull(SocksHostResolverFactory.newInstance());
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
            SocksHostResolverFactory socksHostResolverFactory =
                    SocksHostResolverFactory.newInstance();
            Assert.assertNotNull(socksHostResolverFactory);
            SocksClient socksClient = socksHostResolverFactory.getSocksClient();
            Properties properties = socksClient.getProperties();
            Assert.assertEquals(
                    "127.0.0.1",
                    properties.getValue(GeneralPropertySpecConstants.SOCKS_SERVER_SOCKET_BIND_HOST).toString());
            Assert.assertEquals(
                    "0-65535",
                    properties.getValue(GeneralPropertySpecConstants.SOCKS_SERVER_SOCKET_BIND_PORT_RANGES).toString());
            Assert.assertEquals(
                    "60000",
                    properties.getValue(GeneralPropertySpecConstants.SOCKS_SERVER_SOCKET_CONNECT_TIMEOUT).toString());
        } finally {
            System.clearProperty("socksClient.socksServerUri");
            System.clearProperty("socksClient.socksServerSocket.bindHost");
            System.clearProperty("socksClient.socksServerSocket.bindPortRanges");
            System.clearProperty("socksClient.socksServerSocket.connectTimeout");
        }
    }

}