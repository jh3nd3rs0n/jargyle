package com.github.jh3nd3rs0n.jargyle.protocolbase.socks5.gssapiauthmethod;

import org.junit.Assert;
import org.junit.Test;

public class ProtectionLevelsTest {

    @Test
    public void testNewInstanceFromString01() {
        ProtectionLevels protectionLevels1 = ProtectionLevels.newInstanceFrom(
                "REQUIRED_INTEG,NONE,SELECTIVE_INTEG_OR_CONF");
        ProtectionLevels protectionLevels2 = ProtectionLevels.of(
                ProtectionLevel.REQUIRED_INTEG,
                ProtectionLevel.NONE,
                ProtectionLevel.SELECTIVE_INTEG_OR_CONF);
        Assert.assertEquals(protectionLevels1, protectionLevels2);
    }

    @Test
    public void testNewInstanceFromString02() {
        ProtectionLevels protectionLevels1 = ProtectionLevels.newInstanceFrom(
                "REQUIRED_INTEG_AND_CONF");
        ProtectionLevels protectionLevels2 = ProtectionLevels.of(
                ProtectionLevel.REQUIRED_INTEG_AND_CONF);
        Assert.assertEquals(protectionLevels1, protectionLevels2);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNewInstanceFromStringForIllegalArgumentException01() {
        ProtectionLevels.newInstanceFrom("");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNewInstanceFromStringForIllegalArgumentException02() {
        ProtectionLevels.newInstanceFrom(" ");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNewInstanceFromStringForIllegalArgumentException03() {
        ProtectionLevels.newInstanceFrom("REQUIRED_INTEG,REQUIRED_INTEG_OR_CONF");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNewInstanceFromStringForIllegalArgumentException04() {
        ProtectionLevels.newInstanceFrom("REQUIRED_INTEG,REQUIRED_INTEG_AND_CONF,");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNewInstanceFromStringForIllegalArgumentException05() {
        ProtectionLevels.newInstanceFrom(",,");
    }

    @Test
    public void testContainsProtectionLevel01() {
        Assert.assertFalse(
                ProtectionLevels.of(ProtectionLevel.REQUIRED_INTEG).contains(ProtectionLevel.NONE));
    }

    @Test
    public void testContainsProtectionLevel02() {
        Assert.assertTrue(
                ProtectionLevels.of(ProtectionLevel.NONE, ProtectionLevel.REQUIRED_INTEG).contains(ProtectionLevel.NONE));
    }

    @Test
    public void testContainsProtectionLevel03() {
        Assert.assertTrue(
                ProtectionLevels.of(ProtectionLevel.SELECTIVE_INTEG_OR_CONF, ProtectionLevel.NONE, ProtectionLevel.REQUIRED_INTEG).contains(ProtectionLevel.NONE));
    }

    @Test
    public void testEqualsObject01() {
        ProtectionLevels protectionLevels = ProtectionLevels.of(
                ProtectionLevel.NONE);
        Assert.assertEquals(protectionLevels, protectionLevels);
    }

    @Test
    public void testEqualsObject02() {
        ProtectionLevels protectionLevels = ProtectionLevels.of(
                ProtectionLevel.NONE);
        Assert.assertNotEquals(protectionLevels, null);
    }

    @Test
    public void testEqualsObject03() {
        Object obj1 = ProtectionLevels.of(ProtectionLevel.NONE);
        Object obj2 = new Object();
        Assert.assertNotEquals(obj1, obj2);
    }

    @Test
    public void testEqualsObject04() {
        ProtectionLevels protectionLevels1 = ProtectionLevels.of(
                ProtectionLevel.NONE);
        ProtectionLevels protectionLevels2 = ProtectionLevels.of(
                ProtectionLevel.NONE, ProtectionLevel.REQUIRED_INTEG);
        Assert.assertNotEquals(protectionLevels1, protectionLevels2);
    }

    @Test
    public void testEqualsObject05() {
        ProtectionLevels protectionLevels1 = ProtectionLevels.of(
                ProtectionLevel.NONE, ProtectionLevel.REQUIRED_INTEG);
        ProtectionLevels protectionLevels2 = ProtectionLevels.of(
                ProtectionLevel.NONE, ProtectionLevel.REQUIRED_INTEG);
        Assert.assertEquals(protectionLevels1, protectionLevels2);
    }

    @Test
    public void testGetFirst01() {
        Assert.assertEquals(
                ProtectionLevel.REQUIRED_INTEG_AND_CONF,
                ProtectionLevels.of(ProtectionLevel.REQUIRED_INTEG_AND_CONF, ProtectionLevel.NONE).getFirst());
    }

    @Test
    public void testGetFirst02() {
        Assert.assertEquals(
                ProtectionLevel.NONE,
                ProtectionLevels.of(ProtectionLevel.NONE, ProtectionLevel.REQUIRED_INTEG_AND_CONF).getFirst());
    }

    @Test
    public void testHashCode01() {
        ProtectionLevels protectionLevels1 = ProtectionLevels.of(
                ProtectionLevel.NONE);
        ProtectionLevels protectionLevels2 = ProtectionLevels.of(
                ProtectionLevel.NONE, ProtectionLevel.REQUIRED_INTEG);
        Assert.assertNotEquals(protectionLevels1.hashCode(), protectionLevels2.hashCode());
    }

    @Test
    public void testHashCode02() {
        ProtectionLevels protectionLevels1 = ProtectionLevels.of(
                ProtectionLevel.NONE, ProtectionLevel.REQUIRED_INTEG);
        ProtectionLevels protectionLevels2 = ProtectionLevels.of(
                ProtectionLevel.NONE, ProtectionLevel.REQUIRED_INTEG);
        Assert.assertEquals(protectionLevels1.hashCode(), protectionLevels2.hashCode());
    }

    @Test
    public void testToString01() {
        Assert.assertEquals(
                "REQUIRED_INTEG,NONE",
                ProtectionLevels.of(
                        ProtectionLevel.REQUIRED_INTEG,
                        ProtectionLevel.NONE).toString());
    }

    @Test
    public void testToString02() {
        Assert.assertEquals(
                "REQUIRED_INTEG_AND_CONF",
                ProtectionLevels.of(ProtectionLevel.REQUIRED_INTEG_AND_CONF).toString());
    }

    @Test
    public void testToString03() {
        Assert.assertEquals(
                "SELECTIVE_INTEG_OR_CONF,NONE,REQUIRED_INTEG_AND_CONF",
                ProtectionLevels.of(
                        ProtectionLevel.SELECTIVE_INTEG_OR_CONF,
                        ProtectionLevel.NONE,
                        ProtectionLevel.REQUIRED_INTEG_AND_CONF).toString());
    }

}