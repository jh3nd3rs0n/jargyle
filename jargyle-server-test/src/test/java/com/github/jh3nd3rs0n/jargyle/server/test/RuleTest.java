package com.github.jh3nd3rs0n.jargyle.server.test;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.github.jh3nd3rs0n.jargyle.protocolbase.socks5.Command;
import com.github.jh3nd3rs0n.jargyle.server.FirewallAction;
import com.github.jh3nd3rs0n.jargyle.server.GeneralRuleConditionSpecConstants;
import com.github.jh3nd3rs0n.jargyle.server.GeneralRuleResultSpecConstants;
import com.github.jh3nd3rs0n.jargyle.server.LogAction;
import com.github.jh3nd3rs0n.jargyle.server.Rule;
import com.github.jh3nd3rs0n.jargyle.server.Socks5RuleConditionSpecConstants;

public class RuleTest {

	@Test
	public void testNewInstanceString01() {
		Rule expectedRule = Rule.newInstance(new StringBuilder()
				.append("clientAddress=127.0.0.1,")
				.append("firewallAction=ALLOW")
				.toString());
		Rule actualRule = new Rule.Builder()
				.addRuleCondition(GeneralRuleConditionSpecConstants.CLIENT_ADDRESS.newRuleConditionWithParsableValue("127.0.0.1"))
				.addRuleResult(GeneralRuleResultSpecConstants.FIREWALL_ACTION.newRuleResult(FirewallAction.ALLOW))
				.build();
		assertEquals(expectedRule, actualRule);
	}

	@Test
	public void testNewInstanceString02() {
		Rule expectedRule = Rule.newInstance(new StringBuilder()
				.append("socks5.command=BIND,")
				.append("socks5.command=UDP_ASSOCIATE,")
				.append("firewallAction=DENY")
				.toString());
		Rule actualRule = new Rule.Builder()
				.addRuleCondition(Socks5RuleConditionSpecConstants.SOCKS5_COMMAND.newRuleCondition(Command.BIND.toString()))
				.addRuleCondition(Socks5RuleConditionSpecConstants.SOCKS5_COMMAND.newRuleCondition(Command.UDP_ASSOCIATE.toString()))
				.addRuleResult(GeneralRuleResultSpecConstants.FIREWALL_ACTION.newRuleResult(FirewallAction.DENY))
				.build();
		assertEquals(expectedRule, actualRule);		
	}

	@Test
	public void testNewInstanceString03() {
		Rule expectedRule = Rule.newInstance(new StringBuilder()
				.append("socks5.serverBoundAddress=127.0.0.1,")
				.append("firewallAction=DENY,")
				.append("firewallActionLogAction=LOG_AS_WARNING")
				.toString());
		Rule actualRule = new Rule.Builder()
				.addRuleCondition(Socks5RuleConditionSpecConstants.SOCKS5_SERVER_BOUND_ADDRESS.newRuleConditionWithParsableValue("127.0.0.1"))
				.addRuleResult(GeneralRuleResultSpecConstants.FIREWALL_ACTION.newRuleResult(FirewallAction.DENY))
				.addRuleResult(GeneralRuleResultSpecConstants.FIREWALL_ACTION_LOG_ACTION.newRuleResult(LogAction.LOG_AS_WARNING))
				.build();
		assertEquals(expectedRule, actualRule);	
	}

}
