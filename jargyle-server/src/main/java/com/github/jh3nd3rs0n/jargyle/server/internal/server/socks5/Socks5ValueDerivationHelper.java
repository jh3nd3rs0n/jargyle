package com.github.jh3nd3rs0n.jargyle.server.internal.server.socks5;

import com.github.jh3nd3rs0n.jargyle.common.net.*;
import com.github.jh3nd3rs0n.jargyle.common.number.PositiveInteger;
import com.github.jh3nd3rs0n.jargyle.server.Rule;
import com.github.jh3nd3rs0n.jargyle.server.Settings;
import com.github.jh3nd3rs0n.jargyle.server.Socks5RuleActionSpecConstants;
import com.github.jh3nd3rs0n.jargyle.server.Socks5SettingSpecConstants;
import com.github.jh3nd3rs0n.jargyle.server.internal.server.GeneralValueDerivationHelper;

import java.util.ArrayList;

/**
 * Helper class for deriving SOCKS5 specific values from {@code RuleAction}s
 * and {@code Settings}.
 */
final class Socks5ValueDerivationHelper {

    /**
     * Prevents the construction of unnecessary instances.
     */
    private Socks5ValueDerivationHelper() {
    }

    private static SocketSettings getSocks5OnBindRequestInboundSocketSettingsFrom(
            final Rule rule) {
        SocketSettings socketSettings = SocketSettings.of(new ArrayList<>(
                rule.getRuleActionValues(
                        Socks5RuleActionSpecConstants.SOCKS5_ON_BIND_REQUEST_INBOUND_SOCKET_SETTING)));
        if (!socketSettings.toMap().isEmpty()) {
            return socketSettings;
        }
        return getSocks5OnRequestExternalFacingSocketSettingsFrom(rule);
    }

    private static SocketSettings getSocks5OnBindRequestInboundSocketSettingsFrom(
            final Settings settings) {
        SocketSettings socketSettings = settings.getLastValue(
                Socks5SettingSpecConstants.SOCKS5_ON_BIND_REQUEST_INBOUND_SOCKET_SETTINGS);
        if (!socketSettings.toMap().isEmpty()) {
            return socketSettings;
        }
        return getSocks5OnRequestExternalFacingSocketSettingsFrom(settings);
    }

    public static SocketSettings getSocks5OnBindRequestInboundSocketSettingsFrom(
            final Rule rule, final Settings settings) {
        SocketSettings socketSettings =
                getSocks5OnBindRequestInboundSocketSettingsFrom(rule);
        if (!socketSettings.toMap().isEmpty()) {
            return socketSettings;
        }
        return getSocks5OnBindRequestInboundSocketSettingsFrom(settings);
    }

    private static Host getSocks5OnBindRequestListenBindHostFrom(
            final Rule rule) {
        Host host = rule.getLastRuleActionValue(
                Socks5RuleActionSpecConstants.SOCKS5_ON_BIND_REQUEST_LISTEN_BIND_HOST);
        if (host != null) {
            return host;
        }
        return getSocks5OnRequestExternalFacingBindHostFrom(rule);
    }

    private static Host getSocks5OnBindRequestListenBindHostFrom(
            final Settings settings) {
        Host host = settings.getLastValue(
                Socks5SettingSpecConstants.SOCKS5_ON_BIND_REQUEST_LISTEN_BIND_HOST);
        if (host != null) {
            return host;
        }
        return getSocks5OnRequestExternalFacingBindHostFrom(settings);
    }

    public static Host getSocks5OnBindRequestListenBindHostFrom(
            final Rule rule, final Settings settings) {
        Host host = getSocks5OnBindRequestListenBindHostFrom(rule);
        if (host != null) {
            return host;
        }
        host = getSocks5OnBindRequestListenBindHostFrom(settings);
        if (host != null) {
            return host;
        }
        NetInterface netInterface =
                getSocks5OnBindRequestListenNetInterfaceFrom(rule, settings);
        if (netInterface != null) {
            return netInterface.getHostAddresses(
                    getSocks5OnBindRequestListenBindHostAddressTypesFrom(
                            rule, settings))
                    .stream()
                    .findFirst()
                    .orElse(HostIpv4Address.getAllZerosInstance());
        }
        return HostIpv4Address.getAllZerosInstance();
    }

    private static HostAddressTypes getSocks5OnBindRequestListenBindHostAddressTypesFrom(
            final Rule rule) {
        HostAddressTypes hostAddressTypes = HostAddressTypes.of(
                rule.getRuleActionValues(
                        Socks5RuleActionSpecConstants.SOCKS5_ON_BIND_REQUEST_LISTEN_BIND_HOST_ADDRESS_TYPE));
        if (!hostAddressTypes.toList().isEmpty()) {
            return hostAddressTypes;
        }
        return getSocks5OnRequestExternalFacingBindHostAddressTypesFrom(rule);
    }

    private static HostAddressTypes getSocks5OnBindRequestListenBindHostAddressTypesFrom(
            final Settings settings) {
        HostAddressTypes hostAddressTypes = settings.getLastValue(
                Socks5SettingSpecConstants.SOCKS5_ON_BIND_REQUEST_LISTEN_BIND_HOST_ADDRESS_TYPES);
        if (!hostAddressTypes.toList().isEmpty()) {
            return hostAddressTypes;
        }
        return getSocks5OnRequestExternalFacingBindHostAddressTypesFrom(
                settings);
    }

    private static HostAddressTypes getSocks5OnBindRequestListenBindHostAddressTypesFrom(
            final Rule rule, final Settings settings) {
        HostAddressTypes hostAddressTypes =
                getSocks5OnBindRequestListenBindHostAddressTypesFrom(rule);
        if (!hostAddressTypes.toList().isEmpty()) {
            return hostAddressTypes;
        }
        return getSocks5OnBindRequestListenBindHostAddressTypesFrom(settings);
    }

    private static PortRanges getSocks5OnBindRequestListenBindPortRangesFrom(
            final Rule rule) {
        PortRanges portRanges = PortRanges.of(rule.getRuleActionValues(
                Socks5RuleActionSpecConstants.SOCKS5_ON_BIND_REQUEST_LISTEN_BIND_PORT_RANGE));
        if (!portRanges.toList().isEmpty()) {
            return portRanges;
        }
        return getSocks5OnRequestExternalFacingBindTcpPortRangesFrom(rule);
    }

    private static PortRanges getSocks5OnBindRequestListenBindPortRangesFrom(
            final Settings settings) {
        PortRanges portRanges = settings.getLastValue(
                Socks5SettingSpecConstants.SOCKS5_ON_BIND_REQUEST_LISTEN_BIND_PORT_RANGES);
        if (!portRanges.toList().isEmpty()) {
            return portRanges;
        }
        return getSocks5OnRequestExternalFacingBindTcpPortRangesFrom(settings);
    }

    public static PortRanges getSocks5OnBindRequestListenBindPortRangesFrom(
            final Rule rule, final Settings settings) {
        PortRanges portRanges = getSocks5OnBindRequestListenBindPortRangesFrom(
                rule);
        if (!portRanges.toList().isEmpty()) {
            return portRanges;
        }
        return getSocks5OnBindRequestListenBindPortRangesFrom(settings);
    }

    private static NetInterface getSocks5OnBindRequestListenNetInterfaceFrom(
            final Rule rule) {
        NetInterface netInterface = rule.getLastRuleActionValue(
                Socks5RuleActionSpecConstants.SOCKS5_ON_BIND_REQUEST_LISTEN_NET_INTERFACE);
        if (netInterface != null) {
            return netInterface;
        }
        return getSocks5OnRequestExternalFacingNetInterfaceFrom(rule);
    }

    private static NetInterface getSocks5OnBindRequestListenNetInterfaceFrom(
            final Settings settings) {
        NetInterface netInterface = settings.getLastValue(
                Socks5SettingSpecConstants.SOCKS5_ON_BIND_REQUEST_LISTEN_NET_INTERFACE);
        if (netInterface != null) {
            return netInterface;
        }
        return getSocks5OnRequestExternalFacingNetInterfaceFrom(settings);
    }

    private static NetInterface getSocks5OnBindRequestListenNetInterfaceFrom(
            final Rule rule, final Settings settings) {
        NetInterface netInterface =
                getSocks5OnBindRequestListenNetInterfaceFrom(rule);
        if (netInterface != null) {
            return netInterface;
        }
        return getSocks5OnBindRequestListenNetInterfaceFrom(settings);
    }

