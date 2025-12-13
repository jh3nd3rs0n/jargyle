package com.github.jh3nd3rs0n.jargyle.server.internal.server;

import com.github.jh3nd3rs0n.jargyle.common.net.*;
import com.github.jh3nd3rs0n.jargyle.common.number.PositiveInteger;
import com.github.jh3nd3rs0n.jargyle.common.string.CommaSeparatedValues;
import com.github.jh3nd3rs0n.jargyle.server.*;

import java.util.ArrayList;

/**
 * Helper class for deriving SOCKS specific values from {@code RuleAction}s
 * and {@code Settings}.
 */
public final class SocksValueDerivationHelper {

    /**
     * Prevents the construction of unnecessary instances.
     */    
    private SocksValueDerivationHelper() {
    }

    public static Boolean getSocksGssapiAuthMethodNecReferenceImplFrom(
            final Settings settings) {
        return settings.getLastValue(
                SocksSettingSpecConstants.SOCKS_GSSAPIAUTHMETHOD_NEC_REFERENCE_IMPL);
    }

    public static CommaSeparatedValues getSocksGssapiAuthMethodProtectionLevelsFrom(
            final Settings settings) {
        return settings.getLastValue(
                SocksSettingSpecConstants.SOCKS_GSSAPIAUTHMETHOD_PROTECTION_LEVELS);
    }

    public static Boolean getSocksGssapiAuthMethodSuggestedConfFrom(
            final Settings settings) {
        return settings.getLastValue(
                SocksSettingSpecConstants.SOCKS_GSSAPIAUTHMETHOD_SUGGESTED_CONF);
    }

    public static Integer getSocksGssapiAuthMethodSuggestedIntegFrom(
            final Settings settings) {
        return settings.getLastValue(
                SocksSettingSpecConstants.SOCKS_GSSAPIAUTHMETHOD_SUGGESTED_INTEG);
    }

    public static CommaSeparatedValues getSocksMethodsFrom(
            final Settings settings) {
        return settings.getLastValue(SocksSettingSpecConstants.SOCKS_METHODS);
    }

    public static SocketSettings getSocksOnBindRequestInboundSocketSettingsFrom(
            final Rule rule) {
        SocketSettings socketSettings = SocketSettings.of(new ArrayList<>(
                rule.getRuleActionValues(
                        SocksRuleActionSpecConstants.SOCKS_ON_BIND_REQUEST_INBOUND_SOCKET_SETTING)));
        if (!socketSettings.toMap().isEmpty()) {
            return socketSettings;
        }
        return getSocksOnRequestExternalFacingSocketSettingsFrom(rule);
    }

    public static SocketSettings getSocksOnBindRequestInboundSocketSettingsFrom(
            final Settings settings) {
        SocketSettings socketSettings = settings.getLastValue(
                SocksSettingSpecConstants.SOCKS_ON_BIND_REQUEST_INBOUND_SOCKET_SETTINGS);
        if (!socketSettings.toMap().isEmpty()) {
            return socketSettings;
        }
        return getSocksOnRequestExternalFacingSocketSettingsFrom(settings);
    }

    public static Host getSocksOnBindRequestListenBindHostFrom(
            final Rule rule) {
        Host host = rule.getLastRuleActionValue(
                SocksRuleActionSpecConstants.SOCKS_ON_BIND_REQUEST_LISTEN_BIND_HOST);
        if (host != null) {
            return host;
        }
        return getSocksOnRequestExternalFacingBindHostFrom(rule);
    }

    public static Host getSocksOnBindRequestListenBindHostFrom(
            final Settings settings) {
        Host host = settings.getLastValue(
                SocksSettingSpecConstants.SOCKS_ON_BIND_REQUEST_LISTEN_BIND_HOST);
        if (host != null) {
            return host;
        }
        return getSocksOnRequestExternalFacingBindHostFrom(settings);
    }

    public static HostAddressTypes getSocksOnBindRequestListenBindHostAddressTypesFrom(
            final Rule rule) {
        HostAddressTypes hostAddressTypes = HostAddressTypes.of(
                rule.getRuleActionValues(
                        SocksRuleActionSpecConstants.SOCKS_ON_BIND_REQUEST_LISTEN_BIND_HOST_ADDRESS_TYPE));
        if (!hostAddressTypes.toList().isEmpty()) {
            return hostAddressTypes;
        }
        return getSocksOnRequestExternalFacingBindHostAddressTypesFrom(rule);
    }

    public static HostAddressTypes getSocksOnBindRequestListenBindHostAddressTypesFrom(
            final Settings settings) {
        HostAddressTypes hostAddressTypes = settings.getLastValue(
                SocksSettingSpecConstants.SOCKS_ON_BIND_REQUEST_LISTEN_BIND_HOST_ADDRESS_TYPES);
        if (!hostAddressTypes.toList().isEmpty()) {
            return hostAddressTypes;
        }
        return getSocksOnRequestExternalFacingBindHostAddressTypesFrom(
                settings);
    }

    public static PortRanges getSocksOnBindRequestListenBindPortRangesFrom(
            final Rule rule) {
        PortRanges portRanges = PortRanges.of(rule.getRuleActionValues(
                SocksRuleActionSpecConstants.SOCKS_ON_BIND_REQUEST_LISTEN_BIND_PORT_RANGE));
        if (!portRanges.toList().isEmpty()) {
            return portRanges;
        }
        return getSocksOnRequestExternalFacingBindTcpPortRangesFrom(rule);
    }

    public static PortRanges getSocksOnBindRequestListenBindPortRangesFrom(
            final Settings settings) {
        PortRanges portRanges = settings.getLastValue(
                SocksSettingSpecConstants.SOCKS_ON_BIND_REQUEST_LISTEN_BIND_PORT_RANGES);
        if (!portRanges.toList().isEmpty()) {
            return portRanges;
        }
        return getSocksOnRequestExternalFacingBindTcpPortRangesFrom(settings);
    }

    public static NetInterface getSocksOnBindRequestListenNetInterfaceFrom(
            final Rule rule) {
        NetInterface netInterface = rule.getLastRuleActionValue(
                SocksRuleActionSpecConstants.SOCKS_ON_BIND_REQUEST_LISTEN_NET_INTERFACE);
        if (netInterface != null) {
            return netInterface;
        }
        return getSocksOnRequestExternalFacingNetInterfaceFrom(rule);
    }

