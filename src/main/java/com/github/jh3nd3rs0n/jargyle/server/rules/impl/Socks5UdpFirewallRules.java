package com.github.jh3nd3rs0n.jargyle.server.rules.impl;

import java.util.Arrays;
import java.util.List;

public final class Socks5UdpFirewallRules 
	extends FirewallRules<Socks5UdpFirewallRule> {

	private static final Socks5UdpFirewallRules DEFAULT_INSTANCE = 
			new Socks5UdpFirewallRules(Arrays.asList(
					Socks5UdpFirewallRule.getDefault()));
	
	public static Socks5UdpFirewallRules getDefault() {
		return DEFAULT_INSTANCE;
	}
	
	public static Socks5UdpFirewallRules newInstance(
			final List<Socks5UdpFirewallRule> socks5UdpFirewallRules) {
		return new Socks5UdpFirewallRules(socks5UdpFirewallRules);
	}
	
	public static Socks5UdpFirewallRules newInstance(
			final Socks5UdpFirewallRule... socks5UdpFirewallRules) {
		return newInstance(Arrays.asList(socks5UdpFirewallRules));
	}
	
	public static Socks5UdpFirewallRules newInstance(final String s) {
		return newInstance(Socks5UdpFirewallRule.newInstances(s));
	}
	
	private Socks5UdpFirewallRules(
			final List<Socks5UdpFirewallRule> socks5UdpFirewallRules) {
		super(socks5UdpFirewallRules);
	}

}
