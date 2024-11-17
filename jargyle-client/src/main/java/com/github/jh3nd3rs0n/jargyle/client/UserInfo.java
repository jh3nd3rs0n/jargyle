package com.github.jh3nd3rs0n.jargyle.client;

import com.github.jh3nd3rs0n.jargyle.internal.annotation.SingleValueTypeDoc;

/**
 * Specifies the user information to be used to access the SOCKS server.
 */
@SingleValueTypeDoc(
        description = "Specifies the user information to be used to access "
                + "the SOCKS server",
        name = "User Info",
        syntax = "(See https://www.ietf.org/rfc/rfc2396.txt 3.2.2. Server-based Naming Authority)",
        syntaxName = "USER_INFO"
)
public final class UserInfo {

    /**
     * The regular expression for the user information.
     */
    private static final String USER_INFO_REGEX =
            "\\A([a-zA-Z0-9\\-_.!~*'();:&=+$,]|%[0-9a-fA-F][0-9a-fA-F])*\\z";

    /**
     * The {@code String} representation of this {@code UserInfo}.
     */
    private final String string;

    /**
     * Constructs a {@code UserInfo} of the provided user information.
     *
     * @param str the provided user information
     */
    private UserInfo(final String str) {
        this.string = str;
    }

    /**
     * Returns a new {@code UserInfo} of the provided user information. An
     * {@code IllegalArgumentException} is thrown if the provided user
     * information is invalid.
     *
     * @param s the provided user information
     * @return a new {@code UserInfo} of the provided user information
     */
    public static UserInfo newInstance(final String s) {
        if (!s.matches(USER_INFO_REGEX)) {
            throw new IllegalArgumentException(String.format(
                    "invalid user info: %s",
                    s));
        }
        return new UserInfo(s);
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (this.getClass() != obj.getClass()) {
            return false;
        }
        UserInfo other = (UserInfo) obj;
        return this.string.equals(other.string);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + this.string.hashCode();
        return result;
    }

    /**
     * Returns the {@code String} representation of this {@code UserInfo}.
     *
     * @return the {@code String} representation of this {@code UserInfo}
     */
    @Override
    public String toString() {
        return this.string;
    }

}
