package com.github.jh3nd3rs0n.test.help.gss.kerberos;

/**
 * Thrown when an error occurs setting up or tearing down the
 * {@code KerberosEnvironment}.
 */
public final class KerberosEnvironmentException extends Exception {

    /**
     * Constructs a {@code KerberosEnvironmentException} with the provided
     * cause.
     *
     * @param cause the provided cause
     */
    public KerberosEnvironmentException(final Throwable cause) {
        super(cause);
    }

}
