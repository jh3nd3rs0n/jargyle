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
					+ "TCP connections to the SOCKS server",
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
					+ "endpoints",
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
					+ "binding host address types for all endpoints",
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
					+ "ranges for all TCP endpoints",
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
					+ "ranges for all UDP endpoints",
			name = "bindUdpPortRanges",
			syntax = "bindUdpPortRanges=PORT_RANGES",
			valueType = PortRanges.class
	)
	public static final SettingSpec<PortRanges> BIND_UDP_PORT_RANGES =
			SETTING_SPECS.addThenGet(new PortRangesSettingSpec(
					"bindUdpPortRanges",
					PortRanges.getDefault()));

	@NameValuePairValueSpecDoc(
			description = "The default binding host name or address for all "
					+ "client endpoints",
			name = "clientEndpoints.bindHost",
			syntax = "clientEndpoints.bindHost=HOST",
			valueType = Host.class
	)
	public static final SettingSpec<Host> CLIENT_ENDPOINTS_BIND_HOST =
			SETTING_SPECS.addThenGet(new HostSettingSpec(
					"clientEndpoints.bindHost",
					null));

	@NameValuePairValueSpecDoc(
			description = "The comma separated list of default acceptable "
					+ "binding host address types for all client endpoints",
			name = "clientEndpoints.bindHostAddressTypes",
			syntax = "clientEndpoints.bindHostAddressTypes=HOST_ADDRESS_TYPES",
			valueType = HostAddressTypes.class
	)
	public static final SettingSpec<HostAddressTypes> CLIENT_ENDPOINTS_BIND_HOST_ADDRESS_TYPES =
			SETTING_SPECS.addThenGet(new HostAddressTypesSettingSpec(
					"clientEndpoints.bindHostAddressTypes",
					HostAddressTypes.of()));

	@NameValuePairValueSpecDoc(
			description = "The comma separated list of default binding port "
					+ "ranges for all client TCP endpoints",
			name = "clientEndpoints.bindTcpPortRanges",
			syntax = "clientEndpoints.bindTcpPortRanges=PORT_RANGES",
			valueType = PortRanges.class
	)
	public static final SettingSpec<PortRanges> CLIENT_ENDPOINTS_BIND_TCP_PORT_RANGES =
			SETTING_SPECS.addThenGet(new PortRangesSettingSpec(
					"clientEndpoints.bindTcpPortRanges",
					PortRanges.of()));

	@NameValuePairValueSpecDoc(
			description = "The comma separated list of default binding port "
					+ "ranges for all client UDP endpoints",
			name = "clientEndpoints.bindUdpPortRanges",
			syntax = "clientEndpoints.bindUdpPortRanges=PORT_RANGES",
			valueType = PortRanges.class
	)
	public static final SettingSpec<PortRanges> CLIENT_ENDPOINTS_BIND_UDP_PORT_RANGES =
			SETTING_SPECS.addThenGet(new PortRangesSettingSpec(
					"clientEndpoints.bindUdpPortRanges",
					PortRanges.of()));

	@NameValuePairValueSpecDoc(
			description = "The default network interface that provides a "
					+ "binding host address for all client endpoints",
			name = "clientEndpoints.netInterface",
			syntax = "clientEndpoints.netInterface=NETWORK_INTERFACE",
			valueType = NetInterface.class
	)
	public static final SettingSpec<NetInterface> CLIENT_ENDPOINTS_NET_INTERFACE =
			SETTING_SPECS.addThenGet(new NetInterfaceSettingSpec(
					"clientEndpoints.netInterface",
					null));

	@NameValuePairValueSpecDoc(
			description = "The comma separated list of default socket settings "
					+ "for all client endpoints",
			name = "clientEndpoints.socketSettings",
			syntax = "clientEndpoints.socketSettings=SOCKET_SETTINGS",
			valueType = SocketSettings.class
	)
	public static final SettingSpec<SocketSettings> CLIENT_ENDPOINTS_SOCKET_SETTINGS =
			SETTING_SPECS.addThenGet(new SocketSettingsSettingSpec(
					"clientEndpoints.socketSettings",
					SocketSettings.of()));

	@NameValuePairValueSpecDoc(
			description = "The binding host name or address for the "
					+ "client-facing TCP server socket",
			name = "clientFacingServerSocket.bindHost",
			syntax = "clientFacingServerSocket.bindHost=HOST",
			valueType = Host.class
	)
	public static final SettingSpec<Host> CLIENT_FACING_SERVER_SOCKET_BIND_HOST =
			SETTING_SPECS.addThenGet(new HostSettingSpec(
					"clientFacingServerSocket.bindHost",
					null));

	@NameValuePairValueSpecDoc(
			description = "The comma separated list of acceptable binding "
					+ "host address types for the client-facing TCP server "
					+ "socket",
			name = "clientFacingServerSocket.bindHostAddressTypes",
			syntax = "clientFacingServerSocket.bindHostAddressTypes=HOST_ADDRESS_TYPES",
			valueType = HostAddressTypes.class
	)
	public static final SettingSpec<HostAddressTypes> CLIENT_FACING_SERVER_SOCKET_BIND_HOST_ADDRESS_TYPES =
			SETTING_SPECS.addThenGet(new HostAddressTypesSettingSpec(
					"clientFacingServerSocket.bindHostAddressTypes",
					HostAddressTypes.of()));

	@NameValuePairValueSpecDoc(
			description = "The comma separated list of binding port ranges for "
					+ "the client-facing TCP server socket",
			name = "clientFacingServerSocket.bindPortRanges",
			syntax = "clientFacingServerSocket.bindPortRangesPORT_RANGES",
			valueType = PortRanges.class
	)
	public static final SettingSpec<PortRanges> CLIENT_FACING_SERVER_SOCKET_BIND_PORT_RANGES =
			SETTING_SPECS.addThenGet(new PortRangesSettingSpec(
					"clientFacingServerSocket.bindPortRanges",
					PortRanges.of()));

	@NameValuePairValueSpecDoc(
			description = "The network interface that provides a binding host "
					+ "address for the client-facing TCP server socket",
			name = "clientFacingServerSocket.netInterface",
			syntax = "clientFacingServerSocket.netInterface=NETWORK_INTERFACE",
			valueType = NetInterface.class
	)
	public static final SettingSpec<NetInterface> CLIENT_FACING_SERVER_SOCKET_NET_INTERFACE =
			SETTING_SPECS.addThenGet(new NetInterfaceSettingSpec(
					"clientFacingServerSocket.netInterface",
					null));

	@NameValuePairValueSpecDoc(
			description = "The comma separated list of socket settings for the "
					+ "client-facing TCP server socket",
			name = "clientFacingServerSocket.socketSettings",
			syntax = "clientFacingServerSocket.socketSettings=SOCKET_SETTINGS",
			valueType = SocketSettings.class
	)
	public static final SettingSpec<SocketSettings> CLIENT_FACING_SERVER_SOCKET_SOCKET_SETTINGS =
			SETTING_SPECS.addThenGet(new SocketSettingsSettingSpec(
					"clientFacingServerSocket.socketSettings",
					SocketSettings.of()));
	
	@NameValuePairValueSpecDoc(
			description = "The comma separated list of socket settings for the "
					+ "client TCP socket",
			name = "clientSocket.socketSettings",
			syntax = "clientSocket.socketSettings=SOCKET_SETTINGS",
			valueType = SocketSettings.class
	)
	public static final SettingSpec<SocketSettings> CLIENT_SOCKET_SOCKET_SETTINGS = 
			SETTING_SPECS.addThenGet(new SocketSettingsSettingSpec(
					"clientSocket.socketSettings", 
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
					+ "binding host address for all endpoints",
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
			description = "The port for the SOCKS server (or specifically "
					+ "the port for the client-facing TCP server socket)",
			name = "port",
			syntax = "port=PORT",
			valueType = Port.class
	)
	public static final SettingSpec<Port> PORT = 
			SETTING_SPECS.addThenGet(new PortSettingSpec(
					"port", 
					null));

	@NameValuePairValueSpecDoc(
			description = "The default binding host name or address for all "
					+ "remote endpoints",
			name = "remoteEndpoints.bindHost",
			syntax = "remoteEndpoints.bindHost=HOST",
			valueType = Host.class
	)
	public static final SettingSpec<Host> REMOTE_ENDPOINTS_BIND_HOST =
			SETTING_SPECS.addThenGet(new HostSettingSpec(
					"remoteEndpoints.bindHost",
					null));

	@NameValuePairValueSpecDoc(
			description = "The comma separated list of default acceptable "
					+ "binding host address types for all remote endpoints",
			name = "remoteEndpoints.bindHostAddressTypes",
			syntax = "remoteEndpoints.bindHostAddressTypes=HOST_ADDRESS_TYPES",
			valueType = HostAddressTypes.class
	)
	public static final SettingSpec<HostAddressTypes> REMOTE_ENDPOINTS_BIND_HOST_ADDRESS_TYPES =
			SETTING_SPECS.addThenGet(new HostAddressTypesSettingSpec(
					"remoteEndpoints.bindHostAddressTypes",
					HostAddressTypes.of()));

	@NameValuePairValueSpecDoc(
			description = "The comma separated list of default binding port "
					+ "ranges for all remote TCP endpoints",
			name = "remoteEndpoints.bindTcpPortRanges",
			syntax = "remoteEndpoints.bindTcpPortRanges=PORT_RANGES",
			valueType = PortRanges.class
	)
	public static final SettingSpec<PortRanges> REMOTE_ENDPOINTS_BIND_TCP_PORT_RANGES =
			SETTING_SPECS.addThenGet(new PortRangesSettingSpec(
					"remoteEndpoints.bindTcpPortRanges",
					PortRanges.of()));

	@NameValuePairValueSpecDoc(
			description = "The comma separated list of default binding port "
					+ "ranges for all remote UDP endpoints",
			name = "remoteEndpoints.bindUdpPortRanges",
			syntax = "remoteEndpoints.bindUdpPortRanges=PORT_RANGES",
			valueType = PortRanges.class
	)
	public static final SettingSpec<PortRanges> REMOTE_ENDPOINTS_BIND_UDP_PORT_RANGES =
			SETTING_SPECS.addThenGet(new PortRangesSettingSpec(
					"remoteEndpoints.bindUdpPortRanges",
					PortRanges.of()));

	@NameValuePairValueSpecDoc(
			description = "The default network interface that provides a "
					+ "binding host address for all remote endpoints",
			name = "remoteEndpoints.netInterface",
			syntax = "remoteEndpoints.netInterface=NETWORK_INTERFACE",
			valueType = NetInterface.class
	)
	public static final SettingSpec<NetInterface> REMOTE_ENDPOINTS_NET_INTERFACE =
			SETTING_SPECS.addThenGet(new NetInterfaceSettingSpec(
					"remoteEndpoints.netInterface",
					null));

	@NameValuePairValueSpecDoc(
			description = "The comma separated list of default socket settings "
					+ "for all remote endpoints",
			name = "remoteEndpoints.socketSettings",
			syntax = "remoteEndpoints.socketSettings=SOCKET_SETTINGS",
			valueType = SocketSettings.class
	)
	public static final SettingSpec<SocketSettings> REMOTE_ENDPOINTS_SOCKET_SETTINGS =
			SETTING_SPECS.addThenGet(new SocketSettingsSettingSpec(
					"remoteEndpoints.socketSettings",
					SocketSettings.of()));

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
					+ "for all endpoints",
			name = "socketSettings",
			syntax = "socketSettings=SOCKET_SETTINGS",
			valueType = SocketSettings.class
	)
	public static final SettingSpec<SocketSettings> SOCKET_SETTINGS = 
			SETTING_SPECS.addThenGet(new SocketSettingsSettingSpec(
					"socketSettings", 
					SocketSettings.of()));
	
	public static List<SettingSpec<Object>> values() {
		return SETTING_SPECS.toList();
	}
	
	public static Map<String, SettingSpec<Object>> valuesMap() {
		return SETTING_SPECS.toMap();
	}
	
	private GeneralSettingSpecConstants() { }
	
}
