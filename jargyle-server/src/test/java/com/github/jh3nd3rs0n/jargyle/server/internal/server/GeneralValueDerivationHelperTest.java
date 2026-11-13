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
    public void testGetClientFacingServerSocketBindHostFromSettings01() {
        Host host = HostIpv4Address.getAllZerosInstance();
        Settings settings = Settings.of();
        Assert.assertEquals(
                host,
                GeneralValueDerivationHelper.getClientFacingServerSocketBindHostFrom(settings));
    }

    @Test
    public void testGetClientFacingServerSocketBindHostFromSettings02() {
        Host host = Host.newInstance("127.0.0.1");
        Settings settings = Settings.of(
                GeneralSettingSpecConstants.BIND_HOST.newSetting(host));
        Assert.assertEquals(
                host,
                GeneralValueDerivationHelper.getClientFacingServerSocketBindHostFrom(settings));
    }

    @Test
    public void testGetClientFacingServerSocketBindHostFromSettings03() {
        Host host = Host.newInstance("127.0.0.1");
        Settings settings = Settings.of(
                GeneralSettingSpecConstants.CLIENT_ENDPOINTS_BIND_HOST.newSetting(host));
        Assert.assertEquals(
                host,
                GeneralValueDerivationHelper.getClientFacingServerSocketBindHostFrom(settings));
    }

    @Test
    public void testGetClientFacingServerSocketBindHostFromSettings04() {
        Host host = Host.newInstance("127.0.0.1");
        Settings settings = Settings.of(
                GeneralSettingSpecConstants.CLIENT_FACING_SERVER_SOCKET_BIND_HOST.newSetting(host));
        Assert.assertEquals(
                host,
                GeneralValueDerivationHelper.getClientFacingServerSocketBindHostFrom(settings));
    }

    @Test
    public void testGetClientFacingServerSocketBindHostFromSettings05() throws UnknownHostException, SocketException {
        Host host = Host.newInstance("127.0.0.1");
        NetworkInterface networkInterface = NetworkInterface.getByInetAddress(
                host.toInetAddress());
        NetInterface netInterface = NetInterface.newInstance(networkInterface);
        Settings settings = Settings.of(
                GeneralSettingSpecConstants.NET_INTERFACE.newSetting(netInterface),
                GeneralSettingSpecConstants.BIND_HOST_ADDRESS_TYPES.newSetting(
                        HostAddressTypes.of(HostAddressType.HOST_IPV4_ADDRESS)));
        Assert.assertTrue(
                netInterface.getHostAddresses().contains((HostAddress)
                        GeneralValueDerivationHelper.getClientFacingServerSocketBindHostFrom(settings)));
    }

    @Test
    public void testGetClientFacingServerSocketBindHostFromSettings06() throws UnknownHostException, SocketException {
        Host host = Host.newInstance("127.0.0.1");
        NetworkInterface networkInterface = NetworkInterface.getByInetAddress(
                host.toInetAddress());
        NetInterface netInterface = NetInterface.newInstance(networkInterface);
        Settings settings = Settings.of(
                GeneralSettingSpecConstants.CLIENT_ENDPOINTS_NET_INTERFACE.newSetting(netInterface),
                GeneralSettingSpecConstants.CLIENT_ENDPOINTS_BIND_HOST_ADDRESS_TYPES.newSetting(
                        HostAddressTypes.of(HostAddressType.HOST_IPV4_ADDRESS)));
        Assert.assertTrue(
                netInterface.getHostAddresses().contains((HostAddress)
                        GeneralValueDerivationHelper.getClientFacingServerSocketBindHostFrom(settings)));
    }

    @Test
    public void testGetClientFacingServerSocketBindHostFromSettings07() throws UnknownHostException, SocketException {
        Host host = Host.newInstance("127.0.0.1");
        NetworkInterface networkInterface = NetworkInterface.getByInetAddress(
                host.toInetAddress());
        NetInterface netInterface = NetInterface.newInstance(networkInterface);
        Settings settings = Settings.of(
                GeneralSettingSpecConstants.CLIENT_FACING_SERVER_SOCKET_NET_INTERFACE.newSetting(netInterface),
                GeneralSettingSpecConstants.CLIENT_FACING_SERVER_SOCKET_BIND_HOST_ADDRESS_TYPES.newSetting(
                        HostAddressTypes.of(HostAddressType.HOST_IPV4_ADDRESS)));
        Assert.assertTrue(
                netInterface.getHostAddresses().contains((HostAddress)
                        GeneralValueDerivationHelper.getClientFacingServerSocketBindHostFrom(settings)));
    }

    @Test
    public void testGetClientFacingServerSocketBindPortRangesFromSettings01() {
        PortRanges portRanges = PortRanges.of(PortRange.of(
                SocksServer.DEFAULT_PORT));
        Settings settings = Settings.of();
        Assert.assertEquals(
                portRanges,
                GeneralValueDerivationHelper.getClientFacingServerSocketBindPortRangesFrom(settings));
    }

    @Test
    public void testGetClientFacingServerSocketBindPortRangesFromSettings02() {
        Port port = Port.valueOf(1);
        PortRanges portRanges = PortRanges.of(PortRange.of(
                port));
        Settings settings = Settings.of(
                GeneralSettingSpecConstants.PORT.newSetting(port));
        Assert.assertEquals(
                portRanges,
                GeneralValueDerivationHelper.getClientFacingServerSocketBindPortRangesFrom(settings));
    }

    @Test
    public void testGetClientFacingServerSocketBindPortRangesFromSettings03() {
        Port port = Port.valueOf(1);
        PortRanges portRanges = PortRanges.of(PortRange.of(
                port));
        Settings settings = Settings.of(
                GeneralSettingSpecConstants.BIND_TCP_PORT_RANGES.newSetting(portRanges));
        Assert.assertEquals(
                portRanges,
                GeneralValueDerivationHelper.getClientFacingServerSocketBindPortRangesFrom(settings));
    }

    @Test
    public void testGetClientFacingServerSocketBindPortRangesFromSettings04() {
        Port port = Port.valueOf(1);
        PortRanges portRanges = PortRanges.of(PortRange.of(
                port));
        Settings settings = Settings.of(
                GeneralSettingSpecConstants.CLIENT_ENDPOINTS_BIND_TCP_PORT_RANGES.newSetting(portRanges));
        Assert.assertEquals(
                portRanges,
                GeneralValueDerivationHelper.getClientFacingServerSocketBindPortRangesFrom(settings));
    }

    @Test
    public void testGetClientFacingServerSocketBindPortRangesFromSettings05() {
        Port port = Port.valueOf(1);
        PortRanges portRanges = PortRanges.of(PortRange.of(
                port));
        Settings settings = Settings.of(
                GeneralSettingSpecConstants.CLIENT_FACING_SERVER_SOCKET_BIND_PORT_RANGES.newSetting(portRanges));
        Assert.assertEquals(
                portRanges,
                GeneralValueDerivationHelper.getClientFacingServerSocketBindPortRangesFrom(settings));
    }

    @Test
    public void testGetClientFacingServerSocketSocketSettingsFromSettings01() {
        SocketSettings socketSettings = SocketSettings.of();
        Settings settings = Settings.of();
        Assert.assertEquals(
                socketSettings,
                GeneralValueDerivationHelper.getClientFacingServerSocketSocketSettingsFrom(settings));
    }

    @Test
    public void testGetClientFacingServerSocketSocketSettingsFromSettings02() {
        SocketSetting<?> socketSetting =
                StandardSocketSettingSpecConstants.SO_TIMEOUT.newSocketSetting(
                        NonNegativeInteger.valueOf(0));
        SocketSettings socketSettings = SocketSettings.of(socketSetting);
        Settings settings = Settings.of(
                GeneralSettingSpecConstants.SOCKET_SETTINGS.newSetting(socketSettings));
        Assert.assertEquals(
                socketSettings,
                GeneralValueDerivationHelper.getClientFacingServerSocketSocketSettingsFrom(settings));
    }

    @Test
    public void testGetClientFacingServerSocketSocketSettingsFromSettings03() {
        SocketSetting<?> socketSetting =
                StandardSocketSettingSpecConstants.SO_TIMEOUT.newSocketSetting(
                        NonNegativeInteger.valueOf(0));
        SocketSettings socketSettings = SocketSettings.of(socketSetting);
        Settings settings = Settings.of(
                GeneralSettingSpecConstants.CLIENT_ENDPOINTS_SOCKET_SETTINGS.newSetting(socketSettings));
        Assert.assertEquals(
                socketSettings,
                GeneralValueDerivationHelper.getClientFacingServerSocketSocketSettingsFrom(settings));
    }

    @Test
    public void testGetClientFacingServerSocketSocketSettingsFromSettings04() {
        SocketSetting<?> socketSetting =
                StandardSocketSettingSpecConstants.SO_TIMEOUT.newSocketSetting(
                        NonNegativeInteger.valueOf(0));
        SocketSettings socketSettings = SocketSettings.of(socketSetting);
        Settings settings = Settings.of(
                GeneralSettingSpecConstants.CLIENT_FACING_SERVER_SOCKET_SOCKET_SETTINGS.newSetting(socketSettings));
        Assert.assertEquals(
                socketSettings,
                GeneralValueDerivationHelper.getClientFacingServerSocketSocketSettingsFrom(settings));
    }

    @Test
    public void testGetClientSocketSocketSettingsFromRuleSettings01() {
        SocketSettings socketSettings = SocketSettings.of();
        Rule rule = new Rule.Builder().build();
        Settings settings = Settings.of();
        Assert.assertEquals(
                socketSettings,
                GeneralValueDerivationHelper.getClientSocketSocketSettingsFrom(rule, settings));
    }

    @Test
    public void testGetClientSocketSocketSettingsFromRuleSettings02() {
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
                GeneralValueDerivationHelper.getClientSocketSocketSettingsFrom(rule, settings));
    }

    @Test
    public void testGetClientSocketSocketSettingsFromRuleSettings03() {
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
                GeneralValueDerivationHelper.getClientSocketSocketSettingsFrom(rule, settings));
    }

    @Test
    public void testGetClientSocketSocketSettingsFromRuleSettings04() {
        SocketSetting<?> socketSetting =
                StandardSocketSettingSpecConstants.SO_TIMEOUT.newSocketSetting(
                        NonNegativeInteger.valueOf(0));
        @SuppressWarnings("unchecked")
        SocketSetting<Object> socketSttng = (SocketSetting<Object>) socketSetting;
        SocketSettings socketSettings = SocketSettings.of(socketSttng);
        Rule rule = new Rule.Builder()
                .addRuleAction(GeneralRuleActionSpecConstants.CLIENT_SOCKET_SOCKET_SETTING.newRuleAction(
                        socketSttng))
                .build();
        Settings settings = Settings.of();
        Assert.assertEquals(
                socketSettings,
                GeneralValueDerivationHelper.getClientSocketSocketSettingsFrom(rule, settings));
    }

    @Test
    public void testGetClientSocketSocketSettingsFromRuleSettings05() {
        SocketSetting<?> socketSetting =
                StandardSocketSettingSpecConstants.SO_TIMEOUT.newSocketSetting(
                        NonNegativeInteger.valueOf(0));
        SocketSettings socketSettings = SocketSettings.of(socketSetting);
        Rule rule = new Rule.Builder().build();
        Settings settings = Settings.of(
                GeneralSettingSpecConstants.SOCKET_SETTINGS.newSetting(socketSettings));
        Assert.assertEquals(
                socketSettings,
                GeneralValueDerivationHelper.getClientSocketSocketSettingsFrom(rule, settings));
    }

    @Test
    public void testGetClientSocketSocketSettingsFromRuleSettings06() {
        SocketSetting<?> socketSetting =
                StandardSocketSettingSpecConstants.SO_TIMEOUT.newSocketSetting(
                        NonNegativeInteger.valueOf(0));
        SocketSettings socketSettings = SocketSettings.of(socketSetting);
        Rule rule = new Rule.Builder().build();
        Settings settings = Settings.of(
                GeneralSettingSpecConstants.CLIENT_ENDPOINTS_SOCKET_SETTINGS.newSetting(socketSettings));
        Assert.assertEquals(
                socketSettings,
                GeneralValueDerivationHelper.getClientSocketSocketSettingsFrom(rule, settings));
    }

    @Test
    public void testGetClientSocketSocketSettingsFromRuleSettings07() {
        SocketSetting<?> socketSetting =
                StandardSocketSettingSpecConstants.SO_TIMEOUT.newSocketSetting(
                        NonNegativeInteger.valueOf(0));
        SocketSettings socketSettings = SocketSettings.of(socketSetting);
        Rule rule = new Rule.Builder().build();
        Settings settings = Settings.of(
                GeneralSettingSpecConstants.CLIENT_SOCKET_SOCKET_SETTINGS.newSetting(socketSettings));
        Assert.assertEquals(
                socketSettings,
                GeneralValueDerivationHelper.getClientSocketSocketSettingsFrom(rule, settings));
    }

}