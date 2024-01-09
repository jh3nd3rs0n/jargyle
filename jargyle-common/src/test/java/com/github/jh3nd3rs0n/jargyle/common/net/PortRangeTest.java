package com.github.jh3nd3rs0n.jargyle.common.net;

import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

public class PortRangeTest {

    @Test
    public void testNewInstancePort() {
        Assert.assertNotNull(PortRange.newInstance(Port.newInstanceOf(2456)));
    }

    @Test
    public void testNewInstancePortPort() {
        Assert.assertNotNull(PortRange.newInstance(
                Port.newInstanceOf(1234), Port.newInstanceOf(5678)));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNewInstancePortPortForIllegalArgumentException() {
        PortRange.newInstance(
                Port.newInstanceOf(8765), Port.newInstanceOf(4321));
    }

    @Test
    public void testNewInstanceOfString01() {
        Assert.assertNotNull(PortRange.newInstanceOf("4567"));
    }

    @Test
    public void testNewInstanceOfString02() {
        Assert.assertNotNull(PortRange.newInstanceOf("3623-4983"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNewInstanceOfStringForIllegalArgumentException01() {
        PortRange.newInstanceOf("6785 to 9231");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNewInstanceOfStringForIllegalArgumentException02() {
        PortRange.newInstanceOf("9231-6785");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNewInstanceOfStringForIllegalArgumentException03() {
        PortRange.newInstanceOf("6785-9231-10200");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNewInstanceOfStringForIllegalArgumentException04() {
        PortRange.newInstanceOf("6785-end");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNewInstanceOfStringForIllegalArgumentException05() {
        PortRange.newInstanceOf("beginning-10200");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNewInstanceOfStringForIllegalArgumentException06() {
        PortRange.newInstanceOf("-");
    }

    @Test
    public void testHasPort01() {
        PortRange portRange = PortRange.newInstance(
                Port.newInstanceOf(Port.MIN_INT_VALUE),
                Port.newInstanceOf(Port.MAX_INT_VALUE));
        Assert.assertTrue(portRange.has(Port.newInstanceOf(Port.MIN_INT_VALUE)));
    }

    @Test
    public void testHasPort02() {
        PortRange portRange = PortRange.newInstance(
                Port.newInstanceOf(Port.MIN_INT_VALUE),
                Port.newInstanceOf(Port.MAX_INT_VALUE));
        Assert.assertTrue(portRange.has(Port.newInstanceOf(Port.MAX_INT_VALUE)));
    }

    @Test
    public void testHasPort03() {
        PortRange portRange = PortRange.newInstance(
                Port.newInstanceOf(Port.MIN_INT_VALUE),
                Port.newInstanceOf(Port.MAX_INT_VALUE));
        Assert.assertTrue(portRange.has(Port.newInstanceOf(1080)));
    }

    @Test
    public void testHasPort04() {
        PortRange portRange = PortRange.newInstance(
                Port.newInstanceOf(Port.MIN_INT_VALUE));
        Assert.assertTrue(portRange.has(Port.newInstanceOf(Port.MIN_INT_VALUE)));
    }

    @Test
    public void testHasPort05() {
        PortRange portRange = PortRange.newInstance(
                Port.newInstanceOf(Port.MAX_INT_VALUE));
        Assert.assertTrue(portRange.has(Port.newInstanceOf(Port.MAX_INT_VALUE)));
    }

    @Test
    public void testHasPort06() {
        PortRange portRange = PortRange.newInstance(Port.newInstanceOf(1080));
        Assert.assertTrue(portRange.has(Port.newInstanceOf(1080)));
    }

    @Test
    public void testHasPort07() {
        PortRange portRange = PortRange.newInstance(
                Port.newInstanceOf(1000), Port.newInstanceOf(2000));
        Assert.assertTrue(portRange.has(Port.newInstanceOf(1000)));
    }

    @Test
    public void testHasPort08() {
        PortRange portRange = PortRange.newInstance(
                Port.newInstanceOf(1000), Port.newInstanceOf(2000));
        Assert.assertTrue(portRange.has(Port.newInstanceOf(2000)));
    }

    @Test
    public void testHasPort09() {
        PortRange portRange = PortRange.newInstance(
                Port.newInstanceOf(1000), Port.newInstanceOf(2000));
        Assert.assertTrue(portRange.has(Port.newInstanceOf(1080)));
    }

    @Test
    public void testHasPort10() {
        PortRange portRange = PortRange.newInstance(
                Port.newInstanceOf(1000), Port.newInstanceOf(2000));
        Assert.assertFalse(portRange.has(Port.newInstanceOf(999)));
    }

    @Test
    public void testHasPort11() {
        PortRange portRange = PortRange.newInstance(
                Port.newInstanceOf(1000), Port.newInstanceOf(2000));
        Assert.assertFalse(portRange.has(Port.newInstanceOf(2001)));
    }

    @Test
    public void testEqualsObject01() {
        PortRange portRange = PortRange.newInstance(Port.newInstanceOf(3456));
        Assert.assertEquals(portRange, portRange);
    }

    @Test
    public void testEqualsObject02() {
        PortRange portRange = PortRange.newInstance(Port.newInstanceOf(4589));
        Assert.assertNotEquals(portRange, null);
    }

    @Test
    public void testEqualsObject03() {
        Object obj1 = PortRange.newInstance(Port.newInstanceOf(4921));
        Object obj2 = new Object();
        Assert.assertNotEquals(obj1, obj2);
    }

    @Test
    public void testEqualsObject04() {
        PortRange portRange1 = PortRange.newInstance(
                Port.newInstanceOf(2345), Port.newInstanceOf(3679));
        PortRange portRange2 = PortRange.newInstance(
                Port.newInstanceOf(1234), Port.newInstanceOf(3784));
        Assert.assertNotEquals(portRange1, portRange2);
    }

    @Test
    public void testEqualsObject05() {
        PortRange portRange1 = PortRange.newInstance(
                Port.newInstanceOf(2345), Port.newInstanceOf(3679));
        PortRange portRange2 = PortRange.newInstance(
                Port.newInstanceOf(2345), Port.newInstanceOf(3784));
        Assert.assertNotEquals(portRange1, portRange2);
    }

    @Test
    public void testEqualsObject06() {
        PortRange portRange1 = PortRange.newInstance(Port.newInstanceOf(2345));
        PortRange portRange2 = PortRange.newInstance(Port.newInstanceOf(2345));
        Assert.assertEquals(portRange1, portRange2);
    }

    @Test
    public void testEqualsObject07() {
        PortRange portRange1 = PortRange.newInstance(
                Port.newInstanceOf(2345), Port.newInstanceOf(3679));
        PortRange portRange2 = PortRange.newInstance(
                Port.newInstanceOf(2345), Port.newInstanceOf(3679));
        Assert.assertEquals(portRange1, portRange2);
    }

    @Test
    public void testHashCode01() {
        PortRange portRange1 = PortRange.newInstance(
                Port.newInstanceOf(9999), Port.newInstanceOf(10000));
        PortRange portRange2 = PortRange.newInstance(
                Port.newInstanceOf(9999), Port.newInstanceOf(10000));
        Assert.assertEquals(portRange1.hashCode(), portRange2.hashCode());
    }

    @Test
    public void testHashCode02() {
        PortRange portRange1 = PortRange.newInstance(
                Port.newInstanceOf(9999), Port.newInstanceOf(10000));
        PortRange portRange2 = PortRange.newInstance(
                Port.newInstanceOf(9999), Port.newInstanceOf(10001));
        Assert.assertNotEquals(portRange1.hashCode(), portRange2.hashCode());
    }

    @Test
    public void testIterator01() {
        Iterator<Port> iterator = PortRange.newInstance(
                Port.newInstanceOf(9103)).iterator();
        Assert.assertTrue(iterator.hasNext());
    }

    @Test
    public void testIterator02() {
        Iterator<Port> iterator = PortRange.newInstance(
                Port.newInstanceOf(9103)).iterator();
        iterator.next();
        Assert.assertFalse(iterator.hasNext());
    }

    @Test
    public void testIterator03() {
        Iterator<Port> iterator = PortRange.newInstance(
                Port.newInstanceOf(9103)).iterator();
        Port port = iterator.next();
        Assert.assertEquals(Port.newInstanceOf(9103), port);
    }

    @Test
    public void testIterator04() {
        Iterator<Port> iterator = PortRange.newInstance(
                Port.newInstanceOf(9103), Port.newInstanceOf(9106)).iterator();
        List<Port> expected = new ArrayList<>(List.of(
                Port.newInstanceOf(9103),
                Port.newInstanceOf(9104),
                Port.newInstanceOf(9105),
                Port.newInstanceOf(9106)
        ));
        List<Port> actual = new ArrayList<>();
        while (iterator.hasNext()) {
            actual.add(iterator.next());
        }
        Assert.assertEquals(expected, actual);
    }

    @Test(expected = NoSuchElementException.class)
    public void testIteratorForNoSuchElementException() {
        Iterator<Port> iterator = PortRange.newInstance(
                Port.newInstanceOf(9103)).iterator();
        iterator.next();
        iterator.next();
    }

    @Test
    public void testToString01() {
        PortRange portRange = PortRange.newInstance(Port.newInstanceOf(7924));
        Assert.assertEquals("7924", portRange.toString());
    }

    @Test
    public void testToString02() {
        PortRange portRange = PortRange.newInstance(
                Port.newInstanceOf(5432), Port.newInstanceOf(8769));
        Assert.assertEquals("5432-8769", portRange.toString());
    }

}
