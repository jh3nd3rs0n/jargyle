package com.github.jh3nd3rs0n.jargyle.client.internal.client;

import java.net.ServerSocket;
import java.net.SocketException;
import java.net.SocketOption;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * A collection of {@code SocketOptionValueGetterSetter}s.
 */
public final class SocketOptionValueGetterSetters {

    /**
     * The {@code Map} of {@code SocketOptionValueGetterSetter}s each
     * associated by their {@code SocketOption}.
     */
    private final Map<SocketOption<?>, SocketOptionValueGetterSetter<?>> socketOptionValueGetterSettersMap;

    /**
     * The {@code Set} of {@code SocketOption}s supported by this
     * {@code SocketOptionValueGetterSetters}.
     */
    private final Set<SocketOption<?>> supportedSocketOptions;

    /**
     * Constructs a {@code SocketOptionValueGetterSetters} with the provided
     * {@code SocketOptionValueGetterSetter}s.
     *
     * @param socketOptionValueGetterSetters the provided {@code SocketOptionValueGetterSetter}s
     */
    public SocketOptionValueGetterSetters(
            final SocketOptionValueGetterSetter<?>... socketOptionValueGetterSetters) {
        this(Arrays.asList(socketOptionValueGetterSetters));
    }

    /**
     * Constructs a {@code SocketOptionValueGetterSetters} with the provided
     * {@code List} of {@code SocketOptionValueGetterSetter}s.
     *
     * @param socketOptionValueGetterSetters the provided {@code List} of {@code SocketOptionValueGetterSetter}s
     */
    public SocketOptionValueGetterSetters(
            final List<SocketOptionValueGetterSetter<?>> socketOptionValueGetterSetters) {
        this.socketOptionValueGetterSettersMap = new HashMap<>(
                socketOptionValueGetterSetters.stream().collect(
                        Collectors.toMap(
                                SocketOptionValueGetterSetter::getSocketOption,
                                Function.identity())));
        this.supportedSocketOptions = socketOptionValueGetterSetters.stream().map(
                SocketOptionValueGetterSetter::getSocketOption).collect(Collectors.toSet());
    }

    /**
     * Returns the value of the provided {@code SocketOption} from the
     * provided {@code ServerSocket}. An {@code UnsupportedOperationException}
     * is thrown if this {@code SocketOptionValueGetterSetters} does not
     * support returning the value of the {@code SocketOption} from the
     * provided {@code ServerSocket}.
     *
     * @param socketOption the provided {@code SocketOption}
     * @param serverSocket the provided {@code SocketOption}
     * @param <V>          the specified value type
     * @return the value of the provided {@code SocketOption} from the
     * provided {@code ServerSocket}
     * @throws SocketException if an error occurs in getting the value of the
     *                         provided {@code SocketOption} from the provided
     *                         {@code ServerSocket}
     */
    public <V> V getSocketOptionValue(
            final SocketOption<V> socketOption,
            final ServerSocket serverSocket) throws SocketException {
        Objects.requireNonNull(socketOption);
        SocketOptionValueGetterSetter<?> socketOptionValueGetterSetter =
                this.socketOptionValueGetterSettersMap.get(socketOption);
        if (socketOptionValueGetterSetter == null) {
            throw new UnsupportedOperationException(String.format(
                    "%s is not supported by %s",
                    socketOption.name(), serverSocket.getClass().getName()));
        }
        @SuppressWarnings("unchecked")
        SocketOptionValueGetterSetter<V> sockOptionValueGetterSetter =
                (SocketOptionValueGetterSetter<V>) socketOptionValueGetterSetter;
        return sockOptionValueGetterSetter.getSocketOptionValue(serverSocket);
    }

    /**
     * Returns an unmodifiable {@code Set} of {@code SocketOption}s supported
     * by this {@code SocketOptionValueGetterSetters}.
     *
     * @return an unmodifiable {@code Set} of {@code SocketOption}s supported
     * by this {@code SocketOptionValueGetterSetters}
     */
    public Set<SocketOption<?>> getSupportedSocketOptions() {
        return Collections.unmodifiableSet(this.supportedSocketOptions);
    }

    /**
     * Sets the provided value of the provided {@code SocketOption} to the
     * provided {@code ServerSocket}. An {@code UnsupportedOperationException}
     * is thrown if this {@code SocketOptionValueGetterSetters} does not
     * support setting the provided value of the provided {@code SocketOption}
     * to the provided {@code ServerSocket}.
     *
     * @param socketOption the provided {@code SocketOption}
     * @param value        the provided value of the provided
     *                     {@code SocketOption}
     * @param serverSocket the provided {@code ServerSocket}
     * @param <V>          the specified value type
     * @return the provided {@code ServerSocket}
     * @throws SocketException if an error occurs in setting the provided
     *                         value of the {@code SocketOption} to the
     *                         provided {@code ServerSocket}
     */
    public <V> ServerSocket setSocketOptionValue(
            final SocketOption<V> socketOption,
            final V value,
            final ServerSocket serverSocket) throws SocketException {
        Objects.requireNonNull(socketOption);
        SocketOptionValueGetterSetter<?> socketOptionValueGetterSetter =
                this.socketOptionValueGetterSettersMap.get(socketOption);
        if (socketOptionValueGetterSetter == null) {
            throw new UnsupportedOperationException(String.format(
                    "%s is not supported by %s",
                    socketOption.name(), serverSocket.getClass().getName()));
        }
        @SuppressWarnings("unchecked")
        SocketOptionValueGetterSetter<V> sockOptionValueGetterSetter =
                (SocketOptionValueGetterSetter<V>) socketOptionValueGetterSetter;
        return sockOptionValueGetterSetter.setSocketOptionValue(
                value, serverSocket);
    }

}
