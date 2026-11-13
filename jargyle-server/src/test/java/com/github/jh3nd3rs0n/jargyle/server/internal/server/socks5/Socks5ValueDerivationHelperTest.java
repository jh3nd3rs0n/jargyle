package com.github.jh3nd3rs0n.jargyle.server.internal.server.socks5;

import com.github.jh3nd3rs0n.jargyle.common.net.*;
import com.github.jh3nd3rs0n.jargyle.common.number.NonNegativeInteger;
import com.github.jh3nd3rs0n.jargyle.common.number.PositiveInteger;
import com.github.jh3nd3rs0n.jargyle.common.string.CommaSeparatedValues;
import com.github.jh3nd3rs0n.jargyle.protocolbase.socks5.Address;
import com.github.jh3nd3rs0n.jargyle.protocolbase.socks5.Method;
import com.github.jh3nd3rs0n.jargyle.protocolbase.socks5.Methods;
import com.github.jh3nd3rs0n.jargyle.protocolbase.socks5.gssapiauthmethod.ProtectionLevel;
import com.github.jh3nd3rs0n.jargyle.protocolbase.socks5.gssapiauthmethod.ProtectionLevels;
import com.github.jh3nd3rs0n.jargyle.server.*;
import com.github.jh3nd3rs0n.jargyle.server.socks5.userpassauthmethod.UserRepository;
import org.junit.Assert;
import org.junit.Test;

import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;

public class Socks5ValueDerivationHelperTest {

    @Test
    public void testGetSocks5GssapiAuthMethodNecReferenceImplFromSettings01() {
        Settings settings = Settings.of();
        Assert.assertFalse(Socks5ValueDerivationHelper.getSocks5GssapiAuthMethodNecReferenceImplFrom(settings));
    }

    @Test
    public void testGetSocks5GssapiAuthMethodNecReferenceImplFromSettings02() {
        Settings settings = Settings.of(
                SocksSettingSpecConstants.SOCKS_GSSAPIAUTHMETHOD_NEC_REFERENCE_IMPL.newSetting(
                        Boolean.TRUE));
        Assert.assertTrue(Socks5ValueDerivationHelper.getSocks5GssapiAuthMethodNecReferenceImplFrom(settings));
    }

    @Test
    public void testGetSocks5GssapiAuthMethodNecReferenceImplFromSettings03() {
        Settings settings = Settings.of(
                Socks5SettingSpecConstants.SOCKS5_GSSAPIAUTHMETHOD_NEC_REFERENCE_IMPL.newSetting(
                        Boolean.TRUE));
        Assert.assertTrue(Socks5ValueDerivationHelper.getSocks5GssapiAuthMethodNecReferenceImplFrom(settings));
    }

    @Test
    public void testGetSocks5GssapiAuthMethodProtectionLevelsFromSettings01() {
        Settings settings = Settings.of();
        Assert.assertEquals(
                ProtectionLevels.of(
                        ProtectionLevel.REQUIRED_INTEG_AND_CONF,
                        ProtectionLevel.REQUIRED_INTEG,
                        ProtectionLevel.NONE),
                Socks5ValueDerivationHelper.getSocks5GssapiAuthMethodProtectionLevelsFrom(settings));
    }

    @Test
    public void testGetSocks5GssapiAuthMethodProtectionLevelsFromSettings02() {
        ProtectionLevels protectionLevels = ProtectionLevels.of(
                ProtectionLevel.SELECTIVE_INTEG_OR_CONF,
                ProtectionLevel.REQUIRED_INTEG_AND_CONF);
        Settings settings = Settings.of(
                SocksSettingSpecConstants.SOCKS_GSSAPIAUTHMETHOD_PROTECTION_LEVELS.newSetting(
                        CommaSeparatedValues.newInstanceFrom(protectionLevels.toString())));
        Assert.assertEquals(
                protectionLevels,
                Socks5ValueDerivationHelper.getSocks5GssapiAuthMethodProtectionLevelsFrom(settings));
    }

    @Test
    public void testGetSocks5GssapiAuthMethodProtectionLevelsFromSettings03() {
        ProtectionLevels protectionLevels = ProtectionLevels.of(
                ProtectionLevel.SELECTIVE_INTEG_OR_CONF,
                ProtectionLevel.REQUIRED_INTEG_AND_CONF);
        Settings settings = Settings.of(
                Socks5SettingSpecConstants.SOCKS5_GSSAPIAUTHMETHOD_PROTECTION_LEVELS.newSetting(
                        protectionLevels));
        Assert.assertEquals(
                protectionLevels,
                Socks5ValueDerivationHelper.getSocks5GssapiAuthMethodProtectionLevelsFrom(settings));
    }

    @Test
    public void testGetSocks5GssapiAuthMethodSuggestedConfFromSettings01() {
        Settings settings = Settings.of();
        Assert.assertTrue(Socks5ValueDerivationHelper.getSocks5GssapiAuthMethodSuggestedConfFrom(settings));
    }

    @Test
    public void testGetSocks5GssapiAuthMethodSuggestedConfFromSettings02() {
        Settings settings = Settings.of(
                SocksSettingSpecConstants.SOCKS_GSSAPIAUTHMETHOD_SUGGESTED_CONF.newSetting(
                        Boolean.FALSE));
        Assert.assertFalse(Socks5ValueDerivationHelper.getSocks5GssapiAuthMethodSuggestedConfFrom(settings));
    }

    @Test
    public void testGetSocks5GssapiAuthMethodSuggestedConfFromSettings03() {
        Settings settings = Settings.of(
                Socks5SettingSpecConstants.SOCKS5_GSSAPIAUTHMETHOD_SUGGESTED_CONF.newSetting(
                        Boolean.FALSE));
        Assert.assertFalse(Socks5ValueDerivationHelper.getSocks5GssapiAuthMethodSuggestedConfFrom(settings));
    }

    @Test
    public void testGetSocks5GssapiAuthMethodSuggestedIntegFromSettings01() {
        Settings settings = Settings.of();
        Assert.assertEquals(
                Integer.valueOf(0),
                Socks5ValueDerivationHelper.getSocks5GssapiAuthMethodSuggestedIntegFrom(settings));
    }

    @Test
    public void testGetSocks5GssapiAuthMethodSuggestedIntegFromSettings02() {
        Integer i = Integer.valueOf(1);
        Settings settings = Settings.of(
                SocksSettingSpecConstants.SOCKS_GSSAPIAUTHMETHOD_SUGGESTED_INTEG.newSetting(i));
        Assert.assertEquals(
                i,
                Socks5ValueDerivationHelper.getSocks5GssapiAuthMethodSuggestedIntegFrom(settings));
    }

    @Test
    public void testGetSocks5GssapiAuthMethodSuggestedIntegFromSettings03() {
        Integer i = Integer.valueOf(1);
        Settings settings = Settings.of(
                Socks5SettingSpecConstants.SOCKS5_GSSAPIAUTHMETHOD_SUGGESTED_INTEG.newSetting(i));
        Assert.assertEquals(
                i,
                Socks5ValueDerivationHelper.getSocks5GssapiAuthMethodSuggestedIntegFrom(settings));
    }

    @Test
    public void testGetSocks5MethodsFromSettings01() {
        Settings settings = Settings.of();
        Assert.assertEquals(
                Methods.of(Method.NO_AUTHENTICATION_REQUIRED),
                Socks5ValueDerivationHelper.getSocks5MethodsFrom(settings));
    }

    @Test
    public void testGetSocks5MethodsFromSettings02() {
        Methods methods = Methods.of(Method.USERNAME_PASSWORD, Method.GSSAPI);
        Settings settings = Settings.of(
                SocksSettingSpecConstants.SOCKS_METHODS.newSetting(
                        CommaSeparatedValues.newInstanceFrom(methods.toString())));
        Assert.assertEquals(
                methods,
                Socks5ValueDerivationHelper.getSocks5MethodsFrom(settings));
    }

    @Test
    public void testGetSocks5MethodsFromSettings03() {
        Methods methods = Methods.of(Method.USERNAME_PASSWORD, Method.GSSAPI);
        Settings settings = Settings.of(
                Socks5SettingSpecConstants.SOCKS5_METHODS.newSetting(
                        methods));
        Assert.assertEquals(
                methods,
                Socks5ValueDerivationHelper.getSocks5MethodsFrom(settings));
    }

    @Test
    public void testGetSocks5OnBindRequestRelayBufferSizeFromRuleSettings01() {
        PositiveInteger relayBufferSize = PositiveInteger.valueOf(1024);
        Rule rule = new Rule.Builder().build();
        Settings settings = Settings.of();
        Assert.assertEquals(
                relayBufferSize,
                Socks5ValueDerivationHelper.getSocks5OnBindRequestRelayBufferSizeFrom(rule, settings));
    }

    @Test
    public void testGetSocks5OnBindRequestRelayBufferSizeFromRuleSettings02() {
        PositiveInteger relayBufferSize = PositiveInteger.valueOf(2048);
        Rule rule = new Rule.Builder()
                .addRuleAction(SocksRuleActionSpecConstants.SOCKS_ON_REQUEST_RELAY_BUFFER_SIZE.newRuleAction(
                        relayBufferSize))
                .build();
        Settings settings = Settings.of();
        Assert.assertEquals(
                relayBufferSize,
                Socks5ValueDerivationHelper.getSocks5OnBindRequestRelayBufferSizeFrom(rule, settings));
    }

    @Test
    public void testGetSocks5OnBindRequestRelayBufferSizeFromRuleSettings03() {
        PositiveInteger relayBufferSize = PositiveInteger.valueOf(2048);
        Rule rule = new Rule.Builder()
                .addRuleAction(SocksRuleActionSpecConstants.SOCKS_ON_BIND_REQUEST_RELAY_BUFFER_SIZE.newRuleAction(
                        relayBufferSize))
                .build();
        Settings settings = Settings.of();
        Assert.assertEquals(
                relayBufferSize,
                Socks5ValueDerivationHelper.getSocks5OnBindRequestRelayBufferSizeFrom(rule, settings));
    }

    @Test
    public void testGetSocks5OnBindRequestRelayBufferSizeFromRuleSettings04() {
        PositiveInteger relayBufferSize = PositiveInteger.valueOf(2048);
        Rule rule = new Rule.Builder()
                .addRuleAction(Socks5RuleActionSpecConstants.SOCKS5_ON_BIND_REQUEST_RELAY_BUFFER_SIZE.newRuleAction(
                        relayBufferSize))
                .build();
        Settings settings = Settings.of();
        Assert.assertEquals(
                relayBufferSize,
                Socks5ValueDerivationHelper.getSocks5OnBindRequestRelayBufferSizeFrom(rule, settings));
    }

    @Test
    public void testGetSocks5OnBindRequestRelayBufferSizeFromRuleSettings05() {
        PositiveInteger relayBufferSize = PositiveInteger.valueOf(2048);
        Rule rule = new Rule.Builder().build();
        Settings settings = Settings.of(
                SocksSettingSpecConstants.SOCKS_ON_REQUEST_RELAY_BUFFER_SIZE.newSetting(
                        relayBufferSize));
        Assert.assertEquals(
                relayBufferSize,
                Socks5ValueDerivationHelper.getSocks5OnBindRequestRelayBufferSizeFrom(rule, settings));
    }

    @Test
    public void testGetSocks5OnBindRequestRelayBufferSizeFromRuleSettings06() {
        PositiveInteger relayBufferSize = PositiveInteger.valueOf(2048);
        Rule rule = new Rule.Builder().build();
        Settings settings = Settings.of(
                SocksSettingSpecConstants.SOCKS_ON_BIND_REQUEST_RELAY_BUFFER_SIZE.newSetting(
                        relayBufferSize));
        Assert.assertEquals(
                relayBufferSize,
                Socks5ValueDerivationHelper.getSocks5OnBindRequestRelayBufferSizeFrom(rule, settings));
    }

    @Test
    public void testGetSocks5OnBindRequestRelayBufferSizeFromRuleSettings07() {
        PositiveInteger relayBufferSize = PositiveInteger.valueOf(2048);
        Rule rule = new Rule.Builder().build();
        Settings settings = Settings.of(
                Socks5SettingSpecConstants.SOCKS5_ON_BIND_REQUEST_RELAY_BUFFER_SIZE.newSetting(
                        relayBufferSize));
        Assert.assertEquals(
                relayBufferSize,
                Socks5ValueDerivationHelper.getSocks5OnBindRequestRelayBufferSizeFrom(rule, settings));
    }

    @Test
    public void testGetSocks5OnBindRequestRelayIdleTimeoutFromRuleSettings01() {
        PositiveInteger relayIdleTimeout = PositiveInteger.valueOf(60000);
        Rule rule = new Rule.Builder().build();
        Settings settings = Settings.of();
        Assert.assertEquals(
                relayIdleTimeout,
                Socks5ValueDerivationHelper.getSocks5OnBindRequestRelayIdleTimeoutFrom(rule, settings));
    }

    @Test
    public void testGetSocks5OnBindRequestRelayIdleTimeoutFromRuleSettings02() {
        PositiveInteger relayIdleTimeout = PositiveInteger.valueOf(120000);
        Rule rule = new Rule.Builder()
                .addRuleAction(SocksRuleActionSpecConstants.SOCKS_ON_REQUEST_RELAY_IDLE_TIMEOUT.newRuleAction(
                        relayIdleTimeout))
                .build();
        Settings settings = Settings.of();
        Assert.assertEquals(
                relayIdleTimeout,
                Socks5ValueDerivationHelper.getSocks5OnBindRequestRelayIdleTimeoutFrom(rule, settings));
    }

    @Test
    public void testGetSocks5OnBindRequestRelayIdleTimeoutFromRuleSettings03() {
        PositiveInteger relayIdleTimeout = PositiveInteger.valueOf(120000);
        Rule rule = new Rule.Builder()
                .addRuleAction(SocksRuleActionSpecConstants.SOCKS_ON_BIND_REQUEST_RELAY_IDLE_TIMEOUT.newRuleAction(
                        relayIdleTimeout))
                .build();
        Settings settings = Settings.of();
        Assert.assertEquals(
                relayIdleTimeout,
                Socks5ValueDerivationHelper.getSocks5OnBindRequestRelayIdleTimeoutFrom(rule, settings));
    }

    @Test
    public void testGetSocks5OnBindRequestRelayIdleTimeoutFromRuleSettings04() {
        PositiveInteger relayIdleTimeout = PositiveInteger.valueOf(120000);
        Rule rule = new Rule.Builder()
                .addRuleAction(Socks5RuleActionSpecConstants.SOCKS5_ON_BIND_REQUEST_RELAY_IDLE_TIMEOUT.newRuleAction(
                        relayIdleTimeout))
                .build();
        Settings settings = Settings.of();
        Assert.assertEquals(
                relayIdleTimeout,
                Socks5ValueDerivationHelper.getSocks5OnBindRequestRelayIdleTimeoutFrom(rule, settings));
    }

    @Test
    public void testGetSocks5OnBindRequestRelayIdleTimeoutFromRuleSettings05() {
        PositiveInteger relayIdleTimeout = PositiveInteger.valueOf(120000);
        Rule rule = new Rule.Builder().build();
        Settings settings = Settings.of(
                SocksSettingSpecConstants.SOCKS_ON_REQUEST_RELAY_IDLE_TIMEOUT.newSetting(
                        relayIdleTimeout));
        Assert.assertEquals(
                relayIdleTimeout,
                Socks5ValueDerivationHelper.getSocks5OnBindRequestRelayIdleTimeoutFrom(rule, settings));
    }

    @Test
    public void testGetSocks5OnBindRequestRelayIdleTimeoutFromRuleSettings06() {
        PositiveInteger relayIdleTimeout = PositiveInteger.valueOf(120000);
        Rule rule = new Rule.Builder().build();
        Settings settings = Settings.of(
                SocksSettingSpecConstants.SOCKS_ON_BIND_REQUEST_RELAY_IDLE_TIMEOUT.newSetting(
                        relayIdleTimeout));
        Assert.assertEquals(
                relayIdleTimeout,
                Socks5ValueDerivationHelper.getSocks5OnBindRequestRelayIdleTimeoutFrom(rule, settings));
    }

    @Test
    public void testGetSocks5OnBindRequestRelayIdleTimeoutFromRuleSettings07() {
        PositiveInteger relayIdleTimeout = PositiveInteger.valueOf(120000);
        Rule rule = new Rule.Builder().build();
        Settings settings = Settings.of(
                Socks5SettingSpecConstants.SOCKS5_ON_BIND_REQUEST_RELAY_IDLE_TIMEOUT.newSetting(
                        relayIdleTimeout));
        Assert.assertEquals(
                relayIdleTimeout,
                Socks5ValueDerivationHelper.getSocks5OnBindRequestRelayIdleTimeoutFrom(rule, settings));
    }

    @Test
    public void testGetSocks5OnBindRequestRelayInboundBandwidthLimitFromRuleSettings01() {
        Rule rule = new Rule.Builder().build();
        Settings settings = Settings.of();
        Assert.assertNull(
                Socks5ValueDerivationHelper.getSocks5OnBindRequestRelayInboundBandwidthLimitFrom(rule, settings));
    }

    @Test
    public void testGetSocks5OnBindRequestRelayInboundBandwidthLimitFromRuleSettings02() {
        PositiveInteger relayInboundBandwidthLimit = PositiveInteger.valueOf(
                Integer.MAX_VALUE);
        Rule rule = new Rule.Builder()
                .addRuleAction(SocksRuleActionSpecConstants.SOCKS_ON_REQUEST_RELAY_INBOUND_BANDWIDTH_LIMIT.newRuleAction(
                        relayInboundBandwidthLimit))
                .build();
        Settings settings = Settings.of();
        Assert.assertEquals(
                relayInboundBandwidthLimit,
                Socks5ValueDerivationHelper.getSocks5OnBindRequestRelayInboundBandwidthLimitFrom(rule, settings));
    }

    @Test
    public void testGetSocks5OnBindRequestRelayInboundBandwidthLimitFromRuleSettings03() {
        PositiveInteger relayInboundBandwidthLimit = PositiveInteger.valueOf(
                Integer.MAX_VALUE);
        Rule rule = new Rule.Builder()
                .addRuleAction(SocksRuleActionSpecConstants.SOCKS_ON_BIND_REQUEST_RELAY_INBOUND_BANDWIDTH_LIMIT.newRuleAction(
                        relayInboundBandwidthLimit))
                .build();
        Settings settings = Settings.of();
        Assert.assertEquals(
                relayInboundBandwidthLimit,
                Socks5ValueDerivationHelper.getSocks5OnBindRequestRelayInboundBandwidthLimitFrom(rule, settings));
    }

    @Test
    public void testGetSocks5OnBindRequestRelayInboundBandwidthLimitFromRuleSettings04() {
        PositiveInteger relayInboundBandwidthLimit = PositiveInteger.valueOf(
                Integer.MAX_VALUE);
        Rule rule = new Rule.Builder()
                .addRuleAction(Socks5RuleActionSpecConstants.SOCKS5_ON_BIND_REQUEST_RELAY_INBOUND_BANDWIDTH_LIMIT.newRuleAction(
                        relayInboundBandwidthLimit))
                .build();
        Settings settings = Settings.of();
        Assert.assertEquals(
                relayInboundBandwidthLimit,
                Socks5ValueDerivationHelper.getSocks5OnBindRequestRelayInboundBandwidthLimitFrom(rule, settings));
    }

    @Test
    public void testGetSocks5OnBindRequestRelayInboundBandwidthLimitFromRuleSettings05() {
        PositiveInteger relayInboundBandwidthLimit = PositiveInteger.valueOf(
                Integer.MAX_VALUE);
        Rule rule = new Rule.Builder().build();
        Settings settings = Settings.of(
                SocksSettingSpecConstants.SOCKS_ON_REQUEST_RELAY_INBOUND_BANDWIDTH_LIMIT.newSetting(
                        relayInboundBandwidthLimit));
        Assert.assertEquals(
                relayInboundBandwidthLimit,
                Socks5ValueDerivationHelper.getSocks5OnBindRequestRelayInboundBandwidthLimitFrom(rule, settings));
    }

    @Test
    public void testGetSocks5OnBindRequestRelayInboundBandwidthLimitFromRuleSettings06() {
        PositiveInteger relayInboundBandwidthLimit = PositiveInteger.valueOf(
                Integer.MAX_VALUE);
        Rule rule = new Rule.Builder().build();
        Settings settings = Settings.of(
                SocksSettingSpecConstants.SOCKS_ON_BIND_REQUEST_RELAY_INBOUND_BANDWIDTH_LIMIT.newSetting(
                        relayInboundBandwidthLimit));
        Assert.assertEquals(
                relayInboundBandwidthLimit,
                Socks5ValueDerivationHelper.getSocks5OnBindRequestRelayInboundBandwidthLimitFrom(rule, settings));
    }

    @Test
    public void testGetSocks5OnBindRequestRelayInboundBandwidthLimitFromRuleSettings07() {
        PositiveInteger relayInboundBandwidthLimit = PositiveInteger.valueOf(
                Integer.MAX_VALUE);
        Rule rule = new Rule.Builder().build();
        Settings settings = Settings.of(
                Socks5SettingSpecConstants.SOCKS5_ON_BIND_REQUEST_RELAY_INBOUND_BANDWIDTH_LIMIT.newSetting(
                        relayInboundBandwidthLimit));
        Assert.assertEquals(
                relayInboundBandwidthLimit,
                Socks5ValueDerivationHelper.getSocks5OnBindRequestRelayInboundBandwidthLimitFrom(rule, settings));
    }

    @Test
    public void testGetSocks5OnBindRequestRelayOutboundBandwidthLimitFromRuleSettings01() {
        Rule rule = new Rule.Builder().build();
        Settings settings = Settings.of();
        Assert.assertNull(
                Socks5ValueDerivationHelper.getSocks5OnBindRequestRelayOutboundBandwidthLimitFrom(rule, settings));
    }

    @Test
    public void testGetSocks5OnBindRequestRelayOutboundBandwidthLimitFromRuleSettings02() {
        PositiveInteger relayOutboundBandwidthLimit = PositiveInteger.valueOf(
                Integer.MAX_VALUE);
        Rule rule = new Rule.Builder()
                .addRuleAction(SocksRuleActionSpecConstants.SOCKS_ON_REQUEST_RELAY_OUTBOUND_BANDWIDTH_LIMIT.newRuleAction(
                        relayOutboundBandwidthLimit))
                .build();
        Settings settings = Settings.of();
        Assert.assertEquals(
                relayOutboundBandwidthLimit,
                Socks5ValueDerivationHelper.getSocks5OnBindRequestRelayOutboundBandwidthLimitFrom(rule, settings));
    }

    @Test
    public void testGetSocks5OnBindRequestRelayOutboundBandwidthLimitFromRuleSettings03() {
        PositiveInteger relayOutboundBandwidthLimit = PositiveInteger.valueOf(
                Integer.MAX_VALUE);
        Rule rule = new Rule.Builder()
                .addRuleAction(SocksRuleActionSpecConstants.SOCKS_ON_BIND_REQUEST_RELAY_OUTBOUND_BANDWIDTH_LIMIT.newRuleAction(
                        relayOutboundBandwidthLimit))
                .build();
        Settings settings = Settings.of();
        Assert.assertEquals(
                relayOutboundBandwidthLimit,
                Socks5ValueDerivationHelper.getSocks5OnBindRequestRelayOutboundBandwidthLimitFrom(rule, settings));
    }

    @Test
    public void testGetSocks5OnBindRequestRelayOutboundBandwidthLimitFromRuleSettings04() {
        PositiveInteger relayOutboundBandwidthLimit = PositiveInteger.valueOf(
                Integer.MAX_VALUE);
        Rule rule = new Rule.Builder()
                .addRuleAction(Socks5RuleActionSpecConstants.SOCKS5_ON_BIND_REQUEST_RELAY_OUTBOUND_BANDWIDTH_LIMIT.newRuleAction(
                        relayOutboundBandwidthLimit))
                .build();
        Settings settings = Settings.of();
        Assert.assertEquals(
                relayOutboundBandwidthLimit,
                Socks5ValueDerivationHelper.getSocks5OnBindRequestRelayOutboundBandwidthLimitFrom(rule, settings));
    }

    @Test
    public void testGetSocks5OnBindRequestRelayOutboundBandwidthLimitFromRuleSettings05() {
        PositiveInteger relayOutboundBandwidthLimit = PositiveInteger.valueOf(
                Integer.MAX_VALUE);
        Rule rule = new Rule.Builder().build();
        Settings settings = Settings.of(
                SocksSettingSpecConstants.SOCKS_ON_REQUEST_RELAY_OUTBOUND_BANDWIDTH_LIMIT.newSetting(
                        relayOutboundBandwidthLimit));
        Assert.assertEquals(
                relayOutboundBandwidthLimit,
                Socks5ValueDerivationHelper.getSocks5OnBindRequestRelayOutboundBandwidthLimitFrom(rule, settings));
    }

    @Test
    public void testGetSocks5OnBindRequestRelayOutboundBandwidthLimitFromRuleSettings06() {
        PositiveInteger relayOutboundBandwidthLimit = PositiveInteger.valueOf(
                Integer.MAX_VALUE);
        Rule rule = new Rule.Builder().build();
        Settings settings = Settings.of(
                SocksSettingSpecConstants.SOCKS_ON_BIND_REQUEST_RELAY_OUTBOUND_BANDWIDTH_LIMIT.newSetting(
                        relayOutboundBandwidthLimit));
        Assert.assertEquals(
                relayOutboundBandwidthLimit,
                Socks5ValueDerivationHelper.getSocks5OnBindRequestRelayOutboundBandwidthLimitFrom(rule, settings));
    }

    @Test
    public void testGetSocks5OnBindRequestRelayOutboundBandwidthLimitFromRuleSettings07() {
        PositiveInteger relayOutboundBandwidthLimit = PositiveInteger.valueOf(
                Integer.MAX_VALUE);
        Rule rule = new Rule.Builder().build();
        Settings settings = Settings.of(
                Socks5SettingSpecConstants.SOCKS5_ON_BIND_REQUEST_RELAY_OUTBOUND_BANDWIDTH_LIMIT.newSetting(
                        relayOutboundBandwidthLimit));
        Assert.assertEquals(
                relayOutboundBandwidthLimit,
                Socks5ValueDerivationHelper.getSocks5OnBindRequestRelayOutboundBandwidthLimitFrom(rule, settings));
    }

    @Test
    public void testGetSocks5OnBindRequestRemoteFacingServerSocketBindHostFromRuleSettings01() {
        Host host = HostIpv4Address.getAllZerosInstance();
        Rule rule = new Rule.Builder().build();
        Settings settings = Settings.of();
        Assert.assertEquals(
                host,
                Socks5ValueDerivationHelper.getSocks5OnBindRequestRemoteFacingServerSocketBindHostFrom(rule, settings));
    }

    @Test
    public void testGetSocks5OnBindRequestRemoteFacingServerSocketBindHostFromRuleSettings02() {
        Host host = Host.newInstance("127.0.0.1");
        Rule rule = new Rule.Builder()
                .addRuleAction(GeneralRuleActionSpecConstants.BIND_HOST.newRuleAction(
                        host))
                .build();
        Settings settings = Settings.of();
        Assert.assertEquals(
                host,
                Socks5ValueDerivationHelper.getSocks5OnBindRequestRemoteFacingServerSocketBindHostFrom(rule, settings));
    }

    @Test
    public void testGetSocks5OnBindRequestRemoteFacingServerSocketBindHostFromRuleSettings03() {
        Host host = Host.newInstance("127.0.0.1");
        Rule rule = new Rule.Builder()
                .addRuleAction(GeneralRuleActionSpecConstants.REMOTE_ENDPOINTS_BIND_HOST.newRuleAction(
                        host))
                .build();
        Settings settings = Settings.of();
        Assert.assertEquals(
                host,
                Socks5ValueDerivationHelper.getSocks5OnBindRequestRemoteFacingServerSocketBindHostFrom(rule, settings));
    }

    @Test
    public void testGetSocks5OnBindRequestRemoteFacingServerSocketBindHostFromRuleSettings04() {
        Host host = Host.newInstance("127.0.0.1");
        Rule rule = new Rule.Builder()
                .addRuleAction(SocksRuleActionSpecConstants.SOCKS_ON_REQUEST_REMOTE_ENDPOINTS_BIND_HOST.newRuleAction(
                        host))
                .build();
        Settings settings = Settings.of();
        Assert.assertEquals(
                host,
                Socks5ValueDerivationHelper.getSocks5OnBindRequestRemoteFacingServerSocketBindHostFrom(rule, settings));
    }

