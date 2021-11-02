package com.github.jh3nd3rs0n.jargyle.server.socks5;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import com.github.jh3nd3rs0n.jargyle.transport.socks5.Socks5Request;

public final class Socks5RequestRules {

	private static final Socks5RequestRules DEFAULT_INSTANCE = 
			new Socks5RequestRules(Arrays.asList(Socks5RequestRule.getDefault()));
	
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
	
	private final List<Socks5RequestRule> socks5RequestRules;
	
	private Socks5RequestRules(
			final List<Socks5RequestRule> socks5ReqRules) {
		this.socks5RequestRules = new ArrayList<Socks5RequestRule>(
				socks5ReqRules);
	}
	
	public Socks5RequestRule anyAppliesTo(
			final String sourceAddress,
			final MethodSubnegotiationResults methSubnegotiationResults,
			final Socks5Request socks5Req) {
		for (Socks5RequestRule socks5RequestRule : this.socks5RequestRules) {
			if (socks5RequestRule.appliesTo(
					sourceAddress, methSubnegotiationResults, socks5Req)) {
				return socks5RequestRule;
			}
		}
		String user = methSubnegotiationResults.getUser();
		String possibleUser = (user != null) ? 
				String.format(" (%s)", user) : "";
		throw new IllegalArgumentException(String.format(
				"SOCKS5 request from %s%s does not apply to any rule. SOCKS5 "
				+ "request: %s",
				sourceAddress,
				possibleUser,
				socks5Req));
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
		Socks5RequestRules other = (Socks5RequestRules) obj;
		if (this.socks5RequestRules == null) {
			if (other.socks5RequestRules != null) {
				return false;
			}
		} else if (!this.socks5RequestRules.equals(other.socks5RequestRules)) {
			return false;
		}
		return true;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((this.socks5RequestRules == null) ? 
				0 : this.socks5RequestRules.hashCode());
		return result;
	}

	public List<Socks5RequestRule> toList() {
		return Collections.unmodifiableList(this.socks5RequestRules);
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		for (Iterator<Socks5RequestRule> iterator = this.socks5RequestRules.iterator();
				iterator.hasNext();) {
			Socks5RequestRule socks5RequestRule = iterator.next();
			builder.append(socks5RequestRule);
			if (iterator.hasNext()) {
				builder.append(' ');
			}
		}
		return builder.toString();
	}

}
