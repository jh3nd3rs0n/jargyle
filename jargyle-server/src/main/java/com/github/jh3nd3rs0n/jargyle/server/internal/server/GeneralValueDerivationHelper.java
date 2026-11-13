package com.github.jh3nd3rs0n.jargyle.server.internal.server;

import com.github.jh3nd3rs0n.jargyle.common.net.*;
import com.github.jh3nd3rs0n.jargyle.server.*;

import java.util.ArrayList;

/**
 * Helper class for deriving general specific values from {@code RuleAction}s
 * and {@code Settings}.
 */
public final class GeneralValueDerivationHelper {

    /**
     * Prevents the construction of unnecessary instances.
     */
    private GeneralValueDerivationHelper() {
    }

    private static HostAddressTypes getBindHostAddressTypesFrom(
            final Rule rule) {
        return HostAddressTypes.of(rule.getRuleActionValues(
                GeneralRuleActionSpecConstants.BIND_HOST_ADDRESS_TYPE));
    }

    private static HostAddressTypes getBindHostAddressTypesFrom(
            final Settings settings) {
        return settings.getLastValue(
                GeneralSettingSpecConstants.BIND_HOST_ADDRESS_TYPES);
    }

    private static Host getBindHostFrom(final Rule rule) {
        return rule.getLastRuleActionValue(
                GeneralRuleActionSpecConstants.BIND_HOST);
    }

    private static Host getBindHostFrom(final Settings settings) {
        return settings.getLastValue(GeneralSettingSpecConstants.BIND_HOST);
    }

    private static PortRanges getBindTcpPortRangesFrom(final Rule rule) {
        return PortRanges.of(rule.getRuleActionValues(
                GeneralRuleActionSpecConstants.BIND_TCP_PORT_RANGE));
    }

    private static PortRanges getBindTcpPortRangesFrom(
            final Settings settings) {
        return settings.getLastValue(
                GeneralSettingSpecConstants.BIND_TCP_PORT_RANGES);
    }

    private static PortRanges getBindUdpPortRangesFrom(final Rule rule) {
        return PortRanges.of(rule.getRuleActionValues(
                GeneralRuleActionSpecConstants.BIND_UDP_PORT_RANGE));
    }

    private static PortRanges getBindUdpPortRangesFrom(
            final Settings settings) {
        return settings.getLastValue(
                GeneralSettingSpecConstants.BIND_UDP_PORT_RANGES);
    }

    public static HostAddressTypes getClientEndpointsBindHostAddressTypesFrom(
            final Rule rule) {
        HostAddressTypes hostAddressTypes = HostAddressTypes.of(
                rule.getRuleActionValues(
                        GeneralRuleActionSpecConstants.CLIENT_ENDPOINTS_BIND_HOST_ADDRESS_TYPE));
        if (!hostAddressTypes.toList().isEmpty()) {
            return hostAddressTypes;
        }
        return getBindHostAddressTypesFrom(rule);
    }

    public static HostAddressTypes getClientEndpointsBindHostAddressTypesFrom(
            final Settings settings) {
        HostAddressTypes hostAddressTypes = settings.getLastValue(
                GeneralSettingSpecConstants.CLIENT_ENDPOINTS_BIND_HOST_ADDRESS_TYPES);
        if (!hostAddressTypes.toList().isEmpty()) {
            return hostAddressTypes;
        }
        return getBindHostAddressTypesFrom(settings);
    }

    public static Host getClientEndpointsBindHostFrom(final Rule rule) {
        Host host = rule.getLastRuleActionValue(
                GeneralRuleActionSpecConstants.CLIENT_ENDPOINTS_BIND_HOST);
        if (host != null) {
            return host;
        }
        return getBindHostFrom(rule);
    }

    public static Host getClientEndpointsBindHostFrom(final Settings settings) {
        Host host = settings.getLastValue(
                GeneralSettingSpecConstants.CLIENT_ENDPOINTS_BIND_HOST);
        if (host != null) {
            return host;
        }
        return getBindHostFrom(settings);
    }

