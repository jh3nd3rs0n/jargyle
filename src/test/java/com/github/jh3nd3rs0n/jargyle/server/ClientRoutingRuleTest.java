package com.github.jh3nd3rs0n.jargyle.server;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import com.github.jh3nd3rs0n.jargyle.common.net.AddressRange;

public class ClientRoutingRuleTest {

	@Test
	public void testNewInstances01() {
		List<ClientRoutingRule> expectedClientRoutingRules = ClientRoutingRule.newInstances(
				new StringBuilder()
				.append("routingRule= ")
				.append("clientAddressRange=127.0.0.1 ")
				.append("socksServerAddressRange=0.0.0.0 ")
				.append("routeId=Alice ")
				.append("logAction=LOG_AS_INFO")
				.toString()); 
		List<ClientRoutingRule> actualClientRoutingRules = Arrays.asList(
				new ClientRoutingRule.Builder()
				.clientAddressRange(AddressRange.newInstance("127.0.0.1"))
				.socksServerAddressRange(AddressRange.newInstance("0.0.0.0"))
				.addRouteId("Alice")
				.logAction(LogAction.LOG_AS_INFO)
				.build());
		assertEquals(expectedClientRoutingRules, actualClientRoutingRules);
	}

	@Test
	public void testNewInstances02() {
		List<ClientRoutingRule> expectedClientRoutingRules = ClientRoutingRule.newInstances(
				new StringBuilder()
				.append("routingRule= ")				
				.append("routeId=Alice ")
				.append("socksServerAddressRange=::1-::5 ")
				.append("clientAddressRange=:: ")
				.append("routingRule= ")
				.append("clientAddressRange=127.0.0.1 ")
				.append("socksServerAddressRange=0.0.0.0 ")
				.append("routeId=Bob ")
				.append("logAction=LOG_AS_INFO")
				.toString()); 
		List<ClientRoutingRule> actualClientRoutingRules = Arrays.asList(
				new ClientRoutingRule.Builder()
				.clientAddressRange(AddressRange.newInstance("::"))
				.socksServerAddressRange(AddressRange.newInstance("::1-::5"))
				.addRouteId("Alice")
				.build(),
				new ClientRoutingRule.Builder()
				.clientAddressRange(AddressRange.newInstance("127.0.0.1"))
				.socksServerAddressRange(AddressRange.newInstance("0.0.0.0"))
				.addRouteId("Bob")
				.logAction(LogAction.LOG_AS_INFO)
				.build());
		assertEquals(expectedClientRoutingRules, actualClientRoutingRules);
	}

	@Test
	public void testNewInstances03() {
		List<ClientRoutingRule> expectedClientRoutingRules = ClientRoutingRule.newInstances(
				new StringBuilder()
				.append("routingRule= ")
				.append("socksServerAddressRange=bogus.com ")
				.append("clientAddressRange=bogus.com ")
				.append("socksServerAddressRange=::1-::5 ")
				.append("clientAddressRange=:: ")
				.append("routeId=Alice ")
				.append("routeId=Bob ")
				.append("routingRule= ")
				.append("clientAddressRange=regex:.*bogus\\.com$ ")
				.append("socksServerAddressRange=regex:.*bogus\\.com$ ")
				.append("clientAddressRange=127.0.0.1 ")
				.append("socksServerAddressRange=0.0.0.0 ")
				.append("routeId=Alice ")
				.append("routeId=Bob ")
				.append("routeId=Eve ")
				.append("logAction=LOG_AS_INFO")				
				.toString()); 
		List<ClientRoutingRule> actualClientRoutingRules = Arrays.asList(
				new ClientRoutingRule.Builder()
				.clientAddressRange(AddressRange.newInstance("::"))
				.socksServerAddressRange(AddressRange.newInstance("::1-::5"))
				.addRouteId("Alice")
				.addRouteId("Bob")
				.build(),
				new ClientRoutingRule.Builder()
				.clientAddressRange(AddressRange.newInstance("127.0.0.1"))
				.socksServerAddressRange(AddressRange.newInstance("0.0.0.0"))
				.addRouteId("Alice")
				.addRouteId("Bob")
				.addRouteId("Eve")
				.logAction(LogAction.LOG_AS_INFO)
				.build());
		assertEquals(expectedClientRoutingRules, actualClientRoutingRules);
	}

}
