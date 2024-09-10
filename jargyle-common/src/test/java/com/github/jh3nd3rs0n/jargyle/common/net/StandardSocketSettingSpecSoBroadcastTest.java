package com.github.jh3nd3rs0n.jargyle.common.net;

import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.net.DatagramSocket;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;

public class StandardSocketSettingSpecSoBroadcastTest {

    @Test
    public void testApplyValueDatagramSocket() throws SocketException {
        DatagramSocket datagramSocket = new DatagramSocket(null);
        StandardSocketSettingSpecConstants.SO_BROADCAST.apply(
                Boolean.TRUE, datagramSocket);
        Assert.assertTrue(datagramSocket.getBroadcast());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testApplyValueServerSocketForUnsupportedOperationException() throws IOException {
        StandardSocketSettingSpecConstants.SO_BROADCAST.apply(
                Boolean.TRUE, new ServerSocket());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testApplyValueSocketForUnsupportedOperationException() throws SocketException {
        StandardSocketSettingSpecConstants.SO_BROADCAST.apply(
                Boolean.TRUE, new Socket());
    }

    @Test
    public void testExtractDatagramSocket() throws SocketException {
        DatagramSocket datagramSocket = new DatagramSocket(null);
        datagramSocket.setBroadcast(true);
        Assert.assertTrue(StandardSocketSettingSpecConstants.SO_BROADCAST.extract(
                datagramSocket));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testExtractServerSocketForUnsupportedOperationException() throws IOException {
        StandardSocketSettingSpecConstants.SO_BROADCAST.extract(
                new ServerSocket());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testExtractSocketForUnsupportedOperationException() throws IOException {
        StandardSocketSettingSpecConstants.SO_BROADCAST.extract(
                new Socket());
    }

    @Test
    public void testNewSocketSettingWithParsedValueString() {
        Assert.assertNotNull(
                StandardSocketSettingSpecConstants.SO_BROADCAST.newSocketSettingWithParsedValue(
                        "true"));
    }

}