    public static NetInterface getSocksOnBindRequestListenNetInterfaceFrom(
            final Settings settings) {
        NetInterface netInterface = settings.getLastValue(
                SocksSettingSpecConstants.SOCKS_ON_BIND_REQUEST_LISTEN_NET_INTERFACE);
        if (netInterface != null) {
            return netInterface;
        }
        return getSocksOnRequestExternalFacingNetInterfaceFrom(settings);
    }

    public static SocketSettings getSocksOnBindRequestListenSocketSettingsFrom(
            final Rule rule) {
        SocketSettings socketSettings = SocketSettings.of(new ArrayList<>(
                rule.getRuleActionValues(
                        SocksRuleActionSpecConstants.SOCKS_ON_BIND_REQUEST_LISTEN_SOCKET_SETTING)));
        if (!socketSettings.toMap().isEmpty()) {
            return socketSettings;
        }
        return getSocksOnRequestExternalFacingSocketSettingsFrom(rule);
    }

    public static SocketSettings getSocksOnBindRequestListenSocketSettingsFrom(
            final Settings settings) {
        SocketSettings socketSettings = settings.getLastValue(
                SocksSettingSpecConstants.SOCKS_ON_BIND_REQUEST_LISTEN_SOCKET_SETTINGS);
        if (!socketSettings.toMap().isEmpty()) {
            return socketSettings;
        }
        return getSocksOnRequestExternalFacingSocketSettingsFrom(settings);
    }

    public static PositiveInteger getSocksOnBindRequestRelayBufferSizeFrom(
            final Rule rule) {
        PositiveInteger relayBufferSize = rule.getLastRuleActionValue(
                SocksRuleActionSpecConstants.SOCKS_ON_BIND_REQUEST_RELAY_BUFFER_SIZE);
        if (relayBufferSize != null) {
            return relayBufferSize;
        }
        return getSocksOnRequestRelayBufferSizeFrom(rule);
    }

    public static PositiveInteger getSocksOnBindRequestRelayBufferSizeFrom(
            final Settings settings) {
        PositiveInteger relayBufferSize = settings.getLastValue(
                SocksSettingSpecConstants.SOCKS_ON_BIND_REQUEST_RELAY_BUFFER_SIZE);
        if (relayBufferSize != null) {
            return relayBufferSize;
        }
        return getSocksOnRequestRelayBufferSizeFrom(settings);
    }

    public static PositiveInteger getSocksOnBindRequestRelayIdleTimeoutFrom(
            final Rule rule) {
        PositiveInteger relayIdleTimeout = rule.getLastRuleActionValue(
                SocksRuleActionSpecConstants.SOCKS_ON_BIND_REQUEST_RELAY_IDLE_TIMEOUT);
        if (relayIdleTimeout != null) {
            return relayIdleTimeout;
        }
        return getSocksOnRequestRelayIdleTimeoutFrom(rule);
    }

    public static PositiveInteger getSocksOnBindRequestRelayIdleTimeoutFrom(
            final Settings settings) {
        PositiveInteger relayIdleTimeout = settings.getLastValue(
                SocksSettingSpecConstants.SOCKS_ON_BIND_REQUEST_RELAY_IDLE_TIMEOUT);
        if (relayIdleTimeout != null) {
            return relayIdleTimeout;
        }
        return getSocksOnRequestRelayIdleTimeoutFrom(settings);
    }

    public static PositiveInteger getSocksOnBindRequestRelayInboundBandwidthLimitFrom(
            final Rule rule) {
        PositiveInteger relayInboundBandwidthLimit = rule.getLastRuleActionValue(
                SocksRuleActionSpecConstants.SOCKS_ON_BIND_REQUEST_RELAY_INBOUND_BANDWIDTH_LIMIT);
        if (relayInboundBandwidthLimit != null) {
            return relayInboundBandwidthLimit;
        }
        return getSocksOnRequestRelayInboundBandwidthLimitFrom(rule);
    }

    public static PositiveInteger getSocksOnBindRequestRelayInboundBandwidthLimitFrom(
            final Settings settings) {
        PositiveInteger relayInboundBandwidthLimit = settings.getLastValue(
                SocksSettingSpecConstants.SOCKS_ON_BIND_REQUEST_RELAY_INBOUND_BANDWIDTH_LIMIT);
        if (relayInboundBandwidthLimit != null) {
            return relayInboundBandwidthLimit;
        }
        return getSocksOnRequestRelayInboundBandwidthLimitFrom(settings);
    }

    public static PositiveInteger getSocksOnBindRequestRelayOutboundBandwidthLimitFrom(
            final Rule rule) {
        PositiveInteger relayOutboundBandwidthLimit = rule.getLastRuleActionValue(
                SocksRuleActionSpecConstants.SOCKS_ON_BIND_REQUEST_RELAY_OUTBOUND_BANDWIDTH_LIMIT);
        if (relayOutboundBandwidthLimit != null) {
            return relayOutboundBandwidthLimit;
        }
        return getSocksOnRequestRelayOutboundBandwidthLimitFrom(rule);
    }

    public static PositiveInteger getSocksOnBindRequestRelayOutboundBandwidthLimitFrom(
            final Settings settings) {
        PositiveInteger relayOutboundBandwidthLimit = settings.getLastValue(
                SocksSettingSpecConstants.SOCKS_ON_BIND_REQUEST_RELAY_OUTBOUND_BANDWIDTH_LIMIT);
        if (relayOutboundBandwidthLimit != null) {
            return relayOutboundBandwidthLimit;
        }
        return getSocksOnRequestRelayOutboundBandwidthLimitFrom(settings);
    }

    public static Host getSocksOnRequestExternalFacingBindHostFrom(
            final Rule rule) {
        Host host = rule.getLastRuleActionValue(
                SocksRuleActionSpecConstants.SOCKS_ON_REQUEST_EXTERNAL_FACING_BIND_HOST);
        if (host != null) {
            return host;
        }
        return GeneralValueDerivationHelper.getExternalFacingBindHostFrom(rule);
    }

    public static Host getSocksOnRequestExternalFacingBindHostFrom(
            final Settings settings) {
        Host host = settings.getLastValue(
                SocksSettingSpecConstants.SOCKS_ON_REQUEST_EXTERNAL_FACING_BIND_HOST);
        if (host != null) {
            return host;
        }
        return GeneralValueDerivationHelper.getExternalFacingBindHostFrom(
                settings);
    }

