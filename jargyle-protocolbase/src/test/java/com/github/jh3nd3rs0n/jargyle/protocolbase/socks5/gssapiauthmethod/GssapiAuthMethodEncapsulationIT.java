package com.github.jh3nd3rs0n.jargyle.protocolbase.socks5.gssapiauthmethod;

import com.github.jh3nd3rs0n.jargyle.protocolbase.GssEnvironment;
import org.ietf.jgss.GSSException;
import org.ietf.jgss.MessageProp;
import org.junit.*;
import org.junit.rules.Timeout;

import java.io.IOException;
import java.net.DatagramSocket;
import java.net.Socket;
import java.util.concurrent.TimeUnit;

public class GssapiAuthMethodEncapsulationIT {

    @BeforeClass
    public static void setUpBeforeClass() throws IOException {
        GssEnvironment.setUpBeforeClass(GssapiAuthMethodEncapsulationIT.class);
    }

    @AfterClass
    public static void tearDownAfterClass() throws IOException {
        GssEnvironment.tearDownAfterClass(GssapiAuthMethodEncapsulationIT.class);
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
            GssapiAuthMethodEncapsulation gssapiAuthMethodEncapsulation =
                    new GssapiAuthMethodEncapsulation(socket, gssContext, messageProp);
            try (DatagramSocket datagramSocket1 = new DatagramSocket(null);
                 DatagramSocket datagramSocket2 =
                         gssapiAuthMethodEncapsulation.getDatagramSocket(
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
            GssapiAuthMethodEncapsulation gssapiAuthMethodEncapsulation =
                    new GssapiAuthMethodEncapsulation(socket, gssContext, messageProp);
            try (Socket sock = gssapiAuthMethodEncapsulation.getSocket()) {
                Assert.assertNotEquals(Socket.class, sock.getClass());
            }
        }, ((socket, gssContext) -> {}));
    }

}