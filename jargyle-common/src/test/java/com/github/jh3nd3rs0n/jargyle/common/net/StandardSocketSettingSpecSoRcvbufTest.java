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
        StandardSocketSettingSpecConstants.SO_RCVBUF.apply(
                PositiveInteger.newInstanceOf(420),
                new DatagramSocket(null));
    }

    @Test
    public void testApplyValueServerSocket() throws IOException {
        StandardSocketSettingSpecConstants.SO_RCVBUF.apply(
                PositiveInteger.newInstanceOf(420),
                new ServerSocket());
    }

    @Test
    public void testApplyValueSocket() throws SocketException {
        StandardSocketSettingSpecConstants.SO_RCVBUF.apply(
                PositiveInteger.newInstanceOf(420),
                new Socket());
    }

    @Test
    public void testNewSocketSettingWithParsedValueString() {
        Assert.assertNotNull(
                StandardSocketSettingSpecConstants.SO_RCVBUF.newSocketSettingWithParsedValue(
                        "690"));
    }

}
