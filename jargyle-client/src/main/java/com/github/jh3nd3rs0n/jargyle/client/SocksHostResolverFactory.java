package com.github.jh3nd3rs0n.jargyle.client;

import java.util.Objects;

/**
 * An implementation of {@code HostResolverFactory} that creates 
 * {@code HostResolver}s whose traffic would be routed through the SOCKS 
 * server.
 */
public abstract class SocksHostResolverFactory extends HostResolverFactory {

    /**
     * The {@code SocksClient} of this {@code SocksHostResolverFactory}.
     */
    private final SocksClient socksClient;

    /**
     * Constructs a {@code SocksHostResolverFactory} with the provided
     * {@code SocksClient}.
     *
     * @param client the provided {@code SocksClient}
     */
    public SocksHostResolverFactory(final SocksClient client) { 
        this.socksClient = Objects.requireNonNull(client);
    }

    /**
     * Returns the {@code SocksClient} of this
     * {@code SocksHostResolverFactory}.
     *
     * @return the {@code SocksClient} of this
     * {@code SocksHostResolverFactory}
     */
    public final SocksClient getSocksClient() {
        return this.socksClient;
    }

    /**
     * Returns a new {@code SocksHostResolverFactory} from the system 
     * property {@code socksClient.socksServerUri} and system properties 
     * defined by other {@code PropertySpec} constants classes. {@code null}
     * is returned if the system property {@code socksClient.socksServerUri}
     * is not set or the value is invalid.
     *
     * @return a new {@code SocksHostResolverFactory} from the system 
     * property {@code socksClient.socksServerUri} and system properties 
     * defined by other {@code PropertySpec} constants classes. {@code null}
     * is returned if the system property {@code socksClient.socksServerUri}
     * is not set or the value is invalid.
     */
    public static SocksHostResolverFactory newInstance() {
        SocksClient socksClient = SocksClient.newInstance();
        if (socksClient == null) {
            return null;
        }
        return socksClient.getSocksHostResolverFactory();
    }
    
}
