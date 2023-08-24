package com.github.jh3nd3rs0n.jargyle.server;

import java.util.List;
import java.util.Map;

import com.github.jh3nd3rs0n.jargyle.common.net.Port;

public final class Socks5RuleArgSpecConstants {
	
	private static final RuleArgSpecs RULE_ARG_SPECS = new RuleArgSpecs();
	
	public static final RuleArgSpec<String> SOCKS5_COMMAND = RULE_ARG_SPECS.addThenGet(new RuleArgSpec<String>(
			"socks5.command", 
			String.class));

	public static final RuleArgSpec<String> SOCKS5_DESIRED_DESTINATION_ADDRESS = RULE_ARG_SPECS.addThenGet(new RuleArgSpec<String>(
			"socks5.desiredDestinationAddress", 
			String.class));

	public static final RuleArgSpec<Port> SOCKS5_DESIRED_DESTINATION_PORT = RULE_ARG_SPECS.addThenGet(new RuleArgSpec<Port>(
			"socks5.desiredDestinationPort", 
			Port.class));
	
	public static final RuleArgSpec<String> SOCKS5_METHOD = RULE_ARG_SPECS.addThenGet(new RuleArgSpec<String>(
			"socks5.method", 
			String.class));

	public static final RuleArgSpec<String> SOCKS5_SECOND_SERVER_BOUND_ADDRESS = RULE_ARG_SPECS.addThenGet(new RuleArgSpec<String>(
			"socks5.secondServerBoundAddress", 
			String.class));
	
	public static final RuleArgSpec<Port> SOCKS5_SECOND_SERVER_BOUND_PORT = RULE_ARG_SPECS.addThenGet(new RuleArgSpec<Port>(
			"socks5.secondServerBoundPort", 
			Port.class));
	
	public static final RuleArgSpec<String> SOCKS5_SERVER_BOUND_ADDRESS = RULE_ARG_SPECS.addThenGet(new RuleArgSpec<String>(
			"socks5.serverBoundAddress", 
			String.class));
	
	public static final RuleArgSpec<Port> SOCKS5_SERVER_BOUND_PORT = RULE_ARG_SPECS.addThenGet(new RuleArgSpec<Port>(
			"socks5.serverBoundPort", 
			Port.class));
	
	public static final RuleArgSpec<String> SOCKS5_UDP_INBOUND_DESIRED_DESTINATION_ADDRESS = RULE_ARG_SPECS.addThenGet(new RuleArgSpec<String>(
			"socks5.udp.inbound.desiredDestinationAddress",
			String.class));
	
	public static final RuleArgSpec<Port> SOCKS5_UDP_INBOUND_DESIRED_DESTINATION_PORT = RULE_ARG_SPECS.addThenGet(new RuleArgSpec<Port>(
			"socks5.udp.inbound.desiredDestinationPort",
			Port.class));
	
	public static final RuleArgSpec<String> SOCKS5_UDP_INBOUND_SOURCE_ADDRESS = RULE_ARG_SPECS.addThenGet(new RuleArgSpec<String>(
			"socks5.udp.inbound.sourceAddress", 
			String.class));
	
	public static final RuleArgSpec<Port> SOCKS5_UDP_INBOUND_SOURCE_PORT = RULE_ARG_SPECS.addThenGet(new RuleArgSpec<Port>(
			"socks5.udp.inbound.sourcePort",
			Port.class));
	
	public static final RuleArgSpec<String> SOCKS5_UDP_OUTBOUND_DESIRED_DESTINATION_ADDRESS = RULE_ARG_SPECS.addThenGet(new RuleArgSpec<String>(
			"socks5.udp.outbound.desiredDestinationAddress", 
			String.class));
	
	public static final RuleArgSpec<Port> SOCKS5_UDP_OUTBOUND_DESIRED_DESTINATION_PORT = RULE_ARG_SPECS.addThenGet(new RuleArgSpec<Port>(
			"socks5.udp.outbound.desiredDestinationPort",
			Port.class));
	
	public static final RuleArgSpec<String> SOCKS5_UDP_OUTBOUND_SOURCE_ADDRESS = RULE_ARG_SPECS.addThenGet(new RuleArgSpec<String>(
			"socks5.udp.outbound.sourceAddress",
			String.class));
	
	public static final RuleArgSpec<Port> SOCKS5_UDP_OUTBOUND_SOURCE_PORT = RULE_ARG_SPECS.addThenGet(new RuleArgSpec<Port>(
			"socks5.udp.outbound.sourcePort",
			Port.class));
	
	public static final RuleArgSpec<String> SOCKS5_USER = RULE_ARG_SPECS.addThenGet(new RuleArgSpec<String>(
			"socks5.user", 
			String.class));
	
	public static List<RuleArgSpec<Object>> values() {
		return RULE_ARG_SPECS.toList();
	}
	
	public static Map<String, RuleArgSpec<Object>> valuesMap() {
		return RULE_ARG_SPECS.toMap();
	}
	
	private Socks5RuleArgSpecConstants() { }

}
