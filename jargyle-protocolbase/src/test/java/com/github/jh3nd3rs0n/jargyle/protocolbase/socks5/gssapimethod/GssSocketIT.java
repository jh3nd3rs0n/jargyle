package com.github.jh3nd3rs0n.jargyle.protocolbase.socks5.gssapimethod;

import com.github.jh3nd3rs0n.jargyle.internal.io.InputStreamHelper;
import com.github.jh3nd3rs0n.jargyle.protocolbase.GssEnvironment;
import com.github.jh3nd3rs0n.test.help.string.StringConstants;
import org.ietf.jgss.GSSException;
import org.ietf.jgss.MessageProp;
import org.junit.*;
import org.junit.rules.Timeout;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.SocketException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;

public class GssSocketIT {

    private static int[] toIntBytes(final byte[] bytes) {
        int[] intBytes = new int[bytes.length];
        int i = -1;
        for (byte b : bytes) {
            intBytes[++i] = b & 0xff;
        }
        return Arrays.copyOf(intBytes, i + 1);
    }

    private static String echo(
            final String string,
            final MessageProp messageProp) throws GSSException, IOException {
        byte[] stringBytes = string.getBytes(StandardCharsets.UTF_8);
        int stringBytesLength = stringBytes.length;
        Holder<String> returningStringHolder = new Holder<>();
        returningStringHolder.set("");
        GssObjectITHelper.testClientAndServerGssObjects(
                ((socket, gssContext) -> {
                    try (GssSocket gssSocket = new GssSocket(
                            socket, gssContext, messageProp)) {
                        InputStream in = gssSocket.getInputStream();
                        OutputStream out = gssSocket.getOutputStream();
                        out.write(stringBytes);
                        out.flush();
                        byte[] bytes = new byte[stringBytesLength];
                        int bytesRead = InputStreamHelper.continuouslyReadFrom(
                                in, bytes);
                        bytes = Arrays.copyOf(bytes, bytesRead);
                        returningStringHolder.set(new String(
                                bytes, StandardCharsets.UTF_8));
                    }
                }),
                ((socket, gssContext) -> {
                    try (GssSocket gssSocket = new GssSocket(
                            socket, gssContext, messageProp)) {
                        InputStream in = gssSocket.getInputStream();
                        OutputStream out = gssSocket.getOutputStream();
                        byte[] bytes = new byte[stringBytesLength];
                        int bytesRead = InputStreamHelper.continuouslyReadFrom(
                                in, bytes);
                        bytes = Arrays.copyOf(bytes, bytesRead);
                        out.write(bytes);
                        out.flush();
                    }
                }));
        return returningStringHolder.get();
    }

    private static int testGetInputStreamAvailable(
            final String string,
            final MessageProp messageProp) throws GSSException, IOException {
        byte[] stringBytes = string.getBytes(StandardCharsets.UTF_8);
        int stringBytesLength = stringBytes.length;
        Holder<Integer> availableHolder = new Holder<>();
        availableHolder.set(0);
        GssObjectITHelper.testClientAndServerGssObjects(
                ((socket, gssContext) -> {
                    try (GssSocket gssSocket = new GssSocket(
                            socket, gssContext, messageProp)) {
                        InputStream in = gssSocket.getInputStream();
                        OutputStream out = gssSocket.getOutputStream();
                        out.write(stringBytes);
                        out.flush();
                        availableHolder.set(in.available());
                    }
                }),
                ((socket, gssContext) -> {
                    try (GssSocket gssSocket = new GssSocket(
                            socket, gssContext, messageProp)) {
                        InputStream in = gssSocket.getInputStream();
                        OutputStream out = gssSocket.getOutputStream();
                        byte[] bytes = new byte[stringBytesLength];
                        int bytesRead = InputStreamHelper.continuouslyReadFrom(
                                in, bytes);
                        bytes = Arrays.copyOf(bytes, bytesRead);
                        out.write(bytes);
                        out.flush();
                    }
                }));
        return availableHolder.get();
    }
    
    private static int testGetInputStreamRead(
            final String string, 
            final MessageProp messageProp) throws GSSException, IOException {
        byte[] stringBytes = string.getBytes(StandardCharsets.UTF_8);
        int stringBytesLength = stringBytes.length;
        Holder<Integer> byteHolder = new Holder<>();
        byteHolder.set(-1);
        GssObjectITHelper.testClientAndServerGssObjects(
                ((socket, gssContext) -> {
                    try (GssSocket gssSocket = new GssSocket(
                            socket, gssContext, messageProp)) {
                        InputStream in = gssSocket.getInputStream();
                        OutputStream out = gssSocket.getOutputStream();
                        out.write(stringBytes);
                        out.flush();
                        byteHolder.set(in.read());
                    }
                }),
                ((socket, gssContext) -> {
                    try (GssSocket gssSocket = new GssSocket(
                            socket, gssContext, messageProp)) {
                        InputStream in = gssSocket.getInputStream();
                        OutputStream out = gssSocket.getOutputStream();
                        byte[] bytes = new byte[stringBytesLength];
                        int bytesRead = InputStreamHelper.continuouslyReadFrom(
                                in, bytes);
                        bytes = Arrays.copyOf(bytes, bytesRead);
                        out.write(bytes);
                        out.flush();
                    }
                }));
        return byteHolder.get();
    }

    private static int[] testGetOutputStreamWriteInt(
            final int[] b,
            final MessageProp messageProp) throws GSSException, IOException {
        int bytesLength = 0;
        for (int by : b) {
            if (by < 0 || by > 255) {
                continue;
            }
            bytesLength++;
        }
        Holder<Integer> bytesLengthHolder = new Holder<>();
        bytesLengthHolder.set(bytesLength);
        Holder<int[]> returningIntBytesHolder = new Holder<>();
        returningIntBytesHolder.set(new int[] { });
        GssObjectITHelper.testClientAndServerGssObjects(
                ((socket, gssContext) -> {
                    try (GssSocket gssSocket = new GssSocket(
                            socket, gssContext, messageProp)) {
                        InputStream in = gssSocket.getInputStream();
                        OutputStream out = gssSocket.getOutputStream();
                        for (int by : b) {
                            out.write(by);
                        }
                        out.flush();
                        byte[] bytes = new byte[bytesLengthHolder.get()];
                        int bytesRead = InputStreamHelper.continuouslyReadFrom(
                                in, bytes);
                        bytes = Arrays.copyOf(bytes, bytesRead);
                        returningIntBytesHolder.set(toIntBytes(bytes));
                    }
                }),
                ((socket, gssContext) -> {
                    try (GssSocket gssSocket = new GssSocket(
                            socket, gssContext, messageProp)) {
                        InputStream in = gssSocket.getInputStream();
                        OutputStream out = gssSocket.getOutputStream();
                        byte[] bytes = new byte[bytesLengthHolder.get()];
                        int bytesRead = InputStreamHelper.continuouslyReadFrom(
                                in, bytes);
                        bytes = Arrays.copyOf(bytes, bytesRead);
                        out.write(bytes);
                        out.flush();
                    }
                }));
        return returningIntBytesHolder.get();
    }

    @BeforeClass
    public static void setUpBeforeClass() throws IOException {
        GssEnvironment.setUpBeforeClass(GssSocketIT.class);
    }

    @AfterClass
    public static void tearDownAfterClass() throws IOException {
        GssEnvironment.tearDownAfterClass(GssSocketIT.class);
    }

