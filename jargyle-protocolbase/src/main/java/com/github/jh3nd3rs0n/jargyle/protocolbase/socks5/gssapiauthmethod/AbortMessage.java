package com.github.jh3nd3rs0n.jargyle.protocolbase.socks5.gssapiauthmethod;

import com.github.jh3nd3rs0n.jargyle.common.number.UnsignedByte;

/**
 * A message sent by the server refusing the client's connection for any
 * reason (GSS-API authentication failure or otherwise).
 */
public enum AbortMessage {

    /**
     * The singleton instance of an {@code AbortMessage}.
     */
    INSTANCE;

    /**
     * The {@code byte} value associated with this type of message.
     */
    public static final byte MESSAGE_TYPE_BYTE_VALUE = (byte) 0xff;

    /**
     * The {@code UnsignedByte} associated with this type of message.
     */
    public static final UnsignedByte MESSAGE_TYPE = UnsignedByte.valueOf(
            MESSAGE_TYPE_BYTE_VALUE);

    /**
     * The {@code Version} of this {@code AbortMessage}.
     */
    private final Version version;

    /**
     * The {@code UnsignedByte} associated with this type of message.
     */
    private final UnsignedByte messageType;

    /**
     * Constructs an {@code AbortMessage}.
     */
    AbortMessage() {
        this.version = Version.V1;
        this.messageType = UnsignedByte.valueOf(MESSAGE_TYPE_BYTE_VALUE);
    }

    /**
     * Returns the {@code UnsignedByte} associated with this type of message.
     *
     * @return the {@code UnsignedByte} associated with this type of message
     */
    public UnsignedByte getMessageType() {
        return this.messageType;
    }

    /**
     * Returns the {@code Version} of this {@code AbortMessage}.
     *
     * @return the {@code Version} of this {@code AbortMessage}
     */
    public Version getVersion() {
        return this.version;
    }

    /**
     * Returns the {@code byte} array of this {@code AbortMessage}.
     *
     * @return the {@code byte} array of this {@code AbortMessage}
     */
    public byte[] toByteArray() {
        return new byte[] {
                this.version.byteValue(),
                this.messageType.byteValue()
        };
    }

    /**
     * Returns the {@code String} representation of this {@code AbortMessage}.
     *
     * @return the {@code String} representation of this {@code AbortMessage}
     */
    @Override
    public String toString() {
        return this.getClass().getSimpleName() +
                " [version=" +
                this.version +
                ", messageType=" +
                this.messageType +
                "]";
    }

}
