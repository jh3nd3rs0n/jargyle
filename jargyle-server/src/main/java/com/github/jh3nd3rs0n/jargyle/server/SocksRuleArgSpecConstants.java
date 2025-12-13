package com.github.jh3nd3rs0n.jargyle.server;

import com.github.jh3nd3rs0n.jargyle.common.net.Port;

import java.util.List;
import java.util.Map;

public final class SocksRuleArgSpecConstants {

    private static final RuleArgSpecs RULE_ARG_SPECS = new RuleArgSpecs();

    public static final RuleArgSpec<String> SOCKS_METHOD = RULE_ARG_SPECS.addThenGet(new RuleArgSpec<String>(
            "socks.method",
            String.class));

    public static final RuleArgSpec<String> SOCKS_REPLY_SERVER_BOUND_ADDRESS = RULE_ARG_SPECS.addThenGet(new RuleArgSpec<String>(
            "socks.reply.serverBoundAddress",
            String.class));

    public static final RuleArgSpec<Port> SOCKS_REPLY_SERVER_BOUND_PORT = RULE_ARG_SPECS.addThenGet(new RuleArgSpec<Port>(
            "socks.reply.serverBoundPort",
            Port.class));

    public static final RuleArgSpec<String> SOCKS_REQUEST_COMMAND = RULE_ARG_SPECS.addThenGet(new RuleArgSpec<String>(
            "socks.request.command",
            String.class));

    public static final RuleArgSpec<String> SOCKS_REQUEST_DESIRED_DESTINATION_ADDRESS = RULE_ARG_SPECS.addThenGet(new RuleArgSpec<String>(
            "socks.request.desiredDestinationAddress",
            String.class));

    public static final RuleArgSpec<Port> SOCKS_REQUEST_DESIRED_DESTINATION_PORT = RULE_ARG_SPECS.addThenGet(new RuleArgSpec<Port>(
            "socks.request.desiredDestinationPort",
            Port.class));

    public static final RuleArgSpec<String> SOCKS_SECOND_REPLY_SERVER_BOUND_ADDRESS = RULE_ARG_SPECS.addThenGet(new RuleArgSpec<String>(
            "socks.secondReply.serverBoundAddress",
            String.class));

    public static final RuleArgSpec<Port> SOCKS_SECOND_REPLY_SERVER_BOUND_PORT = RULE_ARG_SPECS.addThenGet(new RuleArgSpec<Port>(
            "socks.secondReply.serverBoundPort",
            Port.class));

    public static final RuleArgSpec<String> SOCKS_UDP_INBOUND_DESIRED_DESTINATION_ADDRESS = RULE_ARG_SPECS.addThenGet(new RuleArgSpec<String>(
            "socks.udp.inbound.desiredDestinationAddress",
            String.class));

    public static final RuleArgSpec<Port> SOCKS_UDP_INBOUND_DESIRED_DESTINATION_PORT = RULE_ARG_SPECS.addThenGet(new RuleArgSpec<Port>(
            "socks.udp.inbound.desiredDestinationPort",
            Port.class));

    public static final RuleArgSpec<String> SOCKS_UDP_INBOUND_SOURCE_ADDRESS = RULE_ARG_SPECS.addThenGet(new RuleArgSpec<String>(
            "socks.udp.inbound.sourceAddress",
            String.class));

    public static final RuleArgSpec<Port> SOCKS_UDP_INBOUND_SOURCE_PORT = RULE_ARG_SPECS.addThenGet(new RuleArgSpec<Port>(
            "socks.udp.inbound.sourcePort",
            Port.class));

    public static final RuleArgSpec<String> SOCKS_UDP_OUTBOUND_DESIRED_DESTINATION_ADDRESS = RULE_ARG_SPECS.addThenGet(new RuleArgSpec<String>(
            "socks.udp.outbound.desiredDestinationAddress",
            String.class));

    public static final RuleArgSpec<Port> SOCKS_UDP_OUTBOUND_DESIRED_DESTINATION_PORT = RULE_ARG_SPECS.addThenGet(new RuleArgSpec<Port>(
            "socks.udp.outbound.desiredDestinationPort",
            Port.class));

    public static final RuleArgSpec<String> SOCKS_UDP_OUTBOUND_SOURCE_ADDRESS = RULE_ARG_SPECS.addThenGet(new RuleArgSpec<String>(
            "socks.udp.outbound.sourceAddress",
            String.class));

    public static final RuleArgSpec<Port> SOCKS_UDP_OUTBOUND_SOURCE_PORT = RULE_ARG_SPECS.addThenGet(new RuleArgSpec<Port>(
            "socks.udp.outbound.sourcePort",
            Port.class));

    public static final RuleArgSpec<String> SOCKS_USER = RULE_ARG_SPECS.addThenGet(new RuleArgSpec<String>(
            "socks.user",
            String.class));

    public static List<RuleArgSpec<Object>> values() {
        return RULE_ARG_SPECS.toList();
    }

    public static Map<String, RuleArgSpec<Object>> valuesMap() {
        return RULE_ARG_SPECS.toMap();
    }
    
    private SocksRuleArgSpecConstants() { }
    
}
