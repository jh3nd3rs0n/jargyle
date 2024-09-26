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
        System.setProperty("socksServerUri.scheme", "socks5");
        System.setProperty("socksServerUri.host", "127.0.0.1");
        try {
            Assert.assertTrue(
                    NetObjectFactory.getInstance() instanceof SocksNetObjectFactory);
        } finally {
            System.clearProperty("socksServerUri.scheme");
            System.clearProperty("socksServerUri.host");
        }
    }

    @Test
    public void testGetInstance02() {
        System.setProperty("socksServerUri.scheme", "socks5");
        try {
            Assert.assertTrue(
                    NetObjectFactory.getInstance() instanceof DefaultNetObjectFactory);
        } finally {
            System.clearProperty("socksServerUri.scheme");
        }
    }

    @Test
    public void testGetInstance03() {
        System.setProperty("socksServerUri.host", "127.0.0.1");
        try {
            Assert.assertTrue(
                    NetObjectFactory.getInstance() instanceof DefaultNetObjectFactory);
        } finally {
            System.clearProperty("socksServerUri.host");
        }
    }

    @Test
    public void testGetInstance04() {
        Assert.assertTrue(
                NetObjectFactory.getInstance() instanceof DefaultNetObjectFactory);
    }

}