    @Test
    public void testGetSocks5OnBindRequestRemoteFacingServerSocketBindHostFromRuleSettings05() {
        Host host = Host.newInstance("127.0.0.1");
        Rule rule = new Rule.Builder()
                .addRuleAction(SocksRuleActionSpecConstants.SOCKS_ON_BIND_REQUEST_REMOTE_FACING_SERVER_SOCKET_BIND_HOST.newRuleAction(
                        host))
                .build();
        Settings settings = Settings.of();
        Assert.assertEquals(
                host,
                Socks5ValueDerivationHelper.getSocks5OnBindRequestRemoteFacingServerSocketBindHostFrom(rule, settings));
    }

    @Test
    public void testGetSocks5OnBindRequestRemoteFacingServerSocketBindHostFromRuleSettings06() {
        Host host = Host.newInstance("127.0.0.1");
        Rule rule = new Rule.Builder()
                .addRuleAction(Socks5RuleActionSpecConstants.SOCKS5_ON_BIND_REQUEST_REMOTE_FACING_SERVER_SOCKET_BIND_HOST.newRuleAction(
                        host))
                .build();
        Settings settings = Settings.of();
        Assert.assertEquals(
                host,
                Socks5ValueDerivationHelper.getSocks5OnBindRequestRemoteFacingServerSocketBindHostFrom(rule, settings));
    }

    @Test
    public void testGetSocks5OnBindRequestRemoteFacingServerSocketBindHostFromRuleSettings07() throws UnknownHostException, SocketException {
        Host host = Host.newInstance("127.0.0.1");
        NetworkInterface networkInterface = NetworkInterface.getByInetAddress(
                host.toInetAddress());
        NetInterface netInterface = NetInterface.newInstance(networkInterface);
        Rule rule = new Rule.Builder()
                .addRuleAction(GeneralRuleActionSpecConstants.NET_INTERFACE.newRuleAction(
                        netInterface))
                .addRuleAction(GeneralRuleActionSpecConstants.BIND_HOST_ADDRESS_TYPE.newRuleAction(
                        HostAddressType.HOST_IPV4_ADDRESS))
                .build();
        Settings settings = Settings.of();
        Assert.assertTrue(
                netInterface.getHostAddresses().contains((HostAddress)
                        Socks5ValueDerivationHelper.getSocks5OnBindRequestRemoteFacingServerSocketBindHostFrom(rule, settings)));
    }

    @Test
    public void testGetSocks5OnBindRequestRemoteFacingServerSocketBindHostFromRuleSettings08() throws UnknownHostException, SocketException {
        Host host = Host.newInstance("127.0.0.1");
        NetworkInterface networkInterface = NetworkInterface.getByInetAddress(
                host.toInetAddress());
        NetInterface netInterface = NetInterface.newInstance(networkInterface);
        Rule rule = new Rule.Builder()
                .addRuleAction(GeneralRuleActionSpecConstants.REMOTE_ENDPOINTS_NET_INTERFACE.newRuleAction(
                        netInterface))
                .addRuleAction(GeneralRuleActionSpecConstants.REMOTE_ENDPOINTS_BIND_HOST_ADDRESS_TYPE.newRuleAction(
                        HostAddressType.HOST_IPV4_ADDRESS))
                .build();
        Settings settings = Settings.of();
        Assert.assertTrue(
                netInterface.getHostAddresses().contains((HostAddress)
                        Socks5ValueDerivationHelper.getSocks5OnBindRequestRemoteFacingServerSocketBindHostFrom(rule, settings)));
    }

    @Test
    public void testGetSocks5OnBindRequestRemoteFacingServerSocketBindHostFromRuleSettings09() throws UnknownHostException, SocketException {
        Host host = Host.newInstance("127.0.0.1");
        NetworkInterface networkInterface = NetworkInterface.getByInetAddress(
                host.toInetAddress());
        NetInterface netInterface = NetInterface.newInstance(networkInterface);
        Rule rule = new Rule.Builder()
                .addRuleAction(SocksRuleActionSpecConstants.SOCKS_ON_REQUEST_REMOTE_ENDPOINTS_NET_INTERFACE.newRuleAction(
                        netInterface))
                .addRuleAction(SocksRuleActionSpecConstants.SOCKS_ON_REQUEST_REMOTE_ENDPOINTS_BIND_HOST_ADDRESS_TYPE.newRuleAction(
                        HostAddressType.HOST_IPV4_ADDRESS))
                .build();
        Settings settings = Settings.of();
        Assert.assertTrue(
                netInterface.getHostAddresses().contains((HostAddress)
                        Socks5ValueDerivationHelper.getSocks5OnBindRequestRemoteFacingServerSocketBindHostFrom(rule, settings)));
    }

    @Test
    public void testGetSocks5OnBindRequestRemoteFacingServerSocketBindHostFromRuleSettings10() throws UnknownHostException, SocketException {
        Host host = Host.newInstance("127.0.0.1");
        NetworkInterface networkInterface = NetworkInterface.getByInetAddress(
                host.toInetAddress());
        NetInterface netInterface = NetInterface.newInstance(networkInterface);
        Rule rule = new Rule.Builder()
                .addRuleAction(SocksRuleActionSpecConstants.SOCKS_ON_BIND_REQUEST_REMOTE_FACING_SERVER_SOCKET_NET_INTERFACE.newRuleAction(
                        netInterface))
                .addRuleAction(SocksRuleActionSpecConstants.SOCKS_ON_BIND_REQUEST_REMOTE_FACING_SERVER_SOCKET_BIND_HOST_ADDRESS_TYPE.newRuleAction(
                        HostAddressType.HOST_IPV4_ADDRESS))
                .build();
        Settings settings = Settings.of();
        Assert.assertTrue(
                netInterface.getHostAddresses().contains((HostAddress)
                        Socks5ValueDerivationHelper.getSocks5OnBindRequestRemoteFacingServerSocketBindHostFrom(rule, settings)));
    }

    @Test
    public void testGetSocks5OnBindRequestRemoteFacingServerSocketBindHostFromRuleSettings11() throws UnknownHostException, SocketException {
        Host host = Host.newInstance("127.0.0.1");
        NetworkInterface networkInterface = NetworkInterface.getByInetAddress(
                host.toInetAddress());
        NetInterface netInterface = NetInterface.newInstance(networkInterface);
        Rule rule = new Rule.Builder()
                .addRuleAction(Socks5RuleActionSpecConstants.SOCKS5_ON_BIND_REQUEST_REMOTE_FACING_SERVER_SOCKET_NET_INTERFACE.newRuleAction(
                        netInterface))
                .addRuleAction(Socks5RuleActionSpecConstants.SOCKS5_ON_BIND_REQUEST_REMOTE_FACING_SERVER_SOCKET_BIND_HOST_ADDRESS_TYPE.newRuleAction(
                        HostAddressType.HOST_IPV4_ADDRESS))
                .build();
        Settings settings = Settings.of();
        Assert.assertTrue(
                netInterface.getHostAddresses().contains((HostAddress)
                        Socks5ValueDerivationHelper.getSocks5OnBindRequestRemoteFacingServerSocketBindHostFrom(rule, settings)));
    }

    @Test
    public void testGetSocks5OnBindRequestRemoteFacingServerSocketBindHostFromRuleSettings12() {
        Host host = Host.newInstance("127.0.0.1");
        Rule rule = new Rule.Builder().build();
        Settings settings = Settings.of(
                GeneralSettingSpecConstants.BIND_HOST.newSetting(host));
        Assert.assertEquals(
                host,
                Socks5ValueDerivationHelper.getSocks5OnBindRequestRemoteFacingServerSocketBindHostFrom(rule, settings));
    }

    @Test
    public void testGetSocks5OnBindRequestRemoteFacingServerSocketBindHostFromRuleSettings13() {
        Host host = Host.newInstance("127.0.0.1");
        Rule rule = new Rule.Builder().build();
        Settings settings = Settings.of(
                GeneralSettingSpecConstants.REMOTE_ENDPOINTS_BIND_HOST.newSetting(
                        host));
        Assert.assertEquals(
                host,
                Socks5ValueDerivationHelper.getSocks5OnBindRequestRemoteFacingServerSocketBindHostFrom(rule, settings));
    }

    @Test
    public void testGetSocks5OnBindRequestRemoteFacingServerSocketBindHostFromRuleSettings14() {
        Host host = Host.newInstance("127.0.0.1");
        Rule rule = new Rule.Builder().build();
        Settings settings = Settings.of(
                SocksSettingSpecConstants.SOCKS_ON_REQUEST_REMOTE_ENDPOINTS_BIND_HOST.newSetting(
                        host));
        Assert.assertEquals(
                host,
                Socks5ValueDerivationHelper.getSocks5OnBindRequestRemoteFacingServerSocketBindHostFrom(rule, settings));
    }

    @Test
    public void testGetSocks5OnBindRequestRemoteFacingServerSocketBindHostFromRuleSettings15() {
        Host host = Host.newInstance("127.0.0.1");
        Rule rule = new Rule.Builder().build();
        Settings settings = Settings.of(
                SocksSettingSpecConstants.SOCKS_ON_BIND_REQUEST_REMOTE_FACING_SERVER_SOCKET_BIND_HOST.newSetting(
                        host));
        Assert.assertEquals(
                host,
                Socks5ValueDerivationHelper.getSocks5OnBindRequestRemoteFacingServerSocketBindHostFrom(rule, settings));
    }

    @Test
    public void testGetSocks5OnBindRequestRemoteFacingServerSocketBindHostFromRuleSettings16() {
        Host host = Host.newInstance("127.0.0.1");
        Rule rule = new Rule.Builder().build();
        Settings settings = Settings.of(
                Socks5SettingSpecConstants.SOCKS5_ON_BIND_REQUEST_REMOTE_FACING_SERVER_SOCKET_BIND_HOST.newSetting(
                        host));
        Assert.assertEquals(
                host,
                Socks5ValueDerivationHelper.getSocks5OnBindRequestRemoteFacingServerSocketBindHostFrom(rule, settings));
    }

    @Test
    public void testGetSocks5OnBindRequestRemoteFacingServerSocketBindHostFromRuleSettings17() throws UnknownHostException, SocketException {
        Host host = Host.newInstance("127.0.0.1");
        NetworkInterface networkInterface = NetworkInterface.getByInetAddress(
                host.toInetAddress());
        NetInterface netInterface = NetInterface.newInstance(networkInterface);
        Rule rule = new Rule.Builder().build();
        Settings settings = Settings.of(
                GeneralSettingSpecConstants.NET_INTERFACE.newSetting(
                        netInterface),
                GeneralSettingSpecConstants.BIND_HOST_ADDRESS_TYPES.newSetting(HostAddressTypes.of(
                        HostAddressType.HOST_IPV4_ADDRESS)));
        Assert.assertTrue(
                netInterface.getHostAddresses().contains((HostAddress)
                        Socks5ValueDerivationHelper.getSocks5OnBindRequestRemoteFacingServerSocketBindHostFrom(rule, settings)));
    }

    @Test
    public void testGetSocks5OnBindRequestRemoteFacingServerSocketBindHostFromRuleSettings18() throws UnknownHostException, SocketException {
        Host host = Host.newInstance("127.0.0.1");
        NetworkInterface networkInterface = NetworkInterface.getByInetAddress(
                host.toInetAddress());
        NetInterface netInterface = NetInterface.newInstance(networkInterface);
        Rule rule = new Rule.Builder().build();
        Settings settings = Settings.of(
                GeneralSettingSpecConstants.REMOTE_ENDPOINTS_NET_INTERFACE.newSetting(
                        netInterface),
                GeneralSettingSpecConstants.REMOTE_ENDPOINTS_BIND_HOST_ADDRESS_TYPES.newSetting(HostAddressTypes.of(
                        HostAddressType.HOST_IPV4_ADDRESS)));
        Assert.assertTrue(
                netInterface.getHostAddresses().contains((HostAddress)
                        Socks5ValueDerivationHelper.getSocks5OnBindRequestRemoteFacingServerSocketBindHostFrom(rule, settings)));
    }

    @Test
    public void testGetSocks5OnBindRequestRemoteFacingServerSocketBindHostFromRuleSettings19() throws UnknownHostException, SocketException {
        Host host = Host.newInstance("127.0.0.1");
        NetworkInterface networkInterface = NetworkInterface.getByInetAddress(
                host.toInetAddress());
        NetInterface netInterface = NetInterface.newInstance(networkInterface);
        Rule rule = new Rule.Builder().build();
        Settings settings = Settings.of(
                SocksSettingSpecConstants.SOCKS_ON_REQUEST_REMOTE_ENDPOINTS_NET_INTERFACE.newSetting(
                        netInterface),
                SocksSettingSpecConstants.SOCKS_ON_REQUEST_REMOTE_ENDPOINTS_BIND_HOST_ADDRESS_TYPES.newSetting(HostAddressTypes.of(
                        HostAddressType.HOST_IPV4_ADDRESS)));
        Assert.assertTrue(
                netInterface.getHostAddresses().contains((HostAddress)
                        Socks5ValueDerivationHelper.getSocks5OnBindRequestRemoteFacingServerSocketBindHostFrom(rule, settings)));
    }

    @Test
    public void testGetSocks5OnBindRequestRemoteFacingServerSocketBindHostFromRuleSettings20() throws UnknownHostException, SocketException {
        Host host = Host.newInstance("127.0.0.1");
        NetworkInterface networkInterface = NetworkInterface.getByInetAddress(
                host.toInetAddress());
        NetInterface netInterface = NetInterface.newInstance(networkInterface);
        Rule rule = new Rule.Builder().build();
        Settings settings = Settings.of(
                SocksSettingSpecConstants.SOCKS_ON_BIND_REQUEST_REMOTE_FACING_SERVER_SOCKET_NET_INTERFACE.newSetting(
                        netInterface),
                SocksSettingSpecConstants.SOCKS_ON_BIND_REQUEST_REMOTE_FACING_SERVER_SOCKET_BIND_HOST_ADDRESS_TYPES.newSetting(HostAddressTypes.of(
                        HostAddressType.HOST_IPV4_ADDRESS)));
        Assert.assertTrue(
                netInterface.getHostAddresses().contains((HostAddress)
                        Socks5ValueDerivationHelper.getSocks5OnBindRequestRemoteFacingServerSocketBindHostFrom(rule, settings)));
    }

    @Test
    public void testGetSocks5OnBindRequestRemoteFacingServerSocketBindHostFromRuleSettings21() throws UnknownHostException, SocketException {
        Host host = Host.newInstance("127.0.0.1");
        NetworkInterface networkInterface = NetworkInterface.getByInetAddress(
                host.toInetAddress());
        NetInterface netInterface = NetInterface.newInstance(networkInterface);
        Rule rule = new Rule.Builder().build();
        Settings settings = Settings.of(
                Socks5SettingSpecConstants.SOCKS5_ON_BIND_REQUEST_REMOTE_FACING_SERVER_SOCKET_NET_INTERFACE.newSetting(
                        netInterface),
                Socks5SettingSpecConstants.SOCKS5_ON_BIND_REQUEST_REMOTE_FACING_SERVER_SOCKET_BIND_HOST_ADDRESS_TYPES.newSetting(HostAddressTypes.of(
                        HostAddressType.HOST_IPV4_ADDRESS)));
        Assert.assertTrue(
                netInterface.getHostAddresses().contains((HostAddress)
                        Socks5ValueDerivationHelper.getSocks5OnBindRequestRemoteFacingServerSocketBindHostFrom(rule, settings)));
    }

    @Test
    public void testGetSocks5OnBindRequestRemoteFacingServerSocketBindPortRangesFromRuleSettings01() {
        PortRanges portRanges = PortRanges.getDefault();
        Rule rule = new Rule.Builder().build();
        Settings settings = Settings.of();
        Assert.assertEquals(
                portRanges,
                Socks5ValueDerivationHelper.getSocks5OnBindRequestRemoteFacingServerSocketBindPortRangesFrom(rule, settings));
    }

    @Test
    public void testGetSocks5OnBindRequestRemoteFacingServerSocketBindPortRangesFromRuleSettings02() {
        PortRange portRange = PortRange.of(
                Port.valueOf(1), Port.valueOf(65535));
        PortRanges portRanges = PortRanges.of(portRange);
        Rule rule = new Rule.Builder()
                .addRuleAction(GeneralRuleActionSpecConstants.BIND_TCP_PORT_RANGE.newRuleAction(
                        portRange))
                .build();
        Settings settings = Settings.of();
        Assert.assertEquals(
                portRanges,
                Socks5ValueDerivationHelper.getSocks5OnBindRequestRemoteFacingServerSocketBindPortRangesFrom(rule, settings));
    }

    @Test
    public void testGetSocks5OnBindRequestRemoteFacingServerSocketBindPortRangesFromRuleSettings03() {
        PortRange portRange = PortRange.of(
                Port.valueOf(1), Port.valueOf(65535));
        PortRanges portRanges = PortRanges.of(portRange);
        Rule rule = new Rule.Builder()
                .addRuleAction(GeneralRuleActionSpecConstants.REMOTE_ENDPOINTS_BIND_TCP_PORT_RANGE.newRuleAction(
                        portRange))
                .build();
        Settings settings = Settings.of();
        Assert.assertEquals(
                portRanges,
                Socks5ValueDerivationHelper.getSocks5OnBindRequestRemoteFacingServerSocketBindPortRangesFrom(rule, settings));
    }

    @Test
    public void testGetSocks5OnBindRequestRemoteFacingServerSocketBindPortRangesFromRuleSettings04() {
        PortRange portRange = PortRange.of(
                Port.valueOf(1), Port.valueOf(65535));
        PortRanges portRanges = PortRanges.of(portRange);
        Rule rule = new Rule.Builder()
                .addRuleAction(SocksRuleActionSpecConstants.SOCKS_ON_REQUEST_REMOTE_ENDPOINTS_BIND_TCP_PORT_RANGE.newRuleAction(
                        portRange))
                .build();
        Settings settings = Settings.of();
        Assert.assertEquals(
                portRanges,
                Socks5ValueDerivationHelper.getSocks5OnBindRequestRemoteFacingServerSocketBindPortRangesFrom(rule, settings));
    }

    @Test
    public void testGetSocks5OnBindRequestRemoteFacingServerSocketBindPortRangesFromRuleSettings05() {
        PortRange portRange = PortRange.of(
                Port.valueOf(1), Port.valueOf(65535));
        PortRanges portRanges = PortRanges.of(portRange);
        Rule rule = new Rule.Builder()
                .addRuleAction(SocksRuleActionSpecConstants.SOCKS_ON_BIND_REQUEST_REMOTE_FACING_SERVER_SOCKET_BIND_PORT_RANGE.newRuleAction(
                        portRange))
                .build();
        Settings settings = Settings.of();
        Assert.assertEquals(
                portRanges,
                Socks5ValueDerivationHelper.getSocks5OnBindRequestRemoteFacingServerSocketBindPortRangesFrom(rule, settings));
    }

    @Test
    public void testGetSocks5OnBindRequestRemoteFacingServerSocketBindPortRangesFromRuleSettings06() {
        PortRange portRange = PortRange.of(
                Port.valueOf(1), Port.valueOf(65535));
        PortRanges portRanges = PortRanges.of(portRange);
        Rule rule = new Rule.Builder()
                .addRuleAction(Socks5RuleActionSpecConstants.SOCKS5_ON_BIND_REQUEST_REMOTE_FACING_SERVER_SOCKET_BIND_PORT_RANGE.newRuleAction(
                        portRange))
                .build();
        Settings settings = Settings.of();
        Assert.assertEquals(
                portRanges,
                Socks5ValueDerivationHelper.getSocks5OnBindRequestRemoteFacingServerSocketBindPortRangesFrom(rule, settings));
    }

    @Test
    public void testGetSocks5OnBindRequestRemoteFacingServerSocketBindPortRangesFromRuleSettings07() {
        PortRange portRange = PortRange.of(
                Port.valueOf(1), Port.valueOf(65535));
        PortRanges portRanges = PortRanges.of(portRange);
        Rule rule = new Rule.Builder().build();
        Settings settings = Settings.of(
                GeneralSettingSpecConstants.BIND_TCP_PORT_RANGES.newSetting(
                        portRanges));
        Assert.assertEquals(
                portRanges,
                Socks5ValueDerivationHelper.getSocks5OnBindRequestRemoteFacingServerSocketBindPortRangesFrom(rule, settings));
    }

    @Test
    public void testGetSocks5OnBindRequestRemoteFacingServerSocketBindPortRangesFromRuleSettings08() {
        PortRange portRange = PortRange.of(
                Port.valueOf(1), Port.valueOf(65535));
        PortRanges portRanges = PortRanges.of(portRange);
        Rule rule = new Rule.Builder().build();
        Settings settings = Settings.of(
                GeneralSettingSpecConstants.REMOTE_ENDPOINTS_BIND_TCP_PORT_RANGES.newSetting(
                        portRanges));
        Assert.assertEquals(
                portRanges,
                Socks5ValueDerivationHelper.getSocks5OnBindRequestRemoteFacingServerSocketBindPortRangesFrom(rule, settings));
    }

    @Test
    public void testGetSocks5OnBindRequestRemoteFacingServerSocketBindPortRangesFromRuleSettings09() {
        PortRange portRange = PortRange.of(
                Port.valueOf(1), Port.valueOf(65535));
        PortRanges portRanges = PortRanges.of(portRange);
        Rule rule = new Rule.Builder().build();
        Settings settings = Settings.of(
                SocksSettingSpecConstants.SOCKS_ON_REQUEST_REMOTE_ENDPOINTS_BIND_TCP_PORT_RANGES.newSetting(
                        portRanges));
        Assert.assertEquals(
                portRanges,
                Socks5ValueDerivationHelper.getSocks5OnBindRequestRemoteFacingServerSocketBindPortRangesFrom(rule, settings));
    }

    @Test
    public void testGetSocks5OnBindRequestRemoteFacingServerSocketBindPortRangesFromRuleSettings10() {
        PortRange portRange = PortRange.of(
                Port.valueOf(1), Port.valueOf(65535));
        PortRanges portRanges = PortRanges.of(portRange);
        Rule rule = new Rule.Builder().build();
        Settings settings = Settings.of(
                SocksSettingSpecConstants.SOCKS_ON_BIND_REQUEST_REMOTE_FACING_SERVER_SOCKET_BIND_PORT_RANGES.newSetting(
                        portRanges));
        Assert.assertEquals(
                portRanges,
                Socks5ValueDerivationHelper.getSocks5OnBindRequestRemoteFacingServerSocketBindPortRangesFrom(rule, settings));
    }

    @Test
    public void testGetSocks5OnBindRequestRemoteFacingServerSocketBindPortRangesFromRuleSettings11() {
        PortRange portRange = PortRange.of(
                Port.valueOf(1), Port.valueOf(65535));
        PortRanges portRanges = PortRanges.of(portRange);
        Rule rule = new Rule.Builder().build();
        Settings settings = Settings.of(
                Socks5SettingSpecConstants.SOCKS5_ON_BIND_REQUEST_REMOTE_FACING_SERVER_SOCKET_BIND_PORT_RANGES.newSetting(
                        portRanges));
        Assert.assertEquals(
                portRanges,
                Socks5ValueDerivationHelper.getSocks5OnBindRequestRemoteFacingServerSocketBindPortRangesFrom(rule, settings));
    }

    @Test
    public void testGetSocks5OnBindRequestRemoteFacingServerSocketSocketSettingsFromRuleSettings01() {
        SocketSettings socketSettings = SocketSettings.of();
        Rule rule = new Rule.Builder().build();
        Settings settings = Settings.of();
        Assert.assertEquals(
                socketSettings,
                Socks5ValueDerivationHelper.getSocks5OnBindRequestRemoteFacingServerSocketSocketSettingsFrom(rule, settings));
    }

    @Test
    public void testGetSocks5OnBindRequestRemoteFacingServerSocketSocketSettingsFromRuleSettings02() {
        SocketSetting<?> socketSetting =
                StandardSocketSettingSpecConstants.SO_TIMEOUT.newSocketSetting(
                        NonNegativeInteger.valueOf(0));
        @SuppressWarnings("unchecked")
        SocketSetting<Object> socketSttng = (SocketSetting<Object>) socketSetting;
        SocketSettings socketSettings = SocketSettings.of(socketSttng);
        Rule rule = new Rule.Builder()
                .addRuleAction(GeneralRuleActionSpecConstants.SOCKET_SETTING.newRuleAction(
                        socketSttng))
                .build();
        Settings settings = Settings.of();
        Assert.assertEquals(
                socketSettings,
                Socks5ValueDerivationHelper.getSocks5OnBindRequestRemoteFacingServerSocketSocketSettingsFrom(rule, settings));
    }

    @Test
    public void testGetSocks5OnBindRequestRemoteFacingServerSocketSocketSettingsFromRuleSettings03() {
        SocketSetting<?> socketSetting =
                StandardSocketSettingSpecConstants.SO_TIMEOUT.newSocketSetting(
                        NonNegativeInteger.valueOf(0));
        @SuppressWarnings("unchecked")
        SocketSetting<Object> socketSttng = (SocketSetting<Object>) socketSetting;
        SocketSettings socketSettings = SocketSettings.of(socketSttng);
        Rule rule = new Rule.Builder()
                .addRuleAction(GeneralRuleActionSpecConstants.REMOTE_ENDPOINTS_SOCKET_SETTING.newRuleAction(
                        socketSttng))
                .build();
        Settings settings = Settings.of();
        Assert.assertEquals(
                socketSettings,
                Socks5ValueDerivationHelper.getSocks5OnBindRequestRemoteFacingServerSocketSocketSettingsFrom(rule, settings));
    }

    @Test
    public void testGetSocks5OnBindRequestRemoteFacingServerSocketSocketSettingsFromRuleSettings04() {
        SocketSetting<?> socketSetting =
                StandardSocketSettingSpecConstants.SO_TIMEOUT.newSocketSetting(
                        NonNegativeInteger.valueOf(0));
        @SuppressWarnings("unchecked")
        SocketSetting<Object> socketSttng = (SocketSetting<Object>) socketSetting;
        SocketSettings socketSettings = SocketSettings.of(socketSttng);
        Rule rule = new Rule.Builder()
                .addRuleAction(SocksRuleActionSpecConstants.SOCKS_ON_REQUEST_REMOTE_ENDPOINTS_SOCKET_SETTING.newRuleAction(
                        socketSttng))
                .build();
        Settings settings = Settings.of();
        Assert.assertEquals(
                socketSettings,
                Socks5ValueDerivationHelper.getSocks5OnBindRequestRemoteFacingServerSocketSocketSettingsFrom(rule, settings));
    }

    @Test
    public void testGetSocks5OnBindRequestRemoteFacingServerSocketSocketSettingsFromRuleSettings05() {
        SocketSetting<?> socketSetting =
                StandardSocketSettingSpecConstants.SO_TIMEOUT.newSocketSetting(
                        NonNegativeInteger.valueOf(0));
        @SuppressWarnings("unchecked")
        SocketSetting<Object> socketSttng = (SocketSetting<Object>) socketSetting;
        SocketSettings socketSettings = SocketSettings.of(socketSttng);
        Rule rule = new Rule.Builder()
                .addRuleAction(SocksRuleActionSpecConstants.SOCKS_ON_BIND_REQUEST_REMOTE_FACING_SERVER_SOCKET_SOCKET_SETTING.newRuleAction(
                        socketSttng))
                .build();
        Settings settings = Settings.of();
        Assert.assertEquals(
                socketSettings,
                Socks5ValueDerivationHelper.getSocks5OnBindRequestRemoteFacingServerSocketSocketSettingsFrom(rule, settings));
    }

    @Test
    public void testGetSocks5OnBindRequestRemoteFacingServerSocketSocketSettingsFromRuleSettings06() {
        SocketSetting<?> socketSetting =
                StandardSocketSettingSpecConstants.SO_TIMEOUT.newSocketSetting(
                        NonNegativeInteger.valueOf(0));
        @SuppressWarnings("unchecked")
        SocketSetting<Object> socketSttng = (SocketSetting<Object>) socketSetting;
        SocketSettings socketSettings = SocketSettings.of(socketSttng);
        Rule rule = new Rule.Builder()
                .addRuleAction(Socks5RuleActionSpecConstants.SOCKS5_ON_BIND_REQUEST_REMOTE_FACING_SERVER_SOCKET_SOCKET_SETTING.newRuleAction(
                        socketSttng))
                .build();
        Settings settings = Settings.of();
        Assert.assertEquals(
                socketSettings,
                Socks5ValueDerivationHelper.getSocks5OnBindRequestRemoteFacingServerSocketSocketSettingsFrom(rule, settings));
    }

