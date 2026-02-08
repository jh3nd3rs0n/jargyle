package com.github.jh3nd3rs0n.jargyle.client;

import org.junit.Test;

import static org.junit.Assert.*;

public class DefaultHostResolverFactoryTest {

    @Test
    public void testNewHostResolver() {
        assertNotNull(DefaultHostResolverFactory.getInstance().newHostResolver());
    }

}