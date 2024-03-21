package com.github.jh3nd3rs0n.jargyle.protocolbase.socks5;

import com.github.jh3nd3rs0n.jargyle.common.number.UnsignedByte;
import com.github.jh3nd3rs0n.jargyle.protocolbase.internal.UnsignedByteIoHelper;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public final class ClientMethodSelectionMessage {

	private final Version version;
	private final Methods methods;

	private ClientMethodSelectionMessage(final Methods meths) {
		this.version = Version.V5;
		this.methods = meths;
	}

	public static ClientMethodSelectionMessage newInstance(
			final Methods meths) {
		List<Method> methsList = Objects.requireNonNull(meths).toList();
		if (methsList.size() > UnsignedByte.MAX_INT_VALUE) {
			throw new IllegalArgumentException(String.format(
					"number of methods must be no more than %s",
					UnsignedByte.MAX_INT_VALUE));
		}
		return new ClientMethodSelectionMessage(meths);
	}

	public static ClientMethodSelectionMessage newInstanceFrom(
			final byte[] b) {
		ClientMethodSelectionMessage cmsm;
		try {
			cmsm = newInstanceFrom(new ByteArrayInputStream(b));
		} catch (IOException e) {
			throw new IllegalArgumentException(e);
		}
		return cmsm;
	}
    public static ClientMethodSelectionMessage newInstanceFrom(
            final InputStream in) throws IOException {
        VersionIoHelper.readVersionFrom(in);
        UnsignedByte methodCount = UnsignedByteIoHelper.readUnsignedByteFrom(
                in);
        List<Method> meths = new ArrayList<Method>();
        for (int i = 0; i < methodCount.intValue(); i++) {
            Method meth;
            try {
                meth = MethodIoHelper.readMethodFrom(in);
            } catch (Socks5Exception e) {
                continue;
            }
            meths.add(meth);
        }
        return newInstance(Methods.of(meths));
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
		ClientMethodSelectionMessage other = (ClientMethodSelectionMessage) obj;
		if (!this.methods.equals(other.methods)) {
			return false;
		}
		return true;
	}

	public Methods getMethods() {
		return this.methods;
	}
	
	public Version getVersion() {
		return this.version;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + this.methods.hashCode();
		return result;
	}

	public byte[] toByteArray() {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		out.write(UnsignedByte.valueOf(this.version.byteValue()).intValue());
		List<Method> methodsList = this.methods.toList();
		out.write(methodsList.size());
		for (Method method : methodsList) {
			out.write(UnsignedByte.valueOf(method.byteValue()).intValue());
		}
		return out.toByteArray();
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append(this.getClass().getSimpleName())
			.append(" [version=")
			.append(this.version)
			.append(", methods=")
			.append(this.methods)
			.append("]");
		return builder.toString();
	}
	
}