    private static SocketSettings getSocks5OnBindRequestListenSocketSettingsFrom(
            final Rule rule) {
        SocketSettings socketSettings = SocketSettings.of(new ArrayList<>(
                rule.getRuleActionValues(
                        Socks5RuleActionSpecConstants.SOCKS5_ON_BIND_REQUEST_LISTEN_SOCKET_SETTING)));
        if (!socketSettings.toMap().isEmpty()) {
            return socketSettings;
        }
        return getSocks5OnRequestExternalFacingSocketSettingsFrom(rule);
    }

    private static SocketSettings getSocks5OnBindRequestListenSocketSettingsFrom(
            final Settings settings) {
        SocketSettings socketSettings = settings.getLastValue(
                Socks5SettingSpecConstants.SOCKS5_ON_BIND_REQUEST_LISTEN_SOCKET_SETTINGS);
        if (!socketSettings.toMap().isEmpty()) {
            return socketSettings;
        }
        return getSocks5OnRequestExternalFacingSocketSettingsFrom(settings);
    }

    public static SocketSettings getSocks5OnBindRequestListenSocketSettingsFrom(
            final Rule rule, final Settings settings) {
        SocketSettings socketSettings =
                getSocks5OnBindRequestListenSocketSettingsFrom(rule);
        if (!socketSettings.toMap().isEmpty()) {
            return socketSettings;
        }
        return getSocks5OnBindRequestListenSocketSettingsFrom(settings);
    }

    private static PositiveInteger getSocks5OnBindRequestRelayBufferSizeFrom(
            final Rule rule) {
        PositiveInteger relayBufferSize = rule.getLastRuleActionValue(
                Socks5RuleActionSpecConstants.SOCKS5_ON_BIND_REQUEST_RELAY_BUFFER_SIZE);
        if (relayBufferSize != null) {
            return relayBufferSize;
        }
        return getSocks5OnRequestRelayBufferSizeFrom(rule);
    }
    
    private static PositiveInteger getSocks5OnBindRequestRelayBufferSizeFrom(
            final Settings settings) {
        PositiveInteger relayBufferSize = settings.getLastValue(
                Socks5SettingSpecConstants.SOCKS5_ON_BIND_REQUEST_RELAY_BUFFER_SIZE);
        if (relayBufferSize != null) {
            return relayBufferSize;
        }
        return getSocks5OnRequestRelayBufferSizeFrom(settings);
    }

    public static PositiveInteger getSocks5OnBindRequestRelayBufferSizeFrom(
            final Rule rule, final Settings settings) {
        PositiveInteger relayBufferSize = 
                getSocks5OnBindRequestRelayBufferSizeFrom(rule);
        if (relayBufferSize != null) {
            return relayBufferSize;
        }
        return getSocks5OnBindRequestRelayBufferSizeFrom(settings);
    }

    private static PositiveInteger getSocks5OnBindRequestRelayIdleTimeoutFrom(
            final Rule rule) {
        PositiveInteger relayIdleTimeout = rule.getLastRuleActionValue(
                Socks5RuleActionSpecConstants.SOCKS5_ON_BIND_REQUEST_RELAY_IDLE_TIMEOUT);
        if (relayIdleTimeout != null) {
            return relayIdleTimeout;
        }
        return getSocks5OnRequestRelayIdleTimeoutFrom(rule);
    }

    private static PositiveInteger getSocks5OnBindRequestRelayIdleTimeoutFrom(
            final Settings settings) {
        PositiveInteger relayIdleTimeout = settings.getLastValue(
                Socks5SettingSpecConstants.SOCKS5_ON_BIND_REQUEST_RELAY_IDLE_TIMEOUT);
        if (relayIdleTimeout != null) {
            return relayIdleTimeout;
        }
        return getSocks5OnRequestRelayIdleTimeoutFrom(settings);
    }

    public static PositiveInteger getSocks5OnBindRequestRelayIdleTimeoutFrom(
            final Rule rule, final Settings settings) {
        PositiveInteger relayIdleTimeout =
                getSocks5OnBindRequestRelayIdleTimeoutFrom(rule);
        if (relayIdleTimeout != null) {
            return relayIdleTimeout;
        }
        return getSocks5OnBindRequestRelayIdleTimeoutFrom(settings);
    }

    private static PositiveInteger getSocks5OnBindRequestRelayInboundBandwidthLimitFrom(
            final Rule rule) {
        PositiveInteger relayInboundBandwidthLimit = rule.getLastRuleActionValue(
                Socks5RuleActionSpecConstants.SOCKS5_ON_BIND_REQUEST_RELAY_INBOUND_BANDWIDTH_LIMIT);
        if (relayInboundBandwidthLimit != null) {
            return relayInboundBandwidthLimit;
        }
        return getSocks5OnRequestRelayInboundBandwidthLimitFrom(rule);
    }

    private static PositiveInteger getSocks5OnBindRequestRelayInboundBandwidthLimitFrom(
            final Settings settings) {
        PositiveInteger relayInboundBandwidthLimit = settings.getLastValue(
                Socks5SettingSpecConstants.SOCKS5_ON_BIND_REQUEST_RELAY_INBOUND_BANDWIDTH_LIMIT);
        if (relayInboundBandwidthLimit != null) {
            return relayInboundBandwidthLimit;
        }
        return getSocks5OnRequestRelayInboundBandwidthLimitFrom(settings);
    }

    public static PositiveInteger getSocks5OnBindRequestRelayInboundBandwidthLimitFrom(
            final Rule rule, final Settings settings) {
        PositiveInteger relayInboundBandwidthLimit =
                getSocks5OnBindRequestRelayInboundBandwidthLimitFrom(rule);
        if (relayInboundBandwidthLimit != null) {
            return relayInboundBandwidthLimit;
        }
        return getSocks5OnBindRequestRelayInboundBandwidthLimitFrom(settings);
    }

    private static PositiveInteger getSocks5OnBindRequestRelayOutboundBandwidthLimitFrom(
            final Rule rule) {
        PositiveInteger relayOutboundBandwidthLimit = rule.getLastRuleActionValue(
                Socks5RuleActionSpecConstants.SOCKS5_ON_BIND_REQUEST_RELAY_OUTBOUND_BANDWIDTH_LIMIT);
        if (relayOutboundBandwidthLimit != null) {
            return relayOutboundBandwidthLimit;
        }
        return getSocks5OnRequestRelayOutboundBandwidthLimitFrom(rule);
    }

    private static PositiveInteger getSocks5OnBindRequestRelayOutboundBandwidthLimitFrom(
            final Settings settings) {
        PositiveInteger relayOutboundBandwidthLimit = settings.getLastValue(
                Socks5SettingSpecConstants.SOCKS5_ON_BIND_REQUEST_RELAY_OUTBOUND_BANDWIDTH_LIMIT);
        if (relayOutboundBandwidthLimit != null) {
            return relayOutboundBandwidthLimit;
        }
        return getSocks5OnRequestRelayOutboundBandwidthLimitFrom(settings);
    }

    public static PositiveInteger getSocks5OnBindRequestRelayOutboundBandwidthLimitFrom(
            final Rule rule, final Settings settings) {
        PositiveInteger relayOutboundBandwidthLimit =
                getSocks5OnBindRequestRelayOutboundBandwidthLimitFrom(rule);
        if (relayOutboundBandwidthLimit != null) {
            return relayOutboundBandwidthLimit;
        }
        return getSocks5OnBindRequestRelayOutboundBandwidthLimitFrom(settings);
    }
    
    private static Host getSocks5OnRequestExternalFacingBindHostFrom(
            final Rule rule) {
        Host host = rule.getLastRuleActionValue(
                Socks5RuleActionSpecConstants.SOCKS5_ON_REQUEST_EXTERNAL_FACING_BIND_HOST);
        if (host != null) {
            return host;
        }
        return GeneralValueDerivationHelper.getExternalFacingBindHostFrom(rule);
    }
    
