package com.github.jh3nd3rs0n.jargyle.common.security;

import java.util.*;

/**
 * A collection of {@code EncryptedPasswordSpec}s.
 */
final class EncryptedPasswordSpecs {

    /**
     * The {@code List} of {@code EncryptedPasswordSpec}s.
     */
    private final List<EncryptedPasswordSpec> encryptedPasswordSpecs;

    /**
     * The {@code Map} of {@code EncryptedPasswordSpec}s each associated by
     * the type name they specify for their {@code EncryptedPassword}.
     */
    private final Map<String, EncryptedPasswordSpec> encryptedPasswordSpecsMap;

    /**
     * Constructs an {@code EncryptedPasswordSpecs}.
     */
    public EncryptedPasswordSpecs() {
        this.encryptedPasswordSpecs = new ArrayList<>();
        this.encryptedPasswordSpecsMap = new HashMap<>();
    }

    /**
     * Adds the provided {@code EncryptedPasswordSpec} to this
     * {@code EncryptedPasswordSpecs} and return the added
     * {@code EncryptedPasswordSpec}.
     *
     * @param value the provided {@code EncryptedPasswordSpec}
     * @return the added {@code EncryptedPasswordSpec}
     */
    public EncryptedPasswordSpec addThenGet(final EncryptedPasswordSpec value) {
        this.encryptedPasswordSpecs.add(value);
        this.encryptedPasswordSpecsMap.put(value.getTypeName(), value);
        return value;
    }

    /**
     * Returns an unmodifiable {@code List} of {@code EncryptedPasswordSpec}s.
     *
     * @return an unmodifiable {@code List} of {@code EncryptedPasswordSpec}s
     */
    public List<EncryptedPasswordSpec> toList() {
        return Collections.unmodifiableList(this.encryptedPasswordSpecs);
    }

    /**
     * Returns an unmodifiable {@code Map} of {@code EncryptedPasswordSpec}s
     * each associated by the type name they specify for their
     * {@code EncryptedPassword}.
     *
     * @return an unmodifiable {@code Map} of {@code EncryptedPasswordSpec}s
     * each associated by the type name they specify for their
     * {@code EncryptedPassword}
     */
    public Map<String, EncryptedPasswordSpec> toMap() {
        return Collections.unmodifiableMap(this.encryptedPasswordSpecsMap);
    }

}
