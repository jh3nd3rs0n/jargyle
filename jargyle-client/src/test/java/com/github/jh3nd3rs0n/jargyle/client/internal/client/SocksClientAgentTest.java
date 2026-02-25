package com.github.jh3nd3rs0n.jargyle.client.internal.client;

import com.github.jh3nd3rs0n.jargyle.client.*;
import com.github.jh3nd3rs0n.jargyle.common.net.*;
import com.github.jh3nd3rs0n.jargyle.common.number.NonNegativeInteger;
import com.github.jh3nd3rs0n.jargyle.common.security.EncryptedPassword;
import com.github.jh3nd3rs0n.jargyle.common.string.CommaSeparatedValues;
import org.ietf.jgss.GSSException;
import org.ietf.jgss.Oid;
import org.junit.Assert;
import org.junit.Test;

import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;

public class SocksClientAgentTest {

    @Test
    public void testCanSocksHostResolverResolveFromSocksServer01() {
        Properties properties = Properties.of();
        SocksClient socksClient = SocksServerUriScheme.SOCKS5
                .newSocksServerUri("127.0.0.1")
                .newSocksClient(properties);
        SocksClientAgent socksClientAgent = new SocksClientAgent(socksClient);
        Assert.assertFalse(socksClientAgent.canSocksHostResolverResolveFromSocksServer());
    }

    @Test
    public void testCanSocksHostResolverResolveFromSocksServer02() {
        Properties properties = Properties.of(
                SocksPropertySpecConstants.SOCKS_SOCKS_HOST_RESOLVER_RESOLVE_FROM_SOCKS_SERVER.newProperty(
                        true));
        SocksClient socksClient = SocksServerUriScheme.SOCKS5
                .newSocksServerUri("127.0.0.1")
                .newSocksClient(properties);
        SocksClientAgent socksClientAgent = new SocksClientAgent(socksClient);
        Assert.assertTrue(socksClientAgent.canSocksHostResolverResolveFromSocksServer());
    }

    @Test
    public void testGetClientBindHost01() {
        Host host = HostIpv4Address.getAllZerosInstance();
        Properties properties = Properties.of();
        SocksClient socksClient = SocksServerUriScheme.SOCKS5
                .newSocksServerUri("127.0.0.1")
                .newSocksClient(properties);
        SocksClientAgent socksClientAgent = new SocksClientAgent(socksClient);        
        Assert.assertEquals(
                host,
                socksClientAgent.getClientBindHost());
    }

    @Test
    public void testGetClientBindHost02() {
        Host host = Host.newInstance("127.0.0.1");
        Properties properties = Properties.of(
                GeneralPropertySpecConstants.CLIENT_BIND_HOST.newProperty(host));
        SocksClient socksClient = SocksServerUriScheme.SOCKS5
                .newSocksServerUri("127.0.0.1")
                .newSocksClient(properties);
        SocksClientAgent socksClientAgent = new SocksClientAgent(socksClient);        
        Assert.assertEquals(
                host,
                socksClientAgent.getClientBindHost());
    }

    @Test
    public void testGetClientBindHost03() throws SocketException, UnknownHostException {
        Host host = Host.newInstance("127.0.0.1");
        NetworkInterface networkInterface = NetworkInterface.getByInetAddress(
                host.toInetAddress());
        NetInterface netInterface = NetInterface.newInstance(networkInterface);
        Properties properties = Properties.of(
                GeneralPropertySpecConstants.CLIENT_NET_INTERFACE.newProperty(netInterface),
                GeneralPropertySpecConstants.CLIENT_BIND_HOST_ADDRESS_TYPES.newProperty(
                        HostAddressTypes.of(HostAddressType.HOST_IPV4_ADDRESS)));
        SocksClient socksClient = SocksServerUriScheme.SOCKS5
                .newSocksServerUri("127.0.0.1")
                .newSocksClient(properties);
        SocksClientAgent socksClientAgent = new SocksClientAgent(socksClient);        
        Assert.assertEquals(
                host,
                socksClientAgent.getClientBindHost());
    }

