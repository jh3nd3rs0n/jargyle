package com.github.jh3nd3rs0n.jargyle.common.net;

import java.util.HashMap;
import java.util.Map;

/**
 * All {@code SocketSettingSpec} constants.
 */
final class SocketSettingSpecConstants {

    /**
     * The {@code Map} of all {@code SocketSettingSpec} constants each
     * associated by the name they specify for their {@code SocketSetting}.
     */
    private static final Map<String, SocketSettingSpec<Object>> VALUES_MAP = new HashMap<>(
            StandardSocketSettingSpecConstants.valuesMap());

    /**
     * Prevents the construction of unnecessary instances.
     */
    private SocketSettingSpecConstants() {
    }

    /**
     * Returns the {@code SocketSettingSpec} constant based on the provided
     * name the constant specifies for its {@code SocketSetting}. An
     * {@code IllegalArgumentException} is thrown if the provided name is not
     * specified by any of the {@code SocketSettingSpec} constants.
     *
     * @param name the provided name the {@code SocketSettingSpec} constant
     *             specifies for its {@code SocketSetting}
     * @return the {@code SocketSettingSpec} constant based on the provided
     * name the constant specifies for its {@code SocketSetting}
     */
    public static SocketSettingSpec<Object> valueOfName(final String name) {
        if (VALUES_MAP.containsKey(name)) {
            return VALUES_MAP.get(name);
        }
        throw new IllegalArgumentException(String.format(
                "unknown socket setting name: %s", name));
    }

}
