package com.github.jh3nd3rs0n.jargyle.server.socks5;

import java.util.Arrays;
import java.util.List;

import com.github.jh3nd3rs0n.jargyle.server.Rules;

public final class Socks5UdpRules extends Rules<Socks5UdpRule> {

	private static final Socks5UdpRules DEFAULT_INSTANCE = 
			new Socks5UdpRules(Arrays.asList(Socks5UdpRule.getDefault()));
	
	public static Socks5UdpRules getDefault() {
		return DEFAULT_INSTANCE;
	}
	
	public static Socks5UdpRules newInstance(
			final List<Socks5UdpRule> socks5UdpRules) {
		return new Socks5UdpRules(socks5UdpRules);
	}
	
	public static Socks5UdpRules newInstance(
			final Socks5UdpRule... socks5UdpRules) {
		return newInstance(Arrays.asList(socks5UdpRules));
	}
	
	public static Socks5UdpRules newInstance(final String s) {
		return newInstance(Socks5UdpRule.newInstances(s));
	}
	
	private Socks5UdpRules(final List<Socks5UdpRule> socks5UdpRules) {
		super(socks5UdpRules);
	}
	
	public Socks5UdpRule anyAppliesTo(
			final String clientAddress,
			final MethodSubnegotiationResults methSubnegotiationResults,
			final String peerAddress) {
		for (Socks5UdpRule socks5UdpRule : this.toList()) {
			if (socks5UdpRule.appliesTo(
					clientAddress, methSubnegotiationResults, peerAddress)) {
				return socks5UdpRule;
			}
		}
		String user = methSubnegotiationResults.getUser();
		String possibleUser = (user != null) ? 
				String.format(" (%s)", user) : "";
		throw new IllegalArgumentException(String.format(
				"traffic between client %s%s and peer %s does not apply to any "
				+ "rule",
				clientAddress,
				possibleUser,
				peerAddress));
	}

}
