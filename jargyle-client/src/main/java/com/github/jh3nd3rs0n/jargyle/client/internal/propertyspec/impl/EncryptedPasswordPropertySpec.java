package com.github.jh3nd3rs0n.jargyle.client.internal.propertyspec.impl;

import com.github.jh3nd3rs0n.jargyle.client.PropertySpec;
import com.github.jh3nd3rs0n.jargyle.common.security.EncryptedPassword;

/**
 * An implementation of {@code PropertySpec} of type {@code EncryptedPassword}.
 */
public final class EncryptedPasswordPropertySpec
        extends PropertySpec<EncryptedPassword> {

    /**
     * Constructs an {@code EncryptedPasswordPropertySpec} with the provided
     * name and the provided default value.
     *
     * @param n          the provided name
     * @param defaultVal the provided default value (can be {@code null})
     */
    public EncryptedPasswordPropertySpec(
            final String n, final EncryptedPassword defaultVal) {
        super(n, EncryptedPassword.class, defaultVal);
    }

    @Override
    protected EncryptedPassword parse(final String value) {
        return EncryptedPassword.newInstance(value.toCharArray());
    }

}
