package com.github.jh3nd3rs0n.jargyle.server.internal.addressrange.impl;

import com.github.jh3nd3rs0n.jargyle.common.net.Host;
import com.github.jh3nd3rs0n.jargyle.common.net.HostName;
import com.github.jh3nd3rs0n.jargyle.server.AddressRange;

public final class DomainnameRange extends AddressRange {

	private static final String REGEX_PREFIX = "regex:";

	public static DomainnameRange newInstanceFrom(final String s) {
		String expression = s;
		boolean hasRegularExpression = false;
		if (s.startsWith(REGEX_PREFIX)) {
			expression = s.substring(REGEX_PREFIX.length());
			hasRegularExpression = true;
		} else {
			if (!(Host.newInstance(s) instanceof HostName)) {
				throw new IllegalArgumentException(
						"domainname range must be either of the following "
						+ "formats: DOMAINNAME, regex:REGULAR_EXPRESSION");
			}
		}
		return new DomainnameRange(expression, hasRegularExpression, s);
	}
	
	private final String expression;
	private final boolean hasRegularExpression;
	private final String string;
	
	private DomainnameRange(
			final String expr, final boolean hasRegex, final String str) {
		this.expression = expr;
		this.hasRegularExpression = hasRegex;
		this.string = str;		
	}
	
	@Override
	public boolean has(final String address) {
		if (!(Host.newInstance(address) instanceof HostName)) {
			return false;
		}
		if (this.hasRegularExpression) {
			return address.matches(this.expression);
		}
		return address.equals(this.expression);
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
		DomainnameRange other = (DomainnameRange) obj;
		if (this.string == null) {
			if (other.string != null) {
				return false;
			}
		} else if (!this.string.equals(other.string)) {
			return false;
		}
		return true;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((this.string == null) ? 
				0 : this.string.hashCode());
		return result;
	}

	@Override
	public String toString() {
		return this.string;
	}

}
