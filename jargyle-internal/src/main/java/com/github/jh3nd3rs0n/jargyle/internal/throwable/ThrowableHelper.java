package com.github.jh3nd3rs0n.jargyle.internal.throwable;

import java.util.Objects;

/**
 * Helper class for {@code Throwable}s.
 */
public final class ThrowableHelper {

    /**
     * Prevents the construction of unnecessary instances.
     */
    private ThrowableHelper() {
    }

    /**
     * Returns the {@code boolean} value indicating if the provided
     * {@code Throwable} is an instance of the provided {@code Class} or has a
     * {@code Throwable} of the provided {@code Class}.
     *
     * @param t   the provided {@code Throwable}
     * @param cls the provided {@code Class}
     * @return the {@code boolean} value indicating if the provided
     * {@code Throwable} is an instance of the provided {@code Class} or has a
     * {@code Throwable} of the provided {@code Class}
     */
    public static boolean isOrHasInstanceOf(
            final Throwable t, final Class<? extends Throwable> cls) {
        Objects.requireNonNull(t);
        Objects.requireNonNull(cls);
        if (cls.isInstance(t)) {
            return true;
        }
        Throwable cause = t.getCause();
        return cause != null && isOrHasInstanceOf(cause, cls);
    }

}
