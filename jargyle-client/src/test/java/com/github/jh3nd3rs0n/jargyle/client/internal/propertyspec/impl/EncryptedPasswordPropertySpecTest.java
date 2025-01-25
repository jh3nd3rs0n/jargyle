package com.github.jh3nd3rs0n.jargyle.client.internal.propertyspec.impl;

import org.junit.Assert;
import org.junit.Test;

public class EncryptedPasswordPropertySpecTest {

    @Test
    public void testParseString01() {
        Assert.assertArrayEquals(
                "".toCharArray(),
                new EncryptedPasswordPropertySpec(
                        "encryptedPasswordProperty", null).parse("")
                        .getPassword());
    }

    @Test
    public void testParseString02() {
        Assert.assertArrayEquals(
                "Hello, World!".toCharArray(),
                new EncryptedPasswordPropertySpec(
                        "encryptedPasswordProperty", null).parse(
                                "Hello, World!")
                        .getPassword());
    }

    @Test
    public void testParseString03() {
        Assert.assertArrayEquals(
                "The quick brown fox jumped over the lazy dog".toCharArray(),
                new EncryptedPasswordPropertySpec(
                        "encryptedPasswordProperty", null).parse(
                                "The quick brown fox jumped over the lazy dog")
                        .getPassword());
    }

}