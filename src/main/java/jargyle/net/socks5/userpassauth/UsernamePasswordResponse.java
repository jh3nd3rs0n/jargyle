package jargyle.net.socks5.userpassauth;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

import jargyle.util.UnsignedByte;

public final class UsernamePasswordResponse {

	private static final class Params {
		private Version version;
		private byte status;
		private byte[] byteArray;
	}
	
	public static final byte STATUS_SUCCESS = 0x0;
	
	public static UsernamePasswordResponse newInstance(
			final byte status) {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		Version version = Version.V1;
		out.write(version.byteValue());
		out.write(status);
		Params params = new Params();
		params.version = version;
		params.status = status;
		params.byteArray = out.toByteArray();
		return new UsernamePasswordResponse(params);
	}
	
	public static UsernamePasswordResponse newInstance(final byte[] b) {
		UsernamePasswordResponse usernamePasswordResponse;
		try {
			usernamePasswordResponse = newInstanceFrom(new ByteArrayInputStream(b));
		} catch (IOException e) {
			throw new IllegalArgumentException(e);
		}
		return usernamePasswordResponse;
	}
	
	public static UsernamePasswordResponse newInstanceFrom(
			final InputStream in) throws IOException {
		int b = -1;
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		b = in.read();
		Version ver = null;
		try {
			ver = Version.valueOf(
					(byte) UnsignedByte.newInstance(b).intValue());
		} catch (IllegalArgumentException e) {
			throw new IOException(e);
		}
		out.write(b);
		b = in.read();
		byte status;
		try {
			status = (byte) UnsignedByte.newInstance(b).intValue();
		} catch (IllegalArgumentException e) {
			throw new IOException(e);
		}
		out.write(b);
		Params params = new Params();
		params.version = ver;
		params.status = status;
		params.byteArray = out.toByteArray();
		return new UsernamePasswordResponse(params);
	}
	
	private final Version version;
	private final byte status;
	private final byte[] byteArray;
	
	private UsernamePasswordResponse(final Params params) {
		this.version = params.version;
		this.status = params.status;
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
		UsernamePasswordResponse other = (UsernamePasswordResponse) obj;
		if (!Arrays.equals(this.byteArray, other.byteArray)) {
			return false;
		}
		return true;
	}

	public byte getStatus() {
		return this.status;
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
			.append(", status=")
			.append(this.status)
			.append("]");
		return builder.toString();
	}
	
}
