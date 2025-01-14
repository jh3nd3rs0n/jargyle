package com.github.jh3nd3rs0n.jargyle.common.net;

import org.junit.Assert;
import org.junit.Test;

public class HostAddressTypesTest {

    @Test
    public void testGetDefault() {
        HostAddressTypes hostAddressTypes = HostAddressTypes.of(
                HostAddressType.IPV4, HostAddressType.IPV6);
        Assert.assertEquals(hostAddressTypes, HostAddressTypes.getDefault());
    }

    @Test
    public void testNewInstanceFromString01() {
        HostAddressTypes hostAddressTypes1 = HostAddressTypes.of(
                HostAddressType.IPV4);
        HostAddressTypes hostAddressTypes2 = HostAddressTypes.newInstanceFrom(
                "IPv4");
        Assert.assertEquals(hostAddressTypes1, hostAddressTypes2);
    }

    @Test
    public void testNewInstanceFromString02() {
        HostAddressTypes hostAddressTypes1 = HostAddressTypes.of(
                HostAddressType.IPV6);
        HostAddressTypes hostAddressTypes2 = HostAddressTypes.newInstanceFrom(
                "IPv6");
        Assert.assertEquals(hostAddressTypes1, hostAddressTypes2);
    }

    @Test
    public void testNewInstanceFromString03() {
        HostAddressTypes hostAddressTypes1 = HostAddressTypes.of(
                HostAddressType.IPV4, HostAddressType.IPV6);
        HostAddressTypes hostAddressTypes2 = HostAddressTypes.newInstanceFrom(
                "IPv4,IPv6");
        Assert.assertEquals(hostAddressTypes1, hostAddressTypes2);
    }

    @Test
    public void testNewInstanceFromString04() {
        HostAddressTypes hostAddressTypes1 = HostAddressTypes.of(
                HostAddressType.IPV6, HostAddressType.IPV4);
        HostAddressTypes hostAddressTypes2 = HostAddressTypes.newInstanceFrom(
                "IPv6,IPv4");
        Assert.assertEquals(hostAddressTypes1, hostAddressTypes2);
    }

    @Test
    public void testNewInstanceFromString05() {
        HostAddressTypes hostAddressTypes1 = HostAddressTypes.of(
                HostAddressType.IPV4,
                HostAddressType.IPV6,
                HostAddressType.IPV4);
        HostAddressTypes hostAddressTypes2 = HostAddressTypes.newInstanceFrom(
                "IPv4,IPv6,IPv4");
        Assert.assertEquals(hostAddressTypes1, hostAddressTypes2);
    }

    @Test
    public void testNewInstanceFromString06() {
        HostAddressTypes hostAddressTypes1 = HostAddressTypes.of(
                HostAddressType.IPV6,
                HostAddressType.IPV4,
                HostAddressType.IPV6);
        HostAddressTypes hostAddressTypes2 = HostAddressTypes.newInstanceFrom(
                "IPv6,IPv4,IPv6");
        Assert.assertEquals(hostAddressTypes1, hostAddressTypes2);
    }

