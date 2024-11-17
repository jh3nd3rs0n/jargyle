package com.github.jh3nd3rs0n.jargyle.client;

import com.github.jh3nd3rs0n.jargyle.common.net.Host;
import com.github.jh3nd3rs0n.jargyle.common.net.Port;
import com.github.jh3nd3rs0n.jargyle.common.net.PortRange;
import com.github.jh3nd3rs0n.jargyle.common.net.PortRanges;
import com.github.jh3nd3rs0n.jargyle.common.number.PositiveInteger;
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
        System.setProperty("socksClient.clientBindHost", "127.0.0.1");
        System.setProperty("socksClient.clientBindPortRanges", "0-65535");
        System.setProperty("socksClient.clientConnectTimeout", "60000");
        try {
            SocksClient socksClient = SocksClient.newInstance();
            Assert.assertNotNull(socksClient);
            Properties properties = socksClient.getProperties();
            Assert.assertEquals(
                    Host.newInstance("127.0.0.1"),
                    properties.getValue(GeneralPropertySpecConstants.CLIENT_BIND_HOST));
            Assert.assertEquals(
                    PortRanges.of(PortRange.of(Port.valueOf(0), Port.valueOf(65535))),
                    properties.getValue(GeneralPropertySpecConstants.CLIENT_BIND_PORT_RANGES));
            Assert.assertEquals(
                    PositiveInteger.valueOf(60000),
                    properties.getValue(GeneralPropertySpecConstants.CLIENT_CONNECT_TIMEOUT));
        } finally {
            System.clearProperty("socksClient.socksServerUri");
            System.clearProperty("socksClient.clientBindHost");
            System.clearProperty("socksClient.clientBindPortRanges");
            System.clearProperty("socksClient.clientConnectTimeout");
        }
    }

}