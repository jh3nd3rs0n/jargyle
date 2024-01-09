package com.github.jh3nd3rs0n.jargyle.common.net;

import com.github.jh3nd3rs0n.jargyle.common.number.Digit;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class PerformancePreferencesTest {

    @Test
    public void testNewInstanceOfString01() {
        Assert.assertNotNull(PerformancePreferences.newInstanceOf("456"));
    }

    @Test
    public void testNewInstanceOfString02() {
        Digit[] expected = new Digit[]{
                Digit.newInstanceOf(0),
                Digit.newInstanceOf(1),
                Digit.newInstanceOf(2)
        };
        PerformancePreferences p = PerformancePreferences.newInstanceOf("012");
        Digit[] actual = new Digit[]{
                p.getConnectionTimeImportance(),
                p.getLatencyImportance(),
                p.getBandwidthImportance()
        };
        Assert.assertArrayEquals(expected, actual);
    }

    @Test
    public void testNewInstanceDigitDigitDigit() {
        Digit d1 = Digit.newInstanceOf(7);
        Digit d2 = Digit.newInstanceOf(8);
        Digit d3 = Digit.newInstanceOf(9);
        Digit[] expected = new Digit[]{d1, d2, d3};
        PerformancePreferences p = PerformancePreferences.newInstance(
                d1, d2, d3);
        Digit[] actual = new Digit[]{
                p.getConnectionTimeImportance(),
                p.getLatencyImportance(),
                p.getBandwidthImportance()
        };
        Assert.assertArrayEquals(expected, actual);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNewInstanceOfStringForIllegalArgumentException() {
        PerformancePreferences.newInstanceOf("1st 2nd 3rd");
    }

    @Test
    public void testNewInstancePerformancePreferences() {
        PerformancePreferences p1 = PerformancePreferences.newInstanceOf("345");
        PerformancePreferences p2 = PerformancePreferences.newInstance(p1);
        Assert.assertEquals(p1, p2);
    }

    @Test
    public void testApplyToServerSocket() throws IOException {
        PerformancePreferences.newInstanceOf("179").applyTo(new ServerSocket());
    }

    @Test
    public void testApplyToSocket() {
        PerformancePreferences.newInstanceOf("492").applyTo(new Socket());
    }

    @Test
    public void testEqualsObject01() {
        PerformancePreferences p = PerformancePreferences.newInstanceOf("012");
        Assert.assertEquals(p, p);
    }

    @Test
    public void testEqualsObject02() {
        PerformancePreferences p = PerformancePreferences.newInstanceOf("012");
        Assert.assertNotEquals(p, null);
    }

    @Test
    public void testEqualsObject03() {
        Object obj1 = PerformancePreferences.newInstanceOf("012");
        Object obj2 = new Object();
        Assert.assertNotEquals(obj1, obj2);
    }

    @Test
    public void testEqualsObject04() {
        PerformancePreferences p1 = PerformancePreferences.newInstanceOf("012");
        PerformancePreferences p2 = PerformancePreferences.newInstanceOf("112");
        Assert.assertNotEquals(p1, p2);
    }

    @Test
    public void testEqualsObject05() {
        PerformancePreferences p1 = PerformancePreferences.newInstanceOf("012");
        PerformancePreferences p2 = PerformancePreferences.newInstanceOf("022");
        Assert.assertNotEquals(p1, p2);
    }

    @Test
    public void testEqualsObject06() {
        PerformancePreferences p1 = PerformancePreferences.newInstanceOf("012");
        PerformancePreferences p2 = PerformancePreferences.newInstanceOf("013");
        Assert.assertNotEquals(p1, p2);
    }

    @Test
    public void testEqualsObject07() {
        PerformancePreferences p1 = PerformancePreferences.newInstanceOf("345");
        PerformancePreferences p2 = PerformancePreferences.newInstanceOf("345");
        Assert.assertEquals(p1, p2);
    }

    @Test
    public void testHashCode01() {
        PerformancePreferences p1 = PerformancePreferences.newInstanceOf("345");
        PerformancePreferences p2 = PerformancePreferences.newInstanceOf("345");
        Assert.assertEquals(p1.hashCode(), p2.hashCode());
    }

    @Test
    public void testHashCode02() {
        PerformancePreferences p1 = PerformancePreferences.newInstanceOf("012");
        PerformancePreferences p2 = PerformancePreferences.newInstanceOf("789");
        Assert.assertNotEquals(p1.hashCode(), p2.hashCode());
    }

    @Test
    public void testToString() {
        PerformancePreferences p = PerformancePreferences.newInstance(
                Digit.newInstanceOf(0),
                Digit.newInstanceOf(1),
                Digit.newInstanceOf(2));
        Assert.assertEquals("012", p.toString());
    }

}