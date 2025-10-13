package com.github.jh3nd3rs0n.jargyle.internal.net.ssl;

import com.github.jh3nd3rs0n.jargyle.test.help.net.Server;
import com.github.jh3nd3rs0n.jargyle.test.help.security.KeyStoreResourceConstants;
import com.github.jh3nd3rs0n.jargyle.test.help.thread.ThreadHelper;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.Socket;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;

public class DefaultSslSocketFactoryIT {

    private static Server server;
    private static int serverPort;

    @BeforeClass
    public static void setUpBeforeClass() throws IOException {
        server = new Server(
                new Server.DefaultServerSocketFactory(),
                0,
                new Server.DefaultWorkerFactory());
        server.start();
        serverPort = server.getPort();
    }

    @AfterClass
    public static void tearDownAfterClass() throws IOException {
        if (server != null
                && !server.getState().equals(Server.State.STOPPED)) {
            server.stop();
        }
        ThreadHelper.interruptibleSleepForThreeSeconds();
    }

    @Test
    public void testGetSocketSocketInputStreamBooleanWithSocketNullFalse01() throws IOException, NoSuchAlgorithmException, KeyManagementException {
        try (Socket socket = new Socket((String) null, serverPort)) {
            DefaultSslSocketFactory defaultSslSocketFactory = new DefaultSslSocketFactory(SslContextHelper.getSslContext(
                    "TLSv1.2",
                    KeyManagerHelper.getKeyManagers(
                            new ByteArrayInputStream(KeyStoreResourceConstants.JARGYLE_TEST_HELP_SECURITY_KEY_STORE_FILE_1.getContentAsBytes()),
                            KeyStoreResourceConstants.JARGYLE_TEST_HELP_SECURITY_KEY_STORE_PASSWORD_FILE_1.getContentAsString().toCharArray(),
                            null),
                    TrustManagerHelper.getTrustManagers(
                            new ByteArrayInputStream(KeyStoreResourceConstants.JARGYLE_TEST_HELP_SECURITY_KEY_STORE_FILE_2.getContentAsBytes()),
                            KeyStoreResourceConstants.JARGYLE_TEST_HELP_SECURITY_KEY_STORE_PASSWORD_FILE_2.getContentAsString().toCharArray(),
                            null)));
            Assert.assertNotNull(defaultSslSocketFactory.getSocket(
                    socket, null, false));
        }
    }

    @Test
    public void testGetSocketSocketInputStreamBooleanWithSocketInputStreamFalse01() throws IOException, NoSuchAlgorithmException, KeyManagementException {
        try (Socket socket = new Socket((String) null, serverPort)) {
            DefaultSslSocketFactory defaultSslSocketFactory = new DefaultSslSocketFactory(SslContextHelper.getSslContext(
                    "TLSv1.2",
                    KeyManagerHelper.getKeyManagers(
                            new ByteArrayInputStream(KeyStoreResourceConstants.JARGYLE_TEST_HELP_SECURITY_KEY_STORE_FILE_1.getContentAsBytes()),
                            KeyStoreResourceConstants.JARGYLE_TEST_HELP_SECURITY_KEY_STORE_PASSWORD_FILE_1.getContentAsString().toCharArray(),
                            null),
                    TrustManagerHelper.getTrustManagers(
                            new ByteArrayInputStream(KeyStoreResourceConstants.JARGYLE_TEST_HELP_SECURITY_KEY_STORE_FILE_2.getContentAsBytes()),
                            KeyStoreResourceConstants.JARGYLE_TEST_HELP_SECURITY_KEY_STORE_PASSWORD_FILE_2.getContentAsString().toCharArray(),
                            null)));
            Assert.assertNotNull(defaultSslSocketFactory.getSocket(
                    socket, new ByteArrayInputStream(new byte[] { }), false));
        }
    }

    @Test
    public void testGetSocketSocketInputStreamBooleanWithSocketInputStreamTrue01() throws IOException, NoSuchAlgorithmException, KeyManagementException {
        try (Socket socket = new Socket((String) null, serverPort)) {
            DefaultSslSocketFactory defaultSslSocketFactory = new DefaultSslSocketFactory(SslContextHelper.getSslContext(
                    "TLSv1.2",
                    KeyManagerHelper.getKeyManagers(
                            new ByteArrayInputStream(KeyStoreResourceConstants.JARGYLE_TEST_HELP_SECURITY_KEY_STORE_FILE_1.getContentAsBytes()),
                            KeyStoreResourceConstants.JARGYLE_TEST_HELP_SECURITY_KEY_STORE_PASSWORD_FILE_1.getContentAsString().toCharArray(),
                            null),
                    TrustManagerHelper.getTrustManagers(
                            new ByteArrayInputStream(KeyStoreResourceConstants.JARGYLE_TEST_HELP_SECURITY_KEY_STORE_FILE_2.getContentAsBytes()),
                            KeyStoreResourceConstants.JARGYLE_TEST_HELP_SECURITY_KEY_STORE_PASSWORD_FILE_2.getContentAsString().toCharArray(),
                            null)));
            Assert.assertNotNull(defaultSslSocketFactory.getSocket(
                    socket, new ByteArrayInputStream(new byte[] { }), true));
        }
    }

