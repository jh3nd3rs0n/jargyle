package com.github.jh3nd3rs0n.jargyle.protocolbase.socks5.gssapimethod;

import com.github.jh3nd3rs0n.jargyle.common.number.UnsignedByte;
import com.github.jh3nd3rs0n.jargyle.common.number.UnsignedShort;
import com.github.jh3nd3rs0n.jargyle.protocolbase.internal.UnsignedByteIoHelper;
import com.github.jh3nd3rs0n.jargyle.protocolbase.internal.UnsignedShortIoHelper;
import com.github.jh3nd3rs0n.jargyle.protocolbase.socks5.Socks5Exception;

import java.io.*;
import java.util.Arrays;
import java.util.Objects;

public final class Message {

	public static final int MAX_TOKEN_LENGTH = 65535;

	private final Version version;
	private final MessageType messageType;
	private final byte[] token;
	
	private Message(final MessageType mType, final byte[] tkn) {
		this.version = Version.V1;
		this.messageType = mType;
		this.token = Arrays.copyOf(tkn, tkn.length);
	}

    public static Message newInstance(
            final MessageType mType, final byte[] tkn) {
        if (tkn.length > MAX_TOKEN_LENGTH) {
            throw new IllegalArgumentException(String.format(
                    "token must be no more than %s bytes",
                    MAX_TOKEN_LENGTH));
        }
        return new Message(Objects.requireNonNull(mType), tkn);
    }

    public static Message newInstanceFrom(final byte[] b) {
        Message message;
        try {
            message = newInstanceFrom(new ByteArrayInputStream(b));
        } catch (IOException e) {
            throw new IllegalArgumentException(e);
        }
        return message;
    }

    public static Message newInstanceFrom(
            final InputStream in) throws IOException {
        readVersionFrom(in);
        MessageType mType = readMessageTypeFrom(in);
        if (mType.equals(MessageType.ABORT)) {
            return newInstance(mType, new byte[]{});
        }
        UnsignedShort len = UnsignedShortIoHelper.readUnsignedShortFrom(
                in);
        byte[] token = new byte[len.intValue()];
        int bytesRead = in.read(token);
        if (bytesRead != len.intValue()) {
            throw new EOFException(String.format(
                    "expected token length is %s byte(s). "
                            + "actual token length is %s byte(s)",
                    len.intValue(), bytesRead));
        }
        return newInstance(mType, token);
    }

    private static MessageType readMessageTypeFrom(
            final InputStream in) throws IOException {
        UnsignedByte b = UnsignedByteIoHelper.readUnsignedByteFrom(in);
        MessageType messageType;
        try {
            messageType = MessageType.valueOfByte(b.byteValue());
        } catch (IllegalArgumentException e) {
            throw new Socks5Exception(e);
        }
        return messageType;
    }

    private static void readVersionFrom(
            final InputStream in) throws IOException {
        UnsignedByte b = UnsignedByteIoHelper.readUnsignedByteFrom(in);
        try {
            Version.valueOfByte(b.byteValue());
        } catch (IllegalArgumentException e) {
            throw new Socks5Exception(e);
        }
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
		Message other = (Message) obj;
		if (!this.messageType.equals(other.messageType)) {
			return false;
		}
		if (!Arrays.equals(this.token, other.token)) {
			return false;
		}
		return true;
	}

	public MessageType getMessageType() {
		return this.messageType;
	}
	
	public byte[] getToken() {
		return Arrays.copyOf(this.token, this.token.length);
	}

	public Version getVersion() {
		return this.version;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + this.messageType.hashCode();
		result = prime * result + Arrays.hashCode(this.token);
		return result;
	}

    public byte[] toByteArray() {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        out.write(UnsignedByte.valueOf(
                this.version.byteValue()).intValue());
        out.write(UnsignedByte.valueOf(
                this.messageType.byteValue()).intValue());
        if (this.messageType.equals(MessageType.ABORT)) {
            return out.toByteArray();
        }
        int tokenLength = this.token.length;
        try {
            out.write(UnsignedShortIoHelper.toByteArray(
                    UnsignedShort.valueOf(tokenLength)));
        } catch (IOException e) {
            throw new AssertionError(e);
        }
        try {
            out.write(this.token);
        } catch (IOException e) {
            throw new AssertionError(e);
        }
        return out.toByteArray();
    }

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append(this.getClass().getSimpleName())
			.append(" [version=")
			.append(this.version)
			.append(", messageType=")
			.append(this.messageType)
			.append("]");
		return builder.toString();
	}

}
