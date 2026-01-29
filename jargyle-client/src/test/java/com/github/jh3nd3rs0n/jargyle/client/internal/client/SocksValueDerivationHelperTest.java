package com.github.jh3nd3rs0n.jargyle.client.internal.client;

import com.github.jh3nd3rs0n.jargyle.client.Properties;
import com.github.jh3nd3rs0n.jargyle.client.SocksPropertySpecConstants;
import com.github.jh3nd3rs0n.jargyle.common.security.EncryptedPassword;
import com.github.jh3nd3rs0n.jargyle.common.string.CommaSeparatedValues;
import org.ietf.jgss.GSSException;
import org.ietf.jgss.Oid;
import org.junit.Assert;
import org.junit.Test;

public class SocksValueDerivationHelperTest {

    @Test
    public void testGetSocksGssapiAuthMethodMechanismOidFromProperties01() throws GSSException {
        Properties properties = Properties.of();
        Assert.assertEquals(
                new Oid("1.2.840.113554.1.2.2"),
                SocksValueDerivationHelper.getSocksGssapiAuthMethodMechanismOidFrom(properties));
    }

    @Test
    public void testGetSocksGssapiAuthMethodMechanismOidFromProperties02() throws GSSException {
        Oid oid = new Oid("2.2.2.2.2.2.2");
        Properties properties = Properties.of(
                SocksPropertySpecConstants.SOCKS_GSSAPIAUTHMETHOD_MECHANISM_OID.newProperty(
                        oid));
        Assert.assertEquals(
                oid,
                SocksValueDerivationHelper.getSocksGssapiAuthMethodMechanismOidFrom(properties));
    }

    @Test
    public void testGetSocksGssapiAuthMethodNecReferenceImplFromProperties01() {
        Properties properties = Properties.of();
        Assert.assertFalse(SocksValueDerivationHelper.getSocksGssapiAuthMethodNecReferenceImplFrom(properties));
    }

    @Test
    public void testGetSocksGssapiAuthMethodNecReferenceImplFromProperties02() {
        Properties properties = Properties.of(
                SocksPropertySpecConstants.SOCKS_GSSAPIAUTHMETHOD_NEC_REFERENCE_IMPL.newProperty(
                        true));
        Assert.assertTrue(SocksValueDerivationHelper.getSocksGssapiAuthMethodNecReferenceImplFrom(properties));
    }

    @Test
    public void testGetSocksGssapiAuthMethodProtectionLevelsFromProperties01() {
        Properties properties = Properties.of();
        Assert.assertEquals(
                CommaSeparatedValues.of(
                        "REQUIRED_INTEG_AND_CONF",
                        "REQUIRED_INTEG",
                        "NONE"),
                SocksValueDerivationHelper.getSocksGssapiAuthMethodProtectionLevelsFrom(properties));
    }

    @Test
    public void testGetSocksGssapiAuthMethodProtectionLevelsFromProperties02() {
        CommaSeparatedValues commaSeparatedValues = CommaSeparatedValues.of(
                "SELECTIVE_INTEG_OR_CONF");
        Properties properties = Properties.of(
                SocksPropertySpecConstants.SOCKS_GSSAPIAUTHMETHOD_PROTECTION_LEVELS.newProperty(
                        commaSeparatedValues));
        Assert.assertEquals(
                commaSeparatedValues,
                SocksValueDerivationHelper.getSocksGssapiAuthMethodProtectionLevelsFrom(properties));
    }

    @Test
    public void testGetSocksGssapiAuthMethodServiceNameFromProperties01() {
        Properties properties = Properties.of();
        Assert.assertNull(SocksValueDerivationHelper.getSocksGssapiAuthMethodServiceNameFrom(properties));
    }

    @Test
    public void testGetSocksGssapiAuthMethodServiceNameFromProperties02() {
        String string = "rcmd/localhost";
        Properties properties = Properties.of(
                SocksPropertySpecConstants.SOCKS_GSSAPIAUTHMETHOD_SERVICE_NAME.newProperty(
                        string));
        Assert.assertEquals(
                string,
                SocksValueDerivationHelper.getSocksGssapiAuthMethodServiceNameFrom(properties));
    }

    @Test
    public void testGetSocksGssapiAuthMethodSuggestedConfFromProperties01() {
        Properties properties = Properties.of();
        Assert.assertTrue(SocksValueDerivationHelper.getSocksGssapiAuthMethodSuggestedConfFrom(properties));
    }

