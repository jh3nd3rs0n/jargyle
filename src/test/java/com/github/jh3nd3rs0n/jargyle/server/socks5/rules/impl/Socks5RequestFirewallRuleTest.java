package com.github.jh3nd3rs0n.jargyle.server.socks5.rules.impl;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import com.github.jh3nd3rs0n.jargyle.server.LogAction;
import com.github.jh3nd3rs0n.jargyle.server.rules.impl.AddressRange;
import com.github.jh3nd3rs0n.jargyle.server.rules.impl.FirewallRuleAction;
import com.github.jh3nd3rs0n.jargyle.server.rules.impl.PortRange;
import com.github.jh3nd3rs0n.jargyle.transport.socks5.Command;
import com.github.jh3nd3rs0n.jargyle.transport.socks5.Method;

public class Socks5RequestFirewallRuleTest {

	@Test
	public void testNewInstances01() {
		List<Socks5RequestFirewallRule> expectedSocks5RequestFirewallRules = 
				Socks5RequestFirewallRule.newInstances(new StringBuilder()
				.append("firewallRuleAction=ALLOW ")
				.append("clientAddressRange=127.0.0.1 ")
				.append("socksServerAddressRange=0.0.0.0 ")
				.append("command=CONNECT ")
				.append("desiredDestinationAddressRange=regex:.* ")
				.append("desiredDestinationPortRange=0-65535")
				.toString()); 
		List<Socks5RequestFirewallRule> actualSocks5RequestFirewallRules = Arrays.asList(
				new Socks5RequestFirewallRule.Builder(FirewallRuleAction.ALLOW)
				.clientAddressRange(AddressRange.newInstance("127.0.0.1"))
				.socksServerAddressRange(AddressRange.newInstance("0.0.0.0"))
				.command(Command.CONNECT)
				.desiredDestinationAddressRange(AddressRange.newInstance("regex:.*"))
				.desiredDestinationPortRange(PortRange.newInstance("0-65535"))
				.build());
		assertEquals(expectedSocks5RequestFirewallRules, actualSocks5RequestFirewallRules);
	}

	@Test
	public void testNewInstances02() {
		List<Socks5RequestFirewallRule> expectedSocks5RequestFirewallRules = 
				Socks5RequestFirewallRule.newInstances(new StringBuilder()
				.append("firewallRuleAction=ALLOW ")
				.append("clientAddressRange=127.0.0.1 ")
				.append("socksServerAddressRange=0.0.0.0 ")
				.append("command=CONNECT ")
				.append("desiredDestinationAddressRange=regex:.* ")
				.append("desiredDestinationPortRange=0-65535 ")
				.append("firewallRuleAction=DENY ")
				.append("command=UDP_ASSOCIATE ")
				.append("logAction=LOG_AS_INFO")				
				.toString()); 
		List<Socks5RequestFirewallRule> actualSocks5RequestFirewallRules = Arrays.asList(
				new Socks5RequestFirewallRule.Builder(FirewallRuleAction.ALLOW)
				.clientAddressRange(AddressRange.newInstance("127.0.0.1"))
				.socksServerAddressRange(AddressRange.newInstance("0.0.0.0"))
				.command(Command.CONNECT)
				.desiredDestinationAddressRange(AddressRange.newInstance("regex:.*"))
				.desiredDestinationPortRange(PortRange.newInstance("0-65535"))
				.build(),
				new Socks5RequestFirewallRule.Builder(FirewallRuleAction.DENY)
				.command(Command.UDP_ASSOCIATE)
				.logAction(LogAction.LOG_AS_INFO)
				.build());
		assertEquals(expectedSocks5RequestFirewallRules, actualSocks5RequestFirewallRules);
	}

	@Test
	public void testNewInstances03() {
		List<Socks5RequestFirewallRule> expectedSocks5RequestFirewallRules = 
				Socks5RequestFirewallRule.newInstances(new StringBuilder()
				.append("firewallRuleAction=ALLOW ")
				.append("clientAddressRange=::1 ")
				.append("socksServerAddressRange=::1-::5 ")
				.append("command=BIND ")
				.append("desiredDestinationAddressRange=:: ")
				.append("clientAddressRange=127.0.0.1 ")
				.append("socksServerAddressRange=0.0.0.0 ")
				.append("command=CONNECT ")
				.append("desiredDestinationAddressRange=regex:.* ")
				.append("desiredDestinationPortRange=0-65535 ")
				.append("firewallRuleAction=DENY ")				
				.append("command=CONNECT ")
				.append("logAction=LOG_AS_WARNING ")				
				.append("command=UDP_ASSOCIATE ")
				.append("logAction=LOG_AS_INFO")				
				.toString()); 
		List<Socks5RequestFirewallRule> actualSocks5RequestFirewallRules = Arrays.asList(
				new Socks5RequestFirewallRule.Builder(FirewallRuleAction.ALLOW)
				.clientAddressRange(AddressRange.newInstance("127.0.0.1"))
				.socksServerAddressRange(AddressRange.newInstance("0.0.0.0"))
				.command(Command.CONNECT)
				.desiredDestinationAddressRange(AddressRange.newInstance("regex:.*"))
				.desiredDestinationPortRange(PortRange.newInstance("0-65535"))
				.build(),
				new Socks5RequestFirewallRule.Builder(FirewallRuleAction.DENY)
				.command(Command.UDP_ASSOCIATE)
				.logAction(LogAction.LOG_AS_INFO)
				.build());
		assertEquals(expectedSocks5RequestFirewallRules, actualSocks5RequestFirewallRules);
	}

	@Test
	public void testNewInstances04() {
		List<Socks5RequestFirewallRule> expectedSocks5RequestFirewallRules = 
				Socks5RequestFirewallRule.newInstances(new StringBuilder()
				.append("firewallRuleAction=ALLOW ")
				.append("clientAddressRange=127.0.0.1 ")
				.append("socksServerAddressRange=0.0.0.0 ")
				.append("command=CONNECT ")
				.append("desiredDestinationAddressRange=regex:.* ")
				.append("desiredDestinationPortRange=0-65535 ")
				.append("firewallRuleAction=DENY ")
				.append("command=UDP_ASSOCIATE ")
				.append("logAction=LOG_AS_INFO ")
				.append("firewallRuleAction=DENY ")
				.append("command=BIND ")
				.append("method=USERNAME_PASSWORD ")
				.append("user=Aladdin ")
				.append("logAction=LOG_AS_WARNING")
				.toString()); 
		List<Socks5RequestFirewallRule> actualSocks5RequestFirewallRules = Arrays.asList(
				new Socks5RequestFirewallRule.Builder(FirewallRuleAction.ALLOW)
				.clientAddressRange(AddressRange.newInstance("127.0.0.1"))
				.socksServerAddressRange(AddressRange.newInstance("0.0.0.0"))
				.command(Command.CONNECT)
				.desiredDestinationAddressRange(AddressRange.newInstance("regex:.*"))
				.desiredDestinationPortRange(PortRange.newInstance("0-65535"))
				.build(),
				new Socks5RequestFirewallRule.Builder(FirewallRuleAction.DENY)
				.command(Command.UDP_ASSOCIATE)
				.logAction(LogAction.LOG_AS_INFO)
				.build(),
				new Socks5RequestFirewallRule.Builder(FirewallRuleAction.DENY)
				.command(Command.BIND)
				.method(Method.USERNAME_PASSWORD)
				.user("Aladdin")
				.logAction(LogAction.LOG_AS_WARNING)
				.build());
		assertEquals(expectedSocks5RequestFirewallRules, actualSocks5RequestFirewallRules);
	}

}
