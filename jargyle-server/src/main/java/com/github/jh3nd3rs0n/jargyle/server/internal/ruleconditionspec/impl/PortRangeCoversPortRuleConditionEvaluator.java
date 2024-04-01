package com.github.jh3nd3rs0n.jargyle.server.internal.ruleconditionspec.impl;

import com.github.jh3nd3rs0n.jargyle.common.net.Port;
import com.github.jh3nd3rs0n.jargyle.common.net.PortRange;
import com.github.jh3nd3rs0n.jargyle.server.RuleConditionEvaluator;

public final class PortRangeCoversPortRuleConditionEvaluator
	extends RuleConditionEvaluator<PortRange, Port> {

	@Override
	public boolean evaluate(final PortRange conditionValue, final Port argValue) {
		return conditionValue.covers(argValue);
	}

}
