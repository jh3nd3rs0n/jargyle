package com.github.jh3nd3rs0n.jargyle.client.internal.propertyspec.impl;

import com.github.jh3nd3rs0n.jargyle.common.net.Host;
import org.junit.Assert;
import org.junit.Test;

public class HostPropertySpecTest {

    @Test
    public void testParseString01() {
        Assert.assertEquals(
                Host.newInstance("localhost"),
                new HostPropertySpec("hostProperty", null).parse(
                        "localhost"));
    }

    @Test
    public void testParseString02() {
        Assert.assertEquals(
                Host.newInstance("127.0.0.1"),
                new HostPropertySpec("hostProperty", null).parse(
                        "127.0.0.1"));
    }

    @Test
    public void testParseString03() {
        Assert.assertEquals(
                Host.newInstance("::1"),
                new HostPropertySpec("hostProperty", null).parse(
                        "::1"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseStringForIllegalArgumentException01() {
        new HostPropertySpec("hostProperty", null).parse(
                "");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseStringForIllegalArgumentException02() {
        new HostPropertySpec("hostProperty", null).parse(
                " ");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseStringForIllegalArgumentException03() {
        new HostPropertySpec("hostProperty", null).parse(
                "@@@@@@@@");
    }

}