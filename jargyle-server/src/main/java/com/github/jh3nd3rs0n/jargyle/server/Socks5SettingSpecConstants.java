package com.github.jh3nd3rs0n.jargyle.server;

import java.util.List;
import java.util.Map;

import com.github.jh3nd3rs0n.jargyle.common.net.Host;
import com.github.jh3nd3rs0n.jargyle.common.net.PortRanges;
import com.github.jh3nd3rs0n.jargyle.common.net.SocketSettings;
import com.github.jh3nd3rs0n.jargyle.common.number.PositiveInteger;
import com.github.jh3nd3rs0n.jargyle.internal.annotation.NameValuePairValueSpecDoc;
import com.github.jh3nd3rs0n.jargyle.internal.annotation.NameValuePairValueSpecsDoc;
import com.github.jh3nd3rs0n.jargyle.protocolbase.socks5.Methods;
import com.github.jh3nd3rs0n.jargyle.protocolbase.socks5.gssapimethod.ProtectionLevels;
import com.github.jh3nd3rs0n.jargyle.server.internal.settingspec.impl.BooleanSettingSpec;
import com.github.jh3nd3rs0n.jargyle.server.internal.settingspec.impl.HostSettingSpec;
import com.github.jh3nd3rs0n.jargyle.server.internal.settingspec.impl.PortRangesSettingSpec;
import com.github.jh3nd3rs0n.jargyle.server.internal.settingspec.impl.PositiveIntegerSettingSpec;
import com.github.jh3nd3rs0n.jargyle.server.internal.settingspec.impl.SocketSettingsSettingSpec;
import com.github.jh3nd3rs0n.jargyle.server.internal.settingspec.impl.Socks5GssapiMethodProtectionLevelsSettingSpec;
import com.github.jh3nd3rs0n.jargyle.server.internal.settingspec.impl.Socks5MethodsSettingSpec;
import com.github.jh3nd3rs0n.jargyle.server.internal.settingspec.impl.Socks5UserpassMethodUserRepositorySettingSpec;
import com.github.jh3nd3rs0n.jargyle.server.socks5.userpassmethod.UserRepository;

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
			name = "socks5.onBind.inboundSocketSettings",
			syntax = "socks5.onBind.inboundSocketSettings=SOCKET_SETTINGS",
			valueType = SocketSettings.class
	)
	public static final SettingSpec<SocketSettings> SOCKS5_ON_BIND_INBOUND_SOCKET_SETTINGS = 
			SETTING_SPECS.addThenGet(new SocketSettingsSettingSpec(
					"socks5.onBind.inboundSocketSettings", 
					SocketSettings.of()));
	
	@NameValuePairValueSpecDoc(
			description = "The binding host name or address for the listen "
					+ "socket if the provided host address is all zeros",
			name = "socks5.onBind.listenBindHost",
			syntax = "socks5.onBind.listenBindHost=HOST",
			valueType = Host.class
	)
	public static final SettingSpec<Host> SOCKS5_ON_BIND_LISTEN_BIND_HOST =
			SETTING_SPECS.addThenGet(new HostSettingSpec(
					"socks5.onBind.listenBindHost",
					null));
	
	@NameValuePairValueSpecDoc(
			description = "The comma separated list of binding port ranges for "
					+ "the listen socket if the provided port is zero",
			name = "socks5.onBind.listenBindPortRanges",
			syntax = "socks5.onBind.listenBindPortRanges=PORT_RANGES",
			valueType = PortRanges.class
	)
	public static final SettingSpec<PortRanges> SOCKS5_ON_BIND_LISTEN_BIND_PORT_RANGES =
			SETTING_SPECS.addThenGet(new PortRangesSettingSpec(
					"socks5.onBind.listenBindPortRanges",
					PortRanges.of()));
	
	@NameValuePairValueSpecDoc(
			description = "The comma separated list of socket settings for the "
					+ "listen socket",
			name = "socks5.onBind.listenSocketSettings",
			syntax = "socks5.onBind.listenSocketSettings=SOCKET_SETTINGS",
			valueType = SocketSettings.class
	)
	public static final SettingSpec<SocketSettings> SOCKS5_ON_BIND_LISTEN_SOCKET_SETTINGS = 
			SETTING_SPECS.addThenGet(new SocketSettingsSettingSpec(
					"socks5.onBind.listenSocketSettings", 
					SocketSettings.of()));
	
	@NameValuePairValueSpecDoc(
			description = "The buffer size in bytes for relaying the data",
			name = "socks5.onBind.relayBufferSize",
			syntax = "socks5.onBind.relayBufferSize=POSITIVE_INTEGER",
			valueType = PositiveInteger.class
	)
	public static final SettingSpec<PositiveInteger> SOCKS5_ON_BIND_RELAY_BUFFER_SIZE = 
			SETTING_SPECS.addThenGet(new PositiveIntegerSettingSpec(
					"socks5.onBind.relayBufferSize", 
					null));
	
	@NameValuePairValueSpecDoc(
			description = "The timeout in milliseconds on relaying no data",
			name = "socks5.onBind.relayIdleTimeout",
			syntax = "socks5.onBind.relayIdleTimeout=POSITIVE_INTEGER",
			valueType = PositiveInteger.class
	)
	public static final SettingSpec<PositiveInteger> SOCKS5_ON_BIND_RELAY_IDLE_TIMEOUT = 
			SETTING_SPECS.addThenGet(new PositiveIntegerSettingSpec(
					"socks5.onBind.relayIdleTimeout", 
					null));
	
	@NameValuePairValueSpecDoc(
			description = "The upper limit on bandwidth in bytes per second of "
					+ "receiving inbound data to be relayed",
			name = "socks5.onBind.relayInboundBandwidthLimit",
			syntax = "socks5.onBind.relayInboundBandwidthLimit=POSITIVE_INTEGER",
			valueType = PositiveInteger.class
	)
	public static final SettingSpec<PositiveInteger> SOCKS5_ON_BIND_RELAY_INBOUND_BANDWIDTH_LIMIT =
			SETTING_SPECS.addThenGet(new PositiveIntegerSettingSpec(
					"socks5.onBind.relayInboundBandwidthLimit",
					null));
	
	@NameValuePairValueSpecDoc(
			description = "The upper limit on bandwidth in bytes per second of "
					+ "receiving outbound data to be relayed",
			name = "socks5.onBind.relayOutboundBandwidthLimit",
			syntax = "socks5.onBind.relayOutboundBandwidthLimit=POSITIVE_INTEGER",
			valueType = PositiveInteger.class
	)
	public static final SettingSpec<PositiveInteger> SOCKS5_ON_BIND_RELAY_OUTBOUND_BANDWIDTH_LIMIT =
			SETTING_SPECS.addThenGet(new PositiveIntegerSettingSpec(
					"socks5.onBind.relayOutboundBandwidthLimit",
					null));
	
	@NameValuePairValueSpecDoc(
			description = "The binding host name or address for all sockets",
			name = "socks5.onCommand.bindHost",
			syntax = "socks5.onCommand.bindHost=HOST",
			valueType = Host.class
	)
	public static final SettingSpec<Host> SOCKS5_ON_COMMAND_BIND_HOST =
			SETTING_SPECS.addThenGet(new HostSettingSpec(
					"socks5.onCommand.bindHost",
					null));
	
	@NameValuePairValueSpecDoc(
			description = "The comma separated list of binding port ranges for "
					+ "all TCP sockets",
			name = "socks5.onCommand.bindTcpPortRanges",
			syntax = "socks5.onCommand.bindTcpPortRanges=PORT_RANGES",
			valueType = PortRanges.class
	)
	public static final SettingSpec<PortRanges> SOCKS5_ON_COMMAND_BIND_TCP_PORT_RANGES =
			SETTING_SPECS.addThenGet(new PortRangesSettingSpec(
					"socks5.onCommand.bindTcpPortRanges",
					PortRanges.of()));
			
	@NameValuePairValueSpecDoc(
			description = "The comma separated list of binding port ranges for "
					+ "all UDP sockets",
			name = "socks5.onCommand.bindUdpPortRanges",
			syntax = "socks5.onCommand.bindUdpPortRanges=PORT_RANGES",
			valueType = PortRanges.class
	)
	public static final SettingSpec<PortRanges> SOCKS5_ON_COMMAND_BIND_UDP_PORT_RANGES =
			SETTING_SPECS.addThenGet(new PortRangesSettingSpec(
					"socks5.onCommand.bindUdpPortRanges",
					PortRanges.of()));

	@NameValuePairValueSpecDoc(
			description = "The binding host name or address for all "
					+ "external-facing sockets",
			name = "socks5.onCommand.externalFacingBindHost",
			syntax = "socks5.onCommand.externalFacingBindHost=HOST",
			valueType = Host.class
	)
	public static final SettingSpec<Host> SOCKS5_ON_COMMAND_EXTERNAL_FACING_BIND_HOST =
			SETTING_SPECS.addThenGet(new HostSettingSpec(
					"socks5.onCommand.externalFacingBindHost",
					null));
	
	@NameValuePairValueSpecDoc(
			description = "The comma separated list of binding port ranges for "
					+ "all external-facing TCP sockets",
			name = "socks5.onCommand.externalFacingBindTcpPortRanges",
			syntax = "socks5.onCommand.externalFacingBindTcpPortRanges=PORT_RANGES",
			valueType = PortRanges.class
	)
	public static final SettingSpec<PortRanges> SOCKS5_ON_COMMAND_EXTERNAL_FACING_BIND_TCP_PORT_RANGES =
			SETTING_SPECS.addThenGet(new PortRangesSettingSpec(
					"socks5.onCommand.externalFacingBindTcpPortRanges",
					PortRanges.of()));
			
	@NameValuePairValueSpecDoc(
			description = "The comma separated list of binding port ranges for "
					+ "all external-facing UDP sockets",
			name = "socks5.onCommand.externalFacingBindUdpPortRanges",
			syntax = "socks5.onCommand.externalFacingBindUdpPortRanges=PORT_RANGES",
			valueType = PortRanges.class
	)
	public static final SettingSpec<PortRanges> SOCKS5_ON_COMMAND_EXTERNAL_FACING_BIND_UDP_PORT_RANGES =
			SETTING_SPECS.addThenGet(new PortRangesSettingSpec(
					"socks5.onCommand.externalFacingBindUdpPortRanges",
					PortRanges.of()));
	
	@NameValuePairValueSpecDoc(
			description = "The comma separated list of socket settings for all "
					+ "external-facing sockets",
			name = "socks5.onCommand.externalFacingSocketSettings",
			syntax = "socks5.onCommand.externalFacingSocketSettings=SOCKET_SETTINGS",
			valueType = SocketSettings.class
	)
	public static final SettingSpec<SocketSettings> SOCKS5_ON_COMMAND_EXTERNAL_FACING_SOCKET_SETTINGS =
			SETTING_SPECS.addThenGet(new SocketSettingsSettingSpec(
					"socks5.onCommand.externalFacingSocketSettings", 
					SocketSettings.of()));

	@NameValuePairValueSpecDoc(
			description = "The binding host name or address for all "
					+ "internal-facing sockets",
			name = "socks5.onCommand.internalFacingBindHost",
			syntax = "socks5.onCommand.internalFacingBindHost=HOST",
			valueType = Host.class
	)
	public static final SettingSpec<Host> SOCKS5_ON_COMMAND_INTERNAL_FACING_BIND_HOST =
			SETTING_SPECS.addThenGet(new HostSettingSpec(
					"socks5.onCommand.internalFacingBindHost",
					null));

	@NameValuePairValueSpecDoc(
			description = "The comma separated list of binding port ranges for "
					+ "all internal-facing UDP sockets",
			name = "socks5.onCommand.internalFacingBindUdpPortRanges",
			syntax = "socks5.onCommand.internalFacingBindUdpPortRanges=PORT_RANGES",
			valueType = PortRanges.class
	)
	public static final SettingSpec<PortRanges> SOCKS5_ON_COMMAND_INTERNAL_FACING_BIND_UDP_PORT_RANGES =
			SETTING_SPECS.addThenGet(new PortRangesSettingSpec(
					"socks5.onCommand.internalFacingBindUdpPortRanges",
					PortRanges.of()));
	
	@NameValuePairValueSpecDoc(
			description = "The comma separated list of socket settings for all "
					+ "internal-facing sockets",
			name = "socks5.onCommand.internalFacingSocketSettings",
			syntax = "socks5.onCommand.internalFacingSocketSettings=SOCKET_SETTINGS",
			valueType = SocketSettings.class
	)
	public static final SettingSpec<SocketSettings> SOCKS5_ON_COMMAND_INTERNAL_FACING_SOCKET_SETTINGS =
			SETTING_SPECS.addThenGet(new SocketSettingsSettingSpec(
					"socks5.onCommand.internalFacingSocketSettings", 
					SocketSettings.of()));
	
	@NameValuePairValueSpecDoc(
			description = "The buffer size in bytes for relaying the data "
					+ "(default is 1024)",
			name = "socks5.onCommand.relayBufferSize",
			syntax = "socks5.onCommand.relayBufferSize=POSITIVE_INTEGER",
			valueType = PositiveInteger.class
	)
	public static final SettingSpec<PositiveInteger> SOCKS5_ON_COMMAND_RELAY_BUFFER_SIZE =
			SETTING_SPECS.addThenGet(new PositiveIntegerSettingSpec(
					"socks5.onCommand.relayBufferSize",
					PositiveInteger.valueOf(1024)));
	
	@NameValuePairValueSpecDoc(
			description = "The timeout in milliseconds on relaying no data "
					+ "(default is 60000)",
			name = "socks5.onCommand.relayIdleTimeout",
			syntax = "socks5.onCommand.relayIdleTimeout=POSITIVE_INTEGER",
			valueType = PositiveInteger.class
	)	
	public static final SettingSpec<PositiveInteger> SOCKS5_ON_COMMAND_RELAY_IDLE_TIMEOUT =
			SETTING_SPECS.addThenGet(new PositiveIntegerSettingSpec(
					"socks5.onCommand.relayIdleTimeout",
					PositiveInteger.valueOf(60000)));
	
	@NameValuePairValueSpecDoc(
			description = "The upper limit on bandwidth in bytes per second of "
					+ "receiving inbound data to be relayed",
			name = "socks5.onCommand.relayInboundBandwidthLimit",
			syntax = "socks5.onCommand.relayInboundBandwidthLimit=POSITIVE_INTEGER",
			valueType = PositiveInteger.class
	)	
	public static final SettingSpec<PositiveInteger> SOCKS5_ON_COMMAND_RELAY_INBOUND_BANDWIDTH_LIMIT =
			SETTING_SPECS.addThenGet(new PositiveIntegerSettingSpec(
					"socks5.onCommand.relayInboundBandwidthLimit",
					null));
	
	@NameValuePairValueSpecDoc(
			description = "The upper limit on bandwidth in bytes per second of "
					+ "receiving outbound data to be relayed",
			name = "socks5.onCommand.relayOutboundBandwidthLimit",
			syntax = "socks5.onCommand.relayOutboundBandwidthLimit=POSITIVE_INTEGER",
			valueType = PositiveInteger.class
	)	
	public static final SettingSpec<PositiveInteger> SOCKS5_ON_COMMAND_RELAY_OUTBOUND_BANDWIDTH_LIMIT =
			SETTING_SPECS.addThenGet(new PositiveIntegerSettingSpec(
					"socks5.onCommand.relayOutboundBandwidthLimit",
					null));	
	
	@NameValuePairValueSpecDoc(
			description = "The comma separated list of socket settings for all "
					+ "sockets",
			name = "socks5.onCommand.socketSettings",
			syntax = "socks5.onCommand.socketSettings=SOCKET_SETTINGS",
			valueType = SocketSettings.class
	)
	public static final SettingSpec<SocketSettings> SOCKS5_ON_COMMAND_SOCKET_SETTINGS =
			SETTING_SPECS.addThenGet(new SocketSettingsSettingSpec(
					"socks5.onCommand.socketSettings", 
					SocketSettings.of()));
	
	@NameValuePairValueSpecDoc(
			description = "The boolean value to indicate if the server-facing "
					+ "socket is to be prepared before connecting (involves "
					+ "applying the specified socket settings, resolving the "
					+ "target host name, and setting the specified timeout on "
					+ "waiting to connect) (default is false)",
			name = "socks5.onConnect.prepareServerFacingSocket",
			syntax = "socks5.onConnect.prepareServerFacingSocket=true|false",
			valueType = Boolean.class
	)	
	public static final SettingSpec<Boolean> SOCKS5_ON_CONNECT_PREPARE_SERVER_FACING_SOCKET = 
			SETTING_SPECS.addThenGet(new BooleanSettingSpec(
					"socks5.onConnect.prepareServerFacingSocket", 
					Boolean.FALSE));
	
	@NameValuePairValueSpecDoc(
			description = "The buffer size in bytes for relaying the data",
			name = "socks5.onConnect.relayBufferSize",
			syntax = "socks5.onConnect.relayBufferSize=POSITIVE_INTEGER",
			valueType = PositiveInteger.class
	)
	public static final SettingSpec<PositiveInteger> SOCKS5_ON_CONNECT_RELAY_BUFFER_SIZE = 
			SETTING_SPECS.addThenGet(new PositiveIntegerSettingSpec(
					"socks5.onConnect.relayBufferSize", 
					null));
	
	@NameValuePairValueSpecDoc(
			description = "The timeout in milliseconds on relaying no data",
			name = "socks5.onConnect.relayIdleTimeout",
			syntax = "socks5.onConnect.relayIdleTimeout=POSITIVE_INTEGER",
			valueType = PositiveInteger.class
	)
	public static final SettingSpec<PositiveInteger> SOCKS5_ON_CONNECT_RELAY_IDLE_TIMEOUT = 
			SETTING_SPECS.addThenGet(new PositiveIntegerSettingSpec(
					"socks5.onConnect.relayIdleTimeout", 
					null));
	
	@NameValuePairValueSpecDoc(
			description = "The upper limit on bandwidth in bytes per second of "
					+ "receiving inbound data to be relayed",
			name = "socks5.onConnect.relayInboundBandwidthLimit",
			syntax = "socks5.onConnect.relayInboundBandwidthLimit=POSITIVE_INTEGER",
			valueType = PositiveInteger.class
	)
	public static final SettingSpec<PositiveInteger> SOCKS5_ON_CONNECT_RELAY_INBOUND_BANDWIDTH_LIMIT =
			SETTING_SPECS.addThenGet(new PositiveIntegerSettingSpec(
					"socks5.onConnect.relayInboundBandwidthLimit",
					null));
	
	@NameValuePairValueSpecDoc(
			description = "The upper limit on bandwidth in bytes per second of "
					+ "receiving outbound data to be relayed",
			name = "socks5.onConnect.relayOutboundBandwidthLimit",
			syntax = "socks5.onConnect.relayOutboundBandwidthLimit=POSITIVE_INTEGER",
			valueType = PositiveInteger.class
	)
	public static final SettingSpec<PositiveInteger> SOCKS5_ON_CONNECT_RELAY_OUTBOUND_BANDWIDTH_LIMIT =
			SETTING_SPECS.addThenGet(new PositiveIntegerSettingSpec(
					"socks5.onConnect.relayOutboundBandwidthLimit",
					null));
	
	@NameValuePairValueSpecDoc(
			description = "The binding host name or address for the "
					+ "server-facing socket",
			name = "socks5.onConnect.serverFacingBindHost",
			syntax = "socks5.onConnect.serverFacingBindHost=HOST",
			valueType = Host.class
	)
	public static final SettingSpec<Host> SOCKS5_ON_CONNECT_SERVER_FACING_BIND_HOST = 
			SETTING_SPECS.addThenGet(new HostSettingSpec(
					"socks5.onConnect.serverFacingBindHost", 
					null));
	
	@NameValuePairValueSpecDoc(
			description = "The comma separated list of binding port ranges for "
					+ "the server-facing socket",
			name = "socks5.onConnect.serverFacingBindPortRanges",
			syntax = "socks5.onConnect.serverFacingBindPortRanges=PORT_RANGES",
			valueType = PortRanges.class
	)	
	public static final SettingSpec<PortRanges> SOCKS5_ON_CONNECT_SERVER_FACING_BIND_PORT_RANGES =
			SETTING_SPECS.addThenGet(new PortRangesSettingSpec(
					"socks5.onConnect.serverFacingBindPortRanges",
					PortRanges.of()));
	
	@NameValuePairValueSpecDoc(
			description = "The timeout in milliseconds on waiting for the "
					+ "server-facing socket to connect (default is 60000)",
			name = "socks5.onConnect.serverFacingConnectTimeout",
			syntax = "socks5.onConnect.serverFacingConnectTimeout=POSITIVE_INTEGER",
			valueType = PositiveInteger.class
	)
	public static final SettingSpec<PositiveInteger> SOCKS5_ON_CONNECT_SERVER_FACING_CONNECT_TIMEOUT = 
			SETTING_SPECS.addThenGet(new PositiveIntegerSettingSpec(
					"socks5.onConnect.serverFacingConnectTimeout", 
					PositiveInteger.valueOf(60000)));
	
	@NameValuePairValueSpecDoc(
			description = "The comma separated list of socket settings for the "
					+ "server-facing socket",
			name = "socks5.onConnect.serverFacingSocketSettings",
			syntax = "socks5.onConnect.serverFacingSocketSettings=SOCKET_SETTINGS",
			valueType = SocketSettings.class
	)
	public static final SettingSpec<SocketSettings> SOCKS5_ON_CONNECT_SERVER_FACING_SOCKET_SETTINGS = 
			SETTING_SPECS.addThenGet(new SocketSettingsSettingSpec(
					"socks5.onConnect.serverFacingSocketSettings", 
					SocketSettings.of()));
	
	@NameValuePairValueSpecDoc(
			description = "The binding host name or address for the "
					+ "client-facing UDP socket",
			name = "socks5.onUdpAssociate.clientFacingBindHost",
			syntax = "socks5.onUdpAssociate.clientFacingBindHost=HOST",
			valueType = Host.class
	)
	public static final SettingSpec<Host> SOCKS5_ON_UDP_ASSOCIATE_CLIENT_FACING_BIND_HOST = 
			SETTING_SPECS.addThenGet(new HostSettingSpec(
					"socks5.onUdpAssociate.clientFacingBindHost", 
					null));
	
	@NameValuePairValueSpecDoc(
			description = "The comma separated list of binding port ranges for "
					+ "the client-facing UDP socket",
			name = "socks5.onUdpAssociate.clientFacingBindPortRanges",
			syntax = "socks5.onUdpAssociate.clientFacingBindPortRanges=PORT_RANGES",
			valueType = PortRanges.class
	)	
	public static final SettingSpec<PortRanges> SOCKS5_ON_UDP_ASSOCIATE_CLIENT_FACING_BIND_PORT_RANGES =
			SETTING_SPECS.addThenGet(new PortRangesSettingSpec(
					"socks5.onUdpAssociate.clientFacingBindPortRanges",
					PortRanges.of()));
	
	@NameValuePairValueSpecDoc(
			description = "The comma separated list of socket settings for the "
					+ "client-facing UDP socket",
			name = "socks5.onUdpAssociate.clientFacingSocketSettings",
			syntax = "socks5.onUdpAssociate.clientFacingSocketSettings=SOCKET_SETTINGS",
			valueType = SocketSettings.class
	)
	public static final SettingSpec<SocketSettings> SOCKS5_ON_UDP_ASSOCIATE_CLIENT_FACING_SOCKET_SETTINGS = 
			SETTING_SPECS.addThenGet(new SocketSettingsSettingSpec(
					"socks5.onUdpAssociate.clientFacingSocketSettings", 
					SocketSettings.of()));

	@NameValuePairValueSpecDoc(
			description = "The binding host name or address for the peer-facing "
					+ "UDP socket",
			name = "socks5.onUdpAssociate.peerFacingBindHost",
			syntax = "socks5.onUdpAssociate.peerFacingBindHost=HOST",
			valueType = Host.class
	)
	public static final SettingSpec<Host> SOCKS5_ON_UDP_ASSOCIATE_PEER_FACING_BIND_HOST = 
			SETTING_SPECS.addThenGet(new HostSettingSpec(
					"socks5.onUdpAssociate.peerFacingBindHost", 
					null));
	
	@NameValuePairValueSpecDoc(
			description = "The comma separated list of binding port ranges for "
					+ "the peer-facing UDP socket",
			name = "socks5.onUdpAssociate.peerFacingBindPortRanges",
			syntax = "socks5.onUdpAssociate.peerFacingBindPortRanges=PORT_RANGES",
			valueType = PortRanges.class
	)	
	public static final SettingSpec<PortRanges> SOCKS5_ON_UDP_ASSOCIATE_PEER_FACING_BIND_PORT_RANGES =
			SETTING_SPECS.addThenGet(new PortRangesSettingSpec(
					"socks5.onUdpAssociate.peerFacingBindPortRanges",
					PortRanges.of()));
	
	@NameValuePairValueSpecDoc(
			description = "The comma separated list of socket settings for the "
					+ "peer-facing UDP socket",
			name = "socks5.onUdpAssociate.peerFacingSocketSettings",
			syntax = "socks5.onUdpAssociate.peerFacingSocketSettings=SOCKET_SETTINGS",
			valueType = SocketSettings.class
	)
	public static final SettingSpec<SocketSettings> SOCKS5_ON_UDP_ASSOCIATE_PEER_FACING_SOCKET_SETTINGS = 
			SETTING_SPECS.addThenGet(new SocketSettingsSettingSpec(
					"socks5.onUdpAssociate.peerFacingSocketSettings", 
					SocketSettings.of()));
	
	@NameValuePairValueSpecDoc(
			description = "The buffer size in bytes for relaying the data",
			name = "socks5.onUdpAssociate.relayBufferSize",
			syntax = "socks5.onUdpAssociate.relayBufferSize=POSITIVE_INTEGER",
			valueType = PositiveInteger.class
	)
	public static final SettingSpec<PositiveInteger> SOCKS5_ON_UDP_ASSOCIATE_RELAY_BUFFER_SIZE = 
			SETTING_SPECS.addThenGet(new PositiveIntegerSettingSpec(
					"socks5.onUdpAssociate.relayBufferSize", 
					null));
	
	@NameValuePairValueSpecDoc(
			description = "The timeout in milliseconds on relaying no data",
			name = "socks5.onUdpAssociate.relayIdleTimeout",
			syntax = "socks5.onUdpAssociate.relayIdleTimeout=POSITIVE_INTEGER",
			valueType = PositiveInteger.class
	)
	public static final SettingSpec<PositiveInteger> SOCKS5_ON_UDP_ASSOCIATE_RELAY_IDLE_TIMEOUT = 
			SETTING_SPECS.addThenGet(new PositiveIntegerSettingSpec(
					"socks5.onUdpAssociate.relayIdleTimeout", 
					null));
	
	@NameValuePairValueSpecDoc(
			description = "The upper limit on bandwidth in bytes per second of "
					+ "receiving inbound data to be relayed",
			name = "socks5.onUdpAssociate.relayInboundBandwidthLimit",
			syntax = "socks5.onUdpAssociate.relayInboundBandwidthLimit=POSITIVE_INTEGER",
			valueType = PositiveInteger.class
	)
	public static final SettingSpec<PositiveInteger> SOCKS5_ON_UDP_ASSOCIATE_RELAY_INBOUND_BANDWIDTH_LIMIT =
			SETTING_SPECS.addThenGet(new PositiveIntegerSettingSpec(
					"socks5.onUdpAssociate.relayInboundBandwidthLimit",
					null));
	
	@NameValuePairValueSpecDoc(
			description = "The upper limit on bandwidth in bytes per second of "
					+ "receiving outbound data to be relayed",
			name = "socks5.onUdpAssociate.relayOutboundBandwidthLimit",
			syntax = "socks5.onUdpAssociate.relayOutboundBandwidthLimit=POSITIVE_INTEGER",
			valueType = PositiveInteger.class
	)
	public static final SettingSpec<PositiveInteger> SOCKS5_ON_UDP_ASSOCIATE_RELAY_OUTBOUND_BANDWIDTH_LIMIT =
			SETTING_SPECS.addThenGet(new PositiveIntegerSettingSpec(
					"socks5.onUdpAssociate.relayOutboundBandwidthLimit",
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
