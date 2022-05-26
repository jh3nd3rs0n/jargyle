package com.github.jh3nd3rs0n.jargyle.server;

import java.util.List;
import java.util.Map;

import com.github.jh3nd3rs0n.jargyle.common.net.Port;
import com.github.jh3nd3rs0n.jargyle.internal.help.HelpText;
import com.github.jh3nd3rs0n.jargyle.server.internal.ruleconditionspec.impl.AddressRangeContainsAddressRuleConditionSpec;
import com.github.jh3nd3rs0n.jargyle.server.internal.ruleconditionspec.impl.CommandEqualsCommandRuleConditionSpec;
import com.github.jh3nd3rs0n.jargyle.server.internal.ruleconditionspec.impl.MethodEqualsMethodRuleConditionSpec;
import com.github.jh3nd3rs0n.jargyle.server.internal.ruleconditionspec.impl.PortRangeContainsPortRuleConditionSpec;
import com.github.jh3nd3rs0n.jargyle.server.internal.ruleconditionspec.impl.StringEqualsStringRuleConditionSpec;
import com.github.jh3nd3rs0n.jargyle.transport.socks5.Command;
import com.github.jh3nd3rs0n.jargyle.transport.socks5.Method;

public final class Socks5RuleConditionSpecConstants {
	
	private static final RuleConditionSpecs RULE_CONDITION_SPECS = new RuleConditionSpecs();
	
	@HelpText(
			doc = "Specifies the SOCKS5 command",
			usage = "socks5.command=SOCKS5_COMMAND"
	)	
	public static final RuleConditionSpec<Command, Command> SOCKS5_COMMAND = RULE_CONDITION_SPECS.addThenGet(new CommandEqualsCommandRuleConditionSpec(
			"socks5.command",
			Socks5RuleArgSpecConstants.SOCKS5_COMMAND));

	@HelpText(
			doc = "Specifies the desired destination address",
			usage = "socks5.desiredDestinationAddress=ADDRESS|ADDRESS1-ADDRESS2|regex:REGULAR_EXPRESSION"
	)	
	public static final RuleConditionSpec<AddressRange, String> SOCKS5_DESIRED_DESTINATION_ADDRESS = RULE_CONDITION_SPECS.addThenGet(new AddressRangeContainsAddressRuleConditionSpec(
			"socks5.desiredDestinationAddress",
			Socks5RuleArgSpecConstants.SOCKS5_DESIRED_DESTINATION_ADDRESS));
	
	@HelpText(
			doc = "Specifies the desired destination port",
			usage = "socks5.desiredDestinationPort=PORT|PORT1-PORT2"
	)	
	public static final RuleConditionSpec<PortRange, Port> SOCKS5_DESIRED_DESTINATION_PORT = RULE_CONDITION_SPECS.addThenGet(new PortRangeContainsPortRuleConditionSpec(
			"socks5.desiredDestinationPort",
			Socks5RuleArgSpecConstants.SOCKS5_DESIRED_DESTINATION_PORT));
	
	@HelpText(
			doc = "Specifies the negotiated SOCKS5 method",
			usage = "socks5.method=SOCKS5_METHOD"
	)
	public static final RuleConditionSpec<Method, Method> SOCKS5_METHOD = RULE_CONDITION_SPECS.addThenGet(new MethodEqualsMethodRuleConditionSpec(
			"socks5.method",
			Socks5RuleArgSpecConstants.SOCKS5_METHOD));
	
	@HelpText(
			doc = "Specifies the second server bound address",
			usage = "socks5.secondServerBoundAddress=ADDRESS|ADDRESS1-ADDRESS2|regex:REGULAR_EXPRESSION"
	)	
	public static final RuleConditionSpec<AddressRange, String> SOCKS5_SECOND_SERVER_BOUND_ADDRESS = RULE_CONDITION_SPECS.addThenGet(new AddressRangeContainsAddressRuleConditionSpec(
			"socks5.secondServerBoundAddress",
			Socks5RuleArgSpecConstants.SOCKS5_SECOND_SERVER_BOUND_ADDRESS));

