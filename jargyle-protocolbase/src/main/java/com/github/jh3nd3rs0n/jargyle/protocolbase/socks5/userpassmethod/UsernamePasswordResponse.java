package com.github.jh3nd3rs0n.jargyle.protocolbase.socks5.userpassmethod;

import com.github.jh3nd3rs0n.jargyle.common.number.UnsignedByte;
import com.github.jh3nd3rs0n.jargyle.protocolbase.internal.UnsignedByteIoHelper;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public final class UsernamePasswordResponse {

	public static final byte STATUS_SUCCESS = 0x0;

	private final Version version;
	private final byte status;

	private UsernamePasswordResponse(final byte stat) {
		this.version = Version.V1;
		this.status = stat;
	}

	public static UsernamePasswordResponse newInstance(final byte stat) {
		return new UsernamePasswordResponse(stat);
	}

	public static UsernamePasswordResponse newInstanceFrom(
			final byte[] b) {
		UsernamePasswordResponse response;
		try {
			response = newInstanceFrom(new ByteArrayInputStream(b));
		} catch (IOException e) {
			throw new IllegalArgumentException(e);
		}
		return response;
	}

    public static UsernamePasswordResponse newInstanceFrom(
            final InputStream in) throws IOException {
        VersionIoHelper.readVersionFrom(in);
        UnsignedByte status = UnsignedByteIoHelper.readUnsignedByteFrom(in);
        return newInstance(status.byteValue());
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
		if (this.status != other.status) {
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
		result = prime * result + this.status;
		return result;
	}

	public byte[] toByteArray() {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		out.write(UnsignedByte.valueOf(
				this.version.byteValue()).intValue());
		out.write(UnsignedByte.valueOf(this.status).intValue());
		return out.toByteArray();
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
