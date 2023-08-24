package com.github.jh3nd3rs0n.jargyle.internal.regex;

public final class RegexHelper {

	public static String getRegexWithInputBoundaries(final String regex) {
		return String.format("\\A%s\\z", regex);
	}
	
	private RegexHelper() { }
	
}
