package com.github.jh3nd3rs0n.jargyle.client;

import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.net.InetAddress;

public class HostResolverTest {

    @Test
    public void testResolveString01() throws IOException {
        HostResolver hostResolver = new HostResolver();
        InetAddress inetAddress = hostResolver.resolve(null);
        Assert.assertTrue(inetAddress.isLoopbackAddress());
    }

    @Test
    public void testResolveString02() throws IOException {
        HostResolver hostResolver = new HostResolver();
        InetAddress inetAddress = hostResolver.resolve("");
        Assert.assertTrue(inetAddress.isLoopbackAddress());
    }

    @Test
    public void testResolveString03() throws IOException {
        HostResolver hostResolver = new HostResolver();
        InetAddress inetAddress = hostResolver.resolve("localhost");
        Assert.assertTrue(inetAddress.isLoopbackAddress());
    }

    @Test
    public void testResolveString04() throws IOException {
        HostResolver hostResolver = new HostResolver();
        InetAddress expectedInetAddress = InetAddress.getByName("127.0.0.1");
        InetAddress actualInetAddress = hostResolver.resolve("127.0.0.1");
        Assert.assertEquals(expectedInetAddress, actualInetAddress);
    }

    @Test
    public void testResolveString05() throws IOException {
        HostResolver hostResolver = new HostResolver();
        InetAddress expectedInetAddress = InetAddress.getByName("::1");
        InetAddress actualInetAddress = hostResolver.resolve("::1");
        Assert.assertEquals(expectedInetAddress, actualInetAddress);
    }

    @Test
    public void testResolveString06() throws IOException {
        HostResolver hostResolver = new HostResolver();
        InetAddress expectedInetAddress = InetAddress.getByName("0.0.0.0");
        InetAddress actualInetAddress = hostResolver.resolve("0.0.0.0");
        Assert.assertEquals(expectedInetAddress, actualInetAddress);
    }

    @Test
    public void testResolveString07() throws IOException {
        HostResolver hostResolver = new HostResolver();
        InetAddress expectedInetAddress = InetAddress.getByName(
                "255.255.255.255");
        InetAddress actualInetAddress = hostResolver.resolve(
                "255.255.255.255");
        Assert.assertEquals(expectedInetAddress, actualInetAddress);
    }

    @Test
    public void testResolveString08() throws IOException {
        HostResolver hostResolver = new HostResolver();
        InetAddress expectedInetAddress = InetAddress.getByName("::");
        InetAddress actualInetAddress = hostResolver.resolve("::");
        Assert.assertEquals(expectedInetAddress, actualInetAddress);
    }

    @Test
    public void testResolveString09() throws IOException {
        HostResolver hostResolver = new HostResolver();
        InetAddress expectedInetAddress = InetAddress.getByName(
                "ffff:ffff:ffff:ffff:ffff:ffff:ffff:ffff");
        InetAddress actualInetAddress = hostResolver.resolve(
                "ffff:ffff:ffff:ffff:ffff:ffff:ffff:ffff");
        Assert.assertEquals(expectedInetAddress, actualInetAddress);
    }

}