    public static PortRanges getClientEndpointsBindUdpPortRangesFrom(final Rule rule) {
        PortRanges portRanges = PortRanges.of(rule.getRuleActionValues(
                GeneralRuleActionSpecConstants.CLIENT_ENDPOINTS_BIND_UDP_PORT_RANGE));
        if (!portRanges.toList().isEmpty()) {
            return portRanges;
        }
        return getBindUdpPortRangesFrom(rule);
    }

    public static PortRanges getClientEndpointsBindUdpPortRangesFrom(
            final Settings settings) {
        PortRanges portRanges = settings.getLastValue(
                GeneralSettingSpecConstants.CLIENT_ENDPOINTS_BIND_UDP_PORT_RANGES);
        if (!portRanges.toList().isEmpty()) {
            return portRanges;
        }
        return getBindUdpPortRangesFrom(settings);
    }

    public static NetInterface getClientEndpointsNetInterfaceFrom(final Rule rule) {
        NetInterface netInterface = rule.getLastRuleActionValue(
                GeneralRuleActionSpecConstants.CLIENT_ENDPOINTS_NET_INTERFACE);
        if (netInterface != null) {
            return netInterface;
        }
        return getNetInterfaceFrom(rule);
    }

    public static NetInterface getClientEndpointsNetInterfaceFrom(
            final Settings settings) {
        NetInterface netInterface = settings.getLastValue(
                GeneralSettingSpecConstants.CLIENT_ENDPOINTS_NET_INTERFACE);
        if (netInterface != null) {
            return netInterface;
        }
        return getNetInterfaceFrom(settings);
    }

    public static SocketSettings getClientEndpointsSocketSettingsFrom(
            final Rule rule) {
        SocketSettings socketSettings = SocketSettings.of(new ArrayList<>(
                rule.getRuleActionValues(
                        GeneralRuleActionSpecConstants.CLIENT_ENDPOINTS_SOCKET_SETTING)));
        if (!socketSettings.toMap().isEmpty()) {
            return socketSettings;
        }
        return getSocketSettingsFrom(rule);
    }

    public static SocketSettings getClientEndpointsSocketSettingsFrom(
            final Settings settings) {
        SocketSettings socketSettings = settings.getLastValue(
                GeneralSettingSpecConstants.CLIENT_ENDPOINTS_SOCKET_SETTINGS);
        if (!socketSettings.toMap().isEmpty()) {
            return socketSettings;
        }
        return getSocketSettingsFrom(settings);
    }

    private static HostAddressTypes getClientFacingServerSocketBindHostAddressTypesFrom(
            final Settings settings) {
        HostAddressTypes hostAddressTypes = settings.getLastValue(
                GeneralSettingSpecConstants.CLIENT_FACING_SERVER_SOCKET_BIND_HOST_ADDRESS_TYPES);
        if (!hostAddressTypes.toList().isEmpty()) {
            return hostAddressTypes;
        }
        return getClientEndpointsBindHostAddressTypesFrom(settings);
    }

    public static Host getClientFacingServerSocketBindHostFrom(final Settings settings) {
        Host host = settings.getLastValue(
                GeneralSettingSpecConstants.CLIENT_FACING_SERVER_SOCKET_BIND_HOST);
        if (host != null) {
            return host;
        }
        host = getClientEndpointsBindHostFrom(settings);
        if (host != null) {
            return host;
        }
        NetInterface netInterface = getClientFacingServerSocketNetInterfaceFrom(settings);
        if (netInterface != null) {
            return netInterface.getHostAddresses(
                            getClientFacingServerSocketBindHostAddressTypesFrom(settings))
                    .stream()
                    .findFirst()
                    .orElse(HostIpv4Address.getAllZerosInstance());
        }
        return HostIpv4Address.getAllZerosInstance();
    }

    public static PortRanges getClientFacingServerSocketBindPortRangesFrom(
            final Settings settings) {
        Port port = settings.getLastValue(GeneralSettingSpecConstants.PORT);
        if (port != null) {
            return PortRanges.of(PortRange.of(port));
        }
        PortRanges portRanges = settings.getLastValue(
                GeneralSettingSpecConstants.CLIENT_FACING_SERVER_SOCKET_BIND_PORT_RANGES);
        if (!portRanges.toList().isEmpty()) {
            return portRanges;
        }
        portRanges = settings.getLastValue(
                GeneralSettingSpecConstants.CLIENT_ENDPOINTS_BIND_TCP_PORT_RANGES);
        if (!portRanges.toList().isEmpty()) {
            return portRanges;
        }
        portRanges = settings.getLastValue(
                GeneralSettingSpecConstants.BIND_TCP_PORT_RANGES);
        if (portRanges.equals(PortRanges.getDefault())) {
            return PortRanges.of(PortRange.of(SocksServer.DEFAULT_PORT));
        }
        return portRanges;
    }