    @Test
    public void testGetSocks5OnBindRequestRemoteFacingServerSocketSocketSettingsFromRuleSettings07() {
        SocketSetting<?> socketSetting =
                StandardSocketSettingSpecConstants.SO_TIMEOUT.newSocketSetting(
                        NonNegativeInteger.valueOf(0));
        SocketSettings socketSettings = SocketSettings.of(socketSetting);
        Rule rule = new Rule.Builder().build();
        Settings settings = Settings.of(
                GeneralSettingSpecConstants.SOCKET_SETTINGS.newSetting(
                        socketSettings));
        Assert.assertEquals(
                socketSettings,
                Socks5ValueDerivationHelper.getSocks5OnBindRequestRemoteFacingServerSocketSocketSettingsFrom(rule, settings));
    }

    @Test
    public void testGetSocks5OnBindRequestRemoteFacingServerSocketSocketSettingsFromRuleSettings08() {
        SocketSetting<?> socketSetting =
                StandardSocketSettingSpecConstants.SO_TIMEOUT.newSocketSetting(
                        NonNegativeInteger.valueOf(0));
        SocketSettings socketSettings = SocketSettings.of(socketSetting);
        Rule rule = new Rule.Builder().build();
        Settings settings = Settings.of(
                GeneralSettingSpecConstants.REMOTE_ENDPOINTS_SOCKET_SETTINGS.newSetting(
                        socketSettings));
        Assert.assertEquals(
                socketSettings,
                Socks5ValueDerivationHelper.getSocks5OnBindRequestRemoteFacingServerSocketSocketSettingsFrom(rule, settings));
    }

    @Test
    public void testGetSocks5OnBindRequestRemoteFacingServerSocketSocketSettingsFromRuleSettings09() {
        SocketSetting<?> socketSetting =
                StandardSocketSettingSpecConstants.SO_TIMEOUT.newSocketSetting(
                        NonNegativeInteger.valueOf(0));
        SocketSettings socketSettings = SocketSettings.of(socketSetting);
        Rule rule = new Rule.Builder().build();
        Settings settings = Settings.of(
                SocksSettingSpecConstants.SOCKS_ON_REQUEST_REMOTE_ENDPOINTS_SOCKET_SETTINGS.newSetting(
                        socketSettings));
        Assert.assertEquals(
                socketSettings,
                Socks5ValueDerivationHelper.getSocks5OnBindRequestRemoteFacingServerSocketSocketSettingsFrom(rule, settings));
    }

    @Test
    public void testGetSocks5OnBindRequestRemoteFacingServerSocketSocketSettingsFromRuleSettings10() {
        SocketSetting<?> socketSetting =
                StandardSocketSettingSpecConstants.SO_TIMEOUT.newSocketSetting(
                        NonNegativeInteger.valueOf(0));
        SocketSettings socketSettings = SocketSettings.of(socketSetting);
        Rule rule = new Rule.Builder().build();
        Settings settings = Settings.of(
                SocksSettingSpecConstants.SOCKS_ON_BIND_REQUEST_REMOTE_FACING_SERVER_SOCKET_SOCKET_SETTINGS.newSetting(
                        socketSettings));
        Assert.assertEquals(
                socketSettings,
                Socks5ValueDerivationHelper.getSocks5OnBindRequestRemoteFacingServerSocketSocketSettingsFrom(rule, settings));
    }

    @Test
    public void testGetSocks5OnBindRequestRemoteFacingServerSocketSocketSettingsFromRuleSettings11() {
        SocketSetting<?> socketSetting =
                StandardSocketSettingSpecConstants.SO_TIMEOUT.newSocketSetting(
                        NonNegativeInteger.valueOf(0));
        SocketSettings socketSettings = SocketSettings.of(socketSetting);
        Rule rule = new Rule.Builder().build();
        Settings settings = Settings.of(
                Socks5SettingSpecConstants.SOCKS5_ON_BIND_REQUEST_REMOTE_FACING_SERVER_SOCKET_SOCKET_SETTINGS.newSetting(
                        socketSettings));
        Assert.assertEquals(
                socketSettings,
                Socks5ValueDerivationHelper.getSocks5OnBindRequestRemoteFacingServerSocketSocketSettingsFrom(rule, settings));
    }

    @Test
    public void testGetSocks5OnBindRequestRemoteSocketSocketSettingsFromRuleSettings01() {
        SocketSettings socketSettings = SocketSettings.of();
        Rule rule = new Rule.Builder().build();
        Settings settings = Settings.of();
        Assert.assertEquals(
                socketSettings,
                Socks5ValueDerivationHelper.getSocks5OnBindRequestRemoteSocketSocketSettingsFrom(rule, settings));
    }

    @Test
    public void testGetSocks5OnBindRequestRemoteSocketSocketSettingsFromRuleSettings02() {
        SocketSetting<?> socketSetting =
                StandardSocketSettingSpecConstants.SO_TIMEOUT.newSocketSetting(
                        NonNegativeInteger.valueOf(0));
        @SuppressWarnings("unchecked")
        SocketSetting<Object> socketSttng = (SocketSetting<Object>) socketSetting;
        SocketSettings socketSettings = SocketSettings.of(socketSttng);
        Rule rule = new Rule.Builder()
                .addRuleAction(GeneralRuleActionSpecConstants.SOCKET_SETTING.newRuleAction(
                        socketSttng))
                .build();
        Settings settings = Settings.of();
        Assert.assertEquals(
                socketSettings,
                Socks5ValueDerivationHelper.getSocks5OnBindRequestRemoteSocketSocketSettingsFrom(rule, settings));
    }

    @Test
    public void testGetSocks5OnBindRequestRemoteSocketSocketSettingsFromRuleSettings03() {
        SocketSetting<?> socketSetting =
                StandardSocketSettingSpecConstants.SO_TIMEOUT.newSocketSetting(
                        NonNegativeInteger.valueOf(0));
        @SuppressWarnings("unchecked")
        SocketSetting<Object> socketSttng = (SocketSetting<Object>) socketSetting;
        SocketSettings socketSettings = SocketSettings.of(socketSttng);
        Rule rule = new Rule.Builder()
                .addRuleAction(GeneralRuleActionSpecConstants.REMOTE_ENDPOINTS_SOCKET_SETTING.newRuleAction(
                        socketSttng))
                .build();
        Settings settings = Settings.of();
        Assert.assertEquals(
                socketSettings,
                Socks5ValueDerivationHelper.getSocks5OnBindRequestRemoteSocketSocketSettingsFrom(rule, settings));
    }

    @Test
    public void testGetSocks5OnBindRequestRemoteSocketSocketSettingsFromRuleSettings04() {
        SocketSetting<?> socketSetting =
                StandardSocketSettingSpecConstants.SO_TIMEOUT.newSocketSetting(
                        NonNegativeInteger.valueOf(0));
        @SuppressWarnings("unchecked")
        SocketSetting<Object> socketSttng = (SocketSetting<Object>) socketSetting;
        SocketSettings socketSettings = SocketSettings.of(socketSttng);
        Rule rule = new Rule.Builder()
                .addRuleAction(SocksRuleActionSpecConstants.SOCKS_ON_REQUEST_REMOTE_ENDPOINTS_SOCKET_SETTING.newRuleAction(
                        socketSttng))
                .build();
        Settings settings = Settings.of();
        Assert.assertEquals(
                socketSettings,
                Socks5ValueDerivationHelper.getSocks5OnBindRequestRemoteSocketSocketSettingsFrom(rule, settings));
    }

    @Test
    public void testGetSocks5OnBindRequestRemoteSocketSocketSettingsFromRuleSettings05() {
        SocketSetting<?> socketSetting =
                StandardSocketSettingSpecConstants.SO_TIMEOUT.newSocketSetting(
                        NonNegativeInteger.valueOf(0));
        @SuppressWarnings("unchecked")
        SocketSetting<Object> socketSttng = (SocketSetting<Object>) socketSetting;
        SocketSettings socketSettings = SocketSettings.of(socketSttng);
        Rule rule = new Rule.Builder()
                .addRuleAction(SocksRuleActionSpecConstants.SOCKS_ON_BIND_REQUEST_REMOTE_SOCKET_SOCKET_SETTING.newRuleAction(
                        socketSttng))
                .build();
        Settings settings = Settings.of();
        Assert.assertEquals(
                socketSettings,
                Socks5ValueDerivationHelper.getSocks5OnBindRequestRemoteSocketSocketSettingsFrom(rule, settings));
    }

    @Test
    public void testGetSocks5OnBindRequestRemoteSocketSocketSettingsFromRuleSettings06() {
        SocketSetting<?> socketSetting =
                StandardSocketSettingSpecConstants.SO_TIMEOUT.newSocketSetting(
                        NonNegativeInteger.valueOf(0));
        @SuppressWarnings("unchecked")
        SocketSetting<Object> socketSttng = (SocketSetting<Object>) socketSetting;
        SocketSettings socketSettings = SocketSettings.of(socketSttng);
        Rule rule = new Rule.Builder()
                .addRuleAction(Socks5RuleActionSpecConstants.SOCKS5_ON_BIND_REQUEST_REMOTE_SOCKET_SOCKET_SETTING.newRuleAction(
                        socketSttng))
                .build();
        Settings settings = Settings.of();
        Assert.assertEquals(
                socketSettings,
                Socks5ValueDerivationHelper.getSocks5OnBindRequestRemoteSocketSocketSettingsFrom(rule, settings));
    }

    @Test
    public void testGetSocks5OnBindRequestRemoteSocketSocketSettingsFromRuleSettings07() {
        SocketSetting<?> socketSetting =
                StandardSocketSettingSpecConstants.SO_TIMEOUT.newSocketSetting(
                        NonNegativeInteger.valueOf(0));
        SocketSettings socketSettings = SocketSettings.of(socketSetting);
        Rule rule = new Rule.Builder().build();
        Settings settings = Settings.of(
                GeneralSettingSpecConstants.SOCKET_SETTINGS.newSetting(
                        socketSettings));
        Assert.assertEquals(
                socketSettings,
                Socks5ValueDerivationHelper.getSocks5OnBindRequestRemoteSocketSocketSettingsFrom(rule, settings));
    }

    @Test
    public void testGetSocks5OnBindRequestRemoteSocketSocketSettingsFromRuleSettings08() {
        SocketSetting<?> socketSetting =
                StandardSocketSettingSpecConstants.SO_TIMEOUT.newSocketSetting(
                        NonNegativeInteger.valueOf(0));
        SocketSettings socketSettings = SocketSettings.of(socketSetting);
        Rule rule = new Rule.Builder().build();
        Settings settings = Settings.of(
                GeneralSettingSpecConstants.REMOTE_ENDPOINTS_SOCKET_SETTINGS.newSetting(
                        socketSettings));
        Assert.assertEquals(
                socketSettings,
                Socks5ValueDerivationHelper.getSocks5OnBindRequestRemoteSocketSocketSettingsFrom(rule, settings));
    }

    @Test
    public void testGetSocks5OnBindRequestRemoteSocketSocketSettingsFromRuleSettings09() {
        SocketSetting<?> socketSetting =
                StandardSocketSettingSpecConstants.SO_TIMEOUT.newSocketSetting(
                        NonNegativeInteger.valueOf(0));
        SocketSettings socketSettings = SocketSettings.of(socketSetting);
        Rule rule = new Rule.Builder().build();
        Settings settings = Settings.of(
                SocksSettingSpecConstants.SOCKS_ON_REQUEST_REMOTE_ENDPOINTS_SOCKET_SETTINGS.newSetting(
                        socketSettings));
        Assert.assertEquals(
                socketSettings,
                Socks5ValueDerivationHelper.getSocks5OnBindRequestRemoteSocketSocketSettingsFrom(rule, settings));
    }

    @Test
    public void testGetSocks5OnBindRequestRemoteSocketSocketSettingsFromRuleSettings10() {
        SocketSetting<?> socketSetting =
                StandardSocketSettingSpecConstants.SO_TIMEOUT.newSocketSetting(
                        NonNegativeInteger.valueOf(0));
        SocketSettings socketSettings = SocketSettings.of(socketSetting);
        Rule rule = new Rule.Builder().build();
        Settings settings = Settings.of(
                SocksSettingSpecConstants.SOCKS_ON_BIND_REQUEST_REMOTE_SOCKET_SOCKET_SETTINGS.newSetting(
                        socketSettings));
        Assert.assertEquals(
                socketSettings,
                Socks5ValueDerivationHelper.getSocks5OnBindRequestRemoteSocketSocketSettingsFrom(rule, settings));
    }

    @Test
    public void testGetSocks5OnBindRequestRemoteSocketSocketSettingsFromRuleSettings11() {
        SocketSetting<?> socketSetting =
                StandardSocketSettingSpecConstants.SO_TIMEOUT.newSocketSetting(
                        NonNegativeInteger.valueOf(0));
        SocketSettings socketSettings = SocketSettings.of(socketSetting);
        Rule rule = new Rule.Builder().build();
        Settings settings = Settings.of(
                Socks5SettingSpecConstants.SOCKS5_ON_BIND_REQUEST_REMOTE_SOCKET_SOCKET_SETTINGS.newSetting(
                        socketSettings));
        Assert.assertEquals(
                socketSettings,
                Socks5ValueDerivationHelper.getSocks5OnBindRequestRemoteSocketSocketSettingsFrom(rule, settings));
    }

    @Test
    public void testGetSocks5OnConnectRequestRelayBufferSizeFromRuleSettings01() {
        PositiveInteger relayBufferSize = PositiveInteger.valueOf(1024);
        Rule rule = new Rule.Builder().build();
        Settings settings = Settings.of();
        Assert.assertEquals(
                relayBufferSize,
                Socks5ValueDerivationHelper.getSocks5OnConnectRequestRelayBufferSizeFrom(rule, settings));
    }

    @Test
    public void testGetSocks5OnConnectRequestRelayBufferSizeFromRuleSettings02() {
        PositiveInteger relayBufferSize = PositiveInteger.valueOf(2048);
        Rule rule = new Rule.Builder()
                .addRuleAction(SocksRuleActionSpecConstants.SOCKS_ON_REQUEST_RELAY_BUFFER_SIZE.newRuleAction(
                        relayBufferSize))
                .build();
        Settings settings = Settings.of();
        Assert.assertEquals(
                relayBufferSize,
                Socks5ValueDerivationHelper.getSocks5OnConnectRequestRelayBufferSizeFrom(rule, settings));
    }

    @Test
    public void testGetSocks5OnConnectRequestRelayBufferSizeFromRuleSettings03() {
        PositiveInteger relayBufferSize = PositiveInteger.valueOf(2048);
        Rule rule = new Rule.Builder()
                .addRuleAction(SocksRuleActionSpecConstants.SOCKS_ON_CONNECT_REQUEST_RELAY_BUFFER_SIZE.newRuleAction(
                        relayBufferSize))
                .build();
        Settings settings = Settings.of();
        Assert.assertEquals(
                relayBufferSize,
                Socks5ValueDerivationHelper.getSocks5OnConnectRequestRelayBufferSizeFrom(rule, settings));
    }

    @Test
    public void testGetSocks5OnConnectRequestRelayBufferSizeFromRuleSettings04() {
        PositiveInteger relayBufferSize = PositiveInteger.valueOf(2048);
        Rule rule = new Rule.Builder()
                .addRuleAction(Socks5RuleActionSpecConstants.SOCKS5_ON_CONNECT_REQUEST_RELAY_BUFFER_SIZE.newRuleAction(
                        relayBufferSize))
                .build();
        Settings settings = Settings.of();
        Assert.assertEquals(
                relayBufferSize,
                Socks5ValueDerivationHelper.getSocks5OnConnectRequestRelayBufferSizeFrom(rule, settings));
    }

    @Test
    public void testGetSocks5OnConnectRequestRelayBufferSizeFromRuleSettings05() {
        PositiveInteger relayBufferSize = PositiveInteger.valueOf(2048);
        Rule rule = new Rule.Builder().build();
        Settings settings = Settings.of(
                SocksSettingSpecConstants.SOCKS_ON_REQUEST_RELAY_BUFFER_SIZE.newSetting(
                        relayBufferSize));
        Assert.assertEquals(
                relayBufferSize,
                Socks5ValueDerivationHelper.getSocks5OnConnectRequestRelayBufferSizeFrom(rule, settings));
    }

    @Test
    public void testGetSocks5OnConnectRequestRelayBufferSizeFromRuleSettings06() {
        PositiveInteger relayBufferSize = PositiveInteger.valueOf(2048);
        Rule rule = new Rule.Builder().build();
        Settings settings = Settings.of(
                SocksSettingSpecConstants.SOCKS_ON_CONNECT_REQUEST_RELAY_BUFFER_SIZE.newSetting(
                        relayBufferSize));
        Assert.assertEquals(
                relayBufferSize,
                Socks5ValueDerivationHelper.getSocks5OnConnectRequestRelayBufferSizeFrom(rule, settings));
    }

    @Test
    public void testGetSocks5OnConnectRequestRelayBufferSizeFromRuleSettings07() {
        PositiveInteger relayBufferSize = PositiveInteger.valueOf(2048);
        Rule rule = new Rule.Builder().build();
        Settings settings = Settings.of(
                Socks5SettingSpecConstants.SOCKS5_ON_CONNECT_REQUEST_RELAY_BUFFER_SIZE.newSetting(
                        relayBufferSize));
        Assert.assertEquals(
                relayBufferSize,
                Socks5ValueDerivationHelper.getSocks5OnConnectRequestRelayBufferSizeFrom(rule, settings));
    }

    @Test
    public void testGetSocks5OnConnectRequestRelayIdleTimeoutFromRuleSettings01() {
        PositiveInteger relayIdleTimeout = PositiveInteger.valueOf(60000);
        Rule rule = new Rule.Builder().build();
        Settings settings = Settings.of();
        Assert.assertEquals(
                relayIdleTimeout,
                Socks5ValueDerivationHelper.getSocks5OnConnectRequestRelayIdleTimeoutFrom(rule, settings));
    }

    @Test
    public void testGetSocks5OnConnectRequestRelayIdleTimeoutFromRuleSettings02() {
        PositiveInteger relayIdleTimeout = PositiveInteger.valueOf(120000);
        Rule rule = new Rule.Builder()
                .addRuleAction(SocksRuleActionSpecConstants.SOCKS_ON_REQUEST_RELAY_IDLE_TIMEOUT.newRuleAction(
                        relayIdleTimeout))
                .build();
        Settings settings = Settings.of();
        Assert.assertEquals(
                relayIdleTimeout,
                Socks5ValueDerivationHelper.getSocks5OnConnectRequestRelayIdleTimeoutFrom(rule, settings));
    }

    @Test
    public void testGetSocks5OnConnectRequestRelayIdleTimeoutFromRuleSettings03() {
        PositiveInteger relayIdleTimeout = PositiveInteger.valueOf(120000);
        Rule rule = new Rule.Builder()
                .addRuleAction(SocksRuleActionSpecConstants.SOCKS_ON_CONNECT_REQUEST_RELAY_IDLE_TIMEOUT.newRuleAction(
                        relayIdleTimeout))
                .build();
        Settings settings = Settings.of();
        Assert.assertEquals(
                relayIdleTimeout,
                Socks5ValueDerivationHelper.getSocks5OnConnectRequestRelayIdleTimeoutFrom(rule, settings));
    }

    @Test
    public void testGetSocks5OnConnectRequestRelayIdleTimeoutFromRuleSettings04() {
        PositiveInteger relayIdleTimeout = PositiveInteger.valueOf(120000);
        Rule rule = new Rule.Builder()
                .addRuleAction(Socks5RuleActionSpecConstants.SOCKS5_ON_CONNECT_REQUEST_RELAY_IDLE_TIMEOUT.newRuleAction(
                        relayIdleTimeout))
                .build();
        Settings settings = Settings.of();
        Assert.assertEquals(
                relayIdleTimeout,
                Socks5ValueDerivationHelper.getSocks5OnConnectRequestRelayIdleTimeoutFrom(rule, settings));
    }

    @Test
    public void testGetSocks5OnConnectRequestRelayIdleTimeoutFromRuleSettings05() {
        PositiveInteger relayIdleTimeout = PositiveInteger.valueOf(120000);
        Rule rule = new Rule.Builder().build();
        Settings settings = Settings.of(
                SocksSettingSpecConstants.SOCKS_ON_REQUEST_RELAY_IDLE_TIMEOUT.newSetting(
                        relayIdleTimeout));
        Assert.assertEquals(
                relayIdleTimeout,
                Socks5ValueDerivationHelper.getSocks5OnConnectRequestRelayIdleTimeoutFrom(rule, settings));
    }

    @Test
    public void testGetSocks5OnConnectRequestRelayIdleTimeoutFromRuleSettings06() {
        PositiveInteger relayIdleTimeout = PositiveInteger.valueOf(120000);
        Rule rule = new Rule.Builder().build();
        Settings settings = Settings.of(
                SocksSettingSpecConstants.SOCKS_ON_CONNECT_REQUEST_RELAY_IDLE_TIMEOUT.newSetting(
                        relayIdleTimeout));
        Assert.assertEquals(
                relayIdleTimeout,
                Socks5ValueDerivationHelper.getSocks5OnConnectRequestRelayIdleTimeoutFrom(rule, settings));
    }

    @Test
    public void testGetSocks5OnConnectRequestRelayIdleTimeoutFromRuleSettings07() {
        PositiveInteger relayIdleTimeout = PositiveInteger.valueOf(120000);
        Rule rule = new Rule.Builder().build();
        Settings settings = Settings.of(
                Socks5SettingSpecConstants.SOCKS5_ON_CONNECT_REQUEST_RELAY_IDLE_TIMEOUT.newSetting(
                        relayIdleTimeout));
        Assert.assertEquals(
                relayIdleTimeout,
                Socks5ValueDerivationHelper.getSocks5OnConnectRequestRelayIdleTimeoutFrom(rule, settings));
    }

    @Test
    public void testGetSocks5OnConnectRequestRelayInboundBandwidthLimitFromRuleSettings01() {
        Rule rule = new Rule.Builder().build();
        Settings settings = Settings.of();
        Assert.assertNull(
                Socks5ValueDerivationHelper.getSocks5OnConnectRequestRelayInboundBandwidthLimitFrom(rule, settings));
    }

    @Test
    public void testGetSocks5OnConnectRequestRelayInboundBandwidthLimitFromRuleSettings02() {
        PositiveInteger relayInboundBandwidthLimit = PositiveInteger.valueOf(
                Integer.MAX_VALUE);
        Rule rule = new Rule.Builder()
                .addRuleAction(SocksRuleActionSpecConstants.SOCKS_ON_REQUEST_RELAY_INBOUND_BANDWIDTH_LIMIT.newRuleAction(
                        relayInboundBandwidthLimit))
                .build();
        Settings settings = Settings.of();
        Assert.assertEquals(
                relayInboundBandwidthLimit,
                Socks5ValueDerivationHelper.getSocks5OnConnectRequestRelayInboundBandwidthLimitFrom(rule, settings));
    }

    @Test
    public void testGetSocks5OnConnectRequestRelayInboundBandwidthLimitFromRuleSettings03() {
        PositiveInteger relayInboundBandwidthLimit = PositiveInteger.valueOf(
                Integer.MAX_VALUE);
        Rule rule = new Rule.Builder()
                .addRuleAction(SocksRuleActionSpecConstants.SOCKS_ON_CONNECT_REQUEST_RELAY_INBOUND_BANDWIDTH_LIMIT.newRuleAction(
                        relayInboundBandwidthLimit))
                .build();
        Settings settings = Settings.of();
        Assert.assertEquals(
                relayInboundBandwidthLimit,
                Socks5ValueDerivationHelper.getSocks5OnConnectRequestRelayInboundBandwidthLimitFrom(rule, settings));
    }

    @Test
    public void testGetSocks5OnConnectRequestRelayInboundBandwidthLimitFromRuleSettings04() {
        PositiveInteger relayInboundBandwidthLimit = PositiveInteger.valueOf(
                Integer.MAX_VALUE);
        Rule rule = new Rule.Builder()
                .addRuleAction(Socks5RuleActionSpecConstants.SOCKS5_ON_CONNECT_REQUEST_RELAY_INBOUND_BANDWIDTH_LIMIT.newRuleAction(
                        relayInboundBandwidthLimit))
                .build();
        Settings settings = Settings.of();
        Assert.assertEquals(
                relayInboundBandwidthLimit,
                Socks5ValueDerivationHelper.getSocks5OnConnectRequestRelayInboundBandwidthLimitFrom(rule, settings));
    }

    @Test
    public void testGetSocks5OnConnectRequestRelayInboundBandwidthLimitFromRuleSettings05() {
        PositiveInteger relayInboundBandwidthLimit = PositiveInteger.valueOf(
                Integer.MAX_VALUE);
        Rule rule = new Rule.Builder().build();
        Settings settings = Settings.of(
                SocksSettingSpecConstants.SOCKS_ON_REQUEST_RELAY_INBOUND_BANDWIDTH_LIMIT.newSetting(
                        relayInboundBandwidthLimit));
        Assert.assertEquals(
                relayInboundBandwidthLimit,
                Socks5ValueDerivationHelper.getSocks5OnConnectRequestRelayInboundBandwidthLimitFrom(rule, settings));
    }

    @Test
    public void testGetSocks5OnConnectRequestRelayInboundBandwidthLimitFromRuleSettings06() {
        PositiveInteger relayInboundBandwidthLimit = PositiveInteger.valueOf(
                Integer.MAX_VALUE);
        Rule rule = new Rule.Builder().build();
        Settings settings = Settings.of(
                SocksSettingSpecConstants.SOCKS_ON_CONNECT_REQUEST_RELAY_INBOUND_BANDWIDTH_LIMIT.newSetting(
                        relayInboundBandwidthLimit));
        Assert.assertEquals(
                relayInboundBandwidthLimit,
                Socks5ValueDerivationHelper.getSocks5OnConnectRequestRelayInboundBandwidthLimitFrom(rule, settings));
    }

    @Test
    public void testGetSocks5OnConnectRequestRelayInboundBandwidthLimitFromRuleSettings07() {
        PositiveInteger relayInboundBandwidthLimit = PositiveInteger.valueOf(
                Integer.MAX_VALUE);
        Rule rule = new Rule.Builder().build();
        Settings settings = Settings.of(
                Socks5SettingSpecConstants.SOCKS5_ON_CONNECT_REQUEST_RELAY_INBOUND_BANDWIDTH_LIMIT.newSetting(
                        relayInboundBandwidthLimit));
        Assert.assertEquals(
                relayInboundBandwidthLimit,
                Socks5ValueDerivationHelper.getSocks5OnConnectRequestRelayInboundBandwidthLimitFrom(rule, settings));
    }

    @Test
    public void testGetSocks5OnConnectRequestRelayOutboundBandwidthLimitFromRuleSettings01() {
        Rule rule = new Rule.Builder().build();
        Settings settings = Settings.of();
        Assert.assertNull(
                Socks5ValueDerivationHelper.getSocks5OnConnectRequestRelayOutboundBandwidthLimitFrom(rule, settings));
    }

    @Test
    public void testGetSocks5OnConnectRequestRelayOutboundBandwidthLimitFromRuleSettings02() {
        PositiveInteger relayOutboundBandwidthLimit = PositiveInteger.valueOf(
                Integer.MAX_VALUE);
        Rule rule = new Rule.Builder()
                .addRuleAction(SocksRuleActionSpecConstants.SOCKS_ON_REQUEST_RELAY_OUTBOUND_BANDWIDTH_LIMIT.newRuleAction(
                        relayOutboundBandwidthLimit))
                .build();
        Settings settings = Settings.of();
        Assert.assertEquals(
                relayOutboundBandwidthLimit,
                Socks5ValueDerivationHelper.getSocks5OnConnectRequestRelayOutboundBandwidthLimitFrom(rule, settings));
    }

    @Test
    public void testGetSocks5OnConnectRequestRelayOutboundBandwidthLimitFromRuleSettings03() {
        PositiveInteger relayOutboundBandwidthLimit = PositiveInteger.valueOf(
                Integer.MAX_VALUE);
        Rule rule = new Rule.Builder()
                .addRuleAction(SocksRuleActionSpecConstants.SOCKS_ON_CONNECT_REQUEST_RELAY_OUTBOUND_BANDWIDTH_LIMIT.newRuleAction(
                        relayOutboundBandwidthLimit))
                .build();
        Settings settings = Settings.of();
        Assert.assertEquals(
                relayOutboundBandwidthLimit,
                Socks5ValueDerivationHelper.getSocks5OnConnectRequestRelayOutboundBandwidthLimitFrom(rule, settings));
    }