    private static Host getSocks5OnRequestExternalFacingBindHostFrom(
            final Settings settings) {
        Host host = settings.getLastValue(
                Socks5SettingSpecConstants.SOCKS5_ON_REQUEST_EXTERNAL_FACING_BIND_HOST);
        if (host != null) {
            return host;
        }
        return GeneralValueDerivationHelper.getExternalFacingBindHostFrom(
                settings);
    }
    
    private static HostAddressTypes getSocks5OnRequestExternalFacingBindHostAddressTypesFrom(
            final Rule rule) {
        HostAddressTypes hostAddressTypes = HostAddressTypes.of(
                rule.getRuleActionValues(
                        Socks5RuleActionSpecConstants.SOCKS5_ON_REQUEST_EXTERNAL_FACING_BIND_HOST_ADDRESS_TYPE));
        if (!hostAddressTypes.toList().isEmpty()) {
            return hostAddressTypes;
        }
        return GeneralValueDerivationHelper.getExternalFacingBindHostAddressTypesFrom(
                rule);
    }
    
    private static HostAddressTypes getSocks5OnRequestExternalFacingBindHostAddressTypesFrom(
            final Settings settings) {
        HostAddressTypes hostAddressTypes = settings.getLastValue(
                Socks5SettingSpecConstants.SOCKS5_ON_REQUEST_EXTERNAL_FACING_BIND_HOST_ADDRESS_TYPES);
        if (!hostAddressTypes.toList().isEmpty()) {
            return hostAddressTypes;
        }
        return GeneralValueDerivationHelper.getExternalFacingBindHostAddressTypesFrom(
                settings);
    }
    
    private static PortRanges getSocks5OnRequestExternalFacingBindTcpPortRangesFrom(
            final Rule rule) {
        PortRanges portRanges = PortRanges.of(rule.getRuleActionValues(
                Socks5RuleActionSpecConstants.SOCKS5_ON_REQUEST_EXTERNAL_FACING_BIND_TCP_PORT_RANGE));
        if (!portRanges.toList().isEmpty()) {
            return portRanges;
        }
        return GeneralValueDerivationHelper.getExternalFacingTcpPortRangesFrom(
                rule);
    }
    
    private static PortRanges getSocks5OnRequestExternalFacingBindTcpPortRangesFrom(
            final Settings settings) {
        PortRanges portRanges = settings.getLastValue(
                Socks5SettingSpecConstants.SOCKS5_ON_REQUEST_EXTERNAL_FACING_BIND_TCP_PORT_RANGES);
        if (!portRanges.toList().isEmpty()) {
            return portRanges;
        }
        return GeneralValueDerivationHelper.getExternalFacingTcpPortRangesFrom(
                settings);
    }

    private static PortRanges getSocks5OnRequestExternalFacingBindUdpPortRangesFrom(
            final Rule rule) {
        PortRanges portRanges = PortRanges.of(rule.getRuleActionValues(
                Socks5RuleActionSpecConstants.SOCKS5_ON_REQUEST_EXTERNAL_FACING_BIND_UDP_PORT_RANGE));
        if (!portRanges.toList().isEmpty()) {
            return portRanges;
        }
        return GeneralValueDerivationHelper.getExternalFacingUdpPortRangesFrom(
                rule);
    }

    private static PortRanges getSocks5OnRequestExternalFacingBindUdpPortRangesFrom(
            final Settings settings) {
        PortRanges portRanges = settings.getLastValue(
                Socks5SettingSpecConstants.SOCKS5_ON_REQUEST_EXTERNAL_FACING_BIND_UDP_PORT_RANGES);
        if (!portRanges.toList().isEmpty()) {
            return portRanges;
        }
        return GeneralValueDerivationHelper.getExternalFacingUdpPortRangesFrom(
                settings);
    }
    
    private static NetInterface getSocks5OnRequestExternalFacingNetInterfaceFrom(
            final Rule rule) {
        NetInterface netInterface = rule.getLastRuleActionValue(
                Socks5RuleActionSpecConstants.SOCKS5_ON_REQUEST_EXTERNAL_FACING_NET_INTERFACE);
        if (netInterface != null) {
            return netInterface;
        }
        return GeneralValueDerivationHelper.getExternalFacingNetInterfaceFrom(
                rule);
    }
    
    private static NetInterface getSocks5OnRequestExternalFacingNetInterfaceFrom(
            final Settings settings) {
        NetInterface netInterface = settings.getLastValue(
                Socks5SettingSpecConstants.SOCKS5_ON_REQUEST_EXTERNAL_FACING_NET_INTERFACE);
        if (netInterface != null) {
            return netInterface;
        }
        return GeneralValueDerivationHelper.getExternalFacingNetInterfaceFrom(
                settings);
    }
    
    private static SocketSettings getSocks5OnRequestExternalFacingSocketSettingsFrom(
            final Rule rule) {
        SocketSettings socketSettings = SocketSettings.of(new ArrayList<>(
                rule.getRuleActionValues(
                        Socks5RuleActionSpecConstants.SOCKS5_ON_REQUEST_EXTERNAL_FACING_SOCKET_SETTING)));
        if (!socketSettings.toMap().isEmpty()) {
            return socketSettings;
        }
        return GeneralValueDerivationHelper.getExternalFacingSocketSettingsFrom(
                rule);
    }
    
    private static SocketSettings getSocks5OnRequestExternalFacingSocketSettingsFrom(
            final Settings settings) {
        SocketSettings socketSettings = settings.getLastValue(
                Socks5SettingSpecConstants.SOCKS5_ON_REQUEST_EXTERNAL_FACING_SOCKET_SETTINGS);
        if (!socketSettings.toMap().isEmpty()) {
            return socketSettings;
        }
        return GeneralValueDerivationHelper.getExternalFacingSocketSettingsFrom(
                settings);
    }

    private static Host getSocks5OnRequestInternalFacingBindHostFrom(
            final Rule rule) {
        Host host = rule.getLastRuleActionValue(
                Socks5RuleActionSpecConstants.SOCKS5_ON_REQUEST_INTERNAL_FACING_BIND_HOST);
        if (host != null) {
            return host;
        }
        return GeneralValueDerivationHelper.getInternalFacingBindHostFrom(rule);
    }

    private static Host getSocks5OnRequestInternalFacingBindHostFrom(
            final Settings settings) {
        Host host = settings.getLastValue(
                Socks5SettingSpecConstants.SOCKS5_ON_REQUEST_INTERNAL_FACING_BIND_HOST);
        if (host != null) {
            return host;
        }
        return GeneralValueDerivationHelper.getInternalFacingBindHostFrom(
                settings);
    }

    private static HostAddressTypes getSocks5OnRequestInternalFacingBindHostAddressTypesFrom(
            final Rule rule) {
        HostAddressTypes hostAddressTypes = HostAddressTypes.of(
                rule.getRuleActionValues(
                        Socks5RuleActionSpecConstants.SOCKS5_ON_REQUEST_INTERNAL_FACING_BIND_HOST_ADDRESS_TYPE));
        if (!hostAddressTypes.toList().isEmpty()) {
            return hostAddressTypes;
        }
        return GeneralValueDerivationHelper.getInternalFacingBindHostAddressTypesFrom(
                rule);
    }

    private static HostAddressTypes getSocks5OnRequestInternalFacingBindHostAddressTypesFrom(
            final Settings settings) {
        HostAddressTypes hostAddressTypes = settings.getLastValue(
                Socks5SettingSpecConstants.SOCKS5_ON_REQUEST_INTERNAL_FACING_BIND_HOST_ADDRESS_TYPES);
        if (!hostAddressTypes.toList().isEmpty()) {
            return hostAddressTypes;
        }
        return GeneralValueDerivationHelper.getInternalFacingBindHostAddressTypesFrom(
                settings);
    }

    private static PortRanges getSocks5OnRequestInternalFacingBindUdpPortRangesFrom(
            final Rule rule) {
        PortRanges portRanges = PortRanges.of(rule.getRuleActionValues(
                Socks5RuleActionSpecConstants.SOCKS5_ON_REQUEST_INTERNAL_FACING_BIND_UDP_PORT_RANGE));
        if (!portRanges.toList().isEmpty()) {
            return portRanges;
        }
        return GeneralValueDerivationHelper.getInternalFacingUdpPortRangesFrom(
                rule);
    }

