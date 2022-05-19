package com.github.jh3nd3rs0n.jargyle.server.rules.impl;

import com.github.jh3nd3rs0n.jargyle.common.net.Port;
import com.github.jh3nd3rs0n.jargyle.server.PortRange;
import com.github.jh3nd3rs0n.jargyle.server.RuleConditionEvaluator;

public final class PortRangeContainsPortRuleConditionEvaluator 
	extends RuleConditionEvaluator<PortRange, Port> {

	@Override
	public boolean evaluate(final PortRange value1, final Port value2) {
		return value1.contains(value2);
	}

}
