package com.github.jh3nd3rs0n.jargyle.server;

import java.util.List;
import java.util.Map;

import com.github.jh3nd3rs0n.jargyle.common.net.*;
import com.github.jh3nd3rs0n.jargyle.common.number.NonNegativeInteger;
import com.github.jh3nd3rs0n.jargyle.internal.annotation.NameValuePairValueSpecDoc;
import com.github.jh3nd3rs0n.jargyle.internal.annotation.NameValuePairValueSpecsDoc;
import com.github.jh3nd3rs0n.jargyle.server.internal.settingspec.impl.*;

@NameValuePairValueSpecsDoc(
		description = "",
		name = "General Settings"
)
public final class GeneralSettingSpecConstants {

	private static final SettingSpecs SETTING_SPECS = new SettingSpecs();

	@NameValuePairValueSpecDoc(
			defaultValue = "50",
			description = "The maximum length of the queue of incoming client "
					+ "connections to the SOCKS server",
			name = "backlog",
			syntax = "backlog=NON_NEGATIVE_INTEGER",
			valueType = NonNegativeInteger.class
	)
	public static final SettingSpec<NonNegativeInteger> BACKLOG =
			SETTING_SPECS.addThenGet(new NonNegativeIntegerSettingSpec(
					"backlog", 
					NonNegativeInteger.valueOf(50)));
	
	@NameValuePairValueSpecDoc(
			defaultValue = "0.0.0.0",
			description = "The default binding host name or address for all "
					+ "sockets",
			name = "bindHost",
			syntax = "bindHost=HOST",
			valueType = Host.class
	)
	public static final SettingSpec<Host> BIND_HOST =
			SETTING_SPECS.addThenGet(new HostSettingSpec(
					"bindHost", 
					null));

	@NameValuePairValueSpecDoc(
			defaultValue = "HOST_IPV4_ADDRESS,HOST_IPV6_ADDRESS",
			description = "The comma separated list of default acceptable "
					+ "binding host address types for all sockets",
			name = "bindHostAddressTypes",
			syntax = "bindHostAddressTypes=HOST_ADDRESS_TYPES",
			valueType = HostAddressTypes.class
	)
	public static final SettingSpec<HostAddressTypes> BIND_HOST_ADDRESS_TYPES =
			SETTING_SPECS.addThenGet(new HostAddressTypesSettingSpec(
					"bindHostAddressTypes",
					HostAddressTypes.getDefault()));

	@NameValuePairValueSpecDoc(
			defaultValue = "0",
			description = "The comma separated list of default binding port "
					+ "ranges for all TCP sockets",
			name = "bindTcpPortRanges",
			syntax = "bindTcpPortRanges=PORT_RANGES",
			valueType = PortRanges.class
	)
	public static final SettingSpec<PortRanges> BIND_TCP_PORT_RANGES =
			SETTING_SPECS.addThenGet(new PortRangesSettingSpec(
					"bindTcpPortRanges",
					PortRanges.getDefault()));
	
