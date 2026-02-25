package com.github.jh3nd3rs0n.jargyle.client.socks5;

import com.github.jh3nd3rs0n.jargyle.client.*;
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

public class Socks5ClientAgentTest {

    @Test
    public void testCanSocks5HostResolverResolveFromSocks5Server01() {
        Properties properties = Properties.of();
        SocksClient socksClient = SocksServerUriScheme.SOCKS5
                .newSocksServerUri("127.0.0.1")
                .newSocksClient(properties);
        Socks5ClientAgent socks5ClientAgent = new Socks5ClientAgent(
                (Socks5Client) socksClient);
        Assert.assertFalse(socks5ClientAgent.canSocks5HostResolverResolveFromSocks5Server());
    }

    @Test
    public void testCanSocks5HostResolverResolveFromSocks5Server02() {
        Properties properties = Properties.of(
                SocksPropertySpecConstants.SOCKS_SOCKS_HOST_RESOLVER_RESOLVE_FROM_SOCKS_SERVER.newProperty(
                        true));
        SocksClient socksClient = SocksServerUriScheme.SOCKS5
                .newSocksServerUri("127.0.0.1")
                .newSocksClient(properties);
        Socks5ClientAgent socks5ClientAgent = new Socks5ClientAgent(
                (Socks5Client) socksClient);
        Assert.assertTrue(socks5ClientAgent.canSocks5HostResolverResolveFromSocks5Server());
    }

    @Test
    public void testCanSocks5HostResolverResolveFromSocks5Server03() {
        Properties properties = Properties.of(
                Socks5PropertySpecConstants.SOCKS5_SOCKS5_HOST_RESOLVER_RESOLVE_FROM_SOCKS5_SERVER.newProperty(
                        true));
        SocksClient socksClient = SocksServerUriScheme.SOCKS5
                .newSocksServerUri("127.0.0.1")
                .newSocksClient(properties);
        Socks5ClientAgent socks5ClientAgent = new Socks5ClientAgent(
                (Socks5Client) socksClient);
        Assert.assertTrue(socks5ClientAgent.canSocks5HostResolverResolveFromSocks5Server());
    }

    @Test
    public void testGetGssapiAuthMethodMechanismOid01() throws GSSException {
        Properties properties = Properties.of();
        SocksClient socksClient = SocksServerUriScheme.SOCKS5
                .newSocksServerUri("127.0.0.1")
                .newSocksClient(properties);
        Socks5ClientAgent socks5ClientAgent = new Socks5ClientAgent(
                (Socks5Client) socksClient);
        Assert.assertEquals(
                new Oid("1.2.840.113554.1.2.2"),
                socks5ClientAgent.getGssapiAuthMethodMechanismOid());
    }

    @Test
    public void testGetGssapiAuthMethodMechanismOid02() throws GSSException {
        Oid oid = new Oid("2.2.2.2.2.2.2");
        Properties properties = Properties.of(
                SocksPropertySpecConstants.SOCKS_GSSAPIAUTHMETHOD_MECHANISM_OID.newProperty(
                        oid));
        SocksClient socksClient = SocksServerUriScheme.SOCKS5
                .newSocksServerUri("127.0.0.1")
                .newSocksClient(properties);
        Socks5ClientAgent socks5ClientAgent = new Socks5ClientAgent(
                (Socks5Client) socksClient);        
        Assert.assertEquals(
                oid,
                socks5ClientAgent.getGssapiAuthMethodMechanismOid());
    }

    @Test
    public void testGetGssapiAuthMethodMechanismOid03() throws GSSException {
        Oid oid = new Oid("2.2.2.2.2.2.2");
        Properties properties = Properties.of(
                Socks5PropertySpecConstants.SOCKS5_GSSAPIAUTHMETHOD_MECHANISM_OID.newProperty(
                        oid));
        SocksClient socksClient = SocksServerUriScheme.SOCKS5
                .newSocksServerUri("127.0.0.1")
                .newSocksClient(properties);
        Socks5ClientAgent socks5ClientAgent = new Socks5ClientAgent(
                (Socks5Client) socksClient);        
        Assert.assertEquals(
                oid,
                socks5ClientAgent.getGssapiAuthMethodMechanismOid());
    }

    @Test
    public void testGetGssapiAuthMethodNecReferenceImpl01() {
        Properties properties = Properties.of();
        SocksClient socksClient = SocksServerUriScheme.SOCKS5
                .newSocksServerUri("127.0.0.1")
                .newSocksClient(properties);
        Socks5ClientAgent socks5ClientAgent = new Socks5ClientAgent(
                (Socks5Client) socksClient);        
        Assert.assertFalse(socks5ClientAgent.getGssapiAuthMethodNecReferenceImpl());
    }

