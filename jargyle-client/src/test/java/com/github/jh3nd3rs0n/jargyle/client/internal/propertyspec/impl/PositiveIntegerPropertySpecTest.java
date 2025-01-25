package com.github.jh3nd3rs0n.jargyle.client.internal.propertyspec.impl;

import com.github.jh3nd3rs0n.jargyle.common.number.PositiveInteger;
import org.junit.Assert;
import org.junit.Test;

public class PositiveIntegerPropertySpecTest {

    @Test
    public void testParseString01() {
        Assert.assertEquals(
                PositiveInteger.valueOf(1),
                new PositiveIntegerPropertySpec("positiveIntegerProperty", null)
                        .parse("1"));
    }

    @Test
    public void testParseString02() {
        Assert.assertEquals(
                PositiveInteger.valueOf(2147483647),
                new PositiveIntegerPropertySpec("positiveIntegerProperty", null)
                        .parse("2147483647"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseStringForIllegalArgumentException01() {
        new PositiveIntegerPropertySpec("positiveIntegerProperty", null)
                .parse("");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseStringForIllegalArgumentException02() {
        new PositiveIntegerPropertySpec("positiveIntegerProperty", null)
                .parse(" ");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseStringForIllegalArgumentException03() {
        new PositiveIntegerPropertySpec("positiveIntegerProperty", null)
                .parse("a");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseStringForIllegalArgumentException04() {
        new PositiveIntegerPropertySpec("positiveIntegerProperty", null)
                .parse("0");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseStringForIllegalArgumentException05() {
        new PositiveIntegerPropertySpec("positiveIntegerProperty", null)
                .parse("2147483648");
    }

}