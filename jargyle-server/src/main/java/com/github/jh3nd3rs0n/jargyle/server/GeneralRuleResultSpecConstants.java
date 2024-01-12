package com.github.jh3nd3rs0n.jargyle.server;

import java.util.List;
import java.util.Map;

import com.github.jh3nd3rs0n.jargyle.common.net.Host;
import com.github.jh3nd3rs0n.jargyle.common.net.PortRange;
import com.github.jh3nd3rs0n.jargyle.common.net.SocketSetting;
import com.github.jh3nd3rs0n.jargyle.common.number.NonNegativeInteger;
import com.github.jh3nd3rs0n.jargyle.internal.annotation.NameValuePairValueSpecDoc;
import com.github.jh3nd3rs0n.jargyle.internal.annotation.NameValuePairValueSpecsDoc;
import com.github.jh3nd3rs0n.jargyle.server.internal.ruleresultspec.impl.FirewallActionRuleResultSpec;
import com.github.jh3nd3rs0n.jargyle.server.internal.ruleresultspec.impl.HostRuleResultSpec;
import com.github.jh3nd3rs0n.jargyle.server.internal.ruleresultspec.impl.LogActionRuleResultSpec;
import com.github.jh3nd3rs0n.jargyle.server.internal.ruleresultspec.impl.NonnegativeIntegerLimitRuleResultSpec;
import com.github.jh3nd3rs0n.jargyle.server.internal.ruleresultspec.impl.PortRangeRuleResultSpec;
import com.github.jh3nd3rs0n.jargyle.server.internal.ruleresultspec.impl.SelectionStrategyRuleResultSpec;
import com.github.jh3nd3rs0n.jargyle.server.internal.ruleresultspec.impl.SocketSettingRuleResultSpec;
import com.github.jh3nd3rs0n.jargyle.server.internal.ruleresultspec.impl.StringRuleResultSpec;

@NameValuePairValueSpecsDoc(
		description = "",
		name = "General Rule Results"
)
public final class GeneralRuleResultSpecConstants {
	
	private static final RuleResultSpecs RULE_RESULT_SPECS = new RuleResultSpecs();

	@NameValuePairValueSpecDoc(
			description = "Specifies the binding host name or address for all "
					+ "sockets",
			name = "bindHost",
			syntax = "bindHost=HOST",
			valueType = Host.class
	)
	public static final RuleResultSpec<Host> BIND_HOST = RULE_RESULT_SPECS.addThenGet(new HostRuleResultSpec(
			"bindHost"));
	
	@NameValuePairValueSpecDoc(
			description = "Specifies a binding port range for all TCP sockets "
					+ "(can be specified multiple times with each rule result "
					+ "specifying another port range)",
			name = "bindTcpPortRange",
			syntax = "bindTcpPortRange=PORT_RANGE",
			valueType = PortRange.class
	)
	public static final RuleResultSpec<PortRange> BIND_TCP_PORT_RANGE = RULE_RESULT_SPECS.addThenGet(new PortRangeRuleResultSpec(
			"bindTcpPortRange"));
	
	@NameValuePairValueSpecDoc(
			description = "Specifies a binding port range for all UDP sockets "
					+ "(can be specified multiple times with each rule result "
					+ "specifying another port ranges)",
			name = "bindUdpPortRange",
			syntax = "bindUdpPortRange=PORT_RANGE",
			valueType = PortRange.class
	)
	public static final RuleResultSpec<PortRange> BIND_UDP_PORT_RANGE = RULE_RESULT_SPECS.addThenGet(new PortRangeRuleResultSpec(
			"bindUdpPortRange"));
	
	@NameValuePairValueSpecDoc(
			description = "Specifies a socket setting for the client socket "
					+ "(can be specified multiple times with each rule result "
					+ "specifying another socket setting)",
			name = "clientSocketSetting",
			syntax = "clientSocketSetting=SOCKET_SETTING",
			valueType = SocketSetting.class
	)	
	public static final RuleResultSpec<SocketSetting<Object>> CLIENT_SOCKET_SETTING = RULE_RESULT_SPECS.addThenGet(new SocketSettingRuleResultSpec(
			"clientSocketSetting"));

