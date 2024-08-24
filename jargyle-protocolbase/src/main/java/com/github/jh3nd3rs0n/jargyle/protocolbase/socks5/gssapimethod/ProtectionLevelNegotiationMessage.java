package com.github.jh3nd3rs0n.jargyle.protocolbase.socks5.gssapimethod;

import com.github.jh3nd3rs0n.jargyle.common.number.UnsignedByte;
import com.github.jh3nd3rs0n.jargyle.protocolbase.internal.UnsignedByteIoHelper;
import com.github.jh3nd3rs0n.jargyle.protocolbase.socks5.Socks5Exception;

import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;

/**
 * A message sent by the client or the server during the security context
 * protection level negotiation process.
 */
public final class ProtectionLevelNegotiationMessage {

    /**
     * The {@code byte} value associated with this type of message.
     */
    public static final byte MESSAGE_TYPE_BYTE_VALUE = (byte) 0x02;

    /**
     * The {@code UnsignedByte} associated with this type of message.
     */
    public static final UnsignedByte MESSAGE_TYPE = UnsignedByte.valueOf(
            MESSAGE_TYPE_BYTE_VALUE);

    /**
     * The {@code Version} of this {@code ProtectionLevelNegotiationMessage}.
     */
    private final Version version;

    /**
     * The {@code UnsignedByte} associated with this type of message.
     */
    private final UnsignedByte messageType;

    /**
     * The {@code Token} of this {@code ProtectionLevelNegotiationMessage}.
     */
    private final Token token;

    /**
     * Constructs a {@code ProtectionLevelNegotiationMessage} with the
     * provided {@code Token}.
     *
     * @param tkn the provided {@code Token}
     */
    private ProtectionLevelNegotiationMessage(final Token tkn) {
        this.version = Version.V1;
        this.messageType = MESSAGE_TYPE;
        this.token = tkn;
    }

    /**
     * Returns a new {@code ProtectionLevelNegotiationMessage} with the
     * provided {@code Token}.
     *
     * @param tkn the provided {@code Token}
     * @return a new {@code ProtectionLevelNegotiationMessage} with the
     * provided {@code Token}
     */
    public static ProtectionLevelNegotiationMessage newInstance(
            final Token tkn) {
        return new ProtectionLevelNegotiationMessage(
                Objects.requireNonNull(tkn));
    }

    /**
     * Returns a new {@code ProtectionLevelNegotiationMessage} from the
     * provided {@code InputStream}. An {@code EOFException} is thrown if the
     * end of the provided {@code InputStream} is reached.
     *
     * @param in the provided {@code InputStream}
     * @return a new {@code ProtectionLevelNegotiationMessage}
     * @throws IOException if the end of the provided {@code InputStream}
     *                     is reached ({@code EOFException}) or if an I/O
     *                     error occurs
     */
    public static ProtectionLevelNegotiationMessage newInstanceFrom(
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
        return ProtectionLevelNegotiationMessage.newInstance(
                Token.newInstanceFrom(in));
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
        ProtectionLevelNegotiationMessage other =
                (ProtectionLevelNegotiationMessage) obj;
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
     * Returns the {@code Token} of this
     * {@code ProtectionLevelNegotiationMessage}.
     *
     * @return the {@code Token} of this
     * {@code ProtectionLevelNegotiationMessage}
     */
    public Token getToken() {
        return this.token;
    }

    /**
     * Returns the {@code Version} of this
     * {@code ProtectionLevelNegotiationMessage}.
     *
     * @return the {@code Version} of this
     * {@code ProtectionLevelNegotiationMessage}
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
     * {@code ProtectionLevelNegotiationMessage}.
     *
     * @return the {@code byte} array of this
     * {@code ProtectionLevelNegotiationMessage}
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
     * {@code ProtectionLevelNegotiationMessage}.
     *
     * @return the {@code String} representation of this
     * {@code ProtectionLevelNegotiationMessage}
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
