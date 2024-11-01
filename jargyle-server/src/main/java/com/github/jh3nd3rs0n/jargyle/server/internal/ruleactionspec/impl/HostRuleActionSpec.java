package com.github.jh3nd3rs0n.jargyle.server.internal.ruleactionspec.impl;

import com.github.jh3nd3rs0n.jargyle.common.net.Host;
import com.github.jh3nd3rs0n.jargyle.server.RuleActionSpec;

public final class HostRuleActionSpec extends RuleActionSpec<Host> {

	public HostRuleActionSpec(final String n) {
		super(n, Host.class);
	}

	@Override
	protected Host parse(final String value) {
		return Host.newInstance(value);
	}

}