    @Test
    public void testGetSocks5OnConnectRequestRelayOutboundBandwidthLimitFromRuleSettings04() {
        PositiveInteger relayOutboundBandwidthLimit = PositiveInteger.valueOf(
                Integer.MAX_VALUE);
        Rule rule = new Rule.Builder()
                .addRuleAction(Socks5RuleActionSpecConstants.SOCKS5_ON_CONNECT_REQUEST_RELAY_OUTBOUND_BANDWIDTH_LIMIT.newRuleAction(
                        relayOutboundBandwidthLimit))
                .build();
        Settings settings = Settings.of();
        Assert.assertEquals(
                relayOutboundBandwidthLimit,
                Socks5ValueDerivationHelper.getSocks5OnConnectRequestRelayOutboundBandwidthLimitFrom(rule, settings));
    }

    @Test
    public void testGetSocks5OnConnectRequestRelayOutboundBandwidthLimitFromRuleSettings05() {
        PositiveInteger relayOutboundBandwidthLimit = PositiveInteger.valueOf(
                Integer.MAX_VALUE);
        Rule rule = new Rule.Builder().build();
        Settings settings = Settings.of(
                SocksSettingSpecConstants.SOCKS_ON_REQUEST_RELAY_OUTBOUND_BANDWIDTH_LIMIT.newSetting(
                        relayOutboundBandwidthLimit));
        Assert.assertEquals(
                relayOutboundBandwidthLimit,
                Socks5ValueDerivationHelper.getSocks5OnConnectRequestRelayOutboundBandwidthLimitFrom(rule, settings));
    }

    @Test
    public void testGetSocks5OnConnectRequestRelayOutboundBandwidthLimitFromRuleSettings06() {
        PositiveInteger relayOutboundBandwidthLimit = PositiveInteger.valueOf(
                Integer.MAX_VALUE);
        Rule rule = new Rule.Builder().build();
        Settings settings = Settings.of(
                SocksSettingSpecConstants.SOCKS_ON_CONNECT_REQUEST_RELAY_OUTBOUND_BANDWIDTH_LIMIT.newSetting(
                        relayOutboundBandwidthLimit));
        Assert.assertEquals(
                relayOutboundBandwidthLimit,
                Socks5ValueDerivationHelper.getSocks5OnConnectRequestRelayOutboundBandwidthLimitFrom(rule, settings));
    }

    @Test
    public void testGetSocks5OnConnectRequestRelayOutboundBandwidthLimitFromRuleSettings07() {
        PositiveInteger relayOutboundBandwidthLimit = PositiveInteger.valueOf(
                Integer.MAX_VALUE);
        Rule rule = new Rule.Builder().build();
        Settings settings = Settings.of(
                Socks5SettingSpecConstants.SOCKS5_ON_CONNECT_REQUEST_RELAY_OUTBOUND_BANDWIDTH_LIMIT.newSetting(
                        relayOutboundBandwidthLimit));
        Assert.assertEquals(
                relayOutboundBandwidthLimit,
                Socks5ValueDerivationHelper.getSocks5OnConnectRequestRelayOutboundBandwidthLimitFrom(rule, settings));
    }

    @Test
    public void testGetSocks5OnConnectRequestRemoteSocketBindHostFromRuleSettings01() {
        Host host = HostIpv4Address.getAllZerosInstance();
        Rule rule = new Rule.Builder().build();
        Settings settings = Settings.of();
        Assert.assertEquals(
                host,
                Socks5ValueDerivationHelper.getSocks5OnConnectRequestRemoteSocketBindHostFrom(rule, settings));
    }

    @Test
    public void testGetSocks5OnConnectRequestRemoteSocketBindHostFromRuleSettings02() {
        Host host = Host.newInstance("127.0.0.1");
        Rule rule = new Rule.Builder()
                .addRuleAction(GeneralRuleActionSpecConstants.BIND_HOST.newRuleAction(
                        host))
                .build();
        Settings settings = Settings.of();
        Assert.assertEquals(
                host,
                Socks5ValueDerivationHelper.getSocks5OnConnectRequestRemoteSocketBindHostFrom(rule, settings));
    }

    @Test
    public void testGetSocks5OnConnectRequestRemoteSocketBindHostFromRuleSettings03() {
        Host host = Host.newInstance("127.0.0.1");
        Rule rule = new Rule.Builder()
                .addRuleAction(GeneralRuleActionSpecConstants.REMOTE_ENDPOINTS_BIND_HOST.newRuleAction(
                        host))
                .build();
        Settings settings = Settings.of();
        Assert.assertEquals(
                host,
                Socks5ValueDerivationHelper.getSocks5OnConnectRequestRemoteSocketBindHostFrom(rule, settings));
    }

    @Test
    public void testGetSocks5OnConnectRequestRemoteSocketBindHostFromRuleSettings04() {
        Host host = Host.newInstance("127.0.0.1");
        Rule rule = new Rule.Builder()
                .addRuleAction(SocksRuleActionSpecConstants.SOCKS_ON_REQUEST_REMOTE_ENDPOINTS_BIND_HOST.newRuleAction(
                        host))
                .build();
        Settings settings = Settings.of();
        Assert.assertEquals(
                host,
                Socks5ValueDerivationHelper.getSocks5OnConnectRequestRemoteSocketBindHostFrom(rule, settings));
    }

    @Test
    public void testGetSocks5OnConnectRequestRemoteSocketBindHostFromRuleSettings05() {
        Host host = Host.newInstance("127.0.0.1");
        Rule rule = new Rule.Builder()
                .addRuleAction(SocksRuleActionSpecConstants.SOCKS_ON_CONNECT_REQUEST_REMOTE_SOCKET_BIND_HOST.newRuleAction(
                        host))
                .build();
        Settings settings = Settings.of();
        Assert.assertEquals(
                host,
                Socks5ValueDerivationHelper.getSocks5OnConnectRequestRemoteSocketBindHostFrom(rule, settings));
    }

    @Test
    public void testGetSocks5OnConnectRequestRemoteSocketBindHostFromRuleSettings06() {
        Host host = Host.newInstance("127.0.0.1");
        Rule rule = new Rule.Builder()
                .addRuleAction(Socks5RuleActionSpecConstants.SOCKS5_ON_CONNECT_REQUEST_REMOTE_SOCKET_BIND_HOST.newRuleAction(
                        host))
                .build();
        Settings settings = Settings.of();
        Assert.assertEquals(
                host,
                Socks5ValueDerivationHelper.getSocks5OnConnectRequestRemoteSocketBindHostFrom(rule, settings));
    }

    @Test
    public void testGetSocks5OnConnectRequestRemoteSocketBindHostFromRuleSettings07() throws UnknownHostException, SocketException {
        Host host = Host.newInstance("127.0.0.1");
        NetworkInterface networkInterface = NetworkInterface.getByInetAddress(
                host.toInetAddress());
        NetInterface netInterface = NetInterface.newInstance(networkInterface);
        Rule rule = new Rule.Builder()
                .addRuleAction(GeneralRuleActionSpecConstants.NET_INTERFACE.newRuleAction(
                        netInterface))
                .addRuleAction(GeneralRuleActionSpecConstants.BIND_HOST_ADDRESS_TYPE.newRuleAction(
                        HostAddressType.HOST_IPV4_ADDRESS))
                .build();
        Settings settings = Settings.of();
        Assert.assertTrue(
                netInterface.getHostAddresses().contains((HostAddress)
                        Socks5ValueDerivationHelper.getSocks5OnConnectRequestRemoteSocketBindHostFrom(rule, settings)));
    }

    @Test
    public void testGetSocks5OnConnectRequestRemoteSocketBindHostFromRuleSettings08() throws UnknownHostException, SocketException {
        Host host = Host.newInstance("127.0.0.1");
        NetworkInterface networkInterface = NetworkInterface.getByInetAddress(
                host.toInetAddress());
        NetInterface netInterface = NetInterface.newInstance(networkInterface);
        Rule rule = new Rule.Builder()
                .addRuleAction(GeneralRuleActionSpecConstants.REMOTE_ENDPOINTS_NET_INTERFACE.newRuleAction(
                        netInterface))
                .addRuleAction(GeneralRuleActionSpecConstants.REMOTE_ENDPOINTS_BIND_HOST_ADDRESS_TYPE.newRuleAction(
                        HostAddressType.HOST_IPV4_ADDRESS))
                .build();
        Settings settings = Settings.of();
        Assert.assertTrue(
                netInterface.getHostAddresses().contains((HostAddress)
                        Socks5ValueDerivationHelper.getSocks5OnConnectRequestRemoteSocketBindHostFrom(rule, settings)));
    }

    @Test
    public void testGetSocks5OnConnectRequestRemoteSocketBindHostFromRuleSettings09() throws UnknownHostException, SocketException {
        Host host = Host.newInstance("127.0.0.1");
        NetworkInterface networkInterface = NetworkInterface.getByInetAddress(
                host.toInetAddress());
        NetInterface netInterface = NetInterface.newInstance(networkInterface);
        Rule rule = new Rule.Builder()
                .addRuleAction(SocksRuleActionSpecConstants.SOCKS_ON_REQUEST_REMOTE_ENDPOINTS_NET_INTERFACE.newRuleAction(
                        netInterface))
                .addRuleAction(SocksRuleActionSpecConstants.SOCKS_ON_REQUEST_REMOTE_ENDPOINTS_BIND_HOST_ADDRESS_TYPE.newRuleAction(
                        HostAddressType.HOST_IPV4_ADDRESS))
                .build();
        Settings settings = Settings.of();
        Assert.assertTrue(
                netInterface.getHostAddresses().contains((HostAddress)
                        Socks5ValueDerivationHelper.getSocks5OnConnectRequestRemoteSocketBindHostFrom(rule, settings)));
    }

    @Test
    public void testGetSocks5OnConnectRequestRemoteSocketBindHostFromRuleSettings10() throws UnknownHostException, SocketException {
        Host host = Host.newInstance("127.0.0.1");
        NetworkInterface networkInterface = NetworkInterface.getByInetAddress(
                host.toInetAddress());
        NetInterface netInterface = NetInterface.newInstance(networkInterface);
        Rule rule = new Rule.Builder()
                .addRuleAction(SocksRuleActionSpecConstants.SOCKS_ON_CONNECT_REQUEST_REMOTE_SOCKET_NET_INTERFACE.newRuleAction(
                        netInterface))
                .addRuleAction(SocksRuleActionSpecConstants.SOCKS_ON_CONNECT_REQUEST_REMOTE_SOCKET_BIND_HOST_ADDRESS_TYPE.newRuleAction(
                        HostAddressType.HOST_IPV4_ADDRESS))
                .build();
        Settings settings = Settings.of();
        Assert.assertTrue(
                netInterface.getHostAddresses().contains((HostAddress)
                        Socks5ValueDerivationHelper.getSocks5OnConnectRequestRemoteSocketBindHostFrom(rule, settings)));
    }

    @Test
    public void testGetSocks5OnConnectRequestRemoteSocketBindHostFromRuleSettings11() throws UnknownHostException, SocketException {
        Host host = Host.newInstance("127.0.0.1");
        NetworkInterface networkInterface = NetworkInterface.getByInetAddress(
                host.toInetAddress());
        NetInterface netInterface = NetInterface.newInstance(networkInterface);
        Rule rule = new Rule.Builder()
                .addRuleAction(Socks5RuleActionSpecConstants.SOCKS5_ON_CONNECT_REQUEST_REMOTE_SOCKET_NET_INTERFACE.newRuleAction(
                        netInterface))
                .addRuleAction(Socks5RuleActionSpecConstants.SOCKS5_ON_CONNECT_REQUEST_REMOTE_SOCKET_BIND_HOST_ADDRESS_TYPE.newRuleAction(
                        HostAddressType.HOST_IPV4_ADDRESS))
                .build();
        Settings settings = Settings.of();
        Assert.assertTrue(
                netInterface.getHostAddresses().contains((HostAddress)
                        Socks5ValueDerivationHelper.getSocks5OnConnectRequestRemoteSocketBindHostFrom(rule, settings)));
    }

    @Test
    public void testGetSocks5OnConnectRequestRemoteSocketBindHostFromRuleSettings12() {
        Host host = Host.newInstance("127.0.0.1");
        Rule rule = new Rule.Builder().build();
        Settings settings = Settings.of(
                GeneralSettingSpecConstants.BIND_HOST.newSetting(host));
        Assert.assertEquals(
                host,
                Socks5ValueDerivationHelper.getSocks5OnConnectRequestRemoteSocketBindHostFrom(rule, settings));
    }

    @Test
    public void testGetSocks5OnConnectRequestRemoteSocketBindHostFromRuleSettings13() {
        Host host = Host.newInstance("127.0.0.1");
        Rule rule = new Rule.Builder().build();
        Settings settings = Settings.of(
                GeneralSettingSpecConstants.REMOTE_ENDPOINTS_BIND_HOST.newSetting(
                        host));
        Assert.assertEquals(
                host,
                Socks5ValueDerivationHelper.getSocks5OnConnectRequestRemoteSocketBindHostFrom(rule, settings));
    }

    @Test
    public void testGetSocks5OnConnectRequestRemoteSocketBindHostFromRuleSettings14() {
        Host host = Host.newInstance("127.0.0.1");
        Rule rule = new Rule.Builder().build();
        Settings settings = Settings.of(
                SocksSettingSpecConstants.SOCKS_ON_REQUEST_REMOTE_ENDPOINTS_BIND_HOST.newSetting(
                        host));
        Assert.assertEquals(
                host,
                Socks5ValueDerivationHelper.getSocks5OnConnectRequestRemoteSocketBindHostFrom(rule, settings));
    }

    @Test
    public void testGetSocks5OnConnectRequestRemoteSocketBindHostFromRuleSettings15() {
        Host host = Host.newInstance("127.0.0.1");
        Rule rule = new Rule.Builder().build();
        Settings settings = Settings.of(
                SocksSettingSpecConstants.SOCKS_ON_CONNECT_REQUEST_REMOTE_SOCKET_BIND_HOST.newSetting(
                        host));
        Assert.assertEquals(
                host,
                Socks5ValueDerivationHelper.getSocks5OnConnectRequestRemoteSocketBindHostFrom(rule, settings));
    }

    @Test
    public void testGetSocks5OnConnectRequestRemoteSocketBindHostFromRuleSettings16() {
        Host host = Host.newInstance("127.0.0.1");
        Rule rule = new Rule.Builder().build();
        Settings settings = Settings.of(
                Socks5SettingSpecConstants.SOCKS5_ON_CONNECT_REQUEST_REMOTE_SOCKET_BIND_HOST.newSetting(
                        host));
        Assert.assertEquals(
                host,
                Socks5ValueDerivationHelper.getSocks5OnConnectRequestRemoteSocketBindHostFrom(rule, settings));
    }

    @Test
    public void testGetSocks5OnConnectRequestRemoteSocketBindHostFromRuleSettings17() throws UnknownHostException, SocketException {
        Host host = Host.newInstance("127.0.0.1");
        NetworkInterface networkInterface = NetworkInterface.getByInetAddress(
                host.toInetAddress());
        NetInterface netInterface = NetInterface.newInstance(networkInterface);
        Rule rule = new Rule.Builder().build();
        Settings settings = Settings.of(
                GeneralSettingSpecConstants.NET_INTERFACE.newSetting(
                        netInterface),
                GeneralSettingSpecConstants.BIND_HOST_ADDRESS_TYPES.newSetting(HostAddressTypes.of(
                        HostAddressType.HOST_IPV4_ADDRESS)));
        Assert.assertTrue(
                netInterface.getHostAddresses().contains((HostAddress)
                        Socks5ValueDerivationHelper.getSocks5OnConnectRequestRemoteSocketBindHostFrom(rule, settings)));
    }

    @Test
    public void testGetSocks5OnConnectRequestRemoteSocketBindHostFromRuleSettings18() throws UnknownHostException, SocketException {
        Host host = Host.newInstance("127.0.0.1");
        NetworkInterface networkInterface = NetworkInterface.getByInetAddress(
                host.toInetAddress());
        NetInterface netInterface = NetInterface.newInstance(networkInterface);
        Rule rule = new Rule.Builder().build();
        Settings settings = Settings.of(
                GeneralSettingSpecConstants.REMOTE_ENDPOINTS_NET_INTERFACE.newSetting(
                        netInterface),
                GeneralSettingSpecConstants.REMOTE_ENDPOINTS_BIND_HOST_ADDRESS_TYPES.newSetting(HostAddressTypes.of(
                        HostAddressType.HOST_IPV4_ADDRESS)));
        Assert.assertTrue(
                netInterface.getHostAddresses().contains((HostAddress)
                        Socks5ValueDerivationHelper.getSocks5OnConnectRequestRemoteSocketBindHostFrom(rule, settings)));
    }

    @Test
    public void testGetSocks5OnConnectRequestRemoteSocketBindHostFromRuleSettings19() throws UnknownHostException, SocketException {
        Host host = Host.newInstance("127.0.0.1");
        NetworkInterface networkInterface = NetworkInterface.getByInetAddress(
                host.toInetAddress());
        NetInterface netInterface = NetInterface.newInstance(networkInterface);
        Rule rule = new Rule.Builder().build();
        Settings settings = Settings.of(
                SocksSettingSpecConstants.SOCKS_ON_REQUEST_REMOTE_ENDPOINTS_NET_INTERFACE.newSetting(
                        netInterface),
                SocksSettingSpecConstants.SOCKS_ON_REQUEST_REMOTE_ENDPOINTS_BIND_HOST_ADDRESS_TYPES.newSetting(HostAddressTypes.of(
                        HostAddressType.HOST_IPV4_ADDRESS)));
        Assert.assertTrue(
                netInterface.getHostAddresses().contains((HostAddress)
                        Socks5ValueDerivationHelper.getSocks5OnConnectRequestRemoteSocketBindHostFrom(rule, settings)));
    }

    @Test
    public void testGetSocks5OnConnectRequestRemoteSocketBindHostFromRuleSettings20() throws UnknownHostException, SocketException {
        Host host = Host.newInstance("127.0.0.1");
        NetworkInterface networkInterface = NetworkInterface.getByInetAddress(
                host.toInetAddress());
        NetInterface netInterface = NetInterface.newInstance(networkInterface);
        Rule rule = new Rule.Builder().build();
        Settings settings = Settings.of(
                SocksSettingSpecConstants.SOCKS_ON_CONNECT_REQUEST_REMOTE_SOCKET_NET_INTERFACE.newSetting(
                        netInterface),
                SocksSettingSpecConstants.SOCKS_ON_CONNECT_REQUEST_REMOTE_SOCKET_BIND_HOST_ADDRESS_TYPES.newSetting(HostAddressTypes.of(
                        HostAddressType.HOST_IPV4_ADDRESS)));
        Assert.assertTrue(
                netInterface.getHostAddresses().contains((HostAddress)
                        Socks5ValueDerivationHelper.getSocks5OnConnectRequestRemoteSocketBindHostFrom(rule, settings)));
    }

    @Test
    public void testGetSocks5OnConnectRequestRemoteSocketBindHostFromRuleSettings21() throws UnknownHostException, SocketException {
        Host host = Host.newInstance("127.0.0.1");
        NetworkInterface networkInterface = NetworkInterface.getByInetAddress(
                host.toInetAddress());
        NetInterface netInterface = NetInterface.newInstance(networkInterface);
        Rule rule = new Rule.Builder().build();
        Settings settings = Settings.of(
                Socks5SettingSpecConstants.SOCKS5_ON_CONNECT_REQUEST_REMOTE_SOCKET_NET_INTERFACE.newSetting(
                        netInterface),
                Socks5SettingSpecConstants.SOCKS5_ON_CONNECT_REQUEST_REMOTE_SOCKET_BIND_HOST_ADDRESS_TYPES.newSetting(HostAddressTypes.of(
                        HostAddressType.HOST_IPV4_ADDRESS)));
        Assert.assertTrue(
                netInterface.getHostAddresses().contains((HostAddress)
                        Socks5ValueDerivationHelper.getSocks5OnConnectRequestRemoteSocketBindHostFrom(rule, settings)));
    }

    @Test
    public void testGetSocks5OnConnectRequestRemoteSocketBindPortRangesFromRuleSettings01() {
        PortRanges portRanges = PortRanges.getDefault();
        Rule rule = new Rule.Builder().build();
        Settings settings = Settings.of();
        Assert.assertEquals(
                portRanges,
                Socks5ValueDerivationHelper.getSocks5OnConnectRequestRemoteSocketBindPortRangesFrom(rule, settings));
    }

    @Test
    public void testGetSocks5OnConnectRequestRemoteSocketBindPortRangesFromRuleSettings02() {
        PortRange portRange = PortRange.of(
                Port.valueOf(1), Port.valueOf(65535));
        PortRanges portRanges = PortRanges.of(portRange);
        Rule rule = new Rule.Builder()
                .addRuleAction(GeneralRuleActionSpecConstants.BIND_TCP_PORT_RANGE.newRuleAction(
                        portRange))
                .build();
        Settings settings = Settings.of();
        Assert.assertEquals(
                portRanges,
                Socks5ValueDerivationHelper.getSocks5OnConnectRequestRemoteSocketBindPortRangesFrom(rule, settings));
    }

    @Test
    public void testGetSocks5OnConnectRequestRemoteSocketBindPortRangesFromRuleSettings03() {
        PortRange portRange = PortRange.of(
                Port.valueOf(1), Port.valueOf(65535));
        PortRanges portRanges = PortRanges.of(portRange);
        Rule rule = new Rule.Builder()
                .addRuleAction(GeneralRuleActionSpecConstants.REMOTE_ENDPOINTS_BIND_TCP_PORT_RANGE.newRuleAction(
                        portRange))
                .build();
        Settings settings = Settings.of();
        Assert.assertEquals(
                portRanges,
                Socks5ValueDerivationHelper.getSocks5OnConnectRequestRemoteSocketBindPortRangesFrom(rule, settings));
    }

    @Test
    public void testGetSocks5OnConnectRequestRemoteSocketBindPortRangesFromRuleSettings04() {
        PortRange portRange = PortRange.of(
                Port.valueOf(1), Port.valueOf(65535));
        PortRanges portRanges = PortRanges.of(portRange);
        Rule rule = new Rule.Builder()
                .addRuleAction(SocksRuleActionSpecConstants.SOCKS_ON_REQUEST_REMOTE_ENDPOINTS_BIND_TCP_PORT_RANGE.newRuleAction(
                        portRange))
                .build();
        Settings settings = Settings.of();
        Assert.assertEquals(
                portRanges,
                Socks5ValueDerivationHelper.getSocks5OnConnectRequestRemoteSocketBindPortRangesFrom(rule, settings));
    }

    @Test
    public void testGetSocks5OnConnectRequestRemoteSocketBindPortRangesFromRuleSettings05() {
        PortRange portRange = PortRange.of(
                Port.valueOf(1), Port.valueOf(65535));
        PortRanges portRanges = PortRanges.of(portRange);
        Rule rule = new Rule.Builder()
                .addRuleAction(SocksRuleActionSpecConstants.SOCKS_ON_CONNECT_REQUEST_REMOTE_SOCKET_BIND_PORT_RANGE.newRuleAction(
                        portRange))
                .build();
        Settings settings = Settings.of();
        Assert.assertEquals(
                portRanges,
                Socks5ValueDerivationHelper.getSocks5OnConnectRequestRemoteSocketBindPortRangesFrom(rule, settings));
    }

    @Test
    public void testGetSocks5OnConnectRequestRemoteSocketBindPortRangesFromRuleSettings06() {
        PortRange portRange = PortRange.of(
                Port.valueOf(1), Port.valueOf(65535));
        PortRanges portRanges = PortRanges.of(portRange);
        Rule rule = new Rule.Builder()
                .addRuleAction(Socks5RuleActionSpecConstants.SOCKS5_ON_CONNECT_REQUEST_REMOTE_SOCKET_BIND_PORT_RANGE.newRuleAction(
                        portRange))
                .build();
        Settings settings = Settings.of();
        Assert.assertEquals(
                portRanges,
                Socks5ValueDerivationHelper.getSocks5OnConnectRequestRemoteSocketBindPortRangesFrom(rule, settings));
    }

    @Test
    public void testGetSocks5OnConnectRequestRemoteSocketBindPortRangesFromRuleSettings07() {
        PortRange portRange = PortRange.of(
                Port.valueOf(1), Port.valueOf(65535));
        PortRanges portRanges = PortRanges.of(portRange);
        Rule rule = new Rule.Builder().build();
        Settings settings = Settings.of(
                GeneralSettingSpecConstants.BIND_TCP_PORT_RANGES.newSetting(
                        portRanges));
        Assert.assertEquals(
                portRanges,
                Socks5ValueDerivationHelper.getSocks5OnConnectRequestRemoteSocketBindPortRangesFrom(rule, settings));
    }

    @Test
    public void testGetSocks5OnConnectRequestRemoteSocketBindPortRangesFromRuleSettings08() {
        PortRange portRange = PortRange.of(
                Port.valueOf(1), Port.valueOf(65535));
        PortRanges portRanges = PortRanges.of(portRange);
        Rule rule = new Rule.Builder().build();
        Settings settings = Settings.of(
                GeneralSettingSpecConstants.REMOTE_ENDPOINTS_BIND_TCP_PORT_RANGES.newSetting(
                        portRanges));
        Assert.assertEquals(
                portRanges,
                Socks5ValueDerivationHelper.getSocks5OnConnectRequestRemoteSocketBindPortRangesFrom(rule, settings));
    }

    @Test
    public void testGetSocks5OnConnectRequestRemoteSocketBindPortRangesFromRuleSettings09() {
        PortRange portRange = PortRange.of(
                Port.valueOf(1), Port.valueOf(65535));
        PortRanges portRanges = PortRanges.of(portRange);
        Rule rule = new Rule.Builder().build();
        Settings settings = Settings.of(
                SocksSettingSpecConstants.SOCKS_ON_REQUEST_REMOTE_ENDPOINTS_BIND_TCP_PORT_RANGES.newSetting(
                        portRanges));
        Assert.assertEquals(
                portRanges,
                Socks5ValueDerivationHelper.getSocks5OnConnectRequestRemoteSocketBindPortRangesFrom(rule, settings));
    }

    @Test
    public void testGetSocks5OnConnectRequestRemoteSocketBindPortRangesFromRuleSettings10() {
        PortRange portRange = PortRange.of(
                Port.valueOf(1), Port.valueOf(65535));
        PortRanges portRanges = PortRanges.of(portRange);
        Rule rule = new Rule.Builder().build();
        Settings settings = Settings.of(
                SocksSettingSpecConstants.SOCKS_ON_CONNECT_REQUEST_REMOTE_SOCKET_BIND_PORT_RANGES.newSetting(
                        portRanges));
        Assert.assertEquals(
                portRanges,
                Socks5ValueDerivationHelper.getSocks5OnConnectRequestRemoteSocketBindPortRangesFrom(rule, settings));
    }

    @Test
    public void testGetSocks5OnConnectRequestRemoteSocketBindPortRangesFromRuleSettings11() {
        PortRange portRange = PortRange.of(
                Port.valueOf(1), Port.valueOf(65535));
        PortRanges portRanges = PortRanges.of(portRange);
        Rule rule = new Rule.Builder().build();
        Settings settings = Settings.of(
                Socks5SettingSpecConstants.SOCKS5_ON_CONNECT_REQUEST_REMOTE_SOCKET_BIND_PORT_RANGES.newSetting(
                        portRanges));
        Assert.assertEquals(
                portRanges,
                Socks5ValueDerivationHelper.getSocks5OnConnectRequestRemoteSocketBindPortRangesFrom(rule, settings));
    }

    @Test
    public void testGetSocks5OnConnectRequestRemoteSocketConnectTimeoutFromRuleSettings01() {
        PositiveInteger i = PositiveInteger.valueOf(60000);
        Rule rule = new Rule.Builder().build();
        Settings settings = Settings.of();
        Assert.assertEquals(i, Socks5ValueDerivationHelper.getSocks5OnConnectRequestRemoteSocketConnectTimeoutFrom(rule, settings));
    }

    @Test
    public void testGetSocks5OnConnectRequestRemoteSocketConnectTimeoutFromRuleSettings02() {
        PositiveInteger i = PositiveInteger.valueOf(1);
        Rule rule = new Rule.Builder()
                .addRuleAction(SocksRuleActionSpecConstants.SOCKS_ON_CONNECT_REQUEST_REMOTE_SOCKET_CONNECT_TIMEOUT.newRuleAction(i))
                .build();
        Settings settings = Settings.of();
        Assert.assertEquals(i, Socks5ValueDerivationHelper.getSocks5OnConnectRequestRemoteSocketConnectTimeoutFrom(rule, settings));
    }

