package com.github.jh3nd3rs0n.jargyle.server;

import java.util.List;
import java.util.Map;

import com.github.jh3nd3rs0n.jargyle.common.net.Host;
import com.github.jh3nd3rs0n.jargyle.common.net.PortRanges;
import com.github.jh3nd3rs0n.jargyle.common.net.SocketSettings;
import com.github.jh3nd3rs0n.jargyle.common.number.PositiveInteger;
import com.github.jh3nd3rs0n.jargyle.internal.help.HelpText;
import com.github.jh3nd3rs0n.jargyle.server.internal.settingspec.impl.BooleanSettingSpec;
import com.github.jh3nd3rs0n.jargyle.server.internal.settingspec.impl.HostSettingSpec;
import com.github.jh3nd3rs0n.jargyle.server.internal.settingspec.impl.MethodsSettingSpec;
import com.github.jh3nd3rs0n.jargyle.server.internal.settingspec.impl.PortRangesSettingSpec;
import com.github.jh3nd3rs0n.jargyle.server.internal.settingspec.impl.PositiveIntegerSettingSpec;
import com.github.jh3nd3rs0n.jargyle.server.internal.settingspec.impl.ProtectionLevelsSettingSpec;
import com.github.jh3nd3rs0n.jargyle.server.internal.settingspec.impl.SocketSettingsSettingSpec;
import com.github.jh3nd3rs0n.jargyle.server.internal.settingspec.impl.UserRepositorySettingSpec;
import com.github.jh3nd3rs0n.jargyle.server.socks5.userpassauth.UserRepository;
import com.github.jh3nd3rs0n.jargyle.transport.socks5.Methods;
import com.github.jh3nd3rs0n.jargyle.transport.socks5.gssapiauth.ProtectionLevels;

public final class Socks5SettingSpecConstants {

	private static final SettingSpecs SETTING_SPECS = new SettingSpecs();
	
	@HelpText(
			doc = "The boolean value to indicate if the exchange of the "
					+ "GSS-API protection level negotiation must be "
					+ "unprotected according to the NEC reference "
					+ "implementation (default is false)", 
			usage = "socks5.gssapiauth.necReferenceImpl=true|false"
	)
	public static final SettingSpec<Boolean> SOCKS5_GSSAPIAUTH_NEC_REFERENCE_IMPL = 
			SETTING_SPECS.addThenGet(new BooleanSettingSpec(
					"socks5.gssapiauth.necReferenceImpl", 
					Boolean.FALSE));
	
	@HelpText(
			doc = "The space separated list of acceptable protection levels "
					+ "after GSS-API authentication (The first is preferred "
					+ "if the client does not provide a protection level that "
					+ "is acceptable.) (default is REQUIRED_INTEG_AND_CONF "
					+ "REQUIRED_INTEG NONE)", 
			usage = "socks5.gssapiauth.protectionLevels=SOCKS5_GSSAPIAUTH_PROTECTION_LEVEL1[ SOCKS5_GSSAPIAUTH_PROTECTION_LEVEL2[...]]"
	)
	public static final SettingSpec<ProtectionLevels> SOCKS5_GSSAPIAUTH_PROTECTION_LEVELS = 
			SETTING_SPECS.addThenGet(new ProtectionLevelsSettingSpec(
					"socks5.gssapiauth.protectionLevels", 
					ProtectionLevels.getDefault()));
	
	@HelpText(
			doc = "The space separated list of acceptable authentication "
					+ "methods in order of preference (default is "
					+ "NO_AUTHENTICATION_REQUIRED)", 
			usage = "socks5.methods=[SOCKS5_METHOD1[ SOCKS5_METHOD2[...]]]"
	)
	public static final SettingSpec<Methods> SOCKS5_METHODS = 
			SETTING_SPECS.addThenGet(new MethodsSettingSpec(
					"socks5.methods", 
					Methods.getDefault()));
	
