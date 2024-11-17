package com.github.jh3nd3rs0n.jargyle.client.socks5;

import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.*;

public class UsernamePasswordTest {

    @Test(expected = IllegalArgumentException.class)
    public void testNewInstanceStringCharArrayForIllegalArgumentException01() {
        char[] chars = new char[256];
        Arrays.fill(chars, 'a');
        UsernamePassword.newInstance(new String(chars), new char[] {});
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNewInstanceStringCharArrayForIllegalArgumentException02() {
        char[] chars = new char[256];
        Arrays.fill(chars, 'a');
        UsernamePassword.newInstance("", chars);
    }

    @Test
    public void testNewInstanceFromString01() {
        UsernamePassword usernamePassword = UsernamePassword.newInstanceFrom(
                "Aladdin:opensesame");
        Assert.assertEquals("Aladdin", usernamePassword.getUsername());
        Assert.assertArrayEquals(
                "opensesame".toCharArray(), usernamePassword.getPassword());
    }

    @Test
    public void testNewInstanceFromString02() {
        UsernamePassword usernamePassword = UsernamePassword.newInstanceFrom(
                "Jasmine:mission%3Aimpossible");
        Assert.assertEquals("Jasmine", usernamePassword.getUsername());
        Assert.assertArrayEquals(
                "mission:impossible".toCharArray(),
                usernamePassword.getPassword());
    }

    @Test
    public void testNewInstanceFromString03() {
        UsernamePassword usernamePassword = UsernamePassword.newInstanceFrom(
                "Abu:safeDriversSave40%25");
        Assert.assertEquals("Abu", usernamePassword.getUsername());
        Assert.assertArrayEquals(
                "safeDriversSave40%".toCharArray(),
                usernamePassword.getPassword());
    }

    @Test
    public void testNewInstanceFromString04() {
        UsernamePassword usernamePassword = UsernamePassword.newInstanceFrom(
                ":");
        Assert.assertEquals("", usernamePassword.getUsername());
        Assert.assertArrayEquals(
                new char[] {},
                usernamePassword.getPassword());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNewInstanceFromStringForIllegalArgumentException01() {
        UsernamePassword.newInstanceFrom("");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNewInstanceFromStringForIllegalArgumentException02() {
        UsernamePassword.newInstanceFrom(" ");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNewInstanceFromStringForIllegalArgumentException03() {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < 100; i++) {
            stringBuilder.append("0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz");
        }
        UsernamePassword.newInstanceFrom(stringBuilder.toString().concat(":"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNewInstanceFromStringForIllegalArgumentException04() {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < 100; i++) {
            stringBuilder.append("0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz");
        }
        UsernamePassword.newInstanceFrom(":".concat(stringBuilder.toString()));
    }

    @Test
    public void testTryNewInstanceFromString01() {
        UsernamePassword usernamePassword = UsernamePassword.tryNewInstanceFrom(
                "Aladdin:opensesame");
        Assert.assertEquals("Aladdin", usernamePassword.getUsername());
        Assert.assertArrayEquals(
                "opensesame".toCharArray(), usernamePassword.getPassword());
    }

    @Test
    public void testTryNewInstanceFromString02() {
        UsernamePassword usernamePassword = UsernamePassword.tryNewInstanceFrom(
                "Jasmine:mission%3Aimpossible");
        Assert.assertEquals("Jasmine", usernamePassword.getUsername());
        Assert.assertArrayEquals(
                "mission:impossible".toCharArray(),
                usernamePassword.getPassword());
    }

    @Test
    public void testTryNewInstanceFromString03() {
        UsernamePassword usernamePassword = UsernamePassword.tryNewInstanceFrom(
                "Abu:safeDriversSave40%25");
        Assert.assertEquals("Abu", usernamePassword.getUsername());
        Assert.assertArrayEquals(
                "safeDriversSave40%".toCharArray(),
                usernamePassword.getPassword());
    }

    @Test
    public void testTryNewInstanceFromString04() {
        UsernamePassword usernamePassword = UsernamePassword.tryNewInstanceFrom(
                ":");
        Assert.assertEquals("", usernamePassword.getUsername());
        Assert.assertArrayEquals(
                new char[] {},
                usernamePassword.getPassword());
    }

    @Test
    public void testTryNewInstanceFromString05() {
        Assert.assertNull(UsernamePassword.tryNewInstanceFrom(""));
    }

    @Test
    public void testTryNewInstanceFromString06() {
        Assert.assertNull(UsernamePassword.tryNewInstanceFrom(" "));
    }

    @Test
    public void testTryNewInstanceFromString07() {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < 100; i++) {
            stringBuilder.append("0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz");
        }
        Assert.assertNull(UsernamePassword.tryNewInstanceFrom(stringBuilder.toString().concat(":")));
    }

    @Test
    public void testTryNewInstanceFromString08() {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < 100; i++) {
            stringBuilder.append("0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz");
        }
        Assert.assertNull(UsernamePassword.tryNewInstanceFrom(":".concat(stringBuilder.toString())));
    }

    @Test
    public void testEqualsObject01() {
        UsernamePassword usernamePassword = UsernamePassword.newInstance(
                "Aladdin", "opensesame".toCharArray());
        Assert.assertEquals(usernamePassword, usernamePassword);
    }

    @Test
    public void testEqualsObject02() {
        UsernamePassword usernamePassword = UsernamePassword.newInstance(
                "Aladdin", "opensesame".toCharArray());
        Assert.assertNotEquals(usernamePassword, null);
    }

    @Test
    public void testEqualsObject03() {
        Object obj1 = UsernamePassword.newInstance(
                "Aladdin", "opensesame".toCharArray());
        Object obj2 = new Object();
        Assert.assertNotEquals(obj1, obj2);
    }

    @Test
    public void testEqualsObject04() {
        UsernamePassword usernamePassword1 = UsernamePassword.newInstance(
                "Aladdin", "opensesame".toCharArray());
        UsernamePassword usernamePassword2 = UsernamePassword.newInstance(
                "Jasmine", "mission:impossible".toCharArray());
        Assert.assertNotEquals(usernamePassword1, usernamePassword2);
    }

    @Test
    public void testEqualsObject05() {
        UsernamePassword usernamePassword1 = UsernamePassword.newInstance(
                "Aladdin", "opensesame".toCharArray());
        UsernamePassword usernamePassword2 = UsernamePassword.newInstance(
                "Aladdin", "mission:impossible".toCharArray());
        Assert.assertNotEquals(usernamePassword1, usernamePassword2);
    }

    @Test
    public void testEqualsObject06() {
        UsernamePassword usernamePassword1 = UsernamePassword.newInstance(
                "Aladdin", "opensesame".toCharArray());
        UsernamePassword usernamePassword2 = UsernamePassword.newInstance(
                "Aladdin", "opensesame".toCharArray());
        Assert.assertEquals(usernamePassword1, usernamePassword2);
    }

    @Test
    public void testHashCode01() {
        UsernamePassword usernamePassword1 = UsernamePassword.newInstance(
                "Aladdin", "opensesame".toCharArray());
        UsernamePassword usernamePassword2 = UsernamePassword.newInstance(
                "Jasmine", "mission:impossible".toCharArray());
        Assert.assertNotEquals(usernamePassword1.hashCode(), usernamePassword2.hashCode());
    }

    @Test
    public void testHashCode02() {
        UsernamePassword usernamePassword1 = UsernamePassword.newInstance(
                "Aladdin", "opensesame".toCharArray());
        UsernamePassword usernamePassword2 = UsernamePassword.newInstance(
                "Aladdin", "mission:impossible".toCharArray());
        Assert.assertNotEquals(usernamePassword1.hashCode(), usernamePassword2.hashCode());
    }

    @Test
    public void testHashCode03() {
        UsernamePassword usernamePassword1 = UsernamePassword.newInstance(
                "Aladdin", "opensesame".toCharArray());
        UsernamePassword usernamePassword2 = UsernamePassword.newInstance(
                "Aladdin", "opensesame".toCharArray());
        Assert.assertEquals(usernamePassword1.hashCode(), usernamePassword2.hashCode());
    }

}