    private static NetInterface getClientFacingServerSocketNetInterfaceFrom(
            final Settings settings) {
        NetInterface netInterface = settings.getLastValue(
                GeneralSettingSpecConstants.CLIENT_FACING_SERVER_SOCKET_NET_INTERFACE);
        if (netInterface != null) {
            return netInterface;
        }
        return getClientEndpointsNetInterfaceFrom(settings);
    }

    public static SocketSettings getClientFacingServerSocketSocketSettingsFrom(
            final Settings settings) {
        SocketSettings socketSettings = settings.getLastValue(
                GeneralSettingSpecConstants.CLIENT_FACING_SERVER_SOCKET_SOCKET_SETTINGS);
        if (!socketSettings.toMap().isEmpty()) {
            return socketSettings;
        }
        return getClientEndpointsSocketSettingsFrom(settings);
    }

    private static SocketSettings getClientSocketSocketSettingsFrom(
            final Rule rule) {
        SocketSettings socketSettings = SocketSettings.of(
                new ArrayList<>(rule.getRuleActionValues(
                        GeneralRuleActionSpecConstants.CLIENT_SOCKET_SOCKET_SETTING)));
        if (!socketSettings.toMap().isEmpty()) {
            return socketSettings;
        }
        return getClientEndpointsSocketSettingsFrom(rule);
    }

    private static SocketSettings getClientSocketSocketSettingsFrom(
            final Settings settings) {
        SocketSettings socketSettings = settings.getLastValue(
                GeneralSettingSpecConstants.CLIENT_SOCKET_SOCKET_SETTINGS);
        if (!socketSettings.toMap().isEmpty()) {
            return socketSettings;
        }
        return getClientEndpointsSocketSettingsFrom(settings);
    }

    public static SocketSettings getClientSocketSocketSettingsFrom(
            final Rule rule, final Settings settings) {
        SocketSettings socketSettings = getClientSocketSocketSettingsFrom(rule);
        if (!socketSettings.toMap().isEmpty()) {
            return socketSettings;
        }
        return getClientSocketSocketSettingsFrom(settings);
    }

    private static NetInterface getNetInterfaceFrom(final Rule rule) {
        return rule.getLastRuleActionValue(
                GeneralRuleActionSpecConstants.NET_INTERFACE);
    }

    private static NetInterface getNetInterfaceFrom(final Settings settings) {
        return settings.getLastValue(
                GeneralSettingSpecConstants.NET_INTERFACE);
    }

    public static HostAddressTypes getRemoteEndpointsBindHostAddressTypesFrom(
            final Rule rule) {
        HostAddressTypes hostAddressTypes = HostAddressTypes.of(
                rule.getRuleActionValues(
                        GeneralRuleActionSpecConstants.REMOTE_ENDPOINTS_BIND_HOST_ADDRESS_TYPE));
        if (!hostAddressTypes.toList().isEmpty()) {
            return hostAddressTypes;
        }
        return getBindHostAddressTypesFrom(rule);
    }

    public static HostAddressTypes getRemoteEndpointsBindHostAddressTypesFrom(
            final Settings settings) {
        HostAddressTypes hostAddressTypes = settings.getLastValue(
                GeneralSettingSpecConstants.REMOTE_ENDPOINTS_BIND_HOST_ADDRESS_TYPES);
        if (!hostAddressTypes.toList().isEmpty()) {
            return hostAddressTypes;
        }
        return getBindHostAddressTypesFrom(settings);
    }

    public static Host getRemoteEndpointsBindHostFrom(final Rule rule) {
        Host host = rule.getLastRuleActionValue(
                GeneralRuleActionSpecConstants.REMOTE_ENDPOINTS_BIND_HOST);
        if (host != null) {
            return host;
        }
        return getBindHostFrom(rule);
    }

