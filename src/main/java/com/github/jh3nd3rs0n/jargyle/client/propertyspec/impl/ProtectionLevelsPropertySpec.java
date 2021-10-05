package com.github.jh3nd3rs0n.jargyle.client.propertyspec.impl;

import com.github.jh3nd3rs0n.jargyle.client.Property;
import com.github.jh3nd3rs0n.jargyle.client.PropertySpec;
import com.github.jh3nd3rs0n.jargyle.transport.socks5.gssapiauth.ProtectionLevels;

public final class ProtectionLevelsPropertySpec 
	extends PropertySpec<ProtectionLevels> {

	public ProtectionLevelsPropertySpec(
			final Object permission, 
			final String s, 
			final ProtectionLevels defaultVal) {
		super(permission, s, ProtectionLevels.class, defaultVal);
	}

	@Override
	public Property<ProtectionLevels> newPropertyOfParsableValue(
			final String value) {
		return super.newProperty(ProtectionLevels.newInstance(value));
	}

}
