package com.github.jh3nd3rs0n.jargyle.server;

import java.util.List;
import java.util.Map;

import com.github.jh3nd3rs0n.jargyle.common.net.*;
import com.github.jh3nd3rs0n.jargyle.common.number.NonnegativeInteger;
import com.github.jh3nd3rs0n.jargyle.internal.annotation.NameValuePairValueSpecDoc;
import com.github.jh3nd3rs0n.jargyle.internal.annotation.NameValuePairValueSpecsDoc;
import com.github.jh3nd3rs0n.jargyle.server.internal.settingspec.impl.HostSettingSpec;
import com.github.jh3nd3rs0n.jargyle.server.internal.settingspec.impl.LogActionSettingSpec;
import com.github.jh3nd3rs0n.jargyle.server.internal.settingspec.impl.NonnegativeIntegerSettingSpec;
import com.github.jh3nd3rs0n.jargyle.server.internal.settingspec.impl.PortRangesSettingSpec;
import com.github.jh3nd3rs0n.jargyle.server.internal.settingspec.impl.PortSettingSpec;
import com.github.jh3nd3rs0n.jargyle.server.internal.settingspec.impl.RuleSettingSpec;
import com.github.jh3nd3rs0n.jargyle.server.internal.settingspec.impl.SelectionStrategySettingSpec;
import com.github.jh3nd3rs0n.jargyle.server.internal.settingspec.impl.SocketSettingsSettingSpec;
import com.github.jh3nd3rs0n.jargyle.server.internal.settingspec.impl.StringSettingSpec;

@NameValuePairValueSpecsDoc(
		description = "",
		name = "General Settings"
)
public final class GeneralSettingSpecConstants {

	private static final SettingSpecs SETTING_SPECS = new SettingSpecs();

	@NameValuePairValueSpecDoc(
			description = "The maximum length of the queue of incoming client "
					+ "connections to the SOCKS server (default is 50)",
			name = "backlog",
			syntax = "backlog=NONNEGATIVE_INTEGER",
			valueType = NonnegativeInteger.class
	)
	public static final SettingSpec<NonnegativeInteger> BACKLOG = 
			SETTING_SPECS.addThenGet(new NonnegativeIntegerSettingSpec(
					"backlog", 
					NonnegativeInteger.newInstance(50)));
	
	@NameValuePairValueSpecDoc(
			description = "The default binding host name or address for all "
					+ "sockets (default is 0.0.0.0)",
			name = "bindHost",
			syntax = "bindHost=HOST",
			valueType = Host.class
	)
	public static final SettingSpec<Host> BIND_HOST =
			SETTING_SPECS.addThenGet(new HostSettingSpec(
					"bindHost", 
					HostIpv4Address.getAllZerosInstance()));
	
	@NameValuePairValueSpecDoc(
			description = "The comma separated list of default binding port "
					+ "ranges for all TCP sockets (default is 0)",
			name = "bindTcpPortRanges",
			syntax = "bindTcpPortRanges=PORT_RANGES",
			valueType = PortRanges.class
	)
	public static final SettingSpec<PortRanges> BIND_TCP_PORT_RANGES =
			SETTING_SPECS.addThenGet(new PortRangesSettingSpec(
					"bindTcpPortRanges",
					PortRanges.getDefault()));
	
	@NameValuePairValueSpecDoc(
			description = "The comma separated list of default binding port "
					+ "ranges for all UDP sockets (default is 0)",
			name = "bindUdpPortRanges",
			syntax = "bindUdpPortRanges=PORT_RANGES",
			valueType = PortRanges.class
	)
	public static final SettingSpec<PortRanges> BIND_UDP_PORT_RANGES =
			SETTING_SPECS.addThenGet(new PortRangesSettingSpec(
					"bindUdpPortRanges",
					PortRanges.getDefault()));
	
	@NameValuePairValueSpecDoc(
			description = "The comma separated list of socket settings for the "
					+ "client socket",
			name = "clientSocketSettings",
			syntax = "clientSocketSettings=SOCKET_SETTINGS",
			valueType = SocketSettings.class
	)
	public static final SettingSpec<SocketSettings> CLIENT_SOCKET_SETTINGS = 
			SETTING_SPECS.addThenGet(new SocketSettingsSettingSpec(
					"clientSocketSettings", 
					SocketSettings.newInstance()));
	
	@NameValuePairValueSpecDoc(
			description = "A documentation setting",
			name = "doc",
			syntax = "doc=TEXT",
			valueType = String.class
	)
	public static final SettingSpec<String> DOC =
			SETTING_SPECS.addThenGet(new StringSettingSpec(
					"doc",
					null));
	
