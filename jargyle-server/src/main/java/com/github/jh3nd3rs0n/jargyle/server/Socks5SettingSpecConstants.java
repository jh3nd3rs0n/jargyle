package com.github.jh3nd3rs0n.jargyle.server;

import com.github.jh3nd3rs0n.jargyle.common.net.*;
import com.github.jh3nd3rs0n.jargyle.common.number.PositiveInteger;
import com.github.jh3nd3rs0n.jargyle.internal.annotation.NameValuePairValueSpecDoc;
import com.github.jh3nd3rs0n.jargyle.internal.annotation.NameValuePairValueSpecsDoc;
import com.github.jh3nd3rs0n.jargyle.protocolbase.socks5.Methods;
import com.github.jh3nd3rs0n.jargyle.protocolbase.socks5.gssapimethod.ProtectionLevels;
import com.github.jh3nd3rs0n.jargyle.server.internal.settingspec.impl.*;
import com.github.jh3nd3rs0n.jargyle.server.socks5.userpassmethod.UserRepository;

import java.util.List;
import java.util.Map;

@NameValuePairValueSpecsDoc(
		description = "",
		name = "SOCKS5 Settings"
)
public final class Socks5SettingSpecConstants {

	private static final SettingSpecs SETTING_SPECS = new SettingSpecs();
	
	@NameValuePairValueSpecDoc(
			description = "The boolean value to indicate if the exchange of "
					+ "the GSS-API protection level negotiation must be "
					+ "unprotected according to the NEC reference "
					+ "implementation (default is false)", 
			name = "socks5.gssapimethod.necReferenceImpl",
			syntax = "socks5.gssapimethod.necReferenceImpl=true|false",
			valueType = Boolean.class
	)
	public static final SettingSpec<Boolean> SOCKS5_GSSAPIMETHOD_NEC_REFERENCE_IMPL = 
			SETTING_SPECS.addThenGet(new BooleanSettingSpec(
					"socks5.gssapimethod.necReferenceImpl", 
					Boolean.FALSE));
	
	@NameValuePairValueSpecDoc(
			description = "The comma separated list of acceptable protection "
					+ "levels after GSS-API authentication (The first is "
					+ "preferred if the client does not provide a protection "
					+ "level that is acceptable.) (default is "
					+ "REQUIRED_INTEG_AND_CONF,REQUIRED_INTEG,NONE)",
			name = "socks5.gssapimethod.protectionLevels",
			syntax = "socks5.gssapimethod.protectionLevels=SOCKS5_GSSAPIMETHOD_PROTECTION_LEVELS",
			valueType = ProtectionLevels.class
	)
	public static final SettingSpec<ProtectionLevels> SOCKS5_GSSAPIMETHOD_PROTECTION_LEVELS = 
			SETTING_SPECS.addThenGet(new Socks5GssapiMethodProtectionLevelsSettingSpec(
					"socks5.gssapimethod.protectionLevels", 
					ProtectionLevels.getDefault()));

	@NameValuePairValueSpecDoc(
			description = "The suggested privacy (i.e. confidentiality) state "
					+ "for GSS-API messages sent after GSS-API authentication "
					+ "(applicable if the negotiated protection level is "
					+ "SELECTIVE_INTEG_OR_CONF) (default is true)",
			name = "socks5.gssapimethod.suggestedConf",
			syntax = "socks5.gssapimethod.suggestedConf=true|false",
			valueType = Boolean.class
	)
	public static final SettingSpec<Boolean> SOCKS5_GSSAPIMETHOD_SUGGESTED_CONF =
			SETTING_SPECS.addThenGet(new BooleanSettingSpec(
					"socks5.gssapimethod.suggestedConf",
					Boolean.TRUE));

	@NameValuePairValueSpecDoc(
			description = "The suggested quality-of-protection (i.e. "
					+ "integrity) value for GSS-API messages sent after "
					+ "GSS-API authentication (applicable if the negotiated "
					+ "protection level is SELECTIVE_INTEG_OR_CONF) (default "
					+ "is 0)",
			name = "socks5.gssapimethod.suggestedInteg",
			syntax = "socks5.gssapimethod.suggestedInteg=-2147483648-2147483647",
			valueType = Integer.class
	)
	public static final SettingSpec<Integer> SOCKS5_GSSAPIMETHOD_SUGGESTED_INTEG =
			SETTING_SPECS.addThenGet(new IntegerSettingSpec(
					"socks5.gssapimethod.suggestedInteg",
					Integer.valueOf(0)));

	@NameValuePairValueSpecDoc(
			description = "The comma separated list of acceptable "
					+ "authentication methods in order of preference (default "
					+ "is NO_AUTHENTICATION_REQUIRED)",
			name = "socks5.methods",
			syntax = "socks5.methods=SOCKS5_METHODS",
			valueType = Methods.class
	)
	public static final SettingSpec<Methods> SOCKS5_METHODS = 
			SETTING_SPECS.addThenGet(new Socks5MethodsSettingSpec(
					"socks5.methods", 
					Methods.getDefault()));
	