    private static PortRanges getSocks5OnRequestInternalFacingBindUdpPortRangesFrom(
            final Settings settings) {
        PortRanges portRanges = settings.getLastValue(
                Socks5SettingSpecConstants.SOCKS5_ON_REQUEST_INTERNAL_FACING_BIND_UDP_PORT_RANGES);
        if (!portRanges.toList().isEmpty()) {
            return portRanges;
        }
        return GeneralValueDerivationHelper.getInternalFacingUdpPortRangesFrom(
                settings);
    }

    private static NetInterface getSocks5OnRequestInternalFacingNetInterfaceFrom(
            final Rule rule) {
        NetInterface netInterface = rule.getLastRuleActionValue(
                Socks5RuleActionSpecConstants.SOCKS5_ON_REQUEST_INTERNAL_FACING_NET_INTERFACE);
        if (netInterface != null) {
            return netInterface;
        }
        return GeneralValueDerivationHelper.getInternalFacingNetInterfaceFrom(
                rule);
    }

    private static NetInterface getSocks5OnRequestInternalFacingNetInterfaceFrom(
            final Settings settings) {
        NetInterface netInterface = settings.getLastValue(
                Socks5SettingSpecConstants.SOCKS5_ON_REQUEST_INTERNAL_FACING_NET_INTERFACE);
        if (netInterface != null) {
            return netInterface;
        }
        return GeneralValueDerivationHelper.getInternalFacingNetInterfaceFrom(
                settings);
    }

    private static SocketSettings getSocks5OnRequestInternalFacingSocketSettingsFrom(
            final Rule rule) {
        SocketSettings socketSettings = SocketSettings.of(new ArrayList<>(
                rule.getRuleActionValues(
                        Socks5RuleActionSpecConstants.SOCKS5_ON_REQUEST_INTERNAL_FACING_SOCKET_SETTING)));
        if (!socketSettings.toMap().isEmpty()) {
            return socketSettings;
        }
        return GeneralValueDerivationHelper.getInternalFacingSocketSettingsFrom(
                rule);
    }

    private static SocketSettings getSocks5OnRequestInternalFacingSocketSettingsFrom(
            final Settings settings) {
        SocketSettings socketSettings = settings.getLastValue(
                Socks5SettingSpecConstants.SOCKS5_ON_REQUEST_INTERNAL_FACING_SOCKET_SETTINGS);
        if (!socketSettings.toMap().isEmpty()) {
            return socketSettings;
        }
        return GeneralValueDerivationHelper.getInternalFacingSocketSettingsFrom(
                settings);
    }
    
    private static PositiveInteger getSocks5OnRequestRelayBufferSizeFrom(
            final Rule rule) {
        return rule.getLastRuleActionValue(
                Socks5RuleActionSpecConstants.SOCKS5_ON_REQUEST_RELAY_BUFFER_SIZE);
    }
    
    private static PositiveInteger getSocks5OnRequestRelayBufferSizeFrom(
            final Settings settings) {
        return settings.getLastValue(
                Socks5SettingSpecConstants.SOCKS5_ON_REQUEST_RELAY_BUFFER_SIZE);
    }
    
    private static PositiveInteger getSocks5OnRequestRelayIdleTimeoutFrom(
            final Rule rule) {
        return rule.getLastRuleActionValue(
                Socks5RuleActionSpecConstants.SOCKS5_ON_REQUEST_RELAY_IDLE_TIMEOUT);
    }
    
    private static PositiveInteger getSocks5OnRequestRelayIdleTimeoutFrom(
            final Settings settings) {
        return settings.getLastValue(
                Socks5SettingSpecConstants.SOCKS5_ON_REQUEST_RELAY_IDLE_TIMEOUT);
    }
    
    private static PositiveInteger getSocks5OnRequestRelayInboundBandwidthLimitFrom(
            final Rule rule) {
        return rule.getLastRuleActionValue(
                Socks5RuleActionSpecConstants.SOCKS5_ON_REQUEST_RELAY_INBOUND_BANDWIDTH_LIMIT);
    }
    
    private static PositiveInteger getSocks5OnRequestRelayInboundBandwidthLimitFrom(
            final Settings settings) {
        return settings.getLastValue(
                Socks5SettingSpecConstants.SOCKS5_ON_REQUEST_RELAY_INBOUND_BANDWIDTH_LIMIT);
    }

    private static PositiveInteger getSocks5OnRequestRelayOutboundBandwidthLimitFrom(
            final Rule rule) {
        return rule.getLastRuleActionValue(
                Socks5RuleActionSpecConstants.SOCKS5_ON_REQUEST_RELAY_OUTBOUND_BANDWIDTH_LIMIT);
    }

    private static PositiveInteger getSocks5OnRequestRelayOutboundBandwidthLimitFrom(
            final Settings settings) {
        return settings.getLastValue(
                Socks5SettingSpecConstants.SOCKS5_ON_REQUEST_RELAY_OUTBOUND_BANDWIDTH_LIMIT);
    }

    private static PositiveInteger getSocks5OnConnectRequestRelayBufferSizeFrom(
            final Rule rule) {
        PositiveInteger relayBufferSize = rule.getLastRuleActionValue(
                Socks5RuleActionSpecConstants.SOCKS5_ON_CONNECT_REQUEST_RELAY_BUFFER_SIZE);
        if (relayBufferSize != null) {
            return relayBufferSize;
        }
        return getSocks5OnRequestRelayBufferSizeFrom(rule);
    }

    private static PositiveInteger getSocks5OnConnectRequestRelayBufferSizeFrom(
            final Settings settings) {
        PositiveInteger relayBufferSize = settings.getLastValue(
                Socks5SettingSpecConstants.SOCKS5_ON_CONNECT_REQUEST_RELAY_BUFFER_SIZE);
        if (relayBufferSize != null) {
            return relayBufferSize;
        }
        return getSocks5OnRequestRelayBufferSizeFrom(settings);
    }

    public static PositiveInteger getSocks5OnConnectRequestRelayBufferSizeFrom(
            final Rule rule, final Settings settings) {
        PositiveInteger relayBufferSize =
                getSocks5OnConnectRequestRelayBufferSizeFrom(rule);
        if (relayBufferSize != null) {
            return relayBufferSize;
        }
        return getSocks5OnConnectRequestRelayBufferSizeFrom(settings);
    }

    private static PositiveInteger getSocks5OnConnectRequestRelayIdleTimeoutFrom(
            final Rule rule) {
        PositiveInteger relayIdleTimeout = rule.getLastRuleActionValue(
                Socks5RuleActionSpecConstants.SOCKS5_ON_CONNECT_REQUEST_RELAY_IDLE_TIMEOUT);
        if (relayIdleTimeout != null) {
            return relayIdleTimeout;
        }
        return getSocks5OnRequestRelayIdleTimeoutFrom(rule);
    }

    private static PositiveInteger getSocks5OnConnectRequestRelayIdleTimeoutFrom(
            final Settings settings) {
        PositiveInteger relayIdleTimeout = settings.getLastValue(
                Socks5SettingSpecConstants.SOCKS5_ON_CONNECT_REQUEST_RELAY_IDLE_TIMEOUT);
        if (relayIdleTimeout != null) {
            return relayIdleTimeout;
        }
        return getSocks5OnRequestRelayIdleTimeoutFrom(settings);
    }

    public static PositiveInteger getSocks5OnConnectRequestRelayIdleTimeoutFrom(
            final Rule rule, final Settings settings) {
        PositiveInteger relayIdleTimeout =
                getSocks5OnConnectRequestRelayIdleTimeoutFrom(rule);
        if (relayIdleTimeout != null) {
            return relayIdleTimeout;
        }
        return getSocks5OnConnectRequestRelayIdleTimeoutFrom(settings);
    }

    private static PositiveInteger getSocks5OnConnectRequestRelayInboundBandwidthLimitFrom(
            final Rule rule) {
        PositiveInteger relayInboundBandwidthLimit = rule.getLastRuleActionValue(
                Socks5RuleActionSpecConstants.SOCKS5_ON_CONNECT_REQUEST_RELAY_INBOUND_BANDWIDTH_LIMIT);
        if (relayInboundBandwidthLimit != null) {
            return relayInboundBandwidthLimit;
        }
        return getSocks5OnRequestRelayInboundBandwidthLimitFrom(rule);
    }

