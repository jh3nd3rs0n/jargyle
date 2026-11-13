package com.github.jh3nd3rs0n.jargyle.server;

import com.github.jh3nd3rs0n.jargyle.common.net.*;
import com.github.jh3nd3rs0n.jargyle.common.number.PositiveInteger;
import com.github.jh3nd3rs0n.jargyle.internal.annotation.NameValuePairValueSpecDoc;
import com.github.jh3nd3rs0n.jargyle.internal.annotation.NameValuePairValueSpecsDoc;
import com.github.jh3nd3rs0n.jargyle.protocolbase.socks5.Methods;
import com.github.jh3nd3rs0n.jargyle.protocolbase.socks5.gssapiauthmethod.ProtectionLevels;
import com.github.jh3nd3rs0n.jargyle.server.internal.settingspec.impl.*;
import com.github.jh3nd3rs0n.jargyle.server.socks5.userpassauthmethod.UserRepository;

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
					+ "implementation",
			name = "socks5.gssapiauthmethod.necReferenceImpl",
			syntax = "socks5.gssapiauthmethod.necReferenceImpl=true|false",
			valueType = Boolean.class
	)
	public static final SettingSpec<Boolean> SOCKS5_GSSAPIAUTHMETHOD_NEC_REFERENCE_IMPL = 
			SETTING_SPECS.addThenGet(new BooleanSettingSpec(
					"socks5.gssapiauthmethod.necReferenceImpl", 
					null));
	
	@NameValuePairValueSpecDoc(
			description = "The comma separated list of acceptable protection "
					+ "levels after GSS-API authentication (The first is "
					+ "preferred if the client does not provide a protection "
					+ "level that is acceptable.)",
			name = "socks5.gssapiauthmethod.protectionLevels",
			syntax = "socks5.gssapiauthmethod.protectionLevels=SOCKS5_GSSAPIAUTHMETHOD_PROTECTION_LEVELS",
			valueType = ProtectionLevels.class
	)
	public static final SettingSpec<ProtectionLevels> SOCKS5_GSSAPIAUTHMETHOD_PROTECTION_LEVELS = 
			SETTING_SPECS.addThenGet(new Socks5GssapiAuthMethodProtectionLevelsSettingSpec(
					"socks5.gssapiauthmethod.protectionLevels", 
					null));

	@NameValuePairValueSpecDoc(
			description = "The suggested privacy (i.e. confidentiality) state "
					+ "for GSS-API messages sent after GSS-API authentication "
					+ "(applicable if the negotiated protection level is "
					+ "SELECTIVE_INTEG_OR_CONF)",
			name = "socks5.gssapiauthmethod.suggestedConf",
			syntax = "socks5.gssapiauthmethod.suggestedConf=true|false",
			valueType = Boolean.class
	)
	public static final SettingSpec<Boolean> SOCKS5_GSSAPIAUTHMETHOD_SUGGESTED_CONF =
			SETTING_SPECS.addThenGet(new BooleanSettingSpec(
					"socks5.gssapiauthmethod.suggestedConf",
					null));

	@NameValuePairValueSpecDoc(
			description = "The suggested quality-of-protection (i.e. "
					+ "integrity) value for GSS-API messages sent after "
					+ "GSS-API authentication (applicable if the negotiated "
					+ "protection level is SELECTIVE_INTEG_OR_CONF)",
			name = "socks5.gssapiauthmethod.suggestedInteg",
			syntax = "socks5.gssapiauthmethod.suggestedInteg=-2147483648-2147483647",
			valueType = Integer.class
	)
	public static final SettingSpec<Integer> SOCKS5_GSSAPIAUTHMETHOD_SUGGESTED_INTEG =
			SETTING_SPECS.addThenGet(new IntegerSettingSpec(
					"socks5.gssapiauthmethod.suggestedInteg",
					null));

	@NameValuePairValueSpecDoc(
			description = "The comma separated list of acceptable "
					+ "authentication methods in order of preference",
			name = "socks5.methods",
			syntax = "socks5.methods=SOCKS5_METHODS",
			valueType = Methods.class
	)
	public static final SettingSpec<Methods> SOCKS5_METHODS = 
			SETTING_SPECS.addThenGet(new Socks5MethodsSettingSpec(
					"socks5.methods", 
					Methods.of()));
	
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
			description = "The binding host name or address for the " 
					+ "remote-facing TCP server socket if the provided host " 
					+ "address is all zeros",
			name = "socks5.onBindRequest.remoteFacingServerSocket.bindHost",
			syntax = "socks5.onBindRequest.remoteFacingServerSocket.bindHost=HOST",
			valueType = Host.class
	)
	public static final SettingSpec<Host> SOCKS5_ON_BIND_REQUEST_REMOTE_FACING_SERVER_SOCKET_BIND_HOST =
			SETTING_SPECS.addThenGet(new HostSettingSpec(
					"socks5.onBindRequest.remoteFacingServerSocket.bindHost",
					null));

	@NameValuePairValueSpecDoc(
			description = "The comma separated list of acceptable binding " 
					+ "host address types for the remote-facing TCP server " 
					+ "socket if the provided host address is all zeros",
			name = "socks5.onBindRequest.remoteFacingServerSocket.bindHostAddressTypes",
			syntax = "socks5.onBindRequest.remoteFacingServerSocket.bindHostAddressTypes=HOST_ADDRESS_TYPES",
			valueType = HostAddressTypes.class
	)
	public static final SettingSpec<HostAddressTypes> SOCKS5_ON_BIND_REQUEST_REMOTE_FACING_SERVER_SOCKET_BIND_HOST_ADDRESS_TYPES =
			SETTING_SPECS.addThenGet(new HostAddressTypesSettingSpec(
					"socks5.onBindRequest.remoteFacingServerSocket.bindHostAddressTypes",
					HostAddressTypes.of()));

	@NameValuePairValueSpecDoc(
			description = "The comma separated list of binding port ranges " 
					+ "for the remote-facing TCP server socket if the " 
					+ "provided port is zero",
			name = "socks5.onBindRequest.remoteFacingServerSocket.bindPortRanges",
			syntax = "socks5.onBindRequest.remoteFacingServerSocket.bindPortRanges=PORT_RANGES",
			valueType = PortRanges.class
	)
	public static final SettingSpec<PortRanges> SOCKS5_ON_BIND_REQUEST_REMOTE_FACING_SERVER_SOCKET_BIND_PORT_RANGES =
			SETTING_SPECS.addThenGet(new PortRangesSettingSpec(
					"socks5.onBindRequest.remoteFacingServerSocket.bindPortRanges",
					PortRanges.of()));

	@NameValuePairValueSpecDoc(
			description = "The network interface that provides a binding "
					+ "host address for the remote-facing TCP server socket " 
					+ "if the provided host address is all zeros",
			name = "socks5.onBindRequest.remoteFacingServerSocket.netInterface",
			syntax = "socks5.onBindRequest.remoteFacingServerSocket.netInterface=NETWORK_INTERFACE",
			valueType = NetInterface.class
	)
	public static final SettingSpec<NetInterface> SOCKS5_ON_BIND_REQUEST_REMOTE_FACING_SERVER_SOCKET_NET_INTERFACE =
			SETTING_SPECS.addThenGet(new NetInterfaceSettingSpec(
					"socks5.onBindRequest.remoteFacingServerSocket.netInterface",
					null));

	@NameValuePairValueSpecDoc(
			description = "The comma separated list of socket settings for " 
					+ "the remote-facing TCP server socket",
			name = "socks5.onBindRequest.remoteFacingServerSocket.socketSettings",
			syntax = "socks5.onBindRequest.remoteFacingServerSocket.socketSettings=SOCKET_SETTINGS",
			valueType = SocketSettings.class
	)
	public static final SettingSpec<SocketSettings> SOCKS5_ON_BIND_REQUEST_REMOTE_FACING_SERVER_SOCKET_SOCKET_SETTINGS = 
			SETTING_SPECS.addThenGet(new SocketSettingsSettingSpec(
					"socks5.onBindRequest.remoteFacingServerSocket.socketSettings", 
					SocketSettings.of()));

	@NameValuePairValueSpecDoc(
			description = "The comma separated list of socket settings for " 
					+ "the remote TCP socket",
			name = "socks5.onBindRequest.remoteSocket.socketSettings",
			syntax = "socks5.onBindRequest.remoteSocket.socketSettings=SOCKET_SETTINGS",
			valueType = SocketSettings.class
	)
	public static final SettingSpec<SocketSettings> SOCKS5_ON_BIND_REQUEST_REMOTE_SOCKET_SOCKET_SETTINGS =
			SETTING_SPECS.addThenGet(new SocketSettingsSettingSpec(
					"socks5.onBindRequest.remoteSocket.socketSettings",
					SocketSettings.of()));
	
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
			description = "The binding host name or address for the remote "
					+ "TCP socket",
			name = "socks5.onConnectRequest.remoteSocket.bindHost",
			syntax = "socks5.onConnectRequest.remoteSocket.bindHost=HOST",
			valueType = Host.class
	)
	public static final SettingSpec<Host> SOCKS5_ON_CONNECT_REQUEST_REMOTE_SOCKET_BIND_HOST = 
			SETTING_SPECS.addThenGet(new HostSettingSpec(
					"socks5.onConnectRequest.remoteSocket.bindHost", 
					null));

	@NameValuePairValueSpecDoc(
			description = "The comma separated list of acceptable binding "
					+ "host address types for the remote TCP socket",
			name = "socks5.onConnectRequest.remoteSocket.bindHostAddressTypes",
			syntax = "socks5.onConnectRequest.remoteSocket.bindHostAddressTypes=HOST_ADDRESS_TYPES",
			valueType = HostAddressTypes.class
	)
	public static final SettingSpec<HostAddressTypes> SOCKS5_ON_CONNECT_REQUEST_REMOTE_SOCKET_BIND_HOST_ADDRESS_TYPES =
			SETTING_SPECS.addThenGet(new HostAddressTypesSettingSpec(
					"socks5.onConnectRequest.remoteSocket.bindHostAddressTypes",
					HostAddressTypes.of()));

	@NameValuePairValueSpecDoc(
			description = "The comma separated list of binding port ranges "
					+ "for the remote TCP socket",
			name = "socks5.onConnectRequest.remoteSocket.bindPortRanges",
			syntax = "socks5.onConnectRequest.remoteSocket.bindPortRanges=PORT_RANGES",
			valueType = PortRanges.class
	)	
	public static final SettingSpec<PortRanges> SOCKS5_ON_CONNECT_REQUEST_REMOTE_SOCKET_BIND_PORT_RANGES =
			SETTING_SPECS.addThenGet(new PortRangesSettingSpec(
					"socks5.onConnectRequest.remoteSocket.bindPortRanges",
					PortRanges.of()));
	
	@NameValuePairValueSpecDoc(
			description = "The timeout in milliseconds on waiting for the "
					+ "remote TCP socket to connect",
			name = "socks5.onConnectRequest.remoteSocket.connectTimeout",
			syntax = "socks5.onConnectRequest.remoteSocket.connectTimeout=POSITIVE_INTEGER",
			valueType = PositiveInteger.class
	)
	public static final SettingSpec<PositiveInteger> SOCKS5_ON_CONNECT_REQUEST_REMOTE_SOCKET_CONNECT_TIMEOUT = 
			SETTING_SPECS.addThenGet(new PositiveIntegerSettingSpec(
					"socks5.onConnectRequest.remoteSocket.connectTimeout", 
					null));

	@NameValuePairValueSpecDoc(
			description = "The network interface that provides a binding "
					+ "host address for the remote TCP socket",
			name = "socks5.onConnectRequest.remoteSocket.netInterface",
			syntax = "socks5.onConnectRequest.remoteSocket.netInterface=NETWORK_INTERFACE",
			valueType = NetInterface.class
	)
	public static final SettingSpec<NetInterface> SOCKS5_ON_CONNECT_REQUEST_REMOTE_SOCKET_NET_INTERFACE =
			SETTING_SPECS.addThenGet(new NetInterfaceSettingSpec(
					"socks5.onConnectRequest.remoteSocket.netInterface",
					null));
	
	@NameValuePairValueSpecDoc(
			description = "The boolean value to indicate if the remote TCP "
					+ "socket is to be prepared before connecting (involves "
					+ "applying the specified socket settings, resolving the "
					+ "target host name, and setting the specified timeout "
					+ "on waiting to connect)",
			name = "socks5.onConnectRequest.remoteSocket.prepareSocket",
			syntax = "socks5.onConnectRequest.remoteSocket.prepareSocket=true|false",
			valueType = Boolean.class
	)
	public static final SettingSpec<Boolean> SOCKS5_ON_CONNECT_REQUEST_REMOTE_SOCKET_PREPARE_SOCKET =
			SETTING_SPECS.addThenGet(new BooleanSettingSpec(
					"socks5.onConnectRequest.remoteSocket.prepareSocket",
					null));

	@NameValuePairValueSpecDoc(
			description = "The comma separated list of socket settings for " 
					+ "the remote TCP socket",
			name = "socks5.onConnectRequest.remoteSocket.socketSettings",
			syntax = "socks5.onConnectRequest.remoteSocket.socketSettings=SOCKET_SETTINGS",
			valueType = SocketSettings.class
	)
	public static final SettingSpec<SocketSettings> SOCKS5_ON_CONNECT_REQUEST_REMOTE_SOCKET_SOCKET_SETTINGS = 
			SETTING_SPECS.addThenGet(new SocketSettingsSettingSpec(
					"socks5.onConnectRequest.remoteSocket.socketSettings", 
					SocketSettings.of()));

	@NameValuePairValueSpecDoc(
			description = "The binding host name or address for the "
					+ "client-facing UDP socket",
			name = "socks5.onUdpAssociateRequest.clientFacingDatagramSocket.bindHost",
			syntax = "socks5.onUdpAssociateRequest.clientFacingDatagramSocket.bindHost=HOST",
			valueType = Host.class
	)
	public static final SettingSpec<Host> SOCKS5_ON_UDP_ASSOCIATE_REQUEST_CLIENT_FACING_DATAGRAM_SOCKET_BIND_HOST = 
			SETTING_SPECS.addThenGet(new HostSettingSpec(
					"socks5.onUdpAssociateRequest.clientFacingDatagramSocket.bindHost", 
					null));

	@NameValuePairValueSpecDoc(
			description = "The comma separated list of acceptable binding "
					+ "host address types for the client-facing UDP socket",
			name = "socks5.onUdpAssociateRequest.clientFacingDatagramSocket.bindHostAddressTypes",
			syntax = "socks5.onUdpAssociateRequest.clientFacingDatagramSocket.bindHostAddressTypes=HOST_ADDRESS_TYPES",
			valueType = HostAddressTypes.class
	)
	public static final SettingSpec<HostAddressTypes> SOCKS5_ON_UDP_ASSOCIATE_REQUEST_CLIENT_FACING_DATAGRAM_SOCKET_BIND_HOST_ADDRESS_TYPES =
			SETTING_SPECS.addThenGet(new HostAddressTypesSettingSpec(
					"socks5.onUdpAssociateRequest.clientFacingDatagramSocket.bindHostAddressTypes",
					HostAddressTypes.of()));

	@NameValuePairValueSpecDoc(
			description = "The comma separated list of binding port ranges for "
					+ "the client-facing UDP socket",
			name = "socks5.onUdpAssociateRequest.clientFacingDatagramSocket.bindPortRanges",
			syntax = "socks5.onUdpAssociateRequest.clientFacingDatagramSocket.bindPortRanges=PORT_RANGES",
			valueType = PortRanges.class
	)	
	public static final SettingSpec<PortRanges> SOCKS5_ON_UDP_ASSOCIATE_REQUEST_CLIENT_FACING_DATAGRAM_SOCKET_BIND_PORT_RANGES =
			SETTING_SPECS.addThenGet(new PortRangesSettingSpec(
					"socks5.onUdpAssociateRequest.clientFacingDatagramSocket.bindPortRanges",
					PortRanges.of()));

	@NameValuePairValueSpecDoc(
			description = "The network interface that provides a binding host "
					+ "address for the client-facing UDP socket",
			name = "socks5.onUdpAssociateRequest.clientFacingDatagramSocket.netInterface",
			syntax = "socks5.onUdpAssociateRequest.clientFacingDatagramSocket.netInterface=NETWORK_INTERFACE",
			valueType = NetInterface.class
	)
	public static final SettingSpec<NetInterface> SOCKS5_ON_UDP_ASSOCIATE_REQUEST_CLIENT_FACING_DATAGRAM_SOCKET_NET_INTERFACE =
			SETTING_SPECS.addThenGet(new NetInterfaceSettingSpec(
					"socks5.onUdpAssociateRequest.clientFacingDatagramSocket.netInterface",
					null));

	@NameValuePairValueSpecDoc(
			description = "The comma separated list of socket settings for the "
					+ "client-facing UDP socket",
			name = "socks5.onUdpAssociateRequest.clientFacingDatagramSocket.socketSettings",
			syntax = "socks5.onUdpAssociateRequest.clientFacingDatagramSocket.socketSettings=SOCKET_SETTINGS",
			valueType = SocketSettings.class
	)
	public static final SettingSpec<SocketSettings> SOCKS5_ON_UDP_ASSOCIATE_REQUEST_CLIENT_FACING_DATAGRAM_SOCKET_SOCKET_SETTINGS = 
			SETTING_SPECS.addThenGet(new SocketSettingsSettingSpec(
					"socks5.onUdpAssociateRequest.clientFacingDatagramSocket.socketSettings", 
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
			description = "The binding host name or address for the remote-facing "
					+ "UDP socket",
			name = "socks5.onUdpAssociateRequest.remoteFacingDatagramSocket.bindHost",
			syntax = "socks5.onUdpAssociateRequest.remoteFacingDatagramSocket.bindHost=HOST",
			valueType = Host.class
	)
	public static final SettingSpec<Host> SOCKS5_ON_UDP_ASSOCIATE_REQUEST_REMOTE_FACING_DATAGRAM_SOCKET_BIND_HOST = 
			SETTING_SPECS.addThenGet(new HostSettingSpec(
					"socks5.onUdpAssociateRequest.remoteFacingDatagramSocket.bindHost", 
					null));

	@NameValuePairValueSpecDoc(
			description = "The comma separated list of acceptable binding "
					+ "host address types for the remote-facing UDP socket",
			name = "socks5.onUdpAssociateRequest.remoteFacingDatagramSocket.bindHostAddressTypes",
			syntax = "socks5.onUdpAssociateRequest.remoteFacingDatagramSocket.bindHostAddressTypes=HOST_ADDRESS_TYPES",
			valueType = HostAddressTypes.class
	)
	public static final SettingSpec<HostAddressTypes> SOCKS5_ON_UDP_ASSOCIATE_REQUEST_REMOTE_FACING_DATAGRAM_SOCKET_BIND_HOST_ADDRESS_TYPES =
			SETTING_SPECS.addThenGet(new HostAddressTypesSettingSpec(
					"socks5.onUdpAssociateRequest.remoteFacingDatagramSocket.bindHostAddressTypes",
					HostAddressTypes.of()));

	@NameValuePairValueSpecDoc(
			description = "The comma separated list of binding port ranges for "
					+ "the remote-facing UDP socket",
			name = "socks5.onUdpAssociateRequest.remoteFacingDatagramSocket.bindPortRanges",
			syntax = "socks5.onUdpAssociateRequest.remoteFacingDatagramSocket.bindPortRanges=PORT_RANGES",
			valueType = PortRanges.class
	)	
	public static final SettingSpec<PortRanges> SOCKS5_ON_UDP_ASSOCIATE_REQUEST_REMOTE_FACING_DATAGRAM_SOCKET_BIND_PORT_RANGES =
			SETTING_SPECS.addThenGet(new PortRangesSettingSpec(
					"socks5.onUdpAssociateRequest.remoteFacingDatagramSocket.bindPortRanges",
					PortRanges.of()));

	@NameValuePairValueSpecDoc(
			description = "The network interface that provides a binding host "
					+ "address for the remote-facing UDP socket",
			name = "socks5.onUdpAssociateRequest.remoteFacingDatagramSocket.netInterface",
			syntax = "socks5.onUdpAssociateRequest.remoteFacingDatagramSocket.netInterface=NETWORK_INTERFACE",
			valueType = NetInterface.class
	)
	public static final SettingSpec<NetInterface> SOCKS5_ON_UDP_ASSOCIATE_REQUEST_REMOTE_FACING_DATAGRAM_SOCKET_NET_INTERFACE =
			SETTING_SPECS.addThenGet(new NetInterfaceSettingSpec(
					"socks5.onUdpAssociateRequest.remoteFacingDatagramSocket.netInterface",
					null));

	@NameValuePairValueSpecDoc(
			description = "The comma separated list of socket settings for the "
					+ "remote-facing UDP socket",
			name = "socks5.onUdpAssociateRequest.remoteFacingDatagramSocket.socketSettings",
			syntax = "socks5.onUdpAssociateRequest.remoteFacingDatagramSocket.socketSettings=SOCKET_SETTINGS",
			valueType = SocketSettings.class
	)
	public static final SettingSpec<SocketSettings> SOCKS5_ON_UDP_ASSOCIATE_REQUEST_REMOTE_FACING_DATAGRAM_SOCKET_SOCKET_SETTINGS = 
			SETTING_SPECS.addThenGet(new SocketSettingsSettingSpec(
					"socks5.onUdpAssociateRequest.remoteFacingDatagramSocket.socketSettings", 
					SocketSettings.of()));
	
	@NameValuePairValueSpecDoc(
			description = "The user repository used for username password "
					+ "authentication",
			name = "socks5.userpassauthmethod.userRepository",
			syntax = "socks5.userpassauthmethod.userRepository=SOCKS5_USERPASSAUTHMETHOD_USER_REPOSITORY",
			valueType = UserRepository.class
	)	
	public static final SettingSpec<UserRepository> SOCKS5_USERPASSAUTHMETHOD_USER_REPOSITORY = 
			SETTING_SPECS.addThenGet(new Socks5UserpassAuthMethodUserRepositorySettingSpec(
					"socks5.userpassauthmethod.userRepository",
					null));
	
	public static List<SettingSpec<Object>> values() {
		return SETTING_SPECS.toList();
	}
	
	public static Map<String, SettingSpec<Object>> valuesMap() {
		return SETTING_SPECS.toMap();
	}
	
	private Socks5SettingSpecConstants() { }
}
