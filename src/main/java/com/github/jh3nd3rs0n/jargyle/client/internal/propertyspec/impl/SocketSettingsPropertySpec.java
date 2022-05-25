package com.github.jh3nd3rs0n.jargyle.client.internal.propertyspec.impl;

import com.github.jh3nd3rs0n.jargyle.client.Property;
import com.github.jh3nd3rs0n.jargyle.client.PropertySpec;
import com.github.jh3nd3rs0n.jargyle.common.net.SocketSettings;

public final class SocketSettingsPropertySpec 
	extends PropertySpec<SocketSettings> {

	public SocketSettingsPropertySpec(
			final String s, final SocketSettings defaultVal) {
		super(s, SocketSettings.class, defaultVal);
	}

	@Override
	public Property<SocketSettings> newPropertyOfParsableValue(
			final String value) {
		return super.newProperty(SocketSettings.newInstance(value));
	}

}
