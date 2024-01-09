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
        StandardSocketSettingSpecConstants.PERF_PREFS.apply(
                PerformancePreferences.newInstanceOf("321"), new ServerSocket());
    }

    @Test
    public void testApplyValueSocket() throws SocketException {
        StandardSocketSettingSpecConstants.PERF_PREFS.apply(
                PerformancePreferences.newInstanceOf("213"), new Socket());
    }

    @Test
    public void testNewSocketSettingWithParsedValueString() {
        Assert.assertNotNull(
                StandardSocketSettingSpecConstants.PERF_PREFS.newSocketSettingWithParsedValue(
                        "567"));
    }

}
