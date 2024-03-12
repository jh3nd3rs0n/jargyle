package com.github.jh3nd3rs0n.jargyle.common.net;

import org.junit.Assert;
import org.junit.Test;

public class PortRangesTest {

    @Test
    public void testEqualsObject01() {
        PortRanges portRanges = PortRanges.of(
                PortRange.of(
                        Port.valueOf(4000), Port.valueOf(5000)),
                PortRange.of(Port.valueOf(8000)));
        Assert.assertEquals(portRanges, portRanges);
    }

    @Test
    public void testEqualsObject02() {
        PortRanges portRanges = PortRanges.of(
                PortRange.of(
                        Port.valueOf(4000), Port.valueOf(5000)),
                PortRange.of(Port.valueOf(8000)));
        Assert.assertNotEquals(portRanges, null);
    }

    @Test
    public void testEqualsObject03() {
        Object obj1 = PortRanges.of(
                PortRange.of(
                        Port.valueOf(4000), Port.valueOf(5000)),
                PortRange.of(Port.valueOf(8000)));
        Object obj2 = new Object();
        Assert.assertNotEquals(obj1, obj2);
    }

    @Test
    public void testEqualsObject04() {
        PortRanges portRanges1 = PortRanges.of(
                PortRange.of(
                        Port.valueOf(4000), Port.valueOf(5000)),
                PortRange.of(Port.valueOf(8000)));
        PortRanges portRanges2 = PortRanges.of(
                PortRange.of(Port.valueOf(4000)),
                PortRange.of(
                        Port.valueOf(5000), Port.valueOf(8000)));
        Assert.assertNotEquals(portRanges1, portRanges2);
    }

    @Test
    public void testEqualsObject05() {
        PortRanges portRanges1 = PortRanges.of(
                PortRange.of(
                        Port.valueOf(4000), Port.valueOf(5000)),
                PortRange.of(Port.valueOf(8000)));
        PortRanges portRanges2 = PortRanges.of(
                PortRange.of(
                        Port.valueOf(4000), Port.valueOf(5000)),
                PortRange.of(Port.valueOf(8000)));
        Assert.assertEquals(portRanges1, portRanges2);
    }

    @Test
    public void testHashCode01() {
        PortRanges portRanges1 = PortRanges.of(
                PortRange.of(
                        Port.valueOf(4000), Port.valueOf(5000)),
                PortRange.of(Port.valueOf(8000)));
        PortRanges portRanges2 = PortRanges.of(
                PortRange.of(
                        Port.valueOf(4000), Port.valueOf(5000)),
                PortRange.of(Port.valueOf(8000)));
        Assert.assertEquals(portRanges1.hashCode(), portRanges2.hashCode());
    }

    @Test
    public void testHashCode02() {
        PortRanges portRanges1 = PortRanges.of(
                PortRange.of(
                        Port.valueOf(4000), Port.valueOf(5000)),
                PortRange.of(Port.valueOf(8000)));
        PortRanges portRanges2 = PortRanges.of(
                PortRange.of(Port.valueOf(4000)),
                PortRange.of(
                        Port.valueOf(5000), Port.valueOf(8000)));
        Assert.assertNotEquals(portRanges1.hashCode(), portRanges2.hashCode());
    }

    @Test
    public void testAnyCoversPort01() {
        PortRanges portRanges = PortRanges.of(
                PortRange.of(
                        Port.valueOf(4000), Port.valueOf(5000)),
                PortRange.of(Port.valueOf(8000)));
        Assert.assertTrue(portRanges.anyCovers(Port.valueOf(8000)));
    }

    @Test
    public void testAnyCoversPort02() {
        PortRanges portRanges = PortRanges.of(
                PortRange.of(
                        Port.valueOf(4000), Port.valueOf(5000)),
                PortRange.of(Port.valueOf(8000)));
        Assert.assertTrue(portRanges.anyCovers(Port.valueOf(4500)));
    }

    @Test
    public void testAnyCoversPort03() {
        PortRanges portRanges = PortRanges.of(
                PortRange.of(
                        Port.valueOf(4000), Port.valueOf(5000)),
                PortRange.of(Port.valueOf(8000)));
        Assert.assertTrue(portRanges.anyCovers(Port.valueOf(4000)));
    }

    @Test
    public void testAnyCoversPort04() {
        PortRanges portRanges = PortRanges.of(
                PortRange.of(
                        Port.valueOf(4000), Port.valueOf(5000)),
                PortRange.of(Port.valueOf(8000)));
        Assert.assertTrue(portRanges.anyCovers(Port.valueOf(5000)));
    }

    @Test
    public void testAnyCoversPort05() {
        PortRanges portRanges = PortRanges.of(
                PortRange.of(
                        Port.valueOf(4000), Port.valueOf(5000)),
                PortRange.of(Port.valueOf(8000)));
        Assert.assertFalse(portRanges.anyCovers(Port.valueOf(3999)));
    }

    @Test
    public void testAnyCoversPort06() {
        PortRanges portRanges = PortRanges.of(
                PortRange.of(
                        Port.valueOf(4000), Port.valueOf(5000)),
                PortRange.of(Port.valueOf(8000)));
        Assert.assertFalse(portRanges.anyCovers(Port.valueOf(5001)));
    }

    @Test
    public void testNewInstanceFromString01() {
        Assert.assertNotNull(PortRanges.newInstanceFrom(""));
    }

    @Test
    public void testNewInstanceFromString02() {
        Assert.assertNotNull(PortRanges.newInstanceFrom("8000"));
    }

    @Test
    public void testNewInstanceFromString03() {
        Assert.assertNotNull(PortRanges.newInstanceFrom("4000-5000,8000"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNewInstanceFromStringForIllegalArgumentException01() {
        PortRanges.newInstanceFrom("4000-5000-8000");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNewInstanceFromStringForIllegalArgumentException02() {
        PortRanges.newInstanceFrom("all ports");
    }

    @Test
    public void testOfPortRangeVarargs01() {
        Assert.assertNotNull(PortRanges.of());
    }

    @Test
    public void testOfPortRangeVarargs02() {
        Assert.assertNotNull(PortRanges.of(PortRange.of(
                Port.valueOf(8080))));
    }

    @Test
    public void testOfPortRangeVarargs03() {
        Assert.assertNotNull(PortRanges.of(
                PortRange.of(
                        Port.valueOf(4000), Port.valueOf(5000)),
                PortRange.of(Port.valueOf(8000))));
    }

    @Test
    public void testToString01() {
        PortRanges portRanges = PortRanges.of();
        Assert.assertEquals("", portRanges.toString());
    }

    @Test
    public void testToString02() {
        PortRanges portRanges = PortRanges.of(
                PortRange.of(
                        Port.valueOf(4000), Port.valueOf(5000)),
                PortRange.of(Port.valueOf(8000)));
        Assert.assertEquals("4000-5000,8000", portRanges.toString());
    }

}