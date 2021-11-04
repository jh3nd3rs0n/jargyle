package com.github.jh3nd3rs0n.jargyle.server.socks5;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public final class Socks5UdpRules {

	private static final Socks5UdpRules DEFAULT_INSTANCE = 
			new Socks5UdpRules(Arrays.asList(Socks5UdpRule.getDefault()));
	
	public static Socks5UdpRules getDefault() {
		return DEFAULT_INSTANCE;
	}
	
	public static Socks5UdpRules newInstance(
			final List<Socks5UdpRule> socks5RepRules) {
		return new Socks5UdpRules(socks5RepRules);
	}
	
	public static Socks5UdpRules newInstance(
			final Socks5UdpRule... socks5RepRules) {
		return newInstance(Arrays.asList(socks5RepRules));
	}
	
	public static Socks5UdpRules newInstance(final String s) {
		return newInstance(Socks5UdpRule.newInstances(s));
	}
	
	private final List<Socks5UdpRule> socks5UdpRules;
	
	private Socks5UdpRules(final List<Socks5UdpRule> socks5RepRules) {
		this.socks5UdpRules = new ArrayList<Socks5UdpRule>(socks5RepRules);
	}
	
	public Socks5UdpRule anyAppliesTo(
			final String clientAddress,
			final MethodSubnegotiationResults methSubnegotiationResults,
			final String peerAddress) {
		for (Socks5UdpRule socks5UdpRule : this.socks5UdpRules) {
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
		Socks5UdpRules other = (Socks5UdpRules) obj;
		if (this.socks5UdpRules == null) {
			if (other.socks5UdpRules != null) {
				return false;
			}
		} else if (!this.socks5UdpRules.equals(
				other.socks5UdpRules)) {
			return false;
		}
		return true;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((this.socks5UdpRules == null) ? 
				0 : this.socks5UdpRules.hashCode());
		return result;
	}

	public List<Socks5UdpRule> toList() {
		return Collections.unmodifiableList(this.socks5UdpRules);
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		for (Iterator<Socks5UdpRule> iterator = 
				this.socks5UdpRules.iterator();
				iterator.hasNext();) {
			Socks5UdpRule socks5UdpRule = iterator.next();
			builder.append(socks5UdpRule);
			if (iterator.hasNext()) {
				builder.append(' ');
			}
		}
		return builder.toString();
	}

}
