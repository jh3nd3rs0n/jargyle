package com.github.jh3nd3rs0n.jargyle.common.net;

import com.github.jh3nd3rs0n.jargyle.common.number.NonnegativeInteger;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.net.DatagramSocket;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;

public class StandardSocketSettingSpecSoTimeoutTest {

    @Test
    public void testApplyValueDatagramSocket() throws SocketException {
        StandardSocketSettingSpecConstants.SO_TIMEOUT.apply(
                NonnegativeInteger.newInstanceOf(1000),
                new DatagramSocket(null));
    }

    @Test
    public void testApplyValueServerSocket() throws IOException {
        StandardSocketSettingSpecConstants.SO_TIMEOUT.apply(
                NonnegativeInteger.newInstanceOf(1000),
                new ServerSocket());
    }

    @Test
    public void testApplyValueSocket() throws SocketException {
        StandardSocketSettingSpecConstants.SO_TIMEOUT.apply(
                NonnegativeInteger.newInstanceOf(1000),
                new Socket());
    }

    @Test
    public void testNewSocketSettingWithParsedValueString() {
        Assert.assertNotNull(
                StandardSocketSettingSpecConstants.SO_TIMEOUT.newSocketSettingWithParsedValue(
                        "1000"));
    }

}
