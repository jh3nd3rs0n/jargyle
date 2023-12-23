package com.github.jh3nd3rs0n.jargyle.common.net;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class HostName extends Host {

    private static final String HOST_NAME_REGEX =
            "\\A([a-z0-9]|[a-z0-9][a-z0-9-_]*[a-z0-9])"
                    + "(\\.([a-z0-9]|[a-z0-9][a-z0-9-_]*[a-z0-9]))*\\z";


    private static final class InetAddressFactoryImpl implements InetAddressFactory {

        private final String string;

        public InetAddressFactoryImpl(final String str) {
            this.string = str;
        }

        @Override
        public InetAddress getInetAddress() throws UnknownHostException {
            return InetAddress.getByName(this.string);
        }

    }

    HostName(String str, InetAddressFactory inetAddrFactory) {
        super(str, inetAddrFactory);
    }

    public static HostName newHostName(final String string) {
        if (!string.matches(HOST_NAME_REGEX)) {
            throw new IllegalArgumentException(String.format(
                    "invalid host name: %s",
                    string));
        }
        return new HostName(string, null);
    }

}