	@NameValuePairValueSpecDoc(
			description = "The default binding host name or address for all "
					+ "external-facing sockets",
			name = "externalFacingBindHost",
			syntax = "externalFacingBindHost=HOST",
			valueType = Host.class
	)
	public static final SettingSpec<Host> EXTERNAL_FACING_BIND_HOST =
			SETTING_SPECS.addThenGet(new HostSettingSpec(
					"externalFacingBindHost",
					null));
	
	@NameValuePairValueSpecDoc(
			description = "The comma separated list of default binding port "
					+ "ranges for all external-facing TCP sockets",
			name = "externalFacingBindTcpPortRanges",
			syntax = "externalFacingBindTcpPortRanges=PORT_RANGES",
			valueType = PortRanges.class
	)
	public static final SettingSpec<PortRanges> EXTERNAL_FACING_BIND_TCP_PORT_RANGES =
			SETTING_SPECS.addThenGet(new PortRangesSettingSpec(
					"externalFacingBindTcpPortRanges",
					PortRanges.newInstance()));
			
	@NameValuePairValueSpecDoc(
			description = "The comma separated list of default binding port "
					+ "ranges for all external-facing UDP sockets",
			name = "externalFacingBindUdpPortRanges",
			syntax = "externalFacingBindUdpPortRanges=PORT_RANGES",
			valueType = PortRanges.class
	)
	public static final SettingSpec<PortRanges> EXTERNAL_FACING_BIND_UDP_PORT_RANGES =
			SETTING_SPECS.addThenGet(new PortRangesSettingSpec(
					"externalFacingBindUdpPortRanges",
					PortRanges.newInstance()));
	
	@NameValuePairValueSpecDoc(
			description = "The comma separated list of default socket settings "
					+ "for all external-facing sockets",
			name = "externalFacingSocketSettings",
			syntax = "externalFacingSocketSettings=SOCKET_SETTINGS",
			valueType = SocketSettings.class
	)
	public static final SettingSpec<SocketSettings> EXTERNAL_FACING_SOCKET_SETTINGS =
			SETTING_SPECS.addThenGet(new SocketSettingsSettingSpec(
					"externalFacingSocketSettings", 
					SocketSettings.newInstance()));

	@NameValuePairValueSpecDoc(
			description = "The default binding host name or address for all "
					+ "internal-facing sockets",
			name = "internalFacingBindHost",
			syntax = "internalFacingBindHost=HOST",
			valueType = Host.class
	)
	public static final SettingSpec<Host> INTERNAL_FACING_BIND_HOST =
			SETTING_SPECS.addThenGet(new HostSettingSpec(
					"internalFacingBindHost",
					null));
	
	@NameValuePairValueSpecDoc(
			description = "The comma separated list of default binding port "
					+ "ranges for all internal-facing TCP sockets",
			name = "internalFacingBindTcpPortRanges",
			syntax = "internalFacingBindTcpPortRanges=PORT_RANGES",
			valueType = PortRanges.class
	)
	public static final SettingSpec<PortRanges> INTERNAL_FACING_BIND_TCP_PORT_RANGES =
			SETTING_SPECS.addThenGet(new PortRangesSettingSpec(
					"internalFacingBindTcpPortRanges",
					PortRanges.newInstance()));
			
	@NameValuePairValueSpecDoc(
			description = "The comma separated list of default binding port "
					+ "ranges for all internal-facing UDP sockets",
			name = "internalFacingBindUdpPortRanges",
			syntax = "internalFacingBindUdpPortRanges=PORT_RANGES",
			valueType = PortRanges.class
	)
	public static final SettingSpec<PortRanges> INTERNAL_FACING_BIND_UDP_PORT_RANGES =
			SETTING_SPECS.addThenGet(new PortRangesSettingSpec(
					"internalFacingBindUdpPortRanges",
					PortRanges.newInstance()));
	
	@NameValuePairValueSpecDoc(
			description = "The comma separated list of default socket settings "
					+ "for all internal-facing sockets",
			name = "internalFacingSocketSettings",
			syntax = "internalFacingSocketSettings=SOCKET_SETTINGS",
			valueType = SocketSettings.class
	)
	public static final SettingSpec<SocketSettings> INTERNAL_FACING_SOCKET_SETTINGS =
			SETTING_SPECS.addThenGet(new SocketSettingsSettingSpec(
					"internalFacingSocketSettings", 
					SocketSettings.newInstance()));
	