    private static PositiveInteger getSocks5OnConnectRequestRelayInboundBandwidthLimitFrom(
            final Settings settings) {
        PositiveInteger relayInboundBandwidthLimit = settings.getLastValue(
                Socks5SettingSpecConstants.SOCKS5_ON_CONNECT_REQUEST_RELAY_INBOUND_BANDWIDTH_LIMIT);
        if (relayInboundBandwidthLimit != null) {
            return relayInboundBandwidthLimit;
        }
        return getSocks5OnRequestRelayInboundBandwidthLimitFrom(settings);
    }

    public static PositiveInteger getSocks5OnConnectRequestRelayInboundBandwidthLimitFrom(
            final Rule rule, final Settings settings) {
        PositiveInteger relayInboundBandwidthLimit =
                getSocks5OnConnectRequestRelayInboundBandwidthLimitFrom(rule);
        if (relayInboundBandwidthLimit != null) {
            return relayInboundBandwidthLimit;
        }
        return getSocks5OnConnectRequestRelayInboundBandwidthLimitFrom(settings);
    }

    private static PositiveInteger getSocks5OnConnectRequestRelayOutboundBandwidthLimitFrom(
            final Rule rule) {
        PositiveInteger relayOutboundBandwidthLimit = rule.getLastRuleActionValue(
                Socks5RuleActionSpecConstants.SOCKS5_ON_CONNECT_REQUEST_RELAY_OUTBOUND_BANDWIDTH_LIMIT);
        if (relayOutboundBandwidthLimit != null) {
            return relayOutboundBandwidthLimit;
        }
        return getSocks5OnRequestRelayOutboundBandwidthLimitFrom(rule);
    }

    private static PositiveInteger getSocks5OnConnectRequestRelayOutboundBandwidthLimitFrom(
            final Settings settings) {
        PositiveInteger relayOutboundBandwidthLimit = settings.getLastValue(
                Socks5SettingSpecConstants.SOCKS5_ON_CONNECT_REQUEST_RELAY_OUTBOUND_BANDWIDTH_LIMIT);
        if (relayOutboundBandwidthLimit != null) {
            return relayOutboundBandwidthLimit;
        }
        return getSocks5OnRequestRelayOutboundBandwidthLimitFrom(settings);
    }

    public static PositiveInteger getSocks5OnConnectRequestRelayOutboundBandwidthLimitFrom(
            final Rule rule, final Settings settings) {
        PositiveInteger relayOutboundBandwidthLimit =
                getSocks5OnConnectRequestRelayOutboundBandwidthLimitFrom(rule);
        if (relayOutboundBandwidthLimit != null) {
            return relayOutboundBandwidthLimit;
        }
        return getSocks5OnConnectRequestRelayOutboundBandwidthLimitFrom(settings);
    }

    private static Host getSocks5OnConnectRequestTargetFacingBindHostFrom(
            final Rule rule) {
        Host host = rule.getLastRuleActionValue(
                Socks5RuleActionSpecConstants.SOCKS5_ON_CONNECT_REQUEST_TARGET_FACING_BIND_HOST);
        if (host != null) {
            return host;
        }
        return getSocks5OnRequestExternalFacingBindHostFrom(rule);
    }

    private static Host getSocks5OnConnectRequestTargetFacingBindHostFrom(
            final Settings settings) {
        Host host = settings.getLastValue(
                Socks5SettingSpecConstants.SOCKS5_ON_CONNECT_REQUEST_TARGET_FACING_BIND_HOST);
        if (host != null) {
            return host;
        }
        return getSocks5OnRequestExternalFacingBindHostFrom(settings);
    }

    public static Host getSocks5OnConnectRequestTargetFacingBindHostFrom(
            final Rule rule, final Settings settings) {
        Host host = getSocks5OnConnectRequestTargetFacingBindHostFrom(rule);
        if (host != null) {
            return host;
        }
        host = getSocks5OnConnectRequestTargetFacingBindHostFrom(settings);
        if (host != null) {
            return host;
        }
        NetInterface netInterface =
                getSocks5OnConnectRequestTargetFacingNetInterfaceFrom(rule, settings);
        if (netInterface != null) {
            return netInterface.getHostAddresses(
                            getSocks5OnConnectRequestTargetFacingBindHostAddressTypesFrom(
                                    rule, settings))
                    .stream()
                    .findFirst()
                    .orElse(HostIpv4Address.getAllZerosInstance());
        }
        return HostIpv4Address.getAllZerosInstance();
    }

    private static HostAddressTypes getSocks5OnConnectRequestTargetFacingBindHostAddressTypesFrom(
            final Rule rule) {
        HostAddressTypes hostAddressTypes = HostAddressTypes.of(
                rule.getRuleActionValues(
                        Socks5RuleActionSpecConstants.SOCKS5_ON_CONNECT_REQUEST_TARGET_FACING_BIND_HOST_ADDRESS_TYPE));
        if (!hostAddressTypes.toList().isEmpty()) {
            return hostAddressTypes;
        }
        return getSocks5OnRequestExternalFacingBindHostAddressTypesFrom(rule);
    }

    private static HostAddressTypes getSocks5OnConnectRequestTargetFacingBindHostAddressTypesFrom(
            final Settings settings) {
        HostAddressTypes hostAddressTypes = settings.getLastValue(
                Socks5SettingSpecConstants.SOCKS5_ON_CONNECT_REQUEST_TARGET_FACING_BIND_HOST_ADDRESS_TYPES);
        if (!hostAddressTypes.toList().isEmpty()) {
            return hostAddressTypes;
        }
        return getSocks5OnRequestExternalFacingBindHostAddressTypesFrom(
                settings);
    }

    private static HostAddressTypes getSocks5OnConnectRequestTargetFacingBindHostAddressTypesFrom(
            final Rule rule, final Settings settings) {
        HostAddressTypes hostAddressTypes =
                getSocks5OnConnectRequestTargetFacingBindHostAddressTypesFrom(rule);
        if (!hostAddressTypes.toList().isEmpty()) {
            return hostAddressTypes;
        }
        return getSocks5OnConnectRequestTargetFacingBindHostAddressTypesFrom(settings);
    }

    private static PortRanges getSocks5OnConnectRequestTargetFacingBindPortRangesFrom(
            final Rule rule) {
        PortRanges portRanges = PortRanges.of(rule.getRuleActionValues(
                Socks5RuleActionSpecConstants.SOCKS5_ON_CONNECT_REQUEST_TARGET_FACING_BIND_PORT_RANGE));
        if (!portRanges.toList().isEmpty()) {
            return portRanges;
        }
        return getSocks5OnRequestExternalFacingBindTcpPortRangesFrom(rule);
    }

    private static PortRanges getSocks5OnConnectRequestTargetFacingBindPortRangesFrom(
            final Settings settings) {
        PortRanges portRanges = settings.getLastValue(
                Socks5SettingSpecConstants.SOCKS5_ON_CONNECT_REQUEST_TARGET_FACING_BIND_PORT_RANGES);
        if (!portRanges.toList().isEmpty()) {
            return portRanges;
        }
        return getSocks5OnRequestExternalFacingBindTcpPortRangesFrom(settings);
    }

    public static PortRanges getSocks5OnConnectRequestTargetFacingBindPortRangesFrom(
            final Rule rule, final Settings settings) {
        PortRanges portRanges = getSocks5OnConnectRequestTargetFacingBindPortRangesFrom(
                rule);
        if (!portRanges.toList().isEmpty()) {
            return portRanges;
        }
        return getSocks5OnConnectRequestTargetFacingBindPortRangesFrom(settings);
    }

    private static NetInterface getSocks5OnConnectRequestTargetFacingNetInterfaceFrom(
            final Rule rule) {
        NetInterface netInterface = rule.getLastRuleActionValue(
                Socks5RuleActionSpecConstants.SOCKS5_ON_CONNECT_REQUEST_TARGET_FACING_NET_INTERFACE);
        if (netInterface != null) {
            return netInterface;
        }
        return getSocks5OnRequestExternalFacingNetInterfaceFrom(rule);
    }

    private static NetInterface getSocks5OnConnectRequestTargetFacingNetInterfaceFrom(
            final Settings settings) {
        NetInterface netInterface = settings.getLastValue(
                Socks5SettingSpecConstants.SOCKS5_ON_CONNECT_REQUEST_TARGET_FACING_NET_INTERFACE);
        if (netInterface != null) {
            return netInterface;
        }
        return getSocks5OnRequestExternalFacingNetInterfaceFrom(settings);
    }

