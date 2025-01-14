package com.github.jh3nd3rs0n.jargyle.common.net;

import com.github.jh3nd3rs0n.jargyle.internal.annotation.ValuesValueTypeDoc;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * A collection of {@code HostAddressType}s.
 */
@ValuesValueTypeDoc(
        description = "A comma separated list of host address types",
        elementValueType = HostAddressType.class,
        name = "Host Address Types",
        syntax = "[HOST_ADDRESS_TYPE1[,HOST_ADDRESS_TYPE2[...]]]",
        syntaxName = "HOST_ADDRESS_TYPES"
)
public final class HostAddressTypes {

    /**
     * The default instance of {@code HostAddressTypes}.
     */
    private static final HostAddressTypes DEFAULT_INSTANCE =
            HostAddressTypes.of(HostAddressType.IPV4, HostAddressType.IPV6);

    /**
     * The {@code List} of {@code HostAddressType}s.
     */
    private final List<HostAddressType> hostAddressTypes;

    /**
     * Constructs a {@code HostAddressTypes} with the provided {@code List}
     * of {@code HostAddressType}s.
     *
     * @param hostAddrTypes the provided {@code List} of
     *                      {@code HostAddressType}s
     */
    private HostAddressTypes(final List<HostAddressType> hostAddrTypes) {
        this.hostAddressTypes = new ArrayList<>(hostAddrTypes);
    }

    /**
     * Returns the default instance of {@code HostAddressTypes}. The default
     * instance contains the following {@code HostAddressType}s in the
     * following order: {@code IPv4} and {@code IPv6}.
     *
     * @return the default instance of {@code HostAddressTypes}
     */
    public static HostAddressTypes getDefault() {
        return DEFAULT_INSTANCE;
    }

    /**
     * Returns a new instance of {@code HostAddressTypes} from the provided
     * {@code String}. The provided {@code String} is a comma separated list
     * of {@code String} representations of {@code HostAddressType}s. if the
     * provided {@code String} is empty, an empty instance of
     * {@code HostAddressTypes} is returned. An
     * {@code IllegalArgumentException} is thrown if the provided
     * {@code String} is invalid.
     *
     * @param s the provided {@code String}
     * @return a new instance of {@code HostAddressTypes} from the provided
     * {@code String}
     */
    public static HostAddressTypes newInstanceFrom(final String s) {
        List<HostAddressType> hostAddressTypes = new ArrayList<>();
        if (s.isEmpty()) {
            return new HostAddressTypes(hostAddressTypes);
        }
        String[] sElements = s.split(",", -1);
        for (String sElement : sElements) {
            hostAddressTypes.add(HostAddressType.valueOfString(sElement));
        }
        return new HostAddressTypes(hostAddressTypes);
    }

    /**
     * Returns a {@code HostAddressTypes} of the provided {@code List} of
     * {@code HostAddressType}s.
     *
     * @param hostAddrTypes the provided {@code List} of
     *                      {@code HostAddressType}s
     * @return a {@code HostAddressTypes} of the provided {@code List} of
     * {@code HostAddressType}s
     */
    public static HostAddressTypes of(
            final List<HostAddressType> hostAddrTypes) {
        return new HostAddressTypes(hostAddrTypes);
    }

    /**
     * Returns a {@code HostAddressTypes} of the provided
     * {@code HostAddressType}s.
     *
     * @param hostAddrTypes the provided {@code HostAddressType}s
     * @return a {@code HostAddressTypes} of the provided
     * {@code HostAddressType}s
     */
    public static HostAddressTypes of(
            final HostAddressType... hostAddrTypes) {
        return of(Arrays.asList(hostAddrTypes));
    }

    /**
     * Returns the first {@code HostAddressType} in this
     * {@code HostAddressTypes} that describes the provided
     * {@code HostAddress}.
     *
     * @param hostAddress the provided {@code HostAddress}
     * @return the first {@code HostAddressType} in this
     * {@code HostAddressTypes} that describes the provided
     * {@code HostAddress} or {@code null} if there is no
     * {@code HostAddressType} in this {@code HostAddressTypes} that describes
     * the provided {@code HostAddress}
     */
    public HostAddressType firstDescribes(final HostAddress hostAddress) {
        for (HostAddressType hostAddressType : this.hostAddressTypes) {
            if (hostAddressType.describes(hostAddress)) {
                return hostAddressType;
            }
        }
        return null;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (this.getClass() != obj.getClass()) {
            return false;
        }
        HostAddressTypes other = (HostAddressTypes) obj;
        return this.hostAddressTypes.equals(other.hostAddressTypes);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + this.hostAddressTypes.hashCode();
        return result;
    }

    /**
     * Returns an unmodifiable {@code List} of {@code HostAddressType}s.
     *
     * @return an unmodifiable {@code List} of {@code HostAddressType}s
     */
    public List<HostAddressType> toList() {
        return Collections.unmodifiableList(this.hostAddressTypes);
    }

    /**
     * Returns the {@code String} representation of this
     * {@code HostAddressTypes}. The {@code String} representation is a comma
     * separated list of {@code String} representations of
     * {@code HostAddressType}s.
     *
     * @return the {@code String} representation of this
     * {@code HostAddressTypes}
     */
    @Override
    public String toString() {
        return this.hostAddressTypes.stream()
                .map(HostAddressType::toString)
                .collect(Collectors.joining(","));
    }

}
