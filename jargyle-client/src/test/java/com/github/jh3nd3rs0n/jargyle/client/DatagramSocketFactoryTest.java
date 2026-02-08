package com.github.jh3nd3rs0n.jargyle.client;

import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.*;

public class DatagramSocketFactoryTest {

    @Test
    public void testGetDefault() {
        Assert.assertTrue(
                DatagramSocketFactory.getDefault() instanceof DefaultDatagramSocketFactory);
    }

    @Test
    public void testGetInstance01() {
        System.setProperty("socksClient.socksServerUri", "socks5://127.0.0.1");
        try {
            Assert.assertTrue(
                    DatagramSocketFactory.getInstance() instanceof SocksDatagramSocketFactory);
        } finally {
            System.clearProperty("socksClient.socksServerUri");
        }
    }

    @Test
    public void testGetInstance02() {
        Assert.assertTrue(
                DatagramSocketFactory.getInstance() instanceof DefaultDatagramSocketFactory);
    }

}