    public static HostAddressTypes getSocksOnRequestExternalFacingBindHostAddressTypesFrom(
            final Rule rule) {
        HostAddressTypes hostAddressTypes = HostAddressTypes.of(
                rule.getRuleActionValues(
                        SocksRuleActionSpecConstants.SOCKS_ON_REQUEST_EXTERNAL_FACING_BIND_HOST_ADDRESS_TYPE));
        if (!hostAddressTypes.toList().isEmpty()) {
            return hostAddressTypes;
        }
        return GeneralValueDerivationHelper.getExternalFacingBindHostAddressTypesFrom(
                rule);
    }

    public static HostAddressTypes getSocksOnRequestExternalFacingBindHostAddressTypesFrom(
            final Settings settings) {
        HostAddressTypes hostAddressTypes = settings.getLastValue(
                SocksSettingSpecConstants.SOCKS_ON_REQUEST_EXTERNAL_FACING_BIND_HOST_ADDRESS_TYPES);
        if (!hostAddressTypes.toList().isEmpty()) {
            return hostAddressTypes;
        }
        return GeneralValueDerivationHelper.getExternalFacingBindHostAddressTypesFrom(
                settings);
    }

    public static PortRanges getSocksOnRequestExternalFacingBindTcpPortRangesFrom(
            final Rule rule) {
        PortRanges portRanges = PortRanges.of(rule.getRuleActionValues(
                SocksRuleActionSpecConstants.SOCKS_ON_REQUEST_EXTERNAL_FACING_BIND_TCP_PORT_RANGE));
        if (!portRanges.toList().isEmpty()) {
            return portRanges;
        }
        return GeneralValueDerivationHelper.getExternalFacingTcpPortRangesFrom(
                rule);
    }

    public static PortRanges getSocksOnRequestExternalFacingBindTcpPortRangesFrom(
            final Settings settings) {
        PortRanges portRanges = settings.getLastValue(
                SocksSettingSpecConstants.SOCKS_ON_REQUEST_EXTERNAL_FACING_BIND_TCP_PORT_RANGES);
        if (!portRanges.toList().isEmpty()) {
            return portRanges;
        }
        return GeneralValueDerivationHelper.getExternalFacingTcpPortRangesFrom(
                settings);
    }

    public static PortRanges getSocksOnRequestExternalFacingBindUdpPortRangesFrom(
            final Rule rule) {
        PortRanges portRanges = PortRanges.of(rule.getRuleActionValues(
                SocksRuleActionSpecConstants.SOCKS_ON_REQUEST_EXTERNAL_FACING_BIND_UDP_PORT_RANGE));
        if (!portRanges.toList().isEmpty()) {
            return portRanges;
        }
        return GeneralValueDerivationHelper.getExternalFacingUdpPortRangesFrom(
                rule);
    }

    public static PortRanges getSocksOnRequestExternalFacingBindUdpPortRangesFrom(
            final Settings settings) {
        PortRanges portRanges = settings.getLastValue(
                SocksSettingSpecConstants.SOCKS_ON_REQUEST_EXTERNAL_FACING_BIND_UDP_PORT_RANGES);
        if (!portRanges.toList().isEmpty()) {
            return portRanges;
        }
        return GeneralValueDerivationHelper.getExternalFacingUdpPortRangesFrom(
                settings);
    }

    public static NetInterface getSocksOnRequestExternalFacingNetInterfaceFrom(
            final Rule rule) {
        NetInterface netInterface = rule.getLastRuleActionValue(
                SocksRuleActionSpecConstants.SOCKS_ON_REQUEST_EXTERNAL_FACING_NET_INTERFACE);
        if (netInterface != null) {
            return netInterface;
        }
        return GeneralValueDerivationHelper.getExternalFacingNetInterfaceFrom(
                rule);
    }

    public static NetInterface getSocksOnRequestExternalFacingNetInterfaceFrom(
            final Settings settings) {
        NetInterface netInterface = settings.getLastValue(
                SocksSettingSpecConstants.SOCKS_ON_REQUEST_EXTERNAL_FACING_NET_INTERFACE);
        if (netInterface != null) {
            return netInterface;
        }
        return GeneralValueDerivationHelper.getExternalFacingNetInterfaceFrom(
                settings);
    }

    public static SocketSettings getSocksOnRequestExternalFacingSocketSettingsFrom(
            final Rule rule) {
        SocketSettings socketSettings = SocketSettings.of(new ArrayList<>(
                rule.getRuleActionValues(
                        SocksRuleActionSpecConstants.SOCKS_ON_REQUEST_EXTERNAL_FACING_SOCKET_SETTING)));
        if (!socketSettings.toMap().isEmpty()) {
            return socketSettings;
        }
        return GeneralValueDerivationHelper.getExternalFacingSocketSettingsFrom(
                rule);
    }

    public static SocketSettings getSocksOnRequestExternalFacingSocketSettingsFrom(
            final Settings settings) {
        SocketSettings socketSettings = settings.getLastValue(
                SocksSettingSpecConstants.SOCKS_ON_REQUEST_EXTERNAL_FACING_SOCKET_SETTINGS);
        if (!socketSettings.toMap().isEmpty()) {
            return socketSettings;
        }
        return GeneralValueDerivationHelper.getExternalFacingSocketSettingsFrom(
                settings);
    }

    public static Host getSocksOnRequestInternalFacingBindHostFrom(
            final Rule rule) {
        Host host = rule.getLastRuleActionValue(
                SocksRuleActionSpecConstants.SOCKS_ON_REQUEST_INTERNAL_FACING_BIND_HOST);
        if (host != null) {
            return host;
        }
        return GeneralValueDerivationHelper.getInternalFacingBindHostFrom(rule);
    }

    public static Host getSocksOnRequestInternalFacingBindHostFrom(
            final Settings settings) {
        Host host = settings.getLastValue(
                SocksSettingSpecConstants.SOCKS_ON_REQUEST_INTERNAL_FACING_BIND_HOST);
        if (host != null) {
            return host;
        }
        return GeneralValueDerivationHelper.getInternalFacingBindHostFrom(
                settings);
    }

    public static HostAddressTypes getSocksOnRequestInternalFacingBindHostAddressTypesFrom(
            final Rule rule) {
        HostAddressTypes hostAddressTypes = HostAddressTypes.of(
                rule.getRuleActionValues(
                        SocksRuleActionSpecConstants.SOCKS_ON_REQUEST_INTERNAL_FACING_BIND_HOST_ADDRESS_TYPE));
        if (!hostAddressTypes.toList().isEmpty()) {
            return hostAddressTypes;
        }
        return GeneralValueDerivationHelper.getInternalFacingBindHostAddressTypesFrom(
                rule);
    }

