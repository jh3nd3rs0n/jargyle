package com.github.jh3nd3rs0n.jargyle.client.internal.propertyspec.impl;

import com.github.jh3nd3rs0n.jargyle.client.PropertySpec;
import com.github.jh3nd3rs0n.jargyle.protocolbase.socks5.gssapimethod.ProtectionLevels;

public final class Socks5GssapiMethodProtectionLevelsPropertySpec 
	extends PropertySpec<ProtectionLevels> {

	public Socks5GssapiMethodProtectionLevelsPropertySpec(
			final String n, final ProtectionLevels defaultVal) {
		super(n, ProtectionLevels.class, defaultVal);
	}

	@Override
	protected ProtectionLevels parse(final String value) {
		return ProtectionLevels.newInstanceFrom(value);
	}

}
