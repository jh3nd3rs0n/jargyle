package com.github.jh3nd3rs0n.jargyle.client.internal.propertyspec.impl;

import org.ietf.jgss.GSSException;
import org.ietf.jgss.Oid;
import org.junit.Assert;
import org.junit.Test;

public class OidPropertySpecTest {

    @Test
    public void testGetDefaultProperty01() throws GSSException {
        Assert.assertEquals(
                new Oid("1.2.840.113554.1.2.2"),
                new OidPropertySpec("oidProperty", "1.2.840.113554.1.2.2")
                        .getDefaultProperty().getValue());
    }

    @Test
    public void testGetDefaultProperty02() throws GSSException {
        Assert.assertEquals(
                new Oid("1.2"),
                new OidPropertySpec("oidProperty", "1.2")
                        .getDefaultProperty().getValue());
    }

    @Test
    public void testGetDefaultProperty03() throws GSSException {
        Assert.assertNull(new OidPropertySpec("oidProperty", null)
                .getDefaultProperty().getValue());
    }

    @Test
    public void testParseString01() throws GSSException {
        Assert.assertEquals(
                new Oid("1.2.840.113554.1.2.2"),
                new OidPropertySpec("oidProperty", null).parse(
                        "1.2.840.113554.1.2.2"));
    }

    @Test
    public void testParseString02() throws GSSException {
        Assert.assertEquals(
                new Oid("1.2"),
                new OidPropertySpec("oidProperty", null).parse(
                        "1.2"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseStringForIllegalArgumentException01() {
        new OidPropertySpec("oidProperty", null).parse(
                "");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseStringForIllegalArgumentException02() {
        new OidPropertySpec("oidProperty", null).parse(
                " ");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseStringForIllegalArgumentException03() {
        new OidPropertySpec("oidProperty", null).parse(
                "-1");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseStringForIllegalArgumentException04() {
        new OidPropertySpec("oidProperty", null).parse(
                "a");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseStringForIllegalArgumentException05() {
        new OidPropertySpec("oidProperty", null).parse(
                "1");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseStringForIllegalArgumentException06() {
        new OidPropertySpec("oidProperty", null).parse(
                "1.");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseStringForIllegalArgumentException07() {
        new OidPropertySpec("oidProperty", null).parse(
                "1.-1");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseStringForIllegalArgumentException08() {
        new OidPropertySpec("oidProperty", null).parse(
                "1.a");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testOidPropertySpecStringStringForIllegalArgumentException01() {
        new OidPropertySpec("oidProperty", "");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testOidPropertySpecStringStringForIllegalArgumentException02() {
        new OidPropertySpec("oidProperty", " ");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testOidPropertySpecStringStringForIllegalArgumentException03() {
        new OidPropertySpec("oidProperty", "-1");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testOidPropertySpecStringStringForIllegalArgumentException04() {
        new OidPropertySpec("oidProperty", "a");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testOidPropertySpecStringStringForIllegalArgumentException05() {
        new OidPropertySpec("oidProperty", "1");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testOidPropertySpecStringStringForIllegalArgumentException06() {
        new OidPropertySpec("oidProperty", "1.");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testOidPropertySpecStringStringForIllegalArgumentException07() {
        new OidPropertySpec("oidProperty", "1.-1");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testOidPropertySpecStringStringForIllegalArgumentException08() {
        new OidPropertySpec("oidProperty", "1.a");
    }

}