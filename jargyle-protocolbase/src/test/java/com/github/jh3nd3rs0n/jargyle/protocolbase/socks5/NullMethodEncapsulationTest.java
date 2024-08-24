package com.github.jh3nd3rs0n.jargyle.protocolbase.socks5;

import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.net.DatagramSocket;
import java.net.Socket;

public class NullMethodEncapsulationTest {

    @Test
    public void testGetDatagramSocket() throws IOException {
        MethodEncapsulation methodEncapsulation = new NullMethodEncapsulation(
                new Socket());
        DatagramSocket datagramSocket = methodEncapsulation.getDatagramSocket(
                new DatagramSocket(null));
        Assert.assertEquals(DatagramSocket.class, datagramSocket.getClass());
    }

    @Test
    public void testGetSocket() {
        MethodEncapsulation methodEncapsulation = new NullMethodEncapsulation(
                new Socket());
        Socket socket = methodEncapsulation.getSocket();
        Assert.assertEquals(Socket.class, socket.getClass());
    }

}