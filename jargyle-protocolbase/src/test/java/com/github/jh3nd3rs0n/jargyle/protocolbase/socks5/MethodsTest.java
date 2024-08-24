package com.github.jh3nd3rs0n.jargyle.protocolbase.socks5;

import org.junit.Assert;
import org.junit.Test;

public class MethodsTest {

    @Test
    public void testGetDefault() {
        Assert.assertEquals(
                Methods.of(Method.NO_AUTHENTICATION_REQUIRED),
                Methods.getDefault());
    }

    @Test
    public void testNewInstanceFromString01() {
        Assert.assertNotNull(
                Methods.newInstanceFrom(""));
    }

    @Test
    public void testNewInstanceFromString02() {
        Assert.assertNotNull(
                Methods.newInstanceFrom("NO_AUTHENTICATION_REQUIRED,USERNAME_PASSWORD"));
    }

    @Test
    public void testNewInstanceFromString03() {
        Assert.assertNotNull(
                Methods.newInstanceFrom("GSSAPI,NO_AUTHENTICATION_REQUIRED,USERNAME_PASSWORD"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNewInstanceFromStringForIllegalArgumentException01() {
        Methods.newInstanceFrom(" ");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNewInstanceFromStringForIllegalArgumentException02() {
        Methods.newInstanceFrom("GSS-API");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNewInstanceFromStringForIllegalArgumentException03() {
        Methods.newInstanceFrom("GSSAPI,USERNAME-PASSWORD");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNewInstanceFromStringForIllegalArgumentException04() {
        Methods.newInstanceFrom(",GSSAPI,USERNAME_PASSWORD");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNewInstanceFromStringForIllegalArgumentException05() {
        Methods.newInstanceFrom("GSSAPI,,USERNAME_PASSWORD");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNewInstanceFromStringForIllegalArgumentException06() {
        Methods.newInstanceFrom("GSSAPI,USERNAME_PASSWORD,");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNewInstanceFromStringForIllegalArgumentException07() {
        Methods.newInstanceFrom(",,");
    }

    @Test
    public void testContainsMethod01() {
        Assert.assertFalse(
                Methods.of().contains(Method.NO_AUTHENTICATION_REQUIRED));
    }

    @Test
    public void testContainsMethod02() {
        Assert.assertFalse(
                Methods.of(Method.GSSAPI, Method.USERNAME_PASSWORD).contains(Method.NO_AUTHENTICATION_REQUIRED));
    }

    @Test
    public void testContainsMethod03() {
        Assert.assertTrue(
                Methods.of(Method.GSSAPI, Method.USERNAME_PASSWORD).contains(Method.USERNAME_PASSWORD));
    }

    @Test
    public void testContainsMethod04() {
        Assert.assertTrue(
                Methods.of(Method.GSSAPI, Method.USERNAME_PASSWORD).contains(Method.GSSAPI));
    }

    @Test
    public void testEqualsObject01() {
        Methods methods = Methods.of();
        Assert.assertEquals(methods, methods);
    }

    @Test
    public void testEqualsObject02() {
        Assert.assertNotEquals(Methods.of(), null);
    }

    @Test
    public void testEqualsObject03() {
        Object obj1 = Methods.of();
        Object obj2 = new Object();
        Assert.assertNotEquals(obj1, obj2);
    }

    @Test
    public void testEqualsObject04() {
        Assert.assertNotEquals(
                Methods.of(Method.GSSAPI, Method.USERNAME_PASSWORD),
                Methods.of(Method.NO_ACCEPTABLE_METHODS, Method.USERNAME_PASSWORD));
    }

    @Test
    public void testEqualsObject05() {
        Assert.assertEquals(
                Methods.of(Method.NO_ACCEPTABLE_METHODS, Method.NO_AUTHENTICATION_REQUIRED),
                Methods.of(Method.NO_ACCEPTABLE_METHODS, Method.NO_AUTHENTICATION_REQUIRED));
    }

    @Test
    public void testHashCode01() {
        Assert.assertNotEquals(
                Methods.of(Method.GSSAPI, Method.USERNAME_PASSWORD).hashCode(),
                Methods.of(Method.NO_ACCEPTABLE_METHODS, Method.USERNAME_PASSWORD).hashCode());
    }

    @Test
    public void testHashCode02() {
        Assert.assertEquals(
                Methods.of(Method.NO_ACCEPTABLE_METHODS, Method.NO_AUTHENTICATION_REQUIRED).hashCode(),
                Methods.of(Method.NO_ACCEPTABLE_METHODS, Method.NO_AUTHENTICATION_REQUIRED).hashCode());
    }

    @Test
    public void testToString01() {
        Assert.assertEquals(
                "NO_ACCEPTABLE_METHODS,NO_AUTHENTICATION_REQUIRED",
                Methods.of(Method.NO_ACCEPTABLE_METHODS, Method.NO_AUTHENTICATION_REQUIRED).toString());
    }

    @Test
    public void testToString02() {
        Assert.assertEquals(
                "GSSAPI,GSSAPI,USERNAME_PASSWORD",
                Methods.of(Method.GSSAPI, Method.GSSAPI, Method.USERNAME_PASSWORD).toString());
    }

    @Test
    public void testToString03() {
        Assert.assertEquals("", Methods.of().toString());
    }

}