package com.github.jh3nd3rs0n.jargyle.server.socks5;

import java.util.Arrays;
import java.util.List;

import com.github.jh3nd3rs0n.jargyle.server.Rules;
import com.github.jh3nd3rs0n.jargyle.transport.socks5.Socks5Request;

public final class Socks5RequestRules extends Rules<Socks5RequestRule> {

	private static final Socks5RequestRules DEFAULT_INSTANCE = 
			new Socks5RequestRules(Arrays.asList(
					Socks5RequestRule.getDefault()));
	
	public static Socks5RequestRules getDefault() {
		return DEFAULT_INSTANCE;
	}
	
	public static Socks5RequestRules newInstance(
			final List<Socks5RequestRule> socks5ReqRules) {
		return new Socks5RequestRules(socks5ReqRules);
	}
	
	public static Socks5RequestRules newInstance(
			final Socks5RequestRule... socks5ReqRules) {
		return newInstance(Arrays.asList(socks5ReqRules));
	}
	
	public static Socks5RequestRules newInstance(final String s) {
		return newInstance(Socks5RequestRule.newInstances(s));
	}
	
	private Socks5RequestRules(final List<Socks5RequestRule> socks5ReqRules) {
		super(socks5ReqRules);
	}
	
	public Socks5RequestRule anyAppliesTo(
			final String clientAddress,
			final MethodSubnegotiationResults methSubnegotiationResults,
			final Socks5Request socks5Req) {
		for (Socks5RequestRule socks5RequestRule : this.toList()) {
			if (socks5RequestRule.appliesTo(
					clientAddress, methSubnegotiationResults, socks5Req)) {
				return socks5RequestRule;
			}
		}
		String user = methSubnegotiationResults.getUser();
		String possibleUser = (user != null) ? 
				String.format(" (%s)", user) : "";
		throw new IllegalArgumentException(String.format(
				"SOCKS5 request from %s%s does not apply to any rule. SOCKS5 "
				+ "request: %s",
				clientAddress,
				possibleUser,
				socks5Req));
	}

}
