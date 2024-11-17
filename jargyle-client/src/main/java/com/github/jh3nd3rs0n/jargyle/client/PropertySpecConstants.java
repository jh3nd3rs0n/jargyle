package com.github.jh3nd3rs0n.jargyle.client;

import java.util.*;

/**
 * All {@code PropertySpec} constants.
 */
final class PropertySpecConstants {

    /**
     * The {@code List} of all {@code PropertySpec} constants.
     */
    private static final List<PropertySpec<Object>> VALUES;

    /**
     * The {@code Map} of all {@code PropertySpec} constants each associated
     * by the name they specify for their {@code Property}.
     */
    private static final Map<String, PropertySpec<Object>> VALUES_MAP;

    static {
        List<PropertySpec<Object>> values = new ArrayList<>();
        values.addAll(DtlsPropertySpecConstants.values());
        values.addAll(GeneralPropertySpecConstants.values());
        values.addAll(Socks5PropertySpecConstants.values());
        values.addAll(SslPropertySpecConstants.values());
        Map<String, PropertySpec<Object>> valuesMap = new HashMap<>();
        valuesMap.putAll(DtlsPropertySpecConstants.valuesMap());
        valuesMap.putAll(GeneralPropertySpecConstants.valuesMap());
        valuesMap.putAll(Socks5PropertySpecConstants.valuesMap());
        valuesMap.putAll(SslPropertySpecConstants.valuesMap());
        VALUES = values;
        VALUES_MAP = valuesMap;
    }

    /**
     * Prevents the construction of unnecessary instances.
     */
    private PropertySpecConstants() {
    }

    /**
     * Returns the {@code PropertySpec} constant based on the provided name
     * the constant specifies for its {@code Property}. An
     * {@code IllegalArgumentException} is thrown if the provided name is not
     * specified by any of the {@code PropertySpec} constants.
     *
     * @param name the provided name the {@code PropertySpec} constant
     *             specifies for its {@code Property}
     * @return the {@code PropertySpec} constant based on the provided name
     * the constant specifies for its {@code Property}
     */
    public static PropertySpec<Object> valueOfName(final String name) {
        if (VALUES_MAP.containsKey(name)) {
            return VALUES_MAP.get(name);
        }
        throw new IllegalArgumentException(String.format(
                "unknown property name: %s", name));
    }

    /**
     * Returns an unmodifiable {@code List} of all {@code PropertySpec}s.
     *
     * @return an unmodifiable {@code List} of all {@code PropertySpec}s
     */
    public static List<PropertySpec<Object>> values() {
        return Collections.unmodifiableList(VALUES);
    }

}
