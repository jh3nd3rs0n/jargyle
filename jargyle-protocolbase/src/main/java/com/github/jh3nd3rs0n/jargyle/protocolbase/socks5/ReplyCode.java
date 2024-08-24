package com.github.jh3nd3rs0n.jargyle.protocolbase.socks5;

import com.github.jh3nd3rs0n.jargyle.common.number.UnsignedByte;

import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * Type of reply from the SOCKS server.
 */
public enum ReplyCode {

    /**
     * A reply from the SOCKS server indicating success.
     */
    SUCCEEDED((byte) 0x00),

    /**
     * A reply from the SOCKS server indicating general SOCKS server failure.
     */
    GENERAL_SOCKS_SERVER_FAILURE((byte) 0x01),

    /**
     * A reply from the SOCKS server indicating that the connection was not
     * allowed by the ruleset.
     */
    CONNECTION_NOT_ALLOWED_BY_RULESET((byte) 0x02),

    /**
     * A reply from the SOCKS server indicating that the network was
     * unreachable.
     */
    NETWORK_UNREACHABLE((byte) 0x03),

    /**
     * A reply from the SOCKS server indicating that the host was unreachable.
     */
    HOST_UNREACHABLE((byte) 0x04),

    /**
     * A reply from the SOCKS server indicating that the connection was
     * refused.
     */
    CONNECTION_REFUSED((byte) 0x05),

    /**
     * A reply from the SOCKS server indicating that the TTL expired.
     */
    TTL_EXPIRED((byte) 0x06),

    /**
     * A reply from the SOCKS server indicating that the command of the
     * request to the SOCKS server is not supported.
     */
    COMMAND_NOT_SUPPORTED((byte) 0x07),

    /**
     * A reply from the SOCKS server indicating that the address type of the
     * desired destination address of the request to the SOCKS server is not
     * supported.
     */
    ADDRESS_TYPE_NOT_SUPPORTED((byte) 0x08);

    /**
     * The {@code byte} value associated with this {@code ReplyCode}.
     */
    private final byte byteValue;

    /**
     * Constructs a {@code ReplyCode} with the provided {@code byte} value.
     *
     * @param bValue the provided {@code byte} value
     */
    ReplyCode(final byte bValue) {
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
    public static ReplyCode valueOfByte(final byte b) {
        for (ReplyCode replyCode : ReplyCode.values()) {
            if (replyCode.byteValue() == b) {
                return replyCode;
            }
        }
        String str = Arrays.stream(ReplyCode.values())
                .map(ReplyCode::byteValue)
                .map(bv -> UnsignedByte.valueOf(bv).intValue())
                .map(Integer::toHexString)
                .collect(Collectors.joining(", "));
        throw new IllegalArgumentException(String.format(
                "expected reply code must be one of the following values: %s. "
                        + "actual value is %s",
                str,
                Integer.toHexString(UnsignedByte.valueOf(b).intValue())));
    }

    /**
     * Returns the {@code byte} value associated with this {@code ReplyCode}.
     *
     * @return the {@code byte} value associated with this {@code ReplyCode}
     */
    public byte byteValue() {
        return this.byteValue;
    }

}
