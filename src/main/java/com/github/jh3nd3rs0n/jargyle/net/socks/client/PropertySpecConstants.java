package com.github.jh3nd3rs0n.jargyle.net.socks.client;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

final class PropertySpecConstants {

	private static final List<PropertySpec<Object>> VALUES;
	
	static {
		List<PropertySpec<Object>> values = 
				new ArrayList<PropertySpec<Object>>();
		values.addAll(DtlsPropertySpecConstants.values());
		values.addAll(GeneralPropertySpecConstants.values());
		values.addAll(Socks5PropertySpecConstants.values());
		values.addAll(SslPropertySpecConstants.values());
		VALUES = values;
	}
	
	public static List<PropertySpec<Object>> values() {
		return Collections.unmodifiableList(VALUES);
	}
	
	private PropertySpecConstants() { }
	
}
