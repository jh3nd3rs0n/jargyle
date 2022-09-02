package com.github.jh3nd3rs0n.jargyle.server;

import java.util.List;
import java.util.Map;

import com.github.jh3nd3rs0n.jargyle.client.GeneralPropertySpecConstants;
import com.github.jh3nd3rs0n.jargyle.client.SocksServerUri;
import com.github.jh3nd3rs0n.jargyle.common.net.Host;
import com.github.jh3nd3rs0n.jargyle.common.net.PortRanges;
import com.github.jh3nd3rs0n.jargyle.common.net.SocketSettings;
import com.github.jh3nd3rs0n.jargyle.common.number.PositiveInteger;
import com.github.jh3nd3rs0n.jargyle.internal.help.HelpText;
import com.github.jh3nd3rs0n.jargyle.server.internal.settingspec.impl.HostSettingSpec;
import com.github.jh3nd3rs0n.jargyle.server.internal.settingspec.impl.PortRangesSettingSpec;
import com.github.jh3nd3rs0n.jargyle.server.internal.settingspec.impl.PositiveIntegerSettingSpec;
import com.github.jh3nd3rs0n.jargyle.server.internal.settingspec.impl.SocketSettingsSettingSpec;
import com.github.jh3nd3rs0n.jargyle.server.internal.settingspec.impl.SocksServerUriSettingSpec;
import com.github.jh3nd3rs0n.jargyle.server.internal.settingspec.impl.StringSettingSpec;

public final class ChainingGeneralSettingSpecConstants {

	private static final SettingSpecs SETTING_SPECS = new SettingSpecs();
	
	@HelpText(
			doc = "The binding host name or address for the internal socket "
					+ "that is used to connect to the other SOCKS server (used "
					+ "for the SOCKS5 commands RESOLVE, BIND and UDP ASSOCIATE) "
					+ "(default is 0.0.0.0)", 
			usage = "chaining.internalBindHost=HOST"
	)
	public static final SettingSpec<Host> CHAINING_INTERNAL_BIND_HOST = 
			SETTING_SPECS.addThenGet(new HostSettingSpec(
					"chaining.internalBindHost", 
					GeneralPropertySpecConstants.INTERNAL_BIND_HOST.getDefaultProperty().getValue()));

	@HelpText(
			doc = "The space separated list of binding port ranges for the "
					+ "internal socket that is used to connect to the other "
					+ "SOCKS server (used for the SOCKS5 commands RESOLVE, "
					+ "BIND and UDP ASSOCIATE) (default is 0)", 
			usage = "chaining.internalBindPortRanges=[PORT_RANGE1[ PORT_RANGE2[ ...]]]"
	)
	public static final SettingSpec<PortRanges> CHAINING_INTERNAL_BIND_PORT_RANGES =
			SETTING_SPECS.addThenGet(new PortRangesSettingSpec(
					"chaining.internalBindPortRanges",
					GeneralPropertySpecConstants.INTERNAL_BIND_PORT_RANGES.getDefaultProperty().getValue()));
	
	@HelpText(
			doc = "The timeout in milliseconds on waiting for the internal "
					+ "socket to connect to the other SOCKS server (used for "
					+ "the SOCKS5 commands RESOLVE, BIND and UDP ASSOCIATE) "
					+ "(default is 60000)", 
			usage = "chaining.internalConnectTimeout=INTEGER_BETWEEN_1_AND_2147483647"
	)
	public static final SettingSpec<PositiveInteger> CHAINING_INTERNAL_CONNECT_TIMEOUT = 
			SETTING_SPECS.addThenGet(new PositiveIntegerSettingSpec(
					"chaining.internalConnectTimeout", 
					GeneralPropertySpecConstants.INTERNAL_CONNECT_TIMEOUT.getDefaultProperty().getValue()));
	
	@HelpText(
			doc = "The space separated list of socket settings for the "
					+ "internal socket that is used to connect to the other "
					+ "SOCKS server (used for the SOCKS5 command RESOLVE and "
					+ "UDP ASSOCIATE)", 
			usage = "chaining.internalSocketSettings=[SOCKET_SETTING1[ SOCKET_SETTING2[...]]]"
	)
	public static final SettingSpec<SocketSettings> CHAINING_INTERNAL_SOCKET_SETTINGS = 
			SETTING_SPECS.addThenGet(new SocketSettingsSettingSpec(
					"chaining.internalSocketSettings", 
					GeneralPropertySpecConstants.INTERNAL_SOCKET_SETTINGS.getDefaultProperty().getValue()));

	@HelpText(
			doc = "The ID for a route through a chain of other SOCKS servers. "
					+ "This setting also marks the current other SOCKS server "
					+ "as the last SOCKS server in the chain of other SOCKS "
					+ "servers", 
			usage = "chaining.routeId=ROUTE_ID"
	)
	public static final SettingSpec<String> CHAINING_ROUTE_ID = 
			SETTING_SPECS.addThenGet(new StringSettingSpec(
					"chaining.routeId",
					null));
	
	@HelpText(
			doc = "The URI of the other SOCKS server", 
			usage = "chaining.socksServerUri=SCHEME://HOST[:PORT]"
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
