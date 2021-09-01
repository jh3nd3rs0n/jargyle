package com.github.jh3nd3rs0n.jargyle.net.socks.client.propertyspec.impl;

import com.github.jh3nd3rs0n.jargyle.net.socks.client.Property;
import com.github.jh3nd3rs0n.jargyle.net.socks.client.PropertySpec;
import com.github.jh3nd3rs0n.jargyle.net.socks.transport.v5.gssapiauth.ProtectionLevels;

final class ProtectionLevelsPropertySpec 
	extends PropertySpec<ProtectionLevels> {

	public ProtectionLevelsPropertySpec(
			final Object permissionObj, 
			final String s, 
			final ProtectionLevels defaultVal) {
		super(permissionObj, s, ProtectionLevels.class, defaultVal);
	}

	@Override
	public Property<ProtectionLevels> newPropertyOfParsableValue(
			final String value) {
		return super.newProperty(ProtectionLevels.newInstance(value));
	}

}
