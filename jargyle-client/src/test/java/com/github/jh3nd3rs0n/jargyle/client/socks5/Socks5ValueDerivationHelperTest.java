package com.github.jh3nd3rs0n.jargyle.client.socks5;

import com.github.jh3nd3rs0n.jargyle.client.Properties;
import com.github.jh3nd3rs0n.jargyle.client.Socks5PropertySpecConstants;
import com.github.jh3nd3rs0n.jargyle.client.SocksPropertySpecConstants;
import com.github.jh3nd3rs0n.jargyle.common.security.EncryptedPassword;
import com.github.jh3nd3rs0n.jargyle.common.string.CommaSeparatedValues;
import com.github.jh3nd3rs0n.jargyle.protocolbase.socks5.Method;
import com.github.jh3nd3rs0n.jargyle.protocolbase.socks5.Methods;
import com.github.jh3nd3rs0n.jargyle.protocolbase.socks5.gssapiauthmethod.ProtectionLevel;
import com.github.jh3nd3rs0n.jargyle.protocolbase.socks5.gssapiauthmethod.ProtectionLevels;
import org.ietf.jgss.GSSException;
import org.ietf.jgss.Oid;
import org.junit.Assert;
import org.junit.Test;

public class Socks5ValueDerivationHelperTest {

    @Test
    public void testGetSocks5GssapiAuthMethodMechanismOidFromProperties01() throws GSSException {
        Properties properties = Properties.of();
        Assert.assertEquals(
                new Oid("1.2.840.113554.1.2.2"),
                Socks5ValueDerivationHelper.getSocks5GssapiAuthMethodMechanismOidFrom(properties));
    }

    @Test
    public void testGetSocks5GssapiAuthMethodMechanismOidFromProperties02() throws GSSException {
        Oid oid = new Oid("2.2.2.2.2.2.2");
        Properties properties = Properties.of(
                SocksPropertySpecConstants.SOCKS_GSSAPIAUTHMETHOD_MECHANISM_OID.newProperty(
                        oid));
        Assert.assertEquals(
                oid,
                Socks5ValueDerivationHelper.getSocks5GssapiAuthMethodMechanismOidFrom(properties));
    }

    @Test
    public void testGetSocks5GssapiAuthMethodMechanismOidFromProperties03() throws GSSException {
        Oid oid = new Oid("2.2.2.2.2.2.2");
        Properties properties = Properties.of(
                Socks5PropertySpecConstants.SOCKS5_GSSAPIAUTHMETHOD_MECHANISM_OID.newProperty(
                        oid));
        Assert.assertEquals(
                oid,
                Socks5ValueDerivationHelper.getSocks5GssapiAuthMethodMechanismOidFrom(properties));
    }

    @Test
    public void testGetSocks5GssapiAuthMethodNecReferenceImplFromProperties01() {
        Properties properties = Properties.of();
        Assert.assertFalse(Socks5ValueDerivationHelper.getSocks5GssapiAuthMethodNecReferenceImplFrom(properties));
    }

    @Test
    public void testGetSocks5GssapiAuthMethodNecReferenceImplFromProperties02() {
        Properties properties = Properties.of(
                SocksPropertySpecConstants.SOCKS_GSSAPIAUTHMETHOD_NEC_REFERENCE_IMPL.newProperty(
                        true));
        Assert.assertTrue(Socks5ValueDerivationHelper.getSocks5GssapiAuthMethodNecReferenceImplFrom(properties));
    }

    @Test
    public void testGetSocks5GssapiAuthMethodNecReferenceImplFromProperties03() {
        Properties properties = Properties.of(
                Socks5PropertySpecConstants.SOCKS5_GSSAPIAUTHMETHOD_NEC_REFERENCE_IMPL.newProperty(
                        true));
        Assert.assertTrue(Socks5ValueDerivationHelper.getSocks5GssapiAuthMethodNecReferenceImplFrom(properties));
    }