	@NameValuePairValueSpecDoc(
			description = "The ID for the last and unassigned route "
					+ "(default is lastRoute)",
			name = "lastRouteId",
			syntax = "lastRouteId=ROUTE_ID",
			valueType = String.class
	)	
	public static final SettingSpec<String> LAST_ROUTE_ID = 
			SETTING_SPECS.addThenGet(new StringSettingSpec(
					"lastRouteId",
					"lastRoute"));
	
	@NameValuePairValueSpecDoc(
			description = "The port for the SOCKS server",
			name = "port",
			syntax = "port=PORT",
			valueType = Port.class
	)
	public static final SettingSpec<Port> PORT = 
			SETTING_SPECS.addThenGet(new PortSettingSpec(
					"port", 
					null));

	@NameValuePairValueSpecDoc(
			description = "The logging action to take if a route is selected",
			name = "routeSelectionLogAction",
			syntax = "routeSelectionLogAction=LOG_ACTION",
			valueType = LogAction.class
	)	
	public static final SettingSpec<LogAction> ROUTE_SELECTION_LOG_ACTION =
			SETTING_SPECS.addThenGet(new LogActionSettingSpec(
					"routeSelectionLogAction",
					null));
	
	@NameValuePairValueSpecDoc(
			description = "The selection strategy for the next route "
					+ "(default is CYCLICAL)",
			name = "routeSelectionStrategy",
			syntax = "routeSelectionStrategy=SELECTION_STRATEGY",
			valueType = SelectionStrategy.class
	)
	public static final SettingSpec<SelectionStrategy> ROUTE_SELECTION_STRATEGY =
			SETTING_SPECS.addThenGet(new SelectionStrategySettingSpec(
					"routeSelectionStrategy",
					SelectionStrategySpecConstants.CYCLICAL.newSelectionStrategy()));
	
	@NameValuePairValueSpecDoc(
			description = "A rule for the SOCKS server "
					+ "(default is firewallAction=ALLOW)",
			name = "rule",
			syntax = "rule=RULE",
			valueType = Rule.class
	)	
	public static final SettingSpec<Rule> RULE = 
			SETTING_SPECS.addThenGet(new RuleSettingSpec(
					"rule",
					Rule.getDefault()));
	
	@NameValuePairValueSpecDoc(
			description = "The comma separated list of default socket settings "
					+ "for all sockets",
			name = "socketSettings",
			syntax = "socketSettings=SOCKET_SETTINGS",
			valueType = SocketSettings.class
	)
	public static final SettingSpec<SocketSettings> SOCKET_SETTINGS = 
			SETTING_SPECS.addThenGet(new SocketSettingsSettingSpec(
					"socketSettings", 
					SocketSettings.newInstance()));
	
	@NameValuePairValueSpecDoc(
			description = "The binding host name or address for the SOCKS "
					+ "server socket",
			name = "socksServerBindHost",
			syntax = "socksServerBindHost=HOST",
			valueType = Host.class
	)
	public static final SettingSpec<Host> SOCKS_SERVER_BIND_HOST =
			SETTING_SPECS.addThenGet(new HostSettingSpec(
					"socksServerBindHost",
					null));
	
	@NameValuePairValueSpecDoc(
			description = "The comma separated list of binding port ranges for "
					+ "the SOCKS server socket",
			name = "socksServerBindPortRanges",
			syntax = "socksServerBindPortRangesPORT_RANGES",
			valueType = PortRanges.class
	)
	public static final SettingSpec<PortRanges> SOCKS_SERVER_BIND_PORT_RANGES =
			SETTING_SPECS.addThenGet(new PortRangesSettingSpec(
					"socksServerBindPortRanges",
					PortRanges.newInstance()));
	
	@NameValuePairValueSpecDoc(
			description = "The comma separated list of socket settings for the "
					+ "SOCKS server socket",
			name = "socksServerSocketSettings",
			syntax = "socksServerSocketSettings=SOCKET_SETTINGS",
			valueType = SocketSettings.class
	)
	public static final SettingSpec<SocketSettings> SOCKS_SERVER_SOCKET_SETTINGS =
			SETTING_SPECS.addThenGet(new SocketSettingsSettingSpec(
					"socksServerSocketSettings",
					SocketSettings.newInstance()));
	
	public static List<SettingSpec<Object>> values() {
		return SETTING_SPECS.toList();
	}
	
	public static Map<String, SettingSpec<Object>> valuesMap() {
		return SETTING_SPECS.toMap();
	}
	
	private GeneralSettingSpecConstants() { }
	
}
