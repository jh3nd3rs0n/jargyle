package jargyle.logging;

public final class LoggerHelper {

	public static String objectMessage(final Object obj, final String message) {
		return String.format("%s: %s", obj, message);
	}
	
	private LoggerHelper() { }
	
}
