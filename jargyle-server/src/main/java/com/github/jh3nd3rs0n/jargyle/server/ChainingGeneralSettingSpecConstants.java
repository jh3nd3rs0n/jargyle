package com.github.jh3nd3rs0n.jargyle.server;

import java.util.List;
import java.util.Map;

import com.github.jh3nd3rs0n.jargyle.client.GeneralPropertySpecConstants;
import com.github.jh3nd3rs0n.jargyle.client.SocksServerUri;
import com.github.jh3nd3rs0n.jargyle.common.net.Host;
import com.github.jh3nd3rs0n.jargyle.common.net.PortRanges;
import com.github.jh3nd3rs0n.jargyle.common.net.SocketSettings;
import com.github.jh3nd3rs0n.jargyle.common.number.PositiveInteger;
import com.github.jh3nd3rs0n.jargyle.internal.annotation.NameValuePairValueSpecDoc;
import com.github.jh3nd3rs0n.jargyle.internal.annotation.NameValuePairValueSpecsDoc;
import com.github.jh3nd3rs0n.jargyle.server.internal.settingspec.impl.HostSettingSpec;
import com.github.jh3nd3rs0n.jargyle.server.internal.settingspec.impl.PortRangesSettingSpec;
import com.github.jh3nd3rs0n.jargyle.server.internal.settingspec.impl.PositiveIntegerSettingSpec;
import com.github.jh3nd3rs0n.jargyle.server.internal.settingspec.impl.SocketSettingsSettingSpec;
import com.github.jh3nd3rs0n.jargyle.server.internal.settingspec.impl.SocksServerUriSettingSpec;
import com.github.jh3nd3rs0n.jargyle.server.internal.settingspec.impl.StringSettingSpec;

@NameValuePairValueSpecsDoc(
		description = "",
		name = "Chaining General Settings"
)
public final class ChainingGeneralSettingSpecConstants {

	private static final SettingSpecs SETTING_SPECS = new SettingSpecs();
	
	@NameValuePairValueSpecDoc(
			description = "The binding host name or address for the client "
					+ "socket that is used to connect to the other SOCKS "
					+ "server (default is 0.0.0.0)",
			name = "chaining.clientBindHost",
			syntax = "chaining.clientBindHost=HOST",
			valueType = Host.class
	)
	public static final SettingSpec<Host> CHAINING_CLIENT_BIND_HOST = 
			SETTING_SPECS.addThenGet(new HostSettingSpec(
					"chaining.clientBindHost", 
					GeneralPropertySpecConstants.CLIENT_BIND_HOST.getDefaultProperty().getValue()));

	@NameValuePairValueSpecDoc(
			description = "The comma separated list of binding port ranges for "
					+ "the client socket that is used to connect to the other "
					+ "SOCKS server (default is 0)",
			name = "chaining.clientBindPortRanges",
			syntax = "chaining.clientBindPortRanges=PORT_RANGES",
			valueType = PortRanges.class
	)
	public static final SettingSpec<PortRanges> CHAINING_CLIENT_BIND_PORT_RANGES =
			SETTING_SPECS.addThenGet(new PortRangesSettingSpec(
					"chaining.clientBindPortRanges",
					GeneralPropertySpecConstants.CLIENT_BIND_PORT_RANGES.getDefaultProperty().getValue()));
	
	@NameValuePairValueSpecDoc(
			description = "The timeout in milliseconds on waiting for the "
					+ "client socket to connect to the other SOCKS server "
					+ "(default is 60000)",
			name = "chaining.clientConnectTimeout",
			syntax = "chaining.clientConnectTimeout=POSITIVE_INTEGER",
			valueType = PositiveInteger.class
	)
	public static final SettingSpec<PositiveInteger> CHAINING_CLIENT_CONNECT_TIMEOUT = 
			SETTING_SPECS.addThenGet(new PositiveIntegerSettingSpec(
					"chaining.clientConnectTimeout", 
					GeneralPropertySpecConstants.CLIENT_CONNECT_TIMEOUT.getDefaultProperty().getValue()));
	
	@NameValuePairValueSpecDoc(
			description = "The comma separated list of socket settings for the "
					+ "client socket that is used to connect to the other "
					+ "SOCKS server",
			name = "chaining.clientSocketSettings",
			syntax = "chaining.clientSocketSettings=SOCKET_SETTINGS",
			valueType = SocketSettings.class
	)
	public static final SettingSpec<SocketSettings> CHAINING_CLIENT_SOCKET_SETTINGS = 
			SETTING_SPECS.addThenGet(new SocketSettingsSettingSpec(
					"chaining.clientSocketSettings", 
					GeneralPropertySpecConstants.CLIENT_SOCKET_SETTINGS.getDefaultProperty().getValue()));

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
