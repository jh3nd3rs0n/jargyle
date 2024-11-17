package com.github.jh3nd3rs0n.jargyle.client;

import org.junit.Assert;
import org.junit.Test;

public class SocksNetObjectFactoryTest {

    @Test
    public void testNewInstance01() {
        System.setProperty("socksClient.socksServerUri", "socks5://127.0.0.1");
        try {
            Assert.assertNotNull(SocksNetObjectFactory.newInstance());
        } finally {
            System.clearProperty("socksClient.socksServerUri");
        }
    }

    @Test
    public void testNewInstance02() {
        System.setProperty("socksClient.socksServerUri", "socks5://127.0.0.1");
        System.setProperty("socksClient.clientBindHost", "127.0.0.1");
        System.setProperty("socksClient.clientBindPortRanges", "0-65535");
        System.setProperty("socksClient.clientConnectTimeout", "60000");
        try {
            SocksNetObjectFactory socksNetObjectFactory =
                    SocksNetObjectFactory.newInstance();
            Assert.assertNotNull(socksNetObjectFactory);
            SocksClient socksClient = socksNetObjectFactory.getSocksClient();
            Properties properties = socksClient.getProperties();
            Assert.assertEquals(
                    "127.0.0.1",
                    properties.getValue(GeneralPropertySpecConstants.CLIENT_BIND_HOST).toString());
            Assert.assertEquals(
                    "0-65535",
                    properties.getValue(GeneralPropertySpecConstants.CLIENT_BIND_PORT_RANGES).toString());
            Assert.assertEquals(
                    "60000",
                    properties.getValue(GeneralPropertySpecConstants.CLIENT_CONNECT_TIMEOUT).toString());
        } finally {
            System.clearProperty("socksClient.socksServerUri");
            System.clearProperty("socksClient.clientBindHost");
            System.clearProperty("socksClient.clientBindPortRanges");
            System.clearProperty("socksClient.clientConnectTimeout");
        }
    }

}