package com.github.jh3nd3rs0n.jargyle.common.net;

import com.github.jh3nd3rs0n.jargyle.common.number.PositiveInteger;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.net.DatagramSocket;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;

public class StandardSocketSettingSpecSoRcvbufTest {

    @Test
    public void testApplyValueDatagramSocket() throws SocketException {
        DatagramSocket datagramSocket = new DatagramSocket(null);
        StandardSocketSettingSpecConstants.SO_RCVBUF.apply(
                PositiveInteger.valueOf(4000),
                datagramSocket);
        Assert.assertEquals(4000, datagramSocket.getReceiveBufferSize());
    }

    @Test
    public void testApplyValueServerSocket() throws IOException {
        ServerSocket serverSocket = new ServerSocket();
        StandardSocketSettingSpecConstants.SO_RCVBUF.apply(
                PositiveInteger.valueOf(4000),
                serverSocket);
        Assert.assertEquals(4000, serverSocket.getReceiveBufferSize());
    }

    @Test
    public void testApplyValueSocket() throws SocketException {
        Socket socket = new Socket();
        StandardSocketSettingSpecConstants.SO_RCVBUF.apply(
                PositiveInteger.valueOf(4000),
                socket);
        Assert.assertEquals(4000, socket.getReceiveBufferSize());
    }

    @Test
    public void testNewSocketSettingWithParsedValueString() {
        Assert.assertNotNull(
                StandardSocketSettingSpecConstants.SO_RCVBUF.newSocketSettingWithParsedValue(
                        "690"));
    }

}
