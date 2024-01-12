package com.github.jh3nd3rs0n.jargyle.server;

import com.github.jh3nd3rs0n.jargyle.protocolbase.socks5.Command;
import org.junit.Assert;
import org.junit.Test;

public class RuleTest {

	@Test
	public void testNewInstanceFromString01() {
		Rule expectedRule = Rule.newInstanceFrom(new StringBuilder()
				.append("clientAddress=127.0.0.1,")
				.append("firewallAction=ALLOW")
				.toString());
		Rule actualRule = new Rule.Builder()
				.addRuleCondition(GeneralRuleConditionSpecConstants.CLIENT_ADDRESS.newRuleConditionWithParsedValue("127.0.0.1"))
				.addRuleResult(GeneralRuleResultSpecConstants.FIREWALL_ACTION.newRuleResult(FirewallAction.ALLOW))
				.build();
		Assert.assertEquals(expectedRule, actualRule);
	}

	@Test
	public void testNewInstanceFromString02() {
		Rule expectedRule = Rule.newInstanceFrom(new StringBuilder()
				.append("socks5.command=BIND,")
				.append("socks5.command=UDP_ASSOCIATE,")
				.append("firewallAction=DENY")
				.toString());
		Rule actualRule = new Rule.Builder()
				.addRuleCondition(Socks5RuleConditionSpecConstants.SOCKS5_COMMAND.newRuleCondition(Command.BIND.toString()))
				.addRuleCondition(Socks5RuleConditionSpecConstants.SOCKS5_COMMAND.newRuleCondition(Command.UDP_ASSOCIATE.toString()))
				.addRuleResult(GeneralRuleResultSpecConstants.FIREWALL_ACTION.newRuleResult(FirewallAction.DENY))
				.build();
		Assert.assertEquals(expectedRule, actualRule);
	}

	@Test
	public void testNewInstanceFromString03() {
		Rule expectedRule = Rule.newInstanceFrom(new StringBuilder()
				.append("socks5.serverBoundAddress=127.0.0.1,")
				.append("firewallAction=DENY,")
				.append("firewallActionLogAction=LOG_AS_WARNING")
				.toString());
		Rule actualRule = new Rule.Builder()
				.addRuleCondition(Socks5RuleConditionSpecConstants.SOCKS5_SERVER_BOUND_ADDRESS.newRuleConditionWithParsedValue("127.0.0.1"))
				.addRuleResult(GeneralRuleResultSpecConstants.FIREWALL_ACTION.newRuleResult(FirewallAction.DENY))
				.addRuleResult(GeneralRuleResultSpecConstants.FIREWALL_ACTION_LOG_ACTION.newRuleResult(LogAction.LOG_AS_WARNING))
				.build();
		Assert.assertEquals(expectedRule, actualRule);
	}

}