	@HelpText(
			doc = "The space separated list of socket settings for the inbound "
					+ "socket", 
			usage = "socks5.onBind.inboundSocketSettings=[SOCKET_SETTING1[ SOCKET_SETTING2[...]]]"
	)
	public static final SettingSpec<SocketSettings> SOCKS5_ON_BIND_INBOUND_SOCKET_SETTINGS = 
			SETTING_SPECS.addThenGet(new SocketSettingsSettingSpec(
					"socks5.onBind.inboundSocketSettings", 
					SocketSettings.newInstance()));
	
	@HelpText(
			doc = "The binding host name or address for the listen socket if "
					+ "the provided host address is all zeros",
			usage = "socks5.onBind.listenBindHost=HOST"
	)
	public static final SettingSpec<Host> SOCKS5_ON_BIND_LISTEN_BIND_HOST =
			SETTING_SPECS.addThenGet(new HostSettingSpec(
					"socks5.onBind.listenBindHost",
					null));
	
	@HelpText(
			doc = "The space separated list of binding port ranges for the "
					+ "listen socket if the provided port is zero",
			usage = "socks5.onBind.listenBindPortRanges=[PORT_RANGE1[ PORT_RANGE2[ ...]]]"
	)
	public static final SettingSpec<PortRanges> SOCKS5_ON_BIND_LISTEN_BIND_PORT_RANGES =
			SETTING_SPECS.addThenGet(new PortRangesSettingSpec(
					"socks5.onBind.listenBindPortRanges",
					PortRanges.newInstance()));
	
	@HelpText(
			doc = "The space separated list of socket settings for the listen "
					+ "socket", 
			usage = "socks5.onBind.listenSocketSettings=[SOCKET_SETTING1[ SOCKET_SETTING2[...]]]"
	)
	public static final SettingSpec<SocketSettings> SOCKS5_ON_BIND_LISTEN_SOCKET_SETTINGS = 
			SETTING_SPECS.addThenGet(new SocketSettingsSettingSpec(
					"socks5.onBind.listenSocketSettings", 
					SocketSettings.newInstance()));
	
	@HelpText(
			doc = "The buffer size in bytes for relaying the data", 
			usage = "socks5.onBind.relayBufferSize=INTEGER_BETWEEN_1_AND_2147483647"
	)
	public static final SettingSpec<PositiveInteger> SOCKS5_ON_BIND_RELAY_BUFFER_SIZE = 
			SETTING_SPECS.addThenGet(new PositiveIntegerSettingSpec(
					"socks5.onBind.relayBufferSize", 
					null));
	
	@HelpText(
			doc = "The timeout in milliseconds on relaying no data", 
			usage = "socks5.onBind.relayIdleTimeout=INTEGER_BETWEEN_1_AND_2147483647"
	)
	public static final SettingSpec<PositiveInteger> SOCKS5_ON_BIND_RELAY_IDLE_TIMEOUT = 
			SETTING_SPECS.addThenGet(new PositiveIntegerSettingSpec(
					"socks5.onBind.relayIdleTimeout", 
					null));
	
	@HelpText(
			doc = "The upper limit on bandwidth in bytes per second of "
					+ "receiving inbound data to be relayed",
			usage = "socks5.onBind.relayInboundBandwidthLimit=INTEGER_BETWEEN_1_AND_2147483647"
	)
	public static final SettingSpec<PositiveInteger> SOCKS5_ON_BIND_RELAY_INBOUND_BANDWIDTH_LIMIT =
			SETTING_SPECS.addThenGet(new PositiveIntegerSettingSpec(
					"socks5.onBind.relayInboundBandwidthLimit",
					null));
	
	@HelpText(
			doc = "The upper limit on bandwidth in bytes per second of "
					+ "receiving outbound data to be relayed",
			usage = "socks5.onBind.relayOutboundBandwidthLimit=INTEGER_BETWEEN_1_AND_2147483647"
	)
	public static final SettingSpec<PositiveInteger> SOCKS5_ON_BIND_RELAY_OUTBOUND_BANDWIDTH_LIMIT =
			SETTING_SPECS.addThenGet(new PositiveIntegerSettingSpec(
					"socks5.onBind.relayOutboundBandwidthLimit",
					null));
	
