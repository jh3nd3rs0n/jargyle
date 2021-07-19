package jargyle.net.socks.client.propertyspec.impl;

import jargyle.net.socks.client.Property;
import jargyle.net.socks.client.PropertySpec;
import jargyle.net.socks.transport.v5.gssapiauth.ProtectionLevels;

final class ProtectionLevelsPropertySpec 
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
