package com.github.jh3nd3rs0n.jargyle.client;

import com.github.jh3nd3rs0n.jargyle.common.bytes.Bytes;
import com.github.jh3nd3rs0n.jargyle.common.number.PositiveInteger;
import com.github.jh3nd3rs0n.jargyle.common.security.EncryptedPassword;
import com.github.jh3nd3rs0n.jargyle.common.string.CommaSeparatedValues;
import com.github.jh3nd3rs0n.jargyle.internal.net.ssl.DtlsDatagramSocket;
import com.github.jh3nd3rs0n.jargyle.test.help.security.KeyStoreResourceConstants;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.*;
import java.net.DatagramSocket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;

public class DtlsDatagramSocketFactoryImplIT {

    private static Path baseDir = null;
    private static byte[] keyStoreBytes1 = null;
    private static Path keyStoreFile1 = null;
    private static char[] keyStorePassword1 = null;

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
    }

    @AfterClass
    public static void tearDownAfterClass() throws IOException {
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
    }

    @Test
    public void testGetDatagramSocketDatagramSocket01() throws IOException {
        Properties properties = Properties.of();
        DtlsDatagramSocketFactoryImpl dtlsDatagramSocketFactory =
                new DtlsDatagramSocketFactoryImpl(properties);
        try (DatagramSocket datagramSocket1 = new DatagramSocket(null);
             DatagramSocket datagramSocket2 =
                     dtlsDatagramSocketFactory.getDatagramSocket(datagramSocket1)) {
            Assert.assertEquals(
                    DatagramSocket.class,
                    datagramSocket2.getClass());
        }
    }

    @Test
    public void testGetDatagramSocketDatagramSocket02() throws IOException {
        Properties properties = Properties.of(
                DtlsPropertySpecConstants.DTLS_ENABLED.newProperty(true));
        DtlsDatagramSocketFactoryImpl dtlsDatagramSocketFactory =
                new DtlsDatagramSocketFactoryImpl(properties);
        try (DatagramSocket datagramSocket1 = new DatagramSocket(null);
             DatagramSocket datagramSocket2 =
                     dtlsDatagramSocketFactory.getDatagramSocket(datagramSocket1)) {
            Assert.assertEquals(
                    DtlsDatagramSocket.class,
                    datagramSocket2.getClass());
        }
    }

    @Test
    public void testGetDatagramSocketDatagramSocket03() throws IOException {
        Properties properties = Properties.of(
                DtlsPropertySpecConstants.DTLS_ENABLED.newProperty(true),
                DtlsPropertySpecConstants.DTLS_TRUST_STORE_BYTES.newProperty(
                        Bytes.of(keyStoreBytes1)),
                DtlsPropertySpecConstants.DTLS_TRUST_STORE_PASSWORD.newProperty(
                        EncryptedPassword.newInstance(keyStorePassword1)));
        DtlsDatagramSocketFactoryImpl dtlsDatagramSocketFactory =
                new DtlsDatagramSocketFactoryImpl(properties);
        try (DatagramSocket datagramSocket1 = new DatagramSocket(null);
             DatagramSocket datagramSocket2 =
                     dtlsDatagramSocketFactory.getDatagramSocket(datagramSocket1)) {
            Assert.assertEquals(
                    DtlsDatagramSocket.class,
                    datagramSocket2.getClass());
        }
    }

    @Test
    public void testGetDatagramSocketDatagramSocket04() throws IOException {
        Properties properties = Properties.of(
                DtlsPropertySpecConstants.DTLS_ENABLED.newProperty(true),
                DtlsPropertySpecConstants.DTLS_TRUST_STORE_FILE.newProperty(
                        keyStoreFile1.toFile()),
                DtlsPropertySpecConstants.DTLS_TRUST_STORE_PASSWORD.newProperty(
                        EncryptedPassword.newInstance(keyStorePassword1)));
        DtlsDatagramSocketFactoryImpl dtlsDatagramSocketFactory =
                new DtlsDatagramSocketFactoryImpl(properties);
        try (DatagramSocket datagramSocket1 = new DatagramSocket(null);
             DatagramSocket datagramSocket2 =
                     dtlsDatagramSocketFactory.getDatagramSocket(datagramSocket1)) {
            Assert.assertEquals(
                    DtlsDatagramSocket.class,
                    datagramSocket2.getClass());
        }
    }

    @Test
    public void testGetDatagramSocketDatagramSocket05() throws IOException {
        CommaSeparatedValues commaSeparatedValues = CommaSeparatedValues.of(
                "TLS_ECDHE_RSA_WITH_NULL_SHA");
        Properties properties = Properties.of(
                DtlsPropertySpecConstants.DTLS_ENABLED.newProperty(true),
                DtlsPropertySpecConstants.DTLS_ENABLED_CIPHER_SUITES.newProperty(
                        commaSeparatedValues));
        DtlsDatagramSocketFactoryImpl dtlsDatagramSocketFactory =
                new DtlsDatagramSocketFactoryImpl(properties);
        try (DatagramSocket datagramSocket1 = new DatagramSocket(null);
             DtlsDatagramSocket datagramSocket2 =
                     (DtlsDatagramSocket) dtlsDatagramSocketFactory.getDatagramSocket(
                             datagramSocket1)) {
            Assert.assertArrayEquals(
                    commaSeparatedValues.toArray(),
                    datagramSocket2.getEnabledCipherSuites());
        }
    }

    @Test
    public void testGetDatagramSocketDatagramSocket06() throws IOException {
        CommaSeparatedValues commaSeparatedValues = CommaSeparatedValues.of(
                "DTLSv1.2");
        Properties properties = Properties.of(
                DtlsPropertySpecConstants.DTLS_ENABLED.newProperty(true),
                DtlsPropertySpecConstants.DTLS_ENABLED_PROTOCOLS.newProperty(
                        commaSeparatedValues));
        DtlsDatagramSocketFactoryImpl dtlsDatagramSocketFactory =
                new DtlsDatagramSocketFactoryImpl(properties);
        try (DatagramSocket datagramSocket1 = new DatagramSocket(null);
             DtlsDatagramSocket datagramSocket2 =
                     (DtlsDatagramSocket) dtlsDatagramSocketFactory.getDatagramSocket(
                             datagramSocket1)) {
            Assert.assertArrayEquals(
                    commaSeparatedValues.toArray(),
                    datagramSocket2.getEnabledProtocols());
        }
    }

    @Test
    public void testGetDatagramSocketDatagramSocket07() throws IOException {
        PositiveInteger positiveInteger = PositiveInteger.valueOf(1024);
        Properties properties = Properties.of(
                DtlsPropertySpecConstants.DTLS_ENABLED.newProperty(true),
                DtlsPropertySpecConstants.DTLS_WRAPPED_RECEIVE_BUFFER_SIZE.newProperty(
                        positiveInteger));
        DtlsDatagramSocketFactoryImpl dtlsDatagramSocketFactory =
                new DtlsDatagramSocketFactoryImpl(properties);
        try (DatagramSocket datagramSocket1 = new DatagramSocket(null);
             DtlsDatagramSocket datagramSocket2 =
                     (DtlsDatagramSocket) dtlsDatagramSocketFactory.getDatagramSocket(
                             datagramSocket1)) {
            Assert.assertEquals(
                    positiveInteger.intValue(),
                    datagramSocket2.getWrappedReceiveBufferSize());
        }
    }

}