package jargyle.server.socks5;

import jargyle.ResourceHelper;
import jargyle.ResourceNameConstants;
import jargyle.client.Properties;
import jargyle.client.PropertySpec;
import jargyle.client.socks5.Socks5Client;
import jargyle.client.socks5.Socks5ServerUri;
import jargyle.client.socks5.UsernamePassword;
import jargyle.common.net.socks5.AuthMethod;
import jargyle.common.net.socks5.AuthMethods;
import jargyle.common.net.socks5.gssapiauth.GssapiProtectionLevels;

public final class Socks5ClientHelper {

	public static Socks5Client newSocks5Client(
			final String host, final Integer port) {
		return new Socks5ServerUri(host, port).newSocksClient(
				Properties.newInstance());
	}
	
	public static Socks5Client newSocks5Client(
			final String host, 
			final Integer port,
			final String gssapiServiceName,
			final GssapiProtectionLevels gssapiProtectionLevels, 
			final boolean gssapiNecReferenceImpl) {
		Properties properties = Properties.newInstance(
				PropertySpec.SOCKS5_AUTH_METHODS.newProperty(
						AuthMethods.newInstance(AuthMethod.GSSAPI)),
				PropertySpec.SOCKS5_GSSAPI_SERVICE_NAME.newProperty(
						gssapiServiceName),
				PropertySpec.SOCKS5_GSSAPI_PROTECTION_LEVELS.newProperty(
						gssapiProtectionLevels),
				PropertySpec.SOCKS5_GSSAPI_NEC_REFERENCE_IMPL.newProperty(
						Boolean.valueOf(gssapiNecReferenceImpl)));
		return new Socks5ServerUri(host, port).newSocksClient(properties);
	}
	
	public static Socks5Client newSocks5Client(
			final String host, 
			final Integer port, 
			final UsernamePassword usernamePassword) {
		Properties properties = Properties.newInstance(
				PropertySpec.SOCKS5_AUTH_METHODS.newProperty(
						AuthMethods.newInstance(AuthMethod.USERNAME_PASSWORD)),
				PropertySpec.SOCKS5_USERNAME.newProperty(
						usernamePassword.getUsername()),
				PropertySpec.SOCKS5_PASSWORD.newProperty(
						usernamePassword.getEncryptedPassword()));
		return new Socks5ServerUri(host, port).newSocksClient(properties);
	}
	
	public static Socks5Client newSocks5ClientUsingSsl(
			final String host, 
			final Integer port) {
		Properties properties = Properties.newInstance(
				PropertySpec.SSL_ENABLED.newProperty(Boolean.TRUE),
				PropertySpec.SSL_TRUST_STORE_FILE.newProperty(
						ResourceHelper.getResourceAsFile(
								ResourceNameConstants.SERVER_KEY_STORE_FILE)),
				PropertySpec.SSL_TRUST_STORE_PASSWORD.newProperty(
						ResourceHelper.getResourceAsString(
								ResourceNameConstants.SERVER_KEY_STORE_PASSWORD_FILE)));
		return new Socks5ServerUri(host, port).newSocksClient(properties);
	}
	
	public static Socks5Client newSocks5ClientUsingSslAndClientAuth(
			final String host, 
			final Integer port) {
		Properties properties = Properties.newInstance(
				PropertySpec.SSL_ENABLED.newProperty(Boolean.TRUE),
				PropertySpec.SSL_KEY_STORE_FILE.newProperty(
						ResourceHelper.getResourceAsFile(
								ResourceNameConstants.CLIENT_KEY_STORE_FILE)),
				PropertySpec.SSL_KEY_STORE_PASSWORD.newProperty(
						ResourceHelper.getResourceAsString(
								ResourceNameConstants.CLIENT_KEY_STORE_PASSWORD_FILE)),
				PropertySpec.SSL_TRUST_STORE_FILE.newProperty(
						ResourceHelper.getResourceAsFile(
								ResourceNameConstants.SERVER_KEY_STORE_FILE)),
				PropertySpec.SSL_TRUST_STORE_PASSWORD.newProperty(
						ResourceHelper.getResourceAsString(
								ResourceNameConstants.SERVER_KEY_STORE_PASSWORD_FILE)));
		return new Socks5ServerUri(host, port).newSocksClient(properties);
	}
	
	private Socks5ClientHelper() { }
	
}
