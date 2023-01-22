package com.github.jh3nd3rs0n.jargyle.common.throwable;

public final class ThrowableHelper {
	
	public static boolean isOrHasInstanceOf(
			final Throwable t, final Class<? extends Throwable> cls) {
		if (cls.isInstance(t)) {
			return true;
		} else {
			Throwable cause = t.getCause();
			if (cause != null && isOrHasInstanceOf(cause, cls)) {
				return true;
			}
		}
		return false;
	}
	
	private ThrowableHelper() { }

}
