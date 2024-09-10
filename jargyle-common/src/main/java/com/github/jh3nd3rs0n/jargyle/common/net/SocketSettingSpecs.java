package com.github.jh3nd3rs0n.jargyle.common.net;

import java.util.*;

/**
 * A collection of {@code SocketSettingSpec}s.
 */
final class SocketSettingSpecs {

    /**
     * The {@code List} of {@code SocketSettingSpec}s.
     */
    private final List<SocketSettingSpec<Object>> socketSettingSpecs;

    /**
     * The {@code Map} of {@code SocketSettingSpec}s each associated by the
     * name they specify for their {@code SocketSetting}.
     */
    private final Map<String, SocketSettingSpec<Object>> socketSettingSpecsMap;

    /**
     * Constructs a {@code SocketSettingSpecs}.
     */
    public SocketSettingSpecs() {
        this.socketSettingSpecs = new ArrayList<>();
        this.socketSettingSpecsMap = new HashMap<>();
    }

    /**
     * Adds the provided {@code SocketSettingSpec} of the specified value type
     * to this {@code SocketSettingSpecs} and then returns the added
     * {@code SocketSettingSpec}.
     *
     * @param value the provided {@code SocketSettingSpec} of the specified
     *              value type
     * @param <T>   the specified value type
     * @return the added {@code SocketSettingSpec}
     */
    public <T> SocketSettingSpec<T> addThenGet(
            final SocketSettingSpec<T> value) {
        @SuppressWarnings("unchecked")
        SocketSettingSpec<Object> val = (SocketSettingSpec<Object>) value;
        this.socketSettingSpecs.add(val);
        this.socketSettingSpecsMap.put(val.getName(), val);
        return value;
    }

    /**
     * Returns an unmodifiable {@code List} of {@code SocketSettingSpec}s.
     *
     * @return an unmodifiable {@code List} of {@code SocketSettingSpec}s
     */
    public List<SocketSettingSpec<Object>> toList() {
        return Collections.unmodifiableList(this.socketSettingSpecs);
    }

    /**
     * Returns an unmodifiable {@code Map} of {@code SocketSettingSpec}s each
     * associated by the name they specify for their {@code SocketSetting}.
     *
     * @return an unmodifiable {@code Map} of {@code SocketSettingSpec}s each
     * associated by the name they specify for their {@code SocketSetting}
     */
    public Map<String, SocketSettingSpec<Object>> toMap() {
        return Collections.unmodifiableMap(this.socketSettingSpecsMap);
    }

}
