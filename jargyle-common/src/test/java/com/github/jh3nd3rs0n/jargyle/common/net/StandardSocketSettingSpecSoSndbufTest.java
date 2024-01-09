package com.github.jh3nd3rs0n.jargyle.common.net;

import com.github.jh3nd3rs0n.jargyle.common.number.PositiveInteger;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.net.DatagramSocket;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;

public class StandardSocketSettingSpecSoSndbufTest {

    @Test
    public void testApplyValueDatagramSocket() throws SocketException {
        DatagramSocket datagramSocket = new DatagramSocket(null);
        StandardSocketSettingSpecConstants.SO_SNDBUF.apply(
                PositiveInteger.newInstanceOf(7240),
                datagramSocket);
        Assert.assertEquals(7240, datagramSocket.getSendBufferSize());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testApplyValueServerSocket() throws IOException {
        StandardSocketSettingSpecConstants.SO_SNDBUF.apply(
                PositiveInteger.newInstanceOf(7240),
                new ServerSocket());
    }

    @Test
    public void testApplyValueSocket() throws SocketException {
        Socket socket = new Socket();
        StandardSocketSettingSpecConstants.SO_SNDBUF.apply(
                PositiveInteger.newInstanceOf(7240),
                socket);
        Assert.assertEquals(7240, socket.getSendBufferSize());
    }

    @Test
    public void testNewSocketSettingWithParsedValueString() {
        Assert.assertNotNull(
                StandardSocketSettingSpecConstants.SO_SNDBUF.newSocketSettingWithParsedValue(
                        "7240"));
    }

}
