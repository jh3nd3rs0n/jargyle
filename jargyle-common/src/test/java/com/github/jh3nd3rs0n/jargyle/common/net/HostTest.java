package com.github.jh3nd3rs0n.jargyle.common.net;

import org.junit.Assert;
import org.junit.Test;

public class HostTest {

    @Test
    public void testNewInstanceOfString01() {
        Assert.assertNotNull(Host.newInstanceOf("127.0.0.1"));
    }

    @Test
    public void testNewInstanceOfString02() {
        Assert.assertNotNull(Host.newInstanceOf("::1"));
    }

    @Test
    public void testNewInstanceOfString03() {
        Assert.assertNotNull(Host.newInstanceOf("localhost"));
    }

    @Test
    public void testNewInstanceOfString04() {
        Assert.assertNotNull(Host.newInstanceOf("google.com"));
    }

    @Test
    public void testNewInstanceOfString05() {
        Assert.assertNotNull(Host.newInstanceOf("jh3nd3rs0n.github.io"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNewInstanceOfStringForIllegalArgumentException01() {
        Host.newInstanceOf("@#$*()@#");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNewInstanceOfStringForIllegalArgumentException02() {
        Host.newInstanceOf("LOCALHOST");
    }

    @Test
    public void testToString() {
        Host host = Host.newInstanceOf("localhost");
        Assert.assertEquals("localhost", host.toString());
    }

}