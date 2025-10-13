package com.github.jh3nd3rs0n.jargyle.client.internal.client;

import com.github.jh3nd3rs0n.jargyle.client.Properties;
import com.github.jh3nd3rs0n.jargyle.client.SslPropertySpecConstants;
import com.github.jh3nd3rs0n.jargyle.common.bytes.Bytes;
import com.github.jh3nd3rs0n.jargyle.common.security.EncryptedPassword;
import com.github.jh3nd3rs0n.jargyle.common.string.CommaSeparatedValues;
import com.github.jh3nd3rs0n.jargyle.test.help.net.Server;
import com.github.jh3nd3rs0n.jargyle.test.help.security.KeyStoreResourceConstants;
import com.github.jh3nd3rs0n.jargyle.test.help.thread.ThreadHelper;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import javax.net.ssl.SSLSocket;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;

public class ConfiguredSslSocketFactoryIT {

    private static Path baseDir = null;
    private static byte[] keyStoreBytes1 = null;
    private static Path keyStoreFile1 = null;
    private static char[] keyStorePassword1 = null;
    private static byte[] keyStoreBytes2 = null;
    private static Path keyStoreFile2 = null;
    private static char[] keyStorePassword2 = null;
    private static Server server;
    private static int serverPort;

    @BeforeClass
    public static void setUpBeforeClass() throws IOException {
        baseDir = Files.createTempDirectory("com.github.jh3nd3rs0n.jargyle-");
        keyStoreBytes1 =
                KeyStoreResourceConstants.JARGYLE_TEST_HELP_SECURITY_KEY_STORE_FILE_1.getContentAsBytes();
        keyStoreFile1 = baseDir.resolve("keystore1.jks");
        try (InputStream in = new ByteArrayInputStream(keyStoreBytes1);
             OutputStream out = Files.newOutputStream(keyStoreFile1)) {
            int b;
            while ((b = in.read()) != -1) {
                out.write(b);
            }
            out.flush();
        }
        keyStorePassword1 = KeyStoreResourceConstants.JARGYLE_TEST_HELP_SECURITY_KEY_STORE_PASSWORD_FILE_1
                .getContentAsString().toCharArray();
        keyStoreBytes2 =
                KeyStoreResourceConstants.JARGYLE_TEST_HELP_SECURITY_KEY_STORE_FILE_2.getContentAsBytes();
        keyStoreFile2 = baseDir.resolve("keystore2.jks");
        try (InputStream in = new ByteArrayInputStream(keyStoreBytes2);
             OutputStream out = Files.newOutputStream(keyStoreFile2)) {
            int b;
            while ((b = in.read()) != -1) {
                out.write(b);
            }
            out.flush();
        }
        keyStorePassword2 = KeyStoreResourceConstants.JARGYLE_TEST_HELP_SECURITY_KEY_STORE_PASSWORD_FILE_2
                .getContentAsString().toCharArray();
        server = new Server(
                new Server.DefaultServerSocketFactory(),
                0,
                new Server.DefaultWorkerFactory());
        server.start();
        serverPort = server.getPort();
    }

