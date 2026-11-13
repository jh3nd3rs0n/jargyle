package com.github.jh3nd3rs0n.jargyle.server.internal.server;

import com.github.jh3nd3rs0n.jargyle.common.net.*;
import com.github.jh3nd3rs0n.jargyle.common.number.NonNegativeInteger;
import com.github.jh3nd3rs0n.jargyle.common.number.PositiveInteger;
import com.github.jh3nd3rs0n.jargyle.common.string.CommaSeparatedValues;
import com.github.jh3nd3rs0n.jargyle.server.*;
import org.junit.Assert;
import org.junit.Test;

import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;

public class SocksValueDerivationHelperTest {

    @Test
    public void testGetSocksGssapiAuthMethodNecReferenceImplFromSettings01() {
        Settings settings = Settings.of();
        Assert.assertFalse(SocksValueDerivationHelper.getSocksGssapiAuthMethodNecReferenceImplFrom(settings));
    }

    @Test
    public void testGetSocksGssapiAuthMethodNecReferenceImplFromSettings02() {
        Settings settings = Settings.of(
                SocksSettingSpecConstants.SOCKS_GSSAPIAUTHMETHOD_NEC_REFERENCE_IMPL.newSetting(
                        Boolean.TRUE));
        Assert.assertTrue(SocksValueDerivationHelper.getSocksGssapiAuthMethodNecReferenceImplFrom(settings));
    }

    @Test
    public void testGetSocksGssapiAuthMethodNecReferenceImplFromSettings03() {
        Settings settings = Settings.of(
                SocksSettingSpecConstants.SOCKS_GSSAPIAUTHMETHOD_NEC_REFERENCE_IMPL.newSetting(
                        Boolean.FALSE));
        Assert.assertFalse(SocksValueDerivationHelper.getSocksGssapiAuthMethodNecReferenceImplFrom(settings));
    }

    @Test
    public void testGetSocksGssapiAuthMethodProtectionLevelsFromSettings01() {
        Settings settings = Settings.of();
        Assert.assertEquals(
                CommaSeparatedValues.of(
                        "REQUIRED_INTEG_AND_CONF", "REQUIRED_INTEG", "NONE"),
                SocksValueDerivationHelper.getSocksGssapiAuthMethodProtectionLevelsFrom(settings));
    }

    @Test
    public void testGetSocksGssapiAuthMethodProtectionLevelsFromSettings02() {
        CommaSeparatedValues commaSeparatedValues = CommaSeparatedValues.of(
                "NONE", "REQUIRED_INTEG", "REQUIRED_INTEG_AND_CONF");
        Settings settings = Settings.of(
                SocksSettingSpecConstants.SOCKS_GSSAPIAUTHMETHOD_PROTECTION_LEVELS.newSetting(
                        commaSeparatedValues));
        Assert.assertEquals(
                commaSeparatedValues,
                SocksValueDerivationHelper.getSocksGssapiAuthMethodProtectionLevelsFrom(settings));
    }

    @Test
    public void testGetSocksGssapiAuthMethodSuggestedConfFromSettings01() {
        Settings settings = Settings.of();
        Assert.assertTrue(SocksValueDerivationHelper.getSocksGssapiAuthMethodSuggestedConfFrom(settings));
    }

    @Test
    public void testGetSocksGssapiAuthMethodSuggestedConfFromSettings02() {
        Settings settings = Settings.of(
                SocksSettingSpecConstants.SOCKS_GSSAPIAUTHMETHOD_SUGGESTED_CONF.newSetting(
                        Boolean.TRUE));
        Assert.assertTrue(SocksValueDerivationHelper.getSocksGssapiAuthMethodSuggestedConfFrom(settings));
    }

    @Test
    public void testGetSocksGssapiAuthMethodSuggestedConfFromSettings03() {
        Settings settings = Settings.of(
                SocksSettingSpecConstants.SOCKS_GSSAPIAUTHMETHOD_SUGGESTED_CONF.newSetting(
                        Boolean.FALSE));
        Assert.assertFalse(SocksValueDerivationHelper.getSocksGssapiAuthMethodSuggestedConfFrom(settings));
    }

    @Test
    public void testGetSocksGssapiAuthMethodSuggestedIntegFromSettings01() {
        Settings settings = Settings.of();
        Assert.assertEquals(
                Integer.valueOf(0),
                SocksValueDerivationHelper.getSocksGssapiAuthMethodSuggestedIntegFrom(settings));
    }

    @Test
    public void testGetSocksGssapiAuthMethodSuggestedIntegFromSettings02() {
        Integer i = Integer.valueOf(1);
        Settings settings = Settings.of(
                SocksSettingSpecConstants.SOCKS_GSSAPIAUTHMETHOD_SUGGESTED_INTEG.newSetting(i));
        Assert.assertEquals(
                i,
                SocksValueDerivationHelper.getSocksGssapiAuthMethodSuggestedIntegFrom(settings));
    }

    @Test
    public void testGetSocksGssapiAuthMethodSuggestedIntegFromSettings03() {
        Integer i = Integer.valueOf(0);
        Settings settings = Settings.of(
                SocksSettingSpecConstants.SOCKS_GSSAPIAUTHMETHOD_SUGGESTED_INTEG.newSetting(i));
        Assert.assertEquals(
                i,
                SocksValueDerivationHelper.getSocksGssapiAuthMethodSuggestedIntegFrom(settings));
    }

    @Test
    public void testGetSocksMethodsFromSettings01() {
        Settings settings = Settings.of();
        Assert.assertEquals(
                CommaSeparatedValues.of("NO_AUTHENTICATION_REQUIRED"),
                SocksValueDerivationHelper.getSocksMethodsFrom(settings));
    }

    @Test
    public void testGetSocksMethodsFromSettings02() {
        CommaSeparatedValues commaSeparatedValues = CommaSeparatedValues.of(
                "USERNAME_PASSWORD", "GSSAPI");
        Settings settings = Settings.of(
                SocksSettingSpecConstants.SOCKS_METHODS.newSetting(
                        commaSeparatedValues));
        Assert.assertEquals(
                commaSeparatedValues,
                SocksValueDerivationHelper.getSocksMethodsFrom(settings));
    }

    @Test
    public void testGetSocksOnBindRequestRelayBufferSizeFromRule01() {
        PositiveInteger relayBufferSize = PositiveInteger.valueOf(2048);
        Rule rule = new Rule.Builder()
                .addRuleAction(SocksRuleActionSpecConstants.SOCKS_ON_REQUEST_RELAY_BUFFER_SIZE.newRuleAction(
                        relayBufferSize))
                .build();
        Assert.assertEquals(
                relayBufferSize,
                SocksValueDerivationHelper.getSocksOnBindRequestRelayBufferSizeFrom(rule));
    }

    @Test
    public void testGetSocksOnBindRequestRelayBufferSizeFromRule02() {
        PositiveInteger relayBufferSize = PositiveInteger.valueOf(2048);
        Rule rule = new Rule.Builder()
                .addRuleAction(SocksRuleActionSpecConstants.SOCKS_ON_BIND_REQUEST_RELAY_BUFFER_SIZE.newRuleAction(
                        relayBufferSize))
                .build();
        Assert.assertEquals(
                relayBufferSize,
                SocksValueDerivationHelper.getSocksOnBindRequestRelayBufferSizeFrom(rule));
    }

    @Test
    public void testGetSocksOnBindRequestRelayBufferSizeFromSettings01() {
        PositiveInteger relayBufferSize = PositiveInteger.valueOf(1024);
        Settings settings = Settings.of();
        Assert.assertEquals(
                relayBufferSize,
                SocksValueDerivationHelper.getSocksOnBindRequestRelayBufferSizeFrom(settings));
    }

    @Test
    public void testGetSocksOnBindRequestRelayBufferSizeFromSettings02() {
        PositiveInteger relayBufferSize = PositiveInteger.valueOf(2048);
        Settings settings = Settings.of(
                SocksSettingSpecConstants.SOCKS_ON_REQUEST_RELAY_BUFFER_SIZE.newSetting(
                        relayBufferSize));
        Assert.assertEquals(
                relayBufferSize,
                SocksValueDerivationHelper.getSocksOnBindRequestRelayBufferSizeFrom(settings));
    }

    @Test
    public void testGetSocksOnBindRequestRelayBufferSizeFromSettings03() {
        PositiveInteger relayBufferSize = PositiveInteger.valueOf(2048);
        Settings settings = Settings.of(
                SocksSettingSpecConstants.SOCKS_ON_BIND_REQUEST_RELAY_BUFFER_SIZE.newSetting(
                        relayBufferSize));
        Assert.assertEquals(
                relayBufferSize,
                SocksValueDerivationHelper.getSocksOnBindRequestRelayBufferSizeFrom(settings));
    }

    @Test
    public void testGetSocksOnBindRequestRelayIdleTimeoutFromRule01() {
        PositiveInteger relayIdleTimeout = PositiveInteger.valueOf(120000);
        Rule rule = new Rule.Builder()
                .addRuleAction(SocksRuleActionSpecConstants.SOCKS_ON_REQUEST_RELAY_IDLE_TIMEOUT.newRuleAction(
                        relayIdleTimeout))
                .build();

        Assert.assertEquals(
                relayIdleTimeout,
                SocksValueDerivationHelper.getSocksOnBindRequestRelayIdleTimeoutFrom(rule));
    }

    @Test
    public void testGetSocksOnBindRequestRelayIdleTimeoutFromRule02() {
        PositiveInteger relayIdleTimeout = PositiveInteger.valueOf(120000);
        Rule rule = new Rule.Builder()
                .addRuleAction(SocksRuleActionSpecConstants.SOCKS_ON_BIND_REQUEST_RELAY_IDLE_TIMEOUT.newRuleAction(
                        relayIdleTimeout))
                .build();

        Assert.assertEquals(
                relayIdleTimeout,
                SocksValueDerivationHelper.getSocksOnBindRequestRelayIdleTimeoutFrom(rule));
    }

    @Test
    public void testGetSocksOnBindRequestRelayIdleTimeoutFromSettings01() {
        PositiveInteger relayIdleTimeout = PositiveInteger.valueOf(60000);
        Settings settings = Settings.of();
        Assert.assertEquals(
                relayIdleTimeout,
                SocksValueDerivationHelper.getSocksOnBindRequestRelayIdleTimeoutFrom(settings));
    }

    @Test
    public void testGetSocksOnBindRequestRelayIdleTimeoutFromSettings02() {
        PositiveInteger relayIdleTimeout = PositiveInteger.valueOf(120000);
        Settings settings = Settings.of(
                SocksSettingSpecConstants.SOCKS_ON_REQUEST_RELAY_IDLE_TIMEOUT.newSetting(
                        relayIdleTimeout));
        Assert.assertEquals(
                relayIdleTimeout,
                SocksValueDerivationHelper.getSocksOnBindRequestRelayIdleTimeoutFrom(settings));
    }

    @Test
    public void testGetSocksOnBindRequestRelayIdleTimeoutFromSettings03() {
        PositiveInteger relayIdleTimeout = PositiveInteger.valueOf(120000);
        Settings settings = Settings.of(
                SocksSettingSpecConstants.SOCKS_ON_BIND_REQUEST_RELAY_IDLE_TIMEOUT.newSetting(
                        relayIdleTimeout));
        Assert.assertEquals(
                relayIdleTimeout,
                SocksValueDerivationHelper.getSocksOnBindRequestRelayIdleTimeoutFrom(settings));
    }

    @Test
    public void testGetSocksOnBindRequestRelayInboundBandwidthLimitFromRule01() {
        PositiveInteger relayInboundBandwidthLimit = PositiveInteger.valueOf(
                Integer.MAX_VALUE);
        Rule rule = new Rule.Builder()
                .addRuleAction(SocksRuleActionSpecConstants.SOCKS_ON_REQUEST_RELAY_INBOUND_BANDWIDTH_LIMIT.newRuleAction(
                        relayInboundBandwidthLimit))
                .build();
        Assert.assertEquals(
                relayInboundBandwidthLimit,
                SocksValueDerivationHelper.getSocksOnBindRequestRelayInboundBandwidthLimitFrom(rule));
    }

    @Test
    public void testGetSocksOnBindRequestRelayInboundBandwidthLimitFromRule02() {
        PositiveInteger relayInboundBandwidthLimit = PositiveInteger.valueOf(
                Integer.MAX_VALUE);
        Rule rule = new Rule.Builder()
                .addRuleAction(SocksRuleActionSpecConstants.SOCKS_ON_BIND_REQUEST_RELAY_INBOUND_BANDWIDTH_LIMIT.newRuleAction(
                        relayInboundBandwidthLimit))
                .build();
        Assert.assertEquals(
                relayInboundBandwidthLimit,
                SocksValueDerivationHelper.getSocksOnBindRequestRelayInboundBandwidthLimitFrom(rule));
    }

    @Test
    public void testGetSocksOnBindRequestRelayInboundBandwidthLimitFromSettings01() {
        Settings settings = Settings.of();
        Assert.assertNull(
                SocksValueDerivationHelper.getSocksOnBindRequestRelayInboundBandwidthLimitFrom(settings));
    }

    @Test
    public void testGetSocksOnBindRequestRelayInboundBandwidthLimitFromSettings02() {
        PositiveInteger relayInboundBandwidthLimit = PositiveInteger.valueOf(
                Integer.MAX_VALUE);
        Settings settings = Settings.of(
                SocksSettingSpecConstants.SOCKS_ON_REQUEST_RELAY_INBOUND_BANDWIDTH_LIMIT.newSetting(
                        relayInboundBandwidthLimit));
        Assert.assertEquals(
                relayInboundBandwidthLimit,
                SocksValueDerivationHelper.getSocksOnBindRequestRelayInboundBandwidthLimitFrom(settings));
    }

    @Test
    public void testGetSocksOnBindRequestRelayInboundBandwidthLimitFromSettings03() {
        PositiveInteger relayInboundBandwidthLimit = PositiveInteger.valueOf(
                Integer.MAX_VALUE);
        Settings settings = Settings.of(
                SocksSettingSpecConstants.SOCKS_ON_BIND_REQUEST_RELAY_INBOUND_BANDWIDTH_LIMIT.newSetting(
                        relayInboundBandwidthLimit));
        Assert.assertEquals(
                relayInboundBandwidthLimit,
                SocksValueDerivationHelper.getSocksOnBindRequestRelayInboundBandwidthLimitFrom(settings));
    }

    @Test
    public void testGetSocksOnBindRequestRelayOutboundBandwidthLimitFromRule01() {
        PositiveInteger relayOutboundBandwidthLimit = PositiveInteger.valueOf(
                Integer.MAX_VALUE);
        Rule rule = new Rule.Builder()
                .addRuleAction(SocksRuleActionSpecConstants.SOCKS_ON_REQUEST_RELAY_OUTBOUND_BANDWIDTH_LIMIT.newRuleAction(
                        relayOutboundBandwidthLimit))
                .build();
        Assert.assertEquals(
                relayOutboundBandwidthLimit,
                SocksValueDerivationHelper.getSocksOnBindRequestRelayOutboundBandwidthLimitFrom(rule));
    }

    @Test
    public void testGetSocksOnBindRequestRelayOutboundBandwidthLimitFromRule02() {
        PositiveInteger relayOutboundBandwidthLimit = PositiveInteger.valueOf(
                Integer.MAX_VALUE);
        Rule rule = new Rule.Builder()
                .addRuleAction(SocksRuleActionSpecConstants.SOCKS_ON_BIND_REQUEST_RELAY_OUTBOUND_BANDWIDTH_LIMIT.newRuleAction(
                        relayOutboundBandwidthLimit))
                .build();
        Assert.assertEquals(
                relayOutboundBandwidthLimit,
                SocksValueDerivationHelper.getSocksOnBindRequestRelayOutboundBandwidthLimitFrom(rule));
    }

    @Test
    public void testGetSocksOnBindRequestRelayOutboundBandwidthLimitFromSettings01() {
        Settings settings = Settings.of();
        Assert.assertNull(
                SocksValueDerivationHelper.getSocksOnBindRequestRelayOutboundBandwidthLimitFrom(settings));
    }

    @Test
    public void testGetSocksOnBindRequestRelayOutboundBandwidthLimitFromSettings02() {
        PositiveInteger relayOutboundBandwidthLimit = PositiveInteger.valueOf(
                Integer.MAX_VALUE);
        Settings settings = Settings.of(
                SocksSettingSpecConstants.SOCKS_ON_REQUEST_RELAY_OUTBOUND_BANDWIDTH_LIMIT.newSetting(
                        relayOutboundBandwidthLimit));
        Assert.assertEquals(
                relayOutboundBandwidthLimit,
                SocksValueDerivationHelper.getSocksOnBindRequestRelayOutboundBandwidthLimitFrom(settings));
    }

    @Test
    public void testGetSocksOnBindRequestRelayOutboundBandwidthLimitFromSettings03() {
        PositiveInteger relayOutboundBandwidthLimit = PositiveInteger.valueOf(
                Integer.MAX_VALUE);
        Settings settings = Settings.of(
                SocksSettingSpecConstants.SOCKS_ON_BIND_REQUEST_RELAY_OUTBOUND_BANDWIDTH_LIMIT.newSetting(
                        relayOutboundBandwidthLimit));
        Assert.assertEquals(
                relayOutboundBandwidthLimit,
                SocksValueDerivationHelper.getSocksOnBindRequestRelayOutboundBandwidthLimitFrom(settings));
    }

    @Test
    public void testGetSocksOnBindRequestRemoteFacingServerSocketBindHostAddressTypesFromRule01() {
        HostAddressType hostAddressType = HostAddressType.HOST_IPV4_ADDRESS;
        HostAddressTypes hostAddressTypes = HostAddressTypes.of(hostAddressType);
        Rule rule = new Rule.Builder()
                .addRuleAction(GeneralRuleActionSpecConstants.BIND_HOST_ADDRESS_TYPE.newRuleAction(
                        hostAddressType))
                .build();
        Assert.assertEquals(
                hostAddressTypes,
                SocksValueDerivationHelper.getSocksOnBindRequestRemoteFacingServerSocketBindHostAddressTypesFrom(rule));
    }

    @Test
    public void testGetSocksOnBindRequestRemoteFacingServerSocketBindHostAddressTypesFromRule02() {
        HostAddressType hostAddressType = HostAddressType.HOST_IPV4_ADDRESS;
        HostAddressTypes hostAddressTypes = HostAddressTypes.of(hostAddressType);
        Rule rule = new Rule.Builder()
                .addRuleAction(GeneralRuleActionSpecConstants.REMOTE_ENDPOINTS_BIND_HOST_ADDRESS_TYPE.newRuleAction(
                        hostAddressType))
                .build();
        Assert.assertEquals(
                hostAddressTypes,
                SocksValueDerivationHelper.getSocksOnBindRequestRemoteFacingServerSocketBindHostAddressTypesFrom(rule));
    }

    @Test
    public void testGetSocksOnBindRequestRemoteFacingServerSocketBindHostAddressTypesFromRule03() {
        HostAddressType hostAddressType = HostAddressType.HOST_IPV4_ADDRESS;
        HostAddressTypes hostAddressTypes = HostAddressTypes.of(hostAddressType);
        Rule rule = new Rule.Builder()
                .addRuleAction(SocksRuleActionSpecConstants.SOCKS_ON_REQUEST_REMOTE_ENDPOINTS_BIND_HOST_ADDRESS_TYPE.newRuleAction(
                        hostAddressType))
                .build();
        Assert.assertEquals(
                hostAddressTypes,
                SocksValueDerivationHelper.getSocksOnBindRequestRemoteFacingServerSocketBindHostAddressTypesFrom(rule));
    }

    @Test
    public void testGetSocksOnBindRequestRemoteFacingServerSocketBindHostAddressTypesFromRule04() {
        HostAddressType hostAddressType = HostAddressType.HOST_IPV4_ADDRESS;
        HostAddressTypes hostAddressTypes = HostAddressTypes.of(hostAddressType);
        Rule rule = new Rule.Builder()
                .addRuleAction(SocksRuleActionSpecConstants.SOCKS_ON_BIND_REQUEST_REMOTE_FACING_SERVER_SOCKET_BIND_HOST_ADDRESS_TYPE.newRuleAction(
                        hostAddressType))
                .build();
        Assert.assertEquals(
                hostAddressTypes,
                SocksValueDerivationHelper.getSocksOnBindRequestRemoteFacingServerSocketBindHostAddressTypesFrom(rule));
    }

