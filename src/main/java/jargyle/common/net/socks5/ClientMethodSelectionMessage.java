package jargyle.common.net.socks5;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import jargyle.common.util.UnsignedByte;

public final class ClientMethodSelectionMessage {
	
	private static final class Params {
		private Version version;
		private Set<Method> methods;
		private byte[] byteArray;
	}
	
	private static final int MIN_METHODS_LENGTH = 1;
	
	public static ClientMethodSelectionMessage newInstance(final byte[] b) {
		ClientMethodSelectionMessage cmsm;
		try {
			cmsm = newInstanceFrom(new ByteArrayInputStream(b));
		} catch (IOException e) {
			throw new IllegalArgumentException(e);
		}
		return cmsm;
	}
	
	public static ClientMethodSelectionMessage newInstance(
			final Set<Method> methods) {
		if (methods.size() == UnsignedByte.MIN_INT_VALUE 
				|| methods.size() > UnsignedByte.MAX_INT_VALUE) {
			throw new IllegalArgumentException(String.format(
					"number of methods must be greater than %s and no more than %s",
					UnsignedByte.MIN_INT_VALUE,
					UnsignedByte.MAX_INT_VALUE));
		}
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		Version version = Version.V5;
		out.write(version.byteValue());
		out.write(methods.size());
		List<Method> meths = new ArrayList<Method>(methods);
		for (int i = 0; i < meths.size(); i++) {
			out.write(meths.get(i).byteValue());
		}
		Params params = new Params();
		params.version = version;
		params.methods = methods;
		params.byteArray = out.toByteArray();
		return new ClientMethodSelectionMessage(params);
	}
	
	public static ClientMethodSelectionMessage newInstanceFrom(
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
		int methodCount;
		try {
			methodCount = UnsignedByte.newInstance(b).intValue();
		} catch (IllegalArgumentException e) {
			throw new IOException(e);
		}
		if (methodCount < MIN_METHODS_LENGTH) {
			throw new IOException(String.format(
					"number of methods must be at least %s", 
					MIN_METHODS_LENGTH));
		}
		out.write(b);
		byte[] bytes = new byte[methodCount];
		int bytesRead = in.read(bytes);
		if (bytesRead != methodCount) {
			throw new IOException(String.format(
					"expected number of methods is %s. "
					+ "actual number of methods is %s", 
					methodCount, bytesRead));
		}
		bytes = Arrays.copyOf(bytes, bytesRead);
		Set<Method> meths = new TreeSet<Method>();
		for (int i = 0; i < bytes.length; i++) {
			b = bytes[i];
			Method meth = null;
			try {
				meth = Method.valueOf(
						(byte) UnsignedByte.newInstance(b).intValue());
			} catch (IllegalArgumentException e) {
				throw new IOException(e);
			}
			meths.add(meth);
			out.write(b);
		}
		Params params = new Params();
		params.version = ver;
		params.methods = meths;
		params.byteArray = out.toByteArray();
		return new ClientMethodSelectionMessage(params);
	}
	
	private final Version version;
	private final Set<Method> methods;
	private final byte[] byteArray;
	
	private ClientMethodSelectionMessage(final Params params) {
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

	public Set<Method> getMethods() {
		return Collections.unmodifiableSet(this.methods);
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
