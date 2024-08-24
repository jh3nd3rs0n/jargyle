package com.github.jh3nd3rs0n.jargyle.server.internal.ruleresultspec.impl;

import com.github.jh3nd3rs0n.jargyle.protocolbase.socks5.Address;
import com.github.jh3nd3rs0n.jargyle.server.RuleResultSpec;

public final class AddressRuleResultSpec extends RuleResultSpec<Address> {

	public AddressRuleResultSpec(final String n) {
		super(n, Address.class);
	}

	@Override
	protected Address parse(final String value) {
		return Address.newInstanceFrom(value);
	}

}
