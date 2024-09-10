package com.github.jh3nd3rs0n.jargyle.common.net;

import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.net.DatagramSocket;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;

public class StandardSocketSettingSpecSoLingerTest {

    @Test(expected = UnsupportedOperationException.class)
    public void testApplyValueDatagramSocket() throws SocketException {
        StandardSocketSettingSpecConstants.SO_LINGER.apply(
                234,
                new DatagramSocket(null));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testApplyValueServerSocket() throws IOException {
        StandardSocketSettingSpecConstants.SO_LINGER.apply(
                234,
                new ServerSocket());
    }

    @Test
    public void testApplyValueSocket01() throws SocketException {
        Socket socket = new Socket();
        StandardSocketSettingSpecConstants.SO_LINGER.apply(
                234,
                socket);
        Assert.assertEquals(234, socket.getSoLinger());
    }

    @Test
    public void testApplyValueSocket02() throws SocketException {
        Socket socket = new Socket();
        StandardSocketSettingSpecConstants.SO_LINGER.apply(
                -234,
                socket);
        Assert.assertEquals(-1, socket.getSoLinger());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testExtractDatagramSocketForUnsupportedOperationException() throws SocketException {
        StandardSocketSettingSpecConstants.SO_LINGER.extract(
                new DatagramSocket(null));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testExtractServerSocketForUnsupportedOperationException() throws IOException {
        StandardSocketSettingSpecConstants.SO_LINGER.extract(
                new ServerSocket());
    }

    @Test
    public void testExtractSocket01() throws SocketException {
        Socket socket = new Socket();
        socket.setSoLinger(true, 12);
        Assert.assertEquals(
                12,
                StandardSocketSettingSpecConstants.SO_LINGER.extract(
                        socket).intValue());
    }

    @Test
    public void testExtractSocket02() throws SocketException {
        Socket socket = new Socket();
        socket.setSoLinger(false, -12);
        Assert.assertEquals(
                -1,
                StandardSocketSettingSpecConstants.SO_LINGER.extract(
                        socket).intValue());
    }

    @Test
    public void testNewSocketSettingWithParsedValueString() {
        Assert.assertNotNull(
                StandardSocketSettingSpecConstants.SO_LINGER.newSocketSettingWithParsedValue(
                        "456"));
    }


}
