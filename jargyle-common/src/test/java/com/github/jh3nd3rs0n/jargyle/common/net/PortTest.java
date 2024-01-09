package com.github.jh3nd3rs0n.jargyle.common.net;

import com.github.jh3nd3rs0n.jargyle.common.number.UnsignedShort;
import org.junit.Assert;
import org.junit.Test;

public class PortTest {

    @Test
    public void testNewInstanceInt() {
        Assert.assertNotNull(Port.newInstanceOf(31));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNewInstanceIntForIllegalArgumentException01() {
        Port.newInstanceOf(-1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNewInstanceIntForIllegalArgumentException02() {
        Port.newInstanceOf(65536);
    }

    @Test
    public void testNewInstanceOfString() {
        Assert.assertNotNull(Port.newInstanceOf("42"));
    }


    @Test(expected = IllegalArgumentException.class)
    public void testNewInstanceOfStringForIllegalArgumentException() {
        Port.newInstanceOf("fifty-six");
    }

    @Test
    public void testNewInstanceUnsignedShort() {
        Assert.assertNotNull(Port.newInstanceOf(UnsignedShort.newInstanceOf(97)));
    }

    @Test
    public void testCompareToPort01() {
        Port port1 = Port.newInstanceOf(12);
        Port port2 = Port.newInstanceOf(36);
        Assert.assertTrue(port1.compareTo(port2) < 0);
    }

    @Test
    public void testCompareToPort02() {
        Port port1 = Port.newInstanceOf(27);
        Port port2 = Port.newInstanceOf(14);
        Assert.assertTrue(port1.compareTo(port2) > 0);
    }

    @Test
    public void testCompareToPort03() {
        Port port1 = Port.newInstanceOf(500);
        Port port2 = Port.newInstanceOf(500);
        Assert.assertEquals(0, port1.compareTo(port2));
    }

    @Test
    public void testEqualsObject01() {
        Port port = Port.newInstanceOf(700);
        Assert.assertEquals(port, port);
    }

    @Test
    public void testEqualsObject02() {
        Port port = Port.newInstanceOf(404);
        Assert.assertNotEquals(port, null);
    }

    @Test
    public void testEqualsObject03() {
        Object obj1 = Port.newInstanceOf(2500);
        Object obj2 = new Object();
        Assert.assertNotEquals(obj1, obj2);
    }

    @Test
    public void testEqualsObject04() {
        Port port1 = Port.newInstanceOf(900);
        Port port2 = Port.newInstanceOf(2500);
        Assert.assertNotEquals(port1, port2);
    }

    @Test
    public void testEqualsObject05() {
        Port port1 = Port.newInstanceOf(1000);
        Port port2 = Port.newInstanceOf(1000);
        Assert.assertEquals(port1, port2);
    }

    @Test
    public void testHashCode01() {
        Port port1 = Port.newInstanceOf(1900);
        Port port2 = Port.newInstanceOf(1900);
        Assert.assertEquals(port1.hashCode(), port2.hashCode());
    }

    @Test
    public void testHashCode02() {
        Port port1 = Port.newInstanceOf(4000);
        Port port2 = Port.newInstanceOf(3000);
        Assert.assertNotEquals(port1.hashCode(), port2.hashCode());
    }

    @Test
    public void intValue() {
        Port port = Port.newInstanceOf("101");
        Assert.assertEquals(101, port.intValue());
    }

    @Test
    public void testToString() {
        Port port = Port.newInstanceOf(8180);
        Assert.assertEquals("8180", port.toString());
    }

    @Test
    public void toUnsignedShort() {
        Port port = Port.newInstanceOf(2000);
        Assert.assertEquals(
                UnsignedShort.newInstanceOf(2000), port.toUnsignedShort());
    }

}