    @Test
    public void testGetSocks5OnConnectRequestRemoteSocketConnectTimeoutFromRuleSettings03() {
        PositiveInteger i = PositiveInteger.valueOf(1);
        Rule rule = new Rule.Builder()
                .addRuleAction(Socks5RuleActionSpecConstants.SOCKS5_ON_CONNECT_REQUEST_REMOTE_SOCKET_CONNECT_TIMEOUT.newRuleAction(i))
                .build();
        Settings settings = Settings.of();
        Assert.assertEquals(i, Socks5ValueDerivationHelper.getSocks5OnConnectRequestRemoteSocketConnectTimeoutFrom(rule, settings));
    }

    @Test
    public void testGetSocks5OnConnectRequestRemoteSocketConnectTimeoutFromRuleSettings04() {
        PositiveInteger i = PositiveInteger.valueOf(1);
        Rule rule = new Rule.Builder().build();
        Settings settings = Settings.of(
                SocksSettingSpecConstants.SOCKS_ON_CONNECT_REQUEST_REMOTE_SOCKET_CONNECT_TIMEOUT.newSetting(i));
        Assert.assertEquals(i, Socks5ValueDerivationHelper.getSocks5OnConnectRequestRemoteSocketConnectTimeoutFrom(rule, settings));
    }

    @Test
    public void testGetSocks5OnConnectRequestRemoteSocketConnectTimeoutFromRuleSettings05() {
        PositiveInteger i = PositiveInteger.valueOf(1);
        Rule rule = new Rule.Builder().build();
        Settings settings = Settings.of(
                Socks5SettingSpecConstants.SOCKS5_ON_CONNECT_REQUEST_REMOTE_SOCKET_CONNECT_TIMEOUT.newSetting(i));
        Assert.assertEquals(i, Socks5ValueDerivationHelper.getSocks5OnConnectRequestRemoteSocketConnectTimeoutFrom(rule, settings));
    }

    @Test
    public void testGetSocks5OnConnectRequestRemoteSocketPrepareSocketFromRuleSettings01() {
        Rule rule = new Rule.Builder().build();
        Settings settings = Settings.of();
        Assert.assertFalse(Socks5ValueDerivationHelper.getSocks5OnConnectRequestRemoteSocketPrepareSocketFrom(rule, settings));
    }

    @Test
    public void testGetSocks5OnConnectRequestRemoteSocketPrepareSocketFromRuleSettings02() {
        Rule rule = new Rule.Builder()
                .addRuleAction(SocksRuleActionSpecConstants.SOCKS_ON_CONNECT_REQUEST_REMOTE_SOCKET_PREPARE_SOCKET.newRuleAction(
                        Boolean.TRUE))
                .build();
        Settings settings = Settings.of();
        Assert.assertTrue(Socks5ValueDerivationHelper.getSocks5OnConnectRequestRemoteSocketPrepareSocketFrom(rule, settings));
    }

    @Test
    public void testGetSocks5OnConnectRequestRemoteSocketPrepareSocketFromRuleSettings03() {
        Rule rule = new Rule.Builder()
                .addRuleAction(Socks5RuleActionSpecConstants.SOCKS5_ON_CONNECT_REQUEST_REMOTE_SOCKET_PREPARE_SOCKET.newRuleAction(
                        Boolean.TRUE))
                .build();
        Settings settings = Settings.of();
        Assert.assertTrue(Socks5ValueDerivationHelper.getSocks5OnConnectRequestRemoteSocketPrepareSocketFrom(rule, settings));
    }

    @Test
    public void testGetSocks5OnConnectRequestRemoteSocketPrepareSocketFromRuleSettings04() {
        Rule rule = new Rule.Builder().build();
        Settings settings = Settings.of(
                SocksSettingSpecConstants.SOCKS_ON_CONNECT_REQUEST_REMOTE_SOCKET_PREPARE_SOCKET.newSetting(
                        Boolean.TRUE));
        Assert.assertTrue(Socks5ValueDerivationHelper.getSocks5OnConnectRequestRemoteSocketPrepareSocketFrom(rule, settings));
    }

    @Test
    public void testGetSocks5OnConnectRequestRemoteSocketPrepareSocketFromRuleSettings05() {
        Rule rule = new Rule.Builder().build();
        Settings settings = Settings.of(
                Socks5SettingSpecConstants.SOCKS5_ON_CONNECT_REQUEST_REMOTE_SOCKET_PREPARE_SOCKET.newSetting(
                        Boolean.TRUE));
        Assert.assertTrue(Socks5ValueDerivationHelper.getSocks5OnConnectRequestRemoteSocketPrepareSocketFrom(rule, settings));
    }

    @Test
    public void testGetSocks5OnConnectRequestRemoteSocketSocketSettingsFromRuleSettings01() {
        SocketSettings socketSettings = SocketSettings.of();
        Rule rule = new Rule.Builder().build();
        Settings settings = Settings.of();
        Assert.assertEquals(
                socketSettings,
                Socks5ValueDerivationHelper.getSocks5OnConnectRequestRemoteSocketSocketSettingsFrom(rule, settings));
    }

    @Test
    public void testGetSocks5OnConnectRequestRemoteSocketSocketSettingsFromRuleSettings02() {
        SocketSetting<?> socketSetting =
                StandardSocketSettingSpecConstants.SO_TIMEOUT.newSocketSetting(
                        NonNegativeInteger.valueOf(0));
        @SuppressWarnings("unchecked")
        SocketSetting<Object> socketSttng = (SocketSetting<Object>) socketSetting;
        SocketSettings socketSettings = SocketSettings.of(socketSttng);
        Rule rule = new Rule.Builder()
                .addRuleAction(GeneralRuleActionSpecConstants.SOCKET_SETTING.newRuleAction(
                        socketSttng))
                .build();
        Settings settings = Settings.of();
        Assert.assertEquals(
                socketSettings,
                Socks5ValueDerivationHelper.getSocks5OnConnectRequestRemoteSocketSocketSettingsFrom(rule, settings));
    }

    @Test
    public void testGetSocks5OnConnectRequestRemoteSocketSocketSettingsFromRuleSettings03() {
        SocketSetting<?> socketSetting =
                StandardSocketSettingSpecConstants.SO_TIMEOUT.newSocketSetting(
                        NonNegativeInteger.valueOf(0));
        @SuppressWarnings("unchecked")
        SocketSetting<Object> socketSttng = (SocketSetting<Object>) socketSetting;
        SocketSettings socketSettings = SocketSettings.of(socketSttng);
        Rule rule = new Rule.Builder()
                .addRuleAction(GeneralRuleActionSpecConstants.REMOTE_ENDPOINTS_SOCKET_SETTING.newRuleAction(
                        socketSttng))
                .build();
        Settings settings = Settings.of();
        Assert.assertEquals(
                socketSettings,
                Socks5ValueDerivationHelper.getSocks5OnConnectRequestRemoteSocketSocketSettingsFrom(rule, settings));
    }

    @Test
    public void testGetSocks5OnConnectRequestRemoteSocketSocketSettingsFromRuleSettings04() {
        SocketSetting<?> socketSetting =
                StandardSocketSettingSpecConstants.SO_TIMEOUT.newSocketSetting(
                        NonNegativeInteger.valueOf(0));
        @SuppressWarnings("unchecked")
        SocketSetting<Object> socketSttng = (SocketSetting<Object>) socketSetting;
        SocketSettings socketSettings = SocketSettings.of(socketSttng);
        Rule rule = new Rule.Builder()
                .addRuleAction(SocksRuleActionSpecConstants.SOCKS_ON_REQUEST_REMOTE_ENDPOINTS_SOCKET_SETTING.newRuleAction(
                        socketSttng))
                .build();
        Settings settings = Settings.of();
        Assert.assertEquals(
                socketSettings,
                Socks5ValueDerivationHelper.getSocks5OnConnectRequestRemoteSocketSocketSettingsFrom(rule, settings));
    }

    @Test
    public void testGetSocks5OnConnectRequestRemoteSocketSocketSettingsFromRuleSettings05() {
        SocketSetting<?> socketSetting =
                StandardSocketSettingSpecConstants.SO_TIMEOUT.newSocketSetting(
                        NonNegativeInteger.valueOf(0));
        @SuppressWarnings("unchecked")
        SocketSetting<Object> socketSttng = (SocketSetting<Object>) socketSetting;
        SocketSettings socketSettings = SocketSettings.of(socketSttng);
        Rule rule = new Rule.Builder()
                .addRuleAction(SocksRuleActionSpecConstants.SOCKS_ON_CONNECT_REQUEST_REMOTE_SOCKET_SOCKET_SETTING.newRuleAction(
                        socketSttng))
                .build();
        Settings settings = Settings.of();
        Assert.assertEquals(
                socketSettings,
                Socks5ValueDerivationHelper.getSocks5OnConnectRequestRemoteSocketSocketSettingsFrom(rule, settings));
    }

    @Test
    public void testGetSocks5OnConnectRequestRemoteSocketSocketSettingsFromRuleSettings06() {
        SocketSetting<?> socketSetting =
                StandardSocketSettingSpecConstants.SO_TIMEOUT.newSocketSetting(
                        NonNegativeInteger.valueOf(0));
        @SuppressWarnings("unchecked")
        SocketSetting<Object> socketSttng = (SocketSetting<Object>) socketSetting;
        SocketSettings socketSettings = SocketSettings.of(socketSttng);
        Rule rule = new Rule.Builder()
                .addRuleAction(Socks5RuleActionSpecConstants.SOCKS5_ON_CONNECT_REQUEST_REMOTE_SOCKET_SOCKET_SETTING.newRuleAction(
                        socketSttng))
                .build();
        Settings settings = Settings.of();
        Assert.assertEquals(
                socketSettings,
                Socks5ValueDerivationHelper.getSocks5OnConnectRequestRemoteSocketSocketSettingsFrom(rule, settings));
    }

    @Test
    public void testGetSocks5OnConnectRequestRemoteSocketSocketSettingsFromRuleSettings07() {
        SocketSetting<?> socketSetting =
                StandardSocketSettingSpecConstants.SO_TIMEOUT.newSocketSetting(
                        NonNegativeInteger.valueOf(0));
        SocketSettings socketSettings = SocketSettings.of(socketSetting);
        Rule rule = new Rule.Builder().build();
        Settings settings = Settings.of(
                GeneralSettingSpecConstants.SOCKET_SETTINGS.newSetting(
                        socketSettings));
        Assert.assertEquals(
                socketSettings,
                Socks5ValueDerivationHelper.getSocks5OnConnectRequestRemoteSocketSocketSettingsFrom(rule, settings));
    }

    @Test
    public void testGetSocks5OnConnectRequestRemoteSocketSocketSettingsFromRuleSettings08() {
        SocketSetting<?> socketSetting =
                StandardSocketSettingSpecConstants.SO_TIMEOUT.newSocketSetting(
                        NonNegativeInteger.valueOf(0));
        SocketSettings socketSettings = SocketSettings.of(socketSetting);
        Rule rule = new Rule.Builder().build();
        Settings settings = Settings.of(
                GeneralSettingSpecConstants.REMOTE_ENDPOINTS_SOCKET_SETTINGS.newSetting(
                        socketSettings));
        Assert.assertEquals(
                socketSettings,
                Socks5ValueDerivationHelper.getSocks5OnConnectRequestRemoteSocketSocketSettingsFrom(rule, settings));
    }

    @Test
    public void testGetSocks5OnConnectRequestRemoteSocketSocketSettingsFromRuleSettings09() {
        SocketSetting<?> socketSetting =
                StandardSocketSettingSpecConstants.SO_TIMEOUT.newSocketSetting(
                        NonNegativeInteger.valueOf(0));
        SocketSettings socketSettings = SocketSettings.of(socketSetting);
        Rule rule = new Rule.Builder().build();
        Settings settings = Settings.of(
                SocksSettingSpecConstants.SOCKS_ON_REQUEST_REMOTE_ENDPOINTS_SOCKET_SETTINGS.newSetting(
                        socketSettings));
        Assert.assertEquals(
                socketSettings,
                Socks5ValueDerivationHelper.getSocks5OnConnectRequestRemoteSocketSocketSettingsFrom(rule, settings));
    }

    @Test
    public void testGetSocks5OnConnectRequestRemoteSocketSocketSettingsFromRuleSettings10() {
        SocketSetting<?> socketSetting =
                StandardSocketSettingSpecConstants.SO_TIMEOUT.newSocketSetting(
                        NonNegativeInteger.valueOf(0));
        SocketSettings socketSettings = SocketSettings.of(socketSetting);
        Rule rule = new Rule.Builder().build();
        Settings settings = Settings.of(
                SocksSettingSpecConstants.SOCKS_ON_CONNECT_REQUEST_REMOTE_SOCKET_SOCKET_SETTINGS.newSetting(
                        socketSettings));
        Assert.assertEquals(
                socketSettings,
                Socks5ValueDerivationHelper.getSocks5OnConnectRequestRemoteSocketSocketSettingsFrom(rule, settings));
    }

    @Test
    public void testGetSocks5OnConnectRequestRemoteSocketSocketSettingsFromRuleSettings11() {
        SocketSetting<?> socketSetting =
                StandardSocketSettingSpecConstants.SO_TIMEOUT.newSocketSetting(
                        NonNegativeInteger.valueOf(0));
        SocketSettings socketSettings = SocketSettings.of(socketSetting);
        Rule rule = new Rule.Builder().build();
        Settings settings = Settings.of(
                Socks5SettingSpecConstants.SOCKS5_ON_CONNECT_REQUEST_REMOTE_SOCKET_SOCKET_SETTINGS.newSetting(
                        socketSettings));
        Assert.assertEquals(
                socketSettings,
                Socks5ValueDerivationHelper.getSocks5OnConnectRequestRemoteSocketSocketSettingsFrom(rule, settings));
    }

    @Test
    public void testGetSocks5OnUdpAssociateRequestClientFacingDatagramSocketBindHostFromRuleSettings01() {
        Host host = HostIpv4Address.getAllZerosInstance();
        Rule rule = new Rule.Builder().build();
        Settings settings = Settings.of();
        Assert.assertEquals(
                host,
                Socks5ValueDerivationHelper.getSocks5OnUdpAssociateRequestClientFacingDatagramSocketBindHostFrom(rule, settings));
    }

    @Test
    public void testGetSocks5OnUdpAssociateRequestClientFacingDatagramSocketBindHostFromRuleSettings02() {
        Host host = Host.newInstance("127.0.0.1");
        Rule rule = new Rule.Builder()
                .addRuleAction(GeneralRuleActionSpecConstants.BIND_HOST.newRuleAction(
                        host))
                .build();
        Settings settings = Settings.of();
        Assert.assertEquals(
                host,
                Socks5ValueDerivationHelper.getSocks5OnUdpAssociateRequestClientFacingDatagramSocketBindHostFrom(rule, settings));
    }

    @Test
    public void testGetSocks5OnUdpAssociateRequestClientFacingDatagramSocketBindHostFromRuleSettings03() {
        Host host = Host.newInstance("127.0.0.1");
        Rule rule = new Rule.Builder()
                .addRuleAction(GeneralRuleActionSpecConstants.CLIENT_ENDPOINTS_BIND_HOST.newRuleAction(
                        host))
                .build();
        Settings settings = Settings.of();
        Assert.assertEquals(
                host,
                Socks5ValueDerivationHelper.getSocks5OnUdpAssociateRequestClientFacingDatagramSocketBindHostFrom(rule, settings));
    }

    @Test
    public void testGetSocks5OnUdpAssociateRequestClientFacingDatagramSocketBindHostFromRuleSettings04() {
        Host host = Host.newInstance("127.0.0.1");
        Rule rule = new Rule.Builder()
                .addRuleAction(SocksRuleActionSpecConstants.SOCKS_ON_REQUEST_CLIENT_ENDPOINTS_BIND_HOST.newRuleAction(
                        host))
                .build();
        Settings settings = Settings.of();
        Assert.assertEquals(
                host,
                Socks5ValueDerivationHelper.getSocks5OnUdpAssociateRequestClientFacingDatagramSocketBindHostFrom(rule, settings));
    }

    @Test
    public void testGetSocks5OnUdpAssociateRequestClientFacingDatagramSocketBindHostFromRuleSettings05() {
        Host host = Host.newInstance("127.0.0.1");
        Rule rule = new Rule.Builder()
                .addRuleAction(SocksRuleActionSpecConstants.SOCKS_ON_UDP_ASSOCIATE_REQUEST_CLIENT_FACING_DATAGRAM_SOCKET_BIND_HOST.newRuleAction(
                        host))
                .build();
        Settings settings = Settings.of();
        Assert.assertEquals(
                host,
                Socks5ValueDerivationHelper.getSocks5OnUdpAssociateRequestClientFacingDatagramSocketBindHostFrom(rule, settings));
    }

    @Test
    public void testGetSocks5OnUdpAssociateRequestClientFacingDatagramSocketBindHostFromRuleSettings06() {
        Host host = Host.newInstance("127.0.0.1");
        Rule rule = new Rule.Builder()
                .addRuleAction(Socks5RuleActionSpecConstants.SOCKS5_ON_UDP_ASSOCIATE_REQUEST_CLIENT_FACING_DATAGRAM_SOCKET_BIND_HOST.newRuleAction(
                        host))
                .build();
        Settings settings = Settings.of();
        Assert.assertEquals(
                host,
                Socks5ValueDerivationHelper.getSocks5OnUdpAssociateRequestClientFacingDatagramSocketBindHostFrom(rule, settings));
    }

    @Test
    public void testGetSocks5OnUdpAssociateRequestClientFacingDatagramSocketBindHostFromRuleSettings07() throws UnknownHostException, SocketException {
        Host host = Host.newInstance("127.0.0.1");
        NetworkInterface networkInterface = NetworkInterface.getByInetAddress(
                host.toInetAddress());
        NetInterface netInterface = NetInterface.newInstance(networkInterface);
        Rule rule = new Rule.Builder()
                .addRuleAction(GeneralRuleActionSpecConstants.NET_INTERFACE.newRuleAction(
                        netInterface))
                .addRuleAction(GeneralRuleActionSpecConstants.BIND_HOST_ADDRESS_TYPE.newRuleAction(
                        HostAddressType.HOST_IPV4_ADDRESS))
                .build();
        Settings settings = Settings.of();
        Assert.assertTrue(
                netInterface.getHostAddresses().contains((HostAddress)
                        Socks5ValueDerivationHelper.getSocks5OnUdpAssociateRequestClientFacingDatagramSocketBindHostFrom(rule, settings)));
    }

    @Test
    public void testGetSocks5OnUdpAssociateRequestClientFacingDatagramSocketBindHostFromRuleSettings08() throws UnknownHostException, SocketException {
        Host host = Host.newInstance("127.0.0.1");
        NetworkInterface networkInterface = NetworkInterface.getByInetAddress(
                host.toInetAddress());
        NetInterface netInterface = NetInterface.newInstance(networkInterface);
        Rule rule = new Rule.Builder()
                .addRuleAction(GeneralRuleActionSpecConstants.CLIENT_ENDPOINTS_NET_INTERFACE.newRuleAction(
                        netInterface))
                .addRuleAction(GeneralRuleActionSpecConstants.CLIENT_ENDPOINTS_BIND_HOST_ADDRESS_TYPE.newRuleAction(
                        HostAddressType.HOST_IPV4_ADDRESS))
                .build();
        Settings settings = Settings.of();
        Assert.assertTrue(
                netInterface.getHostAddresses().contains((HostAddress)
                        Socks5ValueDerivationHelper.getSocks5OnUdpAssociateRequestClientFacingDatagramSocketBindHostFrom(rule, settings)));
    }

    @Test
    public void testGetSocks5OnUdpAssociateRequestClientFacingDatagramSocketBindHostFromRuleSettings09() throws UnknownHostException, SocketException {
        Host host = Host.newInstance("127.0.0.1");
        NetworkInterface networkInterface = NetworkInterface.getByInetAddress(
                host.toInetAddress());
        NetInterface netInterface = NetInterface.newInstance(networkInterface);
        Rule rule = new Rule.Builder()
                .addRuleAction(SocksRuleActionSpecConstants.SOCKS_ON_REQUEST_CLIENT_ENDPOINTS_NET_INTERFACE.newRuleAction(
                        netInterface))
                .addRuleAction(SocksRuleActionSpecConstants.SOCKS_ON_REQUEST_CLIENT_ENDPOINTS_BIND_HOST_ADDRESS_TYPE.newRuleAction(
                        HostAddressType.HOST_IPV4_ADDRESS))
                .build();
        Settings settings = Settings.of();
        Assert.assertTrue(
                netInterface.getHostAddresses().contains((HostAddress)
                        Socks5ValueDerivationHelper.getSocks5OnUdpAssociateRequestClientFacingDatagramSocketBindHostFrom(rule, settings)));
    }

    @Test
    public void testGetSocks5OnUdpAssociateRequestClientFacingDatagramSocketBindHostFromRuleSettings10() throws UnknownHostException, SocketException {
        Host host = Host.newInstance("127.0.0.1");
        NetworkInterface networkInterface = NetworkInterface.getByInetAddress(
                host.toInetAddress());
        NetInterface netInterface = NetInterface.newInstance(networkInterface);
        Rule rule = new Rule.Builder()
                .addRuleAction(SocksRuleActionSpecConstants.SOCKS_ON_UDP_ASSOCIATE_REQUEST_CLIENT_FACING_DATAGRAM_SOCKET_NET_INTERFACE.newRuleAction(
                        netInterface))
                .addRuleAction(SocksRuleActionSpecConstants.SOCKS_ON_UDP_ASSOCIATE_REQUEST_CLIENT_FACING_DATAGRAM_SOCKET_BIND_HOST_ADDRESS_TYPE.newRuleAction(
                        HostAddressType.HOST_IPV4_ADDRESS))
                .build();
        Settings settings = Settings.of();
        Assert.assertTrue(
                netInterface.getHostAddresses().contains((HostAddress)
                        Socks5ValueDerivationHelper.getSocks5OnUdpAssociateRequestClientFacingDatagramSocketBindHostFrom(rule, settings)));
    }

    @Test
    public void testGetSocks5OnUdpAssociateRequestClientFacingDatagramSocketBindHostFromRuleSettings11() throws UnknownHostException, SocketException {
        Host host = Host.newInstance("127.0.0.1");
        NetworkInterface networkInterface = NetworkInterface.getByInetAddress(
                host.toInetAddress());
        NetInterface netInterface = NetInterface.newInstance(networkInterface);
        Rule rule = new Rule.Builder()
                .addRuleAction(Socks5RuleActionSpecConstants.SOCKS5_ON_UDP_ASSOCIATE_REQUEST_CLIENT_FACING_DATAGRAM_SOCKET_NET_INTERFACE.newRuleAction(
                        netInterface))
                .addRuleAction(Socks5RuleActionSpecConstants.SOCKS5_ON_UDP_ASSOCIATE_REQUEST_CLIENT_FACING_DATAGRAM_SOCKET_BIND_HOST_ADDRESS_TYPE.newRuleAction(
                        HostAddressType.HOST_IPV4_ADDRESS))
                .build();
        Settings settings = Settings.of();
        Assert.assertTrue(
                netInterface.getHostAddresses().contains((HostAddress)
                        Socks5ValueDerivationHelper.getSocks5OnUdpAssociateRequestClientFacingDatagramSocketBindHostFrom(rule, settings)));
    }

    @Test
    public void testGetSocks5OnUdpAssociateRequestClientFacingDatagramSocketBindHostFromRuleSettings12() {
        Host host = Host.newInstance("127.0.0.1");
        Rule rule = new Rule.Builder().build();
        Settings settings = Settings.of(
                GeneralSettingSpecConstants.BIND_HOST.newSetting(host));
        Assert.assertEquals(
                host,
                Socks5ValueDerivationHelper.getSocks5OnUdpAssociateRequestClientFacingDatagramSocketBindHostFrom(rule, settings));
    }

    @Test
    public void testGetSocks5OnUdpAssociateRequestClientFacingDatagramSocketBindHostFromRuleSettings13() {
        Host host = Host.newInstance("127.0.0.1");
        Rule rule = new Rule.Builder().build();
        Settings settings = Settings.of(
                GeneralSettingSpecConstants.CLIENT_ENDPOINTS_BIND_HOST.newSetting(
                        host));
        Assert.assertEquals(
                host,
                Socks5ValueDerivationHelper.getSocks5OnUdpAssociateRequestClientFacingDatagramSocketBindHostFrom(rule, settings));
    }

    @Test
    public void testGetSocks5OnUdpAssociateRequestClientFacingDatagramSocketBindHostFromRuleSettings14() {
        Host host = Host.newInstance("127.0.0.1");
        Rule rule = new Rule.Builder().build();
        Settings settings = Settings.of(
                SocksSettingSpecConstants.SOCKS_ON_REQUEST_CLIENT_ENDPOINTS_BIND_HOST.newSetting(
                        host));
        Assert.assertEquals(
                host,
                Socks5ValueDerivationHelper.getSocks5OnUdpAssociateRequestClientFacingDatagramSocketBindHostFrom(rule, settings));
    }

    @Test
    public void testGetSocks5OnUdpAssociateRequestClientFacingDatagramSocketBindHostFromRuleSettings15() {
        Host host = Host.newInstance("127.0.0.1");
        Rule rule = new Rule.Builder().build();
        Settings settings = Settings.of(
                SocksSettingSpecConstants.SOCKS_ON_UDP_ASSOCIATE_REQUEST_CLIENT_FACING_DATAGRAM_SOCKET_BIND_HOST.newSetting(
                        host));
        Assert.assertEquals(
                host,
                Socks5ValueDerivationHelper.getSocks5OnUdpAssociateRequestClientFacingDatagramSocketBindHostFrom(rule, settings));
    }

    @Test
    public void testGetSocks5OnUdpAssociateRequestClientFacingDatagramSocketBindHostFromRuleSettings16() {
        Host host = Host.newInstance("127.0.0.1");
        Rule rule = new Rule.Builder().build();
        Settings settings = Settings.of(
                Socks5SettingSpecConstants.SOCKS5_ON_UDP_ASSOCIATE_REQUEST_CLIENT_FACING_DATAGRAM_SOCKET_BIND_HOST.newSetting(
                        host));
        Assert.assertEquals(
                host,
                Socks5ValueDerivationHelper.getSocks5OnUdpAssociateRequestClientFacingDatagramSocketBindHostFrom(rule, settings));
    }

    @Test
    public void testGetSocks5OnUdpAssociateRequestClientFacingDatagramSocketBindHostFromRuleSettings17() throws UnknownHostException, SocketException {
        Host host = Host.newInstance("127.0.0.1");
        NetworkInterface networkInterface = NetworkInterface.getByInetAddress(
                host.toInetAddress());
        NetInterface netInterface = NetInterface.newInstance(networkInterface);
        Rule rule = new Rule.Builder().build();
        Settings settings = Settings.of(
                GeneralSettingSpecConstants.NET_INTERFACE.newSetting(
                        netInterface),
                GeneralSettingSpecConstants.BIND_HOST_ADDRESS_TYPES.newSetting(HostAddressTypes.of(
                        HostAddressType.HOST_IPV4_ADDRESS)));
        Assert.assertTrue(
                netInterface.getHostAddresses().contains((HostAddress)
                        Socks5ValueDerivationHelper.getSocks5OnUdpAssociateRequestClientFacingDatagramSocketBindHostFrom(rule, settings)));
    }

    @Test
    public void testGetSocks5OnUdpAssociateRequestClientFacingDatagramSocketBindHostFromRuleSettings18() throws UnknownHostException, SocketException {
        Host host = Host.newInstance("127.0.0.1");
        NetworkInterface networkInterface = NetworkInterface.getByInetAddress(
                host.toInetAddress());
        NetInterface netInterface = NetInterface.newInstance(networkInterface);
        Rule rule = new Rule.Builder().build();
        Settings settings = Settings.of(
                GeneralSettingSpecConstants.CLIENT_ENDPOINTS_NET_INTERFACE.newSetting(
                        netInterface),
                GeneralSettingSpecConstants.CLIENT_ENDPOINTS_BIND_HOST_ADDRESS_TYPES.newSetting(HostAddressTypes.of(
                        HostAddressType.HOST_IPV4_ADDRESS)));
        Assert.assertTrue(
                netInterface.getHostAddresses().contains((HostAddress)
                        Socks5ValueDerivationHelper.getSocks5OnUdpAssociateRequestClientFacingDatagramSocketBindHostFrom(rule, settings)));
    }

