package com.github.jh3nd3rs0n.jargyle.protocolbase.socks5.gssapimethod;

import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.EOFException;
import java.io.IOException;
import java.util.Arrays;

public class TokenTest {

    @Test(expected = IllegalArgumentException.class)
    public void testNewInstanceByteArrayForIllegalArgumentException01() {
        byte[] bytes = new byte[Token.MAX_LENGTH + 1];
        Arrays.fill(bytes, (byte) 0x00);
        Token.newInstance(bytes);
    }

    @Test(expected = EOFException.class)
    public void testNewInstanceFromInputStreamForEOFException01() throws IOException {
        Token.newInstanceFrom(new ByteArrayInputStream(new byte[] { }));
    }

    @Test(expected = EOFException.class)
    public void testNewInstanceFromInputStreamForEOFException02() throws IOException {
        Token.newInstanceFrom(new ByteArrayInputStream(new byte[] {
                (byte) 0x00 }));
    }

    @Test(expected = IOException.class)
    public void testNewInstanceFromInputStreamForIOException01() throws IOException {
        Token.newInstanceFrom(new ByteArrayInputStream(new byte[] {
                (byte) 0x00, (byte) 0x01 }));
    }

    @Test(expected = IOException.class)
    public void testNewInstanceFromInputStreamForIOException02() throws IOException {
        Token.newInstanceFrom(new ByteArrayInputStream(new byte[] {
                (byte) 0x00, (byte) 0x02, (byte) 0x01 }));
    }

    @Test
    public void testEqualsObject01() {
        Token token = Token.newInstance(new byte[] {});
        Assert.assertEquals(token, token);
    }

    @Test
    public void testEqualsObject02() {
        Token token = Token.newInstance(new byte[] {});
        Assert.assertNotEquals(token, null);
    }

    @Test
    public void testEqualsObject03() {
        Object obj1 = Token.newInstance(new byte[] {});
        Object obj2 = new Object();
        Assert.assertNotEquals(obj1, obj2);
    }

    @Test
    public void testEqualsObject04() {
        Token token1 = Token.newInstance(new byte[] {});
        Token token2 = Token.newInstance(new byte[] { (byte) 0x00 });
        Assert.assertNotEquals(token1, token2);
    }

    @Test
    public void testEqualsObject05() {
        Token token1 = Token.newInstance(new byte[] {});
        Token token2 = Token.newInstance(new byte[] {});
        Assert.assertEquals(token1, token2);
    }

    @Test
    public void testEqualsObject06() {
        Token token1 = Token.newInstance(new byte[] { (byte) 0x00 });
        Token token2 = Token.newInstance(new byte[] { (byte) 0x00 });
        Assert.assertEquals(token1, token2);
    }

    @Test
    public void testEqualsObject07() {
        Token token1 = Token.newInstance(new byte[] { (byte) 0x01, (byte) 0x02 });
        Token token2 = Token.newInstance(new byte[] { (byte) 0x01, (byte) 0x02 });
        Assert.assertEquals(token1, token2);
    }

    @Test
    public void testHashCode01() {
        Token token1 = Token.newInstance(new byte[] {});
        Token token2 = Token.newInstance(new byte[] { (byte) 0x00 });
        Assert.assertNotEquals(token1.hashCode(), token2.hashCode());
    }

    @Test
    public void testHashCode02() {
        Token token1 = Token.newInstance(new byte[] {});
        Token token2 = Token.newInstance(new byte[] {});
        Assert.assertEquals(token1.hashCode(), token2.hashCode());
    }

    @Test
    public void testHashCode03() {
        Token token1 = Token.newInstance(new byte[] { (byte) 0x00 });
        Token token2 = Token.newInstance(new byte[] { (byte) 0x00 });
        Assert.assertEquals(token1.hashCode(), token2.hashCode());
    }

    @Test
    public void testHashCode04() {
        Token token1 = Token.newInstance(new byte[] { (byte) 0x01, (byte) 0x02 });
        Token token2 = Token.newInstance(new byte[] { (byte) 0x01, (byte) 0x02 });
        Assert.assertEquals(token1.hashCode(), token2.hashCode());
    }

    @Test
    public void testToByteArray01() throws IOException {
        Token token1 = Token.newInstance(new byte[] {});
        Token token2 = Token.newInstanceFrom(new ByteArrayInputStream(
                token1.toByteArray()));
        Assert.assertEquals(token1, token2);
    }

    @Test
    public void testToByteArray02() throws IOException {
        Token token1 = Token.newInstance(new byte[] { (byte) 0x00 });
        Token token2 = Token.newInstanceFrom(new ByteArrayInputStream(
                token1.toByteArray()));
        Assert.assertEquals(token1, token2);
    }

    @Test
    public void testToByteArray03() throws IOException {
        Token token1 = Token.newInstance(new byte[] { (byte) 0x01, (byte) 0x02 });
        Token token2 = Token.newInstanceFrom(new ByteArrayInputStream(
                token1.toByteArray()));
        Assert.assertEquals(token1, token2);
    }

}