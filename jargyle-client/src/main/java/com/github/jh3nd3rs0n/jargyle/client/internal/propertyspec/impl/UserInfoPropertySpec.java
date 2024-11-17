package com.github.jh3nd3rs0n.jargyle.client.internal.propertyspec.impl;

import com.github.jh3nd3rs0n.jargyle.client.PropertySpec;
import com.github.jh3nd3rs0n.jargyle.client.UserInfo;

public final class UserInfoPropertySpec extends PropertySpec<UserInfo> {

    /**
     * Constructs a {@code UserInfoPropertySpec} with the provided name and
     * the provided default value.
     *
     * @param n          the provided name
     * @param defaultVal the provided default value (can be {@code null})
     */
    public UserInfoPropertySpec(final String n, final UserInfo defaultVal) {
        super(n, UserInfo.class, defaultVal);
    }

    @Override
    protected UserInfo parse(final String value) {
        return UserInfo.newInstance(value);
    }

}