    @Test
    public void testGetSocksOnBindRequestRemoteFacingServerSocketBindHostAddressTypesFromSettings01() {
        Settings settings = Settings.of();
        Assert.assertEquals(
                HostAddressTypes.of(
                        HostAddressType.HOST_IPV4_ADDRESS,
                        HostAddressType.HOST_IPV6_ADDRESS),
                SocksValueDerivationHelper.getSocksOnBindRequestRemoteFacingServerSocketBindHostAddressTypesFrom(settings));
    }

    @Test
    public void testGetSocksOnBindRequestRemoteFacingServerSocketBindHostAddressTypesFromSettings02() {
        HostAddressType hostAddressType = HostAddressType.HOST_IPV4_ADDRESS;
        HostAddressTypes hostAddressTypes = HostAddressTypes.of(hostAddressType);
        Settings settings = Settings.of(
                GeneralSettingSpecConstants.BIND_HOST_ADDRESS_TYPES.newSetting(
                        hostAddressTypes));
        Assert.assertEquals(
                hostAddressTypes,
                SocksValueDerivationHelper.getSocksOnBindRequestRemoteFacingServerSocketBindHostAddressTypesFrom(settings));
    }

    @Test
    public void testGetSocksOnBindRequestRemoteFacingServerSocketBindHostAddressTypesFromSettings03() {
        HostAddressType hostAddressType = HostAddressType.HOST_IPV4_ADDRESS;
        HostAddressTypes hostAddressTypes = HostAddressTypes.of(hostAddressType);
        Settings settings = Settings.of(
                GeneralSettingSpecConstants.REMOTE_ENDPOINTS_BIND_HOST_ADDRESS_TYPES.newSetting(
                        hostAddressTypes));
        Assert.assertEquals(
                hostAddressTypes,
                SocksValueDerivationHelper.getSocksOnBindRequestRemoteFacingServerSocketBindHostAddressTypesFrom(settings));
    }

    @Test
    public void testGetSocksOnBindRequestRemoteFacingServerSocketBindHostAddressTypesFromSettings04() {
        HostAddressType hostAddressType = HostAddressType.HOST_IPV4_ADDRESS;
        HostAddressTypes hostAddressTypes = HostAddressTypes.of(hostAddressType);
        Settings settings = Settings.of(
                SocksSettingSpecConstants.SOCKS_ON_REQUEST_REMOTE_ENDPOINTS_BIND_HOST_ADDRESS_TYPES.newSetting(
                        hostAddressTypes));
        Assert.assertEquals(
                hostAddressTypes,
                SocksValueDerivationHelper.getSocksOnBindRequestRemoteFacingServerSocketBindHostAddressTypesFrom(settings));
    }

    @Test
    public void testGetSocksOnBindRequestRemoteFacingServerSocketBindHostAddressTypesFromSettings05() {
        HostAddressType hostAddressType = HostAddressType.HOST_IPV4_ADDRESS;
        HostAddressTypes hostAddressTypes = HostAddressTypes.of(hostAddressType);
        Settings settings = Settings.of(
                SocksSettingSpecConstants.SOCKS_ON_BIND_REQUEST_REMOTE_FACING_SERVER_SOCKET_BIND_HOST_ADDRESS_TYPES.newSetting(
                        hostAddressTypes));
        Assert.assertEquals(
                hostAddressTypes,
                SocksValueDerivationHelper.getSocksOnBindRequestRemoteFacingServerSocketBindHostAddressTypesFrom(settings));
    }

    @Test
    public void testGetSocksOnBindRequestRemoteFacingServerSocketBindHostFromRule01() {
        Host host = Host.newInstance("127.0.0.1");
        Rule rule = new Rule.Builder()
                .addRuleAction(GeneralRuleActionSpecConstants.BIND_HOST.newRuleAction(
                        host))
                .build();
        Assert.assertEquals(
                host,
                SocksValueDerivationHelper.getSocksOnBindRequestRemoteFacingServerSocketBindHostFrom(rule));
    }

    @Test
    public void testGetSocksOnBindRequestRemoteFacingServerSocketBindHostFromRule02() {
        Host host = Host.newInstance("127.0.0.1");
        Rule rule = new Rule.Builder()
                .addRuleAction(GeneralRuleActionSpecConstants.REMOTE_ENDPOINTS_BIND_HOST.newRuleAction(
                        host))
                .build();
        Assert.assertEquals(
                host,
                SocksValueDerivationHelper.getSocksOnBindRequestRemoteFacingServerSocketBindHostFrom(rule));
    }

    @Test
    public void testGetSocksOnBindRequestRemoteFacingServerSocketBindHostFromRule03() {
        Host host = Host.newInstance("127.0.0.1");
        Rule rule = new Rule.Builder()
                .addRuleAction(SocksRuleActionSpecConstants.SOCKS_ON_REQUEST_REMOTE_ENDPOINTS_BIND_HOST.newRuleAction(
                        host))
                .build();
        Assert.assertEquals(
                host,
                SocksValueDerivationHelper.getSocksOnBindRequestRemoteFacingServerSocketBindHostFrom(rule));
    }

    @Test
    public void testGetSocksOnBindRequestRemoteFacingServerSocketBindHostFromRule04() {
        Host host = Host.newInstance("127.0.0.1");
        Rule rule = new Rule.Builder()
                .addRuleAction(SocksRuleActionSpecConstants.SOCKS_ON_BIND_REQUEST_REMOTE_FACING_SERVER_SOCKET_BIND_HOST.newRuleAction(
                        host))
                .build();
        Assert.assertEquals(
                host,
                SocksValueDerivationHelper.getSocksOnBindRequestRemoteFacingServerSocketBindHostFrom(rule));
    }

    @Test
    public void testGetSocksOnBindRequestRemoteFacingServerSocketBindHostFromSettings01() {
        Settings settings = Settings.of();
        Assert.assertNull(SocksValueDerivationHelper.getSocksOnBindRequestRemoteFacingServerSocketBindHostFrom(settings));
    }

    @Test
    public void testGetSocksOnBindRequestRemoteFacingServerSocketBindHostFromSettings02() {
        Host host = Host.newInstance("127.0.0.1");
        Settings settings = Settings.of(
                GeneralSettingSpecConstants.BIND_HOST.newSetting(host));
        Assert.assertEquals(
                host,
                SocksValueDerivationHelper.getSocksOnBindRequestRemoteFacingServerSocketBindHostFrom(settings));
    }

    @Test
    public void testGetSocksOnBindRequestRemoteFacingServerSocketBindHostFromSettings03() {
        Host host = Host.newInstance("127.0.0.1");
        Settings settings = Settings.of(
                GeneralSettingSpecConstants.REMOTE_ENDPOINTS_BIND_HOST.newSetting(
                        host));
        Assert.assertEquals(
                host,
                SocksValueDerivationHelper.getSocksOnBindRequestRemoteFacingServerSocketBindHostFrom(settings));
    }

    @Test
    public void testGetSocksOnBindRequestRemoteFacingServerSocketBindHostFromSettings04() {
        Host host = Host.newInstance("127.0.0.1");
        Settings settings = Settings.of(
                SocksSettingSpecConstants.SOCKS_ON_REQUEST_REMOTE_ENDPOINTS_BIND_HOST.newSetting(
                        host));
        Assert.assertEquals(
                host,
                SocksValueDerivationHelper.getSocksOnBindRequestRemoteFacingServerSocketBindHostFrom(settings));
    }

    @Test
    public void testGetSocksOnBindRequestRemoteFacingServerSocketBindHostFromSettings05() {
        Host host = Host.newInstance("127.0.0.1");
        Settings settings = Settings.of(
                SocksSettingSpecConstants.SOCKS_ON_BIND_REQUEST_REMOTE_FACING_SERVER_SOCKET_BIND_HOST.newSetting(
                        host));
        Assert.assertEquals(
                host,
                SocksValueDerivationHelper.getSocksOnBindRequestRemoteFacingServerSocketBindHostFrom(settings));
    }

    @Test
    public void testGetSocksOnBindRequestRemoteFacingServerSocketBindPortRangesFromRule01() {
        PortRange portRange = PortRange.of(
                Port.valueOf(1), Port.valueOf(65535));
        PortRanges portRanges = PortRanges.of(portRange);
        Rule rule = new Rule.Builder()
                .addRuleAction(GeneralRuleActionSpecConstants.BIND_TCP_PORT_RANGE.newRuleAction(
                        portRange))
                .build();
        Assert.assertEquals(
                portRanges,
                SocksValueDerivationHelper.getSocksOnBindRequestRemoteFacingServerSocketBindPortRangesFrom(rule));
    }

    @Test
    public void testGetSocksOnBindRequestRemoteFacingServerSocketBindPortRangesFromRule02() {
        PortRange portRange = PortRange.of(
                Port.valueOf(1), Port.valueOf(65535));
        PortRanges portRanges = PortRanges.of(portRange);
        Rule rule = new Rule.Builder()
                .addRuleAction(GeneralRuleActionSpecConstants.REMOTE_ENDPOINTS_BIND_TCP_PORT_RANGE.newRuleAction(
                        portRange))
                .build();
        Assert.assertEquals(
                portRanges,
                SocksValueDerivationHelper.getSocksOnBindRequestRemoteFacingServerSocketBindPortRangesFrom(rule));
    }

    @Test
    public void testGetSocksOnBindRequestRemoteFacingServerSocketBindPortRangesFromRule03() {
        PortRange portRange = PortRange.of(
                Port.valueOf(1), Port.valueOf(65535));
        PortRanges portRanges = PortRanges.of(portRange);
        Rule rule = new Rule.Builder()
                .addRuleAction(SocksRuleActionSpecConstants.SOCKS_ON_REQUEST_REMOTE_ENDPOINTS_BIND_TCP_PORT_RANGE.newRuleAction(
                        portRange))
                .build();
        Assert.assertEquals(
                portRanges,
                SocksValueDerivationHelper.getSocksOnBindRequestRemoteFacingServerSocketBindPortRangesFrom(rule));
    }

    @Test
    public void testGetSocksOnBindRequestRemoteFacingServerSocketBindPortRangesFromRule04() {
        PortRange portRange = PortRange.of(
                Port.valueOf(1), Port.valueOf(65535));
        PortRanges portRanges = PortRanges.of(portRange);
        Rule rule = new Rule.Builder()
                .addRuleAction(SocksRuleActionSpecConstants.SOCKS_ON_BIND_REQUEST_REMOTE_FACING_SERVER_SOCKET_BIND_PORT_RANGE.newRuleAction(
                        portRange))
                .build();
        Assert.assertEquals(
                portRanges,
                SocksValueDerivationHelper.getSocksOnBindRequestRemoteFacingServerSocketBindPortRangesFrom(rule));
    }

    @Test
    public void testGetSocksOnBindRequestRemoteFacingServerSocketBindPortRangesFromSettings01() {
        PortRanges portRanges = PortRanges.getDefault();
        Settings settings = Settings.of();
        Assert.assertEquals(
                portRanges,
                SocksValueDerivationHelper.getSocksOnBindRequestRemoteFacingServerSocketBindPortRangesFrom(settings));
    }

    @Test
    public void testGetSocksOnBindRequestRemoteFacingServerSocketBindPortRangesFromSettings02() {
        PortRange portRange = PortRange.of(
                Port.valueOf(1), Port.valueOf(65535));
        PortRanges portRanges = PortRanges.of(portRange);
        Settings settings = Settings.of(
                GeneralSettingSpecConstants.BIND_TCP_PORT_RANGES.newSetting(
                        portRanges));
        Assert.assertEquals(
                portRanges,
                SocksValueDerivationHelper.getSocksOnBindRequestRemoteFacingServerSocketBindPortRangesFrom(settings));
    }

    @Test
    public void testGetSocksOnBindRequestRemoteFacingServerSocketBindPortRangesFromSettings03() {
        PortRange portRange = PortRange.of(
                Port.valueOf(1), Port.valueOf(65535));
        PortRanges portRanges = PortRanges.of(portRange);
        Settings settings = Settings.of(
                GeneralSettingSpecConstants.REMOTE_ENDPOINTS_BIND_TCP_PORT_RANGES.newSetting(
                        portRanges));
        Assert.assertEquals(
                portRanges,
                SocksValueDerivationHelper.getSocksOnBindRequestRemoteFacingServerSocketBindPortRangesFrom(settings));
    }

    @Test
    public void testGetSocksOnBindRequestRemoteFacingServerSocketBindPortRangesFromSettings04() {
        PortRange portRange = PortRange.of(
                Port.valueOf(1), Port.valueOf(65535));
        PortRanges portRanges = PortRanges.of(portRange);
        Settings settings = Settings.of(
                SocksSettingSpecConstants.SOCKS_ON_REQUEST_REMOTE_ENDPOINTS_BIND_TCP_PORT_RANGES.newSetting(
                        portRanges));
        Assert.assertEquals(
                portRanges,
                SocksValueDerivationHelper.getSocksOnBindRequestRemoteFacingServerSocketBindPortRangesFrom(settings));
    }

    @Test
    public void testGetSocksOnBindRequestRemoteFacingServerSocketBindPortRangesFromSettings05() {
        PortRange portRange = PortRange.of(
                Port.valueOf(1), Port.valueOf(65535));
        PortRanges portRanges = PortRanges.of(portRange);
        Settings settings = Settings.of(
                SocksSettingSpecConstants.SOCKS_ON_BIND_REQUEST_REMOTE_FACING_SERVER_SOCKET_BIND_PORT_RANGES.newSetting(
                        portRanges));
        Assert.assertEquals(
                portRanges,
                SocksValueDerivationHelper.getSocksOnBindRequestRemoteFacingServerSocketBindPortRangesFrom(settings));
    }

    @Test
    public void testGetSocksOnBindRequestRemoteFacingServerSocketNetInterfaceFromRule01() throws UnknownHostException, SocketException {
        Host host = Host.newInstance("127.0.0.1");
        NetworkInterface networkInterface = NetworkInterface.getByInetAddress(
                host.toInetAddress());
        NetInterface netInterface = NetInterface.newInstance(networkInterface);
        Rule rule = new Rule.Builder()
                .addRuleAction(GeneralRuleActionSpecConstants.NET_INTERFACE.newRuleAction(
                        netInterface))
                .build();
        Assert.assertEquals(
                netInterface,
                SocksValueDerivationHelper.getSocksOnBindRequestRemoteFacingServerSocketNetInterfaceFrom(rule));
    }

    @Test
    public void testGetSocksOnBindRequestRemoteFacingServerSocketNetInterfaceFromRule02() throws UnknownHostException, SocketException {
        Host host = Host.newInstance("127.0.0.1");
        NetworkInterface networkInterface = NetworkInterface.getByInetAddress(
                host.toInetAddress());
        NetInterface netInterface = NetInterface.newInstance(networkInterface);
        Rule rule = new Rule.Builder()
                .addRuleAction(GeneralRuleActionSpecConstants.REMOTE_ENDPOINTS_NET_INTERFACE.newRuleAction(
                        netInterface))
                .build();
        Assert.assertEquals(
                netInterface,
                SocksValueDerivationHelper.getSocksOnBindRequestRemoteFacingServerSocketNetInterfaceFrom(rule));
    }

    @Test
    public void testGetSocksOnBindRequestRemoteFacingServerSocketNetInterfaceFromRule03() throws UnknownHostException, SocketException {
        Host host = Host.newInstance("127.0.0.1");
        NetworkInterface networkInterface = NetworkInterface.getByInetAddress(
                host.toInetAddress());
        NetInterface netInterface = NetInterface.newInstance(networkInterface);
        Rule rule = new Rule.Builder()
                .addRuleAction(SocksRuleActionSpecConstants.SOCKS_ON_REQUEST_REMOTE_ENDPOINTS_NET_INTERFACE.newRuleAction(
                        netInterface))
                .build();
        Assert.assertEquals(
                netInterface,
                SocksValueDerivationHelper.getSocksOnBindRequestRemoteFacingServerSocketNetInterfaceFrom(rule));
    }

    @Test
    public void testGetSocksOnBindRequestRemoteFacingServerSocketNetInterfaceFromRule04() throws UnknownHostException, SocketException {
        Host host = Host.newInstance("127.0.0.1");
        NetworkInterface networkInterface = NetworkInterface.getByInetAddress(
                host.toInetAddress());
        NetInterface netInterface = NetInterface.newInstance(networkInterface);
        Rule rule = new Rule.Builder()
                .addRuleAction(SocksRuleActionSpecConstants.SOCKS_ON_BIND_REQUEST_REMOTE_FACING_SERVER_SOCKET_NET_INTERFACE.newRuleAction(
                        netInterface))
                .build();
        Assert.assertEquals(
                netInterface,
                SocksValueDerivationHelper.getSocksOnBindRequestRemoteFacingServerSocketNetInterfaceFrom(rule));
    }

    @Test
    public void testGetSocksOnBindRequestRemoteFacingServerSocketNetInterfaceFromSettings01() {
        Settings settings = Settings.of();
        Assert.assertNull(SocksValueDerivationHelper.getSocksOnBindRequestRemoteFacingServerSocketNetInterfaceFrom(settings));
    }

    @Test
    public void testGetSocksOnBindRequestRemoteFacingServerSocketNetInterfaceFromSettings02() throws UnknownHostException, SocketException {
        Host host = Host.newInstance("127.0.0.1");
        NetworkInterface networkInterface = NetworkInterface.getByInetAddress(
                host.toInetAddress());
        NetInterface netInterface = NetInterface.newInstance(networkInterface);
        Settings settings = Settings.of(
                GeneralSettingSpecConstants.NET_INTERFACE.newSetting(
                        netInterface));
        Assert.assertEquals(
                netInterface,
                SocksValueDerivationHelper.getSocksOnBindRequestRemoteFacingServerSocketNetInterfaceFrom(settings));
    }

    @Test
    public void testGetSocksOnBindRequestRemoteFacingServerSocketNetInterfaceFromSettings03() throws UnknownHostException, SocketException {
        Host host = Host.newInstance("127.0.0.1");
        NetworkInterface networkInterface = NetworkInterface.getByInetAddress(
                host.toInetAddress());
        NetInterface netInterface = NetInterface.newInstance(networkInterface);
        Settings settings = Settings.of(
                GeneralSettingSpecConstants.REMOTE_ENDPOINTS_NET_INTERFACE.newSetting(
                        netInterface));
        Assert.assertEquals(
                netInterface,
                SocksValueDerivationHelper.getSocksOnBindRequestRemoteFacingServerSocketNetInterfaceFrom(settings));
    }

    @Test
    public void testGetSocksOnBindRequestRemoteFacingServerSocketNetInterfaceFromSettings04() throws UnknownHostException, SocketException {
        Host host = Host.newInstance("127.0.0.1");
        NetworkInterface networkInterface = NetworkInterface.getByInetAddress(
                host.toInetAddress());
        NetInterface netInterface = NetInterface.newInstance(networkInterface);
        Settings settings = Settings.of(
                SocksSettingSpecConstants.SOCKS_ON_REQUEST_REMOTE_ENDPOINTS_NET_INTERFACE.newSetting(
                        netInterface));
        Assert.assertEquals(
                netInterface,
                SocksValueDerivationHelper.getSocksOnBindRequestRemoteFacingServerSocketNetInterfaceFrom(settings));
    }

    @Test
    public void testGetSocksOnBindRequestRemoteFacingServerSocketNetInterfaceFromSettings05() throws UnknownHostException, SocketException {
        Host host = Host.newInstance("127.0.0.1");
        NetworkInterface networkInterface = NetworkInterface.getByInetAddress(
                host.toInetAddress());
        NetInterface netInterface = NetInterface.newInstance(networkInterface);
        Settings settings = Settings.of(
                SocksSettingSpecConstants.SOCKS_ON_BIND_REQUEST_REMOTE_FACING_SERVER_SOCKET_NET_INTERFACE.newSetting(
                        netInterface));
        Assert.assertEquals(
                netInterface,
                SocksValueDerivationHelper.getSocksOnBindRequestRemoteFacingServerSocketNetInterfaceFrom(settings));
    }

    @Test
    public void testGetSocksOnBindRequestRemoteFacingServerSocketSocketSettingsFromRule01() {
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
        Assert.assertEquals(
                socketSettings,
                SocksValueDerivationHelper.getSocksOnBindRequestRemoteFacingServerSocketSocketSettingsFrom(rule));
    }

