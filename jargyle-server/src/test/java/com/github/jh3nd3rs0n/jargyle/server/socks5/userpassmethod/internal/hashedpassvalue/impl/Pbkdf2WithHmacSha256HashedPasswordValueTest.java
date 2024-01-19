package com.github.jh3nd3rs0n.jargyle.server.socks5.userpassmethod.internal.hashedpassvalue.impl;

import org.junit.Assert;
import org.junit.Test;

public class Pbkdf2WithHmacSha256HashedPasswordValueTest {

    @Test(expected = IllegalArgumentException.class)
    public void testPbkdf2WithHmacSha256HashedPasswordValueStringForIllegalArgumentException01() {
        new Pbkdf2WithHmacSha256HashedPasswordValue(
                "VR0DcGHGjEq2k4/qb+84CBWE0dja7uH124DwSiDtaZc=");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPbkdf2WithHmacSha256HashedPasswordValueStringForIllegalArgumentException02() {
        new Pbkdf2WithHmacSha256HashedPasswordValue(
                "VR0DcGHGjEq2k4/qb+84CBWE0dja7uH124DwSiDtaZc=," +
                        "TyoaMit8WyIRHQ==," +
                        "ie82f0w0je0ajfap==");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPbkdf2WithHmacSha256HashedPasswordValueStringForIllegalArgumentException03() {
        new Pbkdf2WithHmacSha256HashedPasswordValue(
                "?????????????????????????????????," +
                        "TyoaMit8WyIRHQ==");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPbkdf2WithHmacSha256HashedPasswordValueStringForIllegalArgumentException04() {
        new Pbkdf2WithHmacSha256HashedPasswordValue(
                "VR0DcGHGjEq2k4/qb+84CBWE0dja7uH124DwSiDtaZc=," +
                        "????????????????????");
    }

    @Test
    public void testEqualsObject01() {
        Pbkdf2WithHmacSha256HashedPasswordValue s =
                new Pbkdf2WithHmacSha256HashedPasswordValue(
                        "Hello, World".toCharArray(),
                        new byte[] { 0x4f, 0x2a, 0x1a, 0x32, 0x2b, 0x7c, 0x5b, 0x22, 0x11, 0x1d });
        Assert.assertEquals(s, s);
    }

    @Test
    public void testEqualsObject02() {
        Pbkdf2WithHmacSha256HashedPasswordValue s =
                new Pbkdf2WithHmacSha256HashedPasswordValue(
                        "Hello, World".toCharArray(),
                        new byte[] { 0x4f, 0x2a, 0x1a, 0x32, 0x2b, 0x7c, 0x5b, 0x22, 0x11, 0x1d });
        Assert.assertNotEquals(s, null);
    }

    @Test
    public void testEqualsObject03() {
        Object obj1 = new Pbkdf2WithHmacSha256HashedPasswordValue(
                "Hello, World".toCharArray(),
                new byte[] { 0x4f, 0x2a, 0x1a, 0x32, 0x2b, 0x7c, 0x5b, 0x22, 0x11, 0x1d });
        Object obj2 = new Object();
        Assert.assertNotEquals(obj1, obj2);
    }

    @Test
    public void testEqualsObject04() {
        Pbkdf2WithHmacSha256HashedPasswordValue s1 =
                new Pbkdf2WithHmacSha256HashedPasswordValue(
                        "Hello, World".toCharArray(),
                        new byte[] { 0x4f, 0x2a, 0x1a, 0x32, 0x2b, 0x7c, 0x5b, 0x22, 0x11, 0x1d });
        Pbkdf2WithHmacSha256HashedPasswordValue s2 =
                new Pbkdf2WithHmacSha256HashedPasswordValue(
                        "Hello, World".toCharArray(),
                        new byte[] { 0x4a, 0x7a, 0x1a, 0x3c, 0x2b, 0x7c, 0x52, 0x32, 0x11, 0x1a });
        Assert.assertNotEquals(s1, s2);
    }

    @Test
    public void testEqualsObject05() {
        Pbkdf2WithHmacSha256HashedPasswordValue s1 =
                new Pbkdf2WithHmacSha256HashedPasswordValue(
                        "Hello, World".toCharArray(),
                        new byte[] { 0x4f, 0x2a, 0x1a, 0x32, 0x2b, 0x7c, 0x5b, 0x22, 0x11, 0x1d });
        Pbkdf2WithHmacSha256HashedPasswordValue s2 =
                new Pbkdf2WithHmacSha256HashedPasswordValue(
                        "Hello, World".toCharArray(),
                        new byte[] { 0x4f, 0x2a, 0x1a, 0x32, 0x2b, 0x7c, 0x5b, 0x22, 0x11, 0x1d });
        Assert.assertEquals(s1, s2);
    }

    @Test
    public void testHashCode01() {
        Pbkdf2WithHmacSha256HashedPasswordValue s1 =
                new Pbkdf2WithHmacSha256HashedPasswordValue(
                        "Hello, World".toCharArray(),
                        new byte[] { 0x4f, 0x2a, 0x1a, 0x32, 0x2b, 0x7c, 0x5b, 0x22, 0x11, 0x1d });
        Pbkdf2WithHmacSha256HashedPasswordValue s2 =
                new Pbkdf2WithHmacSha256HashedPasswordValue(
                        "Hello, World".toCharArray(),
                        new byte[] { 0x4f, 0x2a, 0x1a, 0x32, 0x2b, 0x7c, 0x5b, 0x22, 0x11, 0x1d });
        Assert.assertEquals(s1.hashCode(), s2.hashCode());
    }

    @Test
    public void testHashCode02() {
        Pbkdf2WithHmacSha256HashedPasswordValue s1 =
                new Pbkdf2WithHmacSha256HashedPasswordValue(
                        "Hello, World".toCharArray(),
                        new byte[] { 0x4f, 0x2a, 0x1a, 0x32, 0x2b, 0x7c, 0x5b, 0x22, 0x11, 0x1d });
        Pbkdf2WithHmacSha256HashedPasswordValue s2 =
                new Pbkdf2WithHmacSha256HashedPasswordValue(
                        "Hello, World".toCharArray(),
                        new byte[] { 0x4a, 0x7a, 0x1a, 0x3c, 0x2b, 0x7c, 0x52, 0x32, 0x11, 0x1a });
        Assert.assertNotEquals(s1.hashCode(), s2.hashCode());
    }

    @Test
    public void testPasswordEqualsCharArray01() {
        char[] password = "Hello, World".toCharArray();
        Pbkdf2WithHmacSha256HashedPasswordValue s =
                new Pbkdf2WithHmacSha256HashedPasswordValue(
                        password,
                        new byte[] { 0x4f, 0x2a, 0x1a, 0x32, 0x2b, 0x7c, 0x5b, 0x22, 0x11, 0x1d });
        Assert.assertTrue(s.passwordEquals(password));
    }

    @Test
    public void testPasswordEqualsCharArray02() {
        Pbkdf2WithHmacSha256HashedPasswordValue s =
                new Pbkdf2WithHmacSha256HashedPasswordValue(
                        "Hello, World".toCharArray(),
                        new byte[] { 0x4f, 0x2a, 0x1a, 0x32, 0x2b, 0x7c, 0x5b, 0x22, 0x11, 0x1d });
        Assert.assertFalse(s.passwordEquals("opensesame".toCharArray()));
    }

    @Test
    public void testPasswordEqualsCharArray03() {
        char[] password = "Hello, World".toCharArray();
        Pbkdf2WithHmacSha256HashedPasswordValue s =
                new Pbkdf2WithHmacSha256HashedPasswordValue(password);
        Assert.assertTrue(s.passwordEquals(password));
    }

    @Test
    public void testPasswordEqualsCharArray04() {
        Pbkdf2WithHmacSha256HashedPasswordValue s =
                new Pbkdf2WithHmacSha256HashedPasswordValue(
                        "Hello, World".toCharArray());
        Assert.assertFalse(s.passwordEquals("opensesame".toCharArray()));
    }

    @Test
    public void testToString01() {
        Pbkdf2WithHmacSha256HashedPasswordValue s =
                new Pbkdf2WithHmacSha256HashedPasswordValue(
                        "Hello, World".toCharArray(),
                        new byte[] { 0x4f, 0x2a, 0x1a, 0x32, 0x2b, 0x7c, 0x5b, 0x22, 0x11, 0x1d });
        Assert.assertEquals(
                "VR0DcGHGjEq2k4/qb+84CBWE0dja7uH124DwSiDtaZc=," +
                        "TyoaMit8WyIRHQ==",
                s.toString());
    }

    @Test
    public void testToString02() {
        String string = "VR0DcGHGjEq2k4/qb+84CBWE0dja7uH124DwSiDtaZc=," +
                "TyoaMit8WyIRHQ==";
        Pbkdf2WithHmacSha256HashedPasswordValue s =
                new Pbkdf2WithHmacSha256HashedPasswordValue(string);
        Assert.assertEquals(string, s.toString());
    }

}