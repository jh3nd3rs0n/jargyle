package com.github.jh3nd3rs0n.jargyle.client.internal.client;

import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.StandardSocketOptions;
import java.util.Set;

public class SocketOptionValueGetterSettersTest {

    @Test
    public void testGetSocketOptionValueSocketOptionServerSocket01() throws IOException {
        SocketOptionValueGetterSetters socketOptionValueGetterSetters =
                new SocketOptionValueGetterSetters(
                        StandardSocketOptionValueGetterSetterConstants.SO_RCVBUF);
        try (ServerSocket serverSocket = new ServerSocket()) {
            serverSocket.setReceiveBufferSize(1500);
            Assert.assertEquals(
                    1500,
                    (int) socketOptionValueGetterSetters.getSocketOptionValue(StandardSocketOptions.SO_RCVBUF, serverSocket));
        }
    }

    @Test
    public void testGetSocketOptionValueSocketOptionServerSocket02() throws IOException {
        SocketOptionValueGetterSetters socketOptionValueGetterSetters =
                new SocketOptionValueGetterSetters(
                        StandardSocketOptionValueGetterSetterConstants.SO_REUSEADDR);
        try (ServerSocket serverSocket = new ServerSocket()) {
            serverSocket.setReuseAddress(true);
            Assert.assertTrue(socketOptionValueGetterSetters.getSocketOptionValue(StandardSocketOptions.SO_REUSEADDR, serverSocket));
        }
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetSocketOptionValueSocketOptionServerSocketForUnsupportedOperationException01() throws IOException {
        SocketOptionValueGetterSetters socketOptionValueGetterSetters =
                new SocketOptionValueGetterSetters();
        try (ServerSocket serverSocket = new ServerSocket()) {
            socketOptionValueGetterSetters.getSocketOptionValue(StandardSocketOptions.SO_RCVBUF, serverSocket);
        }
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetSocketOptionValueSocketOptionServerSocketForUnsupportedOperationException02() throws IOException {
        SocketOptionValueGetterSetters socketOptionValueGetterSetters =
                new SocketOptionValueGetterSetters(StandardSocketOptionValueGetterSetterConstants.SO_RCVBUF);
        try (ServerSocket serverSocket = new ServerSocket()) {
            socketOptionValueGetterSetters.getSocketOptionValue(StandardSocketOptions.SO_REUSEADDR, serverSocket);
        }
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetSocketOptionValueSocketOptionServerSocketForUnsupportedOperationException03() throws IOException {
        SocketOptionValueGetterSetters socketOptionValueGetterSetters =
                new SocketOptionValueGetterSetters(
                        StandardSocketOptionValueGetterSetterConstants.SO_RCVBUF,
                        StandardSocketOptionValueGetterSetterConstants.SO_REUSEADDR);
        try (ServerSocket serverSocket = new ServerSocket()) {
            socketOptionValueGetterSetters.getSocketOptionValue(StandardSocketOptions.SO_SNDBUF, serverSocket);
        }
    }

    @Test
    public void testGetSupportedSocketOptions01() {
        SocketOptionValueGetterSetters socketOptionValueGetterSetters =
                new SocketOptionValueGetterSetters();
        Assert.assertEquals(0, socketOptionValueGetterSetters.getSupportedSocketOptions().size());
    }

    @Test
    public void testGetSupportedSocketOptions02() {
        SocketOptionValueGetterSetters socketOptionValueGetterSetters =
                new SocketOptionValueGetterSetters(StandardSocketOptionValueGetterSetterConstants.SO_RCVBUF);
        Assert.assertEquals(
                Set.of(StandardSocketOptions.SO_RCVBUF),
                socketOptionValueGetterSetters.getSupportedSocketOptions());
    }

    @Test
    public void testGetSupportedSocketOptions03() {
        SocketOptionValueGetterSetters socketOptionValueGetterSetters =
                new SocketOptionValueGetterSetters(StandardSocketOptionValueGetterSetterConstants.SO_REUSEADDR);
        Assert.assertEquals(
                Set.of(StandardSocketOptions.SO_REUSEADDR),
                socketOptionValueGetterSetters.getSupportedSocketOptions());
    }

    @Test
    public void testGetSupportedSocketOptions04() {
        SocketOptionValueGetterSetters socketOptionValueGetterSetters =
                new SocketOptionValueGetterSetters(
                        StandardSocketOptionValueGetterSetterConstants.SO_RCVBUF,
                        StandardSocketOptionValueGetterSetterConstants.SO_REUSEADDR);
        Assert.assertEquals(
                Set.of(
                        StandardSocketOptions.SO_RCVBUF,
                        StandardSocketOptions.SO_REUSEADDR),
                socketOptionValueGetterSetters.getSupportedSocketOptions());
    }

    @Test
    public void testSetSocketOptionValueSocketOptionValueServerSocket01() throws IOException {
        SocketOptionValueGetterSetters socketOptionValueGetterSetters =
                new SocketOptionValueGetterSetters(
                        StandardSocketOptionValueGetterSetterConstants.SO_RCVBUF);
        try (ServerSocket serverSocket1 = new ServerSocket()) {
            ServerSocket serverSocket2 = socketOptionValueGetterSetters.setSocketOptionValue(
                    StandardSocketOptions.SO_RCVBUF, 1500, serverSocket1);
            Assert.assertEquals(
                    1500,
                    serverSocket1.getReceiveBufferSize());
            Assert.assertEquals(serverSocket1, serverSocket2);
        }
    }

    @Test
    public void testSetSocketOptionValueSocketOptionValueServerSocket02() throws IOException {
        SocketOptionValueGetterSetters socketOptionValueGetterSetters =
                new SocketOptionValueGetterSetters(
                        StandardSocketOptionValueGetterSetterConstants.SO_REUSEADDR);
        try (ServerSocket serverSocket1 = new ServerSocket()) {
            ServerSocket serverSocket2 = socketOptionValueGetterSetters.setSocketOptionValue(
                    StandardSocketOptions.SO_REUSEADDR, true, serverSocket1);
            Assert.assertTrue(serverSocket1.getReuseAddress());
            Assert.assertEquals(serverSocket1, serverSocket2);
        }
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetSocketOptionValueSocketOptionValueServerSocketForUnsupportedOperationException01() throws IOException {
        SocketOptionValueGetterSetters socketOptionValueGetterSetters =
                new SocketOptionValueGetterSetters();
        try (ServerSocket serverSocket = new ServerSocket()) {
            socketOptionValueGetterSetters.setSocketOptionValue(StandardSocketOptions.SO_RCVBUF, 1500, serverSocket);
        }
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetSocketOptionValueSocketOptionValueServerSocketForUnsupportedOperationException02() throws IOException {
        SocketOptionValueGetterSetters socketOptionValueGetterSetters =
                new SocketOptionValueGetterSetters(StandardSocketOptionValueGetterSetterConstants.SO_RCVBUF);
        try (ServerSocket serverSocket = new ServerSocket()) {
            socketOptionValueGetterSetters.setSocketOptionValue(StandardSocketOptions.SO_REUSEADDR, true, serverSocket);
        }
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetSocketOptionValueSocketOptionValueServerSocketForUnsupportedOperationException03() throws IOException {
        SocketOptionValueGetterSetters socketOptionValueGetterSetters =
                new SocketOptionValueGetterSetters(
                        StandardSocketOptionValueGetterSetterConstants.SO_RCVBUF,
                        StandardSocketOptionValueGetterSetterConstants.SO_REUSEADDR);
        try (ServerSocket serverSocket = new ServerSocket()) {
            socketOptionValueGetterSetters.setSocketOptionValue(StandardSocketOptions.SO_SNDBUF, 1500, serverSocket);
        }
    }

}