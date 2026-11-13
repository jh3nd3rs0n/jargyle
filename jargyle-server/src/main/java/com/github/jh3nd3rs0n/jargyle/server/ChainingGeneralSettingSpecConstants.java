package com.github.jh3nd3rs0n.jargyle.server;

import java.util.List;
import java.util.Map;

import com.github.jh3nd3rs0n.jargyle.client.GeneralPropertySpecConstants;
import com.github.jh3nd3rs0n.jargyle.client.SocksServerUri;
import com.github.jh3nd3rs0n.jargyle.common.net.*;
import com.github.jh3nd3rs0n.jargyle.common.number.NonNegativeInteger;
import com.github.jh3nd3rs0n.jargyle.internal.annotation.NameValuePairValueSpecDoc;
import com.github.jh3nd3rs0n.jargyle.internal.annotation.NameValuePairValueSpecsDoc;
import com.github.jh3nd3rs0n.jargyle.server.internal.settingspec.impl.*;

@NameValuePairValueSpecsDoc(
		description = "",
		name = "Chaining General Settings"
)
public final class ChainingGeneralSettingSpecConstants {

	private static final SettingSpecs SETTING_SPECS = new SettingSpecs();

	@NameValuePairValueSpecDoc(
			description = "The ID for a route through a chain of other SOCKS "
					+ "servers. This setting also marks the current other "
					+ "SOCKS server as the last SOCKS server in the chain of "
					+ "other SOCKS servers",
			name = "chaining.routeId",
			syntax = "chaining.routeId=ROUTE_ID",
			valueType = String.class
	)
	public static final SettingSpec<String> CHAINING_ROUTE_ID =
			SETTING_SPECS.addThenGet(new StringSettingSpec(
					"chaining.routeId",
					null));
	
	@NameValuePairValueSpecDoc(
			defaultValue = "0.0.0.0",
			description = "The binding host name or address for the other "
					+ "SOCKS server TCP socket",
			name = "chaining.socksServerSocket.bindHost",
			syntax = "chaining.socksServerSocket.bindHost=HOST",
			valueType = Host.class
	)
	public static final SettingSpec<Host> CHAINING_SOCKS_SERVER_SOCKET_BIND_HOST = 
			SETTING_SPECS.addThenGet(new HostSettingSpec(
					"chaining.socksServerSocket.bindHost", 
					GeneralPropertySpecConstants.SOCKS_SERVER_SOCKET_BIND_HOST.getDefaultProperty().getValue()));

	@NameValuePairValueSpecDoc(
			defaultValue = "HOST_IPV4_ADDRESS,HOST_IPV6_ADDRESS",
			description = "The comma separated list of acceptable binding "
					+ "host address types for the other SOCKS server TCP "
					+ "socket",
			name = "chaining.socksServerSocket.bindHostAddressTypes",
			syntax = "chaining.socksServerSocket.bindHostAddressTypes=HOST_ADDRESS_TYPES",
			valueType = HostAddressTypes.class
	)
	public static final SettingSpec<HostAddressTypes> CHAINING_SOCKS_SERVER_SOCKET_BIND_HOST_ADDRESS_TYPES =
			SETTING_SPECS.addThenGet(new HostAddressTypesSettingSpec(
					"chaining.socksServerSocket.bindHostAddressTypes",
					GeneralPropertySpecConstants.SOCKS_SERVER_SOCKET_BIND_HOST_ADDRESS_TYPES.getDefaultProperty().getValue()));

	@NameValuePairValueSpecDoc(
			defaultValue = "0",
			description = "The comma separated list of binding port ranges for "
					+ "the other SOCKS server TCP socket",
			name = "chaining.socksServerSocket.bindPortRanges",
			syntax = "chaining.socksServerSocket.bindPortRanges=PORT_RANGES",
			valueType = PortRanges.class
	)
	public static final SettingSpec<PortRanges> CHAINING_SOCKS_SERVER_SOCKET_BIND_PORT_RANGES =
			SETTING_SPECS.addThenGet(new PortRangesSettingSpec(
					"chaining.socksServerSocket.bindPortRanges",
					GeneralPropertySpecConstants.SOCKS_SERVER_SOCKET_BIND_PORT_RANGES.getDefaultProperty().getValue()));
	