    private static NetInterface getSocks5OnConnectRequestTargetFacingNetInterfaceFrom(
            final Rule rule, final Settings settings) {
        NetInterface netInterface =
                getSocks5OnConnectRequestTargetFacingNetInterfaceFrom(rule);
        if (netInterface != null) {
            return netInterface;
        }
        return getSocks5OnConnectRequestTargetFacingNetInterfaceFrom(settings);
    }

    private static SocketSettings getSocks5OnConnectRequestTargetFacingSocketSettingsFrom(
            final Rule rule) {
        SocketSettings socketSettings = SocketSettings.of(new ArrayList<>(
                rule.getRuleActionValues(
                        Socks5RuleActionSpecConstants.SOCKS5_ON_CONNECT_REQUEST_TARGET_FACING_SOCKET_SETTING)));
        if (!socketSettings.toMap().isEmpty()) {
            return socketSettings;
        }
        return getSocks5OnRequestExternalFacingSocketSettingsFrom(rule);
    }

    private static SocketSettings getSocks5OnConnectRequestTargetFacingSocketSettingsFrom(
            final Settings settings) {
        SocketSettings socketSettings = settings.getLastValue(
                Socks5SettingSpecConstants.SOCKS5_ON_CONNECT_REQUEST_TARGET_FACING_SOCKET_SETTINGS);
        if (!socketSettings.toMap().isEmpty()) {
            return socketSettings;
        }
        return getSocks5OnRequestExternalFacingSocketSettingsFrom(settings);
    }

    public static SocketSettings getSocks5OnConnectRequestTargetFacingSocketSettingsFrom(
            final Rule rule, final Settings settings) {
        SocketSettings socketSettings =
                getSocks5OnConnectRequestTargetFacingSocketSettingsFrom(rule);
        if (!socketSettings.toMap().isEmpty()) {
            return socketSettings;
        }
        return getSocks5OnConnectRequestTargetFacingSocketSettingsFrom(settings);
    }

    private static Host getSocks5OnUdpAssociateRequestClientFacingBindHostFrom(
            final Rule rule) {
        Host host = rule.getLastRuleActionValue(
                Socks5RuleActionSpecConstants.SOCKS5_ON_UDP_ASSOCIATE_REQUEST_CLIENT_FACING_BIND_HOST);
        if (host != null) {
            return host;
        }
        return getSocks5OnRequestInternalFacingBindHostFrom(rule);
    }

    private static Host getSocks5OnUdpAssociateRequestClientFacingBindHostFrom(
            final Settings settings) {
        Host host = settings.getLastValue(
                Socks5SettingSpecConstants.SOCKS5_ON_UDP_ASSOCIATE_REQUEST_CLIENT_FACING_BIND_HOST);
        if (host != null) {
            return host;
        }
        return getSocks5OnRequestInternalFacingBindHostFrom(settings);
    }

    public static Host getSocks5OnUdpAssociateRequestClientFacingBindHostFrom(
            final Rule rule, final Settings settings) {
        Host host = getSocks5OnUdpAssociateRequestClientFacingBindHostFrom(rule);
        if (host != null) {
            return host;
        }
        host = getSocks5OnUdpAssociateRequestClientFacingBindHostFrom(settings);
        if (host != null) {
            return host;
        }
        NetInterface netInterface =
                getSocks5OnUdpAssociateRequestClientFacingNetInterfaceFrom(rule, settings);
        if (netInterface != null) {
            return netInterface.getHostAddresses(
                            getSocks5OnUdpAssociateRequestClientFacingBindHostAddressTypesFrom(
                                    rule, settings))
                    .stream()
                    .findFirst()
                    .orElse(HostIpv4Address.getAllZerosInstance());
        }
        return HostIpv4Address.getAllZerosInstance();
    }

    private static HostAddressTypes getSocks5OnUdpAssociateRequestClientFacingBindHostAddressTypesFrom(
            final Rule rule) {
        HostAddressTypes hostAddressTypes = HostAddressTypes.of(
                rule.getRuleActionValues(
                        Socks5RuleActionSpecConstants.SOCKS5_ON_UDP_ASSOCIATE_REQUEST_CLIENT_FACING_BIND_HOST_ADDRESS_TYPE));
        if (!hostAddressTypes.toList().isEmpty()) {
            return hostAddressTypes;
        }
        return getSocks5OnRequestInternalFacingBindHostAddressTypesFrom(rule);
    }

    private static HostAddressTypes getSocks5OnUdpAssociateRequestClientFacingBindHostAddressTypesFrom(
            final Settings settings) {
        HostAddressTypes hostAddressTypes = settings.getLastValue(
                Socks5SettingSpecConstants.SOCKS5_ON_UDP_ASSOCIATE_REQUEST_CLIENT_FACING_BIND_HOST_ADDRESS_TYPES);
        if (!hostAddressTypes.toList().isEmpty()) {
            return hostAddressTypes;
        }
        return getSocks5OnRequestInternalFacingBindHostAddressTypesFrom(
                settings);
    }

    private static HostAddressTypes getSocks5OnUdpAssociateRequestClientFacingBindHostAddressTypesFrom(
            final Rule rule, final Settings settings) {
        HostAddressTypes hostAddressTypes =
                getSocks5OnUdpAssociateRequestClientFacingBindHostAddressTypesFrom(rule);
        if (!hostAddressTypes.toList().isEmpty()) {
            return hostAddressTypes;
        }
        return getSocks5OnUdpAssociateRequestClientFacingBindHostAddressTypesFrom(settings);
    }

    private static PortRanges getSocks5OnUdpAssociateRequestClientFacingBindPortRangesFrom(
            final Rule rule) {
        PortRanges portRanges = PortRanges.of(rule.getRuleActionValues(
                Socks5RuleActionSpecConstants.SOCKS5_ON_UDP_ASSOCIATE_REQUEST_CLIENT_FACING_BIND_PORT_RANGE));
        if (!portRanges.toList().isEmpty()) {
            return portRanges;
        }
        return getSocks5OnRequestInternalFacingBindUdpPortRangesFrom(rule);
    }

    private static PortRanges getSocks5OnUdpAssociateRequestClientFacingBindPortRangesFrom(
            final Settings settings) {
        PortRanges portRanges = settings.getLastValue(
                Socks5SettingSpecConstants.SOCKS5_ON_UDP_ASSOCIATE_REQUEST_CLIENT_FACING_BIND_PORT_RANGES);
        if (!portRanges.toList().isEmpty()) {
            return portRanges;
        }
        return getSocks5OnRequestInternalFacingBindUdpPortRangesFrom(settings);
    }

    public static PortRanges getSocks5OnUdpAssociateRequestClientFacingBindPortRangesFrom(
            final Rule rule, final Settings settings) {
        PortRanges portRanges = getSocks5OnUdpAssociateRequestClientFacingBindPortRangesFrom(
                rule);
        if (!portRanges.toList().isEmpty()) {
            return portRanges;
        }
        return getSocks5OnUdpAssociateRequestClientFacingBindPortRangesFrom(settings);
    }

    private static NetInterface getSocks5OnUdpAssociateRequestClientFacingNetInterfaceFrom(
            final Rule rule) {
        NetInterface netInterface = rule.getLastRuleActionValue(
                Socks5RuleActionSpecConstants.SOCKS5_ON_UDP_ASSOCIATE_REQUEST_CLIENT_FACING_NET_INTERFACE);
        if (netInterface != null) {
            return netInterface;
        }
        return getSocks5OnRequestInternalFacingNetInterfaceFrom(rule);
    }

    private static NetInterface getSocks5OnUdpAssociateRequestClientFacingNetInterfaceFrom(
            final Settings settings) {
        NetInterface netInterface = settings.getLastValue(
                Socks5SettingSpecConstants.SOCKS5_ON_UDP_ASSOCIATE_REQUEST_CLIENT_FACING_NET_INTERFACE);
        if (netInterface != null) {
            return netInterface;
        }
        return getSocks5OnRequestInternalFacingNetInterfaceFrom(settings);
    }

