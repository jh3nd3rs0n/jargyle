package com.github.jh3nd3rs0n.jargyle.server;

import java.util.List;
import java.util.Map;

import com.github.jh3nd3rs0n.jargyle.common.net.Host;
import com.github.jh3nd3rs0n.jargyle.common.net.Port;
import com.github.jh3nd3rs0n.jargyle.common.net.PortRanges;
import com.github.jh3nd3rs0n.jargyle.common.net.SocketSettings;
import com.github.jh3nd3rs0n.jargyle.common.number.NonnegativeInteger;
import com.github.jh3nd3rs0n.jargyle.internal.help.HelpText;
import com.github.jh3nd3rs0n.jargyle.server.internal.settingspec.impl.HostSettingSpec;
import com.github.jh3nd3rs0n.jargyle.server.internal.settingspec.impl.LogActionSettingSpec;
import com.github.jh3nd3rs0n.jargyle.server.internal.settingspec.impl.NonnegativeIntegerSettingSpec;
import com.github.jh3nd3rs0n.jargyle.server.internal.settingspec.impl.PortRangesSettingSpec;
import com.github.jh3nd3rs0n.jargyle.server.internal.settingspec.impl.PortSettingSpec;
import com.github.jh3nd3rs0n.jargyle.server.internal.settingspec.impl.RuleSettingSpec;
import com.github.jh3nd3rs0n.jargyle.server.internal.settingspec.impl.SelectionStrategySettingSpec;
import com.github.jh3nd3rs0n.jargyle.server.internal.settingspec.impl.SocketSettingsSettingSpec;
import com.github.jh3nd3rs0n.jargyle.server.internal.settingspec.impl.StringSettingSpec;

public final class GeneralSettingSpecConstants {

	private static final SettingSpecs SETTING_SPECS = new SettingSpecs();

	@HelpText(
			doc = "The maximum length of the queue of incoming client "
					+ "connections to the SOCKS server (default is 50)", 
			usage = "backlog=INTEGER_BETWEEN_0_AND_2147483647"
	)
	public static final SettingSpec<NonnegativeInteger> BACKLOG = 
			SETTING_SPECS.addThenGet(new NonnegativeIntegerSettingSpec(
					"backlog", 
					NonnegativeInteger.newInstance(50)));
	
	@HelpText(
			doc = "The default binding host name or address for all sockets "
					+ "(default is 0.0.0.0)",
			usage = "bindHost=HOST"
	)
	public static final SettingSpec<Host> BIND_HOST =
			SETTING_SPECS.addThenGet(new HostSettingSpec(
					"bindHost", 
					Host.getAllZerosInet4Instance()));
	
	@HelpText(
			doc = "The space separated list of default binding port ranges "
					+ "for all TCP sockets (default is 0)",
			usage = "bindTcpPortRanges=[PORT_RANGE1[ PORT_RANGE2[ ...]]]"
	)
	public static final SettingSpec<PortRanges> BIND_TCP_PORT_RANGES =
			SETTING_SPECS.addThenGet(new PortRangesSettingSpec(
					"bindTcpPortRanges",
					PortRanges.getDefault()));
	
