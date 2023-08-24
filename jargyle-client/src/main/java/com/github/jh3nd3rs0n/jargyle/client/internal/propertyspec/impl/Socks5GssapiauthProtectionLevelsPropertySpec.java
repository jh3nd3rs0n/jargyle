package com.github.jh3nd3rs0n.jargyle.client.internal.propertyspec.impl;

import com.github.jh3nd3rs0n.jargyle.client.Property;
import com.github.jh3nd3rs0n.jargyle.client.PropertySpec;
import com.github.jh3nd3rs0n.jargyle.transport.socks5.gssapiauth.ProtectionLevels;

public final class Socks5GssapiauthProtectionLevelsPropertySpec 
	extends PropertySpec<ProtectionLevels> {

	public Socks5GssapiauthProtectionLevelsPropertySpec(
			final String n, final ProtectionLevels defaultVal) {
		super(n, ProtectionLevels.class, defaultVal);
	}

	@Override
	public Property<ProtectionLevels> newPropertyOfParsableValue(
			final String value) {
		return super.newProperty(ProtectionLevels.newInstance(value));
	}

}
