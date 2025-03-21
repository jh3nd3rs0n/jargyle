package com.github.jh3nd3rs0n.jargyle.protocolbase.socks5.gssapimethod;

import com.github.jh3nd3rs0n.jargyle.protocolbase.GssEnvironment;
import org.ietf.jgss.GSSException;
import org.ietf.jgss.MessageProp;
import org.junit.*;
import org.junit.rules.Timeout;

import java.io.IOException;
import java.net.DatagramSocket;
import java.net.Socket;
import java.util.concurrent.TimeUnit;

public class GssapiMethodEncapsulationIT {

    @BeforeClass
    public static void setUpBeforeClass() throws IOException {
        GssEnvironment.setUpBeforeClass(GssapiMethodEncapsulationIT.class);
    }

    @AfterClass
    public static void tearDownAfterClass() throws IOException {
        GssEnvironment.tearDownAfterClass(GssapiMethodEncapsulationIT.class);
    }

    @Rule
    public Timeout globalTimeout = Timeout.builder()
            .withTimeout(60, TimeUnit.SECONDS)
            .withLookingForStuckThread(true)
            .build();


    @Test
    public void testGetDatagramSocketDatagramSocket() throws IOException, GSSException {
        GssObjectITHelper.testClientAndServerGssObjects((socket, gssContext) -> {
            MessageProp messageProp = new MessageProp(0, false);
            GssapiMethodEncapsulation gssapiMethodEncapsulation =
                    new GssapiMethodEncapsulation(socket, gssContext, messageProp);
            try (DatagramSocket datagramSocket1 = new DatagramSocket(null);
                 DatagramSocket datagramSocket2 =
                         gssapiMethodEncapsulation.getDatagramSocket(
                                 datagramSocket1)) {
                Assert.assertNotEquals(
                        datagramSocket1.getClass(), datagramSocket2.getClass());
            }
        }, ((socket, gssContext) -> {}));
    }

    @Test
    public void testGetSocket() throws IOException, GSSException {
        GssObjectITHelper.testClientAndServerGssObjects((socket, gssContext) -> {
            MessageProp messageProp = new MessageProp(0, false);
            GssapiMethodEncapsulation gssapiMethodEncapsulation =
                    new GssapiMethodEncapsulation(socket, gssContext, messageProp);
            try (Socket sock = gssapiMethodEncapsulation.getSocket()) {
                Assert.assertNotEquals(Socket.class, sock.getClass());
            }
        }, ((socket, gssContext) -> {}));
    }

}