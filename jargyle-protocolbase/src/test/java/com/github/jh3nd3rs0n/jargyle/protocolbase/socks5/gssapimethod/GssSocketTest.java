package com.github.jh3nd3rs0n.jargyle.protocolbase.socks5.gssapimethod;

import com.github.jh3nd3rs0n.jargyle.protocolbase.TestGssEnvironment;
import com.github.jh3nd3rs0n.jargyle.test.help.io.MeasuredIoHelper;
import com.github.jh3nd3rs0n.jargyle.test.help.string.TestStringConstants;
import org.ietf.jgss.GSSException;
import org.ietf.jgss.MessageProp;
import org.junit.*;
import org.junit.rules.Timeout;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeUnit;

public class GssSocketTest {

    private static String echo(
            final String string,
            final MessageProp messageProp) throws GSSException, IOException {
        Holder<String> returningStringHolder = new Holder<>();
        GssObjectTestHelper.testClientAndServerGssObjects(
                ((socket, gssContext) -> {
                    try (GssSocket gssSocket = new GssSocket(
                            socket, gssContext, messageProp)) {
                        InputStream in = gssSocket.getInputStream();
                        OutputStream out = gssSocket.getOutputStream();
                        MeasuredIoHelper.writeThenFlush(
                                string.getBytes(StandardCharsets.UTF_8),
                                out);
                        byte[] bytes = MeasuredIoHelper.readFrom(in);
                        returningStringHolder.set(new String(
                                bytes, StandardCharsets.UTF_8));
                    }
                }),
                ((socket, gssContext) -> {
                    try (GssSocket gssSocket = new GssSocket(
                            socket, gssContext, messageProp)) {
                        InputStream in = gssSocket.getInputStream();
                        OutputStream out = gssSocket.getOutputStream();
                        byte[] bytes = MeasuredIoHelper.readFrom(in);
                        MeasuredIoHelper.writeThenFlush(bytes, out);
                    }
                }));
        return returningStringHolder.get();
    }

    @BeforeClass
    public static void setUpBeforeClass() throws IOException {
        TestGssEnvironment.setUpBeforeClass(GssSocketTest.class);
    }

    @AfterClass
    public static void tearDownAfterClass() throws IOException {
        TestGssEnvironment.tearDownAfterClass(GssSocketTest.class);
    }

    @Rule
    public Timeout globalTimeout = Timeout.builder()
            .withTimeout(5, TimeUnit.SECONDS)
            .withLookingForStuckThread(true)
            .build();

