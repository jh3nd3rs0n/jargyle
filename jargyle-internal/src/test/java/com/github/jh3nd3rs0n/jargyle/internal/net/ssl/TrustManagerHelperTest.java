package com.github.jh3nd3rs0n.jargyle.internal.net.ssl;

import com.github.jh3nd3rs0n.jargyle.test.help.security.KeyStoreResourceConstants;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;

public class TrustManagerHelperTest {

    @Test
    public void testGetTrustManagersInputStreamCharArrayString01() throws IOException {
        Assert.assertNotNull(TrustManagerHelper.getTrustManagers(
                new ByteArrayInputStream(KeyStoreResourceConstants.JARGYLE_TEST_HELP_SECURITY_KEY_STORE_FILE_1.getContentAsBytes()),
                KeyStoreResourceConstants.JARGYLE_TEST_HELP_SECURITY_KEY_STORE_PASSWORD_FILE_1.getContentAsString().toCharArray(),
                null));
    }

    @Test
    public void testGetTrustManagersInputStreamCharArrayString02() throws IOException {
        Assert.assertNotNull(TrustManagerHelper.getTrustManagers(
                new ByteArrayInputStream(KeyStoreResourceConstants.JARGYLE_TEST_HELP_SECURITY_KEY_STORE_FILE_2.getContentAsBytes()),
                KeyStoreResourceConstants.JARGYLE_TEST_HELP_SECURITY_KEY_STORE_PASSWORD_FILE_2.getContentAsString().toCharArray(),
                null));
    }

    @Test
    public void testGetTrustManagersInputStreamCharArrayString03() throws IOException {
        Assert.assertNotNull(TrustManagerHelper.getTrustManagers(
                new ByteArrayInputStream(KeyStoreResourceConstants.JARGYLE_TEST_HELP_SECURITY_KEY_STORE_FILE_3.getContentAsBytes()),
                KeyStoreResourceConstants.JARGYLE_TEST_HELP_SECURITY_KEY_STORE_PASSWORD_FILE_3.getContentAsString().toCharArray(),
                null));
    }

    @Test
    public void testGetTrustManagersInputStreamCharArrayString04() throws IOException {
        Assert.assertNotNull(TrustManagerHelper.getTrustManagers(
                new ByteArrayInputStream(KeyStoreResourceConstants.JARGYLE_TEST_HELP_SECURITY_KEY_STORE_FILE_4.getContentAsBytes()),
                KeyStoreResourceConstants.JARGYLE_TEST_HELP_SECURITY_KEY_STORE_PASSWORD_FILE_4.getContentAsString().toCharArray(),
                null));
    }

    @Test
    public void testGetTrustManagersInputStreamCharArrayString05() throws IOException {
        Assert.assertNotNull(TrustManagerHelper.getTrustManagers(
                new ByteArrayInputStream(KeyStoreResourceConstants.JARGYLE_TEST_HELP_SECURITY_KEY_STORE_FILE_5.getContentAsBytes()),
                KeyStoreResourceConstants.JARGYLE_TEST_HELP_SECURITY_KEY_STORE_PASSWORD_FILE_5.getContentAsString().toCharArray(),
                null));
    }

    @Test
    public void testGetTrustManagersInputStreamCharArrayString06() throws IOException {
        Assert.assertNotNull(TrustManagerHelper.getTrustManagers(
                new ByteArrayInputStream(KeyStoreResourceConstants.JARGYLE_TEST_HELP_SECURITY_KEY_STORE_FILE_6.getContentAsBytes()),
                KeyStoreResourceConstants.JARGYLE_TEST_HELP_SECURITY_KEY_STORE_PASSWORD_FILE_6.getContentAsString().toCharArray(),
                null));
    }

    @Test(expected = IOException.class)
    public void testGetTrustManagersInputStreamCharArrayStringForIOException01() throws IOException {
        TrustManagerHelper.getTrustManagers(
                new ByteArrayInputStream(KeyStoreResourceConstants.JARGYLE_TEST_HELP_SECURITY_KEY_STORE_FILE_2.getContentAsBytes()),
                KeyStoreResourceConstants.JARGYLE_TEST_HELP_SECURITY_KEY_STORE_PASSWORD_FILE_1.getContentAsString().toCharArray(),
                null);
    }

