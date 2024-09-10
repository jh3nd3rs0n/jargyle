package com.github.jh3nd3rs0n.jargyle.server;

import java.util.List;
import java.util.Map;

import com.github.jh3nd3rs0n.jargyle.common.net.Port;
import com.github.jh3nd3rs0n.jargyle.common.net.PortRange;
import com.github.jh3nd3rs0n.jargyle.internal.annotation.NameValuePairValueSpecDoc;
import com.github.jh3nd3rs0n.jargyle.internal.annotation.NameValuePairValueSpecsDoc;
import com.github.jh3nd3rs0n.jargyle.protocolbase.socks5.Command;
import com.github.jh3nd3rs0n.jargyle.protocolbase.socks5.Method;
import com.github.jh3nd3rs0n.jargyle.server.internal.ruleconditionspec.impl.AddressRangeCoversAddressRuleConditionSpec;
import com.github.jh3nd3rs0n.jargyle.server.internal.ruleconditionspec.impl.PortRangeCoversPortRuleConditionSpec;
import com.github.jh3nd3rs0n.jargyle.server.internal.ruleconditionspec.impl.StringEqualsStringRuleConditionSpec;

@NameValuePairValueSpecsDoc(
		description = "",
		name = "SOCKS5 Rule Conditions"
)
public final class Socks5RuleConditionSpecConstants {
	
	private static final RuleConditionSpecs RULE_CONDITION_SPECS = new RuleConditionSpecs();

	@NameValuePairValueSpecDoc(
			description = "Specifies the negotiated method",
			name = "socks5.method",
			syntax = "socks5.method=SOCKS5_METHOD",
			valueType = Method.class
	)
	public static final RuleConditionSpec<String, String> SOCKS5_METHOD = RULE_CONDITION_SPECS.addThenGet(new StringEqualsStringRuleConditionSpec(
			"socks5.method",
			Socks5RuleArgSpecConstants.SOCKS5_METHOD));

	@NameValuePairValueSpecDoc(
			description = "Specifies the server bound address of the reply",
			name = "socks5.reply.serverBoundAddress",
			syntax = "socks5.reply.serverBoundAddress=ADDRESS_RANGE",
			valueType = AddressRange.class
	)
	public static final RuleConditionSpec<AddressRange, String> SOCKS5_REPLY_SERVER_BOUND_ADDRESS = RULE_CONDITION_SPECS.addThenGet(new AddressRangeCoversAddressRuleConditionSpec(
			"socks5.reply.serverBoundAddress",
			Socks5RuleArgSpecConstants.SOCKS5_REPLY_SERVER_BOUND_ADDRESS));

	@NameValuePairValueSpecDoc(
			description = "Specifies the server bound port of the reply",
			name = "socks5.reply.serverBoundPort",
			syntax = "socks5.reply.serverBoundPort=PORT_RANGE",
			valueType = PortRange.class
	)
	public static final RuleConditionSpec<PortRange, Port> SOCKS5_REPLY_SERVER_BOUND_PORT = RULE_CONDITION_SPECS.addThenGet(new PortRangeCoversPortRuleConditionSpec(
			"socks5.reply.serverBoundPort",
			Socks5RuleArgSpecConstants.SOCKS5_REPLY_SERVER_BOUND_PORT));

	@NameValuePairValueSpecDoc(
			description = "Specifies the command of the request",
			name = "socks5.request.command",
			syntax = "socks5.request.command=SOCKS5_REQUEST_COMMAND",
			valueType = Command.class
	)	
	public static final RuleConditionSpec<String, String> SOCKS5_REQUEST_COMMAND = RULE_CONDITION_SPECS.addThenGet(new StringEqualsStringRuleConditionSpec(
			"socks5.request.command",
			Socks5RuleArgSpecConstants.SOCKS5_REQUEST_COMMAND));

	@NameValuePairValueSpecDoc(
			description = "Specifies the desired destination address of the " 
					+ "request",
			name = "socks5.request.desiredDestinationAddress",
			syntax = "socks5.request.desiredDestinationAddress=ADDRESS_RANGE",
			valueType = AddressRange.class
	)	
	public static final RuleConditionSpec<AddressRange, String> SOCKS5_REQUEST_DESIRED_DESTINATION_ADDRESS = RULE_CONDITION_SPECS.addThenGet(new AddressRangeCoversAddressRuleConditionSpec(
			"socks5.request.desiredDestinationAddress",
			Socks5RuleArgSpecConstants.SOCKS5_REQUEST_DESIRED_DESTINATION_ADDRESS));
	