    @Test
    public void testGetGssapiAuthMethodNecReferenceImpl02() {
        Properties properties = Properties.of(
                SocksPropertySpecConstants.SOCKS_GSSAPIAUTHMETHOD_NEC_REFERENCE_IMPL.newProperty(
                        true));
        SocksClient socksClient = SocksServerUriScheme.SOCKS5
                .newSocksServerUri("127.0.0.1")
                .newSocksClient(properties);
        Socks5ClientAgent socks5ClientAgent = new Socks5ClientAgent(
                (Socks5Client) socksClient);        
        Assert.assertTrue(socks5ClientAgent.getGssapiAuthMethodNecReferenceImpl());
    }

    @Test
    public void testGetGssapiAuthMethodNecReferenceImpl03() {
        Properties properties = Properties.of(
                Socks5PropertySpecConstants.SOCKS5_GSSAPIAUTHMETHOD_NEC_REFERENCE_IMPL.newProperty(
                        true));
        SocksClient socksClient = SocksServerUriScheme.SOCKS5
                .newSocksServerUri("127.0.0.1")
                .newSocksClient(properties);
        Socks5ClientAgent socks5ClientAgent = new Socks5ClientAgent(
                (Socks5Client) socksClient);        
        Assert.assertTrue(socks5ClientAgent.getGssapiAuthMethodNecReferenceImpl());
    }

    @Test
    public void testGetGssapiAuthMethodProtectionLevels01() {
        Properties properties = Properties.of();
        SocksClient socksClient = SocksServerUriScheme.SOCKS5
                .newSocksServerUri("127.0.0.1")
                .newSocksClient(properties);
        Socks5ClientAgent socks5ClientAgent = new Socks5ClientAgent(
                (Socks5Client) socksClient);        
        Assert.assertEquals(
                CommaSeparatedValues.newInstanceFrom(ProtectionLevels.of(
                        ProtectionLevel.REQUIRED_INTEG_AND_CONF,
                        ProtectionLevel.REQUIRED_INTEG,
                        ProtectionLevel.NONE).toString()),
                socks5ClientAgent.getGssapiAuthMethodProtectionLevels());
    }

    @Test
    public void testGetGssapiAuthMethodProtectionLevels02() {
        ProtectionLevels protectionLevels = ProtectionLevels.of(
                ProtectionLevel.SELECTIVE_INTEG_OR_CONF);
        CommaSeparatedValues commaSeparatedValues =
                CommaSeparatedValues.newInstanceFrom(
                        protectionLevels.toString());
        Properties properties = Properties.of(
                SocksPropertySpecConstants.SOCKS_GSSAPIAUTHMETHOD_PROTECTION_LEVELS.newProperty(
                        commaSeparatedValues));
        SocksClient socksClient = SocksServerUriScheme.SOCKS5
                .newSocksServerUri("127.0.0.1")
                .newSocksClient(properties);
        Socks5ClientAgent socks5ClientAgent = new Socks5ClientAgent(
                (Socks5Client) socksClient);        
        Assert.assertEquals(
                CommaSeparatedValues.newInstanceFrom(protectionLevels.toString()),
                socks5ClientAgent.getGssapiAuthMethodProtectionLevels());
    }

    @Test
    public void testGetGssapiAuthMethodProtectionLevels03() {
        ProtectionLevels protectionLevels = ProtectionLevels.of(
                ProtectionLevel.SELECTIVE_INTEG_OR_CONF);
        Properties properties = Properties.of(
                Socks5PropertySpecConstants.SOCKS5_GSSAPIAUTHMETHOD_PROTECTION_LEVELS.newProperty(
                        protectionLevels));
        SocksClient socksClient = SocksServerUriScheme.SOCKS5
                .newSocksServerUri("127.0.0.1")
                .newSocksClient(properties);
        Socks5ClientAgent socks5ClientAgent = new Socks5ClientAgent(
                (Socks5Client) socksClient);        
        Assert.assertEquals(
                CommaSeparatedValues.newInstanceFrom(protectionLevels.toString()),
                socks5ClientAgent.getGssapiAuthMethodProtectionLevels());
    }

