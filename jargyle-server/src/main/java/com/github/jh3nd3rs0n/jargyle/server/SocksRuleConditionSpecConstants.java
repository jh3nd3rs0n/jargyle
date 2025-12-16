package com.github.jh3nd3rs0n.jargyle.server;

import com.github.jh3nd3rs0n.jargyle.common.net.Port;
import com.github.jh3nd3rs0n.jargyle.common.net.PortRange;
import com.github.jh3nd3rs0n.jargyle.internal.annotation.NameValuePairValueSpecDoc;
import com.github.jh3nd3rs0n.jargyle.internal.annotation.NameValuePairValueSpecsDoc;
import com.github.jh3nd3rs0n.jargyle.server.internal.ruleconditionspec.impl.AddressRangeCoversAddressRuleConditionSpec;
import com.github.jh3nd3rs0n.jargyle.server.internal.ruleconditionspec.impl.PortRangeCoversPortRuleConditionSpec;
import com.github.jh3nd3rs0n.jargyle.server.internal.ruleconditionspec.impl.StringEqualsStringRuleConditionSpec;

import java.util.List;
import java.util.Map;

@NameValuePairValueSpecsDoc(
        description = "",
        name = "SOCKS Rule Conditions"
)
public final class SocksRuleConditionSpecConstants {

    private static final RuleConditionSpecs RULE_CONDITION_SPECS = new RuleConditionSpecs();

    @NameValuePairValueSpecDoc(
            description = "Specifies the negotiated method.",
            name = "socks.method",
            syntax = "socks.method=SOCKS_METHOD",
            valueType = String.class
    )
    public static final RuleConditionSpec<String, String> SOCKS_METHOD = RULE_CONDITION_SPECS.addThenGet(new StringEqualsStringRuleConditionSpec(
            "socks.method",
            SocksRuleArgSpecConstants.SOCKS_METHOD));

    @NameValuePairValueSpecDoc(
            description = "Specifies the server bound address of the reply",
            name = "socks.reply.serverBoundAddress",
            syntax = "socks.reply.serverBoundAddress=ADDRESS_RANGE",
            valueType = AddressRange.class
    )
    public static final RuleConditionSpec<AddressRange, String> SOCKS_REPLY_SERVER_BOUND_ADDRESS = RULE_CONDITION_SPECS.addThenGet(new AddressRangeCoversAddressRuleConditionSpec(
            "socks.reply.serverBoundAddress",
            SocksRuleArgSpecConstants.SOCKS_REPLY_SERVER_BOUND_ADDRESS));

    @NameValuePairValueSpecDoc(
            description = "Specifies the server bound port of the reply",
            name = "socks.reply.serverBoundPort",
            syntax = "socks.reply.serverBoundPort=PORT_RANGE",
            valueType = PortRange.class
    )
    public static final RuleConditionSpec<PortRange, Port> SOCKS_REPLY_SERVER_BOUND_PORT = RULE_CONDITION_SPECS.addThenGet(new PortRangeCoversPortRuleConditionSpec(
            "socks.reply.serverBoundPort",
            SocksRuleArgSpecConstants.SOCKS_REPLY_SERVER_BOUND_PORT));

    @NameValuePairValueSpecDoc(
            description = "Specifies the command of the request",
            name = "socks.request.command",
            syntax = "socks.request.command=SOCKS_REQUEST_COMMAND",
            valueType = String.class
    )
    public static final RuleConditionSpec<String, String> SOCKS_REQUEST_COMMAND = RULE_CONDITION_SPECS.addThenGet(new StringEqualsStringRuleConditionSpec(
            "socks.request.command",
            SocksRuleArgSpecConstants.SOCKS_REQUEST_COMMAND));

    @NameValuePairValueSpecDoc(
            description = "Specifies the desired destination address of the "
                    + "request",
            name = "socks.request.desiredDestinationAddress",
            syntax = "socks.request.desiredDestinationAddress=ADDRESS_RANGE",
            valueType = AddressRange.class
    )
    public static final RuleConditionSpec<AddressRange, String> SOCKS_REQUEST_DESIRED_DESTINATION_ADDRESS = RULE_CONDITION_SPECS.addThenGet(new AddressRangeCoversAddressRuleConditionSpec(
            "socks.request.desiredDestinationAddress",
            SocksRuleArgSpecConstants.SOCKS_REQUEST_DESIRED_DESTINATION_ADDRESS));

