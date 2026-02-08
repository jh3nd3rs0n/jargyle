package com.github.jh3nd3rs0n.jargyle.client;

import java.util.Objects;

/**
 * An implementation of {@code DatagramSocketFactory} that creates 
 * {@code DatagramSocket}s whose traffic would be routed through the SOCKS 
 * server.
 */
public abstract class SocksDatagramSocketFactory extends DatagramSocketFactory {
    
    /**
     * The {@code SocksClient} of this {@code SocksDatagramSocketFactory}.
     */
    private final SocksClient socksClient;

    /**
     * Constructs a {@code SocksDatagramSocketFactory} with the provided
     * {@code SocksClient}.
     *
     * @param client the provided {@code SocksClient}
     */    
    public SocksDatagramSocketFactory(final SocksClient client) {
        this.socksClient = Objects.requireNonNull(client);
    }

    /**
     * Returns the {@code SocksClient} of this
     * {@code SocksDatagramSocketFactory}.
     *
     * @return the {@code SocksClient} of this
     * {@code SocksDatagramSocketFactory}
     */
    public final SocksClient getSocksClient() {
        return this.socksClient;
    }

    /**
     * Returns a new {@code SocksDatagramSocketFactory} from the system 
     * property {@code socksClient.socksServerUri} and system properties 
     * defined by other {@code PropertySpec} constants classes. {@code null} 
     * is returned if the system property {@code socksClient.socksServerUri} 
     * is not set or the value is invalid.
     *
     * @return a new {@code SocksDatagramSocketFactory} from the system 
     * property {@code socksClient.socksServerUri} and system properties 
     * defined by other {@code PropertySpec} constants classes. {@code null}
     * is returned if the system property {@code socksClient.socksServerUri}
     * is not set or the value is invalid.
     */    
    public static SocksDatagramSocketFactory newInstance() {
        SocksClient socksClient = SocksClient.newInstance();
        if (socksClient == null) {
            return null;
        }
        return socksClient.getSocksDatagramSocketFactory();
    }
    
}
