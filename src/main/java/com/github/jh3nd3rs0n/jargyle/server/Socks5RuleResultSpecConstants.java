package com.github.jh3nd3rs0n.jargyle.server;

import java.util.List;
import java.util.Map;

import com.github.jh3nd3rs0n.jargyle.common.net.Host;
import com.github.jh3nd3rs0n.jargyle.common.net.Port;
import com.github.jh3nd3rs0n.jargyle.common.net.SocketSetting;
import com.github.jh3nd3rs0n.jargyle.common.number.PositiveInteger;
import com.github.jh3nd3rs0n.jargyle.internal.help.HelpText;
import com.github.jh3nd3rs0n.jargyle.server.internal.rules.impl.AddressRuleResultSpec;
import com.github.jh3nd3rs0n.jargyle.server.internal.rules.impl.BooleanRuleResultSpec;
import com.github.jh3nd3rs0n.jargyle.server.internal.rules.impl.HostRuleResultSpec;
import com.github.jh3nd3rs0n.jargyle.server.internal.rules.impl.LogActionRuleResultSpec;
import com.github.jh3nd3rs0n.jargyle.server.internal.rules.impl.PortRuleResultSpec;
import com.github.jh3nd3rs0n.jargyle.server.internal.rules.impl.PositiveIntegerRuleResultSpec;
import com.github.jh3nd3rs0n.jargyle.server.internal.rules.impl.SocketSettingRuleResultSpec;
import com.github.jh3nd3rs0n.jargyle.transport.socks5.Address;

public final class Socks5RuleResultSpecConstants {

	private static final RuleResultSpecs RULE_RESULT_SPECS = new RuleResultSpecs();

	@HelpText(
			doc = "Specifies the desired destination address redirect",
			usage = "socks5.desiredDestinationAddressRedirect=ADDRESS"
	)	
	public static final RuleResultSpec<Address> SOCKS5_DESIRED_DESTINATION_ADDRESS_REDIRECT = RULE_RESULT_SPECS.addThenGet(new AddressRuleResultSpec(
			"socks5.desiredDestinationAddressRedirect"));
	
	@HelpText(
			doc = "Specifies the desired destination port redirect",
			usage = "socks5.desiredDestinationPortRedirect=PORT"
	)	
	public static final RuleResultSpec<Port> SOCKS5_DESIRED_DESTINATION_PORT_REDIRECT = RULE_RESULT_SPECS.addThenGet(new PortRuleResultSpec(
			"socks5.desiredDestinationPortRedirect"));
	
	@HelpText(
			doc = "Specifies the logging action to take if the desired "
					+ "destination is redirected",
			usage = "socks5.desiredDestinationRedirectLogAction=LOG_ACTION"
	)	
	public static final RuleResultSpec<LogAction> SOCKS5_DESIRED_DESTINATION_REDIRECT_LOG_ACTION = RULE_RESULT_SPECS.addThenGet(new LogActionRuleResultSpec(
			"socks5.desiredDestinationRedirectLogAction"));
	
	@HelpText(
			doc = "Specifies the socket setting for the inbound socket",
			usage = "socks5.onBind.inboundSocketSetting=SOCKET_SETTING"
	)	
	public static final RuleResultSpec<SocketSetting<Object>> SOCKS5_ON_BIND_INBOUND_SOCKET_SETTING = RULE_RESULT_SPECS.addThenGet(new SocketSettingRuleResultSpec(
			"socks5.onBind.inboundSocketSetting"));
	
	@HelpText(
			doc = "Specifies the socket setting for the listen socket",
			usage = "socks5.onBind.listenSocketSetting=SOCKET_SETTING"
	)	
	public static final RuleResultSpec<SocketSetting<Object>> SOCKS5_ON_BIND_LISTEN_SOCKET_SETTING = RULE_RESULT_SPECS.addThenGet(new SocketSettingRuleResultSpec(
			"socks5.onBind.listenSocketSetting"));
	
