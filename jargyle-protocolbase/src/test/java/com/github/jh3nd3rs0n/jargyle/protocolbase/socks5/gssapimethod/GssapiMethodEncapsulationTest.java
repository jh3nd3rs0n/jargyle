package com.github.jh3nd3rs0n.jargyle.protocolbase.socks5.gssapimethod;

import com.github.jh3nd3rs0n.jargyle.protocolbase.TestGssEnvironment;
import org.ietf.jgss.GSSException;
import org.ietf.jgss.MessageProp;
import org.junit.*;
import org.junit.rules.Timeout;

import java.io.IOException;
import java.net.DatagramSocket;
import java.net.Socket;
import java.util.concurrent.TimeUnit;

public class GssapiMethodEncapsulationTest {

    @BeforeClass
    public static void setUpBeforeClass() throws IOException {
        TestGssEnvironment.setUpBeforeClass(GssapiMethodEncapsulationTest.class);
    }

    @AfterClass
    public static void tearDownAfterClass() throws IOException {
        TestGssEnvironment.tearDownAfterClass(GssapiMethodEncapsulationTest.class);
    }

    @Rule
    public Timeout globalTimeout = Timeout.builder()
            .withTimeout(5, TimeUnit.SECONDS)
            .withLookingForStuckThread(true)
            .build();


    @Test
    public void testGetDatagramSocketDatagramSocket() throws IOException, GSSException {
        GssObjectTestHelper.testClientAndServerGssObjects((socket, gssContext) -> {
            MessageProp messageProp = new MessageProp(0, false);
            GssapiMethodEncapsulation gssapiMethodEncapsulation =
                    new GssapiMethodEncapsulation(socket, gssContext, messageProp);
            DatagramSocket datagramSocket1 = new DatagramSocket(null);
            DatagramSocket datagramSocket2 =
                    gssapiMethodEncapsulation.getDatagramSocket(datagramSocket1);
            Assert.assertNotEquals(
                    datagramSocket1.getClass(), datagramSocket2.getClass());
        }, ((socket, gssContext) -> {}));
    }

    @Test
    public void testGetSocket() throws IOException, GSSException {
        GssObjectTestHelper.testClientAndServerGssObjects((socket, gssContext) -> {
            MessageProp messageProp = new MessageProp(0, false);
            GssapiMethodEncapsulation gssapiMethodEncapsulation =
                    new GssapiMethodEncapsulation(socket, gssContext, messageProp);
            Socket socket1 = new Socket();
            Socket socket2 = gssapiMethodEncapsulation.getSocket();
            Assert.assertNotEquals(socket1.getClass(), socket2.getClass());
        }, ((socket, gssContext) -> {}));
    }

}