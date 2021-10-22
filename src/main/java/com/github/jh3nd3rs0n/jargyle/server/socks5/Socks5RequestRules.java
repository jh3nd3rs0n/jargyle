package com.github.jh3nd3rs0n.jargyle.server.socks5;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import com.github.jh3nd3rs0n.jargyle.transport.socks5.Socks5Request;

public final class Socks5RequestRules {

	private static final Socks5RequestRules EMPTY_INSTANCE = 
			new Socks5RequestRules(Collections.emptyList());
	
	public static Socks5RequestRules getEmptyInstance() {
		return EMPTY_INSTANCE;
	}
	
	public static Socks5RequestRules newInstance(
			final List<Socks5RequestRule> socks5ReqRules) {
		return new Socks5RequestRules(socks5ReqRules);
	}
	
	public static Socks5RequestRules newInstance(
			final Socks5RequestRule... socks5ReqRules) {
		return newInstance(Arrays.asList(socks5ReqRules));
	}
	
	private final List<Socks5RequestRule> socks5RequestRules;
	
	private Socks5RequestRules(
			final List<Socks5RequestRule> socks5ReqRules) {
		this.socks5RequestRules = new ArrayList<Socks5RequestRule>(
				socks5ReqRules);
	}
	
	public Socks5RequestRule anyAppliesTo(
			final String clientAddress, final Socks5Request socks5Req) {
		for (Socks5RequestRule socks5RequestRule : this.socks5RequestRules) {
			if (socks5RequestRule.appliesTo(clientAddress, socks5Req)) {
				return socks5RequestRule;
			}
		}
		return null;
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
		builder.append(this.getClass().getSimpleName())
			.append(" [socks5RequestRules=")
			.append(this.socks5RequestRules)
			.append("]");
		return builder.toString();
	}

}