    @Test
    public void testGetSocksOnBindRequestRemoteFacingServerSocketSocketSettingsFromRule02() {
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
        Assert.assertEquals(
                socketSettings,
                SocksValueDerivationHelper.getSocksOnBindRequestRemoteFacingServerSocketSocketSettingsFrom(rule));
    }

    @Test
    public void testGetSocksOnBindRequestRemoteFacingServerSocketSocketSettingsFromRule03() {
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
        Assert.assertEquals(
                socketSettings,
                SocksValueDerivationHelper.getSocksOnBindRequestRemoteFacingServerSocketSocketSettingsFrom(rule));
    }

    @Test
    public void testGetSocksOnBindRequestRemoteFacingServerSocketSocketSettingsFromRule04() {
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
        Assert.assertEquals(
                socketSettings,
                SocksValueDerivationHelper.getSocksOnBindRequestRemoteFacingServerSocketSocketSettingsFrom(rule));
    }

    @Test
    public void testGetSocksOnBindRequestRemoteFacingServerSocketSocketSettingsFromSettings01() {
        SocketSettings socketSettings = SocketSettings.of();
        Settings settings = Settings.of();
        Assert.assertEquals(
                socketSettings,
                SocksValueDerivationHelper.getSocksOnBindRequestRemoteFacingServerSocketSocketSettingsFrom(settings));
    }

    @Test
    public void testGetSocksOnBindRequestRemoteFacingServerSocketSocketSettingsFromSettings02() {
        SocketSetting<?> socketSetting =
                StandardSocketSettingSpecConstants.SO_TIMEOUT.newSocketSetting(
                        NonNegativeInteger.valueOf(0));
        SocketSettings socketSettings = SocketSettings.of(socketSetting);
        Settings settings = Settings.of(
                GeneralSettingSpecConstants.SOCKET_SETTINGS.newSetting(
                        socketSettings));
        Assert.assertEquals(
                socketSettings,
                SocksValueDerivationHelper.getSocksOnBindRequestRemoteFacingServerSocketSocketSettingsFrom(settings));
    }

    @Test
    public void testGetSocksOnBindRequestRemoteFacingServerSocketSocketSettingsFromSettings03() {
        SocketSetting<?> socketSetting =
                StandardSocketSettingSpecConstants.SO_TIMEOUT.newSocketSetting(
                        NonNegativeInteger.valueOf(0));
        SocketSettings socketSettings = SocketSettings.of(socketSetting);
        Settings settings = Settings.of(
                GeneralSettingSpecConstants.REMOTE_ENDPOINTS_SOCKET_SETTINGS.newSetting(
                        socketSettings));
        Assert.assertEquals(
                socketSettings,
                SocksValueDerivationHelper.getSocksOnBindRequestRemoteFacingServerSocketSocketSettingsFrom(settings));
    }

    @Test
    public void testGetSocksOnBindRequestRemoteFacingServerSocketSocketSettingsFromSettings04() {
        SocketSetting<?> socketSetting =
                StandardSocketSettingSpecConstants.SO_TIMEOUT.newSocketSetting(
                        NonNegativeInteger.valueOf(0));
        SocketSettings socketSettings = SocketSettings.of(socketSetting);
        Settings settings = Settings.of(
                SocksSettingSpecConstants.SOCKS_ON_REQUEST_REMOTE_ENDPOINTS_SOCKET_SETTINGS.newSetting(
                        socketSettings));
        Assert.assertEquals(
                socketSettings,
                SocksValueDerivationHelper.getSocksOnBindRequestRemoteFacingServerSocketSocketSettingsFrom(settings));
    }

    @Test
    public void testGetSocksOnBindRequestRemoteFacingServerSocketSocketSettingsFromSettings05() {
        SocketSetting<?> socketSetting =
                StandardSocketSettingSpecConstants.SO_TIMEOUT.newSocketSetting(
                        NonNegativeInteger.valueOf(0));
        SocketSettings socketSettings = SocketSettings.of(socketSetting);
        Settings settings = Settings.of(
                SocksSettingSpecConstants.SOCKS_ON_BIND_REQUEST_REMOTE_FACING_SERVER_SOCKET_SOCKET_SETTINGS.newSetting(
                        socketSettings));
        Assert.assertEquals(
                socketSettings,
                SocksValueDerivationHelper.getSocksOnBindRequestRemoteFacingServerSocketSocketSettingsFrom(settings));
    }

    @Test
    public void testGetSocksOnBindRequestRemoteSocketSocketSettingsFromRule01() {
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
        Assert.assertEquals(
                socketSettings,
                SocksValueDerivationHelper.getSocksOnBindRequestRemoteSocketSocketSettingsFrom(rule));
    }

    @Test
    public void testGetSocksOnBindRequestRemoteSocketSocketSettingsFromRule02() {
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
        Assert.assertEquals(
                socketSettings,
                SocksValueDerivationHelper.getSocksOnBindRequestRemoteSocketSocketSettingsFrom(rule));
    }

    @Test
    public void testGetSocksOnBindRequestRemoteSocketSocketSettingsFromRule03() {
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
        Assert.assertEquals(
                socketSettings,
                SocksValueDerivationHelper.getSocksOnBindRequestRemoteSocketSocketSettingsFrom(rule));
    }

    @Test
    public void testGetSocksOnBindRequestRemoteSocketSocketSettingsFromRule04() {
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
        Assert.assertEquals(
                socketSettings,
                SocksValueDerivationHelper.getSocksOnBindRequestRemoteSocketSocketSettingsFrom(rule));
    }

    @Test
    public void testGetSocksOnBindRequestRemoteSocketSocketSettingsFromSettings01() {
        SocketSettings socketSettings = SocketSettings.of();
        Settings settings = Settings.of();
        Assert.assertEquals(
                socketSettings,
                SocksValueDerivationHelper.getSocksOnBindRequestRemoteSocketSocketSettingsFrom(settings));
    }

    @Test
    public void testGetSocksOnBindRequestRemoteSocketSocketSettingsFromSettings02() {
        SocketSetting<?> socketSetting =
                StandardSocketSettingSpecConstants.SO_TIMEOUT.newSocketSetting(
                        NonNegativeInteger.valueOf(0));
        SocketSettings socketSettings = SocketSettings.of(socketSetting);
        Settings settings = Settings.of(
                GeneralSettingSpecConstants.SOCKET_SETTINGS.newSetting(
                        socketSettings));
        Assert.assertEquals(
                socketSettings,
                SocksValueDerivationHelper.getSocksOnBindRequestRemoteSocketSocketSettingsFrom(settings));
    }

    @Test
    public void testGetSocksOnBindRequestRemoteSocketSocketSettingsFromSettings03() {
        SocketSetting<?> socketSetting =
                StandardSocketSettingSpecConstants.SO_TIMEOUT.newSocketSetting(
                        NonNegativeInteger.valueOf(0));
        SocketSettings socketSettings = SocketSettings.of(socketSetting);
        Settings settings = Settings.of(
                GeneralSettingSpecConstants.REMOTE_ENDPOINTS_SOCKET_SETTINGS.newSetting(
                        socketSettings));
        Assert.assertEquals(
                socketSettings,
                SocksValueDerivationHelper.getSocksOnBindRequestRemoteSocketSocketSettingsFrom(settings));
    }

    @Test
    public void testGetSocksOnBindRequestRemoteSocketSocketSettingsFromSettings04() {
        SocketSetting<?> socketSetting =
                StandardSocketSettingSpecConstants.SO_TIMEOUT.newSocketSetting(
                        NonNegativeInteger.valueOf(0));
        SocketSettings socketSettings = SocketSettings.of(socketSetting);
        Settings settings = Settings.of(
                SocksSettingSpecConstants.SOCKS_ON_REQUEST_REMOTE_ENDPOINTS_SOCKET_SETTINGS.newSetting(
                        socketSettings));
        Assert.assertEquals(
                socketSettings,
                SocksValueDerivationHelper.getSocksOnBindRequestRemoteSocketSocketSettingsFrom(settings));
    }

    @Test
    public void testGetSocksOnBindRequestRemoteSocketSocketSettingsFromSettings05() {
        SocketSetting<?> socketSetting =
                StandardSocketSettingSpecConstants.SO_TIMEOUT.newSocketSetting(
                        NonNegativeInteger.valueOf(0));
        SocketSettings socketSettings = SocketSettings.of(socketSetting);
        Settings settings = Settings.of(
                SocksSettingSpecConstants.SOCKS_ON_BIND_REQUEST_REMOTE_SOCKET_SOCKET_SETTINGS.newSetting(
                        socketSettings));
        Assert.assertEquals(
                socketSettings,
                SocksValueDerivationHelper.getSocksOnBindRequestRemoteSocketSocketSettingsFrom(settings));
    }

    @Test
    public void testGetSocksOnConnectRequestRelayBufferSizeFromRule01() {
        PositiveInteger relayBufferSize = PositiveInteger.valueOf(2048);
        Rule rule = new Rule.Builder()
                .addRuleAction(SocksRuleActionSpecConstants.SOCKS_ON_REQUEST_RELAY_BUFFER_SIZE.newRuleAction(
                        relayBufferSize))
                .build();
        Assert.assertEquals(
                relayBufferSize,
                SocksValueDerivationHelper.getSocksOnConnectRequestRelayBufferSizeFrom(rule));
    }

    @Test
    public void testGetSocksOnConnectRequestRelayBufferSizeFromRule02() {
        PositiveInteger relayBufferSize = PositiveInteger.valueOf(2048);
        Rule rule = new Rule.Builder()
                .addRuleAction(SocksRuleActionSpecConstants.SOCKS_ON_CONNECT_REQUEST_RELAY_BUFFER_SIZE.newRuleAction(
                        relayBufferSize))
                .build();
        Assert.assertEquals(
                relayBufferSize,
                SocksValueDerivationHelper.getSocksOnConnectRequestRelayBufferSizeFrom(rule));
    }

    @Test
    public void testGetSocksOnConnectRequestRelayBufferSizeFromSettings01() {
        PositiveInteger relayBufferSize = PositiveInteger.valueOf(1024);
        Settings settings = Settings.of();
        Assert.assertEquals(
                relayBufferSize,
                SocksValueDerivationHelper.getSocksOnConnectRequestRelayBufferSizeFrom(settings));
    }

    @Test
    public void testGetSocksOnConnectRequestRelayBufferSizeFromSettings02() {
        PositiveInteger relayBufferSize = PositiveInteger.valueOf(2048);
        Settings settings = Settings.of(
                SocksSettingSpecConstants.SOCKS_ON_REQUEST_RELAY_BUFFER_SIZE.newSetting(
                        relayBufferSize));
        Assert.assertEquals(
                relayBufferSize,
                SocksValueDerivationHelper.getSocksOnConnectRequestRelayBufferSizeFrom(settings));
    }

    @Test
    public void testGetSocksOnConnectRequestRelayBufferSizeFromSettings03() {
        PositiveInteger relayBufferSize = PositiveInteger.valueOf(2048);
        Settings settings = Settings.of(
                SocksSettingSpecConstants.SOCKS_ON_CONNECT_REQUEST_RELAY_BUFFER_SIZE.newSetting(
                        relayBufferSize));
        Assert.assertEquals(
                relayBufferSize,
                SocksValueDerivationHelper.getSocksOnConnectRequestRelayBufferSizeFrom(settings));
    }

    @Test
    public void testGetSocksOnConnectRequestRelayIdleTimeoutFromRule01() {
        PositiveInteger relayIdleTimeout = PositiveInteger.valueOf(120000);
        Rule rule = new Rule.Builder()
                .addRuleAction(SocksRuleActionSpecConstants.SOCKS_ON_REQUEST_RELAY_IDLE_TIMEOUT.newRuleAction(
                        relayIdleTimeout))
                .build();
        Assert.assertEquals(
                relayIdleTimeout,
                SocksValueDerivationHelper.getSocksOnConnectRequestRelayIdleTimeoutFrom(rule));
    }

    @Test
    public void testGetSocksOnConnectRequestRelayIdleTimeoutFromRule02() {
        PositiveInteger relayIdleTimeout = PositiveInteger.valueOf(120000);
        Rule rule = new Rule.Builder()
                .addRuleAction(SocksRuleActionSpecConstants.SOCKS_ON_CONNECT_REQUEST_RELAY_IDLE_TIMEOUT.newRuleAction(
                        relayIdleTimeout))
                .build();
        Assert.assertEquals(
                relayIdleTimeout,
                SocksValueDerivationHelper.getSocksOnConnectRequestRelayIdleTimeoutFrom(rule));
    }

    @Test
    public void testGetSocksOnConnectRequestRelayIdleTimeoutFromSettings01() {
        PositiveInteger relayIdleTimeout = PositiveInteger.valueOf(60000);
        Settings settings = Settings.of();
        Assert.assertEquals(
                relayIdleTimeout,
                SocksValueDerivationHelper.getSocksOnConnectRequestRelayIdleTimeoutFrom(settings));
    }

    @Test
    public void testGetSocksOnConnectRequestRelayIdleTimeoutFromSettings02() {
        PositiveInteger relayIdleTimeout = PositiveInteger.valueOf(120000);

        Settings settings = Settings.of(
                SocksSettingSpecConstants.SOCKS_ON_REQUEST_RELAY_IDLE_TIMEOUT.newSetting(
                        relayIdleTimeout));
        Assert.assertEquals(
                relayIdleTimeout,
                SocksValueDerivationHelper.getSocksOnConnectRequestRelayIdleTimeoutFrom(settings));
    }

    @Test
    public void testGetSocksOnConnectRequestRelayIdleTimeoutFromSettings03() {
        PositiveInteger relayIdleTimeout = PositiveInteger.valueOf(120000);
        Settings settings = Settings.of(
                SocksSettingSpecConstants.SOCKS_ON_CONNECT_REQUEST_RELAY_IDLE_TIMEOUT.newSetting(
                        relayIdleTimeout));
        Assert.assertEquals(
                relayIdleTimeout,
                SocksValueDerivationHelper.getSocksOnConnectRequestRelayIdleTimeoutFrom(settings));
    }

    @Test
    public void testGetSocksOnConnectRequestRelayInboundBandwidthLimitFromRule01() {
        PositiveInteger relayInboundBandwidthLimit = PositiveInteger.valueOf(
                Integer.MAX_VALUE);
        Rule rule = new Rule.Builder()
                .addRuleAction(SocksRuleActionSpecConstants.SOCKS_ON_REQUEST_RELAY_INBOUND_BANDWIDTH_LIMIT.newRuleAction(
                        relayInboundBandwidthLimit))
                .build();
        Assert.assertEquals(
                relayInboundBandwidthLimit,
                SocksValueDerivationHelper.getSocksOnConnectRequestRelayInboundBandwidthLimitFrom(rule));
    }

    @Test
    public void testGetSocksOnConnectRequestRelayInboundBandwidthLimitFromRule02() {
        PositiveInteger relayInboundBandwidthLimit = PositiveInteger.valueOf(
                Integer.MAX_VALUE);
        Rule rule = new Rule.Builder()
                .addRuleAction(SocksRuleActionSpecConstants.SOCKS_ON_CONNECT_REQUEST_RELAY_INBOUND_BANDWIDTH_LIMIT.newRuleAction(
                        relayInboundBandwidthLimit))
                .build();
        Assert.assertEquals(
                relayInboundBandwidthLimit,
                SocksValueDerivationHelper.getSocksOnConnectRequestRelayInboundBandwidthLimitFrom(rule));
    }

    @Test
    public void testGetSocksOnConnectRequestRelayInboundBandwidthLimitFromSettings01() {
        Settings settings = Settings.of();
        Assert.assertNull(
                SocksValueDerivationHelper.getSocksOnConnectRequestRelayInboundBandwidthLimitFrom(settings));
    }

    @Test
    public void testGetSocksOnConnectRequestRelayInboundBandwidthLimitFromSettings02() {
        PositiveInteger relayInboundBandwidthLimit = PositiveInteger.valueOf(
                Integer.MAX_VALUE);
        Settings settings = Settings.of(
                SocksSettingSpecConstants.SOCKS_ON_REQUEST_RELAY_INBOUND_BANDWIDTH_LIMIT.newSetting(
                        relayInboundBandwidthLimit));
        Assert.assertEquals(
                relayInboundBandwidthLimit,
                SocksValueDerivationHelper.getSocksOnConnectRequestRelayInboundBandwidthLimitFrom(settings));
    }

    @Test
    public void testGetSocksOnConnectRequestRelayInboundBandwidthLimitFromSettings03() {
        PositiveInteger relayInboundBandwidthLimit = PositiveInteger.valueOf(
                Integer.MAX_VALUE);
        Settings settings = Settings.of(
                SocksSettingSpecConstants.SOCKS_ON_CONNECT_REQUEST_RELAY_INBOUND_BANDWIDTH_LIMIT.newSetting(
                        relayInboundBandwidthLimit));
        Assert.assertEquals(
                relayInboundBandwidthLimit,
                SocksValueDerivationHelper.getSocksOnConnectRequestRelayInboundBandwidthLimitFrom(settings));
    }

    @Test
    public void testGetSocksOnConnectRequestRelayOutboundBandwidthLimitFromRule01() {
        PositiveInteger relayOutboundBandwidthLimit = PositiveInteger.valueOf(
                Integer.MAX_VALUE);
        Rule rule = new Rule.Builder()
                .addRuleAction(SocksRuleActionSpecConstants.SOCKS_ON_REQUEST_RELAY_OUTBOUND_BANDWIDTH_LIMIT.newRuleAction(
                        relayOutboundBandwidthLimit))
                .build();
        Assert.assertEquals(
                relayOutboundBandwidthLimit,
                SocksValueDerivationHelper.getSocksOnConnectRequestRelayOutboundBandwidthLimitFrom(rule));
    }

    @Test
    public void testGetSocksOnConnectRequestRelayOutboundBandwidthLimitFromRule02() {
        PositiveInteger relayOutboundBandwidthLimit = PositiveInteger.valueOf(
                Integer.MAX_VALUE);
        Rule rule = new Rule.Builder()
                .addRuleAction(SocksRuleActionSpecConstants.SOCKS_ON_CONNECT_REQUEST_RELAY_OUTBOUND_BANDWIDTH_LIMIT.newRuleAction(
                        relayOutboundBandwidthLimit))
                .build();
        Assert.assertEquals(
                relayOutboundBandwidthLimit,
                SocksValueDerivationHelper.getSocksOnConnectRequestRelayOutboundBandwidthLimitFrom(rule));
    }

    @Test
    public void testGetSocksOnConnectRequestRelayOutboundBandwidthLimitFromSettings01() {
        Settings settings = Settings.of();
        Assert.assertNull(
                SocksValueDerivationHelper.getSocksOnConnectRequestRelayOutboundBandwidthLimitFrom(settings));
    }

    @Test
    public void testGetSocksOnConnectRequestRelayOutboundBandwidthLimitFromSettings02() {
        PositiveInteger relayOutboundBandwidthLimit = PositiveInteger.valueOf(
                Integer.MAX_VALUE);
        Settings settings = Settings.of(
                SocksSettingSpecConstants.SOCKS_ON_REQUEST_RELAY_OUTBOUND_BANDWIDTH_LIMIT.newSetting(
                        relayOutboundBandwidthLimit));
        Assert.assertEquals(
                relayOutboundBandwidthLimit,
                SocksValueDerivationHelper.getSocksOnConnectRequestRelayOutboundBandwidthLimitFrom(settings));
    }

    @Test
    public void testGetSocksOnConnectRequestRelayOutboundBandwidthLimitFromSettings03() {
        PositiveInteger relayOutboundBandwidthLimit = PositiveInteger.valueOf(
                Integer.MAX_VALUE);
        Settings settings = Settings.of(
                SocksSettingSpecConstants.SOCKS_ON_CONNECT_REQUEST_RELAY_OUTBOUND_BANDWIDTH_LIMIT.newSetting(
                        relayOutboundBandwidthLimit));
        Assert.assertEquals(
                relayOutboundBandwidthLimit,
                SocksValueDerivationHelper.getSocksOnConnectRequestRelayOutboundBandwidthLimitFrom(settings));
    }

