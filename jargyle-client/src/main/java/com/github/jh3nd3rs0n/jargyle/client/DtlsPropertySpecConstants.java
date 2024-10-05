package com.github.jh3nd3rs0n.jargyle.client;

import com.github.jh3nd3rs0n.jargyle.client.internal.propertyspec.impl.*;
import com.github.jh3nd3rs0n.jargyle.common.number.PositiveInteger;
import com.github.jh3nd3rs0n.jargyle.common.security.EncryptedPassword;
import com.github.jh3nd3rs0n.jargyle.common.string.CommaSeparatedValues;
import com.github.jh3nd3rs0n.jargyle.internal.annotation.NameValuePairValueSpecDoc;
import com.github.jh3nd3rs0n.jargyle.internal.annotation.NameValuePairValueSpecsDoc;

import java.io.File;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

/**
 * DTLS specific {@code PropertySpec} constants.
 */
@NameValuePairValueSpecsDoc(
        description = "DTLS specific properties",
        name = "DTLS Properties"
)
public final class DtlsPropertySpecConstants {

    /**
     * The {@code PropertySpecs} of the {@code PropertySpec} constants of
     * this class.
     */
    private static final PropertySpecs PROPERTY_SPECS = new PropertySpecs();

    /**
     * {@code PropertySpec} constant for {@code socksClient.dtls.enabled}: the
     * {@code Boolean} value to indicate if DTLS connections to the SOCKS
	 * server are enabled (default is {@code false}).
     */
    @NameValuePairValueSpecDoc(
            description = "The boolean value to indicate if DTLS connections "
                    + "to the SOCKS server are enabled (default is false)",
            name = "socksClient.dtls.enabled",
            syntax = "socksClient.dtls.enabled=true|false",
            valueType = Boolean.class
    )
    public static final PropertySpec<Boolean> DTLS_ENABLED =
            PROPERTY_SPECS.addThenGet(new BooleanPropertySpec(
                    "socksClient.dtls.enabled",
                    Boolean.FALSE));

    /**
     * {@code PropertySpec} constant for
     * {@code socksClient.dtls.enabledCipherSuites}: the
	 * {@code CommaSeparatedValues} of acceptable cipher suites enabled for
	 * DTLS connections to the SOCKS server.
     */
    @NameValuePairValueSpecDoc(
            description = "The comma separated list of acceptable cipher "
                    + "suites enabled for DTLS connections to the SOCKS server",
            name = "socksClient.dtls.enabledCipherSuites",
            syntax = "socksClient.dtls.enabledCipherSuites=COMMA_SEPARATED_VALUES",
            valueType = CommaSeparatedValues.class
    )
    public static final PropertySpec<CommaSeparatedValues> DTLS_ENABLED_CIPHER_SUITES =
            PROPERTY_SPECS.addThenGet(new CommaSeparatedValuesPropertySpec(
                    "socksClient.dtls.enabledCipherSuites",
                    null));

    /**
     * {@code PropertySpec} constant for
     * {@code socksClient.dtls.enabledProtocols}: the
	 * {@code CommaSeparatedValues} of acceptable protocol versions enabled
	 * for DTLS connections to the SOCKS server.
     */
    @NameValuePairValueSpecDoc(
            description = "The comma separated list of acceptable protocol "
                    + "versions enabled for DTLS connections to the SOCKS "
                    + "server",
            name = "socksClient.dtls.enabledProtocols",
            syntax = "socksClient.dtls.enabledProtocols=COMMA_SEPARATED_VALUES",
            valueType = CommaSeparatedValues.class
    )
    public static final PropertySpec<CommaSeparatedValues> DTLS_ENABLED_PROTOCOLS =
            PROPERTY_SPECS.addThenGet(new CommaSeparatedValuesPropertySpec(
                    "socksClient.dtls.enabledProtocols",
                    null));

