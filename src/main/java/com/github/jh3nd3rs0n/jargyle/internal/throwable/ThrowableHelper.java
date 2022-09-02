package com.github.jh3nd3rs0n.jargyle.internal.throwable;

public final class ThrowableHelper {

	public static <T extends Throwable> T getRecentCause(
			final Throwable t, final Class<T> recentCauseClass) {
		Throwable cause = t.getCause();
		if (cause == null) {
			return null;
		}
		if (recentCauseClass.isInstance(cause)) {
			@SuppressWarnings("unchecked")
			T c = (T) cause;
			return c;
		}
		return getRecentCause(cause, recentCauseClass);
	}
	
	private ThrowableHelper() { }

}