	@HelpText(
			doc = "The binding host name or address for all sockets",
			usage = "socks5.onCommand.bindHost=HOST"
	)
	public static final SettingSpec<Host> SOCKS5_ON_COMMAND_BIND_HOST =
			SETTING_SPECS.addThenGet(new HostSettingSpec(
					"socks5.onCommand.bindHost",
					null));
	
	@HelpText(
			doc = "The space separated list of binding port ranges for all TCP "
					+ "sockets",
			usage = "socks5.onCommand.bindTcpPortRanges=[PORT_RANGE1[ PORT_RANGE2[ ...]]]"
	)
	public static final SettingSpec<PortRanges> SOCKS5_ON_COMMAND_BIND_TCP_PORT_RANGES =
			SETTING_SPECS.addThenGet(new PortRangesSettingSpec(
					"socks5.onCommand.bindTcpPortRanges",
					PortRanges.newInstance()));
			
	@HelpText(
			doc = "The space separated list of binding port ranges for all UDP "
					+ "sockets",
			usage = "socks5.onCommand.bindUdpPortRanges=[PORT_RANGE1[ PORT_RANGE2[ ...]]]"
	)
	public static final SettingSpec<PortRanges> SOCKS5_ON_COMMAND_BIND_UDP_PORT_RANGES =
			SETTING_SPECS.addThenGet(new PortRangesSettingSpec(
					"socks5.onCommand.bindUdpPortRanges",
					PortRanges.newInstance()));

	@HelpText(
			doc = "The binding host name or address for all external-facing "
					+ "sockets",
			usage = "socks5.onCommand.externalFacingBindHost=HOST"
	)
	public static final SettingSpec<Host> SOCKS5_ON_COMMAND_EXTERNAL_FACING_BIND_HOST =
			SETTING_SPECS.addThenGet(new HostSettingSpec(
					"socks5.onCommand.externalFacingBindHost",
					null));
	
	@HelpText(
			doc = "The space separated list of binding port ranges for all "
					+ "external-facing TCP sockets",
			usage = "socks5.onCommand.externalFacingBindTcpPortRanges=[PORT_RANGE1[ PORT_RANGE2[ ...]]]"
	)
	public static final SettingSpec<PortRanges> SOCKS5_ON_COMMAND_EXTERNAL_FACING_BIND_TCP_PORT_RANGES =
			SETTING_SPECS.addThenGet(new PortRangesSettingSpec(
					"socks5.onCommand.externalFacingBindTcpPortRanges",
					PortRanges.newInstance()));
			
	@HelpText(
			doc = "The space separated list of binding port ranges for all "
					+ "external-facing UDP sockets",
			usage = "socks5.onCommand.externalFacingBindUdpPortRanges=[PORT_RANGE1[ PORT_RANGE2[ ...]]]"
	)
	public static final SettingSpec<PortRanges> SOCKS5_ON_COMMAND_EXTERNAL_FACING_BIND_UDP_PORT_RANGES =
			SETTING_SPECS.addThenGet(new PortRangesSettingSpec(
					"socks5.onCommand.externalFacingBindUdpPortRanges",
					PortRanges.newInstance()));
	
	@HelpText(
			doc = "The space separated list of socket settings for all "
					+ "external-facing sockets",
			usage = "socks5.onCommand.externalFacingSocketSettings=[SOCKET_SETTING1[ SOCKET_SETTING2[...]]]"
	)
	public static final SettingSpec<SocketSettings> SOCKS5_ON_COMMAND_EXTERNAL_FACING_SOCKET_SETTINGS =
			SETTING_SPECS.addThenGet(new SocketSettingsSettingSpec(
					"socks5.onCommand.externalFacingSocketSettings", 
					SocketSettings.newInstance()));

	@HelpText(
			doc = "The binding host name or address for all internal-facing "
					+ "sockets",
			usage = "socks5.onCommand.internalFacingBindHost=HOST"
	)
	public static final SettingSpec<Host> SOCKS5_ON_COMMAND_INTERNAL_FACING_BIND_HOST =
			SETTING_SPECS.addThenGet(new HostSettingSpec(
					"socks5.onCommand.internalFacingBindHost",
					null));
	
