package com.github.jh3nd3rs0n.jargyle.internal.security;

import com.github.jh3nd3rs0n.jargyle.test.help.security.KeyStoreResourceConstants;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;

public class KeyStoreHelperTest {

    @Test
    public void testGetKeyStoreInputStreamCharArrayString01() throws IOException {
        Assert.assertNotNull(KeyStoreHelper.getKeyStore(
                KeyStoreResourceConstants.JARGYLE_TEST_HELP_SECURITY_KEY_STORE_FILE_1.getInputStream(),
                KeyStoreResourceConstants.JARGYLE_TEST_HELP_SECURITY_KEY_STORE_PASSWORD_FILE_1.getContentAsString().toCharArray(),
                null));
    }

    @Test
    public void testGetKeyStoreInputStreamCharArrayString02() throws IOException {
        Assert.assertNotNull(KeyStoreHelper.getKeyStore(
                KeyStoreResourceConstants.JARGYLE_TEST_HELP_SECURITY_KEY_STORE_FILE_2.getInputStream(),
                KeyStoreResourceConstants.JARGYLE_TEST_HELP_SECURITY_KEY_STORE_PASSWORD_FILE_2.getContentAsString().toCharArray(),
                null));
    }

    @Test
    public void testGetKeyStoreInputStreamCharArrayString03() throws IOException {
        Assert.assertNotNull(KeyStoreHelper.getKeyStore(
                KeyStoreResourceConstants.JARGYLE_TEST_HELP_SECURITY_KEY_STORE_FILE_3.getInputStream(),
                KeyStoreResourceConstants.JARGYLE_TEST_HELP_SECURITY_KEY_STORE_PASSWORD_FILE_3.getContentAsString().toCharArray(),
                null));
    }

    @Test
    public void testGetKeyStoreInputStreamCharArrayString04() throws IOException {
        Assert.assertNotNull(KeyStoreHelper.getKeyStore(
                KeyStoreResourceConstants.JARGYLE_TEST_HELP_SECURITY_KEY_STORE_FILE_4.getInputStream(),
                KeyStoreResourceConstants.JARGYLE_TEST_HELP_SECURITY_KEY_STORE_PASSWORD_FILE_4.getContentAsString().toCharArray(),
                null));
    }

    @Test
    public void testGetKeyStoreInputStreamCharArrayString05() throws IOException {
        Assert.assertNotNull(KeyStoreHelper.getKeyStore(
                KeyStoreResourceConstants.JARGYLE_TEST_HELP_SECURITY_KEY_STORE_FILE_5.getInputStream(),
                KeyStoreResourceConstants.JARGYLE_TEST_HELP_SECURITY_KEY_STORE_PASSWORD_FILE_5.getContentAsString().toCharArray(),
                null));
    }

    @Test
    public void testGetKeyStoreInputStreamCharArrayString06() throws IOException {
        Assert.assertNotNull(KeyStoreHelper.getKeyStore(
                KeyStoreResourceConstants.JARGYLE_TEST_HELP_SECURITY_KEY_STORE_FILE_6.getInputStream(),
                KeyStoreResourceConstants.JARGYLE_TEST_HELP_SECURITY_KEY_STORE_PASSWORD_FILE_6.getContentAsString().toCharArray(),
                null));
    }

    @Test(expected = IOException.class)
    public void testGetKeyStoreInputStreamCharArrayStringForIOException01() throws IOException {
        KeyStoreHelper.getKeyStore(
                KeyStoreResourceConstants.JARGYLE_TEST_HELP_SECURITY_KEY_STORE_FILE_1.getInputStream(),
                KeyStoreResourceConstants.JARGYLE_TEST_HELP_SECURITY_KEY_STORE_PASSWORD_FILE_2.getContentAsString().toCharArray(),
                null);
    }

    @Test
    public void testGetKeyStoreInputStreamCharArrayStringWithKeyStoreType01() throws IOException {
        Assert.assertNotNull(KeyStoreHelper.getKeyStore(
                KeyStoreResourceConstants.JARGYLE_TEST_HELP_SECURITY_KEY_STORE_FILE_1.getInputStream(),
                KeyStoreResourceConstants.JARGYLE_TEST_HELP_SECURITY_KEY_STORE_PASSWORD_FILE_1.getContentAsString().toCharArray(),
                "PKCS12"));
    }

    @Test
    public void testGetKeyStoreInputStreamCharArrayStringWithKeyStoreType02() throws IOException {
        Assert.assertNotNull(KeyStoreHelper.getKeyStore(
                KeyStoreResourceConstants.JARGYLE_TEST_HELP_SECURITY_KEY_STORE_FILE_2.getInputStream(),
                KeyStoreResourceConstants.JARGYLE_TEST_HELP_SECURITY_KEY_STORE_PASSWORD_FILE_2.getContentAsString().toCharArray(),
                "PKCS12"));
    }

    @Test
    public void testGetKeyStoreInputStreamCharArrayStringWithKeyStoreType03() throws IOException {
        Assert.assertNotNull(KeyStoreHelper.getKeyStore(
                KeyStoreResourceConstants.JARGYLE_TEST_HELP_SECURITY_KEY_STORE_FILE_3.getInputStream(),
                KeyStoreResourceConstants.JARGYLE_TEST_HELP_SECURITY_KEY_STORE_PASSWORD_FILE_3.getContentAsString().toCharArray(),
                "PKCS12"));
    }

    @Test
    public void testGetKeyStoreInputStreamCharArrayStringWithKeyStoreType04() throws IOException {
        Assert.assertNotNull(KeyStoreHelper.getKeyStore(
                KeyStoreResourceConstants.JARGYLE_TEST_HELP_SECURITY_KEY_STORE_FILE_4.getInputStream(),
                KeyStoreResourceConstants.JARGYLE_TEST_HELP_SECURITY_KEY_STORE_PASSWORD_FILE_4.getContentAsString().toCharArray(),
                "PKCS12"));
    }

    @Test
    public void testGetKeyStoreInputStreamCharArrayStringWithKeyStoreType05() throws IOException {
        Assert.assertNotNull(KeyStoreHelper.getKeyStore(
                KeyStoreResourceConstants.JARGYLE_TEST_HELP_SECURITY_KEY_STORE_FILE_5.getInputStream(),
                KeyStoreResourceConstants.JARGYLE_TEST_HELP_SECURITY_KEY_STORE_PASSWORD_FILE_5.getContentAsString().toCharArray(),
                "PKCS12"));
    }

    @Test
    public void testGetKeyStoreInputStreamCharArrayStringWithKeyStoreType06() throws IOException {
        Assert.assertNotNull(KeyStoreHelper.getKeyStore(
                KeyStoreResourceConstants.JARGYLE_TEST_HELP_SECURITY_KEY_STORE_FILE_6.getInputStream(),
                KeyStoreResourceConstants.JARGYLE_TEST_HELP_SECURITY_KEY_STORE_PASSWORD_FILE_6.getContentAsString().toCharArray(),
                "PKCS12"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetKeyStoreInputStreamCharArrayStringWithKeyStoreTypeForIllegalArgumentException01() throws IOException {
        KeyStoreHelper.getKeyStore(
                KeyStoreResourceConstants.JARGYLE_TEST_HELP_SECURITY_KEY_STORE_FILE_1.getInputStream(),
                KeyStoreResourceConstants.JARGYLE_TEST_HELP_SECURITY_KEY_STORE_PASSWORD_FILE_1.getContentAsString().toCharArray(),
                "");
    }

}