    @Test
    public void testGetSocksGssapiAuthMethodSuggestedConfFromProperties02() {
        Properties properties = Properties.of(
                SocksPropertySpecConstants.SOCKS_GSSAPIAUTHMETHOD_SUGGESTED_CONF.newProperty(
                        false));
        Assert.assertFalse(SocksValueDerivationHelper.getSocksGssapiAuthMethodSuggestedConfFrom(properties));
    }

    @Test
    public void testGetSocksGssapiAuthMethodSuggestedIntegFromProperties01() {
        Properties properties = Properties.of();
        Assert.assertEquals(
                0,
                SocksValueDerivationHelper.getSocksGssapiAuthMethodSuggestedIntegFrom(properties));
    }

    @Test
    public void testGetSocksGssapiAuthMethodSuggestedIntegFromProperties02() {
        int i = 1;
        Properties properties = Properties.of(
                SocksPropertySpecConstants.SOCKS_GSSAPIAUTHMETHOD_SUGGESTED_INTEG.newProperty(
                        i));
        Assert.assertEquals(
                i,
                SocksValueDerivationHelper.getSocksGssapiAuthMethodSuggestedIntegFrom(properties));
    }

    @Test
    public void testGetSocksMethodsFromProperties01() {
        Properties properties = Properties.of();
        Assert.assertEquals(
                CommaSeparatedValues.of("NO_AUTHENTICATION_REQUIRED"),
                SocksValueDerivationHelper.getSocksMethodsFrom(properties));
    }

    @Test
    public void testGetSocksMethodsFromProperties02() {
        CommaSeparatedValues commaSeparatedValues = CommaSeparatedValues.of(
                "GSSAPI", "USERNAME_PASSWORD");
        Properties properties = Properties.of(
                SocksPropertySpecConstants.SOCKS_METHODS.newProperty(
                        commaSeparatedValues));
        Assert.assertEquals(
                commaSeparatedValues,
                SocksValueDerivationHelper.getSocksMethodsFrom(properties));
    }

    @Test
    public void testGetSocksSocksDatagramSocketClientInfoUnavailableFromProperties01() {
        Properties properties = Properties.of();
        Assert.assertFalse(SocksValueDerivationHelper.getSocksSocksDatagramSocketClientInfoUnavailableFrom(properties));
    }

    @Test
    public void testGetSocksSocksDatagramSocketClientInfoUnavailableFromProperties02() {
        Properties properties = Properties.of(
                SocksPropertySpecConstants.SOCKS_SOCKS_DATAGRAM_SOCKET_CLIENT_INFO_UNAVAILABLE.newProperty(
                        true));
        Assert.assertTrue(SocksValueDerivationHelper.getSocksSocksDatagramSocketClientInfoUnavailableFrom(properties));
    }

    @Test
    public void testGetSocksUserpassAuthMethodPasswordFromProperties01() {
        Properties properties = Properties.of();
        Assert.assertArrayEquals(
                EncryptedPassword.newInstance(new char[]{}).getPassword(),
                SocksValueDerivationHelper.getSocksUserpassAuthMethodPasswordFrom(properties).getPassword());
    }

    @Test
    public void testGetSocksUserpassAuthMethodPasswordFromProperties02() {
        EncryptedPassword encryptedPassword = EncryptedPassword.newInstance(
                "opensesame".toCharArray());
        Properties properties = Properties.of(
                SocksPropertySpecConstants.SOCKS_USERPASSAUTHMETHOD_PASSWORD.newProperty(
                        encryptedPassword));
        Assert.assertArrayEquals(
                encryptedPassword.getPassword(),
                SocksValueDerivationHelper.getSocksUserpassAuthMethodPasswordFrom(properties).getPassword());
    }

    @Test
    public void testGetSocksUserpassAuthMethodUsernameFromProperties01() {
        Properties properties = Properties.of();
        Assert.assertEquals(
                System.getProperty("user.name"),
                SocksValueDerivationHelper.getSocksUserpassAuthMethodUsernameFrom(properties));
    }

    @Test
    public void testGetSocksUserpassAuthMethodUsernameFromProperties02() {
        String string = "Aladdin";
        Properties properties = Properties.of(
                SocksPropertySpecConstants.SOCKS_USERPASSAUTHMETHOD_USERNAME.newProperty(
                        string));
        Assert.assertEquals(
                string,
                SocksValueDerivationHelper.getSocksUserpassAuthMethodUsernameFrom(properties));
    }

}