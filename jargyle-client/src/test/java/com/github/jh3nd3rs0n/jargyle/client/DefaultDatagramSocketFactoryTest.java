package com.github.jh3nd3rs0n.jargyle.client;

import org.junit.Test;

import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketException;

import static org.junit.Assert.*;

public class DefaultDatagramSocketFactoryTest {

    @Test
    public void testNewDatagramSocket01() throws SocketException {
        try (DatagramSocket datagramSocket = DefaultDatagramSocketFactory.getInstance()
                .newDatagramSocket()) {
            assertTrue(datagramSocket.getLocalAddress().isAnyLocalAddress());
        }
    }

    @Test
    public void testNewDatagramSocketInt01() throws SocketException {
        try (DatagramSocket datagramSocket = DefaultDatagramSocketFactory.getInstance()
                .newDatagramSocket(0)) {
            assertTrue(datagramSocket.getLocalAddress().isAnyLocalAddress());
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNewDatagramSocketIntForIllegalArgumentException01() throws SocketException {
        DefaultDatagramSocketFactory.getInstance().newDatagramSocket(-1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNewDatagramSocketIntForIllegalArgumentException02() throws SocketException {
        DefaultDatagramSocketFactory.getInstance().newDatagramSocket(65536);
    }

    @Test
    public void testNewDatagramSocketIntInetAddress01() throws SocketException {
        try (DatagramSocket datagramSocket = DefaultDatagramSocketFactory.getInstance()
                .newDatagramSocket(0, null)) {
            assertTrue(datagramSocket.getLocalAddress().isAnyLocalAddress());
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNewDatagramSocketIntInetAddressForIllegalArgumentException01() throws SocketException {
        DefaultDatagramSocketFactory.getInstance().newDatagramSocket(-1, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNewDatagramSocketIntInetAddressForIllegalArgumentException02() throws SocketException {
        DefaultDatagramSocketFactory.getInstance().newDatagramSocket(65536, null);
    }

    @Test
    public void testNewDatagramSocketSocketAddress01() throws SocketException {
        try (DatagramSocket datagramSocket = DefaultDatagramSocketFactory.getInstance()
                .newDatagramSocket(null)) {
            assertFalse(datagramSocket.isBound());
        }
    }

    @Test
    public void testNewDatagramSocketSocketAddress02() throws SocketException {
        try (DatagramSocket datagramSocket = DefaultDatagramSocketFactory.getInstance()
                .newDatagramSocket(new InetSocketAddress(0))) {
            assertTrue(datagramSocket.isBound());
        }
    }

}