    @Test
    public void testGetGssapiAuthMethodServiceName01() {
        Properties properties = Properties.of();
        SocksClient socksClient = SocksServerUriScheme.SOCKS5
                .newSocksServerUri("127.0.0.1")
                .newSocksClient(properties);
        Socks5ClientAgent socks5ClientAgent = new Socks5ClientAgent(
                (Socks5Client) socksClient);        
        Assert.assertNull(socks5ClientAgent.getGssapiAuthMethodServiceName());
    }

    @Test
    public void testGetGssapiAuthMethodServiceName02() {
        String string = "rcmd/localhost";
        Properties properties = Properties.of(
                SocksPropertySpecConstants.SOCKS_GSSAPIAUTHMETHOD_SERVICE_NAME.newProperty(
                        string));
        SocksClient socksClient = SocksServerUriScheme.SOCKS5
                .newSocksServerUri("127.0.0.1")
                .newSocksClient(properties);
        Socks5ClientAgent socks5ClientAgent = new Socks5ClientAgent(
                (Socks5Client) socksClient);        
        Assert.assertEquals(
                string,
                socks5ClientAgent.getGssapiAuthMethodServiceName());
    }

    @Test
    public void testGetGssapiAuthMethodServiceName03() {
        String string = "rcmd/localhost";
        Properties properties = Properties.of(
                Socks5PropertySpecConstants.SOCKS5_GSSAPIAUTHMETHOD_SERVICE_NAME.newProperty(
                        string));
        SocksClient socksClient = SocksServerUriScheme.SOCKS5
                .newSocksServerUri("127.0.0.1")
                .newSocksClient(properties);
        Socks5ClientAgent socks5ClientAgent = new Socks5ClientAgent(
                (Socks5Client) socksClient);        
        Assert.assertEquals(
                string,
                socks5ClientAgent.getGssapiAuthMethodServiceName());
    }

    @Test
    public void testGetGssapiAuthMethodSuggestedConf01() {
        Properties properties = Properties.of();
        SocksClient socksClient = SocksServerUriScheme.SOCKS5
                .newSocksServerUri("127.0.0.1")
                .newSocksClient(properties);
        Socks5ClientAgent socks5ClientAgent = new Socks5ClientAgent(
                (Socks5Client) socksClient);        
        Assert.assertTrue(socks5ClientAgent.getGssapiAuthMethodSuggestedConf());
    }

    @Test
    public void testGetGssapiAuthMethodSuggestedConf02() {
        Properties properties = Properties.of(
                SocksPropertySpecConstants.SOCKS_GSSAPIAUTHMETHOD_SUGGESTED_CONF.newProperty(
                        false));
        SocksClient socksClient = SocksServerUriScheme.SOCKS5
                .newSocksServerUri("127.0.0.1")
                .newSocksClient(properties);
        Socks5ClientAgent socks5ClientAgent = new Socks5ClientAgent(
                (Socks5Client) socksClient);        
        Assert.assertFalse(socks5ClientAgent.getGssapiAuthMethodSuggestedConf());
    }

    @Test
    public void testGetGssapiAuthMethodSuggestedConf03() {
        Properties properties = Properties.of(
                Socks5PropertySpecConstants.SOCKS5_GSSAPIAUTHMETHOD_SUGGESTED_CONF.newProperty(
                        false));
        SocksClient socksClient = SocksServerUriScheme.SOCKS5
                .newSocksServerUri("127.0.0.1")
                .newSocksClient(properties);
        Socks5ClientAgent socks5ClientAgent = new Socks5ClientAgent(
                (Socks5Client) socksClient);        
        Assert.assertFalse(socks5ClientAgent.getGssapiAuthMethodSuggestedConf());
    }

    @Test
    public void testGetGssapiAuthMethodSuggestedInteg01() {
        Properties properties = Properties.of();
        SocksClient socksClient = SocksServerUriScheme.SOCKS5
                .newSocksServerUri("127.0.0.1")
                .newSocksClient(properties);
        Socks5ClientAgent socks5ClientAgent = new Socks5ClientAgent(
                (Socks5Client) socksClient);        
        Assert.assertEquals(
                0,
                socks5ClientAgent.getGssapiAuthMethodSuggestedInteg());
    }

    @Test
    public void testGetGssapiAuthMethodSuggestedInteg02() {
        int i = 1;
        Properties properties = Properties.of(
                SocksPropertySpecConstants.SOCKS_GSSAPIAUTHMETHOD_SUGGESTED_INTEG.newProperty(
                        i));
        SocksClient socksClient = SocksServerUriScheme.SOCKS5
                .newSocksServerUri("127.0.0.1")
                .newSocksClient(properties);
        Socks5ClientAgent socks5ClientAgent = new Socks5ClientAgent(
                (Socks5Client) socksClient);        
        Assert.assertEquals(
                i,
                socks5ClientAgent.getGssapiAuthMethodSuggestedInteg());
    }