    @Test
    public void testGetSocksOnConnectRequestRemoteSocketBindHostAddressTypesFromRule01() {
        HostAddressType hostAddressType = HostAddressType.HOST_IPV4_ADDRESS;
        HostAddressTypes hostAddressTypes = HostAddressTypes.of(hostAddressType);
        Rule rule = new Rule.Builder()
                .addRuleAction(GeneralRuleActionSpecConstants.BIND_HOST_ADDRESS_TYPE.newRuleAction(
                        hostAddressType))
                .build();
        Assert.assertEquals(
                hostAddressTypes,
                SocksValueDerivationHelper.getSocksOnConnectRequestRemoteSocketBindHostAddressTypesFrom(rule));
    }

    @Test
    public void testGetSocksOnConnectRequestRemoteSocketBindHostAddressTypesFromRule02() {
        HostAddressType hostAddressType = HostAddressType.HOST_IPV4_ADDRESS;
        HostAddressTypes hostAddressTypes = HostAddressTypes.of(hostAddressType);
        Rule rule = new Rule.Builder()
                .addRuleAction(GeneralRuleActionSpecConstants.REMOTE_ENDPOINTS_BIND_HOST_ADDRESS_TYPE.newRuleAction(
                        hostAddressType))
                .build();
        Assert.assertEquals(
                hostAddressTypes,
                SocksValueDerivationHelper.getSocksOnConnectRequestRemoteSocketBindHostAddressTypesFrom(rule));
    }

    @Test
    public void testGetSocksOnConnectRequestRemoteSocketBindHostAddressTypesFromRule03() {
        HostAddressType hostAddressType = HostAddressType.HOST_IPV4_ADDRESS;
        HostAddressTypes hostAddressTypes = HostAddressTypes.of(hostAddressType);
        Rule rule = new Rule.Builder()
                .addRuleAction(SocksRuleActionSpecConstants.SOCKS_ON_REQUEST_REMOTE_ENDPOINTS_BIND_HOST_ADDRESS_TYPE.newRuleAction(
                        hostAddressType))
                .build();
        Assert.assertEquals(
                hostAddressTypes,
                SocksValueDerivationHelper.getSocksOnConnectRequestRemoteSocketBindHostAddressTypesFrom(rule));
    }

    @Test
    public void testGetSocksOnConnectRequestRemoteSocketBindHostAddressTypesFromRule04() {
        HostAddressType hostAddressType = HostAddressType.HOST_IPV4_ADDRESS;
        HostAddressTypes hostAddressTypes = HostAddressTypes.of(hostAddressType);
        Rule rule = new Rule.Builder()
                .addRuleAction(SocksRuleActionSpecConstants.SOCKS_ON_CONNECT_REQUEST_REMOTE_SOCKET_BIND_HOST_ADDRESS_TYPE.newRuleAction(
                        hostAddressType))
                .build();
        Assert.assertEquals(
                hostAddressTypes,
                SocksValueDerivationHelper.getSocksOnConnectRequestRemoteSocketBindHostAddressTypesFrom(rule));
    }

    @Test
    public void testGetSocksOnConnectRequestRemoteSocketBindHostAddressTypesFromSettings01() {
        Settings settings = Settings.of();
        Assert.assertEquals(
                HostAddressTypes.of(
                        HostAddressType.HOST_IPV4_ADDRESS,
                        HostAddressType.HOST_IPV6_ADDRESS),
                SocksValueDerivationHelper.getSocksOnConnectRequestRemoteSocketBindHostAddressTypesFrom(settings));
    }

    @Test
    public void testGetSocksOnConnectRequestRemoteSocketBindHostAddressTypesFromSettings02() {
        HostAddressType hostAddressType = HostAddressType.HOST_IPV4_ADDRESS;
        HostAddressTypes hostAddressTypes = HostAddressTypes.of(hostAddressType);
        Settings settings = Settings.of(
                GeneralSettingSpecConstants.BIND_HOST_ADDRESS_TYPES.newSetting(
                        hostAddressTypes));
        Assert.assertEquals(
                hostAddressTypes,
                SocksValueDerivationHelper.getSocksOnConnectRequestRemoteSocketBindHostAddressTypesFrom(settings));
    }

    @Test
    public void testGetSocksOnConnectRequestRemoteSocketBindHostAddressTypesFromSettings03() {
        HostAddressType hostAddressType = HostAddressType.HOST_IPV4_ADDRESS;
        HostAddressTypes hostAddressTypes = HostAddressTypes.of(hostAddressType);
        Settings settings = Settings.of(
                GeneralSettingSpecConstants.REMOTE_ENDPOINTS_BIND_HOST_ADDRESS_TYPES.newSetting(
                        hostAddressTypes));
        Assert.assertEquals(
                hostAddressTypes,
                SocksValueDerivationHelper.getSocksOnConnectRequestRemoteSocketBindHostAddressTypesFrom(settings));
    }

    @Test
    public void testGetSocksOnConnectRequestRemoteSocketBindHostAddressTypesFromSettings04() {
        HostAddressType hostAddressType = HostAddressType.HOST_IPV4_ADDRESS;
        HostAddressTypes hostAddressTypes = HostAddressTypes.of(hostAddressType);
        Settings settings = Settings.of(
                SocksSettingSpecConstants.SOCKS_ON_REQUEST_REMOTE_ENDPOINTS_BIND_HOST_ADDRESS_TYPES.newSetting(
                        hostAddressTypes));
        Assert.assertEquals(
                hostAddressTypes,
                SocksValueDerivationHelper.getSocksOnConnectRequestRemoteSocketBindHostAddressTypesFrom(settings));
    }

    @Test
    public void testGetSocksOnConnectRequestRemoteSocketBindHostAddressTypesFromSettings05() {
        HostAddressType hostAddressType = HostAddressType.HOST_IPV4_ADDRESS;
        HostAddressTypes hostAddressTypes = HostAddressTypes.of(hostAddressType);
        Settings settings = Settings.of(
                SocksSettingSpecConstants.SOCKS_ON_CONNECT_REQUEST_REMOTE_SOCKET_BIND_HOST_ADDRESS_TYPES.newSetting(
                        hostAddressTypes));
        Assert.assertEquals(
                hostAddressTypes,
                SocksValueDerivationHelper.getSocksOnConnectRequestRemoteSocketBindHostAddressTypesFrom(settings));
    }

    @Test
    public void testGetSocksOnConnectRequestRemoteSocketBindHostFromRule01() {
        Host host = Host.newInstance("127.0.0.1");
        Rule rule = new Rule.Builder()
                .addRuleAction(GeneralRuleActionSpecConstants.BIND_HOST.newRuleAction(
                        host))
                .build();
        Assert.assertEquals(
                host,
                SocksValueDerivationHelper.getSocksOnConnectRequestRemoteSocketBindHostFrom(rule));
    }

    @Test
    public void testGetSocksOnConnectRequestRemoteSocketBindHostFromRule02() {
        Host host = Host.newInstance("127.0.0.1");
        Rule rule = new Rule.Builder()
                .addRuleAction(GeneralRuleActionSpecConstants.REMOTE_ENDPOINTS_BIND_HOST.newRuleAction(
                        host))
                .build();
        Assert.assertEquals(
                host,
                SocksValueDerivationHelper.getSocksOnConnectRequestRemoteSocketBindHostFrom(rule));
    }

    @Test
    public void testGetSocksOnConnectRequestRemoteSocketBindHostFromRule03() {
        Host host = Host.newInstance("127.0.0.1");
        Rule rule = new Rule.Builder()
                .addRuleAction(SocksRuleActionSpecConstants.SOCKS_ON_REQUEST_REMOTE_ENDPOINTS_BIND_HOST.newRuleAction(
                        host))
                .build();
        Assert.assertEquals(
                host,
                SocksValueDerivationHelper.getSocksOnConnectRequestRemoteSocketBindHostFrom(rule));
    }

    @Test
    public void testGetSocksOnConnectRequestRemoteSocketBindHostFromRule04() {
        Host host = Host.newInstance("127.0.0.1");
        Rule rule = new Rule.Builder()
                .addRuleAction(SocksRuleActionSpecConstants.SOCKS_ON_CONNECT_REQUEST_REMOTE_SOCKET_BIND_HOST.newRuleAction(
                        host))
                .build();
        Assert.assertEquals(
                host,
                SocksValueDerivationHelper.getSocksOnConnectRequestRemoteSocketBindHostFrom(rule));
    }

    @Test
    public void testGetSocksOnConnectRequestRemoteSocketBindHostFromSettings01() {
        Settings settings = Settings.of();
        Assert.assertNull(SocksValueDerivationHelper.getSocksOnConnectRequestRemoteSocketBindHostFrom(settings));
    }

    @Test
    public void testGetSocksOnConnectRequestRemoteSocketBindHostFromSettings02() {
        Host host = Host.newInstance("127.0.0.1");
        Settings settings = Settings.of(
                GeneralSettingSpecConstants.BIND_HOST.newSetting(host));
        Assert.assertEquals(
                host,
                SocksValueDerivationHelper.getSocksOnConnectRequestRemoteSocketBindHostFrom(settings));
    }

    @Test
    public void testGetSocksOnConnectRequestRemoteSocketBindHostFromSettings03() {
        Host host = Host.newInstance("127.0.0.1");
        Settings settings = Settings.of(
                GeneralSettingSpecConstants.REMOTE_ENDPOINTS_BIND_HOST.newSetting(
                        host));
        Assert.assertEquals(
                host,
                SocksValueDerivationHelper.getSocksOnConnectRequestRemoteSocketBindHostFrom(settings));
    }

    @Test
    public void testGetSocksOnConnectRequestRemoteSocketBindHostFromSettings04() {
        Host host = Host.newInstance("127.0.0.1");
        Settings settings = Settings.of(
                SocksSettingSpecConstants.SOCKS_ON_REQUEST_REMOTE_ENDPOINTS_BIND_HOST.newSetting(
                        host));
        Assert.assertEquals(
                host,
                SocksValueDerivationHelper.getSocksOnConnectRequestRemoteSocketBindHostFrom(settings));
    }

    @Test
    public void testGetSocksOnConnectRequestRemoteSocketBindHostFromSettings05() {
        Host host = Host.newInstance("127.0.0.1");
        Settings settings = Settings.of(
                SocksSettingSpecConstants.SOCKS_ON_CONNECT_REQUEST_REMOTE_SOCKET_BIND_HOST.newSetting(
                        host));
        Assert.assertEquals(
                host,
                SocksValueDerivationHelper.getSocksOnConnectRequestRemoteSocketBindHostFrom(settings));
    }

    @Test
    public void testGetSocksOnConnectRequestRemoteSocketBindPortRangesFromRule01() {
        PortRange portRange = PortRange.of(
                Port.valueOf(1), Port.valueOf(65535));
        PortRanges portRanges = PortRanges.of(portRange);
        Rule rule = new Rule.Builder()
                .addRuleAction(GeneralRuleActionSpecConstants.BIND_TCP_PORT_RANGE.newRuleAction(
                        portRange))
                .build();
        Assert.assertEquals(
                portRanges,
                SocksValueDerivationHelper.getSocksOnConnectRequestRemoteSocketBindPortRangesFrom(rule));
    }

    @Test
    public void testGetSocksOnConnectRequestRemoteSocketBindPortRangesFromRule02() {
        PortRange portRange = PortRange.of(
                Port.valueOf(1), Port.valueOf(65535));
        PortRanges portRanges = PortRanges.of(portRange);
        Rule rule = new Rule.Builder()
                .addRuleAction(GeneralRuleActionSpecConstants.REMOTE_ENDPOINTS_BIND_TCP_PORT_RANGE.newRuleAction(
                        portRange))
                .build();
        Assert.assertEquals(
                portRanges,
                SocksValueDerivationHelper.getSocksOnConnectRequestRemoteSocketBindPortRangesFrom(rule));
    }

    @Test
    public void testGetSocksOnConnectRequestRemoteSocketBindPortRangesFromRule03() {
        PortRange portRange = PortRange.of(
                Port.valueOf(1), Port.valueOf(65535));
        PortRanges portRanges = PortRanges.of(portRange);
        Rule rule = new Rule.Builder()
                .addRuleAction(SocksRuleActionSpecConstants.SOCKS_ON_REQUEST_REMOTE_ENDPOINTS_BIND_TCP_PORT_RANGE.newRuleAction(
                        portRange))
                .build();
        Assert.assertEquals(
                portRanges,
                SocksValueDerivationHelper.getSocksOnConnectRequestRemoteSocketBindPortRangesFrom(rule));
    }

    @Test
    public void testGetSocksOnConnectRequestRemoteSocketBindPortRangesFromRule04() {
        PortRange portRange = PortRange.of(
                Port.valueOf(1), Port.valueOf(65535));
        PortRanges portRanges = PortRanges.of(portRange);
        Rule rule = new Rule.Builder()
                .addRuleAction(SocksRuleActionSpecConstants.SOCKS_ON_CONNECT_REQUEST_REMOTE_SOCKET_BIND_PORT_RANGE.newRuleAction(
                        portRange))
                .build();
        Assert.assertEquals(
                portRanges,
                SocksValueDerivationHelper.getSocksOnConnectRequestRemoteSocketBindPortRangesFrom(rule));
    }

    @Test
    public void testGetSocksOnConnectRequestRemoteSocketBindPortRangesFromSettings01() {
        PortRanges portRanges = PortRanges.getDefault();
        Settings settings = Settings.of();
        Assert.assertEquals(
                portRanges,
                SocksValueDerivationHelper.getSocksOnConnectRequestRemoteSocketBindPortRangesFrom(settings));
    }

    @Test
    public void testGetSocksOnConnectRequestRemoteSocketBindPortRangesFromSettings02() {
        PortRange portRange = PortRange.of(
                Port.valueOf(1), Port.valueOf(65535));
        PortRanges portRanges = PortRanges.of(portRange);
        Settings settings = Settings.of(
                GeneralSettingSpecConstants.BIND_TCP_PORT_RANGES.newSetting(
                        portRanges));
        Assert.assertEquals(
                portRanges,
                SocksValueDerivationHelper.getSocksOnConnectRequestRemoteSocketBindPortRangesFrom(settings));
    }

    @Test
    public void testGetSocksOnConnectRequestRemoteSocketBindPortRangesFromSettings03() {
        PortRange portRange = PortRange.of(
                Port.valueOf(1), Port.valueOf(65535));
        PortRanges portRanges = PortRanges.of(portRange);
        Settings settings = Settings.of(
                GeneralSettingSpecConstants.REMOTE_ENDPOINTS_BIND_TCP_PORT_RANGES.newSetting(
                        portRanges));
        Assert.assertEquals(
                portRanges,
                SocksValueDerivationHelper.getSocksOnConnectRequestRemoteSocketBindPortRangesFrom(settings));
    }

    @Test
    public void testGetSocksOnConnectRequestRemoteSocketBindPortRangesFromSettings04() {
        PortRange portRange = PortRange.of(
                Port.valueOf(1), Port.valueOf(65535));
        PortRanges portRanges = PortRanges.of(portRange);
        Settings settings = Settings.of(
                SocksSettingSpecConstants.SOCKS_ON_REQUEST_REMOTE_ENDPOINTS_BIND_TCP_PORT_RANGES.newSetting(
                        portRanges));
        Assert.assertEquals(
                portRanges,
                SocksValueDerivationHelper.getSocksOnConnectRequestRemoteSocketBindPortRangesFrom(settings));
    }

    @Test
    public void testGetSocksOnConnectRequestRemoteSocketBindPortRangesFromSettings05() {
        PortRange portRange = PortRange.of(
                Port.valueOf(1), Port.valueOf(65535));
        PortRanges portRanges = PortRanges.of(portRange);
        Settings settings = Settings.of(
                SocksSettingSpecConstants.SOCKS_ON_CONNECT_REQUEST_REMOTE_SOCKET_BIND_PORT_RANGES.newSetting(
                        portRanges));
        Assert.assertEquals(
                portRanges,
                SocksValueDerivationHelper.getSocksOnConnectRequestRemoteSocketBindPortRangesFrom(settings));
    }

    @Test
    public void testGetSocksOnConnectRequestRemoteSocketConnectTimeoutFromRule01() {
        PositiveInteger i = PositiveInteger.valueOf(1);
        Rule rule = new Rule.Builder()
                .addRuleAction(SocksRuleActionSpecConstants.SOCKS_ON_CONNECT_REQUEST_REMOTE_SOCKET_CONNECT_TIMEOUT.newRuleAction(i))
                .build();
        Assert.assertEquals(
                i,
                SocksValueDerivationHelper.getSocksOnConnectRequestRemoteSocketConnectTimeoutFrom(rule));
    }

    @Test
    public void testGetSocksOnConnectRequestRemoteSocketConnectTimeoutFromSettings01() {
        Settings settings = Settings.of();
        Assert.assertEquals(
                PositiveInteger.valueOf(60000),
                SocksValueDerivationHelper.getSocksOnConnectRequestRemoteSocketConnectTimeoutFrom(settings));
    }

    @Test
    public void testGetSocksOnConnectRequestRemoteSocketConnectTimeoutFromSettings02() {
        PositiveInteger i = PositiveInteger.valueOf(1);
        Settings settings = Settings.of(
                SocksSettingSpecConstants.SOCKS_ON_CONNECT_REQUEST_REMOTE_SOCKET_CONNECT_TIMEOUT.newSetting(i));
        Assert.assertEquals(
                i,
                SocksValueDerivationHelper.getSocksOnConnectRequestRemoteSocketConnectTimeoutFrom(settings));
    }

    @Test
    public void testGetSocksOnConnectRequestRemoteSocketNetInterfaceFromRule01() throws UnknownHostException, SocketException {
        Host host = Host.newInstance("127.0.0.1");
        NetworkInterface networkInterface = NetworkInterface.getByInetAddress(
                host.toInetAddress());
        NetInterface netInterface = NetInterface.newInstance(networkInterface);
        Rule rule = new Rule.Builder()
                .addRuleAction(GeneralRuleActionSpecConstants.NET_INTERFACE.newRuleAction(
                        netInterface))
                .build();
        Assert.assertEquals(
                netInterface,
                SocksValueDerivationHelper.getSocksOnConnectRequestRemoteSocketNetInterfaceFrom(rule));
    }

    @Test
    public void testGetSocksOnConnectRequestRemoteSocketNetInterfaceFromRule02() throws UnknownHostException, SocketException {
        Host host = Host.newInstance("127.0.0.1");
        NetworkInterface networkInterface = NetworkInterface.getByInetAddress(
                host.toInetAddress());
        NetInterface netInterface = NetInterface.newInstance(networkInterface);
        Rule rule = new Rule.Builder()
                .addRuleAction(GeneralRuleActionSpecConstants.REMOTE_ENDPOINTS_NET_INTERFACE.newRuleAction(
                        netInterface))
                .build();
        Assert.assertEquals(
                netInterface,
                SocksValueDerivationHelper.getSocksOnConnectRequestRemoteSocketNetInterfaceFrom(rule));
    }

    @Test
    public void testGetSocksOnConnectRequestRemoteSocketNetInterfaceFromRule03() throws UnknownHostException, SocketException {
        Host host = Host.newInstance("127.0.0.1");
        NetworkInterface networkInterface = NetworkInterface.getByInetAddress(
                host.toInetAddress());
        NetInterface netInterface = NetInterface.newInstance(networkInterface);
        Rule rule = new Rule.Builder()
                .addRuleAction(SocksRuleActionSpecConstants.SOCKS_ON_REQUEST_REMOTE_ENDPOINTS_NET_INTERFACE.newRuleAction(
                        netInterface))
                .build();
        Assert.assertEquals(
                netInterface,
                SocksValueDerivationHelper.getSocksOnConnectRequestRemoteSocketNetInterfaceFrom(rule));
    }

    @Test
    public void testGetSocksOnConnectRequestRemoteSocketNetInterfaceFromRule04() throws UnknownHostException, SocketException {
        Host host = Host.newInstance("127.0.0.1");
        NetworkInterface networkInterface = NetworkInterface.getByInetAddress(
                host.toInetAddress());
        NetInterface netInterface = NetInterface.newInstance(networkInterface);
        Rule rule = new Rule.Builder()
                .addRuleAction(SocksRuleActionSpecConstants.SOCKS_ON_CONNECT_REQUEST_REMOTE_SOCKET_NET_INTERFACE.newRuleAction(
                        netInterface))
                .build();
        Assert.assertEquals(
                netInterface,
                SocksValueDerivationHelper.getSocksOnConnectRequestRemoteSocketNetInterfaceFrom(rule));
    }

    @Test
    public void testGetSocksOnConnectRequestRemoteSocketNetInterfaceFromSettings01() {
        Settings settings = Settings.of();
        Assert.assertNull(SocksValueDerivationHelper.getSocksOnConnectRequestRemoteSocketNetInterfaceFrom(settings));
    }

