package com.github.jh3nd3rs0n.jargyle.protocolbase.socks5.gssapiauthmethod;

import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;

public class EncapsulatedUserDataMessageTest {

    @Test(expected = IOException.class)
    public void testNewInstanceFromInputStreamForIOException01() throws IOException {
        EncapsulatedUserDataMessage.newInstanceFrom(new ByteArrayInputStream(new byte[] {
                (byte) 0x00 }));
    }

    @Test(expected = IOException.class)
    public void testNewInstanceFromInputStreamForIOException02() throws IOException {
        EncapsulatedUserDataMessage.newInstanceFrom(new ByteArrayInputStream(new byte[] {
                (byte) 0x01, (byte) 0x02 }));
    }

    @Test(expected = IOException.class)
    public void testNewInstanceFromInputStreamForIOException03() throws IOException {
        EncapsulatedUserDataMessage.newInstanceFrom(new ByteArrayInputStream(new byte[] {
                (byte) 0x01, (byte) 0x04 }));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNewInstanceFromByteArrayForIllegalArgumentException01() {
        EncapsulatedUserDataMessage.newInstanceFrom(new byte[] {
                (byte) 0x00 });
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNewInstanceFromByteArrayForIllegalArgumentException02() {
        EncapsulatedUserDataMessage.newInstanceFrom(new byte[] {
                (byte) 0x01 });
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNewInstanceFromByteArrayForIllegalArgumentException03() {
        EncapsulatedUserDataMessage.newInstanceFrom(new byte[] {
                (byte) 0x01, (byte) 0x02 });
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNewInstanceFromByteArrayForIllegalArgumentException04() {
        EncapsulatedUserDataMessage.newInstanceFrom(new byte[] {
                (byte) 0x01, (byte) 0x04 });
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNewInstanceFromByteArrayForIllegalArgumentException05() {
        EncapsulatedUserDataMessage.newInstanceFrom(new byte[] {
                (byte) 0x01, (byte) 0x03 });
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNewInstanceFromByteArrayForIllegalArgumentException06() {
        EncapsulatedUserDataMessage.newInstanceFrom(new byte[] {
                (byte) 0x01, (byte) 0x03, (byte) 0x00, (byte) 0x01, (byte) 0x01, (byte) 0x02 });
    }

    @Test
    public void testEqualsObject01() {
        EncapsulatedUserDataMessage message = EncapsulatedUserDataMessage.newInstance(
                Token.newInstance(new byte[] { (byte) 0x01 }));
        Assert.assertEquals(message, message);
    }

    @Test
    public void testEqualsObject02() {
        EncapsulatedUserDataMessage message = EncapsulatedUserDataMessage.newInstance(
                Token.newInstance(new byte[] { (byte) 0x01 }));
        Assert.assertNotEquals(message, null);
    }

    @Test
    public void testEqualsObject03() {
        Object obj1 = EncapsulatedUserDataMessage.newInstance(
                Token.newInstance(new byte[] { (byte) 0x01 }));
        Object obj2 = new Object();
        Assert.assertNotEquals(obj1, obj2);
    }

    @Test
    public void testEqualsObject04() {
        EncapsulatedUserDataMessage message1 = EncapsulatedUserDataMessage.newInstance(
                Token.newInstance(new byte[] { (byte) 0x01 }));
        EncapsulatedUserDataMessage message2 = EncapsulatedUserDataMessage.newInstance(
                Token.newInstance(new byte[] { (byte) 0x02, (byte) 0x03 }));
        Assert.assertNotEquals(message1, message2);
    }

    @Test
    public void testEqualsObject05() {
        EncapsulatedUserDataMessage message1 = EncapsulatedUserDataMessage.newInstance(
                Token.newInstance(new byte[] { (byte) 0x02, (byte) 0x03 }));
        EncapsulatedUserDataMessage message2 = EncapsulatedUserDataMessage.newInstance(
                Token.newInstance(new byte[] { (byte) 0x04, (byte) 0x05 }));
        Assert.assertNotEquals(message1, message2);
    }

    @Test
    public void testEqualsObject06() {
        EncapsulatedUserDataMessage message1 = EncapsulatedUserDataMessage.newInstance(
                Token.newInstance(new byte[] { (byte) 0x01 }));
        EncapsulatedUserDataMessage message2 = EncapsulatedUserDataMessage.newInstance(
                Token.newInstance(new byte[] { (byte) 0x01 }));
        Assert.assertEquals(message1, message2);
    }

    @Test
    public void testEqualsObject07() {
        EncapsulatedUserDataMessage message1 = EncapsulatedUserDataMessage.newInstance(
                Token.newInstance(new byte[] { (byte) 0x02, (byte) 0x03 }));
        EncapsulatedUserDataMessage message2 = EncapsulatedUserDataMessage.newInstance(
                Token.newInstance(new byte[] { (byte) 0x02, (byte) 0x03 }));
        Assert.assertEquals(message1, message2);
    }

    @Test
    public void testHashCode01() {
        EncapsulatedUserDataMessage message1 = EncapsulatedUserDataMessage.newInstance(
                Token.newInstance(new byte[] { (byte) 0x01 }));
        EncapsulatedUserDataMessage message2 = EncapsulatedUserDataMessage.newInstance(
                Token.newInstance(new byte[] { (byte) 0x02, (byte) 0x03 }));
        Assert.assertNotEquals(message1.hashCode(), message2.hashCode());
    }

    @Test
    public void testHashCode02() {
        EncapsulatedUserDataMessage message1 = EncapsulatedUserDataMessage.newInstance(
                Token.newInstance(new byte[] { (byte) 0x02, (byte) 0x03 }));
        EncapsulatedUserDataMessage message2 = EncapsulatedUserDataMessage.newInstance(
                Token.newInstance(new byte[] { (byte) 0x04, (byte) 0x05 }));
        Assert.assertNotEquals(message1.hashCode(), message2.hashCode());
    }

    @Test
    public void testHashCode03() {
        EncapsulatedUserDataMessage message1 = EncapsulatedUserDataMessage.newInstance(
                Token.newInstance(new byte[] { (byte) 0x01 }));
        EncapsulatedUserDataMessage message2 = EncapsulatedUserDataMessage.newInstance(
                Token.newInstance(new byte[] { (byte) 0x01 }));
        Assert.assertEquals(message1.hashCode(), message2.hashCode());
    }

    @Test
    public void testHashCode04() {
        EncapsulatedUserDataMessage message1 = EncapsulatedUserDataMessage.newInstance(
                Token.newInstance(new byte[] { (byte) 0x02, (byte) 0x03 }));
        EncapsulatedUserDataMessage message2 = EncapsulatedUserDataMessage.newInstance(
                Token.newInstance(new byte[] { (byte) 0x02, (byte) 0x03 }));
        Assert.assertEquals(message1.hashCode(), message2.hashCode());
    }

    @Test
    public void testToByteArray01() throws IOException {
        EncapsulatedUserDataMessage message1 = EncapsulatedUserDataMessage.newInstance(
                Token.newInstance(new byte[] { (byte) 0x01 }));
        EncapsulatedUserDataMessage message2 =
                EncapsulatedUserDataMessage.newInstanceFrom(message1.toByteArray());
        Assert.assertEquals(message1, message2);
    }

    @Test
    public void testToByteArray02() throws IOException {
        EncapsulatedUserDataMessage message1 = EncapsulatedUserDataMessage.newInstance(
                Token.newInstance(new byte[] { (byte) 0x02, (byte) 0x03 }));
        EncapsulatedUserDataMessage message2 =
                EncapsulatedUserDataMessage.newInstanceFrom(message1.toByteArray());
        Assert.assertEquals(message1, message2);
    }

}