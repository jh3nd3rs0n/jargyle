package com.github.jh3nd3rs0n.jargyle.client.internal.client;

import java.net.ServerSocket;
import java.net.SocketException;
import java.net.SocketOption;
import java.util.Objects;

/**
 * Gets/sets the value of a {@code SocketOption} of a specified value type
 * from/to a {@code ServerSocket}.
 *
 * @param <V> the specified value type
 */
public abstract class SocketOptionValueGetterSetter<V> {

    /**
     * The {@code SocketOption}.
     */
    private final SocketOption<V> socketOption;

    /**
     * Constructs a {@code SocketOptionValueGetterSetter} with the provided
     * {@code SocketOption}.
     *
     * @param sockOption the provided {@code SocketOption}
     */
    public SocketOptionValueGetterSetter(final SocketOption<V> sockOption) {
        this.socketOption = Objects.requireNonNull(sockOption);
    }

    /**
     * Returns the {@code SocketOption}.
     *
     * @return the {@code SocketOption}
     */
    public final SocketOption<V> getSocketOption() {
        return this.socketOption;
    }

    /**
     * Returns the value of the {@code SocketOption} from the provided
     * {@code ServerSocket}. An {@code UnsupportedOperationException} is
     * thrown if this {@code SocketOptionValueGetterSetter} does not support
     * returning the value of the {@code SocketOption} from the provided
     * {@code ServerSocket}.
     *
     * @param serverSocket the provided {@code ServerSocket}
     * @return the value of the {@code SocketOption} from the provided
     * {@code ServerSocket}
     * @throws SocketException if an error occurs in getting the value of the
     *                         {@code SocketOption} from the provided
     *                         {@code ServerSocket}
     */
    public V getSocketOptionValue(
            final ServerSocket serverSocket) throws SocketException {
        throw new UnsupportedOperationException(String.format(
                "%s is not supported by %s",
                this.socketOption.name(), serverSocket.getClass().getName()));
    }

    /**
     * Sets the provided value of the {@code SocketOption} to the provided
     * {@code ServerSocket}. An {@code UnsupportedOperationException} is
     * thrown if this {@code SocketOptionValueGetterSetter} does not support
     * setting the provided value of the {@code SocketOption} to the provided
     * {@code ServerSocket}.
     *
     * @param value        the provided value of the {@code SocketOption}
     * @param serverSocket the provided {@code ServerSocket}
     * @return the provided {@code ServerSocket}
     * @throws SocketException if an error occurs in setting the provided
     *                         value of the {@code SocketOption} to the
     *                         provided {@code ServerSocket}
     */
    public ServerSocket setSocketOptionValue(
            final V value,
            final ServerSocket serverSocket) throws SocketException {
        throw new UnsupportedOperationException(String.format(
                "%s is not supported by %s",
                this.socketOption.name(), serverSocket.getClass().getName()));
    }

}