    @Test
    public void testGetTrustManagersInputStreamCharArrayStringWithKeyStoreType01() throws IOException {
        Assert.assertNotNull(TrustManagerHelper.getTrustManagers(
                new ByteArrayInputStream(KeyStoreResourceConstants.JARGYLE_TEST_HELP_SECURITY_KEY_STORE_FILE_1.getContentAsBytes()),
                KeyStoreResourceConstants.JARGYLE_TEST_HELP_SECURITY_KEY_STORE_PASSWORD_FILE_1.getContentAsString().toCharArray(),
                "PKCS12"));
    }

    @Test
    public void testGetTrustManagersInputStreamCharArrayStringWithKeyStoreType02() throws IOException {
        Assert.assertNotNull(TrustManagerHelper.getTrustManagers(
                new ByteArrayInputStream(KeyStoreResourceConstants.JARGYLE_TEST_HELP_SECURITY_KEY_STORE_FILE_2.getContentAsBytes()),
                KeyStoreResourceConstants.JARGYLE_TEST_HELP_SECURITY_KEY_STORE_PASSWORD_FILE_2.getContentAsString().toCharArray(),
                "PKCS12"));
    }

    @Test
    public void testGetTrustManagersInputStreamCharArrayStringWithKeyStoreType03() throws IOException {
        Assert.assertNotNull(TrustManagerHelper.getTrustManagers(
                new ByteArrayInputStream(KeyStoreResourceConstants.JARGYLE_TEST_HELP_SECURITY_KEY_STORE_FILE_3.getContentAsBytes()),
                KeyStoreResourceConstants.JARGYLE_TEST_HELP_SECURITY_KEY_STORE_PASSWORD_FILE_3.getContentAsString().toCharArray(),
                "PKCS12"));
    }

    @Test
    public void testGetTrustManagersInputStreamCharArrayStringWithKeyStoreType04() throws IOException {
        Assert.assertNotNull(TrustManagerHelper.getTrustManagers(
                new ByteArrayInputStream(KeyStoreResourceConstants.JARGYLE_TEST_HELP_SECURITY_KEY_STORE_FILE_4.getContentAsBytes()),
                KeyStoreResourceConstants.JARGYLE_TEST_HELP_SECURITY_KEY_STORE_PASSWORD_FILE_4.getContentAsString().toCharArray(),
                "PKCS12"));
    }

    @Test
    public void testGetTrustManagersInputStreamCharArrayStringWithKeyStoreType05() throws IOException {
        Assert.assertNotNull(TrustManagerHelper.getTrustManagers(
                new ByteArrayInputStream(KeyStoreResourceConstants.JARGYLE_TEST_HELP_SECURITY_KEY_STORE_FILE_5.getContentAsBytes()),
                KeyStoreResourceConstants.JARGYLE_TEST_HELP_SECURITY_KEY_STORE_PASSWORD_FILE_5.getContentAsString().toCharArray(),
                "PKCS12"));
    }

    @Test
    public void testGetTrustManagersInputStreamCharArrayStringWithKeyStoreType06() throws IOException {
        Assert.assertNotNull(TrustManagerHelper.getTrustManagers(
                new ByteArrayInputStream(KeyStoreResourceConstants.JARGYLE_TEST_HELP_SECURITY_KEY_STORE_FILE_6.getContentAsBytes()),
                KeyStoreResourceConstants.JARGYLE_TEST_HELP_SECURITY_KEY_STORE_PASSWORD_FILE_6.getContentAsString().toCharArray(),
                "PKCS12"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetTrustManagersInputStreamCharArrayStringWithKeyStoreTypeForIllegalArgumentException01() throws IOException {
        TrustManagerHelper.getTrustManagers(
                new ByteArrayInputStream(KeyStoreResourceConstants.JARGYLE_TEST_HELP_SECURITY_KEY_STORE_FILE_1.getContentAsBytes()),
                KeyStoreResourceConstants.JARGYLE_TEST_HELP_SECURITY_KEY_STORE_PASSWORD_FILE_1.getContentAsString().toCharArray(),
                "");
    }

}