package jargyle.net.socks.server;

import jargyle.ResourceHelper;
import jargyle.ResourceNameConstants;
import jargyle.net.socks.server.v5.userpassauth.StringSourceUsernamePasswordAuthenticator;
import jargyle.net.socks.transport.v5.AuthMethod;
import jargyle.net.socks.transport.v5.AuthMethods;

public final class ConfigurationHelper {

	public static Configuration newConfiguration() {
		return ImmutableConfiguration.newInstance((Settings) null);
	}
	
	public static Configuration newConfigurationUsingSocks5GssapiAuth() {
		return ImmutableConfiguration.newInstance(Settings.newInstance(
				SettingSpec.SOCKS5_AUTH_METHODS.newSetting(
						AuthMethods.newInstance(AuthMethod.GSSAPI))));
	}
	
	public static Configuration newConfigurationUsingSocks5GssapiAuthNecReferenceImpl() {
		return ImmutableConfiguration.newInstance(Settings.newInstance(
				SettingSpec.SOCKS5_AUTH_METHODS.newSetting(
						AuthMethods.newInstance(AuthMethod.GSSAPI)),
				SettingSpec.SOCKS5_GSSAPI_NEC_REFERENCE_IMPL.newSetting(
						Boolean.TRUE)));
	}
	
	public static Configuration newConfigurationUsingSocks5UsernamePasswordAuth() {
		StringBuilder sb = new StringBuilder();
		sb.append("Aladdin:opensesame ");
		sb.append("Jasmine:mission%3Aimpossible ");
		sb.append("Abu:safeDriversSave40%25");
		return ImmutableConfiguration.newInstance(Settings.newInstance(
				SettingSpec.SOCKS5_AUTH_METHODS.newSetting(
						AuthMethods.newInstance(AuthMethod.USERNAME_PASSWORD)),
				SettingSpec.SOCKS5_USERNAME_PASSWORD_AUTHENTICATOR.newSetting(
						new StringSourceUsernamePasswordAuthenticator(sb.toString()))));
	}
	
	public static Configuration newConfigurationUsingSsl() {
		return ImmutableConfiguration.newInstance(Settings.newInstance(
				SettingSpec.DTLS_ENABLED.newSetting(Boolean.TRUE),
				SettingSpec.DTLS_KEY_STORE_FILE.newSetting(
						ResourceHelper.getResourceAsFile(
								ResourceNameConstants.JARGYLE_SECURITY_SERVER_KEY_STORE_FILE)),
				SettingSpec.DTLS_KEY_STORE_PASSWORD.newSettingOfParsableValue(
						ResourceHelper.getResourceAsString(
								ResourceNameConstants.JARGYLE_SECURITY_SERVER_KEY_STORE_PASSWORD_FILE)),				
				SettingSpec.SSL_ENABLED.newSetting(Boolean.TRUE),
				SettingSpec.SSL_KEY_STORE_FILE.newSetting(
						ResourceHelper.getResourceAsFile(
								ResourceNameConstants.JARGYLE_SECURITY_SERVER_KEY_STORE_FILE)),
				SettingSpec.SSL_KEY_STORE_PASSWORD.newSettingOfParsableValue(
						ResourceHelper.getResourceAsString(
								ResourceNameConstants.JARGYLE_SECURITY_SERVER_KEY_STORE_PASSWORD_FILE))));
	}
	