    @Test
    public void testGetSocks5OnUdpAssociateRequestClientFacingDatagramSocketBindHostFromRuleSettings19() throws UnknownHostException, SocketException {
        Host host = Host.newInstance("127.0.0.1");
        NetworkInterface networkInterface = NetworkInterface.getByInetAddress(
                host.toInetAddress());
        NetInterface netInterface = NetInterface.newInstance(networkInterface);
        Rule rule = new Rule.Builder().build();
        Settings settings = Settings.of(
                SocksSettingSpecConstants.SOCKS_ON_REQUEST_CLIENT_ENDPOINTS_NET_INTERFACE.newSetting(
                        netInterface),
                SocksSettingSpecConstants.SOCKS_ON_REQUEST_CLIENT_ENDPOINTS_BIND_HOST_ADDRESS_TYPES.newSetting(HostAddressTypes.of(
                        HostAddressType.HOST_IPV4_ADDRESS)));
        Assert.assertTrue(
                netInterface.getHostAddresses().contains((HostAddress)
                        Socks5ValueDerivationHelper.getSocks5OnUdpAssociateRequestClientFacingDatagramSocketBindHostFrom(rule, settings)));
    }

    @Test
    public void testGetSocks5OnUdpAssociateRequestClientFacingDatagramSocketBindHostFromRuleSettings20() throws UnknownHostException, SocketException {
        Host host = Host.newInstance("127.0.0.1");
        NetworkInterface networkInterface = NetworkInterface.getByInetAddress(
                host.toInetAddress());
        NetInterface netInterface = NetInterface.newInstance(networkInterface);
        Rule rule = new Rule.Builder().build();
        Settings settings = Settings.of(
                SocksSettingSpecConstants.SOCKS_ON_UDP_ASSOCIATE_REQUEST_CLIENT_FACING_DATAGRAM_SOCKET_NET_INTERFACE.newSetting(
                        netInterface),
                SocksSettingSpecConstants.SOCKS_ON_UDP_ASSOCIATE_REQUEST_CLIENT_FACING_DATAGRAM_SOCKET_BIND_HOST_ADDRESS_TYPES.newSetting(HostAddressTypes.of(
                        HostAddressType.HOST_IPV4_ADDRESS)));
        Assert.assertTrue(
                netInterface.getHostAddresses().contains((HostAddress)
                        Socks5ValueDerivationHelper.getSocks5OnUdpAssociateRequestClientFacingDatagramSocketBindHostFrom(rule, settings)));
    }

    @Test
    public void testGetSocks5OnUdpAssociateRequestClientFacingDatagramSocketBindHostFromRuleSettings21() throws UnknownHostException, SocketException {
        Host host = Host.newInstance("127.0.0.1");
        NetworkInterface networkInterface = NetworkInterface.getByInetAddress(
                host.toInetAddress());
        NetInterface netInterface = NetInterface.newInstance(networkInterface);
        Rule rule = new Rule.Builder().build();
        Settings settings = Settings.of(
                Socks5SettingSpecConstants.SOCKS5_ON_UDP_ASSOCIATE_REQUEST_CLIENT_FACING_DATAGRAM_SOCKET_NET_INTERFACE.newSetting(
                        netInterface),
                Socks5SettingSpecConstants.SOCKS5_ON_UDP_ASSOCIATE_REQUEST_CLIENT_FACING_DATAGRAM_SOCKET_BIND_HOST_ADDRESS_TYPES.newSetting(HostAddressTypes.of(
                        HostAddressType.HOST_IPV4_ADDRESS)));
        Assert.assertTrue(
                netInterface.getHostAddresses().contains((HostAddress)
                        Socks5ValueDerivationHelper.getSocks5OnUdpAssociateRequestClientFacingDatagramSocketBindHostFrom(rule, settings)));
    }

    @Test
    public void testGetSocks5OnUdpAssociateRequestClientFacingDatagramSocketBindPortRangesFromRuleSettings01() {
        PortRanges portRanges = PortRanges.getDefault();
        Rule rule = new Rule.Builder().build();
        Settings settings = Settings.of();
        Assert.assertEquals(
                portRanges,
                Socks5ValueDerivationHelper.getSocks5OnUdpAssociateRequestClientFacingDatagramSocketBindPortRangesFrom(rule, settings));
    }

    @Test
    public void testGetSocks5OnUdpAssociateRequestClientFacingDatagramSocketBindPortRangesFromRuleSettings02() {
        PortRange portRange = PortRange.of(
                Port.valueOf(1), Port.valueOf(65535));
        PortRanges portRanges = PortRanges.of(portRange);
        Rule rule = new Rule.Builder()
                .addRuleAction(GeneralRuleActionSpecConstants.BIND_UDP_PORT_RANGE.newRuleAction(
                        portRange))
                .build();
        Settings settings = Settings.of();
        Assert.assertEquals(
                portRanges,
                Socks5ValueDerivationHelper.getSocks5OnUdpAssociateRequestClientFacingDatagramSocketBindPortRangesFrom(rule, settings));
    }

    @Test
    public void testGetSocks5OnUdpAssociateRequestClientFacingDatagramSocketBindPortRangesFromRuleSettings03() {
        PortRange portRange = PortRange.of(
                Port.valueOf(1), Port.valueOf(65535));
        PortRanges portRanges = PortRanges.of(portRange);
        Rule rule = new Rule.Builder()
                .addRuleAction(GeneralRuleActionSpecConstants.CLIENT_ENDPOINTS_BIND_UDP_PORT_RANGE.newRuleAction(
                        portRange))
                .build();
        Settings settings = Settings.of();
        Assert.assertEquals(
                portRanges,
                Socks5ValueDerivationHelper.getSocks5OnUdpAssociateRequestClientFacingDatagramSocketBindPortRangesFrom(rule, settings));
    }

    @Test
    public void testGetSocks5OnUdpAssociateRequestClientFacingDatagramSocketBindPortRangesFromRuleSettings04() {
        PortRange portRange = PortRange.of(
                Port.valueOf(1), Port.valueOf(65535));
        PortRanges portRanges = PortRanges.of(portRange);
        Rule rule = new Rule.Builder()
                .addRuleAction(SocksRuleActionSpecConstants.SOCKS_ON_REQUEST_CLIENT_ENDPOINTS_BIND_UDP_PORT_RANGE.newRuleAction(
                        portRange))
                .build();
        Settings settings = Settings.of();
        Assert.assertEquals(
                portRanges,
                Socks5ValueDerivationHelper.getSocks5OnUdpAssociateRequestClientFacingDatagramSocketBindPortRangesFrom(rule, settings));
    }

    @Test
    public void testGetSocks5OnUdpAssociateRequestClientFacingDatagramSocketBindPortRangesFromRuleSettings05() {
        PortRange portRange = PortRange.of(
                Port.valueOf(1), Port.valueOf(65535));
        PortRanges portRanges = PortRanges.of(portRange);
        Rule rule = new Rule.Builder()
                .addRuleAction(SocksRuleActionSpecConstants.SOCKS_ON_UDP_ASSOCIATE_REQUEST_CLIENT_FACING_DATAGRAM_SOCKET_BIND_PORT_RANGE.newRuleAction(
                        portRange))
                .build();
        Settings settings = Settings.of();
        Assert.assertEquals(
                portRanges,
                Socks5ValueDerivationHelper.getSocks5OnUdpAssociateRequestClientFacingDatagramSocketBindPortRangesFrom(rule, settings));
    }

    @Test
    public void testGetSocks5OnUdpAssociateRequestClientFacingDatagramSocketBindPortRangesFromRuleSettings06() {
        PortRange portRange = PortRange.of(
                Port.valueOf(1), Port.valueOf(65535));
        PortRanges portRanges = PortRanges.of(portRange);
        Rule rule = new Rule.Builder()
                .addRuleAction(Socks5RuleActionSpecConstants.SOCKS5_ON_UDP_ASSOCIATE_REQUEST_CLIENT_FACING_DATAGRAM_SOCKET_BIND_PORT_RANGE.newRuleAction(
                        portRange))
                .build();
        Settings settings = Settings.of();
        Assert.assertEquals(
                portRanges,
                Socks5ValueDerivationHelper.getSocks5OnUdpAssociateRequestClientFacingDatagramSocketBindPortRangesFrom(rule, settings));
    }

    @Test
    public void testGetSocks5OnUdpAssociateRequestClientFacingDatagramSocketBindPortRangesFromRuleSettings07() {
        PortRange portRange = PortRange.of(
                Port.valueOf(1), Port.valueOf(65535));
        PortRanges portRanges = PortRanges.of(portRange);
        Rule rule = new Rule.Builder().build();
        Settings settings = Settings.of(
                GeneralSettingSpecConstants.BIND_UDP_PORT_RANGES.newSetting(
                        portRanges));
        Assert.assertEquals(
                portRanges,
                Socks5ValueDerivationHelper.getSocks5OnUdpAssociateRequestClientFacingDatagramSocketBindPortRangesFrom(rule, settings));
    }

    @Test
    public void testGetSocks5OnUdpAssociateRequestClientFacingDatagramSocketBindPortRangesFromRuleSettings08() {
        PortRange portRange = PortRange.of(
                Port.valueOf(1), Port.valueOf(65535));
        PortRanges portRanges = PortRanges.of(portRange);
        Rule rule = new Rule.Builder().build();
        Settings settings = Settings.of(
                GeneralSettingSpecConstants.CLIENT_ENDPOINTS_BIND_UDP_PORT_RANGES.newSetting(
                        portRanges));
        Assert.assertEquals(
                portRanges,
                Socks5ValueDerivationHelper.getSocks5OnUdpAssociateRequestClientFacingDatagramSocketBindPortRangesFrom(rule, settings));
    }

    @Test
    public void testGetSocks5OnUdpAssociateRequestClientFacingDatagramSocketBindPortRangesFromRuleSettings09() {
        PortRange portRange = PortRange.of(
                Port.valueOf(1), Port.valueOf(65535));
        PortRanges portRanges = PortRanges.of(portRange);
        Rule rule = new Rule.Builder().build();
        Settings settings = Settings.of(
                SocksSettingSpecConstants.SOCKS_ON_REQUEST_CLIENT_ENDPOINTS_BIND_UDP_PORT_RANGES.newSetting(
                        portRanges));
        Assert.assertEquals(
                portRanges,
                Socks5ValueDerivationHelper.getSocks5OnUdpAssociateRequestClientFacingDatagramSocketBindPortRangesFrom(rule, settings));
    }

    @Test
    public void testGetSocks5OnUdpAssociateRequestClientFacingDatagramSocketBindPortRangesFromRuleSettings10() {
        PortRange portRange = PortRange.of(
                Port.valueOf(1), Port.valueOf(65535));
        PortRanges portRanges = PortRanges.of(portRange);
        Rule rule = new Rule.Builder().build();
        Settings settings = Settings.of(
                SocksSettingSpecConstants.SOCKS_ON_UDP_ASSOCIATE_REQUEST_CLIENT_FACING_DATAGRAM_SOCKET_BIND_PORT_RANGES.newSetting(
                        portRanges));
        Assert.assertEquals(
                portRanges,
                Socks5ValueDerivationHelper.getSocks5OnUdpAssociateRequestClientFacingDatagramSocketBindPortRangesFrom(rule, settings));
    }

    @Test
    public void testGetSocks5OnUdpAssociateRequestClientFacingDatagramSocketBindPortRangesFromRuleSettings11() {
        PortRange portRange = PortRange.of(
                Port.valueOf(1), Port.valueOf(65535));
        PortRanges portRanges = PortRanges.of(portRange);
        Rule rule = new Rule.Builder().build();
        Settings settings = Settings.of(
                Socks5SettingSpecConstants.SOCKS5_ON_UDP_ASSOCIATE_REQUEST_CLIENT_FACING_DATAGRAM_SOCKET_BIND_PORT_RANGES.newSetting(
                        portRanges));
        Assert.assertEquals(
                portRanges,
                Socks5ValueDerivationHelper.getSocks5OnUdpAssociateRequestClientFacingDatagramSocketBindPortRangesFrom(rule, settings));
    }

    @Test
    public void testGetSocks5OnUdpAssociateRequestClientFacingDatagramSocketSocketSettingsFromRuleSettings01() {
        SocketSettings socketSettings = SocketSettings.of();
        Rule rule = new Rule.Builder().build();
        Settings settings = Settings.of();
        Assert.assertEquals(
                socketSettings,
                Socks5ValueDerivationHelper.getSocks5OnUdpAssociateRequestClientFacingDatagramSocketSocketSettingsFrom(rule, settings));
    }

    @Test
    public void testGetSocks5OnUdpAssociateRequestClientFacingDatagramSocketSocketSettingsFromRuleSettings02() {
        SocketSetting<?> socketSetting =
                StandardSocketSettingSpecConstants.SO_TIMEOUT.newSocketSetting(
                        NonNegativeInteger.valueOf(0));
        @SuppressWarnings("unchecked")
        SocketSetting<Object> socketSttng = (SocketSetting<Object>) socketSetting;
        SocketSettings socketSettings = SocketSettings.of(socketSttng);
        Rule rule = new Rule.Builder()
                .addRuleAction(GeneralRuleActionSpecConstants.SOCKET_SETTING.newRuleAction(
                        socketSttng))
                .build();
        Settings settings = Settings.of();
        Assert.assertEquals(
                socketSettings,
                Socks5ValueDerivationHelper.getSocks5OnUdpAssociateRequestClientFacingDatagramSocketSocketSettingsFrom(rule, settings));
    }

    @Test
    public void testGetSocks5OnUdpAssociateRequestClientFacingDatagramSocketSocketSettingsFromRuleSettings03() {
        SocketSetting<?> socketSetting =
                StandardSocketSettingSpecConstants.SO_TIMEOUT.newSocketSetting(
                        NonNegativeInteger.valueOf(0));
        @SuppressWarnings("unchecked")
        SocketSetting<Object> socketSttng = (SocketSetting<Object>) socketSetting;
        SocketSettings socketSettings = SocketSettings.of(socketSttng);
        Rule rule = new Rule.Builder()
                .addRuleAction(GeneralRuleActionSpecConstants.CLIENT_ENDPOINTS_SOCKET_SETTING.newRuleAction(
                        socketSttng))
                .build();
        Settings settings = Settings.of();
        Assert.assertEquals(
                socketSettings,
                Socks5ValueDerivationHelper.getSocks5OnUdpAssociateRequestClientFacingDatagramSocketSocketSettingsFrom(rule, settings));
    }

    @Test
    public void testGetSocks5OnUdpAssociateRequestClientFacingDatagramSocketSocketSettingsFromRuleSettings04() {
        SocketSetting<?> socketSetting =
                StandardSocketSettingSpecConstants.SO_TIMEOUT.newSocketSetting(
                        NonNegativeInteger.valueOf(0));
        @SuppressWarnings("unchecked")
        SocketSetting<Object> socketSttng = (SocketSetting<Object>) socketSetting;
        SocketSettings socketSettings = SocketSettings.of(socketSttng);
        Rule rule = new Rule.Builder()
                .addRuleAction(SocksRuleActionSpecConstants.SOCKS_ON_REQUEST_CLIENT_ENDPOINTS_SOCKET_SETTING.newRuleAction(
                        socketSttng))
                .build();
        Settings settings = Settings.of();
        Assert.assertEquals(
                socketSettings,
                Socks5ValueDerivationHelper.getSocks5OnUdpAssociateRequestClientFacingDatagramSocketSocketSettingsFrom(rule, settings));
    }

    @Test
    public void testGetSocks5OnUdpAssociateRequestClientFacingDatagramSocketSocketSettingsFromRuleSettings05() {
        SocketSetting<?> socketSetting =
                StandardSocketSettingSpecConstants.SO_TIMEOUT.newSocketSetting(
                        NonNegativeInteger.valueOf(0));
        @SuppressWarnings("unchecked")
        SocketSetting<Object> socketSttng = (SocketSetting<Object>) socketSetting;
        SocketSettings socketSettings = SocketSettings.of(socketSttng);
        Rule rule = new Rule.Builder()
                .addRuleAction(SocksRuleActionSpecConstants.SOCKS_ON_UDP_ASSOCIATE_REQUEST_CLIENT_FACING_DATAGRAM_SOCKET_SOCKET_SETTING.newRuleAction(
                        socketSttng))
                .build();
        Settings settings = Settings.of();
        Assert.assertEquals(
                socketSettings,
                Socks5ValueDerivationHelper.getSocks5OnUdpAssociateRequestClientFacingDatagramSocketSocketSettingsFrom(rule, settings));
    }

    @Test
    public void testGetSocks5OnUdpAssociateRequestClientFacingDatagramSocketSocketSettingsFromRuleSettings06() {
        SocketSetting<?> socketSetting =
                StandardSocketSettingSpecConstants.SO_TIMEOUT.newSocketSetting(
                        NonNegativeInteger.valueOf(0));
        @SuppressWarnings("unchecked")
        SocketSetting<Object> socketSttng = (SocketSetting<Object>) socketSetting;
        SocketSettings socketSettings = SocketSettings.of(socketSttng);
        Rule rule = new Rule.Builder()
                .addRuleAction(Socks5RuleActionSpecConstants.SOCKS5_ON_UDP_ASSOCIATE_REQUEST_CLIENT_FACING_DATAGRAM_SOCKET_SOCKET_SETTING.newRuleAction(
                        socketSttng))
                .build();
        Settings settings = Settings.of();
        Assert.assertEquals(
                socketSettings,
                Socks5ValueDerivationHelper.getSocks5OnUdpAssociateRequestClientFacingDatagramSocketSocketSettingsFrom(rule, settings));
    }

    @Test
    public void testGetSocks5OnUdpAssociateRequestClientFacingDatagramSocketSocketSettingsFromRuleSettings07() {
        SocketSetting<?> socketSetting =
                StandardSocketSettingSpecConstants.SO_TIMEOUT.newSocketSetting(
                        NonNegativeInteger.valueOf(0));
        SocketSettings socketSettings = SocketSettings.of(socketSetting);
        Rule rule = new Rule.Builder().build();
        Settings settings = Settings.of(
                GeneralSettingSpecConstants.SOCKET_SETTINGS.newSetting(
                        socketSettings));
        Assert.assertEquals(
                socketSettings,
                Socks5ValueDerivationHelper.getSocks5OnUdpAssociateRequestClientFacingDatagramSocketSocketSettingsFrom(rule, settings));
    }

    @Test
    public void testGetSocks5OnUdpAssociateRequestClientFacingDatagramSocketSocketSettingsFromRuleSettings08() {
        SocketSetting<?> socketSetting =
                StandardSocketSettingSpecConstants.SO_TIMEOUT.newSocketSetting(
                        NonNegativeInteger.valueOf(0));
        SocketSettings socketSettings = SocketSettings.of(socketSetting);
        Rule rule = new Rule.Builder().build();
        Settings settings = Settings.of(
                GeneralSettingSpecConstants.CLIENT_ENDPOINTS_SOCKET_SETTINGS.newSetting(
                        socketSettings));
        Assert.assertEquals(
                socketSettings,
                Socks5ValueDerivationHelper.getSocks5OnUdpAssociateRequestClientFacingDatagramSocketSocketSettingsFrom(rule, settings));
    }

    @Test
    public void testGetSocks5OnUdpAssociateRequestClientFacingDatagramSocketSocketSettingsFromRuleSettings09() {
        SocketSetting<?> socketSetting =
                StandardSocketSettingSpecConstants.SO_TIMEOUT.newSocketSetting(
                        NonNegativeInteger.valueOf(0));
        SocketSettings socketSettings = SocketSettings.of(socketSetting);
        Rule rule = new Rule.Builder().build();
        Settings settings = Settings.of(
                SocksSettingSpecConstants.SOCKS_ON_REQUEST_CLIENT_ENDPOINTS_SOCKET_SETTINGS.newSetting(
                        socketSettings));
        Assert.assertEquals(
                socketSettings,
                Socks5ValueDerivationHelper.getSocks5OnUdpAssociateRequestClientFacingDatagramSocketSocketSettingsFrom(rule, settings));
    }

    @Test
    public void testGetSocks5OnUdpAssociateRequestClientFacingDatagramSocketSocketSettingsFromRuleSettings10() {
        SocketSetting<?> socketSetting =
                StandardSocketSettingSpecConstants.SO_TIMEOUT.newSocketSetting(
                        NonNegativeInteger.valueOf(0));
        SocketSettings socketSettings = SocketSettings.of(socketSetting);
        Rule rule = new Rule.Builder().build();
        Settings settings = Settings.of(
                SocksSettingSpecConstants.SOCKS_ON_UDP_ASSOCIATE_REQUEST_CLIENT_FACING_DATAGRAM_SOCKET_SOCKET_SETTINGS.newSetting(
                        socketSettings));
        Assert.assertEquals(
                socketSettings,
                Socks5ValueDerivationHelper.getSocks5OnUdpAssociateRequestClientFacingDatagramSocketSocketSettingsFrom(rule, settings));
    }

    @Test
    public void testGetSocks5OnUdpAssociateRequestClientFacingDatagramSocketSocketSettingsFromRuleSettings11() {
        SocketSetting<?> socketSetting =
                StandardSocketSettingSpecConstants.SO_TIMEOUT.newSocketSetting(
                        NonNegativeInteger.valueOf(0));
        SocketSettings socketSettings = SocketSettings.of(socketSetting);
        Rule rule = new Rule.Builder().build();
        Settings settings = Settings.of(
                Socks5SettingSpecConstants.SOCKS5_ON_UDP_ASSOCIATE_REQUEST_CLIENT_FACING_DATAGRAM_SOCKET_SOCKET_SETTINGS.newSetting(
                        socketSettings));
        Assert.assertEquals(
                socketSettings,
                Socks5ValueDerivationHelper.getSocks5OnUdpAssociateRequestClientFacingDatagramSocketSocketSettingsFrom(rule, settings));
    }

    @Test
    public void testGetSocks5OnUdpAssociateRequestRelayBufferSizeFromRuleSettings01() {
        PositiveInteger relayBufferSize = PositiveInteger.valueOf(1024);
        Rule rule = new Rule.Builder().build();
        Settings settings = Settings.of();
        Assert.assertEquals(
                relayBufferSize,
                Socks5ValueDerivationHelper.getSocks5OnUdpAssociateRequestRelayBufferSizeFrom(rule, settings));
    }

    @Test
    public void testGetSocks5OnUdpAssociateRequestRelayBufferSizeFromRuleSettings02() {
        PositiveInteger relayBufferSize = PositiveInteger.valueOf(2048);
        Rule rule = new Rule.Builder()
                .addRuleAction(SocksRuleActionSpecConstants.SOCKS_ON_REQUEST_RELAY_BUFFER_SIZE.newRuleAction(
                        relayBufferSize))
                .build();
        Settings settings = Settings.of();
        Assert.assertEquals(
                relayBufferSize,
                Socks5ValueDerivationHelper.getSocks5OnUdpAssociateRequestRelayBufferSizeFrom(rule, settings));
    }

    @Test
    public void testGetSocks5OnUdpAssociateRequestRelayBufferSizeFromRuleSettings03() {
        PositiveInteger relayBufferSize = PositiveInteger.valueOf(2048);
        Rule rule = new Rule.Builder()
                .addRuleAction(SocksRuleActionSpecConstants.SOCKS_ON_UDP_ASSOCIATE_REQUEST_RELAY_BUFFER_SIZE.newRuleAction(
                        relayBufferSize))
                .build();
        Settings settings = Settings.of();
        Assert.assertEquals(
                relayBufferSize,
                Socks5ValueDerivationHelper.getSocks5OnUdpAssociateRequestRelayBufferSizeFrom(rule, settings));
    }

    @Test
    public void testGetSocks5OnUdpAssociateRequestRelayBufferSizeFromRuleSettings04() {
        PositiveInteger relayBufferSize = PositiveInteger.valueOf(2048);
        Rule rule = new Rule.Builder()
                .addRuleAction(Socks5RuleActionSpecConstants.SOCKS5_ON_UDP_ASSOCIATE_REQUEST_RELAY_BUFFER_SIZE.newRuleAction(
                        relayBufferSize))
                .build();
        Settings settings = Settings.of();
        Assert.assertEquals(
                relayBufferSize,
                Socks5ValueDerivationHelper.getSocks5OnUdpAssociateRequestRelayBufferSizeFrom(rule, settings));
    }

    @Test
    public void testGetSocks5OnUdpAssociateRequestRelayBufferSizeFromRuleSettings05() {
        PositiveInteger relayBufferSize = PositiveInteger.valueOf(2048);
        Rule rule = new Rule.Builder().build();
        Settings settings = Settings.of(
                SocksSettingSpecConstants.SOCKS_ON_REQUEST_RELAY_BUFFER_SIZE.newSetting(
                        relayBufferSize));
        Assert.assertEquals(
                relayBufferSize,
                Socks5ValueDerivationHelper.getSocks5OnUdpAssociateRequestRelayBufferSizeFrom(rule, settings));
    }

    @Test
    public void testGetSocks5OnUdpAssociateRequestRelayBufferSizeFromRuleSettings06() {
        PositiveInteger relayBufferSize = PositiveInteger.valueOf(2048);
        Rule rule = new Rule.Builder().build();
        Settings settings = Settings.of(
                SocksSettingSpecConstants.SOCKS_ON_UDP_ASSOCIATE_REQUEST_RELAY_BUFFER_SIZE.newSetting(
                        relayBufferSize));
        Assert.assertEquals(
                relayBufferSize,
                Socks5ValueDerivationHelper.getSocks5OnUdpAssociateRequestRelayBufferSizeFrom(rule, settings));
    }

    @Test
    public void testGetSocks5OnUdpAssociateRequestRelayBufferSizeFromRuleSettings07() {
        PositiveInteger relayBufferSize = PositiveInteger.valueOf(2048);
        Rule rule = new Rule.Builder().build();
        Settings settings = Settings.of(
                Socks5SettingSpecConstants.SOCKS5_ON_UDP_ASSOCIATE_REQUEST_RELAY_BUFFER_SIZE.newSetting(
                        relayBufferSize));
        Assert.assertEquals(
                relayBufferSize,
                Socks5ValueDerivationHelper.getSocks5OnUdpAssociateRequestRelayBufferSizeFrom(rule, settings));
    }

    @Test
    public void testGetSocks5OnUdpAssociateRequestRelayIdleTimeoutFromRuleSettings01() {
        PositiveInteger relayIdleTimeout = PositiveInteger.valueOf(60000);
        Rule rule = new Rule.Builder().build();
        Settings settings = Settings.of();
        Assert.assertEquals(
                relayIdleTimeout,
                Socks5ValueDerivationHelper.getSocks5OnUdpAssociateRequestRelayIdleTimeoutFrom(rule, settings));
    }

    @Test
    public void testGetSocks5OnUdpAssociateRequestRelayIdleTimeoutFromRuleSettings02() {
        PositiveInteger relayIdleTimeout = PositiveInteger.valueOf(120000);
        Rule rule = new Rule.Builder()
                .addRuleAction(SocksRuleActionSpecConstants.SOCKS_ON_REQUEST_RELAY_IDLE_TIMEOUT.newRuleAction(
                        relayIdleTimeout))
                .build();
        Settings settings = Settings.of();
        Assert.assertEquals(
                relayIdleTimeout,
                Socks5ValueDerivationHelper.getSocks5OnUdpAssociateRequestRelayIdleTimeoutFrom(rule, settings));
    }

    @Test
    public void testGetSocks5OnUdpAssociateRequestRelayIdleTimeoutFromRuleSettings03() {
        PositiveInteger relayIdleTimeout = PositiveInteger.valueOf(120000);
        Rule rule = new Rule.Builder()
                .addRuleAction(SocksRuleActionSpecConstants.SOCKS_ON_UDP_ASSOCIATE_REQUEST_RELAY_IDLE_TIMEOUT.newRuleAction(
                        relayIdleTimeout))
                .build();
        Settings settings = Settings.of();
        Assert.assertEquals(
                relayIdleTimeout,
                Socks5ValueDerivationHelper.getSocks5OnUdpAssociateRequestRelayIdleTimeoutFrom(rule, settings));
    }

    @Test
    public void testGetSocks5OnUdpAssociateRequestRelayIdleTimeoutFromRuleSettings04() {
        PositiveInteger relayIdleTimeout = PositiveInteger.valueOf(120000);
        Rule rule = new Rule.Builder()
                .addRuleAction(Socks5RuleActionSpecConstants.SOCKS5_ON_UDP_ASSOCIATE_REQUEST_RELAY_IDLE_TIMEOUT.newRuleAction(
                        relayIdleTimeout))
                .build();
        Settings settings = Settings.of();
        Assert.assertEquals(
                relayIdleTimeout,
                Socks5ValueDerivationHelper.getSocks5OnUdpAssociateRequestRelayIdleTimeoutFrom(rule, settings));
    }