    @Test
    public void testGetClientBindPortRanges01() {
        Properties properties = Properties.of();
        SocksClient socksClient = SocksServerUriScheme.SOCKS5
                .newSocksServerUri("127.0.0.1")
                .newSocksClient(properties);
        SocksClientAgent socksClientAgent = new SocksClientAgent(socksClient);
        Assert.assertEquals(
                PortRanges.getDefault(),
                socksClientAgent.getClientBindPortRanges());
    }

    @Test
    public void testGetClientBindPortRanges02() {
        PortRanges portRanges = PortRanges.of(PortRange.of(
                Port.valueOf(0),
                Port.valueOf(65535)));
        Properties properties = Properties.of(
                GeneralPropertySpecConstants.CLIENT_BIND_PORT_RANGES.newProperty(
                        portRanges));
        SocksClient socksClient = SocksServerUriScheme.SOCKS5
                .newSocksServerUri("127.0.0.1")
                .newSocksClient(properties);
        SocksClientAgent socksClientAgent = new SocksClientAgent(socksClient);
        Assert.assertEquals(
                portRanges,
                socksClientAgent.getClientBindPortRanges());
    }

    @Test
    public void testGetClientConnectTimeout01() {
        Properties properties = Properties.of();
        SocksClient socksClient = SocksServerUriScheme.SOCKS5
                .newSocksServerUri("127.0.0.1")
                .newSocksClient(properties);
        SocksClientAgent socksClientAgent = new SocksClientAgent(socksClient);
        Assert.assertEquals(
                NonNegativeInteger.valueOf(60000),
                socksClientAgent.getClientConnectTimeout());
    }

    @Test
    public void testGetClientConnectTimeout02() {
        NonNegativeInteger connectTimeout = NonNegativeInteger.valueOf(0);
        Properties properties = Properties.of(
                GeneralPropertySpecConstants.CLIENT_CONNECT_TIMEOUT.newProperty(
                        connectTimeout));
        SocksClient socksClient = SocksServerUriScheme.SOCKS5
                .newSocksServerUri("127.0.0.1")
                .newSocksClient(properties);
        SocksClientAgent socksClientAgent = new SocksClientAgent(socksClient);
        Assert.assertEquals(
                connectTimeout,
                socksClientAgent.getClientConnectTimeout());
    }

    @Test
    public void testGetClientSocketSettings01() {
        Properties properties = Properties.of();
        SocksClient socksClient = SocksServerUriScheme.SOCKS5
                .newSocksServerUri("127.0.0.1")
                .newSocksClient(properties);
        SocksClientAgent socksClientAgent = new SocksClientAgent(socksClient);
        Assert.assertEquals(
                SocketSettings.of(),
                socksClientAgent.getClientSocketSettings());
    }

    @Test
    public void testGetClientSocketSettings02() {
        SocketSettings socketSettings = SocketSettings.of(
                StandardSocketSettingSpecConstants.SO_TIMEOUT.newSocketSetting(
                        NonNegativeInteger.valueOf(60000)));
        Properties properties = Properties.of(
                GeneralPropertySpecConstants.CLIENT_SOCKET_SETTINGS.newProperty(
                        socketSettings));
        SocksClient socksClient = SocksServerUriScheme.SOCKS5
                .newSocksServerUri("127.0.0.1")
                .newSocksClient(properties);
        SocksClientAgent socksClientAgent = new SocksClientAgent(socksClient);
        Assert.assertEquals(
                socketSettings,
                socksClientAgent.getClientSocketSettings());
    }

    @Test
    public void testGetGssapiAuthMethodMechanismOid01() throws GSSException {
        Properties properties = Properties.of();
        SocksClient socksClient = SocksServerUriScheme.SOCKS5
                .newSocksServerUri("127.0.0.1")
                .newSocksClient(properties);
        SocksClientAgent socksClientAgent = new SocksClientAgent(socksClient);        
        Assert.assertEquals(
                new Oid("1.2.840.113554.1.2.2"),
                socksClientAgent.getGssapiAuthMethodMechanismOid());
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
        SocksClientAgent socksClientAgent = new SocksClientAgent(socksClient);        
        Assert.assertEquals(
                oid,
                socksClientAgent.getGssapiAuthMethodMechanismOid());
    }

