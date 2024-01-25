package com.github.jh3nd3rs0n.jargyle.common.net;

import org.junit.Assert;
import org.junit.Test;

public class HostTest {

    @Test
    public void testNewInstanceOfString01() {
        Assert.assertNotNull(Host.newInstance("127.0.0.1"));
    }

    @Test
    public void testNewInstanceOfString02() {
        Assert.assertNotNull(Host.newInstance("::1"));
    }

    @Test
    public void testNewInstanceOfString03() {
        Assert.assertNotNull(Host.newInstance("localhost"));
    }

    @Test
    public void testNewInstanceOfString04() {
        Assert.assertNotNull(Host.newInstance("google.com"));
    }

    @Test
    public void testNewInstanceOfString05() {
        Assert.assertNotNull(Host.newInstance("jh3nd3rs0n.github.io"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNewInstanceOfStringForIllegalArgumentException01() {
        Host.newInstance("@#$*()@#");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNewInstanceOfStringForIllegalArgumentException02() {
        Host.newInstance("LOCALHOST");
    }

    @Test
    public void testToString() {
        Host host = Host.newInstance("localhost");
        Assert.assertEquals("localhost", host.toString());
    }

}