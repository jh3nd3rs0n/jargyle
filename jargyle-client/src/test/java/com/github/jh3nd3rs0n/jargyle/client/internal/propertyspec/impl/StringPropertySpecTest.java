package com.github.jh3nd3rs0n.jargyle.client.internal.propertyspec.impl;

import org.junit.Assert;
import org.junit.Test;

public class StringPropertySpecTest {

    @Test
    public void testParseString01() {
        Assert.assertEquals(
                "Hello, World!",
                new StringPropertySpec("stringProperty", null)
                        .parse("Hello, World!"));
    }

}