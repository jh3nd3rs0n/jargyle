package com.github.jh3nd3rs0n.jargyle.protocolbase.socks5.gssapimethod;

import com.github.jh3nd3rs0n.jargyle.protocolbase.GssEnvironment;
import com.github.jh3nd3rs0n.jargyle.test.help.net.DatagramServer;
import com.github.jh3nd3rs0n.jargyle.test.help.string.StringConstants;
import com.github.jh3nd3rs0n.jargyle.test.help.thread.ThreadHelper;
import org.ietf.jgss.GSSException;
import org.ietf.jgss.MessageProp;
import org.junit.*;
import org.junit.rules.Timeout;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;

public class GssDatagramSocketIT {

    private static final int RECEIVE_BUFFER_SIZE =
            DatagramServer.RECEIVE_BUFFER_SIZE;

    private static String echo(
            final String string,
            final MessageProp messageProp) throws GSSException, IOException {
        Holder<String> returningStringHolder = new Holder<>();
        try (DatagramSocket clientDatagramSocket = new DatagramSocket(
                0, InetAddress.getLoopbackAddress());
             DatagramSocket serverDatagramSocket = new DatagramSocket(
                     0, InetAddress.getLoopbackAddress())) {
            GssObjectITHelper.testClientAndServerGssObjects(
                    ((socket, gssContext) -> {
                        try (GssDatagramSocket gssDatagramSocket =
                                     new GssDatagramSocket(
                                             clientDatagramSocket,
                                             gssContext,
                                             messageProp)) {
                            byte[] buffer = string.getBytes(
                                    StandardCharsets.UTF_8);
                            DatagramPacket packet = new DatagramPacket(
                                    buffer,
                                    buffer.length,
                                    serverDatagramSocket.getLocalAddress(),
                                    serverDatagramSocket.getLocalPort());
                            gssDatagramSocket.send(packet);
                            buffer = new byte[RECEIVE_BUFFER_SIZE];
                            packet = new DatagramPacket(buffer, buffer.length);
                            gssDatagramSocket.receive(packet);
                            returningStringHolder.set(new String(
                                    Arrays.copyOfRange(
                                            packet.getData(),
                                            packet.getOffset(),
                                            packet.getLength()),
                                    StandardCharsets.UTF_8));
                        }
                    }),
                    ((socket, gssContext) -> {
                        try (GssDatagramSocket gssDatagramSocket =
                                     new GssDatagramSocket(
                                             serverDatagramSocket,
                                             gssContext,
                                             messageProp)) {
                            byte[] buffer = new byte[RECEIVE_BUFFER_SIZE];
                            DatagramPacket packet = new DatagramPacket(
                                    buffer, buffer.length);
                            gssDatagramSocket.receive(packet);
                            byte[] bytes = Arrays.copyOfRange(
                                    packet.getData(),
                                    packet.getOffset(),
                                    packet.getLength());
                            packet = new DatagramPacket(
                                    bytes,
                                    bytes.length,
                                    clientDatagramSocket.getLocalAddress(),
                                    clientDatagramSocket.getLocalPort());
                            gssDatagramSocket.send(packet);
                            ThreadHelper.interruptibleSleepForThreeSeconds();
                        }
                    }));
        }
        return returningStringHolder.get();
    }

    @BeforeClass
    public static void setUpBeforeClass() throws IOException {
        GssEnvironment.setUpBeforeClass(GssDatagramSocketIT.class);
    }

    @AfterClass
    public static void tearDownAfterClass() throws IOException {
        GssEnvironment.tearDownAfterClass(GssDatagramSocketIT.class);
    }

    @Rule
    public Timeout globalTimeout = Timeout.builder()
            .withTimeout(60, TimeUnit.SECONDS)
            .withLookingForStuckThread(true)
            .build();

    @Test(expected = UnsupportedOperationException.class)
    public void testGetChannelForUnsupportedOperationException() throws GSSException, IOException {
        GssObjectITHelper.testClientAndServerGssObjects(
                (socket, gssContext) -> {
                    try (GssDatagramSocket gssDatagramSocket =
                                 new GssDatagramSocket(
                                         new DatagramSocket(null),
                            gssContext,
                            new MessageProp(0, false))) {
                        gssDatagramSocket.getChannel();
                    }
                },
                ((socket, gssContext) -> {}));
    }

    @Test
    public void testEcho01() throws GSSException, IOException {
        String string = StringConstants.STRING_01;
        String returningString = echo(string, null);
        Assert.assertEquals(string, returningString);
    }

    @Test
    public void testEcho02() throws GSSException, IOException {
        String string = StringConstants.STRING_02;
        String returningString = echo(string, null);
        Assert.assertEquals(string, returningString);
    }

    @Test
    public void testEcho03() throws GSSException, IOException {
        String string = StringConstants.STRING_03;
        String returningString = echo(string, null);
        Assert.assertEquals(string, returningString);
    }