	@NameValuePairValueSpecDoc(
			description = "Specifies the desired destination port of the " 
					+ "request",
			name = "socks5.request.desiredDestinationPort",
			syntax = "socks5.request.desiredDestinationPort=PORT_RANGE",
			valueType = PortRange.class
	)	
	public static final RuleConditionSpec<PortRange, Port> SOCKS5_REQUEST_DESIRED_DESTINATION_PORT = RULE_CONDITION_SPECS.addThenGet(new PortRangeCoversPortRuleConditionSpec(
			"socks5.request.desiredDestinationPort",
			Socks5RuleArgSpecConstants.SOCKS5_REQUEST_DESIRED_DESTINATION_PORT));

	@NameValuePairValueSpecDoc(
			description = "Specifies the server bound address of the second " 
					+ "reply (for the BIND request)",
			name = "socks5.secondReply.serverBoundAddress",
			syntax = "socks5.secondReply.serverBoundAddress=ADDRESS_RANGE",
			valueType = AddressRange.class
	)	
	public static final RuleConditionSpec<AddressRange, String> SOCKS5_SECOND_REPLY_SERVER_BOUND_ADDRESS = RULE_CONDITION_SPECS.addThenGet(new AddressRangeCoversAddressRuleConditionSpec(
			"socks5.secondReply.serverBoundAddress",
			Socks5RuleArgSpecConstants.SOCKS5_SECOND_REPLY_SERVER_BOUND_ADDRESS));

	@NameValuePairValueSpecDoc(
			description = "Specifies the server bound port of the second " 
					+ "reply (for the BIND request)",
			name = "socks5.secondReply.serverBoundPort",
			syntax = "socks5.secondReply.serverBoundPort=PORT_RANGE",
			valueType = PortRange.class
	)	
	public static final RuleConditionSpec<PortRange, Port> SOCKS5_SECOND_REPLY_SERVER_BOUND_PORT = RULE_CONDITION_SPECS.addThenGet(new PortRangeCoversPortRuleConditionSpec(
			"socks5.secondReply.serverBoundPort",
			Socks5RuleArgSpecConstants.SOCKS5_SECOND_REPLY_SERVER_BOUND_PORT));	

	@NameValuePairValueSpecDoc(
			description = "Specifies the UDP inbound desired destination address",
			name = "socks5.udp.inbound.desiredDestinationAddress",
			syntax = "socks5.udp.inbound.desiredDestinationAddress=ADDRESS_RANGE",
			valueType = AddressRange.class
	)
	public static final RuleConditionSpec<AddressRange, String> SOCKS5_UDP_INBOUND_DESIRED_DESTINATION_ADDRESS = RULE_CONDITION_SPECS.addThenGet(new AddressRangeCoversAddressRuleConditionSpec(
			"socks5.udp.inbound.desiredDestinationAddress",
			Socks5RuleArgSpecConstants.SOCKS5_UDP_INBOUND_DESIRED_DESTINATION_ADDRESS));	
	
	@NameValuePairValueSpecDoc(
			description = "Specifies the UDP inbound desired destination port",
			name = "socks5.udp.inbound.desiredDestinationPort",
			syntax = "socks5.udp.inbound.desiredDestinationPort=PORT_RANGE",
			valueType = PortRange.class
	)
	public static final RuleConditionSpec<PortRange, Port> SOCKS5_UDP_INBOUND_DESIRED_DESTINATION_PORT = RULE_CONDITION_SPECS.addThenGet(new PortRangeCoversPortRuleConditionSpec(
			"socks5.udp.inbound.desiredDestinationPort",
			Socks5RuleArgSpecConstants.SOCKS5_UDP_INBOUND_DESIRED_DESTINATION_PORT));
	
