package com.github.jh3nd3rs0n.jargyle.common.net;

import org.junit.Assert;
import org.junit.Test;

public class HostAddressTypeTest {

    @Test
    public void testDescribes01() {
        Assert.assertTrue(HostAddressType.IPV4.describes(
                HostIpv4Address.newHostIpv4Address("127.0.0.1")));
    }

    @Test
    public void testDescribes02() {
        Assert.assertFalse(HostAddressType.IPV4.describes(
                HostIpv6Address.newHostIpv6Address("::1")));
    }

    @Test
    public void testDescribes03() {
        Assert.assertTrue(HostAddressType.IPV6.describes(
                HostIpv6Address.newHostIpv6Address("::1")));
    }

    @Test
    public void testDescribes04() {
        Assert.assertFalse(HostAddressType.IPV6.describes(
                HostIpv4Address.newHostIpv4Address("127.0.0.1")));
    }

    @Test
    public void testToString01() {
        Assert.assertEquals("IPv4", HostAddressType.IPV4.toString());
    }

    @Test
    public void testToString02() {
        Assert.assertEquals("IPv6", HostAddressType.IPV6.toString());
    }

    @Test
    public void testValueOfString01() {
        Assert.assertEquals(
                HostAddressType.IPV4, HostAddressType.valueOfString("IPv4"));
    }

    @Test
    public void testValueOfString02() {
        Assert.assertEquals(
                HostAddressType.IPV6, HostAddressType.valueOfString("IPv6"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testValueOfStringForIllegalArgumentException() {
        HostAddressType.valueOfString("IPv7");
    }

}