    @Test
    public void testGetSocksOnConnectRequestRemoteSocketNetInterfaceFromSettings02() throws UnknownHostException, SocketException {
        Host host = Host.newInstance("127.0.0.1");
        NetworkInterface networkInterface = NetworkInterface.getByInetAddress(
                host.toInetAddress());
        NetInterface netInterface = NetInterface.newInstance(networkInterface);
        Settings settings = Settings.of(
                GeneralSettingSpecConstants.NET_INTERFACE.newSetting(
                        netInterface));
        Assert.assertEquals(
                netInterface,
                SocksValueDerivationHelper.getSocksOnConnectRequestRemoteSocketNetInterfaceFrom(settings));
    }

    @Test
    public void testGetSocksOnConnectRequestRemoteSocketNetInterfaceFromSettings03() throws UnknownHostException, SocketException {
        Host host = Host.newInstance("127.0.0.1");
        NetworkInterface networkInterface = NetworkInterface.getByInetAddress(
                host.toInetAddress());
        NetInterface netInterface = NetInterface.newInstance(networkInterface);
        Settings settings = Settings.of(
                GeneralSettingSpecConstants.REMOTE_ENDPOINTS_NET_INTERFACE.newSetting(
                        netInterface));
        Assert.assertEquals(
                netInterface,
                SocksValueDerivationHelper.getSocksOnConnectRequestRemoteSocketNetInterfaceFrom(settings));
    }

    @Test
    public void testGetSocksOnConnectRequestRemoteSocketNetInterfaceFromSettings04() throws UnknownHostException, SocketException {
        Host host = Host.newInstance("127.0.0.1");
        NetworkInterface networkInterface = NetworkInterface.getByInetAddress(
                host.toInetAddress());
        NetInterface netInterface = NetInterface.newInstance(networkInterface);
        Settings settings = Settings.of(
                SocksSettingSpecConstants.SOCKS_ON_REQUEST_REMOTE_ENDPOINTS_NET_INTERFACE.newSetting(
                        netInterface));
        Assert.assertEquals(
                netInterface,
                SocksValueDerivationHelper.getSocksOnConnectRequestRemoteSocketNetInterfaceFrom(settings));
    }

    @Test
    public void testGetSocksOnConnectRequestRemoteSocketNetInterfaceFromSettings05() throws UnknownHostException, SocketException {
        Host host = Host.newInstance("127.0.0.1");
        NetworkInterface networkInterface = NetworkInterface.getByInetAddress(
                host.toInetAddress());
        NetInterface netInterface = NetInterface.newInstance(networkInterface);
        Settings settings = Settings.of(
                SocksSettingSpecConstants.SOCKS_ON_CONNECT_REQUEST_REMOTE_SOCKET_NET_INTERFACE.newSetting(
                        netInterface));
        Assert.assertEquals(
                netInterface,
                SocksValueDerivationHelper.getSocksOnConnectRequestRemoteSocketNetInterfaceFrom(settings));
    }

    @Test
    public void testGetSocksOnConnectRequestRemoteSocketPrepareSocketFromRule01() {
        Rule rule = new Rule.Builder()
                .addRuleAction(SocksRuleActionSpecConstants.SOCKS_ON_CONNECT_REQUEST_REMOTE_SOCKET_PREPARE_SOCKET.newRuleAction(
                        Boolean.TRUE))
                .build();
        Assert.assertTrue(
                SocksValueDerivationHelper.getSocksOnConnectRequestRemoteSocketPrepareSocketFrom(rule));
    }

    @Test
    public void testGetSocksOnConnectRequestRemoteSocketPrepareSocketFromSettings01() {
        Settings settings = Settings.of();
        Assert.assertFalse(SocksValueDerivationHelper.getSocksOnConnectRequestRemoteSocketPrepareSocketFrom(settings));
    }

    @Test
    public void testGetSocksOnConnectRequestRemoteSocketPrepareSocketFromSettings02() {
        Settings settings = Settings.of(
                SocksSettingSpecConstants.SOCKS_ON_CONNECT_REQUEST_REMOTE_SOCKET_PREPARE_SOCKET.newSetting(
                        Boolean.TRUE));
        Assert.assertTrue(SocksValueDerivationHelper.getSocksOnConnectRequestRemoteSocketPrepareSocketFrom(settings));
    }

    @Test
    public void testGetSocksOnConnectRequestRemoteSocketSocketSettingsFromRule01() {
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
        Assert.assertEquals(
                socketSettings,
                SocksValueDerivationHelper.getSocksOnConnectRequestRemoteSocketSocketSettingsFrom(rule));
    }

    @Test
    public void testGetSocksOnConnectRequestRemoteSocketSocketSettingsFromRule02() {
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
        Assert.assertEquals(
                socketSettings,
                SocksValueDerivationHelper.getSocksOnConnectRequestRemoteSocketSocketSettingsFrom(rule));
    }

    @Test
    public void testGetSocksOnConnectRequestRemoteSocketSocketSettingsFromRule03() {
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
        Assert.assertEquals(
                socketSettings,
                SocksValueDerivationHelper.getSocksOnConnectRequestRemoteSocketSocketSettingsFrom(rule));
    }

    @Test
    public void testGetSocksOnConnectRequestRemoteSocketSocketSettingsFromRule04() {
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
        Assert.assertEquals(
                socketSettings,
                SocksValueDerivationHelper.getSocksOnConnectRequestRemoteSocketSocketSettingsFrom(rule));
    }

    @Test
    public void testGetSocksOnConnectRequestRemoteSocketSocketSettingsFromSettings01() {
        SocketSettings socketSettings = SocketSettings.of();
        Settings settings = Settings.of();
        Assert.assertEquals(
                socketSettings,
                SocksValueDerivationHelper.getSocksOnConnectRequestRemoteSocketSocketSettingsFrom(settings));
    }

    @Test
    public void testGetSocksOnConnectRequestRemoteSocketSocketSettingsFromSettings02() {
        SocketSetting<?> socketSetting =
                StandardSocketSettingSpecConstants.SO_TIMEOUT.newSocketSetting(
                        NonNegativeInteger.valueOf(0));
        SocketSettings socketSettings = SocketSettings.of(socketSetting);
        Settings settings = Settings.of(
                GeneralSettingSpecConstants.SOCKET_SETTINGS.newSetting(
                        socketSettings));
        Assert.assertEquals(
                socketSettings,
                SocksValueDerivationHelper.getSocksOnConnectRequestRemoteSocketSocketSettingsFrom(settings));
    }

    @Test
    public void testGetSocksOnConnectRequestRemoteSocketSocketSettingsFromSettings03() {
        SocketSetting<?> socketSetting =
                StandardSocketSettingSpecConstants.SO_TIMEOUT.newSocketSetting(
                        NonNegativeInteger.valueOf(0));
        SocketSettings socketSettings = SocketSettings.of(socketSetting);
        Settings settings = Settings.of(
                GeneralSettingSpecConstants.REMOTE_ENDPOINTS_SOCKET_SETTINGS.newSetting(
                        socketSettings));
        Assert.assertEquals(
                socketSettings,
                SocksValueDerivationHelper.getSocksOnConnectRequestRemoteSocketSocketSettingsFrom(settings));
    }

    @Test
    public void testGetSocksOnConnectRequestRemoteSocketSocketSettingsFromSettings04() {
        SocketSetting<?> socketSetting =
                StandardSocketSettingSpecConstants.SO_TIMEOUT.newSocketSetting(
                        NonNegativeInteger.valueOf(0));
        SocketSettings socketSettings = SocketSettings.of(socketSetting);
        Settings settings = Settings.of(
                SocksSettingSpecConstants.SOCKS_ON_REQUEST_REMOTE_ENDPOINTS_SOCKET_SETTINGS.newSetting(
                        socketSettings));
        Assert.assertEquals(
                socketSettings,
                SocksValueDerivationHelper.getSocksOnConnectRequestRemoteSocketSocketSettingsFrom(settings));
    }

    @Test
    public void testGetSocksOnConnectRequestRemoteSocketSocketSettingsFromSettings05() {
        SocketSetting<?> socketSetting =
                StandardSocketSettingSpecConstants.SO_TIMEOUT.newSocketSetting(
                        NonNegativeInteger.valueOf(0));
        SocketSettings socketSettings = SocketSettings.of(socketSetting);
        Settings settings = Settings.of(
                SocksSettingSpecConstants.SOCKS_ON_CONNECT_REQUEST_REMOTE_SOCKET_SOCKET_SETTINGS.newSetting(
                        socketSettings));
        Assert.assertEquals(
                socketSettings,
                SocksValueDerivationHelper.getSocksOnConnectRequestRemoteSocketSocketSettingsFrom(settings));
    }

    @Test
    public void testGetSocksOnUdpAssociateRequestClientFacingDatagramSocketBindHostAddressTypesFromRule01() {
        HostAddressType hostAddressType = HostAddressType.HOST_IPV4_ADDRESS;
        HostAddressTypes hostAddressTypes = HostAddressTypes.of(hostAddressType);
        Rule rule = new Rule.Builder()
                .addRuleAction(GeneralRuleActionSpecConstants.BIND_HOST_ADDRESS_TYPE.newRuleAction(
                        hostAddressType))
                .build();
        Assert.assertEquals(
                hostAddressTypes,
                SocksValueDerivationHelper.getSocksOnUdpAssociateRequestClientFacingDatagramSocketBindHostAddressTypesFrom(rule));
    }

    @Test
    public void testGetSocksOnUdpAssociateRequestClientFacingDatagramSocketBindHostAddressTypesFromRule02() {
        HostAddressType hostAddressType = HostAddressType.HOST_IPV4_ADDRESS;
        HostAddressTypes hostAddressTypes = HostAddressTypes.of(hostAddressType);
        Rule rule = new Rule.Builder()
                .addRuleAction(GeneralRuleActionSpecConstants.CLIENT_ENDPOINTS_BIND_HOST_ADDRESS_TYPE.newRuleAction(
                        hostAddressType))
                .build();
        Assert.assertEquals(
                hostAddressTypes,
                SocksValueDerivationHelper.getSocksOnUdpAssociateRequestClientFacingDatagramSocketBindHostAddressTypesFrom(rule));
    }

    @Test
    public void testGetSocksOnUdpAssociateRequestClientFacingDatagramSocketBindHostAddressTypesFromRule03() {
        HostAddressType hostAddressType = HostAddressType.HOST_IPV4_ADDRESS;
        HostAddressTypes hostAddressTypes = HostAddressTypes.of(hostAddressType);
        Rule rule = new Rule.Builder()
                .addRuleAction(SocksRuleActionSpecConstants.SOCKS_ON_REQUEST_CLIENT_ENDPOINTS_BIND_HOST_ADDRESS_TYPE.newRuleAction(
                        hostAddressType))
                .build();
        Assert.assertEquals(
                hostAddressTypes,
                SocksValueDerivationHelper.getSocksOnUdpAssociateRequestClientFacingDatagramSocketBindHostAddressTypesFrom(rule));
    }

    @Test
    public void testGetSocksOnUdpAssociateRequestClientFacingDatagramSocketBindHostAddressTypesFromRule04() {
        HostAddressType hostAddressType = HostAddressType.HOST_IPV4_ADDRESS;
        HostAddressTypes hostAddressTypes = HostAddressTypes.of(hostAddressType);
        Rule rule = new Rule.Builder()
                .addRuleAction(SocksRuleActionSpecConstants.SOCKS_ON_UDP_ASSOCIATE_REQUEST_CLIENT_FACING_DATAGRAM_SOCKET_BIND_HOST_ADDRESS_TYPE.newRuleAction(
                        hostAddressType))
                .build();
        Assert.assertEquals(
                hostAddressTypes,
                SocksValueDerivationHelper.getSocksOnUdpAssociateRequestClientFacingDatagramSocketBindHostAddressTypesFrom(rule));
    }

    @Test
    public void testGetSocksOnUdpAssociateRequestClientFacingDatagramSocketBindHostAddressTypesFromSettings01() {
        Settings settings = Settings.of();
        Assert.assertEquals(
                HostAddressTypes.of(
                        HostAddressType.HOST_IPV4_ADDRESS,
                        HostAddressType.HOST_IPV6_ADDRESS),
                SocksValueDerivationHelper.getSocksOnUdpAssociateRequestClientFacingDatagramSocketBindHostAddressTypesFrom(settings));
    }

    @Test
    public void testGetSocksOnUdpAssociateRequestClientFacingDatagramSocketBindHostAddressTypesFromSettings02() {
        HostAddressType hostAddressType = HostAddressType.HOST_IPV4_ADDRESS;
        HostAddressTypes hostAddressTypes = HostAddressTypes.of(hostAddressType);
        Settings settings = Settings.of(
                GeneralSettingSpecConstants.BIND_HOST_ADDRESS_TYPES.newSetting(
                        hostAddressTypes));
        Assert.assertEquals(
                hostAddressTypes,
                SocksValueDerivationHelper.getSocksOnUdpAssociateRequestClientFacingDatagramSocketBindHostAddressTypesFrom(settings));
    }

    @Test
    public void testGetSocksOnUdpAssociateRequestClientFacingDatagramSocketBindHostAddressTypesFromSettings03() {
        HostAddressType hostAddressType = HostAddressType.HOST_IPV4_ADDRESS;
        HostAddressTypes hostAddressTypes = HostAddressTypes.of(hostAddressType);
        Settings settings = Settings.of(
                GeneralSettingSpecConstants.CLIENT_ENDPOINTS_BIND_HOST_ADDRESS_TYPES.newSetting(
                        hostAddressTypes));
        Assert.assertEquals(
                hostAddressTypes,
                SocksValueDerivationHelper.getSocksOnUdpAssociateRequestClientFacingDatagramSocketBindHostAddressTypesFrom(settings));
    }

    @Test
    public void testGetSocksOnUdpAssociateRequestClientFacingDatagramSocketBindHostAddressTypesFromSettings04() {
        HostAddressType hostAddressType = HostAddressType.HOST_IPV4_ADDRESS;
        HostAddressTypes hostAddressTypes = HostAddressTypes.of(hostAddressType);
        Settings settings = Settings.of(
                SocksSettingSpecConstants.SOCKS_ON_REQUEST_CLIENT_ENDPOINTS_BIND_HOST_ADDRESS_TYPES.newSetting(
                        hostAddressTypes));
        Assert.assertEquals(
                hostAddressTypes,
                SocksValueDerivationHelper.getSocksOnUdpAssociateRequestClientFacingDatagramSocketBindHostAddressTypesFrom(settings));
    }

    @Test
    public void testGetSocksOnUdpAssociateRequestClientFacingDatagramSocketBindHostAddressTypesFromSettings05() {
        HostAddressType hostAddressType = HostAddressType.HOST_IPV4_ADDRESS;
        HostAddressTypes hostAddressTypes = HostAddressTypes.of(hostAddressType);
        Settings settings = Settings.of(
                SocksSettingSpecConstants.SOCKS_ON_UDP_ASSOCIATE_REQUEST_CLIENT_FACING_DATAGRAM_SOCKET_BIND_HOST_ADDRESS_TYPES.newSetting(
                        hostAddressTypes));
        Assert.assertEquals(
                hostAddressTypes,
                SocksValueDerivationHelper.getSocksOnUdpAssociateRequestClientFacingDatagramSocketBindHostAddressTypesFrom(settings));
    }

    @Test
    public void testGetSocksOnUdpAssociateRequestClientFacingDatagramSocketBindHostFromRule01() {
        Host host = Host.newInstance("127.0.0.1");
        Rule rule = new Rule.Builder()
                .addRuleAction(GeneralRuleActionSpecConstants.BIND_HOST.newRuleAction(
                        host))
                .build();
        Assert.assertEquals(
                host,
                SocksValueDerivationHelper.getSocksOnUdpAssociateRequestClientFacingDatagramSocketBindHostFrom(rule));
    }

    @Test
    public void testGetSocksOnUdpAssociateRequestClientFacingDatagramSocketBindHostFromRule02() {
        Host host = Host.newInstance("127.0.0.1");
        Rule rule = new Rule.Builder()
                .addRuleAction(GeneralRuleActionSpecConstants.CLIENT_ENDPOINTS_BIND_HOST.newRuleAction(
                        host))
                .build();
        Assert.assertEquals(
                host,
                SocksValueDerivationHelper.getSocksOnUdpAssociateRequestClientFacingDatagramSocketBindHostFrom(rule));
    }

    @Test
    public void testGetSocksOnUdpAssociateRequestClientFacingDatagramSocketBindHostFromRule03() {
        Host host = Host.newInstance("127.0.0.1");
        Rule rule = new Rule.Builder()
                .addRuleAction(SocksRuleActionSpecConstants.SOCKS_ON_REQUEST_CLIENT_ENDPOINTS_BIND_HOST.newRuleAction(
                        host))
                .build();
        Assert.assertEquals(
                host,
                SocksValueDerivationHelper.getSocksOnUdpAssociateRequestClientFacingDatagramSocketBindHostFrom(rule));
    }

    @Test
    public void testGetSocksOnUdpAssociateRequestClientFacingDatagramSocketBindHostFromRule04() {
        Host host = Host.newInstance("127.0.0.1");
        Rule rule = new Rule.Builder()
                .addRuleAction(SocksRuleActionSpecConstants.SOCKS_ON_UDP_ASSOCIATE_REQUEST_CLIENT_FACING_DATAGRAM_SOCKET_BIND_HOST.newRuleAction(
                        host))
                .build();
        Assert.assertEquals(
                host,
                SocksValueDerivationHelper.getSocksOnUdpAssociateRequestClientFacingDatagramSocketBindHostFrom(rule));
    }

    @Test
    public void testGetSocksOnUdpAssociateRequestClientFacingDatagramSocketBindHostFromSettings01() {
        Settings settings = Settings.of();
        Assert.assertNull(SocksValueDerivationHelper.getSocksOnUdpAssociateRequestClientFacingDatagramSocketBindHostFrom(settings));
    }

    @Test
    public void testGetSocksOnUdpAssociateRequestClientFacingDatagramSocketBindHostFromSettings02() {
        Host host = Host.newInstance("127.0.0.1");
        Settings settings = Settings.of(
                GeneralSettingSpecConstants.BIND_HOST.newSetting(host));
        Assert.assertEquals(
                host,
                SocksValueDerivationHelper.getSocksOnUdpAssociateRequestClientFacingDatagramSocketBindHostFrom(settings));
    }

    @Test
    public void testGetSocksOnUdpAssociateRequestClientFacingDatagramSocketBindHostFromSettings03() {
        Host host = Host.newInstance("127.0.0.1");
        Settings settings = Settings.of(
                GeneralSettingSpecConstants.CLIENT_ENDPOINTS_BIND_HOST.newSetting(
                        host));
        Assert.assertEquals(
                host,
                SocksValueDerivationHelper.getSocksOnUdpAssociateRequestClientFacingDatagramSocketBindHostFrom(settings));
    }

    @Test
    public void testGetSocksOnUdpAssociateRequestClientFacingDatagramSocketBindHostFromSettings04() {
        Host host = Host.newInstance("127.0.0.1");
        Settings settings = Settings.of(
                SocksSettingSpecConstants.SOCKS_ON_REQUEST_CLIENT_ENDPOINTS_BIND_HOST.newSetting(
                        host));
        Assert.assertEquals(
                host,
                SocksValueDerivationHelper.getSocksOnUdpAssociateRequestClientFacingDatagramSocketBindHostFrom(settings));
    }

    @Test
    public void testGetSocksOnUdpAssociateRequestClientFacingDatagramSocketBindHostFromSettings05() {
        Host host = Host.newInstance("127.0.0.1");
        Settings settings = Settings.of(
                SocksSettingSpecConstants.SOCKS_ON_UDP_ASSOCIATE_REQUEST_CLIENT_FACING_DATAGRAM_SOCKET_BIND_HOST.newSetting(
                        host));
        Assert.assertEquals(
                host,
                SocksValueDerivationHelper.getSocksOnUdpAssociateRequestClientFacingDatagramSocketBindHostFrom(settings));
    }

