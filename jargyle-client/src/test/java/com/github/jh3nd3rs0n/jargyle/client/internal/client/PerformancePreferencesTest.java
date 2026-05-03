package com.github.jh3nd3rs0n.jargyle.client.internal.client;

import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.net.Socket;

public class PerformancePreferencesTest {

    @Test
    public void testApplyToSocket01() throws IOException {
        PerformancePreferences performancePreferences =
                new PerformancePreferences(1, 0, 0);
        try (CustomSocket customSocket = new CustomSocket()) {
            performancePreferences.applyTo(customSocket);
            Assert.assertEquals(
                    performancePreferences,
                    customSocket.getPerformancePreferences());
        }
    }

    @Test
    public void testApplyToSocket02() throws IOException {
        PerformancePreferences performancePreferences =
                new PerformancePreferences(0, 1, 2);
        try (CustomSocket customSocket = new CustomSocket()) {
            performancePreferences.applyTo(customSocket);
            Assert.assertEquals(
                    performancePreferences,
                    customSocket.getPerformancePreferences());
        }
    }

    @Test
    public void testEqualsObject01() {
        PerformancePreferences performancePreferences =
                new PerformancePreferences(0, 0, 0);
        Assert.assertEquals(performancePreferences, performancePreferences);
    }

    @Test
    public void testEqualsObject02() {
        PerformancePreferences performancePreferences =
                new PerformancePreferences(0, 0, 1);
        Assert.assertNotEquals(performancePreferences, null);
    }

    @Test
    public void testEqualsObject03() {
        Object obj1 = new PerformancePreferences(0, 1, 0);
        Object obj2 = new Object();
        Assert.assertNotEquals(obj1, obj2);
    }

    @Test
    public void testEqualsObject04() {
        PerformancePreferences performancePreferences1 =
                new PerformancePreferences(1, 0, 0);
        PerformancePreferences performancePreferences2 =
                new PerformancePreferences(2, 0, 0);
        Assert.assertNotEquals(performancePreferences1, performancePreferences2);
    }

    @Test
    public void testEqualsObject05() {
        PerformancePreferences performancePreferences1 =
                new PerformancePreferences(0, 1, 0);
        PerformancePreferences performancePreferences2 =
                new PerformancePreferences(0, 2, 0);
        Assert.assertNotEquals(performancePreferences1, performancePreferences2);
    }

    @Test
    public void testEqualsObject06() {
        PerformancePreferences performancePreferences1 =
                new PerformancePreferences(0, 0, 1);
        PerformancePreferences performancePreferences2 =
                new PerformancePreferences(0, 0, 2);
        Assert.assertNotEquals(performancePreferences1, performancePreferences2);
    }

    @Test
    public void testEqualsObject07() {
        PerformancePreferences performancePreferences1 =
                new PerformancePreferences(0, 1, 2);
        PerformancePreferences performancePreferences2 =
                new PerformancePreferences(0, 1, 2);
        Assert.assertEquals(performancePreferences1, performancePreferences2);
    }

    @Test
    public void testHashCode01() {
        PerformancePreferences performancePreferences1 =
                new PerformancePreferences(0, 1, 2);
        PerformancePreferences performancePreferences2 =
                new PerformancePreferences(0, 1, 2);
        Assert.assertEquals(performancePreferences1.hashCode(), performancePreferences2.hashCode());
    }

    @Test
    public void testHashCode02() {
        PerformancePreferences performancePreferences1 =
                new PerformancePreferences(0, 0, 1);
        PerformancePreferences performancePreferences2 =
                new PerformancePreferences(0, 0, 2);
        Assert.assertNotEquals(performancePreferences1.hashCode(), performancePreferences2.hashCode());
    }

    private static final class CustomSocket extends Socket {

        private PerformancePreferences performancePreferences;

        public CustomSocket() throws IOException {
        }

        @Override
        public void close() throws IOException { super.close(); }

        public PerformancePreferences getPerformancePreferences() {
            return this.performancePreferences;
        }

        @Override
        public void setPerformancePreferences(int connectionTime, int latency, int bandwidth) {
            super.setPerformancePreferences(connectionTime, latency, bandwidth);
            this.performancePreferences = new PerformancePreferences(connectionTime, latency, bandwidth);
        }
        
    }

}