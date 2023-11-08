package com.github.jh3nd3rs0n.jargyle.server.internal.addressrange.impl;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.github.jh3nd3rs0n.jargyle.internal.net.InetAddressHelper;
import com.github.jh3nd3rs0n.jargyle.internal.net.InetAddressRegexConstants;
import com.github.jh3nd3rs0n.jargyle.internal.regex.RegexHelper;
import com.github.jh3nd3rs0n.jargyle.server.AddressRange;

public final class DomainnameRange extends AddressRange {

	private static final String DOMAINNAME_REGEX =
			InetAddressRegexConstants.DOMAINNAME_REGEX;
	
	private static final String DOMAINNAME_REGEX_REGEX = "regex:(?<regex>.*)";
	
	public static boolean isDomainnameRange(final String s) {
		return s.matches(RegexHelper.getRegexWithInputBoundaries(
				DOMAINNAME_REGEX))
				|| s.matches(RegexHelper.getRegexWithInputBoundaries(
						DOMAINNAME_REGEX_REGEX));
	}
	
	public static DomainnameRange newInstance(final String s) {
		String message = "domainname range must be either of the following "
				+ "formats: DOMAINNAME, regex:REGULAR_EXPRESSION";
		if (!isDomainnameRange(s)) {
			throw new IllegalArgumentException(message);
		}
		String expression = s; 
		boolean hasRegularExpression = false;
		Pattern pattern = Pattern.compile(DOMAINNAME_REGEX_REGEX);
		Matcher matcher = pattern.matcher(s);
		if (matcher.matches()) {
			expression = matcher.group("regex");
			hasRegularExpression = true;
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
	public boolean contains(final String address) {
		if (!InetAddressHelper.isDomainname(address)) {
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
