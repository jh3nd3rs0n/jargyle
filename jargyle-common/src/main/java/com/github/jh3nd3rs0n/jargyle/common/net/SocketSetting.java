package com.github.jh3nd3rs0n.jargyle.common.net;

import com.github.jh3nd3rs0n.jargyle.internal.annotation.NameValuePairValueTypeDoc;

import java.net.DatagramSocket;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.Objects;

/**
 * A setting of a specified value type to be applied to a socket.
 *
 * @param <V> the specified value type
 */
@NameValuePairValueTypeDoc(
        description = "A setting to be applied to a socket",
        name = "Socket Setting",
        nameValuePairValueSpecs = {
                StandardSocketSettingSpecConstants.class
        },
        syntax = "NAME=VALUE",
        syntaxName = "SOCKET_SETTING"
)
public final class SocketSetting<V> {

    /**
     * The name of this {@code SocketSetting}.
     */
    private final String name;

    /**
     * The {@code SocketSettingSpec} that defines this {@code SocketSetting}.
     */
    private final SocketSettingSpec<V> socketSettingSpec;

    /**
     * The value of this {@code SocketSetting}.
     */
    private final V value;

    /**
     * The documentation of this {@code SocketSetting}.
     */
    private final String doc;

    /**
     * Constructs a {@code SocketSetting} with the provided
     * {@code SocketSettingSpec} and the provided value.
     *
     * @param spec the provided {@code SocketSettingSpec}
     * @param val  the provided value
     */
    SocketSetting(final SocketSettingSpec<V> spec, final V val) {
        this(spec, val, null);
    }

    /**
     * Constructs a {@code SocketSetting} with the provided
     * {@code SocketSettingSpec}, the provided value, and the optionally
     * provided documentation.
     *
     * @param spec the provided {@code SocketSettingSpec}
     * @param val  the provided value
     * @param d    the optionally provided documentation (can be {@code null})
     */
    SocketSetting(
            final SocketSettingSpec<V> spec, final V val, final String d) {
        V v = spec.getValueType().cast(val);
        this.name = Objects.requireNonNull(spec.getName());
        this.socketSettingSpec = spec;
        this.value = Objects.requireNonNull(v);
        this.doc = d;
    }

    /**
     * Returns a new {@code SocketSetting} of the {@code Object} value type
     * of the provided {@code String}. The provided {@code String} must be
     * the name of the {@code SocketSetting} followed by an equal sign (=)
     * followed by the value that can be parsed for the {@code SocketSetting}.
     * An {@code IllegalArgumentException} is thrown if any part of the provided
     * {@code String} is invalid.
     *
     * @param s the provided {@code String}
     * @return a new {@code SocketSetting} of value type {@code Object} of
     * the provided {@code String}
     */
    public static SocketSetting<Object> newInstanceOf(final String s) {
        String[] sElements = s.split("=", 2);
        if (sElements.length != 2) {
            throw new IllegalArgumentException(
                    "socket setting must be in the following format: "
                            + "NAME=VALUE");
        }
        String name = sElements[0];
        String value = sElements[1];
        return newInstanceWithParsedValue(name, value);
    }

    /**
     * Returns a new {@code SocketSetting} of the specified value type with
     * the provided name and the provided value. An
     * {@code IllegalArgumentException} is thrown if the provided name is
     * invalid.
     *
     * @param name  the provided name
     * @param value the provided value
     * @param <V>   the specified value type
     * @return a new {@code SocketSetting} of the specified value type with
     * the provided name and the provided value
     */
    public static <V> SocketSetting<V> newInstance(
            final String name, final V value) {
        SocketSettingSpec<Object> socketSettingSpec;
        try {
            socketSettingSpec = SocketSettingSpecConstants.valueOfName(name);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException(String.format(
                    "unknown socket setting: %s", name), e);
        }
        @SuppressWarnings("unchecked")
        SocketSetting<V> socketSetting =
                (SocketSetting<V>) socketSettingSpec.newSocketSetting(value);
        return socketSetting;
    }

    /**
     * Returns a new {@code SocketSetting} of the specified value type with
     * the provided name, the provided value, and the optionally provided
     * documentation. An {@code IllegalArgumentException} is thrown if the
     * provided name is invalid.
     *
     * @param name  the provided name
     * @param value the provided value
     * @param doc   the optionally provided documentation (can be {@code null})
     * @param <V>   the specified value type
     * @return a new {@code SocketSetting} of the specified value type with
     * the provided name, the provided value, the optionally provided
     * documentation
     */
    public static <V> SocketSetting<V> newInstance(
            final String name, final V value, final String doc) {
        SocketSettingSpec<Object> socketSettingSpec;
        try {
            socketSettingSpec = SocketSettingSpecConstants.valueOfName(name);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException(String.format(
                    "unknown socket setting: %s", name), e);
        }
        @SuppressWarnings("unchecked")
        SocketSetting<V> socketSetting =
                (SocketSetting<V>) socketSettingSpec.newSocketSetting(
                        value, doc);
        return socketSetting;
    }

