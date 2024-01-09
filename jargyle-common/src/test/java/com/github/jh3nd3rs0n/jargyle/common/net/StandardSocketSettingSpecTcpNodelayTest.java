package com.github.jh3nd3rs0n.jargyle.common.net;

import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.net.DatagramSocket;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;

public class StandardSocketSettingSpecTcpNodelayTest {

    @Test(expected = UnsupportedOperationException.class)
    public void testApplyValueDatagramSocketForUnsupportedOperationException() throws SocketException {
        StandardSocketSettingSpecConstants.TCP_NODELAY.apply(
                Boolean.TRUE, new DatagramSocket(null));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testApplyValueServerSocket() throws IOException {
        StandardSocketSettingSpecConstants.TCP_NODELAY.apply(
                Boolean.TRUE, new ServerSocket());
    }

    @Test
    public void testApplyValueSocket() throws SocketException {
        Socket socket = new Socket();
        StandardSocketSettingSpecConstants.TCP_NODELAY.apply(
                Boolean.TRUE, socket);
        Assert.assertTrue(socket.getTcpNoDelay());
    }

    @Test
    public void testNewSocketSettingWithParsedValueString() {
        Assert.assertNotNull(
                StandardSocketSettingSpecConstants.TCP_NODELAY.newSocketSettingWithParsedValue(
                        "true"));
    }

}
