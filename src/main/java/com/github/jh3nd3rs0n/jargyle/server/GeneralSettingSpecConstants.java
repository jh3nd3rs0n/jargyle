package com.github.jh3nd3rs0n.jargyle.server;

import java.util.List;
import java.util.Map;

import com.github.jh3nd3rs0n.jargyle.common.net.Host;
import com.github.jh3nd3rs0n.jargyle.common.net.Port;
import com.github.jh3nd3rs0n.jargyle.common.net.SocketSettings;
import com.github.jh3nd3rs0n.jargyle.common.number.NonnegativeInteger;
import com.github.jh3nd3rs0n.jargyle.internal.help.HelpText;
import com.github.jh3nd3rs0n.jargyle.server.internal.settingspec.impl.HostSettingSpec;
import com.github.jh3nd3rs0n.jargyle.server.internal.settingspec.impl.LogActionSettingSpec;
import com.github.jh3nd3rs0n.jargyle.server.internal.settingspec.impl.NonnegativeIntegerSettingSpec;
import com.github.jh3nd3rs0n.jargyle.server.internal.settingspec.impl.PortSettingSpec;
import com.github.jh3nd3rs0n.jargyle.server.internal.settingspec.impl.RuleSettingSpec;
import com.github.jh3nd3rs0n.jargyle.server.internal.settingspec.impl.SelectionStrategySettingSpec;
import com.github.jh3nd3rs0n.jargyle.server.internal.settingspec.impl.SocketSettingsSettingSpec;
import com.github.jh3nd3rs0n.jargyle.server.internal.settingspec.impl.StringSettingSpec;

public final class GeneralSettingSpecConstants {

	private static final SettingSpecs SETTING_SPECS = new SettingSpecs();

	@HelpText(
			doc = "The maximum length of the queue of incoming connections "
					+ "(default is 50)", 
			usage = "backlog=INTEGER_BETWEEN_0_AND_2147483647"
	)
	public static final SettingSpec<NonnegativeInteger> BACKLOG = 
			SETTING_SPECS.addThenGet(new NonnegativeIntegerSettingSpec(
					"backlog", 
					NonnegativeInteger.newInstance(50)));
	
	@HelpText(
			doc = "The space separated list of socket settings for the "
					+ "client socket", 
			usage = "clientSocketSettings=[SOCKET_SETTING1[ SOCKET_SETTING2[...]]]"
	)
	public static final SettingSpec<SocketSettings> CLIENT_SOCKET_SETTINGS = 
			SETTING_SPECS.addThenGet(new SocketSettingsSettingSpec(
					"clientSocketSettings", 
					SocketSettings.newInstance()));

	@HelpText(
			doc = "The host name or address for the SOCKS server (default is "
					+ "0.0.0.0)", 
			usage = "host=HOST"
	)
	public static final SettingSpec<Host> HOST = 
			SETTING_SPECS.addThenGet(new HostSettingSpec(
					"host", 
					Host.getAllZerosInet4Instance()));

	@HelpText(
			doc = "The ID for the last and unassigned route "
					+ "(default is lastRoute)", 
			usage = "lastRouteId=ROUTE_ID"
	)	
	public static final SettingSpec<String> LAST_ROUTE_ID = 
			SETTING_SPECS.addThenGet(new StringSettingSpec(
					"lastRouteId",
					"lastRoute"));
	
	@HelpText(
			doc = "The port for the SOCKS server (default is 1080)", 
			usage = "port=INTEGER_BETWEEN_0_AND_65535"
	)
	public static final SettingSpec<Port> PORT = 
			SETTING_SPECS.addThenGet(new PortSettingSpec(
					"port", 
					Port.newInstance(1080)));

	@HelpText(
			doc = "The logging action to take if a route ID is selected from "
					+ "the list of all of the route IDs", 
			usage = "routeIdSelectionLogAction=LOG_ACTION"
	)	
	public static final SettingSpec<LogAction> ROUTE_ID_SELECTION_LOG_ACTION =
			SETTING_SPECS.addThenGet(new LogActionSettingSpec(
					"routeIdSelectionLogAction",
					null));
	
	@HelpText(
			doc = "The selection strategy for the next route ID to use from "
					+ "the list of all of the route IDs (default is CYCLICAL)", 
			usage = "routeIdSelectionStrategy=SELECTION_STRATEGY"
	)
	public static final SettingSpec<SelectionStrategy> ROUTE_ID_SELECTION_STRATEGY =
			SETTING_SPECS.addThenGet(new SelectionStrategySettingSpec(
					"routeIdSelectionStrategy",
					SelectionStrategy.CYCLICAL.newMutableInstance()));
	
	@HelpText(
			doc = "A rule for the SOCKS server "
					+ "(default is firewallAction=ALLOW)", 
			usage = "rule=[RULE_CONDITION1[ RULE_CONDITION2[...]]] [RULE_RESULT1[ RULE_RESULT2[...]]]"
	)	
	public static final SettingSpec<Rule> RULE = 
			SETTING_SPECS.addThenGet(new RuleSettingSpec(
					"rule",
					Rule.getDefault()));
	
	@HelpText(
			doc = "The space separated list of socket settings for the SOCKS "
					+ "server", 
			usage = "socketSettings=[SOCKET_SETTING1[ SOCKET_SETTING2[...]]]"
	)
	public static final SettingSpec<SocketSettings> SOCKET_SETTINGS = 
			SETTING_SPECS.addThenGet(new SocketSettingsSettingSpec(
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
