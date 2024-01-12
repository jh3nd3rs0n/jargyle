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
    public void testToByteArray01() {
        UnsignedShort unsignedShort = UnsignedShort.valueOf(0);
        Assert.assertArrayEquals(
                new byte[]{0x00, 0x00}, unsignedShort.toByteArray());
    }

    @Test
    public void testToByteArray02() {
        UnsignedShort unsignedShort = UnsignedShort.valueOf(255);
        Assert.assertArrayEquals(
                new byte[]{0x00, (byte) 0xff}, unsignedShort.toByteArray());
    }

    @Test
    public void testToByteArray03() {
        UnsignedShort unsignedShort = UnsignedShort.valueOf(65535);
        Assert.assertArrayEquals(
                new byte[]{(byte) 0xff, (byte) 0xff},
                unsignedShort.toByteArray());
    }

    @Test
    public void testToString() {
        UnsignedShort unsignedShort = UnsignedShort.valueOf(65535);
        Assert.assertEquals("65535", unsignedShort.toString());
    }

    @Test
    public void testValueOfByteArray01() {
        Assert.assertNotNull(UnsignedShort.valueOf(
                new byte[]{0x00, 0x00}));
    }

    @Test
    public void testValueOfByteArray02() {
        Assert.assertNotNull(UnsignedShort.valueOf(
                new byte[]{0x00, (byte) 0xff}));
    }

    @Test
    public void testValueOfByteArray03() {
        Assert.assertNotNull(UnsignedShort.valueOf(
                new byte[]{(byte) 0xff, (byte) 0xff}));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testValueOfByteArrayForIllegalArgumentException01() {
        UnsignedShort.valueOf(new byte[]{0x00, 0x00, 0x00});
    }

    @Test(expected = IllegalArgumentException.class)
    public void testValueOfByteArrayForIllegalArgumentException02() {
        UnsignedShort.valueOf(new byte[]{0x00});
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