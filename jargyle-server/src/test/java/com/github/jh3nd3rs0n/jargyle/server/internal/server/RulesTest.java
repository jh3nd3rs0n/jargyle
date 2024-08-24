package com.github.jh3nd3rs0n.jargyle.server.internal.server;

import com.github.jh3nd3rs0n.jargyle.protocolbase.socks5.Command;
import com.github.jh3nd3rs0n.jargyle.server.*;
import org.junit.Assert;
import org.junit.Test;

public class RulesTest {

	@Test
	public void testAnyAppliesTo01() {
		Rule expectedRule = new Rule.Builder()
				.addRuleCondition(Socks5RuleConditionSpecConstants.SOCKS5_REQUEST_COMMAND.newRuleCondition(Command.BIND.toString()))
				.addRuleCondition(Socks5RuleConditionSpecConstants.SOCKS5_REQUEST_COMMAND.newRuleCondition(Command.UDP_ASSOCIATE.toString()))
				.addRuleResult(GeneralRuleResultSpecConstants.FIREWALL_ACTION.newRuleResult(FirewallAction.DENY))
				.build(); 
		Rules rules = Rules.of(
				new Rule.Builder()
				.addRuleCondition(GeneralRuleConditionSpecConstants.CLIENT_ADDRESS.newRuleConditionWithParsedValue("127.0.0.1"))
				.addRuleResult(GeneralRuleResultSpecConstants.FIREWALL_ACTION.newRuleResult(FirewallAction.ALLOW))
				.build(),
				expectedRule,
				new Rule.Builder()
				.addRuleCondition(Socks5RuleConditionSpecConstants.SOCKS5_REPLY_SERVER_BOUND_ADDRESS.newRuleConditionWithParsedValue("127.0.0.1"))
				.addRuleResult(GeneralRuleResultSpecConstants.FIREWALL_ACTION.newRuleResult(FirewallAction.DENY))
				.addRuleResult(GeneralRuleResultSpecConstants.FIREWALL_ACTION_LOG_ACTION.newRuleResult(LogAction.LOG_AS_WARNING))
				.build());
		RuleContext ruleContext = new RuleContext();
		ruleContext.putRuleArgValue(Socks5RuleArgSpecConstants.SOCKS5_REQUEST_COMMAND, Command.UDP_ASSOCIATE.toString());
		Rule actualRule = rules.firstAppliesTo(ruleContext);
		Assert.assertEquals(expectedRule, actualRule);
	}

}
