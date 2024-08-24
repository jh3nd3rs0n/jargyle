package com.github.jh3nd3rs0n.jargyle.protocolbase.internal;

import com.github.jh3nd3rs0n.jargyle.common.number.UnsignedShort;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.EOFException;
import java.io.IOException;

public class UnsignedShortIoHelperTest {

    @Test
    public void testReadUnsignedShortFromInputStream01() throws IOException {
        ByteArrayInputStream in = new ByteArrayInputStream(
                new byte[] { (byte) 0x00, (byte) 0x00 });
        UnsignedShort unsignedShort =
                UnsignedShortIoHelper.readUnsignedShortFrom(in);
        Assert.assertEquals(UnsignedShort.valueOf(0), unsignedShort);
    }

    @Test
    public void testReadUnsignedShortFromInputStream02() throws IOException {
        ByteArrayInputStream in = new ByteArrayInputStream(
                new byte[] { (byte) 0x00, (byte) 0xff });
        UnsignedShort unsignedShort =
                UnsignedShortIoHelper.readUnsignedShortFrom(in);
        Assert.assertEquals(UnsignedShort.valueOf(255), unsignedShort);
    }

    @Test
    public void testReadUnsignedShortFromInputStream03() throws IOException {
        ByteArrayInputStream in = new ByteArrayInputStream(
                new byte[] { (byte) 0xff, (byte) 0xff });
        UnsignedShort unsignedShort =
                UnsignedShortIoHelper.readUnsignedShortFrom(in);
        Assert.assertEquals(UnsignedShort.valueOf(65535), unsignedShort);
    }

    @Test(expected = EOFException.class)
    public void testReadUnsignedShortFromInputStreamForEOFException01() throws IOException {
        UnsignedShortIoHelper.readUnsignedShortFrom(new ByteArrayInputStream(
                new byte[]{}));
    }

    @Test(expected = EOFException.class)
    public void testReadUnsignedShortFromInputStreamForEOFException02() throws IOException {
        UnsignedShortIoHelper.readUnsignedShortFrom(new ByteArrayInputStream(
                new byte[]{(byte) 0xff}));
    }

    @Test
    public void testToByteArrayUnsignedShort01() {
        UnsignedShort unsignedShort = UnsignedShort.valueOf(0);
        Assert.assertArrayEquals(
                new byte[]{0x00, 0x00},
                UnsignedShortIoHelper.toByteArray(unsignedShort));
    }

    @Test
    public void testToByteArrayUnsignedShort02() {
        UnsignedShort unsignedShort = UnsignedShort.valueOf(255);
        Assert.assertArrayEquals(
                new byte[]{0x00, (byte) 0xff},
                UnsignedShortIoHelper.toByteArray(unsignedShort));
    }

    @Test
    public void testToByteArrayUnsignedShort03() {
        UnsignedShort unsignedShort = UnsignedShort.valueOf(65535);
        Assert.assertArrayEquals(
                new byte[]{(byte) 0xff, (byte) 0xff},
                UnsignedShortIoHelper.toByteArray(unsignedShort));
    }

    @Test
    public void testToUnsignedShortByteArray01() {
        UnsignedShort unsignedShort = UnsignedShort.valueOf(0);
        Assert.assertEquals(
                unsignedShort,
                UnsignedShortIoHelper.toUnsignedShort(new byte[]{0x00, 0x00}));
    }

    @Test
    public void testToUnsignedShortByteArray02() {
        UnsignedShort unsignedShort = UnsignedShort.valueOf(255);
        Assert.assertEquals(
                unsignedShort,
                UnsignedShortIoHelper.toUnsignedShort(
                        new byte[]{0x00, (byte) 0xff}));
    }

    @Test
    public void testToUnsignedShortByteArray03() {
        UnsignedShort unsignedShort = UnsignedShort.valueOf(65535);
        Assert.assertEquals(
                unsignedShort,
                UnsignedShortIoHelper.toUnsignedShort(
                        new byte[]{(byte) 0xff, (byte) 0xff}));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToUnsignedShortByteArrayForIllegalArgumentException01() {
        UnsignedShortIoHelper.toUnsignedShort(new byte[]{});
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToUnsignedShortByteArrayForIllegalArgumentException02() {
        UnsignedShortIoHelper.toUnsignedShort(new byte[]{0x00});
    }

}