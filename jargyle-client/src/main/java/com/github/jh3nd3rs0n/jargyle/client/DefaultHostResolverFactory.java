package com.github.jh3nd3rs0n.jargyle.client;

/**
 * Default implementation of {@code HostResolverFactory}. This implementation
 * creates plain {@code HostResolver}s.
 */
final class DefaultHostResolverFactory extends HostResolverFactory {

    private static final DefaultHostResolverFactory INSTANCE = 
            new DefaultHostResolverFactory();

    /**
     * Prevents the construction of additional instances.
     */
    private DefaultHostResolverFactory() { }

    /**
     * Returns the instance of {@code DefaultHostResolverFactory}.
     *
     * @return the instance of {@code DefaultHostResolverFactory}
     */
    public static DefaultHostResolverFactory getInstance() {
        return INSTANCE;
    }

    @Override
    public HostResolver newHostResolver() {
        return new HostResolver();
    }

}
