package com.github.jh3nd3rs0n.jargyle.client.internal.propertyspec.impl;

import com.github.jh3nd3rs0n.jargyle.common.number.NonNegativeInteger;
import org.junit.Assert;
import org.junit.Test;

public class NonNegativeIntegerPropertySpecTest {

    @Test
    public void testParseString01() {
        Assert.assertEquals(
                NonNegativeInteger.valueOf(0),
                new NonNegativeIntegerPropertySpec(
                        "nonNegativeIntegerProperty", null)
                        .parse("0"));
    }

    @Test
    public void testParseString02() {
        Assert.assertEquals(
                NonNegativeInteger.valueOf(1),
                new NonNegativeIntegerPropertySpec(
                        "nonNegativeIntegerProperty", null)
                        .parse("1"));
    }

    @Test
    public void testParseString03() {
        Assert.assertEquals(
                NonNegativeInteger.valueOf(Integer.MAX_VALUE),
                new NonNegativeIntegerPropertySpec(
                        "nonNegativeIntegerProperty", null)
                        .parse("2147483647"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseStringForIllegalArgumentException01() {
        new NonNegativeIntegerPropertySpec(
                "nonNegativeIntegerProperty", null)
                .parse("");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseStringForIllegalArgumentException02() {
        new NonNegativeIntegerPropertySpec(
                "nonNegativeIntegerProperty", null)
                .parse(" ");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseStringForIllegalArgumentException03() {
        new NonNegativeIntegerPropertySpec(
                "nonNegativeIntegerProperty", null)
                .parse("zzz");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseStringForIllegalArgumentException04() {
        new NonNegativeIntegerPropertySpec(
                "nonNegativeIntegerProperty", null)
                .parse("-1");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseStringForIllegalArgumentException05() {
        new NonNegativeIntegerPropertySpec(
                "nonNegativeIntegerProperty", null)
                .parse("2147483648");
    }

}