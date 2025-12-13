package com.github.jh3nd3rs0n.jargyle.client.socks5;

import com.github.jh3nd3rs0n.jargyle.protocolbase.socks5.userpassauthmethod.Request;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Arrays;
import java.util.Objects;

/**
 * A username and password pair.
 */
final class UsernamePassword {

    /**
     * The password of this {@code UsernamePassword}.
     */
    private final char[] password;

    /**
     * The username of this {@code UsernamePassword}
     */
    private final String username;

    /**
     * Constructs a {@code UsernamePassword} with the provided username and
     * the provided password.
     *
     * @param usrname the provided username
     * @param psswrd  the provided password
     */
    private UsernamePassword(final String usrname, final char[] psswrd) {
        this.username = usrname;
        this.password = Arrays.copyOf(psswrd, psswrd.length);
    }

    /**
     * Returns a new {@code UsernamePassword} with the provided username and
     * the provided password. An {@code IllegalArgumentException} is thrown if
     * the length of bytes of the provided username is greater than the
     * maximum length of a username in bytes or if the length of bytes of the
     * provided password is greater than the maximum length of a password in
     * bytes.
     *
     * @param usrname the provided username
     * @param psswrd  the provided password
     * @return a new {@code UsernamePassword} with the provided username and
     * the provided password
     */
    public static UsernamePassword newInstance(
            final String usrname, final char[] psswrd) {
        Objects.requireNonNull(usrname, "username must not be null");
        Objects.requireNonNull(psswrd, "password must not be null");
        Request.validateUsername(usrname);
        Request.validatePassword(psswrd);
        return new UsernamePassword(usrname, psswrd);
    }

    /**
     * Returns a new {@code UsernamePassword} from the provided {@code String}.
     * The provided {@code String} must be the URL encoded username followed
     * by a colon character ({@code :}) followed by the URL encoded password.
     * An {@code IllegalArgumentException} is thrown if any part of the
     * provided {@code String} is invalid, if the length of bytes of the
     * decoded username is greater than the maximum length of a username in
     * bytes, or if the length of bytes of the decoded password is greater
     * than the maximum length of a password in bytes.
     *
     * @param s the provided {@code String}
     * @return a new {@code UsernamePassword} from the provided {@code String}
     */
    public static UsernamePassword newInstanceFrom(final String s) {
        String[] sElements = s.split(":", 2);
        if (sElements.length != 2) {
            throw new IllegalArgumentException(
                    "username password pair must be in the following format: "
                            + "USERNAME:PASSWORD");
        }
        String username;
        try {
            username = URLDecoder.decode(sElements[0], "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new AssertionError(e);
        }
        String password;
        try {
            password = URLDecoder.decode(sElements[1], "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new AssertionError(e);
        }
        return newInstance(username, password.toCharArray());
    }

    /**
     * Returns a new {@code UsernamePassword} from the provided {@code String}
     * or {@code null} if the provided {@code String} is invalid. This method
     * invokes {@link #newInstanceFrom(String)} and returns a new
     * {@code UsernamePassword} or {@code null} if an
     * {@code IllegalArgumentException} is thrown.
     *
     * @param s the provided {@code String}
     * @return a new {@code UsernamePassword} from the provided {@code String}
     * or {@code null} if the provided {@code String} is invalid
     */
    public static UsernamePassword tryNewInstanceFrom(final String s) {
        try {
            return newInstanceFrom(s);
        } catch (IllegalArgumentException e) {
            return null;
        }
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
        UsernamePassword other = (UsernamePassword) obj;
        if (!this.username.equals(other.username)) {
            return false;
        }
        return Arrays.equals(this.password, other.password);
    }

    /**
     * Returns the password of this {@code UsernamePassword}.
     *
     * @return the password of this {@code UsernamePassword}
     */
    public char[] getPassword() {
        return Arrays.copyOf(this.password, this.password.length);
    }

    /**
     * Returns the username of this {@code UsernamePassword}.
     *
     * @return the username of this {@code UsernamePassword}
     */
    public String getUsername() {
        return this.username;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + this.username.hashCode();
        result = prime * result + Arrays.hashCode(this.password);
        return result;
    }

    /**
     * Returns the {@code String} representation of this
     * {@code UsernamePassword}.
     *
     * @return the {@code String} representation of this
     * {@code UsernamePassword}
     */
    @Override
    public String toString() {
        return this.getClass().getSimpleName() +
                " [username=" +
                this.username +
                ", password=" +
                Arrays.toString(this.password) +
                "]";
    }

}