	@NameValuePairValueSpecDoc(
			description = "The comma separated list of socket settings for the "
					+ "inbound socket",
			name = "socks5.onBindRequest.inboundSocketSettings",
			syntax = "socks5.onBindRequest.inboundSocketSettings=SOCKET_SETTINGS",
			valueType = SocketSettings.class
	)
	public static final SettingSpec<SocketSettings> SOCKS5_ON_BIND_REQUEST_INBOUND_SOCKET_SETTINGS = 
			SETTING_SPECS.addThenGet(new SocketSettingsSettingSpec(
					"socks5.onBindRequest.inboundSocketSettings", 
					SocketSettings.of()));
	
	@NameValuePairValueSpecDoc(
			description = "The binding host name or address for the listen "
					+ "socket if the provided host address is all zeros",
			name = "socks5.onBindRequest.listenBindHost",
			syntax = "socks5.onBindRequest.listenBindHost=HOST",
			valueType = Host.class
	)
	public static final SettingSpec<Host> SOCKS5_ON_BIND_REQUEST_LISTEN_BIND_HOST =
			SETTING_SPECS.addThenGet(new HostSettingSpec(
					"socks5.onBindRequest.listenBindHost",
					null));

	@NameValuePairValueSpecDoc(
			description = "The comma separated list of acceptable binding " +
					"host address types for the listen socket if the "
					+ "provided host address is all zeros",
			name = "socks5.onBindRequest.listenBindHostAddressTypes",
			syntax = "socks5.onBindRequest.listenBindHostAddressTypes=HOST_ADDRESS_TYPES",
			valueType = HostAddressTypes.class
	)
	public static final SettingSpec<HostAddressTypes> SOCKS5_ON_BIND_REQUEST_LISTEN_BIND_HOST_ADDRESS_TYPES =
			SETTING_SPECS.addThenGet(new HostAddressTypesSettingSpec(
					"socks5.onBindRequest.listenBindHostAddressTypes",
					HostAddressTypes.of()));

	@NameValuePairValueSpecDoc(
			description = "The comma separated list of binding port ranges for "
					+ "the listen socket if the provided port is zero",
			name = "socks5.onBindRequest.listenBindPortRanges",
			syntax = "socks5.onBindRequest.listenBindPortRanges=PORT_RANGES",
			valueType = PortRanges.class
	)
	public static final SettingSpec<PortRanges> SOCKS5_ON_BIND_REQUEST_LISTEN_BIND_PORT_RANGES =
			SETTING_SPECS.addThenGet(new PortRangesSettingSpec(
					"socks5.onBindRequest.listenBindPortRanges",
					PortRanges.of()));

	@NameValuePairValueSpecDoc(
			description = "The network interface that provides a binding "
					+ "host address for the listen socket if the provided "
					+ "host address is all zeros",
			name = "socks5.onBindRequest.listenNetInterface",
			syntax = "socks5.onBindRequest.listenNetInterface=NETWORK_INTERFACE",
			valueType = NetInterface.class
	)
	public static final SettingSpec<NetInterface> SOCKS5_ON_BIND_REQUEST_LISTEN_NET_INTERFACE =
			SETTING_SPECS.addThenGet(new NetInterfaceSettingSpec(
					"socks5.onBindRequest.listenNetInterface",
					null));

	@NameValuePairValueSpecDoc(
			description = "The comma separated list of socket settings for the "
					+ "listen socket",
			name = "socks5.onBindRequest.listenSocketSettings",
			syntax = "socks5.onBindRequest.listenSocketSettings=SOCKET_SETTINGS",
			valueType = SocketSettings.class
	)
	public static final SettingSpec<SocketSettings> SOCKS5_ON_BIND_REQUEST_LISTEN_SOCKET_SETTINGS = 
			SETTING_SPECS.addThenGet(new SocketSettingsSettingSpec(
					"socks5.onBindRequest.listenSocketSettings", 
					SocketSettings.of()));
	
	@NameValuePairValueSpecDoc(
			description = "The buffer size in bytes for relaying the data",
			name = "socks5.onBindRequest.relayBufferSize",
			syntax = "socks5.onBindRequest.relayBufferSize=POSITIVE_INTEGER",
			valueType = PositiveInteger.class
	)
	public static final SettingSpec<PositiveInteger> SOCKS5_ON_BIND_REQUEST_RELAY_BUFFER_SIZE = 
			SETTING_SPECS.addThenGet(new PositiveIntegerSettingSpec(
					"socks5.onBindRequest.relayBufferSize", 
					null));
	
	@NameValuePairValueSpecDoc(
			description = "The timeout in milliseconds on relaying no data",
			name = "socks5.onBindRequest.relayIdleTimeout",
			syntax = "socks5.onBindRequest.relayIdleTimeout=POSITIVE_INTEGER",
			valueType = PositiveInteger.class
	)
	public static final SettingSpec<PositiveInteger> SOCKS5_ON_BIND_REQUEST_RELAY_IDLE_TIMEOUT = 
			SETTING_SPECS.addThenGet(new PositiveIntegerSettingSpec(
					"socks5.onBindRequest.relayIdleTimeout", 
					null));
	
