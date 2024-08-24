package com.github.jh3nd3rs0n.jargyle.protocolbase.socks5.userpassmethod;

import com.github.jh3nd3rs0n.jargyle.common.number.UnsignedByte;

import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * Protocol version.
 */
public enum Version {

    /**
     * Protocol version 1.
     */
    V1((byte) 0x01);

    /**
     * The {@code byte} value associated with this {@code Version}.
     */
    private final byte byteValue;

    /**
     * Constructs a {@code Version} with the provided {@code byte} value.
     *
     * @param bValue the provided {@code byte} value
     */
    Version(final byte bValue) {
        this.byteValue = bValue;
    }

    /**
     * Returns the enum constant associated with the provided {@code byte}
     * value. An {@code IllegalArgumentException} is thrown if there is no
     * enum constant associated with the provided {@code byte} value.
     *
     * @param b the provided {@code byte} value
     * @return the enum constant associated with the provided {@code byte}
     * value
     */
    public static Version valueOfByte(final byte b) {
        for (Version version : Version.values()) {
            if (version.byteValue() == b) {
                return version;
            }
        }
        String str = Arrays.stream(Version.values())
                .map(Version::byteValue)
                .map(bv -> UnsignedByte.valueOf(bv).intValue())
                .map(Integer::toHexString)
                .collect(Collectors.joining(", "));
        throw new IllegalArgumentException(String.format(
                "expected version must be one of the following values: %s. "
                        + "actual value is %s",
                str,
                Integer.toHexString(UnsignedByte.valueOf(b).intValue())));
    }

    /**
     * Returns the {@code byte} value associated with this {@code Version}.
     *
     * @return the {@code byte} value associated with this {@code Version}
     */
    public byte byteValue() {
        return this.byteValue;
    }

}
