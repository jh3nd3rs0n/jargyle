package com.github.jh3nd3rs0n.jargyle.client.internal.propertyspec.impl;

import com.github.jh3nd3rs0n.jargyle.client.PropertySpec;
import com.github.jh3nd3rs0n.jargyle.protocolbase.socks5.userpassauthmethod.Request;

/**
 * An implementation of {@code PropertySpec} of type {@code String} whose
 * length of the bytes of the {@code String} for the username does not exceed
 * the maximum length of a username in bytes.
 */
public final class Socks5UserpassAuthMethodUsernamePropertySpec
        extends PropertySpec<String> {

    /**
     * Constructs a {@code Socks5UserpassAuthMethodUsernamePropertySpec} with
     * the provided name and the provided default value. An
     * {@code IllegalArgumentException} is thrown if the length of bytes of
     * the provided default value is greater than the maximum length of a
     * username in bytes.
     *
     * @param n          the provided name
     * @param defaultVal the provided default value (can be {@code null})
     */
    public Socks5UserpassAuthMethodUsernamePropertySpec(
            final String n, final String defaultVal) {
        super(
                n,
                String.class,
                (defaultVal == null) ?
                        null : getValidatedUsername(defaultVal));
    }

    /**
     * Returns the provided validated username. An
     * {@code IllegalArgumentException} is thrown if the length of bytes of
     * the provided username is greater than the maximum length of a username
     * in bytes.
     *
     * @param username the provided username
     * @return the provided validated username
     */
    private static String getValidatedUsername(final String username) {
        Request.validateUsername(username);
        return username;
    }

    @Override
    protected String parse(final String value) {
        return value;
    }

    @Override
    protected String validate(final String value) {
        return getValidatedUsername(value);
    }

}