    public static HostAddressTypes getSocksOnRequestInternalFacingBindHostAddressTypesFrom(
            final Settings settings) {
        HostAddressTypes hostAddressTypes = settings.getLastValue(
                SocksSettingSpecConstants.SOCKS_ON_REQUEST_INTERNAL_FACING_BIND_HOST_ADDRESS_TYPES);
        if (!hostAddressTypes.toList().isEmpty()) {
            return hostAddressTypes;
        }
        return GeneralValueDerivationHelper.getInternalFacingBindHostAddressTypesFrom(
                settings);
    }

    public static PortRanges getSocksOnRequestInternalFacingBindUdpPortRangesFrom(
            final Rule rule) {
        PortRanges portRanges = PortRanges.of(rule.getRuleActionValues(
                SocksRuleActionSpecConstants.SOCKS_ON_REQUEST_INTERNAL_FACING_BIND_UDP_PORT_RANGE));
        if (!portRanges.toList().isEmpty()) {
            return portRanges;
        }
        return GeneralValueDerivationHelper.getInternalFacingUdpPortRangesFrom(
                rule);
    }

    public static PortRanges getSocksOnRequestInternalFacingBindUdpPortRangesFrom(
            final Settings settings) {
        PortRanges portRanges = settings.getLastValue(
                SocksSettingSpecConstants.SOCKS_ON_REQUEST_INTERNAL_FACING_BIND_UDP_PORT_RANGES);
        if (!portRanges.toList().isEmpty()) {
            return portRanges;
        }
        return GeneralValueDerivationHelper.getInternalFacingUdpPortRangesFrom(
                settings);
    }

    public static NetInterface getSocksOnRequestInternalFacingNetInterfaceFrom(
            final Rule rule) {
        NetInterface netInterface = rule.getLastRuleActionValue(
                SocksRuleActionSpecConstants.SOCKS_ON_REQUEST_INTERNAL_FACING_NET_INTERFACE);
        if (netInterface != null) {
            return netInterface;
        }
        return GeneralValueDerivationHelper.getInternalFacingNetInterfaceFrom(
                rule);
    }

    public static NetInterface getSocksOnRequestInternalFacingNetInterfaceFrom(
            final Settings settings) {
        NetInterface netInterface = settings.getLastValue(
                SocksSettingSpecConstants.SOCKS_ON_REQUEST_INTERNAL_FACING_NET_INTERFACE);
        if (netInterface != null) {
            return netInterface;
        }
        return GeneralValueDerivationHelper.getInternalFacingNetInterfaceFrom(
                settings);
    }

    public static SocketSettings getSocksOnRequestInternalFacingSocketSettingsFrom(
            final Rule rule) {
        SocketSettings socketSettings = SocketSettings.of(new ArrayList<>(
                rule.getRuleActionValues(
                        SocksRuleActionSpecConstants.SOCKS_ON_REQUEST_INTERNAL_FACING_SOCKET_SETTING)));
        if (!socketSettings.toMap().isEmpty()) {
            return socketSettings;
        }
        return GeneralValueDerivationHelper.getInternalFacingSocketSettingsFrom(
                rule);
    }

    public static SocketSettings getSocksOnRequestInternalFacingSocketSettingsFrom(
            final Settings settings) {
        SocketSettings socketSettings = settings.getLastValue(
                SocksSettingSpecConstants.SOCKS_ON_REQUEST_INTERNAL_FACING_SOCKET_SETTINGS);
        if (!socketSettings.toMap().isEmpty()) {
            return socketSettings;
        }
        return GeneralValueDerivationHelper.getInternalFacingSocketSettingsFrom(
                settings);
    }

    public static PositiveInteger getSocksOnRequestRelayBufferSizeFrom(
            final Rule rule) {
        return rule.getLastRuleActionValue(
                SocksRuleActionSpecConstants.SOCKS_ON_REQUEST_RELAY_BUFFER_SIZE);
    }

    public static PositiveInteger getSocksOnRequestRelayBufferSizeFrom(
            final Settings settings) {
        return settings.getLastValue(
                SocksSettingSpecConstants.SOCKS_ON_REQUEST_RELAY_BUFFER_SIZE);
    }

    public static PositiveInteger getSocksOnRequestRelayIdleTimeoutFrom(
            final Rule rule) {
        return rule.getLastRuleActionValue(
                SocksRuleActionSpecConstants.SOCKS_ON_REQUEST_RELAY_IDLE_TIMEOUT);
    }

    public static PositiveInteger getSocksOnRequestRelayIdleTimeoutFrom(
            final Settings settings) {
        return settings.getLastValue(
                SocksSettingSpecConstants.SOCKS_ON_REQUEST_RELAY_IDLE_TIMEOUT);
    }

    public static PositiveInteger getSocksOnRequestRelayInboundBandwidthLimitFrom(
            final Rule rule) {
        return rule.getLastRuleActionValue(
                SocksRuleActionSpecConstants.SOCKS_ON_REQUEST_RELAY_INBOUND_BANDWIDTH_LIMIT);
    }

    public static PositiveInteger getSocksOnRequestRelayInboundBandwidthLimitFrom(
            final Settings settings) {
        return settings.getLastValue(
                SocksSettingSpecConstants.SOCKS_ON_REQUEST_RELAY_INBOUND_BANDWIDTH_LIMIT);
    }

    public static PositiveInteger getSocksOnRequestRelayOutboundBandwidthLimitFrom(
            final Rule rule) {
        return rule.getLastRuleActionValue(
                SocksRuleActionSpecConstants.SOCKS_ON_REQUEST_RELAY_OUTBOUND_BANDWIDTH_LIMIT);
    }

    public static PositiveInteger getSocksOnRequestRelayOutboundBandwidthLimitFrom(
            final Settings settings) {
        return settings.getLastValue(
                SocksSettingSpecConstants.SOCKS_ON_REQUEST_RELAY_OUTBOUND_BANDWIDTH_LIMIT);
    }

    public static Boolean getSocksOnConnectRequestPrepareTargetFacingSocketFrom(
            final Rule rule) {
        return rule.getLastRuleActionValue(
                SocksRuleActionSpecConstants.SOCKS_ON_CONNECT_REQUEST_PREPARE_TARGET_FACING_SOCKET);
    }
    
    public static Boolean getSocksOnConnectRequestPrepareTargetFacingSocketFrom(
            final Settings settings) {
        return settings.getLastValue(
                SocksSettingSpecConstants.SOCKS_ON_CONNECT_REQUEST_PREPARE_TARGET_FACING_SOCKET);
    }
    
