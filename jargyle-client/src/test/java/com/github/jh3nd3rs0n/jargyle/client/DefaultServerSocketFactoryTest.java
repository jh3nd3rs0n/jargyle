package com.github.jh3nd3rs0n.jargyle.client;

import org.junit.Test;

import java.io.IOException;
import java.net.ServerSocket;

import static org.junit.Assert.*;

public class DefaultServerSocketFactoryTest {

    @Test
    public void testNewServerSocket01() throws IOException {
        try (ServerSocket serverSocket = DefaultServerSocketFactory.getInstance()
                .newServerSocket()) {
            assertFalse(serverSocket.isBound());
        }
    }

    @Test
    public void testNewServerSocketInt01() throws IOException {
        try (ServerSocket serverSocket = DefaultServerSocketFactory.getInstance()
                .newServerSocket(0)) {
            assertTrue(serverSocket.isBound());
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNewServerSocketIntForIllegalArgumentException01() throws IOException {
        DefaultServerSocketFactory.getInstance().newServerSocket(-1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNewServerSocketIntForIllegalArgumentException02() throws IOException {
        DefaultServerSocketFactory.getInstance().newServerSocket(65536);
    }

    @Test
    public void testNewServerSocketIntInt01() throws IOException {
        try (ServerSocket serverSocket = DefaultServerSocketFactory.getInstance()
                .newServerSocket(0, 100)) {
            assertTrue(serverSocket.isBound());
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNewServerSocketIntIntForIllegalArgumentException01() throws IOException {
        DefaultServerSocketFactory.getInstance().newServerSocket(-1, 100);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNewServerSocketIntIntForIllegalArgumentException02() throws IOException {
        DefaultServerSocketFactory.getInstance().newServerSocket(65536, 100);
    }

    @Test
    public void testNewServerSocketIntIntInetAddress01() throws IOException {
        try (ServerSocket serverSocket = DefaultServerSocketFactory.getInstance()
                .newServerSocket(0, 100, null)) {
            assertTrue(serverSocket.getInetAddress().isAnyLocalAddress());
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNewServerSocketIntIntInetAddressForIllegalArgumentException01() throws IOException {
        DefaultServerSocketFactory.getInstance().newServerSocket(-1, 100, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNewServerSocketIntIntInetAddressForIllegalArgumentException02() throws IOException {
        DefaultServerSocketFactory.getInstance().newServerSocket(65536, 100, null);
    }

}