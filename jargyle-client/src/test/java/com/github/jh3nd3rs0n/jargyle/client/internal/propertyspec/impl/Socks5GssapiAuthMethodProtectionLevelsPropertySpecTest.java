package com.github.jh3nd3rs0n.jargyle.client.internal.propertyspec.impl;

import com.github.jh3nd3rs0n.jargyle.protocolbase.socks5.gssapiauthmethod.ProtectionLevel;
import com.github.jh3nd3rs0n.jargyle.protocolbase.socks5.gssapiauthmethod.ProtectionLevels;
import org.junit.Assert;
import org.junit.Test;

public class Socks5GssapiAuthMethodProtectionLevelsPropertySpecTest {

    @Test
    public void testParseString01() {
        Assert.assertEquals(
                ProtectionLevels.of(ProtectionLevel.NONE),
                new Socks5GssapiAuthMethodProtectionLevelsPropertySpec(
                        "socks5GssapiAuthMethodProtectionLevelsProperty", null)
                        .parse("NONE"));
    }

    @Test
    public void testParseString02() {
        Assert.assertEquals(
                ProtectionLevels.of(
                        ProtectionLevel.NONE,
                        ProtectionLevel.REQUIRED_INTEG),
                new Socks5GssapiAuthMethodProtectionLevelsPropertySpec(
                        "socks5GssapiAuthMethodProtectionLevelsProperty", null)
                        .parse("NONE,REQUIRED_INTEG"));
    }

    @Test
    public void testParseString03() {
        Assert.assertEquals(
                ProtectionLevels.of(
                        ProtectionLevel.NONE,
                        ProtectionLevel.REQUIRED_INTEG,
                        ProtectionLevel.REQUIRED_INTEG_AND_CONF),
                new Socks5GssapiAuthMethodProtectionLevelsPropertySpec(
                        "socks5GssapiAuthMethodProtectionLevelsProperty", null)
                        .parse("NONE,REQUIRED_INTEG,REQUIRED_INTEG_AND_CONF"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseStringForIllegalArgumentException01() {
        new Socks5GssapiAuthMethodProtectionLevelsPropertySpec(
                "socks5GssapiAuthMethodProtectionLevelsProperty", null)
                .parse("");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseStringForIllegalArgumentException02() {
        new Socks5GssapiAuthMethodProtectionLevelsPropertySpec(
                "socks5GssapiAuthMethodProtectionLevelsProperty", null)
                .parse(" ");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseStringForIllegalArgumentException03() {
        new Socks5GssapiAuthMethodProtectionLevelsPropertySpec(
                "socks5GssapiAuthMethodProtectionLevelsProperty", null)
                .parse("BOGUS");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseStringForIllegalArgumentException04() {
        new Socks5GssapiAuthMethodProtectionLevelsPropertySpec(
                "socks5GssapiAuthMethodProtectionLevelsProperty", null)
                .parse("NONE,");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseStringForIllegalArgumentException05() {
        new Socks5GssapiAuthMethodProtectionLevelsPropertySpec(
                "socks5GssapiAuthMethodProtectionLevelsProperty", null)
                .parse("NONE,BOGUS");
    }

}