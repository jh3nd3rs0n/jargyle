package com.github.jh3nd3rs0n.jargyle.server.internal.ruleactionspec.impl;

import com.github.jh3nd3rs0n.jargyle.common.net.Port;
import com.github.jh3nd3rs0n.jargyle.server.RuleActionSpec;

public final class PortRuleActionSpec extends RuleActionSpec<Port> {

	public PortRuleActionSpec(final String n) {
		super(n, Port.class);
	}

	@Override
	protected Port parse(final String value) {
		return Port.valueOf(value);
	}

}
