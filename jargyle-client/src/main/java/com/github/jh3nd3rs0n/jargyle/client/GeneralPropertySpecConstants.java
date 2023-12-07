package com.github.jh3nd3rs0n.jargyle.client;

import java.util.List;
import java.util.Map;

import com.github.jh3nd3rs0n.jargyle.client.internal.propertyspec.impl.HostPropertySpec;
import com.github.jh3nd3rs0n.jargyle.client.internal.propertyspec.impl.PortRangesPropertySpec;
import com.github.jh3nd3rs0n.jargyle.client.internal.propertyspec.impl.PositiveIntegerPropertySpec;
import com.github.jh3nd3rs0n.jargyle.client.internal.propertyspec.impl.SocketSettingsPropertySpec;
import com.github.jh3nd3rs0n.jargyle.common.net.Host;
import com.github.jh3nd3rs0n.jargyle.common.net.PortRanges;
import com.github.jh3nd3rs0n.jargyle.common.net.SocketSettings;
import com.github.jh3nd3rs0n.jargyle.common.number.PositiveInteger;
import com.github.jh3nd3rs0n.jargyle.internal.annotation.NameValuePairValueSpecDoc;
import com.github.jh3nd3rs0n.jargyle.internal.annotation.NameValuePairValueSpecsDoc;

@NameValuePairValueSpecsDoc(
		description = "",
		name = "General Properties"
)
public final class GeneralPropertySpecConstants {

	private static final PropertySpecs PROPERTY_SPECS = new PropertySpecs();
	
	@NameValuePairValueSpecDoc(
			description = "The binding host name or address for the client "
					+ "socket that is used to connect to the SOCKS server "
					+ "(used for the SOCKS5 commands RESOLVE, BIND and UDP "
					+ "ASSOCIATE) (default is 0.0.0.0)",
			name = "socksClient.clientBindHost",
			syntax = "socksClient.clientBindHost=HOST",
			valueType = Host.class
	)
	public static final PropertySpec<Host> CLIENT_BIND_HOST = 
			PROPERTY_SPECS.addThenGet(new HostPropertySpec(
					"socksClient.clientBindHost",
					Host.getAllZerosIpv4AddressInstance()));
	
	@NameValuePairValueSpecDoc(
			description = "The comma separated list of binding port ranges for "
					+ "the client socket that is used to connect to the SOCKS "
					+ "server (used for the SOCKS5 commands RESOLVE, BIND and "
					+ "UDP ASSOCIATE) (default is 0)",
			name = "socksClient.clientBindPortRanges",
			syntax = "socksClient.clientBindPortRanges=PORT_RANGES",
			valueType = PortRanges.class
	)
	public static final PropertySpec<PortRanges> CLIENT_BIND_PORT_RANGES =
			PROPERTY_SPECS.addThenGet(new PortRangesPropertySpec(
					"socksClient.clientBindPortRanges",
					PortRanges.getDefault()));
	
	@NameValuePairValueSpecDoc(
			description = "The timeout in milliseconds on waiting for the "
					+ "client socket to connect to the SOCKS server (used for "
					+ "the SOCKS5 commands RESOLVE, BIND and UDP ASSOCIATE) "
					+ "(default is 60000)",
			name = "socksClient.clientConnectTimeout",
			syntax = "socksClient.clientConnectTimeout=POSITIVE_INTEGER",
			valueType = PositiveInteger.class
	)
	public static final PropertySpec<PositiveInteger> CLIENT_CONNECT_TIMEOUT = 
			PROPERTY_SPECS.addThenGet(new PositiveIntegerPropertySpec(
					"socksClient.clientConnectTimeout",
					PositiveInteger.newInstance(60000))); // 1 minute
	
	@NameValuePairValueSpecDoc(
			description = "The comma separated list of socket settings for the "
					+ "client socket that is used to connect to the SOCKS "
					+ "server (used for the SOCKS5 command RESOLVE and UDP "
					+ "ASSOCIATE)",
			name = "socksClient.clientSocketSettings",
			syntax = "socksClient.clientSocketSettings=SOCKET_SETTINGS",
			valueType = SocketSettings.class
	)	
	public static final PropertySpec<SocketSettings> CLIENT_SOCKET_SETTINGS = 
			PROPERTY_SPECS.addThenGet(new SocketSettingsPropertySpec(
					"socksClient.clientSocketSettings",
					SocketSettings.newInstance()));
	
	public static List<PropertySpec<Object>> values() {
		return PROPERTY_SPECS.toList();
	}
	
	public static Map<String, PropertySpec<Object>> valuesMap() {
		return PROPERTY_SPECS.toMap();
	}
	
	private GeneralPropertySpecConstants() { }
}