    @Test
    public void testGetSocks5GssapiAuthMethodProtectionLevelsFromProperties01() {
        Properties properties = Properties.of();
        Assert.assertEquals(
                ProtectionLevels.of(
                        ProtectionLevel.REQUIRED_INTEG_AND_CONF,
                        ProtectionLevel.REQUIRED_INTEG,
                        ProtectionLevel.NONE),
                Socks5ValueDerivationHelper.getSocks5GssapiAuthMethodProtectionLevelsFrom(properties));
    }

    @Test
    public void testGetSocks5GssapiAuthMethodProtectionLevelsFromProperties02() {
        ProtectionLevels protectionLevels = ProtectionLevels.of(
                ProtectionLevel.SELECTIVE_INTEG_OR_CONF);
        CommaSeparatedValues commaSeparatedValues =
                CommaSeparatedValues.newInstanceFrom(
                        protectionLevels.toString());
        Properties properties = Properties.of(
                SocksPropertySpecConstants.SOCKS_GSSAPIAUTHMETHOD_PROTECTION_LEVELS.newProperty(
                        commaSeparatedValues));
        Assert.assertEquals(
                protectionLevels,
                Socks5ValueDerivationHelper.getSocks5GssapiAuthMethodProtectionLevelsFrom(properties));
    }

    @Test
    public void testGetSocks5GssapiAuthMethodProtectionLevelsFromProperties03() {
        ProtectionLevels protectionLevels = ProtectionLevels.of(
                ProtectionLevel.SELECTIVE_INTEG_OR_CONF);
        Properties properties = Properties.of(
                Socks5PropertySpecConstants.SOCKS5_GSSAPIAUTHMETHOD_PROTECTION_LEVELS.newProperty(
                        protectionLevels));
        Assert.assertEquals(
                protectionLevels,
                Socks5ValueDerivationHelper.getSocks5GssapiAuthMethodProtectionLevelsFrom(properties));
    }

    @Test
    public void testGetSocks5GssapiAuthMethodServiceNameFromProperties01() {
        Properties properties = Properties.of();
        Assert.assertNull(Socks5ValueDerivationHelper.getSocks5GssapiAuthMethodServiceNameFrom(properties));
    }

    @Test
    public void testGetSocks5GssapiAuthMethodServiceNameFromProperties02() {
        String string = "rcmd/localhost";
        Properties properties = Properties.of(
                SocksPropertySpecConstants.SOCKS_GSSAPIAUTHMETHOD_SERVICE_NAME.newProperty(
                        string));
        Assert.assertEquals(
                string,
                Socks5ValueDerivationHelper.getSocks5GssapiAuthMethodServiceNameFrom(properties));
    }

    @Test
    public void testGetSocks5GssapiAuthMethodServiceNameFromProperties03() {
        String string = "rcmd/localhost";
        Properties properties = Properties.of(
                Socks5PropertySpecConstants.SOCKS5_GSSAPIAUTHMETHOD_SERVICE_NAME.newProperty(
                        string));
        Assert.assertEquals(
                string,
                Socks5ValueDerivationHelper.getSocks5GssapiAuthMethodServiceNameFrom(properties));
    }

    @Test
    public void testGetSocks5GssapiAuthMethodSuggestedConfFromProperties01() {
        Properties properties = Properties.of();
        Assert.assertTrue(Socks5ValueDerivationHelper.getSocks5GssapiAuthMethodSuggestedConfFrom(properties));
    }

    @Test
    public void testGetSocks5GssapiAuthMethodSuggestedConfFromProperties02() {
        Properties properties = Properties.of(
                SocksPropertySpecConstants.SOCKS_GSSAPIAUTHMETHOD_SUGGESTED_CONF.newProperty(
                        false));
        Assert.assertFalse(Socks5ValueDerivationHelper.getSocks5GssapiAuthMethodSuggestedConfFrom(properties));
    }

    @Test
    public void testGetSocks5GssapiAuthMethodSuggestedConfFromProperties03() {
        Properties properties = Properties.of(
                Socks5PropertySpecConstants.SOCKS5_GSSAPIAUTHMETHOD_SUGGESTED_CONF.newProperty(
                        false));
        Assert.assertFalse(Socks5ValueDerivationHelper.getSocks5GssapiAuthMethodSuggestedConfFrom(properties));
    }

