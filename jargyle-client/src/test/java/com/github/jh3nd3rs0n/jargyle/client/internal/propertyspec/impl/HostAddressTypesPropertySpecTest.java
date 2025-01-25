package com.github.jh3nd3rs0n.jargyle.client.internal.propertyspec.impl;

import com.github.jh3nd3rs0n.jargyle.common.net.HostAddressType;
import com.github.jh3nd3rs0n.jargyle.common.net.HostAddressTypes;
import org.junit.Assert;
import org.junit.Test;

public class HostAddressTypesPropertySpecTest {

    @Test
    public void testParseString01() {
        Assert.assertEquals(
                HostAddressTypes.of(),
                new HostAddressTypesPropertySpec(
                        "hostAddressTypesProperty", null)
                        .parse(""));
    }

    @Test
    public void testParseString02() {
        Assert.assertEquals(
                HostAddressTypes.of(HostAddressType.IPV4),
                new HostAddressTypesPropertySpec(
                        "hostAddressTypesProperty", null)
                        .parse("IPv4"));
    }

    @Test
    public void testParseString03() {
        Assert.assertEquals(
                HostAddressTypes.of(
                        HostAddressType.IPV4, HostAddressType.IPV6),
                new HostAddressTypesPropertySpec(
                        "hostAddressTypesProperty", null)
                        .parse("IPv4,IPv6"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseStringForIllegalArgumentException01() {
        new HostAddressTypesPropertySpec(
                "hostAddressTypesProperty", null)
                .parse("IPv1");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseStringForIllegalArgumentException02() {
        new HostAddressTypesPropertySpec(
                "hostAddressTypesProperty", null)
                .parse(",");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseStringForIllegalArgumentException03() {
        new HostAddressTypesPropertySpec(
                "hostAddressTypesProperty", null)
                .parse("IPv6,IPv1");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseStringForIllegalArgumentException04() {
        new HostAddressTypesPropertySpec(
                "hostAddressTypesProperty", null)
                .parse("IPv6,");
    }

}