	@HelpText(
			doc = "The space separated list of default binding port ranges "
					+ "for all UDP sockets (default is 0)",
			usage = "bindUdpPortRanges=[PORT_RANGE1[ PORT_RANGE2[ ...]]]"
	)
	public static final SettingSpec<PortRanges> BIND_UDP_PORT_RANGES =
			SETTING_SPECS.addThenGet(new PortRangesSettingSpec(
					"bindUdpPortRanges",
					PortRanges.getDefault()));
	
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
			doc = "The default binding host name or address for all "
					+ "external-facing sockets",
			usage = "externalFacingBindHost=HOST"
	)
	public static final SettingSpec<Host> EXTERNAL_FACING_BIND_HOST =
			SETTING_SPECS.addThenGet(new HostSettingSpec(
					"externalFacingBindHost",
					null));
	
	@HelpText(
			doc = "The space separated list of default binding port ranges "
					+ "for all external-facing TCP sockets",
			usage = "externalFacingBindTcpPortRanges=[PORT_RANGE1[ PORT_RANGE2[ ...]]]"
	)
	public static final SettingSpec<PortRanges> EXTERNAL_FACING_BIND_TCP_PORT_RANGES =
			SETTING_SPECS.addThenGet(new PortRangesSettingSpec(
					"externalFacingBindTcpPortRanges",
					PortRanges.newInstance()));
			
	@HelpText(
			doc = "The space separated list of default binding port ranges "
					+ "for all external-facing UDP sockets",
			usage = "externalFacingBindUdpPortRanges=[PORT_RANGE1[ PORT_RANGE2[ ...]]]"
	)
	public static final SettingSpec<PortRanges> EXTERNAL_FACING_BIND_UDP_PORT_RANGES =
			SETTING_SPECS.addThenGet(new PortRangesSettingSpec(
					"externalFacingBindUdpPortRanges",
					PortRanges.newInstance()));
	
	@HelpText(
			doc = "The space separated list of default socket settings for all "
					+ "external-facing sockets",
			usage = "externalFacingSocketSettings=[SOCKET_SETTING1[ SOCKET_SETTING2[...]]]"
	)
	public static final SettingSpec<SocketSettings> EXTERNAL_FACING_SOCKET_SETTINGS =
			SETTING_SPECS.addThenGet(new SocketSettingsSettingSpec(
					"externalFacingSocketSettings", 
					SocketSettings.newInstance()));

	@HelpText(
			doc = "The default binding host name or address for all "
					+ "internal-facing sockets",
			usage = "internalFacingBindHost=HOST"
	)
	public static final SettingSpec<Host> INTERNAL_FACING_BIND_HOST =
			SETTING_SPECS.addThenGet(new HostSettingSpec(
					"internalFacingBindHost",
					null));
	
	@HelpText(
			doc = "The space separated list of default binding port ranges "
					+ "for all internal-facing TCP sockets",
			usage = "internalFacingBindTcpPortRanges=[PORT_RANGE1[ PORT_RANGE2[ ...]]]"
	)
	public static final SettingSpec<PortRanges> INTERNAL_FACING_BIND_TCP_PORT_RANGES =
			SETTING_SPECS.addThenGet(new PortRangesSettingSpec(
					"internalFacingBindTcpPortRanges",
					PortRanges.newInstance()));
			
	@HelpText(
			doc = "The space separated list of default binding port ranges "
					+ "for all internal-facing UDP sockets",
			usage = "internalFacingBindUdpPortRanges=[PORT_RANGE1[ PORT_RANGE2[ ...]]]"
	)
	public static final SettingSpec<PortRanges> INTERNAL_FACING_BIND_UDP_PORT_RANGES =
			SETTING_SPECS.addThenGet(new PortRangesSettingSpec(
					"internalFacingBindUdpPortRanges",
					PortRanges.newInstance()));
	
	@HelpText(
			doc = "The space separated list of default socket settings for all "
					+ "internal-facing sockets",
			usage = "internalFacingSocketSettings=[SOCKET_SETTING1[ SOCKET_SETTING2[...]]]"
	)
	public static final SettingSpec<SocketSettings> INTERNAL_FACING_SOCKET_SETTINGS =
			SETTING_SPECS.addThenGet(new SocketSettingsSettingSpec(
					"internalFacingSocketSettings", 
					SocketSettings.newInstance()));
	
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
			doc = "The port for the SOCKS server", 
			usage = "port=INTEGER_BETWEEN_0_AND_65535"
	)
	public static final SettingSpec<Port> PORT = 
			SETTING_SPECS.addThenGet(new PortSettingSpec(
					"port", 
					null));

	@HelpText(
			doc = "The logging action to take if a route is selected", 
			usage = "routeSelectionLogAction=LOG_ACTION"
	)	
	public static final SettingSpec<LogAction> ROUTE_SELECTION_LOG_ACTION =
			SETTING_SPECS.addThenGet(new LogActionSettingSpec(
					"routeSelectionLogAction",
					null));
	
	@HelpText(
			doc = "The selection strategy for the next route "
					+ "(default is CYCLICAL)", 
			usage = "routeSelectionStrategy=SELECTION_STRATEGY"
	)
	public static final SettingSpec<SelectionStrategy> ROUTE_SELECTION_STRATEGY =
			SETTING_SPECS.addThenGet(new SelectionStrategySettingSpec(
					"routeSelectionStrategy",
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
			doc = "The space separated list of default socket settings for all "
					+ "sockets", 
			usage = "socketSettings=[SOCKET_SETTING1[ SOCKET_SETTING2[...]]]"
	)
	public static final SettingSpec<SocketSettings> SOCKET_SETTINGS = 
			SETTING_SPECS.addThenGet(new SocketSettingsSettingSpec(
					"socketSettings", 
					SocketSettings.newInstance()));
	
	@HelpText(
			doc = "The binding host name or address for the SOCKS server "
					+ "socket",
			usage = "socksServerBindHost=HOST"
	)
	public static final SettingSpec<Host> SOCKS_SERVER_BIND_HOST =
			SETTING_SPECS.addThenGet(new HostSettingSpec(
					"socksServerBindHost",
					null));
	
	@HelpText(
			doc = "The space separated list of binding port ranges for the "
					+ "SOCKS server socket",
			usage = "socksServerBindPortRanges[PORT_RANGE1[ PORT_RANGE2[ ...]]]"
	)
	public static final SettingSpec<PortRanges> SOCKS_SERVER_BIND_PORT_RANGES =
			SETTING_SPECS.addThenGet(new PortRangesSettingSpec(
					"socksServerBindPortRanges",
					PortRanges.newInstance()));
	
	@HelpText(
			doc = "The space separated list of socket settings for the SOCKS "
					+ "server socket",
			usage = "socksServerSocketSettings=[SOCKET_SETTING1[ SOCKET_SETTING2[...]]]"
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
