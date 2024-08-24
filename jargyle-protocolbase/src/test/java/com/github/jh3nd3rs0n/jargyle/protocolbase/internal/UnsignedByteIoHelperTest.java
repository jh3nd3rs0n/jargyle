package com.github.jh3nd3rs0n.jargyle.protocolbase.internal;

import com.github.jh3nd3rs0n.jargyle.common.number.UnsignedByte;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.EOFException;
import java.io.IOException;

import static org.junit.Assert.*;

public class UnsignedByteIoHelperTest {

    @Test
    public void testReadUnsignedByteFromInputStream01() throws IOException {
        ByteArrayInputStream in = new ByteArrayInputStream(
                new byte[] { (byte) 0x00 });
        UnsignedByte unsignedByte = UnsignedByteIoHelper.readUnsignedByteFrom(
                in);
        Assert.assertEquals(UnsignedByte.valueOf((byte) 0x00), unsignedByte);
    }

    @Test
    public void testReadUnsignedByteFromInputStream02() throws IOException {
        ByteArrayInputStream in = new ByteArrayInputStream(
                new byte[] { (byte) 0x70 });
        UnsignedByte unsignedByte = UnsignedByteIoHelper.readUnsignedByteFrom(
                in);
        Assert.assertEquals(UnsignedByte.valueOf((byte) 0x70), unsignedByte);
    }

    @Test
    public void testReadUnsignedByteFromInputStream03() throws IOException {
        ByteArrayInputStream in = new ByteArrayInputStream(
                new byte[] { (byte) 0xff });
        UnsignedByte unsignedByte = UnsignedByteIoHelper.readUnsignedByteFrom(
                in);
        Assert.assertEquals(UnsignedByte.valueOf((byte) 0xff), unsignedByte);
    }

    @Test(expected = EOFException.class)
    public void testReadUnsignedByteFromInputStreamForEOFException() throws IOException {
        UnsignedByteIoHelper.readUnsignedByteFrom(new ByteArrayInputStream(
                new byte[] { }));
    }

}