    @Test
    public void testGetGssapiAuthMethodNecReferenceImpl01() {
        Properties properties = Properties.of();
        SocksClient socksClient = SocksServerUriScheme.SOCKS5
                .newSocksServerUri("127.0.0.1")
                .newSocksClient(properties);
        SocksClientAgent socksClientAgent = new SocksClientAgent(socksClient);        
        Assert.assertFalse(socksClientAgent.getGssapiAuthMethodNecReferenceImpl());
    }

    @Test
    public void testGetGssapiAuthMethodNecReferenceImpl02() {
        Properties properties = Properties.of(
                SocksPropertySpecConstants.SOCKS_GSSAPIAUTHMETHOD_NEC_REFERENCE_IMPL.newProperty(
                        true));
        SocksClient socksClient = SocksServerUriScheme.SOCKS5
                .newSocksServerUri("127.0.0.1")
                .newSocksClient(properties);
        SocksClientAgent socksClientAgent = new SocksClientAgent(socksClient);        
        Assert.assertTrue(socksClientAgent.getGssapiAuthMethodNecReferenceImpl());
    }

    @Test
    public void testGetGssapiAuthMethodProtectionLevels01() {
        Properties properties = Properties.of();
        SocksClient socksClient = SocksServerUriScheme.SOCKS5
                .newSocksServerUri("127.0.0.1")
                .newSocksClient(properties);
        SocksClientAgent socksClientAgent = new SocksClientAgent(socksClient);        
        Assert.assertEquals(
                CommaSeparatedValues.of(
                        "REQUIRED_INTEG_AND_CONF",
                        "REQUIRED_INTEG",
                        "NONE"),
                socksClientAgent.getGssapiAuthMethodProtectionLevels());
    }

    @Test
    public void testGetGssapiAuthMethodProtectionLevels02() {
        CommaSeparatedValues commaSeparatedValues = CommaSeparatedValues.of(
                "SELECTIVE_INTEG_OR_CONF");
        Properties properties = Properties.of(
                SocksPropertySpecConstants.SOCKS_GSSAPIAUTHMETHOD_PROTECTION_LEVELS.newProperty(
                        commaSeparatedValues));
        SocksClient socksClient = SocksServerUriScheme.SOCKS5
                .newSocksServerUri("127.0.0.1")
                .newSocksClient(properties);
        SocksClientAgent socksClientAgent = new SocksClientAgent(socksClient);        
        Assert.assertEquals(
                commaSeparatedValues,
                socksClientAgent.getGssapiAuthMethodProtectionLevels());
    }

    @Test
    public void testGetGssapiAuthMethodServiceName01() {
        Properties properties = Properties.of();
        SocksClient socksClient = SocksServerUriScheme.SOCKS5
                .newSocksServerUri("127.0.0.1")
                .newSocksClient(properties);
        SocksClientAgent socksClientAgent = new SocksClientAgent(socksClient);        
        Assert.assertNull(socksClientAgent.getGssapiAuthMethodServiceName());
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
        SocksClientAgent socksClientAgent = new SocksClientAgent(socksClient);        
        Assert.assertEquals(
                string,
                socksClientAgent.getGssapiAuthMethodServiceName());
    }

    @Test
    public void testGetGssapiAuthMethodSuggestedConf01() {
        Properties properties = Properties.of();
        SocksClient socksClient = SocksServerUriScheme.SOCKS5
                .newSocksServerUri("127.0.0.1")
                .newSocksClient(properties);
        SocksClientAgent socksClientAgent = new SocksClientAgent(socksClient);        
        Assert.assertTrue(socksClientAgent.getGssapiAuthMethodSuggestedConf());
    }