    public static PositiveInteger getSocksOnConnectRequestRelayBufferSizeFrom(
            final Rule rule) {
        PositiveInteger relayBufferSize = rule.getLastRuleActionValue(
                SocksRuleActionSpecConstants.SOCKS_ON_CONNECT_REQUEST_RELAY_BUFFER_SIZE);
        if (relayBufferSize != null) {
            return relayBufferSize;
        }
        return getSocksOnRequestRelayBufferSizeFrom(rule);
    }

    public static PositiveInteger getSocksOnConnectRequestRelayBufferSizeFrom(
            final Settings settings) {
        PositiveInteger relayBufferSize = settings.getLastValue(
                SocksSettingSpecConstants.SOCKS_ON_CONNECT_REQUEST_RELAY_BUFFER_SIZE);
        if (relayBufferSize != null) {
            return relayBufferSize;
        }
        return getSocksOnRequestRelayBufferSizeFrom(settings);
    }

    public static PositiveInteger getSocksOnConnectRequestRelayIdleTimeoutFrom(
            final Rule rule) {
        PositiveInteger relayIdleTimeout = rule.getLastRuleActionValue(
                SocksRuleActionSpecConstants.SOCKS_ON_CONNECT_REQUEST_RELAY_IDLE_TIMEOUT);
        if (relayIdleTimeout != null) {
            return relayIdleTimeout;
        }
        return getSocksOnRequestRelayIdleTimeoutFrom(rule);
    }

    public static PositiveInteger getSocksOnConnectRequestRelayIdleTimeoutFrom(
            final Settings settings) {
        PositiveInteger relayIdleTimeout = settings.getLastValue(
                SocksSettingSpecConstants.SOCKS_ON_CONNECT_REQUEST_RELAY_IDLE_TIMEOUT);
        if (relayIdleTimeout != null) {
            return relayIdleTimeout;
        }
        return getSocksOnRequestRelayIdleTimeoutFrom(settings);
    }

    public static PositiveInteger getSocksOnConnectRequestRelayInboundBandwidthLimitFrom(
            final Rule rule) {
        PositiveInteger relayInboundBandwidthLimit = rule.getLastRuleActionValue(
                SocksRuleActionSpecConstants.SOCKS_ON_CONNECT_REQUEST_RELAY_INBOUND_BANDWIDTH_LIMIT);
        if (relayInboundBandwidthLimit != null) {
            return relayInboundBandwidthLimit;
        }
        return getSocksOnRequestRelayInboundBandwidthLimitFrom(rule);
    }

    public static PositiveInteger getSocksOnConnectRequestRelayInboundBandwidthLimitFrom(
            final Settings settings) {
        PositiveInteger relayInboundBandwidthLimit = settings.getLastValue(
                SocksSettingSpecConstants.SOCKS_ON_CONNECT_REQUEST_RELAY_INBOUND_BANDWIDTH_LIMIT);
        if (relayInboundBandwidthLimit != null) {
            return relayInboundBandwidthLimit;
        }
        return getSocksOnRequestRelayInboundBandwidthLimitFrom(settings);
    }

    public static PositiveInteger getSocksOnConnectRequestRelayOutboundBandwidthLimitFrom(
            final Rule rule) {
        PositiveInteger relayOutboundBandwidthLimit = rule.getLastRuleActionValue(
                SocksRuleActionSpecConstants.SOCKS_ON_CONNECT_REQUEST_RELAY_OUTBOUND_BANDWIDTH_LIMIT);
        if (relayOutboundBandwidthLimit != null) {
            return relayOutboundBandwidthLimit;
        }
        return getSocksOnRequestRelayOutboundBandwidthLimitFrom(rule);
    }

    public static PositiveInteger getSocksOnConnectRequestRelayOutboundBandwidthLimitFrom(
            final Settings settings) {
        PositiveInteger relayOutboundBandwidthLimit = settings.getLastValue(
                SocksSettingSpecConstants.SOCKS_ON_CONNECT_REQUEST_RELAY_OUTBOUND_BANDWIDTH_LIMIT);
        if (relayOutboundBandwidthLimit != null) {
            return relayOutboundBandwidthLimit;
        }
        return getSocksOnRequestRelayOutboundBandwidthLimitFrom(settings);
    }

    public static Host getSocksOnConnectRequestTargetFacingBindHostFrom(
            final Rule rule) {
        Host host = rule.getLastRuleActionValue(
                SocksRuleActionSpecConstants.SOCKS_ON_CONNECT_REQUEST_TARGET_FACING_BIND_HOST);
        if (host != null) {
            return host;
        }
        return getSocksOnRequestExternalFacingBindHostFrom(rule);
    }

    public static Host getSocksOnConnectRequestTargetFacingBindHostFrom(
            final Settings settings) {
        Host host = settings.getLastValue(
                SocksSettingSpecConstants.SOCKS_ON_CONNECT_REQUEST_TARGET_FACING_BIND_HOST);
        if (host != null) {
            return host;
        }
        return getSocksOnRequestExternalFacingBindHostFrom(settings);
    }

    public static HostAddressTypes getSocksOnConnectRequestTargetFacingBindHostAddressTypesFrom(
            final Rule rule) {
        HostAddressTypes hostAddressTypes = HostAddressTypes.of(
                rule.getRuleActionValues(
                        SocksRuleActionSpecConstants.SOCKS_ON_CONNECT_REQUEST_TARGET_FACING_BIND_HOST_ADDRESS_TYPE));
        if (!hostAddressTypes.toList().isEmpty()) {
            return hostAddressTypes;
        }
        return getSocksOnRequestExternalFacingBindHostAddressTypesFrom(rule);
    }

    public static HostAddressTypes getSocksOnConnectRequestTargetFacingBindHostAddressTypesFrom(
            final Settings settings) {
        HostAddressTypes hostAddressTypes = settings.getLastValue(
                SocksSettingSpecConstants.SOCKS_ON_CONNECT_REQUEST_TARGET_FACING_BIND_HOST_ADDRESS_TYPES);
        if (!hostAddressTypes.toList().isEmpty()) {
            return hostAddressTypes;
        }
        return getSocksOnRequestExternalFacingBindHostAddressTypesFrom(
                settings);
    }

