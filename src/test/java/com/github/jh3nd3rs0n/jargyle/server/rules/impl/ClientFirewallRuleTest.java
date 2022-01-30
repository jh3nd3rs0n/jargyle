package com.github.jh3nd3rs0n.jargyle.server.rules.impl;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import com.github.jh3nd3rs0n.jargyle.server.LogAction;

public class ClientFirewallRuleTest {

	@Test
	public void testNewInstances01() {
		List<ClientFirewallRule> expectedClientFirewallRules = ClientFirewallRule.newInstances(
				new StringBuilder()
				.append("firewallRuleAction=ALLOW ")
				.append("clientAddressRange=127.0.0.1 ")
				.append("socksServerAddressRange=0.0.0.0 ")
				.append("logAction=LOG_AS_INFO")
				.toString()); 
		List<ClientFirewallRule> actualClientFirewallRules = Arrays.asList(
				new ClientFirewallRule.Builder(FirewallRuleAction.ALLOW)
				.clientAddressRange(AddressRange.newInstance("127.0.0.1"))
				.socksServerAddressRange(AddressRange.newInstance("0.0.0.0"))
				.logAction(LogAction.LOG_AS_INFO)
				.build());
		assertEquals(expectedClientFirewallRules, actualClientFirewallRules);
	}

	@Test
	public void testNewInstances02() {
		List<ClientFirewallRule> expectedClientFirewallRules = ClientFirewallRule.newInstances(
				new StringBuilder()
				.append("firewallRuleAction=DENY ")				
				.append("socksServerAddressRange=::1-::5 ")				
				.append("clientAddressRange=:: ")
				.append("firewallRuleAction=ALLOW ")
				.append("clientAddressRange=127.0.0.1 ")
				.append("socksServerAddressRange=0.0.0.0 ")
				.append("logAction=LOG_AS_INFO")
				.toString()); 
		List<ClientFirewallRule> actualClientFirewallRules = Arrays.asList(
				new ClientFirewallRule.Builder(FirewallRuleAction.DENY)
				.clientAddressRange(AddressRange.newInstance("::"))
				.socksServerAddressRange(AddressRange.newInstance("::1-::5"))
				.build(),
				new ClientFirewallRule.Builder(FirewallRuleAction.ALLOW)
				.clientAddressRange(AddressRange.newInstance("127.0.0.1"))
				.socksServerAddressRange(AddressRange.newInstance("0.0.0.0"))
				.logAction(LogAction.LOG_AS_INFO)
				.build());
		assertEquals(expectedClientFirewallRules, actualClientFirewallRules);
	}

	@Test
	public void testNewInstances03() {
		List<ClientFirewallRule> expectedClientFirewallRules = ClientFirewallRule.newInstances(
				new StringBuilder()
				.append("firewallRuleAction=DENY ")
				.append("socksServerAddressRange=bogus.com ")				
				.append("clientAddressRange=bogus.com ")
				.append("socksServerAddressRange=::1-::5 ")				
				.append("clientAddressRange=:: ")
				.append("firewallRuleAction=ALLOW ")
				.append("clientAddressRange=regex:.*bogus\\.com$ ")
				.append("socksServerAddressRange=regex:.*bogus\\.com$ ")
				.append("clientAddressRange=127.0.0.1 ")
				.append("socksServerAddressRange=0.0.0.0 ")
				.append("logAction=LOG_AS_INFO")				
				.toString()); 
		List<ClientFirewallRule> actualClientFirewallRules = Arrays.asList(
				new ClientFirewallRule.Builder(FirewallRuleAction.DENY)
				.clientAddressRange(AddressRange.newInstance("::"))
				.socksServerAddressRange(AddressRange.newInstance("::1-::5"))
				.build(),
				new ClientFirewallRule.Builder(FirewallRuleAction.ALLOW)
				.clientAddressRange(AddressRange.newInstance("127.0.0.1"))
				.socksServerAddressRange(AddressRange.newInstance("0.0.0.0"))
				.logAction(LogAction.LOG_AS_INFO)
				.build());
		assertEquals(expectedClientFirewallRules, actualClientFirewallRules);
	}

}
