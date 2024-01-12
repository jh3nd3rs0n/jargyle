package com.github.jh3nd3rs0n.jargyle.common.net;

import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

public class PortRangeTest {

    @Test
    public void testEqualsObject01() {
        PortRange portRange = PortRange.of(Port.valueOf(3456));
        Assert.assertEquals(portRange, portRange);
    }

    @Test
    public void testEqualsObject02() {
        PortRange portRange = PortRange.of(Port.valueOf(4589));
        Assert.assertNotEquals(portRange, null);
    }

    @Test
    public void testEqualsObject03() {
        Object obj1 = PortRange.of(Port.valueOf(4921));
        Object obj2 = new Object();
        Assert.assertNotEquals(obj1, obj2);
    }

    @Test
    public void testEqualsObject04() {
        PortRange portRange1 = PortRange.of(
                Port.valueOf(2345), Port.valueOf(3679));
        PortRange portRange2 = PortRange.of(
                Port.valueOf(1234), Port.valueOf(3784));
        Assert.assertNotEquals(portRange1, portRange2);
    }

    @Test
    public void testEqualsObject05() {
        PortRange portRange1 = PortRange.of(
                Port.valueOf(2345), Port.valueOf(3679));
        PortRange portRange2 = PortRange.of(
                Port.valueOf(2345), Port.valueOf(3784));
        Assert.assertNotEquals(portRange1, portRange2);
    }

    @Test
    public void testEqualsObject06() {
        PortRange portRange1 = PortRange.of(Port.valueOf(2345));
        PortRange portRange2 = PortRange.of(Port.valueOf(2345));
        Assert.assertEquals(portRange1, portRange2);
    }

    @Test
    public void testEqualsObject07() {
        PortRange portRange1 = PortRange.of(
                Port.valueOf(2345), Port.valueOf(3679));
        PortRange portRange2 = PortRange.of(
                Port.valueOf(2345), Port.valueOf(3679));
        Assert.assertEquals(portRange1, portRange2);
    }

    @Test
    public void testHashCode01() {
        PortRange portRange1 = PortRange.of(
                Port.valueOf(9999), Port.valueOf(10000));
        PortRange portRange2 = PortRange.of(
                Port.valueOf(9999), Port.valueOf(10000));
        Assert.assertEquals(portRange1.hashCode(), portRange2.hashCode());
    }

    @Test
    public void testHashCode02() {
        PortRange portRange1 = PortRange.of(
                Port.valueOf(9999), Port.valueOf(10000));
        PortRange portRange2 = PortRange.of(
                Port.valueOf(9999), Port.valueOf(10001));
        Assert.assertNotEquals(portRange1.hashCode(), portRange2.hashCode());
    }

    @Test
    public void testHasPort01() {
        PortRange portRange = PortRange.of(
                Port.valueOf(Port.MIN_INT_VALUE),
                Port.valueOf(Port.MAX_INT_VALUE));
        Assert.assertTrue(portRange.has(Port.valueOf(Port.MIN_INT_VALUE)));
    }

    @Test
    public void testHasPort02() {
        PortRange portRange = PortRange.of(
                Port.valueOf(Port.MIN_INT_VALUE),
                Port.valueOf(Port.MAX_INT_VALUE));
        Assert.assertTrue(portRange.has(Port.valueOf(Port.MAX_INT_VALUE)));
    }

    @Test
    public void testHasPort03() {
        PortRange portRange = PortRange.of(
                Port.valueOf(Port.MIN_INT_VALUE),
                Port.valueOf(Port.MAX_INT_VALUE));
        Assert.assertTrue(portRange.has(Port.valueOf(1080)));
    }

    @Test
    public void testHasPort04() {
        PortRange portRange = PortRange.of(
                Port.valueOf(Port.MIN_INT_VALUE));
        Assert.assertTrue(portRange.has(Port.valueOf(Port.MIN_INT_VALUE)));
    }

    @Test
    public void testHasPort05() {
        PortRange portRange = PortRange.of(
                Port.valueOf(Port.MAX_INT_VALUE));
        Assert.assertTrue(portRange.has(Port.valueOf(Port.MAX_INT_VALUE)));
    }

    @Test
    public void testHasPort06() {
        PortRange portRange = PortRange.of(Port.valueOf(1080));
        Assert.assertTrue(portRange.has(Port.valueOf(1080)));
    }

