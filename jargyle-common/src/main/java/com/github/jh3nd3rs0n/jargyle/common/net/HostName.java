package com.github.jh3nd3rs0n.jargyle.common.net;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * A name of a node of a network.
 */
public final class HostName extends Host {

    /**
     * The regular expression for a host name.
     */
    private static final String HOST_NAME_REGEX =
            "\\A([a-z0-9]|[a-z0-9][a-z0-9-_]*[a-z0-9])"
                    + "(\\.([a-z0-9]|[a-z0-9][a-z0-9-_]*[a-z0-9]))*\\z";

    /**
     * Constructs a {@code HostName} with the provided host name.
     *
     * @param str the provided host name
     */
    HostName(final String str) {
        super(str);
    }

    /**
     * Returns a new {@code HostName} with the provided host name. An
     * {@code IllegalArgumentException} is thrown if the provided host name is
     * invalid.
     *
     * @param string the provided host name
     * @return a new {@code HostName} with the provided host name
     */
    public static HostName newHostName(final String string) {
        if (!string.matches(HOST_NAME_REGEX)) {
            throw new IllegalArgumentException(String.format(
                    "invalid host name: %s",
                    string));
        }
        return new HostName(string);
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
        HostName other = (HostName) obj;
        return this.string.equals(other.string);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + this.string.hashCode();
        return result;
    }

    @Override
    public InetAddress toInetAddress() throws UnknownHostException {
        return InetAddress.getByName(this.string);
    }

}