	@NameValuePairValueSpecDoc(
			defaultValue = "0",
			description = "The comma separated list of default binding port "
					+ "ranges for all UDP sockets",
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
					SocketSettings.of()));
	
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
			description = "The comma separated list of default acceptable "
					+ "binding host address types for all external-facing "
					+ "sockets",
			name = "externalFacingBindHostAddressTypes",
			syntax = "externalFacingBindHostAddressTypes=HOST_ADDRESS_TYPES",
			valueType = HostAddressTypes.class
	)
	public static final SettingSpec<HostAddressTypes> EXTERNAL_FACING_BIND_HOST_ADDRESS_TYPES =
			SETTING_SPECS.addThenGet(new HostAddressTypesSettingSpec(
					"externalFacingBindHostAddressTypes",
					HostAddressTypes.of()));

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
					PortRanges.of()));
			
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
					PortRanges.of()));

	@NameValuePairValueSpecDoc(
			description = "The default network interface that provides a "
					+ "binding host address for all external-facing sockets",
			name = "externalFacingNetInterface",
			syntax = "externalFacingNetInterface=NETWORK_INTERFACE",
			valueType = NetInterface.class
	)
	public static final SettingSpec<NetInterface> EXTERNAL_FACING_NET_INTERFACE =
			SETTING_SPECS.addThenGet(new NetInterfaceSettingSpec(
					"externalFacingNetInterface",
					null));

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
					SocketSettings.of()));

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
			description = "The comma separated list of default acceptable "
					+ "binding host address types for all internal-facing "
					+ "sockets",
			name = "internalFacingBindHostAddressTypes",
			syntax = "internalFacingBindHostAddressTypes=HOST_ADDRESS_TYPES",
			valueType = HostAddressTypes.class
	)
	public static final SettingSpec<HostAddressTypes> INTERNAL_FACING_BIND_HOST_ADDRESS_TYPES =
			SETTING_SPECS.addThenGet(new HostAddressTypesSettingSpec(
					"internalFacingBindHostAddressTypes",
					HostAddressTypes.of()));

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
					PortRanges.of()));
			
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
					PortRanges.of()));

	@NameValuePairValueSpecDoc(
			description = "The default network interface that provides a "
					+ "binding host address for all internal-facing sockets",
			name = "internalFacingNetInterface",
			syntax = "internalFacingNetInterface=NETWORK_INTERFACE",
			valueType = NetInterface.class
	)
	public static final SettingSpec<NetInterface> INTERNAL_FACING_NET_INTERFACE =
			SETTING_SPECS.addThenGet(new NetInterfaceSettingSpec(
					"internalFacingNetInterface",
					null));

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
					SocketSettings.of()));
	
	@NameValuePairValueSpecDoc(
			defaultValue = "lastRoute",
			description = "The ID for the last and unassigned route",
			name = "lastRouteId",
			syntax = "lastRouteId=ROUTE_ID",
			valueType = String.class
	)	
	public static final SettingSpec<String> LAST_ROUTE_ID = 
			SETTING_SPECS.addThenGet(new StringSettingSpec(
					"lastRouteId",
					"lastRoute"));

	@NameValuePairValueSpecDoc(
			description = "The default network interface that provides a "
					+ "binding host address for all sockets",
			name = "netInterface",
			syntax = "netInterface=NETWORK_INTERFACE",
			valueType = NetInterface.class
	)
	public static final SettingSpec<NetInterface> NET_INTERFACE =
			SETTING_SPECS.addThenGet(new NetInterfaceSettingSpec(
					"netInterface",
					null));

	@NameValuePairValueSpecDoc(
			defaultValue = "1080",
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
			defaultValue = "CYCLICAL",
			description = "The selection strategy for the next route",
			name = "routeSelectionStrategy",
			syntax = "routeSelectionStrategy=SELECTION_STRATEGY",
			valueType = SelectionStrategy.class
	)
	public static final SettingSpec<SelectionStrategy> ROUTE_SELECTION_STRATEGY =
			SETTING_SPECS.addThenGet(new SelectionStrategySettingSpec(
					"routeSelectionStrategy",
					SelectionStrategySpecConstants.CYCLICAL.newSelectionStrategy()));
	
	@NameValuePairValueSpecDoc(
			defaultValue = "firewallAction=ALLOW",
			description = "A rule for the SOCKS server",
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
					SocketSettings.of()));
	
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
			description = "The comma separated list of acceptable binding "
					+ "host address types for the SOCKS server socket",
			name = "socksServerBindHostAddressTypes",
			syntax = "socksServerBindHostAddressTypes=HOST_ADDRESS_TYPES",
			valueType = HostAddressTypes.class
	)
	public static final SettingSpec<HostAddressTypes> SOCKS_SERVER_BIND_HOST_ADDRESS_TYPES =
			SETTING_SPECS.addThenGet(new HostAddressTypesSettingSpec(
					"socksServerBindHostAddressTypes",
					HostAddressTypes.of()));

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
					PortRanges.of()));

	@NameValuePairValueSpecDoc(
			description = "The network interface that provides a binding host "
					+ "address for the SOCKS server socket",
			name = "socksServerNetInterface",
			syntax = "socksServerNetInterface=NETWORK_INTERFACE",
			valueType = NetInterface.class
	)
	public static final SettingSpec<NetInterface> SOCKS_SERVER_NET_INTERFACE =
			SETTING_SPECS.addThenGet(new NetInterfaceSettingSpec(
					"socksServerNetInterface",
					null));

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
					SocketSettings.of()));
	
	public static List<SettingSpec<Object>> values() {
		return SETTING_SPECS.toList();
	}
	
	public static Map<String, SettingSpec<Object>> valuesMap() {
		return SETTING_SPECS.toMap();
	}
	
	private GeneralSettingSpecConstants() { }
	
}
