package jargyle.server;

import org.ietf.jgss.Oid;

import jargyle.client.SocksClient;
import jargyle.client.SocksServerUri;
import jargyle.client.socks5.Socks5Client;
import jargyle.client.socks5.UsernamePassword;
import jargyle.common.net.SocketSettings;
import jargyle.common.net.socks5.AuthMethods;
import jargyle.common.net.socks5.gssapiauth.GssapiProtectionLevels;
import jargyle.common.util.PositiveInteger;

public final class SocksClients {

	private static void configureSocks5ClientBuilder(
			final Socks5Client.Builder builder, 
			final Configuration configuration) {
		Settings settings = configuration.getSettings();
		AuthMethods authMethods = settings.getLastValue(
				SettingSpec.EXTERNAL_CLIENT_SOCKS5_AUTH_METHODS, 
				AuthMethods.class);
		if (!authMethods.equals(Socks5Client.DEFAULT_AUTH_METHODS)) {
			builder.authMethods(authMethods);
		}
		Oid gssapiMechanismOid = settings.getLastValue(
				SettingSpec.EXTERNAL_CLIENT_SOCKS5_GSSAPI_MECHANISM_OID, 
				Oid.class);
		if (!gssapiMechanismOid.equals(
				Socks5Client.DEFAULT_GSSAPI_MECHANISM_OID)) {
			builder.gssapiMechanismOid(gssapiMechanismOid);
		}
		boolean gssapiNecReferenceImpl = settings.getLastValue(
				SettingSpec.EXTERNAL_CLIENT_SOCKS5_GSSAPI_NEC_REFERENCE_IMPL,
				Boolean.class).booleanValue();
		if (gssapiNecReferenceImpl 
				!= Socks5Client.DEFAULT_GSSAPI_NEC_REFERENCE_IMPL) {
			builder.gssapiNecReferenceImpl(gssapiNecReferenceImpl);
		}
		GssapiProtectionLevels gssapiProtectionLevels = 
				settings.getLastValue(
						SettingSpec.EXTERNAL_CLIENT_SOCKS5_GSSAPI_PROTECTION_LEVELS, 
						GssapiProtectionLevels.class);
		if (!gssapiProtectionLevels.equals(
				Socks5Client.DEFAULT_GSSAPI_PROTECTION_LEVELS)) {
			builder.gssapiProtectionLevels(gssapiProtectionLevels);
		}
		String gssapiServiceName = settings.getLastValue(
				SettingSpec.EXTERNAL_CLIENT_SOCKS5_GSSAPI_SERVICE_NAME, 
				String.class);
		if (gssapiServiceName != null) {
			builder.gssapiServiceName(gssapiServiceName);
		}
		UsernamePassword usernamePassword = 
				configuration.getExternalClientSocks5UsernamePassword();
		if (usernamePassword != null) {
			builder.usernamePassword(usernamePassword);
		}		
	}
	
	public static SocksClient newSocksClient(
			final Configuration configuration) {
		Settings settings = configuration.getSettings();
		SocksServerUri socksServerUri = settings.getLastValue(
				SettingSpec.EXTERNAL_CLIENT_EXTERNAL_SERVER_URI, 
				SocksServerUri.class);
		SocksClient.Builder builder = null;
		if (socksServerUri == null) {
			return null;
		} else {
			builder = socksServerUri.newSocksClientBuilder();
			String bindHost = settings.getLastValue(
					SettingSpec.EXTERNAL_CLIENT_BIND_HOST, String.class);
			if (bindHost != null) {
				builder.bindHost(bindHost);
			}
			int connectTimeout = settings.getLastValue(
					SettingSpec.EXTERNAL_CLIENT_CONNECT_TIMEOUT, 
					PositiveInteger.class).intValue();
			if (connectTimeout != SocksClient.DEFAULT_CONNECT_TIMEOUT) {
				builder.connectTimeout(connectTimeout);
			}
			SocketSettings socketSettings = settings.getLastValue(
					SettingSpec.EXTERNAL_CLIENT_SOCKET_SETTINGS, 
					SocketSettings.class);
			if (socketSettings != SocksClient.DEFAULT_SOCKET_SETTINGS) {
				builder.socketSettings(socketSettings);
			}
		}
		if (builder instanceof Socks5Client.Builder) {
			Socks5Client.Builder socks5ClientBuilder = 
					(Socks5Client.Builder) builder;
			configureSocks5ClientBuilder(socks5ClientBuilder, configuration);
		} else {
			throw new AssertionError(String.format(
					"unhandled %s: %s", 
					SocksClient.Builder.class.getSimpleName(), 
					builder.getClass().getSimpleName()));
		}
		return builder.build();
	}
	
	private SocksClients() { }
	
}