	public static Configuration newConfigurationUsingSslAndRequestedClientAuth() {
		return ImmutableConfiguration.newInstance(Settings.newInstance(
				SettingSpec.DTLS_ENABLED.newSetting(Boolean.TRUE),
				SettingSpec.DTLS_KEY_STORE_FILE.newSetting(
						ResourceHelper.getResourceAsFile(
								ResourceNameConstants.JARGYLE_SECURITY_SERVER_KEY_STORE_FILE)),
				SettingSpec.DTLS_KEY_STORE_PASSWORD.newSettingOfParsableValue(
						ResourceHelper.getResourceAsString(
								ResourceNameConstants.JARGYLE_SECURITY_SERVER_KEY_STORE_PASSWORD_FILE)),
				SettingSpec.DTLS_TRUST_STORE_FILE.newSetting(
						ResourceHelper.getResourceAsFile(
								ResourceNameConstants.JARGYLE_SECURITY_CLIENT_KEY_STORE_FILE)),
				SettingSpec.DTLS_TRUST_STORE_PASSWORD.newSettingOfParsableValue(
						ResourceHelper.getResourceAsString(
								ResourceNameConstants.JARGYLE_SECURITY_CLIENT_KEY_STORE_PASSWORD_FILE)),
				SettingSpec.DTLS_WANT_CLIENT_AUTH.newSetting(Boolean.TRUE),				
				SettingSpec.SSL_ENABLED.newSetting(Boolean.TRUE),
				SettingSpec.SSL_KEY_STORE_FILE.newSetting(
						ResourceHelper.getResourceAsFile(
								ResourceNameConstants.JARGYLE_SECURITY_SERVER_KEY_STORE_FILE)),
				SettingSpec.SSL_KEY_STORE_PASSWORD.newSettingOfParsableValue(
						ResourceHelper.getResourceAsString(
								ResourceNameConstants.JARGYLE_SECURITY_SERVER_KEY_STORE_PASSWORD_FILE)),
				SettingSpec.SSL_TRUST_STORE_FILE.newSetting(
						ResourceHelper.getResourceAsFile(
								ResourceNameConstants.JARGYLE_SECURITY_CLIENT_KEY_STORE_FILE)),
				SettingSpec.SSL_TRUST_STORE_PASSWORD.newSettingOfParsableValue(
						ResourceHelper.getResourceAsString(
								ResourceNameConstants.JARGYLE_SECURITY_CLIENT_KEY_STORE_PASSWORD_FILE)),
				SettingSpec.SSL_WANT_CLIENT_AUTH.newSetting(Boolean.TRUE)));
	}
	
	public static Configuration newConfigurationUsingSslAndRequiredClientAuth() {
		return ImmutableConfiguration.newInstance(Settings.newInstance(
				SettingSpec.DTLS_ENABLED.newSetting(Boolean.TRUE),
				SettingSpec.DTLS_KEY_STORE_FILE.newSetting(
						ResourceHelper.getResourceAsFile(
								ResourceNameConstants.JARGYLE_SECURITY_SERVER_KEY_STORE_FILE)),
				SettingSpec.DTLS_KEY_STORE_PASSWORD.newSettingOfParsableValue(
						ResourceHelper.getResourceAsString(
								ResourceNameConstants.JARGYLE_SECURITY_SERVER_KEY_STORE_PASSWORD_FILE)),
				SettingSpec.DTLS_NEED_CLIENT_AUTH.newSetting(Boolean.TRUE),
				SettingSpec.DTLS_TRUST_STORE_FILE.newSetting(
						ResourceHelper.getResourceAsFile(
								ResourceNameConstants.JARGYLE_SECURITY_CLIENT_KEY_STORE_FILE)),
				SettingSpec.DTLS_TRUST_STORE_PASSWORD.newSettingOfParsableValue(
						ResourceHelper.getResourceAsString(
								ResourceNameConstants.JARGYLE_SECURITY_CLIENT_KEY_STORE_PASSWORD_FILE)),				
				SettingSpec.SSL_ENABLED.newSetting(Boolean.TRUE),
				SettingSpec.SSL_KEY_STORE_FILE.newSetting(
						ResourceHelper.getResourceAsFile(
								ResourceNameConstants.JARGYLE_SECURITY_SERVER_KEY_STORE_FILE)),
				SettingSpec.SSL_KEY_STORE_PASSWORD.newSettingOfParsableValue(
						ResourceHelper.getResourceAsString(
								ResourceNameConstants.JARGYLE_SECURITY_SERVER_KEY_STORE_PASSWORD_FILE)),
				SettingSpec.SSL_NEED_CLIENT_AUTH.newSetting(Boolean.TRUE),
				SettingSpec.SSL_TRUST_STORE_FILE.newSetting(
						ResourceHelper.getResourceAsFile(
								ResourceNameConstants.JARGYLE_SECURITY_CLIENT_KEY_STORE_FILE)),
				SettingSpec.SSL_TRUST_STORE_PASSWORD.newSettingOfParsableValue(
						ResourceHelper.getResourceAsString(
								ResourceNameConstants.JARGYLE_SECURITY_CLIENT_KEY_STORE_PASSWORD_FILE))));
	}
	
	private ConfigurationHelper() { }
}
