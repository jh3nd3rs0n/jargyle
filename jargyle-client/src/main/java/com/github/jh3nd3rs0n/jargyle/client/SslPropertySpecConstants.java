package com.github.jh3nd3rs0n.jargyle.client;

import com.github.jh3nd3rs0n.jargyle.client.internal.propertyspec.impl.*;
import com.github.jh3nd3rs0n.jargyle.common.security.EncryptedPassword;
import com.github.jh3nd3rs0n.jargyle.common.string.CommaSeparatedValues;
import com.github.jh3nd3rs0n.jargyle.internal.annotation.NameValuePairValueSpecDoc;
import com.github.jh3nd3rs0n.jargyle.internal.annotation.NameValuePairValueSpecsDoc;

import java.io.File;
import java.util.List;
import java.util.Map;

/**
 * SSL/TLS specific {@code PropertySpec} constants.
 */
@NameValuePairValueSpecsDoc(
        description = "SSL/TLS specific properties",
        name = "SSL/TLS Properties"
)
public final class SslPropertySpecConstants {

    /**
     * The {@code PropertySpecs} of the {@code PropertySpec} constants of
     * this class.
     */
    private static final PropertySpecs PROPERTY_SPECS = new PropertySpecs();

    /**
     * {@code PropertySpec} constant for {@code socksClient.ssl.enabled}: the
     * {@code Boolean} value to indicate if SSL/TLS connections to the SOCKS
     * server are enabled (default is {@code false}).
     */
    @NameValuePairValueSpecDoc(
            description = "The boolean value to indicate if SSL/TLS connections "
                    + "to the SOCKS server are enabled (default is false)",
            name = "socksClient.ssl.enabled",
            syntax = "socksClient.ssl.enabled=true|false",
            valueType = Boolean.class
    )
    public static final PropertySpec<Boolean> SSL_ENABLED =
            PROPERTY_SPECS.addThenGet(new BooleanPropertySpec(
                    "socksClient.ssl.enabled",
                    Boolean.FALSE));

    /**
     * {@code PropertySpec} constant for
     * {@code socksClient.ssl.enabledCipherSuites}: the
     * {@code CommaSeparatedValues} of acceptable cipher suites enabled for
     * SSL/TLS connections to the SOCKS server.
     */
    @NameValuePairValueSpecDoc(
            description = "The comma separated list of acceptable cipher "
                    + "suites enabled for SSL/TLS connections to the SOCKS "
                    + "server",
            name = "socksClient.ssl.enabledCipherSuites",
            syntax = "socksClient.ssl.enabledCipherSuites=COMMA_SEPARATED_VALUES",
            valueType = CommaSeparatedValues.class
    )
    public static final PropertySpec<CommaSeparatedValues> SSL_ENABLED_CIPHER_SUITES =
            PROPERTY_SPECS.addThenGet(new CommaSeparatedValuesPropertySpec(
                    "socksClient.ssl.enabledCipherSuites",
                    null));

    /**
     * {@code PropertySpec} constant for
     * {@code socksClient.ssl.enabledProtocols}: the
     * {@code CommaSeparatedValues} of acceptable protocol versions enabled
     * for SSL/TLS connections to the SOCKS server.
     */
    @NameValuePairValueSpecDoc(
            description = "The comma separated list of acceptable protocol "
                    + "versions enabled for SSL/TLS connections to the SOCKS "
                    + "server",
            name = "socksClient.ssl.enabledProtocols",
            syntax = "socksClient.ssl.enabledProtocols=COMMA_SEPARATED_VALUES",
            valueType = CommaSeparatedValues.class
    )
    public static final PropertySpec<CommaSeparatedValues> SSL_ENABLED_PROTOCOLS =
            PROPERTY_SPECS.addThenGet(new CommaSeparatedValuesPropertySpec(
                    "socksClient.ssl.enabledProtocols",
                    null));

    /**
     * {@code PropertySpec} constant for {@code socksClient.ssl.keyStoreFile}:
     * the {@code File} for the key store file for the SSL/TLS connections to
     * the SOCKS server.
     */
    @NameValuePairValueSpecDoc(
            description = "The key store file for the SSL/TLS connections to "
                    + "the SOCKS server",
            name = "socksClient.ssl.keyStoreFile",
            syntax = "socksClient.ssl.keyStoreFile=FILE",
            valueType = File.class
    )
    public static final PropertySpec<File> SSL_KEY_STORE_FILE =
            PROPERTY_SPECS.addThenGet(new FilePropertySpec(
                    "socksClient.ssl.keyStoreFile",
                    null));

