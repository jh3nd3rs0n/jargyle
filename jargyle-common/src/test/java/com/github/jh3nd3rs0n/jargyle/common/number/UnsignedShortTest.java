package com.github.jh3nd3rs0n.jargyle.common.number;

import org.junit.Assert;
import org.junit.Test;

public class UnsignedShortTest {

    @Test
    public void testEqualsObject01() {
        UnsignedShort unsignedShort = UnsignedShort.valueOf(255);
        Assert.assertEquals(unsignedShort, unsignedShort);
    }

    @Test
    public void testEqualsObject02() {
        UnsignedShort unsignedShort = UnsignedShort.valueOf(255);
        Assert.assertNotEquals(unsignedShort, null);
    }

    @Test
    public void testEqualsObject03() {
        Object obj1 = UnsignedShort.valueOf(255);
        Object obj2 = new Object();
        Assert.assertNotEquals(obj1, obj2);
    }

    @Test
    public void testEqualsObject04() {
        Assert.assertNotEquals(
                UnsignedShort.valueOf(255),
                UnsignedShort.valueOf(256));
    }

    @Test
    public void testEqualsObject05() {
        Assert.assertEquals(
                UnsignedShort.valueOf(255),
                UnsignedShort.valueOf(255));
    }

    @Test
    public void testHashCode01() {
        Assert.assertEquals(
                UnsignedShort.valueOf(255).hashCode(),
                UnsignedShort.valueOf(255).hashCode());
    }

    @Test
    public void testHashCode02() {
        Assert.assertNotEquals(
                UnsignedShort.valueOf(255).hashCode(),
                UnsignedShort.valueOf(256).hashCode());
    }

    @Test
    public void testIntValue() {
        UnsignedShort unsignedShort = UnsignedShort.valueOf("255");
        Assert.assertEquals(255, unsignedShort.intValue());
    }

    @Test
    public void testShortValue() {
        UnsignedShort unsignedShort = UnsignedShort.valueOf("1024");
        Assert.assertEquals((short) 1024, unsignedShort.shortValue());
    }

    @Test
    public void testToString() {
        UnsignedShort unsignedShort = UnsignedShort.valueOf(65535);
        Assert.assertEquals("65535", unsignedShort.toString());
    }

    @Test
    public void testValueOfInt01() {
        Assert.assertNotNull(UnsignedShort.valueOf(0));
    }

    @Test
    public void testValueOfInt02() {
        Assert.assertNotNull(UnsignedShort.valueOf(255));
    }

    @Test
    public void testValueOfInt03() {
        Assert.assertNotNull(UnsignedShort.valueOf(65535));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testValueOfIntForIllegalArgumentException01() {
        UnsignedShort.valueOf(-1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testValueOfIntForIllegalArgumentException02() {
        UnsignedShort.valueOf(65536);
    }

    @Test
    public void testValueOfShort01() {
        Assert.assertNotNull(UnsignedShort.valueOf((short) 0));
    }

    @Test
    public void testValueOfShort02() {
        Assert.assertNotNull(UnsignedShort.valueOf((short) 255));
    }

    @Test
    public void testValueOfShort03() {
        Assert.assertNotNull(UnsignedShort.valueOf((short) 65535));
    }

    @Test
    public void testValueOfString01() {
        Assert.assertNotNull(UnsignedShort.valueOf("0"));
    }

    @Test
    public void testValueOfString02() {
        Assert.assertNotNull(UnsignedShort.valueOf("255"));
    }

    @Test
    public void testValueOfString03() {
        Assert.assertNotNull(UnsignedShort.valueOf("65535"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testValueOfStringForIllegalArgumentException01() {
        UnsignedShort.valueOf("0xcafe");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testValueOfStringForIllegalArgumentException02() {
        UnsignedShort.valueOf("-1");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testValueOfStringForIllegalArgumentException03() {
        UnsignedShort.valueOf("65536");
    }

}