	@NameValuePairValueSpecDoc(
			description = "The upper limit on bandwidth in bytes per second of "
					+ "receiving inbound data to be relayed",
			name = "socks5.onBindRequest.relayInboundBandwidthLimit",
			syntax = "socks5.onBindRequest.relayInboundBandwidthLimit=POSITIVE_INTEGER",
			valueType = PositiveInteger.class
	)
	public static final SettingSpec<PositiveInteger> SOCKS5_ON_BIND_REQUEST_RELAY_INBOUND_BANDWIDTH_LIMIT =
			SETTING_SPECS.addThenGet(new PositiveIntegerSettingSpec(
					"socks5.onBindRequest.relayInboundBandwidthLimit",
					null));
	
	@NameValuePairValueSpecDoc(
			description = "The upper limit on bandwidth in bytes per second of "
					+ "receiving outbound data to be relayed",
			name = "socks5.onBindRequest.relayOutboundBandwidthLimit",
			syntax = "socks5.onBindRequest.relayOutboundBandwidthLimit=POSITIVE_INTEGER",
			valueType = PositiveInteger.class
	)
	public static final SettingSpec<PositiveInteger> SOCKS5_ON_BIND_REQUEST_RELAY_OUTBOUND_BANDWIDTH_LIMIT =
			SETTING_SPECS.addThenGet(new PositiveIntegerSettingSpec(
					"socks5.onBindRequest.relayOutboundBandwidthLimit",
					null));

	@NameValuePairValueSpecDoc(
			description = "The boolean value to indicate if the target-facing "
					+ "socket is to be prepared before connecting (involves "
					+ "applying the specified socket settings, resolving the "
					+ "target host name, and setting the specified timeout on "
					+ "waiting to connect) (default is false)",
			name = "socks5.onConnectRequest.prepareTargetFacingSocket",
			syntax = "socks5.onConnectRequest.prepareTargetFacingSocket=true|false",
			valueType = Boolean.class
	)	
	public static final SettingSpec<Boolean> SOCKS5_ON_CONNECT_REQUEST_PREPARE_TARGET_FACING_SOCKET = 
			SETTING_SPECS.addThenGet(new BooleanSettingSpec(
					"socks5.onConnectRequest.prepareTargetFacingSocket", 
					Boolean.FALSE));
	
	@NameValuePairValueSpecDoc(
			description = "The buffer size in bytes for relaying the data",
			name = "socks5.onConnectRequest.relayBufferSize",
			syntax = "socks5.onConnectRequest.relayBufferSize=POSITIVE_INTEGER",
			valueType = PositiveInteger.class
	)
	public static final SettingSpec<PositiveInteger> SOCKS5_ON_CONNECT_REQUEST_RELAY_BUFFER_SIZE = 
			SETTING_SPECS.addThenGet(new PositiveIntegerSettingSpec(
					"socks5.onConnectRequest.relayBufferSize", 
					null));
	
	@NameValuePairValueSpecDoc(
			description = "The timeout in milliseconds on relaying no data",
			name = "socks5.onConnectRequest.relayIdleTimeout",
			syntax = "socks5.onConnectRequest.relayIdleTimeout=POSITIVE_INTEGER",
			valueType = PositiveInteger.class
	)
	public static final SettingSpec<PositiveInteger> SOCKS5_ON_CONNECT_REQUEST_RELAY_IDLE_TIMEOUT = 
			SETTING_SPECS.addThenGet(new PositiveIntegerSettingSpec(
					"socks5.onConnectRequest.relayIdleTimeout", 
					null));
	
	@NameValuePairValueSpecDoc(
			description = "The upper limit on bandwidth in bytes per second of "
					+ "receiving inbound data to be relayed",
			name = "socks5.onConnectRequest.relayInboundBandwidthLimit",
			syntax = "socks5.onConnectRequest.relayInboundBandwidthLimit=POSITIVE_INTEGER",
			valueType = PositiveInteger.class
	)
	public static final SettingSpec<PositiveInteger> SOCKS5_ON_CONNECT_REQUEST_RELAY_INBOUND_BANDWIDTH_LIMIT =
			SETTING_SPECS.addThenGet(new PositiveIntegerSettingSpec(
					"socks5.onConnectRequest.relayInboundBandwidthLimit",
					null));
	
	@NameValuePairValueSpecDoc(
			description = "The upper limit on bandwidth in bytes per second of "
					+ "receiving outbound data to be relayed",
			name = "socks5.onConnectRequest.relayOutboundBandwidthLimit",
			syntax = "socks5.onConnectRequest.relayOutboundBandwidthLimit=POSITIVE_INTEGER",
			valueType = PositiveInteger.class
	)
	public static final SettingSpec<PositiveInteger> SOCKS5_ON_CONNECT_REQUEST_RELAY_OUTBOUND_BANDWIDTH_LIMIT =
			SETTING_SPECS.addThenGet(new PositiveIntegerSettingSpec(
					"socks5.onConnectRequest.relayOutboundBandwidthLimit",
					null));
	
