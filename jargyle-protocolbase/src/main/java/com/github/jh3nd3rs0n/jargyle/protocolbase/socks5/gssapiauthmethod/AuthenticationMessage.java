package com.github.jh3nd3rs0n.jargyle.protocolbase.socks5.gssapiauthmethod;

import com.github.jh3nd3rs0n.jargyle.common.number.UnsignedByte;
import com.github.jh3nd3rs0n.jargyle.protocolbase.internal.UnsignedByteIoHelper;
import com.github.jh3nd3rs0n.jargyle.protocolbase.socks5.Socks5Exception;

import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;

/**
 * A message sent by the client or the server during the authentication
 * process.
 */
public final class AuthenticationMessage {

    /**
     * The {@code byte} value associated with this type of message.
     */
    public static final byte MESSAGE_TYPE_BYTE_VALUE = (byte) 0x01;

    /**
     * The {@code UnsignedByte} associated with this type of message.
     */
    public static final UnsignedByte MESSAGE_TYPE = UnsignedByte.valueOf(
            MESSAGE_TYPE_BYTE_VALUE);

    /**
     * The {@code Version} of this {@code AuthenticationMessage}.
     */
    private final Version version;

    /**
     * The {@code UnsignedByte} associated with this type of message.
     */
    private final UnsignedByte messageType;

    /**
     * The {@code Token} of this {@code AuthenticationMessage}.
     */
    private final Token token;

    /**
     * Constructs an {@code AuthenticationMessage} with the provided
     * {@code Token}.
     *
     * @param tkn the provided {@code Token}
     */
    private AuthenticationMessage(final Token tkn) {
        this.version = Version.V1;
        this.messageType = MESSAGE_TYPE;
        this.token = tkn;
    }

    /**
     * Returns a new {@code AuthenticationMessage} with the provided
     * {@code Token}.
     *
     * @param tkn the provided {@code Token}
     * @return a new {@code AuthenticationMessage} with the provided
     * {@code Token}
     */
    public static AuthenticationMessage newInstance(final Token tkn) {
        return new AuthenticationMessage(Objects.requireNonNull(tkn));
    }

    /**
     * Returns a new {@code AuthenticationMessage} from the provided
     * {@code InputStream} from the client. An {@code EOFException} is thrown
     * if the end of the provided {@code InputStream} is reached.
     *
     * @param in the provided {@code InputStream} from the client
     * @return a new {@code AuthenticationMessage}
     * @throws IOException if the end of the provided {@code InputStream}
     *                     is reached ({@code EOFException}) or if an I/O
     *                     error occurs
     */
    public static AuthenticationMessage newInstanceFromClient(
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
        return AuthenticationMessage.newInstance(Token.newInstanceFrom(in));
    }

    /**
     * Returns a new {@code AuthenticationMessage} from the provided
     * {@code InputStream} from the server. An {@code AbortMessageException}
     * is thrown if an {@code AbortMessage} is received. An
     * {@code EOFException} is thrown if the end of the provided
     * {@code InputStream} is reached.
     *
     * @param in the provided {@code InputStream} from the server
     * @return a new {@code AuthenticationMessage}
     * @throws IOException if an {@code AbortMessage} is received
     *                     ({@code AbortMessageException}), if the end of the
     *                     provided {@code InputStream} is reached
     *                     ({@code EOFException}), or if an I/O error occurs
     */
    public static AuthenticationMessage newInstanceFromServer(
            final InputStream in) throws IOException {
        VersionIoHelper.readVersionFrom(in);
        UnsignedByte mTyp = UnsignedByteIoHelper.readUnsignedByteFrom(in);
        if (mTyp.equals(AbortMessage.MESSAGE_TYPE)) {
            throw new AbortMessageException();
        }
        if (!mTyp.equals(MESSAGE_TYPE)) {
            throw new Socks5Exception(String.format(
                    "expected message type is %s or %s. " +
                            "actual message type is %s",
                    Integer.toHexString(AbortMessage.MESSAGE_TYPE.intValue()),
                    Integer.toHexString(MESSAGE_TYPE.intValue()),
                    Integer.toHexString(mTyp.intValue())));
        }
        return AuthenticationMessage.newInstance(Token.newInstanceFrom(in));
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
        AuthenticationMessage other = (AuthenticationMessage) obj;
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
     * Returns the {@code Token} of this {@code AuthenticationMessage}.
     *
     * @return the {@code Token} of this {@code AuthenticationMessage}
     */
    public Token getToken() {
        return this.token;
    }

    /**
     * Returns the {@code Version} of this {@code AuthenticationMessage}.
     *
     * @return the {@code Version} of this {@code AuthenticationMessage}
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
     * Returns the {@code byte} array of this {@code AuthenticationMessage}.
     *
     * @return the {@code byte} array of this {@code AuthenticationMessage}
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
     * {@code AuthenticationMessage}.
     *
     * @return the {@code String} representation of this
     * {@code AuthenticationMessage}
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
