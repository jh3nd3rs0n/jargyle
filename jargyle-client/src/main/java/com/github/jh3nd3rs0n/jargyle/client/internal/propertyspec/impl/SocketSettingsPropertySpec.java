package com.github.jh3nd3rs0n.jargyle.client.internal.propertyspec.impl;

import com.github.jh3nd3rs0n.jargyle.client.PropertySpec;
import com.github.jh3nd3rs0n.jargyle.common.net.SocketSettings;

public final class SocketSettingsPropertySpec 
	extends PropertySpec<SocketSettings> {

	public SocketSettingsPropertySpec(
			final String n, final SocketSettings defaultVal) {
		super(n, SocketSettings.class, defaultVal);
	}

	@Override
	protected SocketSettings parse(final String value) {
		return SocketSettings.newInstanceFrom(value);
	}

}