	@HelpText(
			doc = "The space separated list of binding port ranges for all "
					+ "internal-facing TCP sockets",
			usage = "socks5.onCommand.internalFacingBindTcpPortRanges=[PORT_RANGE1[ PORT_RANGE2[ ...]]]"
	)
	public static final SettingSpec<PortRanges> SOCKS5_ON_COMMAND_INTERNAL_FACING_BIND_TCP_PORT_RANGES =
			SETTING_SPECS.addThenGet(new PortRangesSettingSpec(
					"socks5.onCommand.internalFacingBindTcpPortRanges",
					PortRanges.newInstance()));
			
	@HelpText(
			doc = "The space separated list of binding port ranges for all "
					+ "internal-facing UDP sockets",
			usage = "socks5.onCommand.internalFacingBindUdpPortRanges=[PORT_RANGE1[ PORT_RANGE2[ ...]]]"
	)
	public static final SettingSpec<PortRanges> SOCKS5_ON_COMMAND_INTERNAL_FACING_BIND_UDP_PORT_RANGES =
			SETTING_SPECS.addThenGet(new PortRangesSettingSpec(
					"socks5.onCommand.internalFacingBindUdpPortRanges",
					PortRanges.newInstance()));
	
	@HelpText(
			doc = "The space separated list of socket settings for all "
					+ "internal-facing sockets",
			usage = "socks5.onCommand.internalFacingSocketSettings=[SOCKET_SETTING1[ SOCKET_SETTING2[...]]]"
	)
	public static final SettingSpec<SocketSettings> SOCKS5_ON_COMMAND_INTERNAL_FACING_SOCKET_SETTINGS =
			SETTING_SPECS.addThenGet(new SocketSettingsSettingSpec(
					"socks5.onCommand.internalFacingSocketSettings", 
					SocketSettings.newInstance()));
	
	@HelpText(
			doc = "The buffer size in bytes for relaying the data (default is "
					+ "1024)", 
			usage = "socks5.onCommand.relayBufferSize=INTEGER_BETWEEN_1_AND_2147483647"
	)
	public static final SettingSpec<PositiveInteger> SOCKS5_ON_COMMAND_RELAY_BUFFER_SIZE =
			SETTING_SPECS.addThenGet(new PositiveIntegerSettingSpec(
					"socks5.onCommand.relayBufferSize",
					PositiveInteger.newInstance(1024)));
	
	@HelpText(
			doc = "The timeout in milliseconds on relaying no data (default is "
					+ "60000", 
			usage = "socks5.onCommand.relayIdleTimeout=INTEGER_BETWEEN_1_AND_2147483647"
	)	
	public static final SettingSpec<PositiveInteger> SOCKS5_ON_COMMAND_RELAY_IDLE_TIMEOUT =
			SETTING_SPECS.addThenGet(new PositiveIntegerSettingSpec(
					"socks5.onCommand.relayIdleTimeout",
					PositiveInteger.newInstance(60000)));
	
	@HelpText(
			doc = "The upper limit on bandwidth in bytes per second of "
					+ "receiving inbound data to be relayed",
			usage = "socks5.onCommand.relayInboundBandwidthLimit=INTEGER_BETWEEN_1_AND_2147483647"
	)	
	public static final SettingSpec<PositiveInteger> SOCKS5_ON_COMMAND_RELAY_INBOUND_BANDWIDTH_LIMIT =
			SETTING_SPECS.addThenGet(new PositiveIntegerSettingSpec(
					"socks5.onCommand.relayInboundBandwidthLimit",
					null));
	
	@HelpText(
			doc = "The upper limit on bandwidth in bytes per second of "
					+ "receiving outbound data to be relayed",
			usage = "socks5.onCommand.relayOutboundBandwidthLimit=INTEGER_BETWEEN_1_AND_2147483647"
	)	
	public static final SettingSpec<PositiveInteger> SOCKS5_ON_COMMAND_RELAY_OUTBOUND_BANDWIDTH_LIMIT =
			SETTING_SPECS.addThenGet(new PositiveIntegerSettingSpec(
					"socks5.onCommand.relayOutboundBandwidthLimit",
					null));	
	
