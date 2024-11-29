package com.github.jh3nd3rs0n.jargyle.common.security;

/**
 * All security specific system property name constants.
 */
public final class SystemPropertyNameConstants {

    /**
     * The name of the system property for the file of the partial password
     * to be used for encryption/decryption.
     */
    public static final String PARTIAL_ENCRYPTION_PASSWORD_FILE_SYSTEM_PROPERTY_NAME =
            "com.github.jh3nd3rs0n.jargyle.common.security.partialEncryptionPasswordFile";

    /**
     * The name of the system property for the partial password to be used for
     * encryption/decryption.
     */
    public static final String PARTIAL_ENCRYPTION_PASSWORD_SYSTEM_PROPERTY_NAME =
            "com.github.jh3nd3rs0n.jargyle.common.security.partialEncryptionPassword";

    /**
     * Prevents the construction of unnecessary instances.
     */
    private SystemPropertyNameConstants() {
    }

}