	@HelpText(
			doc = "Specifies the second server bound port",
			usage = "socks5.secondServerBoundPort=PORT|PORT1-PORT2"
	)	
	public static final RuleConditionSpec<PortRange, Port> SOCKS5_SECOND_SERVER_BOUND_PORT = RULE_CONDITION_SPECS.addThenGet(new PortRangeContainsPortRuleConditionSpec(
			"socks5.secondServerBoundPort",
			Socks5RuleArgSpecConstants.SOCKS5_SECOND_SERVER_BOUND_PORT));	
	
	@HelpText(
			doc = "Specifies the server bound address",
			usage = "socks5.serverBoundAddress=ADDRESS|ADDRESS1-ADDRESS2|regex:REGULAR_EXPRESSION"
	)	
	public static final RuleConditionSpec<AddressRange, String> SOCKS5_SERVER_BOUND_ADDRESS = RULE_CONDITION_SPECS.addThenGet(new AddressRangeContainsAddressRuleConditionSpec(
			"socks5.serverBoundAddress",
			Socks5RuleArgSpecConstants.SOCKS5_SERVER_BOUND_ADDRESS));
	
	@HelpText(
			doc = "Specifies the server bound port",
			usage = "socks5.serverBoundPort=PORT|PORT1-PORT2"
	)	
	public static final RuleConditionSpec<PortRange, Port> SOCKS5_SERVER_BOUND_PORT = RULE_CONDITION_SPECS.addThenGet(new PortRangeContainsPortRuleConditionSpec(
			"socks5.serverBoundPort",
			Socks5RuleArgSpecConstants.SOCKS5_SERVER_BOUND_PORT));

	@HelpText(
			doc = "Specifies the UDP inbound desired destination address",
			usage = "socks5.udp.inbound.desiredDestinationAddress=ADDRESS|ADDRESS1-ADDRESS2|regex:REGULAR_EXPRESSION"
	)
	public static final RuleConditionSpec<AddressRange, String> SOCKS5_UDP_INBOUND_DESIRED_DESTINATION_ADDRESS = RULE_CONDITION_SPECS.addThenGet(new AddressRangeContainsAddressRuleConditionSpec(
			"socks5.udp.inbound.desiredDestinationAddress",
			Socks5RuleArgSpecConstants.SOCKS5_UDP_INBOUND_DESIRED_DESTINATION_ADDRESS));	
	
	@HelpText(
			doc = "Specifies the UDP inbound desired destination port",
			usage = "socks5.udp.inbound.desiredDestinationPort=PORT|PORT1-PORT2"
	)
	public static final RuleConditionSpec<PortRange, Port> SOCKS5_UDP_INBOUND_DESIRED_DESTINATION_PORT = RULE_CONDITION_SPECS.addThenGet(new PortRangeContainsPortRuleConditionSpec(
			"socks5.udp.inbound.desiredDestinationPort",
			Socks5RuleArgSpecConstants.SOCKS5_UDP_INBOUND_DESIRED_DESTINATION_PORT));
	
	@HelpText(
			doc = "Specifies the UDP inbound source address",
			usage = "socks5.udp.inbound.sourceAddress=ADDRESS|ADDRESS1-ADDRESS2|regex:REGULAR_EXPRESSION"
	)
	public static final RuleConditionSpec<AddressRange, String> SOCKS5_UDP_INBOUND_SOURCE_ADDRESS = RULE_CONDITION_SPECS.addThenGet(new AddressRangeContainsAddressRuleConditionSpec(
			"socks5.udp.inbound.sourceAddress",
			Socks5RuleArgSpecConstants.SOCKS5_UDP_INBOUND_SOURCE_ADDRESS));
	
