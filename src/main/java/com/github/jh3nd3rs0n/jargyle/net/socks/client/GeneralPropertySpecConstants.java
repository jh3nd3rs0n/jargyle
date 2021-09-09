package com.github.jh3nd3rs0n.jargyle.net.socks.client;

import java.util.List;

import com.github.jh3nd3rs0n.jargyle.net.Host;
import com.github.jh3nd3rs0n.jargyle.net.Port;
import com.github.jh3nd3rs0n.jargyle.net.SocketSettings;
import com.github.jh3nd3rs0n.jargyle.net.socks.client.propertyspec.impl.HostPropertySpec;
import com.github.jh3nd3rs0n.jargyle.net.socks.client.propertyspec.impl.PortPropertySpec;
import com.github.jh3nd3rs0n.jargyle.net.socks.client.propertyspec.impl.PositiveIntegerPropertySpec;
import com.github.jh3nd3rs0n.jargyle.net.socks.client.propertyspec.impl.SocketSettingsPropertySpec;
import com.github.jh3nd3rs0n.jargyle.util.PositiveInteger;

public final class GeneralPropertySpecConstants {

	private static final PropertySpecs PROPERTY_SPECS = new PropertySpecs();
	
	public static final PropertySpec<Host> INTERNAL_BIND_HOST = 
			PROPERTY_SPECS.addThenGet(new HostPropertySpec(
					NewPropertySpecPermission.INSTANCE,
					"socksClient.internalBindHost",
					Host.getInet4AllZerosInstance()));

	public static final PropertySpec<Port> INTERNAL_BIND_PORT = 
			PROPERTY_SPECS.addThenGet(new PortPropertySpec(
					NewPropertySpecPermission.INSTANCE,
					"socksClient.internalBindPort",
					Port.newInstance(0)));
	
	public static final PropertySpec<PositiveInteger> INTERNAL_CONNECT_TIMEOUT = 
			PROPERTY_SPECS.addThenGet(new PositiveIntegerPropertySpec(
					NewPropertySpecPermission.INSTANCE,
					"socksClient.internalConnectTimeout",
					PositiveInteger.newInstance(60000))); // 1 minute
	
	public static final PropertySpec<SocketSettings> INTERNAL_SOCKET_SETTINGS = 
			PROPERTY_SPECS.addThenGet(new SocketSettingsPropertySpec(
					NewPropertySpecPermission.INSTANCE,
					"socksClient.internalSocketSettings",
					SocketSettings.newInstance()));
	
	public static List<PropertySpec<Object>> values() {
		return PROPERTY_SPECS.toList();
	}
	
	private GeneralPropertySpecConstants() { }
}
