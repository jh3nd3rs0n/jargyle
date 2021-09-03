package com.github.jh3nd3rs0n.jargyle.net.socks.server;

import java.util.Map;

import com.github.jh3nd3rs0n.jargyle.internal.help.HelpText;
import com.github.jh3nd3rs0n.jargyle.net.Host;
import com.github.jh3nd3rs0n.jargyle.net.Port;
import com.github.jh3nd3rs0n.jargyle.net.SocketSettings;
import com.github.jh3nd3rs0n.jargyle.net.socks.server.settingspec.impl.CriteriaSettingSpec;
import com.github.jh3nd3rs0n.jargyle.net.socks.server.settingspec.impl.HostSettingSpec;
import com.github.jh3nd3rs0n.jargyle.net.socks.server.settingspec.impl.NonnegativeIntegerSettingSpec;
import com.github.jh3nd3rs0n.jargyle.net.socks.server.settingspec.impl.PortSettingSpec;
import com.github.jh3nd3rs0n.jargyle.net.socks.server.settingspec.impl.SocketSettingsSettingSpec;
import com.github.jh3nd3rs0n.jargyle.util.Criteria;
import com.github.jh3nd3rs0n.jargyle.util.Criterion;
import com.github.jh3nd3rs0n.jargyle.util.CriterionMethod;
import com.github.jh3nd3rs0n.jargyle.util.NonnegativeInteger;

public final class GeneralSettingSpecConstants {

	private static final SettingSpecs SETTING_SPECS = new SettingSpecs();
	
	@HelpText(
			doc = "The space separated list of allowed client address "
					+ "criteria (default is matches:.*)", 
			usage = "allowedClientAddressCriteria=[equals|matches:VALUE1[ equals|matches:VALUE2[...]]]"
	)
	public static final SettingSpec<Criteria> ALLOWED_CLIENT_ADDRESS_CRITERIA = 
			SETTING_SPECS.add(new CriteriaSettingSpec(
					NewSettingSpecPermission.INSTANCE, 
					"allowedClientAddressCriteria",
					Criteria.newInstance(Criterion.newInstance(CriterionMethod.MATCHES, ".*"))));

	@HelpText(
			doc = "The maximum length of the queue of incoming connections "
					+ "(default is 50)", 
			usage = "backlog=INTEGER_BETWEEN_0_AND_2147483647"
	)
	public static final SettingSpec<NonnegativeInteger> BACKLOG = 
			SETTING_SPECS.add(new NonnegativeIntegerSettingSpec(
					NewSettingSpecPermission.INSTANCE, 
					"backlog",
					NonnegativeInteger.newInstance(50)));
	
	@HelpText(
			doc = "The space separated list of blocked client address criteria", 
			usage = "blockedClientAddressCriteria=[equals|matches:VALUE1[ equals|matches:VALUE2[...]]]"
	)
	public static final SettingSpec<Criteria> BLOCKED_CLIENT_ADDRESS_CRITERIA = 
			SETTING_SPECS.add(new CriteriaSettingSpec(
					NewSettingSpecPermission.INSTANCE, 
					"blockedClientAddressCriteria",
					Criteria.getEmptyInstance()));
	
	@HelpText(
			doc = "The host name or address for the SOCKS server (default is "
					+ "0.0.0.0)", 
			usage = "host=HOST"
	)
	public static final SettingSpec<Host> HOST = 
			SETTING_SPECS.add(new HostSettingSpec(
					NewSettingSpecPermission.INSTANCE, 
					"host",
					Host.getInet4AllZerosInstance()));
	
	@HelpText(
			doc = "The port for the SOCKS server (default is 1080)", 
			usage = "port=INTEGER_BETWEEN_0_AND_65535"
	)
	public static final SettingSpec<Port> PORT = 
			SETTING_SPECS.add(new PortSettingSpec(
					NewSettingSpecPermission.INSTANCE, 
					"port",
					Port.newInstance(1080)));
	
	@HelpText(
			doc = "The space separated list of socket settings for the SOCKS "
					+ "server", 
			usage = "socketSettings=[SOCKET_SETTING1[ SOCKET_SETTING2[...]]]"
	)
	public static final SettingSpec<SocketSettings> SOCKET_SETTINGS = 
			SETTING_SPECS.add(new SocketSettingsSettingSpec(
					NewSettingSpecPermission.INSTANCE, 
					"socketSettings",
					SocketSettings.newInstance()));
	
	public static Map<String, SettingSpec<Object>> valuesMap() {
		return SETTING_SPECS.toMap();
	}
	
	private GeneralSettingSpecConstants() { }
	
}
