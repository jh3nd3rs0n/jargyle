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
					+ "endpoints",
			name = "bindHost",
			syntax = "bindHost=HOST",
			valueType = Host.class
	)
	public static final RuleActionSpec<Host> BIND_HOST = RULE_ACTION_SPECS.addThenGet(new HostRuleActionSpec(
			"bindHost"));

	@NameValuePairValueSpecDoc(
			description = "Specifies an acceptable binding host address type "
					+ "for all endpoints (can be specified multiple times with "
					+ "each rule action specifying another host address type)",
			name = "bindHostAddressType",
			syntax = "bindHostAddressType=HOST_ADDRESS_TYPE",
			valueType = HostAddressType.class
	)
	public static final RuleActionSpec<HostAddressType> BIND_HOST_ADDRESS_TYPE = RULE_ACTION_SPECS.addThenGet(new HostAddressTypeRuleActionSpec(
			"bindHostAddressType"));

	@NameValuePairValueSpecDoc(
			description = "Specifies a binding port range for all TCP endpoints "
					+ "(can be specified multiple times with each rule action "
					+ "specifying another port range)",
			name = "bindTcpPortRange",
			syntax = "bindTcpPortRange=PORT_RANGE",
			valueType = PortRange.class
	)
	public static final RuleActionSpec<PortRange> BIND_TCP_PORT_RANGE = RULE_ACTION_SPECS.addThenGet(new PortRangeRuleActionSpec(
			"bindTcpPortRange"));
	
	@NameValuePairValueSpecDoc(
			description = "Specifies a binding port range for all UDP endpoints "
					+ "(can be specified multiple times with each rule action "
					+ "specifying another port ranges)",
			name = "bindUdpPortRange",
			syntax = "bindUdpPortRange=PORT_RANGE",
			valueType = PortRange.class
	)
	public static final RuleActionSpec<PortRange> BIND_UDP_PORT_RANGE = RULE_ACTION_SPECS.addThenGet(new PortRangeRuleActionSpec(
			"bindUdpPortRange"));

	@NameValuePairValueSpecDoc(
			description = "Specifies the binding host name or address for all "
					+ "client endpoints",
			name = "clientEndpoints.bindHost",
			syntax = "clientEndpoints.bindHost=HOST",
			valueType = Host.class
	)
	public static final RuleActionSpec<Host> CLIENT_ENDPOINTS_BIND_HOST = RULE_ACTION_SPECS.addThenGet(new HostRuleActionSpec(
			"clientEndpoints.bindHost"));

	@NameValuePairValueSpecDoc(
			description = "Specifies an acceptable binding host address type "
					+ "for all client endpoints (can be specified "
					+ "multiple times with each rule action specifying "
					+ "another host address type)",
			name = "clientEndpoints.bindHostAddressType",
			syntax = "clientEndpoints.bindHostAddressType=HOST_ADDRESS_TYPE",
			valueType = HostAddressType.class
	)
	public static final RuleActionSpec<HostAddressType> CLIENT_ENDPOINTS_BIND_HOST_ADDRESS_TYPE = RULE_ACTION_SPECS.addThenGet(new HostAddressTypeRuleActionSpec(
			"clientEndpoints.bindHostAddressType"));

	@NameValuePairValueSpecDoc(
			description = "Specifies a binding port range for all "
					+ "client UDP endpoints (can be specified multiple "
					+ "times with each rule action specifying another port "
					+ "range)",
			name = "clientEndpoints.bindUdpPortRange",
			syntax = "clientEndpoints.bindUdpPortRange=PORT_RANGE",
			valueType = PortRange.class
	)
	public static final RuleActionSpec<PortRange> CLIENT_ENDPOINTS_BIND_UDP_PORT_RANGE = RULE_ACTION_SPECS.addThenGet(new PortRangeRuleActionSpec(
			"clientEndpoints.bindUdpPortRange"));

	@NameValuePairValueSpecDoc(
			description = "Specifies the network interface that provides a "
					+ "binding host address for all client endpoints",
			name = "clientEndpoints.netInterface",
			syntax = "clientEndpoints.netInterface=NETWORK_INTERFACE",
			valueType = NetInterface.class
	)
	public static final RuleActionSpec<NetInterface> CLIENT_ENDPOINTS_NET_INTERFACE = RULE_ACTION_SPECS.addThenGet(new NetInterfaceRuleActionSpec(
			"clientEndpoints.netInterface"));

	@NameValuePairValueSpecDoc(
			description = "Specifies a socket setting for all client "
					+ "endpoints (can be specified multiple times with each " 
					+ "rule action specifying another socket setting)",
			name = "clientEndpoints.socketSetting",
			syntax = "clientEndpoints.socketSetting=SOCKET_SETTING",
			valueType = SocketSetting.class
	)
	public static final RuleActionSpec<SocketSetting<Object>> CLIENT_ENDPOINTS_SOCKET_SETTING = RULE_ACTION_SPECS.addThenGet(new SocketSettingRuleActionSpec(
			"clientEndpoints.socketSetting"));
	
	@NameValuePairValueSpecDoc(
			description = "Specifies a socket setting for the client TCP "
					+ "socket (can be specified multiple times with each "
					+ "rule action specifying another socket setting)",
			name = "clientSocket.socketSetting",
			syntax = "clientSocket.socketSetting=SOCKET_SETTING",
			valueType = SocketSetting.class
	)	
	public static final RuleActionSpec<SocketSetting<Object>> CLIENT_SOCKET_SOCKET_SETTING = RULE_ACTION_SPECS.addThenGet(new SocketSettingRuleActionSpec(
			"clientSocket.socketSetting"));
	
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
			description = "Specifies the network interface that provides a "
					+ "binding host address for all endpoints",
			name = "netInterface",
			syntax = "netInterface=NETWORK_INTERFACE",
			valueType = NetInterface.class
	)
	public static final RuleActionSpec<NetInterface> NET_INTERFACE = RULE_ACTION_SPECS.addThenGet(new NetInterfaceRuleActionSpec(
			"netInterface"));

	@NameValuePairValueSpecDoc(
			description = "Specifies the binding host name or address for all "
					+ "remote endpoints",
			name = "remoteEndpoints.bindHost",
			syntax = "remoteEndpoints.bindHost=HOST",
			valueType = Host.class
	)
	public static final RuleActionSpec<Host> REMOTE_ENDPOINTS_BIND_HOST = RULE_ACTION_SPECS.addThenGet(new HostRuleActionSpec(
			"remoteEndpoints.bindHost"));

	@NameValuePairValueSpecDoc(
			description = "Specifies an acceptable binding host address type "
					+ "for all remote endpoints (can be specified "
					+ "multiple times with each rule action specifying "
					+ "another host address type)",
			name = "remoteEndpoints.bindHostAddressType",
			syntax = "remoteEndpoints.bindHostAddressType=HOST_ADDRESS_TYPE",
			valueType = HostAddressType.class
	)
	public static final RuleActionSpec<HostAddressType> REMOTE_ENDPOINTS_BIND_HOST_ADDRESS_TYPE = RULE_ACTION_SPECS.addThenGet(new HostAddressTypeRuleActionSpec(
			"remoteEndpoints.bindHostAddressType"));

	@NameValuePairValueSpecDoc(
			description = "Specifies a binding port range for all "
					+ "remote TCP endpoints (can be specified multiple "
					+ "times with each rule action specifying another port "
					+ "range)",
			name = "remoteEndpoints.bindTcpPortRange",
			syntax = "remoteEndpoints.bindTcpPortRange=PORT_RANGE",
			valueType = PortRange.class
	)
	public static final RuleActionSpec<PortRange> REMOTE_ENDPOINTS_BIND_TCP_PORT_RANGE = RULE_ACTION_SPECS.addThenGet(new PortRangeRuleActionSpec(
			"remoteEndpoints.bindTcpPortRange"));

	@NameValuePairValueSpecDoc(
			description = "Specifies a binding port range for all "
					+ "remote UDP endpoints (can be specified multiple "
					+ "times with each rule action specifying another port "
					+ "range)",
			name = "remoteEndpoints.bindUdpPortRange",
			syntax = "remoteEndpoints.bindUdpPortRange=PORT_RANGE",
			valueType = PortRange.class
	)
	public static final RuleActionSpec<PortRange> REMOTE_ENDPOINTS_BIND_UDP_PORT_RANGE = RULE_ACTION_SPECS.addThenGet(new PortRangeRuleActionSpec(
			"remoteEndpoints.bindUdpPortRange"));

	@NameValuePairValueSpecDoc(
			description = "Specifies the network interface that provides a "
					+ "binding host address for all remote endpoints",
			name = "remoteEndpoints.netInterface",
			syntax = "remoteEndpoints.netInterface=NETWORK_INTERFACE",
			valueType = NetInterface.class
	)
	public static final RuleActionSpec<NetInterface> REMOTE_ENDPOINTS_NET_INTERFACE = RULE_ACTION_SPECS.addThenGet(new NetInterfaceRuleActionSpec(
			"remoteEndpoints.netInterface"));

	@NameValuePairValueSpecDoc(
			description = "Specifies a socket setting for all remote "
					+ "endpoints (can be specified multiple times with each "
					+ "rule action specifying another socket setting)",
			name = "remoteEndpoints.socketSetting",
			syntax = "remoteEndpoints.socketSetting=SOCKET_SETTING",
			valueType = SocketSetting.class
	)
	public static final RuleActionSpec<SocketSetting<Object>> REMOTE_ENDPOINTS_SOCKET_SETTING = RULE_ACTION_SPECS.addThenGet(new SocketSettingRuleActionSpec(
			"remoteEndpoints.socketSetting"));

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
			description = "Specifies a socket setting for all endpoints (can be "
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