    public static Host getRemoteEndpointsBindHostFrom(final Settings settings) {
        Host host = settings.getLastValue(
                GeneralSettingSpecConstants.REMOTE_ENDPOINTS_BIND_HOST);
        if (host != null) {
            return host;
        }
        return getBindHostFrom(settings);
    }

    public static PortRanges getRemoteEndpointsBindTcpPortRangesFrom(final Rule rule) {
        PortRanges portRanges = PortRanges.of(rule.getRuleActionValues(
                GeneralRuleActionSpecConstants.REMOTE_ENDPOINTS_BIND_TCP_PORT_RANGE));
        if (!portRanges.toList().isEmpty()) {
            return portRanges;
        }
        return getBindTcpPortRangesFrom(rule);
    }

    public static PortRanges getRemoteEndpointsBindTcpPortRangesFrom(
            final Settings settings) {
        PortRanges portRanges = settings.getLastValue(
                GeneralSettingSpecConstants.REMOTE_ENDPOINTS_BIND_TCP_PORT_RANGES);
        if (!portRanges.toList().isEmpty()) {
            return portRanges;
        }
        return getBindTcpPortRangesFrom(settings);
    }

    public static PortRanges getRemoteEndpointsBindUdpPortRangesFrom(final Rule rule) {
        PortRanges portRanges = PortRanges.of(rule.getRuleActionValues(
                GeneralRuleActionSpecConstants.REMOTE_ENDPOINTS_BIND_UDP_PORT_RANGE));
        if (!portRanges.toList().isEmpty()) {
            return portRanges;
        }
        return getBindUdpPortRangesFrom(rule);
    }

    public static PortRanges getRemoteEndpointsBindUdpPortRangesFrom(
            final Settings settings) {
        PortRanges portRanges = settings.getLastValue(
                GeneralSettingSpecConstants.REMOTE_ENDPOINTS_BIND_UDP_PORT_RANGES);
        if (!portRanges.toList().isEmpty()) {
            return portRanges;
        }
        return getBindUdpPortRangesFrom(settings);
    }

    public static NetInterface getRemoteEndpointsNetInterfaceFrom(final Rule rule) {
        NetInterface netInterface = rule.getLastRuleActionValue(
                GeneralRuleActionSpecConstants.REMOTE_ENDPOINTS_NET_INTERFACE);
        if (netInterface != null) {
            return netInterface;
        }
        return getNetInterfaceFrom(rule);
    }

    public static NetInterface getRemoteEndpointsNetInterfaceFrom(
            final Settings settings) {
        NetInterface netInterface = settings.getLastValue(
                GeneralSettingSpecConstants.REMOTE_ENDPOINTS_NET_INTERFACE);
        if (netInterface != null) {
            return netInterface;
        }
        return getNetInterfaceFrom(settings);
    }

    public static SocketSettings getRemoteEndpointsSocketSettingsFrom(
            final Rule rule) {
        SocketSettings socketSettings = SocketSettings.of(new ArrayList<>(
                rule.getRuleActionValues(
                        GeneralRuleActionSpecConstants.REMOTE_ENDPOINTS_SOCKET_SETTING)));
        if (!socketSettings.toMap().isEmpty()) {
            return socketSettings;
        }
        return getSocketSettingsFrom(rule);
    }

    public static SocketSettings getRemoteEndpointsSocketSettingsFrom(
            final Settings settings) {
        SocketSettings socketSettings = settings.getLastValue(
                GeneralSettingSpecConstants.REMOTE_ENDPOINTS_SOCKET_SETTINGS);
        if (!socketSettings.toMap().isEmpty()) {
            return socketSettings;
        }
        return getSocketSettingsFrom(settings);
    }

    private static SocketSettings getSocketSettingsFrom(final Rule rule) {
        return SocketSettings.of(new ArrayList<>(rule.getRuleActionValues(
                GeneralRuleActionSpecConstants.SOCKET_SETTING)));
    }

    private static SocketSettings getSocketSettingsFrom(
            final Settings settings) {
        return settings.getLastValue(
                GeneralSettingSpecConstants.SOCKET_SETTINGS);
    }

}
