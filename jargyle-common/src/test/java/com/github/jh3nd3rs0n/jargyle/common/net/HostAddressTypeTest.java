package com.github.jh3nd3rs0n.jargyle.common.net;

import org.junit.Assert;
import org.junit.Test;

public class HostAddressTypeTest {

    @Test
    public void testDescribes01() {
        Assert.assertTrue(HostAddressType.HOST_IPV4_ADDRESS.describes(
                HostIpv4Address.newHostIpv4Address("127.0.0.1")));
    }

    @Test
    public void testDescribes02() {
        Assert.assertFalse(HostAddressType.HOST_IPV4_ADDRESS.describes(
                HostIpv6Address.newHostIpv6Address("::1")));
    }

    @Test
    public void testDescribes03() {
        Assert.assertTrue(HostAddressType.HOST_IPV6_ADDRESS.describes(
                HostIpv6Address.newHostIpv6Address("::1")));
    }

    @Test
    public void testDescribes04() {
        Assert.assertFalse(HostAddressType.HOST_IPV6_ADDRESS.describes(
                HostIpv4Address.newHostIpv4Address("127.0.0.1")));
    }

    @Test
    public void testToString01() {
        Assert.assertEquals(
                "HOST_IPV4_ADDRESS",
                HostAddressType.HOST_IPV4_ADDRESS.toString());
    }

    @Test
    public void testToString02() {
        Assert.assertEquals(
                "HOST_IPV6_ADDRESS",
                HostAddressType.HOST_IPV6_ADDRESS.toString());
    }

    @Test
    public void testValueOfString01() {
        Assert.assertEquals(
                HostAddressType.HOST_IPV4_ADDRESS,
                HostAddressType.valueOfString("HOST_IPV4_ADDRESS"));
    }

    @Test
    public void testValueOfString02() {
        Assert.assertEquals(
                HostAddressType.HOST_IPV6_ADDRESS,
                HostAddressType.valueOfString("HOST_IPV6_ADDRESS"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testValueOfStringForIllegalArgumentException() {
        HostAddressType.valueOfString("HOST_IPV3_ADDRESS");
    }

}