    @Test
    public void testHasPort07() {
        PortRange portRange = PortRange.of(
                Port.valueOf(1000), Port.valueOf(2000));
        Assert.assertTrue(portRange.has(Port.valueOf(1000)));
    }

    @Test
    public void testHasPort08() {
        PortRange portRange = PortRange.of(
                Port.valueOf(1000), Port.valueOf(2000));
        Assert.assertTrue(portRange.has(Port.valueOf(2000)));
    }

    @Test
    public void testHasPort09() {
        PortRange portRange = PortRange.of(
                Port.valueOf(1000), Port.valueOf(2000));
        Assert.assertTrue(portRange.has(Port.valueOf(1080)));
    }

    @Test
    public void testHasPort10() {
        PortRange portRange = PortRange.of(
                Port.valueOf(1000), Port.valueOf(2000));
        Assert.assertFalse(portRange.has(Port.valueOf(999)));
    }

    @Test
    public void testHasPort11() {
        PortRange portRange = PortRange.of(
                Port.valueOf(1000), Port.valueOf(2000));
        Assert.assertFalse(portRange.has(Port.valueOf(2001)));
    }

    @Test
    public void testIterator01() {
        Iterator<Port> iterator = PortRange.of(
                Port.valueOf(9103)).iterator();
        Assert.assertTrue(iterator.hasNext());
    }

    @Test
    public void testIterator02() {
        Iterator<Port> iterator = PortRange.of(
                Port.valueOf(9103)).iterator();
        iterator.next();
        Assert.assertFalse(iterator.hasNext());
    }

    @Test
    public void testIterator03() {
        Iterator<Port> iterator = PortRange.of(
                Port.valueOf(9103)).iterator();
        Port port = iterator.next();
        Assert.assertEquals(Port.valueOf(9103), port);
    }

    @Test
    public void testIterator04() {
        Iterator<Port> iterator = PortRange.of(
                Port.valueOf(9103), Port.valueOf(9106)).iterator();
        List<Port> expected = new ArrayList<>(List.of(
                Port.valueOf(9103),
                Port.valueOf(9104),
                Port.valueOf(9105),
                Port.valueOf(9106)
        ));
        List<Port> actual = new ArrayList<>();
        while (iterator.hasNext()) {
            actual.add(iterator.next());
        }
        Assert.assertEquals(expected, actual);
    }

    @Test(expected = NoSuchElementException.class)
    public void testIteratorForNoSuchElementException() {
        Iterator<Port> iterator = PortRange.of(
                Port.valueOf(9103)).iterator();
        iterator.next();
        iterator.next();
    }

    @Test
    public void testNewInstanceFromString01() {
        Assert.assertNotNull(PortRange.newInstanceFrom("4567"));
    }

    @Test
    public void testNewInstanceFromString02() {
        Assert.assertNotNull(PortRange.newInstanceFrom("3623-4983"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNewInstanceFromStringForIllegalArgumentException01() {
        PortRange.newInstanceFrom("6785 to 9231");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNewInstanceFromStringForIllegalArgumentException02() {
        PortRange.newInstanceFrom("9231-6785");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNewInstanceFromStringForIllegalArgumentException03() {
        PortRange.newInstanceFrom("6785-9231-10200");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNewInstanceFromStringForIllegalArgumentException04() {
        PortRange.newInstanceFrom("6785-end");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNewInstanceFromStringForIllegalArgumentException05() {
        PortRange.newInstanceFrom("beginning-10200");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNewInstanceFromStringForIllegalArgumentException06() {
        PortRange.newInstanceFrom("-");
    }


    @Test
    public void testOfPort() {
        Assert.assertNotNull(PortRange.of(Port.valueOf(2456)));
    }

    @Test
    public void testOfPortPort() {
        Assert.assertNotNull(PortRange.of(
                Port.valueOf(1234), Port.valueOf(5678)));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testOfPortPortForIllegalArgumentException() {
        PortRange.of(
                Port.valueOf(8765), Port.valueOf(4321));
    }

    @Test
    public void testToString01() {
        PortRange portRange = PortRange.of(Port.valueOf(7924));
        Assert.assertEquals("7924", portRange.toString());
    }

    @Test
    public void testToString02() {
        PortRange portRange = PortRange.of(
                Port.valueOf(5432), Port.valueOf(8769));
        Assert.assertEquals("5432-8769", portRange.toString());
    }

}
