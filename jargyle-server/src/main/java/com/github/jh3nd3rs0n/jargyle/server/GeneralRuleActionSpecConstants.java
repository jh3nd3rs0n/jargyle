package com.github.jh3nd3rs0n.jargyle.server;

import java.util.List;
import java.util.Map;

import com.github.jh3nd3rs0n.jargyle.common.net.*;
import com.github.jh3nd3rs0n.jargyle.common.number.NonNegativeInteger;
import com.github.jh3nd3rs0n.jargyle.internal.annotation.NameValuePairValueSpecDoc;
import com.github.jh3nd3rs0n.jargyle.internal.annotation.NameValuePairValueSpecsDoc;
import com.github.jh3nd3rs0n.jargyle.server.internal.ruleactionspec.impl.*;

@NameValuePairValueSpecsDoc(
		description = "",
		name = "General Rule Actions"
)
public final class GeneralRuleActionSpecConstants {
	
	private static final RuleActionSpecs RULE_ACTION_SPECS = new RuleActionSpecs();

	@NameValuePairValueSpecDoc(
			description = "Specifies the binding host name or address for all "
					+ "sockets",
			name = "bindHost",
			syntax = "bindHost=HOST",
			valueType = Host.class
	)
	public static final RuleActionSpec<Host> BIND_HOST = RULE_ACTION_SPECS.addThenGet(new HostRuleActionSpec(
			"bindHost"));

	@NameValuePairValueSpecDoc(
			description = "Specifies an acceptable binding host address type "
					+ "for all sockets (can be specified multiple times with "
					+ "each rule action specifying another host address type)",
			name = "bindHostAddressType",
			syntax = "bindHostAddressType=HOST_ADDRESS_TYPE",
			valueType = HostAddressType.class
	)
	public static final RuleActionSpec<HostAddressType> BIND_HOST_ADDRESS_TYPE = RULE_ACTION_SPECS.addThenGet(new HostAddressTypeRuleActionSpec(
			"bindHostAddressType"));

	@NameValuePairValueSpecDoc(
			description = "Specifies a binding port range for all TCP sockets "
					+ "(can be specified multiple times with each rule action "
					+ "specifying another port range)",
			name = "bindTcpPortRange",
			syntax = "bindTcpPortRange=PORT_RANGE",
			valueType = PortRange.class
	)
	public static final RuleActionSpec<PortRange> BIND_TCP_PORT_RANGE = RULE_ACTION_SPECS.addThenGet(new PortRangeRuleActionSpec(
			"bindTcpPortRange"));
	
	@NameValuePairValueSpecDoc(
			description = "Specifies a binding port range for all UDP sockets "
					+ "(can be specified multiple times with each rule action "
					+ "specifying another port ranges)",
			name = "bindUdpPortRange",
			syntax = "bindUdpPortRange=PORT_RANGE",
			valueType = PortRange.class
	)
	public static final RuleActionSpec<PortRange> BIND_UDP_PORT_RANGE = RULE_ACTION_SPECS.addThenGet(new PortRangeRuleActionSpec(
			"bindUdpPortRange"));
	
	@NameValuePairValueSpecDoc(
			description = "Specifies a socket setting for the client socket "
					+ "(can be specified multiple times with each rule action "
					+ "specifying another socket setting)",
			name = "clientSocketSetting",
			syntax = "clientSocketSetting=SOCKET_SETTING",
			valueType = SocketSetting.class
	)	
	public static final RuleActionSpec<SocketSetting<Object>> CLIENT_SOCKET_SETTING = RULE_ACTION_SPECS.addThenGet(new SocketSettingRuleActionSpec(
			"clientSocketSetting"));

	@NameValuePairValueSpecDoc(
			description = "Specifies the binding host name or address for all "
					+ "external-facing sockets",
			name = "externalFacingBindHost",
			syntax = "externalFacingBindHost=HOST",
			valueType = Host.class
	)
	public static final RuleActionSpec<Host> EXTERNAL_FACING_BIND_HOST = RULE_ACTION_SPECS.addThenGet(new HostRuleActionSpec(
			"externalFacingBindHost"));

	@NameValuePairValueSpecDoc(
			description = "Specifies an acceptable binding host address type "
					+ "for all external-facing sockets (can be specified "
					+ "multiple times with each rule action specifying "
					+ "another host address type)",
			name = "externalFacingBindHostAddressType",
			syntax = "externalFacingBindHostAddressType=HOST_ADDRESS_TYPE",
			valueType = HostAddressType.class
	)
	public static final RuleActionSpec<HostAddressType> EXTERNAL_FACING_BIND_HOST_ADDRESS_TYPE = RULE_ACTION_SPECS.addThenGet(new HostAddressTypeRuleActionSpec(
			"externalFacingBindHostAddressType"));

