package com.github.jh3nd3rs0n.jargyle.common.net;

import org.junit.Assert;
import org.junit.Test;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class HostIpv6AddressTest {

    @Test
    public void testEqualsObject01() {
        HostIpv6Address hostIpv6Address = HostIpv6Address.newHostIpv6AddressOf(
                "::1");
        Assert.assertEquals(hostIpv6Address, hostIpv6Address);
    }

    @Test
    public void testEqualsObject02() {
        HostIpv6Address hostIpv6Address = HostIpv6Address.newHostIpv6AddressOf(
                "::1");
        Assert.assertNotEquals(hostIpv6Address, null);
    }

    @Test
    public void testEqualsObject03() {
        HostAddress hostIpv6Address = HostIpv6Address.newHostIpv6AddressOf(
                "::1");
        HostAddress hostIpv4Address = HostIpv4Address.newHostIpv4AddressOf(
                "127.0.0.1");
        Assert.assertNotEquals(hostIpv6Address, hostIpv4Address);
    }

    @Test
    public void testEqualsObject04() {
        HostIpv6Address hostIpv6Address1 = HostIpv6Address.newHostIpv6AddressOf(
                "::1");
        HostIpv6Address hostIpv6Address2 = HostIpv6Address.newHostIpv6AddressOf(
                "::");
        Assert.assertNotEquals(hostIpv6Address1, hostIpv6Address2);
    }

    @Test
    public void testEqualsObject05() {
        HostIpv6Address hostIpv6Address1 = HostIpv6Address.newHostIpv6AddressOf(
                "::1");
        HostIpv6Address hostIpv6Address2 = HostIpv6Address.newHostIpv6AddressOf(
                "::1");
        Assert.assertEquals(hostIpv6Address1, hostIpv6Address2);
    }

    @Test
    public void testHashCode01() {
        HostIpv6Address hostIpv6Address1 = HostIpv6Address.newHostIpv6AddressOf(
                "::1");
        HostIpv6Address hostIpv6Address2 = HostIpv6Address.newHostIpv6AddressOf(
                "::1");
        Assert.assertEquals(
                hostIpv6Address1.hashCode(), hostIpv6Address2.hashCode());
    }

    @Test
    public void testHashCode02() {
        HostIpv6Address hostIpv6Address1 = HostIpv6Address.newHostIpv6AddressOf(
                "::1");
        HostIpv6Address hostIpv6Address2 = HostIpv6Address.newHostIpv6AddressOf(
                "::");
        Assert.assertNotEquals(
                hostIpv6Address1.hashCode(), hostIpv6Address2.hashCode());
    }

    @Test
    public void testIsAllZerosIpv6AddressString01() {
        Assert.assertTrue(HostIpv6Address.isAllZerosIpv6Address("::"));
    }

    @Test
    public void testIsAllZerosIpv6AddressString02() {
        Assert.assertTrue(HostIpv6Address.isAllZerosIpv6Address("0::0"));
    }

    @Test
    public void testIsAllZerosIpv6AddressString03() {
        Assert.assertTrue(HostIpv6Address.isAllZerosIpv6Address("0000::0000"));
    }

    @Test
    public void testIsAllZerosIpv6AddressString04() {
        Assert.assertTrue(HostIpv6Address.isAllZerosIpv6Address(
                "0000:0000:0000::0000"));
    }

    @Test
    public void testIsAllZerosIpv6AddressString05() {
        Assert.assertTrue(HostIpv6Address.isAllZerosIpv6Address(
                "0000:0000:0000:0000:0000:0000:0000:0000"));
    }

    @Test
    public void testIsAllZerosIpv6AddressString06() {
        Assert.assertTrue(HostIpv6Address.isAllZerosIpv6Address(
                "0:0:0:0:0:0:0:0"));
    }

    @Test
    public void testIsAllZerosIpv6AddressString07() {
        Assert.assertFalse(HostIpv6Address.isAllZerosIpv6Address(
                "0000:0000:0000:0000:0000:0000:0000::0000:0000:0000:0000:0000:0000:0000"));
    }

    @Test
    public void testIsAllZerosIpv6AddressString08() {
        Assert.assertFalse(HostIpv6Address.isAllZerosIpv6Address(
                ":::::::"));
    }

    @Test
    public void testIsAllZerosIpv6AddressString09() {
        Assert.assertFalse(HostIpv6Address.isAllZerosIpv6Address(
                "all zeros"));
    }

    @Test
    public void testNewHostIpv6AddressOfString01() {
        Assert.assertNotNull(HostIpv6Address.newHostIpv6AddressOf("::1"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNewHostIpv6AddressOfStringForIllegalArgumentException01() {
        HostIpv6Address.newHostIpv6AddressOf("localhost");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNewHostIpv6AddressOfStringForIllegalArgumentException02() {
        HostIpv6Address.newHostIpv6AddressOf("127.0.0.1");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNewHostIpv6AddressOfStringForIllegalArgumentException06() {
        HostIpv6Address.newHostIpv6AddressOf(
                "0000:0000:0000:0000:0000:0000:0000::0000:0000:0000:0000:0000:0000:0000");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNewHostIpv6AddressOfStringForIllegalArgumentException07() {
        HostIpv6Address.newHostIpv6AddressOf(":::::::");
    }

    @Test
    public void testToInetAddress() throws UnknownHostException {
        InetAddress inetAddress1 = HostIpv6Address.newHostIpv6AddressOf(
                "::1").toInetAddress();
        InetAddress inetAddress2 = InetAddress.getByName("0:0:0:0:0:0:0:1");
        Assert.assertEquals(inetAddress1, inetAddress2);
    }

}