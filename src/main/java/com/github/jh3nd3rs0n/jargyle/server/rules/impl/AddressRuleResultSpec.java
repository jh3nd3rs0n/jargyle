package com.github.jh3nd3rs0n.jargyle.server.rules.impl;

import com.github.jh3nd3rs0n.jargyle.server.RuleResult;
import com.github.jh3nd3rs0n.jargyle.server.RuleResultSpec;
import com.github.jh3nd3rs0n.jargyle.transport.socks5.Address;

public final class AddressRuleResultSpec extends RuleResultSpec<Address> {

	public AddressRuleResultSpec(final String s) {
		super(s, Address.class);
	}

	@Override
	public RuleResult<Address> newRuleResultOfParsableValue(
			final String value) {
		return super.newRuleResult(Address.newInstance(value));
	}

}