	@HelpText(
			doc = "The space separated list of socket settings for all sockets",
			usage = "socks5.onCommand.socketSettings=[SOCKET_SETTING1[ SOCKET_SETTING2[...]]]"
	)
	public static final SettingSpec<SocketSettings> SOCKS5_ON_COMMAND_SOCKET_SETTINGS =
			SETTING_SPECS.addThenGet(new SocketSettingsSettingSpec(
					"socks5.onCommand.socketSettings", 
					SocketSettings.newInstance()));
	
	@HelpText(
			doc = "The boolean value to indicate if the server-facing socket "
					+ "is to be prepared before connecting (involves applying "
					+ "the specified socket settings, resolving the target "
					+ "host name, and setting the specified timeout on waiting "
					+ "to connect) (default is false)", 
			usage = "socks5.onConnect.prepareServerFacingSocket=true|false"
	)	
	public static final SettingSpec<Boolean> SOCKS5_ON_CONNECT_PREPARE_SERVER_FACING_SOCKET = 
			SETTING_SPECS.addThenGet(new BooleanSettingSpec(
					"socks5.onConnect.prepareServerFacingSocket", 
					Boolean.FALSE));
	
	@HelpText(
			doc = "The buffer size in bytes for relaying the data", 
			usage = "socks5.onConnect.relayBufferSize=INTEGER_BETWEEN_1_AND_2147483647"
	)
	public static final SettingSpec<PositiveInteger> SOCKS5_ON_CONNECT_RELAY_BUFFER_SIZE = 
			SETTING_SPECS.addThenGet(new PositiveIntegerSettingSpec(
					"socks5.onConnect.relayBufferSize", 
					null));
	
	@HelpText(
			doc = "The timeout in milliseconds on relaying no data", 
			usage = "socks5.onConnect.relayIdleTimeout=INTEGER_BETWEEN_1_AND_2147483647"
	)
	public static final SettingSpec<PositiveInteger> SOCKS5_ON_CONNECT_RELAY_IDLE_TIMEOUT = 
			SETTING_SPECS.addThenGet(new PositiveIntegerSettingSpec(
					"socks5.onConnect.relayIdleTimeout", 
					null));
	
	@HelpText(
			doc = "The upper limit on bandwidth in bytes per second of "
					+ "receiving inbound data to be relayed",
			usage = "socks5.onConnect.relayInboundBandwidthLimit=INTEGER_BETWEEN_1_AND_2147483647"
	)
	public static final SettingSpec<PositiveInteger> SOCKS5_ON_CONNECT_RELAY_INBOUND_BANDWIDTH_LIMIT =
			SETTING_SPECS.addThenGet(new PositiveIntegerSettingSpec(
					"socks5.onConnect.relayInboundBandwidthLimit",
					null));
	
	@HelpText(
			doc = "The upper limit on bandwidth in bytes per second of "
					+ "receiving outbound data to be relayed",
			usage = "socks5.onConnect.relayOutboundBandwidthLimit=INTEGER_BETWEEN_1_AND_2147483647"
	)
	public static final SettingSpec<PositiveInteger> SOCKS5_ON_CONNECT_RELAY_OUTBOUND_BANDWIDTH_LIMIT =
			SETTING_SPECS.addThenGet(new PositiveIntegerSettingSpec(
					"socks5.onConnect.relayOutboundBandwidthLimit",
					null));
	
	@HelpText(
			doc = "The binding host name or address for the server-facing "
					+ "socket", 
			usage = "socks5.onConnect.serverFacingBindHost=HOST"
	)
	public static final SettingSpec<Host> SOCKS5_ON_CONNECT_SERVER_FACING_BIND_HOST = 
			SETTING_SPECS.addThenGet(new HostSettingSpec(
					"socks5.onConnect.serverFacingBindHost", 
					null));
	
