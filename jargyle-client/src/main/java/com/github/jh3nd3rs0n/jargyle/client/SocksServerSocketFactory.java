package com.github.jh3nd3rs0n.jargyle.client;

import java.util.Objects;

/**
 * An implementation of {@code ServerSocketFactory} that creates
 * {@code ServerSocket}s whose traffic would be routed through the SOCKS 
 * server.
 */
public abstract class SocksServerSocketFactory extends ServerSocketFactory {

    /**
     * The {@code SocksClient} of this {@code SocksServerSocketFactory}.
     */
    private final SocksClient socksClient;

    /**
     * Constructs a {@code SocksServerSocketFactory} with the provided
     * {@code SocksClient}.
     *
     * @param client the provided {@code SocksClient}
     */
    public SocksServerSocketFactory(final SocksClient client) {
        this.socksClient = Objects.requireNonNull(client);        
    }

    /**
     * Returns the {@code SocksClient} of this
     * {@code SocksServerSocketFactory}.
     *
     * @return the {@code SocksClient} of this
     * {@code SocksServerSocketFactory}
     */
    public final SocksClient getSocksClient() {
        return this.socksClient;
    }

    /**
     * Returns a new {@code SocksServerSocketFactory} from the system property
     * {@code socksClient.socksServerUri} and system properties defined by
     * other {@code PropertySpec} constants classes. {@code null} is returned
     * if the system property {@code socksClient.socksServerUri} is not set or
     * the value is invalid.
     *
     * @return a new {@code SocksServerSocketFactory} from the system property
     * {@code socksClient.socksServerUri} and system properties defined by
     * other {@code PropertySpec} constants classes. {@code null} is returned
     * if the system property {@code socksClient.socksServerUri} is not set or
     * the value is invalid.
     */
    public static SocksServerSocketFactory newInstance() {
        SocksClient socksClient = SocksClient.newInstance();
        if (socksClient == null) {
            return null;
        }
        return socksClient.getSocksServerSocketFactory();
    }
    
}
