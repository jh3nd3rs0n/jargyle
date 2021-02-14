package jargyle.net.socks.transport.v5.gssapiauth;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Objects;

import jargyle.util.UnsignedByte;
import jargyle.util.UnsignedShort;

public final class Message {

	private static final class Params {
		private Version version;
		private MessageType messageType;
		private int tokenStartIndex;
		private byte[] byteArray;
	}
	
	public static final int MAX_HEADER_LENGTH = 4;
	
	public static final int MAX_TOKEN_LENGTH = 65535;
	
	public static final int MAX_LENGTH = MAX_HEADER_LENGTH + MAX_TOKEN_LENGTH;
	
	public static Message newInstance(final byte[] b) {
		Message message;
		try {
			message = newInstanceFrom(new ByteArrayInputStream(b));
		} catch (IOException e) {
			throw new IllegalArgumentException(e);
		}
		return message;
	}
	
	public static Message newInstance(
			final MessageType messageType,
			final byte[] token) {
		Objects.requireNonNull(messageType, "message type must not be null");
		int tknStartIndex = -1;
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		tknStartIndex++;
		Version version = Version.V1;
		out.write(version.byteValue());
		tknStartIndex++;
		out.write(messageType.byteValue());
		if (messageType.equals(MessageType.ABORT)) {
			tknStartIndex++;
		} else {
			Objects.requireNonNull(token, "token must not be null");
			int tokenLength = token.length;
			if (tokenLength > MAX_TOKEN_LENGTH) {
				throw new IllegalArgumentException(String.format(
						"token must be no more than %s bytes", MAX_TOKEN_LENGTH));
			}
			byte[] bytes = UnsignedShort.newInstance(tokenLength).toByteArray();
			tknStartIndex += bytes.length;
			try {
				out.write(bytes);
			} catch (IOException e) {
				throw new AssertionError(e);
			}
			byte[] tkn = Arrays.copyOf(token, tokenLength);
			tknStartIndex++;
			try {
				out.write(tkn);
			} catch (IOException e) {
				throw new AssertionError(e);
			}
		}
		Params params = new Params();
		params.version = version;
		params.messageType = messageType;
		params.tokenStartIndex = tknStartIndex;
		params.byteArray = out.toByteArray();
		return new Message(params);
	}
	
	public static Message newInstanceFrom(
			final InputStream in) throws IOException {
		int b = -1;
		int tknStartIndex = -1;
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		b = in.read();
		Version ver = null;
		try {
			ver = Version.valueOf(
					(byte) UnsignedByte.newInstance(b).intValue());
		} catch (IllegalArgumentException e) {
			throw new IOException(e);
		}
		tknStartIndex++;
		out.write(b);
		b = in.read();
		MessageType mType = null; 
		try {
			mType = MessageType.valueOf(
					(byte) UnsignedByte.newInstance(b).intValue());
		} catch (IllegalArgumentException e) {
			throw new IOException(e);
		}
		tknStartIndex++;
		out.write(b);
		if (mType.equals(MessageType.ABORT)) {
			tknStartIndex++;
		} else {
			byte[] bytes = new byte[UnsignedShort.BYTE_ARRAY_LENGTH];
			int bytesRead = in.read(bytes);
			bytes = Arrays.copyOf(bytes, bytesRead);
			int len; 
			try {
				len = UnsignedShort.newInstance(bytes).intValue();
			} catch (IllegalArgumentException e) {
				throw new IOException(e);
			}
			tknStartIndex += bytes.length;
			out.write(bytes);
			bytes = new byte[len];
			bytesRead = in.read(bytes);
			if (bytesRead != len) {
				throw new IOException(String.format(
						"expected token length is %s byte(s). "
						+ "actual token length is %s byte(s)", 
						len, bytesRead));				
			}
			bytes = Arrays.copyOf(bytes, bytesRead);
			tknStartIndex++;
			out.write(bytes);
		}
		Params params = new Params();
		params.version = ver;
		params.messageType = mType;
		params.tokenStartIndex = tknStartIndex;
		params.byteArray = out.toByteArray();
		return new Message(params);
	}
	
	private final Version version;
	private final MessageType messageType;
	private final int tokenStartIndex;
	private final byte[] byteArray;
	
	private Message(final Params params) {
		this.version = params.version;
		this.messageType = params.messageType;
		this.tokenStartIndex = params.tokenStartIndex;
		this.byteArray = params.byteArray;
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
		if (!Arrays.equals(this.byteArray, other.byteArray)) {
			return false;
		}
		return true;
	}

	public MessageType getMessageType() {
		return this.messageType;
	}
	
	public byte[] getToken() {
		return Arrays.copyOfRange(
				this.byteArray, this.tokenStartIndex, this.byteArray.length);
	}

	public int getTokenStartIndex() {
		return this.tokenStartIndex;
	}

	public Version getVersion() {
		return this.version;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Arrays.hashCode(this.byteArray);
		return result;
	}

	public byte[] toByteArray() {
		return Arrays.copyOf(this.byteArray, this.byteArray.length);
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append(this.getClass().getSimpleName())
			.append(" [version=")
			.append(this.version)
			.append(", messageType=")
			.append(this.messageType)
			.append(", tokenStartIndex=")
			.append(this.tokenStartIndex)
			.append(", token=")
			.append(Arrays.toString(this.getToken()))
			.append("]");
		return builder.toString();
	}

}
