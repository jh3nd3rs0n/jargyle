package com.github.jh3nd3rs0n.jargyle.internal.net.ssl;

import com.github.jh3nd3rs0n.jargyle.internal.VirtualThreadPerTaskExecutorOrCachedThreadPoolFactory;
import com.github.jh3nd3rs0n.jargyle.test.help.net.TestServer;
import com.github.jh3nd3rs0n.jargyle.test.help.security.TestKeyStoreResourceConstants;
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

public class DefaultSslSocketFactoryTest {

    private static TestServer testServer;
    private static int testServerPort;

    @BeforeClass
    public static void setUpBeforeClass() throws IOException {
        testServer = new TestServer(
                new TestServer.DefaultServerSocketFactory(),
                0,
                new VirtualThreadPerTaskExecutorOrCachedThreadPoolFactory(),
                new TestServer.DefaultWorkerFactory());
        testServer.start();
        testServerPort = testServer.getPort();
    }

    @AfterClass
    public static void tearDownAfterClass() throws IOException {
        if (testServer != null) {
            if (!testServer.getState().equals(TestServer.State.STOPPED)) {
                testServer.stop();
            }
        }
        ThreadHelper.interruptibleSleepForThreeSeconds();
    }

    @Test
    public void testNewSocketSocketInputStreamBooleanWithSocketNullFalse01() throws IOException, NoSuchAlgorithmException, KeyManagementException {
        try (Socket socket = new Socket((String) null, testServerPort)) {
            DefaultSslSocketFactory defaultSslSocketFactory = new DefaultSslSocketFactory(SslContextHelper.getSslContext(
                    "TLSv1.2",
                    KeyManagerHelper.getKeyManagers(
                            TestKeyStoreResourceConstants.JARGYLE_TEST_HELP_SECURITY_KEY_STORE_FILE_1.getInputStream(),
                            TestKeyStoreResourceConstants.JARGYLE_TEST_HELP_SECURITY_KEY_STORE_PASSWORD_FILE_1.getContentAsString().toCharArray(),
                            null),
                    TrustManagerHelper.getTrustManagers(
                            TestKeyStoreResourceConstants.JARGYLE_TEST_HELP_SECURITY_KEY_STORE_FILE_2.getInputStream(),
                            TestKeyStoreResourceConstants.JARGYLE_TEST_HELP_SECURITY_KEY_STORE_PASSWORD_FILE_2.getContentAsString().toCharArray(),
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
                            TestKeyStoreResourceConstants.JARGYLE_TEST_HELP_SECURITY_KEY_STORE_FILE_1.getInputStream(),
                            TestKeyStoreResourceConstants.JARGYLE_TEST_HELP_SECURITY_KEY_STORE_PASSWORD_FILE_1.getContentAsString().toCharArray(),
                            null),
                    TrustManagerHelper.getTrustManagers(
                            TestKeyStoreResourceConstants.JARGYLE_TEST_HELP_SECURITY_KEY_STORE_FILE_2.getInputStream(),
                            TestKeyStoreResourceConstants.JARGYLE_TEST_HELP_SECURITY_KEY_STORE_PASSWORD_FILE_2.getContentAsString().toCharArray(),
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
                            TestKeyStoreResourceConstants.JARGYLE_TEST_HELP_SECURITY_KEY_STORE_FILE_1.getInputStream(),
                            TestKeyStoreResourceConstants.JARGYLE_TEST_HELP_SECURITY_KEY_STORE_PASSWORD_FILE_1.getContentAsString().toCharArray(),
                            null),
                    TrustManagerHelper.getTrustManagers(
                            TestKeyStoreResourceConstants.JARGYLE_TEST_HELP_SECURITY_KEY_STORE_FILE_2.getInputStream(),
                            TestKeyStoreResourceConstants.JARGYLE_TEST_HELP_SECURITY_KEY_STORE_PASSWORD_FILE_2.getContentAsString().toCharArray(),
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
                            TestKeyStoreResourceConstants.JARGYLE_TEST_HELP_SECURITY_KEY_STORE_FILE_1.getInputStream(),
                            TestKeyStoreResourceConstants.JARGYLE_TEST_HELP_SECURITY_KEY_STORE_PASSWORD_FILE_1.getContentAsString().toCharArray(),
                            null),
                    TrustManagerHelper.getTrustManagers(
                            TestKeyStoreResourceConstants.JARGYLE_TEST_HELP_SECURITY_KEY_STORE_FILE_2.getInputStream(),
                            TestKeyStoreResourceConstants.JARGYLE_TEST_HELP_SECURITY_KEY_STORE_PASSWORD_FILE_2.getContentAsString().toCharArray(),
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
                            TestKeyStoreResourceConstants.JARGYLE_TEST_HELP_SECURITY_KEY_STORE_FILE_1.getInputStream(),
                            TestKeyStoreResourceConstants.JARGYLE_TEST_HELP_SECURITY_KEY_STORE_PASSWORD_FILE_1.getContentAsString().toCharArray(),
                            null),
                    TrustManagerHelper.getTrustManagers(
                            TestKeyStoreResourceConstants.JARGYLE_TEST_HELP_SECURITY_KEY_STORE_FILE_2.getInputStream(),
                            TestKeyStoreResourceConstants.JARGYLE_TEST_HELP_SECURITY_KEY_STORE_PASSWORD_FILE_2.getContentAsString().toCharArray(),
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
                            TestKeyStoreResourceConstants.JARGYLE_TEST_HELP_SECURITY_KEY_STORE_FILE_1.getInputStream(),
                            TestKeyStoreResourceConstants.JARGYLE_TEST_HELP_SECURITY_KEY_STORE_PASSWORD_FILE_1.getContentAsString().toCharArray(),
                            null),
                    TrustManagerHelper.getTrustManagers(
                            TestKeyStoreResourceConstants.JARGYLE_TEST_HELP_SECURITY_KEY_STORE_FILE_2.getInputStream(),
                            TestKeyStoreResourceConstants.JARGYLE_TEST_HELP_SECURITY_KEY_STORE_PASSWORD_FILE_2.getContentAsString().toCharArray(),
                            null)));
            Assert.assertNotNull(defaultSslSocketFactory.newSocket(
                    socket, "localhost", testServerPort, true));
        }
    }

}