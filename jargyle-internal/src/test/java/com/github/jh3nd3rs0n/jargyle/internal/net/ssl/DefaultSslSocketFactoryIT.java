package com.github.jh3nd3rs0n.jargyle.internal.net.ssl;

import com.github.jh3nd3rs0n.jargyle.internal.VirtualThreadPerTaskExecutorOrCachedThreadPoolFactory;
import com.github.jh3nd3rs0n.test.help.net.Server;
import com.github.jh3nd3rs0n.test.help.security.KeyStoreResourceConstants;
import com.github.jh3nd3rs0n.test.help.thread.ThreadHelper;
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
    private static int testServerPort;

    @BeforeClass
    public static void setUpBeforeClass() throws IOException {
        server = new Server(
                new Server.DefaultServerSocketFactory(),
                0,
                new VirtualThreadPerTaskExecutorOrCachedThreadPoolFactory(),
                new Server.DefaultWorkerFactory());
        server.start();
        testServerPort = server.getPort();
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
    public void testNewSocketSocketInputStreamBooleanWithSocketNullFalse01() throws IOException, NoSuchAlgorithmException, KeyManagementException {
        try (Socket socket = new Socket((String) null, testServerPort)) {
            DefaultSslSocketFactory defaultSslSocketFactory = new DefaultSslSocketFactory(SslContextHelper.getSslContext(
                    "TLSv1.2",
                    KeyManagerHelper.getKeyManagers(
                            KeyStoreResourceConstants.JARGYLE_TEST_HELP_SECURITY_KEY_STORE_FILE_1.getInputStream(),
                            KeyStoreResourceConstants.JARGYLE_TEST_HELP_SECURITY_KEY_STORE_PASSWORD_FILE_1.getContentAsString().toCharArray(),
                            null),
                    TrustManagerHelper.getTrustManagers(
                            KeyStoreResourceConstants.JARGYLE_TEST_HELP_SECURITY_KEY_STORE_FILE_2.getInputStream(),
                            KeyStoreResourceConstants.JARGYLE_TEST_HELP_SECURITY_KEY_STORE_PASSWORD_FILE_2.getContentAsString().toCharArray(),
                            null)));
            Assert.assertNotNull(defaultSslSocketFactory.newSocket(
                    socket, null, false));
        }
    }

    @Test
    public void testNewSocketSocketInputStreamBooleanWithSocketInputStreamFalse01() throws IOException, NoSuchAlgorithmException, KeyManagementException {
        try (Socket socket = new Socket((String) null, testServerPort)) {
            DefaultSslSocketFactory defaultSslSocketFactory = new DefaultSslSocketFactory(SslContextHelper.getSslContext(
                    "TLSv1.2",
                    KeyManagerHelper.getKeyManagers(
                            KeyStoreResourceConstants.JARGYLE_TEST_HELP_SECURITY_KEY_STORE_FILE_1.getInputStream(),
                            KeyStoreResourceConstants.JARGYLE_TEST_HELP_SECURITY_KEY_STORE_PASSWORD_FILE_1.getContentAsString().toCharArray(),
                            null),
                    TrustManagerHelper.getTrustManagers(
                            KeyStoreResourceConstants.JARGYLE_TEST_HELP_SECURITY_KEY_STORE_FILE_2.getInputStream(),
                            KeyStoreResourceConstants.JARGYLE_TEST_HELP_SECURITY_KEY_STORE_PASSWORD_FILE_2.getContentAsString().toCharArray(),
                            null)));
            Assert.assertNotNull(defaultSslSocketFactory.newSocket(
                    socket, new ByteArrayInputStream(new byte[] { }), false));
        }
    }

    @Test
    public void testNewSocketSocketInputStreamBooleanWithSocketInputStreamTrue01() throws IOException, NoSuchAlgorithmException, KeyManagementException {
        try (Socket socket = new Socket((String) null, testServerPort)) {
            DefaultSslSocketFactory defaultSslSocketFactory = new DefaultSslSocketFactory(SslContextHelper.getSslContext(
                    "TLSv1.2",
                    KeyManagerHelper.getKeyManagers(
                            KeyStoreResourceConstants.JARGYLE_TEST_HELP_SECURITY_KEY_STORE_FILE_1.getInputStream(),
                            KeyStoreResourceConstants.JARGYLE_TEST_HELP_SECURITY_KEY_STORE_PASSWORD_FILE_1.getContentAsString().toCharArray(),
                            null),
                    TrustManagerHelper.getTrustManagers(
                            KeyStoreResourceConstants.JARGYLE_TEST_HELP_SECURITY_KEY_STORE_FILE_2.getInputStream(),
                            KeyStoreResourceConstants.JARGYLE_TEST_HELP_SECURITY_KEY_STORE_PASSWORD_FILE_2.getContentAsString().toCharArray(),
                            null)));
            Assert.assertNotNull(defaultSslSocketFactory.newSocket(
                    socket, new ByteArrayInputStream(new byte[] { }), true));
        }
    }

    @Test
    public void testNewSocketSocketInputStreamBooleanWithSocketNullTrue01() throws IOException, NoSuchAlgorithmException, KeyManagementException {
        try (Socket socket = new Socket((String) null, testServerPort)) {
            DefaultSslSocketFactory defaultSslSocketFactory = new DefaultSslSocketFactory(SslContextHelper.getSslContext(
                    "TLSv1.2",
                    KeyManagerHelper.getKeyManagers(
                            KeyStoreResourceConstants.JARGYLE_TEST_HELP_SECURITY_KEY_STORE_FILE_1.getInputStream(),
                            KeyStoreResourceConstants.JARGYLE_TEST_HELP_SECURITY_KEY_STORE_PASSWORD_FILE_1.getContentAsString().toCharArray(),
                            null),
                    TrustManagerHelper.getTrustManagers(
                            KeyStoreResourceConstants.JARGYLE_TEST_HELP_SECURITY_KEY_STORE_FILE_2.getInputStream(),
                            KeyStoreResourceConstants.JARGYLE_TEST_HELP_SECURITY_KEY_STORE_PASSWORD_FILE_2.getContentAsString().toCharArray(),
                            null)));
            Assert.assertNotNull(defaultSslSocketFactory.newSocket(
                    socket, null, true));
        }
    }

    @Test
    public void testNewSocketSocketStringIntBooleanWithSocketStringIntFalse01() throws IOException, NoSuchAlgorithmException, KeyManagementException {
        try (Socket socket = new Socket((String) null, testServerPort)) {
            DefaultSslSocketFactory defaultSslSocketFactory = new DefaultSslSocketFactory(SslContextHelper.getSslContext(
                    "TLSv1.2",
                    KeyManagerHelper.getKeyManagers(
                            KeyStoreResourceConstants.JARGYLE_TEST_HELP_SECURITY_KEY_STORE_FILE_1.getInputStream(),
                            KeyStoreResourceConstants.JARGYLE_TEST_HELP_SECURITY_KEY_STORE_PASSWORD_FILE_1.getContentAsString().toCharArray(),
                            null),
                    TrustManagerHelper.getTrustManagers(
                            KeyStoreResourceConstants.JARGYLE_TEST_HELP_SECURITY_KEY_STORE_FILE_2.getInputStream(),
                            KeyStoreResourceConstants.JARGYLE_TEST_HELP_SECURITY_KEY_STORE_PASSWORD_FILE_2.getContentAsString().toCharArray(),
                            null)));
            Assert.assertNotNull(defaultSslSocketFactory.newSocket(
                    socket, "localhost", testServerPort, false));
        }
    }

    @Test
    public void testNewSocketSocketStringIntBooleanWithSocketStringIntTrue01() throws IOException, NoSuchAlgorithmException, KeyManagementException {
        try (Socket socket = new Socket((String) null, testServerPort)) {
            DefaultSslSocketFactory defaultSslSocketFactory = new DefaultSslSocketFactory(SslContextHelper.getSslContext(
                    "TLSv1.2",
                    KeyManagerHelper.getKeyManagers(
                            KeyStoreResourceConstants.JARGYLE_TEST_HELP_SECURITY_KEY_STORE_FILE_1.getInputStream(),
                            KeyStoreResourceConstants.JARGYLE_TEST_HELP_SECURITY_KEY_STORE_PASSWORD_FILE_1.getContentAsString().toCharArray(),
                            null),
                    TrustManagerHelper.getTrustManagers(
                            KeyStoreResourceConstants.JARGYLE_TEST_HELP_SECURITY_KEY_STORE_FILE_2.getInputStream(),
                            KeyStoreResourceConstants.JARGYLE_TEST_HELP_SECURITY_KEY_STORE_PASSWORD_FILE_2.getContentAsString().toCharArray(),
                            null)));
            Assert.assertNotNull(defaultSslSocketFactory.newSocket(
                    socket, "localhost", testServerPort, true));
        }
    }

}