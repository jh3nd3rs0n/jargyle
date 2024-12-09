package com.github.jh3nd3rs0n.jargyle.protocolbase.socks5;

import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.EOFException;
import java.io.IOException;

public class AddressTest {

    @Test
    public void testNewInstanceFromInputStreamInputStream01() throws IOException {
        Assert.assertNotNull(Address.newInstanceFrom(new ByteArrayInputStream(new byte[] {
                (byte) 0x01,
                (byte) 0x7f,
                (byte) 0x00,
                (byte) 0x00,
                (byte) 0x01 })));
    }

    @Test
    public void testNewInstanceFromInputStreamInputStream02() throws IOException {
        Assert.assertNotNull(Address.newInstanceFrom(new ByteArrayInputStream(new byte[] {
                (byte) 0x04,
                (byte) 0x00,
                (byte) 0x00,
                (byte) 0x00,
                (byte) 0x00,
                (byte) 0x00,
                (byte) 0x00,
                (byte) 0x00,
                (byte) 0x00,
                (byte) 0x00,
                (byte) 0x00,
                (byte) 0x00,
                (byte) 0x00,
                (byte) 0x00,
                (byte) 0x00,
                (byte) 0x00,
                (byte) 0x01 })));
    }

    @Test
    public void testNewInstanceFromInputStreamInputStream03() throws IOException {
        Assert.assertNotNull(Address.newInstanceFrom(new ByteArrayInputStream(new byte[] {
                (byte) 0x03,
                (byte) 0x09,
                (byte) 0x6c,
                (byte) 0x6f,
                (byte) 0x63,
                (byte) 0x61,
                (byte) 0x6c,
                (byte) 0x68,
                (byte) 0x6f,
                (byte) 0x73,
                (byte) 0x74 })));
    }

    @Test(expected = EOFException.class)
    public void testNewInstanceFromInputStreamInputStreamForEOFException01() throws IOException {
        Address.newInstanceFrom(new ByteArrayInputStream(new byte[] {}));
    }

    @Test(expected = EOFException.class)
    public void testNewInstanceFromInputStreamInputStreamForEOFException02() throws IOException {
        Address.newInstanceFrom(new ByteArrayInputStream(new byte[] { (byte) 0x03 }));
    }

    @Test(expected = IOException.class)
    public void testNewInstanceFromInputStreamInputStreamForIOException01() throws IOException {
        Address.newInstanceFrom(new ByteArrayInputStream(new byte[] { (byte) 0x00 }));
    }

    @Test(expected = IOException.class)
    public void testNewInstanceFromInputStreamInputStreamForIOException02() throws IOException {
        Address.newInstanceFrom(new ByteArrayInputStream(new byte[] { (byte) 0xff }));
    }

    @Test(expected = IOException.class)
    public void testNewInstanceFromInputStreamInputStreamForIOException03() throws IOException {
        Address.newInstanceFrom(new ByteArrayInputStream(new byte[] {
                (byte) 0x03,
                (byte) 0x00 }));
    }

    @Test(expected = IOException.class)
    public void testNewInstanceFromInputStreamInputStreamForIOException04() throws IOException {
        Address.newInstanceFrom(new ByteArrayInputStream(new byte[] {
                (byte) 0x03,
                (byte) 0x01 }));
    }

    @Test(expected = IOException.class)
    public void testNewInstanceFromInputStreamInputStreamForIOException05() throws IOException {
        Address.newInstanceFrom(new ByteArrayInputStream(new byte[] {
                (byte) 0x03,
                (byte) 0x01,
                (byte) 0x00 }));
    }

    @Test(expected = IOException.class)
    public void testNewInstanceFromInputStreamInputStreamForIOException06() throws IOException {
        Address.newInstanceFrom(new ByteArrayInputStream(new byte[] {
                (byte) 0x03,
                (byte) 0x02,
                (byte) 0x00 }));
    }

    @Test(expected = IOException.class)
    public void testNewInstanceFromInputStreamInputStreamForIOException07() throws IOException {
        Address.newInstanceFrom(new ByteArrayInputStream(new byte[] {
                (byte) 0x01 }));
    }

    @Test(expected = IOException.class)
    public void testNewInstanceFromInputStreamInputStreamForIOException08() throws IOException {
        Address.newInstanceFrom(new ByteArrayInputStream(new byte[] {
                (byte) 0x01,
                (byte) 0x45 }));
    }

