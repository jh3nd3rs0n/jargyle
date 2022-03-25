package com.github.jh3nd3rs0n.jargyle.internal.logging;

public final class ObjectLogMessageHelper {

	public static String objectLogMessage(
			final Object obj, final String message, final Object... args) {
		StringBuilder sb = new StringBuilder();
		sb.append(obj);
		sb.append(": ");
		sb.append(String.format(message, args));
		return sb.toString();
	}
	
	private ObjectLogMessageHelper() { }
	
}
