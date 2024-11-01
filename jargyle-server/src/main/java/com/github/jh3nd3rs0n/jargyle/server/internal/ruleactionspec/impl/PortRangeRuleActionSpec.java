package com.github.jh3nd3rs0n.jargyle.server.internal.ruleactionspec.impl;

import com.github.jh3nd3rs0n.jargyle.common.net.PortRange;
import com.github.jh3nd3rs0n.jargyle.server.RuleActionSpec;

public final class PortRangeRuleActionSpec extends RuleActionSpec<PortRange> {

	public PortRangeRuleActionSpec(final String n) {
		super(n, PortRange.class);
	}

	@Override
	protected PortRange parse(final String value) {
		return PortRange.newInstanceFrom(value);
	}

}
