package com.github.jh3nd3rs0n.jargyle.client.internal.propertyspec.impl;

import com.github.jh3nd3rs0n.jargyle.protocolbase.socks5.Method;
import com.github.jh3nd3rs0n.jargyle.protocolbase.socks5.Methods;
import org.junit.Assert;
import org.junit.Test;

public class Socks5MethodsPropertySpecTest {

    @Test
    public void testParseString01() {
        Assert.assertEquals(
                Methods.of(),
                new Socks5MethodsPropertySpec("socks5MethodsProperty", null)
                        .parse(""));
    }

    @Test
    public void testParseString02() {
        Assert.assertEquals(
                Methods.of(Method.NO_AUTHENTICATION_REQUIRED),
                new Socks5MethodsPropertySpec("socks5MethodsProperty", null)
                        .parse("NO_AUTHENTICATION_REQUIRED"));
    }

    @Test
    public void testParseString03() {
        Assert.assertEquals(
                Methods.of(
                        Method.NO_AUTHENTICATION_REQUIRED,
                        Method.GSSAPI),
                new Socks5MethodsPropertySpec("socks5MethodsProperty", null)
                        .parse("NO_AUTHENTICATION_REQUIRED,GSSAPI"));
    }

    @Test
    public void testParseString04() {
        Assert.assertEquals(
                Methods.of(
                        Method.NO_AUTHENTICATION_REQUIRED,
                        Method.GSSAPI,
                        Method.USERNAME_PASSWORD),
                new Socks5MethodsPropertySpec("socks5MethodsProperty", null)
                        .parse("NO_AUTHENTICATION_REQUIRED,GSSAPI,USERNAME_PASSWORD"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseStringForIllegalArgumentException01() {
        new Socks5MethodsPropertySpec("socks5MethodsProperty", null)
                .parse(" ");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseStringForIllegalArgumentException02() {
        new Socks5MethodsPropertySpec("socks5MethodsProperty", null)
                .parse("BOGUS");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseStringForIllegalArgumentException03() {
        new Socks5MethodsPropertySpec("socks5MethodsProperty", null)
                .parse("NO_AUTHENTICATION,");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseStringForIllegalArgumentException04() {
        new Socks5MethodsPropertySpec("socks5MethodsProperty", null)
                .parse("NO_AUTHENTICATION,BOGUS");
    }

}