	@NameValuePairValueSpecDoc(
			defaultValue = "60000",
			description = "The timeout in milliseconds on waiting for the "
					+ "other SOCKS server TCP socket to connect (a timeout "
					+ "of 0 is interpreted as an infinite timeout)",
			name = "chaining.socksServerSocket.connectTimeout",
			syntax = "chaining.socksServerSocket.connectTimeout=NON_NEGATIVE_INTEGER",
			valueType = NonNegativeInteger.class
	)
	public static final SettingSpec<NonNegativeInteger> CHAINING_SOCKS_SERVER_SOCKET_CONNECT_TIMEOUT =
			SETTING_SPECS.addThenGet(new NonNegativeIntegerSettingSpec(
					"chaining.socksServerSocket.connectTimeout", 
					GeneralPropertySpecConstants.SOCKS_SERVER_SOCKET_CONNECT_TIMEOUT.getDefaultProperty().getValue()));

	@NameValuePairValueSpecDoc(
			description = "The network interface that provides a binding "
					+ "host address for the other SOCKS server TCP socket",
			name = "chaining.socksServerSocket.netInterface",
			syntax = "chaining.socksServerSocket.netInterface=NETWORK_INTERFACE",
			valueType = NetInterface.class
	)
	public static final SettingSpec<NetInterface> CHAINING_SOCKS_SERVER_SOCKET_NET_INTERFACE =
			SETTING_SPECS.addThenGet(new NetInterfaceSettingSpec(
					"chaining.socksServerSocket.netInterface",
					GeneralPropertySpecConstants.SOCKS_SERVER_SOCKET_NET_INTERFACE.getDefaultProperty().getValue()));

	@NameValuePairValueSpecDoc(
			defaultValue = "false",
			description = "The boolean value to indicate if the other SOCKS "
					+ "server TCP socket is to be prepared before connecting "
					+ "(involves applying the specified socket settings, "
					+ "resolving the other SOCKS server host name, and "
					+ "setting the specified timeout on waiting to connect)",
			name = "chaining.socksServerSocket.prepareSocket",
			syntax = "chaining.socksServerSocket.prepareSocket=true|false",
			valueType = Boolean.class
	)
	public static final SettingSpec<Boolean> CHAINING_SOCKS_SERVER_SOCKET_PREPARE_SOCKET =
			SETTING_SPECS.addThenGet(new BooleanSettingSpec(
					"chaining.socksServerSocket.prepareSocket",
					GeneralPropertySpecConstants.SOCKS_SERVER_SOCKET_PREPARE_SOCKET.getDefaultProperty().getValue()));

	@NameValuePairValueSpecDoc(
			description = "The comma separated list of socket settings for "
					+ "the other SOCKS server TCP socket",
			name = "chaining.socksServerSocket.socketSettings",
			syntax = "chaining.socksServerSocket.socketSettings=SOCKET_SETTINGS",
			valueType = SocketSettings.class
	)
	public static final SettingSpec<SocketSettings> CHAINING_SOCKS_SERVER_SOCKET_SOCKET_SETTINGS = 
			SETTING_SPECS.addThenGet(new SocketSettingsSettingSpec(
					"chaining.socksServerSocket.socketSettings", 
					GeneralPropertySpecConstants.SOCKS_SERVER_SOCKET_SOCKET_SETTINGS.getDefaultProperty().getValue()));
	
	@NameValuePairValueSpecDoc(
			description = "The URI of the other SOCKS server",
			name = "chaining.socksServerUri",
			syntax = "chaining.socksServerUri=SOCKS_SERVER_URI",
			valueType = SocksServerUri.class
	)
	public static final SettingSpec<SocksServerUri> CHAINING_SOCKS_SERVER_URI = 
			SETTING_SPECS.addThenGet(new SocksServerUriSettingSpec(
					"chaining.socksServerUri", 
					null));
	
	public static List<SettingSpec<Object>> values() {
		return SETTING_SPECS.toList();
	}
	
	public static Map<String, SettingSpec<Object>> valuesMap() {
		return SETTING_SPECS.toMap();
	}
	
	private ChainingGeneralSettingSpecConstants() { }
	
}
