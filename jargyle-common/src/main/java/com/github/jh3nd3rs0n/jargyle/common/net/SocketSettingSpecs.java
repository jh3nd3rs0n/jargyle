package com.github.jh3nd3rs0n.jargyle.common.net;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * A collection of {@code SocketSettingSpec}s.
 */
final class SocketSettingSpecs {

    /**
     * The {@code Map} of {@code SocketSettingSpec}s each associated by the
     * name they specify for their {@code SocketSetting}.
     */
    private final Map<String, SocketSettingSpec<Object>> socketSettingSpecsMap;

    /**
     * Constructs a {@code SocketSettingSpecs}.
     */
    public SocketSettingSpecs() {
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
        this.socketSettingSpecsMap.put(val.getName(), val);
        return value;
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
