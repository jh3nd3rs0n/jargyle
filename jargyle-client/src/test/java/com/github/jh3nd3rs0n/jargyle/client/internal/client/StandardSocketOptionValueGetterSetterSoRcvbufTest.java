package com.github.jh3nd3rs0n.jargyle.client.internal.client;

import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.net.ServerSocket;

public class StandardSocketOptionValueGetterSetterSoRcvbufTest {

    @Test
    public void testGetSocketOptionValueServerSocket() throws IOException {
        try (ServerSocket serverSocket = new ServerSocket()) {
            serverSocket.setReceiveBufferSize(1500);
            Assert.assertEquals(
                    1500,
                    StandardSocketOptionValueGetterSetterConstants.SO_RCVBUF.getSocketOptionValue(
                            serverSocket).intValue());

        }
    }

    @Test
    public void testSetSocketOptionValueValueServerSocket() throws IOException {
        try (ServerSocket serverSocket1 = new ServerSocket()) {
            ServerSocket serverSocket2 = StandardSocketOptionValueGetterSetterConstants.SO_RCVBUF.setSocketOptionValue(
                    4000,
                    serverSocket1);
            Assert.assertEquals(4000, serverSocket1.getReceiveBufferSize());
            Assert.assertEquals(serverSocket1, serverSocket2);
        }
    }

}