    private static NetInterface getSocks5OnUdpAssociateRequestClientFacingNetInterfaceFrom(
            final Rule rule, final Settings settings) {
        NetInterface netInterface =
                getSocks5OnUdpAssociateRequestClientFacingNetInterfaceFrom(rule);
        if (netInterface != null) {
            return netInterface;
        }
        return getSocks5OnUdpAssociateRequestClientFacingNetInterfaceFrom(settings);
    }

    private static SocketSettings getSocks5OnUdpAssociateRequestClientFacingSocketSettingsFrom(
            final Rule rule) {
        SocketSettings socketSettings = SocketSettings.of(new ArrayList<>(
                rule.getRuleActionValues(
                        Socks5RuleActionSpecConstants.SOCKS5_ON_UDP_ASSOCIATE_REQUEST_CLIENT_FACING_SOCKET_SETTING)));
        if (!socketSettings.toMap().isEmpty()) {
            return socketSettings;
        }
        return getSocks5OnRequestInternalFacingSocketSettingsFrom(rule);
    }

    private static SocketSettings getSocks5OnUdpAssociateRequestClientFacingSocketSettingsFrom(
            final Settings settings) {
        SocketSettings socketSettings = settings.getLastValue(
                Socks5SettingSpecConstants.SOCKS5_ON_UDP_ASSOCIATE_REQUEST_CLIENT_FACING_SOCKET_SETTINGS);
        if (!socketSettings.toMap().isEmpty()) {
            return socketSettings;
        }
        return getSocks5OnRequestInternalFacingSocketSettingsFrom(settings);
    }

    public static SocketSettings getSocks5OnUdpAssociateRequestClientFacingSocketSettingsFrom(
            final Rule rule, final Settings settings) {
        SocketSettings socketSettings =
                getSocks5OnUdpAssociateRequestClientFacingSocketSettingsFrom(rule);
        if (!socketSettings.toMap().isEmpty()) {
            return socketSettings;
        }
        return getSocks5OnUdpAssociateRequestClientFacingSocketSettingsFrom(settings);
    }

    private static Host getSocks5OnUdpAssociateRequestPeerFacingBindHostFrom(
            final Rule rule) {
        Host host = rule.getLastRuleActionValue(
                Socks5RuleActionSpecConstants.SOCKS5_ON_UDP_ASSOCIATE_REQUEST_PEER_FACING_BIND_HOST);
        if (host != null) {
            return host;
        }
        return getSocks5OnRequestExternalFacingBindHostFrom(rule);
    }

    private static Host getSocks5OnUdpAssociateRequestPeerFacingBindHostFrom(
            final Settings settings) {
        Host host = settings.getLastValue(
                Socks5SettingSpecConstants.SOCKS5_ON_UDP_ASSOCIATE_REQUEST_PEER_FACING_BIND_HOST);
        if (host != null) {
            return host;
        }
        return getSocks5OnRequestExternalFacingBindHostFrom(settings);
    }

    public static Host getSocks5OnUdpAssociateRequestPeerFacingBindHostFrom(
            final Rule rule, final Settings settings) {
        Host host = getSocks5OnUdpAssociateRequestPeerFacingBindHostFrom(rule);
        if (host != null) {
            return host;
        }
        host = getSocks5OnUdpAssociateRequestPeerFacingBindHostFrom(settings);
        if (host != null) {
            return host;
        }
        NetInterface netInterface =
                getSocks5OnUdpAssociateRequestPeerFacingNetInterfaceFrom(rule, settings);
        if (netInterface != null) {
            return netInterface.getHostAddresses(
                            getSocks5OnUdpAssociateRequestPeerFacingBindHostAddressTypesFrom(
                                    rule, settings))
                    .stream()
                    .findFirst()
                    .orElse(HostIpv4Address.getAllZerosInstance());
        }
        return HostIpv4Address.getAllZerosInstance();
    }

    private static HostAddressTypes getSocks5OnUdpAssociateRequestPeerFacingBindHostAddressTypesFrom(
            final Rule rule) {
        HostAddressTypes hostAddressTypes = HostAddressTypes.of(
                rule.getRuleActionValues(
                        Socks5RuleActionSpecConstants.SOCKS5_ON_UDP_ASSOCIATE_REQUEST_PEER_FACING_BIND_HOST_ADDRESS_TYPE));
        if (!hostAddressTypes.toList().isEmpty()) {
            return hostAddressTypes;
        }
        return getSocks5OnRequestExternalFacingBindHostAddressTypesFrom(rule);
    }

    private static HostAddressTypes getSocks5OnUdpAssociateRequestPeerFacingBindHostAddressTypesFrom(
            final Settings settings) {
        HostAddressTypes hostAddressTypes = settings.getLastValue(
                Socks5SettingSpecConstants.SOCKS5_ON_UDP_ASSOCIATE_REQUEST_PEER_FACING_BIND_HOST_ADDRESS_TYPES);
        if (!hostAddressTypes.toList().isEmpty()) {
            return hostAddressTypes;
        }
        return getSocks5OnRequestExternalFacingBindHostAddressTypesFrom(
                settings);
    }

    private static HostAddressTypes getSocks5OnUdpAssociateRequestPeerFacingBindHostAddressTypesFrom(
            final Rule rule, final Settings settings) {
        HostAddressTypes hostAddressTypes =
                getSocks5OnUdpAssociateRequestPeerFacingBindHostAddressTypesFrom(rule);
        if (!hostAddressTypes.toList().isEmpty()) {
            return hostAddressTypes;
        }
        return getSocks5OnUdpAssociateRequestPeerFacingBindHostAddressTypesFrom(settings);
    }

    private static PortRanges getSocks5OnUdpAssociateRequestPeerFacingBindPortRangesFrom(
            final Rule rule) {
        PortRanges portRanges = PortRanges.of(rule.getRuleActionValues(
                Socks5RuleActionSpecConstants.SOCKS5_ON_UDP_ASSOCIATE_REQUEST_PEER_FACING_BIND_PORT_RANGE));
        if (!portRanges.toList().isEmpty()) {
            return portRanges;
        }
        return getSocks5OnRequestExternalFacingBindUdpPortRangesFrom(rule);
    }

    private static PortRanges getSocks5OnUdpAssociateRequestPeerFacingBindPortRangesFrom(
            final Settings settings) {
        PortRanges portRanges = settings.getLastValue(
                Socks5SettingSpecConstants.SOCKS5_ON_UDP_ASSOCIATE_REQUEST_PEER_FACING_BIND_PORT_RANGES);
        if (!portRanges.toList().isEmpty()) {
            return portRanges;
        }
        return getSocks5OnRequestExternalFacingBindUdpPortRangesFrom(settings);
    }

    public static PortRanges getSocks5OnUdpAssociateRequestPeerFacingBindPortRangesFrom(
            final Rule rule, final Settings settings) {
        PortRanges portRanges = getSocks5OnUdpAssociateRequestPeerFacingBindPortRangesFrom(
                rule);
        if (!portRanges.toList().isEmpty()) {
            return portRanges;
        }
        return getSocks5OnUdpAssociateRequestPeerFacingBindPortRangesFrom(settings);
    }

    private static NetInterface getSocks5OnUdpAssociateRequestPeerFacingNetInterfaceFrom(
            final Rule rule) {
        NetInterface netInterface = rule.getLastRuleActionValue(
                Socks5RuleActionSpecConstants.SOCKS5_ON_UDP_ASSOCIATE_REQUEST_PEER_FACING_NET_INTERFACE);
        if (netInterface != null) {
            return netInterface;
        }
        return getSocks5OnRequestExternalFacingNetInterfaceFrom(rule);
    }

    private static NetInterface getSocks5OnUdpAssociateRequestPeerFacingNetInterfaceFrom(
            final Settings settings) {
        NetInterface netInterface = settings.getLastValue(
                Socks5SettingSpecConstants.SOCKS5_ON_UDP_ASSOCIATE_REQUEST_PEER_FACING_NET_INTERFACE);
        if (netInterface != null) {
            return netInterface;
        }
        return getSocks5OnRequestExternalFacingNetInterfaceFrom(settings);
    }

    private static NetInterface getSocks5OnUdpAssociateRequestPeerFacingNetInterfaceFrom(
            final Rule rule, final Settings settings) {
        NetInterface netInterface =
                getSocks5OnUdpAssociateRequestPeerFacingNetInterfaceFrom(rule);
        if (netInterface != null) {
            return netInterface;
        }
        return getSocks5OnUdpAssociateRequestPeerFacingNetInterfaceFrom(settings);
    }