    @Test
    public void testGetSocksOnUdpAssociateRequestClientFacingDatagramSocketBindPortRangesFromRule01() {
        PortRange portRange = PortRange.of(
                Port.valueOf(1), Port.valueOf(65535));
        PortRanges portRanges = PortRanges.of(portRange);
        Rule rule = new Rule.Builder()
                .addRuleAction(GeneralRuleActionSpecConstants.BIND_UDP_PORT_RANGE.newRuleAction(
                        portRange))
                .build();
        Assert.assertEquals(
                portRanges,
                SocksValueDerivationHelper.getSocksOnUdpAssociateRequestClientFacingDatagramSocketBindPortRangesFrom(rule));
    }

    @Test
    public void testGetSocksOnUdpAssociateRequestClientFacingDatagramSocketBindPortRangesFromRule02() {
        PortRange portRange = PortRange.of(
                Port.valueOf(1), Port.valueOf(65535));
        PortRanges portRanges = PortRanges.of(portRange);
        Rule rule = new Rule.Builder()
                .addRuleAction(GeneralRuleActionSpecConstants.CLIENT_ENDPOINTS_BIND_UDP_PORT_RANGE.newRuleAction(
                        portRange))
                .build();
        Assert.assertEquals(
                portRanges,
                SocksValueDerivationHelper.getSocksOnUdpAssociateRequestClientFacingDatagramSocketBindPortRangesFrom(rule));
    }

    @Test
    public void testGetSocksOnUdpAssociateRequestClientFacingDatagramSocketBindPortRangesFromRule03() {
        PortRange portRange = PortRange.of(
                Port.valueOf(1), Port.valueOf(65535));
        PortRanges portRanges = PortRanges.of(portRange);
        Rule rule = new Rule.Builder()
                .addRuleAction(SocksRuleActionSpecConstants.SOCKS_ON_REQUEST_CLIENT_ENDPOINTS_BIND_UDP_PORT_RANGE.newRuleAction(
                        portRange))
                .build();
        Assert.assertEquals(
                portRanges,
                SocksValueDerivationHelper.getSocksOnUdpAssociateRequestClientFacingDatagramSocketBindPortRangesFrom(rule));
    }

    @Test
    public void testGetSocksOnUdpAssociateRequestClientFacingDatagramSocketBindPortRangesFromRule04() {
        PortRange portRange = PortRange.of(
                Port.valueOf(1), Port.valueOf(65535));
        PortRanges portRanges = PortRanges.of(portRange);
        Rule rule = new Rule.Builder()
                .addRuleAction(SocksRuleActionSpecConstants.SOCKS_ON_UDP_ASSOCIATE_REQUEST_CLIENT_FACING_DATAGRAM_SOCKET_BIND_PORT_RANGE.newRuleAction(
                        portRange))
                .build();
        Assert.assertEquals(
                portRanges,
                SocksValueDerivationHelper.getSocksOnUdpAssociateRequestClientFacingDatagramSocketBindPortRangesFrom(rule));
    }

    @Test
    public void testGetSocksOnUdpAssociateRequestClientFacingDatagramSocketBindPortRangesFromSettings01() {
        PortRanges portRanges = PortRanges.getDefault();
        Settings settings = Settings.of();
        Assert.assertEquals(
                portRanges,
                SocksValueDerivationHelper.getSocksOnUdpAssociateRequestClientFacingDatagramSocketBindPortRangesFrom(settings));
    }

    @Test
    public void testGetSocksOnUdpAssociateRequestClientFacingDatagramSocketBindPortRangesFromSettings02() {
        PortRange portRange = PortRange.of(
                Port.valueOf(1), Port.valueOf(65535));
        PortRanges portRanges = PortRanges.of(portRange);
        Settings settings = Settings.of(
                GeneralSettingSpecConstants.BIND_UDP_PORT_RANGES.newSetting(
                        portRanges));
        Assert.assertEquals(
                portRanges,
                SocksValueDerivationHelper.getSocksOnUdpAssociateRequestClientFacingDatagramSocketBindPortRangesFrom(settings));
    }

    @Test
    public void testGetSocksOnUdpAssociateRequestClientFacingDatagramSocketBindPortRangesFromSettings03() {
        PortRange portRange = PortRange.of(
                Port.valueOf(1), Port.valueOf(65535));
        PortRanges portRanges = PortRanges.of(portRange);
        Settings settings = Settings.of(
                GeneralSettingSpecConstants.CLIENT_ENDPOINTS_BIND_UDP_PORT_RANGES.newSetting(
                        portRanges));
        Assert.assertEquals(
                portRanges,
                SocksValueDerivationHelper.getSocksOnUdpAssociateRequestClientFacingDatagramSocketBindPortRangesFrom(settings));
    }

    @Test
    public void testGetSocksOnUdpAssociateRequestClientFacingDatagramSocketBindPortRangesFromSettings04() {
        PortRange portRange = PortRange.of(
                Port.valueOf(1), Port.valueOf(65535));
        PortRanges portRanges = PortRanges.of(portRange);
        Settings settings = Settings.of(
                SocksSettingSpecConstants.SOCKS_ON_REQUEST_CLIENT_ENDPOINTS_BIND_UDP_PORT_RANGES.newSetting(
                        portRanges));
        Assert.assertEquals(
                portRanges,
                SocksValueDerivationHelper.getSocksOnUdpAssociateRequestClientFacingDatagramSocketBindPortRangesFrom(settings));
    }

    @Test
    public void testGetSocksOnUdpAssociateRequestClientFacingDatagramSocketBindPortRangesFromSettings05() {
        PortRange portRange = PortRange.of(
                Port.valueOf(1), Port.valueOf(65535));
        PortRanges portRanges = PortRanges.of(portRange);
        Settings settings = Settings.of(
                SocksSettingSpecConstants.SOCKS_ON_UDP_ASSOCIATE_REQUEST_CLIENT_FACING_DATAGRAM_SOCKET_BIND_PORT_RANGES.newSetting(
                        portRanges));
        Assert.assertEquals(
                portRanges,
                SocksValueDerivationHelper.getSocksOnUdpAssociateRequestClientFacingDatagramSocketBindPortRangesFrom(settings));
    }

    @Test
    public void testGetSocksOnUdpAssociateRequestClientFacingDatagramSocketNetInterfaceFromRule01() throws UnknownHostException, SocketException {
        Host host = Host.newInstance("127.0.0.1");
        NetworkInterface networkInterface = NetworkInterface.getByInetAddress(
                host.toInetAddress());
        NetInterface netInterface = NetInterface.newInstance(networkInterface);
        Rule rule = new Rule.Builder()
                .addRuleAction(GeneralRuleActionSpecConstants.NET_INTERFACE.newRuleAction(
                        netInterface))
                .build();
        Assert.assertEquals(
                netInterface,
                SocksValueDerivationHelper.getSocksOnUdpAssociateRequestClientFacingDatagramSocketNetInterfaceFrom(rule));
    }

    @Test
    public void testGetSocksOnUdpAssociateRequestClientFacingDatagramSocketNetInterfaceFromRule02() throws UnknownHostException, SocketException {
        Host host = Host.newInstance("127.0.0.1");
        NetworkInterface networkInterface = NetworkInterface.getByInetAddress(
                host.toInetAddress());
        NetInterface netInterface = NetInterface.newInstance(networkInterface);
        Rule rule = new Rule.Builder()
                .addRuleAction(GeneralRuleActionSpecConstants.CLIENT_ENDPOINTS_NET_INTERFACE.newRuleAction(
                        netInterface))
                .build();
        Assert.assertEquals(
                netInterface,
                SocksValueDerivationHelper.getSocksOnUdpAssociateRequestClientFacingDatagramSocketNetInterfaceFrom(rule));
    }

    @Test
    public void testGetSocksOnUdpAssociateRequestClientFacingDatagramSocketNetInterfaceFromRule03() throws UnknownHostException, SocketException {
        Host host = Host.newInstance("127.0.0.1");
        NetworkInterface networkInterface = NetworkInterface.getByInetAddress(
                host.toInetAddress());
        NetInterface netInterface = NetInterface.newInstance(networkInterface);
        Rule rule = new Rule.Builder()
                .addRuleAction(SocksRuleActionSpecConstants.SOCKS_ON_REQUEST_CLIENT_ENDPOINTS_NET_INTERFACE.newRuleAction(
                        netInterface))
                .build();
        Assert.assertEquals(
                netInterface,
                SocksValueDerivationHelper.getSocksOnUdpAssociateRequestClientFacingDatagramSocketNetInterfaceFrom(rule));
    }

    @Test
    public void testGetSocksOnUdpAssociateRequestClientFacingDatagramSocketNetInterfaceFromRule04() throws UnknownHostException, SocketException {
        Host host = Host.newInstance("127.0.0.1");
        NetworkInterface networkInterface = NetworkInterface.getByInetAddress(
                host.toInetAddress());
        NetInterface netInterface = NetInterface.newInstance(networkInterface);
        Rule rule = new Rule.Builder()
                .addRuleAction(SocksRuleActionSpecConstants.SOCKS_ON_UDP_ASSOCIATE_REQUEST_CLIENT_FACING_DATAGRAM_SOCKET_NET_INTERFACE.newRuleAction(
                        netInterface))
                .build();
        Assert.assertEquals(
                netInterface,
                SocksValueDerivationHelper.getSocksOnUdpAssociateRequestClientFacingDatagramSocketNetInterfaceFrom(rule));
    }

    @Test
    public void testGetSocksOnUdpAssociateRequestClientFacingDatagramSocketNetInterfaceFromSettings01() {
        Settings settings = Settings.of();
        Assert.assertNull(SocksValueDerivationHelper.getSocksOnUdpAssociateRequestClientFacingDatagramSocketNetInterfaceFrom(settings));
    }

    @Test
    public void testGetSocksOnUdpAssociateRequestClientFacingDatagramSocketNetInterfaceFromSettings02() throws UnknownHostException, SocketException {
        Host host = Host.newInstance("127.0.0.1");
        NetworkInterface networkInterface = NetworkInterface.getByInetAddress(
                host.toInetAddress());
        NetInterface netInterface = NetInterface.newInstance(networkInterface);
        Settings settings = Settings.of(
                GeneralSettingSpecConstants.NET_INTERFACE.newSetting(
                        netInterface));
        Assert.assertEquals(
                netInterface,
                SocksValueDerivationHelper.getSocksOnUdpAssociateRequestClientFacingDatagramSocketNetInterfaceFrom(settings));
    }

    @Test
    public void testGetSocksOnUdpAssociateRequestClientFacingDatagramSocketNetInterfaceFromSettings03() throws UnknownHostException, SocketException {
        Host host = Host.newInstance("127.0.0.1");
        NetworkInterface networkInterface = NetworkInterface.getByInetAddress(
                host.toInetAddress());
        NetInterface netInterface = NetInterface.newInstance(networkInterface);
        Settings settings = Settings.of(
                GeneralSettingSpecConstants.CLIENT_ENDPOINTS_NET_INTERFACE.newSetting(
                        netInterface));
        Assert.assertEquals(
                netInterface,
                SocksValueDerivationHelper.getSocksOnUdpAssociateRequestClientFacingDatagramSocketNetInterfaceFrom(settings));
    }

    @Test
    public void testGetSocksOnUdpAssociateRequestClientFacingDatagramSocketNetInterfaceFromSettings04() throws UnknownHostException, SocketException {
        Host host = Host.newInstance("127.0.0.1");
        NetworkInterface networkInterface = NetworkInterface.getByInetAddress(
                host.toInetAddress());
        NetInterface netInterface = NetInterface.newInstance(networkInterface);
        Settings settings = Settings.of(
                SocksSettingSpecConstants.SOCKS_ON_REQUEST_CLIENT_ENDPOINTS_NET_INTERFACE.newSetting(
                        netInterface));
        Assert.assertEquals(
                netInterface,
                SocksValueDerivationHelper.getSocksOnUdpAssociateRequestClientFacingDatagramSocketNetInterfaceFrom(settings));
    }

    @Test
    public void testGetSocksOnUdpAssociateRequestClientFacingDatagramSocketNetInterfaceFromSettings05() throws UnknownHostException, SocketException {
        Host host = Host.newInstance("127.0.0.1");
        NetworkInterface networkInterface = NetworkInterface.getByInetAddress(
                host.toInetAddress());
        NetInterface netInterface = NetInterface.newInstance(networkInterface);
        Settings settings = Settings.of(
                SocksSettingSpecConstants.SOCKS_ON_UDP_ASSOCIATE_REQUEST_CLIENT_FACING_DATAGRAM_SOCKET_NET_INTERFACE.newSetting(
                        netInterface));
        Assert.assertEquals(
                netInterface,
                SocksValueDerivationHelper.getSocksOnUdpAssociateRequestClientFacingDatagramSocketNetInterfaceFrom(settings));
    }

    @Test
    public void testGetSocksOnUdpAssociateRequestClientFacingDatagramSocketSocketSettingsFromRule01() {
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
        Assert.assertEquals(
                socketSettings,
                SocksValueDerivationHelper.getSocksOnUdpAssociateRequestClientFacingDatagramSocketSocketSettingsFrom(rule));
    }

    @Test
    public void testGetSocksOnUdpAssociateRequestClientFacingDatagramSocketSocketSettingsFromRule02() {
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
        Assert.assertEquals(
                socketSettings,
                SocksValueDerivationHelper.getSocksOnUdpAssociateRequestClientFacingDatagramSocketSocketSettingsFrom(rule));
    }

    @Test
    public void testGetSocksOnUdpAssociateRequestClientFacingDatagramSocketSocketSettingsFromRule03() {
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
        Assert.assertEquals(
                socketSettings,
                SocksValueDerivationHelper.getSocksOnUdpAssociateRequestClientFacingDatagramSocketSocketSettingsFrom(rule));
    }

    @Test
    public void testGetSocksOnUdpAssociateRequestClientFacingDatagramSocketSocketSettingsFromRule04() {
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
        Assert.assertEquals(
                socketSettings,
                SocksValueDerivationHelper.getSocksOnUdpAssociateRequestClientFacingDatagramSocketSocketSettingsFrom(rule));
    }

    @Test
    public void testGetSocksOnUdpAssociateRequestClientFacingDatagramSocketSocketSettingsFromSettings01() {
        SocketSettings socketSettings = SocketSettings.of();
        Settings settings = Settings.of();
        Assert.assertEquals(
                socketSettings,
                SocksValueDerivationHelper.getSocksOnUdpAssociateRequestClientFacingDatagramSocketSocketSettingsFrom(settings));
    }

    @Test
    public void testGetSocksOnUdpAssociateRequestClientFacingDatagramSocketSocketSettingsFromSettings02() {
        SocketSetting<?> socketSetting =
                StandardSocketSettingSpecConstants.SO_TIMEOUT.newSocketSetting(
                        NonNegativeInteger.valueOf(0));
        SocketSettings socketSettings = SocketSettings.of(socketSetting);
        Settings settings = Settings.of(
                GeneralSettingSpecConstants.SOCKET_SETTINGS.newSetting(
                        socketSettings));
        Assert.assertEquals(
                socketSettings,
                SocksValueDerivationHelper.getSocksOnUdpAssociateRequestClientFacingDatagramSocketSocketSettingsFrom(settings));
    }

    @Test
    public void testGetSocksOnUdpAssociateRequestClientFacingDatagramSocketSocketSettingsFromSettings03() {
        SocketSetting<?> socketSetting =
                StandardSocketSettingSpecConstants.SO_TIMEOUT.newSocketSetting(
                        NonNegativeInteger.valueOf(0));
        SocketSettings socketSettings = SocketSettings.of(socketSetting);
        Settings settings = Settings.of(
                GeneralSettingSpecConstants.CLIENT_ENDPOINTS_SOCKET_SETTINGS.newSetting(
                        socketSettings));
        Assert.assertEquals(
                socketSettings,
                SocksValueDerivationHelper.getSocksOnUdpAssociateRequestClientFacingDatagramSocketSocketSettingsFrom(settings));
    }

    @Test
    public void testGetSocksOnUdpAssociateRequestClientFacingDatagramSocketSocketSettingsFromSettings04() {
        SocketSetting<?> socketSetting =
                StandardSocketSettingSpecConstants.SO_TIMEOUT.newSocketSetting(
                        NonNegativeInteger.valueOf(0));
        SocketSettings socketSettings = SocketSettings.of(socketSetting);
        Settings settings = Settings.of(
                SocksSettingSpecConstants.SOCKS_ON_REQUEST_CLIENT_ENDPOINTS_SOCKET_SETTINGS.newSetting(
                        socketSettings));
        Assert.assertEquals(
                socketSettings,
                SocksValueDerivationHelper.getSocksOnUdpAssociateRequestClientFacingDatagramSocketSocketSettingsFrom(settings));
    }

    @Test
    public void testGetSocksOnUdpAssociateRequestClientFacingDatagramSocketSocketSettingsFromSettings05() {
        SocketSetting<?> socketSetting =
                StandardSocketSettingSpecConstants.SO_TIMEOUT.newSocketSetting(
                        NonNegativeInteger.valueOf(0));
        SocketSettings socketSettings = SocketSettings.of(socketSetting);
        Settings settings = Settings.of(
                SocksSettingSpecConstants.SOCKS_ON_UDP_ASSOCIATE_REQUEST_CLIENT_FACING_DATAGRAM_SOCKET_SOCKET_SETTINGS.newSetting(
                        socketSettings));
        Assert.assertEquals(
                socketSettings,
                SocksValueDerivationHelper.getSocksOnUdpAssociateRequestClientFacingDatagramSocketSocketSettingsFrom(settings));
    }

    @Test
    public void testGetSocksOnUdpAssociateRequestRelayBufferSizeFromRule01() {
        PositiveInteger relayBufferSize = PositiveInteger.valueOf(2048);
        Rule rule = new Rule.Builder()
                .addRuleAction(SocksRuleActionSpecConstants.SOCKS_ON_REQUEST_RELAY_BUFFER_SIZE.newRuleAction(
                        relayBufferSize))
                .build();
        Assert.assertEquals(
                relayBufferSize,
                SocksValueDerivationHelper.getSocksOnUdpAssociateRequestRelayBufferSizeFrom(rule));
    }

    @Test
    public void testGetSocksOnUdpAssociateRequestRelayBufferSizeFromRule02() {
        PositiveInteger relayBufferSize = PositiveInteger.valueOf(2048);
        Rule rule = new Rule.Builder()
                .addRuleAction(SocksRuleActionSpecConstants.SOCKS_ON_UDP_ASSOCIATE_REQUEST_RELAY_BUFFER_SIZE.newRuleAction(
                        relayBufferSize))
                .build();
        Assert.assertEquals(
                relayBufferSize,
                SocksValueDerivationHelper.getSocksOnUdpAssociateRequestRelayBufferSizeFrom(rule));
    }

    @Test
    public void testGetSocksOnUdpAssociateRequestRelayBufferSizeFromSettings01() {
        PositiveInteger relayBufferSize = PositiveInteger.valueOf(1024);
        Settings settings = Settings.of();
        Assert.assertEquals(
                relayBufferSize,
                SocksValueDerivationHelper.getSocksOnUdpAssociateRequestRelayBufferSizeFrom(settings));
    }

    @Test
    public void testGetSocksOnUdpAssociateRequestRelayBufferSizeFromSettings02() {
        PositiveInteger relayBufferSize = PositiveInteger.valueOf(2048);
        Settings settings = Settings.of(
                SocksSettingSpecConstants.SOCKS_ON_REQUEST_RELAY_BUFFER_SIZE.newSetting(
                        relayBufferSize));
        Assert.assertEquals(
                relayBufferSize,
                SocksValueDerivationHelper.getSocksOnUdpAssociateRequestRelayBufferSizeFrom(settings));
    }

    @Test
    public void testGetSocksOnUdpAssociateRequestRelayBufferSizeFromSettings03() {
        PositiveInteger relayBufferSize = PositiveInteger.valueOf(2048);
        Settings settings = Settings.of(
                SocksSettingSpecConstants.SOCKS_ON_UDP_ASSOCIATE_REQUEST_RELAY_BUFFER_SIZE.newSetting(
                        relayBufferSize));
        Assert.assertEquals(
                relayBufferSize,
                SocksValueDerivationHelper.getSocksOnUdpAssociateRequestRelayBufferSizeFrom(settings));
    }

    @Test
    public void testGetSocksOnUdpAssociateRequestRelayIdleTimeoutFromRule01() {
        PositiveInteger relayIdleTimeout = PositiveInteger.valueOf(120000);
        Rule rule = new Rule.Builder()
                .addRuleAction(SocksRuleActionSpecConstants.SOCKS_ON_REQUEST_RELAY_IDLE_TIMEOUT.newRuleAction(
                        relayIdleTimeout))
                .build();
        Assert.assertEquals(
                relayIdleTimeout,
                SocksValueDerivationHelper.getSocksOnUdpAssociateRequestRelayIdleTimeoutFrom(rule));
    }