    @Test
    public void testGetSocks5OnUdpAssociateRequestRelayIdleTimeoutFromRuleSettings05() {
        PositiveInteger relayIdleTimeout = PositiveInteger.valueOf(120000);
        Rule rule = new Rule.Builder().build();
        Settings settings = Settings.of(
                SocksSettingSpecConstants.SOCKS_ON_REQUEST_RELAY_IDLE_TIMEOUT.newSetting(
                        relayIdleTimeout));
        Assert.assertEquals(
                relayIdleTimeout,
                Socks5ValueDerivationHelper.getSocks5OnUdpAssociateRequestRelayIdleTimeoutFrom(rule, settings));
    }

    @Test
    public void testGetSocks5OnUdpAssociateRequestRelayIdleTimeoutFromRuleSettings06() {
        PositiveInteger relayIdleTimeout = PositiveInteger.valueOf(120000);
        Rule rule = new Rule.Builder().build();
        Settings settings = Settings.of(
                SocksSettingSpecConstants.SOCKS_ON_UDP_ASSOCIATE_REQUEST_RELAY_IDLE_TIMEOUT.newSetting(
                        relayIdleTimeout));
        Assert.assertEquals(
                relayIdleTimeout,
                Socks5ValueDerivationHelper.getSocks5OnUdpAssociateRequestRelayIdleTimeoutFrom(rule, settings));
    }

    @Test
    public void testGetSocks5OnUdpAssociateRequestRelayIdleTimeoutFromRuleSettings07() {
        PositiveInteger relayIdleTimeout = PositiveInteger.valueOf(120000);
        Rule rule = new Rule.Builder().build();
        Settings settings = Settings.of(
                Socks5SettingSpecConstants.SOCKS5_ON_UDP_ASSOCIATE_REQUEST_RELAY_IDLE_TIMEOUT.newSetting(
                        relayIdleTimeout));
        Assert.assertEquals(
                relayIdleTimeout,
                Socks5ValueDerivationHelper.getSocks5OnUdpAssociateRequestRelayIdleTimeoutFrom(rule, settings));
    }

    @Test
    public void testGetSocks5OnUdpAssociateRequestRelayInboundBandwidthLimitFromRuleSettings01() {
        Rule rule = new Rule.Builder().build();
        Settings settings = Settings.of();
        Assert.assertNull(
                Socks5ValueDerivationHelper.getSocks5OnUdpAssociateRequestRelayInboundBandwidthLimitFrom(rule, settings));
    }

    @Test
    public void testGetSocks5OnUdpAssociateRequestRelayInboundBandwidthLimitFromRuleSettings02() {
        PositiveInteger relayInboundBandwidthLimit = PositiveInteger.valueOf(
                Integer.MAX_VALUE);
        Rule rule = new Rule.Builder()
                .addRuleAction(SocksRuleActionSpecConstants.SOCKS_ON_REQUEST_RELAY_INBOUND_BANDWIDTH_LIMIT.newRuleAction(
                        relayInboundBandwidthLimit))
                .build();
        Settings settings = Settings.of();
        Assert.assertEquals(
                relayInboundBandwidthLimit,
                Socks5ValueDerivationHelper.getSocks5OnUdpAssociateRequestRelayInboundBandwidthLimitFrom(rule, settings));
    }

    @Test
    public void testGetSocks5OnUdpAssociateRequestRelayInboundBandwidthLimitFromRuleSettings03() {
        PositiveInteger relayInboundBandwidthLimit = PositiveInteger.valueOf(
                Integer.MAX_VALUE);
        Rule rule = new Rule.Builder()
                .addRuleAction(SocksRuleActionSpecConstants.SOCKS_ON_UDP_ASSOCIATE_REQUEST_RELAY_INBOUND_BANDWIDTH_LIMIT.newRuleAction(
                        relayInboundBandwidthLimit))
                .build();
        Settings settings = Settings.of();
        Assert.assertEquals(
                relayInboundBandwidthLimit,
                Socks5ValueDerivationHelper.getSocks5OnUdpAssociateRequestRelayInboundBandwidthLimitFrom(rule, settings));
    }

    @Test
    public void testGetSocks5OnUdpAssociateRequestRelayInboundBandwidthLimitFromRuleSettings04() {
        PositiveInteger relayInboundBandwidthLimit = PositiveInteger.valueOf(
                Integer.MAX_VALUE);
        Rule rule = new Rule.Builder()
                .addRuleAction(Socks5RuleActionSpecConstants.SOCKS5_ON_UDP_ASSOCIATE_REQUEST_RELAY_INBOUND_BANDWIDTH_LIMIT.newRuleAction(
                        relayInboundBandwidthLimit))
                .build();
        Settings settings = Settings.of();
        Assert.assertEquals(
                relayInboundBandwidthLimit,
                Socks5ValueDerivationHelper.getSocks5OnUdpAssociateRequestRelayInboundBandwidthLimitFrom(rule, settings));
    }

    @Test
    public void testGetSocks5OnUdpAssociateRequestRelayInboundBandwidthLimitFromRuleSettings05() {
        PositiveInteger relayInboundBandwidthLimit = PositiveInteger.valueOf(
                Integer.MAX_VALUE);
        Rule rule = new Rule.Builder().build();
        Settings settings = Settings.of(
                SocksSettingSpecConstants.SOCKS_ON_REQUEST_RELAY_INBOUND_BANDWIDTH_LIMIT.newSetting(
                        relayInboundBandwidthLimit));
        Assert.assertEquals(
                relayInboundBandwidthLimit,
                Socks5ValueDerivationHelper.getSocks5OnUdpAssociateRequestRelayInboundBandwidthLimitFrom(rule, settings));
    }

    @Test
    public void testGetSocks5OnUdpAssociateRequestRelayInboundBandwidthLimitFromRuleSettings06() {
        PositiveInteger relayInboundBandwidthLimit = PositiveInteger.valueOf(
                Integer.MAX_VALUE);
        Rule rule = new Rule.Builder().build();
        Settings settings = Settings.of(
                SocksSettingSpecConstants.SOCKS_ON_UDP_ASSOCIATE_REQUEST_RELAY_INBOUND_BANDWIDTH_LIMIT.newSetting(
                        relayInboundBandwidthLimit));
        Assert.assertEquals(
                relayInboundBandwidthLimit,
                Socks5ValueDerivationHelper.getSocks5OnUdpAssociateRequestRelayInboundBandwidthLimitFrom(rule, settings));
    }

    @Test
    public void testGetSocks5OnUdpAssociateRequestRelayInboundBandwidthLimitFromRuleSettings07() {
        PositiveInteger relayInboundBandwidthLimit = PositiveInteger.valueOf(
                Integer.MAX_VALUE);
        Rule rule = new Rule.Builder().build();
        Settings settings = Settings.of(
                Socks5SettingSpecConstants.SOCKS5_ON_UDP_ASSOCIATE_REQUEST_RELAY_INBOUND_BANDWIDTH_LIMIT.newSetting(
                        relayInboundBandwidthLimit));
        Assert.assertEquals(
                relayInboundBandwidthLimit,
                Socks5ValueDerivationHelper.getSocks5OnUdpAssociateRequestRelayInboundBandwidthLimitFrom(rule, settings));
    }

    @Test
    public void testGetSocks5OnUdpAssociateRequestRelayOutboundBandwidthLimitFromRuleSettings01() {
        Rule rule = new Rule.Builder().build();
        Settings settings = Settings.of();
        Assert.assertNull(
                Socks5ValueDerivationHelper.getSocks5OnUdpAssociateRequestRelayOutboundBandwidthLimitFrom(rule, settings));
    }

    @Test
    public void testGetSocks5OnUdpAssociateRequestRelayOutboundBandwidthLimitFromRuleSettings02() {
        PositiveInteger relayOutboundBandwidthLimit = PositiveInteger.valueOf(
                Integer.MAX_VALUE);
        Rule rule = new Rule.Builder()
                .addRuleAction(SocksRuleActionSpecConstants.SOCKS_ON_REQUEST_RELAY_OUTBOUND_BANDWIDTH_LIMIT.newRuleAction(
                        relayOutboundBandwidthLimit))
                .build();
        Settings settings = Settings.of();
        Assert.assertEquals(
                relayOutboundBandwidthLimit,
                Socks5ValueDerivationHelper.getSocks5OnUdpAssociateRequestRelayOutboundBandwidthLimitFrom(rule, settings));
    }

    @Test
    public void testGetSocks5OnUdpAssociateRequestRelayOutboundBandwidthLimitFromRuleSettings03() {
        PositiveInteger relayOutboundBandwidthLimit = PositiveInteger.valueOf(
                Integer.MAX_VALUE);
        Rule rule = new Rule.Builder()
                .addRuleAction(SocksRuleActionSpecConstants.SOCKS_ON_UDP_ASSOCIATE_REQUEST_RELAY_OUTBOUND_BANDWIDTH_LIMIT.newRuleAction(
                        relayOutboundBandwidthLimit))
                .build();
        Settings settings = Settings.of();
        Assert.assertEquals(
                relayOutboundBandwidthLimit,
                Socks5ValueDerivationHelper.getSocks5OnUdpAssociateRequestRelayOutboundBandwidthLimitFrom(rule, settings));
    }

    @Test
    public void testGetSocks5OnUdpAssociateRequestRelayOutboundBandwidthLimitFromRuleSettings04() {
        PositiveInteger relayOutboundBandwidthLimit = PositiveInteger.valueOf(
                Integer.MAX_VALUE);
        Rule rule = new Rule.Builder()
                .addRuleAction(Socks5RuleActionSpecConstants.SOCKS5_ON_UDP_ASSOCIATE_REQUEST_RELAY_OUTBOUND_BANDWIDTH_LIMIT.newRuleAction(
                        relayOutboundBandwidthLimit))
                .build();
        Settings settings = Settings.of();
        Assert.assertEquals(
                relayOutboundBandwidthLimit,
                Socks5ValueDerivationHelper.getSocks5OnUdpAssociateRequestRelayOutboundBandwidthLimitFrom(rule, settings));
    }

    @Test
    public void testGetSocks5OnUdpAssociateRequestRelayOutboundBandwidthLimitFromRuleSettings05() {
        PositiveInteger relayOutboundBandwidthLimit = PositiveInteger.valueOf(
                Integer.MAX_VALUE);
        Rule rule = new Rule.Builder().build();
        Settings settings = Settings.of(
                SocksSettingSpecConstants.SOCKS_ON_REQUEST_RELAY_OUTBOUND_BANDWIDTH_LIMIT.newSetting(
                        relayOutboundBandwidthLimit));
        Assert.assertEquals(
                relayOutboundBandwidthLimit,
                Socks5ValueDerivationHelper.getSocks5OnUdpAssociateRequestRelayOutboundBandwidthLimitFrom(rule, settings));
    }

    @Test
    public void testGetSocks5OnUdpAssociateRequestRelayOutboundBandwidthLimitFromRuleSettings06() {
        PositiveInteger relayOutboundBandwidthLimit = PositiveInteger.valueOf(
                Integer.MAX_VALUE);
        Rule rule = new Rule.Builder().build();
        Settings settings = Settings.of(
                SocksSettingSpecConstants.SOCKS_ON_UDP_ASSOCIATE_REQUEST_RELAY_OUTBOUND_BANDWIDTH_LIMIT.newSetting(
                        relayOutboundBandwidthLimit));
        Assert.assertEquals(
                relayOutboundBandwidthLimit,
                Socks5ValueDerivationHelper.getSocks5OnUdpAssociateRequestRelayOutboundBandwidthLimitFrom(rule, settings));
    }

    @Test
    public void testGetSocks5OnUdpAssociateRequestRelayOutboundBandwidthLimitFromRuleSettings07() {
        PositiveInteger relayOutboundBandwidthLimit = PositiveInteger.valueOf(
                Integer.MAX_VALUE);
        Rule rule = new Rule.Builder().build();
        Settings settings = Settings.of(
                Socks5SettingSpecConstants.SOCKS5_ON_UDP_ASSOCIATE_REQUEST_RELAY_OUTBOUND_BANDWIDTH_LIMIT.newSetting(
                        relayOutboundBandwidthLimit));
        Assert.assertEquals(
                relayOutboundBandwidthLimit,
                Socks5ValueDerivationHelper.getSocks5OnUdpAssociateRequestRelayOutboundBandwidthLimitFrom(rule, settings));
    }

    @Test
    public void testGetSocks5OnUdpAssociateRequestRemoteFacingDatagramSocketBindHostFromRuleSettings01() {
        Host host = HostIpv4Address.getAllZerosInstance();
        Rule rule = new Rule.Builder().build();
        Settings settings = Settings.of();
        Assert.assertEquals(
                host,
                Socks5ValueDerivationHelper.getSocks5OnUdpAssociateRequestRemoteFacingDatagramSocketBindHostFrom(rule, settings));
    }

    @Test
    public void testGetSocks5OnUdpAssociateRequestRemoteFacingDatagramSocketBindHostFromRuleSettings02() {
        Host host = Host.newInstance("127.0.0.1");
        Rule rule = new Rule.Builder()
                .addRuleAction(GeneralRuleActionSpecConstants.BIND_HOST.newRuleAction(
                        host))
                .build();
        Settings settings = Settings.of();
        Assert.assertEquals(
                host,
                Socks5ValueDerivationHelper.getSocks5OnUdpAssociateRequestRemoteFacingDatagramSocketBindHostFrom(rule, settings));
    }

    @Test
    public void testGetSocks5OnUdpAssociateRequestRemoteFacingDatagramSocketBindHostFromRuleSettings03() {
        Host host = Host.newInstance("127.0.0.1");
        Rule rule = new Rule.Builder()
                .addRuleAction(GeneralRuleActionSpecConstants.REMOTE_ENDPOINTS_BIND_HOST.newRuleAction(
                        host))
                .build();
        Settings settings = Settings.of();
        Assert.assertEquals(
                host,
                Socks5ValueDerivationHelper.getSocks5OnUdpAssociateRequestRemoteFacingDatagramSocketBindHostFrom(rule, settings));
    }

    @Test
    public void testGetSocks5OnUdpAssociateRequestRemoteFacingDatagramSocketBindHostFromRuleSettings04() {
        Host host = Host.newInstance("127.0.0.1");
        Rule rule = new Rule.Builder()
                .addRuleAction(SocksRuleActionSpecConstants.SOCKS_ON_REQUEST_REMOTE_ENDPOINTS_BIND_HOST.newRuleAction(
                        host))
                .build();
        Settings settings = Settings.of();
        Assert.assertEquals(
                host,
                Socks5ValueDerivationHelper.getSocks5OnUdpAssociateRequestRemoteFacingDatagramSocketBindHostFrom(rule, settings));
    }

    @Test
    public void testGetSocks5OnUdpAssociateRequestRemoteFacingDatagramSocketBindHostFromRuleSettings05() {
        Host host = Host.newInstance("127.0.0.1");
        Rule rule = new Rule.Builder()
                .addRuleAction(SocksRuleActionSpecConstants.SOCKS_ON_UDP_ASSOCIATE_REQUEST_REMOTE_FACING_DATAGRAM_SOCKET_BIND_HOST.newRuleAction(
                        host))
                .build();
        Settings settings = Settings.of();
        Assert.assertEquals(
                host,
                Socks5ValueDerivationHelper.getSocks5OnUdpAssociateRequestRemoteFacingDatagramSocketBindHostFrom(rule, settings));
    }

    @Test
    public void testGetSocks5OnUdpAssociateRequestRemoteFacingDatagramSocketBindHostFromRuleSettings06() {
        Host host = Host.newInstance("127.0.0.1");
        Rule rule = new Rule.Builder()
                .addRuleAction(Socks5RuleActionSpecConstants.SOCKS5_ON_UDP_ASSOCIATE_REQUEST_REMOTE_FACING_DATAGRAM_SOCKET_BIND_HOST.newRuleAction(
                        host))
                .build();
        Settings settings = Settings.of();
        Assert.assertEquals(
                host,
                Socks5ValueDerivationHelper.getSocks5OnUdpAssociateRequestRemoteFacingDatagramSocketBindHostFrom(rule, settings));
    }

    @Test
    public void testGetSocks5OnUdpAssociateRequestRemoteFacingDatagramSocketBindHostFromRuleSettings07() throws UnknownHostException, SocketException {
        Host host = Host.newInstance("127.0.0.1");
        NetworkInterface networkInterface = NetworkInterface.getByInetAddress(
                host.toInetAddress());
        NetInterface netInterface = NetInterface.newInstance(networkInterface);
        Rule rule = new Rule.Builder()
                .addRuleAction(GeneralRuleActionSpecConstants.NET_INTERFACE.newRuleAction(
                        netInterface))
                .addRuleAction(GeneralRuleActionSpecConstants.BIND_HOST_ADDRESS_TYPE.newRuleAction(
                        HostAddressType.HOST_IPV4_ADDRESS))
                .build();
        Settings settings = Settings.of();
        Assert.assertTrue(
                netInterface.getHostAddresses().contains((HostAddress)
                        Socks5ValueDerivationHelper.getSocks5OnUdpAssociateRequestRemoteFacingDatagramSocketBindHostFrom(rule, settings)));
    }

    @Test
    public void testGetSocks5OnUdpAssociateRequestRemoteFacingDatagramSocketBindHostFromRuleSettings08() throws UnknownHostException, SocketException {
        Host host = Host.newInstance("127.0.0.1");
        NetworkInterface networkInterface = NetworkInterface.getByInetAddress(
                host.toInetAddress());
        NetInterface netInterface = NetInterface.newInstance(networkInterface);
        Rule rule = new Rule.Builder()
                .addRuleAction(GeneralRuleActionSpecConstants.REMOTE_ENDPOINTS_NET_INTERFACE.newRuleAction(
                        netInterface))
                .addRuleAction(GeneralRuleActionSpecConstants.REMOTE_ENDPOINTS_BIND_HOST_ADDRESS_TYPE.newRuleAction(
                        HostAddressType.HOST_IPV4_ADDRESS))
                .build();
        Settings settings = Settings.of();
        Assert.assertTrue(
                netInterface.getHostAddresses().contains((HostAddress)
                        Socks5ValueDerivationHelper.getSocks5OnUdpAssociateRequestRemoteFacingDatagramSocketBindHostFrom(rule, settings)));
    }

    @Test
    public void testGetSocks5OnUdpAssociateRequestRemoteFacingDatagramSocketBindHostFromRuleSettings09() throws UnknownHostException, SocketException {
        Host host = Host.newInstance("127.0.0.1");
        NetworkInterface networkInterface = NetworkInterface.getByInetAddress(
                host.toInetAddress());
        NetInterface netInterface = NetInterface.newInstance(networkInterface);
        Rule rule = new Rule.Builder()
                .addRuleAction(SocksRuleActionSpecConstants.SOCKS_ON_REQUEST_REMOTE_ENDPOINTS_NET_INTERFACE.newRuleAction(
                        netInterface))
                .addRuleAction(SocksRuleActionSpecConstants.SOCKS_ON_REQUEST_REMOTE_ENDPOINTS_BIND_HOST_ADDRESS_TYPE.newRuleAction(
                        HostAddressType.HOST_IPV4_ADDRESS))
                .build();
        Settings settings = Settings.of();
        Assert.assertTrue(
                netInterface.getHostAddresses().contains((HostAddress)
                        Socks5ValueDerivationHelper.getSocks5OnUdpAssociateRequestRemoteFacingDatagramSocketBindHostFrom(rule, settings)));
    }

    @Test
    public void testGetSocks5OnUdpAssociateRequestRemoteFacingDatagramSocketBindHostFromRuleSettings10() throws UnknownHostException, SocketException {
        Host host = Host.newInstance("127.0.0.1");
        NetworkInterface networkInterface = NetworkInterface.getByInetAddress(
                host.toInetAddress());
        NetInterface netInterface = NetInterface.newInstance(networkInterface);
        Rule rule = new Rule.Builder()
                .addRuleAction(SocksRuleActionSpecConstants.SOCKS_ON_UDP_ASSOCIATE_REQUEST_REMOTE_FACING_DATAGRAM_SOCKET_NET_INTERFACE.newRuleAction(
                        netInterface))
                .addRuleAction(SocksRuleActionSpecConstants.SOCKS_ON_UDP_ASSOCIATE_REQUEST_REMOTE_FACING_DATAGRAM_SOCKET_BIND_HOST_ADDRESS_TYPE.newRuleAction(
                        HostAddressType.HOST_IPV4_ADDRESS))
                .build();
        Settings settings = Settings.of();
        Assert.assertTrue(
                netInterface.getHostAddresses().contains((HostAddress)
                        Socks5ValueDerivationHelper.getSocks5OnUdpAssociateRequestRemoteFacingDatagramSocketBindHostFrom(rule, settings)));
    }

    @Test
    public void testGetSocks5OnUdpAssociateRequestRemoteFacingDatagramSocketBindHostFromRuleSettings11() throws UnknownHostException, SocketException {
        Host host = Host.newInstance("127.0.0.1");
        NetworkInterface networkInterface = NetworkInterface.getByInetAddress(
                host.toInetAddress());
        NetInterface netInterface = NetInterface.newInstance(networkInterface);
        Rule rule = new Rule.Builder()
                .addRuleAction(Socks5RuleActionSpecConstants.SOCKS5_ON_UDP_ASSOCIATE_REQUEST_REMOTE_FACING_DATAGRAM_SOCKET_NET_INTERFACE.newRuleAction(
                        netInterface))
                .addRuleAction(Socks5RuleActionSpecConstants.SOCKS5_ON_UDP_ASSOCIATE_REQUEST_REMOTE_FACING_DATAGRAM_SOCKET_BIND_HOST_ADDRESS_TYPE.newRuleAction(
                        HostAddressType.HOST_IPV4_ADDRESS))
                .build();
        Settings settings = Settings.of();
        Assert.assertTrue(
                netInterface.getHostAddresses().contains((HostAddress)
                        Socks5ValueDerivationHelper.getSocks5OnUdpAssociateRequestRemoteFacingDatagramSocketBindHostFrom(rule, settings)));
    }

    @Test
    public void testGetSocks5OnUdpAssociateRequestRemoteFacingDatagramSocketBindHostFromRuleSettings12() {
        Host host = Host.newInstance("127.0.0.1");
        Rule rule = new Rule.Builder().build();
        Settings settings = Settings.of(
                GeneralSettingSpecConstants.BIND_HOST.newSetting(host));
        Assert.assertEquals(
                host,
                Socks5ValueDerivationHelper.getSocks5OnUdpAssociateRequestRemoteFacingDatagramSocketBindHostFrom(rule, settings));
    }

    @Test
    public void testGetSocks5OnUdpAssociateRequestRemoteFacingDatagramSocketBindHostFromRuleSettings13() {
        Host host = Host.newInstance("127.0.0.1");
        Rule rule = new Rule.Builder().build();
        Settings settings = Settings.of(
                GeneralSettingSpecConstants.REMOTE_ENDPOINTS_BIND_HOST.newSetting(
                        host));
        Assert.assertEquals(
                host,
                Socks5ValueDerivationHelper.getSocks5OnUdpAssociateRequestRemoteFacingDatagramSocketBindHostFrom(rule, settings));
    }

    @Test
    public void testGetSocks5OnUdpAssociateRequestRemoteFacingDatagramSocketBindHostFromRuleSettings14() {
        Host host = Host.newInstance("127.0.0.1");
        Rule rule = new Rule.Builder().build();
        Settings settings = Settings.of(
                SocksSettingSpecConstants.SOCKS_ON_REQUEST_REMOTE_ENDPOINTS_BIND_HOST.newSetting(
                        host));
        Assert.assertEquals(
                host,
                Socks5ValueDerivationHelper.getSocks5OnUdpAssociateRequestRemoteFacingDatagramSocketBindHostFrom(rule, settings));
    }

    @Test
    public void testGetSocks5OnUdpAssociateRequestRemoteFacingDatagramSocketBindHostFromRuleSettings15() {
        Host host = Host.newInstance("127.0.0.1");
        Rule rule = new Rule.Builder().build();
        Settings settings = Settings.of(
                SocksSettingSpecConstants.SOCKS_ON_UDP_ASSOCIATE_REQUEST_REMOTE_FACING_DATAGRAM_SOCKET_BIND_HOST.newSetting(
                        host));
        Assert.assertEquals(
                host,
                Socks5ValueDerivationHelper.getSocks5OnUdpAssociateRequestRemoteFacingDatagramSocketBindHostFrom(rule, settings));
    }

    @Test
    public void testGetSocks5OnUdpAssociateRequestRemoteFacingDatagramSocketBindHostFromRuleSettings16() {
        Host host = Host.newInstance("127.0.0.1");
        Rule rule = new Rule.Builder().build();
        Settings settings = Settings.of(
                Socks5SettingSpecConstants.SOCKS5_ON_UDP_ASSOCIATE_REQUEST_REMOTE_FACING_DATAGRAM_SOCKET_BIND_HOST.newSetting(
                        host));
        Assert.assertEquals(
                host,
                Socks5ValueDerivationHelper.getSocks5OnUdpAssociateRequestRemoteFacingDatagramSocketBindHostFrom(rule, settings));
    }

    @Test
    public void testGetSocks5OnUdpAssociateRequestRemoteFacingDatagramSocketBindHostFromRuleSettings17() throws UnknownHostException, SocketException {
        Host host = Host.newInstance("127.0.0.1");
        NetworkInterface networkInterface = NetworkInterface.getByInetAddress(
                host.toInetAddress());
        NetInterface netInterface = NetInterface.newInstance(networkInterface);
        Rule rule = new Rule.Builder().build();
        Settings settings = Settings.of(
                GeneralSettingSpecConstants.NET_INTERFACE.newSetting(
                        netInterface),
                GeneralSettingSpecConstants.BIND_HOST_ADDRESS_TYPES.newSetting(HostAddressTypes.of(
                        HostAddressType.HOST_IPV4_ADDRESS)));
        Assert.assertTrue(
                netInterface.getHostAddresses().contains((HostAddress)
                        Socks5ValueDerivationHelper.getSocks5OnUdpAssociateRequestRemoteFacingDatagramSocketBindHostFrom(rule, settings)));
    }

    @Test
    public void testGetSocks5OnUdpAssociateRequestRemoteFacingDatagramSocketBindHostFromRuleSettings18() throws UnknownHostException, SocketException {
        Host host = Host.newInstance("127.0.0.1");
        NetworkInterface networkInterface = NetworkInterface.getByInetAddress(
                host.toInetAddress());
        NetInterface netInterface = NetInterface.newInstance(networkInterface);
        Rule rule = new Rule.Builder().build();
        Settings settings = Settings.of(
                GeneralSettingSpecConstants.REMOTE_ENDPOINTS_NET_INTERFACE.newSetting(
                        netInterface),
                GeneralSettingSpecConstants.REMOTE_ENDPOINTS_BIND_HOST_ADDRESS_TYPES.newSetting(HostAddressTypes.of(
                        HostAddressType.HOST_IPV4_ADDRESS)));
        Assert.assertTrue(
                netInterface.getHostAddresses().contains((HostAddress)
                        Socks5ValueDerivationHelper.getSocks5OnUdpAssociateRequestRemoteFacingDatagramSocketBindHostFrom(rule, settings)));
    }

    @Test
    public void testGetSocks5OnUdpAssociateRequestRemoteFacingDatagramSocketBindHostFromRuleSettings19() throws UnknownHostException, SocketException {
        Host host = Host.newInstance("127.0.0.1");
        NetworkInterface networkInterface = NetworkInterface.getByInetAddress(
                host.toInetAddress());
        NetInterface netInterface = NetInterface.newInstance(networkInterface);
        Rule rule = new Rule.Builder().build();
        Settings settings = Settings.of(
                SocksSettingSpecConstants.SOCKS_ON_REQUEST_REMOTE_ENDPOINTS_NET_INTERFACE.newSetting(
                        netInterface),
                SocksSettingSpecConstants.SOCKS_ON_REQUEST_REMOTE_ENDPOINTS_BIND_HOST_ADDRESS_TYPES.newSetting(HostAddressTypes.of(
                        HostAddressType.HOST_IPV4_ADDRESS)));
        Assert.assertTrue(
                netInterface.getHostAddresses().contains((HostAddress)
                        Socks5ValueDerivationHelper.getSocks5OnUdpAssociateRequestRemoteFacingDatagramSocketBindHostFrom(rule, settings)));
    }

