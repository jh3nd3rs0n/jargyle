package com.github.jh3nd3rs0n.jargyle.protocolbase.socks5;

import com.github.jh3nd3rs0n.jargyle.common.number.UnsignedByte;

/**
 * Thrown when a {@code Command} is unknown or unsupported.
 */
public final class CommandNotSupportedException extends Socks5Exception {

    /**
     * The default serial version UID.
     */
    private static final long serialVersionUID = 1L;

    /**
     * The {@code UnsignedByte} associated with the {@code Command} that is
     * unknown or unsupported.
     */
    private final UnsignedByte command;

    /**
     * Constructs a {@code CommandNotSupportedException} with the provided
     * {@code UnsignedByte} associated with the {@code Command} that is
     * unknown or unsupported.
     *
     * @param cmd the provided {@code UnsignedByte} associated with the
     *            {@code Command} that is unknown or unsupported
     */
    public CommandNotSupportedException(final UnsignedByte cmd) {
        super(String.format(
                "command not supported: %s",
                Integer.toHexString(cmd.intValue())));
        this.command = cmd;
    }

    /**
     * Returns the provided {@code UnsignedByte} associated with the
     * {@code Command} that is unknown or unsupported.
     *
     * @return the provided {@code UnsignedByte} associated with the
     * {@code Command} that is unknown or unsupported
     */
    public UnsignedByte getCommand() {
        return this.command;
    }

}
