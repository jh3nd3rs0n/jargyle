package com.github.jh3nd3rs0n.jargyle.common.net;

import com.github.jh3nd3rs0n.jargyle.common.number.NonNegativeInteger;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.net.DatagramSocket;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;

public class SocketSettingsTest {

    @Test
    public void testApplyToDatagramSocket() throws SocketException {
        SocketSettings socketSettings = SocketSettings.of(
                SocketSetting.newInstanceFrom("SO_BROADCAST=true"),
                SocketSetting.newInstanceFrom("SO_TIMEOUT=3000"));
        DatagramSocket datagramSocket = new DatagramSocket(null);
        socketSettings.applyTo(datagramSocket);
        Assert.assertTrue(datagramSocket.getBroadcast());
        Assert.assertEquals(3000, datagramSocket.getSoTimeout());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testApplyToDatagramSocketForUnsupportedOperationException() throws SocketException {
        SocketSettings socketSettings = SocketSettings.of(
                SocketSetting.newInstanceFrom("SO_BROADCAST=true"),
                SocketSetting.newInstanceFrom("TCP_NODELAY=true"));
        socketSettings.applyTo(new DatagramSocket(null));
    }

    @Test
    public void testApplyToServerSocket() throws IOException {
        SocketSettings socketSettings = SocketSettings.of(
                SocketSetting.newInstanceFrom("SO_TIMEOUT=3000"),
                SocketSetting.newInstanceFrom("SO_RCVBUF=3000"));
        ServerSocket serverSocket = new ServerSocket();
        socketSettings.applyTo(serverSocket);
        Assert.assertEquals(3000, serverSocket.getSoTimeout());
        Assert.assertEquals(3000, serverSocket.getReceiveBufferSize());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testApplyToServerSocketForUnsupportedOperationException() throws IOException {
        SocketSettings socketSettings = SocketSettings.of(
                SocketSetting.newInstanceFrom("SO_TIMEOUT=3000"),
                SocketSetting.newInstanceFrom("SO_BROADCAST=true"));
        socketSettings.applyTo(new ServerSocket());
    }

    @Test
    public void testApplyToSocket() throws SocketException {
        SocketSettings socketSettings = SocketSettings.of(
                SocketSetting.newInstanceFrom("SO_TIMEOUT=3000"),
                SocketSetting.newInstanceFrom("SO_SNDBUF=3000"));
        Socket socket = new Socket();
        socketSettings.applyTo(socket);
        Assert.assertEquals(3000, socket.getSoTimeout());
        Assert.assertEquals(3000, socket.getSendBufferSize());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testApplyToSocketForUnsupportedOperationException() throws SocketException {
        SocketSettings socketSettings = SocketSettings.of(
                SocketSetting.newInstanceFrom("SO_TIMEOUT=3000"),
                SocketSetting.newInstanceFrom("SO_BROADCAST=true"));
        socketSettings.applyTo(new Socket());
    }

    @Test
    public void testEqualsObject01() {
        SocketSettings socketSettings = SocketSettings.of(
                SocketSetting.newInstanceFrom("SO_TIMEOUT=3000"),
                SocketSetting.newInstanceFrom("SO_SNDBUF=3000"),
                SocketSetting.newInstanceFrom("SO_RCVBUF=3000"));
        Assert.assertEquals(socketSettings, socketSettings);
    }

    @Test
    public void testEqualsObject02() {
        SocketSettings socketSettings = SocketSettings.of(
                SocketSetting.newInstanceFrom("SO_TIMEOUT=3000"),
                SocketSetting.newInstanceFrom("SO_SNDBUF=3000"),
                SocketSetting.newInstanceFrom("SO_RCVBUF=3000"));
        Assert.assertNotEquals(socketSettings, null);
    }

    @Test
    public void testEqualsObject03() {
        Object obj1 = SocketSettings.of(
                SocketSetting.newInstanceFrom("SO_TIMEOUT=3000"),
                SocketSetting.newInstanceFrom("SO_SNDBUF=3000"),
                SocketSetting.newInstanceFrom("SO_RCVBUF=3000"));
        Object obj2 = new Object();
        Assert.assertNotEquals(obj1, obj2);
    }

    @Test
    public void testEqualsObject04() {
        SocketSettings socketSettings1 = SocketSettings.of(
                SocketSetting.newInstanceFrom("SO_TIMEOUT=3000"),
                SocketSetting.newInstanceFrom("SO_SNDBUF=3000"),
                SocketSetting.newInstanceFrom("SO_RCVBUF=3000"));
        SocketSettings socketSettings2 = SocketSettings.of(
                SocketSetting.newInstanceFrom("SO_BROADCAST=true"),
                SocketSetting.newInstanceFrom("SO_TIMEOUT=3000"));
        Assert.assertNotEquals(socketSettings1, socketSettings2);
    }

    @Test
    public void testEqualsObject05() {
        SocketSettings socketSettings1 = SocketSettings.of(
                SocketSetting.newInstanceFrom("SO_TIMEOUT=3000"),
                SocketSetting.newInstanceFrom("SO_SNDBUF=3000"),
                SocketSetting.newInstanceFrom("SO_RCVBUF=3000"));
        SocketSettings socketSettings2 = SocketSettings.of(
                SocketSetting.newInstanceFrom("SO_TIMEOUT=3000"),
                SocketSetting.newInstanceFrom("SO_SNDBUF=3000"),
                SocketSetting.newInstanceFrom("SO_RCVBUF=3000"));
        Assert.assertEquals(socketSettings1, socketSettings2);
    }

    @Test
    public void testGetValueSocketSettingSpec01() {
        SocketSettings socketSettings = SocketSettings.of(
                SocketSetting.newInstanceFrom("SO_TIMEOUT=3000"),
                SocketSetting.newInstanceFrom("SO_REUSEADDR=true")
        );
        Assert.assertEquals(Boolean.TRUE, socketSettings.getValue(
                StandardSocketSettingSpecConstants.SO_REUSEADDR));
    }

    @Test
    public void testGetValueSocketSettingSpec02() {
        SocketSettings socketSettings = SocketSettings.of(
                SocketSetting.newInstanceFrom("SO_TIMEOUT=3000"),
                SocketSetting.newInstanceFrom("SO_REUSEADDR=true"));
        Assert.assertNull(socketSettings.getValue(
                StandardSocketSettingSpecConstants.IP_TOS));
    }

    @Test
    public void testHashCode01() {
        SocketSettings socketSettings1 = SocketSettings.of(
                SocketSetting.newInstanceFrom("SO_TIMEOUT=3000"),
                SocketSetting.newInstanceFrom("SO_SNDBUF=3000"),
                SocketSetting.newInstanceFrom("SO_RCVBUF=3000"));
        SocketSettings socketSettings2 = SocketSettings.of(
                SocketSetting.newInstanceFrom("SO_TIMEOUT=3000"),
                SocketSetting.newInstanceFrom("SO_SNDBUF=3000"),
                SocketSetting.newInstanceFrom("SO_RCVBUF=3000"));
        Assert.assertEquals(
                socketSettings1.hashCode(), socketSettings2.hashCode());
    }

    @Test
    public void testHashCode02() {
        SocketSettings socketSettings1 = SocketSettings.of(
                SocketSetting.newInstanceFrom("SO_TIMEOUT=3000"),
                SocketSetting.newInstanceFrom("SO_SNDBUF=3000"),
                SocketSetting.newInstanceFrom("SO_RCVBUF=3000"));
        SocketSettings socketSettings2 = SocketSettings.of(
                SocketSetting.newInstanceFrom("SO_BROADCAST=true"),
                SocketSetting.newInstanceFrom("SO_TIMEOUT=3000"));
        Assert.assertNotEquals(
                socketSettings1.hashCode(), socketSettings2.hashCode());
    }

    @Test
    public void testNewInstanceFromString01() {
        Assert.assertNotNull(SocketSettings.newInstanceFrom(
                "SO_TIMEOUT=3000,SO_SNDBUF=3000,SO_RCVBUF=3000"));
    }

    @Test
    public void testNewInstanceFromString02() {
        Assert.assertNotNull(SocketSettings.newInstanceFrom(""));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNewInstanceFromStringForIllegalArgumentException01() {
        SocketSettings.newInstanceFrom("no socket settings here");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNewInstanceFromStringForIllegalArgumentException02() {
        SocketSettings.newInstanceFrom("SO_TIMEOUT=3000,SO_SNDBUF=3000,SO_RCVBUF=3000,");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNewInstanceFromStringForIllegalArgumentException03() {
        SocketSettings.newInstanceFrom(",SO_TIMEOUT=3000,SO_SNDBUF=3000,SO_RCVBUF=3000");
    }

    @Test
    public void testOfSocketSettings() {
        Assert.assertNotNull(SocketSettings.of(
                SocketSettings.of(
                        SocketSetting.newInstanceFrom("SO_TIMEOUT=3000"),
                        SocketSetting.newInstanceFrom("SO_SNDBUF=3000"),
                        SocketSetting.newInstanceFrom("SO_RCVBUF=3000"))));
    }

    @Test
    public void testOfSocketSettingVarargs() {
        Assert.assertNotNull(SocketSettings.of(
                SocketSetting.newInstanceFrom("SO_TIMEOUT=3000"),
                SocketSetting.newInstanceFrom("SO_SNDBUF=3000"),
                SocketSetting.newInstanceFrom("SO_RCVBUF=3000")));
    }

    @Test
    public void testPutValueSocketSettingSpecValue01() {
        SocketSettings socketSettings = SocketSettings.of();
        Assert.assertNull(socketSettings.putValue(
                StandardSocketSettingSpecConstants.SO_REUSEADDR, Boolean.TRUE));
        Assert.assertEquals(
                SocketSettings.of(
                        StandardSocketSettingSpecConstants.SO_REUSEADDR.newSocketSetting(
                                Boolean.TRUE)),
                socketSettings);
    }

    @Test
    public void testPutValueSocketSettingSpecValue02() {
        NonNegativeInteger value = NonNegativeInteger.valueOf(1000);
        SocketSettings socketSettings = SocketSettings.of(
                StandardSocketSettingSpecConstants.SO_TIMEOUT.newSocketSetting(
                        value
                )
        );
        NonNegativeInteger newValue = NonNegativeInteger.valueOf(2000);
        Assert.assertEquals(
                value,
                socketSettings.putValue(
                        StandardSocketSettingSpecConstants.SO_TIMEOUT,
                        newValue));
        Assert.assertEquals(
                SocketSettings.of(
                        StandardSocketSettingSpecConstants.SO_TIMEOUT.newSocketSetting(
                                newValue)),
                socketSettings);
    }

    @Test
    public void testRemoveSocketSettingSpec01() {
        NonNegativeInteger value = NonNegativeInteger.valueOf(1000);
        SocketSettings socketSettings = SocketSettings.of(
                StandardSocketSettingSpecConstants.SO_REUSEADDR.newSocketSetting(
                        Boolean.TRUE),
                StandardSocketSettingSpecConstants.SO_TIMEOUT.newSocketSetting(
                        value)
        );
        Assert.assertEquals(
                value,
                socketSettings.remove(StandardSocketSettingSpecConstants.SO_TIMEOUT));
        Assert.assertEquals(
                SocketSettings.of(
                        StandardSocketSettingSpecConstants.SO_REUSEADDR.newSocketSetting(
                                Boolean.TRUE)),
                socketSettings);
    }

    @Test
    public void testRemoveSocketSettingSpec02() {
        NonNegativeInteger value = NonNegativeInteger.valueOf(1000);
        SocketSettings socketSettings = SocketSettings.of(
                StandardSocketSettingSpecConstants.SO_REUSEADDR.newSocketSetting(
                        Boolean.TRUE),
                StandardSocketSettingSpecConstants.SO_TIMEOUT.newSocketSetting(
                        value)
        );
        Assert.assertNull(
                socketSettings.remove(StandardSocketSettingSpecConstants.TCP_NODELAY));
        Assert.assertEquals(
                SocketSettings.of(
                        StandardSocketSettingSpecConstants.SO_REUSEADDR.newSocketSetting(
                                Boolean.TRUE),
                        StandardSocketSettingSpecConstants.SO_TIMEOUT.newSocketSetting(
                                value)),
                socketSettings);
    }

    @Test
    public void testToString01() {
        SocketSettings socketSettings = SocketSettings.of(
                StandardSocketSettingSpecConstants.SO_REUSEADDR.newSocketSetting(
                        Boolean.TRUE),
                StandardSocketSettingSpecConstants.SO_TIMEOUT.newSocketSetting(
                        NonNegativeInteger.valueOf(3000))
        );
        Assert.assertEquals(
                "SO_REUSEADDR=true,SO_TIMEOUT=3000",
                socketSettings.toString());
    }

    @Test
    public void testToString02() {
        SocketSettings socketSettings = SocketSettings.of();
        Assert.assertEquals("", socketSettings.toString());
    }

}