    /**
     * {@code PropertySpec} constant for
     * {@code socksClient.ssl.keyStorePassword}: the {@code EncryptedPassword}
     * for the password for the key store for the SSL/TLS connections to the
     * SOCKS server.
     */
    @NameValuePairValueSpecDoc(
            description = "The password for the key store for the SSL/TLS "
                    + "connections to the SOCKS server",
            name = "socksClient.ssl.keyStorePassword",
            syntax = "socksClient.ssl.keyStorePassword=PASSWORD",
            valueType = String.class
    )
    public static final PropertySpec<EncryptedPassword> SSL_KEY_STORE_PASSWORD =
            PROPERTY_SPECS.addThenGet(new EncryptedPasswordPropertySpec(
                    "socksClient.ssl.keyStorePassword",
                    EncryptedPassword.newInstance(new char[]{})));

    /**
     * {@code PropertySpec} constant for {@code socksClient.ssl.keyStoreType}:
     * the type of key store file for the SSL/TLS connections to the SOCKS
     * server (default is {@code PKCS12}).
     */
    @NameValuePairValueSpecDoc(
            description = "The type of key store file for the SSL/TLS "
                    + "connections to the SOCKS server (default is PKCS12)",
            name = "socksClient.ssl.keyStoreType",
            syntax = "socksClient.ssl.keyStoreType=TYPE",
            valueType = String.class
    )
    public static final PropertySpec<String> SSL_KEY_STORE_TYPE =
            PROPERTY_SPECS.addThenGet(new StringPropertySpec(
                    "socksClient.ssl.keyStoreType",
                    "PKCS12"));

    /**
     * {@code PropertySpec} constant for {@code socksClient.ssl.protocol}: the
     * protocol version for the SSL/TLS connections to the SOCKS server
     * (default is {@code TLSv1.2}).
     */
    @NameValuePairValueSpecDoc(
            description = "The protocol version for the SSL/TLS connections to "
                    + "the SOCKS server (default is TLSv1.2)",
            name = "socksClient.ssl.protocol",
            syntax = "socksClient.ssl.protocol=PROTOCOL",
            valueType = String.class
    )
    public static final PropertySpec<String> SSL_PROTOCOL =
            PROPERTY_SPECS.addThenGet(new StringPropertySpec(
                    "socksClient.ssl.protocol",
                    "TLSv1.2"));

    /**
     * {@code PropertySpec} constant for
     * {@code socksClient.ssl.trustStoreFile}: the {@code File} for the trust
     * store file for the SSL/TLS connections to the SOCKS server.
     */
    @NameValuePairValueSpecDoc(
            description = "The trust store file for the SSL/TLS connections to "
                    + "the SOCKS server",
            name = "socksClient.ssl.trustStoreFile",
            syntax = "socksClient.ssl.trustStoreFile=FILE",
            valueType = File.class
    )
    public static final PropertySpec<File> SSL_TRUST_STORE_FILE =
            PROPERTY_SPECS.addThenGet(new FilePropertySpec(
                    "socksClient.ssl.trustStoreFile",
                    null));

    /**
     * {@code PropertySpec} constant for
     * {@code socksClient.ssl.trustStorePassword}: the
     * {@code EncryptedPassword} for the password for the trust store for the
     * SSL/TLS connections to the SOCKS server.
     */
    @NameValuePairValueSpecDoc(
            description = "The password for the trust store for the SSL/TLS "
                    + "connections to the SOCKS server",
            name = "socksClient.ssl.trustStorePassword",
            syntax = "socksClient.ssl.trustStorePassword=PASSWORD",
            valueType = String.class
    )
    public static final PropertySpec<EncryptedPassword> SSL_TRUST_STORE_PASSWORD =
            PROPERTY_SPECS.addThenGet(new EncryptedPasswordPropertySpec(
                    "socksClient.ssl.trustStorePassword",
                    EncryptedPassword.newInstance(new char[]{})));

    /**
     * {@code PropertySpec} constant for
     * {@code socksClient.ssl.trustStoreType}: the type of trust store file
     * for the SSL/TLS connections to the SOCKS server (default is
     * {@code PKCS12}).
     */
    @NameValuePairValueSpecDoc(
            description = "The type of trust store file for the SSL/TLS "
                    + "connections to the SOCKS server (default is PKCS12)",
            name = "socksClient.ssl.trustStoreType",
            syntax = "socksClient.ssl.trustStoreType=TYPE",
            valueType = String.class
    )
    public static final PropertySpec<String> SSL_TRUST_STORE_TYPE =
            PROPERTY_SPECS.addThenGet(new StringPropertySpec(
                    "socksClient.ssl.trustStoreType",
                    "PKCS12"));

    /**
     * Prevents the construction of unnecessary instances.
     */
    private SslPropertySpecConstants() {
    }

    /**
     * Returns an unmodifiable {@code List} of the {@code PropertySpec}
     * constants.
     *
     * @return an unmodifiable {@code List} of the {@code PropertySpec}
     * constants
     */
    public static List<PropertySpec<Object>> values() {
        return PROPERTY_SPECS.toList();
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
        return PROPERTY_SPECS.toMap();
    }

}