	@HelpText(
			doc = "Specifies the buffer size in bytes for relaying the data", 
			usage = "socks5.onBind.relayBufferSize=INTEGER_BETWEEN_1_AND_2147483647"
	)	
	public static final RuleResultSpec<PositiveInteger> SOCKS5_ON_BIND_RELAY_BUFFER_SIZE = RULE_RESULT_SPECS.addThenGet(new PositiveIntegerRuleResultSpec(
			"socks5.onBind.relayBufferSize"));
	
	@HelpText(
			doc = "Specifies the timeout in milliseconds on relaying no data", 
			usage = "socks5.onBind.relayIdleTimeout=INTEGER_BETWEEN_1_AND_2147483647"
	)	
	public static final RuleResultSpec<PositiveInteger> SOCKS5_ON_BIND_RELAY_IDLE_TIMEOUT = RULE_RESULT_SPECS.addThenGet(new PositiveIntegerRuleResultSpec(
			"socks5.onBind.relayIdleTimeout"));

	@HelpText(
			doc = "Specifies the boolean value to indicate if the "
					+ "server-facing socket is to be prepared before "
					+ "connecting (involves applying the specified socket "
					+ "settings, resolving the target host name, and setting "
					+ "the specified timeout on waiting to connect)", 
			usage = "socks5.onConnect.prepareServerFacingSocket=true|false"
	)	
	public static final RuleResultSpec<Boolean> SOCKS5_ON_CONNECT_PREPARE_SERVER_FACING_SOCKET = RULE_RESULT_SPECS.addThenGet(new BooleanRuleResultSpec(
			"socks5.onConnect.prepareServerFacingSocket"));

	@HelpText(
			doc = "Specifies the buffer size in bytes for relaying the data", 
			usage = "socks5.onConnect.relayBufferSize=INTEGER_BETWEEN_1_AND_2147483647"
	)	
	public static final RuleResultSpec<PositiveInteger> SOCKS5_ON_CONNECT_RELAY_BUFFER_SIZE = RULE_RESULT_SPECS.addThenGet(new PositiveIntegerRuleResultSpec(
			"socks5.onConnect.relayBufferSize"));
	
	@HelpText(
			doc = "Specifies the timeout in milliseconds on relaying no data", 
			usage = "socks5.onConnect.relayIdleTimeout=INTEGER_BETWEEN_1_AND_2147483647"
	)	
	public static final RuleResultSpec<PositiveInteger> SOCKS5_ON_CONNECT_RELAY_IDLE_TIMEOUT = RULE_RESULT_SPECS.addThenGet(new PositiveIntegerRuleResultSpec(
			"socks5.onConnect.relayIdleTimeout"));
	
	@HelpText(
			doc = "Specifies the binding host name or address for the "
					+ "server-facing socket", 
			usage = "socks5.onConnect.serverFacingBindHost=HOST"
	)	
	public static final RuleResultSpec<Host> SOCKS5_ON_CONNECT_SERVER_FACING_BIND_HOST = RULE_RESULT_SPECS.addThenGet(new HostRuleResultSpec(
			"socks5.onConnect.serverFacingBindHost"));
	
	@HelpText(
			doc = "Specifies the timeout in milliseconds on waiting for the "
					+ "server-facing socket to connect", 
			usage = "socks5.onConnect.serverFacingConnectTimeout=INTEGER_BETWEEN_1_AND_2147483647"
	)	
	public static final RuleResultSpec<PositiveInteger> SOCKS5_ON_CONNECT_SERVER_FACING_CONNECT_TIMEOUT = RULE_RESULT_SPECS.addThenGet(new PositiveIntegerRuleResultSpec(
			"socks5.onConnect.serverFacingConnectTimeout"));
	
