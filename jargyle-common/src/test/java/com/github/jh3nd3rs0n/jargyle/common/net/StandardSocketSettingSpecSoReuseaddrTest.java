package com.github.jh3nd3rs0n.jargyle.common.net;

import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.net.DatagramSocket;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;

public class StandardSocketSettingSpecSoReuseaddrTest {

    @Test
    public void testApplyValueDatagramSocket() throws SocketException {
        StandardSocketSettingSpecConstants.SO_REUSEADDR.apply(
                Boolean.TRUE, new DatagramSocket(null));
    }

    @Test
    public void testApplyValueServerSocket() throws IOException {
        StandardSocketSettingSpecConstants.SO_REUSEADDR.apply(
                Boolean.TRUE, new ServerSocket());
    }

    @Test
    public void testApplyValueSocket() throws SocketException {
        StandardSocketSettingSpecConstants.SO_REUSEADDR.apply(
                Boolean.TRUE, new Socket());
    }

    @Test
    public void testNewSocketSettingWithParsedValueString() {
        Assert.assertNotNull(
                StandardSocketSettingSpecConstants.SO_REUSEADDR.newSocketSettingWithParsedValue(
                        "true"));
    }

}
