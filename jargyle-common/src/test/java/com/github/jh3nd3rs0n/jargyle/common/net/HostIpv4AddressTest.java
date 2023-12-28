package com.github.jh3nd3rs0n.jargyle.common.net;

import org.junit.Assert;
import org.junit.Test;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class HostIpv4AddressTest {

    @Test
    public void testEqualsObject01() {
        HostIpv4Address hostIpv4Address = HostIpv4Address.newHostIpv4Address(
                "127.0.0.1");
        Assert.assertEquals(hostIpv4Address, hostIpv4Address);
    }

    @Test
    public void testEqualsObject02() {
        HostIpv4Address hostIpv4Address = HostIpv4Address.newHostIpv4Address(
                "127.0.0.1");
        Assert.assertNotEquals(hostIpv4Address, null);
    }

    @Test
    public void testEqualsObject03() {
        HostAddress hostIpv4Address = HostIpv4Address.newHostIpv4Address(
                "127.0.0.1");
        HostAddress hostIpv6Address = HostIpv6Address.newHostIpv6Address(
                "::1");
        Assert.assertNotEquals(hostIpv4Address, hostIpv6Address);
    }

    @Test
    public void testEqualsObject04() {
        HostAddress hostIpv4Address1 = HostIpv4Address.newHostIpv4Address(
                "127.0.0.1");
        HostAddress hostIpv4Address2 = HostIpv4Address.newHostIpv4Address(
                "0.0.0.0");
        Assert.assertNotEquals(hostIpv4Address1, hostIpv4Address2);
    }

    @Test
    public void testEqualsObject05() {
        HostAddress hostIpv4Address1 = HostIpv4Address.newHostIpv4Address(
                "127.0.0.1");
        HostAddress hostIpv4Address2 = HostIpv4Address.newHostIpv4Address(
                "127.0.0.1");
        Assert.assertEquals(hostIpv4Address1, hostIpv4Address2);
    }

    @Test
    public void toInetAddress() throws UnknownHostException {
        InetAddress inetAddress1 = HostIpv4Address.newHostIpv4Address(
                "127.0.0.1").toInetAddress();
        InetAddress inetAddress2 = InetAddress.getByName("127.0.0.1");
        Assert.assertEquals(inetAddress1, inetAddress2);
    }

    @Test
    public void getAllZerosInstance() {
        HostIpv4Address hostIpv4Address = HostIpv4Address.newHostIpv4Address(
                "0.0.0.0");
        Assert.assertEquals(
                hostIpv4Address, HostIpv4Address.getAllZerosInstance());
    }

    @Test
    public void getAllZerosInetAddress() throws UnknownHostException {
        InetAddress inetAddress = InetAddress.getByName("0.0.0.0");
        Assert.assertEquals(
                inetAddress, HostIpv4Address.getAllZerosInetAddress());
    }

    @Test
    public void isAllZerosIpv4AddressString01() {
        Assert.assertTrue(HostIpv4Address.isAllZerosIpv4Address("0"));
    }

    @Test
    public void isAllZerosIpv4AddressString02() {
        Assert.assertTrue(HostIpv4Address.isAllZerosIpv4Address("0.0"));
    }

    @Test
    public void isAllZerosIpv4AddressString03() {
        Assert.assertTrue(HostIpv4Address.isAllZerosIpv4Address("0.0.0"));
    }

    @Test
    public void isAllZerosIpv4AddressString04() {
        Assert.assertTrue(HostIpv4Address.isAllZerosIpv4Address("0.0.0.0"));
    }

    @Test
    public void isAllZerosIpv4AddressString05() {
        Assert.assertTrue(HostIpv4Address.isAllZerosIpv4Address(
                "00.00.00.00"));
    }

    @Test
    public void isAllZerosIpv4AddressString06() {
        Assert.assertTrue(HostIpv4Address.isAllZerosIpv4Address(
                "000.000.000.000"));
    }

    @Test
    public void isAllZerosIpv4AddressString07() {
        Assert.assertTrue(HostIpv4Address.isAllZerosIpv4Address(
                "0000"));
    }

    @Test
    public void newHostIpv4AddressString01() {
        Assert.assertNotNull(HostIpv4Address.newHostIpv4Address("127.0.0.1"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void newHostIpv4AddressStringForIllegalArgumentException01() {
        HostIpv4Address.newHostIpv4Address("localhost");
    }

    @Test(expected = IllegalArgumentException.class)
    public void newHostIpv4AddressStringForIllegalArgumentException02() {
        HostIpv4Address.newHostIpv4Address("::1");
    }

}