	@NameValuePairValueSpecDoc(
			description = "Specifies a binding port range for all "
					+ "external-facing TCP sockets (can be specified multiple "
					+ "times with each rule action specifying another port "
					+ "range)",
			name = "externalFacingBindTcpPortRange",
			syntax = "externalFacingBindTcpPortRange=PORT_RANGE",
			valueType = PortRange.class
	)
	public static final RuleActionSpec<PortRange> EXTERNAL_FACING_BIND_TCP_PORT_RANGE = RULE_ACTION_SPECS.addThenGet(new PortRangeRuleActionSpec(
			"externalFacingBindTcpPortRange"));
	
	@NameValuePairValueSpecDoc(
			description = "Specifies a binding port range for all "
					+ "external-facing UDP sockets (can be specified multiple "
					+ "times with each rule action specifying another port "
					+ "range)",
			name = "externalFacingBindUdpPortRange",
			syntax = "externalFacingBindUdpPortRange=PORT_RANGE",
			valueType = PortRange.class
	)
	public static final RuleActionSpec<PortRange> EXTERNAL_FACING_BIND_UDP_PORT_RANGE = RULE_ACTION_SPECS.addThenGet(new PortRangeRuleActionSpec(
			"externalFacingBindUdpPortRange"));

	@NameValuePairValueSpecDoc(
			description = "Specifies the network interface that provides a "
					+ "binding host address for all external-facing sockets",
			name = "externalFacingNetInterface",
			syntax = "externalFacingNetInterface=NETWORK_INTERFACE",
			valueType = NetInterface.class
	)
	public static final RuleActionSpec<NetInterface> EXTERNAL_FACING_NET_INTERFACE = RULE_ACTION_SPECS.addThenGet(new NetInterfaceRuleActionSpec(
			"externalFacingNetInterface"));

	@NameValuePairValueSpecDoc(
			description = "Specifies a socket setting for all external-facing "
					+ "sockets (can be specified multiple times with each rule "
					+ "action specifying another socket setting)",
			name = "externalFacingSocketSetting",
			syntax = "externalFacingSocketSetting=SOCKET_SETTING",
			valueType = SocketSetting.class
	)
	public static final RuleActionSpec<SocketSetting<Object>> EXTERNAL_FACING_SOCKET_SETTING = RULE_ACTION_SPECS.addThenGet(new SocketSettingRuleActionSpec(
			"externalFacingSocketSetting"));
	
	@NameValuePairValueSpecDoc(
			description = "Specifies the firewall action to take",
			name = "firewallAction",
			syntax = "firewallAction=FIREWALL_ACTION",
			valueType = FirewallAction.class
	)
	public static final RuleActionSpec<FirewallAction> FIREWALL_ACTION = RULE_ACTION_SPECS.addThenGet(new FirewallActionRuleActionSpec(
			"firewallAction"));
	
	@NameValuePairValueSpecDoc(
			description = "Specifies the limit on the number of simultaneous "
					+ "instances of the rule's firewall action ALLOW",
			name = "firewallActionAllowLimit",
			syntax = "firewallActionAllowLimit=NON_NEGATIVE_INTEGER",
			valueType = NonNegativeInteger.class
	)
	public static final RuleActionSpec<NonNegativeIntegerLimit> FIREWALL_ACTION_ALLOW_LIMIT = RULE_ACTION_SPECS.addThenGet(new NonnegativeIntegerLimitRuleActionSpec(
			"firewallActionAllowLimit"));
	
	@NameValuePairValueSpecDoc(
			description = "Specifies the logging action to take if the limit "
					+ "on the number of simultaneous instances of the rule's "
					+ "firewall action ALLOW has been reached",
			name = "firewallActionAllowLimitReachedLogAction",
			syntax = "firewallActionAllowLimitReachedLogAction=LOG_ACTION",
			valueType = LogAction.class
	)	
	public static final RuleActionSpec<LogAction> FIREWALL_ACTION_ALLOW_LIMIT_REACHED_LOG_ACTION = RULE_ACTION_SPECS.addThenGet(new LogActionRuleActionSpec(
			"firewallActionAllowLimitReachedLogAction"));
	
	@NameValuePairValueSpecDoc(
			description = "Specifies the logging action to take if the "
					+ "firewall action is applied",
			name = "firewallActionLogAction",
			syntax = "firewallActionLogAction=LOG_ACTION",
			valueType = LogAction.class
	)	
	public static final RuleActionSpec<LogAction> FIREWALL_ACTION_LOG_ACTION = RULE_ACTION_SPECS.addThenGet(new LogActionRuleActionSpec(
			"firewallActionLogAction"));

	@NameValuePairValueSpecDoc(
			description = "Specifies the binding host name or address for all "
					+ "internal-facing sockets",
			name = "internalFacingBindHost",
			syntax = "internalFacingBindHost=HOST",
			valueType = Host.class
	)
	public static final RuleActionSpec<Host> INTERNAL_FACING_BIND_HOST = RULE_ACTION_SPECS.addThenGet(new HostRuleActionSpec(
			"internalFacingBindHost"));