    @Test
    public void testGetGssapiAuthMethodSuggestedConf02() {
        Properties properties = Properties.of(
                SocksPropertySpecConstants.SOCKS_GSSAPIAUTHMETHOD_SUGGESTED_CONF.newProperty(
                        false));
        SocksClient socksClient = SocksServerUriScheme.SOCKS5
                .newSocksServerUri("127.0.0.1")
                .newSocksClient(properties);
        SocksClientAgent socksClientAgent = new SocksClientAgent(socksClient);        
        Assert.assertFalse(socksClientAgent.getGssapiAuthMethodSuggestedConf());
    }

    @Test
    public void testGetGssapiAuthMethodSuggestedInteg01() {
        Properties properties = Properties.of();
        SocksClient socksClient = SocksServerUriScheme.SOCKS5
                .newSocksServerUri("127.0.0.1")
                .newSocksClient(properties);
        SocksClientAgent socksClientAgent = new SocksClientAgent(socksClient);        
        Assert.assertEquals(
                0,
                socksClientAgent.getGssapiAuthMethodSuggestedInteg());
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
        SocksClientAgent socksClientAgent = new SocksClientAgent(socksClient);        
        Assert.assertEquals(
                i,
                socksClientAgent.getGssapiAuthMethodSuggestedInteg());
    }

    @Test
    public void testGetMethods01() {
        Properties properties = Properties.of();
        SocksClient socksClient = SocksServerUriScheme.SOCKS5
                .newSocksServerUri("127.0.0.1")
                .newSocksClient(properties);
        SocksClientAgent socksClientAgent = new SocksClientAgent(socksClient);        
        Assert.assertEquals(
                CommaSeparatedValues.of("NO_AUTHENTICATION_REQUIRED"),
                socksClientAgent.getMethods());
    }

    @Test
    public void testGetMethods02() {
        CommaSeparatedValues commaSeparatedValues = CommaSeparatedValues.of(
                "GSSAPI", "USERNAME_PASSWORD");
        Properties properties = Properties.of(
                SocksPropertySpecConstants.SOCKS_METHODS.newProperty(
                        commaSeparatedValues));
        SocksClient socksClient = SocksServerUriScheme.SOCKS5
                .newSocksServerUri("127.0.0.1")
                .newSocksClient(properties);
        SocksClientAgent socksClientAgent = new SocksClientAgent(socksClient);        
        Assert.assertEquals(
                commaSeparatedValues,
                socksClientAgent.getMethods());
    }

    @Test
    public void testGetUserpassAuthMethodPassword01() {
        Properties properties = Properties.of();
        SocksClient socksClient = SocksServerUriScheme.SOCKS5
                .newSocksServerUri("127.0.0.1")
                .newSocksClient(properties);
        SocksClientAgent socksClientAgent = new SocksClientAgent(socksClient);        
        Assert.assertArrayEquals(
                EncryptedPassword.newInstance(new char[]{}).getPassword(),
                socksClientAgent.getUserpassAuthMethodPassword().getPassword());
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
        SocksClientAgent socksClientAgent = new SocksClientAgent(socksClient);        
        Assert.assertArrayEquals(
                encryptedPassword.getPassword(),
                socksClientAgent.getUserpassAuthMethodPassword().getPassword());
    }

    @Test
    public void testGetUserpassAuthMethodUsername01() {
        Properties properties = Properties.of();
        SocksClient socksClient = SocksServerUriScheme.SOCKS5
                .newSocksServerUri("127.0.0.1")
                .newSocksClient(properties);
        SocksClientAgent socksClientAgent = new SocksClientAgent(socksClient);        
        Assert.assertEquals(
                System.getProperty("user.name"),
                socksClientAgent.getUserpassAuthMethodUsername());
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
        SocksClientAgent socksClientAgent = new SocksClientAgent(socksClient);        
        Assert.assertEquals(
                string,
                socksClientAgent.getUserpassAuthMethodUsername());
    }

    @Test
    public void testIsSocksDatagramSocketClientInfoUnavailable01() {
        Properties properties = Properties.of();
        SocksClient socksClient = SocksServerUriScheme.SOCKS5
                .newSocksServerUri("127.0.0.1")
                .newSocksClient(properties);
        SocksClientAgent socksClientAgent = new SocksClientAgent(socksClient);
        Assert.assertFalse(socksClientAgent.isSocksDatagramSocketClientInfoUnavailable());
    }

