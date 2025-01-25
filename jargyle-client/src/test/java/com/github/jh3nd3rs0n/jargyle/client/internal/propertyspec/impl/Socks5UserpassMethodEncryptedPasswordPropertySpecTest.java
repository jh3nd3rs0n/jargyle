package com.github.jh3nd3rs0n.jargyle.client.internal.propertyspec.impl;

import com.github.jh3nd3rs0n.jargyle.common.security.EncryptedPassword;
import org.junit.Assert;
import org.junit.Test;

public class Socks5UserpassMethodEncryptedPasswordPropertySpecTest {

    private static final String TEST_PASSWORD_01;
    private static final String TEST_PASSWORD_02;
    private static final String TEST_PASSWORD_03;
    private static final String TEST_PASSWORD_04;
    
    static {
        String string = "0123456789" 
                + "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
                + "abcdefghijklmnopqrstuvwxyz";
        char[] password = string.toCharArray();
        TEST_PASSWORD_01 = "";
        TEST_PASSWORD_02 = string;
        StringBuilder stringBuilder03 = new StringBuilder();
        for (int i = 0, j = 0; i < 255; i++) {
            stringBuilder03.append(password[j++]);
            if (j == password.length) {
                j = 0;
            }
        }
        TEST_PASSWORD_03 = stringBuilder03.toString();
        StringBuilder stringBuilder04 = new StringBuilder();
        for (int i = 0, j = 0; i < 256; i++) {
            stringBuilder04.append(password[j++]);
            if (j == password.length) {
                j = 0;
            }
        }
        TEST_PASSWORD_04 = stringBuilder04.toString();        
    }
    
    @Test
    public void testParseString01() {
        Assert.assertArrayEquals(
                TEST_PASSWORD_01.toCharArray(),
                new Socks5UserpassMethodEncryptedPasswordPropertySpec(
                        "socks5UserpassMethodEncryptedPasswordProperty", 
                        null)
                        .parse(TEST_PASSWORD_01)
                        .getPassword());
    }

    @Test
    public void testParseString02() {
        Assert.assertArrayEquals(
                TEST_PASSWORD_02.toCharArray(),
                new Socks5UserpassMethodEncryptedPasswordPropertySpec(
                        "socks5UserpassMethodEncryptedPasswordProperty", 
                        null)
                        .parse(TEST_PASSWORD_02)
                        .getPassword());
    }

    @Test
    public void testParseString03() {
        Assert.assertArrayEquals(
                TEST_PASSWORD_03.toCharArray(),
                new Socks5UserpassMethodEncryptedPasswordPropertySpec(
                        "socks5UserpassMethodEncryptedPasswordProperty", 
                        null)
                        .parse(TEST_PASSWORD_03)
                        .getPassword());
    }

    @Test
    public void testParseString04() {
        Assert.assertArrayEquals(
                TEST_PASSWORD_04.toCharArray(),
                new Socks5UserpassMethodEncryptedPasswordPropertySpec(
                        "socks5UserpassMethodEncryptedPasswordProperty",
                        null)
                        .parse(TEST_PASSWORD_04)
                        .getPassword());
    }

    @Test
    public void testSocks5UserpassMethodEncryptedPasswordPropertySpecStringEncryptedPassword01() {
        Assert.assertArrayEquals(
                TEST_PASSWORD_01.toCharArray(),
                new Socks5UserpassMethodEncryptedPasswordPropertySpec(
                        "socks5UserpassMethodEncryptedPasswordProperty",
                        EncryptedPassword.newInstance(
                                TEST_PASSWORD_01.toCharArray()))
                        .getDefaultProperty()
                        .getValue()
                        .getPassword());
    }

    @Test
    public void testSocks5UserpassMethodEncryptedPasswordPropertySpecStringEncryptedPassword02() {
        Assert.assertArrayEquals(
                TEST_PASSWORD_02.toCharArray(),
                new Socks5UserpassMethodEncryptedPasswordPropertySpec(
                        "socks5UserpassMethodEncryptedPasswordProperty",
                        EncryptedPassword.newInstance(
                                TEST_PASSWORD_02.toCharArray()))
                        .getDefaultProperty()
                        .getValue()
                        .getPassword());
    }

    @Test
    public void testSocks5UserpassMethodEncryptedPasswordPropertySpecStringEncryptedPassword03() {
        Assert.assertArrayEquals(
                TEST_PASSWORD_03.toCharArray(),
                new Socks5UserpassMethodEncryptedPasswordPropertySpec(
                        "socks5UserpassMethodEncryptedPasswordProperty",
                        EncryptedPassword.newInstance(
                                TEST_PASSWORD_03.toCharArray()))
                        .getDefaultProperty()
                        .getValue()
                        .getPassword());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSocks5UserpassMethodEncryptedPasswordPropertySpecStringEncryptedPasswordForIllegalArgumentException01() {
        new Socks5UserpassMethodEncryptedPasswordPropertySpec(
                "socks5UserpassMethodEncryptedPasswordProperty",
                EncryptedPassword.newInstance(
                        TEST_PASSWORD_04.toCharArray()));
    }

    @Test
    public void testValidateEncryptedPassword01() {
        Assert.assertArrayEquals(
                TEST_PASSWORD_01.toCharArray(),
                new Socks5UserpassMethodEncryptedPasswordPropertySpec(
                        "socks5UserpassMethodEncryptedPasswordProperty",
                        null)
                        .validate(EncryptedPassword.newInstance(
                                TEST_PASSWORD_01.toCharArray()))
                        .getPassword());
    }

    @Test
    public void testValidateEncryptedPassword02() {
        Assert.assertArrayEquals(
                TEST_PASSWORD_02.toCharArray(),
                new Socks5UserpassMethodEncryptedPasswordPropertySpec(
                        "socks5UserpassMethodEncryptedPasswordProperty",
                        null)
                        .validate(EncryptedPassword.newInstance(
                                TEST_PASSWORD_02.toCharArray()))
                        .getPassword());
    }

    @Test
    public void testValidateEncryptedPassword03() {
        Assert.assertArrayEquals(
                TEST_PASSWORD_03.toCharArray(),
                new Socks5UserpassMethodEncryptedPasswordPropertySpec(
                        "socks5UserpassMethodEncryptedPasswordProperty",
                        null)
                        .validate(EncryptedPassword.newInstance(
                                TEST_PASSWORD_03.toCharArray()))
                        .getPassword());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testValidateEncryptedPasswordForIllegalArgumentException01() {
        new Socks5UserpassMethodEncryptedPasswordPropertySpec(
                "socks5UserpassMethodEncryptedPasswordProperty",
                null)
                .validate(EncryptedPassword.newInstance(
                        TEST_PASSWORD_04.toCharArray()));
    }

}