	@NameValuePairValueSpecDoc(
			description = "The binding host name or address for the "
					+ "target-facing socket",
			name = "socks5.onConnectRequest.targetFacingBindHost",
			syntax = "socks5.onConnectRequest.targetFacingBindHost=HOST",
			valueType = Host.class
	)
	public static final SettingSpec<Host> SOCKS5_ON_CONNECT_REQUEST_TARGET_FACING_BIND_HOST = 
			SETTING_SPECS.addThenGet(new HostSettingSpec(
					"socks5.onConnectRequest.targetFacingBindHost", 
					null));

	@NameValuePairValueSpecDoc(
			description = "The comma separated list of acceptable binding "
					+ "host address types for the target-facing socket",
			name = "socks5.onConnectRequest.targetFacingBindHostAddressTypes",
			syntax = "socks5.onConnectRequest.targetFacingBindHostAddressTypes=HOST_ADDRESS_TYPES",
			valueType = HostAddressTypes.class
	)
	public static final SettingSpec<HostAddressTypes> SOCKS5_ON_CONNECT_REQUEST_TARGET_FACING_BIND_HOST_ADDRESS_TYPES =
			SETTING_SPECS.addThenGet(new HostAddressTypesSettingSpec(
					"socks5.onConnectRequest.targetFacingBindHostAddressTypes",
					HostAddressTypes.of()));

	@NameValuePairValueSpecDoc(
			description = "The comma separated list of binding port ranges for "
					+ "the target-facing socket",
			name = "socks5.onConnectRequest.targetFacingBindPortRanges",
			syntax = "socks5.onConnectRequest.targetFacingBindPortRanges=PORT_RANGES",
			valueType = PortRanges.class
	)	
	public static final SettingSpec<PortRanges> SOCKS5_ON_CONNECT_REQUEST_TARGET_FACING_BIND_PORT_RANGES =
			SETTING_SPECS.addThenGet(new PortRangesSettingSpec(
					"socks5.onConnectRequest.targetFacingBindPortRanges",
					PortRanges.of()));
	
	@NameValuePairValueSpecDoc(
			description = "The timeout in milliseconds on waiting for the "
					+ "target-facing socket to connect (default is 60000)",
			name = "socks5.onConnectRequest.targetFacingConnectTimeout",
			syntax = "socks5.onConnectRequest.targetFacingConnectTimeout=POSITIVE_INTEGER",
			valueType = PositiveInteger.class
	)
	public static final SettingSpec<PositiveInteger> SOCKS5_ON_CONNECT_REQUEST_TARGET_FACING_CONNECT_TIMEOUT = 
			SETTING_SPECS.addThenGet(new PositiveIntegerSettingSpec(
					"socks5.onConnectRequest.targetFacingConnectTimeout", 
					PositiveInteger.valueOf(60000)));

	@NameValuePairValueSpecDoc(
			description = "The network interface that provides a binding "
					+ "host address for the target-facing socket",
			name = "socks5.onConnectRequest.targetFacingNetInterface",
			syntax = "socks5.onConnectRequest.targetFacingNetInterface=NETWORK_INTERFACE",
			valueType = NetInterface.class
	)
	public static final SettingSpec<NetInterface> SOCKS5_ON_CONNECT_REQUEST_TARGET_FACING_NET_INTERFACE =
			SETTING_SPECS.addThenGet(new NetInterfaceSettingSpec(
					"socks5.onConnectRequest.targetFacingNetInterface",
					null));

	@NameValuePairValueSpecDoc(
			description = "The comma separated list of socket settings for the "
					+ "target-facing socket",
			name = "socks5.onConnectRequest.targetFacingSocketSettings",
			syntax = "socks5.onConnectRequest.targetFacingSocketSettings=SOCKET_SETTINGS",
			valueType = SocketSettings.class
	)
	public static final SettingSpec<SocketSettings> SOCKS5_ON_CONNECT_REQUEST_TARGET_FACING_SOCKET_SETTINGS = 
			SETTING_SPECS.addThenGet(new SocketSettingsSettingSpec(
					"socks5.onConnectRequest.targetFacingSocketSettings", 
					SocketSettings.of()));

	@NameValuePairValueSpecDoc(
			description = "The binding host name or address for all "
					+ "external-facing sockets",
			name = "socks5.onRequest.externalFacingBindHost",
			syntax = "socks5.onRequest.externalFacingBindHost=HOST",
			valueType = Host.class
	)
	public static final SettingSpec<Host> SOCKS5_ON_REQUEST_EXTERNAL_FACING_BIND_HOST =
			SETTING_SPECS.addThenGet(new HostSettingSpec(
					"socks5.onRequest.externalFacingBindHost",
					null));

