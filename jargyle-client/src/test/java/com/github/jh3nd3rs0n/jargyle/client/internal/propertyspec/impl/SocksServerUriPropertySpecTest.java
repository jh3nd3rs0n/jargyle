package com.github.jh3nd3rs0n.jargyle.client.internal.propertyspec.impl;

import com.github.jh3nd3rs0n.jargyle.client.SocksServerUriScheme;
import org.junit.Assert;
import org.junit.Test;

public class SocksServerUriPropertySpecTest {

    @Test
    public void testParseString01() {
        Assert.assertEquals(
                SocksServerUriScheme.SOCKS5.newSocksServerUri("localhost"),
                new SocksServerUriPropertySpec("socksServerUriProperty", null)
                        .parse("socks5://localhost"));
    }

    @Test
    public void testParseString02() {
        Assert.assertEquals(
                SocksServerUriScheme.SOCKS5.newSocksServerUri(
                        "Jasmine:mission%3Aimpossible",
                        "localhost"),
                new SocksServerUriPropertySpec("socksServerUriProperty", null)
                        .parse("socks5://Jasmine:mission%3Aimpossible@localhost"));
    }

    @Test
    public void testParseString03() {
        Assert.assertEquals(
                SocksServerUriScheme.SOCKS5.newSocksServerUri(
                        "localhost",
                        1080),
                new SocksServerUriPropertySpec("socksServerUriProperty", null)
                        .parse("socks5://localhost:1080"));
    }

    @Test
    public void testParseString04() {
        Assert.assertEquals(
                SocksServerUriScheme.SOCKS5.newSocksServerUri(
                        "Jasmine:mission%3Aimpossible",
                        "localhost",
                        1080),
                new SocksServerUriPropertySpec("socksServerUriProperty", null)
                        .parse("socks5://Jasmine:mission%3Aimpossible@localhost:1080"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseStringForIllegalArgumentException01() {
        new SocksServerUriPropertySpec("socksServerUriProperty", null)
                .parse("");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseStringForIllegalArgumentException02() {
        new SocksServerUriPropertySpec("socksServerUriProperty", null)
                .parse(" ");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseStringForIllegalArgumentException03() {
        new SocksServerUriPropertySpec("socksServerUriProperty", null)
                .parse("a");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseStringForIllegalArgumentException04() {
        new SocksServerUriPropertySpec("socksServerUriProperty", null)
                .parse("socks1");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseStringForIllegalArgumentException05() {
        new SocksServerUriPropertySpec("socksServerUriProperty", null)
                .parse("socks1://localhost");
    }

}