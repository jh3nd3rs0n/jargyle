package com.github.jh3nd3rs0n.jargyle.server;

import java.util.List;
import java.util.Map;

import com.github.jh3nd3rs0n.jargyle.common.net.Host;
import com.github.jh3nd3rs0n.jargyle.common.net.PortRange;
import com.github.jh3nd3rs0n.jargyle.common.net.SocketSetting;
import com.github.jh3nd3rs0n.jargyle.internal.help.HelpText;
import com.github.jh3nd3rs0n.jargyle.server.internal.ruleresultspec.impl.FirewallActionRuleResultSpec;
import com.github.jh3nd3rs0n.jargyle.server.internal.ruleresultspec.impl.HostRuleResultSpec;
import com.github.jh3nd3rs0n.jargyle.server.internal.ruleresultspec.impl.LogActionRuleResultSpec;
import com.github.jh3nd3rs0n.jargyle.server.internal.ruleresultspec.impl.NonnegativeIntegerLimitRuleResultSpec;
import com.github.jh3nd3rs0n.jargyle.server.internal.ruleresultspec.impl.PortRangeRuleResultSpec;
import com.github.jh3nd3rs0n.jargyle.server.internal.ruleresultspec.impl.SelectionStrategyRuleResultSpec;
import com.github.jh3nd3rs0n.jargyle.server.internal.ruleresultspec.impl.SocketSettingRuleResultSpec;
import com.github.jh3nd3rs0n.jargyle.server.internal.ruleresultspec.impl.StringRuleResultSpec;

public final class GeneralRuleResultSpecConstants {
	
	private static final RuleResultSpecs RULE_RESULT_SPECS = new RuleResultSpecs();

	@HelpText(
			doc = "Specifies the binding host name or address for all sockets",
			usage = "bindHost=HOST"
	)
	public static final RuleResultSpec<Host> BIND_HOST = RULE_RESULT_SPECS.addThenGet(new HostRuleResultSpec(
			"bindHost"));
	
	@HelpText(
			doc = "Specifies a binding port range for all TCP sockets (can be "
					+ "specified multiple times with each rule result "
					+ "specifying another port range)",
			usage = "bindTcpPortRange=PORT|PORT1-PORT2"
	)
	public static final RuleResultSpec<PortRange> BIND_TCP_PORT_RANGE = RULE_RESULT_SPECS.addThenGet(new PortRangeRuleResultSpec(
			"bindTcpPortRange"));
	
	@HelpText(
			doc = "Specifies a binding port range for all UDP sockets (can be "
					+ "specified multiple times with each rule result "
					+ "specifying another port ranges)",
			usage = "bindUdpPortRange=PORT|PORT1-PORT2"
	)
	public static final RuleResultSpec<PortRange> BIND_UDP_PORT_RANGE = RULE_RESULT_SPECS.addThenGet(new PortRangeRuleResultSpec(
			"bindUdpPortRange"));
	
	@HelpText(
			doc = "Specifies a socket setting for the client socket (can be "
					+ "specified multiple times with each rule result "
					+ "specifying another socket setting)",
			usage = "clientSocketSetting=SOCKET_SETTING"
	)	
	public static final RuleResultSpec<SocketSetting<Object>> CLIENT_SOCKET_SETTING = RULE_RESULT_SPECS.addThenGet(new SocketSettingRuleResultSpec(
			"clientSocketSetting"));

	@HelpText(
			doc = "Specifies the binding host name or address for all "
					+ "external-facing sockets",
			usage = "externalFacingBindHost=HOST"
	)
	public static final RuleResultSpec<Host> EXTERNAL_FACING_BIND_HOST = RULE_RESULT_SPECS.addThenGet(new HostRuleResultSpec(
			"externalFacingBindHost"));
	
	@HelpText(
			doc = "Specifies a binding port range for all external-facing TCP "
					+ "sockets (can be specified multiple times with each "
					+ "rule result specifying another port range)",
			usage = "externalFacingBindTcpPortRange=PORT|PORT1-PORT2"
	)
	public static final RuleResultSpec<PortRange> EXTERNAL_FACING_BIND_TCP_PORT_RANGE = RULE_RESULT_SPECS.addThenGet(new PortRangeRuleResultSpec(
			"externalFacingBindTcpPortRange"));
	
	@HelpText(
			doc = "Specifies a binding port range for all external-facing UDP "
					+ "sockets (can be specified multiple times with each "
					+ "rule result specifying another port range)",
			usage = "externalFacingBindUdpPortRange=PORT|PORT1-PORT2"
	)
	public static final RuleResultSpec<PortRange> EXTERNAL_FACING_BIND_UDP_PORT_RANGE = RULE_RESULT_SPECS.addThenGet(new PortRangeRuleResultSpec(
			"externalFacingBindUdpPortRange"));
	