    /**
     * Returns a new {@code SocketSetting} of the {@code Object} value type
     * with the provided name and the parsed value from the provided
     * {@code String}. An {@code IllegalArgumentException} is thrown if the
     * provided name or the provided {@code String} is invalid.
     *
     * @param name  the provided name
     * @param value the provided {@code String}
     * @return a new {@code SocketSetting} of the {@code Object} value type
     * with the provided name and the parsed value from the provided
     * {@code String}
     */
    public static SocketSetting<Object> newInstanceWithParsedValue(
            final String name, final String value) {
        SocketSettingSpec<Object> socketSettingSpec;
        try {
            socketSettingSpec = SocketSettingSpecConstants.valueOfName(name);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException(String.format(
                    "unknown socket setting: %s", name), e);
        }
        return socketSettingSpec.newSocketSettingWithParsedValue(value);
    }

    /**
     * Returns a new {@code SocketSetting} of the {@code Object} value type
     * with the provided name, the parsed value from the provided
     * {@code String}, and the optionally provided documentation. An
     * {@code IllegalArgumentException} is thrown if the provided name or the
     * provided {@code String} is invalid.
     *
     * @param name  the provided name
     * @param value the provided {@code String}
     * @param doc   the optionally provided documentation (can be {@code null})
     * @return a new {@code SocketSetting} of the {@code Object} value type
     * with the provided name, the parsed value from the provided
     * {@code String}, and the optionally provided documentation
     */
    public static SocketSetting<Object> newInstanceWithParsedValue(
            final String name, final String value, final String doc) {
        SocketSettingSpec<Object> socketSettingSpec;
        try {
            socketSettingSpec = SocketSettingSpecConstants.valueOfName(name);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException(String.format(
                    "unknown socket setting: %s", name), e);
        }
        return socketSettingSpec.newSocketSettingWithParsedValue(value, doc);
    }

    /**
     * Applies this {@code SocketSetting} to the provided
     * {@code DatagramSocket}. An {@code UnsupportedOperationException} is
     * thrown if this {@code SocketSetting} does not support application to
     * the provided {@code DatagramSocket}.
     *
     * @param datagramSocket the provided {@code DatagramSocket}
     * @throws SocketException if an error occurs in applying this
     *                         {@code SocketSetting} to the provided
     *                         {@code DatagramSocket}
     */
    public void applyTo(
            final DatagramSocket datagramSocket) throws SocketException {
        try {
            this.socketSettingSpec.apply(this.value, datagramSocket);
        } catch (UnsupportedOperationException e) {
            throw new UnsupportedOperationException(String.format(
                    "socket setting %s does not support application to a %s",
                    this.name, DatagramSocket.class.getName()), e);
        }
    }

    /**
     * Applies this {@code SocketSetting} to the provided {@code ServerSocket}.
     * An {@code UnsupportedOperationException} is thrown if this
     * {@code SocketSetting} does not support application to the provided
     * {@code ServerSocket}.
     *
     * @param serverSocket the provided {@code ServerSocket}
     * @throws SocketException if an error occurs in applying this
     *                         {@code SocketSetting} to the provided
     *                         {@code ServerSocket}
     */
    public void applyTo(
            final ServerSocket serverSocket) throws SocketException {
        try {
            this.socketSettingSpec.apply(this.value, serverSocket);
        } catch (UnsupportedOperationException e) {
            throw new UnsupportedOperationException(String.format(
                    "socket setting %s does not support application to a %s",
                    this.name, ServerSocket.class.getName()), e);
        }
    }

    /**
     * Applies this {@code SocketSetting} to the provided {@code Socket}. An
     * {@code UnsupportedOperationException} is thrown if this
     * {@code SocketSetting} does not support application to the provided
     * {@code Socket}.
     *
     * @param socket the provided {@code Socket}
     * @throws SocketException if an error occurs in applying this
     *                         {@code SocketSetting} to the provided
     *                         {@code Socket}
     */
    public void applyTo(final Socket socket) throws SocketException {
        try {
            this.socketSettingSpec.apply(this.value, socket);
        } catch (UnsupportedOperationException e) {
            throw new UnsupportedOperationException(String.format(
                    "socket setting %s does not support application to a %s",
                    this.name, Socket.class.getName()), e);
        }
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
        SocketSetting<?> other = (SocketSetting<?>) obj;
        if (!this.name.equals(other.name)) {
            return false;
        }
        return this.value.equals(other.value);
    }

    /**
     * Returns the documentation of this {@code SocketSetting}.
     *
     * @return the documentation of this {@code SocketSetting} (or
     * {@code null})
     */
    public String getDoc() {
        return this.doc;
    }

    /**
     * Returns the name of this {@code SocketSetting}.
     *
     * @return the name of this {@code SocketSetting}
     */
    public String getName() {
        return this.name;
    }

    /**
     * Returns the {@code SocketSettingSpec} that defines this
     * {@code SocketSetting}.
     *
     * @return the {@code SocketSettingSpec} that defines this
     * {@code SocketSetting}
     */
    public SocketSettingSpec<V> getSocketSettingSpec() {
        return this.socketSettingSpec;
    }

    /**
     * Returns the value of this {@code SocketSetting}.
     *
     * @return the value of this {@code SocketSetting}
     */
    public V getValue() {
        return this.value;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + this.name.hashCode();
        result = prime * result + this.value.hashCode();
        return result;
    }

    /**
     * Returns the {@code String} representation of this {@code SocketSetting}.
     * The {@code String} representation is the name of this
     * {@code SocketSetting} followed by an equal sign (=) followed by the
     * {@code String} representation of the value of this
     * {@code SocketSetting}.
     *
     * @return the {@code String} representation of this {@code SocketSetting}
     */
    @Override
    public String toString() {
        return String.format("%s=%s", this.name, this.value);
    }

}
