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
        try {
            StandardSocketSettingSpecConstants.IP_TOS.apply(
                    UnsignedByte.newInstanceOf(5), new DatagramSocket(null));
        } catch (SocketException ignored) {
            /*
             * The following exception is caught when running
             * DatagramSocket.setTrafficClass(int) in Windows Subsystem for
             * Linux:
             *
             * java.net.SocketException: Operation not permitted
             *
             * This method runs fine in Windows and Linux
             *
             * OS Version:
             *
             * Ubuntu 20.04.6 LTS (GNU/Linux 4.4.0-19041-Microsoft x86_64)
             *
             * Java Version:
             *
             * openjdk version "17.0.9" 2023-10-17
             * OpenJDK Runtime Environment (build 17.0.9+9-Ubuntu-120.04)
             * OpenJDK 64-Bit Server VM (build 17.0.9+9-Ubuntu-120.04, mixed mode, sharing)
             */
        }
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testApplyValueServerSocketForUnsupportedOperationException() throws IOException {
        StandardSocketSettingSpecConstants.IP_TOS.apply(
                UnsignedByte.newInstanceOf(24), new ServerSocket());
    }

    @Test
    public void testApplyValueSocket() throws SocketException {
        try {
            StandardSocketSettingSpecConstants.IP_TOS.apply(
                    UnsignedByte.newInstanceOf(20), new Socket());
        } catch (SocketException ignored) {
            /*
             * The following exception is caught when running
             * Socket.setTrafficClass(int) in Windows Subsystem for
             * Linux:
             *
             * java.net.SocketException: Operation not permitted
             *
             * This method runs fine in Windows and Linux
             *
             * OS Version:
             *
             * Ubuntu 20.04.6 LTS (GNU/Linux 4.4.0-19041-Microsoft x86_64)
             *
             * Java Version:
             *
             * openjdk version "17.0.9" 2023-10-17
             * OpenJDK Runtime Environment (build 17.0.9+9-Ubuntu-120.04)
             * OpenJDK 64-Bit Server VM (build 17.0.9+9-Ubuntu-120.04, mixed mode, sharing)
             */
        }
    }

    @Test
    public void testNewSocketSettingWithParsedValueString() {
        Assert.assertNotNull(
                StandardSocketSettingSpecConstants.IP_TOS.newSocketSettingWithParsedValue(
                        "19"));
    }

}
