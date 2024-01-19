package com.github.jh3nd3rs0n.jargyle.common.security;

import org.junit.Assert;
import org.junit.Test;

public class EncryptedPasswordSpecConstantsTest {

    @Test
    public void testAesCfbPkcs5PaddingEncryptedPasswordNewEncryptedPasswordCharArray() {
        Assert.assertNotNull(
                EncryptedPasswordSpecConstants.AES_CFB_PKCS5PADDING_ENCRYPTED_PASSWORD.newEncryptedPassword(
                        "Hello, World".toCharArray()));
    }

    @Test
    public void testAesCfbPkcs5PaddingEncryptedPasswordNewEncryptedPasswordString() {
        Assert.assertNotNull(
                EncryptedPasswordSpecConstants.AES_CFB_PKCS5PADDING_ENCRYPTED_PASSWORD.newEncryptedPassword(
                        "7MSMLoYMuv3IrBsdu5YVi+iFMqVTVcGxl1CLp4aGfg4=," +
                                "D/Gq+9TP1pLYbNhOwBpdJA==," +
                                "ceVrk9XvghhylTYoJME5rA=="));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAesCfbPkcs5PaddingEncryptedPasswordNewEncryptedPasswordStringForIllegalArgumentException() {
        Assert.assertNotNull(
                EncryptedPasswordSpecConstants.AES_CFB_PKCS5PADDING_ENCRYPTED_PASSWORD.newEncryptedPassword(
                        "????????????????????????"));
    }

    @Test
    public void testValueOfTypeNameString() {
        Assert.assertNotNull(EncryptedPasswordSpecConstants.valueOfTypeName(
                "AesCfbPkcs5PaddingEncryptedPassword"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testValueOfTypeNameStringForIllegalArgumentException() {
        EncryptedPasswordSpecConstants.valueOfTypeName("BogusEncryptedPassword");
    }

}