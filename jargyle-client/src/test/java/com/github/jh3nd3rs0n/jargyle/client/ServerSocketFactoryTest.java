package com.github.jh3nd3rs0n.jargyle.client;

import org.junit.Assert;
import org.junit.Test;

public class ServerSocketFactoryTest {

    @Test
    public void testGetDefault() {
        Assert.assertTrue(
                ServerSocketFactory.getDefault() instanceof DefaultServerSocketFactory);
    }

    @Test
    public void testGetInstance01() {
        System.setProperty("socksClient.socksServerUri", "socks5://127.0.0.1");
        try {
            Assert.assertTrue(
                    ServerSocketFactory.getInstance() instanceof SocksServerSocketFactory);
        } finally {
            System.clearProperty("socksClient.socksServerUri");
        }
    }

    @Test
    public void testGetInstance02() {
        Assert.assertTrue(
                ServerSocketFactory.getInstance() instanceof DefaultServerSocketFactory);
    }

}