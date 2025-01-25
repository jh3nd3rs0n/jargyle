package com.github.jh3nd3rs0n.jargyle.client.internal.propertyspec.impl;

import com.github.jh3nd3rs0n.jargyle.client.PropertySpec;
import org.ietf.jgss.GSSException;
import org.ietf.jgss.Oid;

/**
 * An implementation of {@code PropertySpec} of type {@code Oid}.
 */
public final class OidPropertySpec extends PropertySpec<Oid> {

    /**
     * Constructs an {@code OidPropertySpec} with the provided name and the
     * provided default value. The provided default value must consist of
	 * positive integers separated by dots. An
	 * {@code IllegalArgumentException} is thrown if the provided default
	 * value is invalid.
     *
     * @param n          the provided name
     * @param defaultVal the provided default value (can be {@code null})
     */
    public OidPropertySpec(final String n, final String defaultVal) {
        super(n, Oid.class, (defaultVal == null) ? null : newOid(defaultVal));
    }

    /**
     * Returns a new {@code Oid} of the provided {@code String} consisting of
     * positive integers separated by dots. An
     * {@code IllegalArgumentException} is thrown if the provided
     * {@code String} is invalid.
     *
     * @param s the provided {@code String} consisting of positive integers
     *          separated by dots
     * @return a new {@code Oid} of the provided {@code String} consisting of
     * positive integers separated by dots
     */
    private static Oid newOid(final String s) {
        Oid oid;
        try {
            oid = new Oid(s);
        } catch (GSSException e) {
            throw new IllegalArgumentException(e);
        }
        return oid;
    }

    @Override
    protected Oid parse(final String value) {
        return newOid(value);
    }

}
