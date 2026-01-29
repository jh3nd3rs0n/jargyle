package com.github.jh3nd3rs0n.jargyle.client.socks5;

import com.github.jh3nd3rs0n.jargyle.client.Properties;
import com.github.jh3nd3rs0n.jargyle.client.Socks5PropertySpecConstants;
import com.github.jh3nd3rs0n.jargyle.client.internal.client.SocksValueDerivationHelper;
import com.github.jh3nd3rs0n.jargyle.common.security.EncryptedPassword;
import com.github.jh3nd3rs0n.jargyle.protocolbase.socks5.Methods;
import com.github.jh3nd3rs0n.jargyle.protocolbase.socks5.gssapiauthmethod.ProtectionLevels;
import org.ietf.jgss.Oid;

/**
 * Helper class for deriving SOCKS5 specific values from {@code Properties}.
 */
final class Socks5ValueDerivationHelper {

    /**
     * Prevents the construction of unnecessary instances.
     */
    private Socks5ValueDerivationHelper() {
    }

    public static Oid getSocks5GssapiAuthMethodMechanismOidFrom(
            final Properties properties) {
        Oid mechanismOid = properties.getValue(
                Socks5PropertySpecConstants.SOCKS5_GSSAPIAUTHMETHOD_MECHANISM_OID);
        if (mechanismOid != null) {
            return mechanismOid;
        }
        return SocksValueDerivationHelper.getSocksGssapiAuthMethodMechanismOidFrom(
                properties);
    }

    public static boolean getSocks5GssapiAuthMethodNecReferenceImplFrom(
            final Properties properties) {
        Boolean necReferenceImpl = properties.getValue(
                Socks5PropertySpecConstants.SOCKS5_GSSAPIAUTHMETHOD_NEC_REFERENCE_IMPL);
        if (necReferenceImpl != null) {
            return necReferenceImpl;
        }
        return SocksValueDerivationHelper.getSocksGssapiAuthMethodNecReferenceImplFrom(
                properties);
    }

    public static ProtectionLevels getSocks5GssapiAuthMethodProtectionLevelsFrom(
            final Properties properties) {
        ProtectionLevels protectionLevels = properties.getValue(
                Socks5PropertySpecConstants.SOCKS5_GSSAPIAUTHMETHOD_PROTECTION_LEVELS);
        if (protectionLevels != null) {
            return protectionLevels;
        }
        return ProtectionLevels.newInstanceFrom(
                SocksValueDerivationHelper.getSocksGssapiAuthMethodProtectionLevelsFrom(properties).toString());
    }

    public static String getSocks5GssapiAuthMethodServiceNameFrom(
            final Properties properties) {
        String serviceName = properties.getValue(
                Socks5PropertySpecConstants.SOCKS5_GSSAPIAUTHMETHOD_SERVICE_NAME);
        if (serviceName != null) {
            return serviceName;
        }
        return SocksValueDerivationHelper.getSocksGssapiAuthMethodServiceNameFrom(
                properties);
    }

    public static boolean getSocks5GssapiAuthMethodSuggestedConfFrom(
            final Properties properties) {
        Boolean suggestedConf = properties.getValue(
                Socks5PropertySpecConstants.SOCKS5_GSSAPIAUTHMETHOD_SUGGESTED_CONF);
        if (suggestedConf != null) {
            return suggestedConf;
        }
        return SocksValueDerivationHelper.getSocksGssapiAuthMethodSuggestedConfFrom(
                properties);
    }

    public static int getSocks5GssapiAuthMethodSuggestedIntegFrom(
            final Properties properties) {
        Integer suggestedInteg = properties.getValue(
                Socks5PropertySpecConstants.SOCKS5_GSSAPIAUTHMETHOD_SUGGESTED_INTEG);
        if (suggestedInteg != null) {
            return suggestedInteg;
        }
        return SocksValueDerivationHelper.getSocksGssapiAuthMethodSuggestedIntegFrom(
                properties);
    }

    public static Methods getSocks5MethodsFrom(final Properties properties) {
        Methods methods = properties.getValue(
                Socks5PropertySpecConstants.SOCKS5_METHODS);
        if (!methods.toList().isEmpty()) {
            return methods;
        }
        return Methods.newInstanceFrom(
                SocksValueDerivationHelper.getSocksMethodsFrom(properties).toString());
    }

    public static boolean getSocks5Socks5DatagramSocketClientInfoUnavailableFrom(
            final Properties properties) {
        Boolean clientInfoUnavailable = properties.getValue(
                Socks5PropertySpecConstants.SOCKS5_SOCKS5_DATAGRAM_SOCKET_CLIENT_INFO_UNAVAILABLE);
        if (clientInfoUnavailable != null) {
            return clientInfoUnavailable;
        }
        return SocksValueDerivationHelper.getSocksSocksDatagramSocketClientInfoUnavailableFrom(
                properties);
    }

    public static EncryptedPassword getSocks5UserpassAuthMethodPasswordFrom(
            final Properties properties) {
        EncryptedPassword password = properties.getValue(
                Socks5PropertySpecConstants.SOCKS5_USERPASSAUTHMETHOD_PASSWORD);
        if (password != null) {
            return password;
        }
        return SocksValueDerivationHelper.getSocksUserpassAuthMethodPasswordFrom(
                properties);
    }

    public static String getSocks5UserpassAuthMethodUsernameFrom(
            final Properties properties) {
        String username = properties.getValue(
                Socks5PropertySpecConstants.SOCKS5_USERPASSAUTHMETHOD_USERNAME);
        if (username != null) {
            return username;
        }
        return SocksValueDerivationHelper.getSocksUserpassAuthMethodUsernameFrom(
                properties);
    }

}
