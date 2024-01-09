package com.github.jh3nd3rs0n.jargyle.common.net;

import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.net.DatagramSocket;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;

public class SocketSettingTest {

    @Test
    public void testNewInstanceOfString() {
        Assert.assertNotNull(SocketSetting.newInstanceOf("SO_TIMEOUT=1000"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNewInstanceOfStringForIllegalArgumentException01() {
        SocketSetting.newInstanceOf("SO_TIMEOUT:1000");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNewInstanceOfStringForIllegalArgumentException02() {
        SocketSetting.newInstanceOf("so_timeout=1000");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNewInstanceOfStringForIllegalArgumentException03() {
        SocketSetting.newInstanceOf("SO_TIMEOUT=1_000");
    }

    @Test
    public void testNewInstanceOfStringType() {
        Assert.assertNotNull(SocketSetting.newInstance(
                "SO_REUSEADDR", Boolean.TRUE));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNewInstanceOfStringTypeForIllegalArgumentException() {
        Assert.assertNotNull(SocketSetting.newInstance(
                "so_reuseaddr", Boolean.TRUE));
    }

    @Test
    public void testNewInstanceOfStringTypeString() {
        Assert.assertNotNull(SocketSetting.newInstance(
                "TCP_NODELAY", Boolean.TRUE, "Disable Nagle's algorithm"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNewInstanceOfStringTypeStringForIllegalArgumentException() {
        Assert.assertNotNull(SocketSetting.newInstance(
                "tcp_no_delay", Boolean.TRUE, "Disable Nagle's algorithm"));
    }

    @Test
    public void testNewInstanceWithParsedValueStringString() {
        Assert.assertNotNull(SocketSetting.newInstanceWithParsedValue(
                "SO_SNDBUF", "2000"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNewInstanceWithParsedValueStringStringForIllegalArgumentException01() {
        SocketSetting.newInstanceWithParsedValue("so_sndbuf", "2000");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNewInstanceWithParsedValueStringStringForIllegalArgumentException02() {
        SocketSetting.newInstanceWithParsedValue("SO_SNDBUF", "2_000");
    }

    @Test
    public void testNewInstanceWithParsedValueStringStringString() {
        Assert.assertNotNull(SocketSetting.newInstanceWithParsedValue(
                "SO_RCVBUF", "3000", "Set receive buffer to 3000"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNewInstanceWithParsedValueStringStringStringForIllegalArgumentException01() {
        SocketSetting.newInstanceWithParsedValue(
                "so_rcvbuf", "3000", "Set receive buffer to 3000");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNewInstanceWithParsedValueStringStringStringForIllegalArgumentException02() {
        SocketSetting.newInstanceWithParsedValue(
                "SO_RCVBUF", "-3000", "Set receive buffer to -3000");
    }

    @Test
    public void testApplyToDatagramSocket() throws SocketException {
        DatagramSocket datagramSocket = new DatagramSocket(null);
        StandardSocketSettingSpecConstants.SO_BROADCAST.newSocketSetting(
                Boolean.TRUE).applyTo(datagramSocket);
        Assert.assertTrue(datagramSocket.getBroadcast());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testApplyToDatagramSocketForUnsupportedOperationException() throws SocketException {
        StandardSocketSettingSpecConstants.TCP_NODELAY.newSocketSetting(
                Boolean.TRUE).applyTo(new DatagramSocket(null));
    }

    @Test
    public void testApplyToServerSocket() throws IOException {
        ServerSocket serverSocket = new ServerSocket();
        StandardSocketSettingSpecConstants.SO_REUSEADDR.newSocketSetting(
                Boolean.TRUE).applyTo(serverSocket);
        Assert.assertTrue(serverSocket.getReuseAddress());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testApplyToServerSocketForUnsupportedOperationException() throws IOException {
        StandardSocketSettingSpecConstants.SO_BROADCAST.newSocketSetting(
                Boolean.TRUE).applyTo(new ServerSocket());
    }

    @Test
    public void testApplyToSocket() throws SocketException {
        Socket socket = new Socket();
        StandardSocketSettingSpecConstants.TCP_NODELAY.newSocketSetting(
                Boolean.TRUE).applyTo(socket);
        Assert.assertTrue(socket.getTcpNoDelay());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testApplyToSocketForUnsupportedOperationException() throws SocketException {
        StandardSocketSettingSpecConstants.SO_BROADCAST.newSocketSetting(
                Boolean.TRUE).applyTo(new Socket());
    }

    @Test
    public void testEqualsObject01() {
        SocketSetting<Object> socketSetting = SocketSetting.newInstanceOf(
                "SO_TIMEOUT=1000");
        Assert.assertEquals(socketSetting, socketSetting);
    }

    @Test
    public void testEqualsObject02() {
        SocketSetting<Object> socketSetting = SocketSetting.newInstanceOf(
                "SO_TIMEOUT=1000");
        Assert.assertNotEquals(socketSetting, null);
    }

    @Test
    public void testEqualsObject03() {
        Object obj1 = SocketSetting.newInstanceOf("SO_TIMEOUT=1000");
        Object obj2 = new Object();
        Assert.assertNotEquals(obj1, obj2);
    }

    @Test
    public void testEqualsObject04() {
        SocketSetting<Object> socketSetting1 = SocketSetting.newInstanceOf(
                "SO_TIMEOUT=1000");
        SocketSetting<Object> socketSetting2 = SocketSetting.newInstanceOf(
                "SO_SNDBUF=1000");
        Assert.assertNotEquals(socketSetting1, socketSetting2);
    }

    @Test
    public void testEqualsObject05() {
        SocketSetting<Object> socketSetting1 = SocketSetting.newInstanceOf(
                "SO_TIMEOUT=1000");
        SocketSetting<Object> socketSetting2 = SocketSetting.newInstanceOf(
                "SO_TIMEOUT=2000");
        Assert.assertNotEquals(socketSetting1, socketSetting2);
    }

    @Test
    public void testEqualsObject06() {
        SocketSetting<Object> socketSetting1 = SocketSetting.newInstanceOf(
                "SO_TIMEOUT=1000");
        SocketSetting<Object> socketSetting2 = SocketSetting.newInstanceOf(
                "SO_TIMEOUT=1000");
        Assert.assertEquals(socketSetting1, socketSetting2);
    }

    @Test
    public void testHashCode01() {
        SocketSetting<Object> socketSetting1 = SocketSetting.newInstanceOf(
                "SO_TIMEOUT=1000");
        SocketSetting<Object> socketSetting2 = SocketSetting.newInstanceOf(
                "SO_TIMEOUT=1000");
        Assert.assertEquals(
                socketSetting1.hashCode(), socketSetting2.hashCode());
    }

    @Test
    public void testHashCode02() {
        SocketSetting<Object> socketSetting1 = SocketSetting.newInstanceOf(
                "SO_TIMEOUT=1000");
        SocketSetting<Object> socketSetting2 = SocketSetting.newInstanceOf(
                "SO_TIMEOUT=2000");
        Assert.assertNotEquals(
                socketSetting1.hashCode(), socketSetting2.hashCode());
    }

    @Test
    public void testToString() {
        SocketSetting<Boolean> socketSetting =
                StandardSocketSettingSpecConstants.SO_KEEPALIVE.newSocketSetting(
                        Boolean.TRUE);
        Assert.assertEquals("SO_KEEPALIVE=true", socketSetting.toString());
    }

}