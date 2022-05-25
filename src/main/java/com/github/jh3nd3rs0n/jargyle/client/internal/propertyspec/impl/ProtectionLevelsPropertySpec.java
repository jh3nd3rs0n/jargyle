package com.github.jh3nd3rs0n.jargyle.client.internal.propertyspec.impl;

import com.github.jh3nd3rs0n.jargyle.client.Property;
import com.github.jh3nd3rs0n.jargyle.client.PropertySpec;
import com.github.jh3nd3rs0n.jargyle.transport.socks5.gssapiauth.ProtectionLevels;

public final class ProtectionLevelsPropertySpec 
	extends PropertySpec<ProtectionLevels> {

	public ProtectionLevelsPropertySpec(
			final String s, final ProtectionLevels defaultVal) {
		super(s, ProtectionLevels.class, defaultVal);
	}

	@Override
	public Property<ProtectionLevels> newPropertyOfParsableValue(
			final String value) {
		return super.newProperty(ProtectionLevels.newInstance(value));
	}

}
