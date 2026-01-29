package com.github.jh3nd3rs0n.jargyle.protocolbase.socks5;

import com.github.jh3nd3rs0n.jargyle.common.number.UnsignedByte;
import com.github.jh3nd3rs0n.jargyle.internal.annotation.EnumValueDoc;
import com.github.jh3nd3rs0n.jargyle.internal.annotation.EnumValueTypeDoc;

import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * Type of request to the SOCKS server.
 */
@EnumValueTypeDoc(
        description = "Type of request to the SOCKS server",
        name = "SOCKS5 Request Command",
        syntax = "CONNECT|BIND|UDP_ASSOCIATE|RESOLVE",
        syntaxName = "SOCKS5_REQUEST_COMMAND"
)
public enum Command {

    /**
     * A request to the SOCKS server to connect to another server.
     */
    @EnumValueDoc(
            description = "A request to the SOCKS server to connect to another "
                    + "server",
            value = "CONNECT"
    )
    CONNECT((byte) 0x01),

    /**
     * A request to the SOCKS server to bind to another address and port in
     * order to receive an inbound connection.
     */
    @EnumValueDoc(
            description = "A request to the SOCKS server to bind to another "
                    + "address and port in order to receive an inbound "
                    + "connection",
            value = "BIND"
    )
    BIND((byte) 0x02),

    /**
     * A request to the SOCKS server to establish an association within the
     * UDP relay process to handle UDP datagrams.
     */
    @EnumValueDoc(
            description = "A request to the SOCKS server to establish an "
                    + "association within the UDP relay process to handle "
                    + "UDP datagrams",
            value = "UDP_ASSOCIATE"
    )
    UDP_ASSOCIATE((byte) 0x03);

    /**
     * The {@code byte} value associated with this {@code Command}.
     */
    private final byte byteValue;

    /**
     * Constructs a {@code Command} with the provided {@code byte} value.
     *
     * @param bValue the provided {@code byte} value
     */
    Command(final byte bValue) {
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
    public static Command valueOfByte(final byte b) {
        for (Command command : Command.values()) {
            if (command.byteValue() == b) {
                return command;
            }
        }
        String str = Arrays.stream(Command.values())
                .map(Command::byteValue)
                .map(bv -> UnsignedByte.valueOf(bv).intValue())
                .map(Integer::toHexString)
                .collect(Collectors.joining(", "));
        throw new IllegalArgumentException(String.format(
                "expected command must be one of the following values: %s. "
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
    public static Command valueOfString(final String s) {
        Command command;
        try {
            command = Command.valueOf(s);
        } catch (IllegalArgumentException e) {
            String str = Arrays.stream(Command.values())
                    .map(Command::toString)
                    .collect(Collectors.joining(", "));
            throw new IllegalArgumentException(String.format(
                    "expected command must be one of the following values: %s. "
                            + "actual value is %s",
                    str,
                    s));
        }
        return command;
    }

    /**
     * Returns the {@code byte} value associated with this {@code Command}.
     *
     * @return the {@code byte} value associated with this {@code Command}
     */
    public byte byteValue() {
        return this.byteValue;
    }

}