	@NameValuePairValueSpecDoc(
			description = "Specifies an acceptable binding host address type "
					+ "for all internal-facing sockets (can be specified "
					+ "multiple times with each rule action specifying "
					+ "another host address type)",
			name = "internalFacingBindHostAddressType",
			syntax = "internalFacingBindHostAddressType=HOST_ADDRESS_TYPE",
			valueType = HostAddressType.class
	)
	public static final RuleActionSpec<HostAddressType> INTERNAL_FACING_BIND_HOST_ADDRESS_TYPE = RULE_ACTION_SPECS.addThenGet(new HostAddressTypeRuleActionSpec(
			"internalFacingBindHostAddressType"));

	@NameValuePairValueSpecDoc(
			description = "Specifies a binding port range for all "
					+ "internal-facing UDP sockets (can be specified multiple "
					+ "times with each rule action specifying another port "
					+ "range)",
			name = "internalFacingBindUdpPortRange",
			syntax = "internalFacingBindUdpPortRange=PORT_RANGE",
			valueType = PortRange.class
	)
	public static final RuleActionSpec<PortRange> INTERNAL_FACING_BIND_UDP_PORT_RANGE = RULE_ACTION_SPECS.addThenGet(new PortRangeRuleActionSpec(
			"internalFacingBindUdpPortRange"));

	@NameValuePairValueSpecDoc(
			description = "Specifies the network interface that provides a "
					+ "binding host address for all internal-facing sockets",
			name = "internalFacingNetInterface",
			syntax = "internalFacingNetInterface=NETWORK_INTERFACE",
			valueType = NetInterface.class
	)
	public static final RuleActionSpec<NetInterface> INTERNAL_FACING_NET_INTERFACE = RULE_ACTION_SPECS.addThenGet(new NetInterfaceRuleActionSpec(
			"internalFacingNetInterface"));

	@NameValuePairValueSpecDoc(
			description = "Specifies a socket setting for all internal-facing "
					+ "sockets (can be specified multiple times with each rule "
					+ "action specifying another socket setting)",
			name = "internalFacingSocketSetting",
			syntax = "internalFacingSocketSetting=SOCKET_SETTING",
			valueType = SocketSetting.class
	)
	public static final RuleActionSpec<SocketSetting<Object>> INTERNAL_FACING_SOCKET_SETTING = RULE_ACTION_SPECS.addThenGet(new SocketSettingRuleActionSpec(
			"internalFacingSocketSetting"));

	@NameValuePairValueSpecDoc(
			description = "Specifies the network interface that provides a "
					+ "binding host address for all sockets",
			name = "netInterface",
			syntax = "netInterface=NETWORK_INTERFACE",
			valueType = NetInterface.class
	)
	public static final RuleActionSpec<NetInterface> NET_INTERFACE = RULE_ACTION_SPECS.addThenGet(new NetInterfaceRuleActionSpec(
			"netInterface"));

	@NameValuePairValueSpecDoc(
			description = "Specifies the logging action to take if a route ID "
					+ "is selected",
			name = "routeSelectionLogAction",
			syntax = "routeSelectionLogAction=LOG_ACTION",
			valueType = LogAction.class
	)	
	public static final RuleActionSpec<LogAction> ROUTE_SELECTION_LOG_ACTION = RULE_ACTION_SPECS.addThenGet(new LogActionRuleActionSpec(
			"routeSelectionLogAction"));
	
	@NameValuePairValueSpecDoc(
			description = "Specifies the selection strategy for the next route "
					+ "ID",
			name = "routeSelectionStrategy",
			syntax = "routeSelectionStrategy=SELECTION_STRATEGY",
			valueType = SelectionStrategy.class
	)	
	public static final RuleActionSpec<SelectionStrategy> ROUTE_SELECTION_STRATEGY = RULE_ACTION_SPECS.addThenGet(new SelectionStrategyRuleActionSpec(
			"routeSelectionStrategy"));
	
	@NameValuePairValueSpecDoc(
			description = "Specifies the ID for a selectable route (can be "
					+ "specified multiple times with each rule action "
					+ "specifying another ID for a selectable route)",
			name = "selectableRouteId",
			syntax = "selectableRouteId=ROUTE_ID",
			valueType = String.class
	)
	public static final RuleActionSpec<String> SELECTABLE_ROUTE_ID = RULE_ACTION_SPECS.addThenGet(new StringRuleActionSpec(
			"selectableRouteId"));
	
	@NameValuePairValueSpecDoc(
			description = "Specifies a socket setting for all sockets (can be "
					+ "specified multiple times with each rule action "
					+ "specifying another socket setting)",
			name = "socketSetting",
			syntax = "socketSetting=SOCKET_SETTING",
			valueType = SocketSetting.class
	)
	public static final RuleActionSpec<SocketSetting<Object>> SOCKET_SETTING = RULE_ACTION_SPECS.addThenGet(new SocketSettingRuleActionSpec(
			"socketSetting"));
	
	public static List<RuleActionSpec<Object>> values() {
		return RULE_ACTION_SPECS.toList();
	}
	
	public static Map<String, RuleActionSpec<Object>> valuesMap() {
		return RULE_ACTION_SPECS.toMap();
	}
	
	public GeneralRuleActionSpecConstants() { }

}