    @Test
    public void testIsSocksDatagramSocketClientInfoUnavailable02() {
        Properties properties = Properties.of(
                SocksPropertySpecConstants.SOCKS_SOCKS_DATAGRAM_SOCKET_CLIENT_INFO_UNAVAILABLE.newProperty(
                        true));
        SocksClient socksClient = SocksServerUriScheme.SOCKS5
                .newSocksServerUri("127.0.0.1")
                .newSocksClient(properties);
        SocksClientAgent socksClientAgent = new SocksClientAgent(socksClient);
        Assert.assertTrue(socksClientAgent.isSocksDatagramSocketClientInfoUnavailable());
    }

    @Test
    public void testToSocksClientIOExceptionThrowable01() {
        Properties properties = Properties.of();
        SocksClient socksClient = SocksServerUriScheme.SOCKS5
                .newSocksServerUri("127.0.0.1")
                .newSocksClient(properties);
        SocksClientAgent socksClientAgent = new SocksClientAgent(socksClient);
        SocksClientIOException e = new SocksClientIOException(
                socksClient, new Exception());
        Assert.assertEquals(e, socksClientAgent.toSocksClientIOException(e));
    }

    @Test
    public void testToSocksClientIOExceptionThrowable02() {
        Properties properties = Properties.of();
        SocksClient socksClient1 = SocksServerUriScheme.SOCKS5
                .newSocksServerUri("127.0.0.1", 1080)
                .newSocksClient(properties);
        SocksClient socksClient2 = SocksServerUriScheme.SOCKS5
                .newSocksServerUri("127.0.0.1", 2080)
                .newSocksClient(properties);
        SocksClientAgent socksClientAgent = new SocksClientAgent(socksClient2);
        Throwable t = new Exception();
        SocksClientSocketException e1 = new SocksClientSocketException(
                socksClient1, t);
        SocksClientIOException e2 = socksClientAgent.toSocksClientIOException(
                e1);
        Assert.assertEquals(socksClient1, e2.getSocksClient());
        Assert.assertEquals(t, e2.getCause());
    }

    @Test
    public void testToSocksClientIOExceptionThrowable03() {
        Properties properties = Properties.of();
        SocksClient socksClient = SocksServerUriScheme.SOCKS5
                .newSocksServerUri("127.0.0.1")
                .newSocksClient(properties);
        SocksClientAgent socksClientAgent = new SocksClientAgent(socksClient);
        Throwable t = new Exception();
        SocksClientIOException e = socksClientAgent.toSocksClientIOException(
                t);
        Assert.assertEquals(socksClient, e.getSocksClient());
        Assert.assertEquals(t, e.getCause());
    }

    @Test
    public void testToSocksClientSocketExceptionThrowable01() {
        Properties properties = Properties.of();
        SocksClient socksClient = SocksServerUriScheme.SOCKS5
                .newSocksServerUri("127.0.0.1")
                .newSocksClient(properties);
        SocksClientAgent socksClientAgent = new SocksClientAgent(socksClient);
        Throwable t = new Exception();
        SocksClientSocketException e = new SocksClientSocketException(
                socksClient, t);
        Assert.assertEquals(e, socksClientAgent.toSocksClientSocketException(e));
    }

    @Test
    public void testToSocksClientSocketExceptionThrowable02() {
        Properties properties = Properties.of();
        SocksClient socksClient = SocksServerUriScheme.SOCKS5
                .newSocksServerUri("127.0.0.1")
                .newSocksClient(properties);
        SocksClientAgent socksClientAgent = new SocksClientAgent(socksClient);
        Throwable t = new Exception();
        SocksClientSocketException e =
                socksClientAgent.toSocksClientSocketException(t);
        Assert.assertEquals(socksClient, e.getSocksClient());
        Assert.assertEquals(t, e.getCause());
    }

}