    @Test
    public void testEcho04() throws GSSException, IOException {
        String string = StringConstants.STRING_04;
        String returningString = echo(string, null);
        Assert.assertEquals(string, returningString);
    }

    @Test
    public void testEcho05() throws GSSException, IOException {
        String string = StringConstants.STRING_05;
        String returningString = echo(string, null);
        Assert.assertEquals(string, returningString);
    }

    @Test
    public void testEchoWithMessageProp0False01() throws GSSException, IOException {
        String string = StringConstants.STRING_01;
        String returningString = echo(string, new MessageProp(0, false));
        Assert.assertEquals(string, returningString);
    }

    @Test
    public void testEchoWithMessageProp0False02() throws GSSException, IOException {
        String string = StringConstants.STRING_02;
        String returningString = echo(string, new MessageProp(0, false));
        Assert.assertEquals(string, returningString);
    }

    @Test
    public void testEchoWithMessageProp0False03() throws GSSException, IOException {
        String string = StringConstants.STRING_03;
        String returningString = echo(string, new MessageProp(0, false));
        Assert.assertEquals(string, returningString);
    }

    @Test
    public void testEchoWithMessageProp0False04() throws GSSException, IOException {
        String string = StringConstants.STRING_04;
        String returningString = echo(string, new MessageProp(0, false));
        Assert.assertEquals(string, returningString);
    }

    @Test
    public void testEchoWithMessageProp0False05() throws GSSException, IOException {
        String string = StringConstants.STRING_05;
        String returningString = echo(string, new MessageProp(0, false));
        Assert.assertEquals(string, returningString);
    }

    @Test
    public void testEchoWithMessageProp0True01() throws GSSException, IOException {
        String string = StringConstants.STRING_01;
        String returningString = echo(string, new MessageProp(0, true));
        Assert.assertEquals(string, returningString);
    }

    @Test
    public void testEchoWithMessageProp0True02() throws GSSException, IOException {
        String string = StringConstants.STRING_02;
        String returningString = echo(string, new MessageProp(0, true));
        Assert.assertEquals(string, returningString);
    }

    @Test
    public void testEchoWithMessageProp0True03() throws GSSException, IOException {
        String string = StringConstants.STRING_03;
        String returningString = echo(string, new MessageProp(0, true));
        Assert.assertEquals(string, returningString);
    }

    @Test
    public void testEchoWithMessageProp0True04() throws GSSException, IOException {
        String string = StringConstants.STRING_04;
        String returningString = echo(string, new MessageProp(0, true));
        Assert.assertEquals(string, returningString);
    }

    @Test
    public void testEchoWithMessageProp0True05() throws GSSException, IOException {
        String string = StringConstants.STRING_05;
        String returningString = echo(string, new MessageProp(0, true));
        Assert.assertEquals(string, returningString);
    }

    @Test
    public void testEchoWithMessageProp1False01() throws GSSException, IOException {
        String string = StringConstants.STRING_01;
        String returningString = echo(string, new MessageProp(1, false));
        Assert.assertEquals(string, returningString);
    }

    @Test
    public void testEchoWithMessageProp1False02() throws GSSException, IOException {
        String string = StringConstants.STRING_02;
        String returningString = echo(string, new MessageProp(1, false));
        Assert.assertEquals(string, returningString);
    }

    @Test
    public void testEchoWithMessageProp1False03() throws GSSException, IOException {
        String string = StringConstants.STRING_03;
        String returningString = echo(string, new MessageProp(1, false));
        Assert.assertEquals(string, returningString);
    }

    @Test
    public void testEchoWithMessageProp1False04() throws GSSException, IOException {
        String string = StringConstants.STRING_04;
        String returningString = echo(string, new MessageProp(1, false));
        Assert.assertEquals(string, returningString);
    }

    @Test
    public void testEchoWithMessageProp1False05() throws GSSException, IOException {
        String string = StringConstants.STRING_05;
        String returningString = echo(string, new MessageProp(1, false));
        Assert.assertEquals(string, returningString);
    }

    @Test
    public void testEchoWithMessageProp1True01() throws GSSException, IOException {
        String string = StringConstants.STRING_01;
        String returningString = echo(string, new MessageProp(1, true));
        Assert.assertEquals(string, returningString);
    }

    @Test
    public void testEchoWithMessageProp1True02() throws GSSException, IOException {
        String string = StringConstants.STRING_02;
        String returningString = echo(string, new MessageProp(1, true));
        Assert.assertEquals(string, returningString);
    }

    @Test
    public void testEchoWithMessageProp1True03() throws GSSException, IOException {
        String string = StringConstants.STRING_03;
        String returningString = echo(string, new MessageProp(1, true));
        Assert.assertEquals(string, returningString);
    }

