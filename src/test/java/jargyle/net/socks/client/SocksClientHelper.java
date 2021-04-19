package jargyle.net.socks.client;

import jargyle.ResourceHelper;
import jargyle.ResourceNameConstants;
import jargyle.net.socks.client.v5.Socks5Client;
import jargyle.net.socks.client.v5.Socks5ServerUri;
import jargyle.net.socks.client.v5.UsernamePassword;
import jargyle.net.socks.transport.v5.AuthMethod;
import jargyle.net.socks.transport.v5.AuthMethods;
import jargyle.net.socks.transport.v5.gssapiauth.ProtectionLevels;

public final class SocksClientHelper {

	public static Socks5Client newSocks5Client(
			final String host, final Integer port) {
		return new Socks5ServerUri(host, port).newSocksClient(
				Properties.newInstance());
	}
	
	public static Socks5Client newSocks5Client(
			final String host, 
			final Integer port,
			final String gssapiServiceName,
			final ProtectionLevels protectionLevels, 
			final boolean gssapiNecReferenceImpl) {
		Properties properties = Properties.newInstance(
				PropertySpec.SOCKS5_AUTH_METHODS.newProperty(
						AuthMethods.newInstance(AuthMethod.GSSAPI)),
				PropertySpec.SOCKS5_GSSAPI_SERVICE_NAME.newProperty(
						gssapiServiceName),
				PropertySpec.SOCKS5_GSSAPI_PROTECTION_LEVELS.newProperty(
						protectionLevels),
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
				PropertySpec.DTLS_ENABLED.newProperty(Boolean.TRUE),
				PropertySpec.DTLS_TRUST_STORE_FILE.newProperty(
						ResourceHelper.getResourceAsFile(
								ResourceNameConstants.JARGYLE_SECURITY_SERVER_KEY_STORE_FILE)),
				PropertySpec.DTLS_TRUST_STORE_PASSWORD.newPropertyOfParsableValue(
						ResourceHelper.getResourceAsString(
								ResourceNameConstants.JARGYLE_SECURITY_SERVER_KEY_STORE_PASSWORD_FILE)),				
				PropertySpec.SSL_ENABLED.newProperty(Boolean.TRUE),
				PropertySpec.SSL_TRUST_STORE_FILE.newProperty(
						ResourceHelper.getResourceAsFile(
								ResourceNameConstants.JARGYLE_SECURITY_SERVER_KEY_STORE_FILE)),
				PropertySpec.SSL_TRUST_STORE_PASSWORD.newPropertyOfParsableValue(
						ResourceHelper.getResourceAsString(
								ResourceNameConstants.JARGYLE_SECURITY_SERVER_KEY_STORE_PASSWORD_FILE)));
		return new Socks5ServerUri(host, port).newSocksClient(properties);
	}
	
	public static Socks5Client newSocks5ClientUsingSslAndClientAuth(
			final String host, 
			final Integer port) {
		Properties properties = Properties.newInstance(
				PropertySpec.DTLS_ENABLED.newProperty(Boolean.TRUE),
				PropertySpec.DTLS_KEY_STORE_FILE.newProperty(
						ResourceHelper.getResourceAsFile(
								ResourceNameConstants.JARGYLE_SECURITY_CLIENT_KEY_STORE_FILE)),
				PropertySpec.DTLS_KEY_STORE_PASSWORD.newPropertyOfParsableValue(
						ResourceHelper.getResourceAsString(
								ResourceNameConstants.JARGYLE_SECURITY_CLIENT_KEY_STORE_PASSWORD_FILE)),
				PropertySpec.DTLS_TRUST_STORE_FILE.newProperty(
						ResourceHelper.getResourceAsFile(
								ResourceNameConstants.JARGYLE_SECURITY_SERVER_KEY_STORE_FILE)),
				PropertySpec.DTLS_TRUST_STORE_PASSWORD.newPropertyOfParsableValue(
						ResourceHelper.getResourceAsString(
								ResourceNameConstants.JARGYLE_SECURITY_SERVER_KEY_STORE_PASSWORD_FILE)),
				PropertySpec.SSL_ENABLED.newProperty(Boolean.TRUE),
				PropertySpec.SSL_KEY_STORE_FILE.newProperty(
						ResourceHelper.getResourceAsFile(
								ResourceNameConstants.JARGYLE_SECURITY_CLIENT_KEY_STORE_FILE)),
				PropertySpec.SSL_KEY_STORE_PASSWORD.newPropertyOfParsableValue(
						ResourceHelper.getResourceAsString(
								ResourceNameConstants.JARGYLE_SECURITY_CLIENT_KEY_STORE_PASSWORD_FILE)),
				PropertySpec.SSL_TRUST_STORE_FILE.newProperty(
						ResourceHelper.getResourceAsFile(
								ResourceNameConstants.JARGYLE_SECURITY_SERVER_KEY_STORE_FILE)),
				PropertySpec.SSL_TRUST_STORE_PASSWORD.newPropertyOfParsableValue(
						ResourceHelper.getResourceAsString(
								ResourceNameConstants.JARGYLE_SECURITY_SERVER_KEY_STORE_PASSWORD_FILE)));
		return new Socks5ServerUri(host, port).newSocksClient(properties);
	}
	
	private SocksClientHelper() { }
	
}
