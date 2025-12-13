package com.github.jh3nd3rs0n.jargyle.client.internal.propertyspec.impl;

import com.github.jh3nd3rs0n.jargyle.client.PropertySpec;
import com.github.jh3nd3rs0n.jargyle.protocolbase.socks5.gssapiauthmethod.ProtectionLevels;

/**
 * An implementation of {@code PropertySpec} of type {@code ProtectionLevels}.
 */
public final class Socks5GssapiAuthMethodProtectionLevelsPropertySpec 
	extends PropertySpec<ProtectionLevels> {

	/**
	 * Constructs a {@code Socks5GssapiAuthMethodProtectionLevelsPropertySpec}
	 * with the provided name and the provided default value.
	 * @param n the provided name
	 * @param defaultVal the provided default value (can be {@code null})
	 */
	public Socks5GssapiAuthMethodProtectionLevelsPropertySpec(
			final String n, final ProtectionLevels defaultVal) {
		super(n, ProtectionLevels.class, defaultVal);
	}

	@Override
	protected ProtectionLevels parse(final String value) {
		return ProtectionLevels.newInstanceFrom(value);
	}

}