    @AfterClass
    public static void tearDownAfterClass() throws IOException {
        if (keyStorePassword2 != null) {
            Arrays.fill(keyStorePassword2, '\0');
        }
        if (keyStoreFile2 != null) {
            Files.deleteIfExists(keyStoreFile2);
            keyStoreFile2 = null;
        }
        if (keyStorePassword1 != null) {
            Arrays.fill(keyStorePassword1, '\0');
        }
        if (keyStoreFile1 != null) {
            Files.deleteIfExists(keyStoreFile1);
            keyStoreFile1 = null;
        }
        if (baseDir != null) {
            Files.deleteIfExists(baseDir);
            baseDir = null;
        }
        if (server != null
                && !server.getState().equals(Server.State.STOPPED)) {
            server.stop();
        }
        ThreadHelper.interruptibleSleepForThreeSeconds();
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testGetSocketSocketInputStreamBooleanForUnsupportedOperationException() throws IOException {
        ConfiguredSslSocketFactory.newInstance(Properties.of()).getSocket(
                new Socket(), null, false);
    }

    @Test
    public void testGetSocketSocketStringIntBoolean01() throws IOException {
        Properties properties = Properties.of();
        ConfiguredSslSocketFactory sslSocketFactory = ConfiguredSslSocketFactory.newInstance(
                properties);
        try (Socket socket1 = new Socket("localhost", serverPort);
             Socket socket2 = sslSocketFactory.getSocket(
                     socket1, "localhost", serverPort, false)) {
            Assert.assertEquals(Socket.class, socket2.getClass());
        }
    }

    @Test
    public void testGetSocketSocketStringIntBoolean02() throws IOException {
        Properties properties = Properties.of(
                SslPropertySpecConstants.SSL_ENABLED.newProperty(true));
        ConfiguredSslSocketFactory sslSocketFactory = ConfiguredSslSocketFactory.newInstance(
                properties);
        try (Socket socket1 = new Socket("localhost", serverPort);
             Socket socket2 = sslSocketFactory.getSocket(
                     socket1, "localhost", serverPort, false)) {
            Assert.assertTrue(socket2 instanceof SSLSocket);
        }
    }

    @Test
    public void testGetSocketSocketStringIntBoolean03() throws IOException {
        Properties properties = Properties.of(
                SslPropertySpecConstants.SSL_ENABLED.newProperty(true),
                SslPropertySpecConstants.SSL_TRUST_STORE_BYTES.newProperty(
                        Bytes.of(keyStoreBytes1)),
                SslPropertySpecConstants.SSL_TRUST_STORE_PASSWORD.newProperty(
                        EncryptedPassword.newInstance(keyStorePassword1)));
        ConfiguredSslSocketFactory sslSocketFactory = ConfiguredSslSocketFactory.newInstance(
                properties);
        try (Socket socket1 = new Socket("localhost", serverPort);
             Socket socket2 = sslSocketFactory.getSocket(
                     socket1, "localhost", serverPort, false)) {
            Assert.assertTrue(socket2 instanceof SSLSocket);
        }
    }

    @Test
    public void testGetSocketSocketStringIntBoolean04() throws IOException {
        Properties properties = Properties.of(
                SslPropertySpecConstants.SSL_ENABLED.newProperty(true),
                SslPropertySpecConstants.SSL_TRUST_STORE_FILE.newProperty(
                        keyStoreFile1.toFile()),
                SslPropertySpecConstants.SSL_TRUST_STORE_PASSWORD.newProperty(
                        EncryptedPassword.newInstance(keyStorePassword1)));
        ConfiguredSslSocketFactory sslSocketFactory = ConfiguredSslSocketFactory.newInstance(
                properties);
        try (Socket socket1 = new Socket("localhost", serverPort);
             Socket socket2 = sslSocketFactory.getSocket(
                     socket1, "localhost", serverPort, false)) {
            Assert.assertTrue(socket2 instanceof SSLSocket);
        }
    }

    @Test
    public void testGetSocketSocketStringIntBoolean05() throws IOException {
        Properties properties = Properties.of(
                SslPropertySpecConstants.SSL_ENABLED.newProperty(true),
                SslPropertySpecConstants.SSL_KEY_STORE_BYTES.newProperty(
                        Bytes.of(keyStoreBytes2)),
                SslPropertySpecConstants.SSL_KEY_STORE_PASSWORD.newProperty(
                        EncryptedPassword.newInstance(keyStorePassword2)));
        ConfiguredSslSocketFactory sslSocketFactory = ConfiguredSslSocketFactory.newInstance(
                properties);
        try (Socket socket1 = new Socket("localhost", serverPort);
             Socket socket2 = sslSocketFactory.getSocket(
                     socket1, "localhost", serverPort, false)) {
            Assert.assertTrue(socket2 instanceof SSLSocket);
        }
    }

    @Test
    public void testGetSocketSocketStringIntBoolean06() throws IOException {
        Properties properties = Properties.of(
                SslPropertySpecConstants.SSL_ENABLED.newProperty(true),
                SslPropertySpecConstants.SSL_KEY_STORE_FILE.newProperty(
                        keyStoreFile2.toFile()),
                SslPropertySpecConstants.SSL_KEY_STORE_PASSWORD.newProperty(
                        EncryptedPassword.newInstance(keyStorePassword2)));
        ConfiguredSslSocketFactory sslSocketFactory = ConfiguredSslSocketFactory.newInstance(
                properties);
        try (Socket socket1 = new Socket("localhost", serverPort);
             Socket socket2 = sslSocketFactory.getSocket(
                     socket1, "localhost", serverPort, false)) {
            Assert.assertTrue(socket2 instanceof SSLSocket);
        }
    }

    @Test
    public void testGetSocketSocketStringIntBoolean07() throws IOException {
        CommaSeparatedValues commaSeparatedValues = CommaSeparatedValues.of(
                "TLS_ECDHE_RSA_WITH_NULL_SHA");
        Properties properties = Properties.of(
                SslPropertySpecConstants.SSL_ENABLED.newProperty(true),
                SslPropertySpecConstants.SSL_ENABLED_CIPHER_SUITES.newProperty(
                        commaSeparatedValues));
        ConfiguredSslSocketFactory sslSocketFactory = ConfiguredSslSocketFactory.newInstance(
                properties);
        try (Socket socket1 = new Socket("localhost", serverPort);
             SSLSocket socket2 = (SSLSocket) sslSocketFactory.getSocket(
                     socket1, "localhost", serverPort, false)) {
            Assert.assertArrayEquals(
                    commaSeparatedValues.toArray(),
                    socket2.getEnabledCipherSuites());
        }
    }

    @Test
    public void testGetSocketSocketStringIntBoolean08() throws IOException {
        CommaSeparatedValues commaSeparatedValues = CommaSeparatedValues.of(
                "TLSv1.2");
        Properties properties = Properties.of(
                SslPropertySpecConstants.SSL_ENABLED.newProperty(true),
                SslPropertySpecConstants.SSL_ENABLED_PROTOCOLS.newProperty(
                        commaSeparatedValues));
        ConfiguredSslSocketFactory sslSocketFactory = ConfiguredSslSocketFactory.newInstance(
                properties);
        try (Socket socket1 = new Socket("localhost", serverPort);
             SSLSocket socket2 = (SSLSocket) sslSocketFactory.getSocket(
                     socket1, "localhost", serverPort, false)) {
            Assert.assertArrayEquals(
                    commaSeparatedValues.toArray(),
                    socket2.getEnabledProtocols());
        }
    }

}