    @Test(expected = IOException.class)
    public void testNewInstanceFromInputStreamInputStreamForIOException09() throws IOException {
        Address.newInstanceFrom(new ByteArrayInputStream(new byte[] {
                (byte) 0x04 }));
    }

    @Test(expected = IOException.class)
    public void testNewInstanceFromInputStreamInputStreamForIOException10() throws IOException {
        Address.newInstanceFrom(new ByteArrayInputStream(new byte[] {
                (byte) 0x04,
                (byte) 0x9f }));
    }

    @Test
    public void testNewInstanceFromStringString01() {
        Assert.assertNotNull(Address.newInstanceFrom("localhost"));
    }

    @Test
    public void testNewInstanceFromStringString02() {
        Assert.assertNotNull(Address.newInstanceFrom("127.0.0.1"));
    }

    @Test
    public void testNewInstanceFromStringString03() {
        Assert.assertNotNull(Address.newInstanceFrom("::1"));
    }

    @Test
    public void testNewInstanceFromStringString04() {
        Assert.assertNotNull(Address.newInstanceFrom("0:0:0:0:0:0:0:1"));
    }

    @Test
    public void testNewInstanceFromStringString05() {
        Assert.assertNotNull(Address.newInstanceFrom("0000:0000:0000:0000:0000:0000:0000:0001"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNewInstanceFromStringStringForIllegalArgumentException01() {
        Address.newInstanceFrom("This is not an address");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNewInstanceFromStringStringForIllegalArgumentException02() {
        Address.newInstanceFrom("@@@@@@@@");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNewInstanceFromStringStringForIllegalArgumentException03() {
        Address.newInstanceFrom("0:0:0:0:0:0:0:0:0:0:0:0");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNewInstanceFromStringStringForIllegalArgumentException04() {
        Address.newInstanceFrom("2google.");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNewInstanceFromStringStringForIllegalArgumentException05() {
        Address.newInstanceFrom(".com");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNewInstanceFromStringStringForIllegalArgumentException06() {
        Address.newInstanceFrom("127.0.0.");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNewInstanceFromStringStringForIllegalArgumentException07() {
        Address.newInstanceFrom(":0:0:0");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNewInstanceFromStringStringForIllegalArgumentException08() {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < 64; i++) {
            stringBuilder.append("abcdefghijklmnopqrstuvwxyz0123456789");
        }
        stringBuilder.append(".com");
        Address.newInstanceFrom(stringBuilder.toString());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNewInstanceFromStringStringForIllegalArgumentException09() {
        Address.newInstanceFrom("");
    }


    @Test
    public void testEqualsObject01() {
        Address address = Address.newInstanceFrom("127.0.0.1");
        Assert.assertEquals(address, address);
    }

    @Test
    public void testEqualsObject02() {
        Address address = Address.newInstanceFrom("127.0.0.1");
        Assert.assertNotEquals(address, null);
    }

    @Test
    public void testEqualsObject03() {
        Object obj1 = Address.newInstanceFrom("127.0.0.1");
        Object obj2 = new Object();
        Assert.assertNotEquals(obj1, obj2);
    }

    @Test
    public void testEqualsObject04() {
        Address address1 = Address.newInstanceFrom("127.0.0.1");
        Address address2 = Address.newInstanceFrom("::1");
        Assert.assertNotEquals(address1, address2);
    }

    @Test
    public void testEqualsObject05() {
        Address address1 = Address.newInstanceFrom("127.0.0.1");
        Address address2 = Address.newInstanceFrom("127.0.0.1");
        Assert.assertEquals(address1, address2);
    }

    @Test
    public void testEqualsObject06() {
        Address address1 = Address.newInstanceFrom("::1");
        Address address2 = Address.newInstanceFrom("0:0:0:0:0:0:0:1");
        Assert.assertEquals(address1, address2);
    }

    @Test
    public void testHashCode01() {
        Address address1 = Address.newInstanceFrom("127.0.0.1");
        Address address2 = Address.newInstanceFrom("::1");
        Assert.assertNotEquals(address1.hashCode(), address2.hashCode());
    }

    @Test
    public void testHashCode02() {
        Address address1 = Address.newInstanceFrom("127.0.0.1");
        Address address2 = Address.newInstanceFrom("127.0.0.1");
        Assert.assertEquals(address1.hashCode(), address2.hashCode());
    }

    @Test
    public void testHashCode03() {
        Address address1 = Address.newInstanceFrom("::1");
        Address address2 = Address.newInstanceFrom("0:0:0:0:0:0:0:1");
        Assert.assertEquals(address1.hashCode(), address2.hashCode());
    }

    @Test
    public void testToByteArray01() throws IOException {
        Address address1 = Address.newInstanceFrom("localhost");
        Address address2 = Address.newInstanceFrom(
                new ByteArrayInputStream(address1.toByteArray()));
        Assert.assertEquals(address1, address2);
    }

    @Test
    public void testToByteArray02() throws IOException {
        Address address1 = Address.newInstanceFrom("127.0.0.1");
        Address address2 = Address.newInstanceFrom(
                new ByteArrayInputStream(address1.toByteArray()));
        Assert.assertEquals(address1, address2);
    }

    @Test
    public void testToByteArray03() throws IOException {
        Address address1 = Address.newInstanceFrom("0:0:0:0:0:0:0:1");
        Address address2 = Address.newInstanceFrom(
                new ByteArrayInputStream(address1.toByteArray()));
        Assert.assertEquals(address1, address2);
    }

    @Test
    public void testToByteArray04() throws IOException {
        Address address1 = Address.newInstanceFrom("github.com");
        Address address2 = Address.newInstanceFrom(
                new ByteArrayInputStream(address1.toByteArray()));
        Assert.assertEquals(address1, address2);
    }

    @Test
    public void testToByteArray05() throws IOException {
        Address address1 = Address.newInstanceFrom("google.com");
        Address address2 = Address.newInstanceFrom(
                new ByteArrayInputStream(address1.toByteArray()));
        Assert.assertEquals(address1, address2);
    }

    @Test
    public void testToByteArray06() throws IOException {
        Address address1 = Address.newInstanceFrom("news.google.com");
        Address address2 = Address.newInstanceFrom(
                new ByteArrayInputStream(address1.toByteArray()));
        Assert.assertEquals(address1, address2);
    }

    @Test
    public void testToByteArray07() throws IOException {
        Address address1 = Address.newInstanceFrom("0.0.0.0");
        Address address2 = Address.newInstanceFrom(
                new ByteArrayInputStream(address1.toByteArray()));
        Assert.assertEquals(address1, address2);
    }

    @Test
    public void testToByteArray08() throws IOException {
        Address address1 = Address.newInstanceFrom("0:0:0:0:0:0:0:0");
        Address address2 = Address.newInstanceFrom(
                new ByteArrayInputStream(address1.toByteArray()));
        Assert.assertEquals(address1, address2);
    }

    @Test
    public void testToByteArray09() throws IOException {
        Address address1 = Address.newInstanceFrom("255.255.255.255");
        Address address2 = Address.newInstanceFrom(
                new ByteArrayInputStream(address1.toByteArray()));
        Assert.assertEquals(address1, address2);
    }

    @Test
    public void testToByteArray10() throws IOException {
        Address address1 = Address.newInstanceFrom(
                "ffff:ffff:ffff:ffff:ffff:ffff:ffff:ffff");
        Address address2 = Address.newInstanceFrom(
                new ByteArrayInputStream(address1.toByteArray()));
        Assert.assertEquals(address1, address2);
    }

    @Test
    public void testToByteArray11() throws IOException {
        Address address1 = Address.newInstanceFrom("255.200.100.0");
        Address address2 = Address.newInstanceFrom(
                new ByteArrayInputStream(address1.toByteArray()));
        Assert.assertEquals(address1, address2);
    }

    @Test
    public void testToByteArray12() throws IOException {
        Address address1 = Address.newInstanceFrom("1234:ab:cd:ef:fe:dc:ba:6789");
        Address address2 = Address.newInstanceFrom(
                new ByteArrayInputStream(address1.toByteArray()));
        Assert.assertEquals(address1, address2);
    }

    @Test
    public void testToString01() {
        Address address = Address.newInstanceFrom("localhost");
        Assert.assertEquals("localhost", address.toString());
    }

    @Test
    public void testToString02() {
        Address address = Address.newInstanceFrom("127.0.0.1");
        Assert.assertEquals("127.0.0.1", address.toString());
    }

    @Test
    public void testToString03() {
        Address address = Address.newInstanceFrom("::1");
        Assert.assertEquals("0:0:0:0:0:0:0:1", address.toString());
    }

}