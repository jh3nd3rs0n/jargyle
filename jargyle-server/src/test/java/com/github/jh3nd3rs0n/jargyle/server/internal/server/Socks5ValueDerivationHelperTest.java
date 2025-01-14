package com.github.jh3nd3rs0n.jargyle.server.internal.server;

import com.github.jh3nd3rs0n.jargyle.common.net.*;
import com.github.jh3nd3rs0n.jargyle.common.number.NonNegativeInteger;
import com.github.jh3nd3rs0n.jargyle.common.number.PositiveInteger;
import com.github.jh3nd3rs0n.jargyle.server.*;
import org.junit.Assert;
import org.junit.Test;

import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;

public class Socks5ValueDerivationHelperTest {

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
        SocketSettings socketSettings = SocketSettings.of(socketSetting);
        Rule rule = new Rule.Builder()
                .addRuleAction(GeneralRuleActionSpecConstants.SOCKET_SETTING.newRuleAction(
                        (SocketSetting<Object>) socketSetting))
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
        SocketSettings socketSettings = SocketSettings.of(socketSetting);
        Rule rule = new Rule.Builder()
                .addRuleAction(GeneralRuleActionSpecConstants.EXTERNAL_FACING_SOCKET_SETTING.newRuleAction(
                        (SocketSetting<Object>) socketSetting))
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
        SocketSettings socketSettings = SocketSettings.of(socketSetting);
        Rule rule = new Rule.Builder()
                .addRuleAction(Socks5RuleActionSpecConstants.SOCKS5_ON_REQUEST_EXTERNAL_FACING_SOCKET_SETTING.newRuleAction(
                        (SocketSetting<Object>) socketSetting))
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
        SocketSettings socketSettings = SocketSettings.of(socketSetting);
        Rule rule = new Rule.Builder()
                .addRuleAction(Socks5RuleActionSpecConstants.SOCKS5_ON_BIND_REQUEST_INBOUND_SOCKET_SETTING.newRuleAction(
                        (SocketSetting<Object>) socketSetting))
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
    public void testGetSocks5OnBindRequestInboundSocketSettingsFromRuleSettings07() {
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
    public void testGetSocks5OnBindRequestInboundSocketSettingsFromRuleSettings08() {
        SocketSetting<?> socketSetting =
                StandardSocketSettingSpecConstants.SO_TIMEOUT.newSocketSetting(
                        NonNegativeInteger.valueOf(0));
        SocketSettings socketSettings = SocketSettings.of(socketSetting);
        Rule rule = new Rule.Builder().build();
        Settings settings = Settings.of(
                Socks5SettingSpecConstants.SOCKS5_ON_REQUEST_EXTERNAL_FACING_SOCKET_SETTINGS.newSetting(
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
                .addRuleAction(Socks5RuleActionSpecConstants.SOCKS5_ON_REQUEST_EXTERNAL_FACING_BIND_HOST.newRuleAction(
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
                .addRuleAction(Socks5RuleActionSpecConstants.SOCKS5_ON_BIND_REQUEST_LISTEN_BIND_HOST.newRuleAction(
                        host))
                .build();
        Settings settings = Settings.of();
        Assert.assertEquals(
                host,
                Socks5ValueDerivationHelper.getSocks5OnBindRequestListenBindHostFrom(rule, settings));
    }

    @Test
    public void testGetSocks5OnBindRequestListenBindHostFromRuleSettings06() throws UnknownHostException, SocketException {
        Host host = Host.newInstance("127.0.0.1");
        NetworkInterface networkInterface = NetworkInterface.getByInetAddress(
                host.toInetAddress());
        NetInterface netInterface = NetInterface.newInstance(networkInterface);
        Rule rule = new Rule.Builder()
                .addRuleAction(GeneralRuleActionSpecConstants.NET_INTERFACE.newRuleAction(
                        netInterface))
                .addRuleAction(GeneralRuleActionSpecConstants.BIND_HOST_ADDRESS_TYPE.newRuleAction(
                        HostAddressType.IPV4))
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
                .addRuleAction(GeneralRuleActionSpecConstants.EXTERNAL_FACING_NET_INTERFACE.newRuleAction(
                        netInterface))
                .addRuleAction(GeneralRuleActionSpecConstants.EXTERNAL_FACING_BIND_HOST_ADDRESS_TYPE.newRuleAction(
                        HostAddressType.IPV4))
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
                .addRuleAction(Socks5RuleActionSpecConstants.SOCKS5_ON_REQUEST_EXTERNAL_FACING_NET_INTERFACE.newRuleAction(
                        netInterface))
                .addRuleAction(Socks5RuleActionSpecConstants.SOCKS5_ON_REQUEST_EXTERNAL_FACING_BIND_HOST_ADDRESS_TYPE.newRuleAction(
                        HostAddressType.IPV4))
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
                .addRuleAction(Socks5RuleActionSpecConstants.SOCKS5_ON_BIND_REQUEST_LISTEN_NET_INTERFACE.newRuleAction(
                        netInterface))
                .addRuleAction(Socks5RuleActionSpecConstants.SOCKS5_ON_BIND_REQUEST_LISTEN_BIND_HOST_ADDRESS_TYPE.newRuleAction(
                        HostAddressType.IPV4))
                .build();
        Settings settings = Settings.of();
        Assert.assertEquals(
                host,
                Socks5ValueDerivationHelper.getSocks5OnBindRequestListenBindHostFrom(rule, settings));
    }

    @Test
    public void testGetSocks5OnBindRequestListenBindHostFromRuleSettings10() {
        Host host = Host.newInstance("127.0.0.1");
        Rule rule = new Rule.Builder().build();
        Settings settings = Settings.of(
                GeneralSettingSpecConstants.BIND_HOST.newSetting(host));
        Assert.assertEquals(
                host,
                Socks5ValueDerivationHelper.getSocks5OnBindRequestListenBindHostFrom(rule, settings));
    }

    @Test
    public void testGetSocks5OnBindRequestListenBindHostFromRuleSettings11() {
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
    public void testGetSocks5OnBindRequestListenBindHostFromRuleSettings12() {
        Host host = Host.newInstance("127.0.0.1");
        Rule rule = new Rule.Builder().build();
        Settings settings = Settings.of(
                Socks5SettingSpecConstants.SOCKS5_ON_REQUEST_EXTERNAL_FACING_BIND_HOST.newSetting(
                        host));
        Assert.assertEquals(
                host,
                Socks5ValueDerivationHelper.getSocks5OnBindRequestListenBindHostFrom(rule, settings));
    }

    @Test
    public void testGetSocks5OnBindRequestListenBindHostFromRuleSettings13() {
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
    public void testGetSocks5OnBindRequestListenBindHostFromRuleSettings14() throws UnknownHostException, SocketException {
        Host host = Host.newInstance("127.0.0.1");
        NetworkInterface networkInterface = NetworkInterface.getByInetAddress(
                host.toInetAddress());
        NetInterface netInterface = NetInterface.newInstance(networkInterface);
        Rule rule = new Rule.Builder().build();
        Settings settings = Settings.of(
                GeneralSettingSpecConstants.NET_INTERFACE.newSetting(
                        netInterface),
                GeneralSettingSpecConstants.BIND_HOST_ADDRESS_TYPES.newSetting(HostAddressTypes.of(
                        HostAddressType.IPV4)));
        Assert.assertEquals(
                host,
                Socks5ValueDerivationHelper.getSocks5OnBindRequestListenBindHostFrom(rule, settings));
    }

    @Test
    public void testGetSocks5OnBindRequestListenBindHostFromRuleSettings15() throws UnknownHostException, SocketException {
        Host host = Host.newInstance("127.0.0.1");
        NetworkInterface networkInterface = NetworkInterface.getByInetAddress(
                host.toInetAddress());
        NetInterface netInterface = NetInterface.newInstance(networkInterface);
        Rule rule = new Rule.Builder().build();
        Settings settings = Settings.of(
                GeneralSettingSpecConstants.EXTERNAL_FACING_NET_INTERFACE.newSetting(
                        netInterface),
                GeneralSettingSpecConstants.EXTERNAL_FACING_BIND_HOST_ADDRESS_TYPES.newSetting(HostAddressTypes.of(
                        HostAddressType.IPV4)));
        Assert.assertEquals(
                host,
                Socks5ValueDerivationHelper.getSocks5OnBindRequestListenBindHostFrom(rule, settings));
    }

    @Test
    public void testGetSocks5OnBindRequestListenBindHostFromRuleSettings16() throws UnknownHostException, SocketException {
        Host host = Host.newInstance("127.0.0.1");
        NetworkInterface networkInterface = NetworkInterface.getByInetAddress(
                host.toInetAddress());
        NetInterface netInterface = NetInterface.newInstance(networkInterface);
        Rule rule = new Rule.Builder().build();
        Settings settings = Settings.of(
                Socks5SettingSpecConstants.SOCKS5_ON_REQUEST_EXTERNAL_FACING_NET_INTERFACE.newSetting(
                        netInterface),
                Socks5SettingSpecConstants.SOCKS5_ON_REQUEST_EXTERNAL_FACING_BIND_HOST_ADDRESS_TYPES.newSetting(HostAddressTypes.of(
                        HostAddressType.IPV4)));
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
                Socks5SettingSpecConstants.SOCKS5_ON_BIND_REQUEST_LISTEN_NET_INTERFACE.newSetting(
                        netInterface),
                Socks5SettingSpecConstants.SOCKS5_ON_BIND_REQUEST_LISTEN_BIND_HOST_ADDRESS_TYPES.newSetting(HostAddressTypes.of(
                        HostAddressType.IPV4)));
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
                .addRuleAction(Socks5RuleActionSpecConstants.SOCKS5_ON_REQUEST_EXTERNAL_FACING_BIND_TCP_PORT_RANGE.newRuleAction(
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
                .addRuleAction(Socks5RuleActionSpecConstants.SOCKS5_ON_BIND_REQUEST_LISTEN_BIND_PORT_RANGE.newRuleAction(
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
        Rule rule = new Rule.Builder().build();
        Settings settings = Settings.of(
                GeneralSettingSpecConstants.BIND_TCP_PORT_RANGES.newSetting(
                        portRanges));
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
                GeneralSettingSpecConstants.EXTERNAL_FACING_BIND_TCP_PORT_RANGES.newSetting(
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
                Socks5SettingSpecConstants.SOCKS5_ON_REQUEST_EXTERNAL_FACING_BIND_TCP_PORT_RANGES.newSetting(
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
        SocketSettings socketSettings = SocketSettings.of(socketSetting);
        Rule rule = new Rule.Builder()
                .addRuleAction(GeneralRuleActionSpecConstants.SOCKET_SETTING.newRuleAction(
                        (SocketSetting<Object>) socketSetting))
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
        SocketSettings socketSettings = SocketSettings.of(socketSetting);
        Rule rule = new Rule.Builder()
                .addRuleAction(GeneralRuleActionSpecConstants.EXTERNAL_FACING_SOCKET_SETTING.newRuleAction(
                        (SocketSetting<Object>) socketSetting))
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
        SocketSettings socketSettings = SocketSettings.of(socketSetting);
        Rule rule = new Rule.Builder()
                .addRuleAction(Socks5RuleActionSpecConstants.SOCKS5_ON_REQUEST_EXTERNAL_FACING_SOCKET_SETTING.newRuleAction(
                        (SocketSetting<Object>) socketSetting))
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
        SocketSettings socketSettings = SocketSettings.of(socketSetting);
        Rule rule = new Rule.Builder()
                .addRuleAction(Socks5RuleActionSpecConstants.SOCKS5_ON_BIND_REQUEST_LISTEN_SOCKET_SETTING.newRuleAction(
                        (SocketSetting<Object>) socketSetting))
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
    public void testGetSocks5OnBindRequestListenSocketSettingsFromRuleSettings07() {
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
    public void testGetSocks5OnBindRequestListenSocketSettingsFromRuleSettings08() {
        SocketSetting<?> socketSetting =
                StandardSocketSettingSpecConstants.SO_TIMEOUT.newSocketSetting(
                        NonNegativeInteger.valueOf(0));
        SocketSettings socketSettings = SocketSettings.of(socketSetting);
        Rule rule = new Rule.Builder().build();
        Settings settings = Settings.of(
                Socks5SettingSpecConstants.SOCKS5_ON_REQUEST_EXTERNAL_FACING_SOCKET_SETTINGS.newSetting(
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
                .addRuleAction(Socks5RuleActionSpecConstants.SOCKS5_ON_REQUEST_RELAY_BUFFER_SIZE.newRuleAction(
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
                .addRuleAction(Socks5RuleActionSpecConstants.SOCKS5_ON_BIND_REQUEST_RELAY_BUFFER_SIZE.newRuleAction(
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
        Rule rule = new Rule.Builder().build();
        Settings settings = Settings.of(
                Socks5SettingSpecConstants.SOCKS5_ON_REQUEST_RELAY_BUFFER_SIZE.newSetting(
                        relayBufferSize));
        Assert.assertEquals(
                relayBufferSize,
                Socks5ValueDerivationHelper.getSocks5OnBindRequestRelayBufferSizeFrom(rule, settings));
    }

    @Test
    public void testGetSocks5OnBindRequestRelayBufferSizeFromRuleSettings05() {
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
                .addRuleAction(Socks5RuleActionSpecConstants.SOCKS5_ON_REQUEST_RELAY_IDLE_TIMEOUT.newRuleAction(
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
                .addRuleAction(Socks5RuleActionSpecConstants.SOCKS5_ON_BIND_REQUEST_RELAY_IDLE_TIMEOUT.newRuleAction(
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
        Rule rule = new Rule.Builder().build();
        Settings settings = Settings.of(
                Socks5SettingSpecConstants.SOCKS5_ON_REQUEST_RELAY_IDLE_TIMEOUT.newSetting(
                        relayIdleTimeout));
        Assert.assertEquals(
                relayIdleTimeout,
                Socks5ValueDerivationHelper.getSocks5OnBindRequestRelayIdleTimeoutFrom(rule, settings));
    }

    @Test
    public void testGetSocks5OnBindRequestRelayIdleTimeoutFromRuleSettings05() {
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
                .addRuleAction(Socks5RuleActionSpecConstants.SOCKS5_ON_REQUEST_RELAY_INBOUND_BANDWIDTH_LIMIT.newRuleAction(
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
                .addRuleAction(Socks5RuleActionSpecConstants.SOCKS5_ON_BIND_REQUEST_RELAY_INBOUND_BANDWIDTH_LIMIT.newRuleAction(
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
        Rule rule = new Rule.Builder().build();
        Settings settings = Settings.of(
                Socks5SettingSpecConstants.SOCKS5_ON_REQUEST_RELAY_INBOUND_BANDWIDTH_LIMIT.newSetting(
                        relayInboundBandwidthLimit));
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
                .addRuleAction(Socks5RuleActionSpecConstants.SOCKS5_ON_REQUEST_RELAY_OUTBOUND_BANDWIDTH_LIMIT.newRuleAction(
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
                .addRuleAction(Socks5RuleActionSpecConstants.SOCKS5_ON_BIND_REQUEST_RELAY_OUTBOUND_BANDWIDTH_LIMIT.newRuleAction(
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
        Rule rule = new Rule.Builder().build();
        Settings settings = Settings.of(
                Socks5SettingSpecConstants.SOCKS5_ON_REQUEST_RELAY_OUTBOUND_BANDWIDTH_LIMIT.newSetting(
                        relayOutboundBandwidthLimit));
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
                Socks5SettingSpecConstants.SOCKS5_ON_BIND_REQUEST_RELAY_OUTBOUND_BANDWIDTH_LIMIT.newSetting(
                        relayOutboundBandwidthLimit));
        Assert.assertEquals(
                relayOutboundBandwidthLimit,
                Socks5ValueDerivationHelper.getSocks5OnBindRequestRelayOutboundBandwidthLimitFrom(rule, settings));
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
                .addRuleAction(Socks5RuleActionSpecConstants.SOCKS5_ON_REQUEST_RELAY_BUFFER_SIZE.newRuleAction(
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
                .addRuleAction(Socks5RuleActionSpecConstants.SOCKS5_ON_CONNECT_REQUEST_RELAY_BUFFER_SIZE.newRuleAction(
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
        Rule rule = new Rule.Builder().build();
        Settings settings = Settings.of(
                Socks5SettingSpecConstants.SOCKS5_ON_REQUEST_RELAY_BUFFER_SIZE.newSetting(
                        relayBufferSize));
        Assert.assertEquals(
                relayBufferSize,
                Socks5ValueDerivationHelper.getSocks5OnConnectRequestRelayBufferSizeFrom(rule, settings));
    }

    @Test
    public void testGetSocks5OnConnectRequestRelayBufferSizeFromRuleSettings05() {
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
                .addRuleAction(Socks5RuleActionSpecConstants.SOCKS5_ON_REQUEST_RELAY_IDLE_TIMEOUT.newRuleAction(
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
                .addRuleAction(Socks5RuleActionSpecConstants.SOCKS5_ON_CONNECT_REQUEST_RELAY_IDLE_TIMEOUT.newRuleAction(
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
        Rule rule = new Rule.Builder().build();
        Settings settings = Settings.of(
                Socks5SettingSpecConstants.SOCKS5_ON_REQUEST_RELAY_IDLE_TIMEOUT.newSetting(
                        relayIdleTimeout));
        Assert.assertEquals(
                relayIdleTimeout,
                Socks5ValueDerivationHelper.getSocks5OnConnectRequestRelayIdleTimeoutFrom(rule, settings));
    }

    @Test
    public void testGetSocks5OnConnectRequestRelayIdleTimeoutFromRuleSettings05() {
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
                .addRuleAction(Socks5RuleActionSpecConstants.SOCKS5_ON_REQUEST_RELAY_INBOUND_BANDWIDTH_LIMIT.newRuleAction(
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
                .addRuleAction(Socks5RuleActionSpecConstants.SOCKS5_ON_CONNECT_REQUEST_RELAY_INBOUND_BANDWIDTH_LIMIT.newRuleAction(
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
        Rule rule = new Rule.Builder().build();
        Settings settings = Settings.of(
                Socks5SettingSpecConstants.SOCKS5_ON_REQUEST_RELAY_INBOUND_BANDWIDTH_LIMIT.newSetting(
                        relayInboundBandwidthLimit));
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
                .addRuleAction(Socks5RuleActionSpecConstants.SOCKS5_ON_REQUEST_RELAY_OUTBOUND_BANDWIDTH_LIMIT.newRuleAction(
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
                .addRuleAction(Socks5RuleActionSpecConstants.SOCKS5_ON_CONNECT_REQUEST_RELAY_OUTBOUND_BANDWIDTH_LIMIT.newRuleAction(
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
        Rule rule = new Rule.Builder().build();
        Settings settings = Settings.of(
                Socks5SettingSpecConstants.SOCKS5_ON_REQUEST_RELAY_OUTBOUND_BANDWIDTH_LIMIT.newSetting(
                        relayOutboundBandwidthLimit));
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
                .addRuleAction(Socks5RuleActionSpecConstants.SOCKS5_ON_REQUEST_EXTERNAL_FACING_BIND_HOST.newRuleAction(
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
                .addRuleAction(Socks5RuleActionSpecConstants.SOCKS5_ON_CONNECT_REQUEST_TARGET_FACING_BIND_HOST.newRuleAction(
                        host))
                .build();
        Settings settings = Settings.of();
        Assert.assertEquals(
                host,
                Socks5ValueDerivationHelper.getSocks5OnConnectRequestTargetFacingBindHostFrom(rule, settings));
    }

    @Test
    public void testGetSocks5OnConnectRequestTargetFacingBindHostFromRuleSettings06() throws UnknownHostException, SocketException {
        Host host = Host.newInstance("127.0.0.1");
        NetworkInterface networkInterface = NetworkInterface.getByInetAddress(
                host.toInetAddress());
        NetInterface netInterface = NetInterface.newInstance(networkInterface);
        Rule rule = new Rule.Builder()
                .addRuleAction(GeneralRuleActionSpecConstants.NET_INTERFACE.newRuleAction(
                        netInterface))
                .addRuleAction(GeneralRuleActionSpecConstants.BIND_HOST_ADDRESS_TYPE.newRuleAction(
                        HostAddressType.IPV4))
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
                .addRuleAction(GeneralRuleActionSpecConstants.EXTERNAL_FACING_NET_INTERFACE.newRuleAction(
                        netInterface))
                .addRuleAction(GeneralRuleActionSpecConstants.EXTERNAL_FACING_BIND_HOST_ADDRESS_TYPE.newRuleAction(
                        HostAddressType.IPV4))
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
                .addRuleAction(Socks5RuleActionSpecConstants.SOCKS5_ON_REQUEST_EXTERNAL_FACING_NET_INTERFACE.newRuleAction(
                        netInterface))
                .addRuleAction(Socks5RuleActionSpecConstants.SOCKS5_ON_REQUEST_EXTERNAL_FACING_BIND_HOST_ADDRESS_TYPE.newRuleAction(
                        HostAddressType.IPV4))
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
                .addRuleAction(Socks5RuleActionSpecConstants.SOCKS5_ON_CONNECT_REQUEST_TARGET_FACING_NET_INTERFACE.newRuleAction(
                        netInterface))
                .addRuleAction(Socks5RuleActionSpecConstants.SOCKS5_ON_CONNECT_REQUEST_TARGET_FACING_BIND_HOST_ADDRESS_TYPE.newRuleAction(
                        HostAddressType.IPV4))
                .build();
        Settings settings = Settings.of();
        Assert.assertEquals(
                host,
                Socks5ValueDerivationHelper.getSocks5OnConnectRequestTargetFacingBindHostFrom(rule, settings));
    }

    @Test
    public void testGetSocks5OnConnectRequestTargetFacingBindHostFromRuleSettings10() {
        Host host = Host.newInstance("127.0.0.1");
        Rule rule = new Rule.Builder().build();
        Settings settings = Settings.of(
                GeneralSettingSpecConstants.BIND_HOST.newSetting(host));
        Assert.assertEquals(
                host,
                Socks5ValueDerivationHelper.getSocks5OnConnectRequestTargetFacingBindHostFrom(rule, settings));
    }

    @Test
    public void testGetSocks5OnConnectRequestTargetFacingBindHostFromRuleSettings11() {
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
    public void testGetSocks5OnConnectRequestTargetFacingBindHostFromRuleSettings12() {
        Host host = Host.newInstance("127.0.0.1");
        Rule rule = new Rule.Builder().build();
        Settings settings = Settings.of(
                Socks5SettingSpecConstants.SOCKS5_ON_REQUEST_EXTERNAL_FACING_BIND_HOST.newSetting(
                        host));
        Assert.assertEquals(
                host,
                Socks5ValueDerivationHelper.getSocks5OnConnectRequestTargetFacingBindHostFrom(rule, settings));
    }

    @Test
    public void testGetSocks5OnConnectRequestTargetFacingBindHostFromRuleSettings13() {
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
    public void testGetSocks5OnConnectRequestTargetFacingBindHostFromRuleSettings14() throws UnknownHostException, SocketException {
        Host host = Host.newInstance("127.0.0.1");
        NetworkInterface networkInterface = NetworkInterface.getByInetAddress(
                host.toInetAddress());
        NetInterface netInterface = NetInterface.newInstance(networkInterface);
        Rule rule = new Rule.Builder().build();
        Settings settings = Settings.of(
                GeneralSettingSpecConstants.NET_INTERFACE.newSetting(
                        netInterface),
                GeneralSettingSpecConstants.BIND_HOST_ADDRESS_TYPES.newSetting(HostAddressTypes.of(
                        HostAddressType.IPV4)));
        Assert.assertEquals(
                host,
                Socks5ValueDerivationHelper.getSocks5OnConnectRequestTargetFacingBindHostFrom(rule, settings));
    }

    @Test
    public void testGetSocks5OnConnectRequestTargetFacingBindHostFromRuleSettings15() throws UnknownHostException, SocketException {
        Host host = Host.newInstance("127.0.0.1");
        NetworkInterface networkInterface = NetworkInterface.getByInetAddress(
                host.toInetAddress());
        NetInterface netInterface = NetInterface.newInstance(networkInterface);
        Rule rule = new Rule.Builder().build();
        Settings settings = Settings.of(
                GeneralSettingSpecConstants.EXTERNAL_FACING_NET_INTERFACE.newSetting(
                        netInterface),
                GeneralSettingSpecConstants.EXTERNAL_FACING_BIND_HOST_ADDRESS_TYPES.newSetting(HostAddressTypes.of(
                        HostAddressType.IPV4)));
        Assert.assertEquals(
                host,
                Socks5ValueDerivationHelper.getSocks5OnConnectRequestTargetFacingBindHostFrom(rule, settings));
    }

    @Test
    public void testGetSocks5OnConnectRequestTargetFacingBindHostFromRuleSettings16() throws UnknownHostException, SocketException {
        Host host = Host.newInstance("127.0.0.1");
        NetworkInterface networkInterface = NetworkInterface.getByInetAddress(
                host.toInetAddress());
        NetInterface netInterface = NetInterface.newInstance(networkInterface);
        Rule rule = new Rule.Builder().build();
        Settings settings = Settings.of(
                Socks5SettingSpecConstants.SOCKS5_ON_REQUEST_EXTERNAL_FACING_NET_INTERFACE.newSetting(
                        netInterface),
                Socks5SettingSpecConstants.SOCKS5_ON_REQUEST_EXTERNAL_FACING_BIND_HOST_ADDRESS_TYPES.newSetting(HostAddressTypes.of(
                        HostAddressType.IPV4)));
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
                Socks5SettingSpecConstants.SOCKS5_ON_CONNECT_REQUEST_TARGET_FACING_NET_INTERFACE.newSetting(
                        netInterface),
                Socks5SettingSpecConstants.SOCKS5_ON_CONNECT_REQUEST_TARGET_FACING_BIND_HOST_ADDRESS_TYPES.newSetting(HostAddressTypes.of(
                        HostAddressType.IPV4)));
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
                .addRuleAction(Socks5RuleActionSpecConstants.SOCKS5_ON_REQUEST_EXTERNAL_FACING_BIND_TCP_PORT_RANGE.newRuleAction(
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
                .addRuleAction(Socks5RuleActionSpecConstants.SOCKS5_ON_CONNECT_REQUEST_TARGET_FACING_BIND_PORT_RANGE.newRuleAction(
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
        Rule rule = new Rule.Builder().build();
        Settings settings = Settings.of(
                GeneralSettingSpecConstants.BIND_TCP_PORT_RANGES.newSetting(
                        portRanges));
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
                GeneralSettingSpecConstants.EXTERNAL_FACING_BIND_TCP_PORT_RANGES.newSetting(
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
                Socks5SettingSpecConstants.SOCKS5_ON_REQUEST_EXTERNAL_FACING_BIND_TCP_PORT_RANGES.newSetting(
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
                Socks5SettingSpecConstants.SOCKS5_ON_CONNECT_REQUEST_TARGET_FACING_BIND_PORT_RANGES.newSetting(
                        portRanges));
        Assert.assertEquals(
                portRanges,
                Socks5ValueDerivationHelper.getSocks5OnConnectRequestTargetFacingBindPortRangesFrom(rule, settings));
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
        SocketSettings socketSettings = SocketSettings.of(socketSetting);
        Rule rule = new Rule.Builder()
                .addRuleAction(GeneralRuleActionSpecConstants.SOCKET_SETTING.newRuleAction(
                        (SocketSetting<Object>) socketSetting))
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
        SocketSettings socketSettings = SocketSettings.of(socketSetting);
        Rule rule = new Rule.Builder()
                .addRuleAction(GeneralRuleActionSpecConstants.EXTERNAL_FACING_SOCKET_SETTING.newRuleAction(
                        (SocketSetting<Object>) socketSetting))
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
        SocketSettings socketSettings = SocketSettings.of(socketSetting);
        Rule rule = new Rule.Builder()
                .addRuleAction(Socks5RuleActionSpecConstants.SOCKS5_ON_REQUEST_EXTERNAL_FACING_SOCKET_SETTING.newRuleAction(
                        (SocketSetting<Object>) socketSetting))
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
        SocketSettings socketSettings = SocketSettings.of(socketSetting);
        Rule rule = new Rule.Builder()
                .addRuleAction(Socks5RuleActionSpecConstants.SOCKS5_ON_CONNECT_REQUEST_TARGET_FACING_SOCKET_SETTING.newRuleAction(
                        (SocketSetting<Object>) socketSetting))
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
    public void testGetSocks5OnConnectRequestTargetFacingSocketSettingsFromRuleSettings07() {
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
    public void testGetSocks5OnConnectRequestTargetFacingSocketSettingsFromRuleSettings08() {
        SocketSetting<?> socketSetting =
                StandardSocketSettingSpecConstants.SO_TIMEOUT.newSocketSetting(
                        NonNegativeInteger.valueOf(0));
        SocketSettings socketSettings = SocketSettings.of(socketSetting);
        Rule rule = new Rule.Builder().build();
        Settings settings = Settings.of(
                Socks5SettingSpecConstants.SOCKS5_ON_REQUEST_EXTERNAL_FACING_SOCKET_SETTINGS.newSetting(
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
                .addRuleAction(Socks5RuleActionSpecConstants.SOCKS5_ON_REQUEST_INTERNAL_FACING_BIND_HOST.newRuleAction(
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
                .addRuleAction(Socks5RuleActionSpecConstants.SOCKS5_ON_UDP_ASSOCIATE_REQUEST_CLIENT_FACING_BIND_HOST.newRuleAction(
                        host))
                .build();
        Settings settings = Settings.of();
        Assert.assertEquals(
                host,
                Socks5ValueDerivationHelper.getSocks5OnUdpAssociateRequestClientFacingBindHostFrom(rule, settings));
    }

    @Test
    public void testGetSocks5OnUdpAssociateRequestClientFacingBindHostFromRuleSettings06() throws UnknownHostException, SocketException {
        Host host = Host.newInstance("127.0.0.1");
        NetworkInterface networkInterface = NetworkInterface.getByInetAddress(
                host.toInetAddress());
        NetInterface netInterface = NetInterface.newInstance(networkInterface);
        Rule rule = new Rule.Builder()
                .addRuleAction(GeneralRuleActionSpecConstants.NET_INTERFACE.newRuleAction(
                        netInterface))
                .addRuleAction(GeneralRuleActionSpecConstants.BIND_HOST_ADDRESS_TYPE.newRuleAction(
                        HostAddressType.IPV4))
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
                .addRuleAction(GeneralRuleActionSpecConstants.INTERNAL_FACING_NET_INTERFACE.newRuleAction(
                        netInterface))
                .addRuleAction(GeneralRuleActionSpecConstants.INTERNAL_FACING_BIND_HOST_ADDRESS_TYPE.newRuleAction(
                        HostAddressType.IPV4))
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
                .addRuleAction(Socks5RuleActionSpecConstants.SOCKS5_ON_REQUEST_INTERNAL_FACING_NET_INTERFACE.newRuleAction(
                        netInterface))
                .addRuleAction(Socks5RuleActionSpecConstants.SOCKS5_ON_REQUEST_INTERNAL_FACING_BIND_HOST_ADDRESS_TYPE.newRuleAction(
                        HostAddressType.IPV4))
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
                .addRuleAction(Socks5RuleActionSpecConstants.SOCKS5_ON_UDP_ASSOCIATE_REQUEST_CLIENT_FACING_NET_INTERFACE.newRuleAction(
                        netInterface))
                .addRuleAction(Socks5RuleActionSpecConstants.SOCKS5_ON_UDP_ASSOCIATE_REQUEST_CLIENT_FACING_BIND_HOST_ADDRESS_TYPE.newRuleAction(
                        HostAddressType.IPV4))
                .build();
        Settings settings = Settings.of();
        Assert.assertEquals(
                host,
                Socks5ValueDerivationHelper.getSocks5OnUdpAssociateRequestClientFacingBindHostFrom(rule, settings));
    }

    @Test
    public void testGetSocks5OnUdpAssociateRequestClientFacingBindHostFromRuleSettings10() {
        Host host = Host.newInstance("127.0.0.1");
        Rule rule = new Rule.Builder().build();
        Settings settings = Settings.of(
                GeneralSettingSpecConstants.BIND_HOST.newSetting(host));
        Assert.assertEquals(
                host,
                Socks5ValueDerivationHelper.getSocks5OnUdpAssociateRequestClientFacingBindHostFrom(rule, settings));
    }

    @Test
    public void testGetSocks5OnUdpAssociateRequestClientFacingBindHostFromRuleSettings11() {
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
    public void testGetSocks5OnUdpAssociateRequestClientFacingBindHostFromRuleSettings12() {
        Host host = Host.newInstance("127.0.0.1");
        Rule rule = new Rule.Builder().build();
        Settings settings = Settings.of(
                Socks5SettingSpecConstants.SOCKS5_ON_REQUEST_INTERNAL_FACING_BIND_HOST.newSetting(
                        host));
        Assert.assertEquals(
                host,
                Socks5ValueDerivationHelper.getSocks5OnUdpAssociateRequestClientFacingBindHostFrom(rule, settings));
    }

    @Test
    public void testGetSocks5OnUdpAssociateRequestClientFacingBindHostFromRuleSettings13() {
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
    public void testGetSocks5OnUdpAssociateRequestClientFacingBindHostFromRuleSettings14() throws UnknownHostException, SocketException {
        Host host = Host.newInstance("127.0.0.1");
        NetworkInterface networkInterface = NetworkInterface.getByInetAddress(
                host.toInetAddress());
        NetInterface netInterface = NetInterface.newInstance(networkInterface);
        Rule rule = new Rule.Builder().build();
        Settings settings = Settings.of(
                GeneralSettingSpecConstants.NET_INTERFACE.newSetting(
                        netInterface),
                GeneralSettingSpecConstants.BIND_HOST_ADDRESS_TYPES.newSetting(HostAddressTypes.of(
                        HostAddressType.IPV4)));
        Assert.assertEquals(
                host,
                Socks5ValueDerivationHelper.getSocks5OnUdpAssociateRequestClientFacingBindHostFrom(rule, settings));
    }

    @Test
    public void testGetSocks5OnUdpAssociateRequestClientFacingBindHostFromRuleSettings15() throws UnknownHostException, SocketException {
        Host host = Host.newInstance("127.0.0.1");
        NetworkInterface networkInterface = NetworkInterface.getByInetAddress(
                host.toInetAddress());
        NetInterface netInterface = NetInterface.newInstance(networkInterface);
        Rule rule = new Rule.Builder().build();
        Settings settings = Settings.of(
                GeneralSettingSpecConstants.INTERNAL_FACING_NET_INTERFACE.newSetting(
                        netInterface),
                GeneralSettingSpecConstants.INTERNAL_FACING_BIND_HOST_ADDRESS_TYPES.newSetting(HostAddressTypes.of(
                        HostAddressType.IPV4)));
        Assert.assertEquals(
                host,
                Socks5ValueDerivationHelper.getSocks5OnUdpAssociateRequestClientFacingBindHostFrom(rule, settings));
    }

    @Test
    public void testGetSocks5OnUdpAssociateRequestClientFacingBindHostFromRuleSettings16() throws UnknownHostException, SocketException {
        Host host = Host.newInstance("127.0.0.1");
        NetworkInterface networkInterface = NetworkInterface.getByInetAddress(
                host.toInetAddress());
        NetInterface netInterface = NetInterface.newInstance(networkInterface);
        Rule rule = new Rule.Builder().build();
        Settings settings = Settings.of(
                Socks5SettingSpecConstants.SOCKS5_ON_REQUEST_INTERNAL_FACING_NET_INTERFACE.newSetting(
                        netInterface),
                Socks5SettingSpecConstants.SOCKS5_ON_REQUEST_INTERNAL_FACING_BIND_HOST_ADDRESS_TYPES.newSetting(HostAddressTypes.of(
                        HostAddressType.IPV4)));
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
                Socks5SettingSpecConstants.SOCKS5_ON_UDP_ASSOCIATE_REQUEST_CLIENT_FACING_NET_INTERFACE.newSetting(
                        netInterface),
                Socks5SettingSpecConstants.SOCKS5_ON_UDP_ASSOCIATE_REQUEST_CLIENT_FACING_BIND_HOST_ADDRESS_TYPES.newSetting(HostAddressTypes.of(
                        HostAddressType.IPV4)));
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
                .addRuleAction(Socks5RuleActionSpecConstants.SOCKS5_ON_REQUEST_INTERNAL_FACING_BIND_UDP_PORT_RANGE.newRuleAction(
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
                .addRuleAction(Socks5RuleActionSpecConstants.SOCKS5_ON_UDP_ASSOCIATE_REQUEST_CLIENT_FACING_BIND_PORT_RANGE.newRuleAction(
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
        Rule rule = new Rule.Builder().build();
        Settings settings = Settings.of(
                GeneralSettingSpecConstants.BIND_UDP_PORT_RANGES.newSetting(
                        portRanges));
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
                GeneralSettingSpecConstants.INTERNAL_FACING_BIND_UDP_PORT_RANGES.newSetting(
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
                Socks5SettingSpecConstants.SOCKS5_ON_REQUEST_INTERNAL_FACING_BIND_UDP_PORT_RANGES.newSetting(
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
        SocketSettings socketSettings = SocketSettings.of(socketSetting);
        Rule rule = new Rule.Builder()
                .addRuleAction(GeneralRuleActionSpecConstants.SOCKET_SETTING.newRuleAction(
                        (SocketSetting<Object>) socketSetting))
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
        SocketSettings socketSettings = SocketSettings.of(socketSetting);
        Rule rule = new Rule.Builder()
                .addRuleAction(GeneralRuleActionSpecConstants.INTERNAL_FACING_SOCKET_SETTING.newRuleAction(
                        (SocketSetting<Object>) socketSetting))
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
        SocketSettings socketSettings = SocketSettings.of(socketSetting);
        Rule rule = new Rule.Builder()
                .addRuleAction(Socks5RuleActionSpecConstants.SOCKS5_ON_REQUEST_INTERNAL_FACING_SOCKET_SETTING.newRuleAction(
                        (SocketSetting<Object>) socketSetting))
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
        SocketSettings socketSettings = SocketSettings.of(socketSetting);
        Rule rule = new Rule.Builder()
                .addRuleAction(Socks5RuleActionSpecConstants.SOCKS5_ON_UDP_ASSOCIATE_REQUEST_CLIENT_FACING_SOCKET_SETTING.newRuleAction(
                        (SocketSetting<Object>) socketSetting))
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
    public void testGetSocks5OnUdpAssociateRequestClientFacingSocketSettingsFromRuleSettings07() {
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
    public void testGetSocks5OnUdpAssociateRequestClientFacingSocketSettingsFromRuleSettings08() {
        SocketSetting<?> socketSetting =
                StandardSocketSettingSpecConstants.SO_TIMEOUT.newSocketSetting(
                        NonNegativeInteger.valueOf(0));
        SocketSettings socketSettings = SocketSettings.of(socketSetting);
        Rule rule = new Rule.Builder().build();
        Settings settings = Settings.of(
                Socks5SettingSpecConstants.SOCKS5_ON_REQUEST_INTERNAL_FACING_SOCKET_SETTINGS.newSetting(
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
                .addRuleAction(Socks5RuleActionSpecConstants.SOCKS5_ON_REQUEST_EXTERNAL_FACING_BIND_HOST.newRuleAction(
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
                .addRuleAction(Socks5RuleActionSpecConstants.SOCKS5_ON_UDP_ASSOCIATE_REQUEST_PEER_FACING_BIND_HOST.newRuleAction(
                        host))
                .build();
        Settings settings = Settings.of();
        Assert.assertEquals(
                host,
                Socks5ValueDerivationHelper.getSocks5OnUdpAssociateRequestPeerFacingBindHostFrom(rule, settings));
    }

    @Test
    public void testGetSocks5OnUdpAssociateRequestPeerFacingBindHostFromRuleSettings06() throws UnknownHostException, SocketException {
        Host host = Host.newInstance("127.0.0.1");
        NetworkInterface networkInterface = NetworkInterface.getByInetAddress(
                host.toInetAddress());
        NetInterface netInterface = NetInterface.newInstance(networkInterface);
        Rule rule = new Rule.Builder()
                .addRuleAction(GeneralRuleActionSpecConstants.NET_INTERFACE.newRuleAction(
                        netInterface))
                .addRuleAction(GeneralRuleActionSpecConstants.BIND_HOST_ADDRESS_TYPE.newRuleAction(
                        HostAddressType.IPV4))
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
                .addRuleAction(GeneralRuleActionSpecConstants.EXTERNAL_FACING_NET_INTERFACE.newRuleAction(
                        netInterface))
                .addRuleAction(GeneralRuleActionSpecConstants.EXTERNAL_FACING_BIND_HOST_ADDRESS_TYPE.newRuleAction(
                        HostAddressType.IPV4))
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
                .addRuleAction(Socks5RuleActionSpecConstants.SOCKS5_ON_REQUEST_EXTERNAL_FACING_NET_INTERFACE.newRuleAction(
                        netInterface))
                .addRuleAction(Socks5RuleActionSpecConstants.SOCKS5_ON_REQUEST_EXTERNAL_FACING_BIND_HOST_ADDRESS_TYPE.newRuleAction(
                        HostAddressType.IPV4))
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
                .addRuleAction(Socks5RuleActionSpecConstants.SOCKS5_ON_UDP_ASSOCIATE_REQUEST_PEER_FACING_NET_INTERFACE.newRuleAction(
                        netInterface))
                .addRuleAction(Socks5RuleActionSpecConstants.SOCKS5_ON_UDP_ASSOCIATE_REQUEST_PEER_FACING_BIND_HOST_ADDRESS_TYPE.newRuleAction(
                        HostAddressType.IPV4))
                .build();
        Settings settings = Settings.of();
        Assert.assertEquals(
                host,
                Socks5ValueDerivationHelper.getSocks5OnUdpAssociateRequestPeerFacingBindHostFrom(rule, settings));
    }

    @Test
    public void testGetSocks5OnUdpAssociateRequestPeerFacingBindHostFromRuleSettings10() {
        Host host = Host.newInstance("127.0.0.1");
        Rule rule = new Rule.Builder().build();
        Settings settings = Settings.of(
                GeneralSettingSpecConstants.BIND_HOST.newSetting(host));
        Assert.assertEquals(
                host,
                Socks5ValueDerivationHelper.getSocks5OnUdpAssociateRequestPeerFacingBindHostFrom(rule, settings));
    }

    @Test
    public void testGetSocks5OnUdpAssociateRequestPeerFacingBindHostFromRuleSettings11() {
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
    public void testGetSocks5OnUdpAssociateRequestPeerFacingBindHostFromRuleSettings12() {
        Host host = Host.newInstance("127.0.0.1");
        Rule rule = new Rule.Builder().build();
        Settings settings = Settings.of(
                Socks5SettingSpecConstants.SOCKS5_ON_REQUEST_EXTERNAL_FACING_BIND_HOST.newSetting(
                        host));
        Assert.assertEquals(
                host,
                Socks5ValueDerivationHelper.getSocks5OnUdpAssociateRequestPeerFacingBindHostFrom(rule, settings));
    }

    @Test
    public void testGetSocks5OnUdpAssociateRequestPeerFacingBindHostFromRuleSettings13() {
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
    public void testGetSocks5OnUdpAssociateRequestPeerFacingBindHostFromRuleSettings14() throws UnknownHostException, SocketException {
        Host host = Host.newInstance("127.0.0.1");
        NetworkInterface networkInterface = NetworkInterface.getByInetAddress(
                host.toInetAddress());
        NetInterface netInterface = NetInterface.newInstance(networkInterface);
        Rule rule = new Rule.Builder().build();
        Settings settings = Settings.of(
                GeneralSettingSpecConstants.NET_INTERFACE.newSetting(
                        netInterface),
                GeneralSettingSpecConstants.BIND_HOST_ADDRESS_TYPES.newSetting(HostAddressTypes.of(
                        HostAddressType.IPV4)));
        Assert.assertEquals(
                host,
                Socks5ValueDerivationHelper.getSocks5OnUdpAssociateRequestPeerFacingBindHostFrom(rule, settings));
    }

    @Test
    public void testGetSocks5OnUdpAssociateRequestPeerFacingBindHostFromRuleSettings15() throws UnknownHostException, SocketException {
        Host host = Host.newInstance("127.0.0.1");
        NetworkInterface networkInterface = NetworkInterface.getByInetAddress(
                host.toInetAddress());
        NetInterface netInterface = NetInterface.newInstance(networkInterface);
        Rule rule = new Rule.Builder().build();
        Settings settings = Settings.of(
                GeneralSettingSpecConstants.EXTERNAL_FACING_NET_INTERFACE.newSetting(
                        netInterface),
                GeneralSettingSpecConstants.EXTERNAL_FACING_BIND_HOST_ADDRESS_TYPES.newSetting(HostAddressTypes.of(
                        HostAddressType.IPV4)));
        Assert.assertEquals(
                host,
                Socks5ValueDerivationHelper.getSocks5OnUdpAssociateRequestPeerFacingBindHostFrom(rule, settings));
    }

    @Test
    public void testGetSocks5OnUdpAssociateRequestPeerFacingBindHostFromRuleSettings16() throws UnknownHostException, SocketException {
        Host host = Host.newInstance("127.0.0.1");
        NetworkInterface networkInterface = NetworkInterface.getByInetAddress(
                host.toInetAddress());
        NetInterface netInterface = NetInterface.newInstance(networkInterface);
        Rule rule = new Rule.Builder().build();
        Settings settings = Settings.of(
                Socks5SettingSpecConstants.SOCKS5_ON_REQUEST_EXTERNAL_FACING_NET_INTERFACE.newSetting(
                        netInterface),
                Socks5SettingSpecConstants.SOCKS5_ON_REQUEST_EXTERNAL_FACING_BIND_HOST_ADDRESS_TYPES.newSetting(HostAddressTypes.of(
                        HostAddressType.IPV4)));
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
                Socks5SettingSpecConstants.SOCKS5_ON_UDP_ASSOCIATE_REQUEST_PEER_FACING_NET_INTERFACE.newSetting(
                        netInterface),
                Socks5SettingSpecConstants.SOCKS5_ON_UDP_ASSOCIATE_REQUEST_PEER_FACING_BIND_HOST_ADDRESS_TYPES.newSetting(HostAddressTypes.of(
                        HostAddressType.IPV4)));
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
                .addRuleAction(Socks5RuleActionSpecConstants.SOCKS5_ON_REQUEST_EXTERNAL_FACING_BIND_UDP_PORT_RANGE.newRuleAction(
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
                .addRuleAction(Socks5RuleActionSpecConstants.SOCKS5_ON_UDP_ASSOCIATE_REQUEST_PEER_FACING_BIND_PORT_RANGE.newRuleAction(
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
        Rule rule = new Rule.Builder().build();
        Settings settings = Settings.of(
                GeneralSettingSpecConstants.BIND_UDP_PORT_RANGES.newSetting(
                        portRanges));
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
                GeneralSettingSpecConstants.EXTERNAL_FACING_BIND_UDP_PORT_RANGES.newSetting(
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
                Socks5SettingSpecConstants.SOCKS5_ON_REQUEST_EXTERNAL_FACING_BIND_UDP_PORT_RANGES.newSetting(
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
        SocketSettings socketSettings = SocketSettings.of(socketSetting);
        Rule rule = new Rule.Builder()
                .addRuleAction(GeneralRuleActionSpecConstants.SOCKET_SETTING.newRuleAction(
                        (SocketSetting<Object>) socketSetting))
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
        SocketSettings socketSettings = SocketSettings.of(socketSetting);
        Rule rule = new Rule.Builder()
                .addRuleAction(GeneralRuleActionSpecConstants.EXTERNAL_FACING_SOCKET_SETTING.newRuleAction(
                        (SocketSetting<Object>) socketSetting))
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
        SocketSettings socketSettings = SocketSettings.of(socketSetting);
        Rule rule = new Rule.Builder()
                .addRuleAction(Socks5RuleActionSpecConstants.SOCKS5_ON_REQUEST_EXTERNAL_FACING_SOCKET_SETTING.newRuleAction(
                        (SocketSetting<Object>) socketSetting))
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
        SocketSettings socketSettings = SocketSettings.of(socketSetting);
        Rule rule = new Rule.Builder()
                .addRuleAction(Socks5RuleActionSpecConstants.SOCKS5_ON_UDP_ASSOCIATE_REQUEST_PEER_FACING_SOCKET_SETTING.newRuleAction(
                        (SocketSetting<Object>) socketSetting))
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
    public void testGetSocks5OnUdpAssociateRequestPeerFacingSocketSettingsFromRuleSettings07() {
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
    public void testGetSocks5OnUdpAssociateRequestPeerFacingSocketSettingsFromRuleSettings08() {
        SocketSetting<?> socketSetting =
                StandardSocketSettingSpecConstants.SO_TIMEOUT.newSocketSetting(
                        NonNegativeInteger.valueOf(0));
        SocketSettings socketSettings = SocketSettings.of(socketSetting);
        Rule rule = new Rule.Builder().build();
        Settings settings = Settings.of(
                Socks5SettingSpecConstants.SOCKS5_ON_REQUEST_EXTERNAL_FACING_SOCKET_SETTINGS.newSetting(
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
                .addRuleAction(Socks5RuleActionSpecConstants.SOCKS5_ON_REQUEST_RELAY_BUFFER_SIZE.newRuleAction(
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
                .addRuleAction(Socks5RuleActionSpecConstants.SOCKS5_ON_UDP_ASSOCIATE_REQUEST_RELAY_BUFFER_SIZE.newRuleAction(
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
        Rule rule = new Rule.Builder().build();
        Settings settings = Settings.of(
                Socks5SettingSpecConstants.SOCKS5_ON_REQUEST_RELAY_BUFFER_SIZE.newSetting(
                        relayBufferSize));
        Assert.assertEquals(
                relayBufferSize,
                Socks5ValueDerivationHelper.getSocks5OnUdpAssociateRequestRelayBufferSizeFrom(rule, settings));
    }

    @Test
    public void testGetSocks5OnUdpAssociateRequestRelayBufferSizeFromRuleSettings05() {
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
                .addRuleAction(Socks5RuleActionSpecConstants.SOCKS5_ON_REQUEST_RELAY_IDLE_TIMEOUT.newRuleAction(
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
                .addRuleAction(Socks5RuleActionSpecConstants.SOCKS5_ON_UDP_ASSOCIATE_REQUEST_RELAY_IDLE_TIMEOUT.newRuleAction(
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
        Rule rule = new Rule.Builder().build();
        Settings settings = Settings.of(
                Socks5SettingSpecConstants.SOCKS5_ON_REQUEST_RELAY_IDLE_TIMEOUT.newSetting(
                        relayIdleTimeout));
        Assert.assertEquals(
                relayIdleTimeout,
                Socks5ValueDerivationHelper.getSocks5OnUdpAssociateRequestRelayIdleTimeoutFrom(rule, settings));
    }

    @Test
    public void testGetSocks5OnUdpAssociateRequestRelayIdleTimeoutFromRuleSettings05() {
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
                .addRuleAction(Socks5RuleActionSpecConstants.SOCKS5_ON_REQUEST_RELAY_INBOUND_BANDWIDTH_LIMIT.newRuleAction(
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
                .addRuleAction(Socks5RuleActionSpecConstants.SOCKS5_ON_UDP_ASSOCIATE_REQUEST_RELAY_INBOUND_BANDWIDTH_LIMIT.newRuleAction(
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
        Rule rule = new Rule.Builder().build();
        Settings settings = Settings.of(
                Socks5SettingSpecConstants.SOCKS5_ON_REQUEST_RELAY_INBOUND_BANDWIDTH_LIMIT.newSetting(
                        relayInboundBandwidthLimit));
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
                .addRuleAction(Socks5RuleActionSpecConstants.SOCKS5_ON_REQUEST_RELAY_OUTBOUND_BANDWIDTH_LIMIT.newRuleAction(
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
                .addRuleAction(Socks5RuleActionSpecConstants.SOCKS5_ON_UDP_ASSOCIATE_REQUEST_RELAY_OUTBOUND_BANDWIDTH_LIMIT.newRuleAction(
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
        Rule rule = new Rule.Builder().build();
        Settings settings = Settings.of(
                Socks5SettingSpecConstants.SOCKS5_ON_REQUEST_RELAY_OUTBOUND_BANDWIDTH_LIMIT.newSetting(
                        relayOutboundBandwidthLimit));
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
                Socks5SettingSpecConstants.SOCKS5_ON_UDP_ASSOCIATE_REQUEST_RELAY_OUTBOUND_BANDWIDTH_LIMIT.newSetting(
                        relayOutboundBandwidthLimit));
        Assert.assertEquals(
                relayOutboundBandwidthLimit,
                Socks5ValueDerivationHelper.getSocks5OnUdpAssociateRequestRelayOutboundBandwidthLimitFrom(rule, settings));
    }

}