    @NameValuePairValueSpecDoc(
            description = "Specifies the desired destination port of the "
                    + "request",
            name = "socks.request.desiredDestinationPort",
            syntax = "socks.request.desiredDestinationPort=PORT_RANGE",
            valueType = PortRange.class
    )
    public static final RuleConditionSpec<PortRange, Port> SOCKS_REQUEST_DESIRED_DESTINATION_PORT = RULE_CONDITION_SPECS.addThenGet(new PortRangeCoversPortRuleConditionSpec(
            "socks.request.desiredDestinationPort",
            SocksRuleArgSpecConstants.SOCKS_REQUEST_DESIRED_DESTINATION_PORT));

    @NameValuePairValueSpecDoc(
            description = "Specifies the server bound address of the second "
                    + "reply (for the BIND request)",
            name = "socks.secondReply.serverBoundAddress",
            syntax = "socks.secondReply.serverBoundAddress=ADDRESS_RANGE",
            valueType = AddressRange.class
    )
    public static final RuleConditionSpec<AddressRange, String> SOCKS_SECOND_REPLY_SERVER_BOUND_ADDRESS = RULE_CONDITION_SPECS.addThenGet(new AddressRangeCoversAddressRuleConditionSpec(
            "socks.secondReply.serverBoundAddress",
            SocksRuleArgSpecConstants.SOCKS_SECOND_REPLY_SERVER_BOUND_ADDRESS));

    @NameValuePairValueSpecDoc(
            description = "Specifies the server bound port of the second "
                    + "reply (for the BIND request)",
            name = "socks.secondReply.serverBoundPort",
            syntax = "socks.secondReply.serverBoundPort=PORT_RANGE",
            valueType = PortRange.class
    )
    public static final RuleConditionSpec<PortRange, Port> SOCKS_SECOND_REPLY_SERVER_BOUND_PORT = RULE_CONDITION_SPECS.addThenGet(new PortRangeCoversPortRuleConditionSpec(
            "socks.secondReply.serverBoundPort",
            SocksRuleArgSpecConstants.SOCKS_SECOND_REPLY_SERVER_BOUND_PORT));

    @NameValuePairValueSpecDoc(
            description = "Specifies the UDP inbound desired destination address",
            name = "socks.udp.inbound.desiredDestinationAddress",
            syntax = "socks.udp.inbound.desiredDestinationAddress=ADDRESS_RANGE",
            valueType = AddressRange.class
    )
    public static final RuleConditionSpec<AddressRange, String> SOCKS_UDP_INBOUND_DESIRED_DESTINATION_ADDRESS = RULE_CONDITION_SPECS.addThenGet(new AddressRangeCoversAddressRuleConditionSpec(
            "socks.udp.inbound.desiredDestinationAddress",
            SocksRuleArgSpecConstants.SOCKS_UDP_INBOUND_DESIRED_DESTINATION_ADDRESS));

    @NameValuePairValueSpecDoc(
            description = "Specifies the UDP inbound desired destination port",
            name = "socks.udp.inbound.desiredDestinationPort",
            syntax = "socks.udp.inbound.desiredDestinationPort=PORT_RANGE",
            valueType = PortRange.class
    )
    public static final RuleConditionSpec<PortRange, Port> SOCKS_UDP_INBOUND_DESIRED_DESTINATION_PORT = RULE_CONDITION_SPECS.addThenGet(new PortRangeCoversPortRuleConditionSpec(
            "socks.udp.inbound.desiredDestinationPort",
            SocksRuleArgSpecConstants.SOCKS_UDP_INBOUND_DESIRED_DESTINATION_PORT));

    @NameValuePairValueSpecDoc(
            description = "Specifies the UDP inbound source address",
            name = "socks.udp.inbound.sourceAddress",
            syntax = "socks.udp.inbound.sourceAddress=ADDRESS_RANGE",
            valueType = AddressRange.class
    )
    public static final RuleConditionSpec<AddressRange, String> SOCKS_UDP_INBOUND_SOURCE_ADDRESS = RULE_CONDITION_SPECS.addThenGet(new AddressRangeCoversAddressRuleConditionSpec(
            "socks.udp.inbound.sourceAddress",
            SocksRuleArgSpecConstants.SOCKS_UDP_INBOUND_SOURCE_ADDRESS));

