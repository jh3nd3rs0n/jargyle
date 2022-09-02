package com.github.jh3nd3rs0n.jargyle.server;

import java.util.List;
import java.util.Map;

import com.github.jh3nd3rs0n.jargyle.common.net.SocketSetting;
import com.github.jh3nd3rs0n.jargyle.internal.help.HelpText;
import com.github.jh3nd3rs0n.jargyle.server.internal.ruleresultspec.impl.FirewallActionRuleResultSpec;
import com.github.jh3nd3rs0n.jargyle.server.internal.ruleresultspec.impl.LogActionRuleResultSpec;
import com.github.jh3nd3rs0n.jargyle.server.internal.ruleresultspec.impl.NonnegativeIntegerLimitRuleResultSpec;
import com.github.jh3nd3rs0n.jargyle.server.internal.ruleresultspec.impl.SelectionStrategyRuleResultSpec;
import com.github.jh3nd3rs0n.jargyle.server.internal.ruleresultspec.impl.SocketSettingRuleResultSpec;
import com.github.jh3nd3rs0n.jargyle.server.internal.ruleresultspec.impl.StringRuleResultSpec;

public final class GeneralRuleResultSpecConstants {
	
	private static final RuleResultSpecs RULE_RESULT_SPECS = new RuleResultSpecs();

	@HelpText(
			doc = "Specifies the socket setting for the client socket",
			usage = "clientSocketSetting=SOCKET_SETTING"
	)	
	public static final RuleResultSpec<SocketSetting<Object>> CLIENT_SOCKET_SETTING = RULE_RESULT_SPECS.addThenGet(new SocketSettingRuleResultSpec(
			"clientSocketSetting"));
	
	@HelpText(
			doc = "Specifies the firewall action to take",
			usage = "firewallAction=FIREWALL_ACTION"
	)
	public static final RuleResultSpec<FirewallAction> FIREWALL_ACTION = RULE_RESULT_SPECS.addThenGet(new FirewallActionRuleResultSpec(
			"firewallAction"));
	
	@HelpText(
			doc = "Specifies the limit on the number of simultaneous instances "
					+ "of the rule's firewall action ALLOW",
			usage = "firewallActionAllowLimit=INTEGER_BETWEEN_0_AND_2147483647"
	)
	public static final RuleResultSpec<NonnegativeIntegerLimit> FIREWALL_ACTION_ALLOW_LIMIT = RULE_RESULT_SPECS.addThenGet(new NonnegativeIntegerLimitRuleResultSpec(
			"firewallActionAllowLimit"));
	
	@HelpText(
			doc = "Specifies the logging action to take if the limit on the "
					+ "number of simultaneous instances of the rule's firewall "
					+ "action ALLOW has been reached",
			usage = "firewallActionAllowLimitReachedLogAction=LOG_ACTION"
	)	
	public static final RuleResultSpec<LogAction> FIREWALL_ACTION_ALLOW_LIMIT_REACHED_LOG_ACTION = RULE_RESULT_SPECS.addThenGet(new LogActionRuleResultSpec(
			"firewallActionAllowLimitReachedLogAction"));
	
	@HelpText(
			doc = "Specifies the logging action to take if the firewall "
					+ "action is applied",
			usage = "firewallActionLogAction=LOG_ACTION"
	)	
	public static final RuleResultSpec<LogAction> FIREWALL_ACTION_LOG_ACTION = RULE_RESULT_SPECS.addThenGet(new LogActionRuleResultSpec(
			"firewallActionLogAction"));
	
	@HelpText(
			doc = "Specifies the ID for a route",
			usage = "routeId=ROUTE_ID"
	)
	public static final RuleResultSpec<String> ROUTE_ID = RULE_RESULT_SPECS.addThenGet(new StringRuleResultSpec(
			"routeId"));
	
	@HelpText(
			doc = "Specifies the logging action to take if a route ID is "
					+ "selected from the list of the route IDs",
			usage = "routeIdSelectionLogAction=LOG_ACTION"
	)	
	public static final RuleResultSpec<LogAction> ROUTE_ID_SELECTION_LOG_ACTION = RULE_RESULT_SPECS.addThenGet(new LogActionRuleResultSpec(
			"routeIdSelectionLogAction"));
	
	@HelpText(
			doc = "Specifies the selection strategy for the next route ID to "
					+ "use from the list of route IDs",
			usage = "routeIdSelectionStrategy=SELECTION_STRATEGY"
	)	
	public static final RuleResultSpec<SelectionStrategy> ROUTE_ID_SELECTION_STRATEGY = RULE_RESULT_SPECS.addThenGet(new SelectionStrategyRuleResultSpec(
			"routeIdSelectionStrategy"));
	
	public static List<RuleResultSpec<Object>> values() {
		return RULE_RESULT_SPECS.toList();
	}
	
	public static Map<String, RuleResultSpec<Object>> valuesMap() {
		return RULE_RESULT_SPECS.toMap();
	}
	
	public GeneralRuleResultSpecConstants() { }

}
