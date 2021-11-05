package com.github.jh3nd3rs0n.jargyle.server.socks5;

import java.util.Arrays;
import java.util.List;

import com.github.jh3nd3rs0n.jargyle.server.Rules;
import com.github.jh3nd3rs0n.jargyle.transport.socks5.Socks5Reply;
import com.github.jh3nd3rs0n.jargyle.transport.socks5.Socks5Request;

public class Socks5ReplyRules extends Rules<Socks5ReplyRule> {

	private static final Socks5ReplyRules DEFAULT_INSTANCE = 
			new Socks5ReplyRules(Arrays.asList(
					Socks5ReplyRule.getDefault()));
	
	public static Socks5ReplyRules getDefault() {
		return DEFAULT_INSTANCE;
	}
	
	public static Socks5ReplyRules newInstance(
			final List<Socks5ReplyRule> socks5RepRules) {
		return new Socks5ReplyRules(socks5RepRules);
	}
	
	public static Socks5ReplyRules newInstance(
			final Socks5ReplyRule... socks5RepRules) {
		return newInstance(Arrays.asList(socks5RepRules));
	}
	
	public static Socks5ReplyRules newInstance(final String s) {
		return newInstance(Socks5ReplyRule.newInstances(s));
	}
	
	private Socks5ReplyRules(final List<Socks5ReplyRule> socks5RepRules) {
		super(socks5RepRules);
	}
	
	public Socks5ReplyRule anyAppliesTo(
			final String clientAddress,
			final MethodSubnegotiationResults methSubnegotiationResults,
			final Socks5Request socks5Req,
			final Socks5Reply socks5Rep) {
		for (Socks5ReplyRule socks5ReplyRule : this.toList()) {
			if (socks5ReplyRule.appliesTo(
					clientAddress, 
					methSubnegotiationResults, 
					socks5Req, 
					socks5Rep)) {
				return socks5ReplyRule;
			}
		}
		String user = methSubnegotiationResults.getUser();
		String possibleUser = (user != null) ? 
				String.format(" (%s)", user) : "";
		throw new IllegalArgumentException(String.format(
				"SOCKS5 reply to %s%s does not apply to any rule. SOCKS5 "
				+ "request: %s. SOCKS reply: %s",
				clientAddress,
				possibleUser,
				socks5Req,
				socks5Rep));
	}
	
}
