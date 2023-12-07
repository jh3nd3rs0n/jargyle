package com.github.jh3nd3rs0n.jargyle.server;

import java.util.List;
import java.util.Map;

import com.github.jh3nd3rs0n.jargyle.common.net.Port;
import com.github.jh3nd3rs0n.jargyle.common.net.PortRange;
import com.github.jh3nd3rs0n.jargyle.internal.annotation.NameValuePairValueSpecDoc;
import com.github.jh3nd3rs0n.jargyle.internal.annotation.NameValuePairValueSpecsDoc;
import com.github.jh3nd3rs0n.jargyle.protocolbase.socks5.Command;
import com.github.jh3nd3rs0n.jargyle.protocolbase.socks5.Method;
import com.github.jh3nd3rs0n.jargyle.server.internal.ruleconditionspec.impl.AddressRangeContainsAddressRuleConditionSpec;
import com.github.jh3nd3rs0n.jargyle.server.internal.ruleconditionspec.impl.PortRangeContainsPortRuleConditionSpec;
import com.github.jh3nd3rs0n.jargyle.server.internal.ruleconditionspec.impl.StringEqualsStringRuleConditionSpec;

@NameValuePairValueSpecsDoc(
		description = "",
		name = "SOCKS5 Rule Conditions"
)
public final class Socks5RuleConditionSpecConstants {
	
	private static final RuleConditionSpecs RULE_CONDITION_SPECS = new RuleConditionSpecs();
	
	@NameValuePairValueSpecDoc(
			description = "Specifies the SOCKS5 command",
			name = "socks5.command",
			syntax = "socks5.command=SOCKS5_COMMAND",
			valueType = Command.class
	)	
	public static final RuleConditionSpec<String, String> SOCKS5_COMMAND = RULE_CONDITION_SPECS.addThenGet(new StringEqualsStringRuleConditionSpec(
			"socks5.command",
			Socks5RuleArgSpecConstants.SOCKS5_COMMAND));

	@NameValuePairValueSpecDoc(
			description = "Specifies the desired destination address",
			name = "socks5.desiredDestinationAddress",
			syntax = "socks5.desiredDestinationAddress=ADDRESS_RANGE",
			valueType = AddressRange.class
	)	
	public static final RuleConditionSpec<AddressRange, String> SOCKS5_DESIRED_DESTINATION_ADDRESS = RULE_CONDITION_SPECS.addThenGet(new AddressRangeContainsAddressRuleConditionSpec(
			"socks5.desiredDestinationAddress",
			Socks5RuleArgSpecConstants.SOCKS5_DESIRED_DESTINATION_ADDRESS));
	
	@NameValuePairValueSpecDoc(
			description = "Specifies the desired destination port",
			name = "socks5.desiredDestinationPort",
			syntax = "socks5.desiredDestinationPort=PORT_RANGE",
			valueType = PortRange.class
	)	
	public static final RuleConditionSpec<PortRange, Port> SOCKS5_DESIRED_DESTINATION_PORT = RULE_CONDITION_SPECS.addThenGet(new PortRangeContainsPortRuleConditionSpec(
			"socks5.desiredDestinationPort",
			Socks5RuleArgSpecConstants.SOCKS5_DESIRED_DESTINATION_PORT));
	
	@NameValuePairValueSpecDoc(
			description = "Specifies the negotiated SOCKS5 method",
			name = "socks5.method",
			syntax = "socks5.method=SOCKS5_METHOD",
			valueType = Method.class
	)
	public static final RuleConditionSpec<String, String> SOCKS5_METHOD = RULE_CONDITION_SPECS.addThenGet(new StringEqualsStringRuleConditionSpec(
			"socks5.method",
			Socks5RuleArgSpecConstants.SOCKS5_METHOD));
	