    @Test
    public void testGetGssapiAuthMethodSuggestedInteg03() {
        int i = 1;
        Properties properties = Properties.of(
                Socks5PropertySpecConstants.SOCKS5_GSSAPIAUTHMETHOD_SUGGESTED_INTEG.newProperty(
                        i));
        SocksClient socksClient = SocksServerUriScheme.SOCKS5
                .newSocksServerUri("127.0.0.1")
                .newSocksClient(properties);
        Socks5ClientAgent socks5ClientAgent = new Socks5ClientAgent(
                (Socks5Client) socksClient);        
        Assert.assertEquals(
                i,
                socks5ClientAgent.getGssapiAuthMethodSuggestedInteg());
    }

    @Test
    public void testGetMethods01() {
        Properties properties = Properties.of();
        SocksClient socksClient = SocksServerUriScheme.SOCKS5
                .newSocksServerUri("127.0.0.1")
                .newSocksClient(properties);
        Socks5ClientAgent socks5ClientAgent = new Socks5ClientAgent(
                (Socks5Client) socksClient);        
        Assert.assertEquals(
                CommaSeparatedValues.newInstanceFrom(
                        Methods.of(Method.NO_AUTHENTICATION_REQUIRED).toString()),
                socks5ClientAgent.getMethods());
    }

    @Test
    public void testGetMethods02() {
        Methods methods = Methods.of(Method.GSSAPI, Method.USERNAME_PASSWORD);
        CommaSeparatedValues commaSeparatedValues =
                CommaSeparatedValues.newInstanceFrom(methods.toString());
        Properties properties = Properties.of(
                SocksPropertySpecConstants.SOCKS_METHODS.newProperty(
                        commaSeparatedValues));
        SocksClient socksClient = SocksServerUriScheme.SOCKS5
                .newSocksServerUri("127.0.0.1")
                .newSocksClient(properties);
        Socks5ClientAgent socks5ClientAgent = new Socks5ClientAgent(
                (Socks5Client) socksClient);        
        Assert.assertEquals(
                CommaSeparatedValues.newInstanceFrom(methods.toString()),
                socks5ClientAgent.getMethods());
    }

    @Test
    public void testGetMethods03() {
        Methods methods = Methods.of(Method.GSSAPI, Method.USERNAME_PASSWORD);
        Properties properties = Properties.of(
                Socks5PropertySpecConstants.SOCKS5_METHODS.newProperty(
                        methods));
        SocksClient socksClient = SocksServerUriScheme.SOCKS5
                .newSocksServerUri("127.0.0.1")
                .newSocksClient(properties);
        Socks5ClientAgent socks5ClientAgent = new Socks5ClientAgent(
                (Socks5Client) socksClient);        
        Assert.assertEquals(
                CommaSeparatedValues.newInstanceFrom(methods.toString()),
                socks5ClientAgent.getMethods());
    }

    @Test
    public void testGetUserpassAuthMethodPassword01() {
        Properties properties = Properties.of();
        SocksClient socksClient = SocksServerUriScheme.SOCKS5
                .newSocksServerUri("127.0.0.1")
                .newSocksClient(properties);
        Socks5ClientAgent socks5ClientAgent = new Socks5ClientAgent(
                (Socks5Client) socksClient);        
        Assert.assertArrayEquals(
                EncryptedPassword.newInstance(new char[]{}).getPassword(),
                socks5ClientAgent.getUserpassAuthMethodPassword().getPassword());
    }

    @Test
    public void testGetUserpassAuthMethodPassword02() {
        EncryptedPassword encryptedPassword = EncryptedPassword.newInstance(
                "opensesame".toCharArray());
        Properties properties = Properties.of(
                SocksPropertySpecConstants.SOCKS_USERPASSAUTHMETHOD_PASSWORD.newProperty(
                        encryptedPassword));
        SocksClient socksClient = SocksServerUriScheme.SOCKS5
                .newSocksServerUri("127.0.0.1")
                .newSocksClient(properties);
        Socks5ClientAgent socks5ClientAgent = new Socks5ClientAgent(
                (Socks5Client) socksClient);        
        Assert.assertArrayEquals(
                encryptedPassword.getPassword(),
                socks5ClientAgent.getUserpassAuthMethodPassword().getPassword());
    }