    @Test
    public void testGetSocketSocketInputStreamBooleanWithSocketNullTrue01() throws IOException, NoSuchAlgorithmException, KeyManagementException {
        try (Socket socket = new Socket((String) null, serverPort)) {
            DefaultSslSocketFactory defaultSslSocketFactory = new DefaultSslSocketFactory(SslContextHelper.getSslContext(
                    "TLSv1.2",
                    KeyManagerHelper.getKeyManagers(
                            new ByteArrayInputStream(KeyStoreResourceConstants.JARGYLE_TEST_HELP_SECURITY_KEY_STORE_FILE_1.getContentAsBytes()),
                            KeyStoreResourceConstants.JARGYLE_TEST_HELP_SECURITY_KEY_STORE_PASSWORD_FILE_1.getContentAsString().toCharArray(),
                            null),
                    TrustManagerHelper.getTrustManagers(
                            new ByteArrayInputStream(KeyStoreResourceConstants.JARGYLE_TEST_HELP_SECURITY_KEY_STORE_FILE_2.getContentAsBytes()),
                            KeyStoreResourceConstants.JARGYLE_TEST_HELP_SECURITY_KEY_STORE_PASSWORD_FILE_2.getContentAsString().toCharArray(),
                            null)));
            Assert.assertNotNull(defaultSslSocketFactory.getSocket(
                    socket, null, true));
        }
    }

    @Test
    public void testGetSocketSocketStringIntBooleanWithSocketStringIntFalse01() throws IOException, NoSuchAlgorithmException, KeyManagementException {
        try (Socket socket = new Socket((String) null, serverPort)) {
            DefaultSslSocketFactory defaultSslSocketFactory = new DefaultSslSocketFactory(SslContextHelper.getSslContext(
                    "TLSv1.2",
                    KeyManagerHelper.getKeyManagers(
                            new ByteArrayInputStream(KeyStoreResourceConstants.JARGYLE_TEST_HELP_SECURITY_KEY_STORE_FILE_1.getContentAsBytes()),
                            KeyStoreResourceConstants.JARGYLE_TEST_HELP_SECURITY_KEY_STORE_PASSWORD_FILE_1.getContentAsString().toCharArray(),
                            null),
                    TrustManagerHelper.getTrustManagers(
                            new ByteArrayInputStream(KeyStoreResourceConstants.JARGYLE_TEST_HELP_SECURITY_KEY_STORE_FILE_2.getContentAsBytes()),
                            KeyStoreResourceConstants.JARGYLE_TEST_HELP_SECURITY_KEY_STORE_PASSWORD_FILE_2.getContentAsString().toCharArray(),
                            null)));
            Assert.assertNotNull(defaultSslSocketFactory.getSocket(
                    socket, "localhost", serverPort, false));
        }
    }

    @Test
    public void testGetSocketSocketStringIntBooleanWithSocketStringIntTrue01() throws IOException, NoSuchAlgorithmException, KeyManagementException {
        try (Socket socket = new Socket((String) null, serverPort)) {
            DefaultSslSocketFactory defaultSslSocketFactory = new DefaultSslSocketFactory(SslContextHelper.getSslContext(
                    "TLSv1.2",
                    KeyManagerHelper.getKeyManagers(
                            new ByteArrayInputStream(KeyStoreResourceConstants.JARGYLE_TEST_HELP_SECURITY_KEY_STORE_FILE_1.getContentAsBytes()),
                            KeyStoreResourceConstants.JARGYLE_TEST_HELP_SECURITY_KEY_STORE_PASSWORD_FILE_1.getContentAsString().toCharArray(),
                            null),
                    TrustManagerHelper.getTrustManagers(
                            new ByteArrayInputStream(KeyStoreResourceConstants.JARGYLE_TEST_HELP_SECURITY_KEY_STORE_FILE_2.getContentAsBytes()),
                            KeyStoreResourceConstants.JARGYLE_TEST_HELP_SECURITY_KEY_STORE_PASSWORD_FILE_2.getContentAsString().toCharArray(),
                            null)));
            Assert.assertNotNull(defaultSslSocketFactory.getSocket(
                    socket, "localhost", serverPort, true));
        }
    }

}