    @Test(expected = UnsupportedOperationException.class)
    public void testGetChannelForUnsupportedOperationException() throws GSSException, IOException {
        GssObjectTestHelper.testClientAndServerGssObjects(
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
        String string = TestStringConstants.STRING_01;
        String returningString = echo(string, null);
        Assert.assertEquals(string, returningString);
    }

    @Test
    public void testEcho02() throws GSSException, IOException {
        String string = TestStringConstants.STRING_02;
        String returningString = echo(string, null);
        Assert.assertEquals(string, returningString);
    }

    @Test
    public void testEcho03() throws GSSException, IOException {
        String string = TestStringConstants.STRING_03;
        String returningString = echo(string, null);
        Assert.assertEquals(string, returningString);
    }

    @Test
    public void testEcho04() throws GSSException, IOException {
        String string = TestStringConstants.STRING_04;
        String returningString = echo(string, null);
        Assert.assertEquals(string, returningString);
    }

    @Test
    public void testEchoWithMessagePropQop0PrivacyStateFalse01() throws GSSException, IOException {
        String string = TestStringConstants.STRING_01;
        String returningString = echo(string, new MessageProp(0, false));
        Assert.assertEquals(string, returningString);
    }

    @Test
    public void testEchoWithMessagePropQop0PrivacyStateFalse02() throws GSSException, IOException {
        String string = TestStringConstants.STRING_02;
        String returningString = echo(string, new MessageProp(0, false));
        Assert.assertEquals(string, returningString);
    }

    @Test
    public void testEchoWithMessagePropQop0PrivacyStateFalse03() throws GSSException, IOException {
        String string = TestStringConstants.STRING_03;
        String returningString = echo(string, new MessageProp(0, false));
        Assert.assertEquals(string, returningString);
    }

    @Test
    public void testEchoWithMessagePropQop0PrivacyStateFalse04() throws GSSException, IOException {
        String string = TestStringConstants.STRING_04;
        String returningString = echo(string, new MessageProp(0, false));
        Assert.assertEquals(string, returningString);
    }

    @Test
    public void testEchoWithMessagePropQop0PrivacyStateTrue01() throws GSSException, IOException {
        String string = TestStringConstants.STRING_01;
        String returningString = echo(string, new MessageProp(0, true));
        Assert.assertEquals(string, returningString);
    }

    @Test
    public void testEchoWithMessagePropQop0PrivacyStateTrue02() throws GSSException, IOException {
        String string = TestStringConstants.STRING_02;
        String returningString = echo(string, new MessageProp(0, true));
        Assert.assertEquals(string, returningString);
    }

    @Test
    public void testEchoWithMessagePropQop0PrivacyStateTrue03() throws GSSException, IOException {
        String string = TestStringConstants.STRING_03;
        String returningString = echo(string, new MessageProp(0, true));
        Assert.assertEquals(string, returningString);
    }

    @Test
    public void testEchoWithMessagePropQop0PrivacyStateTrue04() throws GSSException, IOException {
        String string = TestStringConstants.STRING_04;
        String returningString = echo(string, new MessageProp(0, true));
        Assert.assertEquals(string, returningString);
    }

    @Test
    public void testEchoWithMessagePropQop1PrivacyStateFalse01() throws GSSException, IOException {
        String string = TestStringConstants.STRING_01;
        String returningString = echo(string, new MessageProp(1, false));
        Assert.assertEquals(string, returningString);
    }

    @Test
    public void testEchoWithMessagePropQop1PrivacyStateFalse02() throws GSSException, IOException {
        String string = TestStringConstants.STRING_02;
        String returningString = echo(string, new MessageProp(1, false));
        Assert.assertEquals(string, returningString);
    }

    @Test
    public void testEchoWithMessagePropQop1PrivacyStateFalse03() throws GSSException, IOException {
        String string = TestStringConstants.STRING_03;
        String returningString = echo(string, new MessageProp(1, false));
        Assert.assertEquals(string, returningString);
    }

    @Test
    public void testEchoWithMessagePropQop1PrivacyStateFalse04() throws GSSException, IOException {
        String string = TestStringConstants.STRING_04;
        String returningString = echo(string, new MessageProp(1, false));
        Assert.assertEquals(string, returningString);
    }

    @Test
    public void testEchoWithMessagePropQop1PrivacyStateTrue01() throws GSSException, IOException {
        String string = TestStringConstants.STRING_01;
        String returningString = echo(string, new MessageProp(1, true));
        Assert.assertEquals(string, returningString);
    }

    @Test
    public void testEchoWithMessagePropQop1PrivacyStateTrue02() throws GSSException, IOException {
        String string = TestStringConstants.STRING_02;
        String returningString = echo(string, new MessageProp(1, true));
        Assert.assertEquals(string, returningString);
    }

    @Test
    public void testEchoWithMessagePropQop1PrivacyStateTrue03() throws GSSException, IOException {
        String string = TestStringConstants.STRING_03;
        String returningString = echo(string, new MessageProp(1, true));
        Assert.assertEquals(string, returningString);
    }

    @Test
    public void testEchoWithMessagePropQop1PrivacyStateTrue04() throws GSSException, IOException {
        String string = TestStringConstants.STRING_04;
        String returningString = echo(string, new MessageProp(1, true));
        Assert.assertEquals(string, returningString);
    }

}