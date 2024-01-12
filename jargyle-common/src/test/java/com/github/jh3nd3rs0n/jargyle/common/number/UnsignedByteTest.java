package com.github.jh3nd3rs0n.jargyle.common.number;

import org.junit.Assert;
import org.junit.Test;

public class UnsignedByteTest {

    @Test
    public void testByteValue01() {
        UnsignedByte unsignedByte = UnsignedByte.valueOf("0");
        Assert.assertEquals((byte) 0, unsignedByte.byteValue());
    }

    @Test
    public void testByteValue02() {
        UnsignedByte unsignedByte = UnsignedByte.valueOf("127");
        Assert.assertEquals((byte) 127, unsignedByte.byteValue());
    }

    @Test
    public void testByteValue03() {
        UnsignedByte unsignedByte = UnsignedByte.valueOf("255");
        Assert.assertEquals((byte) 255, unsignedByte.byteValue());
    }

    @Test
    public void testEqualsObject01() {
        UnsignedByte unsignedByte = UnsignedByte.valueOf(127);
        Assert.assertEquals(unsignedByte, unsignedByte);
    }

    @Test
    public void testEqualsObject02() {
        UnsignedByte unsignedByte = UnsignedByte.valueOf(127);
        Assert.assertNotEquals(unsignedByte, null);
    }

    @Test
    public void testEqualsObject03() {
        Object obj1 = UnsignedByte.valueOf(127);
        Object obj2 = new Object();
        Assert.assertNotEquals(obj1, obj2);
    }

    @Test
    public void testEqualsObject04() {
        Assert.assertNotEquals(
                UnsignedByte.valueOf(127),
                UnsignedByte.valueOf(128));
    }

    @Test
    public void testEqualsObject05() {
        Assert.assertEquals(
                UnsignedByte.valueOf(127),
                UnsignedByte.valueOf(127));
    }

    @Test
    public void testHashCode01() {
        Assert.assertEquals(
                UnsignedByte.valueOf(127).hashCode(),
                UnsignedByte.valueOf(127).hashCode());
    }

    @Test
    public void testHashCode02() {
        Assert.assertNotEquals(
                UnsignedByte.valueOf(127).hashCode(),
                UnsignedByte.valueOf(128).hashCode());
    }

    @Test
    public void testIntValue() {
        UnsignedByte unsignedByte = UnsignedByte.valueOf("127");
        Assert.assertEquals(127, unsignedByte.intValue());
    }

    @Test
    public void testToString() {
        UnsignedByte unsignedByte = UnsignedByte.valueOf(127);
        Assert.assertEquals("127", unsignedByte.toString());
    }

    @Test
    public void testValueOfByte01() {
        Assert.assertNotNull(UnsignedByte.valueOf((byte) 0));
    }

    @Test
    public void testValueOfByte02() {
        Assert.assertNotNull(UnsignedByte.valueOf((byte) 145));
    }

    @Test
    public void testValueOfByte03() {
        Assert.assertNotNull(UnsignedByte.valueOf((byte) 255));
    }

    @Test
    public void testValueOfInt01() {
        Assert.assertNotNull(UnsignedByte.valueOf(0));
    }

    @Test
    public void testValueOfInt02() {
        Assert.assertNotNull(UnsignedByte.valueOf(145));
    }

    @Test
    public void testValueOfInt03() {
        Assert.assertNotNull(UnsignedByte.valueOf(255));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testValueOfIntForIllegalArgumentException01() {
        UnsignedByte.valueOf(-1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testValueOfIntForIllegalArgumentException02() {
        UnsignedByte.valueOf(256);
    }

    @Test
    public void testValueOfString01() {
        Assert.assertNotNull(UnsignedByte.valueOf("0"));
    }

    @Test
    public void testValueOfString02() {
        Assert.assertNotNull(UnsignedByte.valueOf("172"));
    }

    @Test
    public void testValueOfString03() {
        Assert.assertNotNull(UnsignedByte.valueOf("255"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testValueOfStringForIllegalArgumentException01() {
        UnsignedByte.valueOf("0xaf");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testValueOfStringForIllegalArgumentException02() {
        UnsignedByte.valueOf("-1");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testValueOfStringForIllegalArgumentException03() {
        UnsignedByte.valueOf("256");
    }

}