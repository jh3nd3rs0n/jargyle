package com.github.jh3nd3rs0n.jargyle.server.rules.impl;

import com.github.jh3nd3rs0n.jargyle.server.RuleArgSpec;
import com.github.jh3nd3rs0n.jargyle.server.RuleCondition;
import com.github.jh3nd3rs0n.jargyle.server.RuleConditionSpec;
import com.github.jh3nd3rs0n.jargyle.transport.socks5.Command;

public final class CommandEqualsCommandRuleConditionSpec 
	extends RuleConditionSpec<Command, Command> {

	public CommandEqualsCommandRuleConditionSpec(
			final String s, final RuleArgSpec<Command> rlArgSpec) {
		super(
				s, 
				Command.class, 
				new ValueEqualsValueRuleConditionEvaluator<Command>(), 
				rlArgSpec);
	}

	@Override
	public RuleCondition<Command, Command> newRuleConditionOfParsableValue(
			final String value) {
		return super.newRuleCondition(Command.valueOfString(value));
	}

}
