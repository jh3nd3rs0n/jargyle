package com.github.jh3nd3rs0n.jargyle.client.internal.propertyspec.impl;

import com.github.jh3nd3rs0n.jargyle.common.net.Port;
import com.github.jh3nd3rs0n.jargyle.common.net.PortRange;
import com.github.jh3nd3rs0n.jargyle.common.net.PortRanges;
import org.junit.Assert;
import org.junit.Test;

public class PortRangesPropertySpecTest {

    @Test
    public void testParseString01() {
        Assert.assertEquals(
                PortRanges.of(),
                new PortRangesPropertySpec("portRangesProperty", null)
                        .parse(""));
    }

    @Test
    public void testParseString02() {
        Assert.assertEquals(
                PortRanges.of(PortRange.of(Port.valueOf(0))),
                new PortRangesPropertySpec("portRangesProperty", null)
                        .parse("0"));
    }

    @Test
    public void testParseString03() {
        Assert.assertEquals(
                PortRanges.of(PortRange.of(Port.valueOf(0), Port.valueOf(65535))),
                new PortRangesPropertySpec("portRangesProperty", null)
                        .parse("0-65535"));
    }

    @Test
    public void testParseString04() {
        Assert.assertEquals(
                PortRanges.of(
                        PortRange.of(Port.valueOf(0), Port.valueOf(65535)),
                        PortRange.of(Port.valueOf(1080))),
                new PortRangesPropertySpec("portRangesProperty", null)
                        .parse("0-65535,1080"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseStringForIllegalArgumentException01() {
        new PortRangesPropertySpec("portRangesProperty", null)
                .parse(" ");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseStringForIllegalArgumentException02() {
        new PortRangesPropertySpec("portRangesProperty", null)
                .parse("-1");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseStringForIllegalArgumentException03() {
        new PortRangesPropertySpec("portRangesProperty", null)
                .parse("a");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseStringForIllegalArgumentException04() {
        new PortRangesPropertySpec("portRangesProperty", null)
                .parse("0--1");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseStringForIllegalArgumentException05() {
        new PortRangesPropertySpec("portRangesProperty", null)
                .parse("0-a");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseStringForIllegalArgumentException06() {
        new PortRangesPropertySpec("portRangesProperty", null)
                .parse("0-65535,-1");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseStringForIllegalArgumentException07() {
        new PortRangesPropertySpec("portRangesProperty", null)
                .parse("0-65535,a");
    }

}