package com.github.jh3nd3rs0n.jargyle.client;

import java.util.Objects;

/**
 * An implementation of {@code NetObjectFactory} that creates networking
 * objects whose traffic would be routed through the SOCKS server.
 */
public abstract class SocksNetObjectFactory extends NetObjectFactory {

    /**
     * The {@code SocksClient} of this {@code SocksNetObjectFactory}.
     */
    private final SocksClient socksClient;

    /**
     * Constructs a {@code SocksNetObjectFactory} with the provided
     * {@code SocksClient}.
     *
     * @param client the provided {@code SocksClient}
     */
    public SocksNetObjectFactory(final SocksClient client) {
        this.socksClient = Objects.requireNonNull(client);
    }

    /**
     * Returns a new {@code SocksNetObjectFactory} from the system property
     * {@code socksClient.socksServerUri} and system properties defined by
     * other {@code PropertySpec} constants classes. {@code null} is returned
     * if the system property {@code socksClient.socksServerUri} is not set or
     * the value is invalid.
     *
     * @return a new {@code SocksNetObjectFactory} from the system property
     * {@code socksClient.socksServerUri} and system properties defined by
     * other {@code PropertySpec} constants classes. {@code null} is returned
     * if the system property {@code socksClient.socksServerUri} is not set or
     * the value is invalid.
     */
    public static SocksNetObjectFactory newInstance() {
        SocksClient socksClient = SocksClient.newInstance();
        if (socksClient == null) {
            return null;
        }
        return socksClient.newSocksNetObjectFactory();
    }

    /**
     * Returns the {@code SocksClient} of this {@code SocksNetObjectFactory}.
     *
     * @return the {@code SocksClient} of this {@code SocksNetObjectFactory}
     */
    public final SocksClient getSocksClient() {
        return this.socksClient;
    }

}
