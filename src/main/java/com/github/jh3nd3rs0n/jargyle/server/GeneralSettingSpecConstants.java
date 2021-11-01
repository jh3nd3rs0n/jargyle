package com.github.jh3nd3rs0n.jargyle.server;

import java.util.List;
import java.util.Map;

import com.github.jh3nd3rs0n.jargyle.common.net.Host;
import com.github.jh3nd3rs0n.jargyle.common.net.Port;
import com.github.jh3nd3rs0n.jargyle.common.net.SocketSettings;
import com.github.jh3nd3rs0n.jargyle.common.number.impl.NonnegativeInteger;
import com.github.jh3nd3rs0n.jargyle.internal.help.HelpText;
import com.github.jh3nd3rs0n.jargyle.server.settingspec.impl.HostSettingSpec;
import com.github.jh3nd3rs0n.jargyle.server.settingspec.impl.NonnegativeIntegerSettingSpec;
import com.github.jh3nd3rs0n.jargyle.server.settingspec.impl.PortSettingSpec;
import com.github.jh3nd3rs0n.jargyle.server.settingspec.impl.RulesSettingSpec;
import com.github.jh3nd3rs0n.jargyle.server.settingspec.impl.SocketSettingsSettingSpec;

public final class GeneralSettingSpecConstants {

	private static final SettingSpecs SETTING_SPECS = new SettingSpecs();

	@HelpText(
			doc = "The maximum length of the queue of incoming connections "
					+ "(default is 50)", 
			usage = "backlog=INTEGER_BETWEEN_0_AND_2147483647"
	)
	public static final SettingSpec<NonnegativeInteger> BACKLOG = 
			SETTING_SPECS.addThenGet(new NonnegativeIntegerSettingSpec(
					NewSettingSpecPermission.INSTANCE, 
					"backlog",
					NonnegativeInteger.newInstance(50)));
	
	@HelpText(
			doc = "The space separated list of socket settings for the "
					+ "client-facing socket", 
			usage = "clientFacingSocketSettings=[SOCKET_SETTING1[ SOCKET_SETTING2[...]]]"
	)
	public static final SettingSpec<SocketSettings> CLIENT_FACING_SOCKET_SETTINGS = 
			SETTING_SPECS.addThenGet(new SocketSettingsSettingSpec(
					NewSettingSpecPermission.INSTANCE, 
					"clientFacingSocketSettings",
					SocketSettings.newInstance()));

	@HelpText(
			doc = "The space separated list of rules for TCP traffic from a "
					+ "client to the SOCKS server (default is ruleAction=ALLOW)", 
			usage = "clientRules=[RULE_FIELD1[ RULE_FIELD2[...]]]"
	)	
	public static final SettingSpec<Rules> CLIENT_RULES =
			SETTING_SPECS.addThenGet(new RulesSettingSpec(
					NewSettingSpecPermission.INSTANCE,
					"clientRules",
					Rules.getDefault()));
	
	@HelpText(
			doc = "The host name or address for the SOCKS server (default is "
					+ "0.0.0.0)", 
			usage = "host=HOST"
	)
	public static final SettingSpec<Host> HOST = 
			SETTING_SPECS.addThenGet(new HostSettingSpec(
					NewSettingSpecPermission.INSTANCE, 
					"host",
					Host.getAllZerosInet4Instance()));
	
	@HelpText(
			doc = "The port for the SOCKS server (default is 1080)", 
			usage = "port=INTEGER_BETWEEN_0_AND_65535"
	)
	public static final SettingSpec<Port> PORT = 
			SETTING_SPECS.addThenGet(new PortSettingSpec(
					NewSettingSpecPermission.INSTANCE, 
					"port",
					Port.newInstance(1080)));
	
	@HelpText(
			doc = "The space separated list of socket settings for the SOCKS "
					+ "server", 
			usage = "socketSettings=[SOCKET_SETTING1[ SOCKET_SETTING2[...]]]"
	)
	public static final SettingSpec<SocketSettings> SOCKET_SETTINGS = 
			SETTING_SPECS.addThenGet(new SocketSettingsSettingSpec(
					NewSettingSpecPermission.INSTANCE, 
					"socketSettings",
					SocketSettings.newInstance()));
	
	public static List<SettingSpec<Object>> values() {
		return SETTING_SPECS.toList();
	}
	
	public static Map<String, SettingSpec<Object>> valuesMap() {
		return SETTING_SPECS.toMap();
	}
	
	private GeneralSettingSpecConstants() { }
	
}