	@HelpText(
			doc = "Specifies the UDP inbound source port",
			usage = "socks5.udp.inbound.sourcePort=PORT|PORT1-PORT2"
	)
	public static final RuleConditionSpec<PortRange, Port> SOCKS5_UDP_INBOUND_SOURCE_PORT = RULE_CONDITION_SPECS.addThenGet(new PortRangeContainsPortRuleConditionSpec(
			"socks5.udp.inbound.sourcePort",
			Socks5RuleArgSpecConstants.SOCKS5_UDP_INBOUND_SOURCE_PORT));
	
	@HelpText(
			doc = "Specifies the UDP outbound desired destination address",
			usage = "socks5.udp.outbound.desiredDestinationAddress=ADDRESS|ADDRESS1-ADDRESS2|regex:REGULAR_EXPRESSION"
	)
	public static final RuleConditionSpec<AddressRange, String> SOCKS5_UDP_OUTBOUND_DESIRED_DESTINATION_ADDRESS = RULE_CONDITION_SPECS.addThenGet(new AddressRangeContainsAddressRuleConditionSpec(
			"socks5.udp.outbound.desiredDestinationAddress",
			Socks5RuleArgSpecConstants.SOCKS5_UDP_OUTBOUND_DESIRED_DESTINATION_ADDRESS));	
	
	@HelpText(
			doc = "Specifies the UDP outbound desired destination port",
			usage = "socks5.udp.outbound.desiredDestinationPort=PORT|PORT1-PORT2"
	)
	public static final RuleConditionSpec<PortRange, Port> SOCKS5_UDP_OUTBOUND_DESIRED_DESTINATION_PORT = RULE_CONDITION_SPECS.addThenGet(new PortRangeContainsPortRuleConditionSpec(
			"socks5.udp.outbound.desiredDestinationPort",
			Socks5RuleArgSpecConstants.SOCKS5_UDP_OUTBOUND_DESIRED_DESTINATION_PORT));
	
	@HelpText(
			doc = "Specifies the UDP outbound source address",
			usage = "socks5.udp.outbound.sourceAddress=ADDRESS|ADDRESS1-ADDRESS2|regex:REGULAR_EXPRESSION"
	)
	public static final RuleConditionSpec<AddressRange, String> SOCKS5_UDP_OUTBOUND_SOURCE_ADDRESS = RULE_CONDITION_SPECS.addThenGet(new AddressRangeContainsAddressRuleConditionSpec(
			"socks5.udp.outbound.sourceAddress",
			Socks5RuleArgSpecConstants.SOCKS5_UDP_OUTBOUND_SOURCE_ADDRESS));
	
	@HelpText(
			doc = "Specifies the UDP outbound source port",
			usage = "socks5.udp.outbound.sourcePort=PORT|PORT1-PORT2"
	)
	public static final RuleConditionSpec<PortRange, Port> SOCKS5_UDP_OUTBOUND_SOURCE_PORT = RULE_CONDITION_SPECS.addThenGet(new PortRangeContainsPortRuleConditionSpec(
			"socks5.udp.outbound.sourcePort",
			Socks5RuleArgSpecConstants.SOCKS5_UDP_OUTBOUND_SOURCE_PORT));

	@HelpText(
			doc = "Specifies the user if any after the negotiated SOCKS5 method",
			usage = "socks5.user=USER"
	)
	public static final RuleConditionSpec<String, String> SOCKS5_USER = RULE_CONDITION_SPECS.addThenGet(new StringEqualsStringRuleConditionSpec(
			"socks5.user",
			Socks5RuleArgSpecConstants.SOCKS5_USER));	
	
	public static List<RuleConditionSpec<Object, Object>> values() {
		return RULE_CONDITION_SPECS.toList();
	}
	
	public static Map<String, RuleConditionSpec<Object, Object>> valuesMap() {
		return RULE_CONDITION_SPECS.toMap();
	}
	
	private Socks5RuleConditionSpecConstants() { }

}