    @NameValuePairValueSpecDoc(
            description = "Specifies the UDP inbound source port",
            name = "socks.udp.inbound.sourcePort",
            syntax = "socks.udp.inbound.sourcePort=PORT_RANGE",
            valueType = PortRange.class
    )
    public static final RuleConditionSpec<PortRange, Port> SOCKS_UDP_INBOUND_SOURCE_PORT = RULE_CONDITION_SPECS.addThenGet(new PortRangeCoversPortRuleConditionSpec(
            "socks.udp.inbound.sourcePort",
            SocksRuleArgSpecConstants.SOCKS_UDP_INBOUND_SOURCE_PORT));

    @NameValuePairValueSpecDoc(
            description = "Specifies the UDP outbound desired destination address",
            name = "socks.udp.outbound.desiredDestinationAddress",
            syntax = "socks.udp.outbound.desiredDestinationAddress=ADDRESS_RANGE",
            valueType = AddressRange.class
    )
    public static final RuleConditionSpec<AddressRange, String> SOCKS_UDP_OUTBOUND_DESIRED_DESTINATION_ADDRESS = RULE_CONDITION_SPECS.addThenGet(new AddressRangeCoversAddressRuleConditionSpec(
            "socks.udp.outbound.desiredDestinationAddress",
            SocksRuleArgSpecConstants.SOCKS_UDP_OUTBOUND_DESIRED_DESTINATION_ADDRESS));

    @NameValuePairValueSpecDoc(
            description = "Specifies the UDP outbound desired destination port",
            name = "socks.udp.outbound.desiredDestinationPort",
            syntax = "socks.udp.outbound.desiredDestinationPort=PORT_RANGE",
            valueType = PortRange.class
    )
    public static final RuleConditionSpec<PortRange, Port> SOCKS_UDP_OUTBOUND_DESIRED_DESTINATION_PORT = RULE_CONDITION_SPECS.addThenGet(new PortRangeCoversPortRuleConditionSpec(
            "socks.udp.outbound.desiredDestinationPort",
            SocksRuleArgSpecConstants.SOCKS_UDP_OUTBOUND_DESIRED_DESTINATION_PORT));

    @NameValuePairValueSpecDoc(
            description = "Specifies the UDP outbound source address",
            name = "socks.udp.outbound.sourceAddress",
            syntax = "socks.udp.outbound.sourceAddress=ADDRESS_RANGE",
            valueType = AddressRange.class
    )
    public static final RuleConditionSpec<AddressRange, String> SOCKS_UDP_OUTBOUND_SOURCE_ADDRESS = RULE_CONDITION_SPECS.addThenGet(new AddressRangeCoversAddressRuleConditionSpec(
            "socks.udp.outbound.sourceAddress",
            SocksRuleArgSpecConstants.SOCKS_UDP_OUTBOUND_SOURCE_ADDRESS));

    @NameValuePairValueSpecDoc(
            description = "Specifies the UDP outbound source port",
            name = "socks.udp.outbound.sourcePort",
            syntax = "socks.udp.outbound.sourcePort=PORT_RANGE",
            valueType = PortRange.class
    )
    public static final RuleConditionSpec<PortRange, Port> SOCKS_UDP_OUTBOUND_SOURCE_PORT = RULE_CONDITION_SPECS.addThenGet(new PortRangeCoversPortRuleConditionSpec(
            "socks.udp.outbound.sourcePort",
            SocksRuleArgSpecConstants.SOCKS_UDP_OUTBOUND_SOURCE_PORT));

    @NameValuePairValueSpecDoc(
            description = "Specifies the user if any after the negotiated "
                    + "method",
            name = "socks.user",
            syntax = "socks.user=USER",
            valueType = String.class
    )
    public static final RuleConditionSpec<String, String> SOCKS_USER = RULE_CONDITION_SPECS.addThenGet(new StringEqualsStringRuleConditionSpec(
            "socks.user",
            SocksRuleArgSpecConstants.SOCKS_USER));

    public static List<RuleConditionSpec<Object, Object>> values() {
        return RULE_CONDITION_SPECS.toList();
    }

    public static Map<String, RuleConditionSpec<Object, Object>> valuesMap() {
        return RULE_CONDITION_SPECS.toMap();
    }
    
    private SocksRuleConditionSpecConstants() { }
    
}