	@HelpText(
			doc = "Specifies the socket setting for the server-facing socket",
			usage = "socks5.onConnect.serverFacingSocketSetting=SOCKET_SETTING"
	)	
	public static final RuleResultSpec<SocketSetting<Object>> SOCKS5_ON_CONNECT_SERVER_FACING_SOCKET_SETTING = RULE_RESULT_SPECS.addThenGet(new SocketSettingRuleResultSpec(
			"socks5.onConnect.serverFacingSocketSetting"));

	@HelpText(
			doc = "Specifies the binding host name or address for the "
					+ "client-facing UDP socket", 
			usage = "socks5.onUdpAssociate.clientFacingBindHost=HOST"
	)
	public static final RuleResultSpec<Host> SOCKS5_ON_UDP_ASSOCIATE_CLIENT_FACING_BIND_HOST = RULE_RESULT_SPECS.addThenGet(new HostRuleResultSpec(
			"socks5.onUdpAssociate.clientFacingBindHost"));
	
	@HelpText(
			doc = "Specifies the socket setting for the client-facing UDP "
					+ "socket",
			usage = "socks5.onUdpAssociate.clientFacingSocketSetting=SOCKET_SETTING"
	)	
	public static final RuleResultSpec<SocketSetting<Object>> SOCKS5_ON_UDP_ASSOCIATE_CLIENT_FACING_SOCKET_SETTING = RULE_RESULT_SPECS.addThenGet(new SocketSettingRuleResultSpec(
			"socks5.onUdpAssociate.clientFacingSocketSetting"));
	
	@HelpText(
			doc = "Specifies the binding host name or address for the "
					+ "peer-facing UDP socket", 
			usage = "socks5.onUdpAssociate.peerFacingBindHost=HOST"
	)	
	public static final RuleResultSpec<Host> SOCKS5_ON_UDP_ASSOCIATE_PEER_FACING_BIND_HOST = RULE_RESULT_SPECS.addThenGet(new HostRuleResultSpec(
			"socks5.onUdpAssociate.peerFacingBindHost"));
	
	@HelpText(
			doc = "Specifies the socket setting for the peer-facing UDP "
					+ "socket",
			usage = "socks5.onUdpAssociate.peerFacingSocketSetting=SOCKET_SETTING"
	)	
	public static final RuleResultSpec<SocketSetting<Object>> SOCKS5_ON_UDP_ASSOCIATE_PEER_FACING_SOCKET_SETTING = RULE_RESULT_SPECS.addThenGet(new SocketSettingRuleResultSpec(
			"socks5.onUdpAssociate.peerFacingSocketSetting"));
	
	@HelpText(
			doc = "Specifies the buffer size in bytes for relaying the data", 
			usage = "socks5.onUdpAssociate.relayBufferSize=INTEGER_BETWEEN_1_AND_2147483647"
	)	
	public static final RuleResultSpec<PositiveInteger> SOCKS5_ON_UDP_ASSOCIATE_RELAY_BUFFER_SIZE = RULE_RESULT_SPECS.addThenGet(new PositiveIntegerRuleResultSpec(
			"socks5.onUdpAssociate.relayBufferSize"));
	
	@HelpText(
			doc = "Specifies the timeout in milliseconds on relaying no data", 
			usage = "socks5.onUdpAssociate.relayIdleTimeout=INTEGER_BETWEEN_1_AND_2147483647"
	)	
	public static final RuleResultSpec<PositiveInteger> SOCKS5_ON_UDP_ASSOCIATE_RELAY_IDLE_TIMEOUT = RULE_RESULT_SPECS.addThenGet(new PositiveIntegerRuleResultSpec(
			"socks5.onUdpAssociate.relayIdleTimeout"));
	
	public static List<RuleResultSpec<Object>> values() {
		return RULE_RESULT_SPECS.toList();
	}
	
	public static Map<String, RuleResultSpec<Object>> valuesMap() {
		return RULE_RESULT_SPECS.toMap();
	}
	
	private Socks5RuleResultSpecConstants() { }

}