    /**
     * {@code PropertySpec} constant for
     * {@code socksClient.dtls.maxPacketSize}: the {@code PositiveInteger} for
	 * the maximum packet size for the DTLS connections to SOCKS server
	 * (default is {@code 32768}).
     */
    @NameValuePairValueSpecDoc(
            description = "The maximum packet size for the DTLS connections to "
                    + "the SOCKS server (default is 32768)",
            name = "socksClient.dtls.maxPacketSize",
            syntax = "socksClient.dtls.maxPacketSize=POSITIVE_INTEGER",
            valueType = PositiveInteger.class
    )
    public static final PropertySpec<PositiveInteger> DTLS_MAX_PACKET_SIZE =
            PROPERTY_SPECS.addThenGet(new PositiveIntegerPropertySpec(
                    "socksClient.dtls.maxPacketSize",
                    PositiveInteger.valueOf(32768)));

    /**
     * {@code PropertySpec} constant for {@code socksClient.dtls.protocol}:
     * the protocol version for the DTLS connections to the SOCKS server
     * (default is {@code DTLSv1.2}).
     */
    @NameValuePairValueSpecDoc(
            description = "The protocol version for the DTLS connections to "
                    + "the SOCKS server (default is DTLSv1.2)",
            name = "socksClient.dtls.protocol",
            syntax = "socksClient.dtls.protocol=PROTOCOL",
            valueType = String.class
    )
    public static final PropertySpec<String> DTLS_PROTOCOL =
            PROPERTY_SPECS.addThenGet(new StringPropertySpec(
                    "socksClient.dtls.protocol",
                    "DTLSv1.2"));

    /**
     * {@code PropertySpec} constant for
     * {@code socksClient.dtls.trustStoreFile}: the {@code File} for the trust
	 * store file for the DTLS connections to the SOCKS server.
     */
    @NameValuePairValueSpecDoc(
            description = "The trust store file for the DTLS connections to "
                    + "the SOCKS server",
            name = "socksClient.dtls.trustStoreFile",
            syntax = "socksClient.dtls.trustStoreFile=FILE",
            valueType = File.class
    )
    public static final PropertySpec<File> DTLS_TRUST_STORE_FILE =
            PROPERTY_SPECS.addThenGet(new FilePropertySpec(
                    "socksClient.dtls.trustStoreFile",
                    null));

    /**
     * {@code PropertySpec} constant for
     * {@code socksClient.dtls.trustStoreInputStream}: the {@code InputStream}
     * of the trust store for the DTLS connections to the SOCKS server.
     */
    public static final PropertySpec<InputStream> DTLS_TRUST_STORE_INPUT_STREAM =
            PROPERTY_SPECS.addThenGet(new InputStreamPropertySpec(
                    "socksClient.dtls.trustStoreInputStream",
                    null));

    /**
     * {@code PropertySpec} constant for
     * {@code socksClient.dtls.trustStorePassword}: the
	 * {@code EncryptedPassword} for the password for the trust store for the
	 * DTLS connections to the SOCKS server.
     */
    @NameValuePairValueSpecDoc(
            description = "The password for the trust store for the DTLS "
                    + "connections to the SOCKS server",
            name = "socksClient.dtls.trustStorePassword",
            syntax = "socksClient.dtls.trustStorePassword=PASSWORD",
            valueType = String.class
    )
    public static final PropertySpec<EncryptedPassword> DTLS_TRUST_STORE_PASSWORD =
            PROPERTY_SPECS.addThenGet(new EncryptedPasswordPropertySpec(
                    "socksClient.dtls.trustStorePassword",
                    EncryptedPassword.newInstance(new char[]{})));

    /**
     * {@code PropertySpec} constant for
     * {@code socksClient.dtls.trustStoreType}: the type of trust store file
     * for the DTLS connections to the SOCKS server (default is
     * {@code PKCS12}).
     */
    @NameValuePairValueSpecDoc(
            description = "The type of trust store file for the DTLS "
                    + "connections to the SOCKS server (default is PKCS12)",
            name = "socksClient.dtls.trustStoreType",
            syntax = "socksClient.dtls.trustStoreType=TYPE",
            valueType = String.class
    )
    public static final PropertySpec<String> DTLS_TRUST_STORE_TYPE =
            PROPERTY_SPECS.addThenGet(new StringPropertySpec(
                    "socksClient.dtls.trustStoreType",
                    "PKCS12"));

    /**
     * Prevents the construction of unnecessary instances.
     */
    private DtlsPropertySpecConstants() {
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
