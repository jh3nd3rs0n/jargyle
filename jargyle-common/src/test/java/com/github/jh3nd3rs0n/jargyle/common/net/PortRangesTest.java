package com.github.jh3nd3rs0n.jargyle.common.net;

import org.junit.Assert;
import org.junit.Test;

public class PortRangesTest {

    @Test
    public void testNewInstancePortRangeVarargs01() {
        Assert.assertNotNull(PortRanges.newInstance());
    }

    @Test
    public void testNewInstancePortRangeVarargs02() {
        Assert.assertNotNull(PortRanges.newInstance(PortRange.newInstance(
                Port.newInstanceOf(8080))));
    }

    @Test
    public void testNewInstancePortRangeVarargs03() {
        Assert.assertNotNull(PortRanges.newInstance(
                PortRange.newInstance(
                        Port.newInstanceOf(4000), Port.newInstanceOf(5000)),
                PortRange.newInstance(Port.newInstanceOf(8000))));
    }

    @Test
    public void testNewInstanceOfString01() {
        Assert.assertNotNull(PortRanges.newInstanceOf(""));
    }

    @Test
    public void testNewInstanceOfString02() {
        Assert.assertNotNull(PortRanges.newInstanceOf("8000"));
    }

    @Test
    public void testNewInstanceOfString03() {
        Assert.assertNotNull(PortRanges.newInstanceOf("4000-5000,8000"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNewInstanceOfStringForIllegalArgumentException01() {
        PortRanges.newInstanceOf("4000-5000-8000");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNewInstanceOfStringForIllegalArgumentException02() {
        PortRanges.newInstanceOf("all ports");
    }

    @Test
    public void testHasPort01() {
        PortRanges portRanges = PortRanges.newInstance(
                PortRange.newInstance(
                        Port.newInstanceOf(4000), Port.newInstanceOf(5000)),
                PortRange.newInstance(Port.newInstanceOf(8000)));
        Assert.assertTrue(portRanges.has(Port.newInstanceOf(8000)));
    }

    @Test
    public void testHasPort02() {
        PortRanges portRanges = PortRanges.newInstance(
                PortRange.newInstance(
                        Port.newInstanceOf(4000), Port.newInstanceOf(5000)),
                PortRange.newInstance(Port.newInstanceOf(8000)));
        Assert.assertTrue(portRanges.has(Port.newInstanceOf(4500)));
    }

    @Test
    public void testHasPort03() {
        PortRanges portRanges = PortRanges.newInstance(
                PortRange.newInstance(
                        Port.newInstanceOf(4000), Port.newInstanceOf(5000)),
                PortRange.newInstance(Port.newInstanceOf(8000)));
        Assert.assertTrue(portRanges.has(Port.newInstanceOf(4000)));
    }

    @Test
    public void testHasPort04() {
        PortRanges portRanges = PortRanges.newInstance(
                PortRange.newInstance(
                        Port.newInstanceOf(4000), Port.newInstanceOf(5000)),
                PortRange.newInstance(Port.newInstanceOf(8000)));
        Assert.assertTrue(portRanges.has(Port.newInstanceOf(5000)));
    }

    @Test
    public void testHasPort05() {
        PortRanges portRanges = PortRanges.newInstance(
                PortRange.newInstance(
                        Port.newInstanceOf(4000), Port.newInstanceOf(5000)),
                PortRange.newInstance(Port.newInstanceOf(8000)));
        Assert.assertFalse(portRanges.has(Port.newInstanceOf(3999)));
    }

    @Test
    public void testHasPort06() {
        PortRanges portRanges = PortRanges.newInstance(
                PortRange.newInstance(
                        Port.newInstanceOf(4000), Port.newInstanceOf(5000)),
                PortRange.newInstance(Port.newInstanceOf(8000)));
        Assert.assertFalse(portRanges.has(Port.newInstanceOf(5001)));
    }

    @Test
    public void testEqualsObject01() {
        PortRanges portRanges = PortRanges.newInstance(
                PortRange.newInstance(
                        Port.newInstanceOf(4000), Port.newInstanceOf(5000)),
                PortRange.newInstance(Port.newInstanceOf(8000)));
        Assert.assertEquals(portRanges, portRanges);
    }

    @Test
    public void testEqualsObject02() {
        PortRanges portRanges = PortRanges.newInstance(
                PortRange.newInstance(
                        Port.newInstanceOf(4000), Port.newInstanceOf(5000)),
                PortRange.newInstance(Port.newInstanceOf(8000)));
        Assert.assertNotEquals(portRanges, null);
    }

    @Test
    public void testEqualsObject03() {
        Object obj1 = PortRanges.newInstance(
                PortRange.newInstance(
                        Port.newInstanceOf(4000), Port.newInstanceOf(5000)),
                PortRange.newInstance(Port.newInstanceOf(8000)));
        Object obj2 = new Object();
        Assert.assertNotEquals(obj1, obj2);
    }

    @Test
    public void testEqualsObject04() {
        PortRanges portRanges1 = PortRanges.newInstance(
                PortRange.newInstance(
                        Port.newInstanceOf(4000), Port.newInstanceOf(5000)),
                PortRange.newInstance(Port.newInstanceOf(8000)));
        PortRanges portRanges2 = PortRanges.newInstance(
                PortRange.newInstance(Port.newInstanceOf(4000)),
                PortRange.newInstance(
                        Port.newInstanceOf(5000), Port.newInstanceOf(8000)));
        Assert.assertNotEquals(portRanges1, portRanges2);
    }

    @Test
    public void testEqualsObject05() {
        PortRanges portRanges1 = PortRanges.newInstance(
                PortRange.newInstance(
                        Port.newInstanceOf(4000), Port.newInstanceOf(5000)),
                PortRange.newInstance(Port.newInstanceOf(8000)));
        PortRanges portRanges2 = PortRanges.newInstance(
                PortRange.newInstance(
                        Port.newInstanceOf(4000), Port.newInstanceOf(5000)),
                PortRange.newInstance(Port.newInstanceOf(8000)));
        Assert.assertEquals(portRanges1, portRanges2);
    }

    @Test
    public void testHashCode01() {
        PortRanges portRanges1 = PortRanges.newInstance(
                PortRange.newInstance(
                        Port.newInstanceOf(4000), Port.newInstanceOf(5000)),
                PortRange.newInstance(Port.newInstanceOf(8000)));
        PortRanges portRanges2 = PortRanges.newInstance(
                PortRange.newInstance(
                        Port.newInstanceOf(4000), Port.newInstanceOf(5000)),
                PortRange.newInstance(Port.newInstanceOf(8000)));
        Assert.assertEquals(portRanges1.hashCode(), portRanges2.hashCode());
    }

    @Test
    public void testHashCode02() {
        PortRanges portRanges1 = PortRanges.newInstance(
                PortRange.newInstance(
                        Port.newInstanceOf(4000), Port.newInstanceOf(5000)),
                PortRange.newInstance(Port.newInstanceOf(8000)));
        PortRanges portRanges2 = PortRanges.newInstance(
                PortRange.newInstance(Port.newInstanceOf(4000)),
                PortRange.newInstance(
                        Port.newInstanceOf(5000), Port.newInstanceOf(8000)));
        Assert.assertNotEquals(portRanges1.hashCode(), portRanges2.hashCode());
    }

    @Test
    public void testToString01() {
        PortRanges portRanges = PortRanges.newInstance();
        Assert.assertEquals("", portRanges.toString());
    }

    @Test
    public void testToString02() {
        PortRanges portRanges = PortRanges.newInstance(
                PortRange.newInstance(
                        Port.newInstanceOf(4000), Port.newInstanceOf(5000)),
                PortRange.newInstance(Port.newInstanceOf(8000)));
        Assert.assertEquals("4000-5000,8000", portRanges.toString());
    }

}