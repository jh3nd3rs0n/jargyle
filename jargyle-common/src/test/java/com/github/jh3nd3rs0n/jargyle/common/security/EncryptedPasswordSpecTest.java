package com.github.jh3nd3rs0n.jargyle.common.security;

import org.junit.Assert;
import org.junit.Test;

public class EncryptedPasswordSpecTest {

    @Test
    public void testEqualsObject01() {
        EncryptedPasswordSpec spec = new TestEncryptedPasswordSpec(
                "TestEncryptedPassword");
        Assert.assertEquals(spec, spec);
    }

    @Test
    public void testEqualsObject02() {
        EncryptedPasswordSpec spec = new TestEncryptedPasswordSpec(
                "TestEncryptedPassword");
        Assert.assertNotEquals(spec, null);
    }

    @Test
    public void testEqualsObject03() {
        Object obj1 = new TestEncryptedPasswordSpec(
                "TestEncryptedPassword");
        Object obj2 = new Object();
        Assert.assertNotEquals(obj1, obj2);
    }

    @Test
    public void testEqualsObject04() {
        EncryptedPasswordSpec spec1 = new TestEncryptedPasswordSpec(
                "TestEncryptedPassword1");
        EncryptedPasswordSpec spec2 = new TestEncryptedPasswordSpec(
                "TestEncryptedPassword2");
        Assert.assertNotEquals(spec1, spec2);
    }

    @Test
    public void testEqualsObject05() {
        EncryptedPasswordSpec spec1 = new TestEncryptedPasswordSpec(
                "TestEncryptedPassword");
        EncryptedPasswordSpec spec2 = new TestEncryptedPasswordSpec(
                "TestEncryptedPassword");
        Assert.assertEquals(spec1, spec2);
    }

    @Test
    public void testHashCode01() {
        EncryptedPasswordSpec spec1 = new TestEncryptedPasswordSpec(
                "TestEncryptedPassword");
        EncryptedPasswordSpec spec2 = new TestEncryptedPasswordSpec(
                "TestEncryptedPassword");
        Assert.assertEquals(spec1.hashCode(), spec2.hashCode());
    }

    @Test
    public void testHashCode02() {
        EncryptedPasswordSpec spec1 = new TestEncryptedPasswordSpec(
                "TestEncryptedPassword1");
        EncryptedPasswordSpec spec2 = new TestEncryptedPasswordSpec(
                "TestEncryptedPassword2");
        Assert.assertNotEquals(spec1.hashCode(), spec2.hashCode());
    }

    @Test
    public void testNewEncryptedPasswordCharArray() {
        EncryptedPasswordSpec spec = new TestEncryptedPasswordSpec(
                "TestEncryptedPassword");
        Assert.assertNotNull(spec.newEncryptedPassword(
                "Hello, World".toCharArray()));
    }

    @Test
    public void testNewEncryptedPasswordString() {
        EncryptedPasswordSpec spec = new TestEncryptedPasswordSpec(
                "TestEncryptedPassword");
        Assert.assertNotNull(spec.newEncryptedPassword(
                "SGVsbG8sIFdvcmxkCg=="));
    }

}