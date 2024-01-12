package com.github.jh3nd3rs0n.jargyle.common.net;

import com.github.jh3nd3rs0n.jargyle.internal.annotation.ValuesValueTypeDoc;

import java.net.DatagramSocket;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * A collection of {@code SocketSetting}s.
 */
@ValuesValueTypeDoc(
        description = "A comma separated list of socket settings.",
        elementValueType = SocketSetting.class,
        name = "Socket Settings",
        syntax = "[SOCKET_SETTING1[,SOCKET_SETTING2[...]]]",
        syntaxName = "SOCKET_SETTINGS"
)
public final class SocketSettings {

    /**
     * The {@code Map} of {@code SocketSetting}s each associated by their
     * {@code SocketSettingSpec}.
     */
    private final Map<SocketSettingSpec<Object>, SocketSetting<Object>> socketSettings;

    /**
     * Constructs a {@code SocketSettings} of the provided {@code List} of
     * {@code SocketSetting}s.
     *
     * @param socketSttngs the provided {@code List} of {@code SocketSetting}s
     */
    private SocketSettings(final List<SocketSetting<?>> socketSttngs) {
        Map<SocketSettingSpec<Object>, SocketSetting<Object>> map =
                new LinkedHashMap<>();
        for (SocketSetting<?> socketSttng : socketSttngs) {
            @SuppressWarnings("unchecked")
            SocketSettingSpec<Object> socketSttngSpec =
                    (SocketSettingSpec<Object>) socketSttng.getSocketSettingSpec();
            map.remove(socketSttngSpec);
            @SuppressWarnings("unchecked")
            SocketSetting<Object> sockSttng =
                    (SocketSetting<Object>) socketSttng;
            map.put(socketSttngSpec, sockSttng);
        }
        this.socketSettings = map;
    }

    /**
     * Constructs a {@code SocketSettings} of another {@code SocketSettings}.
     *
     * @param other the other {@code SocketSettings}
     */
    private SocketSettings(final SocketSettings other) {
        this.socketSettings = new LinkedHashMap<>(other.socketSettings);
    }

    /**
     * Returns a {@code SocketSettings} of the provided {@code List} of
     * {@code SocketSetting}s.
     *
     * @param socketSttngs the provided {@code List} of {@code SocketSetting}s
     * @return a {@code SocketSettings} of the provided {@code List} of
     * {@code SocketSetting}s
     */
    public static SocketSettings of(
            final List<SocketSetting<?>> socketSttngs) {
        return new SocketSettings(socketSttngs);
    }

    /**
     * Returns a {@code SocketSettings} of the provided {@code SocketSetting}s.
     *
     * @param socketSttngs the provided {@code SocketSetting}s
     * @return a {@code SocketSettings} of the provided {@code SocketSetting}s
     */
    public static SocketSettings of(
            final SocketSetting<?>... socketSttngs) {
        return of(Arrays.asList(socketSttngs));
    }

    /**
     * Returns a {@code SocketSettings} of another {@code SocketSettings}.
     *
     * @param other the other {@code SocketSettings}
     * @return a {@code SocketSettings} of another {@code SocketSettings}
     */
    public static SocketSettings of(final SocketSettings other) {
        return new SocketSettings(other);
    }

    /**
     * Returns a new {@code SocketSettings} from the provided {@code String}.
     * The provided {@code String} must be a comma separated list of
     * {@code String} representations of {@code SocketSetting}s. The provided
     * {@code String} can also be empty which would result in an empty
     * {@code SocketSettings}. An {@code IllegalArgumentException} is thrown
     * if the provided {@code String} is invalid.
     *
     * @param s the provided {@code String}
     * @return a new {@code SocketSettings} from the provided {@code String}
     */
    public static SocketSettings newInstanceFrom(final String s) {
        List<SocketSetting<?>> socketSettings = new ArrayList<>();
        if (s.isEmpty()) {
            return of(socketSettings);
        }
        String[] sElements = s.split(",");
        for (String sElement : sElements) {
            SocketSetting<Object> socketSetting =
                    SocketSetting.newInstanceFrom(sElement);
            socketSettings.add(socketSetting);
        }
        return of(socketSettings);
    }

    /**
     * Applies this {@code SocketSettings} to the provided
     * {@code DatagramSocket}. An {@code UnsupportedOperationException} is
     * thrown if one of the {@code SocketSetting}s does not support
     * application to the provided {@code DatagramSocket}.
     *
     * @param datagramSocket the provided {@code DatagramSocket}
     * @throws SocketException if an error occurs in applying this
     *                         {@code SocketSettings} to the provided
     *                         {@code DatagramSocket}
     */
    public void applyTo(
            final DatagramSocket datagramSocket) throws SocketException {
        for (SocketSetting<Object> socketSetting
                : this.socketSettings.values()) {
            socketSetting.applyTo(datagramSocket);
        }
    }

