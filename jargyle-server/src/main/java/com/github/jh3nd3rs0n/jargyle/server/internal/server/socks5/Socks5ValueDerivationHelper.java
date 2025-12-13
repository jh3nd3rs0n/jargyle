package com.github.jh3nd3rs0n.jargyle.server.internal.server.socks5;

import com.github.jh3nd3rs0n.jargyle.common.net.*;
import com.github.jh3nd3rs0n.jargyle.common.number.PositiveInteger;
import com.github.jh3nd3rs0n.jargyle.protocolbase.socks5.Address;
import com.github.jh3nd3rs0n.jargyle.protocolbase.socks5.Methods;
import com.github.jh3nd3rs0n.jargyle.protocolbase.socks5.gssapiauthmethod.ProtectionLevels;
import com.github.jh3nd3rs0n.jargyle.server.*;
import com.github.jh3nd3rs0n.jargyle.server.internal.server.SocksValueDerivationHelper;
import com.github.jh3nd3rs0n.jargyle.server.socks5.userpassauthmethod.UserRepository;

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

    public static Boolean getSocks5GssapiAuthMethodNecReferenceImplFrom(
            final Settings settings) {
        Boolean b = settings.getLastValue(
                Socks5SettingSpecConstants.SOCKS5_GSSAPIAUTHMETHOD_NEC_REFERENCE_IMPL);
        if (b != null) {
            return b;
        }
        return SocksValueDerivationHelper.getSocksGssapiAuthMethodNecReferenceImplFrom(settings);
    }

    public static ProtectionLevels getSocks5GssapiAuthMethodProtectionLevelsFrom(
            final Settings settings) {
        ProtectionLevels protectionLevels = settings.getLastValue(
                Socks5SettingSpecConstants.SOCKS5_GSSAPIAUTHMETHOD_PROTECTION_LEVELS);
        if (protectionLevels != null) {
            return protectionLevels;
        }
        return ProtectionLevels.newInstanceFrom(
                SocksValueDerivationHelper.getSocksGssapiAuthMethodProtectionLevelsFrom(settings).toString());
    }

    public static Boolean getSocks5GssapiAuthMethodSuggestedConfFrom(
            final Settings settings) {
        Boolean b = settings.getLastValue(
                Socks5SettingSpecConstants.SOCKS5_GSSAPIAUTHMETHOD_SUGGESTED_CONF);
        if (b != null) {
            return b;
        }
        return SocksValueDerivationHelper.getSocksGssapiAuthMethodSuggestedConfFrom(settings);
    }

    public static Integer getSocks5GssapiAuthMethodSuggestedIntegFrom(
            final Settings settings) {
        Integer i = settings.getLastValue(
                Socks5SettingSpecConstants.SOCKS5_GSSAPIAUTHMETHOD_SUGGESTED_INTEG);
        if (i != null) {
            return i;
        }
        return SocksValueDerivationHelper.getSocksGssapiAuthMethodSuggestedIntegFrom(settings);
    }

    public static Methods getSocks5MethodsFrom(final Settings settings) {
        Methods methods = settings.getLastValue(
                Socks5SettingSpecConstants.SOCKS5_METHODS);
        if (!methods.toList().isEmpty()) {
            return methods;
        }
        return Methods.newInstanceFrom(
                SocksValueDerivationHelper.getSocksMethodsFrom(settings).toString());
    }
    
    private static SocketSettings getSocks5OnBindRequestInboundSocketSettingsFrom(
            final Rule rule) {
        SocketSettings socketSettings = SocketSettings.of(new ArrayList<>(
                rule.getRuleActionValues(
                        Socks5RuleActionSpecConstants.SOCKS5_ON_BIND_REQUEST_INBOUND_SOCKET_SETTING)));
        if (!socketSettings.toMap().isEmpty()) {
            return socketSettings;
        }
        return SocksValueDerivationHelper.getSocksOnBindRequestInboundSocketSettingsFrom(rule);
    }

    private static SocketSettings getSocks5OnBindRequestInboundSocketSettingsFrom(
            final Settings settings) {
        SocketSettings socketSettings = settings.getLastValue(
                Socks5SettingSpecConstants.SOCKS5_ON_BIND_REQUEST_INBOUND_SOCKET_SETTINGS);
        if (!socketSettings.toMap().isEmpty()) {
            return socketSettings;
        }
        return SocksValueDerivationHelper.getSocksOnBindRequestInboundSocketSettingsFrom(settings);
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
        return SocksValueDerivationHelper.getSocksOnBindRequestListenBindHostFrom(rule);
    }

    private static Host getSocks5OnBindRequestListenBindHostFrom(
            final Settings settings) {
        Host host = settings.getLastValue(
                Socks5SettingSpecConstants.SOCKS5_ON_BIND_REQUEST_LISTEN_BIND_HOST);
        if (host != null) {
            return host;
        }
        return SocksValueDerivationHelper.getSocksOnBindRequestListenBindHostFrom(settings);
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
        return SocksValueDerivationHelper.getSocksOnBindRequestListenBindHostAddressTypesFrom(rule);
    }

    private static HostAddressTypes getSocks5OnBindRequestListenBindHostAddressTypesFrom(
            final Settings settings) {
        HostAddressTypes hostAddressTypes = settings.getLastValue(
                Socks5SettingSpecConstants.SOCKS5_ON_BIND_REQUEST_LISTEN_BIND_HOST_ADDRESS_TYPES);
        if (!hostAddressTypes.toList().isEmpty()) {
            return hostAddressTypes;
        }
        return SocksValueDerivationHelper.getSocksOnBindRequestListenBindHostAddressTypesFrom(settings);
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
        return SocksValueDerivationHelper.getSocksOnBindRequestListenBindPortRangesFrom(rule);
    }

    private static PortRanges getSocks5OnBindRequestListenBindPortRangesFrom(
            final Settings settings) {
        PortRanges portRanges = settings.getLastValue(
                Socks5SettingSpecConstants.SOCKS5_ON_BIND_REQUEST_LISTEN_BIND_PORT_RANGES);
        if (!portRanges.toList().isEmpty()) {
            return portRanges;
        }
        return SocksValueDerivationHelper.getSocksOnBindRequestListenBindPortRangesFrom(settings);
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
        return SocksValueDerivationHelper.getSocksOnBindRequestListenNetInterfaceFrom(rule);
    }

    private static NetInterface getSocks5OnBindRequestListenNetInterfaceFrom(
            final Settings settings) {
        NetInterface netInterface = settings.getLastValue(
                Socks5SettingSpecConstants.SOCKS5_ON_BIND_REQUEST_LISTEN_NET_INTERFACE);
        if (netInterface != null) {
            return netInterface;
        }
        return SocksValueDerivationHelper.getSocksOnBindRequestListenNetInterfaceFrom(settings);
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
        return SocksValueDerivationHelper.getSocksOnBindRequestListenSocketSettingsFrom(rule);
    }

    private static SocketSettings getSocks5OnBindRequestListenSocketSettingsFrom(
            final Settings settings) {
        SocketSettings socketSettings = settings.getLastValue(
                Socks5SettingSpecConstants.SOCKS5_ON_BIND_REQUEST_LISTEN_SOCKET_SETTINGS);
        if (!socketSettings.toMap().isEmpty()) {
            return socketSettings;
        }
        return SocksValueDerivationHelper.getSocksOnBindRequestListenSocketSettingsFrom(settings);
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
        return SocksValueDerivationHelper.getSocksOnBindRequestRelayBufferSizeFrom(rule);
    }
    
    private static PositiveInteger getSocks5OnBindRequestRelayBufferSizeFrom(
            final Settings settings) {
        PositiveInteger relayBufferSize = settings.getLastValue(
                Socks5SettingSpecConstants.SOCKS5_ON_BIND_REQUEST_RELAY_BUFFER_SIZE);
        if (relayBufferSize != null) {
            return relayBufferSize;
        }
        return SocksValueDerivationHelper.getSocksOnBindRequestRelayBufferSizeFrom(settings);
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
        return SocksValueDerivationHelper.getSocksOnBindRequestRelayIdleTimeoutFrom(rule);
    }

    private static PositiveInteger getSocks5OnBindRequestRelayIdleTimeoutFrom(
            final Settings settings) {
        PositiveInteger relayIdleTimeout = settings.getLastValue(
                Socks5SettingSpecConstants.SOCKS5_ON_BIND_REQUEST_RELAY_IDLE_TIMEOUT);
        if (relayIdleTimeout != null) {
            return relayIdleTimeout;
        }
        return SocksValueDerivationHelper.getSocksOnBindRequestRelayIdleTimeoutFrom(settings);
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
        return SocksValueDerivationHelper.getSocksOnBindRequestRelayInboundBandwidthLimitFrom(rule);
    }

    private static PositiveInteger getSocks5OnBindRequestRelayInboundBandwidthLimitFrom(
            final Settings settings) {
        PositiveInteger relayInboundBandwidthLimit = settings.getLastValue(
                Socks5SettingSpecConstants.SOCKS5_ON_BIND_REQUEST_RELAY_INBOUND_BANDWIDTH_LIMIT);
        if (relayInboundBandwidthLimit != null) {
            return relayInboundBandwidthLimit;
        }
        return SocksValueDerivationHelper.getSocksOnBindRequestRelayInboundBandwidthLimitFrom(settings);
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
        return SocksValueDerivationHelper.getSocksOnBindRequestRelayOutboundBandwidthLimitFrom(rule);
    }

    private static PositiveInteger getSocks5OnBindRequestRelayOutboundBandwidthLimitFrom(
            final Settings settings) {
        PositiveInteger relayOutboundBandwidthLimit = settings.getLastValue(
                Socks5SettingSpecConstants.SOCKS5_ON_BIND_REQUEST_RELAY_OUTBOUND_BANDWIDTH_LIMIT);
        if (relayOutboundBandwidthLimit != null) {
            return relayOutboundBandwidthLimit;
        }
        return SocksValueDerivationHelper.getSocksOnBindRequestRelayOutboundBandwidthLimitFrom(settings);
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

    private static Boolean getSocks5OnConnectRequestPrepareTargetFacingSocketFrom(
            final Rule rule) {
        Boolean b = rule.getLastRuleActionValue(
                Socks5RuleActionSpecConstants.SOCKS5_ON_CONNECT_REQUEST_PREPARE_TARGET_FACING_SOCKET);
        if (b != null) {
            return b;
        }
        return SocksValueDerivationHelper.getSocksOnConnectRequestPrepareTargetFacingSocketFrom(rule);
    }

    private static Boolean getSocks5OnConnectRequestPrepareTargetFacingSocketFrom(
            final Settings settings) {
        Boolean b = settings.getLastValue(
                Socks5SettingSpecConstants.SOCKS5_ON_CONNECT_REQUEST_PREPARE_TARGET_FACING_SOCKET);
        if (b != null) {
            return b;
        }
        return SocksValueDerivationHelper.getSocksOnConnectRequestPrepareTargetFacingSocketFrom(settings);
    }

    public static Boolean getSocks5OnConnectRequestPrepareTargetFacingSocketFrom(
            final Rule rule, final Settings settings) {
        Boolean b = getSocks5OnConnectRequestPrepareTargetFacingSocketFrom(rule);
        if (b != null) {
            return b;
        }
        return getSocks5OnConnectRequestPrepareTargetFacingSocketFrom(settings);
    }

    private static PositiveInteger getSocks5OnConnectRequestRelayBufferSizeFrom(
            final Rule rule) {
        PositiveInteger relayBufferSize = rule.getLastRuleActionValue(
                Socks5RuleActionSpecConstants.SOCKS5_ON_CONNECT_REQUEST_RELAY_BUFFER_SIZE);
        if (relayBufferSize != null) {
            return relayBufferSize;
        }
        return SocksValueDerivationHelper.getSocksOnConnectRequestRelayBufferSizeFrom(rule);
    }

    private static PositiveInteger getSocks5OnConnectRequestRelayBufferSizeFrom(
            final Settings settings) {
        PositiveInteger relayBufferSize = settings.getLastValue(
                Socks5SettingSpecConstants.SOCKS5_ON_CONNECT_REQUEST_RELAY_BUFFER_SIZE);
        if (relayBufferSize != null) {
            return relayBufferSize;
        }
        return SocksValueDerivationHelper.getSocksOnConnectRequestRelayBufferSizeFrom(settings);
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
        return SocksValueDerivationHelper.getSocksOnConnectRequestRelayIdleTimeoutFrom(rule);
    }

    private static PositiveInteger getSocks5OnConnectRequestRelayIdleTimeoutFrom(
            final Settings settings) {
        PositiveInteger relayIdleTimeout = settings.getLastValue(
                Socks5SettingSpecConstants.SOCKS5_ON_CONNECT_REQUEST_RELAY_IDLE_TIMEOUT);
        if (relayIdleTimeout != null) {
            return relayIdleTimeout;
        }
        return SocksValueDerivationHelper.getSocksOnConnectRequestRelayIdleTimeoutFrom(settings);
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
        return SocksValueDerivationHelper.getSocksOnConnectRequestRelayInboundBandwidthLimitFrom(rule);
    }

    private static PositiveInteger getSocks5OnConnectRequestRelayInboundBandwidthLimitFrom(
            final Settings settings) {
        PositiveInteger relayInboundBandwidthLimit = settings.getLastValue(
                Socks5SettingSpecConstants.SOCKS5_ON_CONNECT_REQUEST_RELAY_INBOUND_BANDWIDTH_LIMIT);
        if (relayInboundBandwidthLimit != null) {
            return relayInboundBandwidthLimit;
        }
        return SocksValueDerivationHelper.getSocksOnConnectRequestRelayInboundBandwidthLimitFrom(settings);
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
        return SocksValueDerivationHelper.getSocksOnConnectRequestRelayOutboundBandwidthLimitFrom(rule);
    }

    private static PositiveInteger getSocks5OnConnectRequestRelayOutboundBandwidthLimitFrom(
            final Settings settings) {
        PositiveInteger relayOutboundBandwidthLimit = settings.getLastValue(
                Socks5SettingSpecConstants.SOCKS5_ON_CONNECT_REQUEST_RELAY_OUTBOUND_BANDWIDTH_LIMIT);
        if (relayOutboundBandwidthLimit != null) {
            return relayOutboundBandwidthLimit;
        }
        return SocksValueDerivationHelper.getSocksOnConnectRequestRelayOutboundBandwidthLimitFrom(settings);
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
        return SocksValueDerivationHelper.getSocksOnConnectRequestTargetFacingBindHostFrom(rule);
    }

    private static Host getSocks5OnConnectRequestTargetFacingBindHostFrom(
            final Settings settings) {
        Host host = settings.getLastValue(
                Socks5SettingSpecConstants.SOCKS5_ON_CONNECT_REQUEST_TARGET_FACING_BIND_HOST);
        if (host != null) {
            return host;
        }
        return SocksValueDerivationHelper.getSocksOnConnectRequestTargetFacingBindHostFrom(settings);
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
        return SocksValueDerivationHelper.getSocksOnConnectRequestTargetFacingBindHostAddressTypesFrom(rule);
    }

    private static HostAddressTypes getSocks5OnConnectRequestTargetFacingBindHostAddressTypesFrom(
            final Settings settings) {
        HostAddressTypes hostAddressTypes = settings.getLastValue(
                Socks5SettingSpecConstants.SOCKS5_ON_CONNECT_REQUEST_TARGET_FACING_BIND_HOST_ADDRESS_TYPES);
        if (!hostAddressTypes.toList().isEmpty()) {
            return hostAddressTypes;
        }
        return SocksValueDerivationHelper.getSocksOnConnectRequestTargetFacingBindHostAddressTypesFrom(settings);
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
        return SocksValueDerivationHelper.getSocksOnConnectRequestTargetFacingBindPortRangesFrom(rule);
    }

    private static PortRanges getSocks5OnConnectRequestTargetFacingBindPortRangesFrom(
            final Settings settings) {
        PortRanges portRanges = settings.getLastValue(
                Socks5SettingSpecConstants.SOCKS5_ON_CONNECT_REQUEST_TARGET_FACING_BIND_PORT_RANGES);
        if (!portRanges.toList().isEmpty()) {
            return portRanges;
        }
        return SocksValueDerivationHelper.getSocksOnConnectRequestTargetFacingBindPortRangesFrom(settings);
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

    private static PositiveInteger getSocks5OnConnectRequestTargetFacingConnectTimeoutFrom(
            final Rule rule) {
        PositiveInteger i = rule.getLastRuleActionValue(
                Socks5RuleActionSpecConstants.SOCKS5_ON_CONNECT_REQUEST_TARGET_FACING_CONNECT_TIMEOUT);
        if (i != null) {
            return i;
        }
        return SocksValueDerivationHelper.getSocksOnConnectRequestTargetFacingConnectTimeoutFrom(rule);
    }

    private static PositiveInteger getSocks5OnConnectRequestTargetFacingConnectTimeoutFrom(
            final Settings settings) {
        PositiveInteger i = settings.getLastValue(
                Socks5SettingSpecConstants.SOCKS5_ON_CONNECT_REQUEST_TARGET_FACING_CONNECT_TIMEOUT);
        if (i != null) {
            return i;
        }
        return SocksValueDerivationHelper.getSocksOnConnectRequestTargetFacingConnectTimeoutFrom(settings);
    }

    public static PositiveInteger getSocks5OnConnectRequestTargetFacingConnectTimeoutFrom(
            final Rule rule, final Settings settings) {
        PositiveInteger i = getSocks5OnConnectRequestTargetFacingConnectTimeoutFrom(rule);
        if (i != null) {
            return i;
        }
        return getSocks5OnConnectRequestTargetFacingConnectTimeoutFrom(settings);
    }

    private static NetInterface getSocks5OnConnectRequestTargetFacingNetInterfaceFrom(
            final Rule rule) {
        NetInterface netInterface = rule.getLastRuleActionValue(
                Socks5RuleActionSpecConstants.SOCKS5_ON_CONNECT_REQUEST_TARGET_FACING_NET_INTERFACE);
        if (netInterface != null) {
            return netInterface;
        }
        return SocksValueDerivationHelper.getSocksOnConnectRequestTargetFacingNetInterfaceFrom(rule);
    }

    private static NetInterface getSocks5OnConnectRequestTargetFacingNetInterfaceFrom(
            final Settings settings) {
        NetInterface netInterface = settings.getLastValue(
                Socks5SettingSpecConstants.SOCKS5_ON_CONNECT_REQUEST_TARGET_FACING_NET_INTERFACE);
        if (netInterface != null) {
            return netInterface;
        }
        return SocksValueDerivationHelper.getSocksOnConnectRequestTargetFacingNetInterfaceFrom(settings);
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
        return SocksValueDerivationHelper.getSocksOnConnectRequestTargetFacingSocketSettingsFrom(rule);
    }

    private static SocketSettings getSocks5OnConnectRequestTargetFacingSocketSettingsFrom(
            final Settings settings) {
        SocketSettings socketSettings = settings.getLastValue(
                Socks5SettingSpecConstants.SOCKS5_ON_CONNECT_REQUEST_TARGET_FACING_SOCKET_SETTINGS);
        if (!socketSettings.toMap().isEmpty()) {
            return socketSettings;
        }
        return SocksValueDerivationHelper.getSocksOnConnectRequestTargetFacingSocketSettingsFrom(settings);
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
        return SocksValueDerivationHelper.getSocksOnUdpAssociateRequestClientFacingBindHostFrom(rule);
    }

    private static Host getSocks5OnUdpAssociateRequestClientFacingBindHostFrom(
            final Settings settings) {
        Host host = settings.getLastValue(
                Socks5SettingSpecConstants.SOCKS5_ON_UDP_ASSOCIATE_REQUEST_CLIENT_FACING_BIND_HOST);
        if (host != null) {
            return host;
        }
        return SocksValueDerivationHelper.getSocksOnUdpAssociateRequestClientFacingBindHostFrom(settings);
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
        return SocksValueDerivationHelper.getSocksOnUdpAssociateRequestClientFacingBindHostAddressTypesFrom(rule);
    }

    private static HostAddressTypes getSocks5OnUdpAssociateRequestClientFacingBindHostAddressTypesFrom(
            final Settings settings) {
        HostAddressTypes hostAddressTypes = settings.getLastValue(
                Socks5SettingSpecConstants.SOCKS5_ON_UDP_ASSOCIATE_REQUEST_CLIENT_FACING_BIND_HOST_ADDRESS_TYPES);
        if (!hostAddressTypes.toList().isEmpty()) {
            return hostAddressTypes;
        }
        return SocksValueDerivationHelper.getSocksOnUdpAssociateRequestClientFacingBindHostAddressTypesFrom(settings);
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
        return SocksValueDerivationHelper.getSocksOnUdpAssociateRequestClientFacingBindPortRangesFrom(rule);
    }

    private static PortRanges getSocks5OnUdpAssociateRequestClientFacingBindPortRangesFrom(
            final Settings settings) {
        PortRanges portRanges = settings.getLastValue(
                Socks5SettingSpecConstants.SOCKS5_ON_UDP_ASSOCIATE_REQUEST_CLIENT_FACING_BIND_PORT_RANGES);
        if (!portRanges.toList().isEmpty()) {
            return portRanges;
        }
        return SocksValueDerivationHelper.getSocksOnUdpAssociateRequestClientFacingBindPortRangesFrom(settings);
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
        return SocksValueDerivationHelper.getSocksOnUdpAssociateRequestClientFacingNetInterfaceFrom(rule);
    }

    private static NetInterface getSocks5OnUdpAssociateRequestClientFacingNetInterfaceFrom(
            final Settings settings) {
        NetInterface netInterface = settings.getLastValue(
                Socks5SettingSpecConstants.SOCKS5_ON_UDP_ASSOCIATE_REQUEST_CLIENT_FACING_NET_INTERFACE);
        if (netInterface != null) {
            return netInterface;
        }
        return SocksValueDerivationHelper.getSocksOnUdpAssociateRequestClientFacingNetInterfaceFrom(settings);
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
        return SocksValueDerivationHelper.getSocksOnUdpAssociateRequestClientFacingSocketSettingsFrom(rule);
    }

    private static SocketSettings getSocks5OnUdpAssociateRequestClientFacingSocketSettingsFrom(
            final Settings settings) {
        SocketSettings socketSettings = settings.getLastValue(
                Socks5SettingSpecConstants.SOCKS5_ON_UDP_ASSOCIATE_REQUEST_CLIENT_FACING_SOCKET_SETTINGS);
        if (!socketSettings.toMap().isEmpty()) {
            return socketSettings;
        }
        return SocksValueDerivationHelper.getSocksOnUdpAssociateRequestClientFacingSocketSettingsFrom(settings);
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
        return SocksValueDerivationHelper.getSocksOnUdpAssociateRequestPeerFacingBindHostFrom(rule);
    }

    private static Host getSocks5OnUdpAssociateRequestPeerFacingBindHostFrom(
            final Settings settings) {
        Host host = settings.getLastValue(
                Socks5SettingSpecConstants.SOCKS5_ON_UDP_ASSOCIATE_REQUEST_PEER_FACING_BIND_HOST);
        if (host != null) {
            return host;
        }
        return SocksValueDerivationHelper.getSocksOnUdpAssociateRequestPeerFacingBindHostFrom(settings);
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
        return SocksValueDerivationHelper.getSocksOnUdpAssociateRequestPeerFacingBindHostAddressTypesFrom(rule);
    }

    private static HostAddressTypes getSocks5OnUdpAssociateRequestPeerFacingBindHostAddressTypesFrom(
            final Settings settings) {
        HostAddressTypes hostAddressTypes = settings.getLastValue(
                Socks5SettingSpecConstants.SOCKS5_ON_UDP_ASSOCIATE_REQUEST_PEER_FACING_BIND_HOST_ADDRESS_TYPES);
        if (!hostAddressTypes.toList().isEmpty()) {
            return hostAddressTypes;
        }
        return SocksValueDerivationHelper.getSocksOnUdpAssociateRequestPeerFacingBindHostAddressTypesFrom(settings);
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
        return SocksValueDerivationHelper.getSocksOnUdpAssociateRequestPeerFacingBindPortRangesFrom(rule);
    }

    private static PortRanges getSocks5OnUdpAssociateRequestPeerFacingBindPortRangesFrom(
            final Settings settings) {
        PortRanges portRanges = settings.getLastValue(
                Socks5SettingSpecConstants.SOCKS5_ON_UDP_ASSOCIATE_REQUEST_PEER_FACING_BIND_PORT_RANGES);
        if (!portRanges.toList().isEmpty()) {
            return portRanges;
        }
        return SocksValueDerivationHelper.getSocksOnUdpAssociateRequestPeerFacingBindPortRangesFrom(settings);
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
        return SocksValueDerivationHelper.getSocksOnUdpAssociateRequestPeerFacingNetInterfaceFrom(rule);
    }

    private static NetInterface getSocks5OnUdpAssociateRequestPeerFacingNetInterfaceFrom(
            final Settings settings) {
        NetInterface netInterface = settings.getLastValue(
                Socks5SettingSpecConstants.SOCKS5_ON_UDP_ASSOCIATE_REQUEST_PEER_FACING_NET_INTERFACE);
        if (netInterface != null) {
            return netInterface;
        }
        return SocksValueDerivationHelper.getSocksOnUdpAssociateRequestPeerFacingNetInterfaceFrom(settings);
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
        return SocksValueDerivationHelper.getSocksOnUdpAssociateRequestPeerFacingSocketSettingsFrom(rule);
    }

    private static SocketSettings getSocks5OnUdpAssociateRequestPeerFacingSocketSettingsFrom(
            final Settings settings) {
        SocketSettings socketSettings = settings.getLastValue(
                Socks5SettingSpecConstants.SOCKS5_ON_UDP_ASSOCIATE_REQUEST_PEER_FACING_SOCKET_SETTINGS);
        if (!socketSettings.toMap().isEmpty()) {
            return socketSettings;
        }
        return SocksValueDerivationHelper.getSocksOnUdpAssociateRequestPeerFacingSocketSettingsFrom(settings);
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
        return SocksValueDerivationHelper.getSocksOnUdpAssociateRequestRelayBufferSizeFrom(rule);
    }

    private static PositiveInteger getSocks5OnUdpAssociateRequestRelayBufferSizeFrom(
            final Settings settings) {
        PositiveInteger relayBufferSize = settings.getLastValue(
                Socks5SettingSpecConstants.SOCKS5_ON_UDP_ASSOCIATE_REQUEST_RELAY_BUFFER_SIZE);
        if (relayBufferSize != null) {
            return relayBufferSize;
        }
        return SocksValueDerivationHelper.getSocksOnUdpAssociateRequestRelayBufferSizeFrom(settings);
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
        return SocksValueDerivationHelper.getSocksOnUdpAssociateRequestRelayIdleTimeoutFrom(rule);
    }

    private static PositiveInteger getSocks5OnUdpAssociateRequestRelayIdleTimeoutFrom(
            final Settings settings) {
        PositiveInteger relayIdleTimeout = settings.getLastValue(
                Socks5SettingSpecConstants.SOCKS5_ON_UDP_ASSOCIATE_REQUEST_RELAY_IDLE_TIMEOUT);
        if (relayIdleTimeout != null) {
            return relayIdleTimeout;
        }
        return SocksValueDerivationHelper.getSocksOnUdpAssociateRequestRelayIdleTimeoutFrom(settings);
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
        return SocksValueDerivationHelper.getSocksOnUdpAssociateRequestRelayInboundBandwidthLimitFrom(rule);
    }

    private static PositiveInteger getSocks5OnUdpAssociateRequestRelayInboundBandwidthLimitFrom(
            final Settings settings) {
        PositiveInteger relayInboundBandwidthLimit = settings.getLastValue(
                Socks5SettingSpecConstants.SOCKS5_ON_UDP_ASSOCIATE_REQUEST_RELAY_INBOUND_BANDWIDTH_LIMIT);
        if (relayInboundBandwidthLimit != null) {
            return relayInboundBandwidthLimit;
        }
        return SocksValueDerivationHelper.getSocksOnUdpAssociateRequestRelayInboundBandwidthLimitFrom(settings);
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
        return SocksValueDerivationHelper.getSocksOnUdpAssociateRequestRelayOutboundBandwidthLimitFrom(rule);
    }

    private static PositiveInteger getSocks5OnUdpAssociateRequestRelayOutboundBandwidthLimitFrom(
            final Settings settings) {
        PositiveInteger relayOutboundBandwidthLimit = settings.getLastValue(
                Socks5SettingSpecConstants.SOCKS5_ON_UDP_ASSOCIATE_REQUEST_RELAY_OUTBOUND_BANDWIDTH_LIMIT);
        if (relayOutboundBandwidthLimit != null) {
            return relayOutboundBandwidthLimit;
        }
        return SocksValueDerivationHelper.getSocksOnUdpAssociateRequestRelayOutboundBandwidthLimitFrom(settings);
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

    public static Address getSocks5RequestDesiredDestinationAddressRedirectFrom(
            final Rule rule) {
        Address address = rule.getLastRuleActionValue(
                Socks5RuleActionSpecConstants.SOCKS5_REQUEST_DESIRED_DESTINATION_ADDRESS_REDIRECT);
        if (address != null) {
            return address;
        }
        String string = SocksValueDerivationHelper.getSocksRequestDesiredDestinationAddressRedirectFrom(rule);
        if (string != null) {
            return Address.newInstanceFrom(string);
        }
        return null;
    }

    public static Port getSocks5RequestDesiredDestinationPortRedirectFrom(
            final Rule rule) {
        Port port = rule.getLastRuleActionValue(
                Socks5RuleActionSpecConstants.SOCKS5_REQUEST_DESIRED_DESTINATION_PORT_REDIRECT);
        if (port != null) {
            return port;
        }
        return SocksValueDerivationHelper.getSocksRequestDesiredDestinationPortRedirectFrom(rule);
    }

    public static LogAction getSocks5RequestDesiredDestinationRedirectLogActionFrom(
            final Rule rule) {
        LogAction logAction = rule.getLastRuleActionValue(
                Socks5RuleActionSpecConstants.SOCKS5_REQUEST_DESIRED_DESTINATION_REDIRECT_LOG_ACTION);
        if (logAction != null) {
            return logAction;
        }
        return SocksValueDerivationHelper.getSocksRequestDesiredDestinationRedirectLogActionFrom(rule);
    }

    public static UserRepository getSocks5UserpassAuthMethodUserRepositoryFrom(
            final Settings settings) {
        UserRepository userRepository = settings.getLastValue(
                Socks5SettingSpecConstants.SOCKS5_USERPASSAUTHMETHOD_USER_REPOSITORY);
        if (userRepository != null) {
            return userRepository;
        }
        return UserRepository.newInstanceFrom(
                SocksValueDerivationHelper.getSocksUserpassAuthMethodUserRepositoryFrom(settings));
    }

}
