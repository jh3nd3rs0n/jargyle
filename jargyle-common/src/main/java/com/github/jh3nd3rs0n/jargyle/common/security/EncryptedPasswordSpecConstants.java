package com.github.jh3nd3rs0n.jargyle.common.security;

import com.github.jh3nd3rs0n.jargyle.common.security.internal.encryptedpassvalue.impl.AesCfbPkcs5PaddingEncryptedPasswordValue;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * All {@code EncryptedPasswordSpec} constants.
 */
public final class EncryptedPasswordSpecConstants {

    /**
     * The {@code EncryptedPasswordSpecs} of the {@code EncryptedPasswordSpec}
     * constants of this class.
     */
    private static final EncryptedPasswordSpecs ENCRYPTED_PASSWORD_SPECS =
            new EncryptedPasswordSpecs();

    /**
     * {@code EncryptedPasswordSpec} constant for {@code EncryptedPassword}
     * that uses the cipher algorithm {@code AES/CFB/PKCS5Padding}.
     */
    public static final EncryptedPasswordSpec AES_CFB_PKCS5PADDING_ENCRYPTED_PASSWORD = ENCRYPTED_PASSWORD_SPECS.addThenGet(new EncryptedPasswordSpec(
            "AesCfbPkcs5PaddingEncryptedPassword") {

        @Override
        public EncryptedPassword newEncryptedPassword(
                final char[] password) {
            return new EncryptedPassword(
                    this,
                    new AesCfbPkcs5PaddingEncryptedPasswordValue(
                            password));
        }

        @Override
        public EncryptedPassword newEncryptedPassword(
                final String encryptedPasswordValue) {
            return new EncryptedPassword(
                    this,
                    new AesCfbPkcs5PaddingEncryptedPasswordValue(
                            encryptedPasswordValue));
        }

    });

    /**
     * The {@code List} of all {@code EncryptedPasswordSpec} constants.
     */
    private static final List<EncryptedPasswordSpec> VALUES =
            ENCRYPTED_PASSWORD_SPECS.toList();

    /**
     * The {@code Map} of all {@code EncryptedPasswordSpec} constants each
     * associated by the type name they specify for their
     * {@code EncryptedPassword}.
     */
    private static final Map<String, EncryptedPasswordSpec> VALUES_MAP =
            ENCRYPTED_PASSWORD_SPECS.toMap();

    /**
     * Prevents the construction of unnecessary instances.
     */
    private EncryptedPasswordSpecConstants() {
    }

    /**
     * Returns the {@code EncryptedPasswordSpec} constant based on the
     * provided type name the constant specifies for its
     * {@code EncryptedPassword}. An {@code IllegalArgumentException} is
     * thrown if the provided type name is not specified by any of the
     * {@code EncryptedPasswordSpec} constants.
     *
     * @param typeName the type name the {@code EncryptedPasswordSpec}
     *                 specifies for its {@code EncryptedPassword}
     * @return the {@code EncryptedPasswordSpec} constant based on the
     * provided type name the constant specifies for its
     * {@code EncryptedPassword}
     */
    public static EncryptedPasswordSpec valueOfTypeName(final String typeName) {
        if (VALUES_MAP.containsKey(typeName)) {
            return VALUES_MAP.get(typeName);
        }
        String str = VALUES.stream()
                .map(EncryptedPasswordSpec::getTypeName)
                .collect(Collectors.joining(", "));
        throw new IllegalArgumentException(String.format(
                "expected encrypted password type name must be one of the "
                        + "following values: %s. actual value is %s",
                str,
                typeName));
    }

    /**
     * Returns the {@code List} of all {@code EncryptedPasswordSpec} constants.
     *
     * @return the {@code List} of all {@code EncryptedPasswordSpec} constants
     */
    public static List<EncryptedPasswordSpec> values() {
        return VALUES;
    }

    /**
     * Returns the {@code Map} of all {@code EncryptedPasswordSpec} constants
     * each associated by the type name they specify for their
     * {@code EncryptedPassword}.
     *
     * @return the {@code Map} of all {@code EncryptedPasswordSpec} constants
     * each associated by the type name they specify for their
     * {@code EncryptedPassword}
     */
    public static Map<String, EncryptedPasswordSpec> valuesMap() {
        return VALUES_MAP;
    }

}
