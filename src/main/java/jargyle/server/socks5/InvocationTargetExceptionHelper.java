package jargyle.server.socks5;

import java.lang.reflect.InvocationTargetException;

final class InvocationTargetExceptionHelper {

	public static String toString(final InvocationTargetException e) {
		StringBuilder sb = new StringBuilder();
		sb.append(e.getClass().getName());
		String message = e.getMessage();
		if (message != null) {
			sb.append(": ");
			sb.append(message);
			return sb.toString();
		}
		Throwable cause = e.getCause();
		if (cause != null) {
			sb.append(": ");
			sb.append(cause);
			return sb.toString();
		}
		return sb.toString();
	}
	
	private InvocationTargetExceptionHelper() { }
	
}
