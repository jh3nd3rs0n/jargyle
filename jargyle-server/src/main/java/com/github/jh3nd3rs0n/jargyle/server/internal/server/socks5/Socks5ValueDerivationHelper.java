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

    private static HostAddressTypes getSocks5OnBindRequestRemoteFacingServerSocketBindHostAddressTypesFrom(
            final Rule rule) {
        HostAddressTypes hostAddressTypes = HostAddressTypes.of(
                rule.getRuleActionValues(
                        Socks5RuleActionSpecConstants.SOCKS5_ON_BIND_REQUEST_REMOTE_FACING_SERVER_SOCKET_BIND_HOST_ADDRESS_TYPE));
        if (!hostAddressTypes.toList().isEmpty()) {
            return hostAddressTypes;
        }
        return SocksValueDerivationHelper.getSocksOnBindRequestRemoteFacingServerSocketBindHostAddressTypesFrom(rule);
    }

    private static HostAddressTypes getSocks5OnBindRequestRemoteFacingServerSocketBindHostAddressTypesFrom(
            final Settings settings) {
        HostAddressTypes hostAddressTypes = settings.getLastValue(
                Socks5SettingSpecConstants.SOCKS5_ON_BIND_REQUEST_REMOTE_FACING_SERVER_SOCKET_BIND_HOST_ADDRESS_TYPES);
        if (!hostAddressTypes.toList().isEmpty()) {
            return hostAddressTypes;
        }
        return SocksValueDerivationHelper.getSocksOnBindRequestRemoteFacingServerSocketBindHostAddressTypesFrom(settings);
    }

    private static HostAddressTypes getSocks5OnBindRequestRemoteFacingServerSocketBindHostAddressTypesFrom(
            final Rule rule, final Settings settings) {
        HostAddressTypes hostAddressTypes =
                getSocks5OnBindRequestRemoteFacingServerSocketBindHostAddressTypesFrom(rule);
        if (!hostAddressTypes.toList().isEmpty()) {
            return hostAddressTypes;
        }
        return getSocks5OnBindRequestRemoteFacingServerSocketBindHostAddressTypesFrom(settings);
    }

    private static Host getSocks5OnBindRequestRemoteFacingServerSocketBindHostFrom(
            final Rule rule) {
        Host host = rule.getLastRuleActionValue(
                Socks5RuleActionSpecConstants.SOCKS5_ON_BIND_REQUEST_REMOTE_FACING_SERVER_SOCKET_BIND_HOST);
        if (host != null) {
            return host;
        }
        return SocksValueDerivationHelper.getSocksOnBindRequestRemoteFacingServerSocketBindHostFrom(rule);
    }

    private static Host getSocks5OnBindRequestRemoteFacingServerSocketBindHostFrom(
            final Settings settings) {
        Host host = settings.getLastValue(
                Socks5SettingSpecConstants.SOCKS5_ON_BIND_REQUEST_REMOTE_FACING_SERVER_SOCKET_BIND_HOST);
        if (host != null) {
            return host;
        }
        return SocksValueDerivationHelper.getSocksOnBindRequestRemoteFacingServerSocketBindHostFrom(settings);
    }

    public static Host getSocks5OnBindRequestRemoteFacingServerSocketBindHostFrom(
            final Rule rule, final Settings settings) {
        Host host = getSocks5OnBindRequestRemoteFacingServerSocketBindHostFrom(rule);
        if (host != null) {
            return host;
        }
        host = getSocks5OnBindRequestRemoteFacingServerSocketBindHostFrom(settings);
        if (host != null) {
            return host;
        }
        NetInterface netInterface =
                getSocks5OnBindRequestRemoteFacingServerSocketNetInterfaceFrom(rule, settings);
        if (netInterface != null) {
            return netInterface.getHostAddresses(
                            getSocks5OnBindRequestRemoteFacingServerSocketBindHostAddressTypesFrom(
                                    rule, settings))
                    .stream()
                    .findFirst()
                    .orElse(HostIpv4Address.getAllZerosInstance());
        }
        return HostIpv4Address.getAllZerosInstance();
    }

    private static PortRanges getSocks5OnBindRequestRemoteFacingServerSocketBindPortRangesFrom(
            final Rule rule) {
        PortRanges portRanges = PortRanges.of(rule.getRuleActionValues(
                Socks5RuleActionSpecConstants.SOCKS5_ON_BIND_REQUEST_REMOTE_FACING_SERVER_SOCKET_BIND_PORT_RANGE));
        if (!portRanges.toList().isEmpty()) {
            return portRanges;
        }
        return SocksValueDerivationHelper.getSocksOnBindRequestRemoteFacingServerSocketBindPortRangesFrom(rule);
    }

    private static PortRanges getSocks5OnBindRequestRemoteFacingServerSocketBindPortRangesFrom(
            final Settings settings) {
        PortRanges portRanges = settings.getLastValue(
                Socks5SettingSpecConstants.SOCKS5_ON_BIND_REQUEST_REMOTE_FACING_SERVER_SOCKET_BIND_PORT_RANGES);
        if (!portRanges.toList().isEmpty()) {
            return portRanges;
        }
        return SocksValueDerivationHelper.getSocksOnBindRequestRemoteFacingServerSocketBindPortRangesFrom(settings);
    }

    public static PortRanges getSocks5OnBindRequestRemoteFacingServerSocketBindPortRangesFrom(
            final Rule rule, final Settings settings) {
        PortRanges portRanges = getSocks5OnBindRequestRemoteFacingServerSocketBindPortRangesFrom(
                rule);
        if (!portRanges.toList().isEmpty()) {
            return portRanges;
        }
        return getSocks5OnBindRequestRemoteFacingServerSocketBindPortRangesFrom(settings);
    }

    private static NetInterface getSocks5OnBindRequestRemoteFacingServerSocketNetInterfaceFrom(
            final Rule rule) {
        NetInterface netInterface = rule.getLastRuleActionValue(
                Socks5RuleActionSpecConstants.SOCKS5_ON_BIND_REQUEST_REMOTE_FACING_SERVER_SOCKET_NET_INTERFACE);
        if (netInterface != null) {
            return netInterface;
        }
        return SocksValueDerivationHelper.getSocksOnBindRequestRemoteFacingServerSocketNetInterfaceFrom(rule);
    }

    private static NetInterface getSocks5OnBindRequestRemoteFacingServerSocketNetInterfaceFrom(
            final Settings settings) {
        NetInterface netInterface = settings.getLastValue(
                Socks5SettingSpecConstants.SOCKS5_ON_BIND_REQUEST_REMOTE_FACING_SERVER_SOCKET_NET_INTERFACE);
        if (netInterface != null) {
            return netInterface;
        }
        return SocksValueDerivationHelper.getSocksOnBindRequestRemoteFacingServerSocketNetInterfaceFrom(settings);
    }

    private static NetInterface getSocks5OnBindRequestRemoteFacingServerSocketNetInterfaceFrom(
            final Rule rule, final Settings settings) {
        NetInterface netInterface =
                getSocks5OnBindRequestRemoteFacingServerSocketNetInterfaceFrom(rule);
        if (netInterface != null) {
            return netInterface;
        }
        return getSocks5OnBindRequestRemoteFacingServerSocketNetInterfaceFrom(settings);
    }

    private static SocketSettings getSocks5OnBindRequestRemoteFacingServerSocketSocketSettingsFrom(
            final Rule rule) {
        SocketSettings socketSettings = SocketSettings.of(new ArrayList<>(
                rule.getRuleActionValues(
                        Socks5RuleActionSpecConstants.SOCKS5_ON_BIND_REQUEST_REMOTE_FACING_SERVER_SOCKET_SOCKET_SETTING)));
        if (!socketSettings.toMap().isEmpty()) {
            return socketSettings;
        }
        return SocksValueDerivationHelper.getSocksOnBindRequestRemoteFacingServerSocketSocketSettingsFrom(rule);
    }

    private static SocketSettings getSocks5OnBindRequestRemoteFacingServerSocketSocketSettingsFrom(
            final Settings settings) {
        SocketSettings socketSettings = settings.getLastValue(
                Socks5SettingSpecConstants.SOCKS5_ON_BIND_REQUEST_REMOTE_FACING_SERVER_SOCKET_SOCKET_SETTINGS);
        if (!socketSettings.toMap().isEmpty()) {
            return socketSettings;
        }
        return SocksValueDerivationHelper.getSocksOnBindRequestRemoteFacingServerSocketSocketSettingsFrom(settings);
    }

    public static SocketSettings getSocks5OnBindRequestRemoteFacingServerSocketSocketSettingsFrom(
            final Rule rule, final Settings settings) {
        SocketSettings socketSettings =
                getSocks5OnBindRequestRemoteFacingServerSocketSocketSettingsFrom(rule);
        if (!socketSettings.toMap().isEmpty()) {
            return socketSettings;
        }
        return getSocks5OnBindRequestRemoteFacingServerSocketSocketSettingsFrom(settings);
    }

    private static SocketSettings getSocks5OnBindRequestRemoteSocketSocketSettingsFrom(
            final Rule rule) {
        SocketSettings socketSettings = SocketSettings.of(new ArrayList<>(
                rule.getRuleActionValues(
                        Socks5RuleActionSpecConstants.SOCKS5_ON_BIND_REQUEST_REMOTE_SOCKET_SOCKET_SETTING)));
        if (!socketSettings.toMap().isEmpty()) {
            return socketSettings;
        }
        return SocksValueDerivationHelper.getSocksOnBindRequestRemoteSocketSocketSettingsFrom(rule);
    }

    private static SocketSettings getSocks5OnBindRequestRemoteSocketSocketSettingsFrom(
            final Settings settings) {
        SocketSettings socketSettings = settings.getLastValue(
                Socks5SettingSpecConstants.SOCKS5_ON_BIND_REQUEST_REMOTE_SOCKET_SOCKET_SETTINGS);
        if (!socketSettings.toMap().isEmpty()) {
            return socketSettings;
        }
        return SocksValueDerivationHelper.getSocksOnBindRequestRemoteSocketSocketSettingsFrom(settings);
    }

    public static SocketSettings getSocks5OnBindRequestRemoteSocketSocketSettingsFrom(
            final Rule rule, final Settings settings) {
        SocketSettings socketSettings =
                getSocks5OnBindRequestRemoteSocketSocketSettingsFrom(rule);
        if (!socketSettings.toMap().isEmpty()) {
            return socketSettings;
        }
        return getSocks5OnBindRequestRemoteSocketSocketSettingsFrom(settings);
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

    private static HostAddressTypes getSocks5OnConnectRequestRemoteSocketBindHostAddressTypesFrom(
            final Rule rule) {
        HostAddressTypes hostAddressTypes = HostAddressTypes.of(
                rule.getRuleActionValues(
                        Socks5RuleActionSpecConstants.SOCKS5_ON_CONNECT_REQUEST_REMOTE_SOCKET_BIND_HOST_ADDRESS_TYPE));
        if (!hostAddressTypes.toList().isEmpty()) {
            return hostAddressTypes;
        }
        return SocksValueDerivationHelper.getSocksOnConnectRequestRemoteSocketBindHostAddressTypesFrom(rule);
    }

    private static HostAddressTypes getSocks5OnConnectRequestRemoteSocketBindHostAddressTypesFrom(
            final Settings settings) {
        HostAddressTypes hostAddressTypes = settings.getLastValue(
                Socks5SettingSpecConstants.SOCKS5_ON_CONNECT_REQUEST_REMOTE_SOCKET_BIND_HOST_ADDRESS_TYPES);
        if (!hostAddressTypes.toList().isEmpty()) {
            return hostAddressTypes;
        }
        return SocksValueDerivationHelper.getSocksOnConnectRequestRemoteSocketBindHostAddressTypesFrom(settings);
    }

    private static HostAddressTypes getSocks5OnConnectRequestRemoteSocketBindHostAddressTypesFrom(
            final Rule rule, final Settings settings) {
        HostAddressTypes hostAddressTypes =
                getSocks5OnConnectRequestRemoteSocketBindHostAddressTypesFrom(rule);
        if (!hostAddressTypes.toList().isEmpty()) {
            return hostAddressTypes;
        }
        return getSocks5OnConnectRequestRemoteSocketBindHostAddressTypesFrom(settings);
    }

    private static Host getSocks5OnConnectRequestRemoteSocketBindHostFrom(
            final Rule rule) {
        Host host = rule.getLastRuleActionValue(
                Socks5RuleActionSpecConstants.SOCKS5_ON_CONNECT_REQUEST_REMOTE_SOCKET_BIND_HOST);
        if (host != null) {
            return host;
        }
        return SocksValueDerivationHelper.getSocksOnConnectRequestRemoteSocketBindHostFrom(rule);
    }

    private static Host getSocks5OnConnectRequestRemoteSocketBindHostFrom(
            final Settings settings) {
        Host host = settings.getLastValue(
                Socks5SettingSpecConstants.SOCKS5_ON_CONNECT_REQUEST_REMOTE_SOCKET_BIND_HOST);
        if (host != null) {
            return host;
        }
        return SocksValueDerivationHelper.getSocksOnConnectRequestRemoteSocketBindHostFrom(settings);
    }

    public static Host getSocks5OnConnectRequestRemoteSocketBindHostFrom(
            final Rule rule, final Settings settings) {
        Host host = getSocks5OnConnectRequestRemoteSocketBindHostFrom(rule);
        if (host != null) {
            return host;
        }
        host = getSocks5OnConnectRequestRemoteSocketBindHostFrom(settings);
        if (host != null) {
            return host;
        }
        NetInterface netInterface =
                getSocks5OnConnectRequestRemoteSocketNetInterfaceFrom(rule, settings);
        if (netInterface != null) {
            return netInterface.getHostAddresses(
                            getSocks5OnConnectRequestRemoteSocketBindHostAddressTypesFrom(
                                    rule, settings))
                    .stream()
                    .findFirst()
                    .orElse(HostIpv4Address.getAllZerosInstance());
        }
        return HostIpv4Address.getAllZerosInstance();
    }

    private static PortRanges getSocks5OnConnectRequestRemoteSocketBindPortRangesFrom(
            final Rule rule) {
        PortRanges portRanges = PortRanges.of(rule.getRuleActionValues(
                Socks5RuleActionSpecConstants.SOCKS5_ON_CONNECT_REQUEST_REMOTE_SOCKET_BIND_PORT_RANGE));
        if (!portRanges.toList().isEmpty()) {
            return portRanges;
        }
        return SocksValueDerivationHelper.getSocksOnConnectRequestRemoteSocketBindPortRangesFrom(rule);
    }

    private static PortRanges getSocks5OnConnectRequestRemoteSocketBindPortRangesFrom(
            final Settings settings) {
        PortRanges portRanges = settings.getLastValue(
                Socks5SettingSpecConstants.SOCKS5_ON_CONNECT_REQUEST_REMOTE_SOCKET_BIND_PORT_RANGES);
        if (!portRanges.toList().isEmpty()) {
            return portRanges;
        }
        return SocksValueDerivationHelper.getSocksOnConnectRequestRemoteSocketBindPortRangesFrom(settings);
    }

    public static PortRanges getSocks5OnConnectRequestRemoteSocketBindPortRangesFrom(
            final Rule rule, final Settings settings) {
        PortRanges portRanges = getSocks5OnConnectRequestRemoteSocketBindPortRangesFrom(
                rule);
        if (!portRanges.toList().isEmpty()) {
            return portRanges;
        }
        return getSocks5OnConnectRequestRemoteSocketBindPortRangesFrom(settings);
    }

    private static PositiveInteger getSocks5OnConnectRequestRemoteSocketConnectTimeoutFrom(
            final Rule rule) {
        PositiveInteger i = rule.getLastRuleActionValue(
                Socks5RuleActionSpecConstants.SOCKS5_ON_CONNECT_REQUEST_REMOTE_SOCKET_CONNECT_TIMEOUT);
        if (i != null) {
            return i;
        }
        return SocksValueDerivationHelper.getSocksOnConnectRequestRemoteSocketConnectTimeoutFrom(rule);
    }

    private static PositiveInteger getSocks5OnConnectRequestRemoteSocketConnectTimeoutFrom(
            final Settings settings) {
        PositiveInteger i = settings.getLastValue(
                Socks5SettingSpecConstants.SOCKS5_ON_CONNECT_REQUEST_REMOTE_SOCKET_CONNECT_TIMEOUT);
        if (i != null) {
            return i;
        }
        return SocksValueDerivationHelper.getSocksOnConnectRequestRemoteSocketConnectTimeoutFrom(settings);
    }

    public static PositiveInteger getSocks5OnConnectRequestRemoteSocketConnectTimeoutFrom(
            final Rule rule, final Settings settings) {
        PositiveInteger i = getSocks5OnConnectRequestRemoteSocketConnectTimeoutFrom(rule);
        if (i != null) {
            return i;
        }
        return getSocks5OnConnectRequestRemoteSocketConnectTimeoutFrom(settings);
    }

    private static NetInterface getSocks5OnConnectRequestRemoteSocketNetInterfaceFrom(
            final Rule rule) {
        NetInterface netInterface = rule.getLastRuleActionValue(
                Socks5RuleActionSpecConstants.SOCKS5_ON_CONNECT_REQUEST_REMOTE_SOCKET_NET_INTERFACE);
        if (netInterface != null) {
            return netInterface;
        }
        return SocksValueDerivationHelper.getSocksOnConnectRequestRemoteSocketNetInterfaceFrom(rule);
    }

    private static NetInterface getSocks5OnConnectRequestRemoteSocketNetInterfaceFrom(
            final Settings settings) {
        NetInterface netInterface = settings.getLastValue(
                Socks5SettingSpecConstants.SOCKS5_ON_CONNECT_REQUEST_REMOTE_SOCKET_NET_INTERFACE);
        if (netInterface != null) {
            return netInterface;
        }
        return SocksValueDerivationHelper.getSocksOnConnectRequestRemoteSocketNetInterfaceFrom(settings);
    }

    private static NetInterface getSocks5OnConnectRequestRemoteSocketNetInterfaceFrom(
            final Rule rule, final Settings settings) {
        NetInterface netInterface =
                getSocks5OnConnectRequestRemoteSocketNetInterfaceFrom(rule);
        if (netInterface != null) {
            return netInterface;
        }
        return getSocks5OnConnectRequestRemoteSocketNetInterfaceFrom(settings);
    }

    private static Boolean getSocks5OnConnectRequestRemoteSocketPrepareSocketFrom(
            final Rule rule) {
        Boolean b = rule.getLastRuleActionValue(
                Socks5RuleActionSpecConstants.SOCKS5_ON_CONNECT_REQUEST_REMOTE_SOCKET_PREPARE_SOCKET);
        if (b != null) {
            return b;
        }
        return SocksValueDerivationHelper.getSocksOnConnectRequestRemoteSocketPrepareSocketFrom(rule);
    }

    private static Boolean getSocks5OnConnectRequestRemoteSocketPrepareSocketFrom(
            final Settings settings) {
        Boolean b = settings.getLastValue(
                Socks5SettingSpecConstants.SOCKS5_ON_CONNECT_REQUEST_REMOTE_SOCKET_PREPARE_SOCKET);
        if (b != null) {
            return b;
        }
        return SocksValueDerivationHelper.getSocksOnConnectRequestRemoteSocketPrepareSocketFrom(settings);
    }

    public static Boolean getSocks5OnConnectRequestRemoteSocketPrepareSocketFrom(
            final Rule rule, final Settings settings) {
        Boolean b = getSocks5OnConnectRequestRemoteSocketPrepareSocketFrom(rule);
        if (b != null) {
            return b;
        }
        return getSocks5OnConnectRequestRemoteSocketPrepareSocketFrom(settings);
    }

    private static SocketSettings getSocks5OnConnectRequestRemoteSocketSocketSettingsFrom(
            final Rule rule) {
        SocketSettings socketSettings = SocketSettings.of(new ArrayList<>(
                rule.getRuleActionValues(
                        Socks5RuleActionSpecConstants.SOCKS5_ON_CONNECT_REQUEST_REMOTE_SOCKET_SOCKET_SETTING)));
        if (!socketSettings.toMap().isEmpty()) {
            return socketSettings;
        }
        return SocksValueDerivationHelper.getSocksOnConnectRequestRemoteSocketSocketSettingsFrom(rule);
    }

    private static SocketSettings getSocks5OnConnectRequestRemoteSocketSocketSettingsFrom(
            final Settings settings) {
        SocketSettings socketSettings = settings.getLastValue(
                Socks5SettingSpecConstants.SOCKS5_ON_CONNECT_REQUEST_REMOTE_SOCKET_SOCKET_SETTINGS);
        if (!socketSettings.toMap().isEmpty()) {
            return socketSettings;
        }
        return SocksValueDerivationHelper.getSocksOnConnectRequestRemoteSocketSocketSettingsFrom(settings);
    }

    public static SocketSettings getSocks5OnConnectRequestRemoteSocketSocketSettingsFrom(
            final Rule rule, final Settings settings) {
        SocketSettings socketSettings =
                getSocks5OnConnectRequestRemoteSocketSocketSettingsFrom(rule);
        if (!socketSettings.toMap().isEmpty()) {
            return socketSettings;
        }
        return getSocks5OnConnectRequestRemoteSocketSocketSettingsFrom(settings);
    }

    private static HostAddressTypes getSocks5OnUdpAssociateRequestClientFacingDatagramSocketBindHostAddressTypesFrom(
            final Rule rule) {
        HostAddressTypes hostAddressTypes = HostAddressTypes.of(
                rule.getRuleActionValues(
                        Socks5RuleActionSpecConstants.SOCKS5_ON_UDP_ASSOCIATE_REQUEST_CLIENT_FACING_DATAGRAM_SOCKET_BIND_HOST_ADDRESS_TYPE));
        if (!hostAddressTypes.toList().isEmpty()) {
            return hostAddressTypes;
        }
        return SocksValueDerivationHelper.getSocksOnUdpAssociateRequestClientFacingDatagramSocketBindHostAddressTypesFrom(rule);
    }

    private static HostAddressTypes getSocks5OnUdpAssociateRequestClientFacingDatagramSocketBindHostAddressTypesFrom(
            final Settings settings) {
        HostAddressTypes hostAddressTypes = settings.getLastValue(
                Socks5SettingSpecConstants.SOCKS5_ON_UDP_ASSOCIATE_REQUEST_CLIENT_FACING_DATAGRAM_SOCKET_BIND_HOST_ADDRESS_TYPES);
        if (!hostAddressTypes.toList().isEmpty()) {
            return hostAddressTypes;
        }
        return SocksValueDerivationHelper.getSocksOnUdpAssociateRequestClientFacingDatagramSocketBindHostAddressTypesFrom(settings);
    }

    private static HostAddressTypes getSocks5OnUdpAssociateRequestClientFacingDatagramSocketBindHostAddressTypesFrom(
            final Rule rule, final Settings settings) {
        HostAddressTypes hostAddressTypes =
                getSocks5OnUdpAssociateRequestClientFacingDatagramSocketBindHostAddressTypesFrom(rule);
        if (!hostAddressTypes.toList().isEmpty()) {
            return hostAddressTypes;
        }
        return getSocks5OnUdpAssociateRequestClientFacingDatagramSocketBindHostAddressTypesFrom(settings);
    }

    private static Host getSocks5OnUdpAssociateRequestClientFacingDatagramSocketBindHostFrom(
            final Rule rule) {
        Host host = rule.getLastRuleActionValue(
                Socks5RuleActionSpecConstants.SOCKS5_ON_UDP_ASSOCIATE_REQUEST_CLIENT_FACING_DATAGRAM_SOCKET_BIND_HOST);
        if (host != null) {
            return host;
        }
        return SocksValueDerivationHelper.getSocksOnUdpAssociateRequestClientFacingDatagramSocketBindHostFrom(rule);
    }

    private static Host getSocks5OnUdpAssociateRequestClientFacingDatagramSocketBindHostFrom(
            final Settings settings) {
        Host host = settings.getLastValue(
                Socks5SettingSpecConstants.SOCKS5_ON_UDP_ASSOCIATE_REQUEST_CLIENT_FACING_DATAGRAM_SOCKET_BIND_HOST);
        if (host != null) {
            return host;
        }
        return SocksValueDerivationHelper.getSocksOnUdpAssociateRequestClientFacingDatagramSocketBindHostFrom(settings);
    }

    public static Host getSocks5OnUdpAssociateRequestClientFacingDatagramSocketBindHostFrom(
            final Rule rule, final Settings settings) {
        Host host = getSocks5OnUdpAssociateRequestClientFacingDatagramSocketBindHostFrom(rule);
        if (host != null) {
            return host;
        }
        host = getSocks5OnUdpAssociateRequestClientFacingDatagramSocketBindHostFrom(settings);
        if (host != null) {
            return host;
        }
        NetInterface netInterface =
                getSocks5OnUdpAssociateRequestClientFacingDatagramSocketNetInterfaceFrom(rule, settings);
        if (netInterface != null) {
            return netInterface.getHostAddresses(
                            getSocks5OnUdpAssociateRequestClientFacingDatagramSocketBindHostAddressTypesFrom(
                                    rule, settings))
                    .stream()
                    .findFirst()
                    .orElse(HostIpv4Address.getAllZerosInstance());
        }
        return HostIpv4Address.getAllZerosInstance();
    }

    private static PortRanges getSocks5OnUdpAssociateRequestClientFacingDatagramSocketBindPortRangesFrom(
            final Rule rule) {
        PortRanges portRanges = PortRanges.of(rule.getRuleActionValues(
                Socks5RuleActionSpecConstants.SOCKS5_ON_UDP_ASSOCIATE_REQUEST_CLIENT_FACING_DATAGRAM_SOCKET_BIND_PORT_RANGE));
        if (!portRanges.toList().isEmpty()) {
            return portRanges;
        }
        return SocksValueDerivationHelper.getSocksOnUdpAssociateRequestClientFacingDatagramSocketBindPortRangesFrom(rule);
    }

    private static PortRanges getSocks5OnUdpAssociateRequestClientFacingDatagramSocketBindPortRangesFrom(
            final Settings settings) {
        PortRanges portRanges = settings.getLastValue(
                Socks5SettingSpecConstants.SOCKS5_ON_UDP_ASSOCIATE_REQUEST_CLIENT_FACING_DATAGRAM_SOCKET_BIND_PORT_RANGES);
        if (!portRanges.toList().isEmpty()) {
            return portRanges;
        }
        return SocksValueDerivationHelper.getSocksOnUdpAssociateRequestClientFacingDatagramSocketBindPortRangesFrom(settings);
    }

    public static PortRanges getSocks5OnUdpAssociateRequestClientFacingDatagramSocketBindPortRangesFrom(
            final Rule rule, final Settings settings) {
        PortRanges portRanges = getSocks5OnUdpAssociateRequestClientFacingDatagramSocketBindPortRangesFrom(
                rule);
        if (!portRanges.toList().isEmpty()) {
            return portRanges;
        }
        return getSocks5OnUdpAssociateRequestClientFacingDatagramSocketBindPortRangesFrom(settings);
    }

    private static NetInterface getSocks5OnUdpAssociateRequestClientFacingDatagramSocketNetInterfaceFrom(
            final Rule rule) {
        NetInterface netInterface = rule.getLastRuleActionValue(
                Socks5RuleActionSpecConstants.SOCKS5_ON_UDP_ASSOCIATE_REQUEST_CLIENT_FACING_DATAGRAM_SOCKET_NET_INTERFACE);
        if (netInterface != null) {
            return netInterface;
        }
        return SocksValueDerivationHelper.getSocksOnUdpAssociateRequestClientFacingDatagramSocketNetInterfaceFrom(rule);
    }

    private static NetInterface getSocks5OnUdpAssociateRequestClientFacingDatagramSocketNetInterfaceFrom(
            final Settings settings) {
        NetInterface netInterface = settings.getLastValue(
                Socks5SettingSpecConstants.SOCKS5_ON_UDP_ASSOCIATE_REQUEST_CLIENT_FACING_DATAGRAM_SOCKET_NET_INTERFACE);
        if (netInterface != null) {
            return netInterface;
        }
        return SocksValueDerivationHelper.getSocksOnUdpAssociateRequestClientFacingDatagramSocketNetInterfaceFrom(settings);
    }

    private static NetInterface getSocks5OnUdpAssociateRequestClientFacingDatagramSocketNetInterfaceFrom(
            final Rule rule, final Settings settings) {
        NetInterface netInterface =
                getSocks5OnUdpAssociateRequestClientFacingDatagramSocketNetInterfaceFrom(rule);
        if (netInterface != null) {
            return netInterface;
        }
        return getSocks5OnUdpAssociateRequestClientFacingDatagramSocketNetInterfaceFrom(settings);
    }

    private static SocketSettings getSocks5OnUdpAssociateRequestClientFacingDatagramSocketSocketSettingsFrom(
            final Rule rule) {
        SocketSettings socketSettings = SocketSettings.of(new ArrayList<>(
                rule.getRuleActionValues(
                        Socks5RuleActionSpecConstants.SOCKS5_ON_UDP_ASSOCIATE_REQUEST_CLIENT_FACING_DATAGRAM_SOCKET_SOCKET_SETTING)));
        if (!socketSettings.toMap().isEmpty()) {
            return socketSettings;
        }
        return SocksValueDerivationHelper.getSocksOnUdpAssociateRequestClientFacingDatagramSocketSocketSettingsFrom(rule);
    }

    private static SocketSettings getSocks5OnUdpAssociateRequestClientFacingDatagramSocketSocketSettingsFrom(
            final Settings settings) {
        SocketSettings socketSettings = settings.getLastValue(
                Socks5SettingSpecConstants.SOCKS5_ON_UDP_ASSOCIATE_REQUEST_CLIENT_FACING_DATAGRAM_SOCKET_SOCKET_SETTINGS);
        if (!socketSettings.toMap().isEmpty()) {
            return socketSettings;
        }
        return SocksValueDerivationHelper.getSocksOnUdpAssociateRequestClientFacingDatagramSocketSocketSettingsFrom(settings);
    }

    public static SocketSettings getSocks5OnUdpAssociateRequestClientFacingDatagramSocketSocketSettingsFrom(
            final Rule rule, final Settings settings) {
        SocketSettings socketSettings =
                getSocks5OnUdpAssociateRequestClientFacingDatagramSocketSocketSettingsFrom(rule);
        if (!socketSettings.toMap().isEmpty()) {
            return socketSettings;
        }
        return getSocks5OnUdpAssociateRequestClientFacingDatagramSocketSocketSettingsFrom(settings);
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

    private static HostAddressTypes getSocks5OnUdpAssociateRequestRemoteFacingDatagramSocketBindHostAddressTypesFrom(
            final Rule rule) {
        HostAddressTypes hostAddressTypes = HostAddressTypes.of(
                rule.getRuleActionValues(
                        Socks5RuleActionSpecConstants.SOCKS5_ON_UDP_ASSOCIATE_REQUEST_REMOTE_FACING_DATAGRAM_SOCKET_BIND_HOST_ADDRESS_TYPE));
        if (!hostAddressTypes.toList().isEmpty()) {
            return hostAddressTypes;
        }
        return SocksValueDerivationHelper.getSocksOnUdpAssociateRequestRemoteFacingDatagramSocketBindHostAddressTypesFrom(rule);
    }

    private static HostAddressTypes getSocks5OnUdpAssociateRequestRemoteFacingDatagramSocketBindHostAddressTypesFrom(
            final Settings settings) {
        HostAddressTypes hostAddressTypes = settings.getLastValue(
                Socks5SettingSpecConstants.SOCKS5_ON_UDP_ASSOCIATE_REQUEST_REMOTE_FACING_DATAGRAM_SOCKET_BIND_HOST_ADDRESS_TYPES);
        if (!hostAddressTypes.toList().isEmpty()) {
            return hostAddressTypes;
        }
        return SocksValueDerivationHelper.getSocksOnUdpAssociateRequestRemoteFacingDatagramSocketBindHostAddressTypesFrom(settings);
    }

    private static HostAddressTypes getSocks5OnUdpAssociateRequestRemoteFacingDatagramSocketBindHostAddressTypesFrom(
            final Rule rule, final Settings settings) {
        HostAddressTypes hostAddressTypes =
                getSocks5OnUdpAssociateRequestRemoteFacingDatagramSocketBindHostAddressTypesFrom(rule);
        if (!hostAddressTypes.toList().isEmpty()) {
            return hostAddressTypes;
        }
        return getSocks5OnUdpAssociateRequestRemoteFacingDatagramSocketBindHostAddressTypesFrom(settings);
    }

    private static Host getSocks5OnUdpAssociateRequestRemoteFacingDatagramSocketBindHostFrom(
            final Rule rule) {
        Host host = rule.getLastRuleActionValue(
                Socks5RuleActionSpecConstants.SOCKS5_ON_UDP_ASSOCIATE_REQUEST_REMOTE_FACING_DATAGRAM_SOCKET_BIND_HOST);
        if (host != null) {
            return host;
        }
        return SocksValueDerivationHelper.getSocksOnUdpAssociateRequestRemoteFacingDatagramSocketBindHostFrom(rule);
    }

    private static Host getSocks5OnUdpAssociateRequestRemoteFacingDatagramSocketBindHostFrom(
            final Settings settings) {
        Host host = settings.getLastValue(
                Socks5SettingSpecConstants.SOCKS5_ON_UDP_ASSOCIATE_REQUEST_REMOTE_FACING_DATAGRAM_SOCKET_BIND_HOST);
        if (host != null) {
            return host;
        }
        return SocksValueDerivationHelper.getSocksOnUdpAssociateRequestRemoteFacingDatagramSocketBindHostFrom(settings);
    }

    public static Host getSocks5OnUdpAssociateRequestRemoteFacingDatagramSocketBindHostFrom(
            final Rule rule, final Settings settings) {
        Host host = getSocks5OnUdpAssociateRequestRemoteFacingDatagramSocketBindHostFrom(rule);
        if (host != null) {
            return host;
        }
        host = getSocks5OnUdpAssociateRequestRemoteFacingDatagramSocketBindHostFrom(settings);
        if (host != null) {
            return host;
        }
        NetInterface netInterface =
                getSocks5OnUdpAssociateRequestRemoteFacingDatagramSocketNetInterfaceFrom(rule, settings);
        if (netInterface != null) {
            return netInterface.getHostAddresses(
                            getSocks5OnUdpAssociateRequestRemoteFacingDatagramSocketBindHostAddressTypesFrom(
                                    rule, settings))
                    .stream()
                    .findFirst()
                    .orElse(HostIpv4Address.getAllZerosInstance());
        }
        return HostIpv4Address.getAllZerosInstance();
    }

    private static PortRanges getSocks5OnUdpAssociateRequestRemoteFacingDatagramSocketBindPortRangesFrom(
            final Rule rule) {
        PortRanges portRanges = PortRanges.of(rule.getRuleActionValues(
                Socks5RuleActionSpecConstants.SOCKS5_ON_UDP_ASSOCIATE_REQUEST_REMOTE_FACING_DATAGRAM_SOCKET_BIND_PORT_RANGE));
        if (!portRanges.toList().isEmpty()) {
            return portRanges;
        }
        return SocksValueDerivationHelper.getSocksOnUdpAssociateRequestRemoteFacingDatagramSocketBindPortRangesFrom(rule);
    }

    private static PortRanges getSocks5OnUdpAssociateRequestRemoteFacingDatagramSocketBindPortRangesFrom(
            final Settings settings) {
        PortRanges portRanges = settings.getLastValue(
                Socks5SettingSpecConstants.SOCKS5_ON_UDP_ASSOCIATE_REQUEST_REMOTE_FACING_DATAGRAM_SOCKET_BIND_PORT_RANGES);
        if (!portRanges.toList().isEmpty()) {
            return portRanges;
        }
        return SocksValueDerivationHelper.getSocksOnUdpAssociateRequestRemoteFacingDatagramSocketBindPortRangesFrom(settings);
    }

    public static PortRanges getSocks5OnUdpAssociateRequestRemoteFacingDatagramSocketBindPortRangesFrom(
            final Rule rule, final Settings settings) {
        PortRanges portRanges = getSocks5OnUdpAssociateRequestRemoteFacingDatagramSocketBindPortRangesFrom(
                rule);
        if (!portRanges.toList().isEmpty()) {
            return portRanges;
        }
        return getSocks5OnUdpAssociateRequestRemoteFacingDatagramSocketBindPortRangesFrom(settings);
    }

    private static NetInterface getSocks5OnUdpAssociateRequestRemoteFacingDatagramSocketNetInterfaceFrom(
            final Rule rule) {
        NetInterface netInterface = rule.getLastRuleActionValue(
                Socks5RuleActionSpecConstants.SOCKS5_ON_UDP_ASSOCIATE_REQUEST_REMOTE_FACING_DATAGRAM_SOCKET_NET_INTERFACE);
        if (netInterface != null) {
            return netInterface;
        }
        return SocksValueDerivationHelper.getSocksOnUdpAssociateRequestRemoteFacingDatagramSocketNetInterfaceFrom(rule);
    }

    private static NetInterface getSocks5OnUdpAssociateRequestRemoteFacingDatagramSocketNetInterfaceFrom(
            final Settings settings) {
        NetInterface netInterface = settings.getLastValue(
                Socks5SettingSpecConstants.SOCKS5_ON_UDP_ASSOCIATE_REQUEST_REMOTE_FACING_DATAGRAM_SOCKET_NET_INTERFACE);
        if (netInterface != null) {
            return netInterface;
        }
        return SocksValueDerivationHelper.getSocksOnUdpAssociateRequestRemoteFacingDatagramSocketNetInterfaceFrom(settings);
    }

    private static NetInterface getSocks5OnUdpAssociateRequestRemoteFacingDatagramSocketNetInterfaceFrom(
            final Rule rule, final Settings settings) {
        NetInterface netInterface =
                getSocks5OnUdpAssociateRequestRemoteFacingDatagramSocketNetInterfaceFrom(rule);
        if (netInterface != null) {
            return netInterface;
        }
        return getSocks5OnUdpAssociateRequestRemoteFacingDatagramSocketNetInterfaceFrom(settings);
    }

    private static SocketSettings getSocks5OnUdpAssociateRequestRemoteFacingDatagramSocketSocketSettingsFrom(
            final Rule rule) {
        SocketSettings socketSettings = SocketSettings.of(new ArrayList<>(
                rule.getRuleActionValues(
                        Socks5RuleActionSpecConstants.SOCKS5_ON_UDP_ASSOCIATE_REQUEST_REMOTE_FACING_DATAGRAM_SOCKET_SOCKET_SETTING)));
        if (!socketSettings.toMap().isEmpty()) {
            return socketSettings;
        }
        return SocksValueDerivationHelper.getSocksOnUdpAssociateRequestRemoteFacingDatagramSocketSocketSettingsFrom(rule);
    }

    private static SocketSettings getSocks5OnUdpAssociateRequestRemoteFacingDatagramSocketSocketSettingsFrom(
            final Settings settings) {
        SocketSettings socketSettings = settings.getLastValue(
                Socks5SettingSpecConstants.SOCKS5_ON_UDP_ASSOCIATE_REQUEST_REMOTE_FACING_DATAGRAM_SOCKET_SOCKET_SETTINGS);
        if (!socketSettings.toMap().isEmpty()) {
            return socketSettings;
        }
        return SocksValueDerivationHelper.getSocksOnUdpAssociateRequestRemoteFacingDatagramSocketSocketSettingsFrom(settings);
    }

    public static SocketSettings getSocks5OnUdpAssociateRequestRemoteFacingDatagramSocketSocketSettingsFrom(
            final Rule rule, final Settings settings) {
        SocketSettings socketSettings =
                getSocks5OnUdpAssociateRequestRemoteFacingDatagramSocketSocketSettingsFrom(rule);
        if (!socketSettings.toMap().isEmpty()) {
            return socketSettings;
        }
        return getSocks5OnUdpAssociateRequestRemoteFacingDatagramSocketSocketSettingsFrom(settings);
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
