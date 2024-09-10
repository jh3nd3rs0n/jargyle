package com.github.jh3nd3rs0n.jargyle.client;

import java.util.List;
import java.util.Map;

import com.github.jh3nd3rs0n.jargyle.client.internal.propertyspec.impl.HostPropertySpec;
import com.github.jh3nd3rs0n.jargyle.client.internal.propertyspec.impl.PortPropertySpec;
import com.github.jh3nd3rs0n.jargyle.client.internal.propertyspec.impl.SchemePropertySpec;
import com.github.jh3nd3rs0n.jargyle.common.net.Host;
import com.github.jh3nd3rs0n.jargyle.common.net.Port;
import com.github.jh3nd3rs0n.jargyle.internal.annotation.NameValuePairValueSpecDoc;
import com.github.jh3nd3rs0n.jargyle.internal.annotation.NameValuePairValueSpecsDoc;

@NameValuePairValueSpecsDoc(
		description = "",
		name = "SOCKS Server URI Properties"
)
public final class SocksServerUriPropertySpecConstants {

	private static final PropertySpecs PROPERTY_SPECS = new PropertySpecs();
	
	@NameValuePairValueSpecDoc(
			description = "The port of the SOCKS server URI",
			name = "socksServerUri.port",
			syntax = "socksServerUri.port=PORT",
			valueType = Port.class
	)
	public static final PropertySpec<Port> PORT =
			PROPERTY_SPECS.addThenGet(new PortPropertySpec(
					"socksServerUri.port",
					null));
	
	@NameValuePairValueSpecDoc(
			description = "The host name or address of the SOCKS server URI",
			name = "socksServerUri.host",
			syntax = "socksServerUri.host=HOST",
			valueType = Host.class
	)
	public static final PropertySpec<Host> HOST =
			PROPERTY_SPECS.addThenGet(new HostPropertySpec(
					"socksServerUri.host",
					null));

	@NameValuePairValueSpecDoc(
			description = "The scheme of the SOCKS server URI",
			name = "socksServerUri.scheme",
			syntax = "socksServerUri.scheme=SCHEME",
			valueType = Scheme.class
	)
	public static final PropertySpec<Scheme> SCHEME = 
			PROPERTY_SPECS.addThenGet(new SchemePropertySpec(
					"socksServerUri.scheme",
					null));
	
	public static List<PropertySpec<Object>> values() {
		return PROPERTY_SPECS.toList();
	}
	
	public static Map<String, PropertySpec<Object>> valuesMap() {
		return PROPERTY_SPECS.toMap();
	}
	
	private SocksServerUriPropertySpecConstants() { }
	
}
