package com.github.jh3nd3rs0n.jargyle.common.net;

import org.junit.Assert;
import org.junit.Test;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class HostIpv6AddressTest {

    @Test
    public void testEqualsString01() {
        HostIpv6Address hostIpv6Address = HostIpv6Address.newHostIpv6Address(
                "::1");
        Assert.assertEquals(hostIpv6Address, hostIpv6Address);
    }

    @Test
    public void testEqualsString02() {
        HostIpv6Address hostIpv6Address = HostIpv6Address.newHostIpv6Address(
                "::1");
        Assert.assertNotEquals(hostIpv6Address, null);
    }

    @Test
    public void testEqualsString03() {
        HostAddress hostIpv6Address = HostIpv6Address.newHostIpv6Address(
                "::1");
        HostAddress hostIpv4Address = HostIpv4Address.newHostIpv4Address(
                "127.0.0.1");
        Assert.assertNotEquals(hostIpv6Address, hostIpv4Address);
    }

    @Test
    public void testEqualsString04() {
        HostIpv6Address hostIpv6Address1 = HostIpv6Address.newHostIpv6Address(
                "::1");
        HostIpv6Address hostIpv6Address2 = HostIpv6Address.newHostIpv6Address(
                "::");
        Assert.assertNotEquals(hostIpv6Address1, hostIpv6Address2);
    }

    @Test
    public void testEqualsString05() {
        HostIpv6Address hostIpv6Address1 = HostIpv6Address.newHostIpv6Address(
                "::1");
        HostIpv6Address hostIpv6Address2 = HostIpv6Address.newHostIpv6Address(
                "::1");
        Assert.assertEquals(hostIpv6Address1, hostIpv6Address2);
    }

    @Test
    public void toInetAddress() throws UnknownHostException {
        InetAddress inetAddress1 = HostIpv6Address.newHostIpv6Address(
                "::1").toInetAddress();
        InetAddress inetAddress2 = InetAddress.getByName("0:0:0:0:0:0:0:1");
        Assert.assertEquals(inetAddress1, inetAddress2);
    }

    @Test
    public void isAllZerosIpv6AddressString01() {
        Assert.assertTrue(HostIpv6Address.isAllZerosIpv6Address("::"));
    }

    @Test
    public void isAllZerosIpv6AddressString02() {
        Assert.assertTrue(HostIpv6Address.isAllZerosIpv6Address("0::0"));
    }

    @Test
    public void isAllZerosIpv6AddressString03() {
        Assert.assertTrue(HostIpv6Address.isAllZerosIpv6Address("0000::0000"));
    }

    @Test
    public void isAllZerosIpv6AddressString04() {
        Assert.assertTrue(HostIpv6Address.isAllZerosIpv6Address(
                "0000:0000:0000::0000"));
    }

    @Test
    public void isAllZerosIpv6AddressString05() {
        Assert.assertTrue(HostIpv6Address.isAllZerosIpv6Address(
                "0000:0000:0000:0000:0000:0000:0000:0000"));
    }

    @Test
    public void isAllZerosIpv6AddressString06() {
        Assert.assertTrue(HostIpv6Address.isAllZerosIpv6Address(
                "0:0:0:0:0:0:0:0"));
    }

    @Test
    public void newHostIpv6AddressString01() {
        Assert.assertNotNull(HostIpv6Address.newHostIpv6Address("::1"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void newHostIpv6AddressStringForIllegalArgumentException01() {
        HostIpv6Address.newHostIpv6Address("localhost");
    }


    @Test(expected = IllegalArgumentException.class)
    public void newHostIpv6AddressStringForIllegalArgumentException02() {
        HostIpv6Address.newHostIpv6Address("127.0.0.1");
    }

}