    @Test
    public void testNewInstanceFromString07() {
        HostAddressTypes hostAddressTypes1 = HostAddressTypes.of();
        HostAddressTypes hostAddressTypes2 = HostAddressTypes.newInstanceFrom(
                "");
        Assert.assertEquals(hostAddressTypes1, hostAddressTypes2);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNewInstanceFromStringForIllegalArgumentException01() {
        HostAddressTypes.newInstanceFrom(" ");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNewInstanceFromStringForIllegalArgumentException02() {
        HostAddressTypes.newInstanceFrom(",");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNewInstanceFromStringForIllegalArgumentException03() {
        HostAddressTypes.newInstanceFrom(" , ");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNewInstanceFromStringForIllegalArgumentException04() {
        HostAddressTypes.newInstanceFrom("IPv4,IPv7");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNewInstanceFromStringForIllegalArgumentException05() {
        HostAddressTypes.newInstanceFrom("IPv4,IPv6,IPv7");
    }

    @Test
    public void testFirstDescribesHostAddress01() {
        HostAddressTypes hostAddressTypes = HostAddressTypes.of(
                HostAddressType.IPV6, HostAddressType.IPV4);
        HostAddress hostAddress = HostIpv4Address.newHostIpv4Address(
                "127.0.0.1");
        Assert.assertEquals(
                HostAddressType.IPV4,
                hostAddressTypes.firstDescribes(hostAddress));
    }

    @Test
    public void testFirstDescribesHostAddress02() {
        HostAddressTypes hostAddressTypes = HostAddressTypes.of(
                HostAddressType.IPV6, HostAddressType.IPV4);
        HostAddress hostAddress = HostIpv6Address.newHostIpv6Address(
                "::1");
        Assert.assertEquals(
                HostAddressType.IPV6,
                hostAddressTypes.firstDescribes(hostAddress));
    }

    @Test
    public void testFirstDescribesHostAddress03() {
        HostAddressTypes hostAddressTypes = HostAddressTypes.of(
                HostAddressType.IPV6);
        HostAddress hostAddress = HostIpv4Address.newHostIpv4Address(
                "127.0.0.1");
        Assert.assertNull(hostAddressTypes.firstDescribes(hostAddress));
    }

    @Test
    public void testFirstDescribesHostAddress04() {
        HostAddressTypes hostAddressTypes = HostAddressTypes.of(
                HostAddressType.IPV4);
        HostAddress hostAddress = HostIpv6Address.newHostIpv6Address(
                "::1");
        Assert.assertNull(hostAddressTypes.firstDescribes(hostAddress));
    }

    @Test
    public void testFirstDescribesHostAddress05() {
        HostAddressTypes hostAddressTypes = HostAddressTypes.of();
        HostAddress hostAddress = HostIpv4Address.newHostIpv4Address(
                "127.0.0.1");
        Assert.assertNull(hostAddressTypes.firstDescribes(hostAddress));
    }

    @Test
    public void testFirstDescribesHostAddress06() {
        HostAddressTypes hostAddressTypes = HostAddressTypes.of();
        HostAddress hostAddress = HostIpv6Address.newHostIpv6Address(
                "::1");
        Assert.assertNull(hostAddressTypes.firstDescribes(hostAddress));
    }

    @Test
    public void testEqualsObject01() {
        HostAddressTypes hostAddressTypes = HostAddressTypes.of(
                HostAddressType.IPV4, HostAddressType.IPV6);
        Assert.assertEquals(hostAddressTypes, hostAddressTypes);
    }

    @Test
    public void testEqualsObject02() {
        HostAddressTypes hostAddressTypes = HostAddressTypes.of(
                HostAddressType.IPV4, HostAddressType.IPV6);
        Assert.assertNotEquals(hostAddressTypes, null);
    }

    @Test
    public void testEqualsObject03() {
        Object obj1 = HostAddressTypes.of(
                HostAddressType.IPV4, HostAddressType.IPV6);
        Object obj2 = new Object();
        Assert.assertNotEquals(obj1, obj2);
    }

    @Test
    public void testEqualsObject04() {
        HostAddressTypes hostAddressTypes1 = HostAddressTypes.of(
                HostAddressType.IPV4, HostAddressType.IPV6);
        HostAddressTypes hostAddressTypes2 = HostAddressTypes.of(
                HostAddressType.IPV6, HostAddressType.IPV4);
        Assert.assertNotEquals(hostAddressTypes1, hostAddressTypes2);
    }

    @Test
    public void testEqualsObject05() {
        HostAddressTypes hostAddressTypes1 = HostAddressTypes.of(
                HostAddressType.IPV4, HostAddressType.IPV6);
        HostAddressTypes hostAddressTypes2 = HostAddressTypes.of(
                HostAddressType.IPV4, HostAddressType.IPV6);
        Assert.assertEquals(hostAddressTypes1, hostAddressTypes2);
    }

    @Test
    public void testHashCode01() {
        HostAddressTypes hostAddressTypes1 = HostAddressTypes.of(
                HostAddressType.IPV4, HostAddressType.IPV6);
        HostAddressTypes hostAddressTypes2 = HostAddressTypes.of(
                HostAddressType.IPV6, HostAddressType.IPV4);
        Assert.assertNotEquals(
                hostAddressTypes1.hashCode(), hostAddressTypes2.hashCode());
    }

    @Test
    public void testHashCode02() {
        HostAddressTypes hostAddressTypes1 = HostAddressTypes.of(
                HostAddressType.IPV4, HostAddressType.IPV6);
        HostAddressTypes hostAddressTypes2 = HostAddressTypes.of(
                HostAddressType.IPV4, HostAddressType.IPV6);
        Assert.assertEquals(
                hostAddressTypes1.hashCode(), hostAddressTypes2.hashCode());
    }

    @Test
    public void testToString01() {
        Assert.assertEquals(
                "IPv4", HostAddressTypes.of(HostAddressType.IPV4).toString());
    }

    @Test
    public void testToString02() {
        Assert.assertEquals(
                "IPv6", HostAddressTypes.of(HostAddressType.IPV6).toString());
    }

    @Test
    public void testToString03() {
        Assert.assertEquals(
                "IPv4,IPv6",
                HostAddressTypes.of(HostAddressType.IPV4, HostAddressType.IPV6).toString());
    }

    @Test
    public void testToString04() {
        Assert.assertEquals(
                "IPv6,IPv4",
                HostAddressTypes.of(HostAddressType.IPV6, HostAddressType.IPV4).toString());
    }

    @Test
    public void testToString05() {
        Assert.assertEquals(
                "IPv6,IPv4,IPv6",
                HostAddressTypes.of(HostAddressType.IPV6, HostAddressType.IPV4, HostAddressType.IPV6).toString());
    }

    @Test
    public void testToString06() {
        Assert.assertEquals(
                "IPv4,IPv6,IPv4",
                HostAddressTypes.of(HostAddressType.IPV4, HostAddressType.IPV6, HostAddressType.IPV4).toString());
    }

    @Test
    public void testToString07() {
        Assert.assertEquals(
                "",
                HostAddressTypes.of().toString());
    }

}