    @Rule
    public Timeout globalTimeout = Timeout.builder()
            .withTimeout(60, TimeUnit.SECONDS)
            .withLookingForStuckThread(true)
            .build();

    @Test(expected = UnsupportedOperationException.class)
    public void testGetChannelForUnsupportedOperationException() throws GSSException, IOException {
        GssObjectITHelper.testClientAndServerGssObjects(
                ((socket, gssContext) -> {
                    try (GssSocket gssSocket = new GssSocket(
                            socket, gssContext, new MessageProp(0, false))) {
                        gssSocket.getChannel();
                    }
                }),
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
    public void testEcho06() throws GSSException, IOException {
        String string = StringConstants.STRING_06;
        String returningString = echo(string, null);
        Assert.assertEquals(string, returningString);
    }

    @Test
    public void testEcho07() throws GSSException, IOException {
        String string = StringConstants.STRING_07;
        String returningString = echo(string, null);
        Assert.assertEquals(string, returningString);
    }

    @Test
    public void testEcho08() throws GSSException, IOException {
        String string = StringConstants.STRING_08;
        String returningString = echo(string, null);
        Assert.assertEquals(string, returningString);
    }

    @Test
    public void testEcho09() throws GSSException, IOException {
        String string = StringConstants.STRING_09;
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
    public void testEchoWithMessageProp0False06() throws GSSException, IOException {
        String string = StringConstants.STRING_06;
        String returningString = echo(string, new MessageProp(0, false));
        Assert.assertEquals(string, returningString);
    }

    @Test
    public void testEchoWithMessageProp0False07() throws GSSException, IOException {
        String string = StringConstants.STRING_07;
        String returningString = echo(string, new MessageProp(0, false));
        Assert.assertEquals(string, returningString);
    }

    @Test
    public void testEchoWithMessageProp0False08() throws GSSException, IOException {
        String string = StringConstants.STRING_08;
        String returningString = echo(string, new MessageProp(0, false));
        Assert.assertEquals(string, returningString);
    }

    @Test
    public void testEchoWithMessageProp0False09() throws GSSException, IOException {
        String string = StringConstants.STRING_09;
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
    public void testEchoWithMessageProp0True06() throws GSSException, IOException {
        String string = StringConstants.STRING_06;
        String returningString = echo(string, new MessageProp(0, true));
        Assert.assertEquals(string, returningString);
    }

    @Test
    public void testEchoWithMessageProp0True07() throws GSSException, IOException {
        String string = StringConstants.STRING_07;
        String returningString = echo(string, new MessageProp(0, true));
        Assert.assertEquals(string, returningString);
    }

    @Test
    public void testEchoWithMessageProp0True08() throws GSSException, IOException {
        String string = StringConstants.STRING_08;
        String returningString = echo(string, new MessageProp(0, true));
        Assert.assertEquals(string, returningString);
    }

    @Test
    public void testEchoWithMessageProp0True09() throws GSSException, IOException {
        String string = StringConstants.STRING_09;
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
    public void testEchoWithMessageProp1False06() throws GSSException, IOException {
        String string = StringConstants.STRING_06;
        String returningString = echo(string, new MessageProp(1, false));
        Assert.assertEquals(string, returningString);
    }

    @Test
    public void testEchoWithMessageProp1False07() throws GSSException, IOException {
        String string = StringConstants.STRING_07;
        String returningString = echo(string, new MessageProp(1, false));
        Assert.assertEquals(string, returningString);
    }

    @Test
    public void testEchoWithMessageProp1False08() throws GSSException, IOException {
        String string = StringConstants.STRING_08;
        String returningString = echo(string, new MessageProp(1, false));
        Assert.assertEquals(string, returningString);
    }

    @Test
    public void testEchoWithMessageProp1False09() throws GSSException, IOException {
        String string = StringConstants.STRING_09;
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

    @Test
    public void testEchoWithMessageProp1True06() throws GSSException, IOException {
        String string = StringConstants.STRING_06;
        String returningString = echo(string, new MessageProp(1, true));
        Assert.assertEquals(string, returningString);
    }

    @Test
    public void testEchoWithMessageProp1True07() throws GSSException, IOException {
        String string = StringConstants.STRING_07;
        String returningString = echo(string, new MessageProp(1, true));
        Assert.assertEquals(string, returningString);
    }

    @Test
    public void testEchoWithMessageProp1True08() throws GSSException, IOException {
        String string = StringConstants.STRING_08;
        String returningString = echo(string, new MessageProp(1, true));
        Assert.assertEquals(string, returningString);
    }

    @Test
    public void testEchoWithMessageProp1True09() throws GSSException, IOException {
        String string = StringConstants.STRING_09;
        String returningString = echo(string, new MessageProp(1, true));
        Assert.assertEquals(string, returningString);
    }

    @Test(expected = SocketException.class)
    public void testGetInputStreamAvailableForSocketException() throws GSSException, IOException {
        MessageProp messageProp = new MessageProp(0, false);
        GssObjectITHelper.testClientAndServerGssObjects(
                ((socket, gssContext) -> {
                    try (GssSocket gssSocket = new GssSocket(
                            socket, gssContext, messageProp)) {
                        InputStream in = gssSocket.getInputStream();
                        in.close();
                        in.available();
                    }
                }),
                ((socket, gssContext) -> {
                }));
    }

    @Test
    public void testGetInputStreamAvailableWithWithMessageProp0False01() throws GSSException, IOException {
        String string = StringConstants.STRING_01;
        int available = testGetInputStreamAvailable(string, new MessageProp(0, false));
        Assert.assertEquals(0, available);
    }

    @Test
    public void testGetInputStreamAvailableWithWithMessageProp0False02() throws GSSException, IOException {
        String string = StringConstants.STRING_02;
        int available = testGetInputStreamAvailable(string, new MessageProp(0, false));
        Assert.assertEquals(0, available);
    }

    @Test
    public void testGetInputStreamAvailableWithWithMessageProp0False03() throws GSSException, IOException {
        String string = StringConstants.STRING_03;
        int available = testGetInputStreamAvailable(string, new MessageProp(0, false));
        Assert.assertEquals(0, available);
    }

    @Test
    public void testGetInputStreamAvailableWithWithMessageProp0False04() throws GSSException, IOException {
        String string = StringConstants.STRING_04;
        int available = testGetInputStreamAvailable(string, new MessageProp(0, false));
        Assert.assertEquals(0, available);
    }

    @Test
    public void testGetInputStreamAvailableWithWithMessageProp0False05() throws GSSException, IOException {
        String string = StringConstants.STRING_05;
        int available = testGetInputStreamAvailable(string, new MessageProp(0, false));
        Assert.assertEquals(0, available);
    }

    @Test
    public void testGetInputStreamAvailableWithWithMessageProp0False06() throws GSSException, IOException {
        String string = StringConstants.STRING_06;
        int available = testGetInputStreamAvailable(string, new MessageProp(0, false));
        Assert.assertEquals(0, available);
    }

    @Test
    public void testGetInputStreamAvailableWithWithMessageProp0False07() throws GSSException, IOException {
        String string = StringConstants.STRING_07;
        int available = testGetInputStreamAvailable(string, new MessageProp(0, false));
        Assert.assertEquals(0, available);
    }

    @Test
    public void testGetInputStreamAvailableWithWithMessageProp0False08() throws GSSException, IOException {
        String string = StringConstants.STRING_08;
        int available = testGetInputStreamAvailable(string, new MessageProp(0, false));
        Assert.assertEquals(0, available);
    }

    @Test
    public void testGetInputStreamAvailableWithWithMessageProp0False09() throws GSSException, IOException {
        String string = StringConstants.STRING_09;
        int available = testGetInputStreamAvailable(string, new MessageProp(0, false));
        Assert.assertEquals(0, available);
    }

    @Test
    public void testGetInputStreamAvailableWithWithMessageProp0True01() throws GSSException, IOException {
        String string = StringConstants.STRING_01;
        int available = testGetInputStreamAvailable(string, new MessageProp(0, true));
        Assert.assertEquals(0, available);
    }

    @Test
    public void testGetInputStreamAvailableWithWithMessageProp0True02() throws GSSException, IOException {
        String string = StringConstants.STRING_02;
        int available = testGetInputStreamAvailable(string, new MessageProp(0, true));
        Assert.assertEquals(0, available);
    }

    @Test
    public void testGetInputStreamAvailableWithWithMessageProp0True03() throws GSSException, IOException {
        String string = StringConstants.STRING_03;
        int available = testGetInputStreamAvailable(string, new MessageProp(0, true));
        Assert.assertEquals(0, available);
    }

    @Test
    public void testGetInputStreamAvailableWithWithMessageProp0True04() throws GSSException, IOException {
        String string = StringConstants.STRING_04;
        int available = testGetInputStreamAvailable(string, new MessageProp(0, true));
        Assert.assertEquals(0, available);
    }

    @Test
    public void testGetInputStreamAvailableWithWithMessageProp0True05() throws GSSException, IOException {
        String string = StringConstants.STRING_05;
        int available = testGetInputStreamAvailable(string, new MessageProp(0, true));
        Assert.assertEquals(0, available);
    }

    @Test
    public void testGetInputStreamAvailableWithWithMessageProp0True06() throws GSSException, IOException {
        String string = StringConstants.STRING_06;
        int available = testGetInputStreamAvailable(string, new MessageProp(0, true));
        Assert.assertEquals(0, available);
    }

    @Test
    public void testGetInputStreamAvailableWithWithMessageProp0True07() throws GSSException, IOException {
        String string = StringConstants.STRING_07;
        int available = testGetInputStreamAvailable(string, new MessageProp(0, true));
        Assert.assertEquals(0, available);
    }

    @Test
    public void testGetInputStreamAvailableWithWithMessageProp0True08() throws GSSException, IOException {
        String string = StringConstants.STRING_08;
        int available = testGetInputStreamAvailable(string, new MessageProp(0, true));
        Assert.assertEquals(0, available);
    }

    @Test
    public void testGetInputStreamAvailableWithWithMessageProp0True09() throws GSSException, IOException {
        String string = StringConstants.STRING_09;
        int available = testGetInputStreamAvailable(string, new MessageProp(0, true));
        Assert.assertEquals(0, available);
    }

    @Test
    public void testGetInputStreamAvailableWithWithMessageProp1False01() throws GSSException, IOException {
        String string = StringConstants.STRING_01;
        int available = testGetInputStreamAvailable(string, new MessageProp(1, false));
        Assert.assertEquals(0, available);
    }

    @Test
    public void testGetInputStreamAvailableWithWithMessageProp1False02() throws GSSException, IOException {
        String string = StringConstants.STRING_02;
        int available = testGetInputStreamAvailable(string, new MessageProp(1, false));
        Assert.assertEquals(0, available);
    }

    @Test
    public void testGetInputStreamAvailableWithWithMessageProp1False03() throws GSSException, IOException {
        String string = StringConstants.STRING_03;
        int available = testGetInputStreamAvailable(string, new MessageProp(1, false));
        Assert.assertEquals(0, available);
    }

    @Test
    public void testGetInputStreamAvailableWithWithMessageProp1False04() throws GSSException, IOException {
        String string = StringConstants.STRING_04;
        int available = testGetInputStreamAvailable(string, new MessageProp(1, false));
        Assert.assertEquals(0, available);
    }

    @Test
    public void testGetInputStreamAvailableWithWithMessageProp1False05() throws GSSException, IOException {
        String string = StringConstants.STRING_05;
        int available = testGetInputStreamAvailable(string, new MessageProp(1, false));
        Assert.assertEquals(0, available);
    }

    @Test
    public void testGetInputStreamAvailableWithWithMessageProp1False06() throws GSSException, IOException {
        String string = StringConstants.STRING_06;
        int available = testGetInputStreamAvailable(string, new MessageProp(1, false));
        Assert.assertEquals(0, available);
    }

    @Test
    public void testGetInputStreamAvailableWithWithMessageProp1False07() throws GSSException, IOException {
        String string = StringConstants.STRING_07;
        int available = testGetInputStreamAvailable(string, new MessageProp(1, false));
        Assert.assertEquals(0, available);
    }

    @Test
    public void testGetInputStreamAvailableWithWithMessageProp1False08() throws GSSException, IOException {
        String string = StringConstants.STRING_08;
        int available = testGetInputStreamAvailable(string, new MessageProp(1, false));
        Assert.assertEquals(0, available);
    }

    @Test
    public void testGetInputStreamAvailableWithWithMessageProp1False09() throws GSSException, IOException {
        String string = StringConstants.STRING_09;
        int available = testGetInputStreamAvailable(string, new MessageProp(1, false));
        Assert.assertEquals(0, available);
    }

    @Test
    public void testGetInputStreamAvailableWithWithMessageProp1True01() throws GSSException, IOException {
        String string = StringConstants.STRING_01;
        int available = testGetInputStreamAvailable(string, new MessageProp(1, true));
        Assert.assertEquals(0, available);
    }

    @Test
    public void testGetInputStreamAvailableWithWithMessageProp1True02() throws GSSException, IOException {
        String string = StringConstants.STRING_02;
        int available = testGetInputStreamAvailable(string, new MessageProp(1, true));
        Assert.assertEquals(0, available);
    }

    @Test
    public void testGetInputStreamAvailableWithWithMessageProp1True03() throws GSSException, IOException {
        String string = StringConstants.STRING_03;
        int available = testGetInputStreamAvailable(string, new MessageProp(1, true));
        Assert.assertEquals(0, available);
    }

    @Test
    public void testGetInputStreamAvailableWithWithMessageProp1True04() throws GSSException, IOException {
        String string = StringConstants.STRING_04;
        int available = testGetInputStreamAvailable(string, new MessageProp(1, true));
        Assert.assertEquals(0, available);
    }

    @Test
    public void testGetInputStreamAvailableWithWithMessageProp1True05() throws GSSException, IOException {
        String string = StringConstants.STRING_05;
        int available = testGetInputStreamAvailable(string, new MessageProp(1, true));
        Assert.assertEquals(0, available);
    }

    @Test
    public void testGetInputStreamAvailableWithWithMessageProp1True06() throws GSSException, IOException {
        String string = StringConstants.STRING_06;
        int available = testGetInputStreamAvailable(string, new MessageProp(1, true));
        Assert.assertEquals(0, available);
    }

    @Test
    public void testGetInputStreamAvailableWithWithMessageProp1True07() throws GSSException, IOException {
        String string = StringConstants.STRING_07;
        int available = testGetInputStreamAvailable(string, new MessageProp(1, true));
        Assert.assertEquals(0, available);
    }

    @Test
    public void testGetInputStreamAvailableWithWithMessageProp1True08() throws GSSException, IOException {
        String string = StringConstants.STRING_08;
        int available = testGetInputStreamAvailable(string, new MessageProp(1, true));
        Assert.assertEquals(0, available);
    }

    @Test
    public void testGetInputStreamAvailableWithWithMessageProp1True09() throws GSSException, IOException {
        String string = StringConstants.STRING_09;
        int available = testGetInputStreamAvailable(string, new MessageProp(1, true));
        Assert.assertEquals(0, available);
    }
    
    @Test(expected = SocketException.class)
    public void testGetInputStreamReadForSocketException() throws GSSException, IOException {
        GssObjectITHelper.testClientAndServerGssObjects(
                ((socket, gssContext) -> {
                    try (GssSocket gssSocket = new GssSocket(
                            socket, gssContext, new MessageProp(0, false))) {
                        InputStream in = gssSocket.getInputStream();
                        in.close();
                        in.read();
                    }
                }),
                ((socket, gssContext) -> {}));        
    }

    @Test
    public void testGetInputStreamReadWithWithMessageProp0False01() throws GSSException, IOException {
        String string = StringConstants.STRING_01;
        int b = testGetInputStreamRead(string, new MessageProp(0, false));
        Assert.assertEquals(string.getBytes(StandardCharsets.UTF_8)[0], b);
    }

    @Test
    public void testGetInputStreamReadWithWithMessageProp0False02() throws GSSException, IOException {
        String string = StringConstants.STRING_02;
        int b = testGetInputStreamRead(string, new MessageProp(0, false));
        Assert.assertEquals(string.getBytes(StandardCharsets.UTF_8)[0], b);
    }

    @Test
    public void testGetInputStreamReadWithWithMessageProp0False03() throws GSSException, IOException {
        String string = StringConstants.STRING_03;
        int b = testGetInputStreamRead(string, new MessageProp(0, false));
        Assert.assertEquals(string.getBytes(StandardCharsets.UTF_8)[0], b);
    }

    @Test
    public void testGetInputStreamReadWithWithMessageProp0False04() throws GSSException, IOException {
        String string = StringConstants.STRING_04;
        int b = testGetInputStreamRead(string, new MessageProp(0, false));
        Assert.assertEquals(string.getBytes(StandardCharsets.UTF_8)[0], b);
    }

    @Test
    public void testGetInputStreamReadWithWithMessageProp0False05() throws GSSException, IOException {
        String string = StringConstants.STRING_05;
        int b = testGetInputStreamRead(string, new MessageProp(0, false));
        Assert.assertEquals(string.getBytes(StandardCharsets.UTF_8)[0], b);
    }

    @Test
    public void testGetInputStreamReadWithWithMessageProp0False06() throws GSSException, IOException {
        String string = StringConstants.STRING_06;
        int b = testGetInputStreamRead(string, new MessageProp(0, false));
        Assert.assertEquals(string.getBytes(StandardCharsets.UTF_8)[0], b);
    }

    @Test
    public void testGetInputStreamReadWithWithMessageProp0False07() throws GSSException, IOException {
        String string = StringConstants.STRING_07;
        int b = testGetInputStreamRead(string, new MessageProp(0, false));
        Assert.assertEquals(string.getBytes(StandardCharsets.UTF_8)[0], b);
    }

    @Test
    public void testGetInputStreamReadWithWithMessageProp0False08() throws GSSException, IOException {
        String string = StringConstants.STRING_08;
        int b = testGetInputStreamRead(string, new MessageProp(0, false));
        Assert.assertEquals(string.getBytes(StandardCharsets.UTF_8)[0], b);
    }

    @Test
    public void testGetInputStreamReadWithWithMessageProp0False09() throws GSSException, IOException {
        String string = StringConstants.STRING_09;
        int b = testGetInputStreamRead(string, new MessageProp(0, false));
        Assert.assertEquals(string.getBytes(StandardCharsets.UTF_8)[0], b);
    }

    @Test
    public void testGetInputStreamReadWithWithMessageProp0True01() throws GSSException, IOException {
        String string = StringConstants.STRING_01;
        int b = testGetInputStreamRead(string, new MessageProp(0, true));
        Assert.assertEquals(string.getBytes(StandardCharsets.UTF_8)[0], b);
    }

    @Test
    public void testGetInputStreamReadWithWithMessageProp0True02() throws GSSException, IOException {
        String string = StringConstants.STRING_02;
        int b = testGetInputStreamRead(string, new MessageProp(0, true));
        Assert.assertEquals(string.getBytes(StandardCharsets.UTF_8)[0], b);
    }

    @Test
    public void testGetInputStreamReadWithWithMessageProp0True03() throws GSSException, IOException {
        String string = StringConstants.STRING_03;
        int b = testGetInputStreamRead(string, new MessageProp(0, true));
        Assert.assertEquals(string.getBytes(StandardCharsets.UTF_8)[0], b);
    }

    @Test
    public void testGetInputStreamReadWithWithMessageProp0True04() throws GSSException, IOException {
        String string = StringConstants.STRING_04;
        int b = testGetInputStreamRead(string, new MessageProp(0, true));
        Assert.assertEquals(string.getBytes(StandardCharsets.UTF_8)[0], b);
    }

    @Test
    public void testGetInputStreamReadWithWithMessageProp0True05() throws GSSException, IOException {
        String string = StringConstants.STRING_05;
        int b = testGetInputStreamRead(string, new MessageProp(0, true));
        Assert.assertEquals(string.getBytes(StandardCharsets.UTF_8)[0], b);
    }

    @Test
    public void testGetInputStreamReadWithWithMessageProp0True06() throws GSSException, IOException {
        String string = StringConstants.STRING_06;
        int b = testGetInputStreamRead(string, new MessageProp(0, true));
        Assert.assertEquals(string.getBytes(StandardCharsets.UTF_8)[0], b);
    }

    @Test
    public void testGetInputStreamReadWithWithMessageProp0True07() throws GSSException, IOException {
        String string = StringConstants.STRING_07;
        int b = testGetInputStreamRead(string, new MessageProp(0, true));
        Assert.assertEquals(string.getBytes(StandardCharsets.UTF_8)[0], b);
    }

    @Test
    public void testGetInputStreamReadWithWithMessageProp0True08() throws GSSException, IOException {
        String string = StringConstants.STRING_08;
        int b = testGetInputStreamRead(string, new MessageProp(0, true));
        Assert.assertEquals(string.getBytes(StandardCharsets.UTF_8)[0], b);
    }

    @Test
    public void testGetInputStreamReadWithWithMessageProp0True09() throws GSSException, IOException {
        String string = StringConstants.STRING_09;
        int b = testGetInputStreamRead(string, new MessageProp(0, true));
        Assert.assertEquals(string.getBytes(StandardCharsets.UTF_8)[0], b);
    }

    @Test
    public void testGetInputStreamReadWithWithMessageProp1False01() throws GSSException, IOException {
        String string = StringConstants.STRING_01;
        int b = testGetInputStreamRead(string, new MessageProp(1, false));
        Assert.assertEquals(string.getBytes(StandardCharsets.UTF_8)[0], b);
    }

    @Test
    public void testGetInputStreamReadWithWithMessageProp1False02() throws GSSException, IOException {
        String string = StringConstants.STRING_02;
        int b = testGetInputStreamRead(string, new MessageProp(1, false));
        Assert.assertEquals(string.getBytes(StandardCharsets.UTF_8)[0], b);
    }

    @Test
    public void testGetInputStreamReadWithWithMessageProp1False03() throws GSSException, IOException {
        String string = StringConstants.STRING_03;
        int b = testGetInputStreamRead(string, new MessageProp(1, false));
        Assert.assertEquals(string.getBytes(StandardCharsets.UTF_8)[0], b);
    }

    @Test
    public void testGetInputStreamReadWithWithMessageProp1False04() throws GSSException, IOException {
        String string = StringConstants.STRING_04;
        int b = testGetInputStreamRead(string, new MessageProp(1, false));
        Assert.assertEquals(string.getBytes(StandardCharsets.UTF_8)[0], b);
    }

    @Test
    public void testGetInputStreamReadWithWithMessageProp1False05() throws GSSException, IOException {
        String string = StringConstants.STRING_05;
        int b = testGetInputStreamRead(string, new MessageProp(1, false));
        Assert.assertEquals(string.getBytes(StandardCharsets.UTF_8)[0], b);
    }

    @Test
    public void testGetInputStreamReadWithWithMessageProp1False06() throws GSSException, IOException {
        String string = StringConstants.STRING_06;
        int b = testGetInputStreamRead(string, new MessageProp(1, false));
        Assert.assertEquals(string.getBytes(StandardCharsets.UTF_8)[0], b);
    }

    @Test
    public void testGetInputStreamReadWithWithMessageProp1False07() throws GSSException, IOException {
        String string = StringConstants.STRING_07;
        int b = testGetInputStreamRead(string, new MessageProp(1, false));
        Assert.assertEquals(string.getBytes(StandardCharsets.UTF_8)[0], b);
    }

    @Test
    public void testGetInputStreamReadWithWithMessageProp1False08() throws GSSException, IOException {
        String string = StringConstants.STRING_08;
        int b = testGetInputStreamRead(string, new MessageProp(1, false));
        Assert.assertEquals(string.getBytes(StandardCharsets.UTF_8)[0], b);
    }

    @Test
    public void testGetInputStreamReadWithWithMessageProp1False09() throws GSSException, IOException {
        String string = StringConstants.STRING_09;
        int b = testGetInputStreamRead(string, new MessageProp(1, false));
        Assert.assertEquals(string.getBytes(StandardCharsets.UTF_8)[0], b);
    }

    @Test
    public void testGetInputStreamReadWithWithMessageProp1True01() throws GSSException, IOException {
        String string = StringConstants.STRING_01;
        int b = testGetInputStreamRead(string, new MessageProp(1, true));
        Assert.assertEquals(string.getBytes(StandardCharsets.UTF_8)[0], b);
    }

    @Test
    public void testGetInputStreamReadWithWithMessageProp1True02() throws GSSException, IOException {
        String string = StringConstants.STRING_02;
        int b = testGetInputStreamRead(string, new MessageProp(1, true));
        Assert.assertEquals(string.getBytes(StandardCharsets.UTF_8)[0], b);
    }

    @Test
    public void testGetInputStreamReadWithWithMessageProp1True03() throws GSSException, IOException {
        String string = StringConstants.STRING_03;
        int b = testGetInputStreamRead(string, new MessageProp(1, true));
        Assert.assertEquals(string.getBytes(StandardCharsets.UTF_8)[0], b);
    }

    @Test
    public void testGetInputStreamReadWithWithMessageProp1True04() throws GSSException, IOException {
        String string = StringConstants.STRING_04;
        int b = testGetInputStreamRead(string, new MessageProp(1, true));
        Assert.assertEquals(string.getBytes(StandardCharsets.UTF_8)[0], b);
    }

    @Test
    public void testGetInputStreamReadWithWithMessageProp1True05() throws GSSException, IOException {
        String string = StringConstants.STRING_05;
        int b = testGetInputStreamRead(string, new MessageProp(1, true));
        Assert.assertEquals(string.getBytes(StandardCharsets.UTF_8)[0], b);
    }

    @Test
    public void testGetInputStreamReadWithWithMessageProp1True06() throws GSSException, IOException {
        String string = StringConstants.STRING_06;
        int b = testGetInputStreamRead(string, new MessageProp(1, true));
        Assert.assertEquals(string.getBytes(StandardCharsets.UTF_8)[0], b);
    }

    @Test
    public void testGetInputStreamReadWithWithMessageProp1True07() throws GSSException, IOException {
        String string = StringConstants.STRING_07;
        int b = testGetInputStreamRead(string, new MessageProp(1, true));
        Assert.assertEquals(string.getBytes(StandardCharsets.UTF_8)[0], b);
    }

    @Test
    public void testGetInputStreamReadWithWithMessageProp1True08() throws GSSException, IOException {
        String string = StringConstants.STRING_08;
        int b = testGetInputStreamRead(string, new MessageProp(1, true));
        Assert.assertEquals(string.getBytes(StandardCharsets.UTF_8)[0], b);
    }

    @Test
    public void testGetInputStreamReadWithWithMessageProp1True09() throws GSSException, IOException {
        String string = StringConstants.STRING_09;
        int b = testGetInputStreamRead(string, new MessageProp(1, true));
        Assert.assertEquals(string.getBytes(StandardCharsets.UTF_8)[0], b);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testGetInputStreamReadByteArrayIntIntForIndexOutOfBoundsException01() throws GSSException, IOException {
        GssObjectITHelper.testClientAndServerGssObjects(
                ((socket, gssContext) -> {
                    try (GssSocket gssSocket = new GssSocket(
                            socket, gssContext, new MessageProp(0, false))) {
                        InputStream in = gssSocket.getInputStream();
                        byte[] b = new byte[3];
                        in.read(b, -1, b.length);
                    }
                }),
                ((socket, gssContext) -> {}));
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testGetInputStreamReadByteArrayIntIntForIndexOutOfBoundsException02() throws GSSException, IOException {
        GssObjectITHelper.testClientAndServerGssObjects(
                ((socket, gssContext) -> {
                    try (GssSocket gssSocket = new GssSocket(
                            socket, gssContext, new MessageProp(0, false))) {
                        InputStream in = gssSocket.getInputStream();
                        byte[] b = new byte[3];
                        in.read(b, 0, -1);
                    }
                }),
                ((socket, gssContext) -> {}));
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testGetInputStreamReadByteArrayIntIntForIndexOutOfBoundsException03() throws GSSException, IOException {
        GssObjectITHelper.testClientAndServerGssObjects(
                ((socket, gssContext) -> {
                    try (GssSocket gssSocket = new GssSocket(
                            socket, gssContext, new MessageProp(0, false))) {
                        InputStream in = gssSocket.getInputStream();
                        byte[] b = new byte[3];
                        in.read(b, 0, b.length + 1);
                    }
                }),
                ((socket, gssContext) -> {}));
    }

    @Test(expected = SocketException.class)
    public void testGetInputStreamReadByteArrayIntIntForSocketException() throws GSSException, IOException {
        GssObjectITHelper.testClientAndServerGssObjects(
                ((socket, gssContext) -> {
                    try (GssSocket gssSocket = new GssSocket(
                            socket, gssContext, new MessageProp(0, false))) {
                        InputStream in = gssSocket.getInputStream();
                        in.close();
                        byte[] b = new byte[3];
                        in.read(b, 0, b.length);
                    }
                }),
                ((socket, gssContext) -> {}));
    }

    @Test(expected = SocketException.class)
    public void testGetOutputStreamFlushForSocketException() throws GSSException, IOException {
        GssObjectITHelper.testClientAndServerGssObjects(
                ((socket, gssContext) -> {
                    try (GssSocket gssSocket = new GssSocket(
                            socket, gssContext, new MessageProp(0, false))) {
                        OutputStream out = gssSocket.getOutputStream();
                        out.close();
                        out.flush();
                    }
                }),
                ((socket, gssContext) -> {}));
    }

    @Test(expected = SocketException.class)
    public void testGetOutputStreamWriteIntForSocketException() throws GSSException, IOException {
        GssObjectITHelper.testClientAndServerGssObjects(
                ((socket, gssContext) -> {
                    try (GssSocket gssSocket = new GssSocket(
                            socket, gssContext, new MessageProp(0, false))) {
                        OutputStream out = gssSocket.getOutputStream();
                        out.close();
                        out.write(0);
                    }
                }),
                ((socket, gssContext) -> {}));
    }

    @Test
    public void testGetOutputStreamWriteIntWithMessageProp0False00() throws GSSException, IOException {
        int[] stringIntBytes = new int[] { -1, 0, 256, 19, 81, -26, 1020 };
        int[] expectedReturningIntBytes = new int[] { 0, 19, 81 };
        int[] returningIntBytes = testGetOutputStreamWriteInt(
                stringIntBytes, new MessageProp(0, false));
        Assert.assertArrayEquals(expectedReturningIntBytes, returningIntBytes);
    }

    @Test
    public void testGetOutputStreamWriteIntWithMessageProp0False01() throws GSSException, IOException {
        int[] stringIntBytes = toIntBytes(
                StringConstants.STRING_01.getBytes(StandardCharsets.UTF_8));
        int[] returningIntBytes = testGetOutputStreamWriteInt(
                stringIntBytes, new MessageProp(0, false));
        Assert.assertArrayEquals(stringIntBytes, returningIntBytes);
    }

    @Test
    public void testGetOutputStreamWriteIntWithMessageProp0False02() throws GSSException, IOException {
        int[] stringIntBytes = toIntBytes(
                StringConstants.STRING_02.getBytes(StandardCharsets.UTF_8));
        int[] returningIntBytes = testGetOutputStreamWriteInt(
                stringIntBytes, new MessageProp(0, false));
        Assert.assertArrayEquals(stringIntBytes, returningIntBytes);
    }

    @Test
    public void testGetOutputStreamWriteIntWithMessageProp0False03() throws GSSException, IOException {
        int[] stringIntBytes = toIntBytes(
                StringConstants.STRING_03.getBytes(StandardCharsets.UTF_8));
        int[] returningIntBytes = testGetOutputStreamWriteInt(
                stringIntBytes, new MessageProp(0, false));
        Assert.assertArrayEquals(stringIntBytes, returningIntBytes);
    }

    @Test
    public void testGetOutputStreamWriteIntWithMessageProp0False04() throws GSSException, IOException {
        int[] stringIntBytes = toIntBytes(
                StringConstants.STRING_04.getBytes(StandardCharsets.UTF_8));
        int[] returningIntBytes = testGetOutputStreamWriteInt(
                stringIntBytes, new MessageProp(0, false));
        Assert.assertArrayEquals(stringIntBytes, returningIntBytes);
    }

    @Test
    public void testGetOutputStreamWriteIntWithMessageProp0False05() throws GSSException, IOException {
        int[] stringIntBytes = toIntBytes(
                StringConstants.STRING_05.getBytes(StandardCharsets.UTF_8));
        int[] returningIntBytes = testGetOutputStreamWriteInt(
                stringIntBytes, new MessageProp(0, false));
        Assert.assertArrayEquals(stringIntBytes, returningIntBytes);
    }

    @Test
    public void testGetOutputStreamWriteIntWithMessageProp0False06() throws GSSException, IOException {
        int[] stringIntBytes = toIntBytes(
                StringConstants.STRING_06.getBytes(StandardCharsets.UTF_8));
        int[] returningIntBytes = testGetOutputStreamWriteInt(
                stringIntBytes, new MessageProp(0, false));
        Assert.assertArrayEquals(stringIntBytes, returningIntBytes);
    }

    @Test
    public void testGetOutputStreamWriteIntWithMessageProp0False07() throws GSSException, IOException {
        int[] stringIntBytes = toIntBytes(
                StringConstants.STRING_07.getBytes(StandardCharsets.UTF_8));
        int[] returningIntBytes = testGetOutputStreamWriteInt(
                stringIntBytes, new MessageProp(0, false));
        Assert.assertArrayEquals(stringIntBytes, returningIntBytes);
    }

    @Test
    public void testGetOutputStreamWriteIntWithMessageProp0False08() throws GSSException, IOException {
        int[] stringIntBytes = toIntBytes(
                StringConstants.STRING_08.getBytes(StandardCharsets.UTF_8));
        int[] returningIntBytes = testGetOutputStreamWriteInt(
                stringIntBytes, new MessageProp(0, false));
        Assert.assertArrayEquals(stringIntBytes, returningIntBytes);
    }

    @Test
    public void testGetOutputStreamWriteIntWithMessageProp0False09() throws GSSException, IOException {
        int[] stringIntBytes = toIntBytes(
                StringConstants.STRING_09.getBytes(StandardCharsets.UTF_8));
        int[] returningIntBytes = testGetOutputStreamWriteInt(
                stringIntBytes, new MessageProp(0, false));
        Assert.assertArrayEquals(stringIntBytes, returningIntBytes);
    }

    @Test
    public void testGetOutputStreamWriteIntWithMessageProp0True00() throws GSSException, IOException {
        int[] stringIntBytes = new int[] { -1, 0, 256, 19, 81, -26, 1020 };
        int[] expectedReturningIntBytes = new int[] { 0, 19, 81 };
        int[] returningIntBytes = testGetOutputStreamWriteInt(
                stringIntBytes, new MessageProp(0, true));
        Assert.assertArrayEquals(expectedReturningIntBytes, returningIntBytes);
    }

    @Test
    public void testGetOutputStreamWriteIntWithMessageProp0True01() throws GSSException, IOException {
        int[] stringIntBytes = toIntBytes(
                StringConstants.STRING_01.getBytes(StandardCharsets.UTF_8));
        int[] returningIntBytes = testGetOutputStreamWriteInt(
                stringIntBytes, new MessageProp(0, true));
        Assert.assertArrayEquals(stringIntBytes, returningIntBytes);
    }

    @Test
    public void testGetOutputStreamWriteIntWithMessageProp0True02() throws GSSException, IOException {
        int[] stringIntBytes = toIntBytes(
                StringConstants.STRING_02.getBytes(StandardCharsets.UTF_8));
        int[] returningIntBytes = testGetOutputStreamWriteInt(
                stringIntBytes, new MessageProp(0, true));
        Assert.assertArrayEquals(stringIntBytes, returningIntBytes);
    }

    @Test
    public void testGetOutputStreamWriteIntWithMessageProp0True03() throws GSSException, IOException {
        int[] stringIntBytes = toIntBytes(
                StringConstants.STRING_03.getBytes(StandardCharsets.UTF_8));
        int[] returningIntBytes = testGetOutputStreamWriteInt(
                stringIntBytes, new MessageProp(0, true));
        Assert.assertArrayEquals(stringIntBytes, returningIntBytes);
    }

    @Test
    public void testGetOutputStreamWriteIntWithMessageProp0True04() throws GSSException, IOException {
        int[] stringIntBytes = toIntBytes(
                StringConstants.STRING_04.getBytes(StandardCharsets.UTF_8));
        int[] returningIntBytes = testGetOutputStreamWriteInt(
                stringIntBytes, new MessageProp(0, true));
        Assert.assertArrayEquals(stringIntBytes, returningIntBytes);
    }

    @Test
    public void testGetOutputStreamWriteIntWithMessageProp0True05() throws GSSException, IOException {
        int[] stringIntBytes = toIntBytes(
                StringConstants.STRING_05.getBytes(StandardCharsets.UTF_8));
        int[] returningIntBytes = testGetOutputStreamWriteInt(
                stringIntBytes, new MessageProp(0, true));
        Assert.assertArrayEquals(stringIntBytes, returningIntBytes);
    }

    @Test
    public void testGetOutputStreamWriteIntWithMessageProp0True06() throws GSSException, IOException {
        int[] stringIntBytes = toIntBytes(
                StringConstants.STRING_06.getBytes(StandardCharsets.UTF_8));
        int[] returningIntBytes = testGetOutputStreamWriteInt(
                stringIntBytes, new MessageProp(0, true));
        Assert.assertArrayEquals(stringIntBytes, returningIntBytes);
    }

    @Test
    public void testGetOutputStreamWriteIntWithMessageProp0True07() throws GSSException, IOException {
        int[] stringIntBytes = toIntBytes(
                StringConstants.STRING_07.getBytes(StandardCharsets.UTF_8));
        int[] returningIntBytes = testGetOutputStreamWriteInt(
                stringIntBytes, new MessageProp(0, true));
        Assert.assertArrayEquals(stringIntBytes, returningIntBytes);
    }

    @Test
    public void testGetOutputStreamWriteIntWithMessageProp0True08() throws GSSException, IOException {
        int[] stringIntBytes = toIntBytes(
                StringConstants.STRING_08.getBytes(StandardCharsets.UTF_8));
        int[] returningIntBytes = testGetOutputStreamWriteInt(
                stringIntBytes, new MessageProp(0, true));
        Assert.assertArrayEquals(stringIntBytes, returningIntBytes);
    }

    @Test
    public void testGetOutputStreamWriteIntWithMessageProp0True09() throws GSSException, IOException {
        int[] stringIntBytes = toIntBytes(
                StringConstants.STRING_09.getBytes(StandardCharsets.UTF_8));
        int[] returningIntBytes = testGetOutputStreamWriteInt(
                stringIntBytes, new MessageProp(0, true));
        Assert.assertArrayEquals(stringIntBytes, returningIntBytes);
    }

    @Test
    public void testGetOutputStreamWriteIntWithMessageProp1False00() throws GSSException, IOException {
        int[] stringIntBytes = new int[] { -1, 0, 256, 19, 81, -26, 1020 };
        int[] expectedReturningIntBytes = new int[] { 0, 19, 81 };
        int[] returningIntBytes = testGetOutputStreamWriteInt(
                stringIntBytes, new MessageProp(1, false));
        Assert.assertArrayEquals(expectedReturningIntBytes, returningIntBytes);
    }

    @Test
    public void testGetOutputStreamWriteIntWithMessageProp1False01() throws GSSException, IOException {
        int[] stringIntBytes = toIntBytes(
                StringConstants.STRING_01.getBytes(StandardCharsets.UTF_8));
        int[] returningIntBytes = testGetOutputStreamWriteInt(
                stringIntBytes, new MessageProp(1, false));
        Assert.assertArrayEquals(stringIntBytes, returningIntBytes);
    }

    @Test
    public void testGetOutputStreamWriteIntWithMessageProp1False02() throws GSSException, IOException {
        int[] stringIntBytes = toIntBytes(
                StringConstants.STRING_02.getBytes(StandardCharsets.UTF_8));
        int[] returningIntBytes = testGetOutputStreamWriteInt(
                stringIntBytes, new MessageProp(1, false));
        Assert.assertArrayEquals(stringIntBytes, returningIntBytes);
    }

    @Test
    public void testGetOutputStreamWriteIntWithMessageProp1False03() throws GSSException, IOException {
        int[] stringIntBytes = toIntBytes(
                StringConstants.STRING_03.getBytes(StandardCharsets.UTF_8));
        int[] returningIntBytes = testGetOutputStreamWriteInt(
                stringIntBytes, new MessageProp(1, false));
        Assert.assertArrayEquals(stringIntBytes, returningIntBytes);
    }

    @Test
    public void testGetOutputStreamWriteIntWithMessageProp1False04() throws GSSException, IOException {
        int[] stringIntBytes = toIntBytes(
                StringConstants.STRING_04.getBytes(StandardCharsets.UTF_8));
        int[] returningIntBytes = testGetOutputStreamWriteInt(
                stringIntBytes, new MessageProp(1, false));
        Assert.assertArrayEquals(stringIntBytes, returningIntBytes);
    }

    @Test
    public void testGetOutputStreamWriteIntWithMessageProp1False05() throws GSSException, IOException {
        int[] stringIntBytes = toIntBytes(
                StringConstants.STRING_05.getBytes(StandardCharsets.UTF_8));
        int[] returningIntBytes = testGetOutputStreamWriteInt(
                stringIntBytes, new MessageProp(1, false));
        Assert.assertArrayEquals(stringIntBytes, returningIntBytes);
    }

    @Test
    public void testGetOutputStreamWriteIntWithMessageProp1False06() throws GSSException, IOException {
        int[] stringIntBytes = toIntBytes(
                StringConstants.STRING_06.getBytes(StandardCharsets.UTF_8));
        int[] returningIntBytes = testGetOutputStreamWriteInt(
                stringIntBytes, new MessageProp(1, false));
        Assert.assertArrayEquals(stringIntBytes, returningIntBytes);
    }

    @Test
    public void testGetOutputStreamWriteIntWithMessageProp1False07() throws GSSException, IOException {
        int[] stringIntBytes = toIntBytes(
                StringConstants.STRING_07.getBytes(StandardCharsets.UTF_8));
        int[] returningIntBytes = testGetOutputStreamWriteInt(
                stringIntBytes, new MessageProp(1, false));
        Assert.assertArrayEquals(stringIntBytes, returningIntBytes);
    }

    @Test
    public void testGetOutputStreamWriteIntWithMessageProp1False08() throws GSSException, IOException {
        int[] stringIntBytes = toIntBytes(
                StringConstants.STRING_08.getBytes(StandardCharsets.UTF_8));
        int[] returningIntBytes = testGetOutputStreamWriteInt(
                stringIntBytes, new MessageProp(1, false));
        Assert.assertArrayEquals(stringIntBytes, returningIntBytes);
    }

    @Test
    public void testGetOutputStreamWriteIntWithMessageProp1False09() throws GSSException, IOException {
        int[] stringIntBytes = toIntBytes(
                StringConstants.STRING_09.getBytes(StandardCharsets.UTF_8));
        int[] returningIntBytes = testGetOutputStreamWriteInt(
                stringIntBytes, new MessageProp(1, false));
        Assert.assertArrayEquals(stringIntBytes, returningIntBytes);
    }

    @Test
    public void testGetOutputStreamWriteIntWithMessageProp1True00() throws GSSException, IOException {
        int[] stringIntBytes = new int[] { -1, 0, 256, 19, 81, -26, 1020 };
        int[] expectedReturningIntBytes = new int[] { 0, 19, 81 };
        int[] returningIntBytes = testGetOutputStreamWriteInt(
                stringIntBytes, new MessageProp(1, true));
        Assert.assertArrayEquals(expectedReturningIntBytes, returningIntBytes);
    }

    @Test
    public void testGetOutputStreamWriteIntWithMessageProp1True01() throws GSSException, IOException {
        int[] stringIntBytes = toIntBytes(
                StringConstants.STRING_01.getBytes(StandardCharsets.UTF_8));
        int[] returningIntBytes = testGetOutputStreamWriteInt(
                stringIntBytes, new MessageProp(1, true));
        Assert.assertArrayEquals(stringIntBytes, returningIntBytes);
    }

    @Test
    public void testGetOutputStreamWriteIntWithMessageProp1True02() throws GSSException, IOException {
        int[] stringIntBytes = toIntBytes(
                StringConstants.STRING_02.getBytes(StandardCharsets.UTF_8));
        int[] returningIntBytes = testGetOutputStreamWriteInt(
                stringIntBytes, new MessageProp(1, true));
        Assert.assertArrayEquals(stringIntBytes, returningIntBytes);
    }

    @Test
    public void testGetOutputStreamWriteIntWithMessageProp1True03() throws GSSException, IOException {
        int[] stringIntBytes = toIntBytes(
                StringConstants.STRING_03.getBytes(StandardCharsets.UTF_8));
        int[] returningIntBytes = testGetOutputStreamWriteInt(
                stringIntBytes, new MessageProp(1, true));
        Assert.assertArrayEquals(stringIntBytes, returningIntBytes);
    }

    @Test
    public void testGetOutputStreamWriteIntWithMessageProp1True04() throws GSSException, IOException {
        int[] stringIntBytes = toIntBytes(
                StringConstants.STRING_04.getBytes(StandardCharsets.UTF_8));
        int[] returningIntBytes = testGetOutputStreamWriteInt(
                stringIntBytes, new MessageProp(1, true));
        Assert.assertArrayEquals(stringIntBytes, returningIntBytes);
    }

    @Test
    public void testGetOutputStreamWriteIntWithMessageProp1True05() throws GSSException, IOException {
        int[] stringIntBytes = toIntBytes(
                StringConstants.STRING_05.getBytes(StandardCharsets.UTF_8));
        int[] returningIntBytes = testGetOutputStreamWriteInt(
                stringIntBytes, new MessageProp(1, true));
        Assert.assertArrayEquals(stringIntBytes, returningIntBytes);
    }

    @Test
    public void testGetOutputStreamWriteIntWithMessageProp1True06() throws GSSException, IOException {
        int[] stringIntBytes = toIntBytes(
                StringConstants.STRING_06.getBytes(StandardCharsets.UTF_8));
        int[] returningIntBytes = testGetOutputStreamWriteInt(
                stringIntBytes, new MessageProp(1, true));
        Assert.assertArrayEquals(stringIntBytes, returningIntBytes);
    }

    @Test
    public void testGetOutputStreamWriteIntWithMessageProp1True07() throws GSSException, IOException {
        int[] stringIntBytes = toIntBytes(
                StringConstants.STRING_07.getBytes(StandardCharsets.UTF_8));
        int[] returningIntBytes = testGetOutputStreamWriteInt(
                stringIntBytes, new MessageProp(1, true));
        Assert.assertArrayEquals(stringIntBytes, returningIntBytes);
    }

    @Test
    public void testGetOutputStreamWriteIntWithMessageProp1True08() throws GSSException, IOException {
        int[] stringIntBytes = toIntBytes(
                StringConstants.STRING_08.getBytes(StandardCharsets.UTF_8));
        int[] returningIntBytes = testGetOutputStreamWriteInt(
                stringIntBytes, new MessageProp(1, true));
        Assert.assertArrayEquals(stringIntBytes, returningIntBytes);
    }

    @Test
    public void testGetOutputStreamWriteIntWithMessageProp1True09() throws GSSException, IOException {
        int[] stringIntBytes = toIntBytes(
                StringConstants.STRING_09.getBytes(StandardCharsets.UTF_8));
        int[] returningIntBytes = testGetOutputStreamWriteInt(
                stringIntBytes, new MessageProp(1, true));
        Assert.assertArrayEquals(stringIntBytes, returningIntBytes);
    }

    @Test(expected = SocketException.class)
    public void testGetOutputStreamWriteByteArrayIntIntForSocketException() throws GSSException, IOException {
        byte[] b = new byte[] { (byte) 0x01, (byte) 0x02, (byte) 0x03 };
        GssObjectITHelper.testClientAndServerGssObjects(
                ((socket, gssContext) -> {
                    try (GssSocket gssSocket = new GssSocket(
                            socket, gssContext, new MessageProp(0, false))) {
                        OutputStream out = gssSocket.getOutputStream();
                        out.close();
                        out.write(b, 0, b.length);
                    }
                }),
                ((socket, gssContext) -> {}));
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testGetOutputStreamWriteByteArrayIntIntForIndexOutOfBoundsException01() throws GSSException, IOException {
        byte[] b = new byte[] { (byte) 0x01, (byte) 0x02, (byte) 0x03 };
        GssObjectITHelper.testClientAndServerGssObjects(
                ((socket, gssContext) -> {
                    try (GssSocket gssSocket = new GssSocket(
                            socket, gssContext, new MessageProp(0, false))) {
                        OutputStream out = gssSocket.getOutputStream();
                        out.write(b, -1, b.length);
                    }
                }),
                ((socket, gssContext) -> {}));
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testGetOutputStreamWriteByteArrayIntIntForIndexOutOfBoundsException02() throws GSSException, IOException {
        byte[] b = new byte[] { (byte) 0x01, (byte) 0x02, (byte) 0x03 };
        GssObjectITHelper.testClientAndServerGssObjects(
                ((socket, gssContext) -> {
                    try (GssSocket gssSocket = new GssSocket(
                            socket, gssContext, new MessageProp(0, false))) {
                        OutputStream out = gssSocket.getOutputStream();
                        out.write(b, 0, -1);
                    }
                }),
                ((socket, gssContext) -> {}));
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testGetOutputStreamWriteByteArrayIntIntForIndexOutOfBoundsException03() throws GSSException, IOException {
        byte[] b = new byte[] { (byte) 0x01, (byte) 0x02, (byte) 0x03 };
        GssObjectITHelper.testClientAndServerGssObjects(
                ((socket, gssContext) -> {
                    try (GssSocket gssSocket = new GssSocket(
                            socket, gssContext, new MessageProp(0, false))) {
                        OutputStream out = gssSocket.getOutputStream();
                        out.write(b, 0, b.length + 1);
                    }
                }),
                ((socket, gssContext) -> {}));
    }

}