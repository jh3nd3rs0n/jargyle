package com.github.jh3nd3rs0n.jargyle.server.internal.ruleconditionspec.impl;

import com.github.jh3nd3rs0n.jargyle.common.net.Port;
import com.github.jh3nd3rs0n.jargyle.common.net.PortRange;
import com.github.jh3nd3rs0n.jargyle.server.RuleArgSpec;
import com.github.jh3nd3rs0n.jargyle.server.RuleConditionSpec;

public final class PortRangeCoversPortRuleConditionSpec
	extends RuleConditionSpec<PortRange, Port> {

	public PortRangeCoversPortRuleConditionSpec(
			final String n, final RuleArgSpec<Port> rlArgSpec) {
		super(
				n, 
				PortRange.class, 
				new PortRangeCoversPortRuleConditionEvaluator(),
				rlArgSpec);
	}

	@Override
	protected PortRange parse(final String value) {
		return PortRange.newInstanceFrom(value);
	}

}
