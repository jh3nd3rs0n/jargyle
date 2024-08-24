package com.github.jh3nd3rs0n.jargyle.protocolbase.socks5.userpassmethod;

import com.github.jh3nd3rs0n.jargyle.common.number.UnsignedByte;
import com.github.jh3nd3rs0n.jargyle.protocolbase.internal.UnsignedByteIoHelper;
import com.github.jh3nd3rs0n.jargyle.protocolbase.socks5.Socks5Exception;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * A username/password request from the client.
 */
public final class Request {

    /**
     * The maximum length of a username in bytes.
     */
    public static final int MAX_UNAME_LENGTH = 255;

    /**
     * The maximum length of a password in bytes.
     */
    public static final int MAX_PASSWD_LENGTH = 255;

    /**
     * The {@code Charset} used to decode the username or the password as a
     * {@code String} in {@code byte}s.
     */
    private static final Charset CHARSET = StandardCharsets.US_ASCII;

    /**
     * The {@code Version} of this {@code Request}.
     */
    private final Version version;

    /**
     * The username of this {@code Request}.
     */
    private final String username;

    /**
     * The password of this {@code Request}.
     */
    private final char[] password;

    /**
     * Constructs a {@code Request} with the provided username and the
     * provided password.
     *
     * @param usrname the provided username
     * @param passwrd the provided password
     */
    private Request(final String usrname, final char[] passwrd) {
        this.version = Version.V1;
        this.username = usrname;
        this.password = Arrays.copyOf(passwrd, passwrd.length);
    }

    /**
     * Validates the provided password. An {@code IllegalArgumentException} is
     * thrown if the length of bytes of the provided password is greater than
     * the maximum length of a password in bytes.
     *
     * @param password the provided password
     */
    public static void validatePassword(final char[] password) {
        byte[] passwordBytes = getBytes(password);
        if (passwordBytes.length > MAX_PASSWD_LENGTH) {
            throw new IllegalArgumentException(String.format(
                    "password must be no more than %s byte(s)",
                    MAX_PASSWD_LENGTH));
        }
    }

    /**
     * Validates the provided username. An {@code IllegalArgumentException} is
     * thrown if the length of bytes of the provided username is greater than
     * the maximum length of a username in bytes.
     *
     * @param username the provided username
     */
    public static void validateUsername(final String username) {
        byte[] usernameBytes = username.getBytes(CHARSET);
        if (usernameBytes.length > MAX_UNAME_LENGTH) {
            throw new IllegalArgumentException(String.format(
                    "username must be no more than %s byte(s)",
                    MAX_UNAME_LENGTH));
        }
    }

    /**
     * Returns a new {@code Request} with the provided username and the
     * provided password. An {@code IllegalArgumentException} is thrown if
     * the length of bytes of the provided username is greater than the
     * maximum length of a username in bytes or if the length of bytes of the
     * provided password is greater than the maximum length of a password in
     * bytes
     *
     * @param usrname the provided username
     * @param passwrd the provided password
     * @return a new {@code Request} with the provided username and the
     * provided password
     */
    public static Request newInstance(
            final String usrname, final char[] passwrd) {
        validateUsername(Objects.requireNonNull(usrname));
        validatePassword(Objects.requireNonNull(passwrd));
        return new Request(usrname, passwrd);
    }

    /**
     * Returns a new {@code Request} from the provided {@code InputStream}. An
     * {@code EOFException} is thrown if the end of the provided
     * {@code InputStream} is reached.
     *
     * @param in the provided {@code InputStream}
     * @return a new {@code Request}
     * @throws IOException if the end of the provided {@code InputStream}
     *                     is reached ({@code EOFException}) or if an I/O
     *                     error occurs
     */
    public static Request newInstanceFrom(
            final InputStream in) throws IOException {
        VersionIoHelper.readVersionFrom(in);
        UnsignedByte ulen = UnsignedByteIoHelper.readUnsignedByteFrom(in);
        byte[] bytes = new byte[ulen.intValue()];
        if (ulen.intValue() > 0) {
            int bytesRead = in.read(bytes);
            if (ulen.intValue() != bytesRead) {
                throw new Socks5Exception(String.format(
                        "expected username length is %s byte(s). "
                                + "actual username length is %s byte(s)",
                        ulen.intValue(),
                        (bytesRead == -1) ? 0 : bytesRead));
            }
        }
        String uname = new String(bytes, CHARSET);
        UnsignedByte plen = UnsignedByteIoHelper.readUnsignedByteFrom(in);
        bytes = new byte[plen.intValue()];
        if (plen.intValue() > 0) {
            int bytesRead = in.read(bytes);
            if (plen.intValue() != bytesRead) {
                throw new Socks5Exception(String.format(
                        "expected password length is %s byte(s). "
                                + "actual password length is %s byte(s)",
                        plen.intValue(),
                        (bytesRead == -1) ? 0 : bytesRead));
            }
        }
        List<Character> characters = new ArrayList<>();
        try (Reader reader = new InputStreamReader(new ByteArrayInputStream(
                bytes), CHARSET)) {
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

    /**
     * Returns the {@code byte}s of the provided password.
     *
     * @param password the provided password
     * @return the {@code byte}s of the provided password
     */
    private static byte[] getBytes(final char[] password) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        Writer writer = new OutputStreamWriter(out, CHARSET);
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
        Request other = (Request) obj;
        if (!this.username.equals(other.username)) {
            return false;
        }
        return Arrays.equals(this.password, other.password);
    }

    /**
     * Returns the password of this {@code Request}.
     *
     * @return the password of this {@code Request}
     */
    public char[] getPassword() {
        return Arrays.copyOf(this.password, this.password.length);
    }

    /**
     * Returns the username of this {@code Request}.
     *
     * @return the username of this {@code Request}
     */
    public String getUsername() {
        return this.username;
    }

    /**
     * Returns the {@code Version} of this {@code Request}.
     *
     * @return the {@code Version} of this {@code Request}
     */
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

    /**
     * Returns the {@code byte} array of this {@code Request}.
     *
     * @return the {@code byte} array of this {@code Request}
     */
    public byte[] toByteArray() {
        byte[] usernameBytes = this.username.getBytes(CHARSET);
        byte[] passwordBytes = getBytes(this.password);
        byte[] arr = new byte[2 + usernameBytes.length + 1 + passwordBytes.length];
        arr[0] = this.version.byteValue();
        arr[1] = UnsignedByte.valueOf(usernameBytes.length).byteValue();
        System.arraycopy(usernameBytes, 0, arr, 2, usernameBytes.length);
        arr[2 + usernameBytes.length] =
                UnsignedByte.valueOf(passwordBytes.length).byteValue();
        System.arraycopy(
                passwordBytes,
                0,
                arr,
                2 + usernameBytes.length + 1, passwordBytes.length);
        return arr;
    }

    /**
     * Returns the {@code String} representation of this {@code Request}.
     *
     * @return the {@code String} representation of this {@code Request}
     */
    @Override
    public String toString() {
        return this.getClass().getSimpleName() +
                " [version=" +
                this.version +
                ", username=" +
                this.username +
                ", password=" +
                Arrays.toString(this.password) +
                "]";
    }

}
