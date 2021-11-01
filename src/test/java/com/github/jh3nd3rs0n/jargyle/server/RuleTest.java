package com.github.jh3nd3rs0n.jargyle.server;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import com.github.jh3nd3rs0n.jargyle.common.net.AddressRange;

public class RuleTest {

	@Test
	public void testNewInstances01() {
		List<Rule> expectedRules = Rule.newInstances(new StringBuilder()
				.append("ruleAction=ALLOW ")
				.append("sourceAddressRange=127.0.0.1 ")
				.append("destinationAddressRange=0.0.0.0 ")
				.append("logAction=LOG_AS_INFO")
				.toString()); 
		List<Rule> actualRules = Arrays.asList(
				new Rule.Builder(RuleAction.ALLOW)
				.sourceAddressRange(AddressRange.newInstance("127.0.0.1"))
				.destinationAddressRange(AddressRange.newInstance("0.0.0.0"))
				.logAction(LogAction.LOG_AS_INFO)
				.build());
		assertEquals(expectedRules, actualRules);
	}

	@Test
	public void testNewInstances02() {
		List<Rule> expectedRules = Rule.newInstances(new StringBuilder()
				.append("ruleAction=DENY ")				
				.append("destinationAddressRange=::1-::5 ")				
				.append("sourceAddressRange=:: ")
				.append("ruleAction=ALLOW ")
				.append("sourceAddressRange=127.0.0.1 ")
				.append("destinationAddressRange=0.0.0.0 ")
				.append("logAction=LOG_AS_INFO")
				.toString()); 
		List<Rule> actualRules = Arrays.asList(
				new Rule.Builder(RuleAction.DENY)
				.sourceAddressRange(AddressRange.newInstance("::"))
				.destinationAddressRange(AddressRange.newInstance("::1-::5"))
				.build(),
				new Rule.Builder(RuleAction.ALLOW)
				.sourceAddressRange(AddressRange.newInstance("127.0.0.1"))
				.destinationAddressRange(AddressRange.newInstance("0.0.0.0"))
				.logAction(LogAction.LOG_AS_INFO)
				.build());
		assertEquals(expectedRules, actualRules);
	}

	@Test
	public void testNewInstances03() {
		List<Rule> expectedRules = Rule.newInstances(new StringBuilder()
				.append("ruleAction=DENY ")
				.append("destinationAddressRange=bogus.com ")				
				.append("sourceAddressRange=bogus.com ")
				.append("destinationAddressRange=::1-::5 ")				
				.append("sourceAddressRange=:: ")
				.append("ruleAction=ALLOW ")
				.append("sourceAddressRange=regex:.*bogus\\.com$ ")
				.append("destinationAddressRange=regex:.*bogus\\.com$ ")
				.append("sourceAddressRange=127.0.0.1 ")
				.append("destinationAddressRange=0.0.0.0 ")
				.append("logAction=LOG_AS_INFO")				
				.toString()); 
		List<Rule> actualRules = Arrays.asList(
				new Rule.Builder(RuleAction.DENY)
				.sourceAddressRange(AddressRange.newInstance("::"))
				.destinationAddressRange(AddressRange.newInstance("::1-::5"))
				.build(),
				new Rule.Builder(RuleAction.ALLOW)
				.sourceAddressRange(AddressRange.newInstance("127.0.0.1"))
				.destinationAddressRange(AddressRange.newInstance("0.0.0.0"))
				.logAction(LogAction.LOG_AS_INFO)
				.build());
		assertEquals(expectedRules, actualRules);
	}

}
