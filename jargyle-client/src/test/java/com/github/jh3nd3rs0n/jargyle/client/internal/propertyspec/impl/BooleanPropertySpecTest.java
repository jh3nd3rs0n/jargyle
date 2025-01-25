package com.github.jh3nd3rs0n.jargyle.client.internal.propertyspec.impl;

import org.junit.Assert;
import org.junit.Test;

public class BooleanPropertySpecTest {

    @Test
    public void testParseString01() {
        Assert.assertTrue(
                new BooleanPropertySpec("booleanProperty", null)
                        .parse("true"));
    }

    @Test
    public void testParseString02() {
        Assert.assertFalse(
                new BooleanPropertySpec("booleanProperty", null)
                        .parse("false"));
    }

    @Test
    public void testParseString03() {
        Assert.assertFalse(
                new BooleanPropertySpec("booleanProperty", null)
                        .parse("bogus"));
    }

}