package com.github.jh3nd3rs0n.jargyle.server.rules.impl;

import java.net.UnknownHostException;

import com.github.jh3nd3rs0n.jargyle.common.net.Host;
import com.github.jh3nd3rs0n.jargyle.server.RuleResult;
import com.github.jh3nd3rs0n.jargyle.server.RuleResultSpec;

public final class HostRuleResultSpec extends RuleResultSpec<Host> {

	public HostRuleResultSpec(final String s) {
		super(s, Host.class);
	}

	@Override
	public RuleResult<Host> newRuleResultOfParsableValue(final String value) {
		Host host = null;
		try {
			host = Host.newInstance(value);
		} catch (UnknownHostException e) {
			throw new IllegalArgumentException(e);
		}
		return super.newRuleResult(host);
	}

}
