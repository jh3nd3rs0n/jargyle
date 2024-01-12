package com.github.jh3nd3rs0n.jargyle.common.number;

import org.junit.Assert;
import org.junit.Test;

public class NonNegativeIntegerTest {

    @Test
    public void testEqualsObject01() {
        NonNegativeInteger nonNegativeInteger =
                NonNegativeInteger.valueOf(2701);
        Assert.assertEquals(nonNegativeInteger, nonNegativeInteger);
    }

    @Test
    public void testEqualsObject02() {
        Assert.assertNotEquals(NonNegativeInteger.valueOf(2701), null);
    }

    @Test
    public void testEqualsObject03() {
        Object obj1 = NonNegativeInteger.valueOf(2701);
        Object obj2 = new Object();
        Assert.assertNotEquals(obj1, obj2);
    }

    @Test
    public void testEqualsObject04() {
        Assert.assertNotEquals(
                NonNegativeInteger.valueOf(2701),
                NonNegativeInteger.valueOf(2702));
    }

    @Test
    public void testEqualsObject05() {
        Assert.assertEquals(
                NonNegativeInteger.valueOf(2701),
                NonNegativeInteger.valueOf(2701));
    }

    @Test
    public void testHashCode01() {
        Assert.assertEquals(
                NonNegativeInteger.valueOf(2701).hashCode(),
                NonNegativeInteger.valueOf(2701).hashCode());
    }

    @Test
    public void testHashCode02() {
        Assert.assertNotEquals(
                NonNegativeInteger.valueOf(2701).hashCode(),
                NonNegativeInteger.valueOf(2702).hashCode());
    }

    @Test
    public void testIntValue() {
        NonNegativeInteger nonNegativeInteger =
                NonNegativeInteger.valueOf("2701");
        Assert.assertEquals(2701, nonNegativeInteger.intValue());
    }

    @Test
    public void testToString() {
        NonNegativeInteger nonNegativeInteger =
                NonNegativeInteger.valueOf(2701);
        Assert.assertEquals("2701", nonNegativeInteger.toString());
    }

    @Test
    public void testValueOfInt01() {
        Assert.assertNotNull(NonNegativeInteger.valueOf(0));
    }

    @Test
    public void testValueOfInt02() {
        Assert.assertNotNull(NonNegativeInteger.valueOf(987654321));
    }

    @Test
    public void testValueOfInt03() {
        Assert.assertNotNull(NonNegativeInteger.valueOf(2147483647));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testValueOfIntForIllegalArgumentException01() {
        NonNegativeInteger.valueOf(-1);
    }

    @Test
    public void testValueOfString01() {
        Assert.assertNotNull(NonNegativeInteger.valueOf("0"));
    }

    @Test
    public void testValueOfString02() {
        Assert.assertNotNull(NonNegativeInteger.valueOf("1234567890"));
    }

    @Test
    public void testValueOfString03() {
        Assert.assertNotNull(NonNegativeInteger.valueOf("2147483647"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testValueOfStringForIllegalArgumentException01() {
        NonNegativeInteger.valueOf("cafe");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testValueOfStringForIllegalArgumentException02() {
        NonNegativeInteger.valueOf("-1");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testValueOfStringForIllegalArgumentException03() {
        NonNegativeInteger.valueOf("2147483648");
    }

}