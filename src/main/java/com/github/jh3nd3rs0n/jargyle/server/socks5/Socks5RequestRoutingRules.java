package com.github.jh3nd3rs0n.jargyle.server.socks5;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import com.github.jh3nd3rs0n.jargyle.server.RoutingRules;

public final class Socks5RequestRoutingRules 
	extends RoutingRules<Socks5RequestRoutingRule> {

	private static Socks5RequestRoutingRules DEFAULT_INSTANCE = 
			new Socks5RequestRoutingRules(Collections.emptyList());
	
	public static Socks5RequestRoutingRules getDefault() {
		return DEFAULT_INSTANCE;
	}
	
	public static Socks5RequestRoutingRules newInstance(
			final List<Socks5RequestRoutingRule> socks5RequestRoutingRules) {
		return new Socks5RequestRoutingRules(socks5RequestRoutingRules);
	}
	
	public static Socks5RequestRoutingRules newInstance(
			final Socks5RequestRoutingRule... socks5RequestRoutingRules) {
		return newInstance(Arrays.asList(socks5RequestRoutingRules));
	}
	
	public static Socks5RequestRoutingRules newInstance(final String s) {
		return newInstance(Socks5RequestRoutingRule.newInstances(s));
	}
	
	private Socks5RequestRoutingRules(
			final List<Socks5RequestRoutingRule> socks5RequestRoutingRules) {
		super(socks5RequestRoutingRules);
	}
	
}
