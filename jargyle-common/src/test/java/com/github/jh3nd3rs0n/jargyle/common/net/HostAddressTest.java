package com.github.jh3nd3rs0n.jargyle.common.net;

import org.junit.Assert;
import org.junit.Test;

public class HostAddressTest {

    @Test
    public void isAllZerosHostAddressString01() {
        Assert.assertTrue(HostAddress.isAllZerosHostAddress("0"));
    }

    @Test
    public void isAllZerosHostAddressString02() {
        Assert.assertTrue(HostAddress.isAllZerosHostAddress("0.0"));
    }

    @Test
    public void isAllZerosHostAddressString03() {
        Assert.assertTrue(HostAddress.isAllZerosHostAddress("0.0.0"));
    }

    @Test
    public void isAllZerosHostAddressString04() {
        Assert.assertTrue(HostAddress.isAllZerosHostAddress("0.0.0.0"));
    }

    @Test
    public void isAllZerosHostAddressString05() {
        Assert.assertTrue(HostAddress.isAllZerosHostAddress("::"));
    }

    @Test
    public void isAllZerosHostAddressString06() {
        Assert.assertTrue(HostAddress.isAllZerosHostAddress(
                "0:0:0:0:0:0:0:0"));
    }

    @Test
    public void isAllZerosHostAddressString07() {
        Assert.assertFalse(HostAddress.isAllZerosHostAddress(
                "zero zero zero"));
    }

    @Test
    public void isAllZerosHostAddressString08() {
        Assert.assertFalse(HostAddress.isAllZerosHostAddress(
                "nada nada nada"));
    }

    @Test
    public void isAllZerosHostAddressString09() {
        Assert.assertFalse(HostAddress.isAllZerosHostAddress(
                "zilch zilch zilch"));
    }

    @Test
    public void newHostAddressString01() {
        Assert.assertNotNull(HostAddress.newHostAddress("127.0.0.1"));
    }

    @Test
    public void newHostAddressString02() {
        Assert.assertNotNull(HostAddress.newHostAddress("::1"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void newHostAddressStringForIllegalArgumentException01() {
        HostAddress.newHostAddress("localhost");
    }

    @Test(expected = IllegalArgumentException.class)
    public void newHostAddressStringForIllegalArgumentException02() {
        HostAddress.newHostAddress("999.999.999.999");
    }

}