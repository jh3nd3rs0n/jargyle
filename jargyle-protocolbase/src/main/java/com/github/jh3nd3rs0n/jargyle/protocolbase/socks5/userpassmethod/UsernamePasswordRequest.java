package com.github.jh3nd3rs0n.jargyle.protocolbase.socks5.userpassmethod;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.Arrays;

import com.github.jh3nd3rs0n.jargyle.common.number.UnsignedByte;

public final class UsernamePasswordRequest {

	static final class Params {
		Version version;
		String username;
		char[] password;
		byte[] byteArray;
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
		UsernamePasswordRequest request = null;
		try {
			request = new UsernamePasswordRequestInputStream(
					new ByteArrayInputStream(b)).readUsernamePasswordRequest();
		} catch (IOException e) {
			throw new IllegalArgumentException(e);
		}
		return request;
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
	
	UsernamePasswordRequest(final Params params) {
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
