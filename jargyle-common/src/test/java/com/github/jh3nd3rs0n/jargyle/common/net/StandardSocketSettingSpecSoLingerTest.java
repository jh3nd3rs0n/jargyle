package com.github.jh3nd3rs0n.jargyle.common.net;

import com.github.jh3nd3rs0n.jargyle.common.number.NonNegativeInteger;
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
                NonNegativeInteger.valueOf(234),
                new DatagramSocket(null));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testApplyValueServerSocket() throws IOException {
        StandardSocketSettingSpecConstants.SO_LINGER.apply(
                NonNegativeInteger.valueOf(234),
                new ServerSocket());
    }

    @Test
    public void testApplyValueSocket() throws SocketException {
        Socket socket = new Socket();
        StandardSocketSettingSpecConstants.SO_LINGER.apply(
                NonNegativeInteger.valueOf(234),
                socket);
        Assert.assertEquals(234, socket.getSoLinger());
    }

    @Test
    public void testNewSocketSettingWithParsedValueString() {
        Assert.assertNotNull(
                StandardSocketSettingSpecConstants.SO_LINGER.newSocketSettingWithParsedValue(
                        "456"));
    }


}