    @Test
    public void testGetSocksOnUdpAssociateRequestRelayIdleTimeoutFromRule02() {
        PositiveInteger relayIdleTimeout = PositiveInteger.valueOf(120000);
        Rule rule = new Rule.Builder()
                .addRuleAction(SocksRuleActionSpecConstants.SOCKS_ON_UDP_ASSOCIATE_REQUEST_RELAY_IDLE_TIMEOUT.newRuleAction(
                        relayIdleTimeout))
                .build();
        Assert.assertEquals(
                relayIdleTimeout,
                SocksValueDerivationHelper.getSocksOnUdpAssociateRequestRelayIdleTimeoutFrom(rule));
    }

    @Test
    public void testGetSocksOnUdpAssociateRequestRelayIdleTimeoutFromSettings01() {
        PositiveInteger relayIdleTimeout = PositiveInteger.valueOf(60000);
        Settings settings = Settings.of();
        Assert.assertEquals(
                relayIdleTimeout,
                SocksValueDerivationHelper.getSocksOnUdpAssociateRequestRelayIdleTimeoutFrom(settings));
    }

    @Test
    public void testGetSocksOnUdpAssociateRequestRelayIdleTimeoutFromSettings02() {
        PositiveInteger relayIdleTimeout = PositiveInteger.valueOf(120000);
        Settings settings = Settings.of(
                SocksSettingSpecConstants.SOCKS_ON_REQUEST_RELAY_IDLE_TIMEOUT.newSetting(
                        relayIdleTimeout));
        Assert.assertEquals(
                relayIdleTimeout,
                SocksValueDerivationHelper.getSocksOnUdpAssociateRequestRelayIdleTimeoutFrom(settings));
    }

    @Test
    public void testGetSocksOnUdpAssociateRequestRelayIdleTimeoutFromSettings03() {
        PositiveInteger relayIdleTimeout = PositiveInteger.valueOf(120000);
        Settings settings = Settings.of(
                SocksSettingSpecConstants.SOCKS_ON_UDP_ASSOCIATE_REQUEST_RELAY_IDLE_TIMEOUT.newSetting(
                        relayIdleTimeout));
        Assert.assertEquals(
                relayIdleTimeout,
                SocksValueDerivationHelper.getSocksOnUdpAssociateRequestRelayIdleTimeoutFrom(settings));
    }

    @Test
    public void testGetSocksOnUdpAssociateRequestRelayInboundBandwidthLimitFromRule01() {
        PositiveInteger relayInboundBandwidthLimit = PositiveInteger.valueOf(
                Integer.MAX_VALUE);
        Rule rule = new Rule.Builder()
                .addRuleAction(SocksRuleActionSpecConstants.SOCKS_ON_REQUEST_RELAY_INBOUND_BANDWIDTH_LIMIT.newRuleAction(
                        relayInboundBandwidthLimit))
                .build();
        Assert.assertEquals(
                relayInboundBandwidthLimit,
                SocksValueDerivationHelper.getSocksOnUdpAssociateRequestRelayInboundBandwidthLimitFrom(rule));
    }

    @Test
    public void testGetSocksOnUdpAssociateRequestRelayInboundBandwidthLimitFromRule02() {
        PositiveInteger relayInboundBandwidthLimit = PositiveInteger.valueOf(
                Integer.MAX_VALUE);
        Rule rule = new Rule.Builder()
                .addRuleAction(SocksRuleActionSpecConstants.SOCKS_ON_UDP_ASSOCIATE_REQUEST_RELAY_INBOUND_BANDWIDTH_LIMIT.newRuleAction(
                        relayInboundBandwidthLimit))
                .build();
        Assert.assertEquals(
                relayInboundBandwidthLimit,
                SocksValueDerivationHelper.getSocksOnUdpAssociateRequestRelayInboundBandwidthLimitFrom(rule));
    }

    @Test
    public void testGetSocksOnUdpAssociateRequestRelayInboundBandwidthLimitFromSettings01() {
        Settings settings = Settings.of();
        Assert.assertNull(
                SocksValueDerivationHelper.getSocksOnUdpAssociateRequestRelayInboundBandwidthLimitFrom(settings));
    }

    @Test
    public void testGetSocksOnUdpAssociateRequestRelayInboundBandwidthLimitFromSettings02() {
        PositiveInteger relayInboundBandwidthLimit = PositiveInteger.valueOf(
                Integer.MAX_VALUE);
        Settings settings = Settings.of(
                SocksSettingSpecConstants.SOCKS_ON_REQUEST_RELAY_INBOUND_BANDWIDTH_LIMIT.newSetting(
                        relayInboundBandwidthLimit));
        Assert.assertEquals(
                relayInboundBandwidthLimit,
                SocksValueDerivationHelper.getSocksOnUdpAssociateRequestRelayInboundBandwidthLimitFrom(settings));
    }

    @Test
    public void testGetSocksOnUdpAssociateRequestRelayInboundBandwidthLimitFromSettings03() {
        PositiveInteger relayInboundBandwidthLimit = PositiveInteger.valueOf(
                Integer.MAX_VALUE);
        Settings settings = Settings.of(
                SocksSettingSpecConstants.SOCKS_ON_UDP_ASSOCIATE_REQUEST_RELAY_INBOUND_BANDWIDTH_LIMIT.newSetting(
                        relayInboundBandwidthLimit));
        Assert.assertEquals(
                relayInboundBandwidthLimit,
                SocksValueDerivationHelper.getSocksOnUdpAssociateRequestRelayInboundBandwidthLimitFrom(settings));
    }

    @Test
    public void testGetSocksOnUdpAssociateRequestRelayOutboundBandwidthLimitFromRule01() {
        PositiveInteger relayOutboundBandwidthLimit = PositiveInteger.valueOf(
                Integer.MAX_VALUE);
        Rule rule = new Rule.Builder()
                .addRuleAction(SocksRuleActionSpecConstants.SOCKS_ON_REQUEST_RELAY_OUTBOUND_BANDWIDTH_LIMIT.newRuleAction(
                        relayOutboundBandwidthLimit))
                .build();
        Assert.assertEquals(
                relayOutboundBandwidthLimit,
                SocksValueDerivationHelper.getSocksOnUdpAssociateRequestRelayOutboundBandwidthLimitFrom(rule));
    }

    @Test
    public void testGetSocksOnUdpAssociateRequestRelayOutboundBandwidthLimitFromRule02() {
        PositiveInteger relayOutboundBandwidthLimit = PositiveInteger.valueOf(
                Integer.MAX_VALUE);
        Rule rule = new Rule.Builder()
                .addRuleAction(SocksRuleActionSpecConstants.SOCKS_ON_UDP_ASSOCIATE_REQUEST_RELAY_OUTBOUND_BANDWIDTH_LIMIT.newRuleAction(
                        relayOutboundBandwidthLimit))
                .build();
        Assert.assertEquals(
                relayOutboundBandwidthLimit,
                SocksValueDerivationHelper.getSocksOnUdpAssociateRequestRelayOutboundBandwidthLimitFrom(rule));
    }

    @Test
    public void testGetSocksOnUdpAssociateRequestRelayOutboundBandwidthLimitFromSettings01() {
        Settings settings = Settings.of();
        Assert.assertNull(
                SocksValueDerivationHelper.getSocksOnUdpAssociateRequestRelayOutboundBandwidthLimitFrom(settings));
    }

    @Test
    public void testGetSocksOnUdpAssociateRequestRelayOutboundBandwidthLimitFromSettings02() {
        PositiveInteger relayOutboundBandwidthLimit = PositiveInteger.valueOf(
                Integer.MAX_VALUE);
        Settings settings = Settings.of(
                SocksSettingSpecConstants.SOCKS_ON_REQUEST_RELAY_OUTBOUND_BANDWIDTH_LIMIT.newSetting(
                        relayOutboundBandwidthLimit));
        Assert.assertEquals(
                relayOutboundBandwidthLimit,
                SocksValueDerivationHelper.getSocksOnUdpAssociateRequestRelayOutboundBandwidthLimitFrom(settings));
    }

    @Test
    public void testGetSocksOnUdpAssociateRequestRelayOutboundBandwidthLimitFromSettings03() {
        PositiveInteger relayOutboundBandwidthLimit = PositiveInteger.valueOf(
                Integer.MAX_VALUE);
        Settings settings = Settings.of(
                SocksSettingSpecConstants.SOCKS_ON_UDP_ASSOCIATE_REQUEST_RELAY_OUTBOUND_BANDWIDTH_LIMIT.newSetting(
                        relayOutboundBandwidthLimit));
        Assert.assertEquals(
                relayOutboundBandwidthLimit,
                SocksValueDerivationHelper.getSocksOnUdpAssociateRequestRelayOutboundBandwidthLimitFrom(settings));
    }

    @Test
    public void testGetSocksOnUdpAssociateRequestRemoteFacingDatagramSocketBindHostAddressTypesFromRule01() {
        HostAddressType hostAddressType = HostAddressType.HOST_IPV4_ADDRESS;
        HostAddressTypes hostAddressTypes = HostAddressTypes.of(hostAddressType);
        Rule rule = new Rule.Builder()
                .addRuleAction(GeneralRuleActionSpecConstants.BIND_HOST_ADDRESS_TYPE.newRuleAction(
                        hostAddressType))
                .build();
        Assert.assertEquals(
                hostAddressTypes,
                SocksValueDerivationHelper.getSocksOnUdpAssociateRequestRemoteFacingDatagramSocketBindHostAddressTypesFrom(rule));
    }

    @Test
    public void testGetSocksOnUdpAssociateRequestRemoteFacingDatagramSocketBindHostAddressTypesFromRule02() {
        HostAddressType hostAddressType = HostAddressType.HOST_IPV4_ADDRESS;
        HostAddressTypes hostAddressTypes = HostAddressTypes.of(hostAddressType);
        Rule rule = new Rule.Builder()
                .addRuleAction(GeneralRuleActionSpecConstants.REMOTE_ENDPOINTS_BIND_HOST_ADDRESS_TYPE.newRuleAction(
                        hostAddressType))
                .build();
        Assert.assertEquals(
                hostAddressTypes,
                SocksValueDerivationHelper.getSocksOnUdpAssociateRequestRemoteFacingDatagramSocketBindHostAddressTypesFrom(rule));
    }

    @Test
    public void testGetSocksOnUdpAssociateRequestRemoteFacingDatagramSocketBindHostAddressTypesFromRule03() {
        HostAddressType hostAddressType = HostAddressType.HOST_IPV4_ADDRESS;
        HostAddressTypes hostAddressTypes = HostAddressTypes.of(hostAddressType);
        Rule rule = new Rule.Builder()
                .addRuleAction(SocksRuleActionSpecConstants.SOCKS_ON_REQUEST_REMOTE_ENDPOINTS_BIND_HOST_ADDRESS_TYPE.newRuleAction(
                        hostAddressType))
                .build();
        Assert.assertEquals(
                hostAddressTypes,
                SocksValueDerivationHelper.getSocksOnUdpAssociateRequestRemoteFacingDatagramSocketBindHostAddressTypesFrom(rule));
    }

    @Test
    public void testGetSocksOnUdpAssociateRequestRemoteFacingDatagramSocketBindHostAddressTypesFromRule04() {
        HostAddressType hostAddressType = HostAddressType.HOST_IPV4_ADDRESS;
        HostAddressTypes hostAddressTypes = HostAddressTypes.of(hostAddressType);
        Rule rule = new Rule.Builder()
                .addRuleAction(SocksRuleActionSpecConstants.SOCKS_ON_UDP_ASSOCIATE_REQUEST_REMOTE_FACING_DATAGRAM_SOCKET_BIND_HOST_ADDRESS_TYPE.newRuleAction(
                        hostAddressType))
                .build();
        Assert.assertEquals(
                hostAddressTypes,
                SocksValueDerivationHelper.getSocksOnUdpAssociateRequestRemoteFacingDatagramSocketBindHostAddressTypesFrom(rule));
    }

    @Test
    public void testGetSocksOnUdpAssociateRequestRemoteFacingDatagramSocketBindHostAddressTypesFromSettings01() {
        Settings settings = Settings.of();
        Assert.assertEquals(
                HostAddressTypes.of(
                        HostAddressType.HOST_IPV4_ADDRESS,
                        HostAddressType.HOST_IPV6_ADDRESS),
                SocksValueDerivationHelper.getSocksOnUdpAssociateRequestRemoteFacingDatagramSocketBindHostAddressTypesFrom(settings));
    }

    @Test
    public void testGetSocksOnUdpAssociateRequestRemoteFacingDatagramSocketBindHostAddressTypesFromSettings02() {
        HostAddressType hostAddressType = HostAddressType.HOST_IPV4_ADDRESS;
        HostAddressTypes hostAddressTypes = HostAddressTypes.of(hostAddressType);
        Settings settings = Settings.of(
                GeneralSettingSpecConstants.BIND_HOST_ADDRESS_TYPES.newSetting(
                        hostAddressTypes));
        Assert.assertEquals(
                hostAddressTypes,
                SocksValueDerivationHelper.getSocksOnUdpAssociateRequestRemoteFacingDatagramSocketBindHostAddressTypesFrom(settings));
    }

    @Test
    public void testGetSocksOnUdpAssociateRequestRemoteFacingDatagramSocketBindHostAddressTypesFromSettings03() {
        HostAddressType hostAddressType = HostAddressType.HOST_IPV4_ADDRESS;
        HostAddressTypes hostAddressTypes = HostAddressTypes.of(hostAddressType);
        Settings settings = Settings.of(
                GeneralSettingSpecConstants.REMOTE_ENDPOINTS_BIND_HOST_ADDRESS_TYPES.newSetting(
                        hostAddressTypes));
        Assert.assertEquals(
                hostAddressTypes,
                SocksValueDerivationHelper.getSocksOnUdpAssociateRequestRemoteFacingDatagramSocketBindHostAddressTypesFrom(settings));
    }

    @Test
    public void testGetSocksOnUdpAssociateRequestRemoteFacingDatagramSocketBindHostAddressTypesFromSettings04() {
        HostAddressType hostAddressType = HostAddressType.HOST_IPV4_ADDRESS;
        HostAddressTypes hostAddressTypes = HostAddressTypes.of(hostAddressType);
        Settings settings = Settings.of(
                SocksSettingSpecConstants.SOCKS_ON_REQUEST_REMOTE_ENDPOINTS_BIND_HOST_ADDRESS_TYPES.newSetting(
                        hostAddressTypes));
        Assert.assertEquals(
                hostAddressTypes,
                SocksValueDerivationHelper.getSocksOnUdpAssociateRequestRemoteFacingDatagramSocketBindHostAddressTypesFrom(settings));
    }

    @Test
    public void testGetSocksOnUdpAssociateRequestRemoteFacingDatagramSocketBindHostAddressTypesFromSettings05() {
        HostAddressType hostAddressType = HostAddressType.HOST_IPV4_ADDRESS;
        HostAddressTypes hostAddressTypes = HostAddressTypes.of(hostAddressType);
        Settings settings = Settings.of(
                SocksSettingSpecConstants.SOCKS_ON_UDP_ASSOCIATE_REQUEST_REMOTE_FACING_DATAGRAM_SOCKET_BIND_HOST_ADDRESS_TYPES.newSetting(
                        hostAddressTypes));
        Assert.assertEquals(
                hostAddressTypes,
                SocksValueDerivationHelper.getSocksOnUdpAssociateRequestRemoteFacingDatagramSocketBindHostAddressTypesFrom(settings));
    }

    @Test
    public void testGetSocksOnUdpAssociateRequestRemoteFacingDatagramSocketBindHostFromRule01() {
        Host host = Host.newInstance("127.0.0.1");
        Rule rule = new Rule.Builder()
                .addRuleAction(GeneralRuleActionSpecConstants.BIND_HOST.newRuleAction(
                        host))
                .build();
        Assert.assertEquals(
                host,
                SocksValueDerivationHelper.getSocksOnUdpAssociateRequestRemoteFacingDatagramSocketBindHostFrom(rule));
    }

    @Test
    public void testGetSocksOnUdpAssociateRequestRemoteFacingDatagramSocketBindHostFromRule02() {
        Host host = Host.newInstance("127.0.0.1");
        Rule rule = new Rule.Builder()
                .addRuleAction(GeneralRuleActionSpecConstants.REMOTE_ENDPOINTS_BIND_HOST.newRuleAction(
                        host))
                .build();
        Assert.assertEquals(
                host,
                SocksValueDerivationHelper.getSocksOnUdpAssociateRequestRemoteFacingDatagramSocketBindHostFrom(rule));
    }

    @Test
    public void testGetSocksOnUdpAssociateRequestRemoteFacingDatagramSocketBindHostFromRule03() {
        Host host = Host.newInstance("127.0.0.1");
        Rule rule = new Rule.Builder()
                .addRuleAction(SocksRuleActionSpecConstants.SOCKS_ON_REQUEST_REMOTE_ENDPOINTS_BIND_HOST.newRuleAction(
                        host))
                .build();
        Assert.assertEquals(
                host,
                SocksValueDerivationHelper.getSocksOnUdpAssociateRequestRemoteFacingDatagramSocketBindHostFrom(rule));
    }

    @Test
    public void testGetSocksOnUdpAssociateRequestRemoteFacingDatagramSocketBindHostFromRule04() {
        Host host = Host.newInstance("127.0.0.1");
        Rule rule = new Rule.Builder()
                .addRuleAction(SocksRuleActionSpecConstants.SOCKS_ON_UDP_ASSOCIATE_REQUEST_REMOTE_FACING_DATAGRAM_SOCKET_BIND_HOST.newRuleAction(
                        host))
                .build();
        Assert.assertEquals(
                host,
                SocksValueDerivationHelper.getSocksOnUdpAssociateRequestRemoteFacingDatagramSocketBindHostFrom(rule));
    }

    @Test
    public void testGetSocksOnUdpAssociateRequestRemoteFacingDatagramSocketBindHostFromSettings01() {
        Settings settings = Settings.of();
        Assert.assertNull(SocksValueDerivationHelper.getSocksOnUdpAssociateRequestRemoteFacingDatagramSocketBindHostFrom(settings));
    }

    @Test
    public void testGetSocksOnUdpAssociateRequestRemoteFacingDatagramSocketBindHostFromSettings02() {
        Host host = Host.newInstance("127.0.0.1");
        Settings settings = Settings.of(
                GeneralSettingSpecConstants.BIND_HOST.newSetting(host));
        Assert.assertEquals(
                host,
                SocksValueDerivationHelper.getSocksOnUdpAssociateRequestRemoteFacingDatagramSocketBindHostFrom(settings));
    }

    @Test
    public void testGetSocksOnUdpAssociateRequestRemoteFacingDatagramSocketBindHostFromSettings03() {
        Host host = Host.newInstance("127.0.0.1");
        Settings settings = Settings.of(
                GeneralSettingSpecConstants.REMOTE_ENDPOINTS_BIND_HOST.newSetting(
                        host));
        Assert.assertEquals(
                host,
                SocksValueDerivationHelper.getSocksOnUdpAssociateRequestRemoteFacingDatagramSocketBindHostFrom(settings));
    }

    @Test
    public void testGetSocksOnUdpAssociateRequestRemoteFacingDatagramSocketBindHostFromSettings04() {
        Host host = Host.newInstance("127.0.0.1");
        Settings settings = Settings.of(
                SocksSettingSpecConstants.SOCKS_ON_REQUEST_REMOTE_ENDPOINTS_BIND_HOST.newSetting(
                        host));
        Assert.assertEquals(
                host,
                SocksValueDerivationHelper.getSocksOnUdpAssociateRequestRemoteFacingDatagramSocketBindHostFrom(settings));
    }

    @Test
    public void testGetSocksOnUdpAssociateRequestRemoteFacingDatagramSocketBindHostFromSettings05() {
        Host host = Host.newInstance("127.0.0.1");
        Settings settings = Settings.of(
                SocksSettingSpecConstants.SOCKS_ON_UDP_ASSOCIATE_REQUEST_REMOTE_FACING_DATAGRAM_SOCKET_BIND_HOST.newSetting(
                        host));
        Assert.assertEquals(
                host,
                SocksValueDerivationHelper.getSocksOnUdpAssociateRequestRemoteFacingDatagramSocketBindHostFrom(settings));
    }

