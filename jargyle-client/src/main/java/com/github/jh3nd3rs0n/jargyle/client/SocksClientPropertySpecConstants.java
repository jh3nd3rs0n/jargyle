package com.github.jh3nd3rs0n.jargyle.client;

import java.util.*;

/**
 * All SOCKS client specific {@code PropertySpec} constants.
 */
final class SocksClientPropertySpecConstants {

    /**
     * The {@code List} of the {@code PropertySpec} constants.
     */
    private static final List<PropertySpec<Object>> VALUES;

    /**
     * The {@code Map} of the {@code PropertySpec} constants each associated
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
    private SocksClientPropertySpecConstants() {
    }

    /**
     * Returns an unmodifiable {@code List} of the {@code PropertySpec}
     * constants.
     *
     * @return an unmodifiable {@code List} of the {@code PropertySpec}
     * constants
     */
    public static List<PropertySpec<Object>> values() {
        return Collections.unmodifiableList(VALUES);
    }

    /**
     * Returns an unmodifiable {@code Map} of the {@code PropertySpec}
     * constants each associated by the name they specify for their
     * {@code Property}.
     *
     * @return an unmodifiable {@code Map} of the {@code PropertySpec}
     * constants each associated by the name they specify for their
     * {@code Property}
     */
    public static Map<String, PropertySpec<Object>> valuesMap() {
        return Collections.unmodifiableMap(VALUES_MAP);
    }

}
