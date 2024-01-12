package com.github.jh3nd3rs0n.jargyle.common.net;

import com.github.jh3nd3rs0n.jargyle.common.number.UnsignedShort;
import org.junit.Assert;
import org.junit.Test;

public class PortTest {

    @Test
    public void testCompareToPort01() {
        Port port1 = Port.valueOf(12);
        Port port2 = Port.valueOf(36);
        Assert.assertTrue(port1.compareTo(port2) < 0);
    }

    @Test
    public void testCompareToPort02() {
        Port port1 = Port.valueOf(27);
        Port port2 = Port.valueOf(14);
        Assert.assertTrue(port1.compareTo(port2) > 0);
    }

    @Test
    public void testCompareToPort03() {
        Port port1 = Port.valueOf(500);
        Port port2 = Port.valueOf(500);
        Assert.assertEquals(0, port1.compareTo(port2));
    }

    @Test
    public void testEqualsObject01() {
        Port port = Port.valueOf(700);
        Assert.assertEquals(port, port);
    }

    @Test
    public void testEqualsObject02() {
        Port port = Port.valueOf(404);
        Assert.assertNotEquals(port, null);
    }

    @Test
    public void testEqualsObject03() {
        Object obj1 = Port.valueOf(2500);
        Object obj2 = new Object();
        Assert.assertNotEquals(obj1, obj2);
    }

    @Test
    public void testEqualsObject04() {
        Port port1 = Port.valueOf(900);
        Port port2 = Port.valueOf(2500);
        Assert.assertNotEquals(port1, port2);
    }

    @Test
    public void testEqualsObject05() {
        Port port1 = Port.valueOf(1000);
        Port port2 = Port.valueOf(1000);
        Assert.assertEquals(port1, port2);
    }

    @Test
    public void testHashCode01() {
        Port port1 = Port.valueOf(1900);
        Port port2 = Port.valueOf(1900);
        Assert.assertEquals(port1.hashCode(), port2.hashCode());
    }

    @Test
    public void testHashCode02() {
        Port port1 = Port.valueOf(4000);
        Port port2 = Port.valueOf(3000);
        Assert.assertNotEquals(port1.hashCode(), port2.hashCode());
    }

    @Test
    public void testIntValue() {
        Port port = Port.valueOf("101");
        Assert.assertEquals(101, port.intValue());
    }

    @Test
    public void testToString() {
        Port port = Port.valueOf(8180);
        Assert.assertEquals("8180", port.toString());
    }

    @Test
    public void testUnsignedShortValue() {
        Port port = Port.valueOf(2000);
        Assert.assertEquals(
                UnsignedShort.valueOf(2000), port.unsignedShortValue());
    }

    @Test
    public void testValueOfInt() {
        Assert.assertNotNull(Port.valueOf(31));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testValueOfIntForIllegalArgumentException01() {
        Port.valueOf(-1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testValueOfIntForIllegalArgumentException02() {
        Port.valueOf(65536);
    }

    @Test
    public void testValueOfString() {
        Assert.assertNotNull(Port.valueOf("42"));
    }


    @Test(expected = IllegalArgumentException.class)
    public void testValueOfStringForIllegalArgumentException() {
        Port.valueOf("fifty-six");
    }

    @Test
    public void testValueOfUnsignedShort() {
        Assert.assertNotNull(Port.valueOf(UnsignedShort.valueOf(97)));
    }

}