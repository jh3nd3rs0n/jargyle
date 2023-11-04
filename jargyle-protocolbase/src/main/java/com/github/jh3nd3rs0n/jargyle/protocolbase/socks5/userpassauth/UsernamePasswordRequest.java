package com.github.jh3nd3rs0n.jargyle.protocolbase.socks5.userpassauth;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import java.util.Arrays;

import com.github.jh3nd3rs0n.jargyle.common.lang.UnsignedByte;

public final class UsernamePasswordRequest {

	private static final class Params {
		private Version version;
		private String username;
		private char[] password;
		private byte[] byteArray;
	}
	
	public static final int MAX_UNAME_LENGTH = 255;
	public static final int MAX_PASSWD_LENGTH = 255;
	
	private static byte[] getValidatedPasswordBytes(final char[] password) {
		ByteArrayOutputStream byteArrayOutputStream = 
				new ByteArrayOutputStream();
		Writer writer = new OutputStreamWriter(byteArrayOutputStream);
		for (char ch : password) {
			try {
				writer.write(ch);
			} catch (IOException e) {
				throw new AssertionError(e);
			}
		}
		try {
			writer.flush();
		} catch (IOException e) {
			throw new AssertionError(e);
		}
		byte[] passwordBytes = byteArrayOutputStream.toByteArray();
		if (passwordBytes.length > MAX_PASSWD_LENGTH) {
			throw new IllegalArgumentException(String.format(
					"password must be no more than %s byte(s)",
					MAX_PASSWD_LENGTH));
		}
		return passwordBytes;
	}
	
	private static byte[] getValidatedUsernameBytes(final String username) {
		byte[] usernameBytes = username.getBytes();
		if (usernameBytes.length > MAX_UNAME_LENGTH) {
			throw new IllegalArgumentException(String.format(
					"username must be no more than %s byte(s)",
					MAX_UNAME_LENGTH));
		}
		return usernameBytes;
	}
	
	public static UsernamePasswordRequest newInstance(final byte[] b) {
		UsernamePasswordRequest usernamePasswordRequest;
		try {
			usernamePasswordRequest = newInstanceFrom(new ByteArrayInputStream(b));
		} catch (IOException e) {
			throw new IllegalArgumentException(e);
		}
		return usernamePasswordRequest;
	}
	
	public static UsernamePasswordRequest newInstance(
			final String username,
			final char[] password) {
		char[] passwd = Arrays.copyOf(password, password.length);
		byte[] usernameBytes = getValidatedUsernameBytes(username);
		byte[] passwdBytes = getValidatedPasswordBytes(passwd);
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		Version version = Version.V1;
		out.write(UnsignedByte.newInstance(version.byteValue()).intValue());
		out.write(usernameBytes.length);
		try {
			out.write(usernameBytes);
		} catch (IOException e) {
			throw new AssertionError(e);
		}
		out.write(passwdBytes.length);
		try {
			out.write(passwdBytes);
		} catch (IOException e) {
			throw new AssertionError(e);
		}
		Params params = new Params();
		params.version = version;
		params.username = username;
		params.password = passwd;
		params.byteArray = out.toByteArray();
		return new UsernamePasswordRequest(params);
	}
	
	public static UsernamePasswordRequest newInstanceFrom(
			final InputStream in) throws IOException {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		Version ver = Version.valueOfByteFrom(in);
		out.write(UnsignedByte.newInstance(ver.byteValue()).intValue());
		UnsignedByte ulen = UnsignedByte.newInstanceFrom(in);
		out.write(ulen.intValue());
		byte[] bytes = new byte[ulen.intValue()];
		int bytesRead = in.read(bytes);
		if (bytesRead != ulen.intValue()) {
			throw new EOFException(String.format(
					"expected username length is %s byte(s). "
					+ "actual username length is %s byte(s)", 
					ulen.intValue(), bytesRead));
		}
		bytes = Arrays.copyOf(bytes, bytesRead);
		String uname = new String(bytes);
		out.write(bytes);
		UnsignedByte plen = UnsignedByte.newInstanceFrom(in); 
		out.write(plen.intValue());
		bytes = new byte[plen.intValue()];
		bytesRead = in.read(bytes);
		if (bytesRead != plen.intValue()) {
			throw new EOFException(String.format(
					"expected password length is %s byte(s). "
					+ "actual password length is %s byte(s)", 
					plen.intValue(), bytesRead));
		}
		bytes = Arrays.copyOf(bytes, bytesRead);
		Reader reader = new InputStreamReader(new ByteArrayInputStream(
				bytes));
		int passwdLength = 0;
		char[] passwd = new char[passwdLength];
		int ch = -1;
		do {
			try {
				ch = reader.read();
			} catch (IOException e) {
				throw new AssertionError(e);
			}
			if (ch != -1) {
				passwd = Arrays.copyOf(passwd, ++passwdLength);
				passwd[passwdLength - 1] = (char) ch;
			}
		} while (ch != -1);
		out.write(bytes);
		Params params = new Params();
		params.version = ver;
		params.username = uname;
		params.password = passwd;
		params.byteArray = out.toByteArray();
		return new UsernamePasswordRequest(params);
	}
	
	public static void validatePassword(final char[] password) {
		getValidatedPasswordBytes(password);
	}
	
	public static void validateUsername(final String username) {
		getValidatedUsernameBytes(username);
	}
	
	private final Version version;
	private final String username;
	private final char[] password;
	private final byte[] byteArray;
	
	private UsernamePasswordRequest(final Params params) {
		this.version = params.version;
		this.username = params.username;
		this.password = params.password;
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
		UsernamePasswordRequest other = (UsernamePasswordRequest) obj;
		if (!Arrays.equals(this.byteArray, other.byteArray)) {
			return false;
		}
		return true;
	}

	public char[] getPassword() {
		return Arrays.copyOf(this.password, this.password.length);
	}
	
	public String getUsername() {
		return this.username;
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
			.append(", username=")
			.append(this.username)
			.append(", password=")
			.append(Arrays.toString(this.password))
			.append("]");
		return builder.toString();
	}
	
}
