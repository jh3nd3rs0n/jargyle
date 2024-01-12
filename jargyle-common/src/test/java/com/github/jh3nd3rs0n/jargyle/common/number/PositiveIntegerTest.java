package com.github.jh3nd3rs0n.jargyle.common.number;

import org.junit.Assert;
import org.junit.Test;

public class PositiveIntegerTest {

    @Test
    public void testEqualsObject01() {
        PositiveInteger positiveInteger = PositiveInteger.valueOf(1072);
        Assert.assertEquals(positiveInteger, positiveInteger);
    }

    @Test
    public void testEqualsObject02() {
        Assert.assertNotEquals(PositiveInteger.valueOf(1072), null);
    }

    @Test
    public void testEqualsObject03() {
        Object obj1 = PositiveInteger.valueOf(1072);
        Object obj2 = new Object();
        Assert.assertNotEquals(obj1, obj2);
    }

    @Test
    public void testEqualsObject04() {
        Assert.assertNotEquals(
                PositiveInteger.valueOf(1072),
                PositiveInteger.valueOf(1073));
    }

    @Test
    public void testEqualsObject05() {
        Assert.assertEquals(
                PositiveInteger.valueOf(1072),
                PositiveInteger.valueOf(1072));
    }

    @Test
    public void testHashCode01() {
        Assert.assertEquals(
                PositiveInteger.valueOf(1072).hashCode(),
                PositiveInteger.valueOf(1072).hashCode());
    }

    @Test
    public void testHashCode02() {
        Assert.assertNotEquals(
                PositiveInteger.valueOf(1072).hashCode(),
                PositiveInteger.valueOf(1073).hashCode());
    }

    @Test
    public void testIntValue() {
        PositiveInteger positiveInteger = PositiveInteger.valueOf(
                "1072");
        Assert.assertEquals(1072, positiveInteger.intValue());
    }

    @Test
    public void testToString() {
        PositiveInteger positiveInteger = PositiveInteger.valueOf(1072);
        Assert.assertEquals("1072", positiveInteger.toString());
    }

    @Test
    public void testValueOfInt01() {
        Assert.assertNotNull(PositiveInteger.valueOf(1));
    }

    @Test
    public void testValueOfInt02() {
        Assert.assertNotNull(PositiveInteger.valueOf(192837465));
    }

    @Test
    public void testValueOfInt03() {
        Assert.assertNotNull(PositiveInteger.valueOf(2147483647));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testValueOfIntForIllegalArgumentException() {
        PositiveInteger.valueOf(0);
    }

    @Test
    public void testValueOfString01() {
        Assert.assertNotNull(PositiveInteger.valueOf("1"));
    }

    @Test
    public void testValueOfString02() {
        Assert.assertNotNull(PositiveInteger.valueOf("918273645"));
    }

    @Test
    public void testValueOfString03() {
        Assert.assertNotNull(PositiveInteger.valueOf("2147483647"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testValueOfStringForIllegalArgumentException01() {
        PositiveInteger.valueOf("babe");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testValueOfStringForIllegalArgumentException02() {
        PositiveInteger.valueOf("0");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testValueOfStringForIllegalArgumentException03() {
        PositiveInteger.valueOf("2147483648");
    }

}