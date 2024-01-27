package com.github.jh3nd3rs0n.jargyle.common.net;

import org.junit.Assert;
import org.junit.Test;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class HostNameTest {

    @Test
    public void testEqualsObject01() {
        HostName hostName = HostName.newHostName("localhost");
        Assert.assertEquals(hostName, hostName);
    }

    @Test
    public void testEqualsObject02() {
        HostName hostName = HostName.newHostName("localhost");
        Assert.assertNotEquals(hostName, null);
    }

    @Test
    public void testEqualsObject03() {
        Host hostName = HostName.newHostName("localhost");
        Host hostAddress = HostAddress.newHostAddress("127.0.0.1");
        Assert.assertNotEquals(hostName, hostAddress);
    }

    @Test
    public void testEqualsObject04() {
        HostName hostName1 = HostName.newHostName("localhost");
        HostName hostName2 = HostName.newHostName("google.com");
        Assert.assertNotEquals(hostName1, hostName2);
    }

    @Test
    public void testEqualsObject05() {
        HostName hostName1 = HostName.newHostName("localhost");
        HostName hostName2 = HostName.newHostName("localhost");
        Assert.assertEquals(hostName1, hostName2);
    }

    @Test
    public void testHashCode01() {
        HostName hostName1 = HostName.newHostName("localhost");
        HostName hostName2 = HostName.newHostName("localhost");
        Assert.assertEquals(hostName1.hashCode(), hostName2.hashCode());
    }

    @Test
    public void testHashCode02() {
        HostName hostName1 = HostName.newHostName("localhost");
        HostName hostName2 = HostName.newHostName("google.com");
        Assert.assertNotEquals(hostName1.hashCode(), hostName2.hashCode());
    }

    @Test
    public void testNewHostNameString01() {
        Assert.assertNotNull(HostName.newHostName("localhost"));
    }

    @Test
    public void testNewHostNameString02() {
        Assert.assertNotNull(HostName.newHostName("google.com"));
    }

    @Test
    public void testNewHostNameString03() {
        Assert.assertNotNull(HostName.newHostName("jh3nd3rs0n.github.io"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNewHostNameStringForIllegalArgumentException01() {
        HostName.newHostName("LOCALHOST");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNewHostNameStringForIllegalArgumentException02() {
        HostName.newHostName("@#$%^&");
    }

    @Test
    public void testToInetAddress() throws UnknownHostException {
        InetAddress inetAddress1 = HostName.newHostName(
                "localhost").toInetAddress();
        InetAddress inetAddress2 = InetAddress.getByName("127.0.0.1");
        Assert.assertEquals(inetAddress1, inetAddress2);
    }

}