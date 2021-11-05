package com.github.jh3nd3rs0n.jargyle.server;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import com.github.jh3nd3rs0n.jargyle.common.net.AddressRange;

public class ClientRuleTest {

	@Test
	public void testNewInstances01() {
		List<ClientRule> expectedClientRules = ClientRule.newInstances(
				new StringBuilder()
				.append("ruleAction=ALLOW ")
				.append("clientAddressRange=127.0.0.1 ")
				.append("socksServerAddressRange=0.0.0.0 ")
				.append("logAction=LOG_AS_INFO")
				.toString()); 
		List<ClientRule> actualClientRules = Arrays.asList(
				new ClientRule.Builder(RuleAction.ALLOW)
				.clientAddressRange(AddressRange.newInstance("127.0.0.1"))
				.socksServerAddressRange(AddressRange.newInstance("0.0.0.0"))
				.logAction(LogAction.LOG_AS_INFO)
				.build());
		assertEquals(expectedClientRules, actualClientRules);
	}

	@Test
	public void testNewInstances02() {
		List<ClientRule> expectedClientRules = ClientRule.newInstances(
				new StringBuilder()
				.append("ruleAction=DENY ")				
				.append("socksServerAddressRange=::1-::5 ")				
				.append("clientAddressRange=:: ")
				.append("ruleAction=ALLOW ")
				.append("clientAddressRange=127.0.0.1 ")
				.append("socksServerAddressRange=0.0.0.0 ")
				.append("logAction=LOG_AS_INFO")
				.toString()); 
		List<ClientRule> actualClientRules = Arrays.asList(
				new ClientRule.Builder(RuleAction.DENY)
				.clientAddressRange(AddressRange.newInstance("::"))
				.socksServerAddressRange(AddressRange.newInstance("::1-::5"))
				.build(),
				new ClientRule.Builder(RuleAction.ALLOW)
				.clientAddressRange(AddressRange.newInstance("127.0.0.1"))
				.socksServerAddressRange(AddressRange.newInstance("0.0.0.0"))
				.logAction(LogAction.LOG_AS_INFO)
				.build());
		assertEquals(expectedClientRules, actualClientRules);
	}

	@Test
	public void testNewInstances03() {
		List<ClientRule> expectedClientRules = ClientRule.newInstances(
				new StringBuilder()
				.append("ruleAction=DENY ")
				.append("socksServerAddressRange=bogus.com ")				
				.append("clientAddressRange=bogus.com ")
				.append("socksServerAddressRange=::1-::5 ")				
				.append("clientAddressRange=:: ")
				.append("ruleAction=ALLOW ")
				.append("clientAddressRange=regex:.*bogus\\.com$ ")
				.append("socksServerAddressRange=regex:.*bogus\\.com$ ")
				.append("clientAddressRange=127.0.0.1 ")
				.append("socksServerAddressRange=0.0.0.0 ")
				.append("logAction=LOG_AS_INFO")				
				.toString()); 
		List<ClientRule> actualClientRules = Arrays.asList(
				new ClientRule.Builder(RuleAction.DENY)
				.clientAddressRange(AddressRange.newInstance("::"))
				.socksServerAddressRange(AddressRange.newInstance("::1-::5"))
				.build(),
				new ClientRule.Builder(RuleAction.ALLOW)
				.clientAddressRange(AddressRange.newInstance("127.0.0.1"))
				.socksServerAddressRange(AddressRange.newInstance("0.0.0.0"))
				.logAction(LogAction.LOG_AS_INFO)
				.build());
		assertEquals(expectedClientRules, actualClientRules);
	}

}
