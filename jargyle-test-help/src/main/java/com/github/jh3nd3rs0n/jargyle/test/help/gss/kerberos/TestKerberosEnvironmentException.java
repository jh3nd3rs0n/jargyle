package com.github.jh3nd3rs0n.jargyle.test.help.gss.kerberos;

/**
 * Thrown when an error occurs setting up or tearing down the
 * {@code TestKerberosEnvironment}.
 */
public final class TestKerberosEnvironmentException extends Exception {

    /**
     * Constructs a {@code TestKerberosEnvironmentException} with the provided
     * cause.
     *
     * @param cause the provided cause
     */
    public TestKerberosEnvironmentException(final Throwable cause) {
        super(cause);
    }

}
