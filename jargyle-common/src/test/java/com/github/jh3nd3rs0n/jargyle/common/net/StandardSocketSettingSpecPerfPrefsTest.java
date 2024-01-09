package com.github.jh3nd3rs0n.jargyle.common.net;

import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.net.DatagramSocket;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;

public class StandardSocketSettingSpecPerfPrefsTest {

    @Test(expected = UnsupportedOperationException.class)
    public void testApplyValueDatagramSocketForUnsupportedOperationException() throws SocketException {
        StandardSocketSettingSpecConstants.PERF_PREFS.apply(
                PerformancePreferences.newInstanceOf("123"),
                new DatagramSocket(null));
    }

    @Test
    public void testApplyValueServerSocket() throws IOException {
        ServerSocket serverSocket = new ServerSocket();
        SocketException socketException = null;
        try {
            StandardSocketSettingSpecConstants.PERF_PREFS.apply(
                    PerformancePreferences.newInstanceOf("321"), serverSocket);
        } catch (SocketException e) {
            socketException = e;
        }
        Assert.assertNull(socketException);
    }

    @Test
    public void testApplyValueSocket() {
        Socket socket = new Socket();
        SocketException socketException = null;
        try {
            StandardSocketSettingSpecConstants.PERF_PREFS.apply(
                    PerformancePreferences.newInstanceOf("213"), socket);
        } catch (SocketException e) {
            socketException = e;
        }
        Assert.assertNull(socketException);
    }

    @Test
    public void testNewSocketSettingWithParsedValueString() {
        Assert.assertNotNull(
                StandardSocketSettingSpecConstants.PERF_PREFS.newSocketSettingWithParsedValue(
                        "567"));
    }

}
