package com.github.jh3nd3rs0n.jargyle.protocolbase.socks5;

import com.github.jh3nd3rs0n.jargyle.common.net.Port;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.EOFException;
import java.io.IOException;

public class PortIoHelperTest {

    @Test
    public void testReadPortFromInputStream01() throws IOException {
        ByteArrayInputStream in = new ByteArrayInputStream(
                new byte[] { (byte) 0x00, (byte) 0x00 });
        Port port = PortIoHelper.readPortFrom(in);
        Assert.assertEquals(Port.valueOf(0), port);
    }

    @Test
    public void testReadPortFromInputStream02() throws IOException {
        ByteArrayInputStream in = new ByteArrayInputStream(
                new byte[] { (byte) 0x00, (byte) 0xff });
        Port port = PortIoHelper.readPortFrom(in);
        Assert.assertEquals(Port.valueOf(255), port);
    }

    @Test
    public void testReadPortFromInputStream03() throws IOException {
        ByteArrayInputStream in = new ByteArrayInputStream(
                new byte[] { (byte) 0xff, (byte) 0xff });
        Port port = PortIoHelper.readPortFrom(in);
        Assert.assertEquals(Port.valueOf(65535), port);
    }

    @Test(expected = EOFException.class)
    public void testReadPortFromInputStreamForEOFException01() throws IOException {
        PortIoHelper.readPortFrom(new ByteArrayInputStream(
                new byte[]{}));
    }

    @Test(expected = EOFException.class)
    public void testReadPortFromInputStreamForEOFException02() throws IOException {
        PortIoHelper.readPortFrom(new ByteArrayInputStream(
                new byte[]{(byte) 0xff}));
    }

    @Test
    public void testToByteArrayPort01() {
        Port port = Port.valueOf(0);
        Assert.assertArrayEquals(
                new byte[]{0x00, 0x00},
                PortIoHelper.toByteArray(port));
    }

    @Test
    public void testToByteArrayPort02() {
        Port port = Port.valueOf(255);
        Assert.assertArrayEquals(
                new byte[]{0x00, (byte) 0xff},
                PortIoHelper.toByteArray(port));
    }

    @Test
    public void testToByteArrayPort03() {
        Port port = Port.valueOf(65535);
        Assert.assertArrayEquals(
                new byte[]{(byte) 0xff, (byte) 0xff},
                PortIoHelper.toByteArray(port));
    }

}