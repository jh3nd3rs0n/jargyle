package com.github.jh3nd3rs0n.jargyle.internal.net.ssl;

import com.github.jh3nd3rs0n.jargyle.test.help.security.KeyStoreResourceConstants;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;

public class SslSocketFactoryTest {

    @Test
    public void testNewInstanceSSLContext01() throws NoSuchAlgorithmException, KeyManagementException {
        Assert.assertNotNull(SslSocketFactory.newInstance(
                SslContextHelper.getSslContext("TLSv1.2", null, null)));
    }

    @Test
    public void testNewInstanceSSLContext02() throws NoSuchAlgorithmException, KeyManagementException, IOException {
        Assert.assertNotNull(SslSocketFactory.newInstance(
                SslContextHelper.getSslContext(
                        "TLSv1.2",
                        KeyManagerHelper.getKeyManagers(
                                new ByteArrayInputStream(KeyStoreResourceConstants.JARGYLE_TEST_HELP_SECURITY_KEY_STORE_FILE_1.getContentAsBytes()),
                                KeyStoreResourceConstants.JARGYLE_TEST_HELP_SECURITY_KEY_STORE_PASSWORD_FILE_1.getContentAsString().toCharArray(),
                                null),
                        null)));
    }

    @Test
    public void testNewInstanceSSLContext03() throws NoSuchAlgorithmException, KeyManagementException, IOException {
        Assert.assertNotNull(SslSocketFactory.newInstance(
                SslContextHelper.getSslContext(
                        "TLSv1.2",
                        KeyManagerHelper.getKeyManagers(
                                new ByteArrayInputStream(KeyStoreResourceConstants.JARGYLE_TEST_HELP_SECURITY_KEY_STORE_FILE_1.getContentAsBytes()),
                                KeyStoreResourceConstants.JARGYLE_TEST_HELP_SECURITY_KEY_STORE_PASSWORD_FILE_1.getContentAsString().toCharArray(),
                                null),
                        TrustManagerHelper.getTrustManagers(
                                new ByteArrayInputStream(KeyStoreResourceConstants.JARGYLE_TEST_HELP_SECURITY_KEY_STORE_FILE_3.getContentAsBytes()),
                                KeyStoreResourceConstants.JARGYLE_TEST_HELP_SECURITY_KEY_STORE_PASSWORD_FILE_3.getContentAsString().toCharArray(),
                                null))));
    }

    @Test
    public void testNewInstanceSSLContext04() throws NoSuchAlgorithmException, KeyManagementException, IOException {
        Assert.assertNotNull(SslSocketFactory.newInstance(
                SslContextHelper.getSslContext(
                        "TLSv1.2",
                        null,
                        TrustManagerHelper.getTrustManagers(
                                new ByteArrayInputStream(KeyStoreResourceConstants.JARGYLE_TEST_HELP_SECURITY_KEY_STORE_FILE_3.getContentAsBytes()),
                                KeyStoreResourceConstants.JARGYLE_TEST_HELP_SECURITY_KEY_STORE_PASSWORD_FILE_3.getContentAsString().toCharArray(),
                                null))));
    }

}