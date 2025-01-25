package com.github.jh3nd3rs0n.jargyle.client.internal.propertyspec.impl;

import com.github.jh3nd3rs0n.jargyle.common.string.CommaSeparatedValues;
import org.junit.Assert;
import org.junit.Test;

public class CommaSeparatedValuesPropertySpecTest {

    @Test
    public void testParseString01() {
        Assert.assertEquals(
                CommaSeparatedValues.newInstanceFrom(""),
                new CommaSeparatedValuesPropertySpec(
                        "commaSeparatedValuesProperty", null)
                        .parse(""));
    }

    @Test
    public void testParseString02() {
        Assert.assertEquals(
                CommaSeparatedValues.newInstanceFrom("a"),
                new CommaSeparatedValuesPropertySpec(
                        "commaSeparatedValuesProperty", null)
                        .parse("a"));
    }

    @Test
    public void testParseString03() {
        Assert.assertEquals(
                CommaSeparatedValues.newInstanceFrom("a,b"),
                new CommaSeparatedValuesPropertySpec(
                        "commaSeparatedValuesProperty", null)
                        .parse("a,b"));
    }

    @Test
    public void testParseString04() {
        Assert.assertEquals(
                CommaSeparatedValues.newInstanceFrom("a,b,c"),
                new CommaSeparatedValuesPropertySpec(
                        "commaSeparatedValuesProperty", null)
                        .parse("a,b,c"));
    }

}