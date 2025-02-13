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
                HostAddressTypes.of(HostAddressType.HOST_IPV4_ADDRESS),
                new HostAddressTypesPropertySpec(
                        "hostAddressTypesProperty", null)
                        .parse("HOST_IPV4_ADDRESS"));
    }

    @Test
    public void testParseString03() {
        Assert.assertEquals(
                HostAddressTypes.of(
                        HostAddressType.HOST_IPV4_ADDRESS,
                        HostAddressType.HOST_IPV6_ADDRESS),
                new HostAddressTypesPropertySpec(
                        "hostAddressTypesProperty", null)
                        .parse("HOST_IPV4_ADDRESS,HOST_IPV6_ADDRESS"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseStringForIllegalArgumentException01() {
        new HostAddressTypesPropertySpec(
                "hostAddressTypesProperty", null)
                .parse("HOST_IPV3_ADDRESS");
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
                .parse("HOST_IPV6_ADDRESS,HOST_IPV3_ADDRESS");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseStringForIllegalArgumentException04() {
        new HostAddressTypesPropertySpec(
                "hostAddressTypesProperty", null)
                .parse("HOST_IPV6_ADDRESS,");
    }

}