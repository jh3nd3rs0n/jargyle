package com.github.jh3nd3rs0n.jargyle.internal.logging;

public final class ObjectLogMessageHelper {

	public static String objectLogMessage(final Object obj, final String message) {
		return String.format("%s: %s", obj, message);
	}
	
	private ObjectLogMessageHelper() { }
	
}
