package com.github.jh3nd3rs0n.jargyle.common.bytes;

import org.junit.Assert;
import org.junit.Test;

import java.nio.charset.StandardCharsets;

public class BytesTest {

    @Test(expected = IllegalArgumentException.class)
    public void testNewInstanceFromStringForIllegalArgumentException01() {
        Bytes.newInstanceFrom(" ");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNewInstanceFromStringForIllegalArgumentException02() {
        Bytes.newInstanceFrom("@");
    }

    @Test
    public void testEqualsObject01() {
        Bytes bytes = Bytes.newInstanceFrom("");
        Assert.assertEquals(bytes, bytes);
    }

    @Test
    public void testEqualsObject02() {
        Bytes bytes = Bytes.newInstanceFrom("");
        Assert.assertNotEquals(bytes, null);
    }

    @Test
    public void testEqualsObject03() {
        Object obj1 = Bytes.newInstanceFrom("");
        Object obj2 = new Object();
        Assert.assertNotEquals(obj1, obj2);
    }

    @Test
    public void testEqualsObject04() {
        Bytes bytes1 = Bytes.newInstanceFrom("");
        Bytes bytes2 = Bytes.newInstanceFrom("YQ==");
        Assert.assertNotEquals(bytes1, bytes2);
    }

    @Test
    public void testEqualsObject05() {
        Bytes bytes1 = Bytes.newInstanceFrom("");
        Bytes bytes2 = Bytes.newInstanceFrom("");
        Assert.assertEquals(bytes1, bytes2);
    }

    @Test
    public void testHashCode01() {
        Bytes bytes1 = Bytes.newInstanceFrom("");
        Bytes bytes2 = Bytes.newInstanceFrom("YQ==");
        Assert.assertNotEquals(bytes1.hashCode(), bytes2.hashCode());
    }

    @Test
    public void testHashCode02() {
        Bytes bytes1 = Bytes.newInstanceFrom("");
        Bytes bytes2 = Bytes.newInstanceFrom("");
        Assert.assertEquals(bytes1, bytes2);
    }

    @Test
    public void testToArray01() {
        Assert.assertArrayEquals(
                new byte[] { },
                Bytes.newInstanceFrom("").toArray());
    }

    @Test
    public void testToArray02() {
        Assert.assertArrayEquals(
                "a".getBytes(StandardCharsets.UTF_8),
                Bytes.newInstanceFrom("YQ==").toArray());
    }

    @Test
    public void testToString01() {
        Assert.assertEquals(
                "",
                Bytes.of(new byte[] {}).toString());
    }

    @Test
    public void testToString02() {
        Assert.assertEquals(
                "YQ==",
                Bytes.of("a".getBytes(StandardCharsets.UTF_8)).toString());
    }

}