    @Test
    public void testEchoWithMessageProp1True04() throws GSSException, IOException {
        String string = StringConstants.STRING_04;
        String returningString = echo(string, new MessageProp(1, true));
        Assert.assertEquals(string, returningString);
    }

    @Test
    public void testEchoWithMessageProp1True05() throws GSSException, IOException {
        String string = StringConstants.STRING_05;
        String returningString = echo(string, new MessageProp(1, true));
        Assert.assertEquals(string, returningString);
    }
    
    @Test(expected = IOException.class)
    public void testEchoWithMessageProp0FalseForIOException06() throws GSSException, IOException {
        String string = StringConstants.STRING_06;
        String returningString = echo(string, new MessageProp(0, false));
        Assert.assertEquals(string, returningString);
    }

    @Test(expected = IOException.class)
    public void testEchoWithMessageProp0FalseForIOException07() throws GSSException, IOException {
        String string = StringConstants.STRING_07;
        String returningString = echo(string, new MessageProp(0, false));
        Assert.assertEquals(string, returningString);
    }

    @Test(expected = IOException.class)
    public void testEchoWithMessageProp0FalseForIOException08() throws GSSException, IOException {
        String string = StringConstants.STRING_08;
        String returningString = echo(string, new MessageProp(0, false));
        Assert.assertEquals(string, returningString);
    }

    @Test(expected = IOException.class)
    public void testEchoWithMessageProp0FalseForIOException09() throws GSSException, IOException {
        String string = StringConstants.STRING_09;
        String returningString = echo(string, new MessageProp(0, false));
        Assert.assertEquals(string, returningString);
    }

    @Test(expected = IOException.class)
    public void testEchoWithMessageProp0TrueForIOException06() throws GSSException, IOException {
        String string = StringConstants.STRING_06;
        String returningString = echo(string, new MessageProp(0, true));
        Assert.assertEquals(string, returningString);
    }

    @Test(expected = IOException.class)
    public void testEchoWithMessageProp0TrueForIOException07() throws GSSException, IOException {
        String string = StringConstants.STRING_07;
        String returningString = echo(string, new MessageProp(0, true));
        Assert.assertEquals(string, returningString);
    }

    @Test(expected = IOException.class)
    public void testEchoWithMessageProp0TrueForIOException08() throws GSSException, IOException {
        String string = StringConstants.STRING_08;
        String returningString = echo(string, new MessageProp(0, true));
        Assert.assertEquals(string, returningString);
    }

    @Test(expected = IOException.class)
    public void testEchoWithMessageProp0TrueForIOException09() throws GSSException, IOException {
        String string = StringConstants.STRING_09;
        String returningString = echo(string, new MessageProp(0, true));
        Assert.assertEquals(string, returningString);
    }

    @Test(expected = IOException.class)
    public void testEchoWithMessageProp1FalseForIOException06() throws GSSException, IOException {
        String string = StringConstants.STRING_06;
        String returningString = echo(string, new MessageProp(1, false));
        Assert.assertEquals(string, returningString);
    }

    @Test(expected = IOException.class)
    public void testEchoWithMessageProp1FalseForIOException07() throws GSSException, IOException {
        String string = StringConstants.STRING_07;
        String returningString = echo(string, new MessageProp(1, false));
        Assert.assertEquals(string, returningString);
    }

    @Test(expected = IOException.class)
    public void testEchoWithMessageProp1FalseForIOException08() throws GSSException, IOException {
        String string = StringConstants.STRING_08;
        String returningString = echo(string, new MessageProp(1, false));
        Assert.assertEquals(string, returningString);
    }

    @Test(expected = IOException.class)
    public void testEchoWithMessageProp1FalseForIOException09() throws GSSException, IOException {
        String string = StringConstants.STRING_09;
        String returningString = echo(string, new MessageProp(1, false));
        Assert.assertEquals(string, returningString);
    }

    @Test(expected = IOException.class)
    public void testEchoWithMessageProp1TrueForIOException06() throws GSSException, IOException {
        String string = StringConstants.STRING_06;
        String returningString = echo(string, new MessageProp(1, true));
        Assert.assertEquals(string, returningString);
    }

    @Test(expected = IOException.class)
    public void testEchoWithMessageProp1TrueForIOException07() throws GSSException, IOException {
        String string = StringConstants.STRING_07;
        String returningString = echo(string, new MessageProp(1, true));
        Assert.assertEquals(string, returningString);
    }

    @Test(expected = IOException.class)
    public void testEchoWithMessageProp1TrueForIOException08() throws GSSException, IOException {
        String string = StringConstants.STRING_08;
        String returningString = echo(string, new MessageProp(1, true));
        Assert.assertEquals(string, returningString);
    }

    @Test(expected = IOException.class)
    public void testEchoWithMessageProp1TrueForIOException09() throws GSSException, IOException {
        String string = StringConstants.STRING_09;
        String returningString = echo(string, new MessageProp(1, true));
        Assert.assertEquals(string, returningString);
    }

}