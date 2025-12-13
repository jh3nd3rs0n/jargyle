package com.github.jh3nd3rs0n.jargyle.client.internal.propertyspec.impl;

import org.junit.Assert;
import org.junit.Test;

public class Socks5UserpassAuthMethodUsernamePropertySpecTest {

    private static final String TEST_USERNAME_01;
    private static final String TEST_USERNAME_02;
    private static final String TEST_USERNAME_03;
    private static final String TEST_USERNAME_04;

    static {
        String string = "0123456789"
                + "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
                + "abcdefghijklmnopqrstuvwxyz";
        char[] chars = string.toCharArray();
        TEST_USERNAME_01 = "";
        TEST_USERNAME_02 = string;
        StringBuilder stringBuilder03 = new StringBuilder();
        for (int i = 0, j = 0; i < 255; i++) {
            stringBuilder03.append(chars[j++]);
            if (j == chars.length) {
                j = 0;
            }
        }
        TEST_USERNAME_03 = stringBuilder03.toString();
        StringBuilder stringBuilder04 = new StringBuilder();
        for (int i = 0, j = 0; i < 256; i++) {
            stringBuilder04.append(chars[j++]);
            if (j == chars.length) {
                j = 0;
            }
        }
        TEST_USERNAME_04 = stringBuilder04.toString();
    }

    @Test
    public void testParseString01() {
        Assert.assertEquals(
                TEST_USERNAME_01,
                new Socks5UserpassAuthMethodUsernamePropertySpec(
                        "socks5UserpassAuthMethodUsernameProperty",
                        null)
                        .parse(TEST_USERNAME_01));
    }

    @Test
    public void testParseString02() {
        Assert.assertEquals(
                TEST_USERNAME_02,
                new Socks5UserpassAuthMethodUsernamePropertySpec(
                        "socks5UserpassAuthMethodUsernameProperty",
                        null)
                        .parse(TEST_USERNAME_02));
    }

    @Test
    public void testParseString03() {
        Assert.assertEquals(
                TEST_USERNAME_03,
                new Socks5UserpassAuthMethodUsernamePropertySpec(
                        "socks5UserpassAuthMethodUsernameProperty",
                        null)
                        .parse(TEST_USERNAME_03));
    }

    @Test
    public void testParseString04() {
        Assert.assertEquals(
                TEST_USERNAME_04,
                new Socks5UserpassAuthMethodUsernamePropertySpec(
                        "socks5UserpassAuthMethodUsernameProperty",
                        null)
                        .parse(TEST_USERNAME_04));
    }

    @Test
    public void testSocks5UserpassAuthMethodUsernamePropertySpecStringString01() {
        Assert.assertEquals(
                TEST_USERNAME_01,
                new Socks5UserpassAuthMethodUsernamePropertySpec(
                        "socks5UserpassAuthMethodUsernameProperty",
                        TEST_USERNAME_01)
                        .getDefaultProperty()
                        .getValue());
    }

    @Test
    public void testSocks5UserpassAuthMethodUsernamePropertySpecStringString02() {
        Assert.assertEquals(
                TEST_USERNAME_02,
                new Socks5UserpassAuthMethodUsernamePropertySpec(
                        "socks5UserpassAuthMethodUsernameProperty",
                        TEST_USERNAME_02)
                        .getDefaultProperty()
                        .getValue());
    }

    @Test
    public void testSocks5UserpassAuthMethodUsernamePropertySpecStringString03() {
        Assert.assertEquals(
                TEST_USERNAME_03,
                new Socks5UserpassAuthMethodUsernamePropertySpec(
                        "socks5UserpassAuthMethodUsernameProperty",
                        TEST_USERNAME_03)
                        .getDefaultProperty()
                        .getValue());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSocks5UserpassAuthMethodUsernamePropertySpecStringStringForIllegalArgumentException01() {
        new Socks5UserpassAuthMethodUsernamePropertySpec(
                "socks5UserpassAuthMethodUsernameProperty",
                TEST_USERNAME_04);
    }

    @Test
    public void testValidateString01() {
        Assert.assertEquals(
                TEST_USERNAME_01,
                new Socks5UserpassAuthMethodUsernamePropertySpec(
                        "socks5UserpassAuthMethodUsernameProperty",
                        null)
                        .validate(TEST_USERNAME_01));
    }

    @Test
    public void testValidateString02() {
        Assert.assertEquals(
                TEST_USERNAME_02,
                new Socks5UserpassAuthMethodUsernamePropertySpec(
                        "socks5UserpassAuthMethodUsernameProperty",
                        null)
                        .validate(TEST_USERNAME_02));
    }

    @Test
    public void testValidateString03() {
        Assert.assertEquals(
                TEST_USERNAME_03,
                new Socks5UserpassAuthMethodUsernamePropertySpec(
                        "socks5UserpassAuthMethodUsernameProperty",
                        null)
                        .validate(TEST_USERNAME_03));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testValidateStringForIllegalArgumentException01() {
        new Socks5UserpassAuthMethodUsernamePropertySpec(
                "socks5UserpassAuthMethodUsernameProperty",
                null)
                .validate(TEST_USERNAME_04);
    }
    
}