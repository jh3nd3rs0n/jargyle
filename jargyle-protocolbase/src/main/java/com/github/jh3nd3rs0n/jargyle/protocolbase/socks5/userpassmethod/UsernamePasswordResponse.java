package com.github.jh3nd3rs0n.jargyle.protocolbase.socks5.userpassmethod;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Arrays;

import com.github.jh3nd3rs0n.jargyle.common.number.UnsignedByte;

public final class UsernamePasswordResponse {

	static final class Params {
		Version version;
		byte status;
		byte[] byteArray;
	}
	
	public static final byte STATUS_SUCCESS = 0x0;
	
	public static UsernamePasswordResponse newInstance(
			final byte status) {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		Version version = Version.V1;
		out.write(UnsignedByte.newInstance(version.byteValue()).intValue());
		out.write(UnsignedByte.newInstance(status).intValue());
		Params params = new Params();
		params.version = version;
		params.status = status;
		params.byteArray = out.toByteArray();
		return new UsernamePasswordResponse(params);
	}
	
	public static UsernamePasswordResponse newInstance(final byte[] b) {
		UsernamePasswordResponse response = null;
		try {
			response = new UsernamePasswordResponseInputStream(
					new ByteArrayInputStream(b)).readUsernamePasswordResponse();
		} catch (IOException e) {
			throw new IllegalArgumentException(e);
		}
		return response;
	}
	
	private final Version version;
	private final byte status;
	private final byte[] byteArray;
	
	UsernamePasswordResponse(final Params params) {
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