    @Test
    public void testGetUserpassAuthMethodPassword03() {
        EncryptedPassword encryptedPassword = EncryptedPassword.newInstance(
                "opensesame".toCharArray());
        Properties properties = Properties.of(
                Socks5PropertySpecConstants.SOCKS5_USERPASSAUTHMETHOD_PASSWORD.newProperty(
                        encryptedPassword));
        SocksClient socksClient = SocksServerUriScheme.SOCKS5
                .newSocksServerUri("127.0.0.1")
                .newSocksClient(properties);
        Socks5ClientAgent socks5ClientAgent = new Socks5ClientAgent(
                (Socks5Client) socksClient);        
        Assert.assertArrayEquals(
                encryptedPassword.getPassword(),
                socks5ClientAgent.getUserpassAuthMethodPassword().getPassword());
    }

    @Test
    public void testGetUserpassAuthMethodUsername01() {
        Properties properties = Properties.of();
        SocksClient socksClient = SocksServerUriScheme.SOCKS5
                .newSocksServerUri("127.0.0.1")
                .newSocksClient(properties);
        Socks5ClientAgent socks5ClientAgent = new Socks5ClientAgent(
                (Socks5Client) socksClient);        
        Assert.assertEquals(
                System.getProperty("user.name"),
                socks5ClientAgent.getUserpassAuthMethodUsername());
    }

    @Test
    public void testGetUserpassAuthMethodUsername02() {
        String string = "Aladdin";
        Properties properties = Properties.of(
                SocksPropertySpecConstants.SOCKS_USERPASSAUTHMETHOD_USERNAME.newProperty(
                        string));
        SocksClient socksClient = SocksServerUriScheme.SOCKS5
                .newSocksServerUri("127.0.0.1")
                .newSocksClient(properties);
        Socks5ClientAgent socks5ClientAgent = new Socks5ClientAgent(
                (Socks5Client) socksClient);        
        Assert.assertEquals(
                string,
                socks5ClientAgent.getUserpassAuthMethodUsername());
    }

    @Test
    public void testGetUserpassAuthMethodUsername03() {
        String string = "Aladdin";
        Properties properties = Properties.of(
                Socks5PropertySpecConstants.SOCKS5_USERPASSAUTHMETHOD_USERNAME.newProperty(
                        string));
        SocksClient socksClient = SocksServerUriScheme.SOCKS5
                .newSocksServerUri("127.0.0.1")
                .newSocksClient(properties);
        Socks5ClientAgent socks5ClientAgent = new Socks5ClientAgent(
                (Socks5Client) socksClient);        
        Assert.assertEquals(
                string,
                socks5ClientAgent.getUserpassAuthMethodUsername());
    }

    @Test
    public void testIsSocks5DatagramSocketClientInfoUnavailable01() {
        Properties properties = Properties.of();
        SocksClient socksClient = SocksServerUriScheme.SOCKS5
                .newSocksServerUri("127.0.0.1")
                .newSocksClient(properties);
        Socks5ClientAgent socks5ClientAgent = new Socks5ClientAgent(
                (Socks5Client) socksClient);
        Assert.assertFalse(socks5ClientAgent.isSocks5DatagramSocketClientInfoUnavailable());
    }

    @Test
    public void testIsSocks5DatagramSocketClientInfoUnavailable02() {
        Properties properties = Properties.of(
                SocksPropertySpecConstants.SOCKS_SOCKS_DATAGRAM_SOCKET_CLIENT_INFO_UNAVAILABLE.newProperty(
                        true));
        SocksClient socksClient = SocksServerUriScheme.SOCKS5
                .newSocksServerUri("127.0.0.1")
                .newSocksClient(properties);
        Socks5ClientAgent socks5ClientAgent = new Socks5ClientAgent(
                (Socks5Client) socksClient);
        Assert.assertTrue(socks5ClientAgent.isSocks5DatagramSocketClientInfoUnavailable());
    }

    @Test
    public void testIsSocks5DatagramSocketClientInfoUnavailable03() {
        Properties properties = Properties.of(
                Socks5PropertySpecConstants.SOCKS5_SOCKS5_DATAGRAM_SOCKET_CLIENT_INFO_UNAVAILABLE.newProperty(
                        true));
        SocksClient socksClient = SocksServerUriScheme.SOCKS5
                .newSocksServerUri("127.0.0.1")
                .newSocksClient(properties);
        Socks5ClientAgent socks5ClientAgent = new Socks5ClientAgent(
                (Socks5Client) socksClient);
        Assert.assertTrue(socks5ClientAgent.isSocks5DatagramSocketClientInfoUnavailable());
    }

}