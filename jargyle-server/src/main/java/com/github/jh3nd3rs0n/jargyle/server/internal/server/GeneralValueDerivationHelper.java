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
 
    private static Host getBindHostFrom(final Rule rule) {
        return rule.getLastRuleActionValue(
                GeneralRuleActionSpecConstants.BIND_HOST);
    }
    
    private static Host getBindHostFrom(final Settings settings) {
        return settings.getLastValue(GeneralSettingSpecConstants.BIND_HOST);
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
    
    private static SocketSettings getClientSocketSettingsFrom(
            final Rule rule) {
        SocketSettings socketSettings = SocketSettings.of(
                new ArrayList<>(rule.getRuleActionValues(
                        GeneralRuleActionSpecConstants.CLIENT_SOCKET_SETTING)));
        if (!socketSettings.toMap().isEmpty()) {
            return socketSettings;
        }
        return getInternalFacingSocketSettingsFrom(rule);
    } 
    
    private static SocketSettings getClientSocketSettingsFrom(
            final Settings settings) {
        SocketSettings socketSettings = settings.getLastValue(
                GeneralSettingSpecConstants.CLIENT_SOCKET_SETTINGS);
        if (!socketSettings.toMap().isEmpty()) {
            return socketSettings;
        }
        return getInternalFacingSocketSettingsFrom(settings);
    }

    public static SocketSettings getClientSocketSettingsFrom(
            final Rule rule, final Settings settings) {
        SocketSettings socketSettings = getClientSocketSettingsFrom(rule);
        if (!socketSettings.toMap().isEmpty()) {
            return socketSettings;
        }
        return getClientSocketSettingsFrom(settings);
    }
    
    static Host getExternalFacingBindHostFrom(final Rule rule) {
        Host host = rule.getLastRuleActionValue(
                GeneralRuleActionSpecConstants.EXTERNAL_FACING_BIND_HOST);
        if (host != null) {
            return host;
        }
        return getBindHostFrom(rule);
    }
    
    static Host getExternalFacingBindHostFrom(final Settings settings) {
        Host host = settings.getLastValue(
                GeneralSettingSpecConstants.EXTERNAL_FACING_BIND_HOST);
        if (host != null) {
            return host;
        }
        return getBindHostFrom(settings);
    }
    
    static HostAddressTypes getExternalFacingBindHostAddressTypesFrom(
            final Rule rule) {
        HostAddressTypes hostAddressTypes = HostAddressTypes.of(
                rule.getRuleActionValues(
                        GeneralRuleActionSpecConstants.EXTERNAL_FACING_BIND_HOST_ADDRESS_TYPE));
        if (!hostAddressTypes.toList().isEmpty()) {
            return hostAddressTypes;
        }
        return getBindHostAddressTypesFrom(rule);
    }
    
    static HostAddressTypes getExternalFacingBindHostAddressTypesFrom(
            final Settings settings) {
        HostAddressTypes hostAddressTypes = settings.getLastValue(
                GeneralSettingSpecConstants.EXTERNAL_FACING_BIND_HOST_ADDRESS_TYPES);
        if (!hostAddressTypes.toList().isEmpty()) {
            return hostAddressTypes;
        }
        return getBindHostAddressTypesFrom(settings);
    }
    
    static PortRanges getExternalFacingTcpPortRangesFrom(final Rule rule) {
        PortRanges portRanges = PortRanges.of(rule.getRuleActionValues(
                GeneralRuleActionSpecConstants.EXTERNAL_FACING_BIND_TCP_PORT_RANGE));
        if (!portRanges.toList().isEmpty()) {
            return portRanges;
        }
        return getBindTcpPortRangesFrom(rule);
    }
    
    static PortRanges getExternalFacingTcpPortRangesFrom(
            final Settings settings) {
        PortRanges portRanges = settings.getLastValue(
                GeneralSettingSpecConstants.EXTERNAL_FACING_BIND_TCP_PORT_RANGES);
        if (!portRanges.toList().isEmpty()) {
            return portRanges;
        }
        return getBindTcpPortRangesFrom(settings);
    }
    
    static PortRanges getExternalFacingUdpPortRangesFrom(final Rule rule) {
        PortRanges portRanges = PortRanges.of(rule.getRuleActionValues(
                GeneralRuleActionSpecConstants.EXTERNAL_FACING_BIND_UDP_PORT_RANGE));
        if (!portRanges.toList().isEmpty()) {
            return portRanges;
        }
        return getBindUdpPortRangesFrom(rule);
    }
    
    static PortRanges getExternalFacingUdpPortRangesFrom(
            final Settings settings) {
        PortRanges portRanges = settings.getLastValue(
                GeneralSettingSpecConstants.EXTERNAL_FACING_BIND_UDP_PORT_RANGES);
        if (!portRanges.toList().isEmpty()) {
            return portRanges;
        }
        return getBindUdpPortRangesFrom(settings);
    }
    
    static NetInterface getExternalFacingNetInterfaceFrom(final Rule rule) {
        NetInterface netInterface = rule.getLastRuleActionValue(
                GeneralRuleActionSpecConstants.EXTERNAL_FACING_NET_INTERFACE);
        if (netInterface != null) {
            return netInterface;
        }
        return getNetInterfaceFrom(rule);
    }
    
    static NetInterface getExternalFacingNetInterfaceFrom(
            final Settings settings) {
        NetInterface netInterface = settings.getLastValue(
                GeneralSettingSpecConstants.EXTERNAL_FACING_NET_INTERFACE);
        if (netInterface != null) {
            return netInterface;
        }
        return getNetInterfaceFrom(settings);
    }
    
    static SocketSettings getExternalFacingSocketSettingsFrom(
            final Rule rule) {
        SocketSettings socketSettings = SocketSettings.of(new ArrayList<>(
                rule.getRuleActionValues(
                        GeneralRuleActionSpecConstants.EXTERNAL_FACING_SOCKET_SETTING)));
        if (!socketSettings.toMap().isEmpty()) {
            return socketSettings;
        }
        return getSocketSettingsFrom(rule);
    }
    
    static SocketSettings getExternalFacingSocketSettingsFrom(
            final Settings settings) {
        SocketSettings socketSettings = settings.getLastValue(
                GeneralSettingSpecConstants.EXTERNAL_FACING_SOCKET_SETTINGS);
        if (!socketSettings.toMap().isEmpty()) {
            return socketSettings;
        }
        return getSocketSettingsFrom(settings);
    }

    static Host getInternalFacingBindHostFrom(final Rule rule) {
        Host host = rule.getLastRuleActionValue(
                GeneralRuleActionSpecConstants.INTERNAL_FACING_BIND_HOST);
        if (host != null) {
            return host;
        }
        return getBindHostFrom(rule);
    }

    static Host getInternalFacingBindHostFrom(final Settings settings) {
        Host host = settings.getLastValue(
                GeneralSettingSpecConstants.INTERNAL_FACING_BIND_HOST);
        if (host != null) {
            return host;
        }
        return getBindHostFrom(settings);
    }

    static HostAddressTypes getInternalFacingBindHostAddressTypesFrom(
            final Rule rule) {
        HostAddressTypes hostAddressTypes = HostAddressTypes.of(
                rule.getRuleActionValues(
                        GeneralRuleActionSpecConstants.INTERNAL_FACING_BIND_HOST_ADDRESS_TYPE));
        if (!hostAddressTypes.toList().isEmpty()) {
            return hostAddressTypes;
        }
        return getBindHostAddressTypesFrom(rule);
    }

    static HostAddressTypes getInternalFacingBindHostAddressTypesFrom(
            final Settings settings) {
        HostAddressTypes hostAddressTypes = settings.getLastValue(
                GeneralSettingSpecConstants.INTERNAL_FACING_BIND_HOST_ADDRESS_TYPES);
        if (!hostAddressTypes.toList().isEmpty()) {
            return hostAddressTypes;
        }
        return getBindHostAddressTypesFrom(settings);
    }

    static PortRanges getInternalFacingUdpPortRangesFrom(final Rule rule) {
        PortRanges portRanges = PortRanges.of(rule.getRuleActionValues(
                GeneralRuleActionSpecConstants.INTERNAL_FACING_BIND_UDP_PORT_RANGE));
        if (!portRanges.toList().isEmpty()) {
            return portRanges;
        }
        return getBindUdpPortRangesFrom(rule);
    }

    static PortRanges getInternalFacingUdpPortRangesFrom(
            final Settings settings) {
        PortRanges portRanges = settings.getLastValue(
                GeneralSettingSpecConstants.INTERNAL_FACING_BIND_UDP_PORT_RANGES);
        if (!portRanges.toList().isEmpty()) {
            return portRanges;
        }
        return getBindUdpPortRangesFrom(settings);
    }

    static NetInterface getInternalFacingNetInterfaceFrom(final Rule rule) {
        NetInterface netInterface = rule.getLastRuleActionValue(
                GeneralRuleActionSpecConstants.INTERNAL_FACING_NET_INTERFACE);
        if (netInterface != null) {
            return netInterface;
        }
        return getNetInterfaceFrom(rule);
    }

    static NetInterface getInternalFacingNetInterfaceFrom(
            final Settings settings) {
        NetInterface netInterface = settings.getLastValue(
                GeneralSettingSpecConstants.INTERNAL_FACING_NET_INTERFACE);
        if (netInterface != null) {
            return netInterface;
        }
        return getNetInterfaceFrom(settings);
    }

    static SocketSettings getInternalFacingSocketSettingsFrom(
            final Rule rule) {
        SocketSettings socketSettings = SocketSettings.of(new ArrayList<>(
                rule.getRuleActionValues(
                        GeneralRuleActionSpecConstants.INTERNAL_FACING_SOCKET_SETTING)));
        if (!socketSettings.toMap().isEmpty()) {
            return socketSettings;
        }
        return getSocketSettingsFrom(rule);
    }

    static SocketSettings getInternalFacingSocketSettingsFrom(
            final Settings settings) {
        SocketSettings socketSettings = settings.getLastValue(
                GeneralSettingSpecConstants.INTERNAL_FACING_SOCKET_SETTINGS);
        if (!socketSettings.toMap().isEmpty()) {
            return socketSettings;
        }
        return getSocketSettingsFrom(settings);
    }
    
    private static NetInterface getNetInterfaceFrom(final Rule rule) {
        return rule.getLastRuleActionValue(
                GeneralRuleActionSpecConstants.NET_INTERFACE);
    }
    
    private static NetInterface getNetInterfaceFrom(final Settings settings) {
        return settings.getLastValue(
                GeneralSettingSpecConstants.NET_INTERFACE);
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

    public static Host getSocksServerBindHostFrom(final Settings settings) {
        Host host = settings.getLastValue(
                GeneralSettingSpecConstants.SOCKS_SERVER_BIND_HOST);
        if (host != null) {
            return host;
        }
        host = getInternalFacingBindHostFrom(settings);
        if (host != null) {
            return host;
        }
        NetInterface netInterface = getSocksServerNetInterfaceFrom(settings);
        if (netInterface != null) {
            return netInterface.getHostAddresses(
                    getSocksServerBindHostAddressTypesFrom(settings))
                    .stream()
                    .findFirst()
                    .orElse(HostIpv4Address.getAllZerosInstance());
        }
        return HostIpv4Address.getAllZerosInstance();
    }

    private static HostAddressTypes getSocksServerBindHostAddressTypesFrom(
            final Settings settings) {
        HostAddressTypes hostAddressTypes = settings.getLastValue(
                GeneralSettingSpecConstants.SOCKS_SERVER_BIND_HOST_ADDRESS_TYPES);
        if (!hostAddressTypes.toList().isEmpty()) {
            return hostAddressTypes;
        }
        return getInternalFacingBindHostAddressTypesFrom(settings);
    }

    public static PortRanges getSocksServerBindPortRangesFrom(
            final Settings settings) {
        Port port = settings.getLastValue(GeneralSettingSpecConstants.PORT);
        if (port != null) {
            return PortRanges.of(PortRange.of(port));
        }
        PortRanges portRanges = settings.getLastValue(
                GeneralSettingSpecConstants.SOCKS_SERVER_BIND_PORT_RANGES);
        if (!portRanges.toList().isEmpty()) {
            return portRanges;
        }
        portRanges = settings.getLastValue(
                GeneralSettingSpecConstants.INTERNAL_FACING_BIND_TCP_PORT_RANGES);
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

    private static NetInterface getSocksServerNetInterfaceFrom(
            final Settings settings) {
        NetInterface netInterface = settings.getLastValue(
                GeneralSettingSpecConstants.SOCKS_SERVER_NET_INTERFACE);
        if (netInterface != null) {
            return netInterface;
        }
        return getInternalFacingNetInterfaceFrom(settings);
    }

    public static SocketSettings getSocksServerSocketSettingsFrom(
            final Settings settings) {
        SocketSettings socketSettings = settings.getLastValue(
                GeneralSettingSpecConstants.SOCKS_SERVER_SOCKET_SETTINGS);
        if (!socketSettings.toMap().isEmpty()) {
            return socketSettings;
        }
        return getInternalFacingSocketSettingsFrom(settings);
    }
    
}
