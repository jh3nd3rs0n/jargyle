package com.github.jh3nd3rs0n.jargyle.internal.net.ssl;

import com.github.jh3nd3rs0n.test.help.security.KeyStoreResourceConstants;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;

public class DtlsDatagramSocketFactoryTest {

    @Test
    public void testNewInstanceSSLContext01() throws NoSuchAlgorithmException, KeyManagementException {
        Assert.assertNotNull(DtlsDatagramSocketFactory.newInstance(
                SslContextHelper.getSslContext("DTLSv1.2", null, null)));
    }

    @Test
    public void testNewInstanceSSLContext02() throws NoSuchAlgorithmException, KeyManagementException, IOException {
        Assert.assertNotNull(DtlsDatagramSocketFactory.newInstance(
                SslContextHelper.getSslContext(
                        "DTLSv1.2",
                        KeyManagerHelper.getKeyManagers(
                                KeyStoreResourceConstants.JARGYLE_TEST_HELP_SECURITY_KEY_STORE_FILE_2.getInputStream(),
                                KeyStoreResourceConstants.JARGYLE_TEST_HELP_SECURITY_KEY_STORE_PASSWORD_FILE_2.getContentAsString().toCharArray(),
                                null),
                        null)));
    }

    @Test
    public void testNewInstanceSSLContext03() throws NoSuchAlgorithmException, KeyManagementException, IOException {
        Assert.assertNotNull(DtlsDatagramSocketFactory.newInstance(
                SslContextHelper.getSslContext(
                        "DTLSv1.2",
                        KeyManagerHelper.getKeyManagers(
                                KeyStoreResourceConstants.JARGYLE_TEST_HELP_SECURITY_KEY_STORE_FILE_2.getInputStream(),
                                KeyStoreResourceConstants.JARGYLE_TEST_HELP_SECURITY_KEY_STORE_PASSWORD_FILE_2.getContentAsString().toCharArray(),
                                null),
                        TrustManagerHelper.getTrustManagers(
                                KeyStoreResourceConstants.JARGYLE_TEST_HELP_SECURITY_KEY_STORE_FILE_4.getInputStream(),
                                KeyStoreResourceConstants.JARGYLE_TEST_HELP_SECURITY_KEY_STORE_PASSWORD_FILE_4.getContentAsString().toCharArray(),
                                null))));
    }

    @Test
    public void testNewInstanceSSLContext04() throws NoSuchAlgorithmException, KeyManagementException, IOException {
        Assert.assertNotNull(DtlsDatagramSocketFactory.newInstance(
                SslContextHelper.getSslContext(
                        "DTLSv1.2",
                        null,
                        TrustManagerHelper.getTrustManagers(
                                KeyStoreResourceConstants.JARGYLE_TEST_HELP_SECURITY_KEY_STORE_FILE_4.getInputStream(),
                                KeyStoreResourceConstants.JARGYLE_TEST_HELP_SECURITY_KEY_STORE_PASSWORD_FILE_4.getContentAsString().toCharArray(),
                                null))));
    }
    
}