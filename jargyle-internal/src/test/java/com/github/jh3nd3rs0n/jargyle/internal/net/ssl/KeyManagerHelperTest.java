package com.github.jh3nd3rs0n.jargyle.internal.net.ssl;

import com.github.jh3nd3rs0n.test.help.security.KeyStoreResourceConstants;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;

public class KeyManagerHelperTest {

    @Test
    public void testGetKeyManagersInputStreamCharArrayString01() throws IOException {
        Assert.assertNotNull(KeyManagerHelper.getKeyManagers(
                KeyStoreResourceConstants.JARGYLE_TEST_HELP_SECURITY_KEY_STORE_FILE_1.getInputStream(),
                KeyStoreResourceConstants.JARGYLE_TEST_HELP_SECURITY_KEY_STORE_PASSWORD_FILE_1.getContentAsString().toCharArray(),
                null));
    }

    @Test
    public void testGetKeyManagersInputStreamCharArrayString02() throws IOException {
        Assert.assertNotNull(KeyManagerHelper.getKeyManagers(
                KeyStoreResourceConstants.JARGYLE_TEST_HELP_SECURITY_KEY_STORE_FILE_2.getInputStream(),
                KeyStoreResourceConstants.JARGYLE_TEST_HELP_SECURITY_KEY_STORE_PASSWORD_FILE_2.getContentAsString().toCharArray(),
                null));
    }

    @Test
    public void testGetKeyManagersInputStreamCharArrayString03() throws IOException {
        Assert.assertNotNull(KeyManagerHelper.getKeyManagers(
                KeyStoreResourceConstants.JARGYLE_TEST_HELP_SECURITY_KEY_STORE_FILE_3.getInputStream(),
                KeyStoreResourceConstants.JARGYLE_TEST_HELP_SECURITY_KEY_STORE_PASSWORD_FILE_3.getContentAsString().toCharArray(),
                null));
    }

    @Test
    public void testGetKeyManagersInputStreamCharArrayString04() throws IOException {
        Assert.assertNotNull(KeyManagerHelper.getKeyManagers(
                KeyStoreResourceConstants.JARGYLE_TEST_HELP_SECURITY_KEY_STORE_FILE_4.getInputStream(),
                KeyStoreResourceConstants.JARGYLE_TEST_HELP_SECURITY_KEY_STORE_PASSWORD_FILE_4.getContentAsString().toCharArray(),
                null));
    }

    @Test
    public void testGetKeyManagersInputStreamCharArrayString05() throws IOException {
        Assert.assertNotNull(KeyManagerHelper.getKeyManagers(
                KeyStoreResourceConstants.JARGYLE_TEST_HELP_SECURITY_KEY_STORE_FILE_5.getInputStream(),
                KeyStoreResourceConstants.JARGYLE_TEST_HELP_SECURITY_KEY_STORE_PASSWORD_FILE_5.getContentAsString().toCharArray(),
                null));
    }

    @Test
    public void testGetKeyManagersInputStreamCharArrayString06() throws IOException {
        Assert.assertNotNull(KeyManagerHelper.getKeyManagers(
                KeyStoreResourceConstants.JARGYLE_TEST_HELP_SECURITY_KEY_STORE_FILE_6.getInputStream(),
                KeyStoreResourceConstants.JARGYLE_TEST_HELP_SECURITY_KEY_STORE_PASSWORD_FILE_6.getContentAsString().toCharArray(),
                null));
    }

    @Test(expected = IOException.class)
    public void testGetKeyManagersInputStreamCharArrayStringForIOException01() throws IOException {
        KeyManagerHelper.getKeyManagers(
                KeyStoreResourceConstants.JARGYLE_TEST_HELP_SECURITY_KEY_STORE_FILE_2.getInputStream(),
                KeyStoreResourceConstants.JARGYLE_TEST_HELP_SECURITY_KEY_STORE_PASSWORD_FILE_1.getContentAsString().toCharArray(),
                null);
    }

