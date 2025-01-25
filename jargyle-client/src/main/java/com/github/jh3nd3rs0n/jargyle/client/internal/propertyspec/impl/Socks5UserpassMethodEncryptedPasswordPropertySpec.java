package com.github.jh3nd3rs0n.jargyle.client.internal.propertyspec.impl;

import com.github.jh3nd3rs0n.jargyle.client.PropertySpec;
import com.github.jh3nd3rs0n.jargyle.common.security.EncryptedPassword;
import com.github.jh3nd3rs0n.jargyle.protocolbase.socks5.userpassmethod.Request;

import java.util.Arrays;

/**
 * An implementation of {@code PropertySpec} of type {@code EncryptedPassword}
 * whose length of the bytes of the password does not exceed the maximum
 * length of a password in bytes.
 */
public final class Socks5UserpassMethodEncryptedPasswordPropertySpec
        extends PropertySpec<EncryptedPassword> {

    /**
     * Constructs a {@code Socks5UserpassMethodEncryptedPasswordPropertySpec}
     * with the provided name and the provided default value. An
     * {@code IllegalArgumentException} is thrown if the length of bytes of
     * the password of the provided default value is greater than the maximum
     * length of a password in bytes.
     *
     * @param n          the provided name
     * @param defaultVal the provided default value (can be {@code null})
     */
    public Socks5UserpassMethodEncryptedPasswordPropertySpec(
            final String n, final EncryptedPassword defaultVal) {
        super(
                n,
                EncryptedPassword.class,
                (defaultVal == null) ?
                        null : getValidatedEncryptedPassword(defaultVal));
    }

    /**
     * Returns the provided validated {@code EncryptedPassword}. An
     * {@code IllegalArgumentException} is thrown if the length of bytes of
     * the password of the provided {@code EncryptedPassword} is greater than
     * the maximum length of a password in bytes.
     *
     * @param encryptedPassword the provided {@code EncryptedPassword}
     * @return the provided validated {@code EncryptedPassword}
     */
    private static EncryptedPassword getValidatedEncryptedPassword(
            final EncryptedPassword encryptedPassword) {
        char[] password = encryptedPassword.getPassword();
        Request.validatePassword(password);
        Arrays.fill(password, '\0');
        return encryptedPassword;
    }

    @Override
    protected EncryptedPassword parse(final String value) {
        return EncryptedPassword.newInstance(value.toCharArray());
    }

    @Override
    protected EncryptedPassword validate(final EncryptedPassword value) {
        return getValidatedEncryptedPassword(value);
    }

}