	@HelpText(
			doc = "Specifies a socket setting for all external-facing sockets "
					+ "(can be specified multiple times with each rule result "
					+ "specifying another socket setting)",
			usage = "externalFacingSocketSetting=SOCKET_SETTING"
	)
	public static final RuleResultSpec<SocketSetting<Object>> EXTERNAL_FACING_SOCKET_SETTING = RULE_RESULT_SPECS.addThenGet(new SocketSettingRuleResultSpec(
			"externalFacingSocketSetting"));
	
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
			doc = "Specifies the binding host name or address for all "
					+ "internal-facing sockets",
			usage = "internalFacingBindHost=HOST"
	)
	public static final RuleResultSpec<Host> INTERNAL_FACING_BIND_HOST = RULE_RESULT_SPECS.addThenGet(new HostRuleResultSpec(
			"internalFacingBindHost"));
	
	@HelpText(
			doc = "Specifies a binding port range for all internal-facing TCP "
					+ "sockets (can be specified multiple times with each rule "
					+ "result specifying another port range)",
			usage = "internalFacingBindTcpPortRange=PORT|PORT1-PORT2"
	)
	public static final RuleResultSpec<PortRange> INTERNAL_FACING_BIND_TCP_PORT_RANGE = RULE_RESULT_SPECS.addThenGet(new PortRangeRuleResultSpec(
			"internalFacingBindTcpPortRange"));
	
	@HelpText(
			doc = "Specifies a binding port range for all internal-facing UDP "
					+ "sockets (can be specified multiple times with each rule "
					+ "result specifying another port range)",
			usage = "internalFacingBindUdpPortRange=PORT|PORT1-PORT2"
	)
	public static final RuleResultSpec<PortRange> INTERNAL_FACING_BIND_UDP_PORT_RANGE = RULE_RESULT_SPECS.addThenGet(new PortRangeRuleResultSpec(
			"internalFacingBindUdpPortRange"));
	
	@HelpText(
			doc = "Specifies a socket setting for all internal-facing sockets "
					+ "(can be specified multiple times with each rule result "
					+ "specifying another socket setting)",
			usage = "internalFacingSocketSetting=SOCKET_SETTING"
	)
	public static final RuleResultSpec<SocketSetting<Object>> INTERNAL_FACING_SOCKET_SETTING = RULE_RESULT_SPECS.addThenGet(new SocketSettingRuleResultSpec(
			"internalFacingSocketSetting"));
	
	@HelpText(
			doc = "Specifies the logging action to take if a route ID is "
					+ "selected",
			usage = "routeSelectionLogAction=LOG_ACTION"
	)	
	public static final RuleResultSpec<LogAction> ROUTE_SELECTION_LOG_ACTION = RULE_RESULT_SPECS.addThenGet(new LogActionRuleResultSpec(
			"routeSelectionLogAction"));
	
	@HelpText(
			doc = "Specifies the selection strategy for the next route ID",
			usage = "routeSelectionStrategy=SELECTION_STRATEGY"
	)	
	public static final RuleResultSpec<SelectionStrategy> ROUTE_SELECTION_STRATEGY = RULE_RESULT_SPECS.addThenGet(new SelectionStrategyRuleResultSpec(
			"routeSelectionStrategy"));
	
	@HelpText(
			doc = "Specifies the ID for a selectable route (can be specified "
					+ "multiple times with each rule result specifying another "
					+ "ID for a selectable route)",
			usage = "selectableRouteId=ROUTE_ID"
	)
	public static final RuleResultSpec<String> SELECTABLE_ROUTE_ID = RULE_RESULT_SPECS.addThenGet(new StringRuleResultSpec(
			"selectableRouteId"));
	
	@HelpText(
			doc = "Specifies a socket setting for all sockets (can be "
					+ "specified multiple times with each rule result "
					+ "specifying another socket setting)",
			usage = "socketSetting=SOCKET_SETTING"
	)
	public static final RuleResultSpec<SocketSetting<Object>> SOCKET_SETTING = RULE_RESULT_SPECS.addThenGet(new SocketSettingRuleResultSpec(
			"socketSetting"));
	
	public static List<RuleResultSpec<Object>> values() {
		return RULE_RESULT_SPECS.toList();
	}
	
	public static Map<String, RuleResultSpec<Object>> valuesMap() {
		return RULE_RESULT_SPECS.toMap();
	}
	
	public GeneralRuleResultSpecConstants() { }

}
