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

    public static HostAddressTypes getSocksOnBindRequestRemoteFacingServerSocketBindHostAddressTypesFrom(
            final Rule rule) {
        HostAddressTypes hostAddressTypes = HostAddressTypes.of(
                rule.getRuleActionValues(
                        SocksRuleActionSpecConstants.SOCKS_ON_BIND_REQUEST_REMOTE_FACING_SERVER_SOCKET_BIND_HOST_ADDRESS_TYPE));
        if (!hostAddressTypes.toList().isEmpty()) {
            return hostAddressTypes;
        }
        return getSocksOnRequestRemoteEndpointsBindHostAddressTypesFrom(rule);
    }

    public static HostAddressTypes getSocksOnBindRequestRemoteFacingServerSocketBindHostAddressTypesFrom(
            final Settings settings) {
        HostAddressTypes hostAddressTypes = settings.getLastValue(
                SocksSettingSpecConstants.SOCKS_ON_BIND_REQUEST_REMOTE_FACING_SERVER_SOCKET_BIND_HOST_ADDRESS_TYPES);
        if (!hostAddressTypes.toList().isEmpty()) {
            return hostAddressTypes;
        }
        return getSocksOnRequestRemoteEndpointsBindHostAddressTypesFrom(
                settings);
    }

    public static Host getSocksOnBindRequestRemoteFacingServerSocketBindHostFrom(
            final Rule rule) {
        Host host = rule.getLastRuleActionValue(
                SocksRuleActionSpecConstants.SOCKS_ON_BIND_REQUEST_REMOTE_FACING_SERVER_SOCKET_BIND_HOST);
        if (host != null) {
            return host;
        }
        return getSocksOnRequestRemoteEndpointsBindHostFrom(rule);
    }

    public static Host getSocksOnBindRequestRemoteFacingServerSocketBindHostFrom(
            final Settings settings) {
        Host host = settings.getLastValue(
                SocksSettingSpecConstants.SOCKS_ON_BIND_REQUEST_REMOTE_FACING_SERVER_SOCKET_BIND_HOST);
        if (host != null) {
            return host;
        }
        return getSocksOnRequestRemoteEndpointsBindHostFrom(settings);
    }

    public static PortRanges getSocksOnBindRequestRemoteFacingServerSocketBindPortRangesFrom(
            final Rule rule) {
        PortRanges portRanges = PortRanges.of(rule.getRuleActionValues(
                SocksRuleActionSpecConstants.SOCKS_ON_BIND_REQUEST_REMOTE_FACING_SERVER_SOCKET_BIND_PORT_RANGE));
        if (!portRanges.toList().isEmpty()) {
            return portRanges;
        }
        return getSocksOnRequestRemoteEndpointsBindTcpPortRangesFrom(rule);
    }

    public static PortRanges getSocksOnBindRequestRemoteFacingServerSocketBindPortRangesFrom(
            final Settings settings) {
        PortRanges portRanges = settings.getLastValue(
                SocksSettingSpecConstants.SOCKS_ON_BIND_REQUEST_REMOTE_FACING_SERVER_SOCKET_BIND_PORT_RANGES);
        if (!portRanges.toList().isEmpty()) {
            return portRanges;
        }
        return getSocksOnRequestRemoteEndpointsBindTcpPortRangesFrom(settings);
    }

    public static NetInterface getSocksOnBindRequestRemoteFacingServerSocketNetInterfaceFrom(
            final Rule rule) {
        NetInterface netInterface = rule.getLastRuleActionValue(
                SocksRuleActionSpecConstants.SOCKS_ON_BIND_REQUEST_REMOTE_FACING_SERVER_SOCKET_NET_INTERFACE);
        if (netInterface != null) {
            return netInterface;
        }
        return getSocksOnRequestRemoteEndpointsNetInterfaceFrom(rule);
    }

    public static NetInterface getSocksOnBindRequestRemoteFacingServerSocketNetInterfaceFrom(
            final Settings settings) {
        NetInterface netInterface = settings.getLastValue(
                SocksSettingSpecConstants.SOCKS_ON_BIND_REQUEST_REMOTE_FACING_SERVER_SOCKET_NET_INTERFACE);
        if (netInterface != null) {
            return netInterface;
        }
        return getSocksOnRequestRemoteEndpointsNetInterfaceFrom(settings);
    }

    public static SocketSettings getSocksOnBindRequestRemoteFacingServerSocketSocketSettingsFrom(
            final Rule rule) {
        SocketSettings socketSettings = SocketSettings.of(new ArrayList<>(
                rule.getRuleActionValues(
                        SocksRuleActionSpecConstants.SOCKS_ON_BIND_REQUEST_REMOTE_FACING_SERVER_SOCKET_SOCKET_SETTING)));
        if (!socketSettings.toMap().isEmpty()) {
            return socketSettings;
        }
        return getSocksOnRequestRemoteEndpointsSocketSettingsFrom(rule);
    }

    public static SocketSettings getSocksOnBindRequestRemoteFacingServerSocketSocketSettingsFrom(
            final Settings settings) {
        SocketSettings socketSettings = settings.getLastValue(
                SocksSettingSpecConstants.SOCKS_ON_BIND_REQUEST_REMOTE_FACING_SERVER_SOCKET_SOCKET_SETTINGS);
        if (!socketSettings.toMap().isEmpty()) {
            return socketSettings;
        }
        return getSocksOnRequestRemoteEndpointsSocketSettingsFrom(settings);
    }

    public static SocketSettings getSocksOnBindRequestRemoteSocketSocketSettingsFrom(
            final Rule rule) {
        SocketSettings socketSettings = SocketSettings.of(new ArrayList<>(
                rule.getRuleActionValues(
                        SocksRuleActionSpecConstants.SOCKS_ON_BIND_REQUEST_REMOTE_SOCKET_SOCKET_SETTING)));
        if (!socketSettings.toMap().isEmpty()) {
            return socketSettings;
        }
        return getSocksOnRequestRemoteEndpointsSocketSettingsFrom(rule);
    }

    public static SocketSettings getSocksOnBindRequestRemoteSocketSocketSettingsFrom(
            final Settings settings) {
        SocketSettings socketSettings = settings.getLastValue(
                SocksSettingSpecConstants.SOCKS_ON_BIND_REQUEST_REMOTE_SOCKET_SOCKET_SETTINGS);
        if (!socketSettings.toMap().isEmpty()) {
            return socketSettings;
        }
        return getSocksOnRequestRemoteEndpointsSocketSettingsFrom(settings);
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

    public static HostAddressTypes getSocksOnConnectRequestRemoteSocketBindHostAddressTypesFrom(
            final Rule rule) {
        HostAddressTypes hostAddressTypes = HostAddressTypes.of(
                rule.getRuleActionValues(
                        SocksRuleActionSpecConstants.SOCKS_ON_CONNECT_REQUEST_REMOTE_SOCKET_BIND_HOST_ADDRESS_TYPE));
        if (!hostAddressTypes.toList().isEmpty()) {
            return hostAddressTypes;
        }
        return getSocksOnRequestRemoteEndpointsBindHostAddressTypesFrom(rule);
    }

    public static HostAddressTypes getSocksOnConnectRequestRemoteSocketBindHostAddressTypesFrom(
            final Settings settings) {
        HostAddressTypes hostAddressTypes = settings.getLastValue(
                SocksSettingSpecConstants.SOCKS_ON_CONNECT_REQUEST_REMOTE_SOCKET_BIND_HOST_ADDRESS_TYPES);
        if (!hostAddressTypes.toList().isEmpty()) {
            return hostAddressTypes;
        }
        return getSocksOnRequestRemoteEndpointsBindHostAddressTypesFrom(
                settings);
    }

    public static Host getSocksOnConnectRequestRemoteSocketBindHostFrom(
            final Rule rule) {
        Host host = rule.getLastRuleActionValue(
                SocksRuleActionSpecConstants.SOCKS_ON_CONNECT_REQUEST_REMOTE_SOCKET_BIND_HOST);
        if (host != null) {
            return host;
        }
        return getSocksOnRequestRemoteEndpointsBindHostFrom(rule);
    }

    public static Host getSocksOnConnectRequestRemoteSocketBindHostFrom(
            final Settings settings) {
        Host host = settings.getLastValue(
                SocksSettingSpecConstants.SOCKS_ON_CONNECT_REQUEST_REMOTE_SOCKET_BIND_HOST);
        if (host != null) {
            return host;
        }
        return getSocksOnRequestRemoteEndpointsBindHostFrom(settings);
    }

    public static PortRanges getSocksOnConnectRequestRemoteSocketBindPortRangesFrom(
            final Rule rule) {
        PortRanges portRanges = PortRanges.of(rule.getRuleActionValues(
                SocksRuleActionSpecConstants.SOCKS_ON_CONNECT_REQUEST_REMOTE_SOCKET_BIND_PORT_RANGE));
        if (!portRanges.toList().isEmpty()) {
            return portRanges;
        }
        return getSocksOnRequestRemoteEndpointsBindTcpPortRangesFrom(rule);
    }

    public static PortRanges getSocksOnConnectRequestRemoteSocketBindPortRangesFrom(
            final Settings settings) {
        PortRanges portRanges = settings.getLastValue(
                SocksSettingSpecConstants.SOCKS_ON_CONNECT_REQUEST_REMOTE_SOCKET_BIND_PORT_RANGES);
        if (!portRanges.toList().isEmpty()) {
            return portRanges;
        }
        return getSocksOnRequestRemoteEndpointsBindTcpPortRangesFrom(settings);
    }

    public static PositiveInteger getSocksOnConnectRequestRemoteSocketConnectTimeoutFrom(
            final Rule rule) {
        return rule.getLastRuleActionValue(
                SocksRuleActionSpecConstants.SOCKS_ON_CONNECT_REQUEST_REMOTE_SOCKET_CONNECT_TIMEOUT);
    }

    public static PositiveInteger getSocksOnConnectRequestRemoteSocketConnectTimeoutFrom(
            final Settings settings) {
        return settings.getLastValue(
                SocksSettingSpecConstants.SOCKS_ON_CONNECT_REQUEST_REMOTE_SOCKET_CONNECT_TIMEOUT);
    }

    public static NetInterface getSocksOnConnectRequestRemoteSocketNetInterfaceFrom(
            final Rule rule) {
        NetInterface netInterface = rule.getLastRuleActionValue(
                SocksRuleActionSpecConstants.SOCKS_ON_CONNECT_REQUEST_REMOTE_SOCKET_NET_INTERFACE);
        if (netInterface != null) {
            return netInterface;
        }
        return getSocksOnRequestRemoteEndpointsNetInterfaceFrom(rule);
    }

    public static NetInterface getSocksOnConnectRequestRemoteSocketNetInterfaceFrom(
            final Settings settings) {
        NetInterface netInterface = settings.getLastValue(
                SocksSettingSpecConstants.SOCKS_ON_CONNECT_REQUEST_REMOTE_SOCKET_NET_INTERFACE);
        if (netInterface != null) {
            return netInterface;
        }
        return getSocksOnRequestRemoteEndpointsNetInterfaceFrom(settings);
    }

    public static Boolean getSocksOnConnectRequestRemoteSocketPrepareSocketFrom(
            final Rule rule) {
        return rule.getLastRuleActionValue(
                SocksRuleActionSpecConstants.SOCKS_ON_CONNECT_REQUEST_REMOTE_SOCKET_PREPARE_SOCKET);
    }

    public static Boolean getSocksOnConnectRequestRemoteSocketPrepareSocketFrom(
            final Settings settings) {
        return settings.getLastValue(
                SocksSettingSpecConstants.SOCKS_ON_CONNECT_REQUEST_REMOTE_SOCKET_PREPARE_SOCKET);
    }

    public static SocketSettings getSocksOnConnectRequestRemoteSocketSocketSettingsFrom(
            final Rule rule) {
        SocketSettings socketSettings = SocketSettings.of(new ArrayList<>(
                rule.getRuleActionValues(
                        SocksRuleActionSpecConstants.SOCKS_ON_CONNECT_REQUEST_REMOTE_SOCKET_SOCKET_SETTING)));
        if (!socketSettings.toMap().isEmpty()) {
            return socketSettings;
        }
        return getSocksOnRequestRemoteEndpointsSocketSettingsFrom(rule);
    }

    public static SocketSettings getSocksOnConnectRequestRemoteSocketSocketSettingsFrom(
            final Settings settings) {
        SocketSettings socketSettings = settings.getLastValue(
                SocksSettingSpecConstants.SOCKS_ON_CONNECT_REQUEST_REMOTE_SOCKET_SOCKET_SETTINGS);
        if (!socketSettings.toMap().isEmpty()) {
            return socketSettings;
        }
        return getSocksOnRequestRemoteEndpointsSocketSettingsFrom(settings);
    }

    public static HostAddressTypes getSocksOnRequestClientEndpointsBindHostAddressTypesFrom(
            final Rule rule) {
        HostAddressTypes hostAddressTypes = HostAddressTypes.of(
                rule.getRuleActionValues(
                        SocksRuleActionSpecConstants.SOCKS_ON_REQUEST_CLIENT_ENDPOINTS_BIND_HOST_ADDRESS_TYPE));
        if (!hostAddressTypes.toList().isEmpty()) {
            return hostAddressTypes;
        }
        return GeneralValueDerivationHelper.getClientEndpointsBindHostAddressTypesFrom(
                rule);
    }

    public static HostAddressTypes getSocksOnRequestClientEndpointsBindHostAddressTypesFrom(
            final Settings settings) {
        HostAddressTypes hostAddressTypes = settings.getLastValue(
                SocksSettingSpecConstants.SOCKS_ON_REQUEST_CLIENT_ENDPOINTS_BIND_HOST_ADDRESS_TYPES);
        if (!hostAddressTypes.toList().isEmpty()) {
            return hostAddressTypes;
        }
        return GeneralValueDerivationHelper.getClientEndpointsBindHostAddressTypesFrom(
                settings);
    }

    public static Host getSocksOnRequestClientEndpointsBindHostFrom(
            final Rule rule) {
        Host host = rule.getLastRuleActionValue(
                SocksRuleActionSpecConstants.SOCKS_ON_REQUEST_CLIENT_ENDPOINTS_BIND_HOST);
        if (host != null) {
            return host;
        }
        return GeneralValueDerivationHelper.getClientEndpointsBindHostFrom(rule);
    }

    public static Host getSocksOnRequestClientEndpointsBindHostFrom(
            final Settings settings) {
        Host host = settings.getLastValue(
                SocksSettingSpecConstants.SOCKS_ON_REQUEST_CLIENT_ENDPOINTS_BIND_HOST);
        if (host != null) {
            return host;
        }
        return GeneralValueDerivationHelper.getClientEndpointsBindHostFrom(
                settings);
    }

    public static PortRanges getSocksOnRequestClientEndpointsBindUdpPortRangesFrom(
            final Rule rule) {
        PortRanges portRanges = PortRanges.of(rule.getRuleActionValues(
                SocksRuleActionSpecConstants.SOCKS_ON_REQUEST_CLIENT_ENDPOINTS_BIND_UDP_PORT_RANGE));
        if (!portRanges.toList().isEmpty()) {
            return portRanges;
        }
        return GeneralValueDerivationHelper.getClientEndpointsBindUdpPortRangesFrom(
                rule);
    }

    public static PortRanges getSocksOnRequestClientEndpointsBindUdpPortRangesFrom(
            final Settings settings) {
        PortRanges portRanges = settings.getLastValue(
                SocksSettingSpecConstants.SOCKS_ON_REQUEST_CLIENT_ENDPOINTS_BIND_UDP_PORT_RANGES);
        if (!portRanges.toList().isEmpty()) {
            return portRanges;
        }
        return GeneralValueDerivationHelper.getClientEndpointsBindUdpPortRangesFrom(
                settings);
    }

    public static NetInterface getSocksOnRequestClientEndpointsNetInterfaceFrom(
            final Rule rule) {
        NetInterface netInterface = rule.getLastRuleActionValue(
                SocksRuleActionSpecConstants.SOCKS_ON_REQUEST_CLIENT_ENDPOINTS_NET_INTERFACE);
        if (netInterface != null) {
            return netInterface;
        }
        return GeneralValueDerivationHelper.getClientEndpointsNetInterfaceFrom(
                rule);
    }

    public static NetInterface getSocksOnRequestClientEndpointsNetInterfaceFrom(
            final Settings settings) {
        NetInterface netInterface = settings.getLastValue(
                SocksSettingSpecConstants.SOCKS_ON_REQUEST_CLIENT_ENDPOINTS_NET_INTERFACE);
        if (netInterface != null) {
            return netInterface;
        }
        return GeneralValueDerivationHelper.getClientEndpointsNetInterfaceFrom(
                settings);
    }

    public static SocketSettings getSocksOnRequestClientEndpointsSocketSettingsFrom(
            final Rule rule) {
        SocketSettings socketSettings = SocketSettings.of(new ArrayList<>(
                rule.getRuleActionValues(
                        SocksRuleActionSpecConstants.SOCKS_ON_REQUEST_CLIENT_ENDPOINTS_SOCKET_SETTING)));
        if (!socketSettings.toMap().isEmpty()) {
            return socketSettings;
        }
        return GeneralValueDerivationHelper.getClientEndpointsSocketSettingsFrom(
                rule);
    }

    public static SocketSettings getSocksOnRequestClientEndpointsSocketSettingsFrom(
            final Settings settings) {
        SocketSettings socketSettings = settings.getLastValue(
                SocksSettingSpecConstants.SOCKS_ON_REQUEST_CLIENT_ENDPOINTS_SOCKET_SETTINGS);
        if (!socketSettings.toMap().isEmpty()) {
            return socketSettings;
        }
        return GeneralValueDerivationHelper.getClientEndpointsSocketSettingsFrom(
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

    public static HostAddressTypes getSocksOnRequestRemoteEndpointsBindHostAddressTypesFrom(
            final Rule rule) {
        HostAddressTypes hostAddressTypes = HostAddressTypes.of(
                rule.getRuleActionValues(
                        SocksRuleActionSpecConstants.SOCKS_ON_REQUEST_REMOTE_ENDPOINTS_BIND_HOST_ADDRESS_TYPE));
        if (!hostAddressTypes.toList().isEmpty()) {
            return hostAddressTypes;
        }
        return GeneralValueDerivationHelper.getRemoteEndpointsBindHostAddressTypesFrom(
                rule);
    }

    public static HostAddressTypes getSocksOnRequestRemoteEndpointsBindHostAddressTypesFrom(
            final Settings settings) {
        HostAddressTypes hostAddressTypes = settings.getLastValue(
                SocksSettingSpecConstants.SOCKS_ON_REQUEST_REMOTE_ENDPOINTS_BIND_HOST_ADDRESS_TYPES);
        if (!hostAddressTypes.toList().isEmpty()) {
            return hostAddressTypes;
        }
        return GeneralValueDerivationHelper.getRemoteEndpointsBindHostAddressTypesFrom(
                settings);
    }

    public static Host getSocksOnRequestRemoteEndpointsBindHostFrom(
            final Rule rule) {
        Host host = rule.getLastRuleActionValue(
                SocksRuleActionSpecConstants.SOCKS_ON_REQUEST_REMOTE_ENDPOINTS_BIND_HOST);
        if (host != null) {
            return host;
        }
        return GeneralValueDerivationHelper.getRemoteEndpointsBindHostFrom(rule);
    }

    public static Host getSocksOnRequestRemoteEndpointsBindHostFrom(
            final Settings settings) {
        Host host = settings.getLastValue(
                SocksSettingSpecConstants.SOCKS_ON_REQUEST_REMOTE_ENDPOINTS_BIND_HOST);
        if (host != null) {
            return host;
        }
        return GeneralValueDerivationHelper.getRemoteEndpointsBindHostFrom(
                settings);
    }

    public static PortRanges getSocksOnRequestRemoteEndpointsBindTcpPortRangesFrom(
            final Rule rule) {
        PortRanges portRanges = PortRanges.of(rule.getRuleActionValues(
                SocksRuleActionSpecConstants.SOCKS_ON_REQUEST_REMOTE_ENDPOINTS_BIND_TCP_PORT_RANGE));
        if (!portRanges.toList().isEmpty()) {
            return portRanges;
        }
        return GeneralValueDerivationHelper.getRemoteEndpointsBindTcpPortRangesFrom(
                rule);
    }

    public static PortRanges getSocksOnRequestRemoteEndpointsBindTcpPortRangesFrom(
            final Settings settings) {
        PortRanges portRanges = settings.getLastValue(
                SocksSettingSpecConstants.SOCKS_ON_REQUEST_REMOTE_ENDPOINTS_BIND_TCP_PORT_RANGES);
        if (!portRanges.toList().isEmpty()) {
            return portRanges;
        }
        return GeneralValueDerivationHelper.getRemoteEndpointsBindTcpPortRangesFrom(
                settings);
    }

    public static PortRanges getSocksOnRequestRemoteEndpointsBindUdpPortRangesFrom(
            final Rule rule) {
        PortRanges portRanges = PortRanges.of(rule.getRuleActionValues(
                SocksRuleActionSpecConstants.SOCKS_ON_REQUEST_REMOTE_ENDPOINTS_BIND_UDP_PORT_RANGE));
        if (!portRanges.toList().isEmpty()) {
            return portRanges;
        }
        return GeneralValueDerivationHelper.getRemoteEndpointsBindUdpPortRangesFrom(
                rule);
    }

    public static PortRanges getSocksOnRequestRemoteEndpointsBindUdpPortRangesFrom(
            final Settings settings) {
        PortRanges portRanges = settings.getLastValue(
                SocksSettingSpecConstants.SOCKS_ON_REQUEST_REMOTE_ENDPOINTS_BIND_UDP_PORT_RANGES);
        if (!portRanges.toList().isEmpty()) {
            return portRanges;
        }
        return GeneralValueDerivationHelper.getRemoteEndpointsBindUdpPortRangesFrom(
                settings);
    }

    public static NetInterface getSocksOnRequestRemoteEndpointsNetInterfaceFrom(
            final Rule rule) {
        NetInterface netInterface = rule.getLastRuleActionValue(
                SocksRuleActionSpecConstants.SOCKS_ON_REQUEST_REMOTE_ENDPOINTS_NET_INTERFACE);
        if (netInterface != null) {
            return netInterface;
        }
        return GeneralValueDerivationHelper.getRemoteEndpointsNetInterfaceFrom(
                rule);
    }

    public static NetInterface getSocksOnRequestRemoteEndpointsNetInterfaceFrom(
            final Settings settings) {
        NetInterface netInterface = settings.getLastValue(
                SocksSettingSpecConstants.SOCKS_ON_REQUEST_REMOTE_ENDPOINTS_NET_INTERFACE);
        if (netInterface != null) {
            return netInterface;
        }
        return GeneralValueDerivationHelper.getRemoteEndpointsNetInterfaceFrom(
                settings);
    }

    public static SocketSettings getSocksOnRequestRemoteEndpointsSocketSettingsFrom(
            final Rule rule) {
        SocketSettings socketSettings = SocketSettings.of(new ArrayList<>(
                rule.getRuleActionValues(
                        SocksRuleActionSpecConstants.SOCKS_ON_REQUEST_REMOTE_ENDPOINTS_SOCKET_SETTING)));
        if (!socketSettings.toMap().isEmpty()) {
            return socketSettings;
        }
        return GeneralValueDerivationHelper.getRemoteEndpointsSocketSettingsFrom(
                rule);
    }

    public static SocketSettings getSocksOnRequestRemoteEndpointsSocketSettingsFrom(
            final Settings settings) {
        SocketSettings socketSettings = settings.getLastValue(
                SocksSettingSpecConstants.SOCKS_ON_REQUEST_REMOTE_ENDPOINTS_SOCKET_SETTINGS);
        if (!socketSettings.toMap().isEmpty()) {
            return socketSettings;
        }
        return GeneralValueDerivationHelper.getRemoteEndpointsSocketSettingsFrom(
                settings);
    }

    public static HostAddressTypes getSocksOnUdpAssociateRequestClientFacingDatagramSocketBindHostAddressTypesFrom(
            final Rule rule) {
        HostAddressTypes hostAddressTypes = HostAddressTypes.of(
                rule.getRuleActionValues(
                        SocksRuleActionSpecConstants.SOCKS_ON_UDP_ASSOCIATE_REQUEST_CLIENT_FACING_DATAGRAM_SOCKET_BIND_HOST_ADDRESS_TYPE));
        if (!hostAddressTypes.toList().isEmpty()) {
            return hostAddressTypes;
        }
        return getSocksOnRequestClientEndpointsBindHostAddressTypesFrom(rule);
    }

    public static HostAddressTypes getSocksOnUdpAssociateRequestClientFacingDatagramSocketBindHostAddressTypesFrom(
            final Settings settings) {
        HostAddressTypes hostAddressTypes = settings.getLastValue(
                SocksSettingSpecConstants.SOCKS_ON_UDP_ASSOCIATE_REQUEST_CLIENT_FACING_DATAGRAM_SOCKET_BIND_HOST_ADDRESS_TYPES);
        if (!hostAddressTypes.toList().isEmpty()) {
            return hostAddressTypes;
        }
        return getSocksOnRequestClientEndpointsBindHostAddressTypesFrom(
                settings);
    }

    public static Host getSocksOnUdpAssociateRequestClientFacingDatagramSocketBindHostFrom(
            final Rule rule) {
        Host host = rule.getLastRuleActionValue(
                SocksRuleActionSpecConstants.SOCKS_ON_UDP_ASSOCIATE_REQUEST_CLIENT_FACING_DATAGRAM_SOCKET_BIND_HOST);
        if (host != null) {
            return host;
        }
        return getSocksOnRequestClientEndpointsBindHostFrom(rule);
    }

    public static Host getSocksOnUdpAssociateRequestClientFacingDatagramSocketBindHostFrom(
            final Settings settings) {
        Host host = settings.getLastValue(
                SocksSettingSpecConstants.SOCKS_ON_UDP_ASSOCIATE_REQUEST_CLIENT_FACING_DATAGRAM_SOCKET_BIND_HOST);
        if (host != null) {
            return host;
        }
        return getSocksOnRequestClientEndpointsBindHostFrom(settings);
    }

    public static PortRanges getSocksOnUdpAssociateRequestClientFacingDatagramSocketBindPortRangesFrom(
            final Rule rule) {
        PortRanges portRanges = PortRanges.of(rule.getRuleActionValues(
                SocksRuleActionSpecConstants.SOCKS_ON_UDP_ASSOCIATE_REQUEST_CLIENT_FACING_DATAGRAM_SOCKET_BIND_PORT_RANGE));
        if (!portRanges.toList().isEmpty()) {
            return portRanges;
        }
        return getSocksOnRequestClientEndpointsBindUdpPortRangesFrom(rule);
    }

    public static PortRanges getSocksOnUdpAssociateRequestClientFacingDatagramSocketBindPortRangesFrom(
            final Settings settings) {
        PortRanges portRanges = settings.getLastValue(
                SocksSettingSpecConstants.SOCKS_ON_UDP_ASSOCIATE_REQUEST_CLIENT_FACING_DATAGRAM_SOCKET_BIND_PORT_RANGES);
        if (!portRanges.toList().isEmpty()) {
            return portRanges;
        }
        return getSocksOnRequestClientEndpointsBindUdpPortRangesFrom(settings);
    }

    public static NetInterface getSocksOnUdpAssociateRequestClientFacingDatagramSocketNetInterfaceFrom(
            final Rule rule) {
        NetInterface netInterface = rule.getLastRuleActionValue(
                SocksRuleActionSpecConstants.SOCKS_ON_UDP_ASSOCIATE_REQUEST_CLIENT_FACING_DATAGRAM_SOCKET_NET_INTERFACE);
        if (netInterface != null) {
            return netInterface;
        }
        return getSocksOnRequestClientEndpointsNetInterfaceFrom(rule);
    }

    public static NetInterface getSocksOnUdpAssociateRequestClientFacingDatagramSocketNetInterfaceFrom(
            final Settings settings) {
        NetInterface netInterface = settings.getLastValue(
                SocksSettingSpecConstants.SOCKS_ON_UDP_ASSOCIATE_REQUEST_CLIENT_FACING_DATAGRAM_SOCKET_NET_INTERFACE);
        if (netInterface != null) {
            return netInterface;
        }
        return getSocksOnRequestClientEndpointsNetInterfaceFrom(settings);
    }

    public static SocketSettings getSocksOnUdpAssociateRequestClientFacingDatagramSocketSocketSettingsFrom(
            final Rule rule) {
        SocketSettings socketSettings = SocketSettings.of(new ArrayList<>(
                rule.getRuleActionValues(
                        SocksRuleActionSpecConstants.SOCKS_ON_UDP_ASSOCIATE_REQUEST_CLIENT_FACING_DATAGRAM_SOCKET_SOCKET_SETTING)));
        if (!socketSettings.toMap().isEmpty()) {
            return socketSettings;
        }
        return getSocksOnRequestClientEndpointsSocketSettingsFrom(rule);
    }

    public static SocketSettings getSocksOnUdpAssociateRequestClientFacingDatagramSocketSocketSettingsFrom(
            final Settings settings) {
        SocketSettings socketSettings = settings.getLastValue(
                SocksSettingSpecConstants.SOCKS_ON_UDP_ASSOCIATE_REQUEST_CLIENT_FACING_DATAGRAM_SOCKET_SOCKET_SETTINGS);
        if (!socketSettings.toMap().isEmpty()) {
            return socketSettings;
        }
        return getSocksOnRequestClientEndpointsSocketSettingsFrom(settings);
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

    public static HostAddressTypes getSocksOnUdpAssociateRequestRemoteFacingDatagramSocketBindHostAddressTypesFrom(
            final Rule rule) {
        HostAddressTypes hostAddressTypes = HostAddressTypes.of(
                rule.getRuleActionValues(
                        SocksRuleActionSpecConstants.SOCKS_ON_UDP_ASSOCIATE_REQUEST_REMOTE_FACING_DATAGRAM_SOCKET_BIND_HOST_ADDRESS_TYPE));
        if (!hostAddressTypes.toList().isEmpty()) {
            return hostAddressTypes;
        }
        return getSocksOnRequestRemoteEndpointsBindHostAddressTypesFrom(rule);
    }

    public static HostAddressTypes getSocksOnUdpAssociateRequestRemoteFacingDatagramSocketBindHostAddressTypesFrom(
            final Settings settings) {
        HostAddressTypes hostAddressTypes = settings.getLastValue(
                SocksSettingSpecConstants.SOCKS_ON_UDP_ASSOCIATE_REQUEST_REMOTE_FACING_DATAGRAM_SOCKET_BIND_HOST_ADDRESS_TYPES);
        if (!hostAddressTypes.toList().isEmpty()) {
            return hostAddressTypes;
        }
        return getSocksOnRequestRemoteEndpointsBindHostAddressTypesFrom(
                settings);
    }

    public static Host getSocksOnUdpAssociateRequestRemoteFacingDatagramSocketBindHostFrom(
            final Rule rule) {
        Host host = rule.getLastRuleActionValue(
                SocksRuleActionSpecConstants.SOCKS_ON_UDP_ASSOCIATE_REQUEST_REMOTE_FACING_DATAGRAM_SOCKET_BIND_HOST);
        if (host != null) {
            return host;
        }
        return getSocksOnRequestRemoteEndpointsBindHostFrom(rule);
    }

    public static Host getSocksOnUdpAssociateRequestRemoteFacingDatagramSocketBindHostFrom(
            final Settings settings) {
        Host host = settings.getLastValue(
                SocksSettingSpecConstants.SOCKS_ON_UDP_ASSOCIATE_REQUEST_REMOTE_FACING_DATAGRAM_SOCKET_BIND_HOST);
        if (host != null) {
            return host;
        }
        return getSocksOnRequestRemoteEndpointsBindHostFrom(settings);
    }

    public static PortRanges getSocksOnUdpAssociateRequestRemoteFacingDatagramSocketBindPortRangesFrom(
            final Rule rule) {
        PortRanges portRanges = PortRanges.of(rule.getRuleActionValues(
                SocksRuleActionSpecConstants.SOCKS_ON_UDP_ASSOCIATE_REQUEST_REMOTE_FACING_DATAGRAM_SOCKET_BIND_PORT_RANGE));
        if (!portRanges.toList().isEmpty()) {
            return portRanges;
        }
        return getSocksOnRequestRemoteEndpointsBindUdpPortRangesFrom(rule);
    }

    public static PortRanges getSocksOnUdpAssociateRequestRemoteFacingDatagramSocketBindPortRangesFrom(
            final Settings settings) {
        PortRanges portRanges = settings.getLastValue(
                SocksSettingSpecConstants.SOCKS_ON_UDP_ASSOCIATE_REQUEST_REMOTE_FACING_DATAGRAM_SOCKET_BIND_PORT_RANGES);
        if (!portRanges.toList().isEmpty()) {
            return portRanges;
        }
        return getSocksOnRequestRemoteEndpointsBindUdpPortRangesFrom(settings);
    }

    public static NetInterface getSocksOnUdpAssociateRequestRemoteFacingDatagramSocketNetInterfaceFrom(
            final Rule rule) {
        NetInterface netInterface = rule.getLastRuleActionValue(
                SocksRuleActionSpecConstants.SOCKS_ON_UDP_ASSOCIATE_REQUEST_REMOTE_FACING_DATAGRAM_SOCKET_NET_INTERFACE);
        if (netInterface != null) {
            return netInterface;
        }
        return getSocksOnRequestRemoteEndpointsNetInterfaceFrom(rule);
    }

    public static NetInterface getSocksOnUdpAssociateRequestRemoteFacingDatagramSocketNetInterfaceFrom(
            final Settings settings) {
        NetInterface netInterface = settings.getLastValue(
                SocksSettingSpecConstants.SOCKS_ON_UDP_ASSOCIATE_REQUEST_REMOTE_FACING_DATAGRAM_SOCKET_NET_INTERFACE);
        if (netInterface != null) {
            return netInterface;
        }
        return getSocksOnRequestRemoteEndpointsNetInterfaceFrom(settings);
    }

    public static SocketSettings getSocksOnUdpAssociateRequestRemoteFacingDatagramSocketSocketSettingsFrom(
            final Rule rule) {
        SocketSettings socketSettings = SocketSettings.of(new ArrayList<>(
                rule.getRuleActionValues(
                        SocksRuleActionSpecConstants.SOCKS_ON_UDP_ASSOCIATE_REQUEST_REMOTE_FACING_DATAGRAM_SOCKET_SOCKET_SETTING)));
        if (!socketSettings.toMap().isEmpty()) {
            return socketSettings;
        }
        return getSocksOnRequestRemoteEndpointsSocketSettingsFrom(rule);
    }

    public static SocketSettings getSocksOnUdpAssociateRequestRemoteFacingDatagramSocketSocketSettingsFrom(
            final Settings settings) {
        SocketSettings socketSettings = settings.getLastValue(
                SocksSettingSpecConstants.SOCKS_ON_UDP_ASSOCIATE_REQUEST_REMOTE_FACING_DATAGRAM_SOCKET_SOCKET_SETTINGS);
        if (!socketSettings.toMap().isEmpty()) {
            return socketSettings;
        }
        return getSocksOnRequestRemoteEndpointsSocketSettingsFrom(settings);
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
