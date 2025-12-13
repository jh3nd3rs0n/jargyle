package com.github.jh3nd3rs0n.jargyle.protocolbase.socks5.gssapiauthmethod;

import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;

public class AuthenticationMessageTest {

    @Test(expected = IOException.class)
    public void testNewInstanceFromClientInputStreamForIOException01() throws IOException {
        AuthenticationMessage.newInstanceFromClient(new ByteArrayInputStream(new byte[] {
                (byte) 0x00 }));
    }

    @Test(expected = IOException.class)
    public void testNewInstanceFromClientInputStreamForIOException02() throws IOException {
        AuthenticationMessage.newInstanceFromClient(new ByteArrayInputStream(new byte[] {
                (byte) 0x01, (byte) 0x00 }));
    }

    @Test(expected = IOException.class)
    public void testNewInstanceFromClientInputStreamForIOException03() throws IOException {
        AuthenticationMessage.newInstanceFromClient(new ByteArrayInputStream(new byte[] {
                (byte) 0x01, (byte) 0x02 }));
    }

    @Test(expected = IOException.class)
    public void testNewInstanceFromServerInputStreamForIOException01() throws IOException {
        AuthenticationMessage.newInstanceFromServer(new ByteArrayInputStream(new byte[] {
                (byte) 0x00 }));
    }

    @Test(expected = IOException.class)
    public void testNewInstanceFromServerInputStreamForIOException02() throws IOException {
        AuthenticationMessage.newInstanceFromServer(new ByteArrayInputStream(new byte[] {
                (byte) 0x01, (byte) 0x00 }));
    }

    @Test(expected = IOException.class)
    public void testNewInstanceFromServerInputStreamForIOException03() throws IOException {
        AuthenticationMessage.newInstanceFromServer(new ByteArrayInputStream(new byte[] {
                (byte) 0x01, (byte) 0x02 }));
    }

    @Test(expected = AbortMessageException.class)
    public void testNewInstanceFromServerInputStreamForAbortMessageException01() throws IOException {
        AuthenticationMessage.newInstanceFromServer(new ByteArrayInputStream(new byte[] {
                (byte) 0x01, (byte) 0xff }));
    }

    @Test
    public void testEqualsObject01() {
        AuthenticationMessage message = AuthenticationMessage.newInstance(
                Token.newInstance(new byte[] {}));
        Assert.assertEquals(message, message);
    }

    @Test
    public void testEqualsObject02() {
        AuthenticationMessage message = AuthenticationMessage.newInstance(
                Token.newInstance(new byte[] {}));
        Assert.assertNotEquals(message, null);
    }

    @Test
    public void testEqualsObject03() {
        Object obj1 = AuthenticationMessage.newInstance(
                Token.newInstance(new byte[] {}));
        Object obj2 = new Object();
        Assert.assertNotEquals(obj1, obj2);
    }

    @Test
    public void testEqualsObject04() {
        AuthenticationMessage message1 = AuthenticationMessage.newInstance(
                Token.newInstance(new byte[] {}));
        AuthenticationMessage message2 = AuthenticationMessage.newInstance(
                Token.newInstance(new byte[] { (byte) 0x00, (byte) 0x01 }));
        Assert.assertNotEquals(message1, message2);
    }

    @Test
    public void testEqualsObject05() {
        AuthenticationMessage message1 = AuthenticationMessage.newInstance(
                Token.newInstance(new byte[] { (byte) 0x00, (byte) 0x01 }));
        AuthenticationMessage message2 = AuthenticationMessage.newInstance(
                Token.newInstance(new byte[] { (byte) 0x01, (byte) 0x02 }));
        Assert.assertNotEquals(message1, message2);
    }

    @Test
    public void testEqualsObject06() {
        AuthenticationMessage message1 = AuthenticationMessage.newInstance(
                Token.newInstance(new byte[] {}));
        AuthenticationMessage message2 = AuthenticationMessage.newInstance(
                Token.newInstance(new byte[] {}));
        Assert.assertEquals(message1, message2);
    }