    @Test
    public void testGetSocksOnUdpAssociateRequestRemoteFacingDatagramSocketBindPortRangesFromRule01() {
        PortRange portRange = PortRange.of(
                Port.valueOf(1), Port.valueOf(65535));
        PortRanges portRanges = PortRanges.of(portRange);
        Rule rule = new Rule.Builder()
                .addRuleAction(GeneralRuleActionSpecConstants.BIND_UDP_PORT_RANGE.newRuleAction(
                        portRange))
                .build();
        Assert.assertEquals(
                portRanges,
                SocksValueDerivationHelper.getSocksOnUdpAssociateRequestRemoteFacingDatagramSocketBindPortRangesFrom(rule));
    }

    @Test
    public void testGetSocksOnUdpAssociateRequestRemoteFacingDatagramSocketBindPortRangesFromRule02() {
        PortRange portRange = PortRange.of(
                Port.valueOf(1), Port.valueOf(65535));
        PortRanges portRanges = PortRanges.of(portRange);
        Rule rule = new Rule.Builder()
                .addRuleAction(GeneralRuleActionSpecConstants.REMOTE_ENDPOINTS_BIND_UDP_PORT_RANGE.newRuleAction(
                        portRange))
                .build();
        Assert.assertEquals(
                portRanges,
                SocksValueDerivationHelper.getSocksOnUdpAssociateRequestRemoteFacingDatagramSocketBindPortRangesFrom(rule));
    }

    @Test
    public void testGetSocksOnUdpAssociateRequestRemoteFacingDatagramSocketBindPortRangesFromRule03() {
        PortRange portRange = PortRange.of(
                Port.valueOf(1), Port.valueOf(65535));
        PortRanges portRanges = PortRanges.of(portRange);
        Rule rule = new Rule.Builder()
                .addRuleAction(SocksRuleActionSpecConstants.SOCKS_ON_REQUEST_REMOTE_ENDPOINTS_BIND_UDP_PORT_RANGE.newRuleAction(
                        portRange))
                .build();
        Assert.assertEquals(
                portRanges,
                SocksValueDerivationHelper.getSocksOnUdpAssociateRequestRemoteFacingDatagramSocketBindPortRangesFrom(rule));
    }

    @Test
    public void testGetSocksOnUdpAssociateRequestRemoteFacingDatagramSocketBindPortRangesFromRule04() {
        PortRange portRange = PortRange.of(
                Port.valueOf(1), Port.valueOf(65535));
        PortRanges portRanges = PortRanges.of(portRange);
        Rule rule = new Rule.Builder()
                .addRuleAction(SocksRuleActionSpecConstants.SOCKS_ON_UDP_ASSOCIATE_REQUEST_REMOTE_FACING_DATAGRAM_SOCKET_BIND_PORT_RANGE.newRuleAction(
                        portRange))
                .build();
        Assert.assertEquals(
                portRanges,
                SocksValueDerivationHelper.getSocksOnUdpAssociateRequestRemoteFacingDatagramSocketBindPortRangesFrom(rule));
    }

    @Test
    public void testGetSocksOnUdpAssociateRequestRemoteFacingDatagramSocketBindPortRangesFromSettings01() {
        PortRanges portRanges = PortRanges.getDefault();
        Settings settings = Settings.of();
        Assert.assertEquals(
                portRanges,
                SocksValueDerivationHelper.getSocksOnUdpAssociateRequestRemoteFacingDatagramSocketBindPortRangesFrom(settings));
    }

    @Test
    public void testGetSocksOnUdpAssociateRequestRemoteFacingDatagramSocketBindPortRangesFromSettings02() {
        PortRange portRange = PortRange.of(
                Port.valueOf(1), Port.valueOf(65535));
        PortRanges portRanges = PortRanges.of(portRange);
        Settings settings = Settings.of(
                GeneralSettingSpecConstants.BIND_UDP_PORT_RANGES.newSetting(
                        portRanges));
        Assert.assertEquals(
                portRanges,
                SocksValueDerivationHelper.getSocksOnUdpAssociateRequestRemoteFacingDatagramSocketBindPortRangesFrom(settings));
    }

    @Test
    public void testGetSocksOnUdpAssociateRequestRemoteFacingDatagramSocketBindPortRangesFromSettings03() {
        PortRange portRange = PortRange.of(
                Port.valueOf(1), Port.valueOf(65535));
        PortRanges portRanges = PortRanges.of(portRange);
        Settings settings = Settings.of(
                GeneralSettingSpecConstants.REMOTE_ENDPOINTS_BIND_UDP_PORT_RANGES.newSetting(
                        portRanges));
        Assert.assertEquals(
                portRanges,
                SocksValueDerivationHelper.getSocksOnUdpAssociateRequestRemoteFacingDatagramSocketBindPortRangesFrom(settings));
    }

    @Test
    public void testGetSocksOnUdpAssociateRequestRemoteFacingDatagramSocketBindPortRangesFromSettings04() {
        PortRange portRange = PortRange.of(
                Port.valueOf(1), Port.valueOf(65535));
        PortRanges portRanges = PortRanges.of(portRange);
        Settings settings = Settings.of(
                SocksSettingSpecConstants.SOCKS_ON_REQUEST_REMOTE_ENDPOINTS_BIND_UDP_PORT_RANGES.newSetting(
                        portRanges));
        Assert.assertEquals(
                portRanges,
                SocksValueDerivationHelper.getSocksOnUdpAssociateRequestRemoteFacingDatagramSocketBindPortRangesFrom(settings));
    }

    @Test
    public void testGetSocksOnUdpAssociateRequestRemoteFacingDatagramSocketBindPortRangesFromSettings05() {
        PortRange portRange = PortRange.of(
                Port.valueOf(1), Port.valueOf(65535));
        PortRanges portRanges = PortRanges.of(portRange);
        Settings settings = Settings.of(
                SocksSettingSpecConstants.SOCKS_ON_UDP_ASSOCIATE_REQUEST_REMOTE_FACING_DATAGRAM_SOCKET_BIND_PORT_RANGES.newSetting(
                        portRanges));
        Assert.assertEquals(
                portRanges,
                SocksValueDerivationHelper.getSocksOnUdpAssociateRequestRemoteFacingDatagramSocketBindPortRangesFrom(settings));
    }

    @Test
    public void testGetSocksOnUdpAssociateRequestRemoteFacingDatagramSocketNetInterfaceFromRule01() throws UnknownHostException, SocketException {
        Host host = Host.newInstance("127.0.0.1");
        NetworkInterface networkInterface = NetworkInterface.getByInetAddress(
                host.toInetAddress());
        NetInterface netInterface = NetInterface.newInstance(networkInterface);
        Rule rule = new Rule.Builder()
                .addRuleAction(GeneralRuleActionSpecConstants.NET_INTERFACE.newRuleAction(
                        netInterface))
                .build();
        Assert.assertEquals(
                netInterface,
                SocksValueDerivationHelper.getSocksOnUdpAssociateRequestRemoteFacingDatagramSocketNetInterfaceFrom(rule));
    }

    @Test
    public void testGetSocksOnUdpAssociateRequestRemoteFacingDatagramSocketNetInterfaceFromRule02() throws UnknownHostException, SocketException {
        Host host = Host.newInstance("127.0.0.1");
        NetworkInterface networkInterface = NetworkInterface.getByInetAddress(
                host.toInetAddress());
        NetInterface netInterface = NetInterface.newInstance(networkInterface);
        Rule rule = new Rule.Builder()
                .addRuleAction(GeneralRuleActionSpecConstants.REMOTE_ENDPOINTS_NET_INTERFACE.newRuleAction(
                        netInterface))
                .build();
        Assert.assertEquals(
                netInterface,
                SocksValueDerivationHelper.getSocksOnUdpAssociateRequestRemoteFacingDatagramSocketNetInterfaceFrom(rule));
    }

    @Test
    public void testGetSocksOnUdpAssociateRequestRemoteFacingDatagramSocketNetInterfaceFromRule03() throws UnknownHostException, SocketException {
        Host host = Host.newInstance("127.0.0.1");
        NetworkInterface networkInterface = NetworkInterface.getByInetAddress(
                host.toInetAddress());
        NetInterface netInterface = NetInterface.newInstance(networkInterface);
        Rule rule = new Rule.Builder()
                .addRuleAction(SocksRuleActionSpecConstants.SOCKS_ON_REQUEST_REMOTE_ENDPOINTS_NET_INTERFACE.newRuleAction(
                        netInterface))
                .build();
        Assert.assertEquals(
                netInterface,
                SocksValueDerivationHelper.getSocksOnUdpAssociateRequestRemoteFacingDatagramSocketNetInterfaceFrom(rule));
    }

    @Test
    public void testGetSocksOnUdpAssociateRequestRemoteFacingDatagramSocketNetInterfaceFromRule04() throws UnknownHostException, SocketException {
        Host host = Host.newInstance("127.0.0.1");
        NetworkInterface networkInterface = NetworkInterface.getByInetAddress(
                host.toInetAddress());
        NetInterface netInterface = NetInterface.newInstance(networkInterface);
        Rule rule = new Rule.Builder()
                .addRuleAction(SocksRuleActionSpecConstants.SOCKS_ON_UDP_ASSOCIATE_REQUEST_REMOTE_FACING_DATAGRAM_SOCKET_NET_INTERFACE.newRuleAction(
                        netInterface))
                .build();
        Assert.assertEquals(
                netInterface,
                SocksValueDerivationHelper.getSocksOnUdpAssociateRequestRemoteFacingDatagramSocketNetInterfaceFrom(rule));
    }

    @Test
    public void testGetSocksOnUdpAssociateRequestRemoteFacingDatagramSocketNetInterfaceFromSettings01() {
        Settings settings = Settings.of();
        Assert.assertNull(SocksValueDerivationHelper.getSocksOnUdpAssociateRequestRemoteFacingDatagramSocketNetInterfaceFrom(settings));
    }

    @Test
    public void testGetSocksOnUdpAssociateRequestRemoteFacingDatagramSocketNetInterfaceFromSettings02() throws UnknownHostException, SocketException {
        Host host = Host.newInstance("127.0.0.1");
        NetworkInterface networkInterface = NetworkInterface.getByInetAddress(
                host.toInetAddress());
        NetInterface netInterface = NetInterface.newInstance(networkInterface);
        Settings settings = Settings.of(
                GeneralSettingSpecConstants.NET_INTERFACE.newSetting(
                        netInterface));
        Assert.assertEquals(
                netInterface,
                SocksValueDerivationHelper.getSocksOnUdpAssociateRequestRemoteFacingDatagramSocketNetInterfaceFrom(settings));
    }

    @Test
    public void testGetSocksOnUdpAssociateRequestRemoteFacingDatagramSocketNetInterfaceFromSettings03() throws UnknownHostException, SocketException {
        Host host = Host.newInstance("127.0.0.1");
        NetworkInterface networkInterface = NetworkInterface.getByInetAddress(
                host.toInetAddress());
        NetInterface netInterface = NetInterface.newInstance(networkInterface);
        Settings settings = Settings.of(
                GeneralSettingSpecConstants.REMOTE_ENDPOINTS_NET_INTERFACE.newSetting(
                        netInterface));
        Assert.assertEquals(
                netInterface,
                SocksValueDerivationHelper.getSocksOnUdpAssociateRequestRemoteFacingDatagramSocketNetInterfaceFrom(settings));
    }

    @Test
    public void testGetSocksOnUdpAssociateRequestRemoteFacingDatagramSocketNetInterfaceFromSettings04() throws UnknownHostException, SocketException {
        Host host = Host.newInstance("127.0.0.1");
        NetworkInterface networkInterface = NetworkInterface.getByInetAddress(
                host.toInetAddress());
        NetInterface netInterface = NetInterface.newInstance(networkInterface);
        Settings settings = Settings.of(
                SocksSettingSpecConstants.SOCKS_ON_REQUEST_REMOTE_ENDPOINTS_NET_INTERFACE.newSetting(
                        netInterface));
        Assert.assertEquals(
                netInterface,
                SocksValueDerivationHelper.getSocksOnUdpAssociateRequestRemoteFacingDatagramSocketNetInterfaceFrom(settings));
    }

    @Test
    public void testGetSocksOnUdpAssociateRequestRemoteFacingDatagramSocketNetInterfaceFromSettings05() throws UnknownHostException, SocketException {
        Host host = Host.newInstance("127.0.0.1");
        NetworkInterface networkInterface = NetworkInterface.getByInetAddress(
                host.toInetAddress());
        NetInterface netInterface = NetInterface.newInstance(networkInterface);
        Settings settings = Settings.of(
                SocksSettingSpecConstants.SOCKS_ON_UDP_ASSOCIATE_REQUEST_REMOTE_FACING_DATAGRAM_SOCKET_NET_INTERFACE.newSetting(
                        netInterface));
        Assert.assertEquals(
                netInterface,
                SocksValueDerivationHelper.getSocksOnUdpAssociateRequestRemoteFacingDatagramSocketNetInterfaceFrom(settings));
    }

    @Test
    public void testGetSocksOnUdpAssociateRequestRemoteFacingDatagramSocketSocketSettingsFromRule01() {
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
        Assert.assertEquals(
                socketSettings,
                SocksValueDerivationHelper.getSocksOnUdpAssociateRequestRemoteFacingDatagramSocketSocketSettingsFrom(rule));
    }

    @Test
    public void testGetSocksOnUdpAssociateRequestRemoteFacingDatagramSocketSocketSettingsFromRule02() {
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
        Assert.assertEquals(
                socketSettings,
                SocksValueDerivationHelper.getSocksOnUdpAssociateRequestRemoteFacingDatagramSocketSocketSettingsFrom(rule));
    }

    @Test
    public void testGetSocksOnUdpAssociateRequestRemoteFacingDatagramSocketSocketSettingsFromRule03() {
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
        Assert.assertEquals(
                socketSettings,
                SocksValueDerivationHelper.getSocksOnUdpAssociateRequestRemoteFacingDatagramSocketSocketSettingsFrom(rule));
    }

    @Test
    public void testGetSocksOnUdpAssociateRequestRemoteFacingDatagramSocketSocketSettingsFromRule04() {
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
        Assert.assertEquals(
                socketSettings,
                SocksValueDerivationHelper.getSocksOnUdpAssociateRequestRemoteFacingDatagramSocketSocketSettingsFrom(rule));
    }

    @Test
    public void testGetSocksOnUdpAssociateRequestRemoteFacingDatagramSocketSocketSettingsFromSettings01() {
        SocketSettings socketSettings = SocketSettings.of();
        Settings settings = Settings.of();
        Assert.assertEquals(
                socketSettings,
                SocksValueDerivationHelper.getSocksOnUdpAssociateRequestRemoteFacingDatagramSocketSocketSettingsFrom(settings));
    }

    @Test
    public void testGetSocksOnUdpAssociateRequestRemoteFacingDatagramSocketSocketSettingsFromSettings02() {
        SocketSetting<?> socketSetting =
                StandardSocketSettingSpecConstants.SO_TIMEOUT.newSocketSetting(
                        NonNegativeInteger.valueOf(0));
        SocketSettings socketSettings = SocketSettings.of(socketSetting);
        Settings settings = Settings.of(
                GeneralSettingSpecConstants.SOCKET_SETTINGS.newSetting(
                        socketSettings));
        Assert.assertEquals(
                socketSettings,
                SocksValueDerivationHelper.getSocksOnUdpAssociateRequestRemoteFacingDatagramSocketSocketSettingsFrom(settings));
    }

    @Test
    public void testGetSocksOnUdpAssociateRequestRemoteFacingDatagramSocketSocketSettingsFromSettings03() {
        SocketSetting<?> socketSetting =
                StandardSocketSettingSpecConstants.SO_TIMEOUT.newSocketSetting(
                        NonNegativeInteger.valueOf(0));
        SocketSettings socketSettings = SocketSettings.of(socketSetting);
        Settings settings = Settings.of(
                GeneralSettingSpecConstants.REMOTE_ENDPOINTS_SOCKET_SETTINGS.newSetting(
                        socketSettings));
        Assert.assertEquals(
                socketSettings,
                SocksValueDerivationHelper.getSocksOnUdpAssociateRequestRemoteFacingDatagramSocketSocketSettingsFrom(settings));
    }

    @Test
    public void testGetSocksOnUdpAssociateRequestRemoteFacingDatagramSocketSocketSettingsFromSettings04() {
        SocketSetting<?> socketSetting =
                StandardSocketSettingSpecConstants.SO_TIMEOUT.newSocketSetting(
                        NonNegativeInteger.valueOf(0));
        SocketSettings socketSettings = SocketSettings.of(socketSetting);
        Settings settings = Settings.of(
                SocksSettingSpecConstants.SOCKS_ON_REQUEST_REMOTE_ENDPOINTS_SOCKET_SETTINGS.newSetting(
                        socketSettings));
        Assert.assertEquals(
                socketSettings,
                SocksValueDerivationHelper.getSocksOnUdpAssociateRequestRemoteFacingDatagramSocketSocketSettingsFrom(settings));
    }

    @Test
    public void testGetSocksOnUdpAssociateRequestRemoteFacingDatagramSocketSocketSettingsFromSettings05() {
        SocketSetting<?> socketSetting =
                StandardSocketSettingSpecConstants.SO_TIMEOUT.newSocketSetting(
                        NonNegativeInteger.valueOf(0));
        SocketSettings socketSettings = SocketSettings.of(socketSetting);
        Settings settings = Settings.of(
                SocksSettingSpecConstants.SOCKS_ON_UDP_ASSOCIATE_REQUEST_REMOTE_FACING_DATAGRAM_SOCKET_SOCKET_SETTINGS.newSetting(
                        socketSettings));
        Assert.assertEquals(
                socketSettings,
                SocksValueDerivationHelper.getSocksOnUdpAssociateRequestRemoteFacingDatagramSocketSocketSettingsFrom(settings));
    }

    @Test
    public void testGetSocksRequestDesiredDestinationAddressRedirectFromRule01() {
        String string = "127.0.0.1";
        Rule rule = new Rule.Builder()
                .addRuleAction(SocksRuleActionSpecConstants.SOCKS_REQUEST_DESIRED_DESTINATION_ADDRESS_REDIRECT.newRuleAction(
                        string))
                .build();
        Assert.assertEquals(
                string,
                SocksValueDerivationHelper.getSocksRequestDesiredDestinationAddressRedirectFrom(rule));
    }

    @Test
    public void testGetSocksRequestDesiredDestinationPortRedirectFromRule01() {
        Port port = Port.valueOf(443);
        Rule rule = new Rule.Builder()
                .addRuleAction(SocksRuleActionSpecConstants.SOCKS_REQUEST_DESIRED_DESTINATION_PORT_REDIRECT.newRuleAction(
                        port))
                .build();
        Assert.assertEquals(
                port,
                SocksValueDerivationHelper.getSocksRequestDesiredDestinationPortRedirectFrom(rule));
    }

    @Test
    public void testGetSocksRequestDesiredDestinationRedirectLogActionFromRule01() {
        LogAction logAction = LogAction.LOG_AS_INFO;
        Rule rule = new Rule.Builder()
                .addRuleAction(SocksRuleActionSpecConstants.SOCKS_REQUEST_DESIRED_DESTINATION_REDIRECT_LOG_ACTION.newRuleAction(
                        logAction))
                .build();
        Assert.assertEquals(
                logAction,
                SocksValueDerivationHelper.getSocksRequestDesiredDestinationRedirectLogActionFrom(rule));
    }

    @Test
    public void testGetSocksUserpassAuthMethodUserRepositoryFromSettings01() {
        Settings settings = Settings.of();
        Assert.assertEquals(
                "StringSourceUserRepository:",
                SocksValueDerivationHelper.getSocksUserpassAuthMethodUserRepositoryFrom(settings));
    }

    @Test
    public void testGetSocksUserpassAuthMethodUserRepositoryFromSettings02() {
        String string = "StringSourceUserRepository:Aladdin:opensesame";
        Settings settings = Settings.of(
                SocksSettingSpecConstants.SOCKS_USERPASSAUTHMETHOD_USER_REPOSITORY.newSetting(
                        string));
        Assert.assertEquals(
                string,
                SocksValueDerivationHelper.getSocksUserpassAuthMethodUserRepositoryFrom(settings));
    }

}
