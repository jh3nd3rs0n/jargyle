package com.github.jh3nd3rs0n.jargyle.protocolbase.socks5;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import com.github.jh3nd3rs0n.jargyle.common.number.UnsignedByte;

public final class ClientMethodSelectionMessage {
	
	static final class Params {
		Version version;
		Methods methods;
		byte[] byteArray;
	}
	
	public static ClientMethodSelectionMessage newInstance(final byte[] b) {
		ClientMethodSelectionMessage cmsm = null;
		try {
			cmsm = ClientMethodSelectionMessageInputHelper.readClientMethodSelectionMessageFrom(
					new ByteArrayInputStream(b));
		} catch (IOException e) {
			throw new IllegalArgumentException(e);
		}
		return cmsm;
	}
	
	public static ClientMethodSelectionMessage newInstance(
			final Methods methods) {
		List<Method> methodsList = methods.toList();
		if (methodsList.size() > UnsignedByte.MAX_INT_VALUE) {
			throw new IllegalArgumentException(String.format(
					"number of methods must be no more than %s",
					UnsignedByte.MAX_INT_VALUE));
		}
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		Version version = Version.V5;
		out.write(UnsignedByte.valueOf(version.byteValue()).intValue());
		out.write(methodsList.size());
		for (Method method : methodsList) {
			out.write(UnsignedByte.valueOf(method.byteValue()).intValue());
		}
		Params params = new Params();
		params.version = version;
		params.methods = methods;
		params.byteArray = out.toByteArray();
		return new ClientMethodSelectionMessage(params);
	}
	
	private final Version version;
	private final Methods methods;
	private final byte[] byteArray;
	
	ClientMethodSelectionMessage(final Params params) {
		this.version = params.version;
		this.methods = params.methods;
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
		ClientMethodSelectionMessage other = (ClientMethodSelectionMessage) obj;
		if (!Arrays.equals(this.byteArray, other.byteArray)) {
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
			.append(", methods=")
			.append(this.methods)
			.append("]");
		return builder.toString();
	}
	
}
