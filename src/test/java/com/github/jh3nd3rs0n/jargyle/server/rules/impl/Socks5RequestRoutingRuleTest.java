package com.github.jh3nd3rs0n.jargyle.server.rules.impl;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import com.github.jh3nd3rs0n.jargyle.server.LogAction;
import com.github.jh3nd3rs0n.jargyle.transport.socks5.Command;
import com.github.jh3nd3rs0n.jargyle.transport.socks5.Method;

public class Socks5RequestRoutingRuleTest {
	
	@Test
	public void testNewInstances01() {
		List<Socks5RequestRoutingRule> expectedSocks5RequestRoutingRules = Socks5RequestRoutingRule.newInstances(
				new StringBuilder()
				.append("routingRule= ")
				.append("clientAddressRange=127.0.0.1 ")
				.append("socksServerAddressRange=0.0.0.0 ")
				.append("command=CONNECT ")
				.append("desiredDestinationAddressRange=regex:.* ")
				.append("desiredDestinationPortRange=0-65535 ")
				.append("routeId=Alice")
				.toString()); 
		List<Socks5RequestRoutingRule> actualSocks5RequestRoutingRules = Arrays.asList(
				new Socks5RequestRoutingRule.Builder()
				.clientAddressRange(AddressRange.newInstance("127.0.0.1"))
				.socksServerAddressRange(AddressRange.newInstance("0.0.0.0"))
				.command(Command.CONNECT)
				.desiredDestinationAddressRange(AddressRange.newInstance("regex:.*"))
				.desiredDestinationPortRange(PortRange.newInstance("0-65535"))
				.addRouteId("Alice")
				.build());
		assertEquals(expectedSocks5RequestRoutingRules, actualSocks5RequestRoutingRules);
	}

	@Test
	public void testNewInstances02() {
		List<Socks5RequestRoutingRule> expectedSocks5RequestRoutingRules = Socks5RequestRoutingRule.newInstances(
				new StringBuilder()
				.append("routingRule= ")
				.append("clientAddressRange=127.0.0.1 ")
				.append("socksServerAddressRange=0.0.0.0 ")
				.append("command=CONNECT ")
				.append("desiredDestinationAddressRange=regex:.* ")
				.append("desiredDestinationPortRange=0-65535 ")
				.append("routeId=Alice ")
				.append("routingRule= ")
				.append("command=UDP_ASSOCIATE ")
				.append("routeId=Bob ")
				.append("logAction=LOG_AS_INFO")				
				.toString()); 
		List<Socks5RequestRoutingRule> actualSocks5RequestRoutingRules = Arrays.asList(
				new Socks5RequestRoutingRule.Builder()
				.clientAddressRange(AddressRange.newInstance("127.0.0.1"))
				.socksServerAddressRange(AddressRange.newInstance("0.0.0.0"))
				.command(Command.CONNECT)
				.desiredDestinationAddressRange(AddressRange.newInstance("regex:.*"))
				.desiredDestinationPortRange(PortRange.newInstance("0-65535"))
				.addRouteId("Alice")
				.build(),
				new Socks5RequestRoutingRule.Builder()
				.command(Command.UDP_ASSOCIATE)
				.addRouteId("Bob")
				.logAction(LogAction.LOG_AS_INFO)
				.build());
		assertEquals(expectedSocks5RequestRoutingRules, actualSocks5RequestRoutingRules);
	}

	@Test
	public void testNewInstances03() {
		List<Socks5RequestRoutingRule> expectedSocks5RequestRoutingRules = Socks5RequestRoutingRule.newInstances(
				new StringBuilder()
				.append("routingRule= ")
				.append("socksServerAddressRange=::1-::5 ")				
				.append("clientAddressRange=::1 ")
				.append("command=BIND ")
				.append("desiredDestinationAddressRange=:: ")
				.append("clientAddressRange=127.0.0.1 ")
				.append("socksServerAddressRange=0.0.0.0 ")
				.append("command=CONNECT ")
				.append("desiredDestinationAddressRange=regex:.* ")
				.append("desiredDestinationPortRange=0-65535 ")
				.append("routeId=Alice ")
				.append("routeId=Bob ")
				.append("routingRule= ")				
				.append("command=CONNECT ")
				.append("logAction=LOG_AS_WARNING ")				
				.append("command=UDP_ASSOCIATE ")
				.append("routeId=Bob ")
				.append("routeId=Alice ")
				.append("logAction=LOG_AS_INFO")				
				.toString()); 
		List<Socks5RequestRoutingRule> actualSocks5RequestRoutingRules = Arrays.asList(
				new Socks5RequestRoutingRule.Builder()
				.clientAddressRange(AddressRange.newInstance("127.0.0.1"))
				.socksServerAddressRange(AddressRange.newInstance("0.0.0.0"))
				.command(Command.CONNECT)
				.desiredDestinationAddressRange(AddressRange.newInstance("regex:.*"))
				.desiredDestinationPortRange(PortRange.newInstance("0-65535"))
				.addRouteId("Alice")
				.addRouteId("Bob")
				.build(),
				new Socks5RequestRoutingRule.Builder()
				.command(Command.UDP_ASSOCIATE)
				.addRouteId("Bob")
				.addRouteId("Alice")
				.logAction(LogAction.LOG_AS_INFO)
				.build());
		assertEquals(expectedSocks5RequestRoutingRules, actualSocks5RequestRoutingRules);
	}

	@Test
	public void testNewInstances04() {
		List<Socks5RequestRoutingRule> expectedSocks5RequestRoutingRules = Socks5RequestRoutingRule.newInstances(
				new StringBuilder()
				.append("routingRule= ")
				.append("clientAddressRange=127.0.0.1 ")
				.append("command=CONNECT ")
				.append("desiredDestinationAddressRange=regex:.* ")
				.append("desiredDestinationPortRange=0-65535 ")
				.append("routeId=Alice ")
				.append("routeId=Bob ")
				.append("routingRule= ")
				.append("command=UDP_ASSOCIATE ")
				.append("routeId=Bob ")
				.append("routeId=Alice ")
				.append("logAction=LOG_AS_INFO ")
				.append("routingRule= ")
				.append("command=BIND ")
				.append("method=USERNAME_PASSWORD ")
				.append("user=Aladdin ")
				.append("routeId=Eve ")
				.append("logAction=LOG_AS_WARNING")
				.toString()); 
		List<Socks5RequestRoutingRule> actualSocks5RequestRoutingRules = Arrays.asList(
				new Socks5RequestRoutingRule.Builder()
				.clientAddressRange(AddressRange.newInstance("127.0.0.1"))
				.command(Command.CONNECT)
				.desiredDestinationAddressRange(AddressRange.newInstance("regex:.*"))
				.desiredDestinationPortRange(PortRange.newInstance("0-65535"))
				.addRouteId("Alice")
				.addRouteId("Bob")
				.build(),
				new Socks5RequestRoutingRule.Builder()
				.command(Command.UDP_ASSOCIATE)
				.addRouteId("Bob")
				.addRouteId("Alice")
				.logAction(LogAction.LOG_AS_INFO)
				.build(),
				new Socks5RequestRoutingRule.Builder()
				.command(Command.BIND)
				.method(Method.USERNAME_PASSWORD)
				.user("Aladdin")
				.addRouteId("Eve")
				.logAction(LogAction.LOG_AS_WARNING)
				.build());
		assertEquals(expectedSocks5RequestRoutingRules, actualSocks5RequestRoutingRules);
	}

}
