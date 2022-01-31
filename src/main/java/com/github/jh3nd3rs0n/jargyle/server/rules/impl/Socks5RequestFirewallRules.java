package com.github.jh3nd3rs0n.jargyle.server.rules.impl;

import java.util.Arrays;
import java.util.List;

public final class Socks5RequestFirewallRules extends FirewallRules<Socks5RequestFirewallRule> {

	private static final Socks5RequestFirewallRules DEFAULT_INSTANCE = 
			new Socks5RequestFirewallRules(Arrays.asList(
					Socks5RequestFirewallRule.getDefault()));
	
	public static Socks5RequestFirewallRules getDefault() {
		return DEFAULT_INSTANCE;
	}
	
	public static Socks5RequestFirewallRules newInstance(
			final List<Socks5RequestFirewallRule> socks5ReqRules) {
		return new Socks5RequestFirewallRules(socks5ReqRules);
	}
	
	public static Socks5RequestFirewallRules newInstance(
			final Socks5RequestFirewallRule... socks5ReqRules) {
		return newInstance(Arrays.asList(socks5ReqRules));
	}
	
	public static Socks5RequestFirewallRules newInstance(final String s) {
		return newInstance(Socks5RequestFirewallRule.newInstances(s));
	}
	
	private Socks5RequestFirewallRules(final List<Socks5RequestFirewallRule> socks5ReqRules) {
		super(socks5ReqRules);
	}

}
