package com.github.jh3nd3rs0n.jargyle.server.internal.ruleresultspec.impl;

import com.github.jh3nd3rs0n.jargyle.common.net.Port;
import com.github.jh3nd3rs0n.jargyle.server.RuleResult;
import com.github.jh3nd3rs0n.jargyle.server.RuleResultSpec;

public final class PortRuleResultSpec extends RuleResultSpec<Port> {

	public PortRuleResultSpec(final String n) {
		super(n, Port.class);
	}

	@Override
	public RuleResult<Port> newRuleResultOfParsableValue(final String value) {
		return super.newRuleResult(Port.newInstance(value));
	}

}
