package com.github.jh3nd3rs0n.jargyle.server.internal.ruleresultspec.impl;

import com.github.jh3nd3rs0n.jargyle.common.net.Host;
import com.github.jh3nd3rs0n.jargyle.server.RuleResultSpec;

public final class HostRuleResultSpec extends RuleResultSpec<Host> {

	public HostRuleResultSpec(final String n) {
		super(n, Host.class);
	}

	@Override
	protected Host parse(final String value) {
		return Host.newInstance(value);
	}

}
