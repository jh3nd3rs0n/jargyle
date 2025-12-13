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
    public void testGetSocksOnBindRequestInboundSocketSettingsFromRule01() {
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
                SocksValueDerivationHelper.getSocksOnBindRequestInboundSocketSettingsFrom(rule));
    }

    @Test
    public void testGetSocksOnBindRequestInboundSocketSettingsFromRule02() {
        SocketSetting<?> socketSetting =
                StandardSocketSettingSpecConstants.SO_TIMEOUT.newSocketSetting(
                        NonNegativeInteger.valueOf(0));
        @SuppressWarnings("unchecked")
        SocketSetting<Object> socketSttng = (SocketSetting<Object>) socketSetting;
        SocketSettings socketSettings = SocketSettings.of(socketSttng);
        Rule rule = new Rule.Builder()
                .addRuleAction(GeneralRuleActionSpecConstants.EXTERNAL_FACING_SOCKET_SETTING.newRuleAction(
                        socketSttng))
                .build();
        Assert.assertEquals(
                socketSettings,
                SocksValueDerivationHelper.getSocksOnBindRequestInboundSocketSettingsFrom(rule));
    }

    @Test
    public void testGetSocksOnBindRequestInboundSocketSettingsFromRule03() {
        SocketSetting<?> socketSetting =
                StandardSocketSettingSpecConstants.SO_TIMEOUT.newSocketSetting(
                        NonNegativeInteger.valueOf(0));
        @SuppressWarnings("unchecked")
        SocketSetting<Object> socketSttng = (SocketSetting<Object>) socketSetting;
        SocketSettings socketSettings = SocketSettings.of(socketSttng);
        Rule rule = new Rule.Builder()
                .addRuleAction(SocksRuleActionSpecConstants.SOCKS_ON_REQUEST_EXTERNAL_FACING_SOCKET_SETTING.newRuleAction(
                        socketSttng))
                .build();
        Assert.assertEquals(
                socketSettings,
                SocksValueDerivationHelper.getSocksOnBindRequestInboundSocketSettingsFrom(rule));
    }

    @Test
    public void testGetSocksOnBindRequestInboundSocketSettingsFromRule04() {
        SocketSetting<?> socketSetting =
                StandardSocketSettingSpecConstants.SO_TIMEOUT.newSocketSetting(
                        NonNegativeInteger.valueOf(0));
        @SuppressWarnings("unchecked")
        SocketSetting<Object> socketSttng = (SocketSetting<Object>) socketSetting;
        SocketSettings socketSettings = SocketSettings.of(socketSttng);
        Rule rule = new Rule.Builder()
                .addRuleAction(SocksRuleActionSpecConstants.SOCKS_ON_BIND_REQUEST_INBOUND_SOCKET_SETTING.newRuleAction(
                        socketSttng))
                .build();
        Assert.assertEquals(
                socketSettings,
                SocksValueDerivationHelper.getSocksOnBindRequestInboundSocketSettingsFrom(rule));
    }

    @Test
    public void testGetSocksOnBindRequestInboundSocketSettingsFromSettings01() {
        SocketSettings socketSettings = SocketSettings.of();
        Settings settings = Settings.of();
        Assert.assertEquals(
                socketSettings,
                SocksValueDerivationHelper.getSocksOnBindRequestInboundSocketSettingsFrom(settings));
    }

    @Test
    public void testGetSocksOnBindRequestInboundSocketSettingsFromSettings02() {
        SocketSetting<?> socketSetting =
                StandardSocketSettingSpecConstants.SO_TIMEOUT.newSocketSetting(
                        NonNegativeInteger.valueOf(0));
        SocketSettings socketSettings = SocketSettings.of(socketSetting);
        Settings settings = Settings.of(
                GeneralSettingSpecConstants.SOCKET_SETTINGS.newSetting(
                        socketSettings));
        Assert.assertEquals(
                socketSettings,
                SocksValueDerivationHelper.getSocksOnBindRequestInboundSocketSettingsFrom(settings));
    }

    @Test
    public void testGetSocksOnBindRequestInboundSocketSettingsFromSettings03() {
        SocketSetting<?> socketSetting =
                StandardSocketSettingSpecConstants.SO_TIMEOUT.newSocketSetting(
                        NonNegativeInteger.valueOf(0));
        SocketSettings socketSettings = SocketSettings.of(socketSetting);
        Settings settings = Settings.of(
                GeneralSettingSpecConstants.EXTERNAL_FACING_SOCKET_SETTINGS.newSetting(
                        socketSettings));
        Assert.assertEquals(
                socketSettings,
                SocksValueDerivationHelper.getSocksOnBindRequestInboundSocketSettingsFrom(settings));
    }

    @Test
    public void testGetSocksOnBindRequestInboundSocketSettingsFromSettings04() {
        SocketSetting<?> socketSetting =
                StandardSocketSettingSpecConstants.SO_TIMEOUT.newSocketSetting(
                        NonNegativeInteger.valueOf(0));
        SocketSettings socketSettings = SocketSettings.of(socketSetting);
        Settings settings = Settings.of(
                SocksSettingSpecConstants.SOCKS_ON_REQUEST_EXTERNAL_FACING_SOCKET_SETTINGS.newSetting(
                        socketSettings));
        Assert.assertEquals(
                socketSettings,
                SocksValueDerivationHelper.getSocksOnBindRequestInboundSocketSettingsFrom(settings));
    }

    @Test
    public void testGetSocksOnBindRequestInboundSocketSettingsFromSettings05() {
        SocketSetting<?> socketSetting =
                StandardSocketSettingSpecConstants.SO_TIMEOUT.newSocketSetting(
                        NonNegativeInteger.valueOf(0));
        SocketSettings socketSettings = SocketSettings.of(socketSetting);
        Settings settings = Settings.of(
                SocksSettingSpecConstants.SOCKS_ON_BIND_REQUEST_INBOUND_SOCKET_SETTINGS.newSetting(
                        socketSettings));
        Assert.assertEquals(
                socketSettings,
                SocksValueDerivationHelper.getSocksOnBindRequestInboundSocketSettingsFrom(settings));
    }

    @Test
    public void testGetSocksOnBindRequestListenBindHostFromRule01() {
        Host host = Host.newInstance("127.0.0.1");
        Rule rule = new Rule.Builder()
                .addRuleAction(GeneralRuleActionSpecConstants.BIND_HOST.newRuleAction(
                        host))
                .build();
        Assert.assertEquals(
                host,
                SocksValueDerivationHelper.getSocksOnBindRequestListenBindHostFrom(rule));
    }

    @Test
    public void testGetSocksOnBindRequestListenBindHostFromRule02() {
        Host host = Host.newInstance("127.0.0.1");
        Rule rule = new Rule.Builder()
                .addRuleAction(GeneralRuleActionSpecConstants.EXTERNAL_FACING_BIND_HOST.newRuleAction(
                        host))
                .build();
        Assert.assertEquals(
                host,
                SocksValueDerivationHelper.getSocksOnBindRequestListenBindHostFrom(rule));
    }

    @Test
    public void testGetSocksOnBindRequestListenBindHostFromRule03() {
        Host host = Host.newInstance("127.0.0.1");
        Rule rule = new Rule.Builder()
                .addRuleAction(SocksRuleActionSpecConstants.SOCKS_ON_REQUEST_EXTERNAL_FACING_BIND_HOST.newRuleAction(
                        host))
                .build();
        Assert.assertEquals(
                host,
                SocksValueDerivationHelper.getSocksOnBindRequestListenBindHostFrom(rule));
    }

    @Test
    public void testGetSocksOnBindRequestListenBindHostFromRule04() {
        Host host = Host.newInstance("127.0.0.1");
        Rule rule = new Rule.Builder()
                .addRuleAction(SocksRuleActionSpecConstants.SOCKS_ON_BIND_REQUEST_LISTEN_BIND_HOST.newRuleAction(
                        host))
                .build();
        Assert.assertEquals(
                host,
                SocksValueDerivationHelper.getSocksOnBindRequestListenBindHostFrom(rule));
    }

    @Test
    public void testGetSocksOnBindRequestListenBindHostFromSettings01() {
        Settings settings = Settings.of();
        Assert.assertNull(SocksValueDerivationHelper.getSocksOnBindRequestListenBindHostFrom(settings));
    }

    @Test
    public void testGetSocksOnBindRequestListenBindHostFromSettings02() {
        Host host = Host.newInstance("127.0.0.1");
        Settings settings = Settings.of(
                GeneralSettingSpecConstants.BIND_HOST.newSetting(host));
        Assert.assertEquals(
                host,
                SocksValueDerivationHelper.getSocksOnBindRequestListenBindHostFrom(settings));
    }

    @Test
    public void testGetSocksOnBindRequestListenBindHostFromSettings03() {
        Host host = Host.newInstance("127.0.0.1");
        Settings settings = Settings.of(
                GeneralSettingSpecConstants.EXTERNAL_FACING_BIND_HOST.newSetting(
                        host));
        Assert.assertEquals(
                host,
                SocksValueDerivationHelper.getSocksOnBindRequestListenBindHostFrom(settings));
    }

    @Test
    public void testGetSocksOnBindRequestListenBindHostFromSettings04() {
        Host host = Host.newInstance("127.0.0.1");
        Settings settings = Settings.of(
                SocksSettingSpecConstants.SOCKS_ON_REQUEST_EXTERNAL_FACING_BIND_HOST.newSetting(
                        host));
        Assert.assertEquals(
                host,
                SocksValueDerivationHelper.getSocksOnBindRequestListenBindHostFrom(settings));
    }

    @Test
    public void testGetSocksOnBindRequestListenBindHostFromSettings05() {
        Host host = Host.newInstance("127.0.0.1");
        Settings settings = Settings.of(
                SocksSettingSpecConstants.SOCKS_ON_BIND_REQUEST_LISTEN_BIND_HOST.newSetting(
                        host));
        Assert.assertEquals(
                host,
                SocksValueDerivationHelper.getSocksOnBindRequestListenBindHostFrom(settings));
    }

    @Test
    public void testGetSocksOnBindRequestListenBindHostAddressTypesFromRule01() {
        HostAddressType hostAddressType = HostAddressType.HOST_IPV4_ADDRESS;
        HostAddressTypes hostAddressTypes = HostAddressTypes.of(hostAddressType);
        Rule rule = new Rule.Builder()
                .addRuleAction(GeneralRuleActionSpecConstants.BIND_HOST_ADDRESS_TYPE.newRuleAction(
                        hostAddressType))
                .build();
        Assert.assertEquals(
                hostAddressTypes,
                SocksValueDerivationHelper.getSocksOnBindRequestListenBindHostAddressTypesFrom(rule));
    }

    @Test
    public void testGetSocksOnBindRequestListenBindHostAddressTypesFromRule02() {
        HostAddressType hostAddressType = HostAddressType.HOST_IPV4_ADDRESS;
        HostAddressTypes hostAddressTypes = HostAddressTypes.of(hostAddressType);
        Rule rule = new Rule.Builder()
                .addRuleAction(GeneralRuleActionSpecConstants.EXTERNAL_FACING_BIND_HOST_ADDRESS_TYPE.newRuleAction(
                        hostAddressType))
                .build();
        Assert.assertEquals(
                hostAddressTypes,
                SocksValueDerivationHelper.getSocksOnBindRequestListenBindHostAddressTypesFrom(rule));
    }

    @Test
    public void testGetSocksOnBindRequestListenBindHostAddressTypesFromRule03() {
        HostAddressType hostAddressType = HostAddressType.HOST_IPV4_ADDRESS;
        HostAddressTypes hostAddressTypes = HostAddressTypes.of(hostAddressType);
        Rule rule = new Rule.Builder()
                .addRuleAction(SocksRuleActionSpecConstants.SOCKS_ON_REQUEST_EXTERNAL_FACING_BIND_HOST_ADDRESS_TYPE.newRuleAction(
                        hostAddressType))
                .build();
        Assert.assertEquals(
                hostAddressTypes,
                SocksValueDerivationHelper.getSocksOnBindRequestListenBindHostAddressTypesFrom(rule));
    }

    @Test
    public void testGetSocksOnBindRequestListenBindHostAddressTypesFromRule04() {
        HostAddressType hostAddressType = HostAddressType.HOST_IPV4_ADDRESS;
        HostAddressTypes hostAddressTypes = HostAddressTypes.of(hostAddressType);
        Rule rule = new Rule.Builder()
                .addRuleAction(SocksRuleActionSpecConstants.SOCKS_ON_BIND_REQUEST_LISTEN_BIND_HOST_ADDRESS_TYPE.newRuleAction(
                        hostAddressType))
                .build();
        Assert.assertEquals(
                hostAddressTypes,
                SocksValueDerivationHelper.getSocksOnBindRequestListenBindHostAddressTypesFrom(rule));
    }

    @Test
    public void testGetSocksOnBindRequestListenBindHostAddressTypesFromSettings01() {
        Settings settings = Settings.of();
        Assert.assertEquals(
                HostAddressTypes.of(
                        HostAddressType.HOST_IPV4_ADDRESS,
                        HostAddressType.HOST_IPV6_ADDRESS),
                SocksValueDerivationHelper.getSocksOnBindRequestListenBindHostAddressTypesFrom(settings));
    }

    @Test
    public void testGetSocksOnBindRequestListenBindHostAddressTypesFromSettings02() {
        HostAddressType hostAddressType = HostAddressType.HOST_IPV4_ADDRESS;
        HostAddressTypes hostAddressTypes = HostAddressTypes.of(hostAddressType);
        Settings settings = Settings.of(
                GeneralSettingSpecConstants.BIND_HOST_ADDRESS_TYPES.newSetting(
                        hostAddressTypes));
        Assert.assertEquals(
                hostAddressTypes,
                SocksValueDerivationHelper.getSocksOnBindRequestListenBindHostAddressTypesFrom(settings));
    }

    @Test
    public void testGetSocksOnBindRequestListenBindHostAddressTypesFromSettings03() {
        HostAddressType hostAddressType = HostAddressType.HOST_IPV4_ADDRESS;
        HostAddressTypes hostAddressTypes = HostAddressTypes.of(hostAddressType);
        Settings settings = Settings.of(
                GeneralSettingSpecConstants.EXTERNAL_FACING_BIND_HOST_ADDRESS_TYPES.newSetting(
                        hostAddressTypes));
        Assert.assertEquals(
                hostAddressTypes,
                SocksValueDerivationHelper.getSocksOnBindRequestListenBindHostAddressTypesFrom(settings));
    }

    @Test
    public void testGetSocksOnBindRequestListenBindHostAddressTypesFromSettings04() {
        HostAddressType hostAddressType = HostAddressType.HOST_IPV4_ADDRESS;
        HostAddressTypes hostAddressTypes = HostAddressTypes.of(hostAddressType);
        Settings settings = Settings.of(
                SocksSettingSpecConstants.SOCKS_ON_REQUEST_EXTERNAL_FACING_BIND_HOST_ADDRESS_TYPES.newSetting(
                        hostAddressTypes));
        Assert.assertEquals(
                hostAddressTypes,
                SocksValueDerivationHelper.getSocksOnBindRequestListenBindHostAddressTypesFrom(settings));
    }

    @Test
    public void testGetSocksOnBindRequestListenBindHostAddressTypesFromSettings05() {
        HostAddressType hostAddressType = HostAddressType.HOST_IPV4_ADDRESS;
        HostAddressTypes hostAddressTypes = HostAddressTypes.of(hostAddressType);
        Settings settings = Settings.of(
                SocksSettingSpecConstants.SOCKS_ON_BIND_REQUEST_LISTEN_BIND_HOST_ADDRESS_TYPES.newSetting(
                        hostAddressTypes));
        Assert.assertEquals(
                hostAddressTypes,
                SocksValueDerivationHelper.getSocksOnBindRequestListenBindHostAddressTypesFrom(settings));
    }

    @Test
    public void testGetSocksOnBindRequestListenBindPortRangesFromRule01() {
        PortRange portRange = PortRange.of(
                Port.valueOf(1), Port.valueOf(65535));
        PortRanges portRanges = PortRanges.of(portRange);
        Rule rule = new Rule.Builder()
                .addRuleAction(GeneralRuleActionSpecConstants.BIND_TCP_PORT_RANGE.newRuleAction(
                        portRange))
                .build();
        Assert.assertEquals(
                portRanges,
                SocksValueDerivationHelper.getSocksOnBindRequestListenBindPortRangesFrom(rule));
    }

    @Test
    public void testGetSocksOnBindRequestListenBindPortRangesFromRule02() {
        PortRange portRange = PortRange.of(
                Port.valueOf(1), Port.valueOf(65535));
        PortRanges portRanges = PortRanges.of(portRange);
        Rule rule = new Rule.Builder()
                .addRuleAction(GeneralRuleActionSpecConstants.EXTERNAL_FACING_BIND_TCP_PORT_RANGE.newRuleAction(
                        portRange))
                .build();
        Assert.assertEquals(
                portRanges,
                SocksValueDerivationHelper.getSocksOnBindRequestListenBindPortRangesFrom(rule));
    }

    @Test
    public void testGetSocksOnBindRequestListenBindPortRangesFromRule03() {
        PortRange portRange = PortRange.of(
                Port.valueOf(1), Port.valueOf(65535));
        PortRanges portRanges = PortRanges.of(portRange);
        Rule rule = new Rule.Builder()
                .addRuleAction(SocksRuleActionSpecConstants.SOCKS_ON_REQUEST_EXTERNAL_FACING_BIND_TCP_PORT_RANGE.newRuleAction(
                        portRange))
                .build();
        Assert.assertEquals(
                portRanges,
                SocksValueDerivationHelper.getSocksOnBindRequestListenBindPortRangesFrom(rule));
    }

    @Test
    public void testGetSocksOnBindRequestListenBindPortRangesFromRule04() {
        PortRange portRange = PortRange.of(
                Port.valueOf(1), Port.valueOf(65535));
        PortRanges portRanges = PortRanges.of(portRange);
        Rule rule = new Rule.Builder()
                .addRuleAction(SocksRuleActionSpecConstants.SOCKS_ON_BIND_REQUEST_LISTEN_BIND_PORT_RANGE.newRuleAction(
                        portRange))
                .build();
        Assert.assertEquals(
                portRanges,
                SocksValueDerivationHelper.getSocksOnBindRequestListenBindPortRangesFrom(rule));
    }

    @Test
    public void testGetSocksOnBindRequestListenBindPortRangesFromSettings01() {
        PortRanges portRanges = PortRanges.getDefault();
        Settings settings = Settings.of();
        Assert.assertEquals(
                portRanges,
                SocksValueDerivationHelper.getSocksOnBindRequestListenBindPortRangesFrom(settings));
    }

    @Test
    public void testGetSocksOnBindRequestListenBindPortRangesFromSettings02() {
        PortRange portRange = PortRange.of(
                Port.valueOf(1), Port.valueOf(65535));
        PortRanges portRanges = PortRanges.of(portRange);
        Settings settings = Settings.of(
                GeneralSettingSpecConstants.BIND_TCP_PORT_RANGES.newSetting(
                        portRanges));
        Assert.assertEquals(
                portRanges,
                SocksValueDerivationHelper.getSocksOnBindRequestListenBindPortRangesFrom(settings));
    }

    @Test
    public void testGetSocksOnBindRequestListenBindPortRangesFromSettings03() {
        PortRange portRange = PortRange.of(
                Port.valueOf(1), Port.valueOf(65535));
        PortRanges portRanges = PortRanges.of(portRange);
        Settings settings = Settings.of(
                GeneralSettingSpecConstants.EXTERNAL_FACING_BIND_TCP_PORT_RANGES.newSetting(
                        portRanges));
        Assert.assertEquals(
                portRanges,
                SocksValueDerivationHelper.getSocksOnBindRequestListenBindPortRangesFrom(settings));
    }

    @Test
    public void testGetSocksOnBindRequestListenBindPortRangesFromSettings04() {
        PortRange portRange = PortRange.of(
                Port.valueOf(1), Port.valueOf(65535));
        PortRanges portRanges = PortRanges.of(portRange);
        Settings settings = Settings.of(
                SocksSettingSpecConstants.SOCKS_ON_REQUEST_EXTERNAL_FACING_BIND_TCP_PORT_RANGES.newSetting(
                        portRanges));
        Assert.assertEquals(
                portRanges,
                SocksValueDerivationHelper.getSocksOnBindRequestListenBindPortRangesFrom(settings));
    }

    @Test
    public void testGetSocksOnBindRequestListenBindPortRangesFromSettings05() {
        PortRange portRange = PortRange.of(
                Port.valueOf(1), Port.valueOf(65535));
        PortRanges portRanges = PortRanges.of(portRange);
        Settings settings = Settings.of(
                SocksSettingSpecConstants.SOCKS_ON_BIND_REQUEST_LISTEN_BIND_PORT_RANGES.newSetting(
                        portRanges));
        Assert.assertEquals(
                portRanges,
                SocksValueDerivationHelper.getSocksOnBindRequestListenBindPortRangesFrom(settings));
    }

    @Test
    public void testGetSocksOnBindRequestListenNetInterfaceFromRule01() throws UnknownHostException, SocketException {
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
                SocksValueDerivationHelper.getSocksOnBindRequestListenNetInterfaceFrom(rule));
    }

    @Test
    public void testGetSocksOnBindRequestListenNetInterfaceFromRule02() throws UnknownHostException, SocketException {
        Host host = Host.newInstance("127.0.0.1");
        NetworkInterface networkInterface = NetworkInterface.getByInetAddress(
                host.toInetAddress());
        NetInterface netInterface = NetInterface.newInstance(networkInterface);
        Rule rule = new Rule.Builder()
                .addRuleAction(GeneralRuleActionSpecConstants.EXTERNAL_FACING_NET_INTERFACE.newRuleAction(
                        netInterface))
                .build();
        Assert.assertEquals(
                netInterface,
                SocksValueDerivationHelper.getSocksOnBindRequestListenNetInterfaceFrom(rule));
    }

    @Test
    public void testGetSocksOnBindRequestListenNetInterfaceFromRule03() throws UnknownHostException, SocketException {
        Host host = Host.newInstance("127.0.0.1");
        NetworkInterface networkInterface = NetworkInterface.getByInetAddress(
                host.toInetAddress());
        NetInterface netInterface = NetInterface.newInstance(networkInterface);
        Rule rule = new Rule.Builder()
                .addRuleAction(SocksRuleActionSpecConstants.SOCKS_ON_REQUEST_EXTERNAL_FACING_NET_INTERFACE.newRuleAction(
                        netInterface))
                .build();
        Assert.assertEquals(
                netInterface,
                SocksValueDerivationHelper.getSocksOnBindRequestListenNetInterfaceFrom(rule));
    }

    @Test
    public void testGetSocksOnBindRequestListenNetInterfaceFromRule04() throws UnknownHostException, SocketException {
        Host host = Host.newInstance("127.0.0.1");
        NetworkInterface networkInterface = NetworkInterface.getByInetAddress(
                host.toInetAddress());
        NetInterface netInterface = NetInterface.newInstance(networkInterface);
        Rule rule = new Rule.Builder()
                .addRuleAction(SocksRuleActionSpecConstants.SOCKS_ON_BIND_REQUEST_LISTEN_NET_INTERFACE.newRuleAction(
                        netInterface))
                .build();
        Assert.assertEquals(
                netInterface,
                SocksValueDerivationHelper.getSocksOnBindRequestListenNetInterfaceFrom(rule));
    }

    @Test
    public void testGetSocksOnBindRequestListenNetInterfaceFromSettings01() {
        Settings settings = Settings.of();
        Assert.assertNull(SocksValueDerivationHelper.getSocksOnBindRequestListenNetInterfaceFrom(settings));
    }

    @Test
    public void testGetSocksOnBindRequestListenNetInterfaceFromSettings02() throws UnknownHostException, SocketException {
        Host host = Host.newInstance("127.0.0.1");
        NetworkInterface networkInterface = NetworkInterface.getByInetAddress(
                host.toInetAddress());
        NetInterface netInterface = NetInterface.newInstance(networkInterface);        
        Settings settings = Settings.of(
                GeneralSettingSpecConstants.NET_INTERFACE.newSetting(
                        netInterface));
        Assert.assertEquals(
                netInterface, 
                SocksValueDerivationHelper.getSocksOnBindRequestListenNetInterfaceFrom(settings));
    }

    @Test
    public void testGetSocksOnBindRequestListenNetInterfaceFromSettings03() throws UnknownHostException, SocketException {
        Host host = Host.newInstance("127.0.0.1");
        NetworkInterface networkInterface = NetworkInterface.getByInetAddress(
                host.toInetAddress());
        NetInterface netInterface = NetInterface.newInstance(networkInterface);
        Settings settings = Settings.of(
                GeneralSettingSpecConstants.EXTERNAL_FACING_NET_INTERFACE.newSetting(
                        netInterface));
        Assert.assertEquals(
                netInterface,
                SocksValueDerivationHelper.getSocksOnBindRequestListenNetInterfaceFrom(settings));
    }

    @Test
    public void testGetSocksOnBindRequestListenNetInterfaceFromSettings04() throws UnknownHostException, SocketException {
        Host host = Host.newInstance("127.0.0.1");
        NetworkInterface networkInterface = NetworkInterface.getByInetAddress(
                host.toInetAddress());
        NetInterface netInterface = NetInterface.newInstance(networkInterface);
        Settings settings = Settings.of(
                SocksSettingSpecConstants.SOCKS_ON_REQUEST_EXTERNAL_FACING_NET_INTERFACE.newSetting(
                        netInterface));
        Assert.assertEquals(
                netInterface,
                SocksValueDerivationHelper.getSocksOnBindRequestListenNetInterfaceFrom(settings));
    }

    @Test
    public void testGetSocksOnBindRequestListenNetInterfaceFromSettings05() throws UnknownHostException, SocketException {
        Host host = Host.newInstance("127.0.0.1");
        NetworkInterface networkInterface = NetworkInterface.getByInetAddress(
                host.toInetAddress());
        NetInterface netInterface = NetInterface.newInstance(networkInterface);
        Settings settings = Settings.of(
                SocksSettingSpecConstants.SOCKS_ON_BIND_REQUEST_LISTEN_NET_INTERFACE.newSetting(
                        netInterface));
        Assert.assertEquals(
                netInterface,
                SocksValueDerivationHelper.getSocksOnBindRequestListenNetInterfaceFrom(settings));
    }

    @Test
    public void testGetSocksOnBindRequestListenSocketSettingsFromRule01() {
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
                SocksValueDerivationHelper.getSocksOnBindRequestListenSocketSettingsFrom(rule));
    }

    @Test
    public void testGetSocksOnBindRequestListenSocketSettingsFromRule02() {
        SocketSetting<?> socketSetting =
                StandardSocketSettingSpecConstants.SO_TIMEOUT.newSocketSetting(
                        NonNegativeInteger.valueOf(0));
        @SuppressWarnings("unchecked")
        SocketSetting<Object> socketSttng = (SocketSetting<Object>) socketSetting;
        SocketSettings socketSettings = SocketSettings.of(socketSttng);
        Rule rule = new Rule.Builder()
                .addRuleAction(GeneralRuleActionSpecConstants.EXTERNAL_FACING_SOCKET_SETTING.newRuleAction(
                        socketSttng))
                .build();
        Assert.assertEquals(
                socketSettings,
                SocksValueDerivationHelper.getSocksOnBindRequestListenSocketSettingsFrom(rule));
    }

    @Test
    public void testGetSocksOnBindRequestListenSocketSettingsFromRule03() {
        SocketSetting<?> socketSetting =
                StandardSocketSettingSpecConstants.SO_TIMEOUT.newSocketSetting(
                        NonNegativeInteger.valueOf(0));
        @SuppressWarnings("unchecked")
        SocketSetting<Object> socketSttng = (SocketSetting<Object>) socketSetting;
        SocketSettings socketSettings = SocketSettings.of(socketSttng);
        Rule rule = new Rule.Builder()
                .addRuleAction(SocksRuleActionSpecConstants.SOCKS_ON_REQUEST_EXTERNAL_FACING_SOCKET_SETTING.newRuleAction(
                        socketSttng))
                .build();
        Assert.assertEquals(
                socketSettings,
                SocksValueDerivationHelper.getSocksOnBindRequestListenSocketSettingsFrom(rule));
    }

    @Test
    public void testGetSocksOnBindRequestListenSocketSettingsFromRule04() {
        SocketSetting<?> socketSetting =
                StandardSocketSettingSpecConstants.SO_TIMEOUT.newSocketSetting(
                        NonNegativeInteger.valueOf(0));
        @SuppressWarnings("unchecked")
        SocketSetting<Object> socketSttng = (SocketSetting<Object>) socketSetting;
        SocketSettings socketSettings = SocketSettings.of(socketSttng);
        Rule rule = new Rule.Builder()
                .addRuleAction(SocksRuleActionSpecConstants.SOCKS_ON_BIND_REQUEST_LISTEN_SOCKET_SETTING.newRuleAction(
                        socketSttng))
                .build();
        Assert.assertEquals(
                socketSettings,
                SocksValueDerivationHelper.getSocksOnBindRequestListenSocketSettingsFrom(rule));
    }

    @Test
    public void testGetSocksOnBindRequestListenSocketSettingsFromSettings01() {
        SocketSettings socketSettings = SocketSettings.of();
        Settings settings = Settings.of();
        Assert.assertEquals(
                socketSettings,
                SocksValueDerivationHelper.getSocksOnBindRequestListenSocketSettingsFrom(settings));
    }

    @Test
    public void testGetSocksOnBindRequestListenSocketSettingsFromSettings02() {
        SocketSetting<?> socketSetting =
                StandardSocketSettingSpecConstants.SO_TIMEOUT.newSocketSetting(
                        NonNegativeInteger.valueOf(0));
        SocketSettings socketSettings = SocketSettings.of(socketSetting);
        Settings settings = Settings.of(
                GeneralSettingSpecConstants.SOCKET_SETTINGS.newSetting(
                        socketSettings));
        Assert.assertEquals(
                socketSettings,
                SocksValueDerivationHelper.getSocksOnBindRequestListenSocketSettingsFrom(settings));
    }

    @Test
    public void testGetSocksOnBindRequestListenSocketSettingsFromSettings03() {
        SocketSetting<?> socketSetting =
                StandardSocketSettingSpecConstants.SO_TIMEOUT.newSocketSetting(
                        NonNegativeInteger.valueOf(0));
        SocketSettings socketSettings = SocketSettings.of(socketSetting);
        Settings settings = Settings.of(
                GeneralSettingSpecConstants.EXTERNAL_FACING_SOCKET_SETTINGS.newSetting(
                        socketSettings));
        Assert.assertEquals(
                socketSettings,
                SocksValueDerivationHelper.getSocksOnBindRequestListenSocketSettingsFrom(settings));
    }

    @Test
    public void testGetSocksOnBindRequestListenSocketSettingsFromSettings04() {
        SocketSetting<?> socketSetting =
                StandardSocketSettingSpecConstants.SO_TIMEOUT.newSocketSetting(
                        NonNegativeInteger.valueOf(0));
        SocketSettings socketSettings = SocketSettings.of(socketSetting);
        Settings settings = Settings.of(
                SocksSettingSpecConstants.SOCKS_ON_REQUEST_EXTERNAL_FACING_SOCKET_SETTINGS.newSetting(
                        socketSettings));
        Assert.assertEquals(
                socketSettings,
                SocksValueDerivationHelper.getSocksOnBindRequestListenSocketSettingsFrom(settings));
    }

    @Test
    public void testGetSocksOnBindRequestListenSocketSettingsFromSettings05() {
        SocketSetting<?> socketSetting =
                StandardSocketSettingSpecConstants.SO_TIMEOUT.newSocketSetting(
                        NonNegativeInteger.valueOf(0));
        SocketSettings socketSettings = SocketSettings.of(socketSetting);
        Settings settings = Settings.of(
                SocksSettingSpecConstants.SOCKS_ON_BIND_REQUEST_LISTEN_SOCKET_SETTINGS.newSetting(
                        socketSettings));
        Assert.assertEquals(
                socketSettings,
                SocksValueDerivationHelper.getSocksOnBindRequestListenSocketSettingsFrom(settings));
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
    public void testGetSocksOnConnectRequestPrepareTargetFacingSocketFromRule01() {
        Rule rule = new Rule.Builder()
                .addRuleAction(SocksRuleActionSpecConstants.SOCKS_ON_CONNECT_REQUEST_PREPARE_TARGET_FACING_SOCKET.newRuleAction(
                        Boolean.TRUE))
                .build();
        Assert.assertTrue(
                SocksValueDerivationHelper.getSocksOnConnectRequestPrepareTargetFacingSocketFrom(rule));
    }
    
    @Test
    public void testGetSocksOnConnectRequestPrepareTargetFacingSocketFromSettings01() {
        Settings settings = Settings.of();
        Assert.assertFalse(SocksValueDerivationHelper.getSocksOnConnectRequestPrepareTargetFacingSocketFrom(settings));
    }

    @Test
    public void testGetSocksOnConnectRequestPrepareTargetFacingSocketFromSettings02() {
        Settings settings = Settings.of(
                SocksSettingSpecConstants.SOCKS_ON_CONNECT_REQUEST_PREPARE_TARGET_FACING_SOCKET.newSetting(
                        Boolean.TRUE));
        Assert.assertTrue(SocksValueDerivationHelper.getSocksOnConnectRequestPrepareTargetFacingSocketFrom(settings));
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
    public void testGetSocksOnConnectRequestTargetFacingBindHostFromRule01() {
        Host host = Host.newInstance("127.0.0.1");
        Rule rule = new Rule.Builder()
                .addRuleAction(GeneralRuleActionSpecConstants.BIND_HOST.newRuleAction(
                        host))
                .build();
        Assert.assertEquals(
                host,
                SocksValueDerivationHelper.getSocksOnConnectRequestTargetFacingBindHostFrom(rule));
    }

    @Test
    public void testGetSocksOnConnectRequestTargetFacingBindHostFromRule02() {
        Host host = Host.newInstance("127.0.0.1");
        Rule rule = new Rule.Builder()
                .addRuleAction(GeneralRuleActionSpecConstants.EXTERNAL_FACING_BIND_HOST.newRuleAction(
                        host))
                .build();
        Assert.assertEquals(
                host,
                SocksValueDerivationHelper.getSocksOnConnectRequestTargetFacingBindHostFrom(rule));
    }

    @Test
    public void testGetSocksOnConnectRequestTargetFacingBindHostFromRule03() {
        Host host = Host.newInstance("127.0.0.1");
        Rule rule = new Rule.Builder()
                .addRuleAction(SocksRuleActionSpecConstants.SOCKS_ON_REQUEST_EXTERNAL_FACING_BIND_HOST.newRuleAction(
                        host))
                .build();
        Assert.assertEquals(
                host,
                SocksValueDerivationHelper.getSocksOnConnectRequestTargetFacingBindHostFrom(rule));
    }

    @Test
    public void testGetSocksOnConnectRequestTargetFacingBindHostFromRule04() {
        Host host = Host.newInstance("127.0.0.1");
        Rule rule = new Rule.Builder()
                .addRuleAction(SocksRuleActionSpecConstants.SOCKS_ON_CONNECT_REQUEST_TARGET_FACING_BIND_HOST.newRuleAction(
                        host))
                .build();
        Assert.assertEquals(
                host,
                SocksValueDerivationHelper.getSocksOnConnectRequestTargetFacingBindHostFrom(rule));
    }

    @Test
    public void testGetSocksOnConnectRequestTargetFacingBindHostFromSettings01() {
        Settings settings = Settings.of();
        Assert.assertNull(SocksValueDerivationHelper.getSocksOnConnectRequestTargetFacingBindHostFrom(settings));
    }

    @Test
    public void testGetSocksOnConnectRequestTargetFacingBindHostFromSettings02() {
        Host host = Host.newInstance("127.0.0.1");
        Settings settings = Settings.of(
                GeneralSettingSpecConstants.BIND_HOST.newSetting(host));
        Assert.assertEquals(
                host,
                SocksValueDerivationHelper.getSocksOnConnectRequestTargetFacingBindHostFrom(settings));
    }

    @Test
    public void testGetSocksOnConnectRequestTargetFacingBindHostFromSettings03() {
        Host host = Host.newInstance("127.0.0.1");
        Settings settings = Settings.of(
                GeneralSettingSpecConstants.EXTERNAL_FACING_BIND_HOST.newSetting(
                        host));
        Assert.assertEquals(
                host,
                SocksValueDerivationHelper.getSocksOnConnectRequestTargetFacingBindHostFrom(settings));
    }

    @Test
    public void testGetSocksOnConnectRequestTargetFacingBindHostFromSettings04() {
        Host host = Host.newInstance("127.0.0.1");
        Settings settings = Settings.of(
                SocksSettingSpecConstants.SOCKS_ON_REQUEST_EXTERNAL_FACING_BIND_HOST.newSetting(
                        host));
        Assert.assertEquals(
                host,
                SocksValueDerivationHelper.getSocksOnConnectRequestTargetFacingBindHostFrom(settings));
    }

    @Test
    public void testGetSocksOnConnectRequestTargetFacingBindHostFromSettings05() {
        Host host = Host.newInstance("127.0.0.1");
        Settings settings = Settings.of(
                SocksSettingSpecConstants.SOCKS_ON_CONNECT_REQUEST_TARGET_FACING_BIND_HOST.newSetting(
                        host));
        Assert.assertEquals(
                host,
                SocksValueDerivationHelper.getSocksOnConnectRequestTargetFacingBindHostFrom(settings));
    }

    @Test
    public void testGetSocksOnConnectRequestTargetFacingBindHostAddressTypesFromRule01() {
        HostAddressType hostAddressType = HostAddressType.HOST_IPV4_ADDRESS;
        HostAddressTypes hostAddressTypes = HostAddressTypes.of(hostAddressType);
        Rule rule = new Rule.Builder()
                .addRuleAction(GeneralRuleActionSpecConstants.BIND_HOST_ADDRESS_TYPE.newRuleAction(
                        hostAddressType))
                .build();
        Assert.assertEquals(
                hostAddressTypes,
                SocksValueDerivationHelper.getSocksOnConnectRequestTargetFacingBindHostAddressTypesFrom(rule));
    }

    @Test
    public void testGetSocksOnConnectRequestTargetFacingBindHostAddressTypesFromRule02() {
        HostAddressType hostAddressType = HostAddressType.HOST_IPV4_ADDRESS;
        HostAddressTypes hostAddressTypes = HostAddressTypes.of(hostAddressType);
        Rule rule = new Rule.Builder()
                .addRuleAction(GeneralRuleActionSpecConstants.EXTERNAL_FACING_BIND_HOST_ADDRESS_TYPE.newRuleAction(
                        hostAddressType))
                .build();
        Assert.assertEquals(
                hostAddressTypes,
                SocksValueDerivationHelper.getSocksOnConnectRequestTargetFacingBindHostAddressTypesFrom(rule));
    }

    @Test
    public void testGetSocksOnConnectRequestTargetFacingBindHostAddressTypesFromRule03() {
        HostAddressType hostAddressType = HostAddressType.HOST_IPV4_ADDRESS;
        HostAddressTypes hostAddressTypes = HostAddressTypes.of(hostAddressType);
        Rule rule = new Rule.Builder()
                .addRuleAction(SocksRuleActionSpecConstants.SOCKS_ON_REQUEST_EXTERNAL_FACING_BIND_HOST_ADDRESS_TYPE.newRuleAction(
                        hostAddressType))
                .build();
        Assert.assertEquals(
                hostAddressTypes,
                SocksValueDerivationHelper.getSocksOnConnectRequestTargetFacingBindHostAddressTypesFrom(rule));
    }

    @Test
    public void testGetSocksOnConnectRequestTargetFacingBindHostAddressTypesFromRule04() {
        HostAddressType hostAddressType = HostAddressType.HOST_IPV4_ADDRESS;
        HostAddressTypes hostAddressTypes = HostAddressTypes.of(hostAddressType);
        Rule rule = new Rule.Builder()
                .addRuleAction(SocksRuleActionSpecConstants.SOCKS_ON_CONNECT_REQUEST_TARGET_FACING_BIND_HOST_ADDRESS_TYPE.newRuleAction(
                        hostAddressType))
                .build();
        Assert.assertEquals(
                hostAddressTypes,
                SocksValueDerivationHelper.getSocksOnConnectRequestTargetFacingBindHostAddressTypesFrom(rule));
    }

    @Test
    public void testGetSocksOnConnectRequestTargetFacingBindHostAddressTypesFromSettings01() {
        Settings settings = Settings.of();
        Assert.assertEquals(
                HostAddressTypes.of(
                        HostAddressType.HOST_IPV4_ADDRESS,
                        HostAddressType.HOST_IPV6_ADDRESS),
                SocksValueDerivationHelper.getSocksOnConnectRequestTargetFacingBindHostAddressTypesFrom(settings));
    }

    @Test
    public void testGetSocksOnConnectRequestTargetFacingBindHostAddressTypesFromSettings02() {
        HostAddressType hostAddressType = HostAddressType.HOST_IPV4_ADDRESS;
        HostAddressTypes hostAddressTypes = HostAddressTypes.of(hostAddressType);
        Settings settings = Settings.of(
                GeneralSettingSpecConstants.BIND_HOST_ADDRESS_TYPES.newSetting(
                        hostAddressTypes));
        Assert.assertEquals(
                hostAddressTypes,
                SocksValueDerivationHelper.getSocksOnConnectRequestTargetFacingBindHostAddressTypesFrom(settings));
    }

    @Test
    public void testGetSocksOnConnectRequestTargetFacingBindHostAddressTypesFromSettings03() {
        HostAddressType hostAddressType = HostAddressType.HOST_IPV4_ADDRESS;
        HostAddressTypes hostAddressTypes = HostAddressTypes.of(hostAddressType);
        Settings settings = Settings.of(
                GeneralSettingSpecConstants.EXTERNAL_FACING_BIND_HOST_ADDRESS_TYPES.newSetting(
                        hostAddressTypes));
        Assert.assertEquals(
                hostAddressTypes,
                SocksValueDerivationHelper.getSocksOnConnectRequestTargetFacingBindHostAddressTypesFrom(settings));
    }

    @Test
    public void testGetSocksOnConnectRequestTargetFacingBindHostAddressTypesFromSettings04() {
        HostAddressType hostAddressType = HostAddressType.HOST_IPV4_ADDRESS;
        HostAddressTypes hostAddressTypes = HostAddressTypes.of(hostAddressType);
        Settings settings = Settings.of(
                SocksSettingSpecConstants.SOCKS_ON_REQUEST_EXTERNAL_FACING_BIND_HOST_ADDRESS_TYPES.newSetting(
                        hostAddressTypes));
        Assert.assertEquals(
                hostAddressTypes,
                SocksValueDerivationHelper.getSocksOnConnectRequestTargetFacingBindHostAddressTypesFrom(settings));
    }

    @Test
    public void testGetSocksOnConnectRequestTargetFacingBindHostAddressTypesFromSettings05() {
        HostAddressType hostAddressType = HostAddressType.HOST_IPV4_ADDRESS;
        HostAddressTypes hostAddressTypes = HostAddressTypes.of(hostAddressType);
        Settings settings = Settings.of(
                SocksSettingSpecConstants.SOCKS_ON_CONNECT_REQUEST_TARGET_FACING_BIND_HOST_ADDRESS_TYPES.newSetting(
                        hostAddressTypes));
        Assert.assertEquals(
                hostAddressTypes,
                SocksValueDerivationHelper.getSocksOnConnectRequestTargetFacingBindHostAddressTypesFrom(settings));
    }

    @Test
    public void testGetSocksOnConnectRequestTargetFacingBindPortRangesFromRule01() {
        PortRange portRange = PortRange.of(
                Port.valueOf(1), Port.valueOf(65535));
        PortRanges portRanges = PortRanges.of(portRange);
        Rule rule = new Rule.Builder()
                .addRuleAction(GeneralRuleActionSpecConstants.BIND_TCP_PORT_RANGE.newRuleAction(
                        portRange))
                .build();
        Assert.assertEquals(
                portRanges,
                SocksValueDerivationHelper.getSocksOnConnectRequestTargetFacingBindPortRangesFrom(rule));
    }

    @Test
    public void testGetSocksOnConnectRequestTargetFacingBindPortRangesFromRule02() {
        PortRange portRange = PortRange.of(
                Port.valueOf(1), Port.valueOf(65535));
        PortRanges portRanges = PortRanges.of(portRange);
        Rule rule = new Rule.Builder()
                .addRuleAction(GeneralRuleActionSpecConstants.EXTERNAL_FACING_BIND_TCP_PORT_RANGE.newRuleAction(
                        portRange))
                .build();
        Assert.assertEquals(
                portRanges,
                SocksValueDerivationHelper.getSocksOnConnectRequestTargetFacingBindPortRangesFrom(rule));
    }

    @Test
    public void testGetSocksOnConnectRequestTargetFacingBindPortRangesFromRule03() {
        PortRange portRange = PortRange.of(
                Port.valueOf(1), Port.valueOf(65535));
        PortRanges portRanges = PortRanges.of(portRange);
        Rule rule = new Rule.Builder()
                .addRuleAction(SocksRuleActionSpecConstants.SOCKS_ON_REQUEST_EXTERNAL_FACING_BIND_TCP_PORT_RANGE.newRuleAction(
                        portRange))
                .build();
        Assert.assertEquals(
                portRanges,
                SocksValueDerivationHelper.getSocksOnConnectRequestTargetFacingBindPortRangesFrom(rule));
    }

    @Test
    public void testGetSocksOnConnectRequestTargetFacingBindPortRangesFromRule04() {
        PortRange portRange = PortRange.of(
                Port.valueOf(1), Port.valueOf(65535));
        PortRanges portRanges = PortRanges.of(portRange);
        Rule rule = new Rule.Builder()
                .addRuleAction(SocksRuleActionSpecConstants.SOCKS_ON_CONNECT_REQUEST_TARGET_FACING_BIND_PORT_RANGE.newRuleAction(
                        portRange))
                .build();
        Assert.assertEquals(
                portRanges,
                SocksValueDerivationHelper.getSocksOnConnectRequestTargetFacingBindPortRangesFrom(rule));
    }

    @Test
    public void testGetSocksOnConnectRequestTargetFacingBindPortRangesFromSettings01() {
        PortRanges portRanges = PortRanges.getDefault();
        Settings settings = Settings.of();
        Assert.assertEquals(
                portRanges,
                SocksValueDerivationHelper.getSocksOnConnectRequestTargetFacingBindPortRangesFrom(settings));
    }

    @Test
    public void testGetSocksOnConnectRequestTargetFacingBindPortRangesFromSettings02() {
        PortRange portRange = PortRange.of(
                Port.valueOf(1), Port.valueOf(65535));
        PortRanges portRanges = PortRanges.of(portRange);
        Settings settings = Settings.of(
                GeneralSettingSpecConstants.BIND_TCP_PORT_RANGES.newSetting(
                        portRanges));
        Assert.assertEquals(
                portRanges,
                SocksValueDerivationHelper.getSocksOnConnectRequestTargetFacingBindPortRangesFrom(settings));
    }

    @Test
    public void testGetSocksOnConnectRequestTargetFacingBindPortRangesFromSettings03() {
        PortRange portRange = PortRange.of(
                Port.valueOf(1), Port.valueOf(65535));
        PortRanges portRanges = PortRanges.of(portRange);
        Settings settings = Settings.of(
                GeneralSettingSpecConstants.EXTERNAL_FACING_BIND_TCP_PORT_RANGES.newSetting(
                        portRanges));
        Assert.assertEquals(
                portRanges,
                SocksValueDerivationHelper.getSocksOnConnectRequestTargetFacingBindPortRangesFrom(settings));
    }

    @Test
    public void testGetSocksOnConnectRequestTargetFacingBindPortRangesFromSettings04() {
        PortRange portRange = PortRange.of(
                Port.valueOf(1), Port.valueOf(65535));
        PortRanges portRanges = PortRanges.of(portRange);
        Settings settings = Settings.of(
                SocksSettingSpecConstants.SOCKS_ON_REQUEST_EXTERNAL_FACING_BIND_TCP_PORT_RANGES.newSetting(
                        portRanges));
        Assert.assertEquals(
                portRanges,
                SocksValueDerivationHelper.getSocksOnConnectRequestTargetFacingBindPortRangesFrom(settings));
    }

    @Test
    public void testGetSocksOnConnectRequestTargetFacingBindPortRangesFromSettings05() {
        PortRange portRange = PortRange.of(
                Port.valueOf(1), Port.valueOf(65535));
        PortRanges portRanges = PortRanges.of(portRange);
        Settings settings = Settings.of(
                SocksSettingSpecConstants.SOCKS_ON_CONNECT_REQUEST_TARGET_FACING_BIND_PORT_RANGES.newSetting(
                        portRanges));
        Assert.assertEquals(
                portRanges,
                SocksValueDerivationHelper.getSocksOnConnectRequestTargetFacingBindPortRangesFrom(settings));
    }

    @Test
    public void testGetSocksOnConnectRequestTargetFacingConnectTimeoutFromRule01() {
        PositiveInteger i = PositiveInteger.valueOf(1);
        Rule rule = new Rule.Builder()
                .addRuleAction(SocksRuleActionSpecConstants.SOCKS_ON_CONNECT_REQUEST_TARGET_FACING_CONNECT_TIMEOUT.newRuleAction(i))
                .build();
        Assert.assertEquals(
                i, 
                SocksValueDerivationHelper.getSocksOnConnectRequestTargetFacingConnectTimeoutFrom(rule));
    }
    
    @Test
    public void testGetSocksOnConnectRequestTargetFacingConnectTimeoutFromSettings01() {
        Settings settings = Settings.of();
        Assert.assertEquals(
                PositiveInteger.valueOf(60000), 
                SocksValueDerivationHelper.getSocksOnConnectRequestTargetFacingConnectTimeoutFrom(settings));
    }

    @Test
    public void testGetSocksOnConnectRequestTargetFacingConnectTimeoutFromSettings02() {
        PositiveInteger i = PositiveInteger.valueOf(1);
        Settings settings = Settings.of(
                SocksSettingSpecConstants.SOCKS_ON_CONNECT_REQUEST_TARGET_FACING_CONNECT_TIMEOUT.newSetting(i));
        Assert.assertEquals(
                i,
                SocksValueDerivationHelper.getSocksOnConnectRequestTargetFacingConnectTimeoutFrom(settings));
    }

    @Test
    public void testGetSocksOnConnectRequestTargetFacingNetInterfaceFromRule01() throws UnknownHostException, SocketException {
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
                SocksValueDerivationHelper.getSocksOnConnectRequestTargetFacingNetInterfaceFrom(rule));
    }

    @Test
    public void testGetSocksOnConnectRequestTargetFacingNetInterfaceFromRule02() throws UnknownHostException, SocketException {
        Host host = Host.newInstance("127.0.0.1");
        NetworkInterface networkInterface = NetworkInterface.getByInetAddress(
                host.toInetAddress());
        NetInterface netInterface = NetInterface.newInstance(networkInterface);
        Rule rule = new Rule.Builder()
                .addRuleAction(GeneralRuleActionSpecConstants.EXTERNAL_FACING_NET_INTERFACE.newRuleAction(
                        netInterface))
                .build();
        Assert.assertEquals(
                netInterface,
                SocksValueDerivationHelper.getSocksOnConnectRequestTargetFacingNetInterfaceFrom(rule));
    }

    @Test
    public void testGetSocksOnConnectRequestTargetFacingNetInterfaceFromRule03() throws UnknownHostException, SocketException {
        Host host = Host.newInstance("127.0.0.1");
        NetworkInterface networkInterface = NetworkInterface.getByInetAddress(
                host.toInetAddress());
        NetInterface netInterface = NetInterface.newInstance(networkInterface);
        Rule rule = new Rule.Builder()
                .addRuleAction(SocksRuleActionSpecConstants.SOCKS_ON_REQUEST_EXTERNAL_FACING_NET_INTERFACE.newRuleAction(
                        netInterface))
                .build();
        Assert.assertEquals(
                netInterface,
                SocksValueDerivationHelper.getSocksOnConnectRequestTargetFacingNetInterfaceFrom(rule));
    }

    @Test
    public void testGetSocksOnConnectRequestTargetFacingNetInterfaceFromRule04() throws UnknownHostException, SocketException {
        Host host = Host.newInstance("127.0.0.1");
        NetworkInterface networkInterface = NetworkInterface.getByInetAddress(
                host.toInetAddress());
        NetInterface netInterface = NetInterface.newInstance(networkInterface);
        Rule rule = new Rule.Builder()
                .addRuleAction(SocksRuleActionSpecConstants.SOCKS_ON_CONNECT_REQUEST_TARGET_FACING_NET_INTERFACE.newRuleAction(
                        netInterface))
                .build();
        Assert.assertEquals(
                netInterface,
                SocksValueDerivationHelper.getSocksOnConnectRequestTargetFacingNetInterfaceFrom(rule));
    }

    @Test
    public void testGetSocksOnConnectRequestTargetFacingNetInterfaceFromSettings01() {
        Settings settings = Settings.of();
        Assert.assertNull(SocksValueDerivationHelper.getSocksOnConnectRequestTargetFacingNetInterfaceFrom(settings));
    }

    @Test
    public void testGetSocksOnConnectRequestTargetFacingNetInterfaceFromSettings02() throws UnknownHostException, SocketException {
        Host host = Host.newInstance("127.0.0.1");
        NetworkInterface networkInterface = NetworkInterface.getByInetAddress(
                host.toInetAddress());
        NetInterface netInterface = NetInterface.newInstance(networkInterface);
        Settings settings = Settings.of(
                GeneralSettingSpecConstants.NET_INTERFACE.newSetting(
                        netInterface));
        Assert.assertEquals(
                netInterface,
                SocksValueDerivationHelper.getSocksOnConnectRequestTargetFacingNetInterfaceFrom(settings));
    }

    @Test
    public void testGetSocksOnConnectRequestTargetFacingNetInterfaceFromSettings03() throws UnknownHostException, SocketException {
        Host host = Host.newInstance("127.0.0.1");
        NetworkInterface networkInterface = NetworkInterface.getByInetAddress(
                host.toInetAddress());
        NetInterface netInterface = NetInterface.newInstance(networkInterface);
        Settings settings = Settings.of(
                GeneralSettingSpecConstants.EXTERNAL_FACING_NET_INTERFACE.newSetting(
                        netInterface));
        Assert.assertEquals(
                netInterface,
                SocksValueDerivationHelper.getSocksOnConnectRequestTargetFacingNetInterfaceFrom(settings));
    }

    @Test
    public void testGetSocksOnConnectRequestTargetFacingNetInterfaceFromSettings04() throws UnknownHostException, SocketException {
        Host host = Host.newInstance("127.0.0.1");
        NetworkInterface networkInterface = NetworkInterface.getByInetAddress(
                host.toInetAddress());
        NetInterface netInterface = NetInterface.newInstance(networkInterface);
        Settings settings = Settings.of(
                SocksSettingSpecConstants.SOCKS_ON_REQUEST_EXTERNAL_FACING_NET_INTERFACE.newSetting(
                        netInterface));
        Assert.assertEquals(
                netInterface,
                SocksValueDerivationHelper.getSocksOnConnectRequestTargetFacingNetInterfaceFrom(settings));
    }

    @Test
    public void testGetSocksOnConnectRequestTargetFacingNetInterfaceFromSettings05() throws UnknownHostException, SocketException {
        Host host = Host.newInstance("127.0.0.1");
        NetworkInterface networkInterface = NetworkInterface.getByInetAddress(
                host.toInetAddress());
        NetInterface netInterface = NetInterface.newInstance(networkInterface);
        Settings settings = Settings.of(
                SocksSettingSpecConstants.SOCKS_ON_CONNECT_REQUEST_TARGET_FACING_NET_INTERFACE.newSetting(
                        netInterface));
        Assert.assertEquals(
                netInterface,
                SocksValueDerivationHelper.getSocksOnConnectRequestTargetFacingNetInterfaceFrom(settings));
    }
    
    @Test
    public void testGetSocksOnConnectRequestTargetFacingSocketSettingsFromRule01() {
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
                SocksValueDerivationHelper.getSocksOnConnectRequestTargetFacingSocketSettingsFrom(rule));
    }

    @Test
    public void testGetSocksOnConnectRequestTargetFacingSocketSettingsFromRule02() {
        SocketSetting<?> socketSetting =
                StandardSocketSettingSpecConstants.SO_TIMEOUT.newSocketSetting(
                        NonNegativeInteger.valueOf(0));
        @SuppressWarnings("unchecked")
        SocketSetting<Object> socketSttng = (SocketSetting<Object>) socketSetting;
        SocketSettings socketSettings = SocketSettings.of(socketSttng);
        Rule rule = new Rule.Builder()
                .addRuleAction(GeneralRuleActionSpecConstants.EXTERNAL_FACING_SOCKET_SETTING.newRuleAction(
                        socketSttng))
                .build();
        Assert.assertEquals(
                socketSettings,
                SocksValueDerivationHelper.getSocksOnConnectRequestTargetFacingSocketSettingsFrom(rule));
    }

    @Test
    public void testGetSocksOnConnectRequestTargetFacingSocketSettingsFromRule03() {
        SocketSetting<?> socketSetting =
                StandardSocketSettingSpecConstants.SO_TIMEOUT.newSocketSetting(
                        NonNegativeInteger.valueOf(0));
        @SuppressWarnings("unchecked")
        SocketSetting<Object> socketSttng = (SocketSetting<Object>) socketSetting;
        SocketSettings socketSettings = SocketSettings.of(socketSttng);
        Rule rule = new Rule.Builder()
                .addRuleAction(SocksRuleActionSpecConstants.SOCKS_ON_REQUEST_EXTERNAL_FACING_SOCKET_SETTING.newRuleAction(
                        socketSttng))
                .build();
        Assert.assertEquals(
                socketSettings,
                SocksValueDerivationHelper.getSocksOnConnectRequestTargetFacingSocketSettingsFrom(rule));
    }

    @Test
    public void testGetSocksOnConnectRequestTargetFacingSocketSettingsFromRule04() {
        SocketSetting<?> socketSetting =
                StandardSocketSettingSpecConstants.SO_TIMEOUT.newSocketSetting(
                        NonNegativeInteger.valueOf(0));
        @SuppressWarnings("unchecked")
        SocketSetting<Object> socketSttng = (SocketSetting<Object>) socketSetting;
        SocketSettings socketSettings = SocketSettings.of(socketSttng);
        Rule rule = new Rule.Builder()
                .addRuleAction(SocksRuleActionSpecConstants.SOCKS_ON_CONNECT_REQUEST_TARGET_FACING_SOCKET_SETTING.newRuleAction(
                        socketSttng))
                .build();
        Assert.assertEquals(
                socketSettings,
                SocksValueDerivationHelper.getSocksOnConnectRequestTargetFacingSocketSettingsFrom(rule));
    }

    @Test
    public void testGetSocksOnConnectRequestTargetFacingSocketSettingsFromSettings01() {
        SocketSettings socketSettings = SocketSettings.of();
        Settings settings = Settings.of();
        Assert.assertEquals(
                socketSettings,
                SocksValueDerivationHelper.getSocksOnConnectRequestTargetFacingSocketSettingsFrom(settings));
    }

    @Test
    public void testGetSocksOnConnectRequestTargetFacingSocketSettingsFromSettings02() {
        SocketSetting<?> socketSetting =
                StandardSocketSettingSpecConstants.SO_TIMEOUT.newSocketSetting(
                        NonNegativeInteger.valueOf(0));
        SocketSettings socketSettings = SocketSettings.of(socketSetting);
        Settings settings = Settings.of(
                GeneralSettingSpecConstants.SOCKET_SETTINGS.newSetting(
                        socketSettings));
        Assert.assertEquals(
                socketSettings,
                SocksValueDerivationHelper.getSocksOnConnectRequestTargetFacingSocketSettingsFrom(settings));
    }

    @Test
    public void testGetSocksOnConnectRequestTargetFacingSocketSettingsFromSettings03() {
        SocketSetting<?> socketSetting =
                StandardSocketSettingSpecConstants.SO_TIMEOUT.newSocketSetting(
                        NonNegativeInteger.valueOf(0));
        SocketSettings socketSettings = SocketSettings.of(socketSetting);
        Settings settings = Settings.of(
                GeneralSettingSpecConstants.EXTERNAL_FACING_SOCKET_SETTINGS.newSetting(
                        socketSettings));
        Assert.assertEquals(
                socketSettings,
                SocksValueDerivationHelper.getSocksOnConnectRequestTargetFacingSocketSettingsFrom(settings));
    }

    @Test
    public void testGetSocksOnConnectRequestTargetFacingSocketSettingsFromSettings04() {
        SocketSetting<?> socketSetting =
                StandardSocketSettingSpecConstants.SO_TIMEOUT.newSocketSetting(
                        NonNegativeInteger.valueOf(0));
        SocketSettings socketSettings = SocketSettings.of(socketSetting);
        Settings settings = Settings.of(
                SocksSettingSpecConstants.SOCKS_ON_REQUEST_EXTERNAL_FACING_SOCKET_SETTINGS.newSetting(
                        socketSettings));
        Assert.assertEquals(
                socketSettings,
                SocksValueDerivationHelper.getSocksOnConnectRequestTargetFacingSocketSettingsFrom(settings));
    }

    @Test
    public void testGetSocksOnConnectRequestTargetFacingSocketSettingsFromSettings05() {
        SocketSetting<?> socketSetting =
                StandardSocketSettingSpecConstants.SO_TIMEOUT.newSocketSetting(
                        NonNegativeInteger.valueOf(0));
        SocketSettings socketSettings = SocketSettings.of(socketSetting);
        Settings settings = Settings.of(
                SocksSettingSpecConstants.SOCKS_ON_CONNECT_REQUEST_TARGET_FACING_SOCKET_SETTINGS.newSetting(
                        socketSettings));
        Assert.assertEquals(
                socketSettings,
                SocksValueDerivationHelper.getSocksOnConnectRequestTargetFacingSocketSettingsFrom(settings));
    }

    @Test
    public void testGetSocksOnUdpAssociateRequestClientFacingBindHostFromRule01() {
        Host host = Host.newInstance("127.0.0.1");
        Rule rule = new Rule.Builder()
                .addRuleAction(GeneralRuleActionSpecConstants.BIND_HOST.newRuleAction(
                        host))
                .build();
        Assert.assertEquals(
                host,
                SocksValueDerivationHelper.getSocksOnUdpAssociateRequestClientFacingBindHostFrom(rule));
    }

    @Test
    public void testGetSocksOnUdpAssociateRequestClientFacingBindHostFromRule02() {
        Host host = Host.newInstance("127.0.0.1");
        Rule rule = new Rule.Builder()
                .addRuleAction(GeneralRuleActionSpecConstants.INTERNAL_FACING_BIND_HOST.newRuleAction(
                        host))
                .build();
        Assert.assertEquals(
                host,
                SocksValueDerivationHelper.getSocksOnUdpAssociateRequestClientFacingBindHostFrom(rule));
    }

    @Test
    public void testGetSocksOnUdpAssociateRequestClientFacingBindHostFromRule03() {
        Host host = Host.newInstance("127.0.0.1");
        Rule rule = new Rule.Builder()
                .addRuleAction(SocksRuleActionSpecConstants.SOCKS_ON_REQUEST_INTERNAL_FACING_BIND_HOST.newRuleAction(
                        host))
                .build();
        Assert.assertEquals(
                host,
                SocksValueDerivationHelper.getSocksOnUdpAssociateRequestClientFacingBindHostFrom(rule));
    }

    @Test
    public void testGetSocksOnUdpAssociateRequestClientFacingBindHostFromRule04() {
        Host host = Host.newInstance("127.0.0.1");
        Rule rule = new Rule.Builder()
                .addRuleAction(SocksRuleActionSpecConstants.SOCKS_ON_UDP_ASSOCIATE_REQUEST_CLIENT_FACING_BIND_HOST.newRuleAction(
                        host))
                .build();
        Assert.assertEquals(
                host,
                SocksValueDerivationHelper.getSocksOnUdpAssociateRequestClientFacingBindHostFrom(rule));
    }

    @Test
    public void testGetSocksOnUdpAssociateRequestClientFacingBindHostFromSettings01() {
        Settings settings = Settings.of();
        Assert.assertNull(SocksValueDerivationHelper.getSocksOnUdpAssociateRequestClientFacingBindHostFrom(settings));
    }

    @Test
    public void testGetSocksOnUdpAssociateRequestClientFacingBindHostFromSettings02() {
        Host host = Host.newInstance("127.0.0.1");
        Settings settings = Settings.of(
                GeneralSettingSpecConstants.BIND_HOST.newSetting(host));
        Assert.assertEquals(
                host,
                SocksValueDerivationHelper.getSocksOnUdpAssociateRequestClientFacingBindHostFrom(settings));
    }

    @Test
    public void testGetSocksOnUdpAssociateRequestClientFacingBindHostFromSettings03() {
        Host host = Host.newInstance("127.0.0.1");
        Settings settings = Settings.of(
                GeneralSettingSpecConstants.INTERNAL_FACING_BIND_HOST.newSetting(
                        host));
        Assert.assertEquals(
                host,
                SocksValueDerivationHelper.getSocksOnUdpAssociateRequestClientFacingBindHostFrom(settings));
    }

    @Test
    public void testGetSocksOnUdpAssociateRequestClientFacingBindHostFromSettings04() {
        Host host = Host.newInstance("127.0.0.1");
        Settings settings = Settings.of(
                SocksSettingSpecConstants.SOCKS_ON_REQUEST_INTERNAL_FACING_BIND_HOST.newSetting(
                        host));
        Assert.assertEquals(
                host,
                SocksValueDerivationHelper.getSocksOnUdpAssociateRequestClientFacingBindHostFrom(settings));
    }

    @Test
    public void testGetSocksOnUdpAssociateRequestClientFacingBindHostFromSettings05() {
        Host host = Host.newInstance("127.0.0.1");
        Settings settings = Settings.of(
                SocksSettingSpecConstants.SOCKS_ON_UDP_ASSOCIATE_REQUEST_CLIENT_FACING_BIND_HOST.newSetting(
                        host));
        Assert.assertEquals(
                host,
                SocksValueDerivationHelper.getSocksOnUdpAssociateRequestClientFacingBindHostFrom(settings));
    }

    @Test
    public void testGetSocksOnUdpAssociateRequestClientFacingBindHostAddressTypesFromRule01() {
        HostAddressType hostAddressType = HostAddressType.HOST_IPV4_ADDRESS;
        HostAddressTypes hostAddressTypes = HostAddressTypes.of(hostAddressType);
        Rule rule = new Rule.Builder()
                .addRuleAction(GeneralRuleActionSpecConstants.BIND_HOST_ADDRESS_TYPE.newRuleAction(
                        hostAddressType))
                .build();
        Assert.assertEquals(
                hostAddressTypes,
                SocksValueDerivationHelper.getSocksOnUdpAssociateRequestClientFacingBindHostAddressTypesFrom(rule));
    }

    @Test
    public void testGetSocksOnUdpAssociateRequestClientFacingBindHostAddressTypesFromRule02() {
        HostAddressType hostAddressType = HostAddressType.HOST_IPV4_ADDRESS;
        HostAddressTypes hostAddressTypes = HostAddressTypes.of(hostAddressType);
        Rule rule = new Rule.Builder()
                .addRuleAction(GeneralRuleActionSpecConstants.INTERNAL_FACING_BIND_HOST_ADDRESS_TYPE.newRuleAction(
                        hostAddressType))
                .build();
        Assert.assertEquals(
                hostAddressTypes,
                SocksValueDerivationHelper.getSocksOnUdpAssociateRequestClientFacingBindHostAddressTypesFrom(rule));
    }

    @Test
    public void testGetSocksOnUdpAssociateRequestClientFacingBindHostAddressTypesFromRule03() {
        HostAddressType hostAddressType = HostAddressType.HOST_IPV4_ADDRESS;
        HostAddressTypes hostAddressTypes = HostAddressTypes.of(hostAddressType);
        Rule rule = new Rule.Builder()
                .addRuleAction(SocksRuleActionSpecConstants.SOCKS_ON_REQUEST_INTERNAL_FACING_BIND_HOST_ADDRESS_TYPE.newRuleAction(
                        hostAddressType))
                .build();
        Assert.assertEquals(
                hostAddressTypes,
                SocksValueDerivationHelper.getSocksOnUdpAssociateRequestClientFacingBindHostAddressTypesFrom(rule));
    }

    @Test
    public void testGetSocksOnUdpAssociateRequestClientFacingBindHostAddressTypesFromRule04() {
        HostAddressType hostAddressType = HostAddressType.HOST_IPV4_ADDRESS;
        HostAddressTypes hostAddressTypes = HostAddressTypes.of(hostAddressType);
        Rule rule = new Rule.Builder()
                .addRuleAction(SocksRuleActionSpecConstants.SOCKS_ON_UDP_ASSOCIATE_REQUEST_CLIENT_FACING_BIND_HOST_ADDRESS_TYPE.newRuleAction(
                        hostAddressType))
                .build();
        Assert.assertEquals(
                hostAddressTypes,
                SocksValueDerivationHelper.getSocksOnUdpAssociateRequestClientFacingBindHostAddressTypesFrom(rule));
    }

    @Test
    public void testGetSocksOnUdpAssociateRequestClientFacingBindHostAddressTypesFromSettings01() {
        Settings settings = Settings.of();
        Assert.assertEquals(
                HostAddressTypes.of(
                        HostAddressType.HOST_IPV4_ADDRESS,
                        HostAddressType.HOST_IPV6_ADDRESS),
                SocksValueDerivationHelper.getSocksOnUdpAssociateRequestClientFacingBindHostAddressTypesFrom(settings));
    }

    @Test
    public void testGetSocksOnUdpAssociateRequestClientFacingBindHostAddressTypesFromSettings02() {
        HostAddressType hostAddressType = HostAddressType.HOST_IPV4_ADDRESS;
        HostAddressTypes hostAddressTypes = HostAddressTypes.of(hostAddressType);
        Settings settings = Settings.of(
                GeneralSettingSpecConstants.BIND_HOST_ADDRESS_TYPES.newSetting(
                        hostAddressTypes));
        Assert.assertEquals(
                hostAddressTypes,
                SocksValueDerivationHelper.getSocksOnUdpAssociateRequestClientFacingBindHostAddressTypesFrom(settings));
    }

    @Test
    public void testGetSocksOnUdpAssociateRequestClientFacingBindHostAddressTypesFromSettings03() {
        HostAddressType hostAddressType = HostAddressType.HOST_IPV4_ADDRESS;
        HostAddressTypes hostAddressTypes = HostAddressTypes.of(hostAddressType);
        Settings settings = Settings.of(
                GeneralSettingSpecConstants.INTERNAL_FACING_BIND_HOST_ADDRESS_TYPES.newSetting(
                        hostAddressTypes));
        Assert.assertEquals(
                hostAddressTypes,
                SocksValueDerivationHelper.getSocksOnUdpAssociateRequestClientFacingBindHostAddressTypesFrom(settings));
    }

    @Test
    public void testGetSocksOnUdpAssociateRequestClientFacingBindHostAddressTypesFromSettings04() {
        HostAddressType hostAddressType = HostAddressType.HOST_IPV4_ADDRESS;
        HostAddressTypes hostAddressTypes = HostAddressTypes.of(hostAddressType);
        Settings settings = Settings.of(
                SocksSettingSpecConstants.SOCKS_ON_REQUEST_INTERNAL_FACING_BIND_HOST_ADDRESS_TYPES.newSetting(
                        hostAddressTypes));
        Assert.assertEquals(
                hostAddressTypes,
                SocksValueDerivationHelper.getSocksOnUdpAssociateRequestClientFacingBindHostAddressTypesFrom(settings));
    }

    @Test
    public void testGetSocksOnUdpAssociateRequestClientFacingBindHostAddressTypesFromSettings05() {
        HostAddressType hostAddressType = HostAddressType.HOST_IPV4_ADDRESS;
        HostAddressTypes hostAddressTypes = HostAddressTypes.of(hostAddressType);
        Settings settings = Settings.of(
                SocksSettingSpecConstants.SOCKS_ON_UDP_ASSOCIATE_REQUEST_CLIENT_FACING_BIND_HOST_ADDRESS_TYPES.newSetting(
                        hostAddressTypes));
        Assert.assertEquals(
                hostAddressTypes,
                SocksValueDerivationHelper.getSocksOnUdpAssociateRequestClientFacingBindHostAddressTypesFrom(settings));
    }
    
    @Test
    public void testGetSocksOnUdpAssociateRequestClientFacingBindPortRangesFromRule01() {
        PortRange portRange = PortRange.of(
                Port.valueOf(1), Port.valueOf(65535));
        PortRanges portRanges = PortRanges.of(portRange);
        Rule rule = new Rule.Builder()
                .addRuleAction(GeneralRuleActionSpecConstants.BIND_UDP_PORT_RANGE.newRuleAction(
                        portRange))
                .build();
        Assert.assertEquals(
                portRanges,
                SocksValueDerivationHelper.getSocksOnUdpAssociateRequestClientFacingBindPortRangesFrom(rule));
    }

    @Test
    public void testGetSocksOnUdpAssociateRequestClientFacingBindPortRangesFromRule02() {
        PortRange portRange = PortRange.of(
                Port.valueOf(1), Port.valueOf(65535));
        PortRanges portRanges = PortRanges.of(portRange);
        Rule rule = new Rule.Builder()
                .addRuleAction(GeneralRuleActionSpecConstants.INTERNAL_FACING_BIND_UDP_PORT_RANGE.newRuleAction(
                        portRange))
                .build();
        Assert.assertEquals(
                portRanges,
                SocksValueDerivationHelper.getSocksOnUdpAssociateRequestClientFacingBindPortRangesFrom(rule));
    }

    @Test
    public void testGetSocksOnUdpAssociateRequestClientFacingBindPortRangesFromRule03() {
        PortRange portRange = PortRange.of(
                Port.valueOf(1), Port.valueOf(65535));
        PortRanges portRanges = PortRanges.of(portRange);
        Rule rule = new Rule.Builder()
                .addRuleAction(SocksRuleActionSpecConstants.SOCKS_ON_REQUEST_INTERNAL_FACING_BIND_UDP_PORT_RANGE.newRuleAction(
                        portRange))
                .build();
        Assert.assertEquals(
                portRanges,
                SocksValueDerivationHelper.getSocksOnUdpAssociateRequestClientFacingBindPortRangesFrom(rule));
    }

    @Test
    public void testGetSocksOnUdpAssociateRequestClientFacingBindPortRangesFromRule04() {
        PortRange portRange = PortRange.of(
                Port.valueOf(1), Port.valueOf(65535));
        PortRanges portRanges = PortRanges.of(portRange);
        Rule rule = new Rule.Builder()
                .addRuleAction(SocksRuleActionSpecConstants.SOCKS_ON_UDP_ASSOCIATE_REQUEST_CLIENT_FACING_BIND_PORT_RANGE.newRuleAction(
                        portRange))
                .build();
        Assert.assertEquals(
                portRanges,
                SocksValueDerivationHelper.getSocksOnUdpAssociateRequestClientFacingBindPortRangesFrom(rule));
    }

    @Test
    public void testGetSocksOnUdpAssociateRequestClientFacingBindPortRangesFromSettings01() {
        PortRanges portRanges = PortRanges.getDefault();
        Settings settings = Settings.of();
        Assert.assertEquals(
                portRanges,
                SocksValueDerivationHelper.getSocksOnUdpAssociateRequestClientFacingBindPortRangesFrom(settings));
    }

    @Test
    public void testGetSocksOnUdpAssociateRequestClientFacingBindPortRangesFromSettings02() {
        PortRange portRange = PortRange.of(
                Port.valueOf(1), Port.valueOf(65535));
        PortRanges portRanges = PortRanges.of(portRange);
        Settings settings = Settings.of(
                GeneralSettingSpecConstants.BIND_UDP_PORT_RANGES.newSetting(
                        portRanges));
        Assert.assertEquals(
                portRanges,
                SocksValueDerivationHelper.getSocksOnUdpAssociateRequestClientFacingBindPortRangesFrom(settings));
    }

    @Test
    public void testGetSocksOnUdpAssociateRequestClientFacingBindPortRangesFromSettings03() {
        PortRange portRange = PortRange.of(
                Port.valueOf(1), Port.valueOf(65535));
        PortRanges portRanges = PortRanges.of(portRange);
        Settings settings = Settings.of(
                GeneralSettingSpecConstants.INTERNAL_FACING_BIND_UDP_PORT_RANGES.newSetting(
                        portRanges));
        Assert.assertEquals(
                portRanges,
                SocksValueDerivationHelper.getSocksOnUdpAssociateRequestClientFacingBindPortRangesFrom(settings));
    }

    @Test
    public void testGetSocksOnUdpAssociateRequestClientFacingBindPortRangesFromSettings04() {
        PortRange portRange = PortRange.of(
                Port.valueOf(1), Port.valueOf(65535));
        PortRanges portRanges = PortRanges.of(portRange);
        Settings settings = Settings.of(
                SocksSettingSpecConstants.SOCKS_ON_REQUEST_INTERNAL_FACING_BIND_UDP_PORT_RANGES.newSetting(
                        portRanges));
        Assert.assertEquals(
                portRanges,
                SocksValueDerivationHelper.getSocksOnUdpAssociateRequestClientFacingBindPortRangesFrom(settings));
    }

    @Test
    public void testGetSocksOnUdpAssociateRequestClientFacingBindPortRangesFromSettings05() {
        PortRange portRange = PortRange.of(
                Port.valueOf(1), Port.valueOf(65535));
        PortRanges portRanges = PortRanges.of(portRange);
        Settings settings = Settings.of(
                SocksSettingSpecConstants.SOCKS_ON_UDP_ASSOCIATE_REQUEST_CLIENT_FACING_BIND_PORT_RANGES.newSetting(
                        portRanges));
        Assert.assertEquals(
                portRanges,
                SocksValueDerivationHelper.getSocksOnUdpAssociateRequestClientFacingBindPortRangesFrom(settings));
    }

    @Test
    public void testGetSocksOnUdpAssociateRequestClientFacingNetInterfaceFromRule01() throws UnknownHostException, SocketException {
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
                SocksValueDerivationHelper.getSocksOnUdpAssociateRequestClientFacingNetInterfaceFrom(rule));
    }

    @Test
    public void testGetSocksOnUdpAssociateRequestClientFacingNetInterfaceFromRule02() throws UnknownHostException, SocketException {
        Host host = Host.newInstance("127.0.0.1");
        NetworkInterface networkInterface = NetworkInterface.getByInetAddress(
                host.toInetAddress());
        NetInterface netInterface = NetInterface.newInstance(networkInterface);
        Rule rule = new Rule.Builder()
                .addRuleAction(GeneralRuleActionSpecConstants.INTERNAL_FACING_NET_INTERFACE.newRuleAction(
                        netInterface))
                .build();
        Assert.assertEquals(
                netInterface,
                SocksValueDerivationHelper.getSocksOnUdpAssociateRequestClientFacingNetInterfaceFrom(rule));
    }

    @Test
    public void testGetSocksOnUdpAssociateRequestClientFacingNetInterfaceFromRule03() throws UnknownHostException, SocketException {
        Host host = Host.newInstance("127.0.0.1");
        NetworkInterface networkInterface = NetworkInterface.getByInetAddress(
                host.toInetAddress());
        NetInterface netInterface = NetInterface.newInstance(networkInterface);
        Rule rule = new Rule.Builder()
                .addRuleAction(SocksRuleActionSpecConstants.SOCKS_ON_REQUEST_INTERNAL_FACING_NET_INTERFACE.newRuleAction(
                        netInterface))
                .build();
        Assert.assertEquals(
                netInterface,
                SocksValueDerivationHelper.getSocksOnUdpAssociateRequestClientFacingNetInterfaceFrom(rule));
    }

    @Test
    public void testGetSocksOnUdpAssociateRequestClientFacingNetInterfaceFromRule04() throws UnknownHostException, SocketException {
        Host host = Host.newInstance("127.0.0.1");
        NetworkInterface networkInterface = NetworkInterface.getByInetAddress(
                host.toInetAddress());
        NetInterface netInterface = NetInterface.newInstance(networkInterface);
        Rule rule = new Rule.Builder()
                .addRuleAction(SocksRuleActionSpecConstants.SOCKS_ON_UDP_ASSOCIATE_REQUEST_CLIENT_FACING_NET_INTERFACE.newRuleAction(
                        netInterface))
                .build();
        Assert.assertEquals(
                netInterface,
                SocksValueDerivationHelper.getSocksOnUdpAssociateRequestClientFacingNetInterfaceFrom(rule));
    }

    @Test
    public void testGetSocksOnUdpAssociateRequestClientFacingNetInterfaceFromSettings01() {
        Settings settings = Settings.of();
        Assert.assertNull(SocksValueDerivationHelper.getSocksOnUdpAssociateRequestClientFacingNetInterfaceFrom(settings));
    }

    @Test
    public void testGetSocksOnUdpAssociateRequestClientFacingNetInterfaceFromSettings02() throws UnknownHostException, SocketException {
        Host host = Host.newInstance("127.0.0.1");
        NetworkInterface networkInterface = NetworkInterface.getByInetAddress(
                host.toInetAddress());
        NetInterface netInterface = NetInterface.newInstance(networkInterface);
        Settings settings = Settings.of(
                GeneralSettingSpecConstants.NET_INTERFACE.newSetting(
                        netInterface));
        Assert.assertEquals(
                netInterface,
                SocksValueDerivationHelper.getSocksOnUdpAssociateRequestClientFacingNetInterfaceFrom(settings));
    }

    @Test
    public void testGetSocksOnUdpAssociateRequestClientFacingNetInterfaceFromSettings03() throws UnknownHostException, SocketException {
        Host host = Host.newInstance("127.0.0.1");
        NetworkInterface networkInterface = NetworkInterface.getByInetAddress(
                host.toInetAddress());
        NetInterface netInterface = NetInterface.newInstance(networkInterface);
        Settings settings = Settings.of(
                GeneralSettingSpecConstants.INTERNAL_FACING_NET_INTERFACE.newSetting(
                        netInterface));
        Assert.assertEquals(
                netInterface,
                SocksValueDerivationHelper.getSocksOnUdpAssociateRequestClientFacingNetInterfaceFrom(settings));
    }

    @Test
    public void testGetSocksOnUdpAssociateRequestClientFacingNetInterfaceFromSettings04() throws UnknownHostException, SocketException {
        Host host = Host.newInstance("127.0.0.1");
        NetworkInterface networkInterface = NetworkInterface.getByInetAddress(
                host.toInetAddress());
        NetInterface netInterface = NetInterface.newInstance(networkInterface);
        Settings settings = Settings.of(
                SocksSettingSpecConstants.SOCKS_ON_REQUEST_INTERNAL_FACING_NET_INTERFACE.newSetting(
                        netInterface));
        Assert.assertEquals(
                netInterface,
                SocksValueDerivationHelper.getSocksOnUdpAssociateRequestClientFacingNetInterfaceFrom(settings));
    }

    @Test
    public void testGetSocksOnUdpAssociateRequestClientFacingNetInterfaceFromSettings05() throws UnknownHostException, SocketException {
        Host host = Host.newInstance("127.0.0.1");
        NetworkInterface networkInterface = NetworkInterface.getByInetAddress(
                host.toInetAddress());
        NetInterface netInterface = NetInterface.newInstance(networkInterface);
        Settings settings = Settings.of(
                SocksSettingSpecConstants.SOCKS_ON_UDP_ASSOCIATE_REQUEST_CLIENT_FACING_NET_INTERFACE.newSetting(
                        netInterface));
        Assert.assertEquals(
                netInterface,
                SocksValueDerivationHelper.getSocksOnUdpAssociateRequestClientFacingNetInterfaceFrom(settings));
    }
    
    @Test
    public void testGetSocksOnUdpAssociateRequestClientFacingSocketSettingsFromRule01() {
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
                SocksValueDerivationHelper.getSocksOnUdpAssociateRequestClientFacingSocketSettingsFrom(rule));
    }

    @Test
    public void testGetSocksOnUdpAssociateRequestClientFacingSocketSettingsFromRule02() {
        SocketSetting<?> socketSetting =
                StandardSocketSettingSpecConstants.SO_TIMEOUT.newSocketSetting(
                        NonNegativeInteger.valueOf(0));
        @SuppressWarnings("unchecked")
        SocketSetting<Object> socketSttng = (SocketSetting<Object>) socketSetting;
        SocketSettings socketSettings = SocketSettings.of(socketSttng);
        Rule rule = new Rule.Builder()
                .addRuleAction(GeneralRuleActionSpecConstants.INTERNAL_FACING_SOCKET_SETTING.newRuleAction(
                        socketSttng))
                .build();
        Assert.assertEquals(
                socketSettings,
                SocksValueDerivationHelper.getSocksOnUdpAssociateRequestClientFacingSocketSettingsFrom(rule));
    }

    @Test
    public void testGetSocksOnUdpAssociateRequestClientFacingSocketSettingsFromRule03() {
        SocketSetting<?> socketSetting =
                StandardSocketSettingSpecConstants.SO_TIMEOUT.newSocketSetting(
                        NonNegativeInteger.valueOf(0));
        @SuppressWarnings("unchecked")
        SocketSetting<Object> socketSttng = (SocketSetting<Object>) socketSetting;
        SocketSettings socketSettings = SocketSettings.of(socketSttng);
        Rule rule = new Rule.Builder()
                .addRuleAction(SocksRuleActionSpecConstants.SOCKS_ON_REQUEST_INTERNAL_FACING_SOCKET_SETTING.newRuleAction(
                        socketSttng))
                .build();
        Assert.assertEquals(
                socketSettings,
                SocksValueDerivationHelper.getSocksOnUdpAssociateRequestClientFacingSocketSettingsFrom(rule));
    }

    @Test
    public void testGetSocksOnUdpAssociateRequestClientFacingSocketSettingsFromRule04() {
        SocketSetting<?> socketSetting =
                StandardSocketSettingSpecConstants.SO_TIMEOUT.newSocketSetting(
                        NonNegativeInteger.valueOf(0));
        @SuppressWarnings("unchecked")
        SocketSetting<Object> socketSttng = (SocketSetting<Object>) socketSetting;
        SocketSettings socketSettings = SocketSettings.of(socketSttng);
        Rule rule = new Rule.Builder()
                .addRuleAction(SocksRuleActionSpecConstants.SOCKS_ON_UDP_ASSOCIATE_REQUEST_CLIENT_FACING_SOCKET_SETTING.newRuleAction(
                        socketSttng))
                .build();
        Assert.assertEquals(
                socketSettings,
                SocksValueDerivationHelper.getSocksOnUdpAssociateRequestClientFacingSocketSettingsFrom(rule));
    }

    @Test
    public void testGetSocksOnUdpAssociateRequestClientFacingSocketSettingsFromSettings01() {
        SocketSettings socketSettings = SocketSettings.of();
        Settings settings = Settings.of();
        Assert.assertEquals(
                socketSettings,
                SocksValueDerivationHelper.getSocksOnUdpAssociateRequestClientFacingSocketSettingsFrom(settings));
    }

    @Test
    public void testGetSocksOnUdpAssociateRequestClientFacingSocketSettingsFromSettings02() {
        SocketSetting<?> socketSetting =
                StandardSocketSettingSpecConstants.SO_TIMEOUT.newSocketSetting(
                        NonNegativeInteger.valueOf(0));
        SocketSettings socketSettings = SocketSettings.of(socketSetting);
        Settings settings = Settings.of(
                GeneralSettingSpecConstants.SOCKET_SETTINGS.newSetting(
                        socketSettings));
        Assert.assertEquals(
                socketSettings,
                SocksValueDerivationHelper.getSocksOnUdpAssociateRequestClientFacingSocketSettingsFrom(settings));
    }

    @Test
    public void testGetSocksOnUdpAssociateRequestClientFacingSocketSettingsFromSettings03() {
        SocketSetting<?> socketSetting =
                StandardSocketSettingSpecConstants.SO_TIMEOUT.newSocketSetting(
                        NonNegativeInteger.valueOf(0));
        SocketSettings socketSettings = SocketSettings.of(socketSetting);
        Settings settings = Settings.of(
                GeneralSettingSpecConstants.INTERNAL_FACING_SOCKET_SETTINGS.newSetting(
                        socketSettings));
        Assert.assertEquals(
                socketSettings,
                SocksValueDerivationHelper.getSocksOnUdpAssociateRequestClientFacingSocketSettingsFrom(settings));
    }

    @Test
    public void testGetSocksOnUdpAssociateRequestClientFacingSocketSettingsFromSettings04() {
        SocketSetting<?> socketSetting =
                StandardSocketSettingSpecConstants.SO_TIMEOUT.newSocketSetting(
                        NonNegativeInteger.valueOf(0));
        SocketSettings socketSettings = SocketSettings.of(socketSetting);
        Settings settings = Settings.of(
                SocksSettingSpecConstants.SOCKS_ON_REQUEST_INTERNAL_FACING_SOCKET_SETTINGS.newSetting(
                        socketSettings));
        Assert.assertEquals(
                socketSettings,
                SocksValueDerivationHelper.getSocksOnUdpAssociateRequestClientFacingSocketSettingsFrom(settings));
    }

    @Test
    public void testGetSocksOnUdpAssociateRequestClientFacingSocketSettingsFromSettings05() {
        SocketSetting<?> socketSetting =
                StandardSocketSettingSpecConstants.SO_TIMEOUT.newSocketSetting(
                        NonNegativeInteger.valueOf(0));
        SocketSettings socketSettings = SocketSettings.of(socketSetting);
        Settings settings = Settings.of(
                SocksSettingSpecConstants.SOCKS_ON_UDP_ASSOCIATE_REQUEST_CLIENT_FACING_SOCKET_SETTINGS.newSetting(
                        socketSettings));
        Assert.assertEquals(
                socketSettings,
                SocksValueDerivationHelper.getSocksOnUdpAssociateRequestClientFacingSocketSettingsFrom(settings));
    }

    @Test
    public void testGetSocksOnUdpAssociateRequestPeerFacingBindHostFromRule01() {
        Host host = Host.newInstance("127.0.0.1");
        Rule rule = new Rule.Builder()
                .addRuleAction(GeneralRuleActionSpecConstants.BIND_HOST.newRuleAction(
                        host))
                .build();
        Assert.assertEquals(
                host,
                SocksValueDerivationHelper.getSocksOnUdpAssociateRequestPeerFacingBindHostFrom(rule));
    }

    @Test
    public void testGetSocksOnUdpAssociateRequestPeerFacingBindHostFromRule02() {
        Host host = Host.newInstance("127.0.0.1");
        Rule rule = new Rule.Builder()
                .addRuleAction(GeneralRuleActionSpecConstants.EXTERNAL_FACING_BIND_HOST.newRuleAction(
                        host))
                .build();
        Assert.assertEquals(
                host,
                SocksValueDerivationHelper.getSocksOnUdpAssociateRequestPeerFacingBindHostFrom(rule));
    }

    @Test
    public void testGetSocksOnUdpAssociateRequestPeerFacingBindHostFromRule03() {
        Host host = Host.newInstance("127.0.0.1");
        Rule rule = new Rule.Builder()
                .addRuleAction(SocksRuleActionSpecConstants.SOCKS_ON_REQUEST_EXTERNAL_FACING_BIND_HOST.newRuleAction(
                        host))
                .build();
        Assert.assertEquals(
                host,
                SocksValueDerivationHelper.getSocksOnUdpAssociateRequestPeerFacingBindHostFrom(rule));
    }

    @Test
    public void testGetSocksOnUdpAssociateRequestPeerFacingBindHostFromRule04() {
        Host host = Host.newInstance("127.0.0.1");
        Rule rule = new Rule.Builder()
                .addRuleAction(SocksRuleActionSpecConstants.SOCKS_ON_UDP_ASSOCIATE_REQUEST_PEER_FACING_BIND_HOST.newRuleAction(
                        host))
                .build();
        Assert.assertEquals(
                host,
                SocksValueDerivationHelper.getSocksOnUdpAssociateRequestPeerFacingBindHostFrom(rule));
    }

    @Test
    public void testGetSocksOnUdpAssociateRequestPeerFacingBindHostFromSettings01() {
        Settings settings = Settings.of();
        Assert.assertNull(SocksValueDerivationHelper.getSocksOnUdpAssociateRequestPeerFacingBindHostFrom(settings));
    }

    @Test
    public void testGetSocksOnUdpAssociateRequestPeerFacingBindHostFromSettings02() {
        Host host = Host.newInstance("127.0.0.1");
        Settings settings = Settings.of(
                GeneralSettingSpecConstants.BIND_HOST.newSetting(host));
        Assert.assertEquals(
                host,
                SocksValueDerivationHelper.getSocksOnUdpAssociateRequestPeerFacingBindHostFrom(settings));
    }

    @Test
    public void testGetSocksOnUdpAssociateRequestPeerFacingBindHostFromSettings03() {
        Host host = Host.newInstance("127.0.0.1");
        Settings settings = Settings.of(
                GeneralSettingSpecConstants.EXTERNAL_FACING_BIND_HOST.newSetting(
                        host));
        Assert.assertEquals(
                host,
                SocksValueDerivationHelper.getSocksOnUdpAssociateRequestPeerFacingBindHostFrom(settings));
    }

    @Test
    public void testGetSocksOnUdpAssociateRequestPeerFacingBindHostFromSettings04() {
        Host host = Host.newInstance("127.0.0.1");
        Settings settings = Settings.of(
                SocksSettingSpecConstants.SOCKS_ON_REQUEST_EXTERNAL_FACING_BIND_HOST.newSetting(
                        host));
        Assert.assertEquals(
                host,
                SocksValueDerivationHelper.getSocksOnUdpAssociateRequestPeerFacingBindHostFrom(settings));
    }

    @Test
    public void testGetSocksOnUdpAssociateRequestPeerFacingBindHostFromSettings05() {
        Host host = Host.newInstance("127.0.0.1");
        Settings settings = Settings.of(
                SocksSettingSpecConstants.SOCKS_ON_UDP_ASSOCIATE_REQUEST_PEER_FACING_BIND_HOST.newSetting(
                        host));
        Assert.assertEquals(
                host,
                SocksValueDerivationHelper.getSocksOnUdpAssociateRequestPeerFacingBindHostFrom(settings));
    }

    @Test
    public void testGetSocksOnUdpAssociateRequestPeerFacingBindHostAddressTypesFromRule01() {
        HostAddressType hostAddressType = HostAddressType.HOST_IPV4_ADDRESS;
        HostAddressTypes hostAddressTypes = HostAddressTypes.of(hostAddressType);
        Rule rule = new Rule.Builder()
                .addRuleAction(GeneralRuleActionSpecConstants.BIND_HOST_ADDRESS_TYPE.newRuleAction(
                        hostAddressType))
                .build();
        Assert.assertEquals(
                hostAddressTypes,
                SocksValueDerivationHelper.getSocksOnUdpAssociateRequestPeerFacingBindHostAddressTypesFrom(rule));
    }

    @Test
    public void testGetSocksOnUdpAssociateRequestPeerFacingBindHostAddressTypesFromRule02() {
        HostAddressType hostAddressType = HostAddressType.HOST_IPV4_ADDRESS;
        HostAddressTypes hostAddressTypes = HostAddressTypes.of(hostAddressType);
        Rule rule = new Rule.Builder()
                .addRuleAction(GeneralRuleActionSpecConstants.EXTERNAL_FACING_BIND_HOST_ADDRESS_TYPE.newRuleAction(
                        hostAddressType))
                .build();
        Assert.assertEquals(
                hostAddressTypes,
                SocksValueDerivationHelper.getSocksOnUdpAssociateRequestPeerFacingBindHostAddressTypesFrom(rule));
    }

    @Test
    public void testGetSocksOnUdpAssociateRequestPeerFacingBindHostAddressTypesFromRule03() {
        HostAddressType hostAddressType = HostAddressType.HOST_IPV4_ADDRESS;
        HostAddressTypes hostAddressTypes = HostAddressTypes.of(hostAddressType);
        Rule rule = new Rule.Builder()
                .addRuleAction(SocksRuleActionSpecConstants.SOCKS_ON_REQUEST_EXTERNAL_FACING_BIND_HOST_ADDRESS_TYPE.newRuleAction(
                        hostAddressType))
                .build();
        Assert.assertEquals(
                hostAddressTypes,
                SocksValueDerivationHelper.getSocksOnUdpAssociateRequestPeerFacingBindHostAddressTypesFrom(rule));
    }

    @Test
    public void testGetSocksOnUdpAssociateRequestPeerFacingBindHostAddressTypesFromRule04() {
        HostAddressType hostAddressType = HostAddressType.HOST_IPV4_ADDRESS;
        HostAddressTypes hostAddressTypes = HostAddressTypes.of(hostAddressType);
        Rule rule = new Rule.Builder()
                .addRuleAction(SocksRuleActionSpecConstants.SOCKS_ON_UDP_ASSOCIATE_REQUEST_PEER_FACING_BIND_HOST_ADDRESS_TYPE.newRuleAction(
                        hostAddressType))
                .build();
        Assert.assertEquals(
                hostAddressTypes,
                SocksValueDerivationHelper.getSocksOnUdpAssociateRequestPeerFacingBindHostAddressTypesFrom(rule));
    }

    @Test
    public void testGetSocksOnUdpAssociateRequestPeerFacingBindHostAddressTypesFromSettings01() {
        Settings settings = Settings.of();
        Assert.assertEquals(
                HostAddressTypes.of(
                        HostAddressType.HOST_IPV4_ADDRESS,
                        HostAddressType.HOST_IPV6_ADDRESS),
                SocksValueDerivationHelper.getSocksOnUdpAssociateRequestPeerFacingBindHostAddressTypesFrom(settings));
    }

    @Test
    public void testGetSocksOnUdpAssociateRequestPeerFacingBindHostAddressTypesFromSettings02() {
        HostAddressType hostAddressType = HostAddressType.HOST_IPV4_ADDRESS;
        HostAddressTypes hostAddressTypes = HostAddressTypes.of(hostAddressType);
        Settings settings = Settings.of(
                GeneralSettingSpecConstants.BIND_HOST_ADDRESS_TYPES.newSetting(
                        hostAddressTypes));
        Assert.assertEquals(
                hostAddressTypes,
                SocksValueDerivationHelper.getSocksOnUdpAssociateRequestPeerFacingBindHostAddressTypesFrom(settings));
    }

    @Test
    public void testGetSocksOnUdpAssociateRequestPeerFacingBindHostAddressTypesFromSettings03() {
        HostAddressType hostAddressType = HostAddressType.HOST_IPV4_ADDRESS;
        HostAddressTypes hostAddressTypes = HostAddressTypes.of(hostAddressType);
        Settings settings = Settings.of(
                GeneralSettingSpecConstants.EXTERNAL_FACING_BIND_HOST_ADDRESS_TYPES.newSetting(
                        hostAddressTypes));
        Assert.assertEquals(
                hostAddressTypes,
                SocksValueDerivationHelper.getSocksOnUdpAssociateRequestPeerFacingBindHostAddressTypesFrom(settings));
    }

    @Test
    public void testGetSocksOnUdpAssociateRequestPeerFacingBindHostAddressTypesFromSettings04() {
        HostAddressType hostAddressType = HostAddressType.HOST_IPV4_ADDRESS;
        HostAddressTypes hostAddressTypes = HostAddressTypes.of(hostAddressType);
        Settings settings = Settings.of(
                SocksSettingSpecConstants.SOCKS_ON_REQUEST_EXTERNAL_FACING_BIND_HOST_ADDRESS_TYPES.newSetting(
                        hostAddressTypes));
        Assert.assertEquals(
                hostAddressTypes,
                SocksValueDerivationHelper.getSocksOnUdpAssociateRequestPeerFacingBindHostAddressTypesFrom(settings));
    }

    @Test
    public void testGetSocksOnUdpAssociateRequestPeerFacingBindHostAddressTypesFromSettings05() {
        HostAddressType hostAddressType = HostAddressType.HOST_IPV4_ADDRESS;
        HostAddressTypes hostAddressTypes = HostAddressTypes.of(hostAddressType);
        Settings settings = Settings.of(
                SocksSettingSpecConstants.SOCKS_ON_UDP_ASSOCIATE_REQUEST_PEER_FACING_BIND_HOST_ADDRESS_TYPES.newSetting(
                        hostAddressTypes));
        Assert.assertEquals(
                hostAddressTypes,
                SocksValueDerivationHelper.getSocksOnUdpAssociateRequestPeerFacingBindHostAddressTypesFrom(settings));
    }
    
    @Test
    public void testGetSocksOnUdpAssociateRequestPeerFacingBindPortRangesFromRule01() {
        PortRange portRange = PortRange.of(
                Port.valueOf(1), Port.valueOf(65535));
        PortRanges portRanges = PortRanges.of(portRange);
        Rule rule = new Rule.Builder()
                .addRuleAction(GeneralRuleActionSpecConstants.BIND_UDP_PORT_RANGE.newRuleAction(
                        portRange))
                .build();
        Assert.assertEquals(
                portRanges,
                SocksValueDerivationHelper.getSocksOnUdpAssociateRequestPeerFacingBindPortRangesFrom(rule));
    }

    @Test
    public void testGetSocksOnUdpAssociateRequestPeerFacingBindPortRangesFromRule02() {
        PortRange portRange = PortRange.of(
                Port.valueOf(1), Port.valueOf(65535));
        PortRanges portRanges = PortRanges.of(portRange);
        Rule rule = new Rule.Builder()
                .addRuleAction(GeneralRuleActionSpecConstants.EXTERNAL_FACING_BIND_UDP_PORT_RANGE.newRuleAction(
                        portRange))
                .build();
        Assert.assertEquals(
                portRanges,
                SocksValueDerivationHelper.getSocksOnUdpAssociateRequestPeerFacingBindPortRangesFrom(rule));
    }

    @Test
    public void testGetSocksOnUdpAssociateRequestPeerFacingBindPortRangesFromRule03() {
        PortRange portRange = PortRange.of(
                Port.valueOf(1), Port.valueOf(65535));
        PortRanges portRanges = PortRanges.of(portRange);
        Rule rule = new Rule.Builder()
                .addRuleAction(SocksRuleActionSpecConstants.SOCKS_ON_REQUEST_EXTERNAL_FACING_BIND_UDP_PORT_RANGE.newRuleAction(
                        portRange))
                .build();
        Assert.assertEquals(
                portRanges,
                SocksValueDerivationHelper.getSocksOnUdpAssociateRequestPeerFacingBindPortRangesFrom(rule));
    }

    @Test
    public void testGetSocksOnUdpAssociateRequestPeerFacingBindPortRangesFromRule04() {
        PortRange portRange = PortRange.of(
                Port.valueOf(1), Port.valueOf(65535));
        PortRanges portRanges = PortRanges.of(portRange);
        Rule rule = new Rule.Builder()
                .addRuleAction(SocksRuleActionSpecConstants.SOCKS_ON_UDP_ASSOCIATE_REQUEST_PEER_FACING_BIND_PORT_RANGE.newRuleAction(
                        portRange))
                .build();
        Assert.assertEquals(
                portRanges,
                SocksValueDerivationHelper.getSocksOnUdpAssociateRequestPeerFacingBindPortRangesFrom(rule));
    }

    @Test
    public void testGetSocksOnUdpAssociateRequestPeerFacingBindPortRangesFromSettings01() {
        PortRanges portRanges = PortRanges.getDefault();
        Settings settings = Settings.of();
        Assert.assertEquals(
                portRanges,
                SocksValueDerivationHelper.getSocksOnUdpAssociateRequestPeerFacingBindPortRangesFrom(settings));
    }

    @Test
    public void testGetSocksOnUdpAssociateRequestPeerFacingBindPortRangesFromSettings02() {
        PortRange portRange = PortRange.of(
                Port.valueOf(1), Port.valueOf(65535));
        PortRanges portRanges = PortRanges.of(portRange);
        Settings settings = Settings.of(
                GeneralSettingSpecConstants.BIND_UDP_PORT_RANGES.newSetting(
                        portRanges));
        Assert.assertEquals(
                portRanges,
                SocksValueDerivationHelper.getSocksOnUdpAssociateRequestPeerFacingBindPortRangesFrom(settings));
    }

    @Test
    public void testGetSocksOnUdpAssociateRequestPeerFacingBindPortRangesFromSettings03() {
        PortRange portRange = PortRange.of(
                Port.valueOf(1), Port.valueOf(65535));
        PortRanges portRanges = PortRanges.of(portRange);
        Settings settings = Settings.of(
                GeneralSettingSpecConstants.EXTERNAL_FACING_BIND_UDP_PORT_RANGES.newSetting(
                        portRanges));
        Assert.assertEquals(
                portRanges,
                SocksValueDerivationHelper.getSocksOnUdpAssociateRequestPeerFacingBindPortRangesFrom(settings));
    }

    @Test
    public void testGetSocksOnUdpAssociateRequestPeerFacingBindPortRangesFromSettings04() {
        PortRange portRange = PortRange.of(
                Port.valueOf(1), Port.valueOf(65535));
        PortRanges portRanges = PortRanges.of(portRange);
        Settings settings = Settings.of(
                SocksSettingSpecConstants.SOCKS_ON_REQUEST_EXTERNAL_FACING_BIND_UDP_PORT_RANGES.newSetting(
                        portRanges));
        Assert.assertEquals(
                portRanges,
                SocksValueDerivationHelper.getSocksOnUdpAssociateRequestPeerFacingBindPortRangesFrom(settings));
    }

    @Test
    public void testGetSocksOnUdpAssociateRequestPeerFacingBindPortRangesFromSettings05() {
        PortRange portRange = PortRange.of(
                Port.valueOf(1), Port.valueOf(65535));
        PortRanges portRanges = PortRanges.of(portRange);
        Settings settings = Settings.of(
                SocksSettingSpecConstants.SOCKS_ON_UDP_ASSOCIATE_REQUEST_PEER_FACING_BIND_PORT_RANGES.newSetting(
                        portRanges));
        Assert.assertEquals(
                portRanges,
                SocksValueDerivationHelper.getSocksOnUdpAssociateRequestPeerFacingBindPortRangesFrom(settings));
    }

    @Test
    public void testGetSocksOnUdpAssociateRequestPeerFacingNetInterfaceFromRule01() throws UnknownHostException, SocketException {
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
                SocksValueDerivationHelper.getSocksOnUdpAssociateRequestPeerFacingNetInterfaceFrom(rule));
    }

    @Test
    public void testGetSocksOnUdpAssociateRequestPeerFacingNetInterfaceFromRule02() throws UnknownHostException, SocketException {
        Host host = Host.newInstance("127.0.0.1");
        NetworkInterface networkInterface = NetworkInterface.getByInetAddress(
                host.toInetAddress());
        NetInterface netInterface = NetInterface.newInstance(networkInterface);
        Rule rule = new Rule.Builder()
                .addRuleAction(GeneralRuleActionSpecConstants.EXTERNAL_FACING_NET_INTERFACE.newRuleAction(
                        netInterface))
                .build();
        Assert.assertEquals(
                netInterface,
                SocksValueDerivationHelper.getSocksOnUdpAssociateRequestPeerFacingNetInterfaceFrom(rule));
    }

    @Test
    public void testGetSocksOnUdpAssociateRequestPeerFacingNetInterfaceFromRule03() throws UnknownHostException, SocketException {
        Host host = Host.newInstance("127.0.0.1");
        NetworkInterface networkInterface = NetworkInterface.getByInetAddress(
                host.toInetAddress());
        NetInterface netInterface = NetInterface.newInstance(networkInterface);
        Rule rule = new Rule.Builder()
                .addRuleAction(SocksRuleActionSpecConstants.SOCKS_ON_REQUEST_EXTERNAL_FACING_NET_INTERFACE.newRuleAction(
                        netInterface))
                .build();
        Assert.assertEquals(
                netInterface,
                SocksValueDerivationHelper.getSocksOnUdpAssociateRequestPeerFacingNetInterfaceFrom(rule));
    }

    @Test
    public void testGetSocksOnUdpAssociateRequestPeerFacingNetInterfaceFromRule04() throws UnknownHostException, SocketException {
        Host host = Host.newInstance("127.0.0.1");
        NetworkInterface networkInterface = NetworkInterface.getByInetAddress(
                host.toInetAddress());
        NetInterface netInterface = NetInterface.newInstance(networkInterface);
        Rule rule = new Rule.Builder()
                .addRuleAction(SocksRuleActionSpecConstants.SOCKS_ON_UDP_ASSOCIATE_REQUEST_PEER_FACING_NET_INTERFACE.newRuleAction(
                        netInterface))
                .build();
        Assert.assertEquals(
                netInterface,
                SocksValueDerivationHelper.getSocksOnUdpAssociateRequestPeerFacingNetInterfaceFrom(rule));
    }

    @Test
    public void testGetSocksOnUdpAssociateRequestPeerFacingNetInterfaceFromSettings01() {
        Settings settings = Settings.of();
        Assert.assertNull(SocksValueDerivationHelper.getSocksOnUdpAssociateRequestPeerFacingNetInterfaceFrom(settings));
    }

    @Test
    public void testGetSocksOnUdpAssociateRequestPeerFacingNetInterfaceFromSettings02() throws UnknownHostException, SocketException {
        Host host = Host.newInstance("127.0.0.1");
        NetworkInterface networkInterface = NetworkInterface.getByInetAddress(
                host.toInetAddress());
        NetInterface netInterface = NetInterface.newInstance(networkInterface);
        Settings settings = Settings.of(
                GeneralSettingSpecConstants.NET_INTERFACE.newSetting(
                        netInterface));
        Assert.assertEquals(
                netInterface,
                SocksValueDerivationHelper.getSocksOnUdpAssociateRequestPeerFacingNetInterfaceFrom(settings));
    }

    @Test
    public void testGetSocksOnUdpAssociateRequestPeerFacingNetInterfaceFromSettings03() throws UnknownHostException, SocketException {
        Host host = Host.newInstance("127.0.0.1");
        NetworkInterface networkInterface = NetworkInterface.getByInetAddress(
                host.toInetAddress());
        NetInterface netInterface = NetInterface.newInstance(networkInterface);
        Settings settings = Settings.of(
                GeneralSettingSpecConstants.EXTERNAL_FACING_NET_INTERFACE.newSetting(
                        netInterface));
        Assert.assertEquals(
                netInterface,
                SocksValueDerivationHelper.getSocksOnUdpAssociateRequestPeerFacingNetInterfaceFrom(settings));
    }

    @Test
    public void testGetSocksOnUdpAssociateRequestPeerFacingNetInterfaceFromSettings04() throws UnknownHostException, SocketException {
        Host host = Host.newInstance("127.0.0.1");
        NetworkInterface networkInterface = NetworkInterface.getByInetAddress(
                host.toInetAddress());
        NetInterface netInterface = NetInterface.newInstance(networkInterface);
        Settings settings = Settings.of(
                SocksSettingSpecConstants.SOCKS_ON_REQUEST_EXTERNAL_FACING_NET_INTERFACE.newSetting(
                        netInterface));
        Assert.assertEquals(
                netInterface,
                SocksValueDerivationHelper.getSocksOnUdpAssociateRequestPeerFacingNetInterfaceFrom(settings));
    }

    @Test
    public void testGetSocksOnUdpAssociateRequestPeerFacingNetInterfaceFromSettings05() throws UnknownHostException, SocketException {
        Host host = Host.newInstance("127.0.0.1");
        NetworkInterface networkInterface = NetworkInterface.getByInetAddress(
                host.toInetAddress());
        NetInterface netInterface = NetInterface.newInstance(networkInterface);
        Settings settings = Settings.of(
                SocksSettingSpecConstants.SOCKS_ON_UDP_ASSOCIATE_REQUEST_PEER_FACING_NET_INTERFACE.newSetting(
                        netInterface));
        Assert.assertEquals(
                netInterface,
                SocksValueDerivationHelper.getSocksOnUdpAssociateRequestPeerFacingNetInterfaceFrom(settings));
    }
    
    @Test
    public void testGetSocksOnUdpAssociateRequestPeerFacingSocketSettingsFromRule01() {
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
                SocksValueDerivationHelper.getSocksOnUdpAssociateRequestPeerFacingSocketSettingsFrom(rule));
    }

    @Test
    public void testGetSocksOnUdpAssociateRequestPeerFacingSocketSettingsFromRule02() {
        SocketSetting<?> socketSetting =
                StandardSocketSettingSpecConstants.SO_TIMEOUT.newSocketSetting(
                        NonNegativeInteger.valueOf(0));
        @SuppressWarnings("unchecked")
        SocketSetting<Object> socketSttng = (SocketSetting<Object>) socketSetting;
        SocketSettings socketSettings = SocketSettings.of(socketSttng);
        Rule rule = new Rule.Builder()
                .addRuleAction(GeneralRuleActionSpecConstants.EXTERNAL_FACING_SOCKET_SETTING.newRuleAction(
                        socketSttng))
                .build();
        Assert.assertEquals(
                socketSettings,
                SocksValueDerivationHelper.getSocksOnUdpAssociateRequestPeerFacingSocketSettingsFrom(rule));
    }

    @Test
    public void testGetSocksOnUdpAssociateRequestPeerFacingSocketSettingsFromRule03() {
        SocketSetting<?> socketSetting =
                StandardSocketSettingSpecConstants.SO_TIMEOUT.newSocketSetting(
                        NonNegativeInteger.valueOf(0));
        @SuppressWarnings("unchecked")
        SocketSetting<Object> socketSttng = (SocketSetting<Object>) socketSetting;
        SocketSettings socketSettings = SocketSettings.of(socketSttng);
        Rule rule = new Rule.Builder()
                .addRuleAction(SocksRuleActionSpecConstants.SOCKS_ON_REQUEST_EXTERNAL_FACING_SOCKET_SETTING.newRuleAction(
                        socketSttng))
                .build();
        Assert.assertEquals(
                socketSettings,
                SocksValueDerivationHelper.getSocksOnUdpAssociateRequestPeerFacingSocketSettingsFrom(rule));
    }

    @Test
    public void testGetSocksOnUdpAssociateRequestPeerFacingSocketSettingsFromRule04() {
        SocketSetting<?> socketSetting =
                StandardSocketSettingSpecConstants.SO_TIMEOUT.newSocketSetting(
                        NonNegativeInteger.valueOf(0));
        @SuppressWarnings("unchecked")
        SocketSetting<Object> socketSttng = (SocketSetting<Object>) socketSetting;
        SocketSettings socketSettings = SocketSettings.of(socketSttng);
        Rule rule = new Rule.Builder()
                .addRuleAction(SocksRuleActionSpecConstants.SOCKS_ON_UDP_ASSOCIATE_REQUEST_PEER_FACING_SOCKET_SETTING.newRuleAction(
                        socketSttng))
                .build();
        Assert.assertEquals(
                socketSettings,
                SocksValueDerivationHelper.getSocksOnUdpAssociateRequestPeerFacingSocketSettingsFrom(rule));
    }

    @Test
    public void testGetSocksOnUdpAssociateRequestPeerFacingSocketSettingsFromSettings01() {
        SocketSettings socketSettings = SocketSettings.of();
        Settings settings = Settings.of();
        Assert.assertEquals(
                socketSettings,
                SocksValueDerivationHelper.getSocksOnUdpAssociateRequestPeerFacingSocketSettingsFrom(settings));
    }

    @Test
    public void testGetSocksOnUdpAssociateRequestPeerFacingSocketSettingsFromSettings02() {
        SocketSetting<?> socketSetting =
                StandardSocketSettingSpecConstants.SO_TIMEOUT.newSocketSetting(
                        NonNegativeInteger.valueOf(0));
        SocketSettings socketSettings = SocketSettings.of(socketSetting);
        Settings settings = Settings.of(
                GeneralSettingSpecConstants.SOCKET_SETTINGS.newSetting(
                        socketSettings));
        Assert.assertEquals(
                socketSettings,
                SocksValueDerivationHelper.getSocksOnUdpAssociateRequestPeerFacingSocketSettingsFrom(settings));
    }

    @Test
    public void testGetSocksOnUdpAssociateRequestPeerFacingSocketSettingsFromSettings03() {
        SocketSetting<?> socketSetting =
                StandardSocketSettingSpecConstants.SO_TIMEOUT.newSocketSetting(
                        NonNegativeInteger.valueOf(0));
        SocketSettings socketSettings = SocketSettings.of(socketSetting);
        Settings settings = Settings.of(
                GeneralSettingSpecConstants.EXTERNAL_FACING_SOCKET_SETTINGS.newSetting(
                        socketSettings));
        Assert.assertEquals(
                socketSettings,
                SocksValueDerivationHelper.getSocksOnUdpAssociateRequestPeerFacingSocketSettingsFrom(settings));
    }

    @Test
    public void testGetSocksOnUdpAssociateRequestPeerFacingSocketSettingsFromSettings04() {
        SocketSetting<?> socketSetting =
                StandardSocketSettingSpecConstants.SO_TIMEOUT.newSocketSetting(
                        NonNegativeInteger.valueOf(0));
        SocketSettings socketSettings = SocketSettings.of(socketSetting);
        Settings settings = Settings.of(
                SocksSettingSpecConstants.SOCKS_ON_REQUEST_EXTERNAL_FACING_SOCKET_SETTINGS.newSetting(
                        socketSettings));
        Assert.assertEquals(
                socketSettings,
                SocksValueDerivationHelper.getSocksOnUdpAssociateRequestPeerFacingSocketSettingsFrom(settings));
    }

    @Test
    public void testGetSocksOnUdpAssociateRequestPeerFacingSocketSettingsFromSettings05() {
        SocketSetting<?> socketSetting =
                StandardSocketSettingSpecConstants.SO_TIMEOUT.newSocketSetting(
                        NonNegativeInteger.valueOf(0));
        SocketSettings socketSettings = SocketSettings.of(socketSetting);
        Settings settings = Settings.of(
                SocksSettingSpecConstants.SOCKS_ON_UDP_ASSOCIATE_REQUEST_PEER_FACING_SOCKET_SETTINGS.newSetting(
                        socketSettings));
        Assert.assertEquals(
                socketSettings,
                SocksValueDerivationHelper.getSocksOnUdpAssociateRequestPeerFacingSocketSettingsFrom(settings));
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
