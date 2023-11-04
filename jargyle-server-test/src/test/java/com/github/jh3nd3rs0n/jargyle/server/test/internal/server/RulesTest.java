package com.github.jh3nd3rs0n.jargyle.server.test.internal.server;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.github.jh3nd3rs0n.jargyle.protocolbase.socks5.Command;
import com.github.jh3nd3rs0n.jargyle.server.FirewallAction;
import com.github.jh3nd3rs0n.jargyle.server.GeneralRuleConditionSpecConstants;
import com.github.jh3nd3rs0n.jargyle.server.GeneralRuleResultSpecConstants;
import com.github.jh3nd3rs0n.jargyle.server.LogAction;
import com.github.jh3nd3rs0n.jargyle.server.Rule;
import com.github.jh3nd3rs0n.jargyle.server.RuleContext;
import com.github.jh3nd3rs0n.jargyle.server.Socks5RuleArgSpecConstants;
import com.github.jh3nd3rs0n.jargyle.server.Socks5RuleConditionSpecConstants;
import com.github.jh3nd3rs0n.jargyle.server.internal.server.Rules;

public class RulesTest {

	@Test
	public void testAnyAppliesTo01() {
		Rule expectedRule = new Rule.Builder()
				.addRuleCondition(Socks5RuleConditionSpecConstants.SOCKS5_COMMAND.newRuleCondition(Command.BIND.toString()))
				.addRuleCondition(Socks5RuleConditionSpecConstants.SOCKS5_COMMAND.newRuleCondition(Command.UDP_ASSOCIATE.toString()))
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
		ruleContext.putRuleArgValue(Socks5RuleArgSpecConstants.SOCKS5_COMMAND, Command.UDP_ASSOCIATE.toString());
		Rule actualRule = rules.firstAppliesTo(ruleContext);
		assertEquals(expectedRule, actualRule);
	}

}
