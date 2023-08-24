package com.github.jh3nd3rs0n.jargyle.server.internal.ruleresultspec.impl;

import com.github.jh3nd3rs0n.jargyle.common.net.PortRange;
import com.github.jh3nd3rs0n.jargyle.server.RuleResult;
import com.github.jh3nd3rs0n.jargyle.server.RuleResultSpec;

public final class PortRangeRuleResultSpec extends RuleResultSpec<PortRange> {

	public PortRangeRuleResultSpec(final String n) {
		super(n, PortRange.class);
	}

	@Override
	public RuleResult<PortRange> newRuleResultOfParsableValue(
			final String value) {
		return super.newRuleResult(PortRange.newInstance(value));
	}

}
