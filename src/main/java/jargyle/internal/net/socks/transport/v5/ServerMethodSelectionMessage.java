package jargyle.internal.net.socks.transport.v5;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

import jargyle.net.socks.transport.v5.Method;
import jargyle.net.socks.transport.v5.Version;
import jargyle.util.UnsignedByte;

public final class ServerMethodSelectionMessage {

	private static final class Params {
		private Version version;
		private Method method;
		private byte[] byteArray;
	}
	
	public static ServerMethodSelectionMessage newInstance(final byte[] b) {
		ServerMethodSelectionMessage smsm;
		try {
			smsm = newInstanceFrom(new ByteArrayInputStream(b));
		} catch (IOException e) {
			throw new IllegalArgumentException(e);
		}
		return smsm;
	}
	
	public static ServerMethodSelectionMessage newInstance(
			final Method method) {
		Params params = new Params();
		Version version = Version.V5;
		params.version = version;
		params.method = method;
		params.byteArray = new byte[] { version.byteValue(), method.byteValue() };
		return new ServerMethodSelectionMessage(params);
	}
	
	public static ServerMethodSelectionMessage newInstanceFrom(
			final InputStream in) throws IOException {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		Version ver = Version.valueOfByteFrom(in);
		out.write(UnsignedByte.newInstance(ver.byteValue()).intValue());
		Method meth = Method.valueOfByteFrom(in);
		out.write(UnsignedByte.newInstance(meth.byteValue()).intValue());
		Params params = new Params();
		params.version = ver;
		params.method = meth;
		params.byteArray = out.toByteArray();
		return new ServerMethodSelectionMessage(params);
	}
	
	private final Version version;
	private final Method method;
	private final byte[] byteArray;
	
	private ServerMethodSelectionMessage(final Params params) {
		this.version = params.version;
		this.method = params.method;
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
		ServerMethodSelectionMessage other = (ServerMethodSelectionMessage) obj;
		if (!Arrays.equals(this.byteArray, other.byteArray)) {
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
			.append(", method=")
			.append(this.method)
			.append("]");
		return builder.toString();
	}
	
}