    public static PortRanges getSocksOnConnectRequestTargetFacingBindPortRangesFrom(
            final Rule rule) {
        PortRanges portRanges = PortRanges.of(rule.getRuleActionValues(
                SocksRuleActionSpecConstants.SOCKS_ON_CONNECT_REQUEST_TARGET_FACING_BIND_PORT_RANGE));
        if (!portRanges.toList().isEmpty()) {
            return portRanges;
        }
        return getSocksOnRequestExternalFacingBindTcpPortRangesFrom(rule);
    }

    public static PortRanges getSocksOnConnectRequestTargetFacingBindPortRangesFrom(
            final Settings settings) {
        PortRanges portRanges = settings.getLastValue(
                SocksSettingSpecConstants.SOCKS_ON_CONNECT_REQUEST_TARGET_FACING_BIND_PORT_RANGES);
        if (!portRanges.toList().isEmpty()) {
            return portRanges;
        }
        return getSocksOnRequestExternalFacingBindTcpPortRangesFrom(settings);
    }

    public static PositiveInteger getSocksOnConnectRequestTargetFacingConnectTimeoutFrom(
            final Rule rule) {
        return rule.getLastRuleActionValue(
                SocksRuleActionSpecConstants.SOCKS_ON_CONNECT_REQUEST_TARGET_FACING_CONNECT_TIMEOUT);
    }
    
    public static PositiveInteger getSocksOnConnectRequestTargetFacingConnectTimeoutFrom(
            final Settings settings) {
        return settings.getLastValue(
                SocksSettingSpecConstants.SOCKS_ON_CONNECT_REQUEST_TARGET_FACING_CONNECT_TIMEOUT);
    }
    
    public static NetInterface getSocksOnConnectRequestTargetFacingNetInterfaceFrom(
            final Rule rule) {
        NetInterface netInterface = rule.getLastRuleActionValue(
                SocksRuleActionSpecConstants.SOCKS_ON_CONNECT_REQUEST_TARGET_FACING_NET_INTERFACE);
        if (netInterface != null) {
            return netInterface;
        }
        return getSocksOnRequestExternalFacingNetInterfaceFrom(rule);
    }

    public static NetInterface getSocksOnConnectRequestTargetFacingNetInterfaceFrom(
            final Settings settings) {
        NetInterface netInterface = settings.getLastValue(
                SocksSettingSpecConstants.SOCKS_ON_CONNECT_REQUEST_TARGET_FACING_NET_INTERFACE);
        if (netInterface != null) {
            return netInterface;
        }
        return getSocksOnRequestExternalFacingNetInterfaceFrom(settings);
    }

    public static SocketSettings getSocksOnConnectRequestTargetFacingSocketSettingsFrom(
            final Rule rule) {
        SocketSettings socketSettings = SocketSettings.of(new ArrayList<>(
                rule.getRuleActionValues(
                        SocksRuleActionSpecConstants.SOCKS_ON_CONNECT_REQUEST_TARGET_FACING_SOCKET_SETTING)));
        if (!socketSettings.toMap().isEmpty()) {
            return socketSettings;
        }
        return getSocksOnRequestExternalFacingSocketSettingsFrom(rule);
    }

    public static SocketSettings getSocksOnConnectRequestTargetFacingSocketSettingsFrom(
            final Settings settings) {
        SocketSettings socketSettings = settings.getLastValue(
                SocksSettingSpecConstants.SOCKS_ON_CONNECT_REQUEST_TARGET_FACING_SOCKET_SETTINGS);
        if (!socketSettings.toMap().isEmpty()) {
            return socketSettings;
        }
        return getSocksOnRequestExternalFacingSocketSettingsFrom(settings);
    }

    public static Host getSocksOnUdpAssociateRequestClientFacingBindHostFrom(
            final Rule rule) {
        Host host = rule.getLastRuleActionValue(
                SocksRuleActionSpecConstants.SOCKS_ON_UDP_ASSOCIATE_REQUEST_CLIENT_FACING_BIND_HOST);
        if (host != null) {
            return host;
        }
        return getSocksOnRequestInternalFacingBindHostFrom(rule);
    }

    public static Host getSocksOnUdpAssociateRequestClientFacingBindHostFrom(
            final Settings settings) {
        Host host = settings.getLastValue(
                SocksSettingSpecConstants.SOCKS_ON_UDP_ASSOCIATE_REQUEST_CLIENT_FACING_BIND_HOST);
        if (host != null) {
            return host;
        }
        return getSocksOnRequestInternalFacingBindHostFrom(settings);
    }

    public static HostAddressTypes getSocksOnUdpAssociateRequestClientFacingBindHostAddressTypesFrom(
            final Rule rule) {
        HostAddressTypes hostAddressTypes = HostAddressTypes.of(
                rule.getRuleActionValues(
                        SocksRuleActionSpecConstants.SOCKS_ON_UDP_ASSOCIATE_REQUEST_CLIENT_FACING_BIND_HOST_ADDRESS_TYPE));
        if (!hostAddressTypes.toList().isEmpty()) {
            return hostAddressTypes;
        }
        return getSocksOnRequestInternalFacingBindHostAddressTypesFrom(rule);
    }

    public static HostAddressTypes getSocksOnUdpAssociateRequestClientFacingBindHostAddressTypesFrom(
            final Settings settings) {
        HostAddressTypes hostAddressTypes = settings.getLastValue(
                SocksSettingSpecConstants.SOCKS_ON_UDP_ASSOCIATE_REQUEST_CLIENT_FACING_BIND_HOST_ADDRESS_TYPES);
        if (!hostAddressTypes.toList().isEmpty()) {
            return hostAddressTypes;
        }
        return getSocksOnRequestInternalFacingBindHostAddressTypesFrom(
                settings);
    }

    public static PortRanges getSocksOnUdpAssociateRequestClientFacingBindPortRangesFrom(
            final Rule rule) {
        PortRanges portRanges = PortRanges.of(rule.getRuleActionValues(
                SocksRuleActionSpecConstants.SOCKS_ON_UDP_ASSOCIATE_REQUEST_CLIENT_FACING_BIND_PORT_RANGE));
        if (!portRanges.toList().isEmpty()) {
            return portRanges;
        }
        return getSocksOnRequestInternalFacingBindUdpPortRangesFrom(rule);
    }

    public static PortRanges getSocksOnUdpAssociateRequestClientFacingBindPortRangesFrom(
            final Settings settings) {
        PortRanges portRanges = settings.getLastValue(
                SocksSettingSpecConstants.SOCKS_ON_UDP_ASSOCIATE_REQUEST_CLIENT_FACING_BIND_PORT_RANGES);
        if (!portRanges.toList().isEmpty()) {
            return portRanges;
        }
        return getSocksOnRequestInternalFacingBindUdpPortRangesFrom(settings);
    }

