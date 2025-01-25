package com.github.jh3nd3rs0n.jargyle.client.internal.propertyspec.impl;

import org.junit.Assert;
import org.junit.Test;

public class IntegerPropertySpecTest {

    @Test
    public void testParseString01() {
        Assert.assertEquals(
                Integer.MIN_VALUE,
                new IntegerPropertySpec("integerProperty", null).parse(
                        Integer.toString(Integer.MIN_VALUE)).intValue());
    }

    @Test
    public void testParseString02() {
        Assert.assertEquals(
                0,
                new IntegerPropertySpec("integerProperty", null).parse(
                        "0").intValue());
    }

    @Test
    public void testParseString03() {
        Assert.assertEquals(
                Integer.MAX_VALUE,
                new IntegerPropertySpec("integerProperty", null).parse(
                        Integer.toString(Integer.MAX_VALUE)).intValue());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseStringForIllegalArgumentException01() {
        new IntegerPropertySpec("integerProperty", null).parse(
                "");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseStringForIllegalArgumentException02() {
        new IntegerPropertySpec("integerProperty", null).parse(
                " ");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseStringForIllegalArgumentException03() {
        new IntegerPropertySpec("integerProperty", null).parse(
                "positive infinity");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseStringForIllegalArgumentException04() {
        new IntegerPropertySpec("integerProperty", null).parse(
                "-2147483649");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseStringForIllegalArgumentException05() {
        new IntegerPropertySpec("integerProperty", null).parse(
                "2147483648");
    }

}