	@HelpText(
			doc = "The space separated list of binding port ranges for the "
					+ "server-facing socket", 
			usage = "socks5.onConnect.serverFacingBindPortRanges=[PORT_RANGE1[ PORT_RANGE2[ ...]]]"
	)	
	public static final SettingSpec<PortRanges> SOCKS5_ON_CONNECT_SERVER_FACING_BIND_PORT_RANGES =
			SETTING_SPECS.addThenGet(new PortRangesSettingSpec(
					"socks5.onConnect.serverFacingBindPortRanges",
					PortRanges.newInstance()));
	
	@HelpText(
			doc = "The timeout in milliseconds on waiting for the "
					+ "server-facing socket to connect (default is 60000)", 
			usage = "socks5.onConnect.serverFacingConnectTimeout=INTEGER_BETWEEN_1_AND_2147483647"
	)
	public static final SettingSpec<PositiveInteger> SOCKS5_ON_CONNECT_SERVER_FACING_CONNECT_TIMEOUT = 
			SETTING_SPECS.addThenGet(new PositiveIntegerSettingSpec(
					"socks5.onConnect.serverFacingConnectTimeout", 
					PositiveInteger.newInstance(60000)));
	
	@HelpText(
			doc = "The space separated list of socket settings for the "
					+ "server-facing socket", 
			usage = "socks5.onConnect.serverFacingSocketSettings=[SOCKET_SETTING1[ SOCKET_SETTING2[...]]]"
	)
	public static final SettingSpec<SocketSettings> SOCKS5_ON_CONNECT_SERVER_FACING_SOCKET_SETTINGS = 
			SETTING_SPECS.addThenGet(new SocketSettingsSettingSpec(
					"socks5.onConnect.serverFacingSocketSettings", 
					SocketSettings.newInstance()));
	
	@HelpText(
			doc = "The binding host name or address for the client-facing UDP "
					+ "socket", 
			usage = "socks5.onUdpAssociate.clientFacingBindHost=HOST"
	)
	public static final SettingSpec<Host> SOCKS5_ON_UDP_ASSOCIATE_CLIENT_FACING_BIND_HOST = 
			SETTING_SPECS.addThenGet(new HostSettingSpec(
					"socks5.onUdpAssociate.clientFacingBindHost", 
					null));
	
	@HelpText(
			doc = "The space separated list of binding port ranges for the "
					+ "client-facing UDP socket", 
			usage = "socks5.onUdpAssociate.clientFacingBindPortRanges=[PORT_RANGE1[ PORT_RANGE2[ ...]]]"
	)	
	public static final SettingSpec<PortRanges> SOCKS5_ON_UDP_ASSOCIATE_CLIENT_FACING_BIND_PORT_RANGES =
			SETTING_SPECS.addThenGet(new PortRangesSettingSpec(
					"socks5.onUdpAssociate.clientFacingBindPortRanges",
					PortRanges.newInstance()));
	
	@HelpText(
			doc = "The space separated list of socket settings for the "
					+ "client-facing UDP socket", 
			usage = "socks5.onUdpAssociate.clientFacingSocketSettings=[SOCKET_SETTING1[ SOCKET_SETTING2[...]]]"
	)
	public static final SettingSpec<SocketSettings> SOCKS5_ON_UDP_ASSOCIATE_CLIENT_FACING_SOCKET_SETTINGS = 
			SETTING_SPECS.addThenGet(new SocketSettingsSettingSpec(
					"socks5.onUdpAssociate.clientFacingSocketSettings", 
					SocketSettings.newInstance()));

	@HelpText(
			doc = "The binding host name or address for the peer-facing UDP "
					+ "socket", 
			usage = "socks5.onUdpAssociate.peerFacingBindHost=HOST"
	)
	public static final SettingSpec<Host> SOCKS5_ON_UDP_ASSOCIATE_PEER_FACING_BIND_HOST = 
			SETTING_SPECS.addThenGet(new HostSettingSpec(
					"socks5.onUdpAssociate.peerFacingBindHost", 
					null));
	
