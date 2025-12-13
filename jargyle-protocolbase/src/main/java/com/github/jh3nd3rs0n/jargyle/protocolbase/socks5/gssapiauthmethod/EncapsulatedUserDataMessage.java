package com.github.jh3nd3rs0n.jargyle.protocolbase.socks5.gssapiauthmethod;

import com.github.jh3nd3rs0n.jargyle.common.number.UnsignedByte;
import com.github.jh3nd3rs0n.jargyle.protocolbase.internal.UnsignedByteIoHelper;
import com.github.jh3nd3rs0n.jargyle.protocolbase.socks5.Socks5Exception;

import java.io.ByteArrayInputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;

/**
 * A message with user data encapsulated by GSS-API.
 */
public final class EncapsulatedUserDataMessage {

    /**
     * The {@code byte} value associated with this type of message.
     */
    public static final byte MESSAGE_TYPE_BYTE_VALUE = (byte) 0x03;

    /**
     * The {@code UnsignedByte} associated with this type of message.
     */
    public static final UnsignedByte MESSAGE_TYPE = UnsignedByte.valueOf(
            MESSAGE_TYPE_BYTE_VALUE);

    /**
     * The {@code Version} of this {@code EncapsulatedUserDataMessage}.
     */
    private final Version version;

    /**
     * The {@code UnsignedByte} associated with this type of message.
     */
    private final UnsignedByte messageType;

    /**
     * The {@code Token} of this {@code EncapsulatedUserDataMessage}.
     */
    private final Token token;

    /**
     * Constructs an {@code EncapsulatedUserDataMessage} with the provided
     * {@code Token}.
     *
     * @param tkn the provided {@code Token}
     */
    private EncapsulatedUserDataMessage(final Token tkn) {
        this.version = Version.V1;
        this.messageType = MESSAGE_TYPE;
        this.token = tkn;
    }

    /**
     * Returns a new {@code EncapsulatedUserDataMessage} with the provided
     * {@code Token}.
     *
     * @param tkn the provided {@code Token}
     * @return a new {@code EncapsulatedUserDataMessage} with the provided
     * {@code Token}
     */
    public static EncapsulatedUserDataMessage newInstance(final Token tkn) {
        return new EncapsulatedUserDataMessage(Objects.requireNonNull(tkn));
    }

    /**
     * Returns a new {@code EncapsulatedUserDataMessage} from the provided
     * {@code InputStream}. An {@code EOFException} is thrown if the end of
     * the provided {@code InputStream} is reached.
     *
     * @param in the provided {@code InputStream}
     * @return a new {@code EncapsulatedUserDataMessage}
     * @throws IOException if the end of the provided {@code InputStream}
     *                     is reached ({@code EOFException}) or if an I/O
     *                     error occurs
     */
    public static EncapsulatedUserDataMessage newInstanceFrom(
            final InputStream in) throws IOException {
        VersionIoHelper.readVersionFrom(in);
        UnsignedByte mTyp = UnsignedByteIoHelper.readUnsignedByteFrom(in);
        if (!mTyp.equals(MESSAGE_TYPE)) {
            throw new Socks5Exception(String.format(
                    "expected message type is %s. " +
                            "actual message type is %s",
                    Integer.toHexString(MESSAGE_TYPE.intValue()),
                    Integer.toHexString(mTyp.intValue())));
        }
        return EncapsulatedUserDataMessage.newInstance(
                Token.newInstanceFrom(in));
    }

    /**
     * Returns a new {@code EncapsulatedUserDataMessage} from the provided
     * {@code byte} array. An {@code IllegalArgumentException} is thrown if
     * the provided {@code byte} array is invalid.
     *
     * @param b the provided {@code byte} array
     * @return a new {@code EncapsulatedUserDataMessage}
     */
    public static EncapsulatedUserDataMessage newInstanceFrom(final byte[] b) {
        ByteArrayInputStream in = new ByteArrayInputStream(b);
        EncapsulatedUserDataMessage message;
        try {
            message = newInstanceFrom(in);
        } catch (EOFException e) {
            throw new IllegalArgumentException("unexpected end of byte array");
        } catch (IOException e) {
            throw new IllegalArgumentException(e);
        }
        if (in.available() > 0) {
            throw new IllegalArgumentException(
                    "unknown additional bytes in byte array");
        }
        return message;
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
        EncapsulatedUserDataMessage other = (EncapsulatedUserDataMessage) obj;
        return this.token.equals(other.token);
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
     * Returns the {@code Token} of this {@code EncapsulatedUserDataMessage}.
     *
     * @return the {@code Token} of this {@code EncapsulatedUserDataMessage}
     */
    public Token getToken() {
        return this.token;
    }

    /**
     * Returns the {@code Version} of this {@code EncapsulatedUserDataMessage}.
     *
     * @return the {@code Version} of this {@code EncapsulatedUserDataMessage}
     */
    public Version getVersion() {
        return this.version;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + this.version.hashCode();
        result = prime * result + this.messageType.hashCode();
        result = prime * result + this.token.hashCode();
        return result;
    }

    /**
     * Returns the {@code byte} array of this
     * {@code EncapsulatedUserDataMessage}.
     *
     * @return the {@code byte} array of this
     * {@code EncapsulatedUserDataMessage}
     */
    public byte[] toByteArray() {
        byte[] tokenByteArray = this.token.toByteArray();
        byte[] arr = new byte[2 + tokenByteArray.length];
        arr[0] = this.version.byteValue();
        arr[1] = this.messageType.byteValue();
        System.arraycopy(tokenByteArray, 0, arr, 2, tokenByteArray.length);
        return arr;
    }

    /**
     * Returns the {@code String} representation of this
     * {@code EncapsulatedUserDataMessage}.
     *
     * @return the {@code String} representation of this
     * {@code EncapsulatedUserDataMessage}
     */
    @Override
    public String toString() {
        return this.getClass().getSimpleName() +
                " [version=" +
                this.version +
                ", messageType=" +
                this.messageType +
                ", token=" +
                this.token +
                "]";
    }

}
