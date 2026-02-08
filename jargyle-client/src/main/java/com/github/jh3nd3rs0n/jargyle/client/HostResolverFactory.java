package com.github.jh3nd3rs0n.jargyle.client;

/**
 * A factory that creates {@code HostResolver}s.
 */
public abstract class HostResolverFactory {

    /**
     * Allows the construction of subclass instances.
     */
    public HostResolverFactory() { }

    /**
     * Returns the default instance of {@code HostResolverFactory}. The 
     * default instance creates plain {@code HostResolver}s.
     *
     * @return the default instance of {@code HostResolverFactory}
     */
    public static HostResolverFactory getDefault() {
        return DefaultHostResolverFactory.getInstance();
    }

    /**
     * Returns a {@code HostResolverFactory}. If the system property
     * {@code socksClient.socksServerUri} is set and the value is valid, a new
     * {@code SocksHostResolverFactory} is returned. Otherwise, the default
     * instance of {@code HostResolverFactory} from {@link #getDefault()} is
     * returned.
     *
     * @return a {@code HostResolverFactory}
     */
    public static HostResolverFactory getInstance() {
        SocksHostResolverFactory socksHostResolverFactory =
                SocksHostResolverFactory.newInstance();
        if (socksHostResolverFactory != null) {
            return socksHostResolverFactory;
        }
        return getDefault();
    }

    /**
     * Returns a new {@code HostResolver}.
     *
     * @return a new {@code HostResolver}
     */
    public abstract HostResolver newHostResolver();

}