	@NameValuePairValueSpecDoc(
			description = "The comma separated list of acceptable binding "
					+ "host address types for all external-facing sockets",
			name = "socks5.onRequest.externalFacingBindHostAddressTypes",
			syntax = "socks5.onRequest.externalFacingBindHostAddressTypes=HOST_ADDRESS_TYPES",
			valueType = HostAddressTypes.class
	)
	public static final SettingSpec<HostAddressTypes> SOCKS5_ON_REQUEST_EXTERNAL_FACING_BIND_HOST_ADDRESS_TYPES =
			SETTING_SPECS.addThenGet(new HostAddressTypesSettingSpec(
					"socks5.onRequest.externalFacingBindHostAddressTypes",
					HostAddressTypes.of()));

	@NameValuePairValueSpecDoc(
			description = "The comma separated list of binding port ranges for "
					+ "all external-facing TCP sockets",
			name = "socks5.onRequest.externalFacingBindTcpPortRanges",
			syntax = "socks5.onRequest.externalFacingBindTcpPortRanges=PORT_RANGES",
			valueType = PortRanges.class
	)
	public static final SettingSpec<PortRanges> SOCKS5_ON_REQUEST_EXTERNAL_FACING_BIND_TCP_PORT_RANGES =
			SETTING_SPECS.addThenGet(new PortRangesSettingSpec(
					"socks5.onRequest.externalFacingBindTcpPortRanges",
					PortRanges.of()));

	@NameValuePairValueSpecDoc(
			description = "The comma separated list of binding port ranges for "
					+ "all external-facing UDP sockets",
			name = "socks5.onRequest.externalFacingBindUdpPortRanges",
			syntax = "socks5.onRequest.externalFacingBindUdpPortRanges=PORT_RANGES",
			valueType = PortRanges.class
	)
	public static final SettingSpec<PortRanges> SOCKS5_ON_REQUEST_EXTERNAL_FACING_BIND_UDP_PORT_RANGES =
			SETTING_SPECS.addThenGet(new PortRangesSettingSpec(
					"socks5.onRequest.externalFacingBindUdpPortRanges",
					PortRanges.of()));

	@NameValuePairValueSpecDoc(
			description = "The network interface that provides a binding host "
					+ "address for all external-facing sockets",
			name = "socks5.onRequest.externalFacingNetInterface",
			syntax = "socks5.onRequest.externalFacingNetInterface=NETWORK_INTERFACE",
			valueType = NetInterface.class
	)
	public static final SettingSpec<NetInterface> SOCKS5_ON_REQUEST_EXTERNAL_FACING_NET_INTERFACE =
			SETTING_SPECS.addThenGet(new NetInterfaceSettingSpec(
					"socks5.onRequest.externalFacingNetInterface",
					null));

	@NameValuePairValueSpecDoc(
			description = "The comma separated list of socket settings for all "
					+ "external-facing sockets",
			name = "socks5.onRequest.externalFacingSocketSettings",
			syntax = "socks5.onRequest.externalFacingSocketSettings=SOCKET_SETTINGS",
			valueType = SocketSettings.class
	)
	public static final SettingSpec<SocketSettings> SOCKS5_ON_REQUEST_EXTERNAL_FACING_SOCKET_SETTINGS =
			SETTING_SPECS.addThenGet(new SocketSettingsSettingSpec(
					"socks5.onRequest.externalFacingSocketSettings",
					SocketSettings.of()));

	@NameValuePairValueSpecDoc(
			description = "The binding host name or address for all "
					+ "internal-facing sockets",
			name = "socks5.onRequest.internalFacingBindHost",
			syntax = "socks5.onRequest.internalFacingBindHost=HOST",
			valueType = Host.class
	)
	public static final SettingSpec<Host> SOCKS5_ON_REQUEST_INTERNAL_FACING_BIND_HOST =
			SETTING_SPECS.addThenGet(new HostSettingSpec(
					"socks5.onRequest.internalFacingBindHost",
					null));

	@NameValuePairValueSpecDoc(
			description = "The comma separated list of acceptable binding "
					+ "host address types for all internal-facing sockets",
			name = "socks5.onRequest.internalFacingBindHostAddressTypes",
			syntax = "socks5.onRequest.internalFacingBindHostAddressTypes=HOST_ADDRESS_TYPES",
			valueType = HostAddressTypes.class
	)
	public static final SettingSpec<HostAddressTypes> SOCKS5_ON_REQUEST_INTERNAL_FACING_BIND_HOST_ADDRESS_TYPES =
			SETTING_SPECS.addThenGet(new HostAddressTypesSettingSpec(
					"socks5.onRequest.internalFacingBindHostAddressTypes",
					HostAddressTypes.of()));

	@NameValuePairValueSpecDoc(
			description = "The comma separated list of binding port ranges for "
					+ "all internal-facing UDP sockets",
			name = "socks5.onRequest.internalFacingBindUdpPortRanges",
			syntax = "socks5.onRequest.internalFacingBindUdpPortRanges=PORT_RANGES",
			valueType = PortRanges.class
	)
	public static final SettingSpec<PortRanges> SOCKS5_ON_REQUEST_INTERNAL_FACING_BIND_UDP_PORT_RANGES =
			SETTING_SPECS.addThenGet(new PortRangesSettingSpec(
					"socks5.onRequest.internalFacingBindUdpPortRanges",
					PortRanges.of()));

