package com.github.jh3nd3rs0n.jargyle.server.internal.server;

import com.github.jh3nd3rs0n.jargyle.common.net.*;
import com.github.jh3nd3rs0n.jargyle.common.number.NonNegativeInteger;
import com.github.jh3nd3rs0n.jargyle.server.*;
import org.junit.Assert;
import org.junit.Test;

import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;

public class GeneralValueDerivationHelperTest {

    @Test
    public void testGetClientSocketSettingsFromRuleSettings01() {
        SocketSettings socketSettings = SocketSettings.of();
        Rule rule = new Rule.Builder().build();
        Settings settings = Settings.of();
        Assert.assertEquals(
                socketSettings,
                GeneralValueDerivationHelper.getClientSocketSettingsFrom(rule, settings));
    }

    @Test
    public void testGetClientSocketSettingsFromRuleSettings02() {
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
                GeneralValueDerivationHelper.getClientSocketSettingsFrom(rule, settings));
    }

    @Test
    public void testGetClientSocketSettingsFromRuleSettings03() {
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
                GeneralValueDerivationHelper.getClientSocketSettingsFrom(rule, settings));
    }

    @Test
    public void testGetClientSocketSettingsFromRuleSettings04() {
        SocketSetting<?> socketSetting =
                StandardSocketSettingSpecConstants.SO_TIMEOUT.newSocketSetting(
                        NonNegativeInteger.valueOf(0));
        SocketSettings socketSettings = SocketSettings.of(socketSetting);
        Rule rule = new Rule.Builder()
                .addRuleAction(GeneralRuleActionSpecConstants.CLIENT_SOCKET_SETTING.newRuleAction(
                        (SocketSetting<Object>) socketSetting))
                .build();
        Settings settings = Settings.of();
        Assert.assertEquals(
                socketSettings,
                GeneralValueDerivationHelper.getClientSocketSettingsFrom(rule, settings));
    }

    @Test
    public void testGetClientSocketSettingsFromRuleSettings05() {
        SocketSetting<?> socketSetting =
                StandardSocketSettingSpecConstants.SO_TIMEOUT.newSocketSetting(
                        NonNegativeInteger.valueOf(0));
        SocketSettings socketSettings = SocketSettings.of(socketSetting);
        Rule rule = new Rule.Builder().build();
        Settings settings = Settings.of(
                GeneralSettingSpecConstants.SOCKET_SETTINGS.newSetting(socketSettings));
        Assert.assertEquals(
                socketSettings,
                GeneralValueDerivationHelper.getClientSocketSettingsFrom(rule, settings));
    }

    @Test
    public void testGetClientSocketSettingsFromRuleSettings06() {
        SocketSetting<?> socketSetting =
                StandardSocketSettingSpecConstants.SO_TIMEOUT.newSocketSetting(
                        NonNegativeInteger.valueOf(0));
        SocketSettings socketSettings = SocketSettings.of(socketSetting);
        Rule rule = new Rule.Builder().build();
        Settings settings = Settings.of(
                GeneralSettingSpecConstants.INTERNAL_FACING_SOCKET_SETTINGS.newSetting(socketSettings));
        Assert.assertEquals(
                socketSettings,
                GeneralValueDerivationHelper.getClientSocketSettingsFrom(rule, settings));
    }

    @Test
    public void testGetClientSocketSettingsFromRuleSettings07() {
        SocketSetting<?> socketSetting =
                StandardSocketSettingSpecConstants.SO_TIMEOUT.newSocketSetting(
                        NonNegativeInteger.valueOf(0));
        SocketSettings socketSettings = SocketSettings.of(socketSetting);
        Rule rule = new Rule.Builder().build();
        Settings settings = Settings.of(
                GeneralSettingSpecConstants.CLIENT_SOCKET_SETTINGS.newSetting(socketSettings));
        Assert.assertEquals(
                socketSettings,
                GeneralValueDerivationHelper.getClientSocketSettingsFrom(rule, settings));
    }

    @Test
    public void testGetSocksServerBindHostFromSettings01() {
        Host host = HostIpv4Address.getAllZerosInstance();
        Settings settings = Settings.of();
        Assert.assertEquals(
                host,
                GeneralValueDerivationHelper.getSocksServerBindHostFrom(settings));
    }

    @Test
    public void testGetSocksServerBindHostFromSettings02() {
        Host host = Host.newInstance("127.0.0.1");
        Settings settings = Settings.of(
                GeneralSettingSpecConstants.BIND_HOST.newSetting(host));
        Assert.assertEquals(
                host,
                GeneralValueDerivationHelper.getSocksServerBindHostFrom(settings));
    }

    @Test
    public void testGetSocksServerBindHostFromSettings03() {
        Host host = Host.newInstance("127.0.0.1");
        Settings settings = Settings.of(
                GeneralSettingSpecConstants.INTERNAL_FACING_BIND_HOST.newSetting(host));
        Assert.assertEquals(
                host,
                GeneralValueDerivationHelper.getSocksServerBindHostFrom(settings));
    }

    @Test
    public void testGetSocksServerBindHostFromSettings04() {
        Host host = Host.newInstance("127.0.0.1");
        Settings settings = Settings.of(
                GeneralSettingSpecConstants.SOCKS_SERVER_BIND_HOST.newSetting(host));
        Assert.assertEquals(
                host,
                GeneralValueDerivationHelper.getSocksServerBindHostFrom(settings));
    }

    @Test
    public void testGetSocksServerBindHostFromSettings05() throws UnknownHostException, SocketException {
        Host host = Host.newInstance("127.0.0.1");
        NetworkInterface networkInterface = NetworkInterface.getByInetAddress(
                host.toInetAddress());
        NetInterface netInterface = NetInterface.newInstance(networkInterface);
        Settings settings = Settings.of(
                GeneralSettingSpecConstants.NET_INTERFACE.newSetting(netInterface),
                GeneralSettingSpecConstants.BIND_HOST_ADDRESS_TYPES.newSetting(
                        HostAddressTypes.of(HostAddressType.IPV4)));
        Assert.assertEquals(
                host,
                GeneralValueDerivationHelper.getSocksServerBindHostFrom(settings));
    }

    @Test
    public void testGetSocksServerBindHostFromSettings06() throws UnknownHostException, SocketException {
        Host host = Host.newInstance("127.0.0.1");
        NetworkInterface networkInterface = NetworkInterface.getByInetAddress(
                host.toInetAddress());
        NetInterface netInterface = NetInterface.newInstance(networkInterface);
        Settings settings = Settings.of(
                GeneralSettingSpecConstants.INTERNAL_FACING_NET_INTERFACE.newSetting(netInterface),
                GeneralSettingSpecConstants.INTERNAL_FACING_BIND_HOST_ADDRESS_TYPES.newSetting(
                        HostAddressTypes.of(HostAddressType.IPV4)));
        Assert.assertEquals(
                host,
                GeneralValueDerivationHelper.getSocksServerBindHostFrom(settings));
    }

    @Test
    public void testGetSocksServerBindHostFromSettings07() throws UnknownHostException, SocketException {
        Host host = Host.newInstance("127.0.0.1");
        NetworkInterface networkInterface = NetworkInterface.getByInetAddress(
                host.toInetAddress());
        NetInterface netInterface = NetInterface.newInstance(networkInterface);
        Settings settings = Settings.of(
                GeneralSettingSpecConstants.SOCKS_SERVER_NET_INTERFACE.newSetting(netInterface),
                GeneralSettingSpecConstants.SOCKS_SERVER_BIND_HOST_ADDRESS_TYPES.newSetting(
                        HostAddressTypes.of(HostAddressType.IPV4)));
        Assert.assertEquals(
                host,
                GeneralValueDerivationHelper.getSocksServerBindHostFrom(settings));
    }

    @Test
    public void testGetSocksServerBindPortRangesFromSettings01() {
        PortRanges portRanges = PortRanges.of(PortRange.of(
                SocksServer.DEFAULT_PORT));
        Settings settings = Settings.of();
        Assert.assertEquals(
                portRanges,
                GeneralValueDerivationHelper.getSocksServerBindPortRangesFrom(settings));
    }

    @Test
    public void testGetSocksServerBindPortRangesFromSettings02() {
        Port port = Port.valueOf(1);
        PortRanges portRanges = PortRanges.of(PortRange.of(
                port));
        Settings settings = Settings.of(
                GeneralSettingSpecConstants.PORT.newSetting(port));
        Assert.assertEquals(
                portRanges,
                GeneralValueDerivationHelper.getSocksServerBindPortRangesFrom(settings));
    }

    @Test
    public void testGetSocksServerBindPortRangesFromSettings03() {
        Port port = Port.valueOf(1);
        PortRanges portRanges = PortRanges.of(PortRange.of(
                port));
        Settings settings = Settings.of(
                GeneralSettingSpecConstants.BIND_TCP_PORT_RANGES.newSetting(portRanges));
        Assert.assertEquals(
                portRanges,
                GeneralValueDerivationHelper.getSocksServerBindPortRangesFrom(settings));
    }

    @Test
    public void testGetSocksServerBindPortRangesFromSettings04() {
        Port port = Port.valueOf(1);
        PortRanges portRanges = PortRanges.of(PortRange.of(
                port));
        Settings settings = Settings.of(
                GeneralSettingSpecConstants.INTERNAL_FACING_BIND_TCP_PORT_RANGES.newSetting(portRanges));
        Assert.assertEquals(
                portRanges,
                GeneralValueDerivationHelper.getSocksServerBindPortRangesFrom(settings));
    }

    @Test
    public void testGetSocksServerBindPortRangesFromSettings05() {
        Port port = Port.valueOf(1);
        PortRanges portRanges = PortRanges.of(PortRange.of(
                port));
        Settings settings = Settings.of(
                GeneralSettingSpecConstants.SOCKS_SERVER_BIND_PORT_RANGES.newSetting(portRanges));
        Assert.assertEquals(
                portRanges,
                GeneralValueDerivationHelper.getSocksServerBindPortRangesFrom(settings));
    }

    @Test
    public void testGetSocksServerSocketSettingsFromSettings01() {
        SocketSettings socketSettings = SocketSettings.of();
        Settings settings = Settings.of();
        Assert.assertEquals(
                socketSettings,
                GeneralValueDerivationHelper.getSocksServerSocketSettingsFrom(settings));
    }

    @Test
    public void testGetSocksServerSocketSettingsFromSettings02() {
        SocketSetting<?> socketSetting =
                StandardSocketSettingSpecConstants.SO_TIMEOUT.newSocketSetting(
                        NonNegativeInteger.valueOf(0));
        SocketSettings socketSettings = SocketSettings.of(socketSetting);
        Settings settings = Settings.of(
                GeneralSettingSpecConstants.SOCKET_SETTINGS.newSetting(socketSettings));
        Assert.assertEquals(
                socketSettings,
                GeneralValueDerivationHelper.getSocksServerSocketSettingsFrom(settings));
    }

    @Test
    public void testGetSocksServerSocketSettingsFromSettings03() {
        SocketSetting<?> socketSetting =
                StandardSocketSettingSpecConstants.SO_TIMEOUT.newSocketSetting(
                        NonNegativeInteger.valueOf(0));
        SocketSettings socketSettings = SocketSettings.of(socketSetting);
        Settings settings = Settings.of(
                GeneralSettingSpecConstants.INTERNAL_FACING_SOCKET_SETTINGS.newSetting(socketSettings));
        Assert.assertEquals(
                socketSettings,
                GeneralValueDerivationHelper.getSocksServerSocketSettingsFrom(settings));
    }

    @Test
    public void testGetSocksServerSocketSettingsFromSettings04() {
        SocketSetting<?> socketSetting =
                StandardSocketSettingSpecConstants.SO_TIMEOUT.newSocketSetting(
                        NonNegativeInteger.valueOf(0));
        SocketSettings socketSettings = SocketSettings.of(socketSetting);
        Settings settings = Settings.of(
                GeneralSettingSpecConstants.SOCKS_SERVER_SOCKET_SETTINGS.newSetting(socketSettings));
        Assert.assertEquals(
                socketSettings,
                GeneralValueDerivationHelper.getSocksServerSocketSettingsFrom(settings));
    }

}