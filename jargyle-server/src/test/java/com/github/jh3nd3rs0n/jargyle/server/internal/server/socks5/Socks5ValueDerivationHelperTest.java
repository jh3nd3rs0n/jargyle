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
    public void testGetSocks5OnBindRequestInboundSocketSettingsFromRuleSettings01() {
        SocketSettings socketSettings = SocketSettings.of();
        Rule rule = new Rule.Builder().build();
        Settings settings = Settings.of();
        Assert.assertEquals(
                socketSettings,
                Socks5ValueDerivationHelper.getSocks5OnBindRequestInboundSocketSettingsFrom(rule, settings));
    }

    @Test
    public void testGetSocks5OnBindRequestInboundSocketSettingsFromRuleSettings02() {
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
                Socks5ValueDerivationHelper.getSocks5OnBindRequestInboundSocketSettingsFrom(rule, settings));
    }

    @Test
    public void testGetSocks5OnBindRequestInboundSocketSettingsFromRuleSettings03() {
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
        Settings settings = Settings.of();
        Assert.assertEquals(
                socketSettings,
                Socks5ValueDerivationHelper.getSocks5OnBindRequestInboundSocketSettingsFrom(rule, settings));
    }

    @Test
    public void testGetSocks5OnBindRequestInboundSocketSettingsFromRuleSettings04() {
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
        Settings settings = Settings.of();
        Assert.assertEquals(
                socketSettings,
                Socks5ValueDerivationHelper.getSocks5OnBindRequestInboundSocketSettingsFrom(rule, settings));
    }

    @Test
    public void testGetSocks5OnBindRequestInboundSocketSettingsFromRuleSettings05() {
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
        Settings settings = Settings.of();
        Assert.assertEquals(
                socketSettings,
                Socks5ValueDerivationHelper.getSocks5OnBindRequestInboundSocketSettingsFrom(rule, settings));
    }

    @Test
    public void testGetSocks5OnBindRequestInboundSocketSettingsFromRuleSettings06() {
        SocketSetting<?> socketSetting =
                StandardSocketSettingSpecConstants.SO_TIMEOUT.newSocketSetting(
                        NonNegativeInteger.valueOf(0));
        @SuppressWarnings("unchecked")
        SocketSetting<Object> socketSttng = (SocketSetting<Object>) socketSetting;
        SocketSettings socketSettings = SocketSettings.of(socketSttng);
        Rule rule = new Rule.Builder()
                .addRuleAction(Socks5RuleActionSpecConstants.SOCKS5_ON_BIND_REQUEST_INBOUND_SOCKET_SETTING.newRuleAction(
                        socketSttng))
                .build();
        Settings settings = Settings.of();
        Assert.assertEquals(
                socketSettings,
                Socks5ValueDerivationHelper.getSocks5OnBindRequestInboundSocketSettingsFrom(rule, settings));
    }

    @Test
    public void testGetSocks5OnBindRequestInboundSocketSettingsFromRuleSettings07() {
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
                Socks5ValueDerivationHelper.getSocks5OnBindRequestInboundSocketSettingsFrom(rule, settings));
    }

    @Test
    public void testGetSocks5OnBindRequestInboundSocketSettingsFromRuleSettings08() {
        SocketSetting<?> socketSetting =
                StandardSocketSettingSpecConstants.SO_TIMEOUT.newSocketSetting(
                        NonNegativeInteger.valueOf(0));
        SocketSettings socketSettings = SocketSettings.of(socketSetting);
        Rule rule = new Rule.Builder().build();
        Settings settings = Settings.of(
                GeneralSettingSpecConstants.EXTERNAL_FACING_SOCKET_SETTINGS.newSetting(
                        socketSettings));
        Assert.assertEquals(
                socketSettings,
                Socks5ValueDerivationHelper.getSocks5OnBindRequestInboundSocketSettingsFrom(rule, settings));
    }

    @Test
    public void testGetSocks5OnBindRequestInboundSocketSettingsFromRuleSettings09() {
        SocketSetting<?> socketSetting =
                StandardSocketSettingSpecConstants.SO_TIMEOUT.newSocketSetting(
                        NonNegativeInteger.valueOf(0));
        SocketSettings socketSettings = SocketSettings.of(socketSetting);
        Rule rule = new Rule.Builder().build();
        Settings settings = Settings.of(
                SocksSettingSpecConstants.SOCKS_ON_REQUEST_EXTERNAL_FACING_SOCKET_SETTINGS.newSetting(
                        socketSettings));
        Assert.assertEquals(
                socketSettings,
                Socks5ValueDerivationHelper.getSocks5OnBindRequestInboundSocketSettingsFrom(rule, settings));
    }

    @Test
    public void testGetSocks5OnBindRequestInboundSocketSettingsFromRuleSettings10() {
        SocketSetting<?> socketSetting =
                StandardSocketSettingSpecConstants.SO_TIMEOUT.newSocketSetting(
                        NonNegativeInteger.valueOf(0));
        SocketSettings socketSettings = SocketSettings.of(socketSetting);
        Rule rule = new Rule.Builder().build();
        Settings settings = Settings.of(
                SocksSettingSpecConstants.SOCKS_ON_BIND_REQUEST_INBOUND_SOCKET_SETTINGS.newSetting(
                        socketSettings));
        Assert.assertEquals(
                socketSettings,
                Socks5ValueDerivationHelper.getSocks5OnBindRequestInboundSocketSettingsFrom(rule, settings));
    }

    @Test
    public void testGetSocks5OnBindRequestInboundSocketSettingsFromRuleSettings11() {
        SocketSetting<?> socketSetting =
                StandardSocketSettingSpecConstants.SO_TIMEOUT.newSocketSetting(
                        NonNegativeInteger.valueOf(0));
        SocketSettings socketSettings = SocketSettings.of(socketSetting);
        Rule rule = new Rule.Builder().build();
        Settings settings = Settings.of(
                Socks5SettingSpecConstants.SOCKS5_ON_BIND_REQUEST_INBOUND_SOCKET_SETTINGS.newSetting(
                        socketSettings));
        Assert.assertEquals(
                socketSettings,
                Socks5ValueDerivationHelper.getSocks5OnBindRequestInboundSocketSettingsFrom(rule, settings));
    }

    @Test
    public void testGetSocks5OnBindRequestListenBindHostFromRuleSettings01() {
        Host host = HostIpv4Address.getAllZerosInstance();
        Rule rule = new Rule.Builder().build();
        Settings settings = Settings.of();
        Assert.assertEquals(
                host,
                Socks5ValueDerivationHelper.getSocks5OnBindRequestListenBindHostFrom(rule, settings));
    }

    @Test
    public void testGetSocks5OnBindRequestListenBindHostFromRuleSettings02() {
        Host host = Host.newInstance("127.0.0.1");
        Rule rule = new Rule.Builder()
                .addRuleAction(GeneralRuleActionSpecConstants.BIND_HOST.newRuleAction(
                        host))
                .build();
        Settings settings = Settings.of();
        Assert.assertEquals(
                host,
                Socks5ValueDerivationHelper.getSocks5OnBindRequestListenBindHostFrom(rule, settings));
    }

    @Test
    public void testGetSocks5OnBindRequestListenBindHostFromRuleSettings03() {
        Host host = Host.newInstance("127.0.0.1");
        Rule rule = new Rule.Builder()
                .addRuleAction(GeneralRuleActionSpecConstants.EXTERNAL_FACING_BIND_HOST.newRuleAction(
                        host))
                .build();
        Settings settings = Settings.of();
        Assert.assertEquals(
                host,
                Socks5ValueDerivationHelper.getSocks5OnBindRequestListenBindHostFrom(rule, settings));
    }

    @Test
    public void testGetSocks5OnBindRequestListenBindHostFromRuleSettings04() {
        Host host = Host.newInstance("127.0.0.1");
        Rule rule = new Rule.Builder()
                .addRuleAction(SocksRuleActionSpecConstants.SOCKS_ON_REQUEST_EXTERNAL_FACING_BIND_HOST.newRuleAction(
                        host))
                .build();
        Settings settings = Settings.of();
        Assert.assertEquals(
                host,
                Socks5ValueDerivationHelper.getSocks5OnBindRequestListenBindHostFrom(rule, settings));
    }

    @Test
    public void testGetSocks5OnBindRequestListenBindHostFromRuleSettings05() {
        Host host = Host.newInstance("127.0.0.1");
        Rule rule = new Rule.Builder()
                .addRuleAction(SocksRuleActionSpecConstants.SOCKS_ON_BIND_REQUEST_LISTEN_BIND_HOST.newRuleAction(
                        host))
                .build();
        Settings settings = Settings.of();
        Assert.assertEquals(
                host,
                Socks5ValueDerivationHelper.getSocks5OnBindRequestListenBindHostFrom(rule, settings));
    }

    @Test
    public void testGetSocks5OnBindRequestListenBindHostFromRuleSettings06() {
        Host host = Host.newInstance("127.0.0.1");
        Rule rule = new Rule.Builder()
                .addRuleAction(Socks5RuleActionSpecConstants.SOCKS5_ON_BIND_REQUEST_LISTEN_BIND_HOST.newRuleAction(
                        host))
                .build();
        Settings settings = Settings.of();
        Assert.assertEquals(
                host,
                Socks5ValueDerivationHelper.getSocks5OnBindRequestListenBindHostFrom(rule, settings));
    }

    @Test
    public void testGetSocks5OnBindRequestListenBindHostFromRuleSettings07() throws UnknownHostException, SocketException {
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
        Assert.assertEquals(
                host,
                Socks5ValueDerivationHelper.getSocks5OnBindRequestListenBindHostFrom(rule, settings));
    }

    @Test
    public void testGetSocks5OnBindRequestListenBindHostFromRuleSettings08() throws UnknownHostException, SocketException {
        Host host = Host.newInstance("127.0.0.1");
        NetworkInterface networkInterface = NetworkInterface.getByInetAddress(
                host.toInetAddress());
        NetInterface netInterface = NetInterface.newInstance(networkInterface);
        Rule rule = new Rule.Builder()
                .addRuleAction(GeneralRuleActionSpecConstants.EXTERNAL_FACING_NET_INTERFACE.newRuleAction(
                        netInterface))
                .addRuleAction(GeneralRuleActionSpecConstants.EXTERNAL_FACING_BIND_HOST_ADDRESS_TYPE.newRuleAction(
                        HostAddressType.HOST_IPV4_ADDRESS))
                .build();
        Settings settings = Settings.of();
        Assert.assertEquals(
                host,
                Socks5ValueDerivationHelper.getSocks5OnBindRequestListenBindHostFrom(rule, settings));
    }

    @Test
    public void testGetSocks5OnBindRequestListenBindHostFromRuleSettings09() throws UnknownHostException, SocketException {
        Host host = Host.newInstance("127.0.0.1");
        NetworkInterface networkInterface = NetworkInterface.getByInetAddress(
                host.toInetAddress());
        NetInterface netInterface = NetInterface.newInstance(networkInterface);
        Rule rule = new Rule.Builder()
                .addRuleAction(SocksRuleActionSpecConstants.SOCKS_ON_REQUEST_EXTERNAL_FACING_NET_INTERFACE.newRuleAction(
                        netInterface))
                .addRuleAction(SocksRuleActionSpecConstants.SOCKS_ON_REQUEST_EXTERNAL_FACING_BIND_HOST_ADDRESS_TYPE.newRuleAction(
                        HostAddressType.HOST_IPV4_ADDRESS))
                .build();
        Settings settings = Settings.of();
        Assert.assertEquals(
                host,
                Socks5ValueDerivationHelper.getSocks5OnBindRequestListenBindHostFrom(rule, settings));
    }

    @Test
    public void testGetSocks5OnBindRequestListenBindHostFromRuleSettings10() throws UnknownHostException, SocketException {
        Host host = Host.newInstance("127.0.0.1");
        NetworkInterface networkInterface = NetworkInterface.getByInetAddress(
                host.toInetAddress());
        NetInterface netInterface = NetInterface.newInstance(networkInterface);
        Rule rule = new Rule.Builder()
                .addRuleAction(SocksRuleActionSpecConstants.SOCKS_ON_BIND_REQUEST_LISTEN_NET_INTERFACE.newRuleAction(
                        netInterface))
                .addRuleAction(SocksRuleActionSpecConstants.SOCKS_ON_BIND_REQUEST_LISTEN_BIND_HOST_ADDRESS_TYPE.newRuleAction(
                        HostAddressType.HOST_IPV4_ADDRESS))
                .build();
        Settings settings = Settings.of();
        Assert.assertEquals(
                host,
                Socks5ValueDerivationHelper.getSocks5OnBindRequestListenBindHostFrom(rule, settings));
    }

    @Test
    public void testGetSocks5OnBindRequestListenBindHostFromRuleSettings11() throws UnknownHostException, SocketException {
        Host host = Host.newInstance("127.0.0.1");
        NetworkInterface networkInterface = NetworkInterface.getByInetAddress(
                host.toInetAddress());
        NetInterface netInterface = NetInterface.newInstance(networkInterface);
        Rule rule = new Rule.Builder()
                .addRuleAction(Socks5RuleActionSpecConstants.SOCKS5_ON_BIND_REQUEST_LISTEN_NET_INTERFACE.newRuleAction(
                        netInterface))
                .addRuleAction(Socks5RuleActionSpecConstants.SOCKS5_ON_BIND_REQUEST_LISTEN_BIND_HOST_ADDRESS_TYPE.newRuleAction(
                        HostAddressType.HOST_IPV4_ADDRESS))
                .build();
        Settings settings = Settings.of();
        Assert.assertEquals(
                host,
                Socks5ValueDerivationHelper.getSocks5OnBindRequestListenBindHostFrom(rule, settings));
    }

    @Test
    public void testGetSocks5OnBindRequestListenBindHostFromRuleSettings12() {
        Host host = Host.newInstance("127.0.0.1");
        Rule rule = new Rule.Builder().build();
        Settings settings = Settings.of(
                GeneralSettingSpecConstants.BIND_HOST.newSetting(host));
        Assert.assertEquals(
                host,
                Socks5ValueDerivationHelper.getSocks5OnBindRequestListenBindHostFrom(rule, settings));
    }

    @Test
    public void testGetSocks5OnBindRequestListenBindHostFromRuleSettings13() {
        Host host = Host.newInstance("127.0.0.1");
        Rule rule = new Rule.Builder().build();
        Settings settings = Settings.of(
                GeneralSettingSpecConstants.EXTERNAL_FACING_BIND_HOST.newSetting(
                        host));
        Assert.assertEquals(
                host,
                Socks5ValueDerivationHelper.getSocks5OnBindRequestListenBindHostFrom(rule, settings));
    }

    @Test
    public void testGetSocks5OnBindRequestListenBindHostFromRuleSettings14() {
        Host host = Host.newInstance("127.0.0.1");
        Rule rule = new Rule.Builder().build();
        Settings settings = Settings.of(
                SocksSettingSpecConstants.SOCKS_ON_REQUEST_EXTERNAL_FACING_BIND_HOST.newSetting(
                        host));
        Assert.assertEquals(
                host,
                Socks5ValueDerivationHelper.getSocks5OnBindRequestListenBindHostFrom(rule, settings));
    }

    @Test
    public void testGetSocks5OnBindRequestListenBindHostFromRuleSettings15() {
        Host host = Host.newInstance("127.0.0.1");
        Rule rule = new Rule.Builder().build();
        Settings settings = Settings.of(
                SocksSettingSpecConstants.SOCKS_ON_BIND_REQUEST_LISTEN_BIND_HOST.newSetting(
                        host));
        Assert.assertEquals(
                host,
                Socks5ValueDerivationHelper.getSocks5OnBindRequestListenBindHostFrom(rule, settings));
    }

    @Test
    public void testGetSocks5OnBindRequestListenBindHostFromRuleSettings16() {
        Host host = Host.newInstance("127.0.0.1");
        Rule rule = new Rule.Builder().build();
        Settings settings = Settings.of(
                Socks5SettingSpecConstants.SOCKS5_ON_BIND_REQUEST_LISTEN_BIND_HOST.newSetting(
                        host));
        Assert.assertEquals(
                host,
                Socks5ValueDerivationHelper.getSocks5OnBindRequestListenBindHostFrom(rule, settings));
    }

    @Test
    public void testGetSocks5OnBindRequestListenBindHostFromRuleSettings17() throws UnknownHostException, SocketException {
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
        Assert.assertEquals(
                host,
                Socks5ValueDerivationHelper.getSocks5OnBindRequestListenBindHostFrom(rule, settings));
    }

    @Test
    public void testGetSocks5OnBindRequestListenBindHostFromRuleSettings18() throws UnknownHostException, SocketException {
        Host host = Host.newInstance("127.0.0.1");
        NetworkInterface networkInterface = NetworkInterface.getByInetAddress(
                host.toInetAddress());
        NetInterface netInterface = NetInterface.newInstance(networkInterface);
        Rule rule = new Rule.Builder().build();
        Settings settings = Settings.of(
                GeneralSettingSpecConstants.EXTERNAL_FACING_NET_INTERFACE.newSetting(
                        netInterface),
                GeneralSettingSpecConstants.EXTERNAL_FACING_BIND_HOST_ADDRESS_TYPES.newSetting(HostAddressTypes.of(
                        HostAddressType.HOST_IPV4_ADDRESS)));
        Assert.assertEquals(
                host,
                Socks5ValueDerivationHelper.getSocks5OnBindRequestListenBindHostFrom(rule, settings));
    }

    @Test
    public void testGetSocks5OnBindRequestListenBindHostFromRuleSettings19() throws UnknownHostException, SocketException {
        Host host = Host.newInstance("127.0.0.1");
        NetworkInterface networkInterface = NetworkInterface.getByInetAddress(
                host.toInetAddress());
        NetInterface netInterface = NetInterface.newInstance(networkInterface);
        Rule rule = new Rule.Builder().build();
        Settings settings = Settings.of(
                SocksSettingSpecConstants.SOCKS_ON_REQUEST_EXTERNAL_FACING_NET_INTERFACE.newSetting(
                        netInterface),
                SocksSettingSpecConstants.SOCKS_ON_REQUEST_EXTERNAL_FACING_BIND_HOST_ADDRESS_TYPES.newSetting(HostAddressTypes.of(
                        HostAddressType.HOST_IPV4_ADDRESS)));
        Assert.assertEquals(
                host,
                Socks5ValueDerivationHelper.getSocks5OnBindRequestListenBindHostFrom(rule, settings));
    }

    @Test
    public void testGetSocks5OnBindRequestListenBindHostFromRuleSettings20() throws UnknownHostException, SocketException {
        Host host = Host.newInstance("127.0.0.1");
        NetworkInterface networkInterface = NetworkInterface.getByInetAddress(
                host.toInetAddress());
        NetInterface netInterface = NetInterface.newInstance(networkInterface);
        Rule rule = new Rule.Builder().build();
        Settings settings = Settings.of(
                SocksSettingSpecConstants.SOCKS_ON_BIND_REQUEST_LISTEN_NET_INTERFACE.newSetting(
                        netInterface),
                SocksSettingSpecConstants.SOCKS_ON_BIND_REQUEST_LISTEN_BIND_HOST_ADDRESS_TYPES.newSetting(HostAddressTypes.of(
                        HostAddressType.HOST_IPV4_ADDRESS)));
        Assert.assertEquals(
                host,
                Socks5ValueDerivationHelper.getSocks5OnBindRequestListenBindHostFrom(rule, settings));
    }

    @Test
    public void testGetSocks5OnBindRequestListenBindHostFromRuleSettings21() throws UnknownHostException, SocketException {
        Host host = Host.newInstance("127.0.0.1");
        NetworkInterface networkInterface = NetworkInterface.getByInetAddress(
                host.toInetAddress());
        NetInterface netInterface = NetInterface.newInstance(networkInterface);
        Rule rule = new Rule.Builder().build();
        Settings settings = Settings.of(
                Socks5SettingSpecConstants.SOCKS5_ON_BIND_REQUEST_LISTEN_NET_INTERFACE.newSetting(
                        netInterface),
                Socks5SettingSpecConstants.SOCKS5_ON_BIND_REQUEST_LISTEN_BIND_HOST_ADDRESS_TYPES.newSetting(HostAddressTypes.of(
                        HostAddressType.HOST_IPV4_ADDRESS)));
        Assert.assertEquals(
                host,
                Socks5ValueDerivationHelper.getSocks5OnBindRequestListenBindHostFrom(rule, settings));
    }

    @Test
    public void testGetSocks5OnBindRequestListenBindPortRangesFromRuleSettings01() {
        PortRanges portRanges = PortRanges.getDefault();
        Rule rule = new Rule.Builder().build();
        Settings settings = Settings.of();
        Assert.assertEquals(
                portRanges,
                Socks5ValueDerivationHelper.getSocks5OnBindRequestListenBindPortRangesFrom(rule, settings));
    }

    @Test
    public void testGetSocks5OnBindRequestListenBindPortRangesFromRuleSettings02() {
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
                Socks5ValueDerivationHelper.getSocks5OnBindRequestListenBindPortRangesFrom(rule, settings));
    }

    @Test
    public void testGetSocks5OnBindRequestListenBindPortRangesFromRuleSettings03() {
        PortRange portRange = PortRange.of(
                Port.valueOf(1), Port.valueOf(65535));
        PortRanges portRanges = PortRanges.of(portRange);
        Rule rule = new Rule.Builder()
                .addRuleAction(GeneralRuleActionSpecConstants.EXTERNAL_FACING_BIND_TCP_PORT_RANGE.newRuleAction(
                        portRange))
                .build();
        Settings settings = Settings.of();
        Assert.assertEquals(
                portRanges,
                Socks5ValueDerivationHelper.getSocks5OnBindRequestListenBindPortRangesFrom(rule, settings));
    }

    @Test
    public void testGetSocks5OnBindRequestListenBindPortRangesFromRuleSettings04() {
        PortRange portRange = PortRange.of(
                Port.valueOf(1), Port.valueOf(65535));
        PortRanges portRanges = PortRanges.of(portRange);
        Rule rule = new Rule.Builder()
                .addRuleAction(SocksRuleActionSpecConstants.SOCKS_ON_REQUEST_EXTERNAL_FACING_BIND_TCP_PORT_RANGE.newRuleAction(
                        portRange))
                .build();
        Settings settings = Settings.of();
        Assert.assertEquals(
                portRanges,
                Socks5ValueDerivationHelper.getSocks5OnBindRequestListenBindPortRangesFrom(rule, settings));
    }

    @Test
    public void testGetSocks5OnBindRequestListenBindPortRangesFromRuleSettings05() {
        PortRange portRange = PortRange.of(
                Port.valueOf(1), Port.valueOf(65535));
        PortRanges portRanges = PortRanges.of(portRange);
        Rule rule = new Rule.Builder()
                .addRuleAction(SocksRuleActionSpecConstants.SOCKS_ON_BIND_REQUEST_LISTEN_BIND_PORT_RANGE.newRuleAction(
                        portRange))
                .build();
        Settings settings = Settings.of();
        Assert.assertEquals(
                portRanges,
                Socks5ValueDerivationHelper.getSocks5OnBindRequestListenBindPortRangesFrom(rule, settings));
    }

    @Test
    public void testGetSocks5OnBindRequestListenBindPortRangesFromRuleSettings06() {
        PortRange portRange = PortRange.of(
                Port.valueOf(1), Port.valueOf(65535));
        PortRanges portRanges = PortRanges.of(portRange);
        Rule rule = new Rule.Builder()
                .addRuleAction(Socks5RuleActionSpecConstants.SOCKS5_ON_BIND_REQUEST_LISTEN_BIND_PORT_RANGE.newRuleAction(
                        portRange))
                .build();
        Settings settings = Settings.of();
        Assert.assertEquals(
                portRanges,
                Socks5ValueDerivationHelper.getSocks5OnBindRequestListenBindPortRangesFrom(rule, settings));
    }

    @Test
    public void testGetSocks5OnBindRequestListenBindPortRangesFromRuleSettings07() {
        PortRange portRange = PortRange.of(
                Port.valueOf(1), Port.valueOf(65535));
        PortRanges portRanges = PortRanges.of(portRange);
        Rule rule = new Rule.Builder().build();
        Settings settings = Settings.of(
                GeneralSettingSpecConstants.BIND_TCP_PORT_RANGES.newSetting(
                        portRanges));
        Assert.assertEquals(
                portRanges,
                Socks5ValueDerivationHelper.getSocks5OnBindRequestListenBindPortRangesFrom(rule, settings));
    }

    @Test
    public void testGetSocks5OnBindRequestListenBindPortRangesFromRuleSettings08() {
        PortRange portRange = PortRange.of(
                Port.valueOf(1), Port.valueOf(65535));
        PortRanges portRanges = PortRanges.of(portRange);
        Rule rule = new Rule.Builder().build();
        Settings settings = Settings.of(
                GeneralSettingSpecConstants.EXTERNAL_FACING_BIND_TCP_PORT_RANGES.newSetting(
                        portRanges));
        Assert.assertEquals(
                portRanges,
                Socks5ValueDerivationHelper.getSocks5OnBindRequestListenBindPortRangesFrom(rule, settings));
    }

    @Test
    public void testGetSocks5OnBindRequestListenBindPortRangesFromRuleSettings09() {
        PortRange portRange = PortRange.of(
                Port.valueOf(1), Port.valueOf(65535));
        PortRanges portRanges = PortRanges.of(portRange);
        Rule rule = new Rule.Builder().build();
        Settings settings = Settings.of(
                SocksSettingSpecConstants.SOCKS_ON_REQUEST_EXTERNAL_FACING_BIND_TCP_PORT_RANGES.newSetting(
                        portRanges));
        Assert.assertEquals(
                portRanges,
                Socks5ValueDerivationHelper.getSocks5OnBindRequestListenBindPortRangesFrom(rule, settings));
    }

    @Test
    public void testGetSocks5OnBindRequestListenBindPortRangesFromRuleSettings10() {
        PortRange portRange = PortRange.of(
                Port.valueOf(1), Port.valueOf(65535));
        PortRanges portRanges = PortRanges.of(portRange);
        Rule rule = new Rule.Builder().build();
        Settings settings = Settings.of(
                SocksSettingSpecConstants.SOCKS_ON_BIND_REQUEST_LISTEN_BIND_PORT_RANGES.newSetting(
                        portRanges));
        Assert.assertEquals(
                portRanges,
                Socks5ValueDerivationHelper.getSocks5OnBindRequestListenBindPortRangesFrom(rule, settings));
    }

    @Test
    public void testGetSocks5OnBindRequestListenBindPortRangesFromRuleSettings11() {
        PortRange portRange = PortRange.of(
                Port.valueOf(1), Port.valueOf(65535));
        PortRanges portRanges = PortRanges.of(portRange);
        Rule rule = new Rule.Builder().build();
        Settings settings = Settings.of(
                Socks5SettingSpecConstants.SOCKS5_ON_BIND_REQUEST_LISTEN_BIND_PORT_RANGES.newSetting(
                        portRanges));
        Assert.assertEquals(
                portRanges,
                Socks5ValueDerivationHelper.getSocks5OnBindRequestListenBindPortRangesFrom(rule, settings));
    }

    @Test
    public void testGetSocks5OnBindRequestListenSocketSettingsFromRuleSettings01() {
        SocketSettings socketSettings = SocketSettings.of();
        Rule rule = new Rule.Builder().build();
        Settings settings = Settings.of();
        Assert.assertEquals(
                socketSettings,
                Socks5ValueDerivationHelper.getSocks5OnBindRequestListenSocketSettingsFrom(rule, settings));
    }

    @Test
    public void testGetSocks5OnBindRequestListenSocketSettingsFromRuleSettings02() {
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
                Socks5ValueDerivationHelper.getSocks5OnBindRequestListenSocketSettingsFrom(rule, settings));
    }

    @Test
    public void testGetSocks5OnBindRequestListenSocketSettingsFromRuleSettings03() {
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
        Settings settings = Settings.of();
        Assert.assertEquals(
                socketSettings,
                Socks5ValueDerivationHelper.getSocks5OnBindRequestListenSocketSettingsFrom(rule, settings));
    }

    @Test
    public void testGetSocks5OnBindRequestListenSocketSettingsFromRuleSettings04() {
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
        Settings settings = Settings.of();
        Assert.assertEquals(
                socketSettings,
                Socks5ValueDerivationHelper.getSocks5OnBindRequestListenSocketSettingsFrom(rule, settings));
    }

    @Test
    public void testGetSocks5OnBindRequestListenSocketSettingsFromRuleSettings05() {
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
        Settings settings = Settings.of();
        Assert.assertEquals(
                socketSettings,
                Socks5ValueDerivationHelper.getSocks5OnBindRequestListenSocketSettingsFrom(rule, settings));
    }

    @Test
    public void testGetSocks5OnBindRequestListenSocketSettingsFromRuleSettings06() {
        SocketSetting<?> socketSetting =
                StandardSocketSettingSpecConstants.SO_TIMEOUT.newSocketSetting(
                        NonNegativeInteger.valueOf(0));
        @SuppressWarnings("unchecked")
        SocketSetting<Object> socketSttng = (SocketSetting<Object>) socketSetting;
        SocketSettings socketSettings = SocketSettings.of(socketSttng);
        Rule rule = new Rule.Builder()
                .addRuleAction(Socks5RuleActionSpecConstants.SOCKS5_ON_BIND_REQUEST_LISTEN_SOCKET_SETTING.newRuleAction(
                        socketSttng))
                .build();
        Settings settings = Settings.of();
        Assert.assertEquals(
                socketSettings,
                Socks5ValueDerivationHelper.getSocks5OnBindRequestListenSocketSettingsFrom(rule, settings));
    }

    @Test
    public void testGetSocks5OnBindRequestListenSocketSettingsFromRuleSettings07() {
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
                Socks5ValueDerivationHelper.getSocks5OnBindRequestListenSocketSettingsFrom(rule, settings));
    }

    @Test
    public void testGetSocks5OnBindRequestListenSocketSettingsFromRuleSettings08() {
        SocketSetting<?> socketSetting =
                StandardSocketSettingSpecConstants.SO_TIMEOUT.newSocketSetting(
                        NonNegativeInteger.valueOf(0));
        SocketSettings socketSettings = SocketSettings.of(socketSetting);
        Rule rule = new Rule.Builder().build();
        Settings settings = Settings.of(
                GeneralSettingSpecConstants.EXTERNAL_FACING_SOCKET_SETTINGS.newSetting(
                        socketSettings));
        Assert.assertEquals(
                socketSettings,
                Socks5ValueDerivationHelper.getSocks5OnBindRequestListenSocketSettingsFrom(rule, settings));
    }

    @Test
    public void testGetSocks5OnBindRequestListenSocketSettingsFromRuleSettings09() {
        SocketSetting<?> socketSetting =
                StandardSocketSettingSpecConstants.SO_TIMEOUT.newSocketSetting(
                        NonNegativeInteger.valueOf(0));
        SocketSettings socketSettings = SocketSettings.of(socketSetting);
        Rule rule = new Rule.Builder().build();
        Settings settings = Settings.of(
                SocksSettingSpecConstants.SOCKS_ON_REQUEST_EXTERNAL_FACING_SOCKET_SETTINGS.newSetting(
                        socketSettings));
        Assert.assertEquals(
                socketSettings,
                Socks5ValueDerivationHelper.getSocks5OnBindRequestListenSocketSettingsFrom(rule, settings));
    }

    @Test
    public void testGetSocks5OnBindRequestListenSocketSettingsFromRuleSettings10() {
        SocketSetting<?> socketSetting =
                StandardSocketSettingSpecConstants.SO_TIMEOUT.newSocketSetting(
                        NonNegativeInteger.valueOf(0));
        SocketSettings socketSettings = SocketSettings.of(socketSetting);
        Rule rule = new Rule.Builder().build();
        Settings settings = Settings.of(
                SocksSettingSpecConstants.SOCKS_ON_BIND_REQUEST_LISTEN_SOCKET_SETTINGS.newSetting(
                        socketSettings));
        Assert.assertEquals(
                socketSettings,
                Socks5ValueDerivationHelper.getSocks5OnBindRequestListenSocketSettingsFrom(rule, settings));
    }

    @Test
    public void testGetSocks5OnBindRequestListenSocketSettingsFromRuleSettings11() {
        SocketSetting<?> socketSetting =
                StandardSocketSettingSpecConstants.SO_TIMEOUT.newSocketSetting(
                        NonNegativeInteger.valueOf(0));
        SocketSettings socketSettings = SocketSettings.of(socketSetting);
        Rule rule = new Rule.Builder().build();
        Settings settings = Settings.of(
                Socks5SettingSpecConstants.SOCKS5_ON_BIND_REQUEST_LISTEN_SOCKET_SETTINGS.newSetting(
                        socketSettings));
        Assert.assertEquals(
                socketSettings,
                Socks5ValueDerivationHelper.getSocks5OnBindRequestListenSocketSettingsFrom(rule, settings));
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
    public void testGetSocks5OnConnectRequestPrepareTargetFacingSocketFromRuleSettings01() {
        Rule rule = new Rule.Builder().build();
        Settings settings = Settings.of();
        Assert.assertFalse(Socks5ValueDerivationHelper.getSocks5OnConnectRequestPrepareTargetFacingSocketFrom(rule, settings));
    }

    @Test
    public void testGetSocks5OnConnectRequestPrepareTargetFacingSocketFromRuleSettings02() {
        Rule rule = new Rule.Builder()
                .addRuleAction(SocksRuleActionSpecConstants.SOCKS_ON_CONNECT_REQUEST_PREPARE_TARGET_FACING_SOCKET.newRuleAction(
                        Boolean.TRUE))
                .build();
        Settings settings = Settings.of();
        Assert.assertTrue(Socks5ValueDerivationHelper.getSocks5OnConnectRequestPrepareTargetFacingSocketFrom(rule, settings));
    }

    @Test
    public void testGetSocks5OnConnectRequestPrepareTargetFacingSocketFromRuleSettings03() {
        Rule rule = new Rule.Builder()
                .addRuleAction(Socks5RuleActionSpecConstants.SOCKS5_ON_CONNECT_REQUEST_PREPARE_TARGET_FACING_SOCKET.newRuleAction(
                        Boolean.TRUE))
                .build();
        Settings settings = Settings.of();
        Assert.assertTrue(Socks5ValueDerivationHelper.getSocks5OnConnectRequestPrepareTargetFacingSocketFrom(rule, settings));
    }

    @Test
    public void testGetSocks5OnConnectRequestPrepareTargetFacingSocketFromRuleSettings04() {
        Rule rule = new Rule.Builder().build();
        Settings settings = Settings.of(
                SocksSettingSpecConstants.SOCKS_ON_CONNECT_REQUEST_PREPARE_TARGET_FACING_SOCKET.newSetting(
                        Boolean.TRUE));
        Assert.assertTrue(Socks5ValueDerivationHelper.getSocks5OnConnectRequestPrepareTargetFacingSocketFrom(rule, settings));
    }

    @Test
    public void testGetSocks5OnConnectRequestPrepareTargetFacingSocketFromRuleSettings05() {
        Rule rule = new Rule.Builder().build();
        Settings settings = Settings.of(
                Socks5SettingSpecConstants.SOCKS5_ON_CONNECT_REQUEST_PREPARE_TARGET_FACING_SOCKET.newSetting(
                        Boolean.TRUE));
        Assert.assertTrue(Socks5ValueDerivationHelper.getSocks5OnConnectRequestPrepareTargetFacingSocketFrom(rule, settings));
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
    public void testGetSocks5OnConnectRequestTargetFacingBindHostFromRuleSettings01() {
        Host host = HostIpv4Address.getAllZerosInstance();
        Rule rule = new Rule.Builder().build();
        Settings settings = Settings.of();
        Assert.assertEquals(
                host,
                Socks5ValueDerivationHelper.getSocks5OnConnectRequestTargetFacingBindHostFrom(rule, settings));
    }

    @Test
    public void testGetSocks5OnConnectRequestTargetFacingBindHostFromRuleSettings02() {
        Host host = Host.newInstance("127.0.0.1");
        Rule rule = new Rule.Builder()
                .addRuleAction(GeneralRuleActionSpecConstants.BIND_HOST.newRuleAction(
                        host))
                .build();
        Settings settings = Settings.of();
        Assert.assertEquals(
                host,
                Socks5ValueDerivationHelper.getSocks5OnConnectRequestTargetFacingBindHostFrom(rule, settings));
    }

    @Test
    public void testGetSocks5OnConnectRequestTargetFacingBindHostFromRuleSettings03() {
        Host host = Host.newInstance("127.0.0.1");
        Rule rule = new Rule.Builder()
                .addRuleAction(GeneralRuleActionSpecConstants.EXTERNAL_FACING_BIND_HOST.newRuleAction(
                        host))
                .build();
        Settings settings = Settings.of();
        Assert.assertEquals(
                host,
                Socks5ValueDerivationHelper.getSocks5OnConnectRequestTargetFacingBindHostFrom(rule, settings));
    }

    @Test
    public void testGetSocks5OnConnectRequestTargetFacingBindHostFromRuleSettings04() {
        Host host = Host.newInstance("127.0.0.1");
        Rule rule = new Rule.Builder()
                .addRuleAction(SocksRuleActionSpecConstants.SOCKS_ON_REQUEST_EXTERNAL_FACING_BIND_HOST.newRuleAction(
                        host))
                .build();
        Settings settings = Settings.of();
        Assert.assertEquals(
                host,
                Socks5ValueDerivationHelper.getSocks5OnConnectRequestTargetFacingBindHostFrom(rule, settings));
    }

    @Test
    public void testGetSocks5OnConnectRequestTargetFacingBindHostFromRuleSettings05() {
        Host host = Host.newInstance("127.0.0.1");
        Rule rule = new Rule.Builder()
                .addRuleAction(SocksRuleActionSpecConstants.SOCKS_ON_CONNECT_REQUEST_TARGET_FACING_BIND_HOST.newRuleAction(
                        host))
                .build();
        Settings settings = Settings.of();
        Assert.assertEquals(
                host,
                Socks5ValueDerivationHelper.getSocks5OnConnectRequestTargetFacingBindHostFrom(rule, settings));
    }

    @Test
    public void testGetSocks5OnConnectRequestTargetFacingBindHostFromRuleSettings06() {
        Host host = Host.newInstance("127.0.0.1");
        Rule rule = new Rule.Builder()
                .addRuleAction(Socks5RuleActionSpecConstants.SOCKS5_ON_CONNECT_REQUEST_TARGET_FACING_BIND_HOST.newRuleAction(
                        host))
                .build();
        Settings settings = Settings.of();
        Assert.assertEquals(
                host,
                Socks5ValueDerivationHelper.getSocks5OnConnectRequestTargetFacingBindHostFrom(rule, settings));
    }

    @Test
    public void testGetSocks5OnConnectRequestTargetFacingBindHostFromRuleSettings07() throws UnknownHostException, SocketException {
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
        Assert.assertEquals(
                host,
                Socks5ValueDerivationHelper.getSocks5OnConnectRequestTargetFacingBindHostFrom(rule, settings));
    }

    @Test
    public void testGetSocks5OnConnectRequestTargetFacingBindHostFromRuleSettings08() throws UnknownHostException, SocketException {
        Host host = Host.newInstance("127.0.0.1");
        NetworkInterface networkInterface = NetworkInterface.getByInetAddress(
                host.toInetAddress());
        NetInterface netInterface = NetInterface.newInstance(networkInterface);
        Rule rule = new Rule.Builder()
                .addRuleAction(GeneralRuleActionSpecConstants.EXTERNAL_FACING_NET_INTERFACE.newRuleAction(
                        netInterface))
                .addRuleAction(GeneralRuleActionSpecConstants.EXTERNAL_FACING_BIND_HOST_ADDRESS_TYPE.newRuleAction(
                        HostAddressType.HOST_IPV4_ADDRESS))
                .build();
        Settings settings = Settings.of();
        Assert.assertEquals(
                host,
                Socks5ValueDerivationHelper.getSocks5OnConnectRequestTargetFacingBindHostFrom(rule, settings));
    }

    @Test
    public void testGetSocks5OnConnectRequestTargetFacingBindHostFromRuleSettings09() throws UnknownHostException, SocketException {
        Host host = Host.newInstance("127.0.0.1");
        NetworkInterface networkInterface = NetworkInterface.getByInetAddress(
                host.toInetAddress());
        NetInterface netInterface = NetInterface.newInstance(networkInterface);
        Rule rule = new Rule.Builder()
                .addRuleAction(SocksRuleActionSpecConstants.SOCKS_ON_REQUEST_EXTERNAL_FACING_NET_INTERFACE.newRuleAction(
                        netInterface))
                .addRuleAction(SocksRuleActionSpecConstants.SOCKS_ON_REQUEST_EXTERNAL_FACING_BIND_HOST_ADDRESS_TYPE.newRuleAction(
                        HostAddressType.HOST_IPV4_ADDRESS))
                .build();
        Settings settings = Settings.of();
        Assert.assertEquals(
                host,
                Socks5ValueDerivationHelper.getSocks5OnConnectRequestTargetFacingBindHostFrom(rule, settings));
    }

    @Test
    public void testGetSocks5OnConnectRequestTargetFacingBindHostFromRuleSettings10() throws UnknownHostException, SocketException {
        Host host = Host.newInstance("127.0.0.1");
        NetworkInterface networkInterface = NetworkInterface.getByInetAddress(
                host.toInetAddress());
        NetInterface netInterface = NetInterface.newInstance(networkInterface);
        Rule rule = new Rule.Builder()
                .addRuleAction(SocksRuleActionSpecConstants.SOCKS_ON_CONNECT_REQUEST_TARGET_FACING_NET_INTERFACE.newRuleAction(
                        netInterface))
                .addRuleAction(SocksRuleActionSpecConstants.SOCKS_ON_CONNECT_REQUEST_TARGET_FACING_BIND_HOST_ADDRESS_TYPE.newRuleAction(
                        HostAddressType.HOST_IPV4_ADDRESS))
                .build();
        Settings settings = Settings.of();
        Assert.assertEquals(
                host,
                Socks5ValueDerivationHelper.getSocks5OnConnectRequestTargetFacingBindHostFrom(rule, settings));
    }

    @Test
    public void testGetSocks5OnConnectRequestTargetFacingBindHostFromRuleSettings11() throws UnknownHostException, SocketException {
        Host host = Host.newInstance("127.0.0.1");
        NetworkInterface networkInterface = NetworkInterface.getByInetAddress(
                host.toInetAddress());
        NetInterface netInterface = NetInterface.newInstance(networkInterface);
        Rule rule = new Rule.Builder()
                .addRuleAction(Socks5RuleActionSpecConstants.SOCKS5_ON_CONNECT_REQUEST_TARGET_FACING_NET_INTERFACE.newRuleAction(
                        netInterface))
                .addRuleAction(Socks5RuleActionSpecConstants.SOCKS5_ON_CONNECT_REQUEST_TARGET_FACING_BIND_HOST_ADDRESS_TYPE.newRuleAction(
                        HostAddressType.HOST_IPV4_ADDRESS))
                .build();
        Settings settings = Settings.of();
        Assert.assertEquals(
                host,
                Socks5ValueDerivationHelper.getSocks5OnConnectRequestTargetFacingBindHostFrom(rule, settings));
    }

    @Test
    public void testGetSocks5OnConnectRequestTargetFacingBindHostFromRuleSettings12() {
        Host host = Host.newInstance("127.0.0.1");
        Rule rule = new Rule.Builder().build();
        Settings settings = Settings.of(
                GeneralSettingSpecConstants.BIND_HOST.newSetting(host));
        Assert.assertEquals(
                host,
                Socks5ValueDerivationHelper.getSocks5OnConnectRequestTargetFacingBindHostFrom(rule, settings));
    }

    @Test
    public void testGetSocks5OnConnectRequestTargetFacingBindHostFromRuleSettings13() {
        Host host = Host.newInstance("127.0.0.1");
        Rule rule = new Rule.Builder().build();
        Settings settings = Settings.of(
                GeneralSettingSpecConstants.EXTERNAL_FACING_BIND_HOST.newSetting(
                        host));
        Assert.assertEquals(
                host,
                Socks5ValueDerivationHelper.getSocks5OnConnectRequestTargetFacingBindHostFrom(rule, settings));
    }

    @Test
    public void testGetSocks5OnConnectRequestTargetFacingBindHostFromRuleSettings14() {
        Host host = Host.newInstance("127.0.0.1");
        Rule rule = new Rule.Builder().build();
        Settings settings = Settings.of(
                SocksSettingSpecConstants.SOCKS_ON_REQUEST_EXTERNAL_FACING_BIND_HOST.newSetting(
                        host));
        Assert.assertEquals(
                host,
                Socks5ValueDerivationHelper.getSocks5OnConnectRequestTargetFacingBindHostFrom(rule, settings));
    }

    @Test
    public void testGetSocks5OnConnectRequestTargetFacingBindHostFromRuleSettings15() {
        Host host = Host.newInstance("127.0.0.1");
        Rule rule = new Rule.Builder().build();
        Settings settings = Settings.of(
                SocksSettingSpecConstants.SOCKS_ON_CONNECT_REQUEST_TARGET_FACING_BIND_HOST.newSetting(
                        host));
        Assert.assertEquals(
                host,
                Socks5ValueDerivationHelper.getSocks5OnConnectRequestTargetFacingBindHostFrom(rule, settings));
    }

    @Test
    public void testGetSocks5OnConnectRequestTargetFacingBindHostFromRuleSettings16() {
        Host host = Host.newInstance("127.0.0.1");
        Rule rule = new Rule.Builder().build();
        Settings settings = Settings.of(
                Socks5SettingSpecConstants.SOCKS5_ON_CONNECT_REQUEST_TARGET_FACING_BIND_HOST.newSetting(
                        host));
        Assert.assertEquals(
                host,
                Socks5ValueDerivationHelper.getSocks5OnConnectRequestTargetFacingBindHostFrom(rule, settings));
    }

    @Test
    public void testGetSocks5OnConnectRequestTargetFacingBindHostFromRuleSettings17() throws UnknownHostException, SocketException {
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
        Assert.assertEquals(
                host,
                Socks5ValueDerivationHelper.getSocks5OnConnectRequestTargetFacingBindHostFrom(rule, settings));
    }

    @Test
    public void testGetSocks5OnConnectRequestTargetFacingBindHostFromRuleSettings18() throws UnknownHostException, SocketException {
        Host host = Host.newInstance("127.0.0.1");
        NetworkInterface networkInterface = NetworkInterface.getByInetAddress(
                host.toInetAddress());
        NetInterface netInterface = NetInterface.newInstance(networkInterface);
        Rule rule = new Rule.Builder().build();
        Settings settings = Settings.of(
                GeneralSettingSpecConstants.EXTERNAL_FACING_NET_INTERFACE.newSetting(
                        netInterface),
                GeneralSettingSpecConstants.EXTERNAL_FACING_BIND_HOST_ADDRESS_TYPES.newSetting(HostAddressTypes.of(
                        HostAddressType.HOST_IPV4_ADDRESS)));
        Assert.assertEquals(
                host,
                Socks5ValueDerivationHelper.getSocks5OnConnectRequestTargetFacingBindHostFrom(rule, settings));
    }

    @Test
    public void testGetSocks5OnConnectRequestTargetFacingBindHostFromRuleSettings19() throws UnknownHostException, SocketException {
        Host host = Host.newInstance("127.0.0.1");
        NetworkInterface networkInterface = NetworkInterface.getByInetAddress(
                host.toInetAddress());
        NetInterface netInterface = NetInterface.newInstance(networkInterface);
        Rule rule = new Rule.Builder().build();
        Settings settings = Settings.of(
                SocksSettingSpecConstants.SOCKS_ON_REQUEST_EXTERNAL_FACING_NET_INTERFACE.newSetting(
                        netInterface),
                SocksSettingSpecConstants.SOCKS_ON_REQUEST_EXTERNAL_FACING_BIND_HOST_ADDRESS_TYPES.newSetting(HostAddressTypes.of(
                        HostAddressType.HOST_IPV4_ADDRESS)));
        Assert.assertEquals(
                host,
                Socks5ValueDerivationHelper.getSocks5OnConnectRequestTargetFacingBindHostFrom(rule, settings));
    }

    @Test
    public void testGetSocks5OnConnectRequestTargetFacingBindHostFromRuleSettings20() throws UnknownHostException, SocketException {
        Host host = Host.newInstance("127.0.0.1");
        NetworkInterface networkInterface = NetworkInterface.getByInetAddress(
                host.toInetAddress());
        NetInterface netInterface = NetInterface.newInstance(networkInterface);
        Rule rule = new Rule.Builder().build();
        Settings settings = Settings.of(
                SocksSettingSpecConstants.SOCKS_ON_CONNECT_REQUEST_TARGET_FACING_NET_INTERFACE.newSetting(
                        netInterface),
                SocksSettingSpecConstants.SOCKS_ON_CONNECT_REQUEST_TARGET_FACING_BIND_HOST_ADDRESS_TYPES.newSetting(HostAddressTypes.of(
                        HostAddressType.HOST_IPV4_ADDRESS)));
        Assert.assertEquals(
                host,
                Socks5ValueDerivationHelper.getSocks5OnConnectRequestTargetFacingBindHostFrom(rule, settings));
    }

    @Test
    public void testGetSocks5OnConnectRequestTargetFacingBindHostFromRuleSettings21() throws UnknownHostException, SocketException {
        Host host = Host.newInstance("127.0.0.1");
        NetworkInterface networkInterface = NetworkInterface.getByInetAddress(
                host.toInetAddress());
        NetInterface netInterface = NetInterface.newInstance(networkInterface);
        Rule rule = new Rule.Builder().build();
        Settings settings = Settings.of(
                Socks5SettingSpecConstants.SOCKS5_ON_CONNECT_REQUEST_TARGET_FACING_NET_INTERFACE.newSetting(
                        netInterface),
                Socks5SettingSpecConstants.SOCKS5_ON_CONNECT_REQUEST_TARGET_FACING_BIND_HOST_ADDRESS_TYPES.newSetting(HostAddressTypes.of(
                        HostAddressType.HOST_IPV4_ADDRESS)));
        Assert.assertEquals(
                host,
                Socks5ValueDerivationHelper.getSocks5OnConnectRequestTargetFacingBindHostFrom(rule, settings));
    }

    @Test
    public void testGetSocks5OnConnectRequestTargetFacingBindPortRangesFromRuleSettings01() {
        PortRanges portRanges = PortRanges.getDefault();
        Rule rule = new Rule.Builder().build();
        Settings settings = Settings.of();
        Assert.assertEquals(
                portRanges,
                Socks5ValueDerivationHelper.getSocks5OnConnectRequestTargetFacingBindPortRangesFrom(rule, settings));
    }

    @Test
    public void testGetSocks5OnConnectRequestTargetFacingBindPortRangesFromRuleSettings02() {
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
                Socks5ValueDerivationHelper.getSocks5OnConnectRequestTargetFacingBindPortRangesFrom(rule, settings));
    }

    @Test
    public void testGetSocks5OnConnectRequestTargetFacingBindPortRangesFromRuleSettings03() {
        PortRange portRange = PortRange.of(
                Port.valueOf(1), Port.valueOf(65535));
        PortRanges portRanges = PortRanges.of(portRange);
        Rule rule = new Rule.Builder()
                .addRuleAction(GeneralRuleActionSpecConstants.EXTERNAL_FACING_BIND_TCP_PORT_RANGE.newRuleAction(
                        portRange))
                .build();
        Settings settings = Settings.of();
        Assert.assertEquals(
                portRanges,
                Socks5ValueDerivationHelper.getSocks5OnConnectRequestTargetFacingBindPortRangesFrom(rule, settings));
    }

    @Test
    public void testGetSocks5OnConnectRequestTargetFacingBindPortRangesFromRuleSettings04() {
        PortRange portRange = PortRange.of(
                Port.valueOf(1), Port.valueOf(65535));
        PortRanges portRanges = PortRanges.of(portRange);
        Rule rule = new Rule.Builder()
                .addRuleAction(SocksRuleActionSpecConstants.SOCKS_ON_REQUEST_EXTERNAL_FACING_BIND_TCP_PORT_RANGE.newRuleAction(
                        portRange))
                .build();
        Settings settings = Settings.of();
        Assert.assertEquals(
                portRanges,
                Socks5ValueDerivationHelper.getSocks5OnConnectRequestTargetFacingBindPortRangesFrom(rule, settings));
    }

    @Test
    public void testGetSocks5OnConnectRequestTargetFacingBindPortRangesFromRuleSettings05() {
        PortRange portRange = PortRange.of(
                Port.valueOf(1), Port.valueOf(65535));
        PortRanges portRanges = PortRanges.of(portRange);
        Rule rule = new Rule.Builder()
                .addRuleAction(SocksRuleActionSpecConstants.SOCKS_ON_CONNECT_REQUEST_TARGET_FACING_BIND_PORT_RANGE.newRuleAction(
                        portRange))
                .build();
        Settings settings = Settings.of();
        Assert.assertEquals(
                portRanges,
                Socks5ValueDerivationHelper.getSocks5OnConnectRequestTargetFacingBindPortRangesFrom(rule, settings));
    }

    @Test
    public void testGetSocks5OnConnectRequestTargetFacingBindPortRangesFromRuleSettings06() {
        PortRange portRange = PortRange.of(
                Port.valueOf(1), Port.valueOf(65535));
        PortRanges portRanges = PortRanges.of(portRange);
        Rule rule = new Rule.Builder()
                .addRuleAction(Socks5RuleActionSpecConstants.SOCKS5_ON_CONNECT_REQUEST_TARGET_FACING_BIND_PORT_RANGE.newRuleAction(
                        portRange))
                .build();
        Settings settings = Settings.of();
        Assert.assertEquals(
                portRanges,
                Socks5ValueDerivationHelper.getSocks5OnConnectRequestTargetFacingBindPortRangesFrom(rule, settings));
    }

    @Test
    public void testGetSocks5OnConnectRequestTargetFacingBindPortRangesFromRuleSettings07() {
        PortRange portRange = PortRange.of(
                Port.valueOf(1), Port.valueOf(65535));
        PortRanges portRanges = PortRanges.of(portRange);
        Rule rule = new Rule.Builder().build();
        Settings settings = Settings.of(
                GeneralSettingSpecConstants.BIND_TCP_PORT_RANGES.newSetting(
                        portRanges));
        Assert.assertEquals(
                portRanges,
                Socks5ValueDerivationHelper.getSocks5OnConnectRequestTargetFacingBindPortRangesFrom(rule, settings));
    }

    @Test
    public void testGetSocks5OnConnectRequestTargetFacingBindPortRangesFromRuleSettings08() {
        PortRange portRange = PortRange.of(
                Port.valueOf(1), Port.valueOf(65535));
        PortRanges portRanges = PortRanges.of(portRange);
        Rule rule = new Rule.Builder().build();
        Settings settings = Settings.of(
                GeneralSettingSpecConstants.EXTERNAL_FACING_BIND_TCP_PORT_RANGES.newSetting(
                        portRanges));
        Assert.assertEquals(
                portRanges,
                Socks5ValueDerivationHelper.getSocks5OnConnectRequestTargetFacingBindPortRangesFrom(rule, settings));
    }

    @Test
    public void testGetSocks5OnConnectRequestTargetFacingBindPortRangesFromRuleSettings09() {
        PortRange portRange = PortRange.of(
                Port.valueOf(1), Port.valueOf(65535));
        PortRanges portRanges = PortRanges.of(portRange);
        Rule rule = new Rule.Builder().build();
        Settings settings = Settings.of(
                SocksSettingSpecConstants.SOCKS_ON_REQUEST_EXTERNAL_FACING_BIND_TCP_PORT_RANGES.newSetting(
                        portRanges));
        Assert.assertEquals(
                portRanges,
                Socks5ValueDerivationHelper.getSocks5OnConnectRequestTargetFacingBindPortRangesFrom(rule, settings));
    }

    @Test
    public void testGetSocks5OnConnectRequestTargetFacingBindPortRangesFromRuleSettings10() {
        PortRange portRange = PortRange.of(
                Port.valueOf(1), Port.valueOf(65535));
        PortRanges portRanges = PortRanges.of(portRange);
        Rule rule = new Rule.Builder().build();
        Settings settings = Settings.of(
                SocksSettingSpecConstants.SOCKS_ON_CONNECT_REQUEST_TARGET_FACING_BIND_PORT_RANGES.newSetting(
                        portRanges));
        Assert.assertEquals(
                portRanges,
                Socks5ValueDerivationHelper.getSocks5OnConnectRequestTargetFacingBindPortRangesFrom(rule, settings));
    }

    @Test
    public void testGetSocks5OnConnectRequestTargetFacingBindPortRangesFromRuleSettings11() {
        PortRange portRange = PortRange.of(
                Port.valueOf(1), Port.valueOf(65535));
        PortRanges portRanges = PortRanges.of(portRange);
        Rule rule = new Rule.Builder().build();
        Settings settings = Settings.of(
                Socks5SettingSpecConstants.SOCKS5_ON_CONNECT_REQUEST_TARGET_FACING_BIND_PORT_RANGES.newSetting(
                        portRanges));
        Assert.assertEquals(
                portRanges,
                Socks5ValueDerivationHelper.getSocks5OnConnectRequestTargetFacingBindPortRangesFrom(rule, settings));
    }

    @Test
    public void testGetSocks5OnConnectRequestTargetFacingConnectTimeoutFromRuleSettings01() {
        PositiveInteger i = PositiveInteger.valueOf(60000);
        Rule rule = new Rule.Builder().build();
        Settings settings = Settings.of();
        Assert.assertEquals(i, Socks5ValueDerivationHelper.getSocks5OnConnectRequestTargetFacingConnectTimeoutFrom(rule, settings));
    }

    @Test
    public void testGetSocks5OnConnectRequestTargetFacingConnectTimeoutFromRuleSettings02() {
        PositiveInteger i = PositiveInteger.valueOf(1);
        Rule rule = new Rule.Builder()
                .addRuleAction(SocksRuleActionSpecConstants.SOCKS_ON_CONNECT_REQUEST_TARGET_FACING_CONNECT_TIMEOUT.newRuleAction(i))
                .build();
        Settings settings = Settings.of();
        Assert.assertEquals(i, Socks5ValueDerivationHelper.getSocks5OnConnectRequestTargetFacingConnectTimeoutFrom(rule, settings));
    }

    @Test
    public void testGetSocks5OnConnectRequestTargetFacingConnectTimeoutFromRuleSettings03() {
        PositiveInteger i = PositiveInteger.valueOf(1);
        Rule rule = new Rule.Builder()
                .addRuleAction(Socks5RuleActionSpecConstants.SOCKS5_ON_CONNECT_REQUEST_TARGET_FACING_CONNECT_TIMEOUT.newRuleAction(i))
                .build();
        Settings settings = Settings.of();
        Assert.assertEquals(i, Socks5ValueDerivationHelper.getSocks5OnConnectRequestTargetFacingConnectTimeoutFrom(rule, settings));
    }

    @Test
    public void testGetSocks5OnConnectRequestTargetFacingConnectTimeoutFromRuleSettings04() {
        PositiveInteger i = PositiveInteger.valueOf(1);
        Rule rule = new Rule.Builder().build();
        Settings settings = Settings.of(
                SocksSettingSpecConstants.SOCKS_ON_CONNECT_REQUEST_TARGET_FACING_CONNECT_TIMEOUT.newSetting(i));
        Assert.assertEquals(i, Socks5ValueDerivationHelper.getSocks5OnConnectRequestTargetFacingConnectTimeoutFrom(rule, settings));
    }

    @Test
    public void testGetSocks5OnConnectRequestTargetFacingConnectTimeoutFromRuleSettings05() {
        PositiveInteger i = PositiveInteger.valueOf(1);
        Rule rule = new Rule.Builder().build();
        Settings settings = Settings.of(
                Socks5SettingSpecConstants.SOCKS5_ON_CONNECT_REQUEST_TARGET_FACING_CONNECT_TIMEOUT.newSetting(i));
        Assert.assertEquals(i, Socks5ValueDerivationHelper.getSocks5OnConnectRequestTargetFacingConnectTimeoutFrom(rule, settings));
    }

    @Test
    public void testGetSocks5OnConnectRequestTargetFacingSocketSettingsFromRuleSettings01() {
        SocketSettings socketSettings = SocketSettings.of();
        Rule rule = new Rule.Builder().build();
        Settings settings = Settings.of();
        Assert.assertEquals(
                socketSettings,
                Socks5ValueDerivationHelper.getSocks5OnConnectRequestTargetFacingSocketSettingsFrom(rule, settings));
    }

    @Test
    public void testGetSocks5OnConnectRequestTargetFacingSocketSettingsFromRuleSettings02() {
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
                Socks5ValueDerivationHelper.getSocks5OnConnectRequestTargetFacingSocketSettingsFrom(rule, settings));
    }

    @Test
    public void testGetSocks5OnConnectRequestTargetFacingSocketSettingsFromRuleSettings03() {
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
        Settings settings = Settings.of();
        Assert.assertEquals(
                socketSettings,
                Socks5ValueDerivationHelper.getSocks5OnConnectRequestTargetFacingSocketSettingsFrom(rule, settings));
    }

    @Test
    public void testGetSocks5OnConnectRequestTargetFacingSocketSettingsFromRuleSettings04() {
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
        Settings settings = Settings.of();
        Assert.assertEquals(
                socketSettings,
                Socks5ValueDerivationHelper.getSocks5OnConnectRequestTargetFacingSocketSettingsFrom(rule, settings));
    }

    @Test
    public void testGetSocks5OnConnectRequestTargetFacingSocketSettingsFromRuleSettings05() {
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
        Settings settings = Settings.of();
        Assert.assertEquals(
                socketSettings,
                Socks5ValueDerivationHelper.getSocks5OnConnectRequestTargetFacingSocketSettingsFrom(rule, settings));
    }

    @Test
    public void testGetSocks5OnConnectRequestTargetFacingSocketSettingsFromRuleSettings06() {
        SocketSetting<?> socketSetting =
                StandardSocketSettingSpecConstants.SO_TIMEOUT.newSocketSetting(
                        NonNegativeInteger.valueOf(0));
        @SuppressWarnings("unchecked")
        SocketSetting<Object> socketSttng = (SocketSetting<Object>) socketSetting;
        SocketSettings socketSettings = SocketSettings.of(socketSttng);
        Rule rule = new Rule.Builder()
                .addRuleAction(Socks5RuleActionSpecConstants.SOCKS5_ON_CONNECT_REQUEST_TARGET_FACING_SOCKET_SETTING.newRuleAction(
                        socketSttng))
                .build();
        Settings settings = Settings.of();
        Assert.assertEquals(
                socketSettings,
                Socks5ValueDerivationHelper.getSocks5OnConnectRequestTargetFacingSocketSettingsFrom(rule, settings));
    }

    @Test
    public void testGetSocks5OnConnectRequestTargetFacingSocketSettingsFromRuleSettings07() {
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
                Socks5ValueDerivationHelper.getSocks5OnConnectRequestTargetFacingSocketSettingsFrom(rule, settings));
    }

    @Test
    public void testGetSocks5OnConnectRequestTargetFacingSocketSettingsFromRuleSettings08() {
        SocketSetting<?> socketSetting =
                StandardSocketSettingSpecConstants.SO_TIMEOUT.newSocketSetting(
                        NonNegativeInteger.valueOf(0));
        SocketSettings socketSettings = SocketSettings.of(socketSetting);
        Rule rule = new Rule.Builder().build();
        Settings settings = Settings.of(
                GeneralSettingSpecConstants.EXTERNAL_FACING_SOCKET_SETTINGS.newSetting(
                        socketSettings));
        Assert.assertEquals(
                socketSettings,
                Socks5ValueDerivationHelper.getSocks5OnConnectRequestTargetFacingSocketSettingsFrom(rule, settings));
    }

    @Test
    public void testGetSocks5OnConnectRequestTargetFacingSocketSettingsFromRuleSettings09() {
        SocketSetting<?> socketSetting =
                StandardSocketSettingSpecConstants.SO_TIMEOUT.newSocketSetting(
                        NonNegativeInteger.valueOf(0));
        SocketSettings socketSettings = SocketSettings.of(socketSetting);
        Rule rule = new Rule.Builder().build();
        Settings settings = Settings.of(
                SocksSettingSpecConstants.SOCKS_ON_REQUEST_EXTERNAL_FACING_SOCKET_SETTINGS.newSetting(
                        socketSettings));
        Assert.assertEquals(
                socketSettings,
                Socks5ValueDerivationHelper.getSocks5OnConnectRequestTargetFacingSocketSettingsFrom(rule, settings));
    }

    @Test
    public void testGetSocks5OnConnectRequestTargetFacingSocketSettingsFromRuleSettings10() {
        SocketSetting<?> socketSetting =
                StandardSocketSettingSpecConstants.SO_TIMEOUT.newSocketSetting(
                        NonNegativeInteger.valueOf(0));
        SocketSettings socketSettings = SocketSettings.of(socketSetting);
        Rule rule = new Rule.Builder().build();
        Settings settings = Settings.of(
                SocksSettingSpecConstants.SOCKS_ON_CONNECT_REQUEST_TARGET_FACING_SOCKET_SETTINGS.newSetting(
                        socketSettings));
        Assert.assertEquals(
                socketSettings,
                Socks5ValueDerivationHelper.getSocks5OnConnectRequestTargetFacingSocketSettingsFrom(rule, settings));
    }

    @Test
    public void testGetSocks5OnConnectRequestTargetFacingSocketSettingsFromRuleSettings11() {
        SocketSetting<?> socketSetting =
                StandardSocketSettingSpecConstants.SO_TIMEOUT.newSocketSetting(
                        NonNegativeInteger.valueOf(0));
        SocketSettings socketSettings = SocketSettings.of(socketSetting);
        Rule rule = new Rule.Builder().build();
        Settings settings = Settings.of(
                Socks5SettingSpecConstants.SOCKS5_ON_CONNECT_REQUEST_TARGET_FACING_SOCKET_SETTINGS.newSetting(
                        socketSettings));
        Assert.assertEquals(
                socketSettings,
                Socks5ValueDerivationHelper.getSocks5OnConnectRequestTargetFacingSocketSettingsFrom(rule, settings));
    }

    @Test
    public void testGetSocks5OnUdpAssociateRequestClientFacingBindHostFromRuleSettings01() {
        Host host = HostIpv4Address.getAllZerosInstance();
        Rule rule = new Rule.Builder().build();
        Settings settings = Settings.of();
        Assert.assertEquals(
                host,
                Socks5ValueDerivationHelper.getSocks5OnUdpAssociateRequestClientFacingBindHostFrom(rule, settings));
    }

    @Test
    public void testGetSocks5OnUdpAssociateRequestClientFacingBindHostFromRuleSettings02() {
        Host host = Host.newInstance("127.0.0.1");
        Rule rule = new Rule.Builder()
                .addRuleAction(GeneralRuleActionSpecConstants.BIND_HOST.newRuleAction(
                        host))
                .build();
        Settings settings = Settings.of();
        Assert.assertEquals(
                host,
                Socks5ValueDerivationHelper.getSocks5OnUdpAssociateRequestClientFacingBindHostFrom(rule, settings));
    }

    @Test
    public void testGetSocks5OnUdpAssociateRequestClientFacingBindHostFromRuleSettings03() {
        Host host = Host.newInstance("127.0.0.1");
        Rule rule = new Rule.Builder()
                .addRuleAction(GeneralRuleActionSpecConstants.INTERNAL_FACING_BIND_HOST.newRuleAction(
                        host))
                .build();
        Settings settings = Settings.of();
        Assert.assertEquals(
                host,
                Socks5ValueDerivationHelper.getSocks5OnUdpAssociateRequestClientFacingBindHostFrom(rule, settings));
    }

    @Test
    public void testGetSocks5OnUdpAssociateRequestClientFacingBindHostFromRuleSettings04() {
        Host host = Host.newInstance("127.0.0.1");
        Rule rule = new Rule.Builder()
                .addRuleAction(SocksRuleActionSpecConstants.SOCKS_ON_REQUEST_INTERNAL_FACING_BIND_HOST.newRuleAction(
                        host))
                .build();
        Settings settings = Settings.of();
        Assert.assertEquals(
                host,
                Socks5ValueDerivationHelper.getSocks5OnUdpAssociateRequestClientFacingBindHostFrom(rule, settings));
    }

    @Test
    public void testGetSocks5OnUdpAssociateRequestClientFacingBindHostFromRuleSettings05() {
        Host host = Host.newInstance("127.0.0.1");
        Rule rule = new Rule.Builder()
                .addRuleAction(SocksRuleActionSpecConstants.SOCKS_ON_UDP_ASSOCIATE_REQUEST_CLIENT_FACING_BIND_HOST.newRuleAction(
                        host))
                .build();
        Settings settings = Settings.of();
        Assert.assertEquals(
                host,
                Socks5ValueDerivationHelper.getSocks5OnUdpAssociateRequestClientFacingBindHostFrom(rule, settings));
    }

    @Test
    public void testGetSocks5OnUdpAssociateRequestClientFacingBindHostFromRuleSettings06() {
        Host host = Host.newInstance("127.0.0.1");
        Rule rule = new Rule.Builder()
                .addRuleAction(Socks5RuleActionSpecConstants.SOCKS5_ON_UDP_ASSOCIATE_REQUEST_CLIENT_FACING_BIND_HOST.newRuleAction(
                        host))
                .build();
        Settings settings = Settings.of();
        Assert.assertEquals(
                host,
                Socks5ValueDerivationHelper.getSocks5OnUdpAssociateRequestClientFacingBindHostFrom(rule, settings));
    }

    @Test
    public void testGetSocks5OnUdpAssociateRequestClientFacingBindHostFromRuleSettings07() throws UnknownHostException, SocketException {
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
        Assert.assertEquals(
                host,
                Socks5ValueDerivationHelper.getSocks5OnUdpAssociateRequestClientFacingBindHostFrom(rule, settings));
    }

    @Test
    public void testGetSocks5OnUdpAssociateRequestClientFacingBindHostFromRuleSettings08() throws UnknownHostException, SocketException {
        Host host = Host.newInstance("127.0.0.1");
        NetworkInterface networkInterface = NetworkInterface.getByInetAddress(
                host.toInetAddress());
        NetInterface netInterface = NetInterface.newInstance(networkInterface);
        Rule rule = new Rule.Builder()
                .addRuleAction(GeneralRuleActionSpecConstants.INTERNAL_FACING_NET_INTERFACE.newRuleAction(
                        netInterface))
                .addRuleAction(GeneralRuleActionSpecConstants.INTERNAL_FACING_BIND_HOST_ADDRESS_TYPE.newRuleAction(
                        HostAddressType.HOST_IPV4_ADDRESS))
                .build();
        Settings settings = Settings.of();
        Assert.assertEquals(
                host,
                Socks5ValueDerivationHelper.getSocks5OnUdpAssociateRequestClientFacingBindHostFrom(rule, settings));
    }

    @Test
    public void testGetSocks5OnUdpAssociateRequestClientFacingBindHostFromRuleSettings09() throws UnknownHostException, SocketException {
        Host host = Host.newInstance("127.0.0.1");
        NetworkInterface networkInterface = NetworkInterface.getByInetAddress(
                host.toInetAddress());
        NetInterface netInterface = NetInterface.newInstance(networkInterface);
        Rule rule = new Rule.Builder()
                .addRuleAction(SocksRuleActionSpecConstants.SOCKS_ON_REQUEST_INTERNAL_FACING_NET_INTERFACE.newRuleAction(
                        netInterface))
                .addRuleAction(SocksRuleActionSpecConstants.SOCKS_ON_REQUEST_INTERNAL_FACING_BIND_HOST_ADDRESS_TYPE.newRuleAction(
                        HostAddressType.HOST_IPV4_ADDRESS))
                .build();
        Settings settings = Settings.of();
        Assert.assertEquals(
                host,
                Socks5ValueDerivationHelper.getSocks5OnUdpAssociateRequestClientFacingBindHostFrom(rule, settings));
    }

    @Test
    public void testGetSocks5OnUdpAssociateRequestClientFacingBindHostFromRuleSettings10() throws UnknownHostException, SocketException {
        Host host = Host.newInstance("127.0.0.1");
        NetworkInterface networkInterface = NetworkInterface.getByInetAddress(
                host.toInetAddress());
        NetInterface netInterface = NetInterface.newInstance(networkInterface);
        Rule rule = new Rule.Builder()
                .addRuleAction(SocksRuleActionSpecConstants.SOCKS_ON_UDP_ASSOCIATE_REQUEST_CLIENT_FACING_NET_INTERFACE.newRuleAction(
                        netInterface))
                .addRuleAction(SocksRuleActionSpecConstants.SOCKS_ON_UDP_ASSOCIATE_REQUEST_CLIENT_FACING_BIND_HOST_ADDRESS_TYPE.newRuleAction(
                        HostAddressType.HOST_IPV4_ADDRESS))
                .build();
        Settings settings = Settings.of();
        Assert.assertEquals(
                host,
                Socks5ValueDerivationHelper.getSocks5OnUdpAssociateRequestClientFacingBindHostFrom(rule, settings));
    }

    @Test
    public void testGetSocks5OnUdpAssociateRequestClientFacingBindHostFromRuleSettings11() throws UnknownHostException, SocketException {
        Host host = Host.newInstance("127.0.0.1");
        NetworkInterface networkInterface = NetworkInterface.getByInetAddress(
                host.toInetAddress());
        NetInterface netInterface = NetInterface.newInstance(networkInterface);
        Rule rule = new Rule.Builder()
                .addRuleAction(Socks5RuleActionSpecConstants.SOCKS5_ON_UDP_ASSOCIATE_REQUEST_CLIENT_FACING_NET_INTERFACE.newRuleAction(
                        netInterface))
                .addRuleAction(Socks5RuleActionSpecConstants.SOCKS5_ON_UDP_ASSOCIATE_REQUEST_CLIENT_FACING_BIND_HOST_ADDRESS_TYPE.newRuleAction(
                        HostAddressType.HOST_IPV4_ADDRESS))
                .build();
        Settings settings = Settings.of();
        Assert.assertEquals(
                host,
                Socks5ValueDerivationHelper.getSocks5OnUdpAssociateRequestClientFacingBindHostFrom(rule, settings));
    }

    @Test
    public void testGetSocks5OnUdpAssociateRequestClientFacingBindHostFromRuleSettings12() {
        Host host = Host.newInstance("127.0.0.1");
        Rule rule = new Rule.Builder().build();
        Settings settings = Settings.of(
                GeneralSettingSpecConstants.BIND_HOST.newSetting(host));
        Assert.assertEquals(
                host,
                Socks5ValueDerivationHelper.getSocks5OnUdpAssociateRequestClientFacingBindHostFrom(rule, settings));
    }

    @Test
    public void testGetSocks5OnUdpAssociateRequestClientFacingBindHostFromRuleSettings13() {
        Host host = Host.newInstance("127.0.0.1");
        Rule rule = new Rule.Builder().build();
        Settings settings = Settings.of(
                GeneralSettingSpecConstants.INTERNAL_FACING_BIND_HOST.newSetting(
                        host));
        Assert.assertEquals(
                host,
                Socks5ValueDerivationHelper.getSocks5OnUdpAssociateRequestClientFacingBindHostFrom(rule, settings));
    }

    @Test
    public void testGetSocks5OnUdpAssociateRequestClientFacingBindHostFromRuleSettings14() {
        Host host = Host.newInstance("127.0.0.1");
        Rule rule = new Rule.Builder().build();
        Settings settings = Settings.of(
                SocksSettingSpecConstants.SOCKS_ON_REQUEST_INTERNAL_FACING_BIND_HOST.newSetting(
                        host));
        Assert.assertEquals(
                host,
                Socks5ValueDerivationHelper.getSocks5OnUdpAssociateRequestClientFacingBindHostFrom(rule, settings));
    }

    @Test
    public void testGetSocks5OnUdpAssociateRequestClientFacingBindHostFromRuleSettings15() {
        Host host = Host.newInstance("127.0.0.1");
        Rule rule = new Rule.Builder().build();
        Settings settings = Settings.of(
                SocksSettingSpecConstants.SOCKS_ON_UDP_ASSOCIATE_REQUEST_CLIENT_FACING_BIND_HOST.newSetting(
                        host));
        Assert.assertEquals(
                host,
                Socks5ValueDerivationHelper.getSocks5OnUdpAssociateRequestClientFacingBindHostFrom(rule, settings));
    }

    @Test
    public void testGetSocks5OnUdpAssociateRequestClientFacingBindHostFromRuleSettings16() {
        Host host = Host.newInstance("127.0.0.1");
        Rule rule = new Rule.Builder().build();
        Settings settings = Settings.of(
                Socks5SettingSpecConstants.SOCKS5_ON_UDP_ASSOCIATE_REQUEST_CLIENT_FACING_BIND_HOST.newSetting(
                        host));
        Assert.assertEquals(
                host,
                Socks5ValueDerivationHelper.getSocks5OnUdpAssociateRequestClientFacingBindHostFrom(rule, settings));
    }

    @Test
    public void testGetSocks5OnUdpAssociateRequestClientFacingBindHostFromRuleSettings17() throws UnknownHostException, SocketException {
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
        Assert.assertEquals(
                host,
                Socks5ValueDerivationHelper.getSocks5OnUdpAssociateRequestClientFacingBindHostFrom(rule, settings));
    }

    @Test
    public void testGetSocks5OnUdpAssociateRequestClientFacingBindHostFromRuleSettings18() throws UnknownHostException, SocketException {
        Host host = Host.newInstance("127.0.0.1");
        NetworkInterface networkInterface = NetworkInterface.getByInetAddress(
                host.toInetAddress());
        NetInterface netInterface = NetInterface.newInstance(networkInterface);
        Rule rule = new Rule.Builder().build();
        Settings settings = Settings.of(
                GeneralSettingSpecConstants.INTERNAL_FACING_NET_INTERFACE.newSetting(
                        netInterface),
                GeneralSettingSpecConstants.INTERNAL_FACING_BIND_HOST_ADDRESS_TYPES.newSetting(HostAddressTypes.of(
                        HostAddressType.HOST_IPV4_ADDRESS)));
        Assert.assertEquals(
                host,
                Socks5ValueDerivationHelper.getSocks5OnUdpAssociateRequestClientFacingBindHostFrom(rule, settings));
    }

    @Test
    public void testGetSocks5OnUdpAssociateRequestClientFacingBindHostFromRuleSettings19() throws UnknownHostException, SocketException {
        Host host = Host.newInstance("127.0.0.1");
        NetworkInterface networkInterface = NetworkInterface.getByInetAddress(
                host.toInetAddress());
        NetInterface netInterface = NetInterface.newInstance(networkInterface);
        Rule rule = new Rule.Builder().build();
        Settings settings = Settings.of(
                SocksSettingSpecConstants.SOCKS_ON_REQUEST_INTERNAL_FACING_NET_INTERFACE.newSetting(
                        netInterface),
                SocksSettingSpecConstants.SOCKS_ON_REQUEST_INTERNAL_FACING_BIND_HOST_ADDRESS_TYPES.newSetting(HostAddressTypes.of(
                        HostAddressType.HOST_IPV4_ADDRESS)));
        Assert.assertEquals(
                host,
                Socks5ValueDerivationHelper.getSocks5OnUdpAssociateRequestClientFacingBindHostFrom(rule, settings));
    }

    @Test
    public void testGetSocks5OnUdpAssociateRequestClientFacingBindHostFromRuleSettings20() throws UnknownHostException, SocketException {
        Host host = Host.newInstance("127.0.0.1");
        NetworkInterface networkInterface = NetworkInterface.getByInetAddress(
                host.toInetAddress());
        NetInterface netInterface = NetInterface.newInstance(networkInterface);
        Rule rule = new Rule.Builder().build();
        Settings settings = Settings.of(
                SocksSettingSpecConstants.SOCKS_ON_UDP_ASSOCIATE_REQUEST_CLIENT_FACING_NET_INTERFACE.newSetting(
                        netInterface),
                SocksSettingSpecConstants.SOCKS_ON_UDP_ASSOCIATE_REQUEST_CLIENT_FACING_BIND_HOST_ADDRESS_TYPES.newSetting(HostAddressTypes.of(
                        HostAddressType.HOST_IPV4_ADDRESS)));
        Assert.assertEquals(
                host,
                Socks5ValueDerivationHelper.getSocks5OnUdpAssociateRequestClientFacingBindHostFrom(rule, settings));
    }

    @Test
    public void testGetSocks5OnUdpAssociateRequestClientFacingBindHostFromRuleSettings21() throws UnknownHostException, SocketException {
        Host host = Host.newInstance("127.0.0.1");
        NetworkInterface networkInterface = NetworkInterface.getByInetAddress(
                host.toInetAddress());
        NetInterface netInterface = NetInterface.newInstance(networkInterface);
        Rule rule = new Rule.Builder().build();
        Settings settings = Settings.of(
                Socks5SettingSpecConstants.SOCKS5_ON_UDP_ASSOCIATE_REQUEST_CLIENT_FACING_NET_INTERFACE.newSetting(
                        netInterface),
                Socks5SettingSpecConstants.SOCKS5_ON_UDP_ASSOCIATE_REQUEST_CLIENT_FACING_BIND_HOST_ADDRESS_TYPES.newSetting(HostAddressTypes.of(
                        HostAddressType.HOST_IPV4_ADDRESS)));
        Assert.assertEquals(
                host,
                Socks5ValueDerivationHelper.getSocks5OnUdpAssociateRequestClientFacingBindHostFrom(rule, settings));
    }

    @Test
    public void testGetSocks5OnUdpAssociateRequestClientFacingBindPortRangesFromRuleSettings01() {
        PortRanges portRanges = PortRanges.getDefault();
        Rule rule = new Rule.Builder().build();
        Settings settings = Settings.of();
        Assert.assertEquals(
                portRanges,
                Socks5ValueDerivationHelper.getSocks5OnUdpAssociateRequestClientFacingBindPortRangesFrom(rule, settings));
    }

    @Test
    public void testGetSocks5OnUdpAssociateRequestClientFacingBindPortRangesFromRuleSettings02() {
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
                Socks5ValueDerivationHelper.getSocks5OnUdpAssociateRequestClientFacingBindPortRangesFrom(rule, settings));
    }

    @Test
    public void testGetSocks5OnUdpAssociateRequestClientFacingBindPortRangesFromRuleSettings03() {
        PortRange portRange = PortRange.of(
                Port.valueOf(1), Port.valueOf(65535));
        PortRanges portRanges = PortRanges.of(portRange);
        Rule rule = new Rule.Builder()
                .addRuleAction(GeneralRuleActionSpecConstants.INTERNAL_FACING_BIND_UDP_PORT_RANGE.newRuleAction(
                        portRange))
                .build();
        Settings settings = Settings.of();
        Assert.assertEquals(
                portRanges,
                Socks5ValueDerivationHelper.getSocks5OnUdpAssociateRequestClientFacingBindPortRangesFrom(rule, settings));
    }

    @Test
    public void testGetSocks5OnUdpAssociateRequestClientFacingBindPortRangesFromRuleSettings04() {
        PortRange portRange = PortRange.of(
                Port.valueOf(1), Port.valueOf(65535));
        PortRanges portRanges = PortRanges.of(portRange);
        Rule rule = new Rule.Builder()
                .addRuleAction(SocksRuleActionSpecConstants.SOCKS_ON_REQUEST_INTERNAL_FACING_BIND_UDP_PORT_RANGE.newRuleAction(
                        portRange))
                .build();
        Settings settings = Settings.of();
        Assert.assertEquals(
                portRanges,
                Socks5ValueDerivationHelper.getSocks5OnUdpAssociateRequestClientFacingBindPortRangesFrom(rule, settings));
    }

    @Test
    public void testGetSocks5OnUdpAssociateRequestClientFacingBindPortRangesFromRuleSettings05() {
        PortRange portRange = PortRange.of(
                Port.valueOf(1), Port.valueOf(65535));
        PortRanges portRanges = PortRanges.of(portRange);
        Rule rule = new Rule.Builder()
                .addRuleAction(SocksRuleActionSpecConstants.SOCKS_ON_UDP_ASSOCIATE_REQUEST_CLIENT_FACING_BIND_PORT_RANGE.newRuleAction(
                        portRange))
                .build();
        Settings settings = Settings.of();
        Assert.assertEquals(
                portRanges,
                Socks5ValueDerivationHelper.getSocks5OnUdpAssociateRequestClientFacingBindPortRangesFrom(rule, settings));
    }

    @Test
    public void testGetSocks5OnUdpAssociateRequestClientFacingBindPortRangesFromRuleSettings06() {
        PortRange portRange = PortRange.of(
                Port.valueOf(1), Port.valueOf(65535));
        PortRanges portRanges = PortRanges.of(portRange);
        Rule rule = new Rule.Builder()
                .addRuleAction(Socks5RuleActionSpecConstants.SOCKS5_ON_UDP_ASSOCIATE_REQUEST_CLIENT_FACING_BIND_PORT_RANGE.newRuleAction(
                        portRange))
                .build();
        Settings settings = Settings.of();
        Assert.assertEquals(
                portRanges,
                Socks5ValueDerivationHelper.getSocks5OnUdpAssociateRequestClientFacingBindPortRangesFrom(rule, settings));
    }

    @Test
    public void testGetSocks5OnUdpAssociateRequestClientFacingBindPortRangesFromRuleSettings07() {
        PortRange portRange = PortRange.of(
                Port.valueOf(1), Port.valueOf(65535));
        PortRanges portRanges = PortRanges.of(portRange);
        Rule rule = new Rule.Builder().build();
        Settings settings = Settings.of(
                GeneralSettingSpecConstants.BIND_UDP_PORT_RANGES.newSetting(
                        portRanges));
        Assert.assertEquals(
                portRanges,
                Socks5ValueDerivationHelper.getSocks5OnUdpAssociateRequestClientFacingBindPortRangesFrom(rule, settings));
    }

    @Test
    public void testGetSocks5OnUdpAssociateRequestClientFacingBindPortRangesFromRuleSettings08() {
        PortRange portRange = PortRange.of(
                Port.valueOf(1), Port.valueOf(65535));
        PortRanges portRanges = PortRanges.of(portRange);
        Rule rule = new Rule.Builder().build();
        Settings settings = Settings.of(
                GeneralSettingSpecConstants.INTERNAL_FACING_BIND_UDP_PORT_RANGES.newSetting(
                        portRanges));
        Assert.assertEquals(
                portRanges,
                Socks5ValueDerivationHelper.getSocks5OnUdpAssociateRequestClientFacingBindPortRangesFrom(rule, settings));
    }

    @Test
    public void testGetSocks5OnUdpAssociateRequestClientFacingBindPortRangesFromRuleSettings09() {
        PortRange portRange = PortRange.of(
                Port.valueOf(1), Port.valueOf(65535));
        PortRanges portRanges = PortRanges.of(portRange);
        Rule rule = new Rule.Builder().build();
        Settings settings = Settings.of(
                SocksSettingSpecConstants.SOCKS_ON_REQUEST_INTERNAL_FACING_BIND_UDP_PORT_RANGES.newSetting(
                        portRanges));
        Assert.assertEquals(
                portRanges,
                Socks5ValueDerivationHelper.getSocks5OnUdpAssociateRequestClientFacingBindPortRangesFrom(rule, settings));
    }

    @Test
    public void testGetSocks5OnUdpAssociateRequestClientFacingBindPortRangesFromRuleSettings10() {
        PortRange portRange = PortRange.of(
                Port.valueOf(1), Port.valueOf(65535));
        PortRanges portRanges = PortRanges.of(portRange);
        Rule rule = new Rule.Builder().build();
        Settings settings = Settings.of(
                SocksSettingSpecConstants.SOCKS_ON_UDP_ASSOCIATE_REQUEST_CLIENT_FACING_BIND_PORT_RANGES.newSetting(
                        portRanges));
        Assert.assertEquals(
                portRanges,
                Socks5ValueDerivationHelper.getSocks5OnUdpAssociateRequestClientFacingBindPortRangesFrom(rule, settings));
    }

    @Test
    public void testGetSocks5OnUdpAssociateRequestClientFacingBindPortRangesFromRuleSettings11() {
        PortRange portRange = PortRange.of(
                Port.valueOf(1), Port.valueOf(65535));
        PortRanges portRanges = PortRanges.of(portRange);
        Rule rule = new Rule.Builder().build();
        Settings settings = Settings.of(
                Socks5SettingSpecConstants.SOCKS5_ON_UDP_ASSOCIATE_REQUEST_CLIENT_FACING_BIND_PORT_RANGES.newSetting(
                        portRanges));
        Assert.assertEquals(
                portRanges,
                Socks5ValueDerivationHelper.getSocks5OnUdpAssociateRequestClientFacingBindPortRangesFrom(rule, settings));
    }

    @Test
    public void testGetSocks5OnUdpAssociateRequestClientFacingSocketSettingsFromRuleSettings01() {
        SocketSettings socketSettings = SocketSettings.of();
        Rule rule = new Rule.Builder().build();
        Settings settings = Settings.of();
        Assert.assertEquals(
                socketSettings,
                Socks5ValueDerivationHelper.getSocks5OnUdpAssociateRequestClientFacingSocketSettingsFrom(rule, settings));
    }

    @Test
    public void testGetSocks5OnUdpAssociateRequestClientFacingSocketSettingsFromRuleSettings02() {
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
                Socks5ValueDerivationHelper.getSocks5OnUdpAssociateRequestClientFacingSocketSettingsFrom(rule, settings));
    }

    @Test
    public void testGetSocks5OnUdpAssociateRequestClientFacingSocketSettingsFromRuleSettings03() {
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
        Settings settings = Settings.of();
        Assert.assertEquals(
                socketSettings,
                Socks5ValueDerivationHelper.getSocks5OnUdpAssociateRequestClientFacingSocketSettingsFrom(rule, settings));
    }

    @Test
    public void testGetSocks5OnUdpAssociateRequestClientFacingSocketSettingsFromRuleSettings04() {
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
        Settings settings = Settings.of();
        Assert.assertEquals(
                socketSettings,
                Socks5ValueDerivationHelper.getSocks5OnUdpAssociateRequestClientFacingSocketSettingsFrom(rule, settings));
    }

    @Test
    public void testGetSocks5OnUdpAssociateRequestClientFacingSocketSettingsFromRuleSettings05() {
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
        Settings settings = Settings.of();
        Assert.assertEquals(
                socketSettings,
                Socks5ValueDerivationHelper.getSocks5OnUdpAssociateRequestClientFacingSocketSettingsFrom(rule, settings));
    }

    @Test
    public void testGetSocks5OnUdpAssociateRequestClientFacingSocketSettingsFromRuleSettings06() {
        SocketSetting<?> socketSetting =
                StandardSocketSettingSpecConstants.SO_TIMEOUT.newSocketSetting(
                        NonNegativeInteger.valueOf(0));
        @SuppressWarnings("unchecked")
        SocketSetting<Object> socketSttng = (SocketSetting<Object>) socketSetting;
        SocketSettings socketSettings = SocketSettings.of(socketSttng);
        Rule rule = new Rule.Builder()
                .addRuleAction(Socks5RuleActionSpecConstants.SOCKS5_ON_UDP_ASSOCIATE_REQUEST_CLIENT_FACING_SOCKET_SETTING.newRuleAction(
                        socketSttng))
                .build();
        Settings settings = Settings.of();
        Assert.assertEquals(
                socketSettings,
                Socks5ValueDerivationHelper.getSocks5OnUdpAssociateRequestClientFacingSocketSettingsFrom(rule, settings));
    }

    @Test
    public void testGetSocks5OnUdpAssociateRequestClientFacingSocketSettingsFromRuleSettings07() {
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
                Socks5ValueDerivationHelper.getSocks5OnUdpAssociateRequestClientFacingSocketSettingsFrom(rule, settings));
    }

    @Test
    public void testGetSocks5OnUdpAssociateRequestClientFacingSocketSettingsFromRuleSettings08() {
        SocketSetting<?> socketSetting =
                StandardSocketSettingSpecConstants.SO_TIMEOUT.newSocketSetting(
                        NonNegativeInteger.valueOf(0));
        SocketSettings socketSettings = SocketSettings.of(socketSetting);
        Rule rule = new Rule.Builder().build();
        Settings settings = Settings.of(
                GeneralSettingSpecConstants.INTERNAL_FACING_SOCKET_SETTINGS.newSetting(
                        socketSettings));
        Assert.assertEquals(
                socketSettings,
                Socks5ValueDerivationHelper.getSocks5OnUdpAssociateRequestClientFacingSocketSettingsFrom(rule, settings));
    }

    @Test
    public void testGetSocks5OnUdpAssociateRequestClientFacingSocketSettingsFromRuleSettings09() {
        SocketSetting<?> socketSetting =
                StandardSocketSettingSpecConstants.SO_TIMEOUT.newSocketSetting(
                        NonNegativeInteger.valueOf(0));
        SocketSettings socketSettings = SocketSettings.of(socketSetting);
        Rule rule = new Rule.Builder().build();
        Settings settings = Settings.of(
                SocksSettingSpecConstants.SOCKS_ON_REQUEST_INTERNAL_FACING_SOCKET_SETTINGS.newSetting(
                        socketSettings));
        Assert.assertEquals(
                socketSettings,
                Socks5ValueDerivationHelper.getSocks5OnUdpAssociateRequestClientFacingSocketSettingsFrom(rule, settings));
    }

    @Test
    public void testGetSocks5OnUdpAssociateRequestClientFacingSocketSettingsFromRuleSettings10() {
        SocketSetting<?> socketSetting =
                StandardSocketSettingSpecConstants.SO_TIMEOUT.newSocketSetting(
                        NonNegativeInteger.valueOf(0));
        SocketSettings socketSettings = SocketSettings.of(socketSetting);
        Rule rule = new Rule.Builder().build();
        Settings settings = Settings.of(
                SocksSettingSpecConstants.SOCKS_ON_UDP_ASSOCIATE_REQUEST_CLIENT_FACING_SOCKET_SETTINGS.newSetting(
                        socketSettings));
        Assert.assertEquals(
                socketSettings,
                Socks5ValueDerivationHelper.getSocks5OnUdpAssociateRequestClientFacingSocketSettingsFrom(rule, settings));
    }

    @Test
    public void testGetSocks5OnUdpAssociateRequestClientFacingSocketSettingsFromRuleSettings11() {
        SocketSetting<?> socketSetting =
                StandardSocketSettingSpecConstants.SO_TIMEOUT.newSocketSetting(
                        NonNegativeInteger.valueOf(0));
        SocketSettings socketSettings = SocketSettings.of(socketSetting);
        Rule rule = new Rule.Builder().build();
        Settings settings = Settings.of(
                Socks5SettingSpecConstants.SOCKS5_ON_UDP_ASSOCIATE_REQUEST_CLIENT_FACING_SOCKET_SETTINGS.newSetting(
                        socketSettings));
        Assert.assertEquals(
                socketSettings,
                Socks5ValueDerivationHelper.getSocks5OnUdpAssociateRequestClientFacingSocketSettingsFrom(rule, settings));
    }

    @Test
    public void testGetSocks5OnUdpAssociateRequestPeerFacingBindHostFromRuleSettings01() {
        Host host = HostIpv4Address.getAllZerosInstance();
        Rule rule = new Rule.Builder().build();
        Settings settings = Settings.of();
        Assert.assertEquals(
                host,
                Socks5ValueDerivationHelper.getSocks5OnUdpAssociateRequestPeerFacingBindHostFrom(rule, settings));
    }

    @Test
    public void testGetSocks5OnUdpAssociateRequestPeerFacingBindHostFromRuleSettings02() {
        Host host = Host.newInstance("127.0.0.1");
        Rule rule = new Rule.Builder()
                .addRuleAction(GeneralRuleActionSpecConstants.BIND_HOST.newRuleAction(
                        host))
                .build();
        Settings settings = Settings.of();
        Assert.assertEquals(
                host,
                Socks5ValueDerivationHelper.getSocks5OnUdpAssociateRequestPeerFacingBindHostFrom(rule, settings));
    }

    @Test
    public void testGetSocks5OnUdpAssociateRequestPeerFacingBindHostFromRuleSettings03() {
        Host host = Host.newInstance("127.0.0.1");
        Rule rule = new Rule.Builder()
                .addRuleAction(GeneralRuleActionSpecConstants.EXTERNAL_FACING_BIND_HOST.newRuleAction(
                        host))
                .build();
        Settings settings = Settings.of();
        Assert.assertEquals(
                host,
                Socks5ValueDerivationHelper.getSocks5OnUdpAssociateRequestPeerFacingBindHostFrom(rule, settings));
    }

    @Test
    public void testGetSocks5OnUdpAssociateRequestPeerFacingBindHostFromRuleSettings04() {
        Host host = Host.newInstance("127.0.0.1");
        Rule rule = new Rule.Builder()
                .addRuleAction(SocksRuleActionSpecConstants.SOCKS_ON_REQUEST_EXTERNAL_FACING_BIND_HOST.newRuleAction(
                        host))
                .build();
        Settings settings = Settings.of();
        Assert.assertEquals(
                host,
                Socks5ValueDerivationHelper.getSocks5OnUdpAssociateRequestPeerFacingBindHostFrom(rule, settings));
    }

    @Test
    public void testGetSocks5OnUdpAssociateRequestPeerFacingBindHostFromRuleSettings05() {
        Host host = Host.newInstance("127.0.0.1");
        Rule rule = new Rule.Builder()
                .addRuleAction(SocksRuleActionSpecConstants.SOCKS_ON_UDP_ASSOCIATE_REQUEST_PEER_FACING_BIND_HOST.newRuleAction(
                        host))
                .build();
        Settings settings = Settings.of();
        Assert.assertEquals(
                host,
                Socks5ValueDerivationHelper.getSocks5OnUdpAssociateRequestPeerFacingBindHostFrom(rule, settings));
    }

    @Test
    public void testGetSocks5OnUdpAssociateRequestPeerFacingBindHostFromRuleSettings06() {
        Host host = Host.newInstance("127.0.0.1");
        Rule rule = new Rule.Builder()
                .addRuleAction(Socks5RuleActionSpecConstants.SOCKS5_ON_UDP_ASSOCIATE_REQUEST_PEER_FACING_BIND_HOST.newRuleAction(
                        host))
                .build();
        Settings settings = Settings.of();
        Assert.assertEquals(
                host,
                Socks5ValueDerivationHelper.getSocks5OnUdpAssociateRequestPeerFacingBindHostFrom(rule, settings));
    }

    @Test
    public void testGetSocks5OnUdpAssociateRequestPeerFacingBindHostFromRuleSettings07() throws UnknownHostException, SocketException {
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
        Assert.assertEquals(
                host,
                Socks5ValueDerivationHelper.getSocks5OnUdpAssociateRequestPeerFacingBindHostFrom(rule, settings));
    }

    @Test
    public void testGetSocks5OnUdpAssociateRequestPeerFacingBindHostFromRuleSettings08() throws UnknownHostException, SocketException {
        Host host = Host.newInstance("127.0.0.1");
        NetworkInterface networkInterface = NetworkInterface.getByInetAddress(
                host.toInetAddress());
        NetInterface netInterface = NetInterface.newInstance(networkInterface);
        Rule rule = new Rule.Builder()
                .addRuleAction(GeneralRuleActionSpecConstants.EXTERNAL_FACING_NET_INTERFACE.newRuleAction(
                        netInterface))
                .addRuleAction(GeneralRuleActionSpecConstants.EXTERNAL_FACING_BIND_HOST_ADDRESS_TYPE.newRuleAction(
                        HostAddressType.HOST_IPV4_ADDRESS))
                .build();
        Settings settings = Settings.of();
        Assert.assertEquals(
                host,
                Socks5ValueDerivationHelper.getSocks5OnUdpAssociateRequestPeerFacingBindHostFrom(rule, settings));
    }

    @Test
    public void testGetSocks5OnUdpAssociateRequestPeerFacingBindHostFromRuleSettings09() throws UnknownHostException, SocketException {
        Host host = Host.newInstance("127.0.0.1");
        NetworkInterface networkInterface = NetworkInterface.getByInetAddress(
                host.toInetAddress());
        NetInterface netInterface = NetInterface.newInstance(networkInterface);
        Rule rule = new Rule.Builder()
                .addRuleAction(SocksRuleActionSpecConstants.SOCKS_ON_REQUEST_EXTERNAL_FACING_NET_INTERFACE.newRuleAction(
                        netInterface))
                .addRuleAction(SocksRuleActionSpecConstants.SOCKS_ON_REQUEST_EXTERNAL_FACING_BIND_HOST_ADDRESS_TYPE.newRuleAction(
                        HostAddressType.HOST_IPV4_ADDRESS))
                .build();
        Settings settings = Settings.of();
        Assert.assertEquals(
                host,
                Socks5ValueDerivationHelper.getSocks5OnUdpAssociateRequestPeerFacingBindHostFrom(rule, settings));
    }

    @Test
    public void testGetSocks5OnUdpAssociateRequestPeerFacingBindHostFromRuleSettings10() throws UnknownHostException, SocketException {
        Host host = Host.newInstance("127.0.0.1");
        NetworkInterface networkInterface = NetworkInterface.getByInetAddress(
                host.toInetAddress());
        NetInterface netInterface = NetInterface.newInstance(networkInterface);
        Rule rule = new Rule.Builder()
                .addRuleAction(SocksRuleActionSpecConstants.SOCKS_ON_UDP_ASSOCIATE_REQUEST_PEER_FACING_NET_INTERFACE.newRuleAction(
                        netInterface))
                .addRuleAction(SocksRuleActionSpecConstants.SOCKS_ON_UDP_ASSOCIATE_REQUEST_PEER_FACING_BIND_HOST_ADDRESS_TYPE.newRuleAction(
                        HostAddressType.HOST_IPV4_ADDRESS))
                .build();
        Settings settings = Settings.of();
        Assert.assertEquals(
                host,
                Socks5ValueDerivationHelper.getSocks5OnUdpAssociateRequestPeerFacingBindHostFrom(rule, settings));
    }

    @Test
    public void testGetSocks5OnUdpAssociateRequestPeerFacingBindHostFromRuleSettings11() throws UnknownHostException, SocketException {
        Host host = Host.newInstance("127.0.0.1");
        NetworkInterface networkInterface = NetworkInterface.getByInetAddress(
                host.toInetAddress());
        NetInterface netInterface = NetInterface.newInstance(networkInterface);
        Rule rule = new Rule.Builder()
                .addRuleAction(Socks5RuleActionSpecConstants.SOCKS5_ON_UDP_ASSOCIATE_REQUEST_PEER_FACING_NET_INTERFACE.newRuleAction(
                        netInterface))
                .addRuleAction(Socks5RuleActionSpecConstants.SOCKS5_ON_UDP_ASSOCIATE_REQUEST_PEER_FACING_BIND_HOST_ADDRESS_TYPE.newRuleAction(
                        HostAddressType.HOST_IPV4_ADDRESS))
                .build();
        Settings settings = Settings.of();
        Assert.assertEquals(
                host,
                Socks5ValueDerivationHelper.getSocks5OnUdpAssociateRequestPeerFacingBindHostFrom(rule, settings));
    }

    @Test
    public void testGetSocks5OnUdpAssociateRequestPeerFacingBindHostFromRuleSettings12() {
        Host host = Host.newInstance("127.0.0.1");
        Rule rule = new Rule.Builder().build();
        Settings settings = Settings.of(
                GeneralSettingSpecConstants.BIND_HOST.newSetting(host));
        Assert.assertEquals(
                host,
                Socks5ValueDerivationHelper.getSocks5OnUdpAssociateRequestPeerFacingBindHostFrom(rule, settings));
    }

    @Test
    public void testGetSocks5OnUdpAssociateRequestPeerFacingBindHostFromRuleSettings13() {
        Host host = Host.newInstance("127.0.0.1");
        Rule rule = new Rule.Builder().build();
        Settings settings = Settings.of(
                GeneralSettingSpecConstants.EXTERNAL_FACING_BIND_HOST.newSetting(
                        host));
        Assert.assertEquals(
                host,
                Socks5ValueDerivationHelper.getSocks5OnUdpAssociateRequestPeerFacingBindHostFrom(rule, settings));
    }

    @Test
    public void testGetSocks5OnUdpAssociateRequestPeerFacingBindHostFromRuleSettings14() {
        Host host = Host.newInstance("127.0.0.1");
        Rule rule = new Rule.Builder().build();
        Settings settings = Settings.of(
                SocksSettingSpecConstants.SOCKS_ON_REQUEST_EXTERNAL_FACING_BIND_HOST.newSetting(
                        host));
        Assert.assertEquals(
                host,
                Socks5ValueDerivationHelper.getSocks5OnUdpAssociateRequestPeerFacingBindHostFrom(rule, settings));
    }

    @Test
    public void testGetSocks5OnUdpAssociateRequestPeerFacingBindHostFromRuleSettings15() {
        Host host = Host.newInstance("127.0.0.1");
        Rule rule = new Rule.Builder().build();
        Settings settings = Settings.of(
                SocksSettingSpecConstants.SOCKS_ON_UDP_ASSOCIATE_REQUEST_PEER_FACING_BIND_HOST.newSetting(
                        host));
        Assert.assertEquals(
                host,
                Socks5ValueDerivationHelper.getSocks5OnUdpAssociateRequestPeerFacingBindHostFrom(rule, settings));
    }

    @Test
    public void testGetSocks5OnUdpAssociateRequestPeerFacingBindHostFromRuleSettings16() {
        Host host = Host.newInstance("127.0.0.1");
        Rule rule = new Rule.Builder().build();
        Settings settings = Settings.of(
                Socks5SettingSpecConstants.SOCKS5_ON_UDP_ASSOCIATE_REQUEST_PEER_FACING_BIND_HOST.newSetting(
                        host));
        Assert.assertEquals(
                host,
                Socks5ValueDerivationHelper.getSocks5OnUdpAssociateRequestPeerFacingBindHostFrom(rule, settings));
    }

    @Test
    public void testGetSocks5OnUdpAssociateRequestPeerFacingBindHostFromRuleSettings17() throws UnknownHostException, SocketException {
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
        Assert.assertEquals(
                host,
                Socks5ValueDerivationHelper.getSocks5OnUdpAssociateRequestPeerFacingBindHostFrom(rule, settings));
    }

    @Test
    public void testGetSocks5OnUdpAssociateRequestPeerFacingBindHostFromRuleSettings18() throws UnknownHostException, SocketException {
        Host host = Host.newInstance("127.0.0.1");
        NetworkInterface networkInterface = NetworkInterface.getByInetAddress(
                host.toInetAddress());
        NetInterface netInterface = NetInterface.newInstance(networkInterface);
        Rule rule = new Rule.Builder().build();
        Settings settings = Settings.of(
                GeneralSettingSpecConstants.EXTERNAL_FACING_NET_INTERFACE.newSetting(
                        netInterface),
                GeneralSettingSpecConstants.EXTERNAL_FACING_BIND_HOST_ADDRESS_TYPES.newSetting(HostAddressTypes.of(
                        HostAddressType.HOST_IPV4_ADDRESS)));
        Assert.assertEquals(
                host,
                Socks5ValueDerivationHelper.getSocks5OnUdpAssociateRequestPeerFacingBindHostFrom(rule, settings));
    }

    @Test
    public void testGetSocks5OnUdpAssociateRequestPeerFacingBindHostFromRuleSettings19() throws UnknownHostException, SocketException {
        Host host = Host.newInstance("127.0.0.1");
        NetworkInterface networkInterface = NetworkInterface.getByInetAddress(
                host.toInetAddress());
        NetInterface netInterface = NetInterface.newInstance(networkInterface);
        Rule rule = new Rule.Builder().build();
        Settings settings = Settings.of(
                SocksSettingSpecConstants.SOCKS_ON_REQUEST_EXTERNAL_FACING_NET_INTERFACE.newSetting(
                        netInterface),
                SocksSettingSpecConstants.SOCKS_ON_REQUEST_EXTERNAL_FACING_BIND_HOST_ADDRESS_TYPES.newSetting(HostAddressTypes.of(
                        HostAddressType.HOST_IPV4_ADDRESS)));
        Assert.assertEquals(
                host,
                Socks5ValueDerivationHelper.getSocks5OnUdpAssociateRequestPeerFacingBindHostFrom(rule, settings));
    }

    @Test
    public void testGetSocks5OnUdpAssociateRequestPeerFacingBindHostFromRuleSettings20() throws UnknownHostException, SocketException {
        Host host = Host.newInstance("127.0.0.1");
        NetworkInterface networkInterface = NetworkInterface.getByInetAddress(
                host.toInetAddress());
        NetInterface netInterface = NetInterface.newInstance(networkInterface);
        Rule rule = new Rule.Builder().build();
        Settings settings = Settings.of(
                SocksSettingSpecConstants.SOCKS_ON_UDP_ASSOCIATE_REQUEST_PEER_FACING_NET_INTERFACE.newSetting(
                        netInterface),
                SocksSettingSpecConstants.SOCKS_ON_UDP_ASSOCIATE_REQUEST_PEER_FACING_BIND_HOST_ADDRESS_TYPES.newSetting(HostAddressTypes.of(
                        HostAddressType.HOST_IPV4_ADDRESS)));
        Assert.assertEquals(
                host,
                Socks5ValueDerivationHelper.getSocks5OnUdpAssociateRequestPeerFacingBindHostFrom(rule, settings));
    }

    @Test
    public void testGetSocks5OnUdpAssociateRequestPeerFacingBindHostFromRuleSettings21() throws UnknownHostException, SocketException {
        Host host = Host.newInstance("127.0.0.1");
        NetworkInterface networkInterface = NetworkInterface.getByInetAddress(
                host.toInetAddress());
        NetInterface netInterface = NetInterface.newInstance(networkInterface);
        Rule rule = new Rule.Builder().build();
        Settings settings = Settings.of(
                Socks5SettingSpecConstants.SOCKS5_ON_UDP_ASSOCIATE_REQUEST_PEER_FACING_NET_INTERFACE.newSetting(
                        netInterface),
                Socks5SettingSpecConstants.SOCKS5_ON_UDP_ASSOCIATE_REQUEST_PEER_FACING_BIND_HOST_ADDRESS_TYPES.newSetting(HostAddressTypes.of(
                        HostAddressType.HOST_IPV4_ADDRESS)));
        Assert.assertEquals(
                host,
                Socks5ValueDerivationHelper.getSocks5OnUdpAssociateRequestPeerFacingBindHostFrom(rule, settings));
    }

    @Test
    public void testGetSocks5OnUdpAssociateRequestPeerFacingBindPortRangesFromRuleSettings01() {
        PortRanges portRanges = PortRanges.getDefault();
        Rule rule = new Rule.Builder().build();
        Settings settings = Settings.of();
        Assert.assertEquals(
                portRanges,
                Socks5ValueDerivationHelper.getSocks5OnUdpAssociateRequestPeerFacingBindPortRangesFrom(rule, settings));
    }

    @Test
    public void testGetSocks5OnUdpAssociateRequestPeerFacingBindPortRangesFromRuleSettings02() {
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
                Socks5ValueDerivationHelper.getSocks5OnUdpAssociateRequestPeerFacingBindPortRangesFrom(rule, settings));
    }

    @Test
    public void testGetSocks5OnUdpAssociateRequestPeerFacingBindPortRangesFromRuleSettings03() {
        PortRange portRange = PortRange.of(
                Port.valueOf(1), Port.valueOf(65535));
        PortRanges portRanges = PortRanges.of(portRange);
        Rule rule = new Rule.Builder()
                .addRuleAction(GeneralRuleActionSpecConstants.EXTERNAL_FACING_BIND_UDP_PORT_RANGE.newRuleAction(
                        portRange))
                .build();
        Settings settings = Settings.of();
        Assert.assertEquals(
                portRanges,
                Socks5ValueDerivationHelper.getSocks5OnUdpAssociateRequestPeerFacingBindPortRangesFrom(rule, settings));
    }

    @Test
    public void testGetSocks5OnUdpAssociateRequestPeerFacingBindPortRangesFromRuleSettings04() {
        PortRange portRange = PortRange.of(
                Port.valueOf(1), Port.valueOf(65535));
        PortRanges portRanges = PortRanges.of(portRange);
        Rule rule = new Rule.Builder()
                .addRuleAction(SocksRuleActionSpecConstants.SOCKS_ON_REQUEST_EXTERNAL_FACING_BIND_UDP_PORT_RANGE.newRuleAction(
                        portRange))
                .build();
        Settings settings = Settings.of();
        Assert.assertEquals(
                portRanges,
                Socks5ValueDerivationHelper.getSocks5OnUdpAssociateRequestPeerFacingBindPortRangesFrom(rule, settings));
    }

    @Test
    public void testGetSocks5OnUdpAssociateRequestPeerFacingBindPortRangesFromRuleSettings05() {
        PortRange portRange = PortRange.of(
                Port.valueOf(1), Port.valueOf(65535));
        PortRanges portRanges = PortRanges.of(portRange);
        Rule rule = new Rule.Builder()
                .addRuleAction(SocksRuleActionSpecConstants.SOCKS_ON_UDP_ASSOCIATE_REQUEST_PEER_FACING_BIND_PORT_RANGE.newRuleAction(
                        portRange))
                .build();
        Settings settings = Settings.of();
        Assert.assertEquals(
                portRanges,
                Socks5ValueDerivationHelper.getSocks5OnUdpAssociateRequestPeerFacingBindPortRangesFrom(rule, settings));
    }

    @Test
    public void testGetSocks5OnUdpAssociateRequestPeerFacingBindPortRangesFromRuleSettings06() {
        PortRange portRange = PortRange.of(
                Port.valueOf(1), Port.valueOf(65535));
        PortRanges portRanges = PortRanges.of(portRange);
        Rule rule = new Rule.Builder()
                .addRuleAction(Socks5RuleActionSpecConstants.SOCKS5_ON_UDP_ASSOCIATE_REQUEST_PEER_FACING_BIND_PORT_RANGE.newRuleAction(
                        portRange))
                .build();
        Settings settings = Settings.of();
        Assert.assertEquals(
                portRanges,
                Socks5ValueDerivationHelper.getSocks5OnUdpAssociateRequestPeerFacingBindPortRangesFrom(rule, settings));
    }

    @Test
    public void testGetSocks5OnUdpAssociateRequestPeerFacingBindPortRangesFromRuleSettings07() {
        PortRange portRange = PortRange.of(
                Port.valueOf(1), Port.valueOf(65535));
        PortRanges portRanges = PortRanges.of(portRange);
        Rule rule = new Rule.Builder().build();
        Settings settings = Settings.of(
                GeneralSettingSpecConstants.BIND_UDP_PORT_RANGES.newSetting(
                        portRanges));
        Assert.assertEquals(
                portRanges,
                Socks5ValueDerivationHelper.getSocks5OnUdpAssociateRequestPeerFacingBindPortRangesFrom(rule, settings));
    }

    @Test
    public void testGetSocks5OnUdpAssociateRequestPeerFacingBindPortRangesFromRuleSettings08() {
        PortRange portRange = PortRange.of(
                Port.valueOf(1), Port.valueOf(65535));
        PortRanges portRanges = PortRanges.of(portRange);
        Rule rule = new Rule.Builder().build();
        Settings settings = Settings.of(
                GeneralSettingSpecConstants.EXTERNAL_FACING_BIND_UDP_PORT_RANGES.newSetting(
                        portRanges));
        Assert.assertEquals(
                portRanges,
                Socks5ValueDerivationHelper.getSocks5OnUdpAssociateRequestPeerFacingBindPortRangesFrom(rule, settings));
    }

    @Test
    public void testGetSocks5OnUdpAssociateRequestPeerFacingBindPortRangesFromRuleSettings09() {
        PortRange portRange = PortRange.of(
                Port.valueOf(1), Port.valueOf(65535));
        PortRanges portRanges = PortRanges.of(portRange);
        Rule rule = new Rule.Builder().build();
        Settings settings = Settings.of(
                SocksSettingSpecConstants.SOCKS_ON_REQUEST_EXTERNAL_FACING_BIND_UDP_PORT_RANGES.newSetting(
                        portRanges));
        Assert.assertEquals(
                portRanges,
                Socks5ValueDerivationHelper.getSocks5OnUdpAssociateRequestPeerFacingBindPortRangesFrom(rule, settings));
    }

    @Test
    public void testGetSocks5OnUdpAssociateRequestPeerFacingBindPortRangesFromRuleSettings10() {
        PortRange portRange = PortRange.of(
                Port.valueOf(1), Port.valueOf(65535));
        PortRanges portRanges = PortRanges.of(portRange);
        Rule rule = new Rule.Builder().build();
        Settings settings = Settings.of(
                SocksSettingSpecConstants.SOCKS_ON_UDP_ASSOCIATE_REQUEST_PEER_FACING_BIND_PORT_RANGES.newSetting(
                        portRanges));
        Assert.assertEquals(
                portRanges,
                Socks5ValueDerivationHelper.getSocks5OnUdpAssociateRequestPeerFacingBindPortRangesFrom(rule, settings));
    }

    @Test
    public void testGetSocks5OnUdpAssociateRequestPeerFacingBindPortRangesFromRuleSettings11() {
        PortRange portRange = PortRange.of(
                Port.valueOf(1), Port.valueOf(65535));
        PortRanges portRanges = PortRanges.of(portRange);
        Rule rule = new Rule.Builder().build();
        Settings settings = Settings.of(
                Socks5SettingSpecConstants.SOCKS5_ON_UDP_ASSOCIATE_REQUEST_PEER_FACING_BIND_PORT_RANGES.newSetting(
                        portRanges));
        Assert.assertEquals(
                portRanges,
                Socks5ValueDerivationHelper.getSocks5OnUdpAssociateRequestPeerFacingBindPortRangesFrom(rule, settings));
    }

    @Test
    public void testGetSocks5OnUdpAssociateRequestPeerFacingSocketSettingsFromRuleSettings01() {
        SocketSettings socketSettings = SocketSettings.of();
        Rule rule = new Rule.Builder().build();
        Settings settings = Settings.of();
        Assert.assertEquals(
                socketSettings,
                Socks5ValueDerivationHelper.getSocks5OnUdpAssociateRequestPeerFacingSocketSettingsFrom(rule, settings));
    }

    @Test
    public void testGetSocks5OnUdpAssociateRequestPeerFacingSocketSettingsFromRuleSettings02() {
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
                Socks5ValueDerivationHelper.getSocks5OnUdpAssociateRequestPeerFacingSocketSettingsFrom(rule, settings));
    }

    @Test
    public void testGetSocks5OnUdpAssociateRequestPeerFacingSocketSettingsFromRuleSettings03() {
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
        Settings settings = Settings.of();
        Assert.assertEquals(
                socketSettings,
                Socks5ValueDerivationHelper.getSocks5OnUdpAssociateRequestPeerFacingSocketSettingsFrom(rule, settings));
    }

    @Test
    public void testGetSocks5OnUdpAssociateRequestPeerFacingSocketSettingsFromRuleSettings04() {
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
        Settings settings = Settings.of();
        Assert.assertEquals(
                socketSettings,
                Socks5ValueDerivationHelper.getSocks5OnUdpAssociateRequestPeerFacingSocketSettingsFrom(rule, settings));
    }

    @Test
    public void testGetSocks5OnUdpAssociateRequestPeerFacingSocketSettingsFromRuleSettings05() {
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
        Settings settings = Settings.of();
        Assert.assertEquals(
                socketSettings,
                Socks5ValueDerivationHelper.getSocks5OnUdpAssociateRequestPeerFacingSocketSettingsFrom(rule, settings));
    }

    @Test
    public void testGetSocks5OnUdpAssociateRequestPeerFacingSocketSettingsFromRuleSettings06() {
        SocketSetting<?> socketSetting =
                StandardSocketSettingSpecConstants.SO_TIMEOUT.newSocketSetting(
                        NonNegativeInteger.valueOf(0));
        @SuppressWarnings("unchecked")
        SocketSetting<Object> socketSttng = (SocketSetting<Object>) socketSetting;
        SocketSettings socketSettings = SocketSettings.of(socketSttng);
        Rule rule = new Rule.Builder()
                .addRuleAction(Socks5RuleActionSpecConstants.SOCKS5_ON_UDP_ASSOCIATE_REQUEST_PEER_FACING_SOCKET_SETTING.newRuleAction(
                        socketSttng))
                .build();
        Settings settings = Settings.of();
        Assert.assertEquals(
                socketSettings,
                Socks5ValueDerivationHelper.getSocks5OnUdpAssociateRequestPeerFacingSocketSettingsFrom(rule, settings));
    }

    @Test
    public void testGetSocks5OnUdpAssociateRequestPeerFacingSocketSettingsFromRuleSettings07() {
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
                Socks5ValueDerivationHelper.getSocks5OnUdpAssociateRequestPeerFacingSocketSettingsFrom(rule, settings));
    }

    @Test
    public void testGetSocks5OnUdpAssociateRequestPeerFacingSocketSettingsFromRuleSettings08() {
        SocketSetting<?> socketSetting =
                StandardSocketSettingSpecConstants.SO_TIMEOUT.newSocketSetting(
                        NonNegativeInteger.valueOf(0));
        SocketSettings socketSettings = SocketSettings.of(socketSetting);
        Rule rule = new Rule.Builder().build();
        Settings settings = Settings.of(
                GeneralSettingSpecConstants.EXTERNAL_FACING_SOCKET_SETTINGS.newSetting(
                        socketSettings));
        Assert.assertEquals(
                socketSettings,
                Socks5ValueDerivationHelper.getSocks5OnUdpAssociateRequestPeerFacingSocketSettingsFrom(rule, settings));
    }

    @Test
    public void testGetSocks5OnUdpAssociateRequestPeerFacingSocketSettingsFromRuleSettings09() {
        SocketSetting<?> socketSetting =
                StandardSocketSettingSpecConstants.SO_TIMEOUT.newSocketSetting(
                        NonNegativeInteger.valueOf(0));
        SocketSettings socketSettings = SocketSettings.of(socketSetting);
        Rule rule = new Rule.Builder().build();
        Settings settings = Settings.of(
                SocksSettingSpecConstants.SOCKS_ON_REQUEST_EXTERNAL_FACING_SOCKET_SETTINGS.newSetting(
                        socketSettings));
        Assert.assertEquals(
                socketSettings,
                Socks5ValueDerivationHelper.getSocks5OnUdpAssociateRequestPeerFacingSocketSettingsFrom(rule, settings));
    }

    @Test
    public void testGetSocks5OnUdpAssociateRequestPeerFacingSocketSettingsFromRuleSettings10() {
        SocketSetting<?> socketSetting =
                StandardSocketSettingSpecConstants.SO_TIMEOUT.newSocketSetting(
                        NonNegativeInteger.valueOf(0));
        SocketSettings socketSettings = SocketSettings.of(socketSetting);
        Rule rule = new Rule.Builder().build();
        Settings settings = Settings.of(
                SocksSettingSpecConstants.SOCKS_ON_UDP_ASSOCIATE_REQUEST_PEER_FACING_SOCKET_SETTINGS.newSetting(
                        socketSettings));
        Assert.assertEquals(
                socketSettings,
                Socks5ValueDerivationHelper.getSocks5OnUdpAssociateRequestPeerFacingSocketSettingsFrom(rule, settings));
    }

    @Test
    public void testGetSocks5OnUdpAssociateRequestPeerFacingSocketSettingsFromRuleSettings11() {
        SocketSetting<?> socketSetting =
                StandardSocketSettingSpecConstants.SO_TIMEOUT.newSocketSetting(
                        NonNegativeInteger.valueOf(0));
        SocketSettings socketSettings = SocketSettings.of(socketSetting);
        Rule rule = new Rule.Builder().build();
        Settings settings = Settings.of(
                Socks5SettingSpecConstants.SOCKS5_ON_UDP_ASSOCIATE_REQUEST_PEER_FACING_SOCKET_SETTINGS.newSetting(
                        socketSettings));
        Assert.assertEquals(
                socketSettings,
                Socks5ValueDerivationHelper.getSocks5OnUdpAssociateRequestPeerFacingSocketSettingsFrom(rule, settings));
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