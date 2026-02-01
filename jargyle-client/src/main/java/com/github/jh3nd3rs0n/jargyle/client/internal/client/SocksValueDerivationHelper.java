package com.github.jh3nd3rs0n.jargyle.client.internal.client;

import com.github.jh3nd3rs0n.jargyle.client.Properties;
import com.github.jh3nd3rs0n.jargyle.client.SocksPropertySpecConstants;
import com.github.jh3nd3rs0n.jargyle.common.security.EncryptedPassword;
import com.github.jh3nd3rs0n.jargyle.common.string.CommaSeparatedValues;
import org.ietf.jgss.Oid;

/**
 * Helper class for deriving SOCKS specific values from {@code Properties}.
 */
public final class SocksValueDerivationHelper {

    /**
     * Prevents the construction of unnecessary instances.
     */
    private  SocksValueDerivationHelper() {
    }

    public static Oid getSocksGssapiAuthMethodMechanismOidFrom(
            final Properties properties) {
        return properties.getValue(
                SocksPropertySpecConstants.SOCKS_GSSAPIAUTHMETHOD_MECHANISM_OID);
    }

    public static boolean getSocksGssapiAuthMethodNecReferenceImplFrom(
            final Properties properties) {
        return properties.getValue(
                SocksPropertySpecConstants.SOCKS_GSSAPIAUTHMETHOD_NEC_REFERENCE_IMPL);
    }

    public static CommaSeparatedValues getSocksGssapiAuthMethodProtectionLevelsFrom(
            final Properties properties) {
        return properties.getValue(
                SocksPropertySpecConstants.SOCKS_GSSAPIAUTHMETHOD_PROTECTION_LEVELS);
    }

    public static String getSocksGssapiAuthMethodServiceNameFrom(
            final Properties properties) {
        return properties.getValue(
                SocksPropertySpecConstants.SOCKS_GSSAPIAUTHMETHOD_SERVICE_NAME);
    }

    public static boolean getSocksGssapiAuthMethodSuggestedConfFrom(
            final Properties properties) {
        return properties.getValue(
                SocksPropertySpecConstants.SOCKS_GSSAPIAUTHMETHOD_SUGGESTED_CONF);
    }

    public static int getSocksGssapiAuthMethodSuggestedIntegFrom(
            final Properties properties) {
        return properties.getValue(
                SocksPropertySpecConstants.SOCKS_GSSAPIAUTHMETHOD_SUGGESTED_INTEG);
    }

    public static CommaSeparatedValues getSocksMethodsFrom(
            final Properties properties) {
        return properties.getValue(SocksPropertySpecConstants.SOCKS_METHODS);
    }

    public static boolean getSocksSocksDatagramSocketClientInfoUnavailableFrom(
            final Properties properties) {
        return properties.getValue(
                SocksPropertySpecConstants.SOCKS_SOCKS_DATAGRAM_SOCKET_CLIENT_INFO_UNAVAILABLE);
    }

    public static boolean getSocksSocksHostResolverResolveFromSocksServerFrom(
            final Properties properties) {
        return properties.getValue(
                SocksPropertySpecConstants.SOCKS_SOCKS_HOST_RESOLVER_RESOLVE_FROM_SOCKS_SERVER);
    }

    public static EncryptedPassword getSocksUserpassAuthMethodPasswordFrom(
            final Properties properties) {
        return properties.getValue(
                SocksPropertySpecConstants.SOCKS_USERPASSAUTHMETHOD_PASSWORD);
    }

    public static String getSocksUserpassAuthMethodUsernameFrom(
            final Properties properties) {
        return properties.getValue(
                SocksPropertySpecConstants.SOCKS_USERPASSAUTHMETHOD_USERNAME);
    }

}