    /**
     * Applies this {@code SocketSettings} to the provided
     * {@code ServerSocket}. An {@code UnsupportedOperationException} is
     * thrown if one of the {@code SocketSetting}s does not support
     * application to the provided {@code ServerSocket}.
     *
     * @param serverSocket the provided {@code ServerSocket}
     * @throws SocketException if an error occurs in applying this
     *                         {@code SocketSettings} to the provided
     *                         {@code ServerSocket}
     */
    public void applyTo(
            final ServerSocket serverSocket) throws SocketException {
        for (SocketSetting<Object> socketSetting
                : this.socketSettings.values()) {
            socketSetting.applyTo(serverSocket);
        }
    }

    /**
     * Applies this {@code SocketSettings} to the provided {@code Socket}.
     * An {@code UnsupportedOperationException} is thrown if one of the
     * {@code SocketSetting}s does not support application to the provided
     * {@code Socket}.
     *
     * @param socket the provided {@code Socket}
     * @throws SocketException if an error occurs in applying this
     *                         {@code SocketSettings} to the provided
     *                         {@code Socket}
     */
    public void applyTo(final Socket socket) throws SocketException {
        for (SocketSetting<Object> socketSetting
                : this.socketSettings.values()) {
            socketSetting.applyTo(socket);
        }
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
        SocketSettings other = (SocketSettings) obj;
        return this.socketSettings.equals(other.socketSettings);
    }

    /**
     * Returns the value of the {@code SocketSetting} defined by the provided
     * {@code SocketSettingSpec} of the specified value type.
     *
     * @param socketSettingSpec the provided {@code SocketSettingSpec} of the
     *                          specified value type
     * @param <V>               the specified value type
     * @return the value of the {@code SocketSetting} defined by the provided
     * {@code SocketSettingSpec} of the specified value type (or {@code null}
     * if there is no {@code SocketSetting} defined by the provided
     * {@code SocketSettingSpec} of the specified value type)
     */
    public <V> V getValue(final SocketSettingSpec<V> socketSettingSpec) {
        V value = null;
        SocketSetting<Object> socketSetting = this.socketSettings.get(
                socketSettingSpec);
        if (socketSetting != null) {
            value = socketSettingSpec.getValueType().cast(
                    socketSetting.getValue());
        }
        return value;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + this.socketSettings.hashCode();
        return result;
    }

    /**
     * Puts the provided value with a new {@code SocketSetting} defined by the
     * provided {@code SocketSettingSpec} of the specified value type and
     * returns the recent value if any.
     *
     * @param socketSettingSpec the provided {@code SocketSettingSpec} of the
     *                          specified value type
     * @param value             the provided value
     * @param <V>               the specified value type
     * @return the recent value (or {@code null} if there was no recent value)
     */
    public <V> V putValue(
            final SocketSettingSpec<V> socketSettingSpec, final V value) {
        V recentValue = null;
        @SuppressWarnings("unchecked")
        SocketSettingSpec<Object> socketSttngSpec =
                (SocketSettingSpec<Object>) socketSettingSpec;
        SocketSetting<Object> socketSttng = socketSttngSpec.newSocketSetting(
                socketSettingSpec.getValueType().cast(value));
        SocketSetting<Object> recentSocketSetting = this.socketSettings.put(
                socketSttngSpec, socketSttng);
        if (recentSocketSetting != null) {
            recentValue = socketSettingSpec.getValueType().cast(
                    recentSocketSetting.getValue());
        }
        return recentValue;
    }

    /**
     * Removes the value and its {@code SocketSetting} defined by the provided
     * {@code SocketSettingSpec} of the specified value type and returns the
     * recent value if any.
     *
     * @param socketSettingSpec the provided {@code SocketSettingSpec} of the
     *                          specified value type
     * @param <V>               the specified value type
     * @return the recent value (or {@code null} if there was no recent value)
     */
    public <V> V remove(final SocketSettingSpec<V> socketSettingSpec) {
        V recentValue = null;
        @SuppressWarnings("unchecked")
        SocketSettingSpec<Object> socketSttngSpec =
                (SocketSettingSpec<Object>) socketSettingSpec;
        SocketSetting<Object> recentSocketSetting = this.socketSettings.remove(
                socketSttngSpec);
        if (recentSocketSetting != null) {
            recentValue = socketSettingSpec.getValueType().cast(
                    recentSocketSetting.getValue());
        }
        return recentValue;
    }

    /**
     * Returns an unmodifiable {@code Map} of {@code SocketSetting}s each
     * associated by their {@code SocketSettingSpec}.
     *
     * @return an unmodifiable {@code Map} of {@code SocketSetting}s each
     * associated by their {@code SocketSettingSpec}
     */
    public Map<SocketSettingSpec<Object>, SocketSetting<Object>> toMap() {
        return Collections.unmodifiableMap(this.socketSettings);
    }

    /**
     * Returns the {@code String} representation of this
     * {@code SocketSettings}. The {@code String} representation is a comma
     * separated list of {@code String} representations of
     * {@code SocketSetting}s. If this {@code SocketSettings} has no
     * {@code SocketSetting}s, then the {@code String} representation of this
     * {@code SocketSettings} would be an empty {@code String}.
     *
     * @return the {@code String} representation of this
     * {@code SocketSettings}
     */
    @Override
    public String toString() {
        return this.socketSettings.values().stream()
                .map(SocketSetting::toString)
                .collect(Collectors.joining(","));
    }

}