	@HelpText(
			doc = "The space separated list of binding port ranges for the "
					+ "peer-facing UDP socket", 
			usage = "socks5.onUdpAssociate.peerFacingBindPortRanges=[PORT_RANGE1[ PORT_RANGE2[ ...]]]"
	)	
	public static final SettingSpec<PortRanges> SOCKS5_ON_UDP_ASSOCIATE_PEER_FACING_BIND_PORT_RANGES =
			SETTING_SPECS.addThenGet(new PortRangesSettingSpec(
					"socks5.onUdpAssociate.peerFacingBindPortRanges",
					PortRanges.newInstance()));
	
	@HelpText(
			doc = "The space separated list of socket settings for the "
					+ "peer-facing UDP socket", 
			usage = "socks5.onUdpAssociate.peerFacingSocketSettings=[SOCKET_SETTING1[ SOCKET_SETTING2[...]]]"
	)
	public static final SettingSpec<SocketSettings> SOCKS5_ON_UDP_ASSOCIATE_PEER_FACING_SOCKET_SETTINGS = 
			SETTING_SPECS.addThenGet(new SocketSettingsSettingSpec(
					"socks5.onUdpAssociate.peerFacingSocketSettings", 
					SocketSettings.newInstance()));
	
	@HelpText(
			doc = "The buffer size in bytes for relaying the data", 
			usage = "socks5.onUdpAssociate.relayBufferSize=INTEGER_BETWEEN_1_AND_2147483647"
	)
	public static final SettingSpec<PositiveInteger> SOCKS5_ON_UDP_ASSOCIATE_RELAY_BUFFER_SIZE = 
			SETTING_SPECS.addThenGet(new PositiveIntegerSettingSpec(
					"socks5.onUdpAssociate.relayBufferSize", 
					null));
	
	@HelpText(
			doc = "The timeout in milliseconds on relaying no data", 
			usage = "socks5.onUdpAssociate.relayIdleTimeout=INTEGER_BETWEEN_1_AND_2147483647"
	)
	public static final SettingSpec<PositiveInteger> SOCKS5_ON_UDP_ASSOCIATE_RELAY_IDLE_TIMEOUT = 
			SETTING_SPECS.addThenGet(new PositiveIntegerSettingSpec(
					"socks5.onUdpAssociate.relayIdleTimeout", 
					null));
	
	@HelpText(
			doc = "The upper limit on bandwidth in bytes per second of "
					+ "receiving inbound data to be relayed",
			usage = "socks5.onUdpAssociate.relayInboundBandwidthLimit=INTEGER_BETWEEN_1_AND_2147483647"
	)
	public static final SettingSpec<PositiveInteger> SOCKS5_ON_UDP_ASSOCIATE_RELAY_INBOUND_BANDWIDTH_LIMIT =
			SETTING_SPECS.addThenGet(new PositiveIntegerSettingSpec(
					"socks5.onUdpAssociate.relayInboundBandwidthLimit",
					null));
	
	@HelpText(
			doc = "The upper limit on bandwidth in bytes per second of "
					+ "receiving outbound data to be relayed",
			usage = "socks5.onUdpAssociate.relayOutboundBandwidthLimit=INTEGER_BETWEEN_1_AND_2147483647"
	)
	public static final SettingSpec<PositiveInteger> SOCKS5_ON_UDP_ASSOCIATE_RELAY_OUTBOUND_BANDWIDTH_LIMIT =
			SETTING_SPECS.addThenGet(new PositiveIntegerSettingSpec(
					"socks5.onUdpAssociate.relayOutboundBandwidthLimit",
					null));
	
	@HelpText(
			doc = "The user repository used for username password authentication", 
			usage = "socks5.userpassauth.userRepository=CLASS_NAME:INITIALIZATION_VALUE"
	)	
	public static final SettingSpec<UserRepository> SOCKS5_USERPASSAUTH_USER_REPOSITORY = 
			SETTING_SPECS.addThenGet(new UserRepositorySettingSpec(
					"socks5.userpassauth.userRepository",
					UserRepository.newInstance()));
	
	public static List<SettingSpec<Object>> values() {
		return SETTING_SPECS.toList();
	}
	
	public static Map<String, SettingSpec<Object>> valuesMap() {
		return SETTING_SPECS.toMap();
	}
	
	private Socks5SettingSpecConstants() { }
}