    @Test
    public void testGetSocks5OnUdpAssociateRequestRemoteFacingDatagramSocketBindHostFromRuleSettings20() throws UnknownHostException, SocketException {
        Host host = Host.newInstance("127.0.0.1");
        NetworkInterface networkInterface = NetworkInterface.getByInetAddress(
                host.toInetAddress());
        NetInterface netInterface = NetInterface.newInstance(networkInterface);
        Rule rule = new Rule.Builder().build();
        Settings settings = Settings.of(
                SocksSettingSpecConstants.SOCKS_ON_UDP_ASSOCIATE_REQUEST_REMOTE_FACING_DATAGRAM_SOCKET_NET_INTERFACE.newSetting(
                        netInterface),
                SocksSettingSpecConstants.SOCKS_ON_UDP_ASSOCIATE_REQUEST_REMOTE_FACING_DATAGRAM_SOCKET_BIND_HOST_ADDRESS_TYPES.newSetting(HostAddressTypes.of(
                        HostAddressType.HOST_IPV4_ADDRESS)));
        Assert.assertTrue(
                netInterface.getHostAddresses().contains((HostAddress)
                        Socks5ValueDerivationHelper.getSocks5OnUdpAssociateRequestRemoteFacingDatagramSocketBindHostFrom(rule, settings)));
    }

    @Test
    public void testGetSocks5OnUdpAssociateRequestRemoteFacingDatagramSocketBindHostFromRuleSettings21() throws UnknownHostException, SocketException {
        Host host = Host.newInstance("127.0.0.1");
        NetworkInterface networkInterface = NetworkInterface.getByInetAddress(
                host.toInetAddress());
        NetInterface netInterface = NetInterface.newInstance(networkInterface);
        Rule rule = new Rule.Builder().build();
        Settings settings = Settings.of(
                Socks5SettingSpecConstants.SOCKS5_ON_UDP_ASSOCIATE_REQUEST_REMOTE_FACING_DATAGRAM_SOCKET_NET_INTERFACE.newSetting(
                        netInterface),
                Socks5SettingSpecConstants.SOCKS5_ON_UDP_ASSOCIATE_REQUEST_REMOTE_FACING_DATAGRAM_SOCKET_BIND_HOST_ADDRESS_TYPES.newSetting(HostAddressTypes.of(
                        HostAddressType.HOST_IPV4_ADDRESS)));
        Assert.assertTrue(
                netInterface.getHostAddresses().contains((HostAddress)
                        Socks5ValueDerivationHelper.getSocks5OnUdpAssociateRequestRemoteFacingDatagramSocketBindHostFrom(rule, settings)));
    }

    @Test
    public void testGetSocks5OnUdpAssociateRequestRemoteFacingDatagramSocketBindPortRangesFromRuleSettings01() {
        PortRanges portRanges = PortRanges.getDefault();
        Rule rule = new Rule.Builder().build();
        Settings settings = Settings.of();
        Assert.assertEquals(
                portRanges,
                Socks5ValueDerivationHelper.getSocks5OnUdpAssociateRequestRemoteFacingDatagramSocketBindPortRangesFrom(rule, settings));
    }

    @Test
    public void testGetSocks5OnUdpAssociateRequestRemoteFacingDatagramSocketBindPortRangesFromRuleSettings02() {
        PortRange portRange = PortRange.of(
                Port.valueOf(1), Port.valueOf(65535));
        PortRanges portRanges = PortRanges.of(portRange);
        Rule rule = new Rule.Builder()
                .addRuleAction(GeneralRuleActionSpecConstants.BIND_UDP_PORT_RANGE.newRuleAction(
                        portRange))
                .build();
        Settings settings = Settings.of();
        Assert.assertEquals(
                portRanges,
                Socks5ValueDerivationHelper.getSocks5OnUdpAssociateRequestRemoteFacingDatagramSocketBindPortRangesFrom(rule, settings));
    }

    @Test
    public void testGetSocks5OnUdpAssociateRequestRemoteFacingDatagramSocketBindPortRangesFromRuleSettings03() {
        PortRange portRange = PortRange.of(
                Port.valueOf(1), Port.valueOf(65535));
        PortRanges portRanges = PortRanges.of(portRange);
        Rule rule = new Rule.Builder()
                .addRuleAction(GeneralRuleActionSpecConstants.REMOTE_ENDPOINTS_BIND_UDP_PORT_RANGE.newRuleAction(
                        portRange))
                .build();
        Settings settings = Settings.of();
        Assert.assertEquals(
                portRanges,
                Socks5ValueDerivationHelper.getSocks5OnUdpAssociateRequestRemoteFacingDatagramSocketBindPortRangesFrom(rule, settings));
    }

    @Test
    public void testGetSocks5OnUdpAssociateRequestRemoteFacingDatagramSocketBindPortRangesFromRuleSettings04() {
        PortRange portRange = PortRange.of(
                Port.valueOf(1), Port.valueOf(65535));
        PortRanges portRanges = PortRanges.of(portRange);
        Rule rule = new Rule.Builder()
                .addRuleAction(SocksRuleActionSpecConstants.SOCKS_ON_REQUEST_REMOTE_ENDPOINTS_BIND_UDP_PORT_RANGE.newRuleAction(
                        portRange))
                .build();
        Settings settings = Settings.of();
        Assert.assertEquals(
                portRanges,
                Socks5ValueDerivationHelper.getSocks5OnUdpAssociateRequestRemoteFacingDatagramSocketBindPortRangesFrom(rule, settings));
    }

    @Test
    public void testGetSocks5OnUdpAssociateRequestRemoteFacingDatagramSocketBindPortRangesFromRuleSettings05() {
        PortRange portRange = PortRange.of(
                Port.valueOf(1), Port.valueOf(65535));
        PortRanges portRanges = PortRanges.of(portRange);
        Rule rule = new Rule.Builder()
                .addRuleAction(SocksRuleActionSpecConstants.SOCKS_ON_UDP_ASSOCIATE_REQUEST_REMOTE_FACING_DATAGRAM_SOCKET_BIND_PORT_RANGE.newRuleAction(
                        portRange))
                .build();
        Settings settings = Settings.of();
        Assert.assertEquals(
                portRanges,
                Socks5ValueDerivationHelper.getSocks5OnUdpAssociateRequestRemoteFacingDatagramSocketBindPortRangesFrom(rule, settings));
    }

    @Test
    public void testGetSocks5OnUdpAssociateRequestRemoteFacingDatagramSocketBindPortRangesFromRuleSettings06() {
        PortRange portRange = PortRange.of(
                Port.valueOf(1), Port.valueOf(65535));
        PortRanges portRanges = PortRanges.of(portRange);
        Rule rule = new Rule.Builder()
                .addRuleAction(Socks5RuleActionSpecConstants.SOCKS5_ON_UDP_ASSOCIATE_REQUEST_REMOTE_FACING_DATAGRAM_SOCKET_BIND_PORT_RANGE.newRuleAction(
                        portRange))
                .build();
        Settings settings = Settings.of();
        Assert.assertEquals(
                portRanges,
                Socks5ValueDerivationHelper.getSocks5OnUdpAssociateRequestRemoteFacingDatagramSocketBindPortRangesFrom(rule, settings));
    }

    @Test
    public void testGetSocks5OnUdpAssociateRequestRemoteFacingDatagramSocketBindPortRangesFromRuleSettings07() {
        PortRange portRange = PortRange.of(
                Port.valueOf(1), Port.valueOf(65535));
        PortRanges portRanges = PortRanges.of(portRange);
        Rule rule = new Rule.Builder().build();
        Settings settings = Settings.of(
                GeneralSettingSpecConstants.BIND_UDP_PORT_RANGES.newSetting(
                        portRanges));
        Assert.assertEquals(
                portRanges,
                Socks5ValueDerivationHelper.getSocks5OnUdpAssociateRequestRemoteFacingDatagramSocketBindPortRangesFrom(rule, settings));
    }

    @Test
    public void testGetSocks5OnUdpAssociateRequestRemoteFacingDatagramSocketBindPortRangesFromRuleSettings08() {
        PortRange portRange = PortRange.of(
                Port.valueOf(1), Port.valueOf(65535));
        PortRanges portRanges = PortRanges.of(portRange);
        Rule rule = new Rule.Builder().build();
        Settings settings = Settings.of(
                GeneralSettingSpecConstants.REMOTE_ENDPOINTS_BIND_UDP_PORT_RANGES.newSetting(
                        portRanges));
        Assert.assertEquals(
                portRanges,
                Socks5ValueDerivationHelper.getSocks5OnUdpAssociateRequestRemoteFacingDatagramSocketBindPortRangesFrom(rule, settings));
    }

    @Test
    public void testGetSocks5OnUdpAssociateRequestRemoteFacingDatagramSocketBindPortRangesFromRuleSettings09() {
        PortRange portRange = PortRange.of(
                Port.valueOf(1), Port.valueOf(65535));
        PortRanges portRanges = PortRanges.of(portRange);
        Rule rule = new Rule.Builder().build();
        Settings settings = Settings.of(
                SocksSettingSpecConstants.SOCKS_ON_REQUEST_REMOTE_ENDPOINTS_BIND_UDP_PORT_RANGES.newSetting(
                        portRanges));
        Assert.assertEquals(
                portRanges,
                Socks5ValueDerivationHelper.getSocks5OnUdpAssociateRequestRemoteFacingDatagramSocketBindPortRangesFrom(rule, settings));
    }

    @Test
    public void testGetSocks5OnUdpAssociateRequestRemoteFacingDatagramSocketBindPortRangesFromRuleSettings10() {
        PortRange portRange = PortRange.of(
                Port.valueOf(1), Port.valueOf(65535));
        PortRanges portRanges = PortRanges.of(portRange);
        Rule rule = new Rule.Builder().build();
        Settings settings = Settings.of(
                SocksSettingSpecConstants.SOCKS_ON_UDP_ASSOCIATE_REQUEST_REMOTE_FACING_DATAGRAM_SOCKET_BIND_PORT_RANGES.newSetting(
                        portRanges));
        Assert.assertEquals(
                portRanges,
                Socks5ValueDerivationHelper.getSocks5OnUdpAssociateRequestRemoteFacingDatagramSocketBindPortRangesFrom(rule, settings));
    }

    @Test
    public void testGetSocks5OnUdpAssociateRequestRemoteFacingDatagramSocketBindPortRangesFromRuleSettings11() {
        PortRange portRange = PortRange.of(
                Port.valueOf(1), Port.valueOf(65535));
        PortRanges portRanges = PortRanges.of(portRange);
        Rule rule = new Rule.Builder().build();
        Settings settings = Settings.of(
                Socks5SettingSpecConstants.SOCKS5_ON_UDP_ASSOCIATE_REQUEST_REMOTE_FACING_DATAGRAM_SOCKET_BIND_PORT_RANGES.newSetting(
                        portRanges));
        Assert.assertEquals(
                portRanges,
                Socks5ValueDerivationHelper.getSocks5OnUdpAssociateRequestRemoteFacingDatagramSocketBindPortRangesFrom(rule, settings));
    }

    @Test
    public void testGetSocks5OnUdpAssociateRequestRemoteFacingDatagramSocketSocketSettingsFromRuleSettings01() {
        SocketSettings socketSettings = SocketSettings.of();
        Rule rule = new Rule.Builder().build();
        Settings settings = Settings.of();
        Assert.assertEquals(
                socketSettings,
                Socks5ValueDerivationHelper.getSocks5OnUdpAssociateRequestRemoteFacingDatagramSocketSocketSettingsFrom(rule, settings));
    }

    @Test
    public void testGetSocks5OnUdpAssociateRequestRemoteFacingDatagramSocketSocketSettingsFromRuleSettings02() {
        SocketSetting<?> socketSetting =
                StandardSocketSettingSpecConstants.SO_TIMEOUT.newSocketSetting(
                        NonNegativeInteger.valueOf(0));
        @SuppressWarnings("unchecked")
        SocketSetting<Object> socketSttng = (SocketSetting<Object>) socketSetting;
        SocketSettings socketSettings = SocketSettings.of(socketSttng);
        Rule rule = new Rule.Builder()
                .addRuleAction(GeneralRuleActionSpecConstants.SOCKET_SETTING.newRuleAction(
                        socketSttng))
                .build();
        Settings settings = Settings.of();
        Assert.assertEquals(
                socketSettings,
                Socks5ValueDerivationHelper.getSocks5OnUdpAssociateRequestRemoteFacingDatagramSocketSocketSettingsFrom(rule, settings));
    }

    @Test
    public void testGetSocks5OnUdpAssociateRequestRemoteFacingDatagramSocketSocketSettingsFromRuleSettings03() {
        SocketSetting<?> socketSetting =
                StandardSocketSettingSpecConstants.SO_TIMEOUT.newSocketSetting(
                        NonNegativeInteger.valueOf(0));
        @SuppressWarnings("unchecked")
        SocketSetting<Object> socketSttng = (SocketSetting<Object>) socketSetting;
        SocketSettings socketSettings = SocketSettings.of(socketSttng);
        Rule rule = new Rule.Builder()
                .addRuleAction(GeneralRuleActionSpecConstants.REMOTE_ENDPOINTS_SOCKET_SETTING.newRuleAction(
                        socketSttng))
                .build();
        Settings settings = Settings.of();
        Assert.assertEquals(
                socketSettings,
                Socks5ValueDerivationHelper.getSocks5OnUdpAssociateRequestRemoteFacingDatagramSocketSocketSettingsFrom(rule, settings));
    }

    @Test
    public void testGetSocks5OnUdpAssociateRequestRemoteFacingDatagramSocketSocketSettingsFromRuleSettings04() {
        SocketSetting<?> socketSetting =
                StandardSocketSettingSpecConstants.SO_TIMEOUT.newSocketSetting(
                        NonNegativeInteger.valueOf(0));
        @SuppressWarnings("unchecked")
        SocketSetting<Object> socketSttng = (SocketSetting<Object>) socketSetting;
        SocketSettings socketSettings = SocketSettings.of(socketSttng);
        Rule rule = new Rule.Builder()
                .addRuleAction(SocksRuleActionSpecConstants.SOCKS_ON_REQUEST_REMOTE_ENDPOINTS_SOCKET_SETTING.newRuleAction(
                        socketSttng))
                .build();
        Settings settings = Settings.of();
        Assert.assertEquals(
                socketSettings,
                Socks5ValueDerivationHelper.getSocks5OnUdpAssociateRequestRemoteFacingDatagramSocketSocketSettingsFrom(rule, settings));
    }

    @Test
    public void testGetSocks5OnUdpAssociateRequestRemoteFacingDatagramSocketSocketSettingsFromRuleSettings05() {
        SocketSetting<?> socketSetting =
                StandardSocketSettingSpecConstants.SO_TIMEOUT.newSocketSetting(
                        NonNegativeInteger.valueOf(0));
        @SuppressWarnings("unchecked")
        SocketSetting<Object> socketSttng = (SocketSetting<Object>) socketSetting;
        SocketSettings socketSettings = SocketSettings.of(socketSttng);
        Rule rule = new Rule.Builder()
                .addRuleAction(SocksRuleActionSpecConstants.SOCKS_ON_UDP_ASSOCIATE_REQUEST_REMOTE_FACING_DATAGRAM_SOCKET_SOCKET_SETTING.newRuleAction(
                        socketSttng))
                .build();
        Settings settings = Settings.of();
        Assert.assertEquals(
                socketSettings,
                Socks5ValueDerivationHelper.getSocks5OnUdpAssociateRequestRemoteFacingDatagramSocketSocketSettingsFrom(rule, settings));
    }

    @Test
    public void testGetSocks5OnUdpAssociateRequestRemoteFacingDatagramSocketSocketSettingsFromRuleSettings06() {
        SocketSetting<?> socketSetting =
                StandardSocketSettingSpecConstants.SO_TIMEOUT.newSocketSetting(
                        NonNegativeInteger.valueOf(0));
        @SuppressWarnings("unchecked")
        SocketSetting<Object> socketSttng = (SocketSetting<Object>) socketSetting;
        SocketSettings socketSettings = SocketSettings.of(socketSttng);
        Rule rule = new Rule.Builder()
                .addRuleAction(Socks5RuleActionSpecConstants.SOCKS5_ON_UDP_ASSOCIATE_REQUEST_REMOTE_FACING_DATAGRAM_SOCKET_SOCKET_SETTING.newRuleAction(
                        socketSttng))
                .build();
        Settings settings = Settings.of();
        Assert.assertEquals(
                socketSettings,
                Socks5ValueDerivationHelper.getSocks5OnUdpAssociateRequestRemoteFacingDatagramSocketSocketSettingsFrom(rule, settings));
    }

    @Test
    public void testGetSocks5OnUdpAssociateRequestRemoteFacingDatagramSocketSocketSettingsFromRuleSettings07() {
        SocketSetting<?> socketSetting =
                StandardSocketSettingSpecConstants.SO_TIMEOUT.newSocketSetting(
                        NonNegativeInteger.valueOf(0));
        SocketSettings socketSettings = SocketSettings.of(socketSetting);
        Rule rule = new Rule.Builder().build();
        Settings settings = Settings.of(
                GeneralSettingSpecConstants.SOCKET_SETTINGS.newSetting(
                        socketSettings));
        Assert.assertEquals(
                socketSettings,
                Socks5ValueDerivationHelper.getSocks5OnUdpAssociateRequestRemoteFacingDatagramSocketSocketSettingsFrom(rule, settings));
    }

    @Test
    public void testGetSocks5OnUdpAssociateRequestRemoteFacingDatagramSocketSocketSettingsFromRuleSettings08() {
        SocketSetting<?> socketSetting =
                StandardSocketSettingSpecConstants.SO_TIMEOUT.newSocketSetting(
                        NonNegativeInteger.valueOf(0));
        SocketSettings socketSettings = SocketSettings.of(socketSetting);
        Rule rule = new Rule.Builder().build();
        Settings settings = Settings.of(
                GeneralSettingSpecConstants.REMOTE_ENDPOINTS_SOCKET_SETTINGS.newSetting(
                        socketSettings));
        Assert.assertEquals(
                socketSettings,
                Socks5ValueDerivationHelper.getSocks5OnUdpAssociateRequestRemoteFacingDatagramSocketSocketSettingsFrom(rule, settings));
    }

    @Test
    public void testGetSocks5OnUdpAssociateRequestRemoteFacingDatagramSocketSocketSettingsFromRuleSettings09() {
        SocketSetting<?> socketSetting =
                StandardSocketSettingSpecConstants.SO_TIMEOUT.newSocketSetting(
                        NonNegativeInteger.valueOf(0));
        SocketSettings socketSettings = SocketSettings.of(socketSetting);
        Rule rule = new Rule.Builder().build();
        Settings settings = Settings.of(
                SocksSettingSpecConstants.SOCKS_ON_REQUEST_REMOTE_ENDPOINTS_SOCKET_SETTINGS.newSetting(
                        socketSettings));
        Assert.assertEquals(
                socketSettings,
                Socks5ValueDerivationHelper.getSocks5OnUdpAssociateRequestRemoteFacingDatagramSocketSocketSettingsFrom(rule, settings));
    }

    @Test
    public void testGetSocks5OnUdpAssociateRequestRemoteFacingDatagramSocketSocketSettingsFromRuleSettings10() {
        SocketSetting<?> socketSetting =
                StandardSocketSettingSpecConstants.SO_TIMEOUT.newSocketSetting(
                        NonNegativeInteger.valueOf(0));
        SocketSettings socketSettings = SocketSettings.of(socketSetting);
        Rule rule = new Rule.Builder().build();
        Settings settings = Settings.of(
                SocksSettingSpecConstants.SOCKS_ON_UDP_ASSOCIATE_REQUEST_REMOTE_FACING_DATAGRAM_SOCKET_SOCKET_SETTINGS.newSetting(
                        socketSettings));
        Assert.assertEquals(
                socketSettings,
                Socks5ValueDerivationHelper.getSocks5OnUdpAssociateRequestRemoteFacingDatagramSocketSocketSettingsFrom(rule, settings));
    }

    @Test
    public void testGetSocks5OnUdpAssociateRequestRemoteFacingDatagramSocketSocketSettingsFromRuleSettings11() {
        SocketSetting<?> socketSetting =
                StandardSocketSettingSpecConstants.SO_TIMEOUT.newSocketSetting(
                        NonNegativeInteger.valueOf(0));
        SocketSettings socketSettings = SocketSettings.of(socketSetting);
        Rule rule = new Rule.Builder().build();
        Settings settings = Settings.of(
                Socks5SettingSpecConstants.SOCKS5_ON_UDP_ASSOCIATE_REQUEST_REMOTE_FACING_DATAGRAM_SOCKET_SOCKET_SETTINGS.newSetting(
                        socketSettings));
        Assert.assertEquals(
                socketSettings,
                Socks5ValueDerivationHelper.getSocks5OnUdpAssociateRequestRemoteFacingDatagramSocketSocketSettingsFrom(rule, settings));
    }

    @Test
    public void testGetSocks5RequestDesiredDestinationAddressRedirectFromRule01() {
        Rule rule = new Rule.Builder()
                .addRuleAction(SocksRuleActionSpecConstants.SOCKS_REQUEST_DESIRED_DESTINATION_ADDRESS_REDIRECT.newRuleAction(
                        null))
                .build();
        Assert.assertNull(
                Socks5ValueDerivationHelper.getSocks5RequestDesiredDestinationAddressRedirectFrom(rule));
    }

    @Test
    public void testGetSocks5RequestDesiredDestinationAddressRedirectFromRule02() {
        String string = "127.0.0.1";
        Address address = Address.newInstanceFrom(string);
        Rule rule = new Rule.Builder()
                .addRuleAction(SocksRuleActionSpecConstants.SOCKS_REQUEST_DESIRED_DESTINATION_ADDRESS_REDIRECT.newRuleAction(
                        string))
                .build();
        Assert.assertEquals(
                address,
                Socks5ValueDerivationHelper.getSocks5RequestDesiredDestinationAddressRedirectFrom(rule));
    }

    @Test
    public void testGetSocks5RequestDesiredDestinationAddressRedirectFromRule03() {
        Rule rule = new Rule.Builder()
                .addRuleAction(Socks5RuleActionSpecConstants.SOCKS5_REQUEST_DESIRED_DESTINATION_ADDRESS_REDIRECT.newRuleAction(
                        null))
                .build();
        Assert.assertNull(
                Socks5ValueDerivationHelper.getSocks5RequestDesiredDestinationAddressRedirectFrom(rule));
    }

    @Test
    public void testGetSocks5RequestDesiredDestinationAddressRedirectFromRule04() {
        String string = "127.0.0.1";
        Address address = Address.newInstanceFrom(string);
        Rule rule = new Rule.Builder()
                .addRuleAction(Socks5RuleActionSpecConstants.SOCKS5_REQUEST_DESIRED_DESTINATION_ADDRESS_REDIRECT.newRuleAction(
                        address))
                .build();
        Assert.assertEquals(
                address,
                Socks5ValueDerivationHelper.getSocks5RequestDesiredDestinationAddressRedirectFrom(rule));
    }

    @Test
    public void testGetSocks5RequestDesiredDestinationPortRedirectFromRule01() {
        Rule rule = new Rule.Builder()
                .addRuleAction(SocksRuleActionSpecConstants.SOCKS_REQUEST_DESIRED_DESTINATION_PORT_REDIRECT.newRuleAction(
                        null))
                .build();
        Assert.assertNull(
                Socks5ValueDerivationHelper.getSocks5RequestDesiredDestinationPortRedirectFrom(rule));
    }

    @Test
    public void testGetSocks5RequestDesiredDestinationPortRedirectFromRule02() {
        Port port = Port.valueOf(443);
        Rule rule = new Rule.Builder()
                .addRuleAction(SocksRuleActionSpecConstants.SOCKS_REQUEST_DESIRED_DESTINATION_PORT_REDIRECT.newRuleAction(
                        port))
                .build();
        Assert.assertEquals(
                port,
                Socks5ValueDerivationHelper.getSocks5RequestDesiredDestinationPortRedirectFrom(rule));
    }

    @Test
    public void testGetSocks5RequestDesiredDestinationPortRedirectFromRule03() {
        Rule rule = new Rule.Builder()
                .addRuleAction(Socks5RuleActionSpecConstants.SOCKS5_REQUEST_DESIRED_DESTINATION_PORT_REDIRECT.newRuleAction(
                        null))
                .build();
        Assert.assertNull(
                Socks5ValueDerivationHelper.getSocks5RequestDesiredDestinationPortRedirectFrom(rule));
    }

    @Test
    public void testGetSocks5RequestDesiredDestinationPortRedirectFromRule04() {
        Port port = Port.valueOf(443);
        Rule rule = new Rule.Builder()
                .addRuleAction(Socks5RuleActionSpecConstants.SOCKS5_REQUEST_DESIRED_DESTINATION_PORT_REDIRECT.newRuleAction(
                        port))
                .build();
        Assert.assertEquals(
                port,
                Socks5ValueDerivationHelper.getSocks5RequestDesiredDestinationPortRedirectFrom(rule));
    }

    @Test
    public void testGetSocks5RequestDesiredDestinationRedirectLogActionFromRule01() {
        Rule rule = new Rule.Builder()
                .addRuleAction(SocksRuleActionSpecConstants.SOCKS_REQUEST_DESIRED_DESTINATION_REDIRECT_LOG_ACTION.newRuleAction(
                        null))
                .build();
        Assert.assertNull(
                Socks5ValueDerivationHelper.getSocks5RequestDesiredDestinationRedirectLogActionFrom(rule));
    }

    @Test
    public void testGetSocks5RequestDesiredDestinationRedirectLogActionFromRule02() {
        LogAction logAction = LogAction.LOG_AS_INFO;
        Rule rule = new Rule.Builder()
                .addRuleAction(SocksRuleActionSpecConstants.SOCKS_REQUEST_DESIRED_DESTINATION_REDIRECT_LOG_ACTION.newRuleAction(
                        logAction))
                .build();
        Assert.assertEquals(
                logAction,
                Socks5ValueDerivationHelper.getSocks5RequestDesiredDestinationRedirectLogActionFrom(rule));
    }

    @Test
    public void testGetSocks5RequestDesiredDestinationRedirectLogActionFromRule03() {
        Rule rule = new Rule.Builder()
                .addRuleAction(Socks5RuleActionSpecConstants.SOCKS5_REQUEST_DESIRED_DESTINATION_REDIRECT_LOG_ACTION.newRuleAction(
                        null))
                .build();
        Assert.assertNull(
                Socks5ValueDerivationHelper.getSocks5RequestDesiredDestinationRedirectLogActionFrom(rule));
    }

    @Test
    public void testGetSocks5RequestDesiredDestinationRedirectLogActionFromRule04() {
        LogAction logAction = LogAction.LOG_AS_INFO;
        Rule rule = new Rule.Builder()
                .addRuleAction(Socks5RuleActionSpecConstants.SOCKS5_REQUEST_DESIRED_DESTINATION_REDIRECT_LOG_ACTION.newRuleAction(
                        logAction))
                .build();
        Assert.assertEquals(
                logAction,
                Socks5ValueDerivationHelper.getSocks5RequestDesiredDestinationRedirectLogActionFrom(rule));
    }

    @Test
    public void testGetSocks5UserpassAuthMethodUserRepositoryFromSettings01() {
        Settings settings = Settings.of();
        UserRepository userRepository =
                Socks5ValueDerivationHelper.getSocks5UserpassAuthMethodUserRepositoryFrom(settings);
        Assert.assertEquals(
                "StringSourceUserRepository",
                userRepository.getTypeName());
        Assert.assertEquals(
                "",
                userRepository.getInitializationString());
    }

    @Test
    public void testGetSocks5UserpassAuthMethodUserRepositoryFromSettings02() {
        String string = "StringSourceUserRepository:Aladdin:opensesame";
        Settings settings = Settings.of(
                SocksSettingSpecConstants.SOCKS_USERPASSAUTHMETHOD_USER_REPOSITORY.newSetting(
                        string));
        UserRepository userRepository =
                Socks5ValueDerivationHelper.getSocks5UserpassAuthMethodUserRepositoryFrom(settings);
        Assert.assertEquals(
                "StringSourceUserRepository",
                userRepository.getTypeName());
        Assert.assertEquals(
                "Aladdin:opensesame",
                userRepository.getInitializationString());
    }

    @Test
    public void testGetSocks5UserpassAuthMethodUserRepositoryFromSettings03() {
        String string = "StringSourceUserRepository:Aladdin:opensesame";
        UserRepository userRepository = UserRepository.newInstanceFrom(string);
        Settings settings = Settings.of(
                Socks5SettingSpecConstants.SOCKS5_USERPASSAUTHMETHOD_USER_REPOSITORY.newSetting(
                        userRepository));
        Assert.assertEquals(
                userRepository,
                Socks5ValueDerivationHelper.getSocks5UserpassAuthMethodUserRepositoryFrom(settings));
    }

}