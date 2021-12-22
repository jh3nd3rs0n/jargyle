package com.github.jh3nd3rs0n.jargyle.server;

import java.util.List;
import java.util.Map;

import com.github.jh3nd3rs0n.jargyle.common.net.Host;
import com.github.jh3nd3rs0n.jargyle.common.net.Port;
import com.github.jh3nd3rs0n.jargyle.common.net.SocketSettings;
import com.github.jh3nd3rs0n.jargyle.common.number.impl.NonnegativeInteger;
import com.github.jh3nd3rs0n.jargyle.internal.help.HelpText;
import com.github.jh3nd3rs0n.jargyle.server.settingspec.impl.ClientRoutingRulesSettingSpec;
import com.github.jh3nd3rs0n.jargyle.server.settingspec.impl.ClientFirewallRulesSettingSpec;
import com.github.jh3nd3rs0n.jargyle.server.settingspec.impl.HostSettingSpec;
import com.github.jh3nd3rs0n.jargyle.server.settingspec.impl.LogActionSettingSpec;
import com.github.jh3nd3rs0n.jargyle.server.settingspec.impl.NonnegativeIntegerSettingSpec;
import com.github.jh3nd3rs0n.jargyle.server.settingspec.impl.PortSettingSpec;
import com.github.jh3nd3rs0n.jargyle.server.settingspec.impl.SelectionStrategySettingSpec;
import com.github.jh3nd3rs0n.jargyle.server.settingspec.impl.SocketSettingsSettingSpec;
import com.github.jh3nd3rs0n.jargyle.server.settingspec.impl.StringSettingSpec;

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
			doc = "The space separated list of firewall rules for TCP traffic "
					+ "from a client to the SOCKS server "
					+ "(default is firewallRuleAction=ALLOW)", 
			usage = "clientFirewallRules=[CLIENT_FIREWALL_RULE_FIELD1[ CLIENT_FIREWALL_RULE_FIELD2[...]]]"
	)	
	public static final SettingSpec<ClientFirewallRules> CLIENT_FIREWALL_RULES =
			SETTING_SPECS.addThenGet(new ClientFirewallRulesSettingSpec(
					NewSettingSpecPermission.INSTANCE,
					"clientFirewallRules",
					ClientFirewallRules.getDefault()));
	
	@HelpText(
			doc = "The space separated list of routing rules for a client", 
			usage = "clientRoutingRules=[CLIENT_ROUTING_RULE_FIELD1[ CLIENT_ROUTING_RULE_FIELD2[...]]]"
	)	
	public static final SettingSpec<ClientRoutingRules> CLIENT_ROUTING_RULES =
			SETTING_SPECS.addThenGet(new ClientRoutingRulesSettingSpec(
					NewSettingSpecPermission.INSTANCE,
					"clientRoutingRules",
					ClientRoutingRules.getDefault()));
	
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
			doc = "The ID for the last and unassigned route "
					+ "(default is lastRoute)", 
			usage = "lastRouteId=ROUTE_ID"
	)	
	public static final SettingSpec<String> LAST_ROUTE_ID = 
			SETTING_SPECS.addThenGet(new StringSettingSpec(
					NewSettingSpecPermission.INSTANCE,
					"lastRouteId",
					"lastRoute"));
	
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
			doc = "The logging action to take if a route is selected from the "
					+ "list of all of the route IDs", 
			usage = "routeSelectionLogAction=LOG_ACTION"
	)	
	public static final SettingSpec<LogAction> ROUTE_SELECTION_LOG_ACTION =
			SETTING_SPECS.addThenGet(new LogActionSettingSpec(
					NewSettingSpecPermission.INSTANCE,
					"routeSelectionLogAction",
					null));
	
	@HelpText(
			doc = "The selection strategy for the next route to use from the "
					+ "list of all of the route IDs (default is CYCLICAL)", 
			usage = "routeSelectionStrategy=SELECTION_STRATEGY"
	)
	public static final SettingSpec<SelectionStrategy> ROUTE_SELECTION_STRATEGY =
			SETTING_SPECS.addThenGet(new SelectionStrategySettingSpec(
					NewSettingSpecPermission.INSTANCE,
					"routeSelectionStrategy",
					SelectionStrategy.CYCLICAL));
	
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
