package com.github.jh3nd3rs0n.jargyle.common.net;

import com.github.jh3nd3rs0n.jargyle.internal.annotation.EnumValueDoc;
import com.github.jh3nd3rs0n.jargyle.internal.annotation.EnumValueTypeDoc;

import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * Type of {@code HostAddress}.
 */
@EnumValueTypeDoc(
        description = "Type of host address",
        name = "Host Address Type",
        syntax = "HOST_IPV4_ADDRESS|HOST_IPV6_ADDRESS",
        syntaxName = "HOST_ADDRESS_TYPE"
)
public enum HostAddressType {

    /**
     * {@code HostIpv4Address}.
     */
    @EnumValueDoc(
            description = "Host IPv4 address",
            value = "HOST_IPV4_ADDRESS"
    )
    HOST_IPV4_ADDRESS("HOST_IPV4_ADDRESS") {
        @Override
        public boolean describes(final HostAddress hostAddress) {
            return hostAddress instanceof HostIpv4Address;
        }
    },

    /**
     * {@code HostIpv6Address}.
     */
    @EnumValueDoc(
            description = "Host IPv6 address",
            value = "HOST_IPV6_ADDRESS"
    )
    HOST_IPV6_ADDRESS("HOST_IPV6_ADDRESS") {
        @Override
        public boolean describes(final HostAddress hostAddress) {
            return hostAddress instanceof HostIpv6Address;
        }
    };

    /**
     * The {@code String} representation of this {@code HostAddressType}.
     */
    private final String string;

    /**
     * Constructs a {@code HostAddressType} with the provided host address
     * type.
     *
     * @param str the provided host address type
     */
    HostAddressType(final String str) {
        this.string = str;
    }

    /**
     * Returns the enum constant of the provided {@code String}
     * representation. An {@code IllegalArgumentException} is thrown if there
     * is no enum constant of the provided {@code String} representation.
     *
     * @param string the provided {@code String} representation
     * @return the enum constant of the provided {@code String}
     * representation
     */
    public static HostAddressType valueOfString(final String string) {
        for (HostAddressType hostAddressType : HostAddressType.values()) {
            if (hostAddressType.toString().equals(string)) {
                return hostAddressType;
            }
        }
        String str = Arrays.stream(HostAddressType.values())
                .map(HostAddressType::toString)
                .collect(Collectors.joining(", "));
        throw new IllegalArgumentException(String.format(
                "expected host address type must be one of the following "
                        + "values: %s. actual value is %s",
                str,
                string));
    }

    /**
     * Returns the {@code boolean} value to indicate if this
     * {@code HostAddressType} describes the provided {@code HostAddress}.
     *
     * @param hostAddress the provided {@code HostAddress}
     * @return the {@code boolean} value to indicate if this
     * {@code HostAddressType} describes the provided {@code HostAddress}
     */
    public abstract boolean describes(final HostAddress hostAddress);

    /**
     * Returns the {@code String} representation of this
     * {@code HostAddressType}.
     *
     * @return the {@code String} representation of this
     * {@code HostAddressType}
     */
    @Override
    public String toString() {
        return this.string;
    }

}
