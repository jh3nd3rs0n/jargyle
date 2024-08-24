package com.github.jh3nd3rs0n.jargyle.protocolbase.socks5;

/**
 * Thrown when an error occurs during method sub-negotiation.
 */
public final class MethodSubNegotiationException extends Socks5Exception {

    /**
     * The default serial version UID.
     */
    private static final long serialVersionUID = 1L;

    /**
     * The {@code Method} selected before the error occurred during method
     * sub-negotiation.
     */
    private final Method method;

    /**
     * Constructs a {@code MethodSubNegotiationException} with the provided
     * {@code Method} selected before the error occurred during method
     * sub-negotiation and the provided message.
     *
     * @param meth    the provided {@code Method} selected before the error
     *                occurred during sub-negotiation
     * @param message the provided message
     */
    public MethodSubNegotiationException(
            final Method meth, final String message) {
        super(getMessage(meth, message, null));
        this.method = meth;
    }

    /**
     * Constructs a {@code MethodSubNegotiationException} with the provided
     * {@code Method} selected before the error occurred during method
     * sub-negotiation and the provided cause.
     *
     * @param meth  the provided {@code Method} selected before the error
     *              occurred during sub-negotiation
     * @param cause the provided cause
     */
    public MethodSubNegotiationException(
            final Method meth, final Throwable cause) {
        super(getMessage(meth, null, cause), cause);
        this.method = meth;
    }

    /**
     * Returns a message based on the provided {@code Method} selected before
     * the error occurred during sub-negotiation, the optionally provided
     * message, and the optionally provided cause.
     *
     * @param method  the provided {@code Method} selected before the error
     *                occurred during sub-negotiation
     * @param message the optionally provided message (can be {@code null})
     * @param cause   the optionally provided cause (can be {@code null})
     * @return a message based on the provided {@code Method} selected before
     * the error occurred during sub-negotiation, the optionally provided
     * message, and the optionally provided cause
     */
    private static String getMessage(
            final Method method, final String message, final Throwable cause) {
        StringBuilder sb = new StringBuilder(String.format("method %s", method));
        if (message != null) {
            sb.append(": ");
            sb.append(message);
        }
        if (cause != null) {
            sb.append(": ");
            sb.append(cause);
        }
        return sb.toString();
    }

    /**
     * Returns the {@code Method} selected before the error occurred during
     * sub-negotiation.
     *
     * @return the {@code Method} selected before the error occurred during
     * sub-negotiation
     */
    public Method getMethod() {
        return this.method;
    }

}
