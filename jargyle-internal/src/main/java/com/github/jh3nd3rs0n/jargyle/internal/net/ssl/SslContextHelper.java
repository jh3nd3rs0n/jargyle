package com.github.jh3nd3rs0n.jargyle.internal.net.ssl;

import javax.net.ssl.KeyManager;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

/**
 * Helper class for {@code SSLContext}s.
 */
public final class SslContextHelper {

    /**
     * Prevents the construction of unnecessary instances.
     */
    private SslContextHelper() {
    }

    /**
     * Returns a {@code SSLContext} that implements the provided protocol and
     * is initialized with the provided array of {@code KeyManager}s containing
     * authentication keys and the provided array of {@code TrustManager}s
     * containing peer authentication trust decisions.
     *
     * @param protocol      the provided protocol
     * @param keyManagers   the provided array of {@code KeyManager}s containing
     *                      authentication keys (can be {@code null})
     * @param trustManagers the provided array of {@code TrustManager}s
     *                      containing peer authentication trust decisions
     *                      (can be {@code null})
     * @return a {@code SSLContext} that implements the provided protocol and
     * is initialized with the provided array of {@code KeyManager}s containing
     * authentication keys and the provided array of {@code TrustManager}s
     * containing peer authentication trust decisions
     * @throws NoSuchAlgorithmException if the provided protocol is not
     *                                  supported
     * @throws KeyManagementException   if initialization of the
     *                                  {@code SSLContext} fails.
     */
    public static SSLContext getSslContext(
            final String protocol,
            final KeyManager[] keyManagers,
            final TrustManager[] trustManagers)
            throws NoSuchAlgorithmException, KeyManagementException {
        SSLContext context = SSLContext.getInstance(protocol);
        context.init(keyManagers, trustManagers, new SecureRandom());
        return context;
    }

}