    @Test
    public void testGetSocks5GssapiAuthMethodSuggestedIntegFromProperties01() {
        Properties properties = Properties.of();
        Assert.assertEquals(
                0,
                Socks5ValueDerivationHelper.getSocks5GssapiAuthMethodSuggestedIntegFrom(properties));
    }

    @Test
    public void testGetSocks5GssapiAuthMethodSuggestedIntegFromProperties02() {
        int i = 1;
        Properties properties = Properties.of(
                SocksPropertySpecConstants.SOCKS_GSSAPIAUTHMETHOD_SUGGESTED_INTEG.newProperty(
                        i));
        Assert.assertEquals(
                i,
                Socks5ValueDerivationHelper.getSocks5GssapiAuthMethodSuggestedIntegFrom(properties));
    }

    @Test
    public void testGetSocks5GssapiAuthMethodSuggestedIntegFromProperties03() {
        int i = 1;
        Properties properties = Properties.of(
                Socks5PropertySpecConstants.SOCKS5_GSSAPIAUTHMETHOD_SUGGESTED_INTEG.newProperty(
                        i));
        Assert.assertEquals(
                i,
                Socks5ValueDerivationHelper.getSocks5GssapiAuthMethodSuggestedIntegFrom(properties));
    }

    @Test
    public void testGetSocks5MethodsFromProperties01() {
        Properties properties = Properties.of();
        Assert.assertEquals(
                Methods.of(Method.NO_AUTHENTICATION_REQUIRED),
                Socks5ValueDerivationHelper.getSocks5MethodsFrom(properties));
    }

    @Test
    public void testGetSocks5MethodsFromProperties02() {
        Methods methods = Methods.of(Method.GSSAPI, Method.USERNAME_PASSWORD);
        CommaSeparatedValues commaSeparatedValues =
                CommaSeparatedValues.newInstanceFrom(methods.toString());
        Properties properties = Properties.of(
                SocksPropertySpecConstants.SOCKS_METHODS.newProperty(
                        commaSeparatedValues));
        Assert.assertEquals(
                methods,
                Socks5ValueDerivationHelper.getSocks5MethodsFrom(properties));
    }

    @Test
    public void testGetSocks5MethodsFromProperties03() {
        Methods methods = Methods.of(Method.GSSAPI, Method.USERNAME_PASSWORD);
        Properties properties = Properties.of(
                Socks5PropertySpecConstants.SOCKS5_METHODS.newProperty(
                        methods));
        Assert.assertEquals(
                methods,
                Socks5ValueDerivationHelper.getSocks5MethodsFrom(properties));
    }

    @Test
    public void testGetSocks5Socks5DatagramSocketClientInfoUnavailableFromProperties01() {
        Properties properties = Properties.of();
        Assert.assertFalse(Socks5ValueDerivationHelper.getSocks5Socks5DatagramSocketClientInfoUnavailableFrom(properties));
    }

    @Test
    public void testGetSocks5Socks5DatagramSocketClientInfoUnavailableFromProperties02() {
        Properties properties = Properties.of(
                SocksPropertySpecConstants.SOCKS_SOCKS_DATAGRAM_SOCKET_CLIENT_INFO_UNAVAILABLE.newProperty(
                        true));
        Assert.assertTrue(Socks5ValueDerivationHelper.getSocks5Socks5DatagramSocketClientInfoUnavailableFrom(properties));
    }

    @Test
    public void testGetSocks5Socks5DatagramSocketClientInfoUnavailableFromProperties03() {
        Properties properties = Properties.of(
                Socks5PropertySpecConstants.SOCKS5_SOCKS5_DATAGRAM_SOCKET_CLIENT_INFO_UNAVAILABLE.newProperty(
                        true));
        Assert.assertTrue(Socks5ValueDerivationHelper.getSocks5Socks5DatagramSocketClientInfoUnavailableFrom(properties));
    }

