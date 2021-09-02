package com.github.jh3nd3rs0n.jargyle.net.socks.client.propertyspec.impl;

import com.github.jh3nd3rs0n.jargyle.net.SocketSettings;
import com.github.jh3nd3rs0n.jargyle.net.socks.client.Property;
import com.github.jh3nd3rs0n.jargyle.net.socks.client.PropertySpec;

public final class SocketSettingsPropertySpec 
	extends PropertySpec<SocketSettings> {

	public SocketSettingsPropertySpec(
			final Object permissionObj, 
			final String s, 
			final SocketSettings defaultVal) {
		super(permissionObj, s, SocketSettings.class, defaultVal);
	}

	@Override
	public Property<SocketSettings> newPropertyOfParsableValue(
			final String value) {
		return super.newProperty(SocketSettings.newInstance(value));
	}

}
