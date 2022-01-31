package com.github.jh3nd3rs0n.jargyle.server.rules.impl;

import java.util.Arrays;
import java.util.List;

public class Socks5ReplyFirewallRules extends FirewallRules<Socks5ReplyFirewallRule> {

	private static final Socks5ReplyFirewallRules DEFAULT_INSTANCE = 
			new Socks5ReplyFirewallRules(Arrays.asList(
					Socks5ReplyFirewallRule.getDefault()));
	
	public static Socks5ReplyFirewallRules getDefault() {
		return DEFAULT_INSTANCE;
	}
	
	public static Socks5ReplyFirewallRules newInstance(
			final List<Socks5ReplyFirewallRule> socks5RepFirewallRules) {
		return new Socks5ReplyFirewallRules(socks5RepFirewallRules);
	}
	
	public static Socks5ReplyFirewallRules newInstance(
			final Socks5ReplyFirewallRule... socks5RepFirewallRules) {
		return newInstance(Arrays.asList(socks5RepFirewallRules));
	}
	
	public static Socks5ReplyFirewallRules newInstance(final String s) {
		return newInstance(Socks5ReplyFirewallRule.newInstances(s));
	}
	
	private Socks5ReplyFirewallRules(
			final List<Socks5ReplyFirewallRule> socks5RepFirewallRules) {
		super(socks5RepFirewallRules);
	}
	
}