	@NameValuePairValueSpecDoc(
			description = "The network interface that provides a binding host "
					+ "address for all internal-facing sockets",
			name = "socks5.onRequest.internalFacingNetInterface",
			syntax = "socks5.onRequest.internalFacingNetInterface=NETWORK_INTERFACE",
			valueType = NetInterface.class
	)
	public static final SettingSpec<NetInterface> SOCKS5_ON_REQUEST_INTERNAL_FACING_NET_INTERFACE =
			SETTING_SPECS.addThenGet(new NetInterfaceSettingSpec(
					"socks5.onRequest.internalFacingNetInterface",
					null));

	@NameValuePairValueSpecDoc(
			description = "The comma separated list of socket settings for all "
					+ "internal-facing sockets",
			name = "socks5.onRequest.internalFacingSocketSettings",
			syntax = "socks5.onRequest.internalFacingSocketSettings=SOCKET_SETTINGS",
			valueType = SocketSettings.class
	)
	public static final SettingSpec<SocketSettings> SOCKS5_ON_REQUEST_INTERNAL_FACING_SOCKET_SETTINGS =
			SETTING_SPECS.addThenGet(new SocketSettingsSettingSpec(
					"socks5.onRequest.internalFacingSocketSettings",
					SocketSettings.of()));

	@NameValuePairValueSpecDoc(
			description = "The buffer size in bytes for relaying the data "
					+ "(default is 1024)",
			name = "socks5.onRequest.relayBufferSize",
			syntax = "socks5.onRequest.relayBufferSize=POSITIVE_INTEGER",
			valueType = PositiveInteger.class
	)
	public static final SettingSpec<PositiveInteger> SOCKS5_ON_REQUEST_RELAY_BUFFER_SIZE =
			SETTING_SPECS.addThenGet(new PositiveIntegerSettingSpec(
					"socks5.onRequest.relayBufferSize",
					PositiveInteger.valueOf(1024)));

	@NameValuePairValueSpecDoc(
			description = "The timeout in milliseconds on relaying no data "
					+ "(default is 60000)",
			name = "socks5.onRequest.relayIdleTimeout",
			syntax = "socks5.onRequest.relayIdleTimeout=POSITIVE_INTEGER",
			valueType = PositiveInteger.class
	)
	public static final SettingSpec<PositiveInteger> SOCKS5_ON_REQUEST_RELAY_IDLE_TIMEOUT =
			SETTING_SPECS.addThenGet(new PositiveIntegerSettingSpec(
					"socks5.onRequest.relayIdleTimeout",
					PositiveInteger.valueOf(60000)));

	@NameValuePairValueSpecDoc(
			description = "The upper limit on bandwidth in bytes per second of "
					+ "receiving inbound data to be relayed",
			name = "socks5.onRequest.relayInboundBandwidthLimit",
			syntax = "socks5.onRequest.relayInboundBandwidthLimit=POSITIVE_INTEGER",
			valueType = PositiveInteger.class
	)
	public static final SettingSpec<PositiveInteger> SOCKS5_ON_REQUEST_RELAY_INBOUND_BANDWIDTH_LIMIT =
			SETTING_SPECS.addThenGet(new PositiveIntegerSettingSpec(
					"socks5.onRequest.relayInboundBandwidthLimit",
					null));

	@NameValuePairValueSpecDoc(
			description = "The upper limit on bandwidth in bytes per second of "
					+ "receiving outbound data to be relayed",
			name = "socks5.onRequest.relayOutboundBandwidthLimit",
			syntax = "socks5.onRequest.relayOutboundBandwidthLimit=POSITIVE_INTEGER",
			valueType = PositiveInteger.class
	)
	public static final SettingSpec<PositiveInteger> SOCKS5_ON_REQUEST_RELAY_OUTBOUND_BANDWIDTH_LIMIT =
			SETTING_SPECS.addThenGet(new PositiveIntegerSettingSpec(
					"socks5.onRequest.relayOutboundBandwidthLimit",
					null));

	@NameValuePairValueSpecDoc(
			description = "The binding host name or address for the "
					+ "client-facing UDP socket",
			name = "socks5.onUdpAssociateRequest.clientFacingBindHost",
			syntax = "socks5.onUdpAssociateRequest.clientFacingBindHost=HOST",
			valueType = Host.class
	)
	public static final SettingSpec<Host> SOCKS5_ON_UDP_ASSOCIATE_REQUEST_CLIENT_FACING_BIND_HOST = 
			SETTING_SPECS.addThenGet(new HostSettingSpec(
					"socks5.onUdpAssociateRequest.clientFacingBindHost", 
					null));

	@NameValuePairValueSpecDoc(
			description = "The comma separated list of acceptable binding "
					+ "host address types for the client-facing UDP socket",
			name = "socks5.onUdpAssociateRequest.clientFacingBindHostAddressTypes",
			syntax = "socks5.onUdpAssociateRequest.clientFacingBindHostAddressTypes=HOST_ADDRESS_TYPES",
			valueType = HostAddressTypes.class
	)
	public static final SettingSpec<HostAddressTypes> SOCKS5_ON_UDP_ASSOCIATE_REQUEST_CLIENT_FACING_BIND_HOST_ADDRESS_TYPES =
			SETTING_SPECS.addThenGet(new HostAddressTypesSettingSpec(
					"socks5.onUdpAssociateRequest.clientFacingBindHostAddressTypes",
					HostAddressTypes.of()));

