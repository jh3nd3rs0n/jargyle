package com.github.jh3nd3rs0n.jargyle.common.net;

import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.net.DatagramSocket;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;

public class StandardSocketSettingSpecSoKeepaliveTest {

    @Test(expected = UnsupportedOperationException.class)
    public void testApplyValueDatagramSocketForUnsupportedOperationException() throws SocketException {
        StandardSocketSettingSpecConstants.SO_KEEPALIVE.apply(
                Boolean.TRUE, new DatagramSocket(null));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testApplyValueServerSocket() throws IOException {
        StandardSocketSettingSpecConstants.SO_KEEPALIVE.apply(
                Boolean.TRUE, new ServerSocket());
    }

    @Test
    public void testApplyValueSocket() throws SocketException {
        Socket socket = new Socket();
        StandardSocketSettingSpecConstants.SO_KEEPALIVE.apply(
                Boolean.TRUE, socket);
        Assert.assertTrue(socket.getKeepAlive());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testExtractDatagramSocketForUnsupportedOperationException() throws SocketException {
        StandardSocketSettingSpecConstants.SO_KEEPALIVE.extract(
                new DatagramSocket(null));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testExtractServerSocketForUnsupportedOperationException() throws IOException {
        StandardSocketSettingSpecConstants.SO_KEEPALIVE.extract(
                new ServerSocket());
    }

    @Test
    public void testExtractSocket() throws SocketException {
        Socket socket = new Socket();
        socket.setKeepAlive(true);
        Assert.assertTrue(StandardSocketSettingSpecConstants.SO_KEEPALIVE.extract(
                socket));
    }

    @Test
    public void testNewSocketSettingWithParsedValueString() {
        Assert.assertNotNull(
                StandardSocketSettingSpecConstants.SO_KEEPALIVE.newSocketSettingWithParsedValue(
                        "true"));
    }

}
