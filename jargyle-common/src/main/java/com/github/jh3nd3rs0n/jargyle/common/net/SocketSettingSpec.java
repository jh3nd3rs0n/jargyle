package com.github.jh3nd3rs0n.jargyle.common.net;

import java.net.DatagramSocket;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.Objects;

/**
 * The specification of a {@code SocketSetting} of the specified value type.
 *
 * @param <V> the specified value type
 */
public abstract class SocketSettingSpec<V> {

    /**
     * The name of the {@code SocketSetting}.
     */
    private final String name;

    /**
     * The value type of the {@code SocketSetting}.
     */
    private final Class<V> valueType;

    /**
     * Constructs a {@code SocketSettingSpec} with the provided name of the
     * {@code SocketSetting} and the provided value type of the
     * {@code SocketSetting}.
     *
     * @param n       the provided name of the {@code SocketSetting}
     * @param valType the provided value type of the {@code SocketSetting}
     */
    SocketSettingSpec(final String n, final Class<V> valType) {
        Objects.requireNonNull(n);
        Objects.requireNonNull(valType);
        this.name = n;
        this.valueType = valType;
    }

    /**
     * Applies the provided value to the provided {@code DatagramSocket}. An
     * {@code UnsupportedOperationException} is thrown if this
     * {@code SocketSettingSpec} does not support application to the provided
     * {@code DatagramSocket}.
     *
     * @param value          the provided value
     * @param datagramSocket the provided {@code DatagramSocket}
     * @throws SocketException if an error occurs in applying the value to the
     *                         provided {@code DatagramSocket}
     */
    public void apply(
            final V value,
            final DatagramSocket datagramSocket) throws SocketException {
        throw new UnsupportedOperationException(String.format(
                "socket setting spec %s does not support application to a %s",
                this.name, DatagramSocket.class.getName()));
    }

    /**
     * Applies the provided value to the provided {@code ServerSocket}. An
     * {@code UnsupportedOperationException} is thrown if this
     * {@code SocketSettingSpec} does not support application to the provided
     * {@code ServerSocket}.
     *
     * @param value        the provided value
     * @param serverSocket the provided {@code ServerSocket}
     * @throws SocketException if an error occurs in applying the value to the
     *                         provided {@code ServerSocket}
     */
    public void apply(
            final V value,
            final ServerSocket serverSocket) throws SocketException {
        throw new UnsupportedOperationException(String.format(
                "socket setting spec %s does not support application to a %s",
                this.name, ServerSocket.class.getName()));
    }

    /**
     * Applies the provided value to the provided {@code Socket}. An
     * {@code UnsupportedOperationException} is thrown if this
     * {@code SocketSettingSpec} does not support application to the provided
     * {@code Socket}.
     *
     * @param value  the provided value
     * @param socket the provided {@code Socket}
     * @throws SocketException if an error occurs in applying the value to the
     *                         provided {@code Socket}
     */
    public void apply(
            final V value,
            final Socket socket) throws SocketException {
        throw new UnsupportedOperationException(String.format(
                "socket setting spec %s does not support application to a %s",
                this.name, Socket.class.getName()));
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (this.getClass() != obj.getClass()) {
            return false;
        }
        SocketSettingSpec<?> other = (SocketSettingSpec<?>) obj;
        if (!this.name.equals(other.name)) {
            return false;
        }
        return this.valueType == other.valueType;
    }

    /**
     * Returns the name of the {@code SocketSetting}.
     *
     * @return the name of the {@code SocketSetting}
     */
    public final String getName() {
        return this.name;
    }

    /**
     * Returns the value type of the {@code SocketSetting}.
     *
     * @return the value type of the {@code SocketSetting}
     */
    public final Class<V> getValueType() {
        return this.valueType;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + this.name.hashCode();
        result = prime * result + this.valueType.hashCode();
        return result;
    }

    /**
     * Returns a new {@code SocketSetting} with the provided value.
     *
     * @param value the provided value
     * @return a new {@code SocketSetting} with the provided value
     */
    public final SocketSetting<V> newSocketSetting(final V value) {
        return new SocketSetting<>(
                this,
                this.valueType.cast(this.validate(value)));
    }

    /**
     * Returns a new {@code SocketSetting} with the provided value and the
     * optionally provided documentation.
     *
     * @param value the provided value
     * @param doc   the optionally provided documentation (can be {@code null})
     * @return a new {@code SocketSetting} with the provided value and the
     * optionally provided documentation
     */
    public final SocketSetting<V> newSocketSetting(
            final V value, final String doc) {
        return new SocketSetting<>(
                this,
                this.valueType.cast(this.validate(value)),
                doc);
    }

    /**
     * Returns a new {@code SocketSetting} with the parsed value from the
     * provided {@code String} value. An {@code IllegalArgumentException} is
     * thrown if the provided {@code String} value to be parsed is invalid.
     *
     * @param value the provided {@code String} value to be parsed
     * @return a new {@code SocketSetting} with the parsed value from the
     * provided {@code String} value
     */
    public final SocketSetting<V> newSocketSettingWithParsedValue(
            final String value) {
        return this.newSocketSetting(this.parse(value));
    }

    /**
     * Returns a new {@code SocketSetting} with the parsed value from the
     * provided {@code String} value and with the optionally provided
     * documentation. An {@code IllegalArgumentException} is thrown if the
     * provided {@code String} value to be parsed is invalid.
     *
     * @param value the provided {@code String} value to be parsed
     * @param doc   the optionally provided documentation (can be {@code null})
     * @return a new {@code SocketSetting} with the parsed value from the
     * provided {@code String} value and with the optionally provided
     * documentation
     */
    public final SocketSetting<V> newSocketSettingWithParsedValue(
            final String value, final String doc) {
        return this.newSocketSetting(this.parse(value), doc);
    }

    /**
     * Returns the parsed value from the provided {@code String} value. An
     * {@code IllegalArgumentException} is thrown if the provided
     * {@code String} value to be parsed is invalid.
     *
     * @param value the {@code String} value to be parsed
     * @return the parsed value from the provided {@code String} value
     */
    protected abstract V parse(final String value);

    /**
     * Returns the {@code String} representation of this
     * {@code SocketSettingSpec}.
     *
     * @return the {@code String} representation of this
     * {@code SocketSettingSpec}
     */
    @Override
    public final String toString() {
        return this.getClass().getSimpleName() +
                " [name=" +
                this.name +
                "]";
    }

    /**
     * Returns the valid provided value. Implementations can override this
     * method to throw an {@code IllegalArgumentException} if the provided
     * value is invalid.
     *
     * @param value the provided value
     * @return the valid provided value
     */
    protected V validate(final V value) {
        return value;
    }

}