	@NameValuePairValueSpecDoc(
			description = "The comma separated list of binding port ranges for "
					+ "the client-facing UDP socket",
			name = "socks5.onUdpAssociateRequest.clientFacingBindPortRanges",
			syntax = "socks5.onUdpAssociateRequest.clientFacingBindPortRanges=PORT_RANGES",
			valueType = PortRanges.class
	)	
	public static final SettingSpec<PortRanges> SOCKS5_ON_UDP_ASSOCIATE_REQUEST_CLIENT_FACING_BIND_PORT_RANGES =
			SETTING_SPECS.addThenGet(new PortRangesSettingSpec(
					"socks5.onUdpAssociateRequest.clientFacingBindPortRanges",
					PortRanges.of()));

	@NameValuePairValueSpecDoc(
			description = "The network interface that provides a binding host "
					+ "address for the client-facing UDP socket",
			name = "socks5.onUdpAssociateRequest.clientFacingNetInterface",
			syntax = "socks5.onUdpAssociateRequest.clientFacingNetInterface=NETWORK_INTERFACE",
			valueType = NetInterface.class
	)
	public static final SettingSpec<NetInterface> SOCKS5_ON_UDP_ASSOCIATE_REQUEST_CLIENT_FACING_NET_INTERFACE =
			SETTING_SPECS.addThenGet(new NetInterfaceSettingSpec(
					"socks5.onUdpAssociateRequest.clientFacingNetInterface",
					null));

	@NameValuePairValueSpecDoc(
			description = "The comma separated list of socket settings for the "
					+ "client-facing UDP socket",
			name = "socks5.onUdpAssociateRequest.clientFacingSocketSettings",
			syntax = "socks5.onUdpAssociateRequest.clientFacingSocketSettings=SOCKET_SETTINGS",
			valueType = SocketSettings.class
	)
	public static final SettingSpec<SocketSettings> SOCKS5_ON_UDP_ASSOCIATE_REQUEST_CLIENT_FACING_SOCKET_SETTINGS = 
			SETTING_SPECS.addThenGet(new SocketSettingsSettingSpec(
					"socks5.onUdpAssociateRequest.clientFacingSocketSettings", 
					SocketSettings.of()));

	@NameValuePairValueSpecDoc(
			description = "The binding host name or address for the peer-facing "
					+ "UDP socket",
			name = "socks5.onUdpAssociateRequest.peerFacingBindHost",
			syntax = "socks5.onUdpAssociateRequest.peerFacingBindHost=HOST",
			valueType = Host.class
	)
	public static final SettingSpec<Host> SOCKS5_ON_UDP_ASSOCIATE_REQUEST_PEER_FACING_BIND_HOST = 
			SETTING_SPECS.addThenGet(new HostSettingSpec(
					"socks5.onUdpAssociateRequest.peerFacingBindHost", 
					null));

	@NameValuePairValueSpecDoc(
			description = "The comma separated list of acceptable binding "
					+ "host address types for the peer-facing UDP socket",
			name = "socks5.onUdpAssociateRequest.peerFacingBindHostAddressTypes",
			syntax = "socks5.onUdpAssociateRequest.peerFacingBindHostAddressTypes=HOST_ADDRESS_TYPES",
			valueType = HostAddressTypes.class
	)
	public static final SettingSpec<HostAddressTypes> SOCKS5_ON_UDP_ASSOCIATE_REQUEST_PEER_FACING_BIND_HOST_ADDRESS_TYPES =
			SETTING_SPECS.addThenGet(new HostAddressTypesSettingSpec(
					"socks5.onUdpAssociateRequest.peerFacingBindHostAddressTypes",
					HostAddressTypes.of()));

	@NameValuePairValueSpecDoc(
			description = "The comma separated list of binding port ranges for "
					+ "the peer-facing UDP socket",
			name = "socks5.onUdpAssociateRequest.peerFacingBindPortRanges",
			syntax = "socks5.onUdpAssociateRequest.peerFacingBindPortRanges=PORT_RANGES",
			valueType = PortRanges.class
	)	
	public static final SettingSpec<PortRanges> SOCKS5_ON_UDP_ASSOCIATE_REQUEST_PEER_FACING_BIND_PORT_RANGES =
			SETTING_SPECS.addThenGet(new PortRangesSettingSpec(
					"socks5.onUdpAssociateRequest.peerFacingBindPortRanges",
					PortRanges.of()));