    public static NetInterface getSocksOnUdpAssociateRequestClientFacingNetInterfaceFrom(
            final Rule rule) {
        NetInterface netInterface = rule.getLastRuleActionValue(
                SocksRuleActionSpecConstants.SOCKS_ON_UDP_ASSOCIATE_REQUEST_CLIENT_FACING_NET_INTERFACE);
        if (netInterface != null) {
            return netInterface;
        }
        return getSocksOnRequestInternalFacingNetInterfaceFrom(rule);
    }

    public static NetInterface getSocksOnUdpAssociateRequestClientFacingNetInterfaceFrom(
            final Settings settings) {
        NetInterface netInterface = settings.getLastValue(
                SocksSettingSpecConstants.SOCKS_ON_UDP_ASSOCIATE_REQUEST_CLIENT_FACING_NET_INTERFACE);
        if (netInterface != null) {
            return netInterface;
        }
        return getSocksOnRequestInternalFacingNetInterfaceFrom(settings);
    }

    public static SocketSettings getSocksOnUdpAssociateRequestClientFacingSocketSettingsFrom(
            final Rule rule) {
        SocketSettings socketSettings = SocketSettings.of(new ArrayList<>(
                rule.getRuleActionValues(
                        SocksRuleActionSpecConstants.SOCKS_ON_UDP_ASSOCIATE_REQUEST_CLIENT_FACING_SOCKET_SETTING)));
        if (!socketSettings.toMap().isEmpty()) {
            return socketSettings;
        }
        return getSocksOnRequestInternalFacingSocketSettingsFrom(rule);
    }

    public static SocketSettings getSocksOnUdpAssociateRequestClientFacingSocketSettingsFrom(
            final Settings settings) {
        SocketSettings socketSettings = settings.getLastValue(
                SocksSettingSpecConstants.SOCKS_ON_UDP_ASSOCIATE_REQUEST_CLIENT_FACING_SOCKET_SETTINGS);
        if (!socketSettings.toMap().isEmpty()) {
            return socketSettings;
        }
        return getSocksOnRequestInternalFacingSocketSettingsFrom(settings);
    }

    public static Host getSocksOnUdpAssociateRequestPeerFacingBindHostFrom(
            final Rule rule) {
        Host host = rule.getLastRuleActionValue(
                SocksRuleActionSpecConstants.SOCKS_ON_UDP_ASSOCIATE_REQUEST_PEER_FACING_BIND_HOST);
        if (host != null) {
            return host;
        }
        return getSocksOnRequestExternalFacingBindHostFrom(rule);
    }

    public static Host getSocksOnUdpAssociateRequestPeerFacingBindHostFrom(
            final Settings settings) {
        Host host = settings.getLastValue(
                SocksSettingSpecConstants.SOCKS_ON_UDP_ASSOCIATE_REQUEST_PEER_FACING_BIND_HOST);
        if (host != null) {
            return host;
        }
        return getSocksOnRequestExternalFacingBindHostFrom(settings);
    }

    public static HostAddressTypes getSocksOnUdpAssociateRequestPeerFacingBindHostAddressTypesFrom(
            final Rule rule) {
        HostAddressTypes hostAddressTypes = HostAddressTypes.of(
                rule.getRuleActionValues(
                        SocksRuleActionSpecConstants.SOCKS_ON_UDP_ASSOCIATE_REQUEST_PEER_FACING_BIND_HOST_ADDRESS_TYPE));
        if (!hostAddressTypes.toList().isEmpty()) {
            return hostAddressTypes;
        }
        return getSocksOnRequestExternalFacingBindHostAddressTypesFrom(rule);
    }

    public static HostAddressTypes getSocksOnUdpAssociateRequestPeerFacingBindHostAddressTypesFrom(
            final Settings settings) {
        HostAddressTypes hostAddressTypes = settings.getLastValue(
                SocksSettingSpecConstants.SOCKS_ON_UDP_ASSOCIATE_REQUEST_PEER_FACING_BIND_HOST_ADDRESS_TYPES);
        if (!hostAddressTypes.toList().isEmpty()) {
            return hostAddressTypes;
        }
        return getSocksOnRequestExternalFacingBindHostAddressTypesFrom(
                settings);
    }

    public static PortRanges getSocksOnUdpAssociateRequestPeerFacingBindPortRangesFrom(
            final Rule rule) {
        PortRanges portRanges = PortRanges.of(rule.getRuleActionValues(
                SocksRuleActionSpecConstants.SOCKS_ON_UDP_ASSOCIATE_REQUEST_PEER_FACING_BIND_PORT_RANGE));
        if (!portRanges.toList().isEmpty()) {
            return portRanges;
        }
        return getSocksOnRequestExternalFacingBindUdpPortRangesFrom(rule);
    }

    public static PortRanges getSocksOnUdpAssociateRequestPeerFacingBindPortRangesFrom(
            final Settings settings) {
        PortRanges portRanges = settings.getLastValue(
                SocksSettingSpecConstants.SOCKS_ON_UDP_ASSOCIATE_REQUEST_PEER_FACING_BIND_PORT_RANGES);
        if (!portRanges.toList().isEmpty()) {
            return portRanges;
        }
        return getSocksOnRequestExternalFacingBindUdpPortRangesFrom(settings);
    }

    public static NetInterface getSocksOnUdpAssociateRequestPeerFacingNetInterfaceFrom(
            final Rule rule) {
        NetInterface netInterface = rule.getLastRuleActionValue(
                SocksRuleActionSpecConstants.SOCKS_ON_UDP_ASSOCIATE_REQUEST_PEER_FACING_NET_INTERFACE);
        if (netInterface != null) {
            return netInterface;
        }
        return getSocksOnRequestExternalFacingNetInterfaceFrom(rule);
    }

    public static NetInterface getSocksOnUdpAssociateRequestPeerFacingNetInterfaceFrom(
            final Settings settings) {
        NetInterface netInterface = settings.getLastValue(
                SocksSettingSpecConstants.SOCKS_ON_UDP_ASSOCIATE_REQUEST_PEER_FACING_NET_INTERFACE);
        if (netInterface != null) {
            return netInterface;
        }
        return getSocksOnRequestExternalFacingNetInterfaceFrom(settings);
    }

