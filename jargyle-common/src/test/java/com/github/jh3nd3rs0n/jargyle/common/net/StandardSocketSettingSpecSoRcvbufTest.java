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
    public void testExtractDatagramSocket() throws SocketException {
        DatagramSocket datagramSocket = new DatagramSocket(null);
        datagramSocket.setReceiveBufferSize(3000);
        Assert.assertEquals(
                3000,
                StandardSocketSettingSpecConstants.SO_RCVBUF.extract(
                        datagramSocket).intValue());
    }

    @Test
    public void testExtractServerSocket() throws IOException {
        ServerSocket serverSocket = new ServerSocket();
        serverSocket.setReceiveBufferSize(1500);
        Assert.assertEquals(
                1500,
                StandardSocketSettingSpecConstants.SO_RCVBUF.extract(
                        serverSocket).intValue());
    }

    @Test
    public void testExtractSocket() throws SocketException {
        Socket socket = new Socket();
        socket.setReceiveBufferSize(1984);
        Assert.assertEquals(
                1984,
                StandardSocketSettingSpecConstants.SO_RCVBUF.extract(
                        socket).intValue());
    }

    @Test
    public void testNewSocketSettingWithParsedValueString() {
        Assert.assertNotNull(
                StandardSocketSettingSpecConstants.SO_RCVBUF.newSocketSettingWithParsedValue(
                        "690"));
    }

}
