package com.github.jh3nd3rs0n.jargyle.common.number;

import org.junit.Assert;
import org.junit.Test;

public class DigitTest {

    @Test
    public void testEqualsObject01() {
        Digit digit = Digit.valueOf(5);
        Assert.assertEquals(digit, digit);
    }

    @Test
    public void testEqualsObject02() {
        Assert.assertNotEquals(Digit.valueOf(5), null);
    }

    @Test
    public void testEqualsObject03() {
        Object obj1 = Digit.valueOf(5);
        Object obj2 = new Object();
        Assert.assertNotEquals(obj1, obj2);
    }

    @Test
    public void testEqualsObject04() {
        Assert.assertNotEquals(Digit.valueOf(5), Digit.valueOf(6));
    }

    @Test
    public void testEqualsObject05() {
        Assert.assertEquals(Digit.valueOf(5), Digit.valueOf(5));
    }

    @Test
    public void testHashCode01() {
        Assert.assertEquals(
                Digit.valueOf(5).hashCode(),
                Digit.valueOf(5).hashCode());
    }

    @Test
    public void testHashCode02() {
        Assert.assertNotEquals(
                Digit.valueOf(5).hashCode(),
                Digit.valueOf(6).hashCode());
    }

    @Test
    public void testIntValue() {
        Digit digit = Digit.valueOf("5");
        Assert.assertEquals(5, digit.intValue());
    }

    @Test
    public void testToString() {
        Digit digit = Digit.valueOf(5);
        Assert.assertEquals("5", digit.toString());
    }

    @Test
    public void testValueOfInt01() {
        Assert.assertNotNull(Digit.valueOf(0));
    }

    @Test
    public void testValueOfInt02() {
        Assert.assertNotNull(Digit.valueOf(8));
    }

    @Test
    public void testValueOfInt03() {
        Assert.assertNotNull(Digit.valueOf(9));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testValueOfIntForIllegalArgumentException01() {
        Digit.valueOf(-1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testValueOfIntForIllegalArgumentException02() {
        Digit.valueOf(10);
    }

    @Test
    public void testValueOfString01() {
        Assert.assertNotNull(Digit.valueOf("0"));
    }

    @Test
    public void testValueOfString02() {
        Assert.assertNotNull(Digit.valueOf("2"));
    }

    @Test
    public void testValueOfString03() {
        Assert.assertNotNull(Digit.valueOf("9"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testValueOfStringForIllegalArgumentException01() {
        Digit.valueOf("a");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testValueOfStringForIllegalArgumentException02() {
        Digit.valueOf("-1");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testValueOfStringForIllegalArgumentException03() {
        Digit.valueOf("10");
    }

}