    public static SocketSettings getSocksOnUdpAssociateRequestPeerFacingSocketSettingsFrom(
            final Rule rule) {
        SocketSettings socketSettings = SocketSettings.of(new ArrayList<>(
                rule.getRuleActionValues(
                        SocksRuleActionSpecConstants.SOCKS_ON_UDP_ASSOCIATE_REQUEST_PEER_FACING_SOCKET_SETTING)));
        if (!socketSettings.toMap().isEmpty()) {
            return socketSettings;
        }
        return getSocksOnRequestExternalFacingSocketSettingsFrom(rule);
    }

    public static SocketSettings getSocksOnUdpAssociateRequestPeerFacingSocketSettingsFrom(
            final Settings settings) {
        SocketSettings socketSettings = settings.getLastValue(
                SocksSettingSpecConstants.SOCKS_ON_UDP_ASSOCIATE_REQUEST_PEER_FACING_SOCKET_SETTINGS);
        if (!socketSettings.toMap().isEmpty()) {
            return socketSettings;
        }
        return getSocksOnRequestExternalFacingSocketSettingsFrom(settings);
    }

    public static PositiveInteger getSocksOnUdpAssociateRequestRelayBufferSizeFrom(
            final Rule rule) {
        PositiveInteger relayBufferSize = rule.getLastRuleActionValue(
                SocksRuleActionSpecConstants.SOCKS_ON_UDP_ASSOCIATE_REQUEST_RELAY_BUFFER_SIZE);
        if (relayBufferSize != null) {
            return relayBufferSize;
        }
        return getSocksOnRequestRelayBufferSizeFrom(rule);
    }

    public static PositiveInteger getSocksOnUdpAssociateRequestRelayBufferSizeFrom(
            final Settings settings) {
        PositiveInteger relayBufferSize = settings.getLastValue(
                SocksSettingSpecConstants.SOCKS_ON_UDP_ASSOCIATE_REQUEST_RELAY_BUFFER_SIZE);
        if (relayBufferSize != null) {
            return relayBufferSize;
        }
        return getSocksOnRequestRelayBufferSizeFrom(settings);
    }

    public static PositiveInteger getSocksOnUdpAssociateRequestRelayIdleTimeoutFrom(
            final Rule rule) {
        PositiveInteger relayIdleTimeout = rule.getLastRuleActionValue(
                SocksRuleActionSpecConstants.SOCKS_ON_UDP_ASSOCIATE_REQUEST_RELAY_IDLE_TIMEOUT);
        if (relayIdleTimeout != null) {
            return relayIdleTimeout;
        }
        return getSocksOnRequestRelayIdleTimeoutFrom(rule);
    }

    public static PositiveInteger getSocksOnUdpAssociateRequestRelayIdleTimeoutFrom(
            final Settings settings) {
        PositiveInteger relayIdleTimeout = settings.getLastValue(
                SocksSettingSpecConstants.SOCKS_ON_UDP_ASSOCIATE_REQUEST_RELAY_IDLE_TIMEOUT);
        if (relayIdleTimeout != null) {
            return relayIdleTimeout;
        }
        return getSocksOnRequestRelayIdleTimeoutFrom(settings);
    }

    public static PositiveInteger getSocksOnUdpAssociateRequestRelayInboundBandwidthLimitFrom(
            final Rule rule) {
        PositiveInteger relayInboundBandwidthLimit = rule.getLastRuleActionValue(
                SocksRuleActionSpecConstants.SOCKS_ON_UDP_ASSOCIATE_REQUEST_RELAY_INBOUND_BANDWIDTH_LIMIT);
        if (relayInboundBandwidthLimit != null) {
            return relayInboundBandwidthLimit;
        }
        return getSocksOnRequestRelayInboundBandwidthLimitFrom(rule);
    }

    public static PositiveInteger getSocksOnUdpAssociateRequestRelayInboundBandwidthLimitFrom(
            final Settings settings) {
        PositiveInteger relayInboundBandwidthLimit = settings.getLastValue(
                SocksSettingSpecConstants.SOCKS_ON_UDP_ASSOCIATE_REQUEST_RELAY_INBOUND_BANDWIDTH_LIMIT);
        if (relayInboundBandwidthLimit != null) {
            return relayInboundBandwidthLimit;
        }
        return getSocksOnRequestRelayInboundBandwidthLimitFrom(settings);
    }

    public static PositiveInteger getSocksOnUdpAssociateRequestRelayOutboundBandwidthLimitFrom(
            final Rule rule) {
        PositiveInteger relayOutboundBandwidthLimit = rule.getLastRuleActionValue(
                SocksRuleActionSpecConstants.SOCKS_ON_UDP_ASSOCIATE_REQUEST_RELAY_OUTBOUND_BANDWIDTH_LIMIT);
        if (relayOutboundBandwidthLimit != null) {
            return relayOutboundBandwidthLimit;
        }
        return getSocksOnRequestRelayOutboundBandwidthLimitFrom(rule);
    }

    public static PositiveInteger getSocksOnUdpAssociateRequestRelayOutboundBandwidthLimitFrom(
            final Settings settings) {
        PositiveInteger relayOutboundBandwidthLimit = settings.getLastValue(
                SocksSettingSpecConstants.SOCKS_ON_UDP_ASSOCIATE_REQUEST_RELAY_OUTBOUND_BANDWIDTH_LIMIT);
        if (relayOutboundBandwidthLimit != null) {
            return relayOutboundBandwidthLimit;
        }
        return getSocksOnRequestRelayOutboundBandwidthLimitFrom(settings);
    }

    public static String getSocksRequestDesiredDestinationAddressRedirectFrom(
            final Rule rule) {
        return rule.getLastRuleActionValue(
                SocksRuleActionSpecConstants.SOCKS_REQUEST_DESIRED_DESTINATION_ADDRESS_REDIRECT);
    }

    public static Port getSocksRequestDesiredDestinationPortRedirectFrom(
            final Rule rule) {
        return rule.getLastRuleActionValue(
                SocksRuleActionSpecConstants.SOCKS_REQUEST_DESIRED_DESTINATION_PORT_REDIRECT);
    }

    public static LogAction getSocksRequestDesiredDestinationRedirectLogActionFrom(
            final Rule rule) {
        return rule.getLastRuleActionValue(
                SocksRuleActionSpecConstants.SOCKS_REQUEST_DESIRED_DESTINATION_REDIRECT_LOG_ACTION);
    }

    public static String getSocksUserpassAuthMethodUserRepositoryFrom(
            final Settings settings) {
        return settings.getLastValue(
                SocksSettingSpecConstants.SOCKS_USERPASSAUTHMETHOD_USER_REPOSITORY);
    }

}
