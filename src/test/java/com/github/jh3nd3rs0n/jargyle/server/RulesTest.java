package com.github.jh3nd3rs0n.jargyle.server;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.github.jh3nd3rs0n.jargyle.transport.socks5.Command;

public class RulesTest {

	@Test
	public void testAnyAppliesTo01() {
		Rule expectedRule = new Rule.Builder()
				.addRuleCondition(Socks5RuleConditionSpecConstants.SOCKS5_COMMAND.newRuleCondition(Command.BIND))
				.addRuleCondition(Socks5RuleConditionSpecConstants.SOCKS5_COMMAND.newRuleCondition(Command.UDP_ASSOCIATE))
				.addRuleResult(GeneralRuleResultSpecConstants.FIREWALL_ACTION.newRuleResult(FirewallAction.DENY))
				.build(); 
		Rules rules = Rules.newInstance(
				new Rule.Builder()
				.addRuleCondition(GeneralRuleConditionSpecConstants.CLIENT_ADDRESS.newRuleConditionOfParsableValue("127.0.0.1"))
				.addRuleResult(GeneralRuleResultSpecConstants.FIREWALL_ACTION.newRuleResult(FirewallAction.ALLOW))
				.build(),
				expectedRule,
				new Rule.Builder()
				.addRuleCondition(Socks5RuleConditionSpecConstants.SOCKS5_SERVER_BOUND_ADDRESS.newRuleConditionOfParsableValue("127.0.0.1"))
				.addRuleResult(GeneralRuleResultSpecConstants.FIREWALL_ACTION.newRuleResult(FirewallAction.DENY))
				.addRuleResult(GeneralRuleResultSpecConstants.FIREWALL_ACTION_LOG_ACTION.newRuleResult(LogAction.LOG_AS_WARNING))
				.build());
		RuleContext ruleContext = new RuleContext();
		ruleContext.putRuleArgValue(Socks5RuleArgSpecConstants.SOCKS5_COMMAND, Command.UDP_ASSOCIATE);
		Rule actualRule = rules.firstAppliesTo(ruleContext);
		assertEquals(expectedRule, actualRule);
	}

}