	@NameValuePairValueSpecDoc(
			description = "Specifies the second server bound address",
			name = "socks5.secondServerBoundAddress",
			syntax = "socks5.secondServerBoundAddress=ADDRESS_RANGE",
			valueType = AddressRange.class
	)	
	public static final RuleConditionSpec<AddressRange, String> SOCKS5_SECOND_SERVER_BOUND_ADDRESS = RULE_CONDITION_SPECS.addThenGet(new AddressRangeContainsAddressRuleConditionSpec(
			"socks5.secondServerBoundAddress",
			Socks5RuleArgSpecConstants.SOCKS5_SECOND_SERVER_BOUND_ADDRESS));

	@NameValuePairValueSpecDoc(
			description = "Specifies the second server bound port",
			name = "socks5.secondServerBoundPort",
			syntax = "socks5.secondServerBoundPort=PORT_RANGE",
			valueType = PortRange.class
	)	
	public static final RuleConditionSpec<PortRange, Port> SOCKS5_SECOND_SERVER_BOUND_PORT = RULE_CONDITION_SPECS.addThenGet(new PortRangeContainsPortRuleConditionSpec(
			"socks5.secondServerBoundPort",
			Socks5RuleArgSpecConstants.SOCKS5_SECOND_SERVER_BOUND_PORT));	
	
	@NameValuePairValueSpecDoc(
			description = "Specifies the server bound address",
			name = "socks5.serverBoundAddress",
			syntax = "socks5.serverBoundAddress=ADDRESS_RANGE",
			valueType = AddressRange.class
	)	
	public static final RuleConditionSpec<AddressRange, String> SOCKS5_SERVER_BOUND_ADDRESS = RULE_CONDITION_SPECS.addThenGet(new AddressRangeContainsAddressRuleConditionSpec(
			"socks5.serverBoundAddress",
			Socks5RuleArgSpecConstants.SOCKS5_SERVER_BOUND_ADDRESS));
	
	@NameValuePairValueSpecDoc(
			description = "Specifies the server bound port",
			name = "socks5.serverBoundPort",
			syntax = "socks5.serverBoundPort=PORT_RANGE",
			valueType = PortRange.class
	)	
	public static final RuleConditionSpec<PortRange, Port> SOCKS5_SERVER_BOUND_PORT = RULE_CONDITION_SPECS.addThenGet(new PortRangeContainsPortRuleConditionSpec(
			"socks5.serverBoundPort",
			Socks5RuleArgSpecConstants.SOCKS5_SERVER_BOUND_PORT));

	@NameValuePairValueSpecDoc(
			description = "Specifies the UDP inbound desired destination address",
			name = "socks5.udp.inbound.desiredDestinationAddress",
			syntax = "socks5.udp.inbound.desiredDestinationAddress=ADDRESS_RANGE",
			valueType = AddressRange.class
	)
	public static final RuleConditionSpec<AddressRange, String> SOCKS5_UDP_INBOUND_DESIRED_DESTINATION_ADDRESS = RULE_CONDITION_SPECS.addThenGet(new AddressRangeContainsAddressRuleConditionSpec(
			"socks5.udp.inbound.desiredDestinationAddress",
			Socks5RuleArgSpecConstants.SOCKS5_UDP_INBOUND_DESIRED_DESTINATION_ADDRESS));	
	
	@NameValuePairValueSpecDoc(
			description = "Specifies the UDP inbound desired destination port",
			name = "socks5.udp.inbound.desiredDestinationPort",
			syntax = "socks5.udp.inbound.desiredDestinationPort=PORT_RANGE",
			valueType = PortRange.class
	)
	public static final RuleConditionSpec<PortRange, Port> SOCKS5_UDP_INBOUND_DESIRED_DESTINATION_PORT = RULE_CONDITION_SPECS.addThenGet(new PortRangeContainsPortRuleConditionSpec(
			"socks5.udp.inbound.desiredDestinationPort",
			Socks5RuleArgSpecConstants.SOCKS5_UDP_INBOUND_DESIRED_DESTINATION_PORT));
	
	@NameValuePairValueSpecDoc(
			description = "Specifies the UDP inbound source address",
			name = "socks5.udp.inbound.sourceAddress",
			syntax = "socks5.udp.inbound.sourceAddress=ADDRESS_RANGE",
			valueType = AddressRange.class
	)
	public static final RuleConditionSpec<AddressRange, String> SOCKS5_UDP_INBOUND_SOURCE_ADDRESS = RULE_CONDITION_SPECS.addThenGet(new AddressRangeContainsAddressRuleConditionSpec(
			"socks5.udp.inbound.sourceAddress",
			Socks5RuleArgSpecConstants.SOCKS5_UDP_INBOUND_SOURCE_ADDRESS));
	
