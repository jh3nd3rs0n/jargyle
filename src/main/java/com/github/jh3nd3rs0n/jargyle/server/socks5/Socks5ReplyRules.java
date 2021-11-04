package com.github.jh3nd3rs0n.jargyle.server.socks5;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import com.github.jh3nd3rs0n.jargyle.transport.socks5.Socks5Reply;
import com.github.jh3nd3rs0n.jargyle.transport.socks5.Socks5Request;

public class Socks5ReplyRules {

	private static final Socks5ReplyRules DEFAULT_INSTANCE = 
			new Socks5ReplyRules(Arrays.asList(
					Socks5ReplyRule.getDefault()));
	
	public static Socks5ReplyRules getDefault() {
		return DEFAULT_INSTANCE;
	}
	
	public static Socks5ReplyRules newInstance(
			final List<Socks5ReplyRule> socks5ReqRules) {
		return new Socks5ReplyRules(socks5ReqRules);
	}
	
	public static Socks5ReplyRules newInstance(
			final Socks5ReplyRule... socks5ReqRules) {
		return newInstance(Arrays.asList(socks5ReqRules));
	}
	
	public static Socks5ReplyRules newInstance(final String s) {
		return newInstance(Socks5ReplyRule.newInstances(s));
	}
	
	private final List<Socks5ReplyRule> socks5ReplyRules;
	
	private Socks5ReplyRules(
			final List<Socks5ReplyRule> socks5ReqRules) {
		this.socks5ReplyRules = new ArrayList<Socks5ReplyRule>(
				socks5ReqRules);
	}
	
	public Socks5ReplyRule anyAppliesTo(
			final String clientAddress,
			final MethodSubnegotiationResults methSubnegotiationResults,
			final Socks5Request socks5Req,
			final Socks5Reply socks5Rep) {
		for (Socks5ReplyRule socks5ReplyRule : this.socks5ReplyRules) {
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
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (this.getClass() != obj.getClass()) {
			return false;
		}
		Socks5ReplyRules other = (Socks5ReplyRules) obj;
		if (this.socks5ReplyRules == null) {
			if (other.socks5ReplyRules != null) {
				return false;
			}
		} else if (!this.socks5ReplyRules.equals(other.socks5ReplyRules)) {
			return false;
		}
		return true;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((this.socks5ReplyRules == null) ? 
				0 : this.socks5ReplyRules.hashCode());
		return result;
	}

	public List<Socks5ReplyRule> toList() {
		return Collections.unmodifiableList(this.socks5ReplyRules);
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		for (Iterator<Socks5ReplyRule> iterator = this.socks5ReplyRules.iterator();
				iterator.hasNext();) {
			Socks5ReplyRule socks5ReplyRule = iterator.next();
			builder.append(socks5ReplyRule);
			if (iterator.hasNext()) {
				builder.append(' ');
			}
		}
		return builder.toString();
	}
	
}
