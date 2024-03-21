package com.github.jh3nd3rs0n.jargyle.protocolbase.socks5;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;

public final class ServerMethodSelectionMessage {

	private final Version version;
	private final Method method;

	private ServerMethodSelectionMessage(final Method meth) {
		this.version = Version.V5;
		this.method = meth;
	}

	public static ServerMethodSelectionMessage newInstance(final Method meth) {
		return new ServerMethodSelectionMessage(Objects.requireNonNull(meth));
	}

    public static ServerMethodSelectionMessage newInstanceFrom(
            final byte[] b) {
        ServerMethodSelectionMessage smsm;
        try {
            smsm = newInstanceFrom(new ByteArrayInputStream(b));
        } catch (IOException e) {
            throw new IllegalArgumentException(e);
        }
        return smsm;
    }

    public static ServerMethodSelectionMessage newInstanceFrom(
            final InputStream in) throws IOException {
        VersionIoHelper.readVersionFrom(in);
        Method meth = MethodIoHelper.readMethodFrom(in);
        return newInstance(meth);
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
		ServerMethodSelectionMessage other = (ServerMethodSelectionMessage) obj;
		if (!this.method.equals(other.method)) {
			return false;
		}
		return true;
	}

	public Method getMethod() {
		return this.method;
	}
	
	public Version getVersion() {
		return this.version;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + this.method.hashCode();
		return result;
	}

	public byte[] toByteArray() {
		return new byte[] { this.version.byteValue(), this.method.byteValue() };
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append(this.getClass().getSimpleName())
			.append(" [version=")
			.append(this.version)
			.append(", method=")
			.append(this.method)
			.append("]");
		return builder.toString();
	}
	
}
