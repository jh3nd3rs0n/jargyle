package com.github.jh3nd3rs0n.jargyle.common.net;

import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.*;

public class HostTest {

    @Test
    public void newInstanceString01() {
        Assert.assertNotNull(Host.newInstance("127.0.0.1"));
    }

    @Test
    public void newInstanceString02() {
        Assert.assertNotNull(Host.newInstance("::1"));
    }

    @Test
    public void newInstanceString03() {
        Assert.assertNotNull(Host.newInstance("localhost"));
    }

    @Test
    public void newInstanceString04() {
        Assert.assertNotNull(Host.newInstance("google.com"));
    }

    @Test
    public void newInstanceString05() {
        Assert.assertNotNull(Host.newInstance("jh3nd3rs0n.github.io"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void newInstanceStringForIllegalArgumentException01() {
        Host.newInstance("@#$*()@#");
    }


    @Test(expected = IllegalArgumentException.class)
    public void newInstanceStringForIllegalArgumentException02() {
        Host.newInstance("LOCALHOST");
    }

}