package com.github.jh3nd3rs0n.jargyle.client;

import org.junit.Assert;
import org.junit.Test;

public class NetObjectFactoryTest {

    @Test
    public void testGetDefault() {
        Assert.assertTrue(
                NetObjectFactory.getDefault() instanceof DefaultNetObjectFactory);
    }

    @Test
    public void testGetInstance01() {
        System.setProperty("socksClient.socksServerUri", "socks5://127.0.0.1");
        try {
            Assert.assertTrue(
                    NetObjectFactory.getInstance() instanceof SocksNetObjectFactory);
        } finally {
            System.clearProperty("socksClient.socksServerUri");
        }
    }

    @Test
    public void testGetInstance02() {
        Assert.assertTrue(
                NetObjectFactory.getInstance() instanceof DefaultNetObjectFactory);
    }

}