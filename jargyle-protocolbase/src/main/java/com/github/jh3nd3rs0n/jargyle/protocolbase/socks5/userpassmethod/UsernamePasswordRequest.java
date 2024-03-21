package com.github.jh3nd3rs0n.jargyle.protocolbase.socks5.userpassmethod;

import com.github.jh3nd3rs0n.jargyle.common.number.UnsignedByte;
import com.github.jh3nd3rs0n.jargyle.protocolbase.internal.UnsignedByteIoHelper;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public final class UsernamePasswordRequest {

	public static final int MAX_UNAME_LENGTH = 255;
	public static final int MAX_PASSWD_LENGTH = 255;

	public static void validatePassword(final char[] password) {
		byte[] passwordBytes = toByteArray(
				password);
		if (passwordBytes.length > MAX_PASSWD_LENGTH) {
			throw new IllegalArgumentException(String.format(
					"password must be no more than %s byte(s)",
					MAX_PASSWD_LENGTH));
		}
	}
	
	public static void validateUsername(final String username) {
		byte[] usernameBytes = username.getBytes();
		if (usernameBytes.length > MAX_UNAME_LENGTH) {
			throw new IllegalArgumentException(String.format(
					"username must be no more than %s byte(s)",
					MAX_UNAME_LENGTH));
		}
	}
	
	private final Version version;
	private final String username;
	private final char[] password;

	private UsernamePasswordRequest(
			final String usrname, final char[] passwrd) {
		this.version = Version.V1;
		this.username = usrname;
		this.password = passwrd;
	}

	public static UsernamePasswordRequest newInstance(
			final String usrname, final char[] passwrd) {
		validateUsername(Objects.requireNonNull(usrname));
		validatePassword(Objects.requireNonNull(passwrd));
		return new UsernamePasswordRequest(usrname, passwrd);
	}

	public static UsernamePasswordRequest newInstanceFrom(final byte[] b) {
		UsernamePasswordRequest request;
		try {
			request = newInstanceFrom(new ByteArrayInputStream(b));
		} catch (IOException e) {
			throw new IllegalArgumentException(e);
		}
		return request;
	}

    public static UsernamePasswordRequest newInstanceFrom(
            final InputStream in) throws IOException {
        VersionIoHelper.readVersionFrom(in);
        UnsignedByte ulen = UnsignedByteIoHelper.readUnsignedByteFrom(in);
        byte[] bytes = new byte[ulen.intValue()];
        int bytesRead = in.read(bytes);
        if (bytesRead != ulen.intValue()) {
            throw new EOFException(String.format(
                    "expected username length is %s byte(s). "
                    + "actual username length is %s byte(s)",
                    ulen.intValue(), bytesRead));
        }
        String uname = new String(bytes);
        UnsignedByte plen = UnsignedByteIoHelper.readUnsignedByteFrom(in);
        bytes = new byte[plen.intValue()];
        bytesRead = in.read(bytes);
        if (bytesRead != plen.intValue()) {
            throw new EOFException(String.format(
                    "expected password length is %s byte(s). "
                    + "actual password length is %s byte(s)",
                    plen.intValue(), bytesRead));
        }
        List<Character> characters = new ArrayList<>();
        try (Reader reader = new InputStreamReader(new ByteArrayInputStream(
                bytes))) {
            int ch;
            while ((ch = reader.read()) != -1) {
                characters.add((char) ch);
            }
        } catch (IOException e) {
            throw new AssertionError(e);
        }
        int charactersCount = characters.size();
        char[] passwd = new char[charactersCount];
        for (int i = 0; i < charactersCount; i++) {
            passwd[i] = characters.get(i);
        }
        return newInstance(uname, passwd);
    }

    private static byte[] toByteArray(final char[] password) {
        ByteArrayOutputStream out =	new ByteArrayOutputStream();
        Writer writer = new OutputStreamWriter(out);
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
        return out.toByteArray();
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
		if (!this.username.equals(other.username)) {
			return false;
		}
		if (!Arrays.equals(this.password, other.password)) {
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
		result = prime * result + this.username.hashCode();
		result = prime * result + Arrays.hashCode(this.password);
		return result;
	}

	public byte[] toByteArray() {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		out.write(UnsignedByte.valueOf(
				this.version.byteValue()).intValue());
		byte[] usernameBytes = this.username.getBytes();
		out.write(usernameBytes.length);
		try {
			out.write(usernameBytes);
		} catch (IOException e) {
			throw new AssertionError(e);
		}
		byte[] passwordBytes = toByteArray(this.password);
		out.write(passwordBytes.length);
		try {
			out.write(passwordBytes);
		} catch (IOException e) {
			throw new AssertionError(e);
		}
		return out.toByteArray();
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