	@NameValuePairValueSpecDoc(
			description = "Specifies the binding host name or address for all "
					+ "external-facing sockets",
			name = "externalFacingBindHost",
			syntax = "externalFacingBindHost=HOST",
			valueType = Host.class
	)
	public static final RuleResultSpec<Host> EXTERNAL_FACING_BIND_HOST = RULE_RESULT_SPECS.addThenGet(new HostRuleResultSpec(
			"externalFacingBindHost"));
	
	@NameValuePairValueSpecDoc(
			description = "Specifies a binding port range for all "
					+ "external-facing TCP sockets (can be specified multiple "
					+ "times with each rule result specifying another port "
					+ "range)",
			name = "externalFacingBindTcpPortRange",
			syntax = "externalFacingBindTcpPortRange=PORT_RANGE",
			valueType = PortRange.class
	)
	public static final RuleResultSpec<PortRange> EXTERNAL_FACING_BIND_TCP_PORT_RANGE = RULE_RESULT_SPECS.addThenGet(new PortRangeRuleResultSpec(
			"externalFacingBindTcpPortRange"));
	
	@NameValuePairValueSpecDoc(
			description = "Specifies a binding port range for all "
					+ "external-facing UDP sockets (can be specified multiple "
					+ "times with each rule result specifying another port "
					+ "range)",
			name = "externalFacingBindUdpPortRange",
			syntax = "externalFacingBindUdpPortRange=PORT_RANGE",
			valueType = PortRange.class
	)
	public static final RuleResultSpec<PortRange> EXTERNAL_FACING_BIND_UDP_PORT_RANGE = RULE_RESULT_SPECS.addThenGet(new PortRangeRuleResultSpec(
			"externalFacingBindUdpPortRange"));
	
	@NameValuePairValueSpecDoc(
			description = "Specifies a socket setting for all external-facing "
					+ "sockets (can be specified multiple times with each rule "
					+ "result specifying another socket setting)",
			name = "externalFacingSocketSetting",
			syntax = "externalFacingSocketSetting=SOCKET_SETTING",
			valueType = SocketSetting.class
	)
	public static final RuleResultSpec<SocketSetting<Object>> EXTERNAL_FACING_SOCKET_SETTING = RULE_RESULT_SPECS.addThenGet(new SocketSettingRuleResultSpec(
			"externalFacingSocketSetting"));
	
	@NameValuePairValueSpecDoc(
			description = "Specifies the firewall action to take",
			name = "firewallAction",
			syntax = "firewallAction=FIREWALL_ACTION",
			valueType = FirewallAction.class
	)
	public static final RuleResultSpec<FirewallAction> FIREWALL_ACTION = RULE_RESULT_SPECS.addThenGet(new FirewallActionRuleResultSpec(
			"firewallAction"));
	
	@NameValuePairValueSpecDoc(
			description = "Specifies the limit on the number of simultaneous "
					+ "instances of the rule's firewall action ALLOW",
			name = "firewallActionAllowLimit",
			syntax = "firewallActionAllowLimit=NON_NEGATIVE_INTEGER",
			valueType = NonNegativeInteger.class
	)
	public static final RuleResultSpec<NonNegativeIntegerLimit> FIREWALL_ACTION_ALLOW_LIMIT = RULE_RESULT_SPECS.addThenGet(new NonnegativeIntegerLimitRuleResultSpec(
			"firewallActionAllowLimit"));
	
	@NameValuePairValueSpecDoc(
			description = "Specifies the logging action to take if the limit "
					+ "on the number of simultaneous instances of the rule's "
					+ "firewall action ALLOW has been reached",
			name = "firewallActionAllowLimitReachedLogAction",
			syntax = "firewallActionAllowLimitReachedLogAction=LOG_ACTION",
			valueType = LogAction.class
	)	
	public static final RuleResultSpec<LogAction> FIREWALL_ACTION_ALLOW_LIMIT_REACHED_LOG_ACTION = RULE_RESULT_SPECS.addThenGet(new LogActionRuleResultSpec(
			"firewallActionAllowLimitReachedLogAction"));
	
