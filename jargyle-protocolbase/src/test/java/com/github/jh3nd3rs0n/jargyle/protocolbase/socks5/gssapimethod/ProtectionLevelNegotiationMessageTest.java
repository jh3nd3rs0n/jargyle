package com.github.jh3nd3rs0n.jargyle.protocolbase.socks5.gssapimethod;

import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;

public class ProtectionLevelNegotiationMessageTest {

    @Test(expected = IOException.class)
    public void testNewInstanceFromInputStreamForIOException01() throws IOException {
        ProtectionLevelNegotiationMessage.newInstanceFrom(new ByteArrayInputStream(new byte[] {
                (byte) 0x00 }));
    }

    @Test(expected = IOException.class)
    public void testNewInstanceFromInputStreamForIOException02() throws IOException {
        ProtectionLevelNegotiationMessage.newInstanceFrom(new ByteArrayInputStream(new byte[] {
                (byte) 0x01, (byte) 0x01 }));
    }

    @Test(expected = IOException.class)
    public void testNewInstanceFromInputStreamForIOException03() throws IOException {
        ProtectionLevelNegotiationMessage.newInstanceFrom(new ByteArrayInputStream(new byte[] {
                (byte) 0x01, (byte) 0x03 }));
    }

    @Test
    public void testEqualsObject01() {
        ProtectionLevelNegotiationMessage message =
                ProtectionLevelNegotiationMessage.newInstance(
                        Token.newInstance(new byte[] { (byte) 0x01 }));
        Assert.assertEquals(message, message);
    }

    @Test
    public void testEqualsObject02() {
        ProtectionLevelNegotiationMessage message =
                ProtectionLevelNegotiationMessage.newInstance(
                        Token.newInstance(new byte[] { (byte) 0x01 }));
        Assert.assertNotEquals(message, null);
    }

    @Test
    public void testEqualsObject03() {
        Object obj1 = ProtectionLevelNegotiationMessage.newInstance(
                Token.newInstance(new byte[] { (byte) 0x01 }));
        Object obj2 = new Object();
        Assert.assertNotEquals(obj1, obj2);
    }

    @Test
    public void testEqualsObject04() {
        ProtectionLevelNegotiationMessage message1 =
                ProtectionLevelNegotiationMessage.newInstance(
                        Token.newInstance(new byte[] { (byte) 0x01 }));
        ProtectionLevelNegotiationMessage message2 =
                ProtectionLevelNegotiationMessage.newInstance(
                        Token.newInstance(new byte[] { (byte) 0x02, (byte) 0x03 }));
        Assert.assertNotEquals(message1, message2);
    }

    @Test
    public void testEqualsObject05() {
        ProtectionLevelNegotiationMessage message1 =
                ProtectionLevelNegotiationMessage.newInstance(
                        Token.newInstance(new byte[] { (byte) 0x02, (byte) 0x03 }));
        ProtectionLevelNegotiationMessage message2 =
                ProtectionLevelNegotiationMessage.newInstance(
                        Token.newInstance(new byte[] { (byte) 0x04, (byte) 0x05 }));
        Assert.assertNotEquals(message1, message2);
    }

    @Test
    public void testEqualsObject06() {
        ProtectionLevelNegotiationMessage message1 =
                ProtectionLevelNegotiationMessage.newInstance(
                        Token.newInstance(new byte[] { (byte) 0x01 }));
        ProtectionLevelNegotiationMessage message2 =
                ProtectionLevelNegotiationMessage.newInstance(
                        Token.newInstance(new byte[] { (byte) 0x01 }));
        Assert.assertEquals(message1, message2);
    }

    @Test
    public void testEqualsObject07() {
        ProtectionLevelNegotiationMessage message1 =
                ProtectionLevelNegotiationMessage.newInstance(
                        Token.newInstance(new byte[] { (byte) 0x02, (byte) 0x03 }));
        ProtectionLevelNegotiationMessage message2 =
                ProtectionLevelNegotiationMessage.newInstance(
                        Token.newInstance(new byte[] { (byte) 0x02, (byte) 0x03 }));
        Assert.assertEquals(message1, message2);
    }

    @Test
    public void testHashCode01() {
        ProtectionLevelNegotiationMessage message1 =
                ProtectionLevelNegotiationMessage.newInstance(
                        Token.newInstance(new byte[] { (byte) 0x01 }));
        ProtectionLevelNegotiationMessage message2 =
                ProtectionLevelNegotiationMessage.newInstance(
                        Token.newInstance(new byte[] { (byte) 0x02, (byte) 0x03 }));
        Assert.assertNotEquals(message1.hashCode(), message2.hashCode());
    }

    @Test
    public void testHashCode02() {
        ProtectionLevelNegotiationMessage message1 =
                ProtectionLevelNegotiationMessage.newInstance(
                        Token.newInstance(new byte[] { (byte) 0x02, (byte) 0x03 }));
        ProtectionLevelNegotiationMessage message2 =
                ProtectionLevelNegotiationMessage.newInstance(
                        Token.newInstance(new byte[] { (byte) 0x04, (byte) 0x05 }));
        Assert.assertNotEquals(message1.hashCode(), message2.hashCode());
    }

    @Test
    public void testHashCode03() {
        ProtectionLevelNegotiationMessage message1 =
                ProtectionLevelNegotiationMessage.newInstance(
                        Token.newInstance(new byte[] { (byte) 0x01 }));
        ProtectionLevelNegotiationMessage message2 =
                ProtectionLevelNegotiationMessage.newInstance(
                        Token.newInstance(new byte[] { (byte) 0x01 }));
        Assert.assertEquals(message1.hashCode(), message2.hashCode());
    }

    @Test
    public void testHashCode04() {
        ProtectionLevelNegotiationMessage message1 =
                ProtectionLevelNegotiationMessage.newInstance(
                        Token.newInstance(new byte[] { (byte) 0x02, (byte) 0x03 }));
        ProtectionLevelNegotiationMessage message2 =
                ProtectionLevelNegotiationMessage.newInstance(
                        Token.newInstance(new byte[] { (byte) 0x02, (byte) 0x03 }));
        Assert.assertEquals(message1.hashCode(), message2.hashCode());
    }

    @Test
    public void testToByteArray01() throws IOException {
        ProtectionLevelNegotiationMessage message1 =
                ProtectionLevelNegotiationMessage.newInstance(
                        Token.newInstance(new byte[] { (byte) 0x01 }));
        ProtectionLevelNegotiationMessage message2 =
                ProtectionLevelNegotiationMessage.newInstanceFrom(
                        new ByteArrayInputStream(message1.toByteArray()));
        Assert.assertEquals(message1, message2);
    }

    @Test
    public void testToByteArray02() throws IOException {
        ProtectionLevelNegotiationMessage message1 =
                ProtectionLevelNegotiationMessage.newInstance(
                        Token.newInstance(new byte[] { (byte) 0x02, (byte) 0x03 }));
        ProtectionLevelNegotiationMessage message2 =
                ProtectionLevelNegotiationMessage.newInstanceFrom(
                        new ByteArrayInputStream(message1.toByteArray()));
        Assert.assertEquals(message1, message2);
    }

}