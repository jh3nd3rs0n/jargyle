package com.github.jh3nd3rs0n.jargyle.server.internal.ruleconditionspec.impl;

import com.github.jh3nd3rs0n.jargyle.common.net.Port;
import com.github.jh3nd3rs0n.jargyle.common.net.PortRange;
import com.github.jh3nd3rs0n.jargyle.server.RuleArgSpec;
import com.github.jh3nd3rs0n.jargyle.server.RuleCondition;
import com.github.jh3nd3rs0n.jargyle.server.RuleConditionSpec;

public final class PortRangeContainsPortRuleConditionSpec 
	extends RuleConditionSpec<PortRange, Port> {

	public PortRangeContainsPortRuleConditionSpec(
			final String s, final RuleArgSpec<Port> rlArgSpec) {
		super(
				s, 
				PortRange.class, 
				new PortRangeContainsPortRuleConditionEvaluator(), 
				rlArgSpec);
	}

	@Override
	public RuleCondition<PortRange, Port> newRuleConditionOfParsableValue(
			final String value) {
		return super.newRuleCondition(PortRange.newInstance(value));
	}

}