    @Test
    public void testGetSocks5Socks5HostResolverResolveFromSocks5ServerFromProperties01() {
        Properties properties = Properties.of();
        Assert.assertFalse(Socks5ValueDerivationHelper.getSocks5Socks5HostResolverResolveFromSocks5ServerFrom(properties));
    }

    @Test
    public void testGetSocks5Socks5HostResolverResolveFromSocks5ServerFromProperties02() {
        Properties properties = Properties.of(
                SocksPropertySpecConstants.SOCKS_SOCKS_HOST_RESOLVER_RESOLVE_FROM_SOCKS_SERVER.newProperty(
                        true));
        Assert.assertTrue(Socks5ValueDerivationHelper.getSocks5Socks5HostResolverResolveFromSocks5ServerFrom(properties));
    }

    @Test
    public void testGetSocks5Socks5HostResolverResolveFromSocks5ServerFromProperties03() {
        Properties properties = Properties.of(
                Socks5PropertySpecConstants.SOCKS5_SOCKS5_HOST_RESOLVER_RESOLVE_FROM_SOCKS5_SERVER.newProperty(
                        true));
        Assert.assertTrue(Socks5ValueDerivationHelper.getSocks5Socks5HostResolverResolveFromSocks5ServerFrom(properties));
    }

    @Test
    public void testGetSocks5UserpassAuthMethodPasswordFromProperties01() {
        Properties properties = Properties.of();
        Assert.assertArrayEquals(
                EncryptedPassword.newInstance(new char[]{}).getPassword(),
                Socks5ValueDerivationHelper.getSocks5UserpassAuthMethodPasswordFrom(properties).getPassword());
    }

    @Test
    public void testGetSocks5UserpassAuthMethodPasswordFromProperties02() {
        EncryptedPassword encryptedPassword = EncryptedPassword.newInstance(
                "opensesame".toCharArray());
        Properties properties = Properties.of(
                SocksPropertySpecConstants.SOCKS_USERPASSAUTHMETHOD_PASSWORD.newProperty(
                        encryptedPassword));
        Assert.assertArrayEquals(
                encryptedPassword.getPassword(),
                Socks5ValueDerivationHelper.getSocks5UserpassAuthMethodPasswordFrom(properties).getPassword());
    }

    @Test
    public void testGetSocks5UserpassAuthMethodPasswordFromProperties03() {
        EncryptedPassword encryptedPassword = EncryptedPassword.newInstance(
                "opensesame".toCharArray());
        Properties properties = Properties.of(
                Socks5PropertySpecConstants.SOCKS5_USERPASSAUTHMETHOD_PASSWORD.newProperty(
                        encryptedPassword));
        Assert.assertArrayEquals(
                encryptedPassword.getPassword(),
                Socks5ValueDerivationHelper.getSocks5UserpassAuthMethodPasswordFrom(properties).getPassword());
    }

    @Test
    public void testGetSocks5UserpassAuthMethodUsernameFromProperties01() {
        Properties properties = Properties.of();
        Assert.assertEquals(
                System.getProperty("user.name"),
                Socks5ValueDerivationHelper.getSocks5UserpassAuthMethodUsernameFrom(properties));
    }

    @Test
    public void testGetSocks5UserpassAuthMethodUsernameFromProperties02() {
        String string = "Aladdin";
        Properties properties = Properties.of(
                SocksPropertySpecConstants.SOCKS_USERPASSAUTHMETHOD_USERNAME.newProperty(
                        string));
        Assert.assertEquals(
                string,
                Socks5ValueDerivationHelper.getSocks5UserpassAuthMethodUsernameFrom(properties));
    }

    @Test
    public void testGetSocks5UserpassAuthMethodUsernameFromProperties03() {
        String string = "Aladdin";
        Properties properties = Properties.of(
                Socks5PropertySpecConstants.SOCKS5_USERPASSAUTHMETHOD_USERNAME.newProperty(
                        string));
        Assert.assertEquals(
                string,
                Socks5ValueDerivationHelper.getSocks5UserpassAuthMethodUsernameFrom(properties));
    }

}