    private static SocketSettings getSocks5OnUdpAssociateRequestPeerFacingSocketSettingsFrom(
            final Rule rule) {
        SocketSettings socketSettings = SocketSettings.of(new ArrayList<>(
                rule.getRuleActionValues(
                        Socks5RuleActionSpecConstants.SOCKS5_ON_UDP_ASSOCIATE_REQUEST_PEER_FACING_SOCKET_SETTING)));
        if (!socketSettings.toMap().isEmpty()) {
            return socketSettings;
        }
        return getSocks5OnRequestExternalFacingSocketSettingsFrom(rule);
    }

    private static SocketSettings getSocks5OnUdpAssociateRequestPeerFacingSocketSettingsFrom(
            final Settings settings) {
        SocketSettings socketSettings = settings.getLastValue(
                Socks5SettingSpecConstants.SOCKS5_ON_UDP_ASSOCIATE_REQUEST_PEER_FACING_SOCKET_SETTINGS);
        if (!socketSettings.toMap().isEmpty()) {
            return socketSettings;
        }
        return getSocks5OnRequestExternalFacingSocketSettingsFrom(settings);
    }

    public static SocketSettings getSocks5OnUdpAssociateRequestPeerFacingSocketSettingsFrom(
            final Rule rule, final Settings settings) {
        SocketSettings socketSettings =
                getSocks5OnUdpAssociateRequestPeerFacingSocketSettingsFrom(rule);
        if (!socketSettings.toMap().isEmpty()) {
            return socketSettings;
        }
        return getSocks5OnUdpAssociateRequestPeerFacingSocketSettingsFrom(settings);
    }

    private static PositiveInteger getSocks5OnUdpAssociateRequestRelayBufferSizeFrom(
            final Rule rule) {
        PositiveInteger relayBufferSize = rule.getLastRuleActionValue(
                Socks5RuleActionSpecConstants.SOCKS5_ON_UDP_ASSOCIATE_REQUEST_RELAY_BUFFER_SIZE);
        if (relayBufferSize != null) {
            return relayBufferSize;
        }
        return getSocks5OnRequestRelayBufferSizeFrom(rule);
    }

    private static PositiveInteger getSocks5OnUdpAssociateRequestRelayBufferSizeFrom(
            final Settings settings) {
        PositiveInteger relayBufferSize = settings.getLastValue(
                Socks5SettingSpecConstants.SOCKS5_ON_UDP_ASSOCIATE_REQUEST_RELAY_BUFFER_SIZE);
        if (relayBufferSize != null) {
            return relayBufferSize;
        }
        return getSocks5OnRequestRelayBufferSizeFrom(settings);
    }

    public static PositiveInteger getSocks5OnUdpAssociateRequestRelayBufferSizeFrom(
            final Rule rule, final Settings settings) {
        PositiveInteger relayBufferSize =
                getSocks5OnUdpAssociateRequestRelayBufferSizeFrom(rule);
        if (relayBufferSize != null) {
            return relayBufferSize;
        }
        return getSocks5OnUdpAssociateRequestRelayBufferSizeFrom(settings);
    }

    private static PositiveInteger getSocks5OnUdpAssociateRequestRelayIdleTimeoutFrom(
            final Rule rule) {
        PositiveInteger relayIdleTimeout = rule.getLastRuleActionValue(
                Socks5RuleActionSpecConstants.SOCKS5_ON_UDP_ASSOCIATE_REQUEST_RELAY_IDLE_TIMEOUT);
        if (relayIdleTimeout != null) {
            return relayIdleTimeout;
        }
        return getSocks5OnRequestRelayIdleTimeoutFrom(rule);
    }

    private static PositiveInteger getSocks5OnUdpAssociateRequestRelayIdleTimeoutFrom(
            final Settings settings) {
        PositiveInteger relayIdleTimeout = settings.getLastValue(
                Socks5SettingSpecConstants.SOCKS5_ON_UDP_ASSOCIATE_REQUEST_RELAY_IDLE_TIMEOUT);
        if (relayIdleTimeout != null) {
            return relayIdleTimeout;
        }
        return getSocks5OnRequestRelayIdleTimeoutFrom(settings);
    }

    public static PositiveInteger getSocks5OnUdpAssociateRequestRelayIdleTimeoutFrom(
            final Rule rule, final Settings settings) {
        PositiveInteger relayIdleTimeout =
                getSocks5OnUdpAssociateRequestRelayIdleTimeoutFrom(rule);
        if (relayIdleTimeout != null) {
            return relayIdleTimeout;
        }
        return getSocks5OnUdpAssociateRequestRelayIdleTimeoutFrom(settings);
    }

    private static PositiveInteger getSocks5OnUdpAssociateRequestRelayInboundBandwidthLimitFrom(
            final Rule rule) {
        PositiveInteger relayInboundBandwidthLimit = rule.getLastRuleActionValue(
                Socks5RuleActionSpecConstants.SOCKS5_ON_UDP_ASSOCIATE_REQUEST_RELAY_INBOUND_BANDWIDTH_LIMIT);
        if (relayInboundBandwidthLimit != null) {
            return relayInboundBandwidthLimit;
        }
        return getSocks5OnRequestRelayInboundBandwidthLimitFrom(rule);
    }

    private static PositiveInteger getSocks5OnUdpAssociateRequestRelayInboundBandwidthLimitFrom(
            final Settings settings) {
        PositiveInteger relayInboundBandwidthLimit = settings.getLastValue(
                Socks5SettingSpecConstants.SOCKS5_ON_UDP_ASSOCIATE_REQUEST_RELAY_INBOUND_BANDWIDTH_LIMIT);
        if (relayInboundBandwidthLimit != null) {
            return relayInboundBandwidthLimit;
        }
        return getSocks5OnRequestRelayInboundBandwidthLimitFrom(settings);
    }

    public static PositiveInteger getSocks5OnUdpAssociateRequestRelayInboundBandwidthLimitFrom(
            final Rule rule, final Settings settings) {
        PositiveInteger relayInboundBandwidthLimit =
                getSocks5OnUdpAssociateRequestRelayInboundBandwidthLimitFrom(rule);
        if (relayInboundBandwidthLimit != null) {
            return relayInboundBandwidthLimit;
        }
        return getSocks5OnUdpAssociateRequestRelayInboundBandwidthLimitFrom(settings);
    }

    private static PositiveInteger getSocks5OnUdpAssociateRequestRelayOutboundBandwidthLimitFrom(
            final Rule rule) {
        PositiveInteger relayOutboundBandwidthLimit = rule.getLastRuleActionValue(
                Socks5RuleActionSpecConstants.SOCKS5_ON_UDP_ASSOCIATE_REQUEST_RELAY_OUTBOUND_BANDWIDTH_LIMIT);
        if (relayOutboundBandwidthLimit != null) {
            return relayOutboundBandwidthLimit;
        }
        return getSocks5OnRequestRelayOutboundBandwidthLimitFrom(rule);
    }

    private static PositiveInteger getSocks5OnUdpAssociateRequestRelayOutboundBandwidthLimitFrom(
            final Settings settings) {
        PositiveInteger relayOutboundBandwidthLimit = settings.getLastValue(
                Socks5SettingSpecConstants.SOCKS5_ON_UDP_ASSOCIATE_REQUEST_RELAY_OUTBOUND_BANDWIDTH_LIMIT);
        if (relayOutboundBandwidthLimit != null) {
            return relayOutboundBandwidthLimit;
        }
        return getSocks5OnRequestRelayOutboundBandwidthLimitFrom(settings);
    }

    public static PositiveInteger getSocks5OnUdpAssociateRequestRelayOutboundBandwidthLimitFrom(
            final Rule rule, final Settings settings) {
        PositiveInteger relayOutboundBandwidthLimit =
                getSocks5OnUdpAssociateRequestRelayOutboundBandwidthLimitFrom(rule);
        if (relayOutboundBandwidthLimit != null) {
            return relayOutboundBandwidthLimit;
        }
        return getSocks5OnUdpAssociateRequestRelayOutboundBandwidthLimitFrom(settings);
    }
    
}
