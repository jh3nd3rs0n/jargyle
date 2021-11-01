package com.github.jh3nd3rs0n.jargyle.server.socks5;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import com.github.jh3nd3rs0n.jargyle.common.net.AddressRange;
import com.github.jh3nd3rs0n.jargyle.common.net.PortRange;
import com.github.jh3nd3rs0n.jargyle.server.LogAction;
import com.github.jh3nd3rs0n.jargyle.server.RuleAction;
import com.github.jh3nd3rs0n.jargyle.transport.socks5.Command;

public class Socks5RequestRuleTest {

	@Test
	public void testNewInstances01() {
		List<Socks5RequestRule> expectedSocks5RequestRules = 
				Socks5RequestRule.newInstances(new StringBuilder()
				.append("ruleAction=ALLOW ")
				.append("sourceAddressRange=127.0.0.1 ")
				.append("command=CONNECT ")
				.append("desiredDestinationAddressRange=regex:.* ")
				.append("desiredDestinationPortRange=0-65535")
				.toString()); 
		List<Socks5RequestRule> actualSocks5RequestRules = Arrays.asList(
				new Socks5RequestRule.Builder(RuleAction.ALLOW)
				.sourceAddressRange(AddressRange.newInstance("127.0.0.1"))
				.command(Command.CONNECT)
				.desiredDestinationAddressRange(AddressRange.newInstance("regex:.*"))
				.desiredDestinationPortRange(PortRange.newInstance("0-65535"))
				.build());
		assertEquals(expectedSocks5RequestRules, actualSocks5RequestRules);
	}

	@Test
	public void testNewInstances02() {
		List<Socks5RequestRule> expectedSocks5RequestRules = 
				Socks5RequestRule.newInstances(new StringBuilder()
				.append("ruleAction=ALLOW ")
				.append("sourceAddressRange=127.0.0.1 ")
				.append("command=CONNECT ")
				.append("desiredDestinationAddressRange=regex:.* ")
				.append("desiredDestinationPortRange=0-65535 ")
				.append("ruleAction=DENY ")
				.append("command=UDP_ASSOCIATE ")
				.append("logAction=LOG_AS_INFO")				
				.toString()); 
		List<Socks5RequestRule> actualSocks5RequestRules = Arrays.asList(
				new Socks5RequestRule.Builder(RuleAction.ALLOW)
				.sourceAddressRange(AddressRange.newInstance("127.0.0.1"))
				.command(Command.CONNECT)
				.desiredDestinationAddressRange(AddressRange.newInstance("regex:.*"))
				.desiredDestinationPortRange(PortRange.newInstance("0-65535"))
				.build(),
				new Socks5RequestRule.Builder(RuleAction.DENY)
				.command(Command.UDP_ASSOCIATE)
				.logAction(LogAction.LOG_AS_INFO)
				.build());
		assertEquals(expectedSocks5RequestRules, actualSocks5RequestRules);
	}

	@Test
	public void testNewInstances03() {
		List<Socks5RequestRule> expectedSocks5RequestRules = 
				Socks5RequestRule.newInstances(new StringBuilder()
				.append("ruleAction=ALLOW ")
				.append("sourceAddressRange=::1 ")
				.append("command=BIND ")
				.append("desiredDestinationAddressRange=:: ")
				.append("sourceAddressRange=127.0.0.1 ")
				.append("command=CONNECT ")
				.append("desiredDestinationAddressRange=regex:.* ")
				.append("desiredDestinationPortRange=0-65535 ")
				.append("ruleAction=DENY ")				
				.append("command=CONNECT ")
				.append("logAction=LOG_AS_WARNING ")				
				.append("command=UDP_ASSOCIATE ")
				.append("logAction=LOG_AS_INFO")				
				.toString()); 
		List<Socks5RequestRule> actualSocks5RequestRules = Arrays.asList(
				new Socks5RequestRule.Builder(RuleAction.ALLOW)
				.sourceAddressRange(AddressRange.newInstance("127.0.0.1"))
				.command(Command.CONNECT)
				.desiredDestinationAddressRange(AddressRange.newInstance("regex:.*"))
				.desiredDestinationPortRange(PortRange.newInstance("0-65535"))
				.build(),
				new Socks5RequestRule.Builder(RuleAction.DENY)
				.command(Command.UDP_ASSOCIATE)
				.logAction(LogAction.LOG_AS_INFO)
				.build());
		assertEquals(expectedSocks5RequestRules, actualSocks5RequestRules);
	}

}