    @Test
    public void testGetKeyManagersInputStreamCharArrayStringWithKeyStoreType01() throws IOException {
        Assert.assertNotNull(KeyManagerHelper.getKeyManagers(
                KeyStoreResourceConstants.JARGYLE_TEST_HELP_SECURITY_KEY_STORE_FILE_1.getInputStream(),
                KeyStoreResourceConstants.JARGYLE_TEST_HELP_SECURITY_KEY_STORE_PASSWORD_FILE_1.getContentAsString().toCharArray(),
                "PKCS12"));
    }

    @Test
    public void testGetKeyManagersInputStreamCharArrayStringWithKeyStoreType02() throws IOException {
        Assert.assertNotNull(KeyManagerHelper.getKeyManagers(
                KeyStoreResourceConstants.JARGYLE_TEST_HELP_SECURITY_KEY_STORE_FILE_2.getInputStream(),
                KeyStoreResourceConstants.JARGYLE_TEST_HELP_SECURITY_KEY_STORE_PASSWORD_FILE_2.getContentAsString().toCharArray(),
                "PKCS12"));
    }

    @Test
    public void testGetKeyManagersInputStreamCharArrayStringWithKeyStoreType03() throws IOException {
        Assert.assertNotNull(KeyManagerHelper.getKeyManagers(
                KeyStoreResourceConstants.JARGYLE_TEST_HELP_SECURITY_KEY_STORE_FILE_3.getInputStream(),
                KeyStoreResourceConstants.JARGYLE_TEST_HELP_SECURITY_KEY_STORE_PASSWORD_FILE_3.getContentAsString().toCharArray(),
                "PKCS12"));
    }

    @Test
    public void testGetKeyManagersInputStreamCharArrayStringWithKeyStoreType04() throws IOException {
        Assert.assertNotNull(KeyManagerHelper.getKeyManagers(
                KeyStoreResourceConstants.JARGYLE_TEST_HELP_SECURITY_KEY_STORE_FILE_4.getInputStream(),
                KeyStoreResourceConstants.JARGYLE_TEST_HELP_SECURITY_KEY_STORE_PASSWORD_FILE_4.getContentAsString().toCharArray(),
                "PKCS12"));
    }

    @Test
    public void testGetKeyManagersInputStreamCharArrayStringWithKeyStoreType05() throws IOException {
        Assert.assertNotNull(KeyManagerHelper.getKeyManagers(
                KeyStoreResourceConstants.JARGYLE_TEST_HELP_SECURITY_KEY_STORE_FILE_5.getInputStream(),
                KeyStoreResourceConstants.JARGYLE_TEST_HELP_SECURITY_KEY_STORE_PASSWORD_FILE_5.getContentAsString().toCharArray(),
                "PKCS12"));
    }

    @Test
    public void testGetKeyManagersInputStreamCharArrayStringWithKeyStoreType06() throws IOException {
        Assert.assertNotNull(KeyManagerHelper.getKeyManagers(
                KeyStoreResourceConstants.JARGYLE_TEST_HELP_SECURITY_KEY_STORE_FILE_6.getInputStream(),
                KeyStoreResourceConstants.JARGYLE_TEST_HELP_SECURITY_KEY_STORE_PASSWORD_FILE_6.getContentAsString().toCharArray(),
                "PKCS12"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetKeyManagersInputStreamCharArrayStringWithKeyStoreTypeForIllegalArgumentException01() throws IOException {
        KeyManagerHelper.getKeyManagers(
                KeyStoreResourceConstants.JARGYLE_TEST_HELP_SECURITY_KEY_STORE_FILE_1.getInputStream(),
                KeyStoreResourceConstants.JARGYLE_TEST_HELP_SECURITY_KEY_STORE_PASSWORD_FILE_1.getContentAsString().toCharArray(),
                "");
    }

}