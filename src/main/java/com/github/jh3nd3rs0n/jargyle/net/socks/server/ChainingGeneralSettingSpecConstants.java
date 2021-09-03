package com.github.jh3nd3rs0n.jargyle.net.socks.server;

import java.util.Map;

import com.github.jh3nd3rs0n.jargyle.internal.help.HelpText;
import com.github.jh3nd3rs0n.jargyle.net.Host;
import com.github.jh3nd3rs0n.jargyle.net.SocketSettings;
import com.github.jh3nd3rs0n.jargyle.net.socks.client.SocksServerUri;
import com.github.jh3nd3rs0n.jargyle.net.socks.client.GeneralPropertySpecConstants;
import com.github.jh3nd3rs0n.jargyle.net.socks.server.settingspec.impl.HostSettingSpec;
import com.github.jh3nd3rs0n.jargyle.net.socks.server.settingspec.impl.PositiveIntegerSettingSpec;
import com.github.jh3nd3rs0n.jargyle.net.socks.server.settingspec.impl.SocketSettingsSettingSpec;
import com.github.jh3nd3rs0n.jargyle.net.socks.server.settingspec.impl.SocksServerUriSettingSpec;
import com.github.jh3nd3rs0n.jargyle.util.PositiveInteger;

public final class ChainingGeneralSettingSpecConstants {

	private static final SettingSpecs SETTING_SPECS = new SettingSpecs();
	
	@HelpText(
			doc = "The binding host name or address for the internal socket "
					+ "that is used to connect to the other SOCKS server (used "
					+ "for the SOCKS5 commands RESOLVE, BIND and UDP ASSOCIATE) "
					+ "(default is 0.0.0.0)", 
			usage = "chaining.bindHost=HOST"
	)
	public static final SettingSpec<Host> CHAINING_BIND_HOST = 
			SETTING_SPECS.add(new HostSettingSpec(
					NewSettingSpecPermission.INSTANCE, 
					"chaining.bindHost",
					GeneralPropertySpecConstants.BIND_HOST.getDefaultProperty().getValue()));

	@HelpText(
			doc = "The timeout in milliseconds on waiting for the internal "
					+ "socket to connect to the other SOCKS server (used for "
					+ "the SOCKS5 commands RESOLVE, BIND and UDP ASSOCIATE) "
					+ "(default is 60000)", 
			usage = "chaining.connectTimeout=INTEGER_BETWEEN_1_AND_2147483647"
	)
	public static final SettingSpec<PositiveInteger> CHAINING_CONNECT_TIMEOUT = 
			SETTING_SPECS.add(new PositiveIntegerSettingSpec(
					NewSettingSpecPermission.INSTANCE, 
					"chaining.connectTimeout",
					GeneralPropertySpecConstants.CONNECT_TIMEOUT.getDefaultProperty().getValue()));
	
	@HelpText(
			doc = "The space separated list of socket settings for the "
					+ "internal socket that is used to connect to the other "
					+ "SOCKS server (used for the SOCKS5 command RESOLVE and "
					+ "UDP ASSOCIATE)", 
			usage = "chaining.socketSettings=[SOCKET_SETTING1[ SOCKET_SETTING2[...]]]"
	)
	public static final SettingSpec<SocketSettings> CHAINING_SOCKET_SETTINGS = 
			SETTING_SPECS.add(new SocketSettingsSettingSpec(
					NewSettingSpecPermission.INSTANCE, 
					"chaining.socketSettings",
					GeneralPropertySpecConstants.SOCKET_SETTINGS.getDefaultProperty().getValue()));
	
	@HelpText(
			doc = "The URI of the other SOCKS server", 
			usage = "chaining.socksServerUri=SCHEME://HOST[:PORT]"
	)
	public static final SettingSpec<SocksServerUri> CHAINING_SOCKS_SERVER_URI = 
			SETTING_SPECS.add(new SocksServerUriSettingSpec(
					NewSettingSpecPermission.INSTANCE, 
					"chaining.socksServerUri",
					null));
	
	public static Map<String, SettingSpec<Object>> valuesMap() {
		return SETTING_SPECS.toMap();
	}
	
	private ChainingGeneralSettingSpecConstants() { }
	
}