    @Test
    public void testEqualsObject07() {
        AuthenticationMessage message1 = AuthenticationMessage.newInstance(
                Token.newInstance(new byte[] { (byte) 0x00, (byte) 0x01 }));
        AuthenticationMessage message2 = AuthenticationMessage.newInstance(
                Token.newInstance(new byte[] { (byte) 0x00, (byte) 0x01 }));
        Assert.assertEquals(message1, message2);
    }

    @Test
    public void testHashCode01() {
        AuthenticationMessage message1 = AuthenticationMessage.newInstance(
                Token.newInstance(new byte[] {}));
        AuthenticationMessage message2 = AuthenticationMessage.newInstance(
                Token.newInstance(new byte[] { (byte) 0x00, (byte) 0x01 }));
        Assert.assertNotEquals(message1.hashCode(), message2.hashCode());
    }

    @Test
    public void testHashCode02() {
        AuthenticationMessage message1 = AuthenticationMessage.newInstance(
                Token.newInstance(new byte[] { (byte) 0x00, (byte) 0x01 }));
        AuthenticationMessage message2 = AuthenticationMessage.newInstance(
                Token.newInstance(new byte[] { (byte) 0x01, (byte) 0x02 }));
        Assert.assertNotEquals(message1.hashCode(), message2.hashCode());
    }

    @Test
    public void testHashCode03() {
        AuthenticationMessage message1 = AuthenticationMessage.newInstance(
                Token.newInstance(new byte[] {}));
        AuthenticationMessage message2 = AuthenticationMessage.newInstance(
                Token.newInstance(new byte[] {}));
        Assert.assertEquals(message1.hashCode(), message2.hashCode());
    }

    @Test
    public void testHashCode04() {
        AuthenticationMessage message1 = AuthenticationMessage.newInstance(
                Token.newInstance(new byte[] { (byte) 0x00, (byte) 0x01 }));
        AuthenticationMessage message2 = AuthenticationMessage.newInstance(
                Token.newInstance(new byte[] { (byte) 0x00, (byte) 0x01 }));
        Assert.assertEquals(message1.hashCode(), message2.hashCode());
    }

    @Test
    public void testToByteArray01() throws IOException {
        AuthenticationMessage message1 = AuthenticationMessage.newInstance(
                Token.newInstance(new byte[] {}));
        AuthenticationMessage message2 =
                AuthenticationMessage.newInstanceFromClient(
                        new ByteArrayInputStream(message1.toByteArray()));
        Assert.assertEquals(message1, message2);
    }

    @Test
    public void testToByteArray02() throws IOException {
        AuthenticationMessage message1 = AuthenticationMessage.newInstance(
                Token.newInstance(new byte[] { (byte) 0x00, (byte) 0x01 }));
        AuthenticationMessage message2 =
                AuthenticationMessage.newInstanceFromClient(
                        new ByteArrayInputStream(message1.toByteArray()));
        Assert.assertEquals(message1, message2);
    }

    @Test
    public void testToByteArray03() throws IOException {
        AuthenticationMessage message1 = AuthenticationMessage.newInstance(
                Token.newInstance(new byte[] {}));
        AuthenticationMessage message2 =
                AuthenticationMessage.newInstanceFromServer(
                        new ByteArrayInputStream(message1.toByteArray()));
        Assert.assertEquals(message1, message2);
    }

    @Test
    public void testToByteArray04() throws IOException {
        AuthenticationMessage message1 = AuthenticationMessage.newInstance(
                Token.newInstance(new byte[] { (byte) 0x00, (byte) 0x01 }));
        AuthenticationMessage message2 =
                AuthenticationMessage.newInstanceFromServer(
                        new ByteArrayInputStream(message1.toByteArray()));
        Assert.assertEquals(message1, message2);
    }

}