	@NameValuePairValueSpecDoc(
			description = "Specifies the UDP inbound source port",
			name = "socks5.udp.inbound.sourcePort",
			syntax = "socks5.udp.inbound.sourcePort=PORT_RANGE",
			valueType = PortRange.class
	)
	public static final RuleConditionSpec<PortRange, Port> SOCKS5_UDP_INBOUND_SOURCE_PORT = RULE_CONDITION_SPECS.addThenGet(new PortRangeContainsPortRuleConditionSpec(
			"socks5.udp.inbound.sourcePort",
			Socks5RuleArgSpecConstants.SOCKS5_UDP_INBOUND_SOURCE_PORT));
	
	@NameValuePairValueSpecDoc(
			description = "Specifies the UDP outbound desired destination address",
			name = "socks5.udp.outbound.desiredDestinationAddress",
			syntax = "socks5.udp.outbound.desiredDestinationAddress=ADDRESS_RANGE",
			valueType = AddressRange.class
	)
	public static final RuleConditionSpec<AddressRange, String> SOCKS5_UDP_OUTBOUND_DESIRED_DESTINATION_ADDRESS = RULE_CONDITION_SPECS.addThenGet(new AddressRangeContainsAddressRuleConditionSpec(
			"socks5.udp.outbound.desiredDestinationAddress",
			Socks5RuleArgSpecConstants.SOCKS5_UDP_OUTBOUND_DESIRED_DESTINATION_ADDRESS));	
	
	@NameValuePairValueSpecDoc(
			description = "Specifies the UDP outbound desired destination port",
			name = "socks5.udp.outbound.desiredDestinationPort",
			syntax = "socks5.udp.outbound.desiredDestinationPort=PORT_RANGE",
			valueType = PortRange.class
	)
	public static final RuleConditionSpec<PortRange, Port> SOCKS5_UDP_OUTBOUND_DESIRED_DESTINATION_PORT = RULE_CONDITION_SPECS.addThenGet(new PortRangeContainsPortRuleConditionSpec(
			"socks5.udp.outbound.desiredDestinationPort",
			Socks5RuleArgSpecConstants.SOCKS5_UDP_OUTBOUND_DESIRED_DESTINATION_PORT));
	
	@NameValuePairValueSpecDoc(
			description = "Specifies the UDP outbound source address",
			name = "socks5.udp.outbound.sourceAddress",
			syntax = "socks5.udp.outbound.sourceAddress=ADDRESS_RANGE",
			valueType = AddressRange.class
	)
	public static final RuleConditionSpec<AddressRange, String> SOCKS5_UDP_OUTBOUND_SOURCE_ADDRESS = RULE_CONDITION_SPECS.addThenGet(new AddressRangeContainsAddressRuleConditionSpec(
			"socks5.udp.outbound.sourceAddress",
			Socks5RuleArgSpecConstants.SOCKS5_UDP_OUTBOUND_SOURCE_ADDRESS));
	
	@NameValuePairValueSpecDoc(
			description = "Specifies the UDP outbound source port",
			name = "socks5.udp.outbound.sourcePort",
			syntax = "socks5.udp.outbound.sourcePort=PORT_RANGE",
			valueType = PortRange.class
	)
	public static final RuleConditionSpec<PortRange, Port> SOCKS5_UDP_OUTBOUND_SOURCE_PORT = RULE_CONDITION_SPECS.addThenGet(new PortRangeContainsPortRuleConditionSpec(
			"socks5.udp.outbound.sourcePort",
			Socks5RuleArgSpecConstants.SOCKS5_UDP_OUTBOUND_SOURCE_PORT));

	@NameValuePairValueSpecDoc(
			description = "Specifies the user if any after the negotiated "
					+ "SOCKS5 method",
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
