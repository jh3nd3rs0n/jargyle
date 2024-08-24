package com.github.jh3nd3rs0n.jargyle.protocolbase.socks5;

import com.github.jh3nd3rs0n.jargyle.common.number.UnsignedByte;
import com.github.jh3nd3rs0n.jargyle.internal.annotation.EnumValueDoc;
import com.github.jh3nd3rs0n.jargyle.internal.annotation.EnumValueTypeDoc;

import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * Authentication method.
 */
@EnumValueTypeDoc(
        description = "Authentication method",
        name = "SOCKS5 Method",
        syntax = "NO_AUTHENTICATION_REQUIRED|GSSAPI|USERNAME_PASSWORD",
        syntaxName = "SOCKS5_METHOD"
)
public enum Method {

    /**
     * No authentication required.
     */
    @EnumValueDoc(
            description = "No authentication required",
            value = "NO_AUTHENTICATION_REQUIRED"
    )
    NO_AUTHENTICATION_REQUIRED((byte) 0x00),

    /**
     * GSS-API authentication.
     */
    @EnumValueDoc(
            description = "GSS-API authentication",
            value = "GSSAPI"
    )
    GSSAPI((byte) 0x01),

    /**
     * Username password authentication.
     */
    @EnumValueDoc(
            description = "Username password authentication",
            value = "USERNAME_PASSWORD"
    )
    USERNAME_PASSWORD((byte) 0x02),

    /**
     * No acceptable methods.
     */
    NO_ACCEPTABLE_METHODS((byte) 0xff);

    /**
     * The {@code byte} value associated with this {@code Method}.
     */
    private final byte byteValue;

    /**
     * Constructs a {@code Method} with the provided {@code byte} value.
     *
     * @param bValue the provided {@code byte} value
     */
    Method(final byte bValue) {
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
    public static Method valueOfByte(final byte b) {
        for (Method method : Method.values()) {
            if (method.byteValue() == b) {
                return method;
            }
        }
        String str = Arrays.stream(Method.values())
                .map(Method::byteValue)
                .map(bv -> UnsignedByte.valueOf(bv).intValue())
                .map(Integer::toHexString)
                .collect(Collectors.joining(", "));
        throw new IllegalArgumentException(String.format(
                "expected method must be one of the following values: %s. "
                        + "actual value is %s",
                str,
                Integer.toHexString(UnsignedByte.valueOf(b).intValue())));
    }

    /**
     * Returns the enum constant of the provided {@code String}. An
     * {@code IllegalArgumentException} is thrown if there is no enum constant
     * of the provided {@code String}.
     *
     * @param s the provided {@code String}
     * @return the enum constant of the provided {@code String}
     */
    public static Method valueOfString(final String s) {
        Method method;
        try {
            method = Method.valueOf(s);
        } catch (IllegalArgumentException e) {
            String str = Arrays.stream(Method.values())
                    .map(Method::toString)
                    .collect(Collectors.joining(", "));
            throw new IllegalArgumentException(String.format(
                    "expected method must be one of the following values: %s. "
                            + "actual value is %s",
                    str,
                    s));
        }
        return method;
    }

    /**
     * Returns the {@code byte} value associated with this {@code Method}.
     *
     * @return the {@code byte} value associated with this {@code Method}
     */
    public byte byteValue() {
        return this.byteValue;
    }

}
