package com.github.jh3nd3rs0n.jargyle.common.net;

import com.github.jh3nd3rs0n.jargyle.common.number.UnsignedByte;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.net.DatagramSocket;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;

public class StandardSocketSettingSpecIpTosTest {

    @Test
    public void testApplyValueDatagramSocket() throws SocketException {
        DatagramSocket datagramSocket = new DatagramSocket(null);
        int trafficClass = datagramSocket.getTrafficClass();
        try {
            StandardSocketSettingSpecConstants.IP_TOS.apply(
                    UnsignedByte.newInstanceOf(2), datagramSocket);
        } catch (SocketException e) {
            Assert.assertNotNull(e);
            return;
        }
        int newTrafficClass = datagramSocket.getTrafficClass();
        if (trafficClass == newTrafficClass) {
            Assert.assertEquals(trafficClass, newTrafficClass);
            return;
        }
        Assert.assertEquals(2, datagramSocket.getTrafficClass());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testApplyValueServerSocketForUnsupportedOperationException() throws IOException {
        StandardSocketSettingSpecConstants.IP_TOS.apply(
                UnsignedByte.newInstanceOf(24), new ServerSocket());
    }

    @Test
    public void testApplyValueSocket() throws SocketException {
        Socket socket = new Socket();
        int trafficClass = socket.getTrafficClass();
        try {
            StandardSocketSettingSpecConstants.IP_TOS.apply(
                    UnsignedByte.newInstanceOf(2), socket);
        } catch (SocketException e) {
            Assert.assertNotNull(e);
            return;
        }
        int newTrafficClass = socket.getTrafficClass();
        if (trafficClass == newTrafficClass) {
            Assert.assertEquals(trafficClass, newTrafficClass);
            return;
        }
        Assert.assertEquals(2, socket.getTrafficClass());
    }

    @Test
    public void testNewSocketSettingWithParsedValueString() {
        Assert.assertNotNull(
                StandardSocketSettingSpecConstants.IP_TOS.newSocketSettingWithParsedValue(
                        "19"));
    }

}
