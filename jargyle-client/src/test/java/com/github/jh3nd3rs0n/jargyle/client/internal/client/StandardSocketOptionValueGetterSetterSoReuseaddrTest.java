package com.github.jh3nd3rs0n.jargyle.client.internal.client;

import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.net.ServerSocket;

public class StandardSocketOptionValueGetterSetterSoReuseaddrTest {

    @Test
    public void testGetSocketOptionValueServerSocket() throws IOException {
        try (ServerSocket serverSocket = new ServerSocket()) {
            serverSocket.setReuseAddress(true);
            Assert.assertTrue(
                    StandardSocketOptionValueGetterSetterConstants.SO_REUSEADDR.getSocketOptionValue(
                            serverSocket));
        }
    }

    @Test
    public void testSetSocketOptionValueValueServerSocket() throws IOException {
        try (ServerSocket serverSocket1 = new ServerSocket()) {
            ServerSocket serverSocket2 = StandardSocketOptionValueGetterSetterConstants.SO_REUSEADDR.setSocketOptionValue(
                    Boolean.TRUE, serverSocket1);
            Assert.assertTrue(serverSocket1.getReuseAddress());
            Assert.assertEquals(serverSocket1, serverSocket2);
        }
    }

}
