package com.github.jh3nd3rs0n.jargyle.internal.net.ssl;

import com.github.jh3nd3rs0n.jargyle.test.help.security.TestKeyStoreResourceConstants;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;

public class SslContextHelperTest {

    @Test
    public void testGetSslContextStringKeyManagerArrayTrustManagerArray01() throws NoSuchAlgorithmException, KeyManagementException {
        Assert.assertNotNull(SslContextHelper.getSslContext(
                "TLSv1.2", null, null));
    }

    @Test
    public void testGetSslContextStringKeyManagerArrayTrustManagerArray02() throws NoSuchAlgorithmException, KeyManagementException {
        Assert.assertNotNull(SslContextHelper.getSslContext(
                "DTLSv1.2", null, null));
    }

    @Test(expected = NoSuchAlgorithmException.class)
    public void testGetSslContextStringKeyManagerArrayTrustManagerArrayForNoSuchAlgorithmException01() throws NoSuchAlgorithmException, KeyManagementException {
        SslContextHelper.getSslContext("", null, null);
    }

    @Test
    public void testGetSslContextStringKeyManagerArrayTrustManagerArrayWithKeyManagers01() throws NoSuchAlgorithmException, KeyManagementException, IOException {
        Assert.assertNotNull(SslContextHelper.getSslContext(
                "TLSv1.2",
                KeyManagerHelper.getKeyManagers(
                        TestKeyStoreResourceConstants.JARGYLE_TEST_HELP_SECURITY_KEY_STORE_FILE_1.getInputStream(),
                        TestKeyStoreResourceConstants.JARGYLE_TEST_HELP_SECURITY_KEY_STORE_PASSWORD_FILE_1.getContentAsString().toCharArray(),
                        null),
                null));
    }

    @Test
    public void testGetSslContextStringKeyManagerArrayTrustManagerArrayWithKeyManagers02() throws NoSuchAlgorithmException, KeyManagementException, IOException {
        Assert.assertNotNull(SslContextHelper.getSslContext(
                "DTLSv1.2",
                KeyManagerHelper.getKeyManagers(
                        TestKeyStoreResourceConstants.JARGYLE_TEST_HELP_SECURITY_KEY_STORE_FILE_2.getInputStream(),
                        TestKeyStoreResourceConstants.JARGYLE_TEST_HELP_SECURITY_KEY_STORE_PASSWORD_FILE_2.getContentAsString().toCharArray(),
                        null),
                null));
    }

    @Test
    public void testGetSslContextStringKeyManagerArrayTrustManagerArrayWithKeyManagersAndTrustManagers01() throws NoSuchAlgorithmException, KeyManagementException, IOException {
        Assert.assertNotNull(SslContextHelper.getSslContext(
                "TLSv1.2",
                KeyManagerHelper.getKeyManagers(
                        TestKeyStoreResourceConstants.JARGYLE_TEST_HELP_SECURITY_KEY_STORE_FILE_1.getInputStream(),
                        TestKeyStoreResourceConstants.JARGYLE_TEST_HELP_SECURITY_KEY_STORE_PASSWORD_FILE_1.getContentAsString().toCharArray(),
                        null),
                TrustManagerHelper.getTrustManagers(
                        TestKeyStoreResourceConstants.JARGYLE_TEST_HELP_SECURITY_KEY_STORE_FILE_2.getInputStream(),
                        TestKeyStoreResourceConstants.JARGYLE_TEST_HELP_SECURITY_KEY_STORE_PASSWORD_FILE_2.getContentAsString().toCharArray(),
                        null)));
    }

    @Test
    public void testGetSslContextStringKeyManagerArrayTrustManagerArrayWithKeyManagersAndTrustManagers02() throws NoSuchAlgorithmException, KeyManagementException, IOException {
        Assert.assertNotNull(SslContextHelper.getSslContext(
                "DTLSv1.2",
                KeyManagerHelper.getKeyManagers(
                        TestKeyStoreResourceConstants.JARGYLE_TEST_HELP_SECURITY_KEY_STORE_FILE_2.getInputStream(),
                        TestKeyStoreResourceConstants.JARGYLE_TEST_HELP_SECURITY_KEY_STORE_PASSWORD_FILE_2.getContentAsString().toCharArray(),
                        null),
                TrustManagerHelper.getTrustManagers(
                        TestKeyStoreResourceConstants.JARGYLE_TEST_HELP_SECURITY_KEY_STORE_FILE_1.getInputStream(),
                        TestKeyStoreResourceConstants.JARGYLE_TEST_HELP_SECURITY_KEY_STORE_PASSWORD_FILE_1.getContentAsString().toCharArray(),
                        null)));
    }

    @Test
    public void testGetSslContextStringKeyManagerArrayTrustManagerArrayWithTrustManagers01() throws NoSuchAlgorithmException, KeyManagementException, IOException {
        Assert.assertNotNull(SslContextHelper.getSslContext(
                "TLSv1.2",
                null,
                TrustManagerHelper.getTrustManagers(
                        TestKeyStoreResourceConstants.JARGYLE_TEST_HELP_SECURITY_KEY_STORE_FILE_2.getInputStream(),
                        TestKeyStoreResourceConstants.JARGYLE_TEST_HELP_SECURITY_KEY_STORE_PASSWORD_FILE_2.getContentAsString().toCharArray(),
                        null)));
    }

    @Test
    public void testGetSslContextStringKeyManagerArrayTrustManagerArrayWithTrustManagers02() throws NoSuchAlgorithmException, KeyManagementException, IOException {
        Assert.assertNotNull(SslContextHelper.getSslContext(
                "DTLSv1.2",
                null,
                TrustManagerHelper.getTrustManagers(
                        TestKeyStoreResourceConstants.JARGYLE_TEST_HELP_SECURITY_KEY_STORE_FILE_1.getInputStream(),
                        TestKeyStoreResourceConstants.JARGYLE_TEST_HELP_SECURITY_KEY_STORE_PASSWORD_FILE_1.getContentAsString().toCharArray(),
                        null)));
    }

}