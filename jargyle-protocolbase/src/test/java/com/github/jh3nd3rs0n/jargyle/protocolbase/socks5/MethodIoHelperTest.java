package com.github.jh3nd3rs0n.jargyle.protocolbase.socks5;

import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.EOFException;
import java.io.IOException;

public class MethodIoHelperTest {

    @Test
    public void testReadMethodFromInputStream01() throws IOException {
        ByteArrayInputStream in = new ByteArrayInputStream(
                new byte[] { (byte) 0x00 });
        Method method = MethodIoHelper.readMethodFrom(in);
        Assert.assertEquals(Method.valueOfByte((byte) 0x00), method);
    }

    @Test
    public void testReadMethodFromInputStream02() throws IOException {
        ByteArrayInputStream in = new ByteArrayInputStream(
                new byte[] { (byte) 0x01 });
        Method method = MethodIoHelper.readMethodFrom(in);
        Assert.assertEquals(Method.valueOfByte((byte) 0x01), method);
    }

    @Test
    public void testReadMethodFromInputStream03() throws IOException {
        ByteArrayInputStream in = new ByteArrayInputStream(
                new byte[] { (byte) 0x02 });
        Method method = MethodIoHelper.readMethodFrom(in);
        Assert.assertEquals(Method.valueOfByte((byte) 0x02), method);
    }

    @Test
    public void testReadMethodFromInputStream04() throws IOException {
        ByteArrayInputStream in = new ByteArrayInputStream(
                new byte[] { (byte) 0xff });
        Method method = MethodIoHelper.readMethodFrom(in);
        Assert.assertEquals(Method.valueOfByte((byte) 0xff), method);
    }

    @Test(expected = IOException.class)
    public void testReadMethodFromInputStreamForIOException() throws IOException {
        ByteArrayInputStream in = new ByteArrayInputStream(
                new byte[] { (byte) 0xfe });
        MethodIoHelper.readMethodFrom(in);
    }

    @Test(expected = EOFException.class)
    public void testReadMethodFromInputStreamForEOFException() throws IOException {
        ByteArrayInputStream in = new ByteArrayInputStream(new byte[] { });
        MethodIoHelper.readMethodFrom(in);
    }

}