package com.github.jh3nd3rs0n.jargyle.internal.net.ssl;

import com.github.jh3nd3rs0n.test.help.security.KeyStoreResourceConstants;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.net.DatagramSocket;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;

public class DefaultDtlsDatagramSocketFactoryTest {

    @Test
    public void testNewDatagramSocketDatagramSocket01() throws IOException, NoSuchAlgorithmException, KeyManagementException {
        DefaultDtlsDatagramSocketFactory defaultDtlsDatagramSocketFactory = new DefaultDtlsDatagramSocketFactory(SslContextHelper.getSslContext(
                "DTLSv1.2",
                KeyManagerHelper.getKeyManagers(
                        KeyStoreResourceConstants.JARGYLE_TEST_HELP_SECURITY_KEY_STORE_FILE_2.getInputStream(),
                        KeyStoreResourceConstants.JARGYLE_TEST_HELP_SECURITY_KEY_STORE_PASSWORD_FILE_2.getContentAsString().toCharArray(),
                        null),
                TrustManagerHelper.getTrustManagers(
                        KeyStoreResourceConstants.JARGYLE_TEST_HELP_SECURITY_KEY_STORE_FILE_1.getInputStream(),
                        KeyStoreResourceConstants.JARGYLE_TEST_HELP_SECURITY_KEY_STORE_PASSWORD_FILE_1.getContentAsString().toCharArray(),
                        null)));
        Assert.assertNotNull(defaultDtlsDatagramSocketFactory.newDatagramSocket(
                new DatagramSocket(null)));
    }

}