	@NameValuePairValueSpecDoc(
			description = "The network interface that provides a binding host "
					+ "address for the peer-facing UDP socket",
			name = "socks5.onUdpAssociateRequest.peerFacingNetInterface",
			syntax = "socks5.onUdpAssociateRequest.peerFacingNetInterface=NETWORK_INTERFACE",
			valueType = NetInterface.class
	)
	public static final SettingSpec<NetInterface> SOCKS5_ON_UDP_ASSOCIATE_REQUEST_PEER_FACING_NET_INTERFACE =
			SETTING_SPECS.addThenGet(new NetInterfaceSettingSpec(
					"socks5.onUdpAssociateRequest.peerFacingNetInterface",
					null));

	@NameValuePairValueSpecDoc(
			description = "The comma separated list of socket settings for the "
					+ "peer-facing UDP socket",
			name = "socks5.onUdpAssociateRequest.peerFacingSocketSettings",
			syntax = "socks5.onUdpAssociateRequest.peerFacingSocketSettings=SOCKET_SETTINGS",
			valueType = SocketSettings.class
	)
	public static final SettingSpec<SocketSettings> SOCKS5_ON_UDP_ASSOCIATE_REQUEST_PEER_FACING_SOCKET_SETTINGS = 
			SETTING_SPECS.addThenGet(new SocketSettingsSettingSpec(
					"socks5.onUdpAssociateRequest.peerFacingSocketSettings", 
					SocketSettings.of()));
	
	@NameValuePairValueSpecDoc(
			description = "The buffer size in bytes for relaying the data",
			name = "socks5.onUdpAssociateRequest.relayBufferSize",
			syntax = "socks5.onUdpAssociateRequest.relayBufferSize=POSITIVE_INTEGER",
			valueType = PositiveInteger.class
	)
	public static final SettingSpec<PositiveInteger> SOCKS5_ON_UDP_ASSOCIATE_REQUEST_RELAY_BUFFER_SIZE = 
			SETTING_SPECS.addThenGet(new PositiveIntegerSettingSpec(
					"socks5.onUdpAssociateRequest.relayBufferSize", 
					null));
	
	@NameValuePairValueSpecDoc(
			description = "The timeout in milliseconds on relaying no data",
			name = "socks5.onUdpAssociateRequest.relayIdleTimeout",
			syntax = "socks5.onUdpAssociateRequest.relayIdleTimeout=POSITIVE_INTEGER",
			valueType = PositiveInteger.class
	)
	public static final SettingSpec<PositiveInteger> SOCKS5_ON_UDP_ASSOCIATE_REQUEST_RELAY_IDLE_TIMEOUT = 
			SETTING_SPECS.addThenGet(new PositiveIntegerSettingSpec(
					"socks5.onUdpAssociateRequest.relayIdleTimeout", 
					null));
	
	@NameValuePairValueSpecDoc(
			description = "The upper limit on bandwidth in bytes per second of "
					+ "receiving inbound data to be relayed",
			name = "socks5.onUdpAssociateRequest.relayInboundBandwidthLimit",
			syntax = "socks5.onUdpAssociateRequest.relayInboundBandwidthLimit=POSITIVE_INTEGER",
			valueType = PositiveInteger.class
	)
	public static final SettingSpec<PositiveInteger> SOCKS5_ON_UDP_ASSOCIATE_REQUEST_RELAY_INBOUND_BANDWIDTH_LIMIT =
			SETTING_SPECS.addThenGet(new PositiveIntegerSettingSpec(
					"socks5.onUdpAssociateRequest.relayInboundBandwidthLimit",
					null));
	
	@NameValuePairValueSpecDoc(
			description = "The upper limit on bandwidth in bytes per second of "
					+ "receiving outbound data to be relayed",
			name = "socks5.onUdpAssociateRequest.relayOutboundBandwidthLimit",
			syntax = "socks5.onUdpAssociateRequest.relayOutboundBandwidthLimit=POSITIVE_INTEGER",
			valueType = PositiveInteger.class
	)
	public static final SettingSpec<PositiveInteger> SOCKS5_ON_UDP_ASSOCIATE_REQUEST_RELAY_OUTBOUND_BANDWIDTH_LIMIT =
			SETTING_SPECS.addThenGet(new PositiveIntegerSettingSpec(
					"socks5.onUdpAssociateRequest.relayOutboundBandwidthLimit",
					null));
	
	@NameValuePairValueSpecDoc(
			description = "The user repository used for username password "
					+ "authentication (default is StringSourceUserRepository:)",
			name = "socks5.userpassmethod.userRepository",
			syntax = "socks5.userpassmethod.userRepository=SOCKS5_USERPASSMETHOD_USER_REPOSITORY",
			valueType = UserRepository.class
	)	
	public static final SettingSpec<UserRepository> SOCKS5_USERPASSMETHOD_USER_REPOSITORY = 
			SETTING_SPECS.addThenGet(new Socks5UserpassMethodUserRepositorySettingSpec(
					"socks5.userpassmethod.userRepository",
					UserRepository.newInstance()));
	
	public static List<SettingSpec<Object>> values() {
		return SETTING_SPECS.toList();
	}
	
	public static Map<String, SettingSpec<Object>> valuesMap() {
		return SETTING_SPECS.toMap();
	}
	
	private Socks5SettingSpecConstants() { }
}
