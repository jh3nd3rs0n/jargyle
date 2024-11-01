package com.github.jh3nd3rs0n.jargyle.server.internal.ruleactionspec.impl;

import com.github.jh3nd3rs0n.jargyle.protocolbase.socks5.Address;
import com.github.jh3nd3rs0n.jargyle.server.RuleActionSpec;

public final class Socks5AddressRuleActionSpec extends RuleActionSpec<Address> {

	public Socks5AddressRuleActionSpec(final String n) {
		super(n, Address.class);
	}

	@Override
	protected Address parse(final String value) {
		return Address.newInstanceFrom(value);
	}

}
