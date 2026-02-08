package com.github.jh3nd3rs0n.jargyle.client;

import java.util.Objects;

/**
 * An implementation of {@code SocketFactory} that creates {@code Socket}s
 * whose traffic would be routed through the SOCKS server.
 */
public abstract class SocksSocketFactory extends SocketFactory {

    /**
     * The {@code SocksClient} of this {@code SocksSocketFactory}.
     */
    private final SocksClient socksClient;

    /**
     * Constructs a {@code SocksSocketFactory} with the provided
     * {@code SocksClient}.
     *
     * @param client the provided {@code SocksClient}
     */
    public SocksSocketFactory(final SocksClient client) {
        this.socksClient = Objects.requireNonNull(client);
    }

    /**
     * Returns the {@code SocksClient} of this {@code SocksSocketFactory}.
     *
     * @return the {@code SocksClient} of this {@code SocksSocketFactory}
     */
    public final SocksClient getSocksClient() {
        return this.socksClient;
    }

    /**
     * Returns a new {@code SocksSocketFactory} from the system property
     * {@code socksClient.socksServerUri} and system properties defined by
     * other {@code PropertySpec} constants classes. {@code null} is returned
     * if the system property {@code socksClient.socksServerUri} is not set or
     * the value is invalid.
     *
     * @return a new {@code SocksSocketFactory} from the system property
     * {@code socksClient.socksServerUri} and system properties defined by
     * other {@code PropertySpec} constants classes. {@code null} is returned
     * if the system property {@code socksClient.socksServerUri} is not set or
     * the value is invalid.
     */
    public static SocksSocketFactory newInstance() {
        SocksClient socksClient = SocksClient.newInstance();
        if (socksClient == null) {
            return null;
        }
        return socksClient.getSocksSocketFactory();
    }

}
