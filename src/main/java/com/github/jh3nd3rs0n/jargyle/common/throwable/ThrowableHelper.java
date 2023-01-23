package com.github.jh3nd3rs0n.jargyle.common.throwable;

public final class ThrowableHelper {
	
	public static boolean isOrHasInstanceOf(
			final Throwable t, final Class<? extends Throwable> cls) {
		if (cls.isInstance(t)) {
			return true;
		}
		Throwable cause = t.getCause();
		return cause != null && isOrHasInstanceOf(cause, cls);
	}
	
	private ThrowableHelper() { }

}