	@NameValuePairValueSpecDoc(
			description = "Specifies the UDP inbound source address",
			name = "socks5.udp.inbound.sourceAddress",
			syntax = "socks5.udp.inbound.sourceAddress=ADDRESS_RANGE",
			valueType = AddressRange.class
	)
	public static final RuleConditionSpec<AddressRange, String> SOCKS5_UDP_INBOUND_SOURCE_ADDRESS = RULE_CONDITION_SPECS.addThenGet(new AddressRangeCoversAddressRuleConditionSpec(
			"socks5.udp.inbound.sourceAddress",
			Socks5RuleArgSpecConstants.SOCKS5_UDP_INBOUND_SOURCE_ADDRESS));
	
	@NameValuePairValueSpecDoc(
			description = "Specifies the UDP inbound source port",
			name = "socks5.udp.inbound.sourcePort",
			syntax = "socks5.udp.inbound.sourcePort=PORT_RANGE",
			valueType = PortRange.class
	)
	public static final RuleConditionSpec<PortRange, Port> SOCKS5_UDP_INBOUND_SOURCE_PORT = RULE_CONDITION_SPECS.addThenGet(new PortRangeCoversPortRuleConditionSpec(
			"socks5.udp.inbound.sourcePort",
			Socks5RuleArgSpecConstants.SOCKS5_UDP_INBOUND_SOURCE_PORT));
	
	@NameValuePairValueSpecDoc(
			description = "Specifies the UDP outbound desired destination address",
			name = "socks5.udp.outbound.desiredDestinationAddress",
			syntax = "socks5.udp.outbound.desiredDestinationAddress=ADDRESS_RANGE",
			valueType = AddressRange.class
	)
	public static final RuleConditionSpec<AddressRange, String> SOCKS5_UDP_OUTBOUND_DESIRED_DESTINATION_ADDRESS = RULE_CONDITION_SPECS.addThenGet(new AddressRangeCoversAddressRuleConditionSpec(
			"socks5.udp.outbound.desiredDestinationAddress",
			Socks5RuleArgSpecConstants.SOCKS5_UDP_OUTBOUND_DESIRED_DESTINATION_ADDRESS));	
	
	@NameValuePairValueSpecDoc(
			description = "Specifies the UDP outbound desired destination port",
			name = "socks5.udp.outbound.desiredDestinationPort",
			syntax = "socks5.udp.outbound.desiredDestinationPort=PORT_RANGE",
			valueType = PortRange.class
	)
	public static final RuleConditionSpec<PortRange, Port> SOCKS5_UDP_OUTBOUND_DESIRED_DESTINATION_PORT = RULE_CONDITION_SPECS.addThenGet(new PortRangeCoversPortRuleConditionSpec(
			"socks5.udp.outbound.desiredDestinationPort",
			Socks5RuleArgSpecConstants.SOCKS5_UDP_OUTBOUND_DESIRED_DESTINATION_PORT));
	
	@NameValuePairValueSpecDoc(
			description = "Specifies the UDP outbound source address",
			name = "socks5.udp.outbound.sourceAddress",
			syntax = "socks5.udp.outbound.sourceAddress=ADDRESS_RANGE",
			valueType = AddressRange.class
	)
	public static final RuleConditionSpec<AddressRange, String> SOCKS5_UDP_OUTBOUND_SOURCE_ADDRESS = RULE_CONDITION_SPECS.addThenGet(new AddressRangeCoversAddressRuleConditionSpec(
			"socks5.udp.outbound.sourceAddress",
			Socks5RuleArgSpecConstants.SOCKS5_UDP_OUTBOUND_SOURCE_ADDRESS));
	
	@NameValuePairValueSpecDoc(
			description = "Specifies the UDP outbound source port",
			name = "socks5.udp.outbound.sourcePort",
			syntax = "socks5.udp.outbound.sourcePort=PORT_RANGE",
			valueType = PortRange.class
	)
	public static final RuleConditionSpec<PortRange, Port> SOCKS5_UDP_OUTBOUND_SOURCE_PORT = RULE_CONDITION_SPECS.addThenGet(new PortRangeCoversPortRuleConditionSpec(
			"socks5.udp.outbound.sourcePort",
			Socks5RuleArgSpecConstants.SOCKS5_UDP_OUTBOUND_SOURCE_PORT));

	@NameValuePairValueSpecDoc(
			description = "Specifies the user if any after the negotiated "
					+ "method",
			name = "socks5.user",
			syntax = "socks5.user=USER",
			valueType = String.class
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