	@NameValuePairValueSpecDoc(
			description = "Specifies the logging action to take if the "
					+ "firewall action is applied",
			name = "firewallActionLogAction",
			syntax = "firewallActionLogAction=LOG_ACTION",
			valueType = LogAction.class
	)	
	public static final RuleResultSpec<LogAction> FIREWALL_ACTION_LOG_ACTION = RULE_RESULT_SPECS.addThenGet(new LogActionRuleResultSpec(
			"firewallActionLogAction"));

	@NameValuePairValueSpecDoc(
			description = "Specifies the binding host name or address for all "
					+ "internal-facing sockets",
			name = "internalFacingBindHost",
			syntax = "internalFacingBindHost=HOST",
			valueType = Host.class
	)
	public static final RuleResultSpec<Host> INTERNAL_FACING_BIND_HOST = RULE_RESULT_SPECS.addThenGet(new HostRuleResultSpec(
			"internalFacingBindHost"));

	@NameValuePairValueSpecDoc(
			description = "Specifies a binding port range for all "
					+ "internal-facing UDP sockets (can be specified multiple "
					+ "times with each rule result specifying another port "
					+ "range)",
			name = "internalFacingBindUdpPortRange",
			syntax = "internalFacingBindUdpPortRange=PORT_RANGE",
			valueType = PortRange.class
	)
	public static final RuleResultSpec<PortRange> INTERNAL_FACING_BIND_UDP_PORT_RANGE = RULE_RESULT_SPECS.addThenGet(new PortRangeRuleResultSpec(
			"internalFacingBindUdpPortRange"));
	
	@NameValuePairValueSpecDoc(
			description = "Specifies a socket setting for all internal-facing "
					+ "sockets (can be specified multiple times with each rule "
					+ "result specifying another socket setting)",
			name = "internalFacingSocketSetting",
			syntax = "internalFacingSocketSetting=SOCKET_SETTING",
			valueType = SocketSetting.class
	)
	public static final RuleResultSpec<SocketSetting<Object>> INTERNAL_FACING_SOCKET_SETTING = RULE_RESULT_SPECS.addThenGet(new SocketSettingRuleResultSpec(
			"internalFacingSocketSetting"));
	
	@NameValuePairValueSpecDoc(
			description = "Specifies the logging action to take if a route ID "
					+ "is selected",
			name = "routeSelectionLogAction",
			syntax = "routeSelectionLogAction=LOG_ACTION",
			valueType = LogAction.class
	)	
	public static final RuleResultSpec<LogAction> ROUTE_SELECTION_LOG_ACTION = RULE_RESULT_SPECS.addThenGet(new LogActionRuleResultSpec(
			"routeSelectionLogAction"));
	
	@NameValuePairValueSpecDoc(
			description = "Specifies the selection strategy for the next route "
					+ "ID",
			name = "routeSelectionStrategy",
			syntax = "routeSelectionStrategy=SELECTION_STRATEGY",
			valueType = SelectionStrategy.class
	)	
	public static final RuleResultSpec<SelectionStrategy> ROUTE_SELECTION_STRATEGY = RULE_RESULT_SPECS.addThenGet(new SelectionStrategyRuleResultSpec(
			"routeSelectionStrategy"));
	
	@NameValuePairValueSpecDoc(
			description = "Specifies the ID for a selectable route (can be "
					+ "specified multiple times with each rule result "
					+ "specifying another ID for a selectable route)",
			name = "selectableRouteId",
			syntax = "selectableRouteId=ROUTE_ID",
			valueType = String.class
	)
	public static final RuleResultSpec<String> SELECTABLE_ROUTE_ID = RULE_RESULT_SPECS.addThenGet(new StringRuleResultSpec(
			"selectableRouteId"));
	
	@NameValuePairValueSpecDoc(
			description = "Specifies a socket setting for all sockets (can be "
					+ "specified multiple times with each rule result "
					+ "specifying another socket setting)",
			name = "socketSetting",
			syntax = "socketSetting=